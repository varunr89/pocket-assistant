package com.varun.pocketassistant.pipeline

import android.util.Log
import java.io.File
import java.io.RandomAccessFile
import java.nio.ByteBuffer
import java.nio.ByteOrder
import kotlin.math.sqrt

/**
 * Pre-ASR audio conditioning:
 * 1) Drop near-silence frames (keep small pads around speech)
 * 2) Optional time-compress by [speed] (local Parakeet uses ~1.35×; cloud uses 1.0)
 *
 * Output is 16 kHz mono PCM16 WAV.
 */
object AsrAudioPreprocessor {
    data class Result(
        val file: File,
        val originalDurationMs: Long,
        val processedDurationMs: Long,
        val keptSpeechRatio: Float,
        val speed: Float,
    ) {
        val compressionRatio: Float
            get() = if (processedDurationMs <= 0L) 0f
            else originalDurationMs.toFloat() / processedDurationMs.toFloat()
    }

    /**
     * @param speed playback-speed factor (>1 faster). Typical 1.2–1.5 for local; 1.0 for cloud.
     */
    fun prepare(
        inputWav: File,
        outputDir: File,
        speed: Float = DEFAULT_SPEED,
        silenceRms: Float = SILENCE_RMS,
        minSilenceFramesToDrop: Int = MIN_SILENCE_FRAMES_TO_DROP,
        padFrames: Int = PAD_FRAMES,
    ): Result {
        require(speed in 1.0f..2.0f) { "speed must be in 1.0..2.0, got $speed" }
        val pcm = readPcm16Mono16k(inputWav)
        val originalMs = pcm.size * 1000L / SAMPLE_RATE
        if (pcm.isEmpty()) {
            val empty = File(outputDir, "asr_prep_empty_${inputWav.name}")
            writeWav(empty, ShortArray(0))
            return Result(empty, originalMs, 0L, 0f, speed)
        }

        val trimmed = trimSilence(pcm, silenceRms, minSilenceFramesToDrop, padFrames)
        val keptRatio = if (pcm.isEmpty()) 0f else trimmed.size.toFloat() / pcm.size.toFloat()
        val sped = if (speed <= 1.001f) trimmed else timeCompress(trimmed, speed)
        outputDir.mkdirs()
        val out = File(outputDir, "asr_prep_${inputWav.nameWithoutExtension}_${(speed * 100).toInt()}x.wav")
        writeWav(out, sped)
        val processedMs = sped.size * 1000L / SAMPLE_RATE
        Log.i(
            TAG,
            "prep ${inputWav.name}: origMs=$originalMs keptSpeech=${"%.2f".format(keptRatio)} " +
                "speed=${"%.2f".format(speed)} outMs=$processedMs " +
                "compression=${"%.2f".format(originalMs.toFloat() / processedMs.coerceAtLeast(1))}x " +
                "bytes ${inputWav.length()}→${out.length()}",
        )
        return Result(out, originalMs, processedMs, keptRatio, speed)
    }

    /** Cloud path: silence trim only (no time-compression). */
    fun prepareForCloud(inputWav: File, outputDir: File): Result =
        prepare(inputWav, outputDir, speed = CLOUD_SPEED)

    /**
     * Split a 16 kHz mono PCM16 WAV into chunks of at most [maxDurationMs],
     * preferring the nearest silence gap to the target duration (avoids mid-word cuts).
     */
    fun splitByDurationMs(
        inputWav: File,
        outputDir: File,
        maxDurationMs: Long = CLOUD_CHUNK_MS,
    ): List<File> {
        require(maxDurationMs >= 30_000L) { "maxDurationMs too small: $maxDurationMs" }
        val pcm = readPcm16Mono16k(inputWav)
        if (pcm.isEmpty()) return emptyList()
        val maxSamples = (SAMPLE_RATE * maxDurationMs / 1000L).toInt().coerceAtLeast(1)
        if (pcm.size <= maxSamples) return listOf(inputWav)
        outputDir.mkdirs()
        val silenceSearchRadius = (SAMPLE_RATE * 15).coerceAtMost(maxSamples / 4) // ±15s
        val out = ArrayList<File>()
        var offset = 0
        var idx = 0
        while (offset < pcm.size) {
            val remaining = pcm.size - offset
            if (remaining <= maxSamples) {
                val slice = pcm.copyOfRange(offset, pcm.size)
                val file = File(
                    outputDir,
                    "asr_chunk_${inputWav.nameWithoutExtension}_${idx.toString().padStart(3, '0')}.wav",
                )
                writeWav(file, slice)
                out += file
                break
            }
            val targetEnd = offset + maxSamples
            val splitAt = findNearestSilenceSplit(pcm, targetEnd, silenceSearchRadius)
                .coerceIn(offset + maxSamples / 2, (offset + maxSamples).coerceAtMost(pcm.size))
            val slice = pcm.copyOfRange(offset, splitAt)
            val file = File(
                outputDir,
                "asr_chunk_${inputWav.nameWithoutExtension}_${idx.toString().padStart(3, '0')}.wav",
            )
            writeWav(file, slice)
            out += file
            offset = splitAt
            idx++
        }
        Log.i(TAG, "split ${inputWav.name} into ${out.size} chunks ≤${maxDurationMs}ms (silence-aware)")
        return out
    }

    /** Prefer a quiet frame near [targetSample]; fall back to [targetSample] if none. */
    private fun findNearestSilenceSplit(pcm: ShortArray, targetSample: Int, radius: Int): Int {
        val start = (targetSample - radius).coerceAtLeast(0)
        val end = (targetSample + radius).coerceAtMost(pcm.size)
        var best = targetSample.coerceIn(0, pcm.size)
        var bestRms = Float.MAX_VALUE
        var pos = start
        while (pos + FRAME_SAMPLES <= end) {
            val rms = frameRms(pcm, pos, FRAME_SAMPLES)
            if (rms < SILENCE_RMS && rms < bestRms) {
                bestRms = rms
                best = pos + FRAME_SAMPLES
            }
            pos += FRAME_SAMPLES
        }
        return best.coerceIn(0, pcm.size)
    }

    private fun trimSilence(
        pcm: ShortArray,
        silenceRms: Float,
        minSilenceFramesToDrop: Int,
        padFrames: Int,
    ): ShortArray {
        if (pcm.size < FRAME_SAMPLES) return pcm
        val frameCount = pcm.size / FRAME_SAMPLES
        val voiced = BooleanArray(frameCount)
        for (f in 0 until frameCount) {
            voiced[f] = frameRms(pcm, f * FRAME_SAMPLES, FRAME_SAMPLES) >= silenceRms
        }
        // Expand speech with pad frames.
        val keep = BooleanArray(frameCount)
        for (f in 0 until frameCount) {
            if (!voiced[f]) continue
            val start = (f - padFrames).coerceAtLeast(0)
            val end = (f + padFrames).coerceAtMost(frameCount - 1)
            for (i in start..end) keep[i] = true
        }
        // Drop only silence runs longer than threshold; keep short gaps.
        var i = 0
        while (i < frameCount) {
            if (keep[i]) {
                i++
                continue
            }
            var j = i
            while (j < frameCount && !keep[j]) j++
            val run = j - i
            if (run < minSilenceFramesToDrop) {
                for (k in i until j) keep[k] = true
            }
            i = j
        }
        val out = ArrayList<Short>(pcm.size)
        for (f in 0 until frameCount) {
            if (!keep[f]) continue
            val base = f * FRAME_SAMPLES
            for (s in 0 until FRAME_SAMPLES) out.add(pcm[base + s])
        }
        // Remainder samples after last full frame: keep if last frame kept.
        val remStart = frameCount * FRAME_SAMPLES
        if (remStart < pcm.size && (frameCount == 0 || keep[frameCount - 1])) {
            for (s in remStart until pcm.size) out.add(pcm[s])
        }
        if (out.isEmpty()) return ShortArray(0)
        return ShortArray(out.size) { out[it] }
    }

    /** Linear-interpolation time compression (raises pitch; ASR-tolerant at 1.2–1.5×). */
    private fun timeCompress(pcm: ShortArray, speed: Float): ShortArray {
        val outLen = (pcm.size / speed).toInt().coerceAtLeast(1)
        val out = ShortArray(outLen)
        for (i in 0 until outLen) {
            val src = i * speed
            val i0 = src.toInt().coerceIn(0, pcm.size - 1)
            val i1 = (i0 + 1).coerceAtMost(pcm.size - 1)
            val frac = src - i0
            val v = pcm[i0] * (1f - frac) + pcm[i1] * frac
            out[i] = v.toInt().coerceIn(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt()).toShort()
        }
        return out
    }

    private fun frameRms(pcm: ShortArray, offset: Int, length: Int): Float {
        var sum = 0.0
        val end = (offset + length).coerceAtMost(pcm.size)
        val n = end - offset
        if (n <= 0) return 0f
        for (i in offset until end) {
            val x = pcm[i] / 32768.0
            sum += x * x
        }
        return sqrt(sum / n).toFloat()
    }

    private fun readPcm16Mono16k(wav: File): ShortArray {
        RandomAccessFile(wav, "r").use { raf ->
            val header = ByteArray(44)
            if (raf.read(header) < 44) return ShortArray(0)
            val bb = ByteBuffer.wrap(header).order(ByteOrder.LITTLE_ENDIAN)
            require(String(header, 0, 4) == "RIFF") { "not RIFF: ${wav.name}" }
            bb.position(22)
            val channels = bb.short.toInt()
            val rate = bb.int
            bb.position(34)
            val bits = bb.short.toInt()
            require(channels == 1 && rate == SAMPLE_RATE && bits == 16) {
                "expected 16kHz mono PCM16, got ch=$channels rate=$rate bits=$bits (${wav.name})"
            }
            // Find data chunk (some writers insert extra chunks).
            raf.seek(12)
            var dataSize = -1
            while (raf.filePointer + 8 <= raf.length()) {
                val idBytes = ByteArray(4)
                raf.readFully(idBytes)
                val sizeBuf = ByteArray(4)
                raf.readFully(sizeBuf)
                val size = ByteBuffer.wrap(sizeBuf).order(ByteOrder.LITTLE_ENDIAN).int
                val id = String(idBytes)
                if (id == "data") {
                    dataSize = size
                    break
                }
                raf.seek(raf.filePointer + size)
            }
            if (dataSize <= 0) return ShortArray(0)
            val bytes = ByteArray(dataSize)
            raf.readFully(bytes)
            val samples = ShortArray(dataSize / 2)
            ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().get(samples)
            return samples
        }
    }

    private fun writeWav(file: File, pcm: ShortArray) {
        val dataSize = pcm.size * 2
        val header = ByteBuffer.allocate(44).order(ByteOrder.LITTLE_ENDIAN)
        header.put("RIFF".toByteArray())
        header.putInt(36 + dataSize)
        header.put("WAVE".toByteArray())
        header.put("fmt ".toByteArray())
        header.putInt(16)
        header.putShort(1)
        header.putShort(1)
        header.putInt(SAMPLE_RATE)
        header.putInt(SAMPLE_RATE * 2)
        header.putShort(2)
        header.putShort(16)
        header.put("data".toByteArray())
        header.putInt(dataSize)
        RandomAccessFile(file, "rw").use { raf ->
            raf.setLength(0)
            raf.write(header.array())
            val body = ByteBuffer.allocate(dataSize).order(ByteOrder.LITTLE_ENDIAN)
            for (s in pcm) body.putShort(s)
            raf.write(body.array())
        }
    }

    private const val TAG = "AsrAudioPreprocessor"
    const val SAMPLE_RATE = 16_000
    /** ~30 ms frames. */
    private const val FRAME_SAMPLES = 480
    /**
     * Aggressive silence gate (normalized float RMS). Room tone often ~0.001–0.008;
     * speech typically ≫ 0.02.
     */
    const val SILENCE_RMS = 0.012f
    /** Drop silence runs longer than ~250 ms. */
    private const val MIN_SILENCE_FRAMES_TO_DROP = 8
    /** Keep ~120 ms pad around speech. */
    private const val PAD_FRAMES = 4
    /** Midpoint of requested 1.2–1.5× for local Parakeet. */
    const val DEFAULT_SPEED = 1.35f
    /** Cloud Whisper: silence only — speedup hurt WER in OpenRouter benches. */
    const val CLOUD_SPEED = 1.0f
    /** Upload chunk target (~2 min). Capture rolls stay at [CLOUD_MAX_SEGMENT_MS]. */
    const val CLOUD_CHUNK_MS = 120_000L
    /** Capture roll interval when cloud ASR is preferred/required. */
    const val CLOUD_MAX_SEGMENT_MS = 600_000L
    /** Capture roll interval for local Parakeet. */
    const val LOCAL_MAX_SEGMENT_MS = 120_000L
}
