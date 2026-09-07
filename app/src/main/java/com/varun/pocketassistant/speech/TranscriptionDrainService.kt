package com.varun.pocketassistant.speech

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.varun.pocketassistant.PocketAssistantApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Foreground-gated catch-up transcription service (product decision
 * 2026-09-06, option A): drains the PENDING segment backlog through the ML
 * Kit GenAI ASR engine ONLY while the app is the top foreground app.
 *
 * - Started by [AppForegroundTracker] when the app comes to the foreground,
 *   stopped when it leaves. A regular (non-foreground-type) service is fine
 *   precisely because it never needs to run in the background.
 * - When not foreground the drain no-ops and waits ([TranscriptionDrain]
 *   re-checks the gate before every segment); the AICore
 *   BACKGROUND_USE_BLOCKED error remains the authoritative backstop and is
 *   treated as an expected state, still recorded in diagnostics.
 * - The loop stops itself whenever [DrainResult] says there is nothing more
 *   it may legally do right now (not foreground / disabled / quota-paused).
 */
class TranscriptionDrainService : Service() {
    private val serviceJob = SupervisorJob()
    private val serviceScope = CoroutineScope(serviceJob + Dispatchers.Main.immediate)
    private var loopJob: Job? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val app = application as? PocketAssistantApp ?: return START_NOT_STICKY
        if (loopJob?.isActive != true) {
            loopJob = serviceScope.launch {
                drainLoop(app.container.transcriptionDrain)
            }
        }
        return START_NOT_STICKY
    }

    private suspend fun drainLoop(drain: TranscriptionDrain) {
        while (true) {
            val result = drain.drainOnce()
            when {
                result.notForeground || result.disabled || result.quotaPaused -> {
                    Log.i(TAG, "drain paused: $result")
                    stopSelf()
                    return
                }
                result.quiet -> delay(IDLE_POLL_MS)
                else -> delay(PASS_POLL_MS)
            }
        }
    }

    override fun onDestroy() {
        loopJob?.cancel()
        serviceJob.cancel()
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    companion object {
        private const val TAG = "TranscriptionDrainService"
        private const val IDLE_POLL_MS = 10_000L
        private const val PASS_POLL_MS = 1_000L

        fun start(context: Context) {
            context.startService(Intent(context, TranscriptionDrainService::class.java))
        }

        fun stop(context: Context) {
            context.stopService(Intent(context, TranscriptionDrainService::class.java))
        }
    }
}