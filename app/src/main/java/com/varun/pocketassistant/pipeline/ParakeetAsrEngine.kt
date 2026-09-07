package com.varun.pocketassistant.pipeline

import android.content.Context
import android.os.Build
import android.system.Os
import android.util.Log
import com.google.ai.edge.examples.asr.FileAudioSource
import com.google.ai.edge.examples.asr.HuggingfaceTokenizer
import com.google.ai.edge.examples.asr.LevenshteinTokenMerger
import com.google.ai.edge.examples.asr.LiteRtRunner
import com.google.ai.edge.examples.asr.LogMelSpectroConfig
import com.google.ai.edge.examples.asr.MelSpectroProcessor
import com.google.ai.edge.examples.asr.ModelConfig
import com.google.ai.edge.examples.asr.TdtDecoder
import com.google.ai.edge.litert.Accelerator
import com.google.ai.edge.litert.BuiltinNpuAcceleratorProvider
import com.google.ai.edge.litert.LiteRtException
import com.google.ai.edge.litert.NpuCompatibilityChecker
import java.io.File
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

/**
 * On-device Parakeet TDT ASR via LiteRT on Google Tensor TPU.
 *
 * Follows google-ai-edge/litert-samples ASR file path:
 * - 5s model window, 2s overlap (MainActivity.FILE_AUDIO_CHUNK_OVERLAP_DURATION)
 * - Levenshtein merge distance 5 (MainActivity.FILE_MAX_LEVENSHTEIN_DISTANCE)
 * - Upstream LiteRtRunner + TdtDecoder
 *
 * Device-required deltas vs stock sample (documented):
 * - npuOnly=true for AOT Tensor G5 packs
 * - TdtDecoder resets RNN state index per window
 * - Single-flight + single-thread native access (NativeEngineGate); abort the
 *   whole file on a mid-file NPU invoke flake instead of reload-and-continue
 *   (reloading a shared in-use CompiledModel is the SIGSEGV source)
 * - Explicit accelerator selection (ParakeetAccelerator.NPU default; CPU uses
 *   the pinned i8 export, gated only on CPU weights + tokenizer — no NPU checks)
 */

/** Which LiteRT accelerator the engine compiles the Parakeet model for. */
enum class ParakeetAccelerator { NPU, CPU }

object ParakeetLocalModels {
    const val REL_DIR = "models/parakeet"
    const val CPU_MODEL = "model.tflite"
    const val NPU_MODEL = "model_npu.tflite"
    const val TOKENIZER = "tokenizer.json"

    /**
     * The pinned CPU export (parakeet_tdt_0.6b_v3_5s_i8_stateful.tflite,
     * staged as model.tflite) is ~614 MB. Require a real weight file, not a
     * placeholder — a CPU_MODEL constant is not a working fallback.
     */
    private const val CPU_MODEL_MIN_BYTES = 500_000_000L

    fun dir(context: Context): File = File(context.filesDir, REL_DIR)

    fun tokenizerFile(context: Context): File = File(dir(context), TOKENIZER)

    fun npuModel(context: Context): File = File(dir(context), NPU_MODEL)

    fun cpuModel(context: Context): File = File(dir(context), CPU_MODEL)

    fun hasTokenizer(context: Context): Boolean =
        tokenizerFile(context).exists() && tokenizerFile(context).length() > 1_000L

    fun hasNpuModel(context: Context): Boolean =
        npuModel(context).exists() && npuModel(context).length() > 1_000_000L

    fun hasCpuModel(context: Context): Boolean =
        cpuModel(context).exists() && cpuModel(context).length() >= CPU_MODEL_MIN_BYTES

    fun isAvailable(context: Context): Boolean =
        hasTokenizer(context) &&
            hasNpuModel(context) &&
            NpuCompatibilityChecker.GoogleTensor.isDeviceSupported()

    /** CPU availability depends only on tokenizer + CPU weights — no NPU checks. */
    fun isCpuAvailable(context: Context): Boolean =
        hasTokenizer(context) && hasCpuModel(context)

    fun modelConfig(context: Context): ModelConfig {
        return ModelConfig(
            modelPath = "$REL_DIR/$CPU_MODEL",
            modelRemoteUrl = "",
            npuModelPath = "$REL_DIR/$NPU_MODEL",
            npuModelRemoteUrlPattern = "",
            tokenizerPath = "$REL_DIR/$TOKENIZER",
            tokenizerRemoteUrl = "",
            inputMilliseconds = 5_000,
            logMelSpectro = LogMelSpectroConfig(
                nFFT = 512,
                nMels = 128,
                nFrames = 500,
                preemphasis = 0.97f,
            ),
            hasDecoder = true,
            decodeStartTokenId = 8192,
            decodeStopTokenId = -1,
            decodeSkipUntilTokenId = -1,
            gpuEnforceFloat32 = true,
            gpuShareConstantTensors = true,
            npuHighPerformance = true,
        )
    }
}

class ParakeetAsrEngine(private val context: Context) : AutoCloseable {
    private val appContext = context.applicationContext
    /**
     * Single-flight + single-thread gate for the non-thread-safe LiteRT
     * CompiledModel. All load/preprocess/decode/close work runs through it so a
     * shared handle is never closed while another caller is invoking it.
     */
    private val gate = NativeEngineGate()
    private var recognizer: LiteRtRunner? = null
    private var tokenizer: HuggingfaceTokenizer? = null
    private var preprocessor: MelSpectroProcessor? = null
    private var loadedAccelerator: ParakeetAccelerator? = null
    private var modelInputInterval = 5.seconds

    val providerLabel: String = "parakeet_tdt_npu"

    private fun ensureLoaded(accelerator: ParakeetAccelerator) {
        if (recognizer != null && loadedAccelerator == accelerator) return
        if (recognizer != null) {
            // Accelerator switch: dispose the old handle first. We hold the gate,
            // so nothing else is invoking it — never close an in-use handle.
            Log.i(TAG, "Switching Parakeet accelerator $loadedAccelerator -> $accelerator")
            closeBlocking()
        }

        val config = ParakeetLocalModels.modelConfig(appContext)
        tokenizer = HuggingfaceTokenizer(appContext, config)
        preprocessor = MelSpectroProcessor(SAMPLING_RATE, config.logMelSpectro!!)
        modelInputInterval = config.inputMilliseconds.milliseconds

        when (accelerator) {
            ParakeetAccelerator.NPU -> {
                require(ParakeetLocalModels.isAvailable(appContext)) {
                    "Parakeet Tensor G5 NPU pack missing or device unsupported " +
                        "(soc=${Build.SOC_MODEL}, npuModel=${ParakeetLocalModels.hasNpuModel(appContext)}, " +
                        "tokenizer=${ParakeetLocalModels.hasTokenizer(appContext)}, " +
                        "googleTensor=${NpuCompatibilityChecker.GoogleTensor.isDeviceSupported()})"
                }

                val npuPath = ParakeetLocalModels.npuModel(appContext).absolutePath
                val nativeLibDir = appContext.applicationInfo.nativeLibraryDir
                val provider = BuiltinNpuAcceleratorProvider(appContext)
                Log.i(
                    TAG,
                    "NPU load: soc=${Build.SOC_MODEL} model=$npuPath " +
                        "size=${File(npuPath).length()} nativeLibDir=$nativeLibDir " +
                        "providerReady=${provider.isLibraryReady()} " +
                        "deviceSupported=${provider.isDeviceSupported()} " +
                        "dispatchSo=${File(nativeLibDir, "libLiteRtDispatch_GoogleTensor.so").exists()}",
                )

                runCatching {
                    Os.setenv("ADSP_LIBRARY_PATH", nativeLibDir, true)
                    Os.setenv("LD_LIBRARY_PATH", nativeLibDir, true)
                }.onFailure { Log.w(TAG, "setenv native lib path failed", it) }
                runCatching {
                    System.loadLibrary("LiteRtDispatch_GoogleTensor")
                    Log.i(TAG, "Loaded libLiteRtDispatch_GoogleTensor")
                }.onFailure { Log.w(TAG, "Explicit load of LiteRtDispatch_GoogleTensor failed: ${it.message}") }

                try {
                    recognizer = LiteRtRunner(
                        appContext,
                        config,
                        Accelerator.NPU,
                        npuOnly = true,
                    ) { model, cfg -> TdtDecoder(model, cfg) }
                    Log.i(TAG, "CompiledModel created on NPU (litert-samples path, npuOnly)")
                } catch (t: Throwable) {
                    throw enrich(accelerator, "create CompiledModel(NPU)", t)
                }
            }

            ParakeetAccelerator.CPU -> {
                require(ParakeetLocalModels.isCpuAvailable(appContext)) {
                    "Parakeet CPU (i8) pack missing " +
                        "(cpuModel=${ParakeetLocalModels.hasCpuModel(appContext)}, " +
                        "cpuBytes=${ParakeetLocalModels.cpuModel(appContext).length()}, " +
                        "tokenizer=${ParakeetLocalModels.hasTokenizer(appContext)})"
                }
                val cpuPath = ParakeetLocalModels.cpuModel(appContext).absolutePath
                Log.i(
                    TAG,
                    "CPU load: model=$cpuPath size=${File(cpuPath).length()} " +
                        "soc=${Build.SOC_MODEL}",
                )
                try {
                    recognizer = LiteRtRunner(
                        appContext,
                        config,
                        Accelerator.CPU,
                        npuOnly = false,
                    ) { model, cfg -> TdtDecoder(model, cfg) }
                    Log.i(TAG, "CompiledModel created on CPU (i8 export, no NPU dispatch)")
                } catch (t: Throwable) {
                    throw enrich(accelerator, "create CompiledModel(CPU)", t)
                }
            }
        }
        loadedAccelerator = accelerator
    }

    /**
     * File transcription matching litert-samples MainActivity.processFileAudio parameters,
     * after aggressive silence trim + time-compression.
     *
     * Single-flight and pinned to the engine's serial dispatcher: concurrent
     * callers queue instead of sharing the CompiledModel. A cancelled caller does
     * not interrupt in-flight native work.
     *
     * @param accelerator which model export to compile for (default NPU — the
     * production path). The CPU path uses the pinned i8 CPU export and must not
     * depend on NPU availability.
     */
    suspend fun transcribe(
        wav: File,
        accelerator: ParakeetAccelerator = ParakeetAccelerator.NPU,
    ): String = gate.withExclusiveAccess { transcribeBlocking(wav, accelerator) }

    private fun transcribeBlocking(wav: File, accelerator: ParakeetAccelerator): String {
        ensureLoaded(accelerator)
        val mel = preprocessor!!
        val tok = tokenizer!!
        val overlap = FILE_AUDIO_CHUNK_OVERLAP_DURATION
        val overlapRatio = (overlap / modelInputInterval).toFloat()
        val startedAt = System.nanoTime()

        val prepDir = File(appContext.cacheDir, "asr_prep/${wav.nameWithoutExtension}_${System.nanoTime()}")
            .also { it.mkdirs() }
        val prep = AsrAudioPreprocessor.prepare(wav, prepDir, speed = AsrAudioPreprocessor.DEFAULT_SPEED)
        if (prep.processedDurationMs < 400L) {
            Log.i(TAG, "Transcribe skip: too little speech after prep (${prep.processedDurationMs}ms)")
            prepDir.deleteRecursively()
            return ""
        }

        Log.i(
            TAG,
            "Transcribe start wav=${wav.absolutePath} bytes=${wav.length()} " +
                "prepMs=${prep.processedDurationMs} origMs=${prep.originalDurationMs} " +
                "kept=${"%.2f".format(prep.keptSpeechRatio)} speed=${prep.speed} overlap=$overlap",
        )

        try {
            prep.file.inputStream().use { input ->
                FileAudioSource(input, SAMPLING_RATE, modelInputInterval, overlap).use { source ->
                    LevenshteinTokenMerger(
                        tok,
                        overlapRatio,
                        maxLevenshteinDistance = FILE_MAX_LEVENSHTEIN_DISTANCE,
                    ).use { merger ->
                        val confirmedParts = mutableListOf<String>()
                        var lastUnconfirmed = ""
                        var window = 0
                        for (chunk in source.getAudioData()) {
                            window++
                            val features = mel.process(chunk.copyOf())
                            val tokens = try {
                                recognizer!!.recognize(features).toList()
                            } catch (t: Throwable) {
                                // Abort the whole file on a failed window: reloading the
                                // shared CompiledModel mid-file resets decoder state and
                                // closes a handle another caller may be invoking (SIGSEGV
                                // class). The engine stays loaded; the next caller starts
                                // from a fresh window on the still-valid handle.
                                throw enrich(accelerator, "window=$window recognize", t)
                            }
                            for ((tokenId, timestamp) in tokens) {
                                val decoded = try {
                                    merger.decode(tokenId, timestamp)
                                } catch (t: IllegalArgumentException) {
                                    Log.w(TAG, "window=$window merger skipped: ${t.message}")
                                    null
                                } ?: continue
                                if (decoded.confirmedText.isNotBlank()) {
                                    confirmedParts.add(decoded.confirmedText)
                                }
                                lastUnconfirmed = decoded.unconfirmedText
                            }
                        }
                        val parts = confirmedParts.toMutableList()
                        if (lastUnconfirmed.isNotBlank()) parts.add(lastUnconfirmed)
                        val text = parts.filter { it.isNotBlank() }.joinToString(" ").trim()
                        val wallMs = (System.nanoTime() - startedAt) / 1_000_000L
                        val rtfOrig = if (prep.originalDurationMs > 0) {
                            wallMs.toDouble() / prep.originalDurationMs
                        } else {
                            -1.0
                        }
                        val rtfPrep = if (prep.processedDurationMs > 0) {
                            wallMs.toDouble() / prep.processedDurationMs
                        } else {
                            -1.0
                        }
                        Log.i(
                            TAG,
                            "Transcribe done chars=${text.length} windows=$window " +
                                "wallMs=$wallMs origMs=${prep.originalDurationMs} prepMs=${prep.processedDurationMs} " +
                                "rtfOrig=${"%.3f".format(rtfOrig)} rtfPrep=${"%.3f".format(rtfPrep)} " +
                                "(rtfOrig=wall/original; <0.5 means >=2x vs meeting time)",
                        )
                        return text
                    }
                }
            }
        } finally {
            prepDir.deleteRecursively()
        }
    }

    override fun close() {
        // Waits for in-flight native work before disposing; never closes an
        // in-use handle (the SIGSEGV source).
        gate.close { closeBlocking() }
    }

    private fun closeBlocking() {
        recognizer?.close()
        tokenizer?.close()
        preprocessor?.close()
        recognizer = null
        tokenizer = null
        preprocessor = null
        loadedAccelerator = null
    }

    private fun enrich(
        accelerator: ParakeetAccelerator,
        stage: String,
        t: Throwable,
    ): IllegalStateException {
        val litert = generateSequence(t) { it.cause }.filterIsInstance<LiteRtException>().firstOrNull()
        val detail = buildString {
            append("Parakeet $accelerator failed at $stage: ${t.message}")
            if (litert != null) append(" | LiteRtException status=${litert.message}")
            append(" | soc=${Build.SOC_MODEL}")
            val model = when (accelerator) {
                ParakeetAccelerator.NPU -> ParakeetLocalModels.npuModel(appContext).name
                ParakeetAccelerator.CPU -> ParakeetLocalModels.cpuModel(appContext).name
            }
            append(" | model=$model")
        }
        Log.e(TAG, detail, t)
        return IllegalStateException(detail, t)
    }

    companion object {
        private const val TAG = "ParakeetAsrEngine"
        private const val SAMPLING_RATE = 16_000
        /** litert-samples MainActivity.FILE_AUDIO_CHUNK_OVERLAP_DURATION */
        private val FILE_AUDIO_CHUNK_OVERLAP_DURATION = 2.seconds
        /** litert-samples MainActivity.FILE_MAX_LEVENSHTEIN_DISTANCE */
        private const val FILE_MAX_LEVENSHTEIN_DISTANCE = 5
    }
}
