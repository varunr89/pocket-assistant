package com.varun.pocketassistant

import android.app.Application
import android.util.Log
import androidx.work.Configuration
import androidx.work.WorkManager
import com.varun.pocketassistant.data.AppContainer
import com.varun.pocketassistant.data.OrphanSessionImporter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.util.concurrent.Executors

class PocketAssistantApp : Application(), Configuration.Provider {
    lateinit var container: AppContainer
        private set

    private val appScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    /**
     * Single-threaded pipeline executor. Parallel AsrWorkers each materialise PCM for
     * prep/split and previously OOMed the 512 MB largeHeap under requeue-all.
     */
    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setMinimumLoggingLevel(Log.INFO)
            .setExecutor(Executors.newSingleThreadExecutor())
            .setTaskExecutor(Executors.newSingleThreadExecutor())
            .setMaxSchedulerLimit(20)
            .build()

    override fun onCreate() {
        super.onCreate()
        // Default WorkManagerInitializer is removed in the manifest; init explicitly.
        WorkManager.initialize(this, workManagerConfiguration)
        container = AppContainer(this)
        appScope.launch {
            try {
                val imported = OrphanSessionImporter(
                    audioStorage = container.audioStorage,
                    sessionDao = container.database.sessionDao(),
                    segmentDao = container.database.segmentDao(),
                    repository = container.sessionRepository,
                ).importMissing()
                if (imported > 0) {
                    Log.i("PocketAssistantApp", "Recovered $imported orphan segment(s) for transcription")
                }
            } catch (t: Throwable) {
                Log.e("PocketAssistantApp", "Orphan import failed", t)
            }
            try {
                val pendingAsr = container.sessionRepository.getPendingWork(50).map { it.id }
                container.pipelineScheduler.requeuePendingAsr(pendingAsr)
                val pendingMeetings = container.meetingRepository.recoverStuckCleaning() +
                    container.meetingRepository.getPendingCleanup(50).map { it.id }
                container.pipelineScheduler.requeuePendingMeetings(pendingMeetings.distinct())
            } catch (t: Throwable) {
                Log.e("PocketAssistantApp", "Pipeline requeue failed", t)
            }
        }
    }
}
