package com.varun.pocketassistant.capture

import java.time.DayOfWeek
import java.time.Instant
import java.time.ZoneId

/**
 * Weekly capture window + manual override (meeting-layer spec 75f596b):
 * the mic is ON only inside the window — or whenever [overrideEnabled] is on.
 *
 * Outside the window nothing is captured: the engine keeps the mic released
 * and writes zero audio bytes. Inside the window (or under override) the
 * capture stack runs exactly as before (mic -> VAD -> 16k mono segments).
 *
 * Boundaries are minute-inclusive on the start and minute-exclusive on the
 * end: a window of 08:00-17:00 is open at exactly 08:00:00 and closed at
 * exactly 17:00:00. An overnight window (start > end) wraps past midnight
 * and its post-midnight leg belongs to the day the window started on.
 */
data class WeeklyCaptureSchedule(
    val weekdays: Set<DayOfWeek> = DEFAULT_WEEKDAYS,
    val startMinuteOfDay: Int = DEFAULT_START_MINUTE,
    val endMinuteOfDay: Int = DEFAULT_END_MINUTE,
    val overrideEnabled: Boolean = false,
) {
    fun isCaptureOpenAt(at: Instant, zone: ZoneId): Boolean {
        if (overrideEnabled) return true
        val local = at.atZone(zone)
        val minute = local.hour * 60 + local.minute
        // For an overnight window, times before the end belong to the window
        // that started yesterday, so evaluate the weekday against that day.
        val windowDay = if (startMinuteOfDay < endMinuteOfDay || minute >= startMinuteOfDay) {
            local.dayOfWeek
        } else {
            local.dayOfWeek.minus(1)
        }
        if (windowDay !in weekdays) return false
        return when {
            startMinuteOfDay == endMinuteOfDay -> false
            startMinuteOfDay < endMinuteOfDay ->
                minute >= startMinuteOfDay && minute < endMinuteOfDay
            else -> minute >= startMinuteOfDay || minute < endMinuteOfDay
        }
    }

    /** Human summary for settings/Today UI, e.g. "Weekdays 08:00-17:00". */
    fun summary(): String {
        val days = when {
            weekdays.isEmpty() -> "No days"
            weekdays == ALL_DAYS -> "Every day"
            weekdays == DEFAULT_WEEKDAYS -> "Weekdays"
            else -> weekdays.sortedBy { it.value }.joinToString(",") {
                it.name.take(3).lowercase().replaceFirstChar(Char::uppercase)
            }
        }
        return "$days ${fmtTime(startMinuteOfDay)}-${fmtTime(endMinuteOfDay)}"
    }

    /** Stable single-line encoding for the Room settings KV row. */
    fun encode(): String =
        listOf(
            weekdays.sortedBy { it.value }.joinToString(",") { it.name },
            startMinuteOfDay.toString(),
            endMinuteOfDay.toString(),
            if (overrideEnabled) "1" else "0",
        ).joinToString("|")

    companion object {
        val DEFAULT_WEEKDAYS: Set<DayOfWeek> =
            DayOfWeek.entries.filter { it in DayOfWeek.MONDAY..DayOfWeek.FRIDAY }.toSet()
        val ALL_DAYS: Set<DayOfWeek> = DayOfWeek.entries.toSet()
        const val DEFAULT_START_MINUTE = 8 * 60 // 08:00
        const val DEFAULT_END_MINUTE = 17 * 60 // 17:00
        private const val MINUTES_PER_DAY = 24 * 60

        /**
         * Parse the store encoding. Missing or corrupt fields fall back to
         * their defaults; a present-but-empty weekday list intentionally
         * survives (an enabled schedule with zero days means "never").
         */
        fun parse(raw: String?): WeeklyCaptureSchedule {
            val parts = raw?.split("|") ?: emptyList()
            if (parts.isEmpty()) return WeeklyCaptureSchedule()
            val dayToken = parts.getOrNull(0)
            val days = when {
                dayToken == null -> DEFAULT_WEEKDAYS
                dayToken.isBlank() -> emptySet()
                else -> {
                    val parsed = dayToken.split(",").mapNotNull { token ->
                        runCatching { DayOfWeek.valueOf(token.trim()) }.getOrNull()
                    }.toSet()
                    // All-corrupt is not a user choice — fall back to defaults.
                    if (parsed.isEmpty()) DEFAULT_WEEKDAYS else parsed
                }
            }
            val start = parseMinute(parts.getOrNull(1), DEFAULT_START_MINUTE)
            val end = parseMinute(parts.getOrNull(2), DEFAULT_END_MINUTE)
            val override = parts.getOrNull(3) == "1"
            return WeeklyCaptureSchedule(days, start, end, override)
        }

        private fun parseMinute(raw: String?, fallback: Int): Int {
            val v = raw?.toIntOrNull() ?: return fallback
            return if (v in 0 until MINUTES_PER_DAY) v else fallback
        }

        private fun fmtTime(minuteOfDay: Int): String =
            "%02d:%02d".format(minuteOfDay / 60, minuteOfDay % 60)
    }
}