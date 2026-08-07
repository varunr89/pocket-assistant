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
import com.varun.pocketassistant.data.TranscriptStatus

class AsrWorker(
    appContext: Context,
    params: WorkerParameters,
) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        val segmentId = inputData.getString(KEY_SEGMENT_ID) ?: return Result.failure()
        val app = applicationContext as? PocketAssistantApp
            ?: return Result.failure()
        setForeground(createForegroundInfo("Transcribing…"))
        return try {
            app.container.asrStage.process(segmentId)
            Result.success()
        } catch (t: Throwable) {
            Log.w(TAG, "ASR work failed segment=$segmentId attempt=$runAttemptCount: ${t.message}")
            if (runAttemptCount >= MAX_ATTEMPTS - 1) {
                // Terminal failure already persisted by AsrStage; do not retry forever.
                Result.failure()
            } else {
                // Leave status FAILED with error so UI shows something; WorkManager will re-run.
                val seg = app.container.sessionRepository.getSegment(segmentId)
                if (seg != null && seg.transcriptStatus == TranscriptStatus.FAILED.name) {
                    app.container.sessionRepository.updateTranscript(
                        segmentId,
                        TranscriptStatus.PENDING,
                    )
                }
                Result.retry()
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
        const val KEY_SEGMENT_ID = "segmentId"
        private const val TAG = "AsrWorker"
        private const val MAX_ATTEMPTS = 3
        private const val CHANNEL_ID = "pipeline_processing"
        private const val NOTIFICATION_ID = 42_001
    }
}
