package com.varun.pocketassistant.data

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.RoomDatabase
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "sessions")
data class SessionEntity(
    @PrimaryKey val id: String,
    val startedAtMs: Long,
    val endedAtMs: Long? = null,
    val status: String,
    val segmentCount: Int = 0,
    val speechDurationMs: Long = 0L,
    val notes: String? = null,
)

@Entity(tableName = "segments")
data class SegmentEntity(
    @PrimaryKey val id: String,
    val sessionId: String,
    val filePath: String,
    val startedAtMs: Long,
    val endedAtMs: Long,
    val durationMs: Long,
    val byteSize: Long,
    val transcriptStatus: String = TranscriptStatus.PENDING.name,
    val transcript: String? = null,
    val diarizedTranscript: String? = null,
    val cleanedTranscript: String? = null,
    val asrProvider: String? = null,
    val cleanupProvider: String? = null,
    val meetingId: String? = null,
)

@Entity(tableName = "meetings")
data class MeetingEntity(
    @PrimaryKey val id: String,
    val title: String? = null,
    val startedAtMs: Long,
    val endedAtMs: Long,
    val status: String = MeetingStatus.PENDING_CLEANUP.name,
    val cleanedTranscript: String? = null,
    val metadataJson: String? = null,
    val cleanupProvider: String? = null,
    val createdAtMs: Long = System.currentTimeMillis(),
)

enum class TranscriptStatus {
    PENDING,
    PROCESSING,
    READY,
    CLEANING,
    CLEANED,
    FAILED,
    SKIPPED_SILENCE,
    CLEAN_FAILED,
}

enum class MeetingStatus {
    PENDING_CLEANUP,
    CLEANING,
    READY,
    FAILED,
}

@Dao
interface SessionDao {
    @Query("SELECT * FROM sessions ORDER BY startedAtMs DESC")
    fun observeSessions(): Flow<List<SessionEntity>>

    @Query("SELECT * FROM sessions WHERE id = :id")
    suspend fun getById(id: String): SessionEntity?

    @Query("SELECT * FROM sessions WHERE status = :status LIMIT 1")
    suspend fun getByStatus(status: String): SessionEntity?

    @Insert
    suspend fun insert(session: SessionEntity)

    @Update
    suspend fun update(session: SessionEntity)

    @Query("DELETE FROM sessions WHERE id = :id")
    suspend fun delete(id: String)

    @Query("SELECT * FROM sessions WHERE endedAtMs IS NOT NULL AND endedAtMs < :cutoffMs")
    suspend fun getEndedBefore(cutoffMs: Long): List<SessionEntity>
}

@Dao
interface SegmentDao {
    @Query("SELECT * FROM segments WHERE sessionId = :sessionId ORDER BY startedAtMs ASC")
    fun observeForSession(sessionId: String): Flow<List<SegmentEntity>>

    @Query("SELECT * FROM segments WHERE sessionId = :sessionId ORDER BY startedAtMs ASC")
    suspend fun getForSession(sessionId: String): List<SegmentEntity>

    @Query("SELECT * FROM segments ORDER BY startedAtMs DESC")
    fun observeAll(): Flow<List<SegmentEntity>>

    @Query(
        """
        SELECT * FROM segments
        WHERE meetingId IS NULL
        ORDER BY startedAtMs DESC
        """,
    )
    fun observeUncategorized(): Flow<List<SegmentEntity>>

    @Query("SELECT * FROM segments ORDER BY startedAtMs DESC")
    suspend fun getAll(): List<SegmentEntity>

    @Query(
        """
        SELECT * FROM segments
        WHERE startedAtMs < :endMs AND endedAtMs > :startMs
        ORDER BY startedAtMs ASC
        """,
    )
    suspend fun getOverlapping(startMs: Long, endMs: Long): List<SegmentEntity>

    @Query("SELECT * FROM segments WHERE meetingId = :meetingId ORDER BY startedAtMs ASC")
    fun observeForMeeting(meetingId: String): Flow<List<SegmentEntity>>

    @Query("SELECT * FROM segments WHERE meetingId = :meetingId ORDER BY startedAtMs ASC")
    suspend fun getForMeeting(meetingId: String): List<SegmentEntity>

    @Query("UPDATE segments SET meetingId = :meetingId WHERE id IN (:ids)")
    suspend fun assignMeeting(meetingId: String, ids: List<String>)

    @Query("UPDATE segments SET meetingId = :meetingId WHERE id = :id")
    suspend fun setMeetingId(id: String, meetingId: String)

    @Query("UPDATE segments SET meetingId = NULL WHERE id = :id")
    suspend fun clearMeetingId(id: String)

    @Query("UPDATE segments SET meetingId = NULL WHERE meetingId = :meetingId")
    suspend fun clearMeeting(meetingId: String)

    @Query("DELETE FROM segments WHERE id = :id")
    suspend fun deleteById(id: String)

    @Query("SELECT * FROM segments WHERE transcriptStatus = :status ORDER BY startedAtMs ASC LIMIT :limit")
    suspend fun getByTranscriptStatus(status: String, limit: Int = 5): List<SegmentEntity>

    @Query(
        """
        SELECT * FROM segments
        WHERE transcriptStatus IN (:a, :b)
        ORDER BY startedAtMs ASC
        LIMIT :limit
        """,
    )
    suspend fun getNeedingAsr(a: String, b: String, limit: Int = 10): List<SegmentEntity>

    @Query("SELECT * FROM segments WHERE id = :id")
    suspend fun getById(id: String): SegmentEntity?

    @Insert
    suspend fun insert(segment: SegmentEntity)

    @Update
    suspend fun update(segment: SegmentEntity)

    @Query("DELETE FROM segments WHERE sessionId = :sessionId")
    suspend fun deleteForSession(sessionId: String)
}

@Dao
interface MeetingDao {
    @Query("SELECT * FROM meetings ORDER BY startedAtMs DESC")
    fun observeAll(): Flow<List<MeetingEntity>>

    @Query(
        """
        SELECT * FROM meetings
        WHERE title LIKE '%' || :q || '%'
           OR metadataJson LIKE '%' || :q || '%'
           OR cleanedTranscript LIKE '%' || :q || '%'
        ORDER BY startedAtMs DESC
        """,
    )
    fun search(q: String): Flow<List<MeetingEntity>>

    @Query("SELECT * FROM meetings WHERE id = :id")
    suspend fun getById(id: String): MeetingEntity?

    @Query("SELECT * FROM meetings WHERE id = :id")
    fun observeById(id: String): Flow<MeetingEntity?>

    @Query("SELECT * FROM meetings WHERE status IN (:a, :b) ORDER BY createdAtMs ASC LIMIT :limit")
    suspend fun getNeedingCleanup(a: String, b: String, limit: Int = 10): List<MeetingEntity>

    @Query("SELECT * FROM meetings ORDER BY createdAtMs ASC LIMIT :limit")
    suspend fun getAll(limit: Int = 100): List<MeetingEntity>

    @Insert
    suspend fun insert(meeting: MeetingEntity)

    @Update
    suspend fun update(meeting: MeetingEntity)

    @Query("DELETE FROM meetings WHERE id = :id")
    suspend fun delete(id: String)
}

@Database(
    entities = [SessionEntity::class, SegmentEntity::class, MeetingEntity::class],
    version = 4,
    exportSchema = false,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun sessionDao(): SessionDao
    abstract fun segmentDao(): SegmentDao
    abstract fun meetingDao(): MeetingDao
}
