package com.varun.pocketassistant.pipeline

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.util.concurrent.TimeUnit

data class OpenRouterModelInfo(
    val id: String,
    val name: String,
    val supportsReasoning: Boolean = false,
    val supportedEfforts: List<String> = emptyList(),
    val defaultEffort: String? = null,
    val reasoningMandatory: Boolean = false,
)

/**
 * OpenRouter model-id helpers.
 *
 * "Latest" aliases use a leading `~` (e.g. `~deepseek/deepseek-v4-flash-latest`).
 * Stripping that tilde makes the id invalid.
 */
object OpenRouterModels {
    fun normalizeId(raw: String): String {
        val trimmed = raw.trim()
        if (trimmed.isEmpty()) return trimmed
        // Keep intentional ~ aliases; only strip a lone leading colon typo.
        val id = if (trimmed.startsWith(':') && !trimmed.startsWith("://")) {
            trimmed.removePrefix(":")
        } else {
            trimmed
        }
        // Saved prefs may have lost the required ~ from an older bug.
        if (!id.startsWith('~') && id.contains("-latest")) {
            return "~$id"
        }
        return id
    }

    fun matches(a: String, b: String): Boolean {
        val left = normalizeId(a)
        val right = normalizeId(b)
        if (left == right) return true
        return left.trimStart('~') == right.trimStart('~')
    }
}

enum class ReasoningEffort(val apiValue: String, val label: String) {
    DEFAULT("", "Model default"),
    NONE("none", "None"),
    MINIMAL("minimal", "Minimal"),
    LOW("low", "Low"),
    MEDIUM("medium", "Medium"),
    HIGH("high", "High"),
    XHIGH("xhigh", "X-High"),
    MAX("max", "Max"),
    ;

    companion object {
        fun fromStored(value: String): ReasoningEffort =
            entries.firstOrNull { it.apiValue == value.trim() } ?: DEFAULT

        val selectable = entries
    }
}

/**
 * Fetches live model catalogs from OpenRouter (or any OpenAI-compatible `/models` host).
 *
 * [apiKey] / [baseUrl] override the saved [PipelineConfig] so the settings UI can load
 * models before the user taps Save.
 */
class OpenRouterModelsClient(
    private val config: PipelineConfig,
) {
    private val http: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .dns(ResilientDns)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .build()
    }

    suspend fun listTranscriptionModels(
        apiKey: String? = null,
        baseUrl: String? = null,
    ): List<OpenRouterModelInfo> = withContext(Dispatchers.IO) {
        val filtered = fetchModels(
            query = "output_modalities=transcription",
            apiKey = apiKey,
            baseUrl = baseUrl,
        )
        if (filtered.isNotEmpty()) return@withContext filtered
        // Fallback: some gateways ignore the modality filter.
        fetchModels(query = null, apiKey = apiKey, baseUrl = baseUrl)
            .filter { model ->
                val id = model.id.lowercase()
                id.contains("whisper") ||
                    id.contains("transcri") ||
                    id.contains("speech") ||
                    id.contains("asr")
            }
    }

    suspend fun listChatModels(
        apiKey: String? = null,
        baseUrl: String? = null,
    ): List<OpenRouterModelInfo> = withContext(Dispatchers.IO) {
        val text = runCatching {
            fetchModels(query = "output_modalities=text", apiKey = apiKey, baseUrl = baseUrl)
        }.getOrDefault(emptyList())
        val catalog = text.ifEmpty {
            fetchModels(query = null, apiKey = apiKey, baseUrl = baseUrl)
        }
        catalog.filter { model ->
            val id = model.id.lowercase()
            !id.contains("whisper") &&
                !id.contains("tts") &&
                !id.contains("embed") &&
                !id.contains("moderation") &&
                !id.contains("transcri")
        }
    }

    private fun fetchModels(
        query: String?,
        apiKey: String? = null,
        baseUrl: String? = null,
    ): List<OpenRouterModelInfo> {
        val saved = config.load()
        val key = apiKey?.trim()?.ifBlank { null } ?: saved.cloudApiKey.trim()
        val base = (baseUrl?.trim()?.ifBlank { null } ?: saved.cloudBaseUrl)
            .trimEnd('/')
        require(key.isNotBlank()) { "Cloud API key not set" }
        require(base.isNotBlank()) { "Cloud base URL not set" }
        val url = if (query.isNullOrBlank()) {
            "$base/models"
        } else {
            "$base/models?$query"
        }
        val isOpenRouter = base.contains("openrouter.ai", ignoreCase = true)
        val builder = Request.Builder()
            .url(url)
            .get()
            .header("Authorization", "Bearer $key")
        if (isOpenRouter) {
            builder.header("HTTP-Referer", "https://github.com/varunramesh/pocket-assistant")
            builder.header("X-Title", "Pocket Assistant")
        }
        http.newCall(builder.build()).execute().use { response ->
            val body = response.body?.string().orEmpty()
            if (!response.isSuccessful) {
                throw IllegalStateException("Models HTTP ${response.code}: $body")
            }
            val data = JSONObject(body).optJSONArray("data") ?: return emptyList()
            val out = ArrayList<OpenRouterModelInfo>(data.length())
            for (i in 0 until data.length()) {
                val obj = data.optJSONObject(i) ?: continue
                val id = obj.optString("id").trim()
                if (id.isEmpty()) continue
                val reasoning = obj.optJSONObject("reasoning")
                val efforts = mutableListOf<String>()
                reasoning?.optJSONArray("supported_efforts")?.let { arr ->
                    for (j in 0 until arr.length()) {
                        arr.optString(j).takeIf { it.isNotBlank() }?.let { efforts += it }
                    }
                }
                out += OpenRouterModelInfo(
                    id = id,
                    name = obj.optString("name").ifBlank { id },
                    supportsReasoning = reasoning != null,
                    supportedEfforts = efforts,
                    defaultEffort = reasoning?.optString("default_effort")?.takeIf { it.isNotBlank() },
                    reasoningMandatory = reasoning?.optBoolean("mandatory", false) == true,
                )
            }
            return out.sortedBy { it.id.lowercase() }
        }
    }
}
