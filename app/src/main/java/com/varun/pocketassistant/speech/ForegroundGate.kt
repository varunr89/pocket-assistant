package com.varun.pocketassistant.speech

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Handler
import android.os.Looper

/**
 * Cheap, dependency-free foreground predicate. AICore only serves the TOP
 * FOREGROUND app, and this gate is the app-side first line: the drain prefers
 * to no-op here rather than burn a failed AICore call (the
 * BACKGROUND_USE_BLOCKED error remains the authoritative backstop).
 */
fun interface ForegroundGate {
    fun isForeground(): Boolean
}

/**
 * Tracks started-activity count via lifecycle callbacks and reports 0→1
 * (foreground) / 1→0 (background) transitions, debounced so activity
 * rotation does not stop/start the drain service.
 *
 * Registered against the Application context; lives for the process.
 */
class AppForegroundTracker(
    context: Context,
    private val debounceMs: Long = DEFAULT_DEBOUNCE_MS,
    private val onTransition: (nowForeground: Boolean) -> Unit,
) : ForegroundGate {

    private val app = context.applicationContext as Application
    private val handler = Handler(Looper.getMainLooper())
    private val stopRunnable = Runnable { onTransition(false) }

    @Volatile
    private var startedCount = 0

    private val callbacks = object : Application.ActivityLifecycleCallbacks {
        override fun onActivityStarted(activity: Activity) {
            if (startedCount == 0) {
                handler.removeCallbacks(stopRunnable)
                onTransition(true)
            }
            startedCount++
        }

        override fun onActivityStopped(activity: Activity) {
            startedCount--
            if (startedCount <= 0) {
                startedCount = 0
                handler.postDelayed(stopRunnable, debounceMs)
            }
        }

        override fun onActivityCreated(activity: Activity, savedInstanceState: android.os.Bundle?) = Unit
        override fun onActivityResumed(activity: Activity) = Unit
        override fun onActivityPaused(activity: Activity) = Unit
        override fun onActivitySaveInstanceState(activity: Activity, outState: android.os.Bundle) = Unit
        override fun onActivityDestroyed(activity: Activity) = Unit
    }

    init {
        app.registerActivityLifecycleCallbacks(callbacks)
    }

    override fun isForeground(): Boolean = startedCount > 0

    companion object {
        private const val DEFAULT_DEBOUNCE_MS = 300L
    }
}