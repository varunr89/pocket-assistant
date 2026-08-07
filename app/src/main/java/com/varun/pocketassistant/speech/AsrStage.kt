package com.varun.pocketassistant.speech

import android.util.Log
import com.varun.pocketassistant.data.SessionRepository
import com.varun.pocketassistant.data.SkipReason
import com.varun.pocketassistant.data.TranscriptStatus
import com.varun.pocketassistant.pipeline.ProviderRouter
import java.io.File

/**
 * ASR stage logic (no background queue). Invoked by [com.varun.pocketassistant.pipeline.work.AsrWorker].
 */
class AsrStage(
    private val repository: SessionRepository,
    private val router: ProviderRouter,
) {
    /**
     * @return true if the segment is settled (ready / skipped); false if already done / skipped.
     * @throws Throwable on ASR failure (caller decides retry vs permanent fail).
     */
    suspend fun process(segmentId: String) {
        val segment = repository.getSegment(segmentId) ?: return
        val status = segment.transcriptStatus

        if (status == TranscriptStatus.SKIPPED_SILENCE.name) return
        if (status == TranscriptStatus.READY.name && !segment.transcript.isNullOrBlank()) return
        if (status == TranscriptStatus.CLEANED.name && !segment.transcript.isNullOrBlank()) return
        if (segment.skipReason != null) return

        val needsAsr = status == TranscriptStatus.PENDING.name ||
            status == TranscriptStatus.FAILED.name ||
            status == TranscriptStatus.PROCESSING.name ||
            segment.transcript.isNullOrBlank()

        // Legacy mega WAV: mark with structured skipReason, do not burn ASR attempts.
        if (segment.durationMs > 1_200_000L &&
            segment.filePath.substringAfterLast('/').matches(Regex("""speech_\d+\.wav"""))
        ) {
            repository.updateTranscript(
                segmentId,
                TranscriptStatus.SKIPPED_SILENCE,
                skipReason = SkipReason.LEGACY_TOO_LONG,
                clearAsrError = true,
            )
            return
        }

        if (!needsAsr) return

        val started = System.currentTimeMillis()
        repository.updateTranscript(segmentId, TranscriptStatus.PROCESSING, clearAsrError = true)
        val wav = File(segment.filePath)
        if (!wav.exists() || wav.length() < 44 + 16_000) {
            repository.updateTranscript(segmentId, TranscriptStatus.SKIPPED_SILENCE)
            return
        }
        try {
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
                clearAsrError = true,
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
                asrLastError = t.message ?: t.javaClass.simpleName,
            )
            throw t
        }
    }

    companion object {
        private const val TAG = "AsrStage"
    }
}
