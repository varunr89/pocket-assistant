package com.varun.pocketassistant.speech

import java.io.ByteArrayOutputStream
import java.io.File
import java.nio.file.Files
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

/**
 * RIFF/WAVE validation ahead of the ML Kit fd contract: raw headerless
 * PCM16, mono, 16 kHz. Invalid audio must be rejected BEFORE any AICore call.
 *
 * NOTE: no JUnit 4.13-only APIs here — the unit-test classpath is shadowed by
 * libs/jlibrosa-...-jar-with-dependencies.jar, which bundles a 2011-era JUnit
 * (no assertThrows, old @Rule). See TranscriptionDrainTest for the helper.
 */
class WavPcmTest {

    private var tmpDir: File? = null

    @Before
    fun setUp() {
        tmpDir = Files.createTempDirectory("wavpcm-test").toFile()
    }

    @After
    fun tearDown() {
        tmpDir?.deleteRecursively()
        tmpDir = null
    }

    private fun newFile(name: String): File = File(tmpDir!!, name)

    @Test
    fun validSixteenKhzMonoPcm16Parses() {
        val file = writeWav(newFile("ok.wav"), sampleRate = 16_000, channels = 1, bits = 16, dataBytes = 64_000)

        val info = WavPcm.parse(file)

        assertEquals(1, info.channels)
        assertEquals(16_000, info.sampleRate)
        assertEquals(16, info.bitsPerSample)
        assertEquals(44L, info.dataOffset)
        assertEquals(64_000L, info.dataBytes)
        assertEquals(2_000L, info.durationMs)
        assertTrue(info.isAcceptable)
    }

    @Test
    fun wrongSampleRateIsRejected() {
        val file = writeWav(newFile("8k.wav"), sampleRate = 8_000, channels = 1, bits = 16, dataBytes = 32_000)

        val failure = expectThrowable<ForegroundAsrFailure.InvalidAudio> {
            WavPcm.parse(file)
        }
        assertTrue(failure.message!!.contains("8000"))
        assertTrue(failure.message!!.contains("16000"))
    }

    @Test
    fun stereoIsRejected() {
        val file = writeWav(newFile("stereo.wav"), sampleRate = 16_000, channels = 2, bits = 16, dataBytes = 64_000)

        expectThrowable<ForegroundAsrFailure.InvalidAudio> {
            WavPcm.parse(file)
        }
    }

    @Test
    fun nonPcmEncodingIsRejected() {
        val file = writeWav(newFile("mu.wav"), sampleRate = 16_000, channels = 1, bits = 16, dataBytes = 64_000, audioFormat = 7)

        expectThrowable<ForegroundAsrFailure.InvalidAudio> {
            WavPcm.parse(file)
        }
    }

    @Test
    fun notRiffIsRejected() {
        val file = newFile("junk.wav")
        file.writeBytes(ByteArray(100) { 7 })

        expectThrowable<ForegroundAsrFailure.InvalidAudio> {
            WavPcm.parse(file)
        }
    }

    @Test
    fun missingFileIsRejected() {
        val missing = newFile("absent.wav")

        val failure = expectThrowable<ForegroundAsrFailure.InvalidAudio> {
            WavPcm.parse(missing)
        }
        assertTrue(failure.message!!.contains("absent.wav"))
    }

    @Test
    fun truncatedHeaderIsRejected() {
        val file = newFile("tiny.wav")
        file.writeBytes(ByteArray(20))

        expectThrowable<ForegroundAsrFailure.InvalidAudio> {
            WavPcm.parse(file)
        }
    }

    @Test
    fun dataBeforeFmtChunkIsRejected() {
        val out = ByteArrayOutputStream()
        fun tag(s: String) = out.write(s.toByteArray(Charsets.US_ASCII))
        fun u16(v: Int) {
            out.write(v and 0xFF)
            out.write((v shr 8) and 0xFF)
        }
        fun u32(v: Long) {
            repeat(4) { i -> out.write(((v shr (8 * i)) and 0xFF).toInt()) }
        }
        tag("RIFF")
        u32(36L + 64_000)
        tag("WAVE")
        tag("data")
        u32(64_000L)
        tag("fmt ")
        u32(16)
        u16(1)
        u16(1)
        u32(16_000L)
        u32(32_000L)
        u16(2)
        u16(16)

        val file = newFile("reordered.wav")
        file.outputStream().use { stream ->
            stream.write(out.toByteArray())
            stream.write(ByteArray(64_000))
        }

        expectThrowable<ForegroundAsrFailure.InvalidAudio> {
            WavPcm.parse(file)
        }
    }

    private fun writeWav(
        file: File,
        sampleRate: Int,
        channels: Int,
        bits: Int,
        dataBytes: Int,
        audioFormat: Int = 1,
    ): File {
        val out = ByteArrayOutputStream()
        fun tag(s: String) = out.write(s.toByteArray(Charsets.US_ASCII))
        fun u16(v: Int) {
            out.write(v and 0xFF)
            out.write((v shr 8) and 0xFF)
        }
        fun u32(v: Long) {
            repeat(4) { i -> out.write(((v shr (8 * i)) and 0xFF).toInt()) }
        }
        val byteRate = sampleRate.toLong() * channels * (bits / 8)
        val blockAlign = channels * (bits / 8)
        tag("RIFF")
        u32(36L + dataBytes)
        tag("WAVE")
        tag("fmt ")
        u32(16)
        u16(audioFormat)
        u16(channels)
        u32(sampleRate.toLong())
        u32(byteRate)
        u16(blockAlign)
        u16(bits)
        tag("data")
        u32(dataBytes.toLong())

        file.outputStream().use { stream ->
            stream.write(out.toByteArray())
            stream.write(ByteArray(dataBytes))
        }
        return file
    }

    private inline fun <reified T : Throwable> expectThrowable(block: () -> Unit): T {
        try {
            block()
        } catch (t: Throwable) {
            if (t is T) return t
            throw AssertionError("expected ${T::class.java.simpleName}, got ${t::class.java.name}", t)
        }
        throw AssertionError("expected ${T::class.java.simpleName}, nothing was thrown")
    }
}