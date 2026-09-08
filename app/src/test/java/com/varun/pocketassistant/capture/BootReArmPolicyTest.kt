package com.varun.pocketassistant.capture

import java.time.DayOfWeek
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Boot re-arm policy (L1 boot-receiver fix, QA run 2026-09-08): the pure
 * "should the boot path re-start the capture service" decision over the
 * persisted schedule. BootReArmReceiver.onReceive itself is
 * Android-framework-bound (broadcast + AlarmManager wiring) and is not
 * unit-testable on the JVM without Robolectric — the receiver delegates
 * every schedule decision to this policy, so pinning it pins the receiver's
 * behavior: an ON schedule re-arms the service start, an OFF schedule
 * stays off.
 *
 * Same hand-rolled JUnit-4.8 assertion surface as CaptureScheduleTest /
 * CaptureScheduleGateTest (jlibrosa shadowing blocks assertThrows/@Rule).
 */
class BootReArmPolicyTest {

    @Test
    fun defaultWeekdayScheduleRearms() {
        // Defaults: Mon-Fri 08:00-17:00, override off — capture-capable.
        assertTrue(BootReArmPolicy.shouldRearm(WeeklyCaptureSchedule()))
    }

    @Test
    fun activeWindowRearmsEvenThoughGateIsClosedRightNow() {
        // Re-arm means "the schedule could open the mic", not "open at this
        // instant": a 06:00 unlock must still start the service so the
        // 08:00 window opens without an app open (the L1 failure).
        val schedule = WeeklyCaptureSchedule(
            weekdays = setOf(DayOfWeek.MONDAY),
            startMinuteOfDay = 8 * 60,
            endMinuteOfDay = 17 * 60,
        )
        assertTrue(BootReArmPolicy.shouldRearm(schedule))
    }

    @Test
    fun overnightWindowRearms() {
        val schedule = WeeklyCaptureSchedule(
            weekdays = setOf(DayOfWeek.MONDAY),
            startMinuteOfDay = 22 * 60,
            endMinuteOfDay = 6 * 60,
        )
        assertTrue(BootReArmPolicy.shouldRearm(schedule))
    }

    @Test
    fun overrideRearmsEvenWithNoEnabledDays() {
        // "Mic on outside schedule" is the manual ON — it must beat an
        // emptied weekday set.
        val schedule = WeeklyCaptureSchedule(
            weekdays = emptySet(),
            overrideEnabled = true,
        )
        assertTrue(BootReArmPolicy.shouldRearm(schedule))
    }

    @Test
    fun fullyDisabledScheduleStaysOff() {
        // User turned every day off and the override is off: the boot path
        // must NOT start the service (nothing would ever open the gate).
        val schedule = WeeklyCaptureSchedule(weekdays = emptySet())
        assertFalse(BootReArmPolicy.shouldRearm(schedule))
    }

    @Test
    fun degenerateWindowWithEqualStartAndEndStaysOff() {
        // start == end is the model's "window never open" encoding
        // (isCaptureOpenAt returns false); re-arming it would run the
        // service forever with no possible capture.
        val schedule = WeeklyCaptureSchedule(
            weekdays = setOf(DayOfWeek.MONDAY),
            startMinuteOfDay = 9 * 60,
            endMinuteOfDay = 9 * 60,
        )
        assertFalse(BootReArmPolicy.shouldRearm(schedule))
    }
}