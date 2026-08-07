package com.varun.pocketassistant.data

import android.util.Log
import com.varun.pocketassistant.capture.AudioStorage
import java.io.File
import java.io.RandomAccessFile
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.util.UUID

/**
 * Recovers speech WAV files that still exist on disk after a destructive DB migration
 * (or any other metadata loss), then queues them for transcription.
 */
class OrphanSessionImporter(
    private val audioStorage: AudioStorage,
    private val sessionDao: SessionDao,
    private val segmentDao: SegmentDao,
    private val repository: SessionRepository,
) {
    suspend fun importMissing(): Int {
        val root = audioStorage.sessionsRoot()
        if (!root.exists()) return 0

        var imported = 0
        val dirs = root.listFiles()?.filter { it.isDirectory } ?: return 0
        for (dir in dirs) {
            val wavs = dir.listFiles()?.filter {
                it.isFile && it.name.startsWith("speech_") && it.name.endsWith(".wav") && it.length() > 44
            }?.sortedBy { it.length() } // smaller first so transcripts appear sooner
                ?: continue
            if (wavs.isEmpty()) continue

            val already = segmentDao.getForSession(dir.name)
            val existing = already.map { it.filePath }.toHashSet()

            var session = sessionDao.getById(dir.name)
            if (session == null) {
                val started = wavs.first().let { parseStartMs(it.name) ?: it.lastModified() }
                val ended = wavs.last().lastModified()
                session = SessionEntity(
                    id = dir.name,
                    startedAtMs = started,
                    endedAtMs = ended,
                    status = SessionStatus.COMPLETED.name,
                    segmentCount = 0,
                    speechDurationMs = 0,
                    notes = "Recovered from disk",
                )
                sessionDao.insert(session)
                Log.i(TAG, "Recovered session ${session.id}")
            }

            var segmentCount = session.segmentCount
            var speechMs = session.speechDurationMs
            for (wav in wavs) {
                if (wav.absolutePath in existing) continue

                val startedAt = parseStartMs(wav.name) ?: wav.lastModified()
                val durationMs = estimateDurationMs(wav)
                val endedAt = startedAt + durationMs
                val segment = SegmentEntity(
                    id = UUID.randomUUID().toString(),
                    sessionId = session.id,
                    filePath = wav.absolutePath,
                    startedAtMs = startedAt,
                    endedAtMs = endedAt,
                    durationMs = durationMs,
                    byteSize = wav.length(),
                    transcriptStatus = TranscriptStatus.PENDING.name,
                )
                segmentDao.insert(segment)
                segmentCount++
                speechMs += durationMs
                imported++
                existing.add(wav.absolutePath)
                repository.pipelineScheduler?.enqueueAsr(segment.id)
                Log.i(TAG, "Queued orphan ${wav.name} (${wav.length()} bytes)")
            }

            sessionDao.update(
                session.copy(
                    segmentCount = segmentCount,
                    speechDurationMs = speechMs,
                    status = SessionStatus.COMPLETED.name,
                    endedAtMs = session.endedAtMs ?: System.currentTimeMillis(),
                ),
            )
        }
        return imported
    }

    private fun parseStartMs(name: String): Long? {
        // speech_<epochMs>.wav
        // speech_<parentEpochMs>_partNNN.wav — wall time = parent open + N * 120s
        val stem = name.removePrefix("speech_").removeSuffix(".wav")
        val partMatch = Regex("""^(\d+)_part(\d+)$""").matchEntire(stem)
        if (partMatch != null) {
            val base = partMatch.groupValues[1].toLongOrNull() ?: return null
            val part = partMatch.groupValues[2].toIntOrNull() ?: return null
            return base + part * 120_000L
        }
        return stem.toLongOrNull()
    }

    private fun estimateDurationMs(wav: File): Long {
        return try {
            RandomAccessFile(wav, "r").use { raf ->
                val header = ByteArray(44)
                raf.readFully(header)
                val bb = ByteBuffer.wrap(header).order(ByteOrder.LITTLE_ENDIAN)
                bb.position(24)
                val sampleRate = bb.int
                bb.position(22)
                val channels = bb.short.toInt()
                bb.position(34)
                val bits = bb.short.toInt()
                bb.position(40)
                val dataBytes = bb.int.toLong().coerceAtLeast(0)
                val bytesPerSec = sampleRate.toLong() * channels * (bits / 8)
                if (bytesPerSec <= 0) return@use wav.length() / 32 // rough 16k mono pcm
                dataBytes * 1000L / bytesPerSec
            }
        } catch (_: Throwable) {
            // 16-bit mono 16kHz ≈ 32 bytes/ms
            ((wav.length() - 44).coerceAtLeast(0)) * 1000L / 32_000L
        }
    }

    companion object {
        private const val TAG = "OrphanImporter"
    }
}
