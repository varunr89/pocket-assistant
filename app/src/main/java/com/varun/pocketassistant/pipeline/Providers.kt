package com.varun.pocketassistant.pipeline

import java.io.File

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
) {
    suspend fun transcribe(wav: File): AsrResult {
        val settings = config.load()
        val detailBase = "file=${wav.name} bytes=${wav.length()}"
        return route(
            mode = settings.asrMode,
            localAvailable = localAsr.isAvailable(),
            cloudAvailable = cloudAsr.isAvailable(),
            runLocal = {
                PipelineTelemetry.timed(
                    "ASR",
                    "$detailBase provider=${localAsr.id}",
                    timeoutMs = PipelineTelemetry.ASR_TIMEOUT_MS,
                ) {
                    localAsr.transcribe(wav)
                }
            },
            runCloud = {
                PipelineTelemetry.timed(
                    "ASR",
                    "$detailBase provider=${cloudAsr.id}",
                    timeoutMs = PipelineTelemetry.ASR_TIMEOUT_MS,
                ) {
                    cloudAsr.transcribe(wav)
                }
            },
            stage = "ASR",
        )
    }

    suspend fun clean(raw: String, diarized: Boolean): TextStageResult {
        val settings = config.load()
        val detailBase = "inChars=${raw.length} diarized=$diarized"
        return route(
            mode = settings.cleanupMode,
            localAvailable = localText.isAvailable(),
            cloudAvailable = cloudText.isAvailable(),
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
                PipelineTelemetry.timed(
                    "cleanup",
                    "$detailBase provider=${cloudText.id} model=${settings.cloudCleanupModel}",
                    timeoutMs = PipelineTelemetry.CHAT_TIMEOUT_MS,
                ) {
                    TextStageResult(
                        text = cloudText.cleanTranscript(raw, diarized),
                        providerId = cloudText.id,
                    )
                }
            },
            stage = "cleanup",
        )
    }

    suspend fun summarize(cleanedTranscript: String): TextStageResult {
        val settings = config.load()
        val detailBase = "inChars=${cleanedTranscript.length}"
        return route(
            mode = settings.summaryMode,
            localAvailable = localText.isAvailable(),
            cloudAvailable = cloudText.isAvailable(),
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
                PipelineTelemetry.timed(
                    "summary",
                    "$detailBase provider=${cloudText.id} model=${settings.cloudSummaryModel}",
                    timeoutMs = PipelineTelemetry.CHAT_TIMEOUT_MS,
                ) {
                    TextStageResult(
                        text = cloudText.summarize(cleanedTranscript),
                        providerId = cloudText.id,
                    )
                }
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
            localAvailable = localText.isAvailable(),
            cloudAvailable = cloudText.isAvailable(),
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
                PipelineTelemetry.timed(
                    "actions",
                    "$detailBase provider=${cloudText.id} model=${settings.cloudSummaryModel}",
                    timeoutMs = PipelineTelemetry.CHAT_TIMEOUT_MS,
                ) {
                    TextStageResult(
                        text = cloudText.prompt(system, prompt),
                        providerId = cloudText.id,
                    )
                }
            },
            stage = "actions",
        )
    }

    private suspend fun <T> route(
        mode: ProviderMode,
        localAvailable: Boolean,
        cloudAvailable: Boolean,
        runLocal: suspend () -> T,
        runCloud: suspend () -> T,
        stage: String,
    ): T {
        fun fail(msg: String): Nothing = error("$stage: $msg")
        return when (mode) {
            ProviderMode.PREFER_LOCAL -> {
                if (localAvailable) {
                    runCatching { runLocal() }.getOrElse { localErr ->
                        if (!cloudAvailable) throw localErr
                        android.util.Log.w(TAG, "$stage local failed, falling back to cloud", localErr)
                        runCloud()
                    }
                } else if (cloudAvailable) {
                    runCloud()
                } else {
                    fail("no local or cloud provider available")
                }
            }
            ProviderMode.PREFER_CLOUD -> {
                if (cloudAvailable) {
                    runCatching { runCloud() }.getOrElse { cloudErr ->
                        // ASR may fall back to on-device; text stages fail loudly so we don't
                        // hang on a local LLM after a cloud error (and so the UI shows why).
                        if (stage == "ASR" && localAvailable) {
                            android.util.Log.w(
                                TAG,
                                "$stage cloud failed (${cloudErr.message}), falling back to local",
                                cloudErr,
                            )
                            runLocal()
                        } else {
                            android.util.Log.e(
                                TAG,
                                "$stage cloud failed (${cloudErr.message}) — not falling back to local",
                                cloudErr,
                            )
                            throw cloudErr
                        }
                    }
                } else if (localAvailable) {
                    android.util.Log.w(
                        TAG,
                        "$stage prefer cloud but cloud unavailable (save OpenRouter API key?) — using local",
                    )
                    runLocal()
                } else {
                    fail("no cloud or local provider available")
                }
            }
        }
    }

    companion object {
        private const val TAG = "ProviderRouter"
    }
}
