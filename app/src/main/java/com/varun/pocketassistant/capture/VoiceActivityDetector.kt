package com.varun.pocketassistant.capture

/**
 * Lightweight energy VAD for Phase 1.
 *
 * Opens a capture window only after [openGateMs] of *continuous* speech-energy
 * above a fixed RMS threshold (default 160). This prevents hours of
 * near-silence from being written when the mic hears room tone.
 *
 * Prefer [TenVoiceActivityDetector] for production; this remains as a fallback
 * if TEN native libs fail to load.
 */
interface VoiceActivityDetector {
    fun accept(frame: ShortArray, length: Int): VadDecision

    fun reset()

    /** Display threshold (RMS for energy VAD, open probability for TEN). */
    val speechThreshold: Float

    /** Latest speech probability in [0, 1], or -1 if not applicable. */
    val lastSpeechProbability: Float
        get() = -1f
}

class EnergyVoiceActivityDetector(
    private val sampleRate: Int = AudioCaptureEngine.SAMPLE_RATE,
    private val frameMs: Int = 30,
    /** Must stay above threshold this long before a segment opens. */
    private val openGateMs: Int = 10_000,
    /** Keep writing this long after the last speech frame (conversation pauses). */
    private val endHangoverMs: Int = 60_000,
    /** Fixed speech energy gate — not adaptive. */
    private val speechRmsThreshold: Double = 160.0,
) : VoiceActivityDetector {
    private val frameSamples = sampleRate * frameMs / 1000
    private val openGateFrames = (openGateMs / frameMs).coerceAtLeast(1)
    private val endHangoverFrames = (endHangoverMs / frameMs).coerceAtLeast(1)
    private val pending = ShortArray(frameSamples * 2)
    private var pendingCount = 0

    private var inSpeech = false
    private var hangover = 0
    private var speechFrames = 0
    private var lastRawSpeech = false

    override val speechThreshold: Float = speechRmsThreshold.toFloat()

    override fun accept(frame: ShortArray, length: Int): VadDecision {
        var offset = 0
        var windowOpen = inSpeech
        var raw = lastRawSpeech
        while (offset < length) {
            val toCopy = minOf(frameSamples - pendingCount, length - offset)
            System.arraycopy(frame, offset, pending, pendingCount, toCopy)
            pendingCount += toCopy
            offset += toCopy

            if (pendingCount < frameSamples) break

            val rms = rms(pending, frameSamples)
            raw = rms >= speechRmsThreshold
            windowOpen = updateState(raw)
            pendingCount = 0
        }
        lastRawSpeech = raw
        val gateProgressMs = if (!inSpeech) {
            speechFrames.toLong() * frameMs
        } else {
            openGateMs.toLong()
        }
        return VadDecision(
            windowOpen = windowOpen,
            rawSpeech = raw,
            hangoverRemainingMs = when {
                windowOpen && !raw -> hangover.toLong() * frameMs
                windowOpen && raw -> endHangoverMs.toLong()
                else -> 0L
            },
            openGateProgressMs = gateProgressMs,
            openGateRequiredMs = openGateMs.toLong(),
            speechProbability = -1f,
        )
    }

    override fun reset() {
        pendingCount = 0
        inSpeech = false
        hangover = 0
        speechFrames = 0
        lastRawSpeech = false
    }

    private fun updateState(isSpeechFrame: Boolean): Boolean {
        if (!inSpeech) {
            if (isSpeechFrame) {
                speechFrames++
                if (speechFrames >= openGateFrames) {
                    inSpeech = true
                    hangover = endHangoverFrames
                }
            } else {
                speechFrames = 0
            }
        } else {
            if (isSpeechFrame) {
                hangover = endHangoverFrames
            } else {
                hangover--
                if (hangover <= 0) {
                    inSpeech = false
                    speechFrames = 0
                }
            }
        }
        return inSpeech
    }

    private fun rms(samples: ShortArray, length: Int): Double {
        var sum = 0.0
        for (i in 0 until length) {
            val v = samples[i].toDouble()
            sum += v * v
        }
        return kotlin.math.sqrt(sum / length)
    }
}

/**
 * TEN VAD–driven open/close with hysteresis + majority open gate.
 *
 * - Open when enough recent frames have p >= [openThreshold] (not unbroken streak).
 * - While open, stay open while p >= [closeThreshold]; otherwise burn [endHangoverMs].
 * - Pre-roll / post-roll remain owned by [AudioCaptureEngine].
 */
class TenVoiceActivityDetector(
    private val hopSize: Int = HOP_SIZE,
    /** Probability to count a frame as speech for opening. */
    private val openThreshold: Float = 0.50f,
    /** Probability below which hangover starts (hysteresis vs open). */
    private val closeThreshold: Float = 0.50f,
    /** Wall-clock window used for the majority open gate. */
    private val openGateMs: Int = 1_500,
    /** Fraction of frames in the open-gate window that must be voiced. */
    private val openGateVoicedRatio: Float = 0.60f,
    /** Keep writing after last speech-like frame (conversation pauses). */
    private val endHangoverMs: Int = 10_000,
) : VoiceActivityDetector {
    private val frameMs = hopSize * 1000 / AudioCaptureEngine.SAMPLE_RATE
    private val openGateFrames = (openGateMs / frameMs).coerceAtLeast(1)
    private val endHangoverFrames = (endHangoverMs / frameMs).coerceAtLeast(1)
    private val minVoicedToOpen =
        (openGateFrames * openGateVoicedRatio).toInt().coerceAtLeast(1)

    private val hop = ShortArray(hopSize)
    private var pendingCount = 0

    private var handle: Long = 0L
    private val outProbability = FloatArray(1)
    private val outFlag = IntArray(1)

    private var inSpeech = false
    private var hangover = 0
    private var lastProbability = 0f
    private var lastRawSpeech = false

    /** Ring of recent open/close decisions for majority open gate. */
    private val recentVoiced = BooleanArray(openGateFrames)
    private var recentIndex = 0
    private var recentFilled = 0
    private var recentVoicedCount = 0

    override val speechThreshold: Float = openThreshold

    override val lastSpeechProbability: Float
        get() = lastProbability

    init {
        // Native create threshold matches open; we apply close hysteresis ourselves.
        handle = TenVadNative.nativeCreate(hopSize, openThreshold)
        require(handle != 0L) {
            "ten_vad_create failed (hop=$hopSize threshold=$openThreshold)"
        }
    }

    override fun accept(frame: ShortArray, length: Int): VadDecision {
        var offset = 0
        var windowOpen = inSpeech
        var raw = lastRawSpeech
        while (offset < length) {
            val toCopy = minOf(hopSize - pendingCount, length - offset)
            System.arraycopy(frame, offset, hop, pendingCount, toCopy)
            pendingCount += toCopy
            offset += toCopy

            if (pendingCount < hopSize) break

            val rc = TenVadNative.nativeProcess(handle, hop, outProbability, outFlag)
            if (rc != 0) {
                throw IllegalStateException("ten_vad_process failed code=$rc")
            }
            lastProbability = outProbability[0]
            raw = lastProbability >= openThreshold
            val keepSpeech = lastProbability >= closeThreshold
            windowOpen = updateState(rawSpeechForOpen = raw, speechForHangover = keepSpeech)
            pendingCount = 0
        }
        lastRawSpeech = raw

        val gateProgressMs = if (!inSpeech) {
            // Progress ≈ voiced fraction of the required majority window.
            val filled = recentFilled.coerceAtLeast(1)
            val ratio = recentVoicedCount.toFloat() / filled
            (ratio * openGateMs).toLong().coerceAtMost(openGateMs.toLong())
        } else {
            openGateMs.toLong()
        }

        return VadDecision(
            windowOpen = windowOpen,
            rawSpeech = raw,
            hangoverRemainingMs = when {
                windowOpen && lastProbability < closeThreshold ->
                    hangover.toLong() * frameMs
                windowOpen -> endHangoverMs.toLong()
                else -> 0L
            },
            openGateProgressMs = gateProgressMs,
            openGateRequiredMs = openGateMs.toLong(),
            speechProbability = lastProbability,
        )
    }

    override fun reset() {
        pendingCount = 0
        inSpeech = false
        hangover = 0
        lastProbability = 0f
        lastRawSpeech = false
        recentIndex = 0
        recentFilled = 0
        recentVoicedCount = 0
        recentVoiced.fill(false)
        // Recreate native instance so internal TEN state is clean.
        if (handle != 0L) {
            TenVadNative.nativeDestroy(handle)
            handle = 0L
        }
        handle = TenVadNative.nativeCreate(hopSize, openThreshold)
        require(handle != 0L) { "ten_vad_create failed on reset" }
    }

    fun close() {
        if (handle != 0L) {
            TenVadNative.nativeDestroy(handle)
            handle = 0L
        }
    }

    private fun updateState(rawSpeechForOpen: Boolean, speechForHangover: Boolean): Boolean {
        pushRecent(rawSpeechForOpen)

        if (!inSpeech) {
            if (recentFilled >= openGateFrames && recentVoicedCount >= minVoicedToOpen) {
                inSpeech = true
                hangover = endHangoverFrames
            }
        } else {
            if (speechForHangover) {
                hangover = endHangoverFrames
            } else {
                hangover--
                if (hangover <= 0) {
                    inSpeech = false
                    clearRecent()
                }
            }
        }
        return inSpeech
    }

    private fun pushRecent(voiced: Boolean) {
        if (recentFilled == openGateFrames) {
            if (recentVoiced[recentIndex]) recentVoicedCount--
        } else {
            recentFilled++
        }
        recentVoiced[recentIndex] = voiced
        if (voiced) recentVoicedCount++
        recentIndex = (recentIndex + 1) % openGateFrames
    }

    private fun clearRecent() {
        recentIndex = 0
        recentFilled = 0
        recentVoicedCount = 0
        recentVoiced.fill(false)
    }

    companion object {
        /** 16 ms at 16 kHz — TEN's recommended hop. */
        const val HOP_SIZE = 256

        fun createOrFallback(): VoiceActivityDetector =
            try {
                val vad = TenVoiceActivityDetector()
                android.util.Log.i(
                    "TenVAD",
                    "TEN VAD ready version=${TenVadNative.nativeVersion()} " +
                        "open=${vad.speechThreshold}",
                )
                vad
            } catch (t: Throwable) {
                android.util.Log.e("TenVAD", "TEN VAD unavailable; using energy fallback", t)
                EnergyVoiceActivityDetector()
            }
    }
}

/** Pre-roll buffer so speech starts aren't clipped when VAD opens late. */
class RingPcmBuffer(
    capacitySamples: Int,
) {
    private val buffer = ShortArray(capacitySamples)
    private var writePos = 0
    private var size = 0

    val bufferedSamples: Int get() = size

    fun push(samples: ShortArray, length: Int) {
        for (i in 0 until length) {
            buffer[writePos] = samples[i]
            writePos = (writePos + 1) % buffer.size
            if (size < buffer.size) size++
        }
    }

    fun drainSnapshot(): ShortArray {
        if (size == 0) return ShortArray(0)
        val out = ShortArray(size)
        val start = if (size == buffer.size) writePos else 0
        for (i in 0 until size) {
            out[i] = buffer[(start + i) % buffer.size]
        }
        return out
    }

    fun clear() {
        writePos = 0
        size = 0
    }
}
