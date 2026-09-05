package com.varun.pocketassistant.data

import android.content.Context
import androidx.room.Room
import com.varun.pocketassistant.capture.AudioStorage
import com.varun.pocketassistant.capture.RetentionPolicy
import com.varun.pocketassistant.pipeline.CloudAsrProvider
import com.varun.pocketassistant.pipeline.CloudCircuitBreaker
import com.varun.pocketassistant.pipeline.CloudCleanupProvider
import com.varun.pocketassistant.pipeline.GemmaCleanupProvider
import com.varun.pocketassistant.pipeline.OpenAiCompatibleClient
import com.varun.pocketassistant.pipeline.ParakeetAsrProvider
import com.varun.pocketassistant.pipeline.PipelineConfig
import com.varun.pocketassistant.pipeline.ProviderRouter
import com.varun.pocketassistant.pipeline.work.PipelineScheduler
import com.varun.pocketassistant.speech.AsrStage
import com.varun.pocketassistant.speech.MeetingStage
import com.varun.pocketassistant.speech.SpeakerProfileStore
import java.io.File

class AppContainer(context: Context) {
    private val appContext = context.applicationContext

    val database: AppDatabase = Room.databaseBuilder(
        appContext,
        AppDatabase::class.java,
        "pocket_assistant.db",
    )
        .addMigrations(MIGRATION_4_5, MIGRATION_5_6, MIGRATION_6_7, MIGRATION_7_8)
        .build()

    val audioStorage = AudioStorage(appContext)
    val retentionPolicy = RetentionPolicy(daysToKeep = 14)
    val pipelineConfig = PipelineConfig(appContext)
    val speakerStore = SpeakerProfileStore(appContext)

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
