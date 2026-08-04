package com.varun.pocketassistant.ui

import android.app.Application
import android.media.MediaPlayer
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.varun.pocketassistant.PocketAssistantApp
import com.varun.pocketassistant.capture.CaptureState
import com.varun.pocketassistant.capture.CaptureStats
import com.varun.pocketassistant.capture.RecordingHub
import com.varun.pocketassistant.capture.RecordingService
import com.varun.pocketassistant.data.MeetingEntity
import com.varun.pocketassistant.data.OverlapPreview
import com.varun.pocketassistant.data.SegmentEntity
import com.varun.pocketassistant.data.SessionEntity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class PlaybackState(
    val segmentId: String? = null,
    val isPlaying: Boolean = false,
)

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    private val app = application as PocketAssistantApp
    private val repo = app.container.sessionRepository
    private val meetingRepo = app.container.meetingRepository

    val sessions: StateFlow<List<SessionEntity>> = repo.observeSessions()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val recordings: StateFlow<List<SegmentEntity>> = repo.observeUncategorizedRecordings()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    private val _meetingSearch = MutableStateFlow("")
    val meetingSearch: StateFlow<String> = _meetingSearch.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val meetings: StateFlow<List<MeetingEntity>> = _meetingSearch
        .flatMapLatest { q -> meetingRepo.searchMeetings(q) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    /** Unfiltered list for assign/move pickers. */
    val allMeetings: StateFlow<List<MeetingEntity>> = meetingRepo.observeMeetings()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val captureStats: StateFlow<CaptureStats> = RecordingHub.stats

    private val _playback = MutableStateFlow(PlaybackState())
    val playback: StateFlow<PlaybackState> = _playback.asStateFlow()

    private val _createPreview = MutableStateFlow<OverlapPreview?>(null)
    val createPreview: StateFlow<OverlapPreview?> = _createPreview.asStateFlow()

    private var mediaPlayer: MediaPlayer? = null

    fun setMeetingSearch(query: String) {
        _meetingSearch.value = query
    }

    fun startRecording() {
        RecordingService.start(getApplication())
    }

    fun pauseRecording() {
        RecordingService.pause(getApplication())
    }

    fun resumeRecording() {
        RecordingService.resume(getApplication())
    }

    fun stopRecording() {
        RecordingService.stop(getApplication())
    }

    fun deleteSession(sessionId: String) {
        viewModelScope.launch {
            if (_playback.value.segmentId != null) stopPlayback()
            repo.deleteSession(sessionId)
        }
    }

    fun observeSegments(sessionId: String): StateFlow<List<SegmentEntity>> =
        segmentFlows.getOrPut(sessionId) {
            repo.observeSegments(sessionId)
                .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
        }

    fun observeMeetingRecordings(meetingId: String): StateFlow<List<SegmentEntity>> =
        meetingRecordingFlows.getOrPut(meetingId) {
            meetingRepo.observeMeetingRecordings(meetingId)
                .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
        }

    fun observeMeeting(meetingId: String): StateFlow<MeetingEntity?> =
        meetingFlows.getOrPut(meetingId) {
            meetingRepo.observeMeeting(meetingId)
                .stateIn(
                    viewModelScope,
                    SharingStarted.WhileSubscribed(5_000),
                    meetings.value.firstOrNull { it.id == meetingId },
                )
        }

    private val segmentFlows = mutableMapOf<String, StateFlow<List<SegmentEntity>>>()
    private val meetingRecordingFlows = mutableMapOf<String, StateFlow<List<SegmentEntity>>>()
    private val meetingFlows = mutableMapOf<String, StateFlow<MeetingEntity?>>()

    fun previewMeetingRange(startMs: Long, endMs: Long) {
        viewModelScope.launch {
            _createPreview.value = if (endMs > startMs) {
                meetingRepo.previewOverlap(startMs, endMs)
            } else {
                null
            }
        }
    }

    fun clearCreatePreview() {
        _createPreview.value = null
    }

    fun createMeeting(startMs: Long, endMs: Long, onCreated: (String) -> Unit) {
        viewModelScope.launch {
            val meeting = meetingRepo.createMeeting(startMs, endMs)
            onCreated(meeting.id)
        }
    }

    fun updateMeetingRange(meetingId: String, startMs: Long, endMs: Long) {
        viewModelScope.launch {
            meetingRepo.updateMeetingRange(meetingId, startMs, endMs)
        }
    }

    fun deleteMeeting(meetingId: String) {
        viewModelScope.launch {
            meetingRepo.deleteMeeting(meetingId)
        }
    }

    fun retryMeetingCleanup(meetingId: String) {
        viewModelScope.launch {
            meetingRepo.retryCleanup(meetingId)
        }
    }

    fun assignRecordingToMeeting(segmentId: String, meetingId: String) {
        viewModelScope.launch {
            if (_playback.value.segmentId == segmentId) stopPlayback()
            meetingRepo.assignRecording(segmentId, meetingId)
        }
    }

    fun unassignRecording(segmentId: String) {
        viewModelScope.launch {
            meetingRepo.unassignRecording(segmentId)
        }
    }

    fun deleteRecording(segmentId: String) {
        viewModelScope.launch {
            if (_playback.value.segmentId == segmentId) stopPlayback()
            meetingRepo.deleteRecording(segmentId)
        }
    }

    fun togglePlayback(segment: SegmentEntity) {
        val current = _playback.value
        if (current.segmentId == segment.id && current.isPlaying) {
            stopPlayback()
            return
        }
        stopPlayback()
        try {
            val player = MediaPlayer().apply {
                setDataSource(segment.filePath)
                setOnCompletionListener { stopPlayback() }
                prepare()
                start()
            }
            mediaPlayer = player
            _playback.value = PlaybackState(segmentId = segment.id, isPlaying = true)
        } catch (_: Throwable) {
            _playback.value = PlaybackState()
        }
    }

    fun stopPlayback() {
        mediaPlayer?.run {
            runCatching { stop() }
            release()
        }
        mediaPlayer = null
        _playback.value = PlaybackState()
    }

    override fun onCleared() {
        stopPlayback()
        super.onCleared()
    }
}

fun CaptureState.label(): String = when (this) {
    CaptureState.IDLE -> "Idle"
    CaptureState.RECORDING -> "Listening"
    CaptureState.PAUSED -> "Paused"
}
