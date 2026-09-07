package com.varun.pocketassistant.capture

import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import kotlinx.coroutines.CoroutineScope
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
 * the gate's transitions over a virtual clock, exactly as production reads
 * them ([CaptureScheduleGate.isOpenNow] / [awaitOpen]).
 *
 * Same JUnit-4.8 assertion surface as [CaptureScheduleTest] (jlibrosa
 * shadowing blocks assertThrows/@Rule).
 */
@OptIn(ExperimentalCoroutinesApi::class)
class CaptureScheduleGateTest {

    private val zone = ZoneId.of("UTC")
    private val monday = LocalDate.of(2026, 9, 7)

    private fun at(date: LocalDate, time: LocalTime): Long =
        date.atTime(time).atZone(zone).toInstant().toEpochMilli()

    private fun gate(
        scope: CoroutineScope,
        schedule: MutableStateFlow<WeeklyCaptureSchedule>,
        zone: ZoneId = this.zone,
        clock: () -> Long,
    ): CaptureScheduleGate = CaptureScheduleGate(
        scheduleFlow = schedule,
        scope = scope,
        clockMs = clock,
        zone = { zone },
    )

    @Test
    fun closedBeforeWindowOpensAtExactly0800() = runTest {
        var now = at(monday, LocalTime.of(7, 30))
        val schedule = MutableStateFlow(WeeklyCaptureSchedule())
        val gate = gate(backgroundScope, schedule) { now }
        runCurrent()
        assertFalse(gate.isOpenNow()) // 07:30 — ahead of the window

        now = at(monday, LocalTime.of(7, 59))
        assertFalse(gate.isOpenNow()) // still just before the window

        // Boundary: exactly 08:00 opens (start is minute-inclusive).
        now = at(monday, LocalTime.of(8, 0))
        assertTrue(gate.isOpenNow())
    }

    @Test
    fun openDuringWindowClosesAtExactly1700() = runTest {
        var now = at(monday, LocalTime.of(12, 0))
        val schedule = MutableStateFlow(WeeklyCaptureSchedule())
        val gate = gate(backgroundScope, schedule) { now }
        runCurrent()
        assertTrue(gate.isOpenNow())

        now = at(monday, LocalTime.of(16, 59))
        assertTrue(gate.isOpenNow())

        // Boundary: exactly 17:00 closes (end is minute-exclusive).
        now = at(monday, LocalTime.of(17, 0))
        assertFalse(gate.isOpenNow())
    }

    @Test
    fun overrideFlipsGateLiveWithoutWaitingForAPoll() = runTest {
        // Monday 02:00 — hours outside the window.
        val now = at(monday, LocalTime.of(2, 0))
        val schedule = MutableStateFlow(WeeklyCaptureSchedule())
        val gate = gate(backgroundScope, schedule) { now }
        runCurrent()
        assertFalse(gate.isOpenNow())

        // Override on -> gate opens immediately once the latest-schedule
        // tracker absorbs the flow change; the engine does not wait for a
        // boundary poll round.
        schedule.value = schedule.value.copy(overrideEnabled = true)
        runCurrent()
        assertTrue(gate.isOpenNow())

        schedule.value = schedule.value.copy(overrideEnabled = false)
        runCurrent()
        assertFalse(gate.isOpenNow())
    }

    @Test
    fun weekendStaysClosedAndMondayWindowReopens() = runTest {
        var now = at(monday.minusDays(1), LocalTime.of(22, 0)) // Sunday 22:00
        val schedule = MutableStateFlow(WeeklyCaptureSchedule())
        val gate = gate(backgroundScope, schedule) { now }
        runCurrent()
        assertFalse(gate.isOpenNow()) // Sunday night — weekend, closed

        now = at(monday, LocalTime.of(8, 0))
        assertTrue(gate.isOpenNow()) // Monday 08:00 — the schedule reopens it
    }

    @Test
    fun zoneIsRespectedSameInstantOpensLocallyButClosesInUtc() = runTest {
        // Monday 23:30 UTC == Monday 16:30 America/Los_Angeles (PDT, UTC-7):
        // inside 08:00-17:00 local time the gate is open, while the same
        // instant is after 17:00 in UTC. Pins that production's
        // ZoneId.systemDefault() evaluation (not a hard-coded UTC) is the
        // contract — the injected zone is honored per gate.
        val instant = at(monday, LocalTime.of(23, 30))
        val laGate = gate(
            backgroundScope,
            MutableStateFlow(WeeklyCaptureSchedule()),
            ZoneId.of("America/Los_Angeles"),
            { instant },
        )
        val utcGate = gate(
            backgroundScope,
            MutableStateFlow(WeeklyCaptureSchedule()),
        ) { instant }
        assertTrue(laGate.isOpenNow()) // 16:30 Monday in LA — window open
        assertFalse(utcGate.isOpenNow()) // 23:30 Monday in UTC — window closed
    }

    // ---- awaitOpen: the frame-precise entry the engine suspends on ----

    @Test
    fun awaitOpenSuspendsWhileClosedThenReturnsAtBoundary() = runTest {
        var now = at(monday, LocalTime.of(7, 59))
        val schedule = MutableStateFlow(WeeklyCaptureSchedule())
        val gate = gate(backgroundScope, schedule) { now }
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
        val gate = gate(backgroundScope, schedule) { now }
        var opened: Boolean? = null
        backgroundScope.launch { opened = gate.awaitOpen() }
        runCurrent()
        assertNull(opened) // Monday 02:00 — outside the window

        schedule.value = schedule.value.copy(overrideEnabled = true)
        runCurrent() // the latest-schedule tracker absorbs the override
        advanceTimeBy(1_100) // <= 1 poll round of waiting
        runCurrent()
        assertEquals(true, opened)
    }
}