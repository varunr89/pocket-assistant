package com.varun.pocketassistant.ui

import android.text.format.DateFormat as AndroidDateFormat
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.varun.pocketassistant.PocketAssistantApp
import com.varun.pocketassistant.capture.WeeklyCaptureSchedule
import java.time.DayOfWeek
import kotlinx.coroutines.launch

/**
 * Settings → Capture schedule: the user-editable weekly capture window
 * (default weekdays 08:00-17:00) plus the manual "Mic on outside schedule"
 * override. Every control persists immediately to the Room settings row —
 * this screen is the settings persistence contract; the capture engine gate
 * reacts to the stored value regardless of which surface changed it.
 */
@Composable
fun CaptureScheduleSettingsSection() {
    val context = LocalContext.current
    val app = context.applicationContext as PocketAssistantApp
    val store = app.container.captureScheduleStore
    val scope = rememberCoroutineScope()
    val schedule by store.observe().collectAsState(initial = WeeklyCaptureSchedule())

    fun save(updated: WeeklyCaptureSchedule) {
        scope.launch { store.save(updated) }
    }

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Capture schedule", style = MaterialTheme.typography.titleLarge)
        Text(
            "The mic only records inside this window (${schedule.summary()}). " +
                "Outside it nothing is captured — battery and privacy. Toggle the " +
                "override below for one-off capture outside the window.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            DayOfWeek.entries.forEach { day ->
                FilterChip(
                    selected = day in schedule.weekdays,
                    onClick = {
                        val next = if (day in schedule.weekdays) {
                            schedule.weekdays - day
                        } else {
                            schedule.weekdays + day
                        }
                        save(schedule.copy(weekdays = next))
                    },
                    label = {
                        Text(day.name.take(3).lowercase().replaceFirstChar(Char::uppercase))
                    },
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            TimeOfDayField(
                label = "Start",
                minuteOfDay = schedule.startMinuteOfDay,
                modifier = Modifier.weight(1f),
                onChange = { save(schedule.copy(startMinuteOfDay = it)) },
            )
            TimeOfDayField(
                label = "End",
                minuteOfDay = schedule.endMinuteOfDay,
                modifier = Modifier.weight(1f),
                onChange = { save(schedule.copy(endMinuteOfDay = it)) },
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Column(modifier = Modifier.weight(1f).padding(end = 12.dp)) {
                Text("Mic on outside schedule", style = MaterialTheme.typography.titleMedium)
                Text(
                    "One-off capture outside the window while this is on. " +
                        "Toggling this is the only manual start/stop.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            Switch(
                checked = schedule.overrideEnabled,
                onCheckedChange = { save(schedule.copy(overrideEnabled = it)) },
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TimeOfDayField(
    label: String,
    minuteOfDay: Int,
    onChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    var show by remember { mutableStateOf(false) }

    Column(modifier = modifier) {
        Text(label, style = MaterialTheme.typography.labelLarge)
        OutlinedButton(onClick = { show = true }, modifier = Modifier.fillMaxWidth()) {
            Text("%02d:%02d".format(minuteOfDay / 60, minuteOfDay % 60))
        }
    }

    if (show) {
        val timeState = rememberTimePickerState(
            initialHour = minuteOfDay / 60,
            initialMinute = minuteOfDay % 60,
            is24Hour = AndroidDateFormat.is24HourFormat(context),
        )
        AlertDialog(
            onDismissRequest = { show = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        onChange(timeState.hour * 60 + timeState.minute)
                        show = false
                    },
                ) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { show = false }) { Text("Cancel") }
            },
            text = {
                Column(
                    modifier = Modifier.padding(top = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    TimePicker(state = timeState)
                }
            },
        )
    }
}