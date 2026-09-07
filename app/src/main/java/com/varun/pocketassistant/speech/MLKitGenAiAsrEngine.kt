package com.varun.pocketassistant.speech

import android.os.ParcelFileDescriptor
import android.util.Log
import com.google.mlkit.genai.common.DownloadStatus
import com.google.mlkit.genai.common.FeatureStatus
import com.google.mlkit.genai.common.GenAiException
import com.google.mlkit.genai.common.audio.AudioSource
import com.google.mlkit.genai.speechrecognition.SpeechRecognition
import com.google.mlkit.genai.speechrecognition.SpeechRecognizer
import com.google.mlkit.genai.speechrecognition.SpeechRecognizerOptions
import com.google.mlkit.genai.speechrecognition.SpeechRecognizerRequest
import com.google.mlkit.genai.speechrecognition.SpeechRecognizerResponse
import com.google.mlkit.genai.speechrecognition.speechRecognizerOptions
import com.google.mlkit.genai.speechrecognition.speechRecognizerRequest
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.RandomAccessFile
import java.util.Locale
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout

/**
 * Foreground-gated ASR through Google's official ML Kit GenAI Speech
 * Recognition API, Advanced mode (Gemini Nano via AICore, Pixel 10/11).
 *
 * Every symbol used here is verified against the shipped
 * `com.google.mlkit:genai-speech-recognition:1.0.0-alpha1` AAR (javap), the
 * official docs (developers.google.com/ml-kit/genai/speech-recognition/android)
 * and the official sample (googlesamples/mlkit android/speech). The API is
 * ALPHA — re-verify on any version bump.
 *
 * Contract notes:
 * - Non-streaming use: one Flow, collected to Completion; only
 *   [SpeechRecognizerResponse.FinalTextResponse] text is accumulated.
 *   Partial text is intentionally never published.
 * - [AudioSource.fromPfd] requires raw headerless PCM16 mono 16 kHz delivered
 *   at a real-time rate; the segment WAV header is validated and stripped by
 *   [WavPcm], then paced through a pipe at ~1x audio duration.
 * - AICore only serves the TOP FOREGROUND app: callers gate before calling;
 *   BACKGROUND_USE_BLOCKED is surfaced as [ForegroundAsrFailure.BackgroundUseBlocked].
 */
class MLKitGenAiAsrEngine : ForegroundAsrEngine {

    override val id: String = "mlkit_genai_advanced"

    override suspend fun transcribe(wav: File, localeTag: String): String {
        // Validate BEFORE touching AICore; garbage audio must fail visibly.
        val audio = WavPcm.parse(wav)
        val locale = parseLocale(localeTag)

        // Client is per-recognition: lifecycles are independent, no shared
        // mutable state, and close() in finally can never leak a handle.
        val recognizer = SpeechRecognition.getClient(
            speechRecognizerOptions {
                this.locale = locale
                preferredMode = SpeechRecognizerOptions.Mode.MODE_ADVANCED
            },
        )
        try {
            ensureModelReady(recognizer)
            val startedAt = System.currentTimeMillis()
            val text = runRecognition(recognizer, wav, audio)
            Log.i(
                TAG,
                "transcribed ${wav.name}: audioMs=${audio.durationMs} chars=${text.length} " +
                    "wallMs=${System.currentTimeMillis() - startedAt}",
            )
            return text
        } finally {
            runCatching { recognizer.close() }
        }
    }

    /** checkStatus → download-on-demand; terminal bound so a stuck AICore is visible. */
    private suspend fun ensureModelReady(recognizer: SpeechRecognizer) {
        repeat(MAX_STATUS_POLLS) { poll ->
            when (recognizer.checkStatus()) {
                FeatureStatus.AVAILABLE -> return
                FeatureStatus.DOWNLOADABLE -> {
                    Log.i(TAG, "ASR model downloadable — starting download")
                    downloadModel(recognizer)
                    return
                }
                FeatureStatus.DOWNLOADING -> {
                    Log.i(TAG, "ASR model still downloading (poll ${poll + 1}/$MAX_STATUS_POLLS)")
                    delay(STATUS_POLL_DELAY_MS)
                }
                else -> throw ForegroundAsrFailure.Unavailable(
                    "ASR feature not available on this device or AICore is not ready " +
                        "(FeatureStatus not AVAILABLE/DOWNLOADABLE/DOWNLOADING)",
                )
            }
        }
        throw ForegroundAsrFailure.Unavailable(
            "ASR model did not become available after ${MAX_STATUS_POLLS * STATUS_POLL_DELAY_MS / 1000}s",
        )
    }

    private suspend fun downloadModel(recognizer: SpeechRecognizer) {
        var lastLogBytes = -1L
        recognizer.download().collect { status ->
            when (status) {
                is DownloadStatus.DownloadStarted -> {
                    Log.i(TAG, "ASR model download started: ${status.bytesToDownload} bytes")
                    lastLogBytes = 0
                }
                is DownloadStatus.DownloadProgress -> {
                    if (status.totalBytesDownloaded - lastLogBytes > LOG_EVERY_BYTES) {
                        Log.i(TAG, "ASR model download: ${status.totalBytesDownloaded} bytes")
                        lastLogBytes = status.totalBytesDownloaded
                    }
                }
                is DownloadStatus.DownloadCompleted -> Log.i(TAG, "ASR model download completed")
                is DownloadStatus.DownloadFailed ->
                    throw ForegroundAsrFailure.Unavailable(
                        "ASR model download failed: ${status.e.message}",
                    )
            }
        }
    }

    private suspend fun runRecognition(
        recognizer: SpeechRecognizer,
        wav: File,
        audio: WavPcm.FormatInfo,
    ): String {
        val pipes = ParcelFileDescriptor.createPipe()
        val readEnd = pipes[0]
        val writeEnd = pipes[1]
        val text = StringBuilder()
        // Recognition proceeds at the audio's real-time rate; bound total wall
        // time generously so a wedged AICore session is a visible failure.
        val timeoutMs = (audio.durationMs * 2 + MIN_TIMEOUT_SLACK_MS)
            .coerceIn(MIN_TIMEOUT_MS, MAX_TIMEOUT_MS)
        var failure: Throwable? = null

        coroutineScope {
            val feeder = async(Dispatchers.IO) { feedPaced(wav, audio, writeEnd) }
            try {
                withTimeout(timeoutMs) {
                    collectRecognition(recognizer, readEnd, text)
                }
            } catch (t: TimeoutCancellationException) {
                failure = ForegroundAsrFailure.Unavailable(
                    "ASR recognition timed out after ${timeoutMs}ms " +
                        "(audioMs=${audio.durationMs})",
                )
            } catch (ce: CancellationException) {
                // Real cancellation (drain stopped mid-segment): propagate.
                failure = ce
            } catch (t: Throwable) {
                failure = t
            } finally {
                // Unblock the feeder FIRST (a blocked pipe write only fails
                // once its fd is closed), then stop and join.
                runCatching { writeEnd.close() }
                runCatching { readEnd.close() }
                feeder.cancel()
                feeder.join()
                // Best-effort teardown even if the scope is being cancelled.
                withContext(NonCancellable) {
                    runCatching { recognizer.stopRecognition() }
                }
            }
        }

        failure?.let { throw it }
        return text.toString()
    }

    private suspend fun collectRecognition(
        recognizer: SpeechRecognizer,
        readEnd: ParcelFileDescriptor,
        text: StringBuilder,
    ) {
        val request: SpeechRecognizerRequest = speechRecognizerRequest {
            audioSource = AudioSource.fromPfd(readEnd)
        }
        recognizer.startRecognition(request).collect { response ->
            when (response) {
                is SpeechRecognizerResponse.FinalTextResponse -> text.append(response.text)
                // Partial text is subject to change; only finals are published.
                is SpeechRecognizerResponse.PartialTextResponse -> Unit
                is SpeechRecognizerResponse.CompletedResponse -> Unit
                is SpeechRecognizerResponse.ErrorResponse -> throw classify(response.e)
            }
        }
    }

    /** Paced pipe writer: ML Kit requires the fd to yield ~32 KB/s (real-time). */
    private suspend fun feedPaced(
        wav: File,
        audio: WavPcm.FormatInfo,
        writeEnd: ParcelFileDescriptor,
    ) {
        try {
            RandomAccessFile(wav, "r").use { raf ->
                raf.seek(audio.dataOffset)
                FileOutputStream(writeEnd.fileDescriptor).use { out ->
                    val buffer = ByteArray(CHUNK_BYTES)
                    var remaining = audio.dataBytes
                    while (remaining > 0) {
                        val toRead = minOf(remaining, buffer.size.toLong()).toInt()
                        val read = raf.read(buffer, 0, toRead)
                        if (read <= 0) break
                        out.write(buffer, 0, read)
                        remaining -= read
                        if (remaining > 0) delay(CHUNK_MS)
                    }
                    out.flush()
                }
            }
        } catch (ce: CancellationException) {
            throw ce
        } catch (e: IOException) {
            // Expected when the recogniser finished early and the drain closed
            // the pipe (EPIPE) or the scope was torn down. Not a transcript
            // failure by itself — the collector is the source of truth.
            Log.w(TAG, "audio feed interrupted: ${e.message}")
        }
    }

    private fun parseLocale(localeTag: String): Locale = when {
        localeTag.isBlank() -> Locale.US
        else -> runCatching { Locale.forLanguageTag(localeTag) }.getOrDefault(Locale.US)
    }

    private fun classify(e: GenAiException): ForegroundAsrFailure {
        val detail = e.message ?: "no message"
        return when (e.errorCode) {
            GenAiException.ErrorCode.BUSY ->
                ForegroundAsrFailure.Busy("AICore busy (short-window quota): $detail")
            GenAiException.ErrorCode.PER_APP_BATTERY_USE_QUOTA_EXCEEDED ->
                ForegroundAsrFailure.DailyQuotaExceeded("AICore daily battery quota exceeded: $detail")
            GenAiException.ErrorCode.BACKGROUND_USE_BLOCKED ->
                ForegroundAsrFailure.BackgroundUseBlocked("AICore blocked background use: $detail")
            else ->
                ForegroundAsrFailure.Unavailable("AICore errorCode=${e.errorCode}: $detail")
        }
    }

    companion object {
        private const val TAG = "MLKitGenAiAsr"

        // 100 ms of 16 kHz mono PCM16 = 3200 bytes.
        private const val CHUNK_MS = 100L
        private const val CHUNK_BYTES = 3200

        private const val MAX_STATUS_POLLS = 12
        private const val STATUS_POLL_DELAY_MS = 10_000L
        private const val LOG_EVERY_BYTES = 2_000_000L

        private const val MIN_TIMEOUT_SLACK_MS = 60_000L
        private const val MIN_TIMEOUT_MS = 60_000L
        private const val MAX_TIMEOUT_MS = 600_000L
    }
}