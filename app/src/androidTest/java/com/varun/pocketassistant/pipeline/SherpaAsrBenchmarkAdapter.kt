package com.varun.pocketassistant.pipeline

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import com.k2fsa.sherpa.onnx.FeatureConfig
import com.k2fsa.sherpa.onnx.OfflineModelConfig
import com.k2fsa.sherpa.onnx.OfflineRecognizer
import com.k2fsa.sherpa.onnx.OfflineRecognizerConfig
import com.k2fsa.sherpa.onnx.OfflineTransducerModelConfig
import com.k2fsa.sherpa.onnx.WaveReader
import java.io.File

/**
 * sherpa-onnx is an androidTest-only dependency (M1 increment 4), so its .so
 * files are packaged in the TEST APK, not the app APK. The app process's
 * classloader cannot find them via System.loadLibrary; preload them by absolute
 * path from the instrumentation APK's nativeLibraryDir before any sherpa class
 * initializes.
 */
object SherpaNativeLibs {
    private val libNames = listOf(
        "libonnxruntime.so",
        "libsherpa-onnx-c-api.so",
        "libsherpa-onnx-cxx-api.so",
        "libsherpa-onnx-jni.so",
    )

    @Volatile
    private var loaded = false

    fun ensureLoaded() {
        if (loaded) return
        synchronized(this) {
            if (loaded) return
            val libDir = File(
                InstrumentationRegistry.getInstrumentation()
                    .context.applicationInfo.nativeLibraryDir,
            )
            libNames.forEach { name ->
                val f = File(libDir, name)
                check(f.exists()) { "sherpa native lib missing from test APK: $name (dir=$libDir)" }
                System.load(f.absolutePath)
            }
            loaded = true
        }
    }
}

/**
 * Benchmark-only sherpa-onnx CPU int8 adapter for Parakeet TDT 0.6B v3
 * (M1 increment 4). Implements the same [AsrProvider] seam as the production
 * providers so the CPU-first runtime comparison runs through the identical
 * interface; NOT wired as a production provider — sherpa stays androidTest-only.
 *
 * Fidelity-first input policy: consumes the ORIGINAL WAV unchanged (speed 1.0,
 * no destructive silence trim/time-compression), decoded by the artifact's
 * reference frontend (sherpa FeatureConfig: 16 kHz, 80-dim fbank) and
 * greedy-search transducer decoder (model-type nemo_transducer, CPU provider).
 */
class SherpaAsrBenchmarkAdapter(context: Context) : AsrProvider {
    override val id: String = "sherpa_parakeet_tdt_cpu_i8"

    private val modelDir: File = File(context.filesDir, "models/sherpa")

    /** Per-run measurement; a run's wall = sessionLoadMs (cold only) + decodeMs. */
    data class TimedResult(
        val result: AsrResult,
        val sessionLoadMs: Long,
        val decodeMs: Long,
        val audioMs: Long,
        val rtf: Double,
    )

    /**
     * A loaded recognizer handle. [loadMs] is the model-load cost, paid once;
     * every [transcribeTimed] after that is a warm decode.
     */
    class Session private constructor(
        private val recognizer: OfflineRecognizer,
        val loadMs: Long,
    ) {
        fun transcribeTimed(wav: File): TimedResult {
            val wave = WaveReader.readWave(wav.absolutePath)
            val audioMs = wave.samples.size * 1000L / wave.sampleRate.coerceAtLeast(1)
            val stream = recognizer.createStream()
            try {
                val decodeStart = System.nanoTime()
                stream.acceptWaveform(wave.samples, wave.sampleRate)
                recognizer.decode(stream)
                val text = recognizer.getResult(stream).text.trim()
                val decodeMs = (System.nanoTime() - decodeStart) / 1_000_000L
                val rtf = if (audioMs > 0) decodeMs.toDouble() / audioMs else -1.0
                return TimedResult(
                    result = AsrResult(plain = text, diarized = null, providerId = ID),
                    sessionLoadMs = 0L,
                    decodeMs = decodeMs,
                    audioMs = audioMs,
                    rtf = rtf,
                )
            } finally {
                stream.release()
            }
        }

        fun release() = recognizer.release()

        companion object {
            fun create(modelDir: File): Session {
                SherpaNativeLibs.ensureLoaded()
                val loadStart = System.nanoTime()
                val recognizer = OfflineRecognizer(
                    config = OfflineRecognizerConfig(
                        featConfig = FeatureConfig(sampleRate = 16_000, featureDim = 80),
                        modelConfig = OfflineModelConfig(
                            transducer = OfflineTransducerModelConfig(
                                encoder = File(modelDir, "encoder.int8.onnx").absolutePath,
                                decoder = File(modelDir, "decoder.int8.onnx").absolutePath,
                                joiner = File(modelDir, "joiner.int8.onnx").absolutePath,
                            ),
                            tokens = File(modelDir, "tokens.txt").absolutePath,
                            numThreads = NUM_THREADS,
                            provider = "cpu",
                            modelType = "nemo_transducer",
                        ),
                    ),
                )
                val loadMs = (System.nanoTime() - loadStart) / 1_000_000L
                return Session(recognizer, loadMs)
            }
        }
    }

    fun modelFilesPresent(): Boolean = REQUIRED_FILES.all {
        val f = File(modelDir, it)
        f.exists() && f.length() > 1_000L
    }

    fun newSession(): Session {
        require(modelFilesPresent()) {
            "sherpa parakeet int8 model files missing under ${modelDir.absolutePath} " +
                "(expected: ${REQUIRED_FILES.joinToString()})"
        }
        return Session.create(modelDir)
    }

    override fun isAvailable(): Boolean = modelFilesPresent()

    override suspend fun transcribe(wav: File): AsrResult {
        val session = newSession()
        return try {
            session.transcribeTimed(wav).result
        } finally {
            session.release()
        }
    }

    companion object {
        const val ID = "sherpa_parakeet_tdt_cpu_i8"
        const val NUM_THREADS = 2
        val REQUIRED_FILES = listOf(
            "encoder.int8.onnx",
            "decoder.int8.onnx",
            "joiner.int8.onnx",
            "tokens.txt",
        )
    }
}
