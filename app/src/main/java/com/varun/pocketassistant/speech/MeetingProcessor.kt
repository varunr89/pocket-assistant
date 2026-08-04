package com.varun.pocketassistant.speech

import android.content.Context
import android.util.Log
import com.varun.pocketassistant.data.MeetingRepository
import com.varun.pocketassistant.data.MeetingStatus
import com.varun.pocketassistant.data.TranscriptStatus
import com.varun.pocketassistant.pipeline.CleanupPrompts
import com.varun.pocketassistant.pipeline.CloudAsrProvider
import com.varun.pocketassistant.pipeline.CloudCleanupProvider
import com.varun.pocketassistant.pipeline.GemmaCleanupProvider
import com.varun.pocketassistant.pipeline.MeetingCleanupParser
import com.varun.pocketassistant.pipeline.OpenAiCompatibleClient
import com.varun.pocketassistant.pipeline.ParakeetAsrProvider
import com.varun.pocketassistant.pipeline.PipelineConfig
import com.varun.pocketassistant.pipeline.ProviderRouter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.DateFormat
import java.util.Date

/**
 * Runs Gemma (or cloud) cleanup once per meeting on concatenated raw transcripts.
 * Waits until linked recordings finish ASR when needed.
 */
class MeetingProcessor(
    context: Context,
    private val repository: MeetingRepository,
    private val scope: CoroutineScope,
    private val pipelineConfig: PipelineConfig,
) {
    private val appContext = context.applicationContext
    private val client = OpenAiCompatibleClient(pipelineConfig)
    private val router = ProviderRouter(
        config = pipelineConfig,
        localAsr = ParakeetAsrProvider(appContext),
        cloudAsr = CloudAsrProvider(appContext, pipelineConfig, client),
        localText = GemmaCleanupProvider(appContext, pipelineConfig),
        cloudText = CloudCleanupProvider(pipelineConfig, client),
    )
    private val channel = Channel<String>(Channel.UNLIMITED)
    private val timeFmt = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM)

    init {
        scope.launch(Dispatchers.IO) {
            repository.recoverStuckCleaning().forEach { channel.trySend(it) }
            repository.getPendingCleanup(20).forEach { channel.trySend(it.id) }
            for (meetingId in channel) {
                processOne(meetingId)
            }
        }
    }

    fun enqueue(meetingId: String) {
        channel.trySend(meetingId)
    }

    fun requeuePending() {
        scope.launch(Dispatchers.IO) {
            repository.getPendingCleanup(50).forEach { channel.trySend(it.id) }
        }
    }

    /** Reset READY/FAILED meetings and run cleanup+summary again with current settings. */
    fun requeueAll() {
        scope.launch(Dispatchers.IO) {
            val ids = repository.requeueAllForCleanup(100)
            Log.i(TAG, "Requeued ${ids.size} meetings for cleanup/summary")
            ids.forEach { channel.trySend(it) }
        }
    }

    private suspend fun processOne(meetingId: String) {
        val meeting = repository.getMeeting(meetingId) ?: return
        // READY = done. CLEANING is only skipped while this process owns it;
        // recoverStuckCleaning / retryCleanup reset abandoned CLEANING rows.
        if (meeting.status == MeetingStatus.READY.name) return
        if (meeting.status == MeetingStatus.CLEANING.name) return

        val settings = pipelineConfig.load()
        Log.i(
            TAG,
            "Meeting $meetingId cleanupMode=${settings.cleanupMode} summaryMode=${settings.summaryMode} " +
                "cloudConfigured=${pipelineConfig.cloudConfigured()} " +
                "cleanupModel=${settings.cloudCleanupModel} summaryModel=${settings.cloudSummaryModel}",
        )

        val linked = repository.getRecordings(meetingId)
        if (linked.isEmpty()) {
            repository.updateMeeting(
                meeting.copy(
                    status = MeetingStatus.FAILED.name,
                    cleanedTranscript = "No recordings in this time range.",
                ),
            )
            return
        }

        if (!repository.allRecordingsAsrSettled(meetingId)) {
            val pending = linked.count {
                it.transcriptStatus == TranscriptStatus.PENDING.name ||
                    it.transcriptStatus == TranscriptStatus.PROCESSING.name
            }
            Log.i(TAG, "Meeting $meetingId waiting on ASR ($pending still in progress)")
            if (meeting.status != MeetingStatus.PENDING_CLEANUP.name) {
                repository.updateMeeting(meeting.copy(status = MeetingStatus.PENDING_CLEANUP.name))
            }
            delay(15_000)
            channel.trySend(meetingId)
            return
        }

        val recordings = linked.filter { it.transcriptStatus == TranscriptStatus.READY.name }
        if (recordings.isEmpty()) {
            repository.updateMeeting(
                meeting.copy(
                    status = MeetingStatus.FAILED.name,
                    cleanedTranscript = "No usable transcripts in this time range (ASR skipped or failed).",
                ),
            )
            Log.w(TAG, "Meeting $meetingId has no READY recordings after ASR settled")
            return
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
            repository.updateMeeting(
                meeting.copy(
                    status = MeetingStatus.FAILED.name,
                    cleanedTranscript = "Recordings had empty transcripts.",
                ),
            )
            return
        }

        val diarized = recordings.any { !it.diarizedTranscript.isNullOrBlank() }

        val meetingStarted = System.currentTimeMillis()
        try {
            repository.updateMeeting(meeting.copy(status = MeetingStatus.CLEANING.name))
            val cleanStarted = System.currentTimeMillis()
            val cleaned = router.clean(combined, diarized)
            val cleanMs = System.currentTimeMillis() - cleanStarted

            val summaryStarted = System.currentTimeMillis()
            val summary = router.summarize(cleaned.text)
            val summaryMs = System.currentTimeMillis() - summaryStarted

            val assembled = CleanupPrompts.assembleMeetingMarkdown(summary.text, cleaned.text)
            val parsed = MeetingCleanupParser.parse(assembled)
            val providerLabel = buildString {
                append("clean=${cleaned.providerId}")
                if (cleaned.providerId.startsWith("cloud")) {
                    append("(${settings.cloudCleanupModel})")
                }
                append(";summary=${summary.providerId}")
                if (summary.providerId.startsWith("cloud")) {
                    append("(${settings.cloudSummaryModel})")
                }
            }
            val wallMs = System.currentTimeMillis() - meetingStarted
            repository.updateMeeting(
                meeting.copy(
                    status = MeetingStatus.READY.name,
                    title = parsed.title,
                    cleanedTranscript = parsed.cleanedMarkdown,
                    metadataJson = parsed.toMetadataJson(),
                    cleanupProvider = providerLabel,
                ),
            )
            Log.i(
                TAG,
                "Meeting $meetingId via $providerLabel " +
                    "rawChars=${combined.length} cleanChars=${cleaned.text.length} " +
                    "cleanMs=$cleanMs summaryMs=$summaryMs wallMs=$wallMs " +
                    "title=${parsed.title}",
            )
        } catch (t: Throwable) {
            val wallMs = System.currentTimeMillis() - meetingStarted
            Log.e(TAG, "Meeting cleanup failed for $meetingId after ${wallMs}ms", t)
            repository.updateMeeting(
                meeting.copy(
                    status = MeetingStatus.FAILED.name,
                    cleanedTranscript = t.message,
                ),
            )
        }
    }

    companion object {
        private const val TAG = "MeetingProcessor"
    }
}
