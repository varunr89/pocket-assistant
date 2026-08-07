package com.varun.pocketassistant.speech

import android.util.Log
import com.varun.pocketassistant.data.MeetingRepository
import com.varun.pocketassistant.data.MeetingStatus
import com.varun.pocketassistant.data.TranscriptStatus
import com.varun.pocketassistant.pipeline.CleanupPrompts
import com.varun.pocketassistant.pipeline.MeetingCleanupParser
import com.varun.pocketassistant.pipeline.PipelineConfig
import com.varun.pocketassistant.pipeline.ProviderRouter
import java.text.DateFormat
import java.util.Date

sealed class MeetingStageResult {
    data object Success : MeetingStageResult()
    /** ASR still in progress — WorkManager should Result.retry(). */
    data object WaitingAsr : MeetingStageResult()
    data class Failed(val message: String, val retryable: Boolean = true) : MeetingStageResult()
}

/**
 * Meeting cleanup / summary stage logic (no Channel / spin loop).
 * Invoked by [com.varun.pocketassistant.pipeline.work.MeetingStageWorker].
 */
class MeetingStage(
    private val repository: MeetingRepository,
    private val pipelineConfig: PipelineConfig,
    private val router: ProviderRouter,
) {
    private val timeFmt = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM)

    suspend fun runCleanup(meetingId: String): MeetingStageResult {
        val meeting = repository.getMeeting(meetingId) ?: return MeetingStageResult.Success
        if (meeting.status == MeetingStatus.READY.name) return MeetingStageResult.Success
        // Already cleaned — summary worker can proceed.
        if (!meeting.cleanTextOnly.isNullOrBlank()) return MeetingStageResult.Success

        val settings = pipelineConfig.load()
        Log.i(
            TAG,
            "Meeting $meetingId cleanupMode=${settings.cleanupMode} " +
                "cloudConfigured=${pipelineConfig.cloudConfigured()} " +
                "cleanupModel=${settings.cloudCleanupModel}",
        )

        val linked = repository.getRecordings(meetingId)
        if (linked.isEmpty()) {
            repository.updateMeeting(
                meeting.copy(
                    status = MeetingStatus.FAILED.name,
                    lastError = "No recordings in this time range.",
                    cleanedTranscript = "No recordings in this time range.",
                ),
            )
            return MeetingStageResult.Failed("No recordings", retryable = false)
        }

        if (!repository.allRecordingsAsrSettled(meetingId)) {
            val pendingAsr = linked.count {
                it.transcriptStatus == TranscriptStatus.PENDING.name ||
                    it.transcriptStatus == TranscriptStatus.PROCESSING.name
            }
            Log.i(TAG, "Meeting $meetingId waiting on ASR ($pendingAsr still in progress)")
            if (meeting.status != MeetingStatus.PENDING_CLEANUP.name) {
                repository.updateMeeting(meeting.copy(status = MeetingStatus.PENDING_CLEANUP.name))
            }
            return MeetingStageResult.WaitingAsr
        }

        val recordings = linked.filter { it.transcriptStatus == TranscriptStatus.READY.name }
        if (recordings.isEmpty()) {
            val msg = "No usable transcripts in this time range (ASR skipped or failed)."
            repository.updateMeeting(
                meeting.copy(
                    status = MeetingStatus.FAILED.name,
                    lastError = msg,
                    cleanedTranscript = msg,
                ),
            )
            return MeetingStageResult.Failed(msg, retryable = false)
        }

        val combined = buildString {
            for (seg in recordings) {
                val raw = seg.diarizedTranscript?.takeIf { it.isNotBlank() }
                    ?: seg.transcript?.takeIf { it.isNotBlank() }
                    ?: continue
                append("--- ")
                append(timeFmt.format(Date(seg.startedAtMs)))
                append(" ---\n")
                append(raw.trim())
                append("\n\n")
            }
        }.trim()

        if (combined.isBlank()) {
            val msg = "Recordings had empty transcripts."
            repository.updateMeeting(
                meeting.copy(
                    status = MeetingStatus.FAILED.name,
                    lastError = msg,
                    cleanedTranscript = msg,
                ),
            )
            return MeetingStageResult.Failed(msg, retryable = false)
        }

        val diarized = recordings.any { !it.diarizedTranscript.isNullOrBlank() }
        val cleanStarted = System.currentTimeMillis()
        repository.updateMeeting(meeting.copy(status = MeetingStatus.CLEANING.name, lastError = null))

        return try {
            val cleaned = router.clean(combined, diarized)
            if (cleaned.text.isBlank()) error("cleanup returned empty text")
            val providerLabel = buildString {
                append("clean=${cleaned.providerId}")
                if (cleaned.providerId.startsWith("cloud")) {
                    append("(${settings.cloudCleanupModel})")
                }
            }
            repository.updateMeeting(
                meeting.copy(
                    status = MeetingStatus.PENDING_CLEANUP.name,
                    cleanTextOnly = cleaned.text,
                    cleanupProvider = providerLabel,
                    lastError = null,
                ),
            )
            Log.i(
                TAG,
                "Meeting $meetingId cleanup ok chars=${cleaned.text.length} " +
                    "ms=${System.currentTimeMillis() - cleanStarted}",
            )
            MeetingStageResult.Success
        } catch (t: Throwable) {
            Log.e(TAG, "Meeting $meetingId cleanup failed", t)
            repository.updateMeeting(
                meeting.copy(
                    status = MeetingStatus.FAILED.name,
                    lastError = t.message ?: "Cleanup failed",
                ),
            )
            MeetingStageResult.Failed(t.message ?: "Cleanup failed", retryable = true)
        }
    }

    suspend fun runSummary(meetingId: String): MeetingStageResult {
        val meeting = repository.getMeeting(meetingId) ?: return MeetingStageResult.Success
        if (meeting.status == MeetingStatus.READY.name) return MeetingStageResult.Success

        val cleanedText = meeting.cleanTextOnly?.takeIf { it.isNotBlank() }
        if (cleanedText == null) {
            // Cleanup never completed — run it first (or fail).
            val cleanResult = runCleanup(meetingId)
            if (cleanResult !is MeetingStageResult.Success) return cleanResult
            val refreshed = repository.getMeeting(meetingId)
            val text = refreshed?.cleanTextOnly?.takeIf { it.isNotBlank() }
                ?: return MeetingStageResult.Failed("No cleaned text after cleanup", retryable = true)
            return summarizeWithText(meetingId, text, refreshed.cleanupProvider)
        }
        return summarizeWithText(meetingId, cleanedText, meeting.cleanupProvider)
    }

    private suspend fun summarizeWithText(
        meetingId: String,
        cleanedText: String,
        existingProvider: String?,
    ): MeetingStageResult {
        val meeting = repository.getMeeting(meetingId)
            ?: return MeetingStageResult.Failed("Meeting gone", retryable = false)
        val settings = pipelineConfig.load()
        val summaryStarted = System.currentTimeMillis()
        repository.updateMeeting(meeting.copy(status = MeetingStatus.CLEANING.name, lastError = null))

        return try {
            val summary = router.summarize(cleanedText)
            if (summary.text.isBlank()) error("summary returned empty text")
            val assembled = CleanupPrompts.assembleMeetingMarkdown(summary.text, cleanedText)
            val parsed = MeetingCleanupParser.parse(assembled)
            val providerLabel = buildString {
                append(existingProvider ?: "clean=?")
                append(";summary=${summary.providerId}")
                if (summary.providerId.startsWith("cloud")) {
                    append("(${settings.cloudSummaryModel})")
                }
            }
            repository.updateMeeting(
                meeting.copy(
                    status = MeetingStatus.READY.name,
                    title = parsed.title,
                    cleanedTranscript = parsed.cleanedMarkdown,
                    cleanTextOnly = cleanedText,
                    metadataJson = parsed.toMetadataJson(),
                    cleanupProvider = providerLabel,
                    lastError = null,
                ),
            )
            Log.i(
                TAG,
                "Meeting $meetingId summary ok title=${parsed.title} " +
                    "ms=${System.currentTimeMillis() - summaryStarted}",
            )
            MeetingStageResult.Success
        } catch (t: Throwable) {
            Log.e(TAG, "Meeting $meetingId summary failed — keeping cleanTextOnly", t)
            repository.updateMeeting(
                meeting.copy(
                    status = MeetingStatus.FAILED.name,
                    cleanTextOnly = cleanedText,
                    cleanedTranscript = meeting.cleanedTranscript ?: CleanupPrompts.assembleMeetingMarkdown(
                        summaryMarkdown = "Title: (summary failed)\n\nOverview:\n${t.message}",
                        cleanedTranscript = cleanedText,
                    ),
                    lastError = t.message ?: "Summary failed",
                    cleanupProvider = (existingProvider ?: "") + ";summary=FAILED",
                ),
            )
            MeetingStageResult.Failed(t.message ?: "Summary failed", retryable = true)
        }
    }

    companion object {
        private const val TAG = "MeetingStage"
    }
}
