package com.varun.pocketassistant.data

import android.content.Context
import androidx.room.Room
import com.varun.pocketassistant.capture.AudioStorage
import com.varun.pocketassistant.capture.RetentionPolicy
import com.varun.pocketassistant.pipeline.PipelineConfig
import com.varun.pocketassistant.speech.MeetingProcessor
import com.varun.pocketassistant.speech.TranscriptionQueue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

class AppContainer(context: Context) {
    private val appContext = context.applicationContext
    private val appScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    val database: AppDatabase = Room.databaseBuilder(
        appContext,
        AppDatabase::class.java,
        "pocket_assistant.db",
    ).fallbackToDestructiveMigration().build()

    val audioStorage = AudioStorage(appContext)
    val retentionPolicy = RetentionPolicy(daysToKeep = 14)
    val pipelineConfig = PipelineConfig(appContext)

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

    val transcriptionQueue = TranscriptionQueue(
        context = appContext,
        repository = sessionRepository,
        scope = appScope,
        pipelineConfig = pipelineConfig,
    ).also { sessionRepository.transcriptionQueue = it }

    val meetingProcessor = MeetingProcessor(
        context = appContext,
        repository = meetingRepository,
        scope = appScope,
        pipelineConfig = pipelineConfig,
    ).also { meetingRepository.meetingProcessor = it }
}
