package com.varun.pocketassistant.speech

/**
 * Foreground-gated on-device ASR boundary (M1 product decision 2026-09-06:
 * ML Kit GenAI Speech Recognition, Advanced mode via AICore).
 *
 * Implementations throw [ForegroundAsrFailure] subtypes for every failure
 * mode the drain processor must distinguish; anything else thrown from
 * [transcribe] is treated by callers as an unexpected engine bug (still a
 * visible failure, never swallowed).
 */
interface ForegroundAsrEngine {
    /** Stable provider id persisted in the segment's `asrProvider` column. */
    val id: String

    /**
     * Transcribe one captured WAV segment (16 kHz mono PCM16).
     *
     * @param wav segment audio file as written by [com.varun.pocketassistant.capture.WavWriter].
     * @param localeTag BCP-47 tag (e.g. "en-US"); empty means caller default.
     * @return accumulated final transcript text (never partial text).
     * @throws ForegroundAsrFailure typed failure (see the classifiers below).
     */
    suspend fun transcribe(wav: java.io.File, localeTag: String): String
}

/** Typed failure classification of the AICore boundary. */
sealed class ForegroundAsrFailure(message: String) : Exception(message) {
    /**
     * AICore short-window quota saturated (ErrorCode.BUSY).
     * Retry with exponential backoff while still foreground.
     */
    class Busy(message: String) : ForegroundAsrFailure(message)

    /**
     * AICore long-duration (per-day) battery quota exhausted
     * (ErrorCode.PER_APP_BATTERY_USE_QUOTA_EXCEEDED). Stop draining for the
     * rest of the day and record it — retrying burns nothing but time.
     */
    class DailyQuotaExceeded(message: String) : ForegroundAsrFailure(message)

    /**
     * ErrorCode.BACKGROUND_USE_BLOCKED: AICore refuses background inference.
     * This is the EXPECTED state whenever the app is not the top foreground
     * app — the drain records it in diagnostics (no-silent-failure SLO) and
     * waits; it is never retried as a transient error.
     */
    class BackgroundUseBlocked(message: String) : ForegroundAsrFailure(message)

    /**
     * Device/model/feature unavailable (feature UNAVAILABLE, download failed,
     * AICore setup error, response timeout, unrecognised error code). A
     * visible, persistent failure for the segment.
     */
    class Unavailable(message: String) : ForegroundAsrFailure(message)

    /**
     * Segment audio does not meet the ML Kit input contract (missing file,
     * not RIFF/WAVE, not PCM16, not mono, not 16 kHz). Rejected BEFORE any
     * AICore call — never fed to the model.
     */
    class InvalidAudio(message: String) : ForegroundAsrFailure(message)
}

/**
 * Process-wide drain diagnostics. Every count is an explicit signal, and the
 * expected-but-notable states (background-blocked, daily quota) are counted
 * separately so the no-silent-failure SLO has evidence, not just logs.
 */
data class DrainDiagnostics(
    val processed: Long = 0,
    val skippedSilence: Long = 0,
    val failed: Long = 0,
    val busyRetries: Long = 0,
    val dailyQuotaHits: Long = 0,
    val backgroundBlockedHits: Long = 0,
    /** Local epoch-day the drain last paused for the daily quota. */
    val quotaPausedDayEpoch: Long? = null,
    val lastEventAtMs: Long = 0,
) {
    fun withProcessed(): DrainDiagnostics =
        copy(processed = processed + 1, lastEventAtMs = System.currentTimeMillis())

    fun withSkippedSilence(): DrainDiagnostics =
        copy(skippedSilence = skippedSilence + 1, lastEventAtMs = System.currentTimeMillis())

    fun withFailed(): DrainDiagnostics =
        copy(failed = failed + 1, lastEventAtMs = System.currentTimeMillis())

    fun withBusyRetry(): DrainDiagnostics =
        copy(busyRetries = busyRetries + 1, lastEventAtMs = System.currentTimeMillis())

    fun withDailyQuotaHit(dayEpoch: Long): DrainDiagnostics =
        copy(
            dailyQuotaHits = dailyQuotaHits + 1,
            quotaPausedDayEpoch = dayEpoch,
            lastEventAtMs = System.currentTimeMillis(),
        )

    fun withBackgroundBlocked(): DrainDiagnostics =
        copy(backgroundBlockedHits = backgroundBlockedHits + 1, lastEventAtMs = System.currentTimeMillis())
}