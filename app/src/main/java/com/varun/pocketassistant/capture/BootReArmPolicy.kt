package com.varun.pocketassistant.capture

/**
 * Pure decision for the boot re-arm path (L1 fix): given the persisted
 * schedule, should the capture service be re-started after a reboot?
 *
 * Semantics is "capture-capable", not "capture right now": the recording
 * service runs continuously and the [CaptureScheduleGate] is the authority
 * that keeps the mic OFF outside the schedule window (and honors the
 * override). So the boot path re-arms whenever the schedule could ever open
 * the mic — a weekday window that opens at 08:00 must be re-armed at a
 * 06:00 unlock, or the morning window would be silently lost (the exact
 * L1 failure this fixes). The gate, never this policy, decides microphone
 * access.
 *
 * A schedule is capture-capable when the manual override is on, or when it
 * has at least one enabled weekday with a real window (start != end makes
 * the window non-degenerate; an overnight window wraps past midnight).
 * No clock or zone is involved: capability depends only on the saved
 * schedule, which keeps the decision trivially testable on the JVM.
 */
object BootReArmPolicy {

    fun shouldRearm(schedule: WeeklyCaptureSchedule): Boolean {
        if (schedule.overrideEnabled) return true
        if (schedule.weekdays.isEmpty()) return false
        return schedule.startMinuteOfDay != schedule.endMinuteOfDay
    }
}