package com.varun.pocketassistant.pipeline.work

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.ServiceInfo
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import com.varun.pocketassistant.PocketAssistantApp
import com.varun.pocketassistant.R
import com.varun.pocketassistant.speech.MeetingStageResult

class MeetingStageWorker(
    appContext: Context,
    params: WorkerParameters,
) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        val meetingId = inputData.getString(KEY_MEETING_ID) ?: return Result.failure()
        val stage = inputData.getString(KEY_STAGE) ?: STAGE_CLEANUP
        val app = applicationContext as? PocketAssistantApp
            ?: return Result.failure()
        val label = if (stage == STAGE_SUMMARY) "Summarizing meeting…" else "Cleaning meeting…"
        setForeground(createForegroundInfo(label))

        val outcome = when (stage) {
            STAGE_SUMMARY -> app.container.meetingStage.runSummary(meetingId)
            else -> app.container.meetingStage.runCleanup(meetingId)
        }

        return when (outcome) {
            is MeetingStageResult.Success -> Result.success()
            is MeetingStageResult.WaitingAsr -> {
                Log.i(TAG, "Meeting $meetingId waiting on ASR — Result.retry()")
                Result.retry()
            }
            is MeetingStageResult.Failed -> {
                Log.w(TAG, "Meeting $meetingId $stage failed: ${outcome.message}")
                if (!outcome.retryable || runAttemptCount >= MAX_ATTEMPTS - 1) {
                    Result.failure()
                } else {
                    Result.retry()
                }
            }
        }
    }

    private fun createForegroundInfo(text: String): ForegroundInfo {
        ensureChannel()
        val notification = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setContentTitle("Pocket Assistant")
            .setContentText(text)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setOngoing(true)
            .setSilent(true)
            .build()
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ForegroundInfo(
                NOTIFICATION_ID,
                notification,
                ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC,
            )
        } else {
            ForegroundInfo(NOTIFICATION_ID, notification)
        }
    }

    private fun ensureChannel() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return
        val nm = applicationContext.getSystemService(NotificationManager::class.java) ?: return
        nm.createNotificationChannel(
            NotificationChannel(
                CHANNEL_ID,
                "Pipeline processing",
                NotificationManager.IMPORTANCE_LOW,
            ),
        )
    }

    companion object {
        const val KEY_MEETING_ID = "meetingId"
        const val KEY_STAGE = "stage"
        const val STAGE_CLEANUP = "cleanup"
        const val STAGE_SUMMARY = "summary"
        private const val TAG = "MeetingStageWorker"
        private const val MAX_ATTEMPTS = 3
        private const val CHANNEL_ID = "pipeline_processing"
        private const val NOTIFICATION_ID = 42_002
    }
}
