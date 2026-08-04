package com.varun.pocketassistant.pipeline

import android.content.Context
import android.util.Log
import com.google.ai.edge.litertlm.Backend
import com.google.ai.edge.litertlm.Content
import com.google.ai.edge.litertlm.Contents
import com.google.ai.edge.litertlm.ConversationConfig
import com.google.ai.edge.litertlm.Engine
import com.google.ai.edge.litertlm.EngineConfig
import com.google.ai.edge.litertlm.SamplerConfig
import java.io.File
import java.util.concurrent.atomic.AtomicReference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext

object GemmaLocalModels {
    const val REL_DIR = "models/gemma4b"
    const val MODEL_NAME = "gemma-4-E2B-it_Google_Tensor_G5.litertlm"

    fun dir(context: Context): File = File(context.filesDir, REL_DIR)

    fun modelFile(context: Context): File = File(dir(context), MODEL_NAME)

    fun isAvailable(context: Context): Boolean {
        val f = modelFile(context)
        return f.exists() && f.length() > 1_000_000_000L
    }

    private val engineRef = AtomicReference<Engine?>(null)
    private val mutex = Mutex()

    suspend fun cleanTranscript(
        context: Context,
        raw: String,
        diarized: Boolean,
        settings: PipelineSettings,
    ): String = prompt(
        context = context,
        system = "You clean speech transcripts. Output only the cleaned transcript text.",
        user = CleanupPrompts.buildCleanup(settings, raw, diarized),
    )

    suspend fun summarize(
        context: Context,
        cleaned: String,
        settings: PipelineSettings,
    ): String = prompt(
        context = context,
        system = "You summarize meetings. Output only the requested Markdown sections.",
        user = CleanupPrompts.buildSummary(settings, cleaned),
    )

    suspend fun prompt(
        context: Context,
        system: String,
        user: String,
    ): String =
        withContext(Dispatchers.Default) {
            mutex.withLock {
                val app = context.applicationContext
                require(isAvailable(app)) { "Gemma G5 model pack missing under files/$REL_DIR" }
                val engine = engineRef.get() ?: createEngine(app).also { engineRef.set(it) }
                runPrompt(engine, system, user)
            }
        }

    private fun runPrompt(engine: Engine, system: String, user: String): String {
        engine.createConversation(
            ConversationConfig(
                systemInstruction = Contents.of(system),
                samplerConfig = SamplerConfig(
                    topK = 40,
                    topP = 0.95,
                    temperature = 0.2,
                    seed = 0,
                ),
            ),
        ).use { conv ->
            val reply = conv.sendMessage(user)
            return messageText(reply).ifBlank { error("Gemma returned empty text") }
        }
    }

    private fun messageText(message: com.google.ai.edge.litertlm.Message): String =
        message.contents.contents
            .mapNotNull { content ->
                (content as? Content.Text)?.text
            }
            .joinToString("")
            .trim()

    private fun createEngine(context: Context): Engine {
        val path = modelFile(context).absolutePath
        Log.i(TAG, "Initializing LiteRT-LM NPU engine: $path")
        val config = EngineConfig(
            modelPath = path,
            backend = Backend.NPU(context.applicationInfo.nativeLibraryDir),
            cacheDir = context.cacheDir.absolutePath,
        )
        return Engine(config).also { it.initialize() }
    }

    private const val TAG = "GemmaLocalModels"
}
