package com.varun.pocketassistant.capture

/**
 * What the capture engine is doing right now — used for on-screen observability.
 */
enum class CapturePhase {
    IDLE,
    LISTENING,
    PRE_ROLL_FLUSH,
    LIVE_SPEECH,
    POST_ROLL,
    PAUSED,
    /** Capture schedule gate is closed: mic released, nothing written. */
    SCHEDULED_OFF,
}

data class CaptureEvent(
    val atMs: Long,
    val message: String,
)

data class RmsSample(
    val atMs: Long,
    val rms: Float,
    val phase: CapturePhase,
)

data class CaptureStats(
    val state: CaptureState = CaptureState.IDLE,
    val phase: CapturePhase = CapturePhase.IDLE,
    val sessionId: String? = null,
    val segmentCount: Int = 0,
    val speechActive: Boolean = false,
    /** Instantaneous mic energy (RMS of latest PCM window). */
    val rms: Float = 0f,
    val peak: Float = 0f,
    /**
     * Open-gate threshold for display:
     * - TEN VAD: speech probability in [0, 1]
     * - Energy fallback: RMS threshold
     */
    val speechThreshold: Float = 0.5f,
    /** Latest TEN speech probability in [0, 1], or -1 if energy VAD. */
    val speechProbability: Float = -1f,
    /** Rolling ~60s RMS history for the live graph. */
    val rmsHistory: List<RmsSample> = emptyList(),
    /** How much pre-roll is currently buffered while waiting for speech. */
    val preRollBufferedMs: Long = 0L,
    /** Remaining post-roll hangover after last speech frame. */
    val hangoverRemainingMs: Long = 0L,
    /** Progress toward the sustained-speech gate before a segment opens. */
    val openGateProgressMs: Long = 0L,
    val openGateRequiredMs: Long = 1_500L,
    val currentSegmentBytes: Long = 0L,
    val currentSegmentDurationMs: Long = 0L,
    val totalBytesWritten: Long = 0L,
    val framesRead: Long = 0L,
    val uptimeMs: Long = 0L,
    val lastSpeechAtMs: Long? = null,
    val liveTranscript: String = "",
    val partialTranscript: String = "",
    val captionsStatus: String = "off",
    val lastError: String? = null,
    val events: List<CaptureEvent> = emptyList(),
)

data class VadDecision(
    val windowOpen: Boolean,
    val rawSpeech: Boolean,
    val hangoverRemainingMs: Long,
    val openGateProgressMs: Long = 0L,
    val openGateRequiredMs: Long = 1_500L,
    val speechProbability: Float = -1f,
)
