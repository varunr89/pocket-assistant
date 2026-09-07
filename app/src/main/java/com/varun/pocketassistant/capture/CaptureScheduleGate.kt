package com.varun.pocketassistant.capture

import java.time.Instant
import java.time.ZoneId
import java.util.concurrent.atomic.AtomicReference
import kotlin.coroutines.coroutineContext
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

/**
 * Runtime capture gate: the live on/off authority for the capture engine.
 *
 * Two surfaces, both over [WeeklyCaptureSchedule]:
 * - [isOpenNow] / [awaitOpen]: frame-precise checks against the latest
 *   schedule and the injected clock. The engine gates EVERY frame read and
 *   segment write on these, so window boundaries (08:00 / 17:00) take effect
 *   within one audio frame — no audio is written outside the schedule.
 * - [open]: a StateFlow observation signal, re-evaluated whenever the
 *   persisted schedule changes (including the override toggle) or a periodic
 *   [tickMs] fires; used for UI/observability and pinned by the gate tests.
 *
 * The clock and zone are injectable for JVM tests; production uses device
 * wall-clock time in the system default zone.
 */
class CaptureScheduleGate(
    scheduleFlow: Flow<WeeklyCaptureSchedule>,
    scope: CoroutineScope,
    private val tickMs: Long = DEFAULT_TICK_MS,
    private val awaitPollMs: Long = DEFAULT_AWAIT_POLL_MS,
    private val clockMs: () -> Long = System::currentTimeMillis,
    private val zone: () -> ZoneId = { ZoneId.systemDefault() },
) {
    private val latest = AtomicReference(WeeklyCaptureSchedule())

    val open: StateFlow<Boolean> = combine(
        scheduleFlow,
        tick(),
    ) { schedule, _ -> schedule.isCaptureOpenAt(Instant.ofEpochMilli(clockMs()), zone()) }
        .distinctUntilChanged()
        .stateIn(
            scope = scope,
            started = SharingStarted.Eagerly,
            initialValue = WeeklyCaptureSchedule()
                .isCaptureOpenAt(Instant.ofEpochMilli(clockMs()), zone()),
        )

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

    private fun tick(): Flow<Unit> = flow {
        while (true) {
            emit(Unit)
            delay(tickMs)
        }
    }

    companion object {
        /** How often the [open] observation signal re-checks boundaries. */
        const val DEFAULT_TICK_MS = 15_000L
        /** Poll interval for [awaitOpen] — bounds boundary imprecision to ~1s. */
        const val DEFAULT_AWAIT_POLL_MS = 1_000L
    }
}