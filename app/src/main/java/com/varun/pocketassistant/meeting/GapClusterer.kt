package com.varun.pocketassistant.meeting

import com.varun.pocketassistant.data.MeetingRepository
import com.varun.pocketassistant.data.SegmentEntity
import java.util.UUID

/**
 * Groups uncategorized recordings into meeting proposals by time gaps.
 * Pure logic — no Android / Room dependencies.
 */
object GapClusterer {
    /** Start a new cluster when silence between clips exceeds this. */
    const val DEFAULT_GAP_MS = 20L * 60_000L

    /** Cap a single proposal so all-day capture does not become one mega-meeting. */
    const val DEFAULT_MAX_SPAN_MS = 3L * 60 * 60_000L

    data class Proposal(
        val id: String,
        val startMs: Long,
        val endMs: Long,
        val segmentIds: List<String>,
    )

    fun cluster(
        segments: List<SegmentEntity>,
        gapMs: Long = DEFAULT_GAP_MS,
        maxSpanMs: Long = DEFAULT_MAX_SPAN_MS,
    ): List<Proposal> {
        val sorted = segments
            .filter { MeetingRepository.isAssignableRecording(it) && it.meetingId == null }
            .sortedBy { it.startedAtMs }
        if (sorted.isEmpty()) return emptyList()

        val out = mutableListOf<Proposal>()
        var batch = mutableListOf(sorted.first())
        var batchStart = sorted.first().startedAtMs
        var batchEnd = sorted.first().endedAtMs

        fun flush() {
            if (batch.isEmpty()) return
            val ids = batch.map { it.id }
            out += Proposal(
                id = stableId(ids),
                startMs = batchStart,
                endMs = batchEnd,
                segmentIds = ids,
            )
            batch = mutableListOf()
        }

        for (i in 1 until sorted.size) {
            val seg = sorted[i]
            val gap = seg.startedAtMs - batchEnd
            val wouldSpan = seg.endedAtMs - batchStart
            if (gap > gapMs || wouldSpan > maxSpanMs) {
                flush()
                batchStart = seg.startedAtMs
                batchEnd = seg.endedAtMs
                batch.add(seg)
            } else {
                batch.add(seg)
                batchEnd = maxOf(batchEnd, seg.endedAtMs)
            }
        }
        flush()

        // Single-clip proposals are still useful if the clip is substantial (≥ 2 min).
        return out.filter { p ->
            p.segmentIds.size > 1 || (p.endMs - p.startMs) >= 120_000L
        }
    }

    private fun stableId(segmentIds: List<String>): String {
        val key = segmentIds.sorted().joinToString("|")
        return UUID.nameUUIDFromBytes(key.toByteArray()).toString()
    }
}
