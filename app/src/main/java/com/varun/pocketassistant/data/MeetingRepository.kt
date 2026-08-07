package com.varun.pocketassistant.data

import com.varun.pocketassistant.pipeline.work.PipelineScheduler
import kotlinx.coroutines.flow.Flow
import java.util.UUID

class MeetingRepository(
    private val meetingDao: MeetingDao,
    private val segmentDao: SegmentDao,
) {
    @Volatile
    var pipelineScheduler: PipelineScheduler? = null

    fun observeMeetings(): Flow<List<MeetingEntity>> = meetingDao.observeAll()

    fun observeMeeting(id: String): Flow<MeetingEntity?> = meetingDao.observeById(id)

    fun searchMeetings(query: String): Flow<List<MeetingEntity>> {
        val q = query.trim()
        return if (q.isEmpty()) meetingDao.observeAll() else meetingDao.search(q)
    }

    fun observeMeetingRecordings(meetingId: String): Flow<List<SegmentEntity>> =
        segmentDao.observeForMeeting(meetingId)

    suspend fun getMeeting(id: String): MeetingEntity? = meetingDao.getById(id)

    suspend fun getRecordings(meetingId: String): List<SegmentEntity> =
        segmentDao.getForMeeting(meetingId)

    /** Segments whose time range overlaps [startMs, endMs). */
    suspend fun getSegmentsOverlapping(startMs: Long, endMs: Long): List<SegmentEntity> =
        segmentDao.getOverlapping(startMs, endMs)

    /** Meetings whose range overlaps [startMs, endMs). */
    suspend fun getMeetingsOverlapping(startMs: Long, endMs: Long): List<MeetingEntity> =
        meetingDao.getAll(500).filter { it.startedAtMs < endMs && it.endedAtMs > startMs }
            .sortedBy { it.startedAtMs }

    suspend fun previewOverlap(startMs: Long, endMs: Long): OverlapPreview {
        val segs = segmentDao.getOverlapping(startMs, endMs)
            .filter { isAssignableRecording(it) }
        val ready = segs.filter { it.transcriptStatus == TranscriptStatus.READY.name }
        val pendingAsr = segs.filter {
            it.transcriptStatus == TranscriptStatus.PENDING.name ||
                it.transcriptStatus == TranscriptStatus.PROCESSING.name
        }
        return OverlapPreview(
            recordings = segs,
            count = segs.size,
            speechDurationMs = segs.sumOf { it.durationMs },
            readyCount = ready.size,
            readyDurationMs = ready.sumOf { it.durationMs },
            pendingAsrCount = pendingAsr.size,
        )
    }

    suspend fun createMeeting(startMs: Long, endMs: Long): MeetingEntity {
        require(endMs > startMs) { "Meeting end must be after start" }
        val now = System.currentTimeMillis()
        val meeting = MeetingEntity(
            id = UUID.randomUUID().toString(),
            startedAtMs = startMs,
            endedAtMs = endMs,
            status = MeetingStatus.PENDING_CLEANUP.name,
            createdAtMs = now,
            updatedAtMs = now,
        )
        meetingDao.insert(meeting)
        assignOverlapping(meeting.id, startMs, endMs)
        pipelineScheduler?.enqueueMeeting(meeting.id)
        return meeting
    }

    suspend fun updateMeetingRange(meetingId: String, startMs: Long, endMs: Long): MeetingEntity? {
        require(endMs > startMs) { "Meeting end must be after start" }
        val current = meetingDao.getById(meetingId) ?: return null
        segmentDao.clearMeeting(meetingId)
        val updated = current.copy(
            startedAtMs = startMs,
            endedAtMs = endMs,
            title = null,
            cleanedTranscript = null,
            cleanTextOnly = null,
            metadataJson = null,
            cleanupProvider = null,
            lastError = null,
            status = MeetingStatus.PENDING_CLEANUP.name,
            updatedAtMs = System.currentTimeMillis(),
        )
        meetingDao.update(updated)
        assignOverlapping(meetingId, startMs, endMs)
        pipelineScheduler?.enqueueMeeting(meetingId, replace = true)
        return updated
    }

    suspend fun deleteMeeting(meetingId: String) {
        segmentDao.clearMeeting(meetingId)
        meetingDao.delete(meetingId)
    }

    suspend fun assignRecording(segmentId: String, meetingId: String) {
        val segment = segmentDao.getById(segmentId) ?: return
        val previousMeetingId = segment.meetingId
        segmentDao.setMeetingId(segmentId, meetingId)
        if (previousMeetingId != null && previousMeetingId != meetingId) {
            markMeetingNeedsCleanup(previousMeetingId)
        }
        markMeetingNeedsCleanup(meetingId)
    }

    suspend fun unassignRecording(segmentId: String) {
        val segment = segmentDao.getById(segmentId) ?: return
        val previousMeetingId = segment.meetingId ?: return
        segmentDao.clearMeetingId(segmentId)
        markMeetingNeedsCleanup(previousMeetingId)
    }

    suspend fun deleteRecording(segmentId: String) {
        val segment = segmentDao.getById(segmentId) ?: return
        val previousMeetingId = segment.meetingId
        segmentDao.deleteById(segmentId)
        runCatching { java.io.File(segment.filePath).delete() }
        if (previousMeetingId != null) {
            markMeetingNeedsCleanup(previousMeetingId)
        }
    }

    suspend fun retryCleanup(meetingId: String) {
        val meeting = meetingDao.getById(meetingId) ?: return
        // Keep cleanTextOnly when present so summary-only retry can skip cleanup.
        val keepClean = !meeting.cleanTextOnly.isNullOrBlank()
        meetingDao.update(
            meeting.copy(
                status = MeetingStatus.PENDING_CLEANUP.name,
                title = if (keepClean) meeting.title else null,
                cleanedTranscript = if (keepClean) meeting.cleanedTranscript else null,
                cleanTextOnly = meeting.cleanTextOnly,
                metadataJson = if (keepClean) meeting.metadataJson else null,
                cleanupProvider = if (keepClean) meeting.cleanupProvider else null,
                lastError = null,
                updatedAtMs = System.currentTimeMillis(),
            ),
        )
        if (keepClean) {
            pipelineScheduler?.enqueueMeetingSummary(meetingId, replace = true)
        } else {
            pipelineScheduler?.enqueueMeeting(meetingId, replace = true)
        }
    }

    private suspend fun markMeetingNeedsCleanup(meetingId: String) {
        val meeting = meetingDao.getById(meetingId) ?: return
        meetingDao.update(
            meeting.copy(
                status = MeetingStatus.PENDING_CLEANUP.name,
                title = null,
                cleanedTranscript = null,
                cleanTextOnly = null,
                metadataJson = null,
                cleanupProvider = null,
                lastError = null,
                updatedAtMs = System.currentTimeMillis(),
            ),
        )
        pipelineScheduler?.enqueueMeeting(meetingId, replace = true)
    }

    suspend fun getPendingCleanup(limit: Int): List<MeetingEntity> =
        meetingDao.getNeedingCleanup(
            MeetingStatus.PENDING_CLEANUP.name,
            MeetingStatus.FAILED.name,
            limit,
        )

    /**
     * Reset existing meetings so cleanup/summary runs again with current pipeline settings.
     * Includes meetings stuck in CLEANING (e.g. process killed mid-run).
     */
    suspend fun requeueAllForCleanup(limit: Int = 100): List<String> {
        val meetings = meetingDao.getAll(limit)
        val ids = mutableListOf<String>()
        val now = System.currentTimeMillis()
        for (meeting in meetings) {
            meetingDao.update(
                meeting.copy(
                    status = MeetingStatus.PENDING_CLEANUP.name,
                    title = null,
                    cleanedTranscript = null,
                    cleanTextOnly = null,
                    metadataJson = null,
                    cleanupProvider = null,
                    lastError = null,
                    updatedAtMs = now,
                ),
            )
            ids += meeting.id
        }
        return ids
    }

    /** Recover meetings left in CLEANING after a crash / force-stop. */
    suspend fun recoverStuckCleaning(): List<String> {
        val stuck = meetingDao.getAll(200).filter { it.status == MeetingStatus.CLEANING.name }
        val now = System.currentTimeMillis()
        for (meeting in stuck) {
            meetingDao.update(
                meeting.copy(
                    status = MeetingStatus.PENDING_CLEANUP.name,
                    updatedAtMs = now,
                ),
            )
        }
        return stuck.map { it.id }
    }

    suspend fun updateMeeting(meeting: MeetingEntity) {
        meetingDao.update(meeting.copy(updatedAtMs = System.currentTimeMillis()))
    }

    /** True when every linked recording is done with ASR (ready, skipped, or failed). */
    suspend fun allRecordingsAsrSettled(meetingId: String): Boolean {
        val recordings = segmentDao.getForMeeting(meetingId)
        if (recordings.isEmpty()) return true
        return recordings.none {
            it.transcriptStatus == TranscriptStatus.PENDING.name ||
                it.transcriptStatus == TranscriptStatus.PROCESSING.name
        }
    }

    private suspend fun assignOverlapping(meetingId: String, startMs: Long, endMs: Long) {
        val ids = segmentDao.getOverlapping(startMs, endMs)
            .filter { isAssignableRecording(it) }
            .map { it.id }
        if (ids.isNotEmpty()) {
            segmentDao.assignMeeting(meetingId, ids)
        }
    }

    companion object {
        fun isAssignableRecording(segment: SegmentEntity): Boolean {
            if (segment.transcriptStatus == TranscriptStatus.SKIPPED_SILENCE.name) return false
            if (segment.skipReason == SkipReason.SUPERSEDED) return false
            // Prefer split parts over a giant parent WAV still sitting in the same range.
            val name = segment.filePath.substringAfterLast('/')
            if (name.matches(Regex("""speech_\d+\.wav""")) && segment.durationMs > 180_000L) {
                return false
            }
            return true
        }
    }
}

data class OverlapPreview(
    val recordings: List<SegmentEntity>,
    val count: Int,
    val speechDurationMs: Long,
    val readyCount: Int = 0,
    val readyDurationMs: Long = 0L,
    val pendingAsrCount: Int = 0,
)
