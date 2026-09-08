package com.varun.pocketassistant.capture

import java.time.Instant
import java.time.ZoneId
import java.util.concurrent.atomic.AtomicReference
import kotlin.coroutines.coroutineContext
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CompletableDeferred
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
 *
 * Load semantics (B2 — persisted-schedule race): the gate starts CLOSED and
 * stays closed until the persisted schedule has been observed at least once.
 * It never falls back to the default schedule before the Room flow emits, so
 * a non-default saved schedule that is closed right now cannot flash the mic
 * open against the default. The engine awaits [awaitLoaded] before its first
 * mic decision, so the initial RECORDING/SCHEDULED_OFF publish is always
 * decided against the SAVED schedule, never the default.
 */
class CaptureScheduleGate(
    scheduleFlow: Flow<WeeklyCaptureSchedule>,
    scope: CoroutineScope,
    private val awaitPollMs: Long = DEFAULT_AWAIT_POLL_MS,
    private val clockMs: () -> Long = System::currentTimeMillis,
    private val zone: () -> ZoneId = { ZoneId.systemDefault() },
) {
    // null until the persisted schedule has been observed at least once; the
    // gate is CLOSED (never open) until then.
    private val latest = AtomicReference<WeeklyCaptureSchedule?>(null)
    private val loaded = CompletableDeferred<Unit>()

    init {
        // Track the newest schedule for the frame-precise checks below. The
        // Room flow emits the persisted row (and every save/settings edit).
        scope.launch {
            scheduleFlow.collect { s ->
                latest.set(s)
                loaded.complete(Unit)
            }
        }
    }

    /** Frame-precise: is capture allowed right now? Closed until loaded. */
    fun isOpenNow(): Boolean {
        val s = latest.get() ?: return false
        return s.isCaptureOpenAt(Instant.ofEpochMilli(clockMs()), zone())
    }

    /**
     * Suspends until the persisted schedule has been observed at least once.
     * The engine calls this before its first mic decision so the initial
     * RECORDING/SCHEDULED_OFF publish is decided against the SAVED schedule
     * (never the default). Room's observe() flow always emits the current row
     * on collection, so this completes promptly.
     */
    suspend fun awaitLoaded() {
        loaded.await()
    }

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
