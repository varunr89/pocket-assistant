package com.varun.pocketassistant.capture

import com.varun.pocketassistant.data.SettingsDao
import com.varun.pocketassistant.data.SettingsEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Room-backed persistence for the weekly capture schedule + override flag.
 * Backed by the DB v9 `settings` key-value table (one row per key); the
 * schedule is stored as a single encoded string under [KEY_CAPTURE_SCHEDULE].
 * Until the user edits it, the store reads back the spec defaults.
 */
class CaptureScheduleStore(private val settingsDao: SettingsDao) {

    fun observe(): Flow<WeeklyCaptureSchedule> =
        settingsDao.observeValue(KEY_CAPTURE_SCHEDULE).map { raw ->
            WeeklyCaptureSchedule.parse(raw)
        }

    suspend fun load(): WeeklyCaptureSchedule =
        WeeklyCaptureSchedule.parse(settingsDao.getValue(KEY_CAPTURE_SCHEDULE))

    suspend fun save(schedule: WeeklyCaptureSchedule) {
        settingsDao.upsert(
            SettingsEntity(key = KEY_CAPTURE_SCHEDULE, value = schedule.encode()),
        )
    }

    /** Toggle the manual "mic on outside schedule" override. */
    suspend fun setOverride(enabled: Boolean) {
        save(load().copy(overrideEnabled = enabled))
    }

    companion object {
        private const val KEY_CAPTURE_SCHEDULE = "capture_schedule"
    }
}