package com.varun.pocketassistant.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.varun.pocketassistant.capture.CapturePhase
import com.varun.pocketassistant.capture.RmsSample

@Composable
fun RmsHistoryGraph(
    samples: List<RmsSample>,
    threshold: Float,
    modifier: Modifier = Modifier,
) {
    val buffer = Color(0xFF8FA8A3)
    val preroll = Color(0xFFC9852A)
    val live = Color(0xFF1F6F70)
    val postroll = Color(0xFFB85C38)
    val idle = Color(0xFFD0D8D4)
    val line = Color(0xFF1A2422)
    val thresh = Color(0xFFB3261E)

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(140.dp),
    ) {
        val w = size.width
        val h = size.height
        if (w <= 0f || h <= 0f) return@Canvas

        val now = samples.lastOrNull()?.atMs ?: System.currentTimeMillis()
        val windowMs = 60_000f
        val startMs = now - windowMs.toLong()
        val maxRms = maxOf(threshold * 2.5f, samples.maxOfOrNull { it.rms } ?: threshold, 1f)

        fun xFor(t: Long): Float = ((t - startMs).toFloat() / windowMs).coerceIn(0f, 1f) * w
        fun yFor(rms: Float): Float = h - (rms / maxRms).coerceIn(0f, 1f) * h

        // Phase background bands between consecutive samples.
        if (samples.size >= 2) {
            for (i in 0 until samples.lastIndex) {
                val a = samples[i]
                val b = samples[i + 1]
                val left = xFor(a.atMs)
                val right = xFor(b.atMs)
                if (right <= left) continue
                val color = when (a.phase) {
                    CapturePhase.LISTENING -> buffer.copy(alpha = 0.22f)
                    CapturePhase.PRE_ROLL_FLUSH -> preroll.copy(alpha = 0.28f)
                    CapturePhase.LIVE_SPEECH -> live.copy(alpha = 0.28f)
                    CapturePhase.POST_ROLL -> postroll.copy(alpha = 0.28f)
                    else -> idle.copy(alpha = 0.12f)
                }
                drawRect(
                    color = color,
                    topLeft = Offset(left, 0f),
                    size = Size(right - left, h),
                )
            }
        }

        // Threshold guide.
        val ty = yFor(threshold)
        drawLine(
            color = thresh.copy(alpha = 0.7f),
            start = Offset(0f, ty),
            end = Offset(w, ty),
            strokeWidth = 2f,
        )

        if (samples.size < 2) return@Canvas

        val path = Path()
        samples.forEachIndexed { index, sample ->
            val x = xFor(sample.atMs)
            val y = yFor(sample.rms)
            if (index == 0) path.moveTo(x, y) else path.lineTo(x, y)
        }
        drawPath(
            path = path,
            color = line,
            style = Stroke(width = 3f, cap = StrokeCap.Round),
        )
    }
}

fun phaseColor(phase: CapturePhase): Color = when (phase) {
    CapturePhase.LISTENING -> Color(0xFF8FA8A3)
    CapturePhase.PRE_ROLL_FLUSH -> Color(0xFFC9852A)
    CapturePhase.LIVE_SPEECH -> Color(0xFF1F6F70)
    CapturePhase.POST_ROLL -> Color(0xFFB85C38)
    CapturePhase.PAUSED -> Color(0xFFB85C38)
    CapturePhase.SCHEDULED_OFF -> Color(0xFFD0D8D4)
    CapturePhase.IDLE -> Color(0xFFD0D8D4)
}
