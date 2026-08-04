package com.varun.pocketassistant.capture

import android.content.Context
import java.io.File

class AudioStorage(context: Context) {
    private val root: File = File(context.filesDir, "sessions").also { it.mkdirs() }

    fun sessionsRoot(): File = root

    fun sessionDir(sessionId: String): File =
        File(root, sessionId).also { it.mkdirs() }

    fun ensureSessionDir(sessionId: String): File = sessionDir(sessionId)

    fun newSegmentFile(sessionId: String, startedAtMs: Long): File =
        File(sessionDir(sessionId), "speech_$startedAtMs.wav")

    fun deleteSessionDir(sessionId: String) {
        sessionDir(sessionId).deleteRecursively()
    }
}

class RetentionPolicy(private val daysToKeep: Int) {
    fun cutoffEpochMs(nowMs: Long): Long =
        nowMs - daysToKeep * 24L * 60L * 60L * 1000L
}
