package com.varun.pocketassistant.pipeline

import android.util.Log
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.withTimeout
import kotlin.system.measureTimeMillis

/**
 * Operation budgets + structured timing logs for ASR / cleanup / summary.
 *
 * Look for tag [TAG] in logcat, e.g.:
 * `adb logcat -s PipelineTelemetry`
 */
object PipelineTelemetry {
    const val TAG = "PipelineTelemetry"

    /**
     * Wall budget for a single ASR chunk attempt (one HTTP transcription).
     * Cloud multi-chunk work uses this per chunk; local ASR uses it for the whole file.
     */
    const val ASR_CHUNK_TIMEOUT_MS = 5 * 60 * 1000L

    /** @deprecated Prefer [ASR_CHUNK_TIMEOUT_MS]. */
    const val ASR_TIMEOUT_MS = ASR_CHUNK_TIMEOUT_MS

    /**
     * Wall budget for a single cloud chat HTTP attempt (cleanup / summary / actions).
     * Retries each get a fresh budget — do not wrap all attempts in one outer timeout.
     */
    const val CHAT_ATTEMPT_TIMEOUT_MS = 90_000L

    /** Local LLM wall budget for a full clean/summary call. */
    const val CHAT_TIMEOUT_MS = 3 * 60 * 1000L

    /** @deprecated Prefer [ASR_TIMEOUT_MS] / [CHAT_TIMEOUT_MS]. */
    const val OPERATION_TIMEOUT_MS = ASR_TIMEOUT_MS

    /** Shorter connect timeout to limit OkHttp address-walk amplification. */
    const val HTTP_CONNECT_TIMEOUT_MS = 8_000L

    /**
     * Per-attempt OkHttp budgets (chat). Kept under [CHAT_ATTEMPT_TIMEOUT_MS]
     * so the coroutine wall can cancel and retry.
     */
    const val CHAT_HTTP_READ_TIMEOUT_MS = 55_000L
    const val CHAT_HTTP_WRITE_TIMEOUT_MS = 30_000L
    const val CHAT_HTTP_CALL_TIMEOUT_MS = 60_000L

    /**
     * Per-attempt OkHttp socket budgets (ASR). No flat callTimeout — a healthy slow
     * upload should not be killed while a stalled one still trips read/write.
     */
    const val ASR_HTTP_READ_TIMEOUT_MS = 90_000L
    const val ASR_HTTP_WRITE_TIMEOUT_MS = 60_000L

    /** @deprecated Prefer stage-specific HTTP timeouts. */
    const val HTTP_READ_TIMEOUT_MS = ASR_HTTP_READ_TIMEOUT_MS

    /** HTTP-level ASR tries inside one chunk (WorkManager retries the whole segment). */
    const val ASR_HTTP_MAX_ATTEMPTS = 3

    /** Max attempts for meeting-wide cleanup stage (WorkManager also backs this). */
    const val CLEANUP_MAX_ATTEMPTS = 3

    /** Max attempts for meeting summary stage. */
    const val SUMMARY_MAX_ATTEMPTS = 3

    /** HTTP-level chat tries per stage attempt. */
    const val CHAT_HTTP_MAX_ATTEMPTS = 3

    suspend fun <T> timed(
        stage: String,
        detail: String = "",
        timeoutMs: Long = OPERATION_TIMEOUT_MS,
        block: suspend () -> T,
    ): T {
        val start = System.currentTimeMillis()
        return try {
            val result = withTimeout(timeoutMs) { block() }
            val elapsed = System.currentTimeMillis() - start
            Log.i(
                TAG,
                "$stage ok ${detail.trim()} elapsedMs=$elapsed timeoutMs=$timeoutMs",
            )
            result
        } catch (t: TimeoutCancellationException) {
            val elapsed = System.currentTimeMillis() - start
            Log.e(
                TAG,
                "$stage TIMEOUT ${detail.trim()} elapsedMs=$elapsed timeoutMs=$timeoutMs",
            )
            throw IllegalStateException(
                "$stage timed out after ${elapsed}ms (limit ${timeoutMs}ms)",
                t,
            )
        } catch (t: Throwable) {
            val elapsed = System.currentTimeMillis() - start
            Log.e(
                TAG,
                "$stage FAIL ${detail.trim()} elapsedMs=$elapsed error=${t.message}",
                t,
            )
            throw t
        }
    }

    fun <T> measure(
        stage: String,
        detail: String = "",
        block: () -> T,
    ): T {
        var result: T
        val elapsed = measureTimeMillis {
            result = block()
        }
        Log.i(TAG, "$stage ok ${detail.trim()} elapsedMs=$elapsed")
        return result
    }
}
