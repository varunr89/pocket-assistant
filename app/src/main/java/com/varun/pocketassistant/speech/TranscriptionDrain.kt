package com.varun.pocketassistant.speech

import android.util.Log
import com.varun.pocketassistant.data.SegmentEntity
import com.varun.pocketassistant.data.SessionRepository
import com.varun.pocketassistant.data.TranscriptStatus
import java.io.File
import java.time.LocalDate
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.delay

/** Minimal view of a segment waiting in the catch-up queue. */
data class PendingSegment(
    val id: String,
    val filePath: String,
    val startedAtMs: Long,
    val durationMs: Long,
) {
    companion object {
        fun from(entity: SegmentEntity): PendingSegment = PendingSegment(
            id = entity.id,
            filePath = entity.filePath,
            startedAtMs = entity.startedAtMs,
            durationMs = entity.durationMs,
        )
    }
}

/**
 * Narrow persistence seam for the catch-up drain (JVM-fakeable in unit
 * tests). Status transitions mirror [com.varun.pocketassistant.speech.AsrStage]
 * exactly so segments stay on the same transcript state machine.
 */
interface CatchUpQueue {
    /** PENDING segments, ordered oldest-first (crash-leftover PROCESSING rows are recovered). */
    suspend fun pending(limit: Int): List<PendingSegment>

    /** PENDING → PROCESSING. Returns false if the segment was claimed elsewhere. */
    suspend fun claim(segmentId: String): Boolean

    suspend fun markReady(segmentId: String, text: String, providerId: String)
    suspend fun markSkippedSilence(segmentId: String)
    suspend fun markFailed(segmentId: String, error: String)

    /** Roll an attempt back (BUSY / quota / blocked): PROCESSING → PENDING, wait. */
    suspend fun markWaiting(segmentId: String)
}

/** Production adapter over [SessionRepository]. */
class RepositoryCatchUpQueue(
    private val repository: SessionRepository,
    private val batchSize: Int = DEFAULT_BATCH_SIZE,
) : CatchUpQueue {

    override suspend fun pending(limit: Int): List<PendingSegment> =
        repository.getPendingWork(limit.coerceAtMost(batchSize))
            .filter { it.transcriptStatus == TranscriptStatus.PENDING.name }
            .map(PendingSegment::from)

    override suspend fun claim(segmentId: String): Boolean {
        val segment = repository.getSegment(segmentId) ?: return false
        if (segment.transcriptStatus != TranscriptStatus.PENDING.name) return false
        repository.updateTranscript(segmentId, TranscriptStatus.PROCESSING, clearAsrError = true)
        return true
    }

    override suspend fun markReady(segmentId: String, text: String, providerId: String) {
        repository.updateTranscript(
            segmentId = segmentId,
            status = TranscriptStatus.READY,
            transcript = text,
            asrProvider = providerId,
            clearAsrError = true,
        )
    }

    override suspend fun markSkippedSilence(segmentId: String) {
        repository.updateTranscript(segmentId, TranscriptStatus.SKIPPED_SILENCE, clearAsrError = true)
    }

    override suspend fun markFailed(segmentId: String, error: String) {
        repository.updateTranscript(
            segmentId = segmentId,
            status = TranscriptStatus.FAILED,
            asrLastError = error,
        )
    }

    override suspend fun markWaiting(segmentId: String) {
        repository.updateTranscript(segmentId, TranscriptStatus.PENDING, clearAsrError = true)
    }

    companion object {
        private const val DEFAULT_BATCH_SIZE = 20
    }
}

/** One drain pass. Every terminal state is explicit; nothing is silent. */
data class DrainResult(
    val quiet: Boolean = false,
    val notForeground: Boolean = false,
    /** Drain disabled by configuration (e.g. cloud mode owns transcription). */
    val disabled: Boolean = false,
    val quotaPaused: Boolean = false,
    val processed: Int = 0,
    val skippedSilence: Int = 0,
    val failed: Int = 0,
    /** Segments rolled back to PENDING (BUSY / quota / blocked) — retried later. */
    val waiting: Int = 0,
) {
    val settled: Int get() = processed + skippedSilence + failed
    /** True when the service should poll again soon instead of stopping. */
    fun shouldKeepPolling(): Boolean =
        !notForeground && !disabled && !quotaPaused && (settled > 0 || waiting > 0)
}

/**
 * Foreground-gated catch-up drain. One [drainOnce] pass claims and processes
 * up to one batch of PENDING segments; it no-ops (and waits) whenever the app
 * is not foreground. The service decides when to poll again from [DrainResult].
 *
 * Failure policy (docs/engineering/architecture-final.md §5 AICore contract):
 * - [ForegroundAsrFailure.Busy] → exponential backoff in this pass, segment
 *   waits. After [MAX_BUSY_RETRIES_IN_PASS] consecutive BUSY hits the pass
 *   stops (remaining segments stay PENDING; the service polls again shortly).
 * - [ForegroundAsrFailure.DailyQuotaExceeded] → segment waits; drain pauses
 *   until the next local day (recorded in [DrainDiagnostics]).
 * - [ForegroundAsrFailure.BackgroundUseBlocked] → EXPECTED state: segment
 *   waits, recorded in diagnostics — never FAILED, never a retry storm.
 * - [ForegroundAsrFailure.Unavailable] / [InvalidAudio] → visible FAILED.
 * - Anything else → visible FAILED with an "unexpected" prefix.
 */
class TranscriptionDrain(
    private val engine: ForegroundAsrEngine,
    private val queue: CatchUpQueue,
    private val gate: ForegroundGate,
    /** False while another path (cloud tier) owns transcription — drain no-ops. */
    private val enabled: () -> Boolean,
    private val localeTag: () -> String = { "en-US" },
    /** Test seam: delays are injected, never real inside tests. */
    private val sleeper: suspend (Long) -> Unit = { delay(it) },
    private val dayEpoch: () -> Long = { LocalDate.now().toEpochDay() },
    /** Receives every diagnostics snapshot after each pass (service → hub). */
    private val diagnosticsListener: (DrainDiagnostics) -> Unit = {},
) {
    @Volatile
    private var diagnostics = DrainDiagnostics()

    fun snapshotDiagnostics(): DrainDiagnostics = diagnostics

    suspend fun drainOnce(): DrainResult {
        emitDiagnostics()

        val today = dayEpoch()
        if (diagnostics.quotaPausedDayEpoch == today) {
            return DrainResult(quotaPaused = true)
        }
        if (!gate.isForeground()) return DrainResult(notForeground = true)
        if (!enabled()) return DrainResult(disabled = true)

        val batch = queue.pending(BATCH_LIMIT)
        if (batch.isEmpty()) return DrainResult(quiet = true).also { emitDiagnostics() }

        var result = DrainResult()
        var busyAttempt = 0
        for (segment in batch) {
            if (!gate.isForeground()) {
                result = result.copy(notForeground = true)
                break
            }
            if (diagnostics.quotaPausedDayEpoch == dayEpoch()) {
                result = result.copy(quotaPaused = true)
                break
            }
            if (!queue.claim(segment.id)) continue

            try {
                val text = engine.transcribe(File(segment.filePath), localeTag())
                if (text.isBlank()) {
                    queue.markSkippedSilence(segment.id)
                    result = result.copy(skippedSilence = result.skippedSilence + 1)
                    diagnostics = diagnostics.withSkippedSilence()
                } else {
                    queue.markReady(segment.id, text, engine.id)
                    result = result.copy(processed = result.processed + 1)
                    diagnostics = diagnostics.withProcessed()
                }
                busyAttempt = 0
            } catch (failure: ForegroundAsrFailure) {
                when (failure) {
                    is ForegroundAsrFailure.Busy -> {
                        Log.w(TAG, "AICore busy for ${segment.id} — exponential backoff, segment waits")
                        queue.markWaiting(segment.id)
                        diagnostics = diagnostics.withBusyRetry()
                        busyAttempt++
                        sleeper(backoffFor(busyAttempt))
                        result = result.copy(waiting = result.waiting + 1)
                        if (busyAttempt >= MAX_BUSY_RETRIES_IN_PASS) {
                            Log.w(TAG, "BUSY budget exhausted this pass — stopping to reschedule")
                            break
                        }
                    }
                    is ForegroundAsrFailure.DailyQuotaExceeded -> {
                        Log.w(TAG, "AICore daily battery quota exceeded at ${segment.id} — pause for the day")
                        queue.markWaiting(segment.id)
                        diagnostics = diagnostics.withDailyQuotaHit(dayEpoch())
                        result = result.copy(waiting = result.waiting + 1, quotaPaused = true)
                        break
                    }
                    is ForegroundAsrFailure.BackgroundUseBlocked -> {
                        // Expected whenever we are not the top foreground app;
                        // the gate and this error agree — record and wait.
                        Log.w(
                            TAG,
                            "AICore refused background use for ${segment.id} — " +
                                "expected outside foreground; recorded in diagnostics",
                        )
                        queue.markWaiting(segment.id)
                        diagnostics = diagnostics.withBackgroundBlocked()
                        result = result.copy(waiting = result.waiting + 1, notForeground = true)
                        break
                    }
                    is ForegroundAsrFailure.Unavailable,
                    is ForegroundAsrFailure.InvalidAudio,
                    -> {
                        Log.e(TAG, "ASR failed visibly for ${segment.id}: ${failure.message}")
                        queue.markFailed(segment.id, failure.message ?: failure.javaClass.simpleName)
                        diagnostics = diagnostics.withFailed()
                        result = result.copy(failed = result.failed + 1)
                    }
                }
            } catch (ce: CancellationException) {
                // Drain stopped mid-segment: leave the claim (PROCESSING) for
                // the next pass to recover; never mark it as a failure.
                emitDiagnostics()
                throw ce
            } catch (t: Throwable) {
                Log.e(TAG, "unexpected failure transcribing ${segment.id}", t)
                queue.markFailed(segment.id, "unexpected: ${t.message ?: t.javaClass.simpleName}")
                result = result.copy(failed = result.failed + 1)
                diagnostics = diagnostics.withFailed()
            }
        }
        emitDiagnostics()
        return result
    }

    private fun emitDiagnostics() {
        diagnosticsListener(diagnostics)
    }

    companion object {
        private const val TAG = "TranscriptionDrain"
        private const val BATCH_LIMIT = 20
        private const val MAX_BUSY_RETRIES_IN_PASS = 5
        private val BACKOFF_MS = longArrayOf(2_000, 4_000, 8_000, 16_000, 30_000)

        /** Exponential backoff for this pass's nth BUSY hit (capped at 30s). */
        fun backoffFor(attempt: Int): Long {
            val index = (attempt - 1).coerceIn(0, BACKOFF_MS.lastIndex)
            return BACKOFF_MS[index]
        }
    }
}