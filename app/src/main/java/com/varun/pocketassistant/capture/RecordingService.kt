package com.varun.pocketassistant.capture

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.app.ServiceCompat
import com.varun.pocketassistant.MainActivity
import com.varun.pocketassistant.PocketAssistantApp
import com.varun.pocketassistant.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class RecordingService : Service() {
    private val serviceJob = SupervisorJob()
    private val serviceScope = CoroutineScope(serviceJob + Dispatchers.Main.immediate)

    private lateinit var engine: AudioCaptureEngine
    private var lastNotifyAtMs = 0L
    private var lastNotifyKey: String? = null

    override fun onCreate() {
        super.onCreate()
        createChannel()
        val app = application as PocketAssistantApp
        val ioScope = CoroutineScope(serviceJob + Dispatchers.IO)
        engine = AudioCaptureEngine(
            context = this,
            sessionRepository = app.container.sessionRepository,
            audioStorage = app.container.audioStorage,
            scope = ioScope,
            pipelineConfig = app.container.pipelineConfig,
            gate = CaptureScheduleGate(
                scheduleFlow = app.container.captureScheduleStore.observe(),
                scope = serviceScope,
            ),
        )
        RecordingHub.bind(engine)

        serviceScope.launch {
            engine.stats.collectLatest { stats ->
                RecordingHub.publish(stats)
                maybeUpdateNotification(stats)
            }
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_START -> {
                startAsForeground()
                engine.start()
            }
            ACTION_PAUSE -> engine.pause()
            ACTION_RESUME -> {
                startAsForeground()
                engine.resume()
            }
            ACTION_STOP -> {
                engine.stop()
                stopForeground(STOP_FOREGROUND_REMOVE)
                stopSelf()
            }
            else -> {
                startAsForeground()
                engine.start()
            }
        }
        return START_STICKY
    }

    private fun startAsForeground() {
        ServiceCompat.startForeground(
            this,
            NOTIFICATION_ID,
            buildNotification(engine.stats.value),
            ServiceInfo.FOREGROUND_SERVICE_TYPE_MICROPHONE,
        )
    }

    private fun maybeUpdateNotification(stats: CaptureStats) {
        val key = "${stats.state}|${stats.phase}|${stats.segmentCount}|${stats.captionsStatus}|" +
            "${stats.currentSegmentBytes / (256 * 1024)}"
        val now = System.currentTimeMillis()
        if (key == lastNotifyKey && now - lastNotifyAtMs < 1_000L) return
        lastNotifyKey = key
        lastNotifyAtMs = now
        getSystemService(NotificationManager::class.java)
            .notify(NOTIFICATION_ID, buildNotification(stats))
    }

    private fun buildNotification(stats: CaptureStats): Notification {
        val openIntent = PendingIntent.getActivity(
            this,
            0,
            Intent(this, MainActivity::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )

        val title = when (stats.phase) {
            CapturePhase.LIVE_SPEECH -> "Live speech — writing"
            CapturePhase.PRE_ROLL_FLUSH -> "Flushing pre-roll buffer"
            CapturePhase.POST_ROLL -> "Post-roll (${stats.hangoverRemainingMs / 1000}s left)"
            CapturePhase.LISTENING -> "Listening — buffering ${stats.preRollBufferedMs / 1000}s"
            CapturePhase.PAUSED -> getString(R.string.notification_paused)
            CapturePhase.SCHEDULED_OFF -> "Mic off — outside schedule"
            CapturePhase.IDLE -> getString(R.string.app_name)
        }

        val openMb = AudioCaptureEngine.formatBytes(stats.currentSegmentBytes)
        val text = if (stats.phase == CapturePhase.SCHEDULED_OFF) {
            "Outside your capture schedule — nothing is captured. Turn on " +
                "\"Mic on outside schedule\" to capture now."
        } else if (stats.speechProbability >= 0f) {
            "VAD p=%.2f · seg %d · open %s · %s".format(
                stats.speechProbability,
                stats.segmentCount,
                openMb,
                stats.phase.name,
            )
        } else {
            "RMS ${stats.rms.toInt()}/${stats.speechThreshold.toInt()} · seg ${stats.segmentCount} · open $openMb · ${stats.phase.name}"
        }

        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(text)
            .setStyle(NotificationCompat.BigTextStyle().bigText(text))
            .setSmallIcon(R.drawable.ic_mic)
            .setContentIntent(openIntent)
            .setOngoing(stats.state != CaptureState.IDLE)
            .setOnlyAlertOnce(true)
            .setForegroundServiceBehavior(NotificationCompat.FOREGROUND_SERVICE_IMMEDIATE)
            .setCategory(NotificationCompat.CATEGORY_SERVICE)

        if (stats.phase == CapturePhase.SCHEDULED_OFF) {
            // The schedule owns the mic while gated; no pause/resume here.
        } else if (stats.state == CaptureState.RECORDING) {
            builder.addAction(0, "Pause", servicePendingIntent(ACTION_PAUSE, 1))
        } else if (stats.state == CaptureState.PAUSED) {
            builder.addAction(0, "Resume", servicePendingIntent(ACTION_RESUME, 2))
        }
        builder.addAction(0, "Stop", servicePendingIntent(ACTION_STOP, 3))

        return builder.build()
    }

    private fun servicePendingIntent(action: String, requestCode: Int): PendingIntent {
        val intent = Intent(this, RecordingService::class.java).setAction(action)
        return PendingIntent.getService(
            this,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )
    }

    private fun createChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID,
            getString(R.string.notification_channel_name),
            NotificationManager.IMPORTANCE_LOW,
        ).apply {
            description = getString(R.string.notification_channel_desc)
        }
        getSystemService(NotificationManager::class.java).createNotificationChannel(channel)
    }

    override fun onDestroy() {
        RecordingHub.unbind(engine)
        serviceScope.cancel()
        serviceJob.cancel()
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    companion object {
        const val CHANNEL_ID = "recording"
        const val NOTIFICATION_ID = 42
        const val ACTION_START = "com.varun.pocketassistant.action.START"
        const val ACTION_PAUSE = "com.varun.pocketassistant.action.PAUSE"
        const val ACTION_RESUME = "com.varun.pocketassistant.action.RESUME"
        const val ACTION_STOP = "com.varun.pocketassistant.action.STOP"

        fun start(context: Context) {
            context.startForegroundService(
                Intent(context, RecordingService::class.java).setAction(ACTION_START),
            )
        }

        fun pause(context: Context) {
            context.startService(
                Intent(context, RecordingService::class.java).setAction(ACTION_PAUSE),
            )
        }

        fun resume(context: Context) {
            context.startForegroundService(
                Intent(context, RecordingService::class.java).setAction(ACTION_RESUME),
            )
        }

        fun stop(context: Context) {
            context.startService(
                Intent(context, RecordingService::class.java).setAction(ACTION_STOP),
            )
        }
    }
}

/** Process-wide handle so the UI can observe live capture stats. */
object RecordingHub {
    @Volatile
    private var engine: AudioCaptureEngine? = null

    private val _stats = MutableStateFlow(CaptureStats())
    val stats: StateFlow<CaptureStats> = _stats.asStateFlow()

    fun bind(engine: AudioCaptureEngine) {
        this.engine = engine
    }

    fun unbind(engine: AudioCaptureEngine) {
        if (this.engine === engine) {
            this.engine = null
            _stats.value = CaptureStats()
        }
    }

    fun publish(stats: CaptureStats) {
        _stats.value = stats
    }

    fun current(): AudioCaptureEngine? = engine
}
