package com.varun.pocketassistant.capture

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.UserManager
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.varun.pocketassistant.MainActivity
import com.varun.pocketassistant.PocketAssistantApp
import com.varun.pocketassistant.R
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
 * boot while the device is locked — no mic before the user unlocks. This app
 * has no directBootAware component, so the system cannot start this receiver
 * before the first unlock at all; `BOOT_COMPLETED` is the valid
 * post-first-unlock signal for a non-directBootAware receiver (a
 * non-directBootAware receiver does NOT receive `LOCKED_BOOT_COMPLETED` —
 * that broadcast requires Direct-Boot awareness). The `isUserUnlocked` check
 * below is defense-in-depth on top of that, not the primary gate.
 *
 * Why the receiver does NOT start the microphone foreground service itself:
 * [RecordingService] is a microphone-type foreground service, and for apps
 * targeting API 34+ the system refuses to create a microphone FGS while the
 * app is in the background — the `RECORD_AUDIO` permission is subject to
 * while-in-use restrictions, so a microphone FGS can only be created while
 * the app has a visible activity, or via a while-in-use exemption (a
 * notification/widget interaction, a system component, etc.). An
 * [android.app.AlarmManager] alarm — exact or inexact — is NOT one of those
 * exemptions: it fires with no visible activity and no user interaction, so
 * the deferred `startForegroundService` would throw
 * `ForegroundServiceStartNotAllowedException` (the same silent-loss shape as
 * L1). The receiver therefore re-arms by posting a "capture paused" resume
 * notification; tapping it opens [MainActivity], whose existing
 * `startCaptureServiceIfAppropriate()` starts the service while the app is in
 * the foreground — the only path with real authority to start the mic FGS.
 * [BootReArmPolicy] decides whether anything is posted at all — a schedule
 * the user turned fully off stays off (and any stale resume notification is
 * cancelled).
 *
 * Best-effort notes: the resume notification is a user-visible prompt, not an
 * automatic start — capture resumes on the user's tap (or the next app open),
 * which is the strongest re-arm the platform permits for a background mic FGS.
 * A battery-restricted device can also defer `BOOT_COMPLETED` delivery, so QA
 * should confirm the device is not battery-restricted before judging L1.
 */
class BootReArmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != Intent.ACTION_BOOT_COMPLETED) return

        // Privacy contract: never touch capture before the first unlock.
        val userManager = context.getSystemService(UserManager::class.java)
        if (!userManager.isUserUnlocked) {
            Log.i(TAG, "Ignoring BOOT_COMPLETED — device still locked; no capture before first unlock")
            return
        }
        // Mirrors MainActivity.startCaptureServiceIfAppropriate(): the boot
        // path re-arms only when the microphone permission already exists;
        // the first-run permission flow re-arms capture from its callback.
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO)
            != PackageManager.PERMISSION_GRANTED
        ) {
            Log.i(TAG, "Ignoring BOOT_COMPLETED — microphone permission not granted")
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
                    cancelResumeNotification(context)
                    return@launch
                }
                Log.i(TAG, "Capture schedule active (${schedule.summary()}) — posting resume notification")
                postResumeNotification(context)
            } finally {
                pending.finish()
                scope.cancel()
            }
        }
    }

    /**
     * Posts the "capture paused after reboot" prompt. Tapping it opens
     * [MainActivity], which starts [RecordingService] while the app is in the
     * foreground (the only legal path to create a microphone FGS on API 34+).
     */
    private fun postResumeNotification(context: Context) {
        val nm = context.getSystemService(NotificationManager::class.java)
        nm.createNotificationChannel(
            NotificationChannel(
                CHANNEL_ID,
                context.getString(R.string.notification_rearm_channel_name),
                NotificationManager.IMPORTANCE_DEFAULT,
            ).apply {
                description = context.getString(R.string.notification_rearm_channel_desc)
            },
        )
        val contentIntent = PendingIntent.getActivity(
            context,
            REQUEST_CODE_RESUME,
            Intent(context, MainActivity::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_mic)
            .setContentTitle(context.getString(R.string.notification_rearm_title))
            .setContentText(context.getString(R.string.notification_rearm_text))
            .setContentIntent(contentIntent)
            .setAutoCancel(true)
            .setCategory(NotificationCompat.CATEGORY_SERVICE)
            .build()
        nm.notify(NOTIFICATION_ID, notification)
    }

    /** Removes a stale resume prompt when the schedule is fully off. */
    private fun cancelResumeNotification(context: Context) {
        context.getSystemService(NotificationManager::class.java)
            .cancel(NOTIFICATION_ID)
    }

    companion object {
        private const val TAG = "BootReArmReceiver"
        private const val CHANNEL_ID = "capture_rearm"
        private const val NOTIFICATION_ID = 4713
        private const val REQUEST_CODE_RESUME = 4713
    }
}
