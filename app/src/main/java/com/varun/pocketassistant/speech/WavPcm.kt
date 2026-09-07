package com.varun.pocketassistant.speech

import java.io.File
import java.io.RandomAccessFile

/**
 * Minimal RIFF/WAVE parser for captured segments. ML Kit's
 * `AudioSource.fromPfd` contract is strict (raw headerless PCM16, mono,
 * 16 kHz), so a segment is validated BEFORE any AICore call and rejected
 * with a diagnosable [ForegroundAsrFailure.InvalidAudio] otherwise.
 *
 * Pure JVM code (no Android imports) so it is unit-testable on the host.
 */
object WavPcm {

    data class FormatInfo(
        val channels: Int,
        val sampleRate: Int,
        val bitsPerSample: Int,
        val dataOffset: Long,
        val dataBytes: Long,
    ) {
        val durationMs: Long
            get() = when {
                sampleRate <= 0 || channels <= 0 || bitsPerSample <= 0 -> 0L
                else -> dataBytes * 1000L / (sampleRate.toLong() * channels * (bitsPerSample / 8))
            }

        val isAcceptable: Boolean
            get() = channels == 1 && sampleRate == 16_000 && bitsPerSample == 16

        fun toContractError(file: File): String =
            "audio input contract violation for ${file.name}: " +
                "channels=$channels (need 1), sampleRate=$sampleRate (need 16000), " +
                "bits=$bitsPerSample (need 16)"
    }

    const val MIN_FILE_BYTES = 44L

    fun parse(file: File): FormatInfo {
        if (!file.isFile || file.length() < MIN_FILE_BYTES) {
            throw ForegroundAsrFailure.InvalidAudio(
                "missing or truncated segment audio: $file (${file.length()} bytes)",
            )
        }
        RandomAccessFile(file, "r").use { raf ->
            val riff = ByteArray(4)
            raf.readFully(riff)
            if (String(riff, Charsets.US_ASCII) != "RIFF") {
                throw ForegroundAsrFailure.InvalidAudio("${file.name} is not RIFF/WAVE audio")
            }
            skipFully(raf, 4) // RIFF size
            val wave = ByteArray(4)
            raf.readFully(wave)
            if (String(wave, Charsets.US_ASCII) != "WAVE") {
                throw ForegroundAsrFailure.InvalidAudio("${file.name} is not RIFF/WAVE audio")
            }

            var format: FormatInfo? = null
            while (raf.filePointer + 8 <= raf.length()) {
                val idBytes = ByteArray(4)
                raf.readFully(idBytes)
                val id = String(idBytes, Charsets.US_ASCII)
                val chunkSize = readU32LE(raf)
                when (id) {
                    "fmt " -> format = readFmtChunk(raf, chunkSize)
                    "data" -> {
                        val fmt = format
                            ?: throw ForegroundAsrFailure.InvalidAudio(
                                "${file.name}: data chunk before fmt chunk",
                            )
                        val info = fmt.copy(dataOffset = raf.filePointer, dataBytes = chunkSize)
                        if (!info.isAcceptable) {
                            throw ForegroundAsrFailure.InvalidAudio(info.toContractError(file))
                        }
                        return info
                    }
                    else -> skipFully(raf, chunkSize + (chunkSize and 1L))
                }
            }
            throw ForegroundAsrFailure.InvalidAudio("${file.name}: no data chunk found")
        }
    }

    private fun readFmtChunk(raf: RandomAccessFile, chunkSize: Long): FormatInfo {
        if (chunkSize < 16) {
            throw ForegroundAsrFailure.InvalidAudio("malformed fmt chunk (size=$chunkSize)")
        }
        val audioFormat = readU16LE(raf)
        val channels = readU16LE(raf)
        val sampleRate = readU32LE(raf).toInt()
        raf.readInt() // byte rate (LE), unused
        raf.readShort() // block align (LE), unused
        val bitsPerSample = readU16LE(raf)
        skipFully(raf, (chunkSize - 16) + ((chunkSize - 16) and 1L))
        return FormatInfo(
            channels = channels,
            sampleRate = sampleRate,
            bitsPerSample = bitsPerSample,
            dataOffset = -1,
            dataBytes = 0,
        ).also {
            if (audioFormat != 1) {
                throw ForegroundAsrFailure.InvalidAudio(
                    "unsupported WAV encoding audioFormat=$audioFormat (need PCM=1)",
                )
            }
        }
    }

    private fun skipFully(raf: RandomAccessFile, count: Long) {
        var remaining = count
        while (remaining > 0) {
            val skipped = raf.skipBytes(remaining.toInt().coerceAtMost(Int.MAX_VALUE))
            if (skipped <= 0) {
                throw ForegroundAsrFailure.InvalidAudio("truncated RIFF chunk")
            }
            remaining -= skipped
        }
    }

    private fun readU16LE(raf: RandomAccessFile): Int {
        val b0 = raf.read()
        val b1 = raf.read()
        if (b0 < 0 || b1 < 0) throw ForegroundAsrFailure.InvalidAudio("truncated RIFF chunk")
        return (b0 and 0xFF) or ((b1 and 0xFF) shl 8)
    }

    private fun readU32LE(raf: RandomAccessFile): Long {
        val b0 = raf.read()
        val b1 = raf.read()
        val b2 = raf.read()
        val b3 = raf.read()
        if (b0 < 0 || b1 < 0 || b2 < 0 || b3 < 0) {
            throw ForegroundAsrFailure.InvalidAudio("truncated RIFF chunk")
        }
        return (b0 and 0xFF).toLong() or
            ((b1 and 0xFF).toLong() shl 8) or
            ((b2 and 0xFF).toLong() shl 16) or
            ((b3 and 0xFF).toLong() shl 24)
    }
}