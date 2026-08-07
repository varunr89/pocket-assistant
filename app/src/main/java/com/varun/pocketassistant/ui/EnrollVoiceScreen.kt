package com.varun.pocketassistant.ui

import android.Manifest
import android.content.pm.PackageManager
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.varun.pocketassistant.PocketAssistantApp
import com.varun.pocketassistant.capture.AudioCaptureEngine
import com.varun.pocketassistant.capture.WavWriter
import com.varun.pocketassistant.speech.VoiceEmbedder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.sqrt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EnrollVoiceScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val app = context.applicationContext as PocketAssistantApp
    val store = app.container.speakerStore
    val scope = rememberCoroutineScope()

    var status by remember {
        mutableStateOf(
            if (store.hasEnrollment()) "Voice profile enrolled. Re-train anytime."
            else "No voice profile yet.",
        )
    }
    var recording by remember { mutableStateOf(false) }
    var secondsLeft by remember { mutableIntStateOf(0) }
    var error by remember { mutableStateOf<String?>(null) }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { granted ->
        if (!granted) error = "Microphone permission required"
    }

    fun startEnrollment() {
        val granted = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.RECORD_AUDIO,
        ) == PackageManager.PERMISSION_GRANTED
        if (!granted) {
            permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
            return
        }
        error = null
        recording = true
        secondsLeft = 15
        scope.launch(Dispatchers.IO) {
            val out = store.enrollmentWavFile()
            val minBuf = AudioRecord.getMinBufferSize(
                AudioCaptureEngine.SAMPLE_RATE,
                AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT,
            )
            val recorder = AudioRecord(
                MediaRecorder.AudioSource.MIC,
                AudioCaptureEngine.SAMPLE_RATE,
                AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT,
                maxOf(minBuf, AudioCaptureEngine.SAMPLE_RATE / 5) * 2,
            )
            if (recorder.state != AudioRecord.STATE_INITIALIZED) {
                withContext(Dispatchers.Main) {
                    recording = false
                    error = "Could not open microphone"
                }
                return@launch
            }
            val writer = WavWriter(out, AudioCaptureEngine.SAMPLE_RATE)
            val pcm = ArrayList<Short>(AudioCaptureEngine.SAMPLE_RATE * 15)
            val buf = ShortArray(AudioCaptureEngine.SAMPLE_RATE / 10)
            recorder.startRecording()
            val endAt = System.currentTimeMillis() + 15_000L
            try {
                while (isActive && System.currentTimeMillis() < endAt) {
                    val n = recorder.read(buf, 0, buf.size)
                    if (n > 0) {
                        writer.writePcm(buf, 0, n)
                        for (i in 0 until n) pcm.add(buf[i])
                    }
                    val left = ((endAt - System.currentTimeMillis()) / 1000L).toInt().coerceAtLeast(0)
                    withContext(Dispatchers.Main) { secondsLeft = left }
                }
            } finally {
                runCatching { recorder.stop() }
                recorder.release()
                writer.close()
            }

            val samples = pcm.toShortArray()
            val filtered = samples.filterIndexed { idx, _ ->
                if (idx % 320 != 0) {
                    true
                } else {
                    val end = minOf(samples.size, idx + 320)
                    var sum = 0.0
                    for (i in idx until end) {
                        val v = samples[i].toDouble()
                        sum += v * v
                    }
                    sqrt(sum / (end - idx)) >= 160.0
                }
            }.toShortArray()
            val speechish = if (filtered.isEmpty()) samples else filtered

            val embedding = VoiceEmbedder.embed(speechish, AudioCaptureEngine.SAMPLE_RATE)
            store.saveEmbedding(embedding)
            withContext(Dispatchers.Main) {
                recording = false
                status = "Enrolled. New transcripts will label [You] vs [Other]."
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Train my voice") },
                navigationIcon = {
                    TextButton(onClick = onBack) { Text("Back") }
                },
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(
                "Speak naturally for 15 seconds in a quiet place. " +
                    "We’ll use this as your voice profile so transcripts can mark what you said.",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Text(status, style = MaterialTheme.typography.titleLarge)
            if (recording) {
                Text(
                    "Recording… ${secondsLeft}s left",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.secondary,
                )
            }
            if (error != null) {
                Text(error!!, color = MaterialTheme.colorScheme.error)
            }
            Spacer(Modifier.height(8.dp))
            Row {
                Button(
                    onClick = { if (!recording) startEnrollment() },
                    enabled = !recording,
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp)),
                ) {
                    Icon(Icons.Default.Mic, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text(if (store.hasEnrollment()) "Re-train" else "Start training")
                }
            }
            Text(
                "This is a lightweight on-device profile for now. " +
                    "We’ll swap in a Tensor speaker-embedding model later for higher accuracy.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}
