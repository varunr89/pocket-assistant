package com.varun.pocketassistant.pipeline

import java.io.File
import kotlinx.coroutines.CancellationException

data class AsrResult(
    val plain: String,
    val diarized: String? = null,
    val providerId: String,
)

data class TextStageResult(
    val text: String,
    val providerId: String,
)

/** @deprecated Use [TextStageResult]. */
typealias CleanupResult = TextStageResult

interface AsrProvider {
    val id: String
    fun isAvailable(): Boolean
    suspend fun transcribe(wav: File): AsrResult
}

interface MeetingTextProvider {
    val id: String
    fun isAvailable(): Boolean
    /** Pass 1: cleaned transcript text only. */
    suspend fun cleanTranscript(rawTranscript: String, diarized: Boolean): String
    /** Pass 2: Title + Overview markdown from a cleaned transcript. */
    suspend fun summarize(cleanedTranscript: String): String
    /** Generic chat for actions / follow-ups. */
    suspend fun prompt(system: String, user: String): String
}

/** @deprecated Use [MeetingTextProvider]. */
typealias CleanupProvider = MeetingTextProvider

class ProviderRouter(
    private val config: PipelineConfig,
    private val localAsr: AsrProvider,
    private val cloudAsr: AsrProvider,
    private val localText: MeetingTextProvider,
    private val cloudText: MeetingTextProvider,
    private val circuitBreaker: CloudCircuitBreaker? = null,
) {
    suspend fun transcribe(wav: File): AsrResult {
        val settings = config.load()
        val detailBase = "file=${wav.name} bytes=${wav.length()}"
        return route(
            mode = settings.asrMode,
            allowFallback = settings.allowFallback,
            localAvailable = localAsr.isAvailable(),
            cloudAvailable = cloudAsr.isAvailable() && (circuitBreaker?.isHealthy() != false),
            runLocal = {
                PipelineTelemetry.timed(
                    "ASR",
                    "$detailBase provider=${localAsr.id}",
                    timeoutMs = PipelineTelemetry.ASR_CHUNK_TIMEOUT_MS,
                ) {
                    localAsr.transcribe(wav)
                }
            },
            runCloud = {
                cloudAsr.transcribe(wav)
            },
            stage = "ASR",
        )
    }

    suspend fun clean(raw: String, diarized: Boolean): TextStageResult {
        val settings = config.load()
        val detailBase = "inChars=${raw.length} diarized=$diarized"
        return route(
            mode = settings.cleanupMode,
            allowFallback = settings.allowFallback,
            localAvailable = localText.isAvailable(),
            cloudAvailable = cloudText.isAvailable() && (circuitBreaker?.isHealthy() != false),
            runLocal = {
                PipelineTelemetry.timed(
                    "cleanup",
                    "$detailBase provider=${localText.id}",
                    timeoutMs = PipelineTelemetry.CHAT_TIMEOUT_MS,
                ) {
                    TextStageResult(
                        text = localText.cleanTranscript(raw, diarized),
                        providerId = localText.id,
                    )
                }
            },
            runCloud = {
                TextStageResult(
                    text = cloudText.cleanTranscript(raw, diarized),
                    providerId = cloudText.id,
                )
            },
            stage = "cleanup",
        )
    }

    suspend fun summarize(cleanedTranscript: String): TextStageResult {
        val settings = config.load()
        val detailBase = "inChars=${cleanedTranscript.length}"
        return route(
            mode = settings.summaryMode,
            allowFallback = settings.allowFallback,
            localAvailable = localText.isAvailable(),
            cloudAvailable = cloudText.isAvailable() && (circuitBreaker?.isHealthy() != false),
            runLocal = {
                PipelineTelemetry.timed(
                    "summary",
                    "$detailBase provider=${localText.id}",
                    timeoutMs = PipelineTelemetry.CHAT_TIMEOUT_MS,
                ) {
                    TextStageResult(
                        text = localText.summarize(cleanedTranscript),
                        providerId = localText.id,
                    )
                }
            },
            runCloud = {
                TextStageResult(
                    text = cloudText.summarize(cleanedTranscript),
                    providerId = cloudText.id,
                )
            },
            stage = "summary",
        )
    }

    /** Future meeting actions / follow-ups. Uses [PipelineSettings.actionsMode]. */
    suspend fun runActions(
        prompt: String,
        system: String = "You extract meeting action items. Output only the requested text.",
    ): TextStageResult {
        val settings = config.load()
        val detailBase = "inChars=${prompt.length}"
        return route(
            mode = settings.actionsMode,
            allowFallback = settings.allowFallback,
            localAvailable = localText.isAvailable(),
            cloudAvailable = cloudText.isAvailable() && (circuitBreaker?.isHealthy() != false),
            runLocal = {
                PipelineTelemetry.timed(
                    "actions",
                    "$detailBase provider=${localText.id}",
                    timeoutMs = PipelineTelemetry.CHAT_TIMEOUT_MS,
                ) {
                    TextStageResult(
                        text = localText.prompt(system, prompt),
                        providerId = localText.id,
                    )
                }
            },
            runCloud = {
                TextStageResult(
                    text = cloudText.prompt(system, prompt),
                    providerId = cloudText.id,
                )
            },
            stage = "actions",
        )
    }

    private suspend fun <T> route(
        mode: ProviderMode,
        allowFallback: Boolean,
        localAvailable: Boolean,
        cloudAvailable: Boolean,
        runLocal: suspend () -> T,
        runCloud: suspend () -> T,
        stage: String,
    ): T {
        fun fail(msg: String): Nothing = error("$stage: $msg")
        val primaryIsCloud = mode == ProviderMode.PREFER_CLOUD
        val primaryAvailable = if (primaryIsCloud) cloudAvailable else localAvailable
        val secondaryAvailable = if (primaryIsCloud) localAvailable else cloudAvailable
        val runPrimary = if (primaryIsCloud) runCloud else runLocal
        val runSecondary = if (primaryIsCloud) runLocal else runCloud
        val primaryLabel = if (primaryIsCloud) "cloud" else "local"
        val secondaryLabel = if (primaryIsCloud) "local" else "cloud"

        if (primaryAvailable) {
            try {
                return runPrimary()
            } catch (ce: CancellationException) {
                throw ce
            } catch (primaryErr: Throwable) {
                // Only fall back for non-transient failures after transport retries are exhausted.
                // A 502 must not divert to slow local Gemma.
                if (!allowFallback ||
                    !secondaryAvailable ||
                    OpenAiCompatibleClient.isTransientTransportError(primaryErr)
                ) {
                    throw primaryErr
                }
                android.util.Log.w(
                    TAG,
                    "$stage $primaryLabel failed non-transient (${primaryErr.message}), " +
                        "falling back to $secondaryLabel",
                    primaryErr,
                )
                return runSecondary()
            }
        }
        if (allowFallback && secondaryAvailable) {
            android.util.Log.w(
                TAG,
                "$stage $primaryLabel unavailable — using $secondaryLabel",
            )
            return runSecondary()
        }
        if (!primaryAvailable && !secondaryAvailable) {
            fail("no local or cloud provider available")
        }
        fail("$primaryLabel unavailable (enable fallback or configure the other side)")
    }

    companion object {
        private const val TAG = "ProviderRouter"
    }
}
