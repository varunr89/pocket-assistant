package com.varun.pocketassistant.data

import com.varun.pocketassistant.capture.AudioStorage
import com.varun.pocketassistant.capture.RetentionPolicy
import com.varun.pocketassistant.pipeline.work.PipelineScheduler
import kotlinx.coroutines.flow.Flow
import java.io.File
import java.util.UUID

enum class SessionStatus {
    RECORDING,
    PAUSED,
    COMPLETED,
}

class SessionRepository(
    private val sessionDao: SessionDao,
    private val segmentDao: SegmentDao,
    private val audioStorage: AudioStorage,
    private val retentionPolicy: RetentionPolicy,
) {
    @Volatile
    var pipelineScheduler: PipelineScheduler? = null

    fun observeSessions(): Flow<List<SessionEntity>> = sessionDao.observeSessions()

    fun observeSegments(sessionId: String): Flow<List<SegmentEntity>> =
        segmentDao.observeForSession(sessionId)

    fun observeAllRecordings(): Flow<List<SegmentEntity>> = segmentDao.observeAll()

    fun observeUncategorizedRecordings(): Flow<List<SegmentEntity>> =
        segmentDao.observeUncategorized()

    suspend fun getSession(id: String): SessionEntity? = sessionDao.getById(id)

    suspend fun getSegments(sessionId: String): List<SegmentEntity> =
        segmentDao.getForSession(sessionId)

    suspend fun getSegment(id: String): SegmentEntity? = segmentDao.getById(id)

    suspend fun getPendingWork(limit: Int): List<SegmentEntity> {
        // Reclaim interrupted jobs left in PROCESSING after process death.
        segmentDao.getByTranscriptStatus(TranscriptStatus.PROCESSING.name, limit).forEach { seg ->
            segmentDao.update(
                seg.copy(
                    transcriptStatus = TranscriptStatus.PENDING.name,
                    updatedAtMs = System.currentTimeMillis(),
                ),
            )
        }
        return segmentDao.getNeedingAsr(
            TranscriptStatus.PENDING.name,
            TranscriptStatus.FAILED.name,
            limit,
        ).filter { it.skipReason == null }
    }

    suspend fun startSession(): SessionEntity {
        val existing = sessionDao.getByStatus(SessionStatus.RECORDING.name)
            ?: sessionDao.getByStatus(SessionStatus.PAUSED.name)
        if (existing != null) return existing

        val session = SessionEntity(
            id = UUID.randomUUID().toString(),
            startedAtMs = System.currentTimeMillis(),
            status = SessionStatus.RECORDING.name,
        )
        sessionDao.insert(session)
        audioStorage.ensureSessionDir(session.id)
        return session
    }

    suspend fun setStatus(sessionId: String, status: SessionStatus) {
        val current = sessionDao.getById(sessionId) ?: return
        sessionDao.update(
            current.copy(
                status = status.name,
                endedAtMs = if (status == SessionStatus.COMPLETED) {
                    System.currentTimeMillis()
                } else {
                    current.endedAtMs
                },
            ),
        )
    }

    suspend fun addSegment(
        sessionId: String,
        file: File,
        startedAtMs: Long,
        endedAtMs: Long,
    ): SegmentEntity {
        val now = System.currentTimeMillis()
        val segment = SegmentEntity(
            id = UUID.randomUUID().toString(),
            sessionId = sessionId,
            filePath = file.absolutePath,
            startedAtMs = startedAtMs,
            endedAtMs = endedAtMs,
            durationMs = endedAtMs - startedAtMs,
            byteSize = file.length(),
            transcriptStatus = TranscriptStatus.PENDING.name,
            updatedAtMs = now,
        )
        segmentDao.insert(segment)

        val session = sessionDao.getById(sessionId)
        if (session != null) {
            sessionDao.update(
                session.copy(
                    segmentCount = session.segmentCount + 1,
                    speechDurationMs = session.speechDurationMs + segment.durationMs,
                ),
            )
        }
        pipelineScheduler?.enqueueAsr(segment.id)
        return segment
    }

    suspend fun updateTranscript(
        segmentId: String,
        status: TranscriptStatus,
        transcript: String? = null,
        diarized: String? = null,
        cleaned: String? = null,
        asrProvider: String? = null,
        cleanupProvider: String? = null,
        asrLastError: String? = null,
        skipReason: String? = null,
        clearAsrError: Boolean = false,
    ) {
        val current = segmentDao.getById(segmentId) ?: return
        segmentDao.update(
            current.copy(
                transcriptStatus = status.name,
                transcript = transcript ?: current.transcript,
                diarizedTranscript = diarized ?: current.diarizedTranscript,
                cleanedTranscript = cleaned ?: current.cleanedTranscript,
                asrProvider = asrProvider ?: current.asrProvider,
                cleanupProvider = cleanupProvider ?: current.cleanupProvider,
                asrLastError = when {
                    clearAsrError -> null
                    asrLastError != null -> asrLastError
                    else -> current.asrLastError
                },
                skipReason = skipReason ?: current.skipReason,
                updatedAtMs = System.currentTimeMillis(),
            ),
        )
    }

    suspend fun requeueAllForAsr() {
        val segs = segmentDao.getNeedingAsr(
            TranscriptStatus.PENDING.name,
            TranscriptStatus.FAILED.name,
            100,
        ) + segmentDao.getByTranscriptStatus(TranscriptStatus.READY.name, 100) +
            segmentDao.getByTranscriptStatus(TranscriptStatus.CLEANED.name, 100) +
            segmentDao.getByTranscriptStatus(TranscriptStatus.CLEAN_FAILED.name, 100) +
            segmentDao.getByTranscriptStatus(TranscriptStatus.PROCESSING.name, 100)
        segs.distinctBy { it.id }.forEach { seg ->
            if (seg.skipReason != null) return@forEach
            segmentDao.update(
                seg.copy(
                    transcriptStatus = TranscriptStatus.PENDING.name,
                    transcript = null,
                    diarizedTranscript = null,
                    cleanedTranscript = null,
                    asrProvider = null,
                    cleanupProvider = null,
                    asrLastError = null,
                    updatedAtMs = System.currentTimeMillis(),
                ),
            )
            pipelineScheduler?.enqueueAsr(seg.id)
        }
    }

    /** Reset one recording and enqueue ASR again (e.g. after a failed transcription). */
    suspend fun requeueForAsr(segmentId: String): Boolean {
        val seg = segmentDao.getById(segmentId) ?: return false
        if (seg.skipReason != null) return false
        segmentDao.update(
            seg.copy(
                transcriptStatus = TranscriptStatus.PENDING.name,
                transcript = null,
                diarizedTranscript = null,
                cleanedTranscript = null,
                asrProvider = null,
                cleanupProvider = null,
                asrLastError = null,
                updatedAtMs = System.currentTimeMillis(),
            ),
        )
        pipelineScheduler?.enqueueAsr(segmentId, replace = true)
        return true
    }

    suspend fun deleteSession(sessionId: String) {
        segmentDao.deleteForSession(sessionId)
        sessionDao.delete(sessionId)
        audioStorage.deleteSessionDir(sessionId)
    }

    suspend fun applyRetention() {
        val cutoff = retentionPolicy.cutoffEpochMs(System.currentTimeMillis())
        sessionDao.getEndedBefore(cutoff).forEach { deleteSession(it.id) }
    }
}
