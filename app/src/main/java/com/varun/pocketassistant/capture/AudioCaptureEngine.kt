package com.varun.pocketassistant.capture

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.util.Log
import androidx.core.content.ContextCompat
import com.varun.pocketassistant.data.SessionRepository
import com.varun.pocketassistant.pipeline.AsrAudioPreprocessor
import com.varun.pocketassistant.pipeline.PipelineConfig
import com.varun.pocketassistant.pipeline.ProviderMode
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.io.File
import kotlin.math.abs
import kotlin.math.sqrt

enum class CaptureState {
    IDLE,
    RECORDING,
    PAUSED,
    /** Service is alive but the capture schedule gate holds the mic OFF. */
    SCHEDULED_OFF,
}

/**
 * Continuously reads the mic, runs VAD, and only persists speech segments as WAV files.
 */
class AudioCaptureEngine(
    private val context: Context,
    private val sessionRepository: SessionRepository,
    private val audioStorage: AudioStorage,
    private val scope: CoroutineScope,
    private val pipelineConfig: PipelineConfig? = null,
    private val vad: VoiceActivityDetector = TenVoiceActivityDetector.createOrFallback(),
    private val gate: CaptureScheduleGate,
) {
    private val mutex = Mutex()
    private var captureJob: Job? = null
    private var audioRecord: AudioRecord? = null
    private var writer: WavWriter? = null
    private var activeSegmentStartMs: Long = 0L
    private var activeSegmentFile: File? = null
    private var activeSegmentBytes: Long = 0L
    private var wasSpeech = false
    private var paused = false
    private var lastFlushAtMs = 0L
    private var sessionStartedAtMs = 0L
    private var totalBytesWritten = 0L
    private var framesRead = 0L
    private var lastSpeechAtMs: Long? = null
    private var lastRawSpeech = false
    private var phase = CapturePhase.IDLE
    private var lastGraphPublishMs = 0L

    private val preRoll = RingPcmBuffer(SAMPLE_RATE * PRE_ROLL_MS / 1000)
    private val events = ArrayDeque<CaptureEvent>()
    private val rmsHistory = ArrayDeque<RmsSample>()

    private val _stats = MutableStateFlow(CaptureStats(speechThreshold = vad.speechThreshold))
    val stats: StateFlow<CaptureStats> = _stats.asStateFlow()

    fun start() {
        if (captureJob?.isActive == true) return
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO)
            != PackageManager.PERMISSION_GRANTED
        ) {
            _stats.value = _stats.value.copy(lastError = "Microphone permission missing")
            return
        }

        captureJob = scope.launch(Dispatchers.IO) {
            try {
                val session = sessionRepository.startSession()
                sessionStartedAtMs = System.currentTimeMillis()
                totalBytesWritten = 0
                framesRead = 0
                rmsHistory.clear()
                // Gate-aware initial publish: starting outside the schedule
                // must surface as SCHEDULED_OFF immediately (mic released),
                // never as a transient LISTENING before the loop closes the
                // gate a few ms later.
                val gateOpen = gate.isOpenNow()
                phase = if (gateOpen) CapturePhase.LISTENING else CapturePhase.SCHEDULED_OFF
                pushEvent(if (gateOpen) "Capture started" else "Capture started — outside schedule, mic off")
                publish(
                    CaptureStats(
                        state = if (gateOpen) CaptureState.RECORDING else CaptureState.SCHEDULED_OFF,
                        phase = phase,
                        sessionId = session.id,
                        segmentCount = session.segmentCount,
                        speechThreshold = vad.speechThreshold,
                        events = events.toList(),
                    ),
                )
                runCaptureLoop(session.id)
            } catch (t: Throwable) {
                Log.e(TAG, "Capture failed", t)
                pushEvent("Capture failed: ${t.message}")
                publish(
                    _stats.value.copy(
                        state = CaptureState.IDLE,
                        phase = CapturePhase.IDLE,
                        lastError = t.message ?: "Capture failed",
                        events = events.toList(),
                    ),
                )
            } finally {
                closeWriterIfNeeded(finalize = true)
                releaseRecorder()
                vad.reset()
                preRoll.clear()
                wasSpeech = false
                phase = CapturePhase.IDLE
                if (_stats.value.state != CaptureState.IDLE) {
                    publish(
                        _stats.value.copy(
                            state = CaptureState.IDLE,
                            phase = CapturePhase.IDLE,
                            speechActive = false,
                            events = events.toList(),
                        ),
                    )
                }
            }
        }
    }

    fun pause() {
        paused = true
        scope.launch {
            mutex.withLock {
                closeWriterIfNeeded(finalize = true)
                wasSpeech = false
                vad.reset()
                preRoll.clear()
            }
            val sessionId = _stats.value.sessionId
            if (sessionId != null) {
                sessionRepository.setStatus(sessionId, com.varun.pocketassistant.data.SessionStatus.PAUSED)
            }
            phase = CapturePhase.PAUSED
            pushEvent("Paused")
            publish(
                _stats.value.copy(
                    state = CaptureState.PAUSED,
                    phase = CapturePhase.PAUSED,
                    speechActive = false,
                    events = events.toList(),
                ),
            )
        }
    }

    fun resume() {
        paused = false
        scope.launch {
            val sessionId = _stats.value.sessionId
            if (sessionId != null) {
                sessionRepository.setStatus(
                    sessionId,
                    com.varun.pocketassistant.data.SessionStatus.RECORDING,
                )
            }
            // Gate-aware (sibling of start()): never flash LISTENING while the
            // schedule holds the mic off; the loop re-authorizes per frame.
            val gateOpen = gate.isOpenNow()
            phase = if (gateOpen) CapturePhase.LISTENING else CapturePhase.SCHEDULED_OFF
            pushEvent(if (gateOpen) "Resumed" else "Resumed — outside schedule, mic off")
            publish(
                _stats.value.copy(
                    state = if (gateOpen) CaptureState.RECORDING else CaptureState.SCHEDULED_OFF,
                    phase = phase,
                    events = events.toList(),
                ),
            )
        }
    }

    private suspend fun runCaptureLoop(sessionId: String) {
        // Defensive re-check: start() verifies the permission, but it can be
        // revoked between start() and this loop. Also satisfies lint
        // MissingPermission next to the AudioRecord constructor.
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO)
            != PackageManager.PERMISSION_GRANTED
        ) {
            error("Microphone permission missing")
        }
        val minBuf = AudioRecord.getMinBufferSize(
            SAMPLE_RATE,
            AudioFormat.CHANNEL_IN_MONO,
            AudioFormat.ENCODING_PCM_16BIT,
        )
        require(minBuf > 0) { "AudioRecord buffer unavailable ($minBuf)" }

        val bufferSize = maxOf(minBuf, SAMPLE_RATE / 5) // ~200ms
        while (scope.isActive && captureJob?.isActive == true) {
            if (!gate.isOpenNow()) {
                // Schedule gate closed: the mic is OFF and nothing is written.
                // Release any open segment/writer from the previous window,
                // then wait — without touching the mic — until the schedule (or
                // the override toggle) opens the gate again.
                releaseRecorder()
                mutex.withLock { closeWriterIfNeeded(finalize = true) }
                vad.reset()
                preRoll.clear()
                wasSpeech = false
                phase = CapturePhase.SCHEDULED_OFF
                pushEvent("Capture schedule closed — mic off")
                publish(
                    _stats.value.copy(
                        state = CaptureState.SCHEDULED_OFF,
                        phase = CapturePhase.SCHEDULED_OFF,
                        speechActive = false,
                        events = events.toList(),
                    ),
                )
                if (!gate.awaitOpen()) return // service stopping — leave cleanly
                pushEvent("Capture schedule open — mic on")
                continue
            }

            // Gate open: (re)open the mic for this window. Reconstructed on
            // every window entry; releaseRecorder() above guarantees only one
            // recorder exists at a time.
            val recorder = AudioRecord(
                MediaRecorder.AudioSource.MIC,
                SAMPLE_RATE,
                AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT,
                bufferSize * 2,
            )
            if (recorder.state != AudioRecord.STATE_INITIALIZED) {
                recorder.release()
                error("AudioRecord failed to initialize")
            }

            audioRecord = recorder
            recorder.startRecording()
            pushEvent("Mic opened @ ${SAMPLE_RATE}Hz")
            phase = CapturePhase.LISTENING
            publish(
                _stats.value.copy(
                    state = if (paused) CaptureState.PAUSED else CaptureState.RECORDING,
                    phase = CapturePhase.LISTENING,
                    speechActive = false,
                    events = events.toList(),
                ),
            )

            val readBuffer = ShortArray(bufferSize)
            while (scope.isActive && captureJob?.isActive == true && gate.isOpenNow()) {
                val read = recorder.read(readBuffer, 0, readBuffer.size)
                if (read <= 0) continue
                if (paused) continue

                mutex.withLock {
                    processFrame(sessionId, readBuffer, read)
                }
            }
            releaseRecorder()
        }
    }

    private suspend fun processFrame(sessionId: String, frame: ShortArray, length: Int) {
        framesRead++
        val (rms, peak) = rmsAndPeak(frame, length)
        val decision = vad.accept(frame, length)

        if (decision.rawSpeech) {
            lastSpeechAtMs = System.currentTimeMillis()
            if (!lastRawSpeech) {
                val p = decision.speechProbability
                pushEvent(
                    if (p >= 0f) {
                        "TEN VAD speech (p=%.2f)".format(p)
                    } else {
                        "Speech energy above threshold (RMS ${rms.toInt()})"
                    },
                )
            }
        }
        lastRawSpeech = decision.rawSpeech

        if (!decision.windowOpen) {
            if (wasSpeech) {
                closeWriterIfNeeded(finalize = true)
                wasSpeech = false
                phase = CapturePhase.LISTENING
                pushEvent("Segment closed after post-roll")
            } else {
                preRoll.push(frame, length)
                phase = CapturePhase.LISTENING
            }
            publishFrame(rms, peak, decision, speechActive = false)
            return
        }

        if (!wasSpeech) {
            openSegment(sessionId, frame, length, decision)
        } else {
            writer?.writePcm(frame, 0, length)
            activeSegmentBytes += length * 2L
            phase = if (decision.rawSpeech) CapturePhase.LIVE_SPEECH else CapturePhase.POST_ROLL
            val now = System.currentTimeMillis()
            if (now - lastFlushAtMs >= 1_000L) {
                writer?.flush()
                lastFlushAtMs = now
            }
            // Hard cap: close and reopen so ASR never sees multi‑tens-of-minutes WAVs.
            val openForMs = now - activeSegmentStartMs
            if (openForMs >= maxSegmentMs()) {
                pushEvent("Capture cap ${maxSegmentMs() / 1000}s — rolling to new segment")
                closeWriterIfNeeded(finalize = true)
                // VAD window is still open; start the next file immediately.
                openSegment(sessionId, frame, length, decision)
            }
        }
        publishFrame(rms, peak, decision, speechActive = true)
    }

    private suspend fun openSegment(
        sessionId: String,
        frame: ShortArray,
        length: Int,
        decision: VadDecision,
    ) {
        phase = CapturePhase.PRE_ROLL_FLUSH
        val startMs = System.currentTimeMillis()
        val file = audioStorage.newSegmentFile(sessionId, startMs)
        val newWriter = WavWriter(file, SAMPLE_RATE)
        val rolled = preRoll.drainSnapshot()
        val preRollMs = rolled.size * 1000L / SAMPLE_RATE
        activeSegmentBytes = 0L
        if (rolled.isNotEmpty()) {
            newWriter.writePcm(rolled, 0, rolled.size)
            activeSegmentBytes = rolled.size * 2L
        }
        newWriter.writePcm(frame, 0, length)
        activeSegmentBytes += length * 2L
        writer = newWriter
        activeSegmentFile = file
        activeSegmentStartMs = startMs - preRollMs
        wasSpeech = true
        preRoll.clear()
        pushEvent("Opened segment (+${preRollMs}ms pre-roll)")
        phase = if (decision.rawSpeech) CapturePhase.LIVE_SPEECH else CapturePhase.POST_ROLL
    }

    private fun publishFrame(
        rms: Float,
        peak: Float,
        decision: VadDecision,
        speechActive: Boolean,
    ) {
        val now = System.currentTimeMillis()
        rmsHistory.addLast(RmsSample(atMs = now, rms = rms, phase = phase))
        val cutoff = now - GRAPH_WINDOW_MS
        while (rmsHistory.isNotEmpty() && rmsHistory.first().atMs < cutoff) {
            rmsHistory.removeFirst()
        }
        // Throttle UI publishes so Compose isn't flooded.
        if (now - lastGraphPublishMs < GRAPH_PUBLISH_MS && speechActive == _stats.value.speechActive) {
            return
        }
        lastGraphPublishMs = now
        val currentDur = if (wasSpeech) now - activeSegmentStartMs else 0L
        publish(
            _stats.value.copy(
                state = if (paused) CaptureState.PAUSED else CaptureState.RECORDING,
                phase = phase,
                speechActive = speechActive,
                rms = rms,
                peak = peak,
                speechThreshold = vad.speechThreshold,
                speechProbability = decision.speechProbability,
                rmsHistory = rmsHistory.toList(),
                preRollBufferedMs = preRoll.bufferedSamples * 1000L / SAMPLE_RATE,
                hangoverRemainingMs = decision.hangoverRemainingMs,
                openGateProgressMs = decision.openGateProgressMs,
                openGateRequiredMs = decision.openGateRequiredMs,
                currentSegmentBytes = activeSegmentBytes,
                currentSegmentDurationMs = currentDur,
                totalBytesWritten = totalBytesWritten + activeSegmentBytes,
                framesRead = framesRead,
                uptimeMs = if (sessionStartedAtMs == 0L) 0L else now - sessionStartedAtMs,
                lastSpeechAtMs = lastSpeechAtMs,
                events = events.toList(),
            ),
        )
    }

    private suspend fun closeWriterIfNeeded(finalize: Boolean) {
        val currentWriter = writer ?: return
        val file = activeSegmentFile
        val startMs = activeSegmentStartMs
        val sessionId = _stats.value.sessionId
        val bytes = activeSegmentBytes
        writer = null
        activeSegmentFile = null
        activeSegmentBytes = 0L

        currentWriter.close()
        if (!finalize || file == null || sessionId == null) return

        val endMs = System.currentTimeMillis()
        val duration = endMs - startMs
        if (duration < MIN_SEGMENT_MS || file.length() < MIN_SEGMENT_BYTES) {
            file.delete()
            pushEvent("Dropped tiny segment (${duration}ms)")
            return
        }

        totalBytesWritten += bytes
        sessionRepository.addSegment(sessionId, file, startMs, endMs)
        pushEvent(
            "Saved segment ${formatBytes(bytes)} / ${duration / 1000}s",
        )
        publish(
            _stats.value.copy(
                segmentCount = _stats.value.segmentCount + 1,
                currentSegmentBytes = 0,
                currentSegmentDurationMs = 0,
                totalBytesWritten = totalBytesWritten,
                events = events.toList(),
            ),
        )
    }

    private fun releaseRecorder() {
        try {
            audioRecord?.stop()
        } catch (_: IllegalStateException) {
        }
        audioRecord?.release()
        audioRecord = null
    }

    private fun publish(stats: CaptureStats) {
        _stats.value = stats
    }

    private fun pushEvent(message: String) {
        events.addLast(CaptureEvent(System.currentTimeMillis(), message))
        while (events.size > 40) events.removeFirst()
        Log.i(TAG, message)
    }

    private fun rmsAndPeak(samples: ShortArray, length: Int): Pair<Float, Float> {
        var sum = 0.0
        var peak = 0
        for (i in 0 until length) {
            val v = samples[i].toInt()
            sum += v.toDouble() * v
            peak = maxOf(peak, abs(v))
        }
        return sqrt(sum / length).toFloat() to peak.toFloat()
    }

    /** Prefer ~10 min rolls for cloud STT; keep 2 min for local Parakeet. */
    private fun maxSegmentMs(): Long {
        val cfg = pipelineConfig ?: return AsrAudioPreprocessor.LOCAL_MAX_SEGMENT_MS
        val settings = runCatching { cfg.load() }.getOrNull()
            ?: return AsrAudioPreprocessor.LOCAL_MAX_SEGMENT_MS
        val cloudPreferred = settings.asrMode == ProviderMode.PREFER_CLOUD
        return if (cloudPreferred && cfg.cloudConfigured()) {
            AsrAudioPreprocessor.CLOUD_MAX_SEGMENT_MS
        } else {
            AsrAudioPreprocessor.LOCAL_MAX_SEGMENT_MS
        }
    }

    companion object {
        const val SAMPLE_RATE = 16_000
        /** Audio kept before VAD opens, so soft lead-ins aren't clipped. */
        private const val PRE_ROLL_MS = 10_000
        private const val GRAPH_WINDOW_MS = 60_000L
        private const val GRAPH_PUBLISH_MS = 50L
        private const val MIN_SEGMENT_MS = 400L
        private const val MIN_SEGMENT_BYTES = 44L + (SAMPLE_RATE * 2 * 0.3).toLong()
        private const val TAG = "AudioCaptureEngine"

        fun formatBytes(bytes: Long): String {
            if (bytes < 1024) return "$bytes B"
            val kb = bytes / 1024.0
            return if (kb < 1024) "%.1f KB".format(kb) else "%.1f MB".format(kb / 1024.0)
        }
    }
}
