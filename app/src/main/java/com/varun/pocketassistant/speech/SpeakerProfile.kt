package com.varun.pocketassistant.speech

import android.content.Context
import java.io.File
import java.io.FileOutputStream
import kotlin.math.ln
import kotlin.math.sqrt

/**
 * Very small speaker fingerprint: averaged log-energy across mel-like bands.
 * Placeholder until a Tensor speaker-embedding model lands.
 */
object VoiceEmbedder {
    private const val BANDS = 32

    fun embed(samples: ShortArray, sampleRate: Int = 16_000): FloatArray {
        val frame = sampleRate / 50 // 20ms
        val hop = frame / 2
        val acc = FloatArray(BANDS)
        var frames = 0
        var i = 0
        while (i + frame <= samples.size) {
            if (frameRms(samples, i, frame) >= 160.0) {
                accumulateBands(samples, i, frame, acc)
                frames++
            }
            i += hop
        }
        if (frames == 0) {
            accumulateBands(samples, 0, minOf(frame, samples.size), acc)
            frames = 1
        }
        for (b in acc.indices) acc[b] /= frames.toFloat()
        return l2Normalize(acc)
    }

    fun cosine(a: FloatArray, b: FloatArray): Float {
        val n = minOf(a.size, b.size)
        var dot = 0f
        for (i in 0 until n) dot += a[i] * b[i]
        return dot
    }

    private fun accumulateBands(samples: ShortArray, offset: Int, length: Int, acc: FloatArray) {
        val bandSize = (length / BANDS).coerceAtLeast(1)
        for (b in 0 until BANDS) {
            var sum = 0.0
            val start = offset + b * bandSize
            val end = minOf(offset + length, start + bandSize)
            for (i in start until end) {
                val v = samples[i].toFloat() / 32768f
                sum += v * v
            }
            acc[b] += ln(1.0 + sum).toFloat()
        }
    }

    private fun frameRms(samples: ShortArray, offset: Int, length: Int): Double {
        var sum = 0.0
        for (i in offset until offset + length) {
            val v = samples[i].toDouble()
            sum += v * v
        }
        return sqrt(sum / length)
    }

    private fun l2Normalize(v: FloatArray): FloatArray {
        var sum = 0f
        for (x in v) sum += x * x
        val norm = sqrt(sum)
        if (norm < 1e-6f) return v
        return FloatArray(v.size) { v[it] / norm }
    }
}

class SpeakerProfileStore(context: Context) {
    private val dir = File(context.filesDir, "speaker").also { it.mkdirs() }
    private val embeddingFile = File(dir, "user_embedding.bin")
    private val enrollmentWav = File(dir, "enrollment.wav")

    fun hasEnrollment(): Boolean = embeddingFile.exists() && embeddingFile.length() > 0

    fun enrollmentWavFile(): File = enrollmentWav

    fun saveEmbedding(embedding: FloatArray) {
        FileOutputStream(embeddingFile).use { out ->
            val bytes = ByteArray(embedding.size * 4)
            var i = 0
            for (v in embedding) {
                val bits = v.toBits()
                bytes[i++] = (bits and 0xff).toByte()
                bytes[i++] = ((bits shr 8) and 0xff).toByte()
                bytes[i++] = ((bits shr 16) and 0xff).toByte()
                bytes[i++] = ((bits shr 24) and 0xff).toByte()
            }
            out.write(bytes)
        }
    }

    fun loadEmbedding(): FloatArray? {
        if (!embeddingFile.exists()) return null
        val bytes = embeddingFile.readBytes()
        val out = FloatArray(bytes.size / 4)
        var i = 0
        for (idx in out.indices) {
            val b0 = bytes[i].toInt() and 0xff
            val b1 = bytes[i + 1].toInt() and 0xff
            val b2 = bytes[i + 2].toInt() and 0xff
            val b3 = bytes[i + 3].toInt() and 0xff
            out[idx] = Float.fromBits(b0 or (b1 shl 8) or (b2 shl 16) or (b3 shl 24))
            i += 4
        }
        return out
    }
}
