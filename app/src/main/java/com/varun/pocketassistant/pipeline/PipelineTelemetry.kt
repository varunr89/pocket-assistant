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

    /** ASR (audio upload) ceiling. */
    const val ASR_TIMEOUT_MS = 3 * 60 * 1000L

    /** Chat cleanup / summary / actions wall budget (includes a few short retries). */
    const val CHAT_TIMEOUT_MS = 3 * 60 * 1000L

    /** @deprecated Prefer [ASR_TIMEOUT_MS] / [CHAT_TIMEOUT_MS]. */
    const val OPERATION_TIMEOUT_MS = ASR_TIMEOUT_MS

    const val HTTP_CONNECT_TIMEOUT_MS = 15_000L

    /** Per-attempt OkHttp read/call budgets (chat). */
    const val CHAT_HTTP_READ_TIMEOUT_MS = 55_000L
    const val CHAT_HTTP_CALL_TIMEOUT_MS = 60_000L

    /** Per-attempt OkHttp budgets (ASR — larger payloads). */
    const val ASR_HTTP_READ_TIMEOUT_MS = 2 * 60 * 1000L
    const val ASR_HTTP_CALL_TIMEOUT_MS = 2 * 60 * 1000L + 30_000L

    /** @deprecated Prefer stage-specific HTTP timeouts. */
    const val HTTP_READ_TIMEOUT_MS = ASR_HTTP_READ_TIMEOUT_MS

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
