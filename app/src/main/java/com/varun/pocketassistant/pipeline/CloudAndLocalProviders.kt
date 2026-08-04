package com.varun.pocketassistant.pipeline

import android.util.Base64
import android.util.Log
import kotlinx.coroutines.delay
import kotlinx.coroutines.suspendCancellableCoroutine
import okhttp3.Call
import okhttp3.Callback
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.io.IOException
import java.io.InterruptedIOException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

/**
 * OpenAI-compatible + OpenRouter HTTP client for STT and chat completions.
 *
 * Uses OkHttp (more reliable DNS / TLS than HttpURLConnection on Android).
 */
class OpenAiCompatibleClient(
    private val config: PipelineConfig,
) {
    private val asrHttp: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .dns(ResilientDns)
            .connectTimeout(PipelineTelemetry.HTTP_CONNECT_TIMEOUT_MS, TimeUnit.MILLISECONDS)
            .readTimeout(PipelineTelemetry.ASR_HTTP_READ_TIMEOUT_MS, TimeUnit.MILLISECONDS)
            .writeTimeout(PipelineTelemetry.ASR_HTTP_READ_TIMEOUT_MS, TimeUnit.MILLISECONDS)
            .callTimeout(PipelineTelemetry.ASR_HTTP_CALL_TIMEOUT_MS, TimeUnit.MILLISECONDS)
            .retryOnConnectionFailure(true)
            .build()
    }

    private val chatHttp: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .dns(ResilientDns)
            .connectTimeout(PipelineTelemetry.HTTP_CONNECT_TIMEOUT_MS, TimeUnit.MILLISECONDS)
            .readTimeout(PipelineTelemetry.CHAT_HTTP_READ_TIMEOUT_MS, TimeUnit.MILLISECONDS)
            .writeTimeout(PipelineTelemetry.CHAT_HTTP_READ_TIMEOUT_MS, TimeUnit.MILLISECONDS)
            .callTimeout(PipelineTelemetry.CHAT_HTTP_CALL_TIMEOUT_MS, TimeUnit.MILLISECONDS)
            .retryOnConnectionFailure(true)
            .build()
    }

    suspend fun transcribeAudio(wav: File): String {
        val settings = config.load()
        require(settings.cloudApiKey.isNotBlank()) { "Cloud API key not set" }
        return withRetries(label = "Cloud ASR", host = hostOf(settings.cloudBaseUrl)) {
            if (settings.isOpenRouter() || wav.length() > MULTIPART_SAFE_BYTES) {
                transcribeOpenRouterJson(wav, settings)
            } else {
                transcribeMultipart(wav, settings)
            }
        }
    }

    private suspend fun transcribeOpenRouterJson(wav: File, settings: PipelineSettings): String {
        val bytes = wav.readBytes()
        val b64 = Base64.encodeToString(bytes, Base64.NO_WRAP)
        val format = wav.extension.lowercase().ifBlank { "wav" }
        val payload = JSONObject()
            .put("model", OpenRouterModels.normalizeId(settings.cloudAsrModel))
            .put(
                "input_audio",
                JSONObject()
                    .put("data", b64)
                    .put("format", format),
            )
        val lang = settings.sttLanguage.trim()
        if (lang.isNotEmpty()) payload.put("language", lang)

        val request = baseRequest(settings, "${settings.cloudBaseUrl.trimEnd('/')}/audio/transcriptions")
            .post(payload.toString().toRequestBody(JSON))
            .build()
        Log.i(TAG, "ASR POST json bytes=${bytes.size} model=${settings.cloudAsrModel}")
        return readTranscriptionResponse(execute(asrHttp, request), "Cloud ASR")
    }

    private suspend fun transcribeMultipart(wav: File, settings: PipelineSettings): String {
        val bodyBuilder = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("model", OpenRouterModels.normalizeId(settings.cloudAsrModel))
            .addFormDataPart("response_format", "json")
        val lang = settings.sttLanguage.trim()
        if (lang.isNotEmpty()) bodyBuilder.addFormDataPart("language", lang)
        bodyBuilder.addFormDataPart(
            "file",
            wav.name,
            wav.asRequestBody("audio/wav".toMediaType()),
        )
        val request = baseRequest(settings, "${settings.cloudBaseUrl.trimEnd('/')}/audio/transcriptions")
            .post(bodyBuilder.build())
            .build()
        Log.i(TAG, "ASR POST multipart bytes=${wav.length()} model=${settings.cloudAsrModel}")
        return readTranscriptionResponse(execute(asrHttp, request), "Cloud ASR")
    }

    suspend fun chat(
        userPrompt: String,
        systemPrompt: String = DEFAULT_SYSTEM,
        model: String? = null,
        applyReasoning: Boolean = false,
    ): String {
        val settings = config.load()
        require(settings.cloudApiKey.isNotBlank()) { "Cloud API key not set" }
        val modelId = (model?.trim()?.ifBlank { null }
            ?: settings.cloudCleanupModel.ifBlank { PipelineSettings.DEFAULT_CLEANUP_MODEL })
            .let { OpenRouterModels.normalizeId(it) }
        return withRetries(
            label = "Cloud chat model=$modelId inChars=${userPrompt.length}",
            host = hostOf(settings.cloudBaseUrl),
            maxAttempts = CHAT_MAX_ATTEMPTS,
        ) {
            chatOnce(
                settings = settings,
                modelId = modelId,
                userPrompt = userPrompt,
                systemPrompt = systemPrompt,
                applyReasoning = applyReasoning,
            )
        }
    }

    private suspend fun chatOnce(
        settings: PipelineSettings,
        modelId: String,
        userPrompt: String,
        systemPrompt: String,
        applyReasoning: Boolean,
    ): String {
        val messages = JSONArray()
            .put(
                JSONObject()
                    .put("role", "system")
                    .put("content", systemPrompt),
            )
            .put(
                JSONObject()
                    .put("role", "user")
                    .put("content", userPrompt),
            )
        val payload = JSONObject()
            .put("model", modelId)
            .put("messages", messages)
            .put("temperature", 0.2)
        if (applyReasoning) {
            applyReasoning(payload, settings)
        }
        val bodyBytes = payload.toString().toByteArray(Charsets.UTF_8)
        val request = baseRequest(settings, "${settings.cloudBaseUrl.trimEnd('/')}/chat/completions")
            .post(bodyBytes.toRequestBody(JSON))
            .build()
        val started = System.currentTimeMillis()
        Log.i(
            TAG,
            "CHAT POST model=$modelId promptChars=${userPrompt.length} bodyBytes=${bodyBytes.size} reasoning=$applyReasoning",
        )
        val body = execute(chatHttp, request)
        Log.i(TAG, "CHAT ok model=$modelId elapsedMs=${System.currentTimeMillis() - started} respChars=${body.length}")
        val message = JSONObject(body)
            .getJSONArray("choices")
            .getJSONObject(0)
            .getJSONObject("message")
        val content = message.optString("content").trim()
        return content.ifBlank { error("Cloud chat returned empty content") }
    }

    private fun applyReasoning(payload: JSONObject, settings: PipelineSettings) {
        val effort = settings.reasoningEffort.trim()
        if (effort.isEmpty() && !settings.excludeReasoningFromResponse) return
        if (effort.isEmpty() && settings.excludeReasoningFromResponse) {
            return
        }
        val reasoning = JSONObject()
        if (effort.isNotEmpty()) {
            reasoning.put("effort", effort)
            if (effort != "none") {
                reasoning.put("enabled", true)
            }
        }
        if (settings.excludeReasoningFromResponse && effort.isNotEmpty() && effort != "none") {
            reasoning.put("exclude", true)
        }
        if (reasoning.length() > 0) {
            payload.put("reasoning", reasoning)
        }
    }

    /** @deprecated Use [chat]. */
    suspend fun chatCleanup(prompt: String): String = chat(prompt)

    private fun baseRequest(settings: PipelineSettings, url: String): Request.Builder {
        val builder = Request.Builder()
            .url(url)
            .header("Authorization", "Bearer ${settings.cloudApiKey}")
        if (settings.isOpenRouter()) {
            builder.header("HTTP-Referer", OPENROUTER_REFERER)
            builder.header("X-Title", OPENROUTER_TITLE)
        }
        return builder
    }

    /**
     * Cancellable OkHttp call. Blocking [okhttp3.Call.execute] ignores coroutine
     * cancellation — that let cleanup hang ~11 min past the stage timeout.
     */
    private suspend fun execute(client: OkHttpClient, request: Request): String =
        suspendCancellableCoroutine { cont ->
            val call = client.newCall(request)
            cont.invokeOnCancellation { call.cancel() }
            call.enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    if (cont.isActive) cont.resumeWithException(e)
                }

                override fun onResponse(call: Call, response: Response) {
                    response.use { resp ->
                        val body = resp.body?.string().orEmpty()
                        if (!cont.isActive) return
                        if (!resp.isSuccessful) {
                            cont.resumeWithException(
                                IllegalStateException(
                                    formatCloudHttpError("Cloud", resp.code, body),
                                ),
                            )
                        } else {
                            cont.resume(body)
                        }
                    }
                }
            })
        }

    private fun readTranscriptionResponse(body: String, label: String): String {
        val text = JSONObject(body).optString("text").trim()
        Log.i(TAG, "$label ok (${text.length} chars)")
        return text
    }

    private suspend fun <T> withRetries(
        label: String,
        host: String,
        maxAttempts: Int = MAX_ATTEMPTS,
        block: suspend () -> T,
    ): T {
        var last: Throwable? = null
        repeat(maxAttempts) { attempt ->
            try {
                return block()
            } catch (t: Throwable) {
                last = t
                if (!isRetryable(t) || attempt == maxAttempts - 1) {
                    throw humanizeNetworkError(label, host, t)
                }
                val delayMs = if (isDnsFailure(t)) {
                    (2_000L * (attempt + 1)).coerceAtMost(8_000L)
                } else {
                    (1_000L * (1 shl attempt)).coerceAtMost(6_000L)
                }
                Log.w(
                    TAG,
                    "$label attempt ${attempt + 1}/$maxAttempts failed (${t.javaClass.simpleName}: ${t.message}); retry in ${delayMs}ms",
                )
                delay(delayMs)
            }
        }
        throw humanizeNetworkError(label, host, last ?: error("$label failed"))
    }

    companion object {
        private const val TAG = "OpenAiClient"
        private const val MULTIPART_SAFE_BYTES = 20L * 1024L * 1024L
        private const val OPENROUTER_REFERER = "https://github.com/varunramesh/pocket-assistant"
        private const val OPENROUTER_TITLE = "Pocket Assistant"
        private const val DEFAULT_SYSTEM =
            "You clean speech transcripts and summarize meetings. Output only the requested Markdown or text."
        private const val MAX_ATTEMPTS = 3
        private const val CHAT_MAX_ATTEMPTS = 3
        private val JSON = "application/json; charset=utf-8".toMediaType()

        private fun hostOf(baseUrl: String): String {
            val trimmed = baseUrl.trim().trimEnd('/')
            return "$trimmed/".toHttpUrlOrNull()?.host
                ?: trimmed.removePrefix("https://").removePrefix("http://").substringBefore('/')
        }

        private fun isDnsFailure(t: Throwable): Boolean =
            generateSequence(t) { it.cause }.any {
                it is UnknownHostException ||
                    it.message.orEmpty().contains("Unable to resolve host", ignoreCase = true) ||
                    it.message.orEmpty().contains("No address associated with hostname", ignoreCase = true)
            }

        private fun isRetryable(t: Throwable): Boolean {
            generateSequence(t) { it.cause }.forEach { err ->
                when (err) {
                    is SocketException,
                    is SocketTimeoutException,
                    is InterruptedIOException,
                    is UnknownHostException,
                    is SSLException,
                    -> return true
                }
                val msg = err.message.orEmpty()
                if (msg.contains("connection abort", ignoreCase = true) ||
                    msg.contains("Broken pipe", ignoreCase = true) ||
                    msg.contains("Connection reset", ignoreCase = true) ||
                    msg.contains("failed to connect", ignoreCase = true) ||
                    msg.contains("Unable to resolve host", ignoreCase = true) ||
                    msg.contains("No address associated with hostname", ignoreCase = true)
                ) {
                    return true
                }
                if (err is IllegalStateException) {
                    if (msg.contains("HTTP 429") ||
                        msg.contains("HTTP 502") ||
                        msg.contains("HTTP 503") ||
                        msg.contains("HTTP 504")
                    ) {
                        return true
                    }
                }
            }
            return false
        }

        private fun humanizeNetworkError(label: String, host: String, t: Throwable): Throwable {
            if (isDnsFailure(t)) {
                return IllegalStateException(
                    "$label can’t resolve $host right now. " +
                        "Toggle Airplane mode, disable Private DNS/VPN, or switch Wi‑Fi↔cellular, then Retry.",
                    t,
                )
            }
            val msg = t.message.orEmpty()
            if (msg.contains("connection abort", ignoreCase = true) || t is SocketException) {
                return IllegalStateException(
                    "$label network interrupted ($msg). Tap Retry — usually a flaky Wi‑Fi/VPN hop.",
                    t,
                )
            }
            return t
        }

        private fun formatCloudHttpError(label: String, code: Int, body: String): String {
            val message = runCatching {
                JSONObject(body)
                    .optJSONObject("error")
                    ?.optString("message")
                    ?.takeIf { it.isNotBlank() }
            }.getOrNull()
            val tip = when {
                message == null -> null
                message.contains("not a valid model", ignoreCase = true) ->
                    " Re-pick the model in Pipeline (OpenRouter “-latest” aliases need a leading ~)."
                code == 429 -> " Rate limited — retry shortly or pick another model."
                else -> null
            }
            return buildString {
                append(label)
                append(" HTTP ")
                append(code)
                append(": ")
                append(message ?: body.take(240))
                tip?.let { append(it) }
            }
        }
    }
}

class CloudAsrProvider(
    private val context: android.content.Context,
    private val config: PipelineConfig,
    private val client: OpenAiCompatibleClient,
) : AsrProvider {
    override val id: String = "cloud_stt"

    override fun isAvailable(): Boolean = config.cloudConfigured()

    override suspend fun transcribe(wav: File): AsrResult {
        val app = context.applicationContext
        val prepDir = File(app.cacheDir, "cloud_asr_prep").also { it.mkdirs() }
        // Bench: silence-only (no 1.35×) cut OpenRouter WER ~29% → ~20% on meeting parts.
        val prep = AsrAudioPreprocessor.prepareForCloud(wav, prepDir)
        if (prep.processedDurationMs <= 0L || prep.file.length() < 44 + 16_000) {
            error("Cloud ASR: no speech left after silence trim")
        }
        val chunks = AsrAudioPreprocessor.splitByDurationMs(
            inputWav = prep.file,
            outputDir = prepDir,
            maxDurationMs = AsrAudioPreprocessor.CLOUD_CHUNK_MS,
        )
        require(chunks.isNotEmpty()) { "Cloud ASR: empty chunk list" }
        val parts = chunks.mapIndexed { i, chunk ->
            Log.i(
                "CloudAsr",
                "chunk ${i + 1}/${chunks.size}: ${chunk.name} ${chunk.length()} bytes",
            )
            client.transcribeAudio(chunk).trim()
        }
        val text = parts.filter { it.isNotBlank() }.joinToString("\n\n")
        if (text.isBlank()) error("Cloud ASR returned empty transcript")
        val model = config.load().cloudAsrModel
        return AsrResult(
            plain = text,
            diarized = null,
            providerId = "$id:$model${if (chunks.size > 1) ":${chunks.size}ch" else ""}",
        )
    }
}

class CloudCleanupProvider(
    private val config: PipelineConfig,
    private val client: OpenAiCompatibleClient,
) : MeetingTextProvider {
    override val id: String = "cloud_chat"

    override fun isAvailable(): Boolean = config.cloudConfigured()

    override suspend fun cleanTranscript(rawTranscript: String, diarized: Boolean): String {
        val settings = config.load()
        return client.chat(
            userPrompt = CleanupPrompts.buildCleanup(settings, rawTranscript, diarized),
            systemPrompt = "You clean speech transcripts. Output only the cleaned transcript text.",
            model = settings.cloudCleanupModel,
            applyReasoning = false,
        )
    }

    override suspend fun summarize(cleanedTranscript: String): String {
        val settings = config.load()
        return client.chat(
            userPrompt = CleanupPrompts.buildSummary(settings, cleanedTranscript),
            systemPrompt = "You summarize meetings. Output only the requested Markdown sections.",
            model = settings.cloudSummaryModel,
            applyReasoning = true,
        )
    }

    override suspend fun prompt(system: String, user: String): String {
        val settings = config.load()
        return client.chat(
            userPrompt = user,
            systemPrompt = system,
            model = settings.cloudSummaryModel,
            applyReasoning = true,
        )
    }
}

/**
 * Local Parakeet/LiteRT TDT ASR (Tensor G5 NPU when model_npu.tflite is present).
 */
class ParakeetAsrProvider(
    private val context: android.content.Context,
) : AsrProvider {
    override val id: String = "parakeet_tdt"

    private val engine by lazy { ParakeetAsrEngine(context.applicationContext) }

    override fun isAvailable(): Boolean = ParakeetLocalModels.isAvailable(context.applicationContext)

    override suspend fun transcribe(wav: File): AsrResult {
        val text = engine.transcribeSuspend(wav)
        if (text.isBlank()) error("Parakeet returned empty transcript")
        return AsrResult(plain = text, diarized = null, providerId = engine.providerLabel)
    }
}

/**
 * Local Gemma 4 E2B cleanup / summary via LiteRT-LM (Tensor G5 NPU pack).
 */
class GemmaCleanupProvider(
    private val context: android.content.Context,
    private val config: PipelineConfig,
) : MeetingTextProvider {
    override val id: String = "gemma_4_e2b"

    override fun isAvailable(): Boolean = GemmaLocalModels.isAvailable(context.applicationContext)

    override suspend fun cleanTranscript(rawTranscript: String, diarized: Boolean): String =
        GemmaLocalModels.cleanTranscript(
            context = context.applicationContext,
            raw = rawTranscript,
            diarized = diarized,
            settings = config.load(),
        )

    override suspend fun summarize(cleanedTranscript: String): String =
        GemmaLocalModels.summarize(
            context = context.applicationContext,
            cleaned = cleanedTranscript,
            settings = config.load(),
        )

    override suspend fun prompt(system: String, user: String): String =
        GemmaLocalModels.prompt(
            context = context.applicationContext,
            system = system,
            user = user,
        )
}
