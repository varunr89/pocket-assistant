package com.varun.pocketassistant.capture

import java.time.Instant
import java.time.ZoneId
import java.util.concurrent.atomic.AtomicReference
import kotlin.coroutines.coroutineContext
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

/**
 * Runtime capture gate: the live on/off authority for the capture engine.
 *
 * Two frame-precise surfaces over [WeeklyCaptureSchedule]:
 * - [isOpenNow] / [awaitOpen]: checks against the latest schedule and the
 *   injected clock. The engine gates EVERY frame read and segment write on
 *   these, so window boundaries (08:00 / 17:00) take effect within one audio
 *   frame — no audio is written outside the schedule, and the notification /
 *   UI never claims LISTENING while the gate is closed.
 * - The open/closed transitions themselves surface through the engine stats
 *   (RECORDING vs SCHEDULED_OFF), which the notification and UI observe.
 *
 * The clock and zone are injectable for JVM tests; production uses device
 * wall-clock time in the system default zone.
 */
class CaptureScheduleGate(
    scheduleFlow: Flow<WeeklyCaptureSchedule>,
    scope: CoroutineScope,
    private val awaitPollMs: Long = DEFAULT_AWAIT_POLL_MS,
    private val clockMs: () -> Long = System::currentTimeMillis,
    private val zone: () -> ZoneId = { ZoneId.systemDefault() },
) {
    private val latest = AtomicReference(WeeklyCaptureSchedule())

    init {
        // Track the newest schedule for the frame-precise checks below. The
        // Room flow emits the persisted row (and every save/settings edit).
        scope.launch {
            scheduleFlow.collect { latest.set(it) }
        }
    }

    /** Frame-precise: is capture allowed right now? */
    fun isOpenNow(): Boolean =
        latest.get().isCaptureOpenAt(Instant.ofEpochMilli(clockMs()), zone())

    /**
     * Suspends until capture is allowed (schedule window or override), polling
     * at [awaitPollMs] so an edited schedule or the override toggle is picked
     * up without a service restart. Returns false if the calling coroutine is
     * cancelled while waiting (e.g. the service stopping).
     */
    suspend fun awaitOpen(): Boolean {
        while (true) {
            if (isOpenNow()) return true
            try {
                delay(awaitPollMs)
            } catch (e: CancellationException) {
                return false
            }
            if (!coroutineContext.isActive) return false
        }
    }

    companion object {
        /** Poll interval for [awaitOpen] — bounds boundary imprecision to ~1s. */
        const val DEFAULT_AWAIT_POLL_MS = 1_000L
    }
}