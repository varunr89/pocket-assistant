package com.varun.pocketassistant.capture

import java.io.Closeable
import java.io.File
import java.io.RandomAccessFile
import java.nio.ByteBuffer
import java.nio.ByteOrder

/**
 * Streams PCM16 mono into a WAV file, patching the header on close.
 * Writes are flush-friendly so a crash loses at most the current open segment.
 */
class WavWriter(
    private val file: File,
    private val sampleRate: Int,
    private val channels: Int = 1,
) : Closeable {
    private val raf = RandomAccessFile(file, "rw")
    private var dataBytes: Long = 0
    private var closed = false

    init {
        raf.setLength(0)
        writeHeader(0)
    }

    @Synchronized
    fun writePcm(buffer: ShortArray, offset: Int, length: Int) {
        check(!closed) { "WavWriter already closed" }
        val bytes = ByteBuffer.allocate(length * 2).order(ByteOrder.LITTLE_ENDIAN)
        for (i in offset until offset + length) {
            bytes.putShort(buffer[i])
        }
        val array = bytes.array()
        raf.write(array)
        dataBytes += array.size
    }

    @Synchronized
    fun flush() {
        if (closed) return
        raf.fd.sync()
    }

    override fun close() {
        synchronized(this) {
            if (closed) return
            writeHeader(dataBytes.toInt())
            raf.fd.sync()
            raf.close()
            closed = true
        }
    }

    private fun writeHeader(dataSize: Int) {
        val byteRate = sampleRate * channels * 2
        val blockAlign = channels * 2
        val buffer = ByteBuffer.allocate(44).order(ByteOrder.LITTLE_ENDIAN)
        buffer.put("RIFF".toByteArray())
        buffer.putInt(36 + dataSize)
        buffer.put("WAVE".toByteArray())
        buffer.put("fmt ".toByteArray())
        buffer.putInt(16)
        buffer.putShort(1) // PCM
        buffer.putShort(channels.toShort())
        buffer.putInt(sampleRate)
        buffer.putInt(byteRate)
        buffer.putShort(blockAlign.toShort())
        buffer.putShort(16) // bits
        buffer.put("data".toByteArray())
        buffer.putInt(dataSize)
        raf.seek(0)
        raf.write(buffer.array())
        raf.seek(44L + dataBytes)
    }
}
