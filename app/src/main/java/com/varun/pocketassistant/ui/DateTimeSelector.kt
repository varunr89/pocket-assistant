package com.varun.pocketassistant.ui

import android.text.format.DateFormat as AndroidDateFormat
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import java.text.DateFormat
import java.util.Calendar
import java.util.Date
import java.util.TimeZone

/**
 * Date + time selectors for a local epoch millis value.
 * Material DatePicker uses UTC midnight; we convert carefully to local calendar fields.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateTimeSelector(
    label: String,
    epochMs: Long,
    onChange: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    var showDate by remember { mutableStateOf(false) }
    var showTime by remember { mutableStateOf(false) }
    val dateFmt = remember { DateFormat.getDateInstance(DateFormat.MEDIUM) }
    val timeFmt = remember { DateFormat.getTimeInstance(DateFormat.SHORT) }

    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(label, style = MaterialTheme.typography.titleLarge)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            FilledTonalButton(
                onClick = { showDate = true },
                modifier = Modifier.weight(1f),
            ) {
                Text(dateFmt.format(Date(epochMs)))
            }
            FilledTonalButton(
                onClick = { showTime = true },
                modifier = Modifier.weight(1f),
            ) {
                Text(timeFmt.format(Date(epochMs)))
            }
        }
    }

    if (showDate) {
        val dateState = rememberDatePickerState(
            initialSelectedDateMillis = localEpochToUtcDateMillis(epochMs),
        )
        DatePickerDialog(
            onDismissRequest = { showDate = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        val utcDay = dateState.selectedDateMillis
                        if (utcDay != null) {
                            onChange(combineUtcDateWithLocalTime(utcDay, epochMs))
                        }
                        showDate = false
                    },
                ) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { showDate = false }) { Text("Cancel") }
            },
        ) {
            DatePicker(state = dateState)
        }
    }

    if (showTime) {
        val cal = remember(epochMs) {
            Calendar.getInstance().apply { timeInMillis = epochMs }
        }
        val timeState = rememberTimePickerState(
            initialHour = cal.get(Calendar.HOUR_OF_DAY),
            initialMinute = cal.get(Calendar.MINUTE),
            is24Hour = AndroidDateFormat.is24HourFormat(context),
        )
        TimePickerDialog(
            onDismissRequest = { showTime = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        onChange(
                            withLocalTime(
                                epochMs,
                                timeState.hour,
                                timeState.minute,
                            ),
                        )
                        showTime = false
                    },
                ) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { showTime = false }) { Text("Cancel") }
            },
        ) {
            TimePicker(state = timeState)
        }
    }
}

@Composable
private fun TimePickerDialog(
    onDismissRequest: () -> Unit,
    confirmButton: @Composable () -> Unit,
    dismissButton: @Composable () -> Unit,
    content: @Composable ColumnScope.() -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = confirmButton,
        dismissButton = dismissButton,
        text = {
            Column(
                modifier = Modifier.padding(top = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                content = content,
            )
        },
    )
}

/** Midnight UTC for the local calendar date of [localEpochMs], as Material DatePicker expects. */
private fun localEpochToUtcDateMillis(localEpochMs: Long): Long {
    val local = Calendar.getInstance().apply { timeInMillis = localEpochMs }
    return Calendar.getInstance(TimeZone.getTimeZone("UTC")).apply {
        clear()
        set(Calendar.YEAR, local.get(Calendar.YEAR))
        set(Calendar.MONTH, local.get(Calendar.MONTH))
        set(Calendar.DAY_OF_MONTH, local.get(Calendar.DAY_OF_MONTH))
    }.timeInMillis
}

/** Apply Y/M/D from UTC date millis onto the local time-of-day from [keepTimeFromEpochMs]. */
private fun combineUtcDateWithLocalTime(utcDateMillis: Long, keepTimeFromEpochMs: Long): Long {
    val utc = Calendar.getInstance(TimeZone.getTimeZone("UTC")).apply {
        timeInMillis = utcDateMillis
    }
    val time = Calendar.getInstance().apply { timeInMillis = keepTimeFromEpochMs }
    return Calendar.getInstance().apply {
        set(Calendar.YEAR, utc.get(Calendar.YEAR))
        set(Calendar.MONTH, utc.get(Calendar.MONTH))
        set(Calendar.DAY_OF_MONTH, utc.get(Calendar.DAY_OF_MONTH))
        set(Calendar.HOUR_OF_DAY, time.get(Calendar.HOUR_OF_DAY))
        set(Calendar.MINUTE, time.get(Calendar.MINUTE))
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }.timeInMillis
}

private fun withLocalTime(epochMs: Long, hour: Int, minute: Int): Long {
    return Calendar.getInstance().apply {
        timeInMillis = epochMs
        set(Calendar.HOUR_OF_DAY, hour)
        set(Calendar.MINUTE, minute)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }.timeInMillis
}
