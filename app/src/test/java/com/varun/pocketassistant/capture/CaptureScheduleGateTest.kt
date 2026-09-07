package com.varun.pocketassistant.capture

import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Runtime capture gate (M1 meeting-layer increment 3a): the authority the
 * capture engine obeys. The engine's contract is that while the gate is
 * closed the mic stays released and NO frame is processed — these tests pin
 * the gate's transitions over virtual time: window boundaries tick into
 * effect and the override flips it live without waiting for a tick. The
 * [CaptureScheduleGate.awaitOpen] tests pin the frame-precise entry the
 * engine actually suspends on.
 *
 * The gate's collection (including its infinite boundary ticker) runs in the
 * test backgroundScope so runTest does not wait on the never-ending ticker.
 * Same JUnit-4.8 assertion surface as [CaptureScheduleTest] (jlibrosa
 * shadowing blocks assertThrows/@Rule).
 */
@OptIn(ExperimentalCoroutinesApi::class)
class CaptureScheduleGateTest {

    private val zone = ZoneId.of("UTC")
    private val monday = LocalDate.of(2026, 9, 7)
    private val tickMs = 60_000L

    private fun at(date: LocalDate, time: LocalTime): Long =
        date.atTime(time).atZone(zone).toInstant().toEpochMilli()

    @Test
    fun closedBeforeWindowOpensAtExactly0800() = runTest {
        var now = at(monday, LocalTime.of(7, 30))
        val schedule = MutableStateFlow(WeeklyCaptureSchedule())
        val gate = CaptureScheduleGate(
            scheduleFlow = schedule,
            scope = backgroundScope,
            tickMs = tickMs,
            clockMs = { now },
            zone = { zone },
        )
        runCurrent()
        assertFalse(gate.open.value) // 07:30 — ahead of the window

        now = at(monday, LocalTime.of(7, 59))
        advanceTimeBy(tickMs + 1)
        runCurrent()
        assertFalse(gate.open.value) // still just before the window

        // Boundary: exactly 08:00 opens (start is minute-inclusive).
        now = at(monday, LocalTime.of(8, 0))
        advanceTimeBy(tickMs + 1)
        runCurrent()
        assertTrue(gate.open.value)
    }

    @Test
    fun openDuringWindowClosesAtExactly1700() = runTest {
        var now = at(monday, LocalTime.of(12, 0))
        val schedule = MutableStateFlow(WeeklyCaptureSchedule())
        val gate = CaptureScheduleGate(
            scheduleFlow = schedule,
            scope = backgroundScope,
            tickMs = tickMs,
            clockMs = { now },
            zone = { zone },
        )
        runCurrent()
        assertTrue(gate.open.value)

        now = at(monday, LocalTime.of(16, 59))
        advanceTimeBy(tickMs + 1)
        runCurrent()
        assertTrue(gate.open.value)

        // Boundary: exactly 17:00 closes (end is minute-exclusive).
        now = at(monday, LocalTime.of(17, 0))
        advanceTimeBy(tickMs + 1)
        runCurrent()
        assertFalse(gate.open.value)
    }

    @Test
    fun overrideFlipsGateLiveWithoutWaitingForATick() = runTest {
        // Monday 02:00 — hours outside the window.
        val now = at(monday, LocalTime.of(2, 0))
        val schedule = MutableStateFlow(WeeklyCaptureSchedule())
        val gate = CaptureScheduleGate(
            scheduleFlow = schedule,
            scope = backgroundScope,
            tickMs = tickMs,
            clockMs = { now },
            zone = { zone },
        )
        runCurrent()
        assertFalse(gate.open.value)

        // (a)/(c) override on -> gate opens immediately from the flow change;
        // the engine does not wait for the next 60s tick.
        schedule.value = schedule.value.copy(overrideEnabled = true)
        runCurrent()
        assertTrue(gate.open.value)

        schedule.value = schedule.value.copy(overrideEnabled = false)
        runCurrent()
        assertFalse(gate.open.value)
    }

    @Test
    fun weekendStaysClosedAndMondayWindowReopens() = runTest {
        var now = at(monday.minusDays(1), LocalTime.of(22, 0)) // Sunday 22:00
        val schedule = MutableStateFlow(WeeklyCaptureSchedule())
        val gate = CaptureScheduleGate(
            scheduleFlow = schedule,
            scope = backgroundScope,
            tickMs = tickMs,
            clockMs = { now },
            zone = { zone },
        )
        runCurrent()
        assertFalse(gate.open.value) // Sunday night — weekend, closed

        now = at(monday, LocalTime.of(8, 0))
        advanceTimeBy(tickMs + 1)
        runCurrent()
        assertTrue(gate.open.value) // Monday 08:00 — the schedule reopens it
    }

    // ---- awaitOpen: the frame-precise entry the engine suspends on ----

    @Test
    fun awaitOpenSuspendsWhileClosedThenReturnsAtBoundary() = runTest {
        var now = at(monday, LocalTime.of(7, 59))
        val schedule = MutableStateFlow(WeeklyCaptureSchedule())
        val gate = CaptureScheduleGate(
            scheduleFlow = schedule,
            scope = backgroundScope,
            tickMs = tickMs,
            clockMs = { now },
            zone = { zone },
        )
        var opened: Boolean? = null
        backgroundScope.launch { opened = gate.awaitOpen() }
        runCurrent()
        assertNull(opened) // 07:59 — waiting

        // Two poll rounds later the clock still says 07:59: still waiting.
        advanceTimeBy(2_100)
        runCurrent()
        assertNull(opened)

        // The clock crosses the boundary; the next poll iteration opens.
        now = at(monday, LocalTime.of(8, 0))
        advanceTimeBy(1_100)
        runCurrent()
        assertEquals(true, opened)
    }

    @Test
    fun awaitOpenPicksUpOverrideWithinOnePoll() = runTest {
        val now = at(monday, LocalTime.of(2, 0))
        val schedule = MutableStateFlow(WeeklyCaptureSchedule())
        val gate = CaptureScheduleGate(
            scheduleFlow = schedule,
            scope = backgroundScope,
            tickMs = tickMs,
            clockMs = { now },
            zone = { zone },
        )
        var opened: Boolean? = null
        backgroundScope.launch { opened = gate.awaitOpen() }
        runCurrent()
        assertNull(opened) // Monday 02:00 — outside the window

        schedule.value = schedule.value.copy(overrideEnabled = true)
        runCurrent() // the latest-schedule tracker absorbs the override
        advanceTimeBy(1_100) // <= 1 poll round of waiting (not 60s tick)
        runCurrent()
        assertEquals(true, opened)
    }
}