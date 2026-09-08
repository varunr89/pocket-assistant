package com.varun.pocketassistant.capture

import android.Manifest
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.SystemClock
import android.os.UserManager
import android.util.Log
import androidx.core.content.ContextCompat
import com.varun.pocketassistant.PocketAssistantApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

/**
 * Boot re-arm for schedule-gated capture (L1 failure fix; QA report
 * 2026-09-08, docs/qa/results/2026-09-08-schedule-gated-capture/report.md).
 *
 * Contract (Varun's decision): capture re-arms at FIRST UNLOCK, never from
 * boot while the device is locked — no mic before the user unlocks. This
 * app has no directBootAware component, so the system cannot start this
 * receiver before the first unlock at all; the `isUserUnlocked` check below
 * is defense-in-depth on top of that, not the primary gate.
 *
 * Why the receiver does not call startForegroundService directly:
 * [RecordingService] is a microphone-type foreground service, and for apps
 * targeting API 34+ the system refuses to start microphone-type FGS while
 * the process carries the BOOT_COMPLETED temporary-allowlist attribution
 * (ForegroundServiceStartNotAllowedException: "FGS type microphone not
 * allowed to start from BOOT_COMPLETED"; the restriction also covers
 * LOCKED_BOOT_COMPLETED delivery and has applied to microphone since
 * Android 14). The receiver instead arms a one-shot [AlarmManager] alarm;
 * when it fires, the app runs under the alarm's own temporary allowlist
 * (not the boot one), so the microphone FGS start succeeds. The alarm is
 * re-armed on every boot, which also makes the deferral survive process
 * death between unlock and the deferred start. [BootReArmPolicy] decides
 * whether anything is armed at all — a schedule the user turned fully off
 * stays off.
 */
class BootReArmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action ?: return
        if (action != Intent.ACTION_BOOT_COMPLETED &&
            action != Intent.ACTION_LOCKED_BOOT_COMPLETED
        ) {
            return
        }

        // Privacy contract: never touch capture before the first unlock.
        val userManager = context.getSystemService(UserManager::class.java)
        if (!userManager.isUserUnlocked) {
            Log.i(TAG, "Ignoring $action — device still locked; no capture before first unlock")
            return
        }
        // Mirrors MainActivity.startCaptureServiceIfAppropriate(): the boot
        // path re-arms only when the microphone permission already exists;
        // the first-run permission flow re-arms capture from its callback.
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO)
            != PackageManager.PERMISSION_GRANTED
        ) {
            Log.i(TAG, "Ignoring $action — microphone permission not granted")
            return
        }

        val app = context.applicationContext as PocketAssistantApp
        val pending = goAsync()
        val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
        scope.launch {
            try {
                // Privacy-first load failure semantics: if the saved schedule
                // cannot be read, stay OFF (a corrupt DB must not silently
                // re-enable capture against a user's explicit off choice).
                val schedule = runCatching { app.container.captureScheduleStore.load() }
                    .getOrNull()
                if (schedule == null) {
                    Log.w(TAG, "Cannot read capture schedule at boot re-arm; staying off")
                    return@launch
                }
                if (!BootReArmPolicy.shouldRearm(schedule)) {
                    Log.i(TAG, "Capture schedule off (${schedule.summary()}) — stays off")
                    return@launch
                }
                Log.i(TAG, "Capture schedule active (${schedule.summary()}) — re-arming via deferred alarm")
                armDeferredStart(context)
            } finally {
                pending.finish()
                scope.cancel()
            }
        }
    }

    /**
     * Schedules the single start point. [PendingIntent.getForegroundService]
     * makes the alarm itself deliver ACTION_START, so [RecordingService]'s
     * existing gate-aware start path runs unchanged (startAsForeground +
     * engine.start(), which publishes SCHEDULED_OFF / RECORDING from the
     * gate's first check — no "Listening" flash outside the window).
     */
    private fun armDeferredStart(context: Context) {
        val alarmManager = context.getSystemService(AlarmManager::class.java)
        alarmManager.setAndAllowWhileIdle(
            AlarmManager.ELAPSED_REALTIME_WAKEUP,
            SystemClock.elapsedRealtime() + REARM_DELAY_MS,
            deferredStartIntent(context),
        )
    }

    private fun deferredStartIntent(context: Context): PendingIntent =
        PendingIntent.getForegroundService(
            context,
            REQUEST_CODE_DEFERRED_START,
            Intent(context, RecordingService::class.java)
                .setAction(RecordingService.ACTION_START),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )

    companion object {
        private const val TAG = "BootReArmReceiver"
        private const val REQUEST_CODE_DEFERRED_START = 4713

        /**
         * Runs the re-arm only after the system's BOOT_COMPLETED temporary
         * allowlist (~20s) has expired, so the microphone FGS start is not
         * boot-attributed and rejected (see class doc). Inexact alarm: under
         * doze the fire may slip by minutes; the recording service + gate
         * absorb that, capture opens at the next schedule window regardless.
         */
        private const val REARM_DELAY_MS = 60_000L
    }
}