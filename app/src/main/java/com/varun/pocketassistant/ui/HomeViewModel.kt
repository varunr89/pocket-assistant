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
import com.varun.pocketassistant.capture.WeeklyCaptureSchedule
import com.varun.pocketassistant.data.MeetingEntity
import com.varun.pocketassistant.data.OverlapPreview
import com.varun.pocketassistant.data.SegmentEntity
import com.varun.pocketassistant.data.SessionEntity
import com.varun.pocketassistant.meeting.GapClusterer
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

data class PlaybackState(
    val segmentId: String? = null,
    val isPlaying: Boolean = false,
)

enum class HomeBrowseMode { DAY, LIST }

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

    /** Persisted weekly capture schedule + override (Room-backed). */
    val captureSchedule: StateFlow<WeeklyCaptureSchedule> =
        app.container.captureScheduleStore.observe()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), WeeklyCaptureSchedule())

    private val _playback = MutableStateFlow(PlaybackState())
    val playback: StateFlow<PlaybackState> = _playback.asStateFlow()

    private val _createPreview = MutableStateFlow<OverlapPreview?>(null)
    val createPreview: StateFlow<OverlapPreview?> = _createPreview.asStateFlow()

    private val _browseMode = MutableStateFlow(HomeBrowseMode.DAY)
    val browseMode: StateFlow<HomeBrowseMode> = _browseMode.asStateFlow()

    private val _dayStartMs = MutableStateFlow(startOfDayMs())
    val dayStartMs: StateFlow<Long> = _dayStartMs.asStateFlow()

    private val _daySegments = MutableStateFlow<List<SegmentEntity>>(emptyList())
    val daySegments: StateFlow<List<SegmentEntity>> = _daySegments.asStateFlow()

    private val _dayMeetings = MutableStateFlow<List<MeetingEntity>>(emptyList())
    val dayMeetings: StateFlow<List<MeetingEntity>> = _dayMeetings.asStateFlow()

    private val _proposals = MutableStateFlow<List<GapClusterer.Proposal>>(emptyList())
    val proposals: StateFlow<List<GapClusterer.Proposal>> = _proposals.asStateFlow()

    private val dismissedProposalIds = mutableSetOf<String>()

    private val _selectionStartMs = MutableStateFlow<Long?>(null)
    val selectionStartMs: StateFlow<Long?> = _selectionStartMs.asStateFlow()

    private val _selectionEndMs = MutableStateFlow<Long?>(null)
    val selectionEndMs: StateFlow<Long?> = _selectionEndMs.asStateFlow()

    private var mediaPlayer: MediaPlayer? = null

    val allRecordings: StateFlow<List<SegmentEntity>> = repo.observeAllRecordings()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    init {
        viewModelScope.launch {
            combine(_dayStartMs, allRecordings, allMeetings) { day, _, _ -> day }
                .collect { refreshDay(it) }
        }
    }

    private suspend fun refreshDay(dayStart: Long) {
        val dayEnd = dayStart + TimeUnit.DAYS.toMillis(1)
        _daySegments.value = meetingRepo.getSegmentsOverlapping(dayStart, dayEnd)
            .sortedBy { it.startedAtMs }
        _dayMeetings.value = meetingRepo.getMeetingsOverlapping(dayStart, dayEnd)
        // Drop proposals that no longer apply after assign/accept.
        _proposals.value = _proposals.value.filter { p ->
            p.id !in dismissedProposalIds &&
                p.segmentIds.any { id ->
                    _daySegments.value.any { it.id == id && it.meetingId == null }
                }
        }
    }

    fun setBrowseMode(mode: HomeBrowseMode) {
        _browseMode.value = mode
    }

    fun shiftDay(deltaDays: Int) {
        _dayStartMs.value += TimeUnit.DAYS.toMillis(deltaDays.toLong())
        clearSelection()
        _proposals.value = emptyList()
    }

    fun goToToday() {
        _dayStartMs.value = startOfDayMs()
        clearSelection()
        _proposals.value = emptyList()
    }

    fun suggestMeetingsForDay() {
        viewModelScope.launch {
            val day = _dayStartMs.value
            val dayEnd = day + TimeUnit.DAYS.toMillis(1)
            val segs = meetingRepo.getSegmentsOverlapping(day, dayEnd)
                .filter { it.meetingId == null }
            val clustered = GapClusterer.cluster(segs)
                .filter { it.id !in dismissedProposalIds }
            _proposals.value = clustered
        }
    }

    fun acceptProposal(proposal: GapClusterer.Proposal, onCreated: (String) -> Unit = {}) {
        viewModelScope.launch {
            val meeting = meetingRepo.createMeeting(proposal.startMs, proposal.endMs)
            dismissedProposalIds += proposal.id
            _proposals.value = _proposals.value.filter { it.id != proposal.id }
            refreshDay(_dayStartMs.value)
            onCreated(meeting.id)
        }
    }

    fun dismissProposal(proposal: GapClusterer.Proposal) {
        dismissedProposalIds += proposal.id
        _proposals.value = _proposals.value.filter { it.id != proposal.id }
    }

    fun setTimelineSelection(startMs: Long?, endMs: Long?) {
        _selectionStartMs.value = startMs
        _selectionEndMs.value = endMs
    }

    fun clearSelection() {
        _selectionStartMs.value = null
        _selectionEndMs.value = null
    }

    fun createMeetingFromSelection(onCreated: (String) -> Unit) {
        val a = _selectionStartMs.value ?: return
        val b = _selectionEndMs.value ?: return
        val start = min(a, b)
        val end = max(a, b)
        if (abs(end - start) < 60_000L) return
        createMeeting(start, end) { id ->
            clearSelection()
            onCreated(id)
        }
    }

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

    /** Manual "mic on outside schedule" override — the only manual on/off. */
    fun setCaptureOverride(enabled: Boolean) {
        viewModelScope.launch {
            app.container.captureScheduleStore.setOverride(enabled)
        }
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
            refreshDay(_dayStartMs.value)
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
            refreshDay(_dayStartMs.value)
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

    fun retranscribeRecording(segmentId: String) {
        viewModelScope.launch {
            repo.requeueForAsr(segmentId)
        }
    }

    fun observeAsrAttempts(segmentId: String) =
        app.container.pipelineScheduler.observeAsr(segmentId)

    fun observeMeetingWork(meetingId: String) =
        app.container.pipelineScheduler.observeMeeting(meetingId)

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
    CaptureState.SCHEDULED_OFF -> "Schedule off"
}
