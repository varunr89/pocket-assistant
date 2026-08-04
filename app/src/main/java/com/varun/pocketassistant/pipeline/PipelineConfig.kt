package com.varun.pocketassistant.pipeline

import android.content.Context
import androidx.core.content.edit
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

/**
 * Which side to try first. Always falls back to the other when available.
 */
enum class ProviderMode {
    PREFER_CLOUD,
    PREFER_LOCAL,
}

data class PipelineSettings(
    val asrMode: ProviderMode = ProviderMode.PREFER_CLOUD,
    val cleanupMode: ProviderMode = ProviderMode.PREFER_CLOUD,
    val summaryMode: ProviderMode = ProviderMode.PREFER_CLOUD,
    /** Reserved for future meeting actions / follow-ups. */
    val actionsMode: ProviderMode = ProviderMode.PREFER_CLOUD,
    val cloudBaseUrl: String = DEFAULT_BASE_URL,
    val cloudApiKey: String = "",
    val cloudAsrModel: String = DEFAULT_ASR_MODEL,
    /** Cheap/fast model for transcript cleanup. */
    val cloudCleanupModel: String = DEFAULT_CLEANUP_MODEL,
    /** Stronger model for meeting summarization (and future multi-meeting synthesis). */
    val cloudSummaryModel: String = DEFAULT_SUMMARY_MODEL,
    /**
     * OpenRouter `reasoning.effort` applied to the **summary** chat call
     * (DeepSeek, o-series, Gemini thinking, …).
     * Blank = omit (model default). Values: none, minimal, low, medium, high, xhigh, max.
     */
    val reasoningEffort: String = "",
    /** When true, ask OpenRouter to keep reasoning internal (`reasoning.exclude=true`). */
    val excludeReasoningFromResponse: Boolean = true,
    /** ISO-639-1 language for STT (empty = auto-detect). */
    val sttLanguage: String = "",
    val localAsrModelId: String = "parakeet_tdt",
    val localCleanupModelId: String = "gemma-4b",
    /**
     * Transcript cleanup template. Placeholders: {{transcript}}, {{glossary}}, {{speaker_note}}.
     * Blank uses [CleanupPrompts.DEFAULT_CLEANUP_PROMPT].
     */
    val cleanupPrompt: String = "",
    /**
     * Meeting summarization template (Title + Overview). Placeholders: {{transcript}}, {{glossary}}.
     * Blank uses [CleanupPrompts.DEFAULT_SUMMARY_PROMPT].
     */
    val summaryPrompt: String = "",
    /** Domain glossary injected into prompts. Blank uses [CleanupPrompts.DEFAULT_GLOSSARY]. */
    val glossary: String = "",
) {
    fun isOpenRouter(): Boolean =
        cloudBaseUrl.contains("openrouter.ai", ignoreCase = true)

    companion object {
        const val DEFAULT_BASE_URL = "https://openrouter.ai/api/v1"
        const val DEFAULT_ASR_MODEL = "openai/whisper-1"
        const val DEFAULT_CLEANUP_MODEL = "google/gemini-2.5-flash"
        const val DEFAULT_SUMMARY_MODEL = "anthropic/claude-sonnet-4"
        /** @deprecated Prefer [DEFAULT_CLEANUP_MODEL] / [DEFAULT_SUMMARY_MODEL]. */
        const val DEFAULT_CHAT_MODEL = DEFAULT_CLEANUP_MODEL

        val SUGGESTED_ASR_MODELS = listOf(
            "openai/whisper-1",
            "openai/whisper-large-v3",
        )

        val SUGGESTED_CLEANUP_MODELS = listOf(
            "google/gemini-2.5-flash",
            "openai/gpt-4o-mini",
            "google/gemini-2.5-flash-lite",
        )

        val SUGGESTED_SUMMARY_MODELS = listOf(
            "anthropic/claude-sonnet-4",
            "openai/gpt-4o",
            "deepseek/deepseek-r1",
            "google/gemini-2.5-pro",
        )

        val SUGGESTED_CHAT_MODELS = SUGGESTED_CLEANUP_MODELS + SUGGESTED_SUMMARY_MODELS
    }
}

class PipelineConfig(context: Context) {
    private val appContext = context.applicationContext

    private val prefs by lazy {
        try {
            val masterKey = MasterKey.Builder(appContext)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build()
            EncryptedSharedPreferences.create(
                appContext,
                "pipeline_secure",
                masterKey,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM,
            )
        } catch (_: Throwable) {
            appContext.getSharedPreferences("pipeline_secure_fallback", Context.MODE_PRIVATE)
        }
    }

    fun load(): PipelineSettings = PipelineSettings(
        asrMode = parseMode(prefs.getString(KEY_ASR_MODE, null)),
        cleanupMode = parseMode(prefs.getString(KEY_CLEANUP_MODE, null)),
        summaryMode = parseMode(prefs.getString(KEY_SUMMARY_MODE, null)
            ?: prefs.getString(KEY_CLEANUP_MODE, null)),
        actionsMode = parseMode(prefs.getString(KEY_ACTIONS_MODE, null)
            ?: prefs.getString(KEY_SUMMARY_MODE, null)
            ?: prefs.getString(KEY_CLEANUP_MODE, null)),
        cloudBaseUrl = PipelineSettings.DEFAULT_BASE_URL,
        cloudApiKey = prefs.getString(KEY_API_KEY, null).orEmpty(),
        cloudAsrModel = OpenRouterModels.normalizeId(
            prefs.getString(KEY_ASR_MODEL, null) ?: PipelineSettings.DEFAULT_ASR_MODEL,
        ),
        cloudCleanupModel = OpenRouterModels.normalizeId(
            prefs.getString(KEY_CLEANUP_MODEL, null)
                ?: prefs.getString(KEY_CHAT_MODEL, null)
                ?: PipelineSettings.DEFAULT_CLEANUP_MODEL,
        ),
        cloudSummaryModel = OpenRouterModels.normalizeId(
            prefs.getString(KEY_SUMMARY_MODEL, null)
                ?: prefs.getString(KEY_CHAT_MODEL, null)
                ?: PipelineSettings.DEFAULT_SUMMARY_MODEL,
        ),
        reasoningEffort = prefs.getString(KEY_REASONING_EFFORT, null).orEmpty(),
        excludeReasoningFromResponse = prefs.getBoolean(KEY_EXCLUDE_REASONING, true),
        sttLanguage = prefs.getString(KEY_STT_LANGUAGE, null).orEmpty(),
        localAsrModelId = prefs.getString(KEY_LOCAL_ASR, null) ?: "parakeet_tdt",
        localCleanupModelId = prefs.getString(KEY_LOCAL_CLEANUP, null) ?: "gemma-4b",
        cleanupPrompt = prefs.getString(KEY_CLEANUP_PROMPT, null).orEmpty(),
        summaryPrompt = prefs.getString(KEY_SUMMARY_PROMPT, null).orEmpty(),
        glossary = prefs.getString(KEY_GLOSSARY, null).orEmpty(),
    )

    fun save(settings: PipelineSettings) {
        prefs.edit {
            putString(KEY_ASR_MODE, settings.asrMode.name)
            putString(KEY_CLEANUP_MODE, settings.cleanupMode.name)
            putString(KEY_SUMMARY_MODE, settings.summaryMode.name)
            putString(KEY_ACTIONS_MODE, settings.actionsMode.name)
            putString(KEY_BASE_URL, PipelineSettings.DEFAULT_BASE_URL)
            putString(KEY_API_KEY, settings.cloudApiKey.trim())
            putString(KEY_ASR_MODEL, OpenRouterModels.normalizeId(settings.cloudAsrModel))
            putString(KEY_CLEANUP_MODEL, OpenRouterModels.normalizeId(settings.cloudCleanupModel))
            putString(KEY_SUMMARY_MODEL, OpenRouterModels.normalizeId(settings.cloudSummaryModel))
            // Keep legacy key in sync for older builds / debug extras.
            putString(KEY_CHAT_MODEL, OpenRouterModels.normalizeId(settings.cloudCleanupModel))
            putString(KEY_REASONING_EFFORT, settings.reasoningEffort.trim())
            putBoolean(KEY_EXCLUDE_REASONING, settings.excludeReasoningFromResponse)
            putString(KEY_STT_LANGUAGE, settings.sttLanguage.trim())
            putString(KEY_LOCAL_ASR, settings.localAsrModelId.trim())
            putString(KEY_LOCAL_CLEANUP, settings.localCleanupModelId.trim())
            putString(KEY_CLEANUP_PROMPT, settings.cleanupPrompt)
            putString(KEY_SUMMARY_PROMPT, settings.summaryPrompt)
            putString(KEY_GLOSSARY, settings.glossary)
        }
    }

    fun cloudConfigured(): Boolean {
        val s = load()
        return s.cloudApiKey.isNotBlank() && s.cloudBaseUrl.isNotBlank()
    }

    companion object {
        private const val KEY_ASR_MODE = "asr_mode"
        private const val KEY_CLEANUP_MODE = "cleanup_mode"
        private const val KEY_SUMMARY_MODE = "summary_mode"
        private const val KEY_ACTIONS_MODE = "actions_mode"
        private const val KEY_BASE_URL = "cloud_base_url"
        private const val KEY_API_KEY = "cloud_api_key"
        private const val KEY_ASR_MODEL = "cloud_asr_model"
        private const val KEY_CLEANUP_MODEL = "cloud_cleanup_model"
        private const val KEY_SUMMARY_MODEL = "cloud_summary_model"
        /** Legacy single chat model; still read as fallback for both stages. */
        private const val KEY_CHAT_MODEL = "cloud_chat_model"
        private const val KEY_REASONING_EFFORT = "reasoning_effort"
        private const val KEY_EXCLUDE_REASONING = "exclude_reasoning"
        private const val KEY_STT_LANGUAGE = "stt_language"
        private const val KEY_LOCAL_ASR = "local_asr_model"
        private const val KEY_LOCAL_CLEANUP = "local_cleanup_model"
        private const val KEY_CLEANUP_PROMPT = "cleanup_prompt"
        private const val KEY_SUMMARY_PROMPT = "summary_prompt"
        private const val KEY_GLOSSARY = "glossary"

        /** Maps legacy ONLY_* values onto the two prefer modes. */
        fun parseMode(raw: String?): ProviderMode =
            when (raw) {
                null, "" -> ProviderMode.PREFER_CLOUD
                "ONLY_CLOUD", ProviderMode.PREFER_CLOUD.name -> ProviderMode.PREFER_CLOUD
                "ONLY_LOCAL", ProviderMode.PREFER_LOCAL.name -> ProviderMode.PREFER_LOCAL
                else -> runCatching { ProviderMode.valueOf(raw) }.getOrDefault(ProviderMode.PREFER_CLOUD)
            }
    }
}
