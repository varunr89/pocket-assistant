package com.varun.pocketassistant.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.varun.pocketassistant.capture.AudioCaptureEngine
import com.varun.pocketassistant.capture.CapturePhase
import com.varun.pocketassistant.capture.CaptureStats
import com.varun.pocketassistant.capture.CaptureState
import com.varun.pocketassistant.data.MeetingEntity
import com.varun.pocketassistant.data.MeetingStatus
import com.varun.pocketassistant.data.SegmentEntity
import org.json.JSONObject
import java.text.DateFormat
import java.util.Date
import java.util.concurrent.TimeUnit

@Composable
fun PocketAssistantRoot(viewModel: HomeViewModel = viewModel()) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(
                viewModel = viewModel,
                onCreateMeeting = { navController.navigate("createMeeting") },
                onOpenMeeting = { id -> navController.navigate("meeting/$id") },
                onTrainVoice = { navController.navigate("enroll") },
                onPipeline = { navController.navigate("pipeline") },
            )
        }
        composable("enroll") {
            EnrollVoiceScreen(onBack = { navController.popBackStack() })
        }
        composable("pipeline") {
            PipelineSettingsScreen(onBack = { navController.popBackStack() })
        }
        composable("createMeeting") {
            CreateMeetingScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onCreated = { id ->
                    navController.popBackStack()
                    navController.navigate("meeting/$id")
                },
            )
        }
        composable(
            route = "meeting/{meetingId}",
            arguments = listOf(navArgument("meetingId") { type = NavType.StringType }),
        ) { entry ->
            val meetingId = entry.arguments?.getString("meetingId") ?: return@composable
            MeetingDetailScreen(
                meetingId = meetingId,
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onCreateMeeting: () -> Unit,
    onOpenMeeting: (String) -> Unit,
    onTrainVoice: () -> Unit,
    onPipeline: () -> Unit,
) {
    val context = LocalContext.current
    val recordings by viewModel.recordings.collectAsState()
    val meetings by viewModel.meetings.collectAsState()
    val allMeetings by viewModel.allMeetings.collectAsState()
    val meetingSearch by viewModel.meetingSearch.collectAsState()
    val playback by viewModel.playback.collectAsState()
    val stats by viewModel.captureStats.collectAsState()
    val browseMode by viewModel.browseMode.collectAsState()
    val dayStartMs by viewModel.dayStartMs.collectAsState()
    val daySegments by viewModel.daySegments.collectAsState()
    val dayMeetings by viewModel.dayMeetings.collectAsState()
    val proposals by viewModel.proposals.collectAsState()
    val selectionStartMs by viewModel.selectionStartMs.collectAsState()
    val selectionEndMs by viewModel.selectionEndMs.collectAsState()

    var permissionHint by remember { mutableStateOf<String?>(null) }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions(),
    ) { result ->
        val granted = result[Manifest.permission.RECORD_AUDIO] == true
        if (granted) {
            permissionHint = null
            viewModel.startRecording()
        } else {
            permissionHint = "Microphone permission is required to capture conversations."
        }
    }

    fun ensurePermissionsAndStart() {
        val needsMic = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.RECORD_AUDIO,
        ) != PackageManager.PERMISSION_GRANTED
        val needsNotif = Build.VERSION.SDK_INT >= 33 &&
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS,
            ) != PackageManager.PERMISSION_GRANTED

        if (needsMic || needsNotif) {
            val perms = buildList {
                add(Manifest.permission.RECORD_AUDIO)
                if (Build.VERSION.SDK_INT >= 33) add(Manifest.permission.POST_NOTIFICATIONS)
            }
            permissionLauncher.launch(perms.toTypedArray())
        } else {
            viewModel.startRecording()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Pocket Assistant") },
                actions = {
                    TextButton(onClick = onPipeline) { Text("Pipeline") }
                    TextButton(onClick = onTrainVoice) { Text("Train voice") }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                ),
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onCreateMeeting) {
                Icon(Icons.Default.Add, contentDescription = "Create meeting")
            }
        },
        containerColor = Color.Transparent,
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            MaterialTheme.colorScheme.background,
                            Color(0xFFE8F0EC),
                            MaterialTheme.colorScheme.background,
                        ),
                    ),
                )
                .padding(padding),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp)
                    .verticalScroll(rememberScrollState()),
            ) {
                CaptureHero(
                    stats = stats,
                    error = stats.lastError ?: permissionHint,
                    onStart = { ensurePermissionsAndStart() },
                    onPause = viewModel::pauseRecording,
                    onResume = viewModel::resumeRecording,
                    onStop = viewModel::stopRecording,
                )

                Spacer(Modifier.height(16.dp))
                ObservabilityPanel(stats = stats)

                Spacer(Modifier.height(28.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilterChip(
                        selected = browseMode == HomeBrowseMode.DAY,
                        onClick = { viewModel.setBrowseMode(HomeBrowseMode.DAY) },
                        label = { Text("Day") },
                    )
                    FilterChip(
                        selected = browseMode == HomeBrowseMode.LIST,
                        onClick = { viewModel.setBrowseMode(HomeBrowseMode.LIST) },
                        label = { Text("List") },
                    )
                }
                Spacer(Modifier.height(12.dp))

                if (browseMode == HomeBrowseMode.DAY) {
                    DayTimelinePanel(
                        dayStartMs = dayStartMs,
                        segments = daySegments,
                        meetings = dayMeetings,
                        proposals = proposals,
                        selectionStartMs = selectionStartMs,
                        selectionEndMs = selectionEndMs,
                        onPrevDay = { viewModel.shiftDay(-1) },
                        onNextDay = { viewModel.shiftDay(1) },
                        onToday = viewModel::goToToday,
                        onSuggest = viewModel::suggestMeetingsForDay,
                        onAcceptProposal = { p ->
                            viewModel.acceptProposal(p) { id -> onOpenMeeting(id) }
                        },
                        onDismissProposal = viewModel::dismissProposal,
                        onOpenMeeting = onOpenMeeting,
                        onSelectionChange = viewModel::setTimelineSelection,
                        onCreateFromSelection = {
                            viewModel.createMeetingFromSelection { id -> onOpenMeeting(id) }
                        },
                        onAssignSegment = { segmentId, meetingId ->
                            viewModel.assignRecordingToMeeting(segmentId, meetingId)
                        },
                    )
                } else {
                Text("Meetings", style = MaterialTheme.typography.titleLarge)
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = meetingSearch,
                    onValueChange = viewModel::setMeetingSearch,
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    label = { Text("Search meetings") },
                    placeholder = { Text("Title, topics, people…") },
                )
                Spacer(Modifier.height(12.dp))
                if (meetings.isEmpty()) {
                    Text(
                        if (meetingSearch.isBlank()) {
                            "No meetings yet. Tap + to mark a start and end time."
                        } else {
                            "No meetings match “$meetingSearch”."
                        },
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                } else {
                    meetings.forEach { meeting ->
                        MeetingRow(
                            meeting = meeting,
                            onClick = { onOpenMeeting(meeting.id) },
                            onDelete = { viewModel.deleteMeeting(meeting.id) },
                        )
                        Spacer(Modifier.height(10.dp))
                    }
                }

                Spacer(Modifier.height(28.dp))
                Text("Uncategorized recordings", style = MaterialTheme.typography.titleLarge)
                Spacer(Modifier.height(4.dp))
                Text(
                    "Raw transcripts not yet in a meeting. Create a meeting to group, clean, and summarize them.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Spacer(Modifier.height(12.dp))
                if (recordings.isEmpty()) {
                    Text(
                        "Nothing uncategorized. New speech clips show up here until you add them to a meeting.",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                } else {
                    recordings.take(80).forEach { segment ->
                        RecordingRow(
                            segment = segment,
                            meetingLabel = null,
                            playing = playback.segmentId == segment.id && playback.isPlaying,
                            onPlay = { viewModel.togglePlayback(segment) },
                            onOpenMeeting = null,
                            meetings = allMeetings,
                            onAssignToMeeting = { meetingId ->
                                viewModel.assignRecordingToMeeting(segment.id, meetingId)
                            },
                            onUnassign = null,
                            onDelete = { viewModel.deleteRecording(segment.id) },
                            onRetranscribe = { viewModel.retranscribeRecording(segment.id) },
                        )
                        Spacer(Modifier.height(10.dp))
                    }
                }
                }
                Spacer(Modifier.height(88.dp))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateMeetingScreen(
    viewModel: HomeViewModel,
    onBack: () -> Unit,
    onCreated: (String) -> Unit,
) {
    val now = System.currentTimeMillis()
    var startMs by remember { mutableLongStateOf(now - TimeUnit.HOURS.toMillis(1)) }
    var endMs by remember { mutableLongStateOf(now) }
    val preview by viewModel.createPreview.collectAsState()
    val dateTimeFmt = remember {
        DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT)
    }

    LaunchedEffect(startMs, endMs) {
        viewModel.previewMeetingRange(startMs, endMs)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Create meeting") },
                navigationIcon = {
                    TextButton(onClick = {
                        viewModel.clearCreatePreview()
                        onBack()
                    }) { Text("Back") }
                },
            )
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(20.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(
                "Pick a time range. READY recordings that overlap it are linked and cleaned together.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            DateTimeSelector(
                label = "Start",
                epochMs = startMs,
                onChange = { startMs = it },
                modifier = Modifier.fillMaxWidth(),
            )
            DateTimeSelector(
                label = "End",
                epochMs = endMs,
                onChange = { endMs = it },
                modifier = Modifier.fillMaxWidth(),
            )

            val previewText = preview?.let {
                buildString {
                    append("${it.count} recordings · ${formatDuration(it.speechDurationMs)} speech")
                    if (it.pendingAsrCount > 0) {
                        append(" · ${it.readyCount} transcribed")
                        append(" (${formatDuration(it.readyDurationMs)})")
                        append(", ${it.pendingAsrCount} still transcribing")
                    }
                }
            } ?: if (endMs <= startMs) "End must be after start" else "Checking…"
            Text(previewText, style = MaterialTheme.typography.bodyLarge)

            preview?.recordings?.take(12)?.forEach { seg ->
                Text(
                    "• ${dateTimeFmt.format(Date(seg.startedAtMs))} · ${formatDuration(seg.durationMs)}" +
                        " · ${seg.transcriptStatus.lowercase()}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }

            Spacer(Modifier.height(8.dp))
            Button(
                onClick = {
                    viewModel.createMeeting(startMs, endMs) { id ->
                        viewModel.clearCreatePreview()
                        onCreated(id)
                    }
                },
                enabled = endMs > startMs && (preview?.count ?: 0) > 0,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(
                    if ((preview?.pendingAsrCount ?: 0) > 0) {
                        "Create (cleanup waits for ASR)"
                    } else {
                        "Create & clean"
                    },
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MeetingDetailScreen(
    meetingId: String,
    viewModel: HomeViewModel,
    onBack: () -> Unit,
) {
    val meeting by viewModel.observeMeeting(meetingId).collectAsState()
    val recordings by viewModel.observeMeetingRecordings(meetingId).collectAsState()
    val allMeetings by viewModel.allMeetings.collectAsState()
    val playback by viewModel.playback.collectAsState()
    val dateTimeFmt = remember {
        DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT)
    }
    var editing by remember(meetingId) { mutableStateOf(false) }
    var editStart by remember(meetingId) { mutableLongStateOf(0L) }
    var editEnd by remember(meetingId) { mutableLongStateOf(0L) }
    var editInitialized by remember(meetingId) { mutableStateOf(false) }

    LaunchedEffect(meeting?.id, meeting?.startedAtMs, meeting?.endedAtMs) {
        val m = meeting ?: return@LaunchedEffect
        if (!editInitialized || !editing) {
            editStart = m.startedAtMs
            editEnd = m.endedAtMs
            editInitialized = true
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        meeting?.title?.takeIf { it.isNotBlank() } ?: "Meeting",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                },
                navigationIcon = {
                    TextButton(onClick = onBack) { Text("Back") }
                },
                actions = {
                    TextButton(
                        onClick = {
                            if (editing) {
                                meeting?.let {
                                    editStart = it.startedAtMs
                                    editEnd = it.endedAtMs
                                }
                            }
                            editing = !editing
                        },
                        enabled = meeting != null,
                    ) {
                        Text(if (editing) "Cancel" else "Edit range")
                    }
                    IconButton(
                        onClick = {
                            viewModel.deleteMeeting(meetingId)
                            onBack()
                        },
                        enabled = meeting != null,
                    ) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete meeting")
                    }
                },
            )
        },
    ) { padding ->
        val current = meeting
        if (current == null) {
            // Keep layout stable while the cached/Room flow emits — avoid "not found" flash.
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    "Loading…",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            return@Scaffold
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState()),
        ) {
            Text(
                "${dateTimeFmt.format(Date(current.startedAtMs))} → ${dateTimeFmt.format(Date(current.endedAtMs))}",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Text(
                current.status.lowercase().replace('_', ' ') +
                    (current.cleanupProvider?.let { " · $it" } ?: ""),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            Spacer(Modifier.height(8.dp))
            OutlinedButton(
                onClick = { viewModel.retryMeetingCleanup(meetingId) },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text("Retry cleanup / summary")
            }

            if (editing) {
                Spacer(Modifier.height(12.dp))
                DateTimeSelector(
                    label = "Start",
                    epochMs = editStart,
                    onChange = { editStart = it },
                    modifier = Modifier.fillMaxWidth(),
                )
                DateTimeSelector(
                    label = "End",
                    epochMs = editEnd,
                    onChange = { editEnd = it },
                    modifier = Modifier.fillMaxWidth(),
                )
                Button(
                    onClick = {
                        viewModel.updateMeetingRange(meetingId, editStart, editEnd)
                        editing = false
                    },
                    enabled = editEnd > editStart,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text("Save range & reprocess")
                }
            }

            val meta = remember(current.metadataJson) { parseMeetingMeta(current.metadataJson) }
            if (meta.topics.isNotEmpty() || meta.people.isNotEmpty()) {
                Spacer(Modifier.height(12.dp))
                Row(
                    modifier = Modifier.horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    meta.people.forEach { FilterChip(selected = false, onClick = {}, label = { Text(it) }) }
                    meta.topics.forEach { FilterChip(selected = true, onClick = {}, label = { Text(it) }) }
                }
            }

            val statusMessage = when (current.status) {
                MeetingStatus.CLEANING.name -> "Cleaning / summarizing…"
                MeetingStatus.PENDING_CLEANUP.name -> {
                    val waitingAsr = recordings.any {
                        it.transcriptStatus == "PENDING" || it.transcriptStatus == "PROCESSING"
                    }
                    if (waitingAsr || recordings.isEmpty()) {
                        "Waiting for transcription to finish on linked recordings…"
                    } else {
                        "Queued for cleanup / summary…"
                    }
                }
                else -> null
            }
            if (statusMessage != null) {
                Spacer(Modifier.height(16.dp))
                Text(statusMessage, color = MaterialTheme.colorScheme.secondary)
            } else if (current.status == MeetingStatus.FAILED.name) {
                Spacer(Modifier.height(16.dp))
                Text(
                    current.lastError
                        ?: current.cleanedTranscript
                        ?: "Cleanup failed",
                    color = MaterialTheme.colorScheme.error,
                )
                if (!current.cleanTextOnly.isNullOrBlank()) {
                    Spacer(Modifier.height(8.dp))
                    Text(
                        "Cleaned transcript kept — Retry will finish summary.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            } else if (current.status == MeetingStatus.READY.name &&
                !current.cleanedTranscript.isNullOrBlank()
            ) {
                Spacer(Modifier.height(16.dp))
                Text(
                    current.cleanedTranscript.orEmpty(),
                    style = MaterialTheme.typography.bodyLarge,
                )
            }

            Spacer(Modifier.height(24.dp))
            Text(
                if (recordings.isEmpty()) {
                    "Linked recordings"
                } else {
                    "Linked recordings (${recordings.size})"
                },
                style = MaterialTheme.typography.titleLarge,
            )
            Spacer(Modifier.height(8.dp))
            if (recordings.isEmpty()) {
                Text(
                    "No recordings linked. Assign clips from Uncategorized, or edit the time range.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            } else {
                val otherMeetings = allMeetings.filter { it.id != meetingId }
                recordings.forEach { segment ->
                    key(segment.id) {
                        RecordingRow(
                            segment = segment,
                            meetingLabel = null,
                            playing = playback.segmentId == segment.id && playback.isPlaying,
                            onPlay = { viewModel.togglePlayback(segment) },
                            onOpenMeeting = null,
                            showRaw = true,
                            meetings = otherMeetings,
                            onAssignToMeeting = { targetId ->
                                viewModel.assignRecordingToMeeting(segment.id, targetId)
                            },
                            onUnassign = { viewModel.unassignRecording(segment.id) },
                            onDelete = { viewModel.deleteRecording(segment.id) },
                            onRetranscribe = { viewModel.retranscribeRecording(segment.id) },
                        )
                        Spacer(Modifier.height(10.dp))
                    }
                }
            }
            Spacer(Modifier.height(32.dp))
        }
    }
}

@Composable
private fun MeetingRow(
    meeting: MeetingEntity,
    onClick: () -> Unit,
    onDelete: () -> Unit,
) {
    val formatter = remember {
        DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT)
    }
    val overviewLine = remember(meeting.cleanedTranscript, meeting.metadataJson) {
        val topics = parseMeetingMeta(meeting.metadataJson).topics
        when {
            topics.isNotEmpty() -> topics.take(3).joinToString(" · ")
            !meeting.cleanedTranscript.isNullOrBlank() ->
                meeting.cleanedTranscript.lineSequence()
                    .map { it.trim() }
                    .firstOrNull { it.isNotBlank() && !it.startsWith("#") }
                    .orEmpty()
            else -> meeting.status.lowercase().replace('_', ' ')
        }
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(MaterialTheme.colorScheme.surface)
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                meeting.title?.takeIf { it.isNotBlank() } ?: "Untitled meeting",
                style = MaterialTheme.typography.titleLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Spacer(Modifier.height(4.dp))
            Text(
                "${formatter.format(Date(meeting.startedAtMs))} · ${meeting.status.lowercase().replace('_', ' ')}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            if (overviewLine.isNotBlank()) {
                Text(
                    overviewLine,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
        IconButton(onClick = onDelete) {
            Icon(Icons.Default.Delete, contentDescription = "Delete meeting")
        }
    }
}

@Composable
private fun RecordingRow(
    segment: SegmentEntity,
    meetingLabel: String?,
    playing: Boolean,
    onPlay: () -> Unit,
    onOpenMeeting: (() -> Unit)?,
    showRaw: Boolean = true,
    meetings: List<MeetingEntity> = emptyList(),
    onAssignToMeeting: ((String) -> Unit)? = null,
    onUnassign: (() -> Unit)? = null,
    onDelete: (() -> Unit)? = null,
    onRetranscribe: (() -> Unit)? = null,
) {
    val formatter = remember {
        DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM)
    }
    val meetingFmt = remember {
        DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT)
    }
    val raw = segment.diarizedTranscript?.takeIf { it.isNotBlank() }
        ?: segment.transcript?.takeIf { it.isNotBlank() }
    val canRetranscribe = onRetranscribe != null &&
        segment.transcriptStatus == "FAILED" &&
        segment.skipReason == null
    var menuOpen by remember { mutableStateOf(false) }
    var assignOpen by remember { mutableStateOf(false) }
    var confirmDelete by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(14.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onPlay) {
                Icon(
                    imageVector = if (playing) Icons.Default.Stop else Icons.Default.PlayArrow,
                    contentDescription = if (playing) "Stop" else "Play",
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    formatter.format(Date(segment.startedAtMs)),
                    style = MaterialTheme.typography.titleLarge,
                )
                Text(
                    formatDuration(segment.durationMs) + " · " + formatBytes(segment.byteSize) +
                        " · " + segment.transcriptStatus.lowercase() +
                        (segment.asrProvider?.let { " · $it" } ?: ""),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            if (onAssignToMeeting != null || onUnassign != null || onDelete != null || canRetranscribe) {
                Box {
                    IconButton(onClick = { menuOpen = true }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "Recording actions")
                    }
                    DropdownMenu(expanded = menuOpen, onDismissRequest = { menuOpen = false }) {
                        if (canRetranscribe) {
                            DropdownMenuItem(
                                text = { Text("Retranscribe") },
                                onClick = {
                                    menuOpen = false
                                    onRetranscribe?.invoke()
                                },
                            )
                        }
                        if (onAssignToMeeting != null) {
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        if (onUnassign != null) "Move to meeting…" else "Assign to meeting…",
                                    )
                                },
                                onClick = {
                                    menuOpen = false
                                    assignOpen = true
                                },
                            )
                        }
                        if (onUnassign != null) {
                            DropdownMenuItem(
                                text = { Text("Remove from meeting") },
                                onClick = {
                                    menuOpen = false
                                    onUnassign()
                                },
                            )
                        }
                        if (onDelete != null) {
                            DropdownMenuItem(
                                text = { Text("Delete recording") },
                                onClick = {
                                    menuOpen = false
                                    confirmDelete = true
                                },
                            )
                        }
                    }
                }
            }
        }
        if (meetingLabel != null && onOpenMeeting != null) {
            Spacer(Modifier.height(6.dp))
            FilterChip(
                selected = true,
                onClick = onOpenMeeting,
                label = { Text(meetingLabel) },
            )
        }
        when {
            showRaw && !raw.isNullOrBlank() && segment.transcriptStatus != "FAILED" -> {
                Spacer(Modifier.height(8.dp))
                Text(
                    raw,
                    style = MaterialTheme.typography.bodyLarge,
                    maxLines = 6,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            segment.transcriptStatus == "PENDING" ||
                segment.transcriptStatus == "PROCESSING" -> {
                Spacer(Modifier.height(8.dp))
                Text(
                    if (segment.asrLastError != null) {
                        "Retrying transcription… (${segment.asrLastError})"
                    } else {
                        "Transcribing…"
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.secondary,
                )
            }
            segment.transcriptStatus == "SKIPPED_SILENCE" -> {
                Spacer(Modifier.height(8.dp))
                Text(
                    "No speech recognized in this clip.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            segment.transcriptStatus == "FAILED" -> {
                Spacer(Modifier.height(8.dp))
                Text(
                    segment.asrLastError
                        ?: segment.diarizedTranscript
                        ?: "Transcription failed",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.error,
                )
                if (canRetranscribe) {
                    Spacer(Modifier.height(8.dp))
                    OutlinedButton(
                        onClick = { onRetranscribe?.invoke() },
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text("Retranscribe")
                    }
                }
            }
        }
    }

    if (assignOpen && onAssignToMeeting != null) {
        AlertDialog(
            onDismissRequest = { assignOpen = false },
            title = { Text(if (onUnassign != null) "Move to meeting" else "Assign to meeting") },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(320.dp)
                        .verticalScroll(rememberScrollState()),
                ) {
                    if (meetings.isEmpty()) {
                        Text(
                            "No other meetings yet. Create one first.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    } else {
                        meetings.forEach { meeting ->
                            TextButton(
                                onClick = {
                                    onAssignToMeeting(meeting.id)
                                    assignOpen = false
                                },
                                modifier = Modifier.fillMaxWidth(),
                            ) {
                                Text(
                                    buildString {
                                        append(meeting.title?.takeIf { it.isNotBlank() } ?: "Untitled")
                                        append(" · ")
                                        append(meetingFmt.format(Date(meeting.startedAtMs)))
                                    },
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis,
                                )
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { assignOpen = false }) { Text("Cancel") }
            },
        )
    }

    if (confirmDelete && onDelete != null) {
        AlertDialog(
            onDismissRequest = { confirmDelete = false },
            title = { Text("Delete recording?") },
            text = {
                Text("Removes this clip and its audio file. This cannot be undone.")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        confirmDelete = false
                        onDelete()
                    },
                ) { Text("Delete") }
            },
            dismissButton = {
                TextButton(onClick = { confirmDelete = false }) { Text("Cancel") }
            },
        )
    }
}

@Composable
private fun CaptureHero(
    stats: CaptureStats,
    error: String?,
    onStart: () -> Unit,
    onPause: () -> Unit,
    onResume: () -> Unit,
    onStop: () -> Unit,
) {
    val state = stats.state
    val speechActive = stats.speechActive
    val pulse = rememberInfiniteTransition(label = "pulse")
    val scale by pulse.animateFloat(
        initialValue = 1f,
        targetValue = if (state == CaptureState.RECORDING && speechActive) 1.08f else 1f,
        animationSpec = infiniteRepeatable(tween(700), RepeatMode.Reverse),
        label = "scale",
    )
    val orbColor by animateColorAsState(
        targetValue = when (stats.phase) {
            CapturePhase.LIVE_SPEECH -> MaterialTheme.colorScheme.secondary
            CapturePhase.PRE_ROLL_FLUSH -> Color(0xFFC9852A)
            CapturePhase.POST_ROLL -> MaterialTheme.colorScheme.tertiary
            CapturePhase.LISTENING -> MaterialTheme.colorScheme.primary
            CapturePhase.PAUSED -> MaterialTheme.colorScheme.tertiary
            CapturePhase.IDLE -> MaterialTheme.colorScheme.surfaceVariant
        },
        label = "orb",
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(28.dp))
            .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.72f))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            "Pocket Assistant",
            style = MaterialTheme.typography.displayLarge,
            color = MaterialTheme.colorScheme.primary,
        )
        Spacer(Modifier.height(8.dp))
        Text(
            phaseMessage(stats),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(Modifier.height(28.dp))

        Box(
            modifier = Modifier
                .size(120.dp)
                .scale(scale)
                .clip(CircleShape)
                .background(orbColor),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = Icons.Default.Mic,
                contentDescription = null,
                tint = if (state == CaptureState.IDLE) {
                    MaterialTheme.colorScheme.onSurfaceVariant
                } else {
                    Color.White
                },
                modifier = Modifier.size(48.dp),
            )
        }

        Spacer(Modifier.height(16.dp))
        Text(
            "${stats.phase.name} · ${stats.segmentCount} saved · open ${AudioCaptureEngine.formatBytes(stats.currentSegmentBytes)}",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurface,
        )

        if (error != null) {
            Spacer(Modifier.height(8.dp))
            Text(
                error,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.error,
            )
        }

        Spacer(Modifier.height(24.dp))
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            when (state) {
                CaptureState.IDLE -> {
                    Button(
                        onClick = onStart,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                        ),
                    ) {
                        Icon(Icons.Default.Mic, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        Text("Start")
                    }
                }
                CaptureState.RECORDING -> {
                    FilledTonalButton(onClick = onPause) {
                        Icon(Icons.Default.Pause, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        Text("Pause")
                    }
                    Button(
                        onClick = onStop,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.tertiary,
                        ),
                    ) {
                        Icon(Icons.Default.Stop, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        Text("Stop")
                    }
                }
                CaptureState.PAUSED -> {
                    Button(onClick = onResume) {
                        Icon(Icons.Default.PlayArrow, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        Text("Resume")
                    }
                    FilledTonalButton(onClick = onStop) {
                        Icon(Icons.Default.Stop, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        Text("Stop")
                    }
                }
            }
        }
    }
}

@Composable
private fun ObservabilityPanel(stats: CaptureStats) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(16.dp),
    ) {
        Text("Live signal", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(8.dp))
        RmsHistoryGraph(
            samples = stats.rmsHistory,
            threshold = if (stats.speechProbability >= 0f) 0f else stats.speechThreshold,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            if (stats.speechProbability >= 0f) {
                "TEN VAD p=%.2f / open %.2f · RMS %d · peak %d".format(
                    stats.speechProbability,
                    stats.speechThreshold,
                    stats.rms.toInt(),
                    stats.peak.toInt(),
                )
            } else {
                "RMS ${stats.rms.toInt()} / threshold ${stats.speechThreshold.toInt()} · peak ${stats.peak.toInt()}"
            },
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        if (stats.state == CaptureState.RECORDING &&
            stats.phase == CapturePhase.LISTENING &&
            stats.openGateProgressMs > 0
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "Speech gate: ${stats.openGateProgressMs}ms / ${stats.openGateRequiredMs}ms (majority voiced)",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.secondary,
            )
            LinearProgressIndicator(
                progress = {
                    (stats.openGateProgressMs.toFloat() / stats.openGateRequiredMs.toFloat())
                        .coerceIn(0f, 1f)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(8.dp)),
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            FilterChip(selected = stats.phase == CapturePhase.LISTENING, onClick = {}, label = { Text("Buffer") })
            FilterChip(selected = stats.phase == CapturePhase.PRE_ROLL_FLUSH, onClick = {}, label = { Text("Pre-roll") })
            FilterChip(selected = stats.phase == CapturePhase.LIVE_SPEECH, onClick = {}, label = { Text("Live") })
            FilterChip(selected = stats.phase == CapturePhase.POST_ROLL, onClick = {}, label = { Text("Post-roll") })
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            "Pre-roll buffer: ${stats.preRollBufferedMs / 1000}s · " +
                "Post-roll left: ${stats.hangoverRemainingMs / 1000}s · " +
                "Uptime: ${formatDuration(stats.uptimeMs)}",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(
            "Open segment: ${AudioCaptureEngine.formatBytes(stats.currentSegmentBytes)} " +
                "(${formatDuration(stats.currentSegmentDurationMs)}) · " +
                "Total written: ${AudioCaptureEngine.formatBytes(stats.totalBytesWritten)}",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        if (stats.partialTranscript.isNotBlank() || stats.liveTranscript.isNotBlank()) {
            Spacer(Modifier.height(12.dp))
            Text("Live transcript", style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.height(4.dp))
            if (stats.partialTranscript.isNotBlank()) {
                Text(
                    "… ${stats.partialTranscript}",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.secondary,
                )
            }
            if (stats.liveTranscript.isNotBlank()) {
                Text(
                    stats.liveTranscript,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 8,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }

        if (stats.events.isNotEmpty()) {
            Spacer(Modifier.height(12.dp))
            Text("Event log", style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.height(4.dp))
            stats.events.asReversed().take(12).forEach { event ->
                Text(
                    "• ${event.message}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}

private data class MeetingMeta(
    val topics: List<String> = emptyList(),
    val people: List<String> = emptyList(),
)

private fun parseMeetingMeta(json: String?): MeetingMeta {
    if (json.isNullOrBlank()) return MeetingMeta()
    return runCatching {
        val obj = JSONObject(json)
        fun arr(key: String): List<String> {
            val a = obj.optJSONArray(key) ?: return emptyList()
            return buildList {
                for (i in 0 until a.length()) {
                    val v = a.optString(i).trim()
                    if (v.isNotBlank()) add(v)
                }
            }
        }
        MeetingMeta(topics = arr("topics"), people = arr("people"))
    }.getOrDefault(MeetingMeta())
}

private fun phaseMessage(stats: CaptureStats): String = when (stats.phase) {
    CapturePhase.IDLE -> "Ready to listen around you."
    CapturePhase.LISTENING ->
        if (stats.openGateProgressMs > 0) {
            "Hearing speech… TEN VAD gate ${stats.openGateProgressMs}ms / ${stats.openGateRequiredMs}ms."
        } else {
            "Listening. TEN VAD opens after ~${stats.openGateRequiredMs}ms of mostly-voiced audio."
        }
    CapturePhase.PRE_ROLL_FLUSH -> "Speech gate passed — writing buffered audio into the segment."
    CapturePhase.LIVE_SPEECH -> "Live mic audio is being written to the open segment."
    CapturePhase.POST_ROLL ->
        "In post-roll window (${stats.hangoverRemainingMs / 1000}s left). Still writing; waiting for more speech or close."
    CapturePhase.PAUSED -> "Capture paused. Mic is idle."
}

private fun formatDuration(ms: Long): String {
    val totalSec = TimeUnit.MILLISECONDS.toSeconds(ms).coerceAtLeast(0)
    val h = totalSec / 3600
    val m = (totalSec % 3600) / 60
    val s = totalSec % 60
    return if (h > 0) "%d:%02d:%02d".format(h, m, s) else "%d:%02d".format(m, s)
}

private fun formatBytes(bytes: Long): String {
    if (bytes < 1024) return "$bytes B"
    val kb = bytes / 1024.0
    return if (kb < 1024) "%.1f KB".format(kb) else "%.1f MB".format(kb / 1024.0)
}
