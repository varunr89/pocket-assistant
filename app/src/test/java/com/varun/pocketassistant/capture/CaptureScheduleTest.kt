package com.varun.pocketassistant.capture

import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Weekday/time/override gate math (M1 meeting-layer increment 3a).
 *
 * JVM tests only — note the repo's jlibrosa fat jar shadows JUnit with its
 * bundled 2011 copy, so these use the 4.8-era assertion surface only (no
 * assertThrows, no @Rule).
 */
class CaptureScheduleTest {

    private val zone = ZoneId.of("UTC")
    private val monday = LocalDate.of(2026, 9, 7)

    private fun at(day: DayOfWeek, hour: Int, minute: Int): Instant =
        monday.plusDays(((day.value - DayOfWeek.MONDAY.value) % 7).toLong())
            .atTime(LocalTime.of(hour, minute))
            .atZone(zone)
            .toInstant()

    private fun open(s: WeeklyCaptureSchedule, day: DayOfWeek, hour: Int, minute: Int): Boolean =
        s.isCaptureOpenAt(at(day, hour, minute), zone)

    @Test
    fun defaultsAreWeekdays0800to1700OverrideOff() {
        val s = WeeklyCaptureSchedule()
        assertEquals(
            DayOfWeek.entries.filter { it in DayOfWeek.MONDAY..DayOfWeek.FRIDAY }.toSet(),
            s.weekdays,
        )
        assertEquals(480, s.startMinuteOfDay)
        assertEquals(1020, s.endMinuteOfDay)
        assertFalse(s.overrideEnabled)
    }

    // ---- (a) schedule off -> capture gate closed (nothing may be written) ----

    @Test
    fun closedOutsideWindowNoCapture() {
        val s = WeeklyCaptureSchedule()
        assertFalse(open(s, DayOfWeek.MONDAY, 0, 0))
        assertFalse(open(s, DayOfWeek.THURSDAY, 7, 59))
        assertFalse(open(s, DayOfWeek.FRIDAY, 22, 0))
    }

    @Test
    fun weekendsClosedEvenInsideWorkingHours() {
        val s = WeeklyCaptureSchedule()
        assertFalse(open(s, DayOfWeek.SATURDAY, 12, 0))
        assertFalse(open(s, DayOfWeek.SUNDAY, 12, 0))
    }

    @Test
    fun emptyWeekdaysNeverOpen() {
        val s = WeeklyCaptureSchedule(weekdays = emptySet())
        assertFalse(open(s, DayOfWeek.MONDAY, 12, 0))
    }

    // ---- (b) schedule on -> capture proceeds ----

    @Test
    fun openInsideWeekdayWindow() {
        val s = WeeklyCaptureSchedule()
        assertTrue(open(s, DayOfWeek.MONDAY, 8, 30))
        assertTrue(open(s, DayOfWeek.WEDNESDAY, 12, 30))
        assertTrue(open(s, DayOfWeek.FRIDAY, 16, 59))
    }

    // ---- (d) boundaries: exactly 08:00 / exactly 17:00 ----

    @Test
    fun boundaryStartInclusive_exactly0800Open() {
        val s = WeeklyCaptureSchedule()
        assertTrue(open(s, DayOfWeek.TUESDAY, 8, 0))
        assertFalse(open(s, DayOfWeek.TUESDAY, 7, 59))
    }

    @Test
    fun boundaryEndExclusive_exactly1700Closed() {
        val s = WeeklyCaptureSchedule()
        assertTrue(open(s, DayOfWeek.TUESDAY, 16, 59))
        assertFalse(open(s, DayOfWeek.TUESDAY, 17, 0))
        assertFalse(open(s, DayOfWeek.TUESDAY, 17, 1))
    }

    // ---- (c) override toggles capture on/off ----

    @Test
    fun overrideOpensOutsideSchedule() {
        val s = WeeklyCaptureSchedule(overrideEnabled = true)
        assertTrue(open(s, DayOfWeek.SUNDAY, 3, 0))
        assertTrue(open(s, DayOfWeek.MONDAY, 23, 30))
        assertTrue(open(s, DayOfWeek.SATURDAY, 17, 0))
    }

    @Test
    fun overrideOffMeansScheduleApplies() {
        val s = WeeklyCaptureSchedule(overrideEnabled = false)
        assertFalse(open(s, DayOfWeek.SUNDAY, 3, 0))
    }

    // ---- schedule shape edge cases ----

    @Test
    fun startEqualsEndClosedAllDay() {
        val s = WeeklyCaptureSchedule(startMinuteOfDay = 540, endMinuteOfDay = 540)
        assertFalse(open(s, DayOfWeek.MONDAY, 9, 0))
    }

    @Test
    fun overnightWindowWrapsPastMidnight() {
        val s = WeeklyCaptureSchedule(
            weekdays = setOf(DayOfWeek.MONDAY),
            startMinuteOfDay = 22 * 60,
            endMinuteOfDay = 2 * 60,
        )
        assertFalse(open(s, DayOfWeek.MONDAY, 21, 59))
        assertTrue(open(s, DayOfWeek.MONDAY, 23, 0))
        // Post-midnight leg belongs to the window that started Monday.
        assertTrue(open(s, DayOfWeek.TUESDAY, 1, 30))
        assertFalse(open(s, DayOfWeek.TUESDAY, 2, 0))
    }

    // ---- persistence encoding ----

    @Test
    fun encodeParseRoundTrip() {
        val s = WeeklyCaptureSchedule(
            weekdays = setOf(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY),
            startMinuteOfDay = 555,
            endMinuteOfDay = 1000,
            overrideEnabled = true,
        )
        assertEquals(s, WeeklyCaptureSchedule.parse(s.encode()))
    }

    @Test
    fun parseNullIsDefault() {
        assertEquals(WeeklyCaptureSchedule(), WeeklyCaptureSchedule.parse(null))
    }

    @Test
    fun parseGarbageFallsBackToDefaults() {
        assertEquals(WeeklyCaptureSchedule(), WeeklyCaptureSchedule.parse("garbage||bogus|wat"))
    }

    @Test
    fun parseFieldWiseFallbackKeepsValidParts() {
        val s = WeeklyCaptureSchedule.parse("MONDAY,FRIDAY|9999|120")
        assertEquals(setOf(DayOfWeek.MONDAY, DayOfWeek.FRIDAY), s.weekdays)
        assertEquals(480, s.startMinuteOfDay) // 9999 invalid -> default
        assertEquals(120, s.endMinuteOfDay) // valid minutes survive
        assertFalse(s.overrideEnabled)
    }

    @Test
    fun parseExplicitlyEmptyWeekdaysStaysEmpty() {
        val s = WeeklyCaptureSchedule.parse("|480|1020|0")
        assertEquals(emptySet<DayOfWeek>(), s.weekdays)
        assertFalse(s.overrideEnabled)
    }

    @Test
    fun summaryHumanLabels() {
        assertEquals("Weekdays 08:00-17:00", WeeklyCaptureSchedule().summary())
        assertEquals("No days 08:00-17:00", WeeklyCaptureSchedule(weekdays = emptySet()).summary())
        val mixed = WeeklyCaptureSchedule(weekdays = setOf(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY))
        assertEquals("Mon,Wed 08:00-17:00", mixed.summary())
    }
}