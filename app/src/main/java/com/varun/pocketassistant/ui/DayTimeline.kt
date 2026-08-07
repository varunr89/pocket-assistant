package com.varun.pocketassistant.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.varun.pocketassistant.data.MeetingEntity
import com.varun.pocketassistant.data.SegmentEntity
import com.varun.pocketassistant.meeting.GapClusterer
import java.text.DateFormat
import java.util.Calendar
import java.util.Date
import java.util.concurrent.TimeUnit
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

private val HOUR_HEIGHT = 56.dp
private const val DAY_START_HOUR = 6
private const val DAY_END_HOUR = 23

@Composable
fun DayTimelinePanel(
    dayStartMs: Long,
    segments: List<SegmentEntity>,
    meetings: List<MeetingEntity>,
    proposals: List<GapClusterer.Proposal>,
    selectionStartMs: Long?,
    selectionEndMs: Long?,
    onPrevDay: () -> Unit,
    onNextDay: () -> Unit,
    onToday: () -> Unit,
    onSuggest: () -> Unit,
    onAcceptProposal: (GapClusterer.Proposal) -> Unit,
    onDismissProposal: (GapClusterer.Proposal) -> Unit,
    onOpenMeeting: (String) -> Unit,
    onSelectionChange: (Long?, Long?) -> Unit,
    onCreateFromSelection: () -> Unit,
    onAssignSegment: (segmentId: String, meetingId: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val dayFmt = remember { DateFormat.getDateInstance(DateFormat.MEDIUM) }
    val timeFmt = remember { DateFormat.getTimeInstance(DateFormat.SHORT) }
    val visibleStart = dayStartMs + TimeUnit.HOURS.toMillis(DAY_START_HOUR.toLong())
    val visibleEnd = dayStartMs + TimeUnit.HOURS.toMillis(DAY_END_HOUR.toLong())
    val hours = DAY_END_HOUR - DAY_START_HOUR
    val totalHeight = HOUR_HEIGHT * hours

    Column(modifier = modifier.fillMaxWidth()) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            TextButton(onClick = onPrevDay) { Text("‹") }
            Text(
                dayFmt.format(Date(dayStartMs)),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.weight(1f),
            )
            TextButton(onClick = onToday) { Text("Today") }
            TextButton(onClick = onNextDay) { Text("›") }
        }
        Row {
            OutlinedButton(onClick = onSuggest, modifier = Modifier.weight(1f)) {
                Text("Suggest meetings")
            }
            if (selectionStartMs != null && selectionEndMs != null &&
                abs(selectionEndMs - selectionStartMs) >= 60_000L
            ) {
                Spacer(Modifier.width(8.dp))
                OutlinedButton(onClick = onCreateFromSelection, modifier = Modifier.weight(1f)) {
                    Text("Create from selection")
                }
            }
        }
        if (proposals.isNotEmpty()) {
            Spacer(Modifier.height(8.dp))
            proposals.forEach { p ->
                ProposalCard(
                    proposal = p,
                    timeFmt = timeFmt,
                    onAccept = { onAcceptProposal(p) },
                    onDismiss = { onDismissProposal(p) },
                )
                Spacer(Modifier.height(6.dp))
            }
        }
        Spacer(Modifier.height(8.dp))
        Text(
            "Drag on the left time gutter to select a range. Tap a meeting band to open it; " +
                "tap an unassigned clip to attach it to the overlapping meeting.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(Modifier.height(8.dp))

        val scroll = rememberScrollState()
        val density = LocalDensity.current
        var dragAnchor by remember { mutableStateOf<Long?>(null) }
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
                .height(totalHeight)
                .verticalScroll(scroll)
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f)),
        ) {
            val widthPx = constraints.maxWidth.toFloat()
            val hourPx = with(density) { HOUR_HEIGHT.toPx() }
            val totalPx = hourPx * hours

            fun yFor(ms: Long): Float {
                val clamped = ms.coerceIn(visibleStart, visibleEnd)
                return ((clamped - visibleStart).toFloat() / (visibleEnd - visibleStart)) * totalPx
            }

            fun msFor(y: Float): Long {
                val t = (y / totalPx).coerceIn(0f, 1f)
                return visibleStart + ((visibleEnd - visibleStart) * t).toLong()
            }

            for (h in 0 until hours) {
                val top = HOUR_HEIGHT * h
                Box(
                    Modifier
                        .offset(y = top)
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
                )
                Text(
                    String.format("%02d:00", DAY_START_HOUR + h),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier
                        .offset(y = top + 2.dp)
                        .padding(start = 4.dp),
                )
            }

            meetings.forEach { m ->
                val top = yFor(max(m.startedAtMs, visibleStart))
                val bottom = yFor(min(m.endedAtMs, visibleEnd))
                val h = max(bottom - top, with(density) { 12.dp.toPx() })
                Box(
                    Modifier
                        .offset(x = 48.dp, y = with(density) { top.toDp() })
                        .width(with(density) { (widthPx * 0.55f).toDp() })
                        .height(with(density) { h.toDp() })
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.18f))
                        .clickable { onOpenMeeting(m.id) }
                        .padding(6.dp),
                ) {
                    Text(
                        m.title?.takeIf { it.isNotBlank() } ?: "Meeting",
                        style = MaterialTheme.typography.labelMedium,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        color = MaterialTheme.colorScheme.primary,
                    )
                }
            }

            proposals.forEach { p ->
                val top = yFor(max(p.startMs, visibleStart))
                val bottom = yFor(min(p.endMs, visibleEnd))
                val h = max(bottom - top, with(density) { 12.dp.toPx() })
                Box(
                    Modifier
                        .offset(x = 48.dp, y = with(density) { top.toDp() })
                        .width(with(density) { (widthPx * 0.55f).toDp() })
                        .height(with(density) { h.toDp() })
                        .clip(RoundedCornerShape(8.dp))
                        .border(1.dp, MaterialTheme.colorScheme.tertiary, RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.tertiary.copy(alpha = 0.10f))
                        .padding(6.dp),
                ) {
                    Text(
                        "Suggested · ${p.segmentIds.size} clips",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.tertiary,
                    )
                }
            }

            segments.forEach { seg ->
                val top = yFor(max(seg.startedAtMs, visibleStart))
                val bottom = yFor(min(seg.endedAtMs, visibleEnd))
                val h = max(bottom - top, with(density) { 10.dp.toPx() })
                val nearestMeeting = meetings
                    .filter { seg.startedAtMs < it.endedAtMs && seg.endedAtMs > it.startedAtMs }
                    .minByOrNull {
                        abs(
                            (it.startedAtMs + it.endedAtMs) / 2 -
                                (seg.startedAtMs + seg.endedAtMs) / 2,
                        )
                    }
                Box(
                    Modifier
                        .offset(
                            x = with(density) { (widthPx * 0.62f).toDp() },
                            y = with(density) { top.toDp() },
                        )
                        .width(with(density) { (widthPx * 0.35f).toDp() })
                        .height(with(density) { h.toDp() })
                        .clip(RoundedCornerShape(6.dp))
                        .background(segmentColor(seg))
                        .clickable {
                            when {
                                seg.meetingId != null -> onOpenMeeting(seg.meetingId!!)
                                nearestMeeting != null -> onAssignSegment(seg.id, nearestMeeting.id)
                            }
                        }
                        .padding(4.dp),
                ) {
                    Text(
                        formatDuration(seg.durationMs),
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White,
                        maxLines = 1,
                    )
                }
            }

            if (selectionStartMs != null && selectionEndMs != null) {
                val a = min(selectionStartMs, selectionEndMs)
                val b = max(selectionStartMs, selectionEndMs)
                val top = yFor(max(a, visibleStart))
                val bottom = yFor(min(b, visibleEnd))
                Box(
                    Modifier
                        .offset(x = 48.dp, y = with(density) { top.toDp() })
                        .width(with(density) { (widthPx - with(density) { 48.dp.toPx() }).toDp() })
                        .height(with(density) { max(bottom - top, 4f).toDp() })
                        .background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.25f)),
                )
            }

            Box(
                Modifier
                    .width(48.dp)
                    .height(totalHeight)
                    .pointerInput(dayStartMs) {
                        detectDragGestures(
                            onDragStart = { offset ->
                                val ms = msFor(offset.y)
                                dragAnchor = ms
                                onSelectionChange(ms, ms)
                            },
                            onDrag = { change, _ ->
                                change.consume()
                                val anchor = dragAnchor ?: return@detectDragGestures
                                onSelectionChange(anchor, msFor(change.position.y))
                            },
                            onDragEnd = { dragAnchor = null },
                            onDragCancel = {
                                dragAnchor = null
                                onSelectionChange(null, null)
                            },
                        )
                    },
            )
        }
    }
}

@Composable
private fun ProposalCard(
    proposal: GapClusterer.Proposal,
    timeFmt: DateFormat,
    onAccept: () -> Unit,
    onDismiss: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.5f))
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                "${timeFmt.format(Date(proposal.startMs))} – ${timeFmt.format(Date(proposal.endMs))}",
                style = MaterialTheme.typography.titleSmall,
            )
            Text(
                "${proposal.segmentIds.size} clips · ${formatDuration(proposal.endMs - proposal.startMs)}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        TextButton(onClick = onDismiss) { Text("Dismiss") }
        TextButton(onClick = onAccept) { Text("Accept") }
    }
}

@Composable
private fun segmentColor(seg: SegmentEntity): Color {
    val scheme = MaterialTheme.colorScheme
    return when (seg.transcriptStatus) {
        "READY", "CLEANED" -> scheme.primary
        "FAILED", "CLEAN_FAILED" -> scheme.error
        "PENDING", "PROCESSING" -> scheme.secondary
        else -> scheme.outline
    }
}

private fun formatDuration(ms: Long): String {
    val totalSec = TimeUnit.MILLISECONDS.toSeconds(ms.coerceAtLeast(0))
    val m = totalSec / 60
    val s = totalSec % 60
    return if (m >= 60) {
        val h = m / 60
        "%dh %02dm".format(h, m % 60)
    } else {
        "%d:%02d".format(m, s)
    }
}

fun startOfDayMs(epochMs: Long = System.currentTimeMillis()): Long {
    val cal = Calendar.getInstance()
    cal.timeInMillis = epochMs
    cal.set(Calendar.HOUR_OF_DAY, 0)
    cal.set(Calendar.MINUTE, 0)
    cal.set(Calendar.SECOND, 0)
    cal.set(Calendar.MILLISECOND, 0)
    return cal.timeInMillis
}
