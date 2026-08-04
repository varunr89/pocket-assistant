package com.varun.pocketassistant.speech

import android.content.Context
import android.util.Log
import com.varun.pocketassistant.data.SessionRepository
import com.varun.pocketassistant.data.TranscriptStatus
import com.varun.pocketassistant.pipeline.CloudAsrProvider
import com.varun.pocketassistant.pipeline.CloudCleanupProvider
import com.varun.pocketassistant.pipeline.GemmaCleanupProvider
import com.varun.pocketassistant.pipeline.OpenAiCompatibleClient
import com.varun.pocketassistant.pipeline.ParakeetAsrProvider
import com.varun.pocketassistant.pipeline.PipelineConfig
import com.varun.pocketassistant.pipeline.ProviderRouter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import java.io.File

/**
 * Background queue: closed WAV → Parakeet (or cloud) ASR → READY raw transcript.
 * Gemma cleanup runs on meetings via [MeetingProcessor], not here.
 */
class TranscriptionQueue(
    context: Context,
    private val repository: SessionRepository,
    private val scope: CoroutineScope,
    private val pipelineConfig: PipelineConfig,
) {
    private val appContext = context.applicationContext
    private val speakerStore = SpeakerProfileStore(appContext)
    private val client = OpenAiCompatibleClient(pipelineConfig)
    private val router = ProviderRouter(
        config = pipelineConfig,
        localAsr = ParakeetAsrProvider(appContext),
        cloudAsr = CloudAsrProvider(appContext, pipelineConfig, client),
        localText = GemmaCleanupProvider(appContext, pipelineConfig),
        cloudText = CloudCleanupProvider(pipelineConfig, client),
    )
    private val channel = Channel<String>(Channel.UNLIMITED)

    init {
        scope.launch(Dispatchers.IO) {
            repository.getPendingWork(20).forEach { channel.trySend(it.id) }
            for (segmentId in channel) {
                processOne(segmentId)
            }
        }
    }

    fun enqueue(segmentId: String) {
        channel.trySend(segmentId)
    }

    fun speakerStore(): SpeakerProfileStore = speakerStore

    fun requeuePending() {
        scope.launch(Dispatchers.IO) {
            repository.getPendingWork(50).forEach { channel.trySend(it.id) }
        }
    }

    private suspend fun processOne(segmentId: String) {
        val segment = repository.getSegment(segmentId) ?: return
        val status = segment.transcriptStatus

        if (status == TranscriptStatus.SKIPPED_SILENCE.name) return
        if (status == TranscriptStatus.READY.name && !segment.transcript.isNullOrBlank()) return
        if (status == TranscriptStatus.CLEANED.name && !segment.transcript.isNullOrBlank()) return

        val needsAsr = status == TranscriptStatus.PENDING.name ||
            status == TranscriptStatus.FAILED.name ||
            status == TranscriptStatus.PROCESSING.name ||
            segment.transcript.isNullOrBlank()

        // Do not revive superseded mega WAVs or other intentional failures with a note.
        if (status == TranscriptStatus.FAILED.name &&
            segment.transcript?.contains("superseded", ignoreCase = true) == true
        ) {
            return
        }
        // Skip truly giant unsplit captures — cloud uses ~10 min rolls; local uses 2 min.
        // Anything over 20 min named speech_<epoch>.wav (no _part) is treated as legacy mega.
        if (segment.durationMs > 1_200_000L &&
            segment.filePath.substringAfterLast('/').matches(Regex("""speech_\d+\.wav"""))
        ) {
            repository.updateTranscript(
                segmentId,
                TranscriptStatus.SKIPPED_SILENCE,
                transcript = "skipped unsplit mega wav (>20 min)",
            )
            return
        }

        if (!needsAsr) return

        val started = System.currentTimeMillis()
        try {
            repository.updateTranscript(segmentId, TranscriptStatus.PROCESSING)
            val wav = File(segment.filePath)
            if (!wav.exists() || wav.length() < 44 + 16_000) {
                repository.updateTranscript(segmentId, TranscriptStatus.SKIPPED_SILENCE)
                return
            }
            val result = router.transcribe(wav)
            if (result.plain.isBlank()) {
                repository.updateTranscript(segmentId, TranscriptStatus.SKIPPED_SILENCE)
                return
            }
            repository.updateTranscript(
                segmentId = segmentId,
                status = TranscriptStatus.READY,
                transcript = result.plain,
                diarized = result.diarized,
                asrProvider = result.providerId,
            )
            val elapsed = System.currentTimeMillis() - started
            Log.i(
                TAG,
                "ASR $segmentId via ${result.providerId} " +
                    "audioMs=${segment.durationMs} chars=${result.plain.length} wallMs=$elapsed",
            )
        } catch (t: Throwable) {
            val elapsed = System.currentTimeMillis() - started
            Log.e(TAG, "ASR failed for $segmentId after ${elapsed}ms", t)
            repository.updateTranscript(
                segmentId = segmentId,
                status = TranscriptStatus.FAILED,
                diarized = t.message,
            )
        }
    }

    companion object {
        private const val TAG = "TranscriptionQueue"
    }
}
