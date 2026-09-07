package com.varun.pocketassistant.pipeline

import android.content.Context
import android.os.Build
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import java.io.File
import java.io.RandomAccessFile
import java.nio.ByteBuffer
import java.nio.ByteOrder
import kotlinx.coroutines.runBlocking
import org.json.JSONArray
import org.json.JSONObject
import org.junit.Assume.assumeTrue
import org.junit.Test
import org.junit.runner.RunWith

/**
 * CPU-first runtime comparison (M1 increment 4): sherpa-onnx CPU int8 vs
 * available LiteRT CPU vs serialized LiteRT NPU on identical original 16 kHz
 * mono WAVs from filesDir/benchmark_wavs (staged by
 * scripts/run-cpu-benchmark.sh).
 *
 * Not a CI test — needs staged models + corpus on device; assumption-gated so
 * a bare device skips instead of fails. Results (per-file per-backend cold/warm
 * wall, load/decode, RTF, failures, raw text) are written to
 * filesDir/benchmark_out/benchmark_results.json. git SHA / APK hashes are
 * recorded by the runner script, not here.
 *
 * Input-policy honesty: sherpa consumes the original WAV unchanged (speed 1.0,
 * no trim); the LiteRT engine uses its current production preprocessing
 * (1.35x time-compress + silence trim). Fidelity-first ablation of the LiteRT
 * path is increment 5 — these runs are NOT a fully controlled comparison yet.
 */
@RunWith(AndroidJUnit4::class)
class CpuFirstBenchmarkTest {
    private val context: Context = ApplicationProvider.getApplicationContext()
    private val corpusDir: File = File(context.filesDir, "benchmark_wavs")
    private val outDir: File = File(context.filesDir, "benchmark_out")

    @Test
    fun cpuFirstComparison() = runBlocking {
        val wavs = corpusDir.listFiles { f -> f.isFile && f.name.endsWith(".wav") }
            ?.sortedBy { it.name }
            .orEmpty()
        assumeTrue(wavs.isNotEmpty())

        val root = JSONObject()
            .put("generatedAtMs", System.currentTimeMillis())
            .put("soc", Build.SOC_MODEL ?: "unknown")
            .put("model", Build.MODEL ?: "unknown")
            .put("androidRelease", Build.VERSION.RELEASE ?: "unknown")
            .put(
                "note",
                "sherpa: original WAV (speed 1.0, no trim). LiteRT: production " +
                    "preprocessing (1.35x + trim) until increment 5 adds explicit trim control.",
            )

        val files = JSONArray()
        for (wav in wavs) {
            files.put(
                JSONObject()
                    .put("wav", wav.name)
                    .put("bytes", wav.length())
                    .put("audioMs", audioDurationMs(wav)),
            )
        }

        // Each backend runs once over the whole corpus: first file = cold
        // (model load paid), remaining files = warm. Rows are tagged per wav.
        val runs = JSONArray()
        runSherpaBackend(runs, wavs)
        runLiteRtBackend(runs, wavs, ParakeetAccelerator.CPU)
        runLiteRtBackend(runs, wavs, ParakeetAccelerator.NPU)

        root.put("files", files)
        root.put("runs", runs)

        outDir.mkdirs()
        val out = File(outDir, "benchmark_results.json")
        out.writeText(root.toString(2))
        android.util.Log.i(TAG, "Benchmark results written to ${out.absolutePath}")
    }

    /** sherpa CPU int8: one loaded session; cold = first file (load paid), warm = rest. */
    private fun runSherpaBackend(backends: JSONArray, wavs: List<File>) {
        val label = SherpaAsrBenchmarkAdapter.ID
        val adapter = SherpaAsrBenchmarkAdapter(context)
        if (!adapter.modelFilesPresent()) {
            backends.put(skipJson(label, "sherpa model files not staged"))
            return
        }
        val session = try {
            adapter.newSession()
        } catch (t: Throwable) {
            backends.put(failJson(label, "cold", "load failed: ${t.message}"))
            return
        }
        try {
            var first = true
            for (wav in wavs) {
                val pass = if (first) "cold" else "warm"
                first = false
                try {
                    val r = session.transcribeTimed(wav)
                    backends.put(
                        runJson(
                            backend = label,
                            wav = wav.name,
                            pass = pass,
                            wallMs = r.decodeMs + r.sessionLoadMs,
                            loadMs = r.sessionLoadMs,
                            decodeMs = r.decodeMs,
                            audioMs = r.audioMs,
                            rtf = if (r.audioMs > 0) (r.decodeMs + r.sessionLoadMs).toDouble() / r.audioMs else -1.0,
                            preprocess = "none (original wav, speed 1.0, no trim)",
                            text = r.result.plain,
                        ),
                    )
                } catch (t: Throwable) {
                    backends.put(failJson(label, pass, "${t.javaClass.simpleName}: ${t.message}"))
                }
            }
        } finally {
            runCatching { session.release() }
        }
    }

    /** LiteRT engine on [accelerator]: fresh engine = cold first file, warm = rest. */
    private suspend fun runLiteRtBackend(
        backends: JSONArray,
        wavs: List<File>,
        accelerator: ParakeetAccelerator,
    ) {
        val label = when (accelerator) {
            ParakeetAccelerator.CPU -> "litert_parakeet_tdt_cpu_i8"
            ParakeetAccelerator.NPU -> "litert_parakeet_tdt_npu_f32"
        }
        val engine = ParakeetAsrEngine(context)
        try {
            var first = true
            for (wav in wavs) {
                val pass = if (first) "cold" else "warm"
                first = false
                val start = System.nanoTime()
                try {
                    val text = engine.transcribe(wav, accelerator)
                    val wallMs = (System.nanoTime() - start) / 1_000_000L
                    val audioMs = audioDurationMs(wav)
                    backends.put(
                        runJson(
                            backend = label,
                            wav = wav.name,
                            pass = pass,
                            wallMs = wallMs,
                            loadMs = if (pass == "cold") wallMs else 0L,
                            decodeMs = if (pass == "cold") 0L else wallMs,
                            audioMs = audioMs,
                            rtf = if (audioMs > 0) wallMs.toDouble() / audioMs else -1.0,
                            preprocess = "production defaults (1.35x + trim); increment 5 ablation pending",
                            text = text,
                        ),
                    )
                } catch (t: Throwable) {
                    backends.put(failJson(label, pass, "${t.javaClass.simpleName}: ${t.message}"))
                }
            }
        } finally {
            engine.close()
        }
    }

    /** WAV duration from the data chunk (16 kHz mono PCM16 assumed; -1 if unparseable). */
    private fun audioDurationMs(wav: File): Long {
        if (wav.length() < 44) return -1L
        return try {
            RandomAccessFile(wav, "r").use { raf ->
                val header = ByteArray(44)
                raf.readFully(header)
                val bb = ByteBuffer.wrap(header).order(ByteOrder.LITTLE_ENDIAN)
                bb.position(22)
                val channels = bb.short.toInt()
                val rate = bb.int
                raf.seek(12)
                var dataSize = -1L
                while (raf.filePointer + 8 <= raf.length()) {
                    val idBytes = ByteArray(4)
                    raf.readFully(idBytes)
                    val sizeBuf = ByteArray(4)
                    raf.readFully(sizeBuf)
                    val size = ByteBuffer.wrap(sizeBuf).order(ByteOrder.LITTLE_ENDIAN).int.toLong()
                    if (String(idBytes) == "data") {
                        dataSize = size
                        break
                    }
                    raf.seek(raf.filePointer + size)
                }
                if (dataSize <= 0 || rate <= 0 || channels <= 0) {
                    -1L
                } else {
                    dataSize * 1000L / (rate.toLong() * channels * 2)
                }
            }
        } catch (t: Throwable) {
            -1L
        }
    }

    private fun runJson(
        backend: String,
        wav: String,
        pass: String,
        wallMs: Long,
        loadMs: Long,
        decodeMs: Long,
        audioMs: Long,
        rtf: Double,
        preprocess: String,
        text: String,
    ): JSONObject = JSONObject()
        .put("backend", backend)
        .put("wav", wav)
        .put("pass", pass)
        .put("ok", true)
        .put("wallMs", wallMs)
        .put("loadMs", loadMs)
        .put("decodeMs", decodeMs)
        .put("audioMs", audioMs)
        .put("rtf", rtf)
        .put("preprocess", preprocess)
        .put("text", text)

    private fun failJson(backend: String, pass: String, error: String?): JSONObject =
        JSONObject()
            .put("backend", backend)
            .put("pass", pass)
            .put("ok", false)
            .put("error", error ?: "unknown error")

    private fun skipJson(backend: String, reason: String): JSONObject =
        JSONObject()
            .put("backend", backend)
            .put("ok", false)
            .put("skipped", true)
            .put("error", reason)

    companion object {
        private const val TAG = "CpuFirstBenchmarkTest"
    }
}
