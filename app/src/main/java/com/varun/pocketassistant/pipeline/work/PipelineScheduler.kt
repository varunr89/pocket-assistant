package com.varun.pocketassistant.pipeline.work

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.workDataOf
import java.util.concurrent.TimeUnit

/**
 * Sole enqueue / dedup / WorkInfo API for ASR and meeting stages.
 */
class PipelineScheduler(context: Context) {
    private val appContext = context.applicationContext
    private val wm = WorkManager.getInstance(appContext)

    fun enqueueAsr(segmentId: String, replace: Boolean = false, expedited: Boolean = true) {
        val builder = OneTimeWorkRequestBuilder<AsrWorker>()
            .setInputData(workDataOf(AsrWorker.KEY_SEGMENT_ID to segmentId))
            .setConstraints(connectedConstraint())
            .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, 10, TimeUnit.SECONDS)
            .addTag(TAG_ASR)
            .addTag(asrTag(segmentId))
        if (expedited) {
            builder.setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
        }
        val policy = if (replace) ExistingWorkPolicy.REPLACE else ExistingWorkPolicy.KEEP
        wm.enqueueUniqueWork(asrName(segmentId), policy, builder.build())
    }

    fun enqueueMeeting(meetingId: String, replace: Boolean = false, expedited: Boolean = true) {
        val cleanupBuilder = OneTimeWorkRequestBuilder<MeetingStageWorker>()
            .setInputData(
                workDataOf(
                    MeetingStageWorker.KEY_MEETING_ID to meetingId,
                    MeetingStageWorker.KEY_STAGE to MeetingStageWorker.STAGE_CLEANUP,
                ),
            )
            .setConstraints(connectedConstraint())
            .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, 10, TimeUnit.SECONDS)
            .addTag(TAG_MEETING)
            .addTag(meetingTag(meetingId))
        if (expedited) {
            cleanupBuilder.setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
        }
        val summary = OneTimeWorkRequestBuilder<MeetingStageWorker>()
            .setInputData(
                workDataOf(
                    MeetingStageWorker.KEY_MEETING_ID to meetingId,
                    MeetingStageWorker.KEY_STAGE to MeetingStageWorker.STAGE_SUMMARY,
                ),
            )
            .setConstraints(connectedConstraint())
            .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, 10, TimeUnit.SECONDS)
            .addTag(TAG_MEETING)
            .addTag(meetingTag(meetingId))
            .build()
        val policy = if (replace) ExistingWorkPolicy.REPLACE else ExistingWorkPolicy.KEEP
        wm.beginUniqueWork(meetingCleanupName(meetingId), policy, cleanupBuilder.build())
            .then(summary)
            .enqueue()
    }

    fun enqueueMeetingSummary(meetingId: String, replace: Boolean = false) {
        val summary = OneTimeWorkRequestBuilder<MeetingStageWorker>()
            .setInputData(
                workDataOf(
                    MeetingStageWorker.KEY_MEETING_ID to meetingId,
                    MeetingStageWorker.KEY_STAGE to MeetingStageWorker.STAGE_SUMMARY,
                ),
            )
            .setConstraints(connectedConstraint())
            .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, 10, TimeUnit.SECONDS)
            .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
            .addTag(TAG_MEETING)
            .addTag(meetingTag(meetingId))
            .build()
        val policy = if (replace) ExistingWorkPolicy.REPLACE else ExistingWorkPolicy.KEEP
        wm.enqueueUniqueWork(meetingSummaryName(meetingId), policy, summary)
    }

    fun observeAsr(segmentId: String): LiveData<List<WorkInfo>> =
        wm.getWorkInfosForUniqueWorkLiveData(asrName(segmentId))

    fun observeMeeting(meetingId: String): LiveData<List<WorkInfo>> =
        wm.getWorkInfosByTagLiveData(meetingTag(meetingId))

    fun requeuePendingAsr(ids: List<String>) {
        ids.forEach { enqueueAsr(it, expedited = false) }
    }

    fun requeuePendingMeetings(ids: List<String>) {
        ids.forEach { enqueueMeeting(it, expedited = false) }
    }

    private fun connectedConstraint(): Constraints =
        Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

    companion object {
        const val TAG_ASR = "pipeline_asr"
        const val TAG_MEETING = "pipeline_meeting"

        fun asrName(segmentId: String) = "asr-$segmentId"
        fun asrTag(segmentId: String) = "asr-tag-$segmentId"
        fun meetingCleanupName(meetingId: String) = "meeting-cleanup-$meetingId"
        fun meetingSummaryName(meetingId: String) = "meeting-summary-$meetingId"
        fun meetingTag(meetingId: String) = "meeting-tag-$meetingId"
    }
}
