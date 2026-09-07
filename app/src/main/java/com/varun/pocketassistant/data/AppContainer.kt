package com.varun.pocketassistant.data

import android.content.Context
import androidx.room.Room
import com.varun.pocketassistant.capture.AudioStorage
import com.varun.pocketassistant.capture.CaptureScheduleStore
import com.varun.pocketassistant.capture.RetentionPolicy
import com.varun.pocketassistant.pipeline.CloudAsrProvider
import com.varun.pocketassistant.pipeline.CloudCircuitBreaker
import com.varun.pocketassistant.pipeline.CloudCleanupProvider
import com.varun.pocketassistant.pipeline.GemmaCleanupProvider
import com.varun.pocketassistant.pipeline.OpenAiCompatibleClient
import com.varun.pocketassistant.pipeline.ParakeetAsrProvider
import com.varun.pocketassistant.pipeline.PipelineConfig
import com.varun.pocketassistant.pipeline.ProviderMode
import com.varun.pocketassistant.pipeline.ProviderRouter
import com.varun.pocketassistant.pipeline.work.PipelineScheduler
import com.varun.pocketassistant.speech.AppForegroundTracker
import com.varun.pocketassistant.speech.AsrStage
import com.varun.pocketassistant.speech.MLKitGenAiAsrEngine
import com.varun.pocketassistant.speech.MeetingStage
import com.varun.pocketassistant.speech.RepositoryCatchUpQueue
import com.varun.pocketassistant.speech.SpeakerProfileStore
import com.varun.pocketassistant.speech.TranscriptionDrain
import com.varun.pocketassistant.speech.TranscriptionDrainHub
import com.varun.pocketassistant.speech.TranscriptionDrainService
import java.io.File
import java.util.Locale

class AppContainer(context: Context) {
    private val appContext = context.applicationContext

    val database: AppDatabase = Room.databaseBuilder(
        appContext,
        AppDatabase::class.java,
        "pocket_assistant.db",
    )
        .addMigrations(MIGRATION_4_5, MIGRATION_5_6, MIGRATION_6_7, MIGRATION_7_8, MIGRATION_8_9)
        .build()

    val audioStorage = AudioStorage(appContext)
    val retentionPolicy = RetentionPolicy(daysToKeep = 14)
    val pipelineConfig = PipelineConfig(appContext)
    val speakerStore = SpeakerProfileStore(appContext)

    /** Persisted weekly capture schedule + override (Room `settings` KV row). */
    val captureScheduleStore = CaptureScheduleStore(database.settingsDao())

    val cloudCircuitBreaker = CloudCircuitBreaker()
    val openAiClient = OpenAiCompatibleClient(pipelineConfig, cloudCircuitBreaker)

    val providerRouter = ProviderRouter(
        config = pipelineConfig,
        localAsr = ParakeetAsrProvider(appContext),
        cloudAsr = CloudAsrProvider(appContext, pipelineConfig, openAiClient),
        localText = GemmaCleanupProvider(appContext, pipelineConfig),
        cloudText = CloudCleanupProvider(pipelineConfig, openAiClient),
        circuitBreaker = cloudCircuitBreaker,
    )

    val sessionRepository = SessionRepository(
        sessionDao = database.sessionDao(),
        segmentDao = database.segmentDao(),
        audioStorage = audioStorage,
        retentionPolicy = retentionPolicy,
    )

    val meetingRepository = MeetingRepository(
        meetingDao = database.meetingDao(),
        segmentDao = database.segmentDao(),
    )

    /**
     * Foreground-gated catch-up transcription (M1 2026-09-06 option A).
     * The drain owns local/freemium ASR segments ONLY while the app is the
     * top foreground app; the tracker starts/stops the service on foreground
     * transitions. Cloud wiring (`cloud_stt`, paid tier) is untouched.
     */
    private val foregroundGate = AppForegroundTracker(appContext) { nowForeground ->
        if (nowForeground) {
            TranscriptionDrainService.start(appContext)
        } else {
            TranscriptionDrainService.stop(appContext)
        }
    }

    val catchUpQueue = RepositoryCatchUpQueue(sessionRepository)

    val transcriptionDrain = TranscriptionDrain(
        engine = MLKitGenAiAsrEngine(),
        queue = catchUpQueue,
        gate = foregroundGate,
        // Local mode owns transcription (freemium); cloud mode keeps the
        // legacy background worker until the routing increment re-wires it.
        enabled = { pipelineConfig.load().asrMode == ProviderMode.PREFER_LOCAL },
        localeTag = {
            val tag = pipelineConfig.load().sttLanguage
            if (tag.isBlank()) "en-US" else {
                runCatching { Locale.forLanguageTag(tag).toLanguageTag() }.getOrDefault("en-US")
            }
        },
        diagnosticsListener = { TranscriptionDrainHub.publish(it) },
    )

    val asrStage = AsrStage(
        repository = sessionRepository,
        router = providerRouter,
    )

    val meetingStage = MeetingStage(
        repository = meetingRepository,
        pipelineConfig = pipelineConfig,
        router = providerRouter,
    )

    val pipelineScheduler = PipelineScheduler(appContext).also {
        sessionRepository.pipelineScheduler = it
        meetingRepository.pipelineScheduler = it
    }

    init {
        sweepPrepCaches()
    }

    private fun sweepPrepCaches() {
        listOf("cloud_asr_prep", "asr_prep").forEach { name ->
            val dir = File(appContext.cacheDir, name)
            if (dir.isDirectory) {
                dir.deleteRecursively()
            }
        }
    }
}
