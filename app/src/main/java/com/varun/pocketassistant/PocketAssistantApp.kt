package com.varun.pocketassistant

import android.app.Application
import android.util.Log
import com.varun.pocketassistant.data.AppContainer
import com.varun.pocketassistant.data.OrphanSessionImporter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class PocketAssistantApp : Application() {
    lateinit var container: AppContainer
        private set

    private val appScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onCreate() {
        super.onCreate()
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
        }
    }
}
