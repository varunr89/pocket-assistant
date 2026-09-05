package com.varun.pocketassistant.ui;

import android.app.Application;
import android.media.MediaPlayer;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelKt;
import androidx.work.WorkInfo;
import com.google.android.gms.actions.SearchIntents;
import com.varun.pocketassistant.PocketAssistantApp;
import com.varun.pocketassistant.capture.CaptureStats;
import com.varun.pocketassistant.capture.RecordingHub;
import com.varun.pocketassistant.capture.RecordingService;
import com.varun.pocketassistant.data.MeetingEntity;
import com.varun.pocketassistant.data.MeetingRepository;
import com.varun.pocketassistant.data.OverlapPreview;
import com.varun.pocketassistant.data.SegmentEntity;
import com.varun.pocketassistant.data.SessionEntity;
import com.varun.pocketassistant.data.SessionRepository;
import com.varun.pocketassistant.meeting.GapClusterer;
import com.varun.pocketassistant.pipeline.PipelineTelemetry;
import com.varun.pocketassistant.pipeline.work.AsrWorker;
import com.varun.pocketassistant.pipeline.work.MeetingStageWorker;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import kotlin.Metadata;
import kotlin.Result;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.comparisons.ComparisonsKt;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.Boxing;
import kotlin.coroutines.jvm.internal.ContinuationImpl;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SpillingKt;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.functions.Function4;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.BuildersKt__Builders_commonKt;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.flow.Flow;
import kotlinx.coroutines.flow.FlowCollector;
import kotlinx.coroutines.flow.FlowKt;
import kotlinx.coroutines.flow.MutableStateFlow;
import kotlinx.coroutines.flow.SharingStarted;
import kotlinx.coroutines.flow.StateFlow;
import kotlinx.coroutines.flow.StateFlowKt;

/* JADX INFO: compiled from: HomeViewModel.kt */
/* JADX INFO: loaded from: classes4.dex */
@Metadata(d1 = {"\u0000Ä\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\t\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010#\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0006\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0014\n\u0002\u0010%\n\u0002\b\u0010\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\b\u0007\u0018\u00002\u00020\u0001B\u000f\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0004\b\u0004\u0010\u0005J\u0016\u0010J\u001a\u00020K2\u0006\u0010L\u001a\u000201H\u0082@¢\u0006\u0002\u0010MJ\u000e\u0010N\u001a\u00020K2\u0006\u0010O\u001a\u00020-J\u000e\u0010P\u001a\u00020K2\u0006\u0010Q\u001a\u00020RJ\u0006\u0010S\u001a\u00020KJ\u0006\u0010X\u001a\u00020KJ$\u0010Y\u001a\u00020K2\u0006\u0010Z\u001a\u00020;2\u0014\b\u0002\u0010[\u001a\u000e\u0012\u0004\u0012\u00020\u0017\u0012\u0004\u0012\u00020K0\\J\u000e\u0010]\u001a\u00020K2\u0006\u0010Z\u001a\u00020;J\u001f\u0010^\u001a\u00020K2\b\u0010_\u001a\u0004\u0018\u0001012\b\u0010`\u001a\u0004\u0018\u000101¢\u0006\u0002\u0010aJ\u0006\u0010b\u001a\u00020KJ\u001a\u0010c\u001a\u00020K2\u0012\u0010[\u001a\u000e\u0012\u0004\u0012\u00020\u0017\u0012\u0004\u0012\u00020K0\\J\u000e\u0010d\u001a\u00020K2\u0006\u0010e\u001a\u00020\u0017J\u0006\u0010f\u001a\u00020KJ\u0006\u0010g\u001a\u00020KJ\u0006\u0010h\u001a\u00020KJ\u0006\u0010i\u001a\u00020KJ\u000e\u0010j\u001a\u00020K2\u0006\u0010k\u001a\u00020\u0017J\u001a\u0010l\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00130\u000e0\r2\u0006\u0010k\u001a\u00020\u0017J\u001a\u0010m\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00130\u000e0\r2\u0006\u0010n\u001a\u00020\u0017J\u0016\u0010o\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u001b0\r2\u0006\u0010n\u001a\u00020\u0017J\u0016\u0010t\u001a\u00020K2\u0006\u0010_\u001a\u0002012\u0006\u0010`\u001a\u000201J\u0006\u0010u\u001a\u00020KJ*\u0010v\u001a\u00020K2\u0006\u0010_\u001a\u0002012\u0006\u0010`\u001a\u0002012\u0012\u0010[\u001a\u000e\u0012\u0004\u0012\u00020\u0017\u0012\u0004\u0012\u00020K0\\J\u001e\u0010w\u001a\u00020K2\u0006\u0010n\u001a\u00020\u00172\u0006\u0010_\u001a\u0002012\u0006\u0010`\u001a\u000201J\u000e\u0010x\u001a\u00020K2\u0006\u0010n\u001a\u00020\u0017J\u000e\u0010y\u001a\u00020K2\u0006\u0010n\u001a\u00020\u0017J\u0016\u0010z\u001a\u00020K2\u0006\u0010{\u001a\u00020\u00172\u0006\u0010n\u001a\u00020\u0017J\u000e\u0010|\u001a\u00020K2\u0006\u0010{\u001a\u00020\u0017J\u000e\u0010}\u001a\u00020K2\u0006\u0010{\u001a\u00020\u0017J\u000e\u0010~\u001a\u00020K2\u0006\u0010{\u001a\u00020\u0017J\u0017\u0010\u007f\u001a\u00020K2\u0006\u0010{\u001a\u00020\u00172\u0007\u0010\u0080\u0001\u001a\u00020RJ\u001d\u0010\u0081\u0001\u001a\u0010\u0012\u000b\u0012\t\u0012\u0005\u0012\u00030\u0083\u00010\u000e0\u0082\u00012\u0006\u0010{\u001a\u00020\u0017J\u001d\u0010\u0084\u0001\u001a\u0010\u0012\u000b\u0012\t\u0012\u0005\u0012\u00030\u0083\u00010\u000e0\u0082\u00012\u0006\u0010n\u001a\u00020\u0017J\u0010\u0010\u0085\u0001\u001a\u00020K2\u0007\u0010\u0086\u0001\u001a\u00020\u0013J\u0007\u0010\u0087\u0001\u001a\u00020KJ\t\u0010\u0088\u0001\u001a\u00020KH\u0014R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082\u0004¢\u0006\u0002\n\u0000R\u001d\u0010\f\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000f0\u000e0\r¢\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011R\u001d\u0010\u0012\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00130\u000e0\r¢\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0011R\u0014\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\u00170\u0016X\u0082\u0004¢\u0006\u0002\n\u0000R\u0017\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\u00170\r¢\u0006\b\n\u0000\u001a\u0004\b\u0019\u0010\u0011R#\u0010\u001a\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u001b0\u000e0\r¢\u0006\u000e\n\u0000\u0012\u0004\b\u001c\u0010\u001d\u001a\u0004\b\u001e\u0010\u0011R\u001d\u0010\u001f\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u001b0\u000e0\r¢\u0006\b\n\u0000\u001a\u0004\b \u0010\u0011R\u0017\u0010!\u001a\b\u0012\u0004\u0012\u00020\"0\r¢\u0006\b\n\u0000\u001a\u0004\b#\u0010\u0011R\u0014\u0010$\u001a\b\u0012\u0004\u0012\u00020%0\u0016X\u0082\u0004¢\u0006\u0002\n\u0000R\u0017\u0010&\u001a\b\u0012\u0004\u0012\u00020%0\r¢\u0006\b\n\u0000\u001a\u0004\b'\u0010\u0011R\u0016\u0010(\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010)0\u0016X\u0082\u0004¢\u0006\u0002\n\u0000R\u0019\u0010*\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010)0\r¢\u0006\b\n\u0000\u001a\u0004\b+\u0010\u0011R\u0014\u0010,\u001a\b\u0012\u0004\u0012\u00020-0\u0016X\u0082\u0004¢\u0006\u0002\n\u0000R\u0017\u0010.\u001a\b\u0012\u0004\u0012\u00020-0\r¢\u0006\b\n\u0000\u001a\u0004\b/\u0010\u0011R\u0014\u00100\u001a\b\u0012\u0004\u0012\u0002010\u0016X\u0082\u0004¢\u0006\u0002\n\u0000R\u0017\u00102\u001a\b\u0012\u0004\u0012\u0002010\r¢\u0006\b\n\u0000\u001a\u0004\b3\u0010\u0011R\u001a\u00104\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00130\u000e0\u0016X\u0082\u0004¢\u0006\u0002\n\u0000R\u001d\u00105\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00130\u000e0\r¢\u0006\b\n\u0000\u001a\u0004\b6\u0010\u0011R\u001a\u00107\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u001b0\u000e0\u0016X\u0082\u0004¢\u0006\u0002\n\u0000R\u001d\u00108\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u001b0\u000e0\r¢\u0006\b\n\u0000\u001a\u0004\b9\u0010\u0011R\u001a\u0010:\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020;0\u000e0\u0016X\u0082\u0004¢\u0006\u0002\n\u0000R\u001d\u0010<\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020;0\u000e0\r¢\u0006\b\n\u0000\u001a\u0004\b=\u0010\u0011R\u0014\u0010>\u001a\b\u0012\u0004\u0012\u00020\u00170?X\u0082\u0004¢\u0006\u0002\n\u0000R\u0016\u0010@\u001a\n\u0012\u0006\u0012\u0004\u0018\u0001010\u0016X\u0082\u0004¢\u0006\u0002\n\u0000R\u0019\u0010A\u001a\n\u0012\u0006\u0012\u0004\u0018\u0001010\r¢\u0006\b\n\u0000\u001a\u0004\bB\u0010\u0011R\u0016\u0010C\u001a\n\u0012\u0006\u0012\u0004\u0018\u0001010\u0016X\u0082\u0004¢\u0006\u0002\n\u0000R\u0019\u0010D\u001a\n\u0012\u0006\u0012\u0004\u0018\u0001010\r¢\u0006\b\n\u0000\u001a\u0004\bE\u0010\u0011R\u0010\u0010F\u001a\u0004\u0018\u00010GX\u0082\u000e¢\u0006\u0002\n\u0000R\u001d\u0010H\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00130\u000e0\r¢\u0006\b\n\u0000\u001a\u0004\bI\u0010\u0011R\u0014\u0010T\u001a\b\u0012\u0004\u0012\u00020U0\u0016X\u0082\u0004¢\u0006\u0002\n\u0000R\u0017\u0010V\u001a\b\u0012\u0004\u0012\u00020U0\r¢\u0006\b\n\u0000\u001a\u0004\bW\u0010\u0011R&\u0010p\u001a\u001a\u0012\u0004\u0012\u00020\u0017\u0012\u0010\u0012\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00130\u000e0\r0qX\u0082\u0004¢\u0006\u0002\n\u0000R&\u0010r\u001a\u001a\u0012\u0004\u0012\u00020\u0017\u0012\u0010\u0012\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00130\u000e0\r0qX\u0082\u0004¢\u0006\u0002\n\u0000R\"\u0010s\u001a\u0016\u0012\u0004\u0012\u00020\u0017\u0012\f\u0012\n\u0012\u0006\u0012\u0004\u0018\u00010\u001b0\r0qX\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u0089\u0001"}, d2 = {"Lcom/varun/pocketassistant/ui/HomeViewModel;", "Landroidx/lifecycle/AndroidViewModel;", "application", "Landroid/app/Application;", "<init>", "(Landroid/app/Application;)V", "app", "Lcom/varun/pocketassistant/PocketAssistantApp;", "repo", "Lcom/varun/pocketassistant/data/SessionRepository;", "meetingRepo", "Lcom/varun/pocketassistant/data/MeetingRepository;", "sessions", "Lkotlinx/coroutines/flow/StateFlow;", "", "Lcom/varun/pocketassistant/data/SessionEntity;", "getSessions", "()Lkotlinx/coroutines/flow/StateFlow;", "recordings", "Lcom/varun/pocketassistant/data/SegmentEntity;", "getRecordings", "_meetingSearch", "Lkotlinx/coroutines/flow/MutableStateFlow;", "", "meetingSearch", "getMeetingSearch", "meetings", "Lcom/varun/pocketassistant/data/MeetingEntity;", "getMeetings$annotations", "()V", "getMeetings", "allMeetings", "getAllMeetings", "captureStats", "Lcom/varun/pocketassistant/capture/CaptureStats;", "getCaptureStats", "_playback", "Lcom/varun/pocketassistant/ui/PlaybackState;", "playback", "getPlayback", "_createPreview", "Lcom/varun/pocketassistant/data/OverlapPreview;", "createPreview", "getCreatePreview", "_browseMode", "Lcom/varun/pocketassistant/ui/HomeBrowseMode;", "browseMode", "getBrowseMode", "_dayStartMs", "", "dayStartMs", "getDayStartMs", "_daySegments", "daySegments", "getDaySegments", "_dayMeetings", "dayMeetings", "getDayMeetings", "_proposals", "Lcom/varun/pocketassistant/meeting/GapClusterer$Proposal;", "proposals", "getProposals", "dismissedProposalIds", "", "_selectionStartMs", "selectionStartMs", "getSelectionStartMs", "_selectionEndMs", "selectionEndMs", "getSelectionEndMs", "mediaPlayer", "Landroid/media/MediaPlayer;", "allRecordings", "getAllRecordings", "refreshDay", "", "dayStart", "(JLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "setBrowseMode", "mode", "shiftDay", "deltaDays", "", "goToToday", "_detectingMeetings", "", "detectingMeetings", "getDetectingMeetings", "suggestMeetingsForDay", "acceptProposal", "proposal", "onCreated", "Lkotlin/Function1;", "dismissProposal", "setTimelineSelection", "startMs", "endMs", "(Ljava/lang/Long;Ljava/lang/Long;)V", "clearSelection", "createMeetingFromSelection", "setMeetingSearch", SearchIntents.EXTRA_QUERY, "startRecording", "pauseRecording", "resumeRecording", "stopRecording", "deleteSession", "sessionId", "observeSegments", "observeMeetingRecordings", MeetingStageWorker.KEY_MEETING_ID, "observeMeeting", "segmentFlows", "", "meetingRecordingFlows", "meetingFlows", "previewMeetingRange", "clearCreatePreview", "createMeeting", "updateMeetingRange", "deleteMeeting", "retryMeetingCleanup", "assignRecordingToMeeting", AsrWorker.KEY_SEGMENT_ID, "unassignRecording", "deleteRecording", "retranscribeRecording", "confirmYouSpeaker", "youSpeakerId", "observeAsrAttempts", "Landroidx/lifecycle/LiveData;", "Landroidx/work/WorkInfo;", "observeMeetingWork", "togglePlayback", "segment", "stopPlayback", "onCleared", "app_debug"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class HomeViewModel extends AndroidViewModel {
    public static final int $stable = 8;
    private final MutableStateFlow<HomeBrowseMode> _browseMode;
    private final MutableStateFlow<OverlapPreview> _createPreview;
    private final MutableStateFlow<List<MeetingEntity>> _dayMeetings;
    private final MutableStateFlow<List<SegmentEntity>> _daySegments;
    private final MutableStateFlow<Long> _dayStartMs;
    private final MutableStateFlow<Boolean> _detectingMeetings;
    private final MutableStateFlow<String> _meetingSearch;
    private final MutableStateFlow<PlaybackState> _playback;
    private final MutableStateFlow<List<GapClusterer.Proposal>> _proposals;
    private final MutableStateFlow<Long> _selectionEndMs;
    private final MutableStateFlow<Long> _selectionStartMs;
    private final StateFlow<List<MeetingEntity>> allMeetings;
    private final StateFlow<List<SegmentEntity>> allRecordings;
    private final PocketAssistantApp app;
    private final StateFlow<HomeBrowseMode> browseMode;
    private final StateFlow<CaptureStats> captureStats;
    private final StateFlow<OverlapPreview> createPreview;
    private final StateFlow<List<MeetingEntity>> dayMeetings;
    private final StateFlow<List<SegmentEntity>> daySegments;
    private final StateFlow<Long> dayStartMs;
    private final StateFlow<Boolean> detectingMeetings;
    private final Set<String> dismissedProposalIds;
    private MediaPlayer mediaPlayer;
    private final Map<String, StateFlow<MeetingEntity>> meetingFlows;
    private final Map<String, StateFlow<List<SegmentEntity>>> meetingRecordingFlows;
    private final MeetingRepository meetingRepo;
    private final StateFlow<String> meetingSearch;
    private final StateFlow<List<MeetingEntity>> meetings;
    private final StateFlow<PlaybackState> playback;
    private final StateFlow<List<GapClusterer.Proposal>> proposals;
    private final StateFlow<List<SegmentEntity>> recordings;
    private final SessionRepository repo;
    private final Map<String, StateFlow<List<SegmentEntity>>> segmentFlows;
    private final StateFlow<Long> selectionEndMs;
    private final StateFlow<Long> selectionStartMs;
    private final StateFlow<List<SessionEntity>> sessions;

    /* JADX INFO: renamed from: com.varun.pocketassistant.ui.HomeViewModel$refreshDay$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: HomeViewModel.kt */
    @Metadata(k = 3, mv = {2, 2, 0}, xi = 48)
    @DebugMetadata(c = "com.varun.pocketassistant.ui.HomeViewModel", f = "HomeViewModel.kt", i = {0, 0, 1, 1}, l = {107, 109}, m = "refreshDay", n = {"dayStart", "dayEnd", "dayStart", "dayEnd"}, s = {"J$0", "J$1", "J$0", "J$1"})
    static final class C06961 extends ContinuationImpl {
        long J$0;
        long J$1;
        Object L$0;
        int label;
        /* synthetic */ Object result;

        C06961(Continuation<? super C06961> continuation) {
            super(continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            this.result = obj;
            this.label |= Integer.MIN_VALUE;
            return HomeViewModel.this.refreshDay(0L, this);
        }
    }

    public static /* synthetic */ void getMeetings$annotations() {
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public HomeViewModel(Application application) {
        super(application);
        Intrinsics.checkNotNullParameter(application, "application");
        this.app = (PocketAssistantApp) application;
        this.repo = this.app.getContainer().getSessionRepository();
        this.meetingRepo = this.app.getContainer().getMeetingRepository();
        this.sessions = FlowKt.stateIn(this.repo.observeSessions(), ViewModelKt.getViewModelScope(this), SharingStarted.Companion.WhileSubscribed$default(SharingStarted.INSTANCE, 5000L, 0L, 2, null), CollectionsKt.emptyList());
        this.recordings = FlowKt.stateIn(this.repo.observeUncategorizedRecordings(), ViewModelKt.getViewModelScope(this), SharingStarted.Companion.WhileSubscribed$default(SharingStarted.INSTANCE, 5000L, 0L, 2, null), CollectionsKt.emptyList());
        this._meetingSearch = StateFlowKt.MutableStateFlow("");
        this.meetingSearch = FlowKt.asStateFlow(this._meetingSearch);
        this.meetings = FlowKt.stateIn(FlowKt.transformLatest(this._meetingSearch, new HomeViewModel$special$$inlined$flatMapLatest$1(null, this)), ViewModelKt.getViewModelScope(this), SharingStarted.Companion.WhileSubscribed$default(SharingStarted.INSTANCE, 5000L, 0L, 2, null), CollectionsKt.emptyList());
        this.allMeetings = FlowKt.stateIn(this.meetingRepo.observeMeetings(), ViewModelKt.getViewModelScope(this), SharingStarted.Companion.WhileSubscribed$default(SharingStarted.INSTANCE, 5000L, 0L, 2, null), CollectionsKt.emptyList());
        this.captureStats = RecordingHub.INSTANCE.getStats();
        this._playback = StateFlowKt.MutableStateFlow(new PlaybackState(null, false, 3, null));
        this.playback = FlowKt.asStateFlow(this._playback);
        this._createPreview = StateFlowKt.MutableStateFlow(null);
        this.createPreview = FlowKt.asStateFlow(this._createPreview);
        this._browseMode = StateFlowKt.MutableStateFlow(HomeBrowseMode.DAY);
        this.browseMode = FlowKt.asStateFlow(this._browseMode);
        this._dayStartMs = StateFlowKt.MutableStateFlow(Long.valueOf(DayTimelineKt.startOfDayMs$default(0L, 1, null)));
        this.dayStartMs = FlowKt.asStateFlow(this._dayStartMs);
        this._daySegments = StateFlowKt.MutableStateFlow(CollectionsKt.emptyList());
        this.daySegments = FlowKt.asStateFlow(this._daySegments);
        this._dayMeetings = StateFlowKt.MutableStateFlow(CollectionsKt.emptyList());
        this.dayMeetings = FlowKt.asStateFlow(this._dayMeetings);
        this._proposals = StateFlowKt.MutableStateFlow(CollectionsKt.emptyList());
        this.proposals = FlowKt.asStateFlow(this._proposals);
        this.dismissedProposalIds = new LinkedHashSet();
        this._selectionStartMs = StateFlowKt.MutableStateFlow(null);
        this.selectionStartMs = FlowKt.asStateFlow(this._selectionStartMs);
        this._selectionEndMs = StateFlowKt.MutableStateFlow(null);
        this.selectionEndMs = FlowKt.asStateFlow(this._selectionEndMs);
        this.allRecordings = FlowKt.stateIn(this.repo.observeAllRecordings(), ViewModelKt.getViewModelScope(this), SharingStarted.Companion.WhileSubscribed$default(SharingStarted.INSTANCE, 5000L, 0L, 2, null), CollectionsKt.emptyList());
        BuildersKt__Builders_commonKt.launch$default(ViewModelKt.getViewModelScope(this), null, null, new AnonymousClass1(null), 3, null);
        this._detectingMeetings = StateFlowKt.MutableStateFlow(false);
        this.detectingMeetings = FlowKt.asStateFlow(this._detectingMeetings);
        this.segmentFlows = new LinkedHashMap();
        this.meetingRecordingFlows = new LinkedHashMap();
        this.meetingFlows = new LinkedHashMap();
    }

    public final StateFlow<List<SessionEntity>> getSessions() {
        return this.sessions;
    }

    public final StateFlow<List<SegmentEntity>> getRecordings() {
        return this.recordings;
    }

    public final StateFlow<String> getMeetingSearch() {
        return this.meetingSearch;
    }

    public final StateFlow<List<MeetingEntity>> getMeetings() {
        return this.meetings;
    }

    public final StateFlow<List<MeetingEntity>> getAllMeetings() {
        return this.allMeetings;
    }

    public final StateFlow<CaptureStats> getCaptureStats() {
        return this.captureStats;
    }

    public final StateFlow<PlaybackState> getPlayback() {
        return this.playback;
    }

    public final StateFlow<OverlapPreview> getCreatePreview() {
        return this.createPreview;
    }

    public final StateFlow<HomeBrowseMode> getBrowseMode() {
        return this.browseMode;
    }

    public final StateFlow<Long> getDayStartMs() {
        return this.dayStartMs;
    }

    public final StateFlow<List<SegmentEntity>> getDaySegments() {
        return this.daySegments;
    }

    public final StateFlow<List<MeetingEntity>> getDayMeetings() {
        return this.dayMeetings;
    }

    public final StateFlow<List<GapClusterer.Proposal>> getProposals() {
        return this.proposals;
    }

    public final StateFlow<Long> getSelectionStartMs() {
        return this.selectionStartMs;
    }

    public final StateFlow<Long> getSelectionEndMs() {
        return this.selectionEndMs;
    }

    public final StateFlow<List<SegmentEntity>> getAllRecordings() {
        return this.allRecordings;
    }

    /* JADX INFO: renamed from: com.varun.pocketassistant.ui.HomeViewModel$1, reason: invalid class name */
    /* JADX INFO: compiled from: HomeViewModel.kt */
    @Metadata(d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H\n"}, d2 = {"<anonymous>", "", "Lkotlinx/coroutines/CoroutineScope;"}, k = 3, mv = {2, 2, 0}, xi = 48)
    @DebugMetadata(c = "com.varun.pocketassistant.ui.HomeViewModel$1", f = "HomeViewModel.kt", i = {}, l = {101}, m = "invokeSuspend", n = {}, s = {})
    static final class AnonymousClass1 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
        int label;

        AnonymousClass1(Continuation<? super AnonymousClass1> continuation) {
            super(2, continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
            return HomeViewModel.this.new AnonymousClass1(continuation);
        }

        @Override // kotlin.jvm.functions.Function2
        public final Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
            return ((AnonymousClass1) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
        }

        /* JADX INFO: renamed from: com.varun.pocketassistant.ui.HomeViewModel$1$1, reason: invalid class name and collision with other inner class name */
        /* JADX INFO: compiled from: HomeViewModel.kt */
        @Metadata(d1 = {"\u0000\u0016\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00012\f\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u00042\f\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00060\u0004H\n"}, d2 = {"<anonymous>", "", "day", "<unused var>", "", "Lcom/varun/pocketassistant/data/SegmentEntity;", "Lcom/varun/pocketassistant/data/MeetingEntity;"}, k = 3, mv = {2, 2, 0}, xi = 48)
        @DebugMetadata(c = "com.varun.pocketassistant.ui.HomeViewModel$1$1", f = "HomeViewModel.kt", i = {}, l = {}, m = "invokeSuspend", n = {}, s = {})
        static final class C01591 extends SuspendLambda implements Function4<Long, List<? extends SegmentEntity>, List<? extends MeetingEntity>, Continuation<? super Long>, Object> {
            /* synthetic */ long J$0;
            int label;

            C01591(Continuation<? super C01591> continuation) {
                super(4, continuation);
            }

            public final Object invoke(long j, List<SegmentEntity> list, List<MeetingEntity> list2, Continuation<? super Long> continuation) {
                C01591 c01591 = new C01591(continuation);
                c01591.J$0 = j;
                return c01591.invokeSuspend(Unit.INSTANCE);
            }

            @Override // kotlin.jvm.functions.Function4
            public /* bridge */ /* synthetic */ Object invoke(Long l, List<? extends SegmentEntity> list, List<? extends MeetingEntity> list2, Continuation<? super Long> continuation) {
                return invoke(l.longValue(), (List<SegmentEntity>) list, (List<MeetingEntity>) list2, continuation);
            }

            @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
            public final Object invokeSuspend(Object $result) {
                long day = this.J$0;
                IntrinsicsKt.getCOROUTINE_SUSPENDED();
                switch (this.label) {
                    case 0:
                        ResultKt.throwOnFailure($result);
                        return Boxing.boxLong(day);
                    default:
                        throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
            }
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object $result) {
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    Flow flowCombine = FlowKt.combine(HomeViewModel.this._dayStartMs, HomeViewModel.this.getAllRecordings(), HomeViewModel.this.getAllMeetings(), new C01591(null));
                    final HomeViewModel homeViewModel = HomeViewModel.this;
                    this.label = 1;
                    if (flowCombine.collect(new FlowCollector() { // from class: com.varun.pocketassistant.ui.HomeViewModel.1.2
                        public final Object emit(long it, Continuation<? super Unit> continuation) {
                            Object objRefreshDay = homeViewModel.refreshDay(it, continuation);
                            return objRefreshDay == IntrinsicsKt.getCOROUTINE_SUSPENDED() ? objRefreshDay : Unit.INSTANCE;
                        }

                        @Override // kotlinx.coroutines.flow.FlowCollector
                        public /* bridge */ /* synthetic */ Object emit(Object value, Continuation $completion) {
                            return emit(((Number) value).longValue(), (Continuation<? super Unit>) $completion);
                        }
                    }, this) == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    break;
                case 1:
                    ResultKt.throwOnFailure($result);
                    break;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
            return Unit.INSTANCE;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code duplicated, block: B:20:0x009c A[RETURN] */
    /* JADX WARN: Code duplicated, block: B:21:0x009d  */
    /* JADX WARN: Code duplicated, block: B:25:0x00bf  */
    /* JADX WARN: Code duplicated, block: B:27:0x00d9  */
    /* JADX WARN: Code duplicated, block: B:29:0x00e4  */
    /* JADX WARN: Code duplicated, block: B:32:0x00f0  */
    /* JADX WARN: Code duplicated, block: B:35:0x00fa  */
    /* JADX WARN: Code duplicated, block: B:37:0x0116  */
    /* JADX WARN: Code duplicated, block: B:40:0x0121  */
    /* JADX WARN: Code duplicated, block: B:43:0x012b  */
    /* JADX WARN: Code duplicated, block: B:45:0x0141  */
    /* JADX WARN: Code duplicated, block: B:48:0x0149  */
    /* JADX WARN: Code duplicated, block: B:51:0x014e A[LOOP:2: B:41:0x0125->B:51:0x014e, LOOP_END] */
    /* JADX WARN: Code duplicated, block: B:56:0x0157 A[LOOP:1: B:33:0x00f4->B:56:0x0157, LOOP_END] */
    /* JADX WARN: Code duplicated, block: B:59:0x0163  */
    /* JADX WARN: Code duplicated, block: B:60:0x0166  */
    /* JADX WARN: Code duplicated, block: B:63:0x016b  */
    /* JADX WARN: Code duplicated, block: B:69:0x016e A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:70:0x015e A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:71:0x0155 A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:72:0x014c A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:73:0x0151 A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:7:0x001a  */
    public final Object refreshDay(long dayStart, Continuation<? super Unit> continuation) {
        C06961 c06961;
        Object segmentsOverlapping;
        MutableStateFlow<List<SegmentEntity>> mutableStateFlow;
        long dayStart2;
        long dayEnd;
        MutableStateFlow mutableStateFlow2;
        Object meetingsOverlapping;
        long dayEnd2;
        Iterable value;
        Collection arrayList;
        GapClusterer.Proposal proposal;
        boolean z;
        Iterable segmentIds;
        Iterator it;
        boolean z2;
        Iterable iterable;
        String str;
        Iterator it2;
        Iterable value2;
        Iterator it3;
        boolean z3;
        SegmentEntity segmentEntity;
        Iterator it4;
        boolean z4;
        HomeViewModel homeViewModel = this;
        if (continuation instanceof C06961) {
            c06961 = (C06961) continuation;
            if ((c06961.label & Integer.MIN_VALUE) != 0) {
                c06961.label -= Integer.MIN_VALUE;
            } else {
                c06961 = homeViewModel.new C06961(continuation);
            }
        } else {
            c06961 = homeViewModel.new C06961(continuation);
        }
        C06961 c06962 = c06961;
        Object $result = c06962.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (c06962.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                long dayEnd3 = TimeUnit.DAYS.toMillis(1L) + dayStart;
                MutableStateFlow<List<SegmentEntity>> mutableStateFlow3 = homeViewModel._daySegments;
                MeetingRepository meetingRepository = homeViewModel.meetingRepo;
                c06962.L$0 = mutableStateFlow3;
                c06962.J$0 = dayStart;
                c06962.J$1 = dayEnd3;
                c06962.label = 1;
                segmentsOverlapping = meetingRepository.getSegmentsOverlapping(dayStart, dayEnd3, c06962);
                if (segmentsOverlapping == coroutine_suspended) {
                    return coroutine_suspended;
                }
                mutableStateFlow = mutableStateFlow3;
                dayStart2 = dayStart;
                dayEnd = dayEnd3;
                mutableStateFlow.setValue(CollectionsKt.sortedWith((Iterable) segmentsOverlapping, new Comparator() { // from class: com.varun.pocketassistant.ui.HomeViewModel$refreshDay$$inlined$sortedBy$1
                    /* JADX WARN: Multi-variable type inference failed */
                    @Override // java.util.Comparator
                    public final int compare(T t, T t2) {
                        return ComparisonsKt.compareValues(Long.valueOf(((SegmentEntity) t).getStartedAtMs()), Long.valueOf(((SegmentEntity) t2).getStartedAtMs()));
                    }
                }));
                mutableStateFlow2 = homeViewModel._dayMeetings;
                MeetingRepository meetingRepository2 = homeViewModel.meetingRepo;
                c06962.L$0 = mutableStateFlow2;
                c06962.J$0 = dayStart2;
                c06962.J$1 = dayEnd;
                c06962.label = 2;
                meetingsOverlapping = meetingRepository2.getMeetingsOverlapping(dayStart2, dayEnd, c06962);
                if (meetingsOverlapping == coroutine_suspended) {
                    return coroutine_suspended;
                }
                dayEnd2 = dayEnd;
                mutableStateFlow2.setValue(meetingsOverlapping);
                MutableStateFlow<List<GapClusterer.Proposal>> mutableStateFlow4 = homeViewModel._proposals;
                value = homeViewModel._proposals.getValue();
                arrayList = new ArrayList();
                for (Object obj : value) {
                    proposal = (GapClusterer.Proposal) obj;
                    Iterable iterable2 = value;
                    long dayEnd4 = dayEnd2;
                    if (homeViewModel.dismissedProposalIds.contains(proposal.getId())) {
                        z = false;
                    } else {
                        segmentIds = proposal.getSegmentIds();
                        if ((segmentIds instanceof Collection) || !((Collection) segmentIds).isEmpty()) {
                            it = segmentIds.iterator();
                            while (true) {
                                if (it.hasNext()) {
                                    iterable = segmentIds;
                                    str = (String) it.next();
                                    it2 = it;
                                    value2 = homeViewModel._daySegments.getValue();
                                    if ((value2 instanceof Collection) || !((Collection) value2).isEmpty()) {
                                        it3 = value2.iterator();
                                        while (true) {
                                            if (it3.hasNext()) {
                                                segmentEntity = (SegmentEntity) it3.next();
                                                it4 = it3;
                                                if (Intrinsics.areEqual(segmentEntity.getId(), str) || segmentEntity.getMeetingId() != null) {
                                                    z4 = false;
                                                } else {
                                                    z4 = true;
                                                }
                                                if (z4) {
                                                    z3 = true;
                                                } else {
                                                    it3 = it4;
                                                }
                                            } else {
                                                z3 = false;
                                            }
                                        }
                                    } else {
                                        z3 = false;
                                    }
                                    if (z3) {
                                        z2 = true;
                                    } else {
                                        homeViewModel = this;
                                        segmentIds = iterable;
                                        it = it2;
                                    }
                                } else {
                                    z2 = false;
                                }
                            }
                        } else {
                            z2 = false;
                        }
                        if (z2) {
                            z = true;
                        } else {
                            z = false;
                        }
                    }
                    if (z) {
                        arrayList.add(obj);
                    }
                    homeViewModel = this;
                    value = iterable2;
                    dayEnd2 = dayEnd4;
                }
                mutableStateFlow4.setValue((List) arrayList);
                return Unit.INSTANCE;
            case 1:
                long dayEnd5 = c06962.J$1;
                long dayStart3 = c06962.J$0;
                mutableStateFlow = (MutableStateFlow) c06962.L$0;
                ResultKt.throwOnFailure($result);
                dayStart2 = dayStart3;
                segmentsOverlapping = $result;
                dayEnd = dayEnd5;
                mutableStateFlow.setValue(CollectionsKt.sortedWith((Iterable) segmentsOverlapping, new Comparator() { // from class: com.varun.pocketassistant.ui.HomeViewModel$refreshDay$$inlined$sortedBy$1
                    /* JADX WARN: Multi-variable type inference failed */
                    @Override // java.util.Comparator
                    public final int compare(T t, T t2) {
                        return ComparisonsKt.compareValues(Long.valueOf(((SegmentEntity) t).getStartedAtMs()), Long.valueOf(((SegmentEntity) t2).getStartedAtMs()));
                    }
                }));
                mutableStateFlow2 = homeViewModel._dayMeetings;
                MeetingRepository meetingRepository3 = homeViewModel.meetingRepo;
                c06962.L$0 = mutableStateFlow2;
                c06962.J$0 = dayStart2;
                c06962.J$1 = dayEnd;
                c06962.label = 2;
                meetingsOverlapping = meetingRepository3.getMeetingsOverlapping(dayStart2, dayEnd, c06962);
                if (meetingsOverlapping == coroutine_suspended) {
                    return coroutine_suspended;
                }
                dayEnd2 = dayEnd;
                mutableStateFlow2.setValue(meetingsOverlapping);
                MutableStateFlow<List<GapClusterer.Proposal>> mutableStateFlow5 = homeViewModel._proposals;
                value = homeViewModel._proposals.getValue();
                arrayList = new ArrayList();
                while (r15.hasNext()) {
                    proposal = (GapClusterer.Proposal) obj;
                    Iterable iterable3 = value;
                    long dayEnd6 = dayEnd2;
                    if (homeViewModel.dismissedProposalIds.contains(proposal.getId())) {
                        segmentIds = proposal.getSegmentIds();
                        if (segmentIds instanceof Collection) {
                            it = segmentIds.iterator();
                            while (true) {
                                if (it.hasNext()) {
                                    iterable = segmentIds;
                                    str = (String) it.next();
                                    it2 = it;
                                    value2 = homeViewModel._daySegments.getValue();
                                    if (value2 instanceof Collection) {
                                        it3 = value2.iterator();
                                        while (true) {
                                            if (it3.hasNext()) {
                                                segmentEntity = (SegmentEntity) it3.next();
                                                it4 = it3;
                                                if (Intrinsics.areEqual(segmentEntity.getId(), str)) {
                                                    z4 = false;
                                                } else {
                                                    z4 = false;
                                                }
                                                if (z4) {
                                                    z3 = true;
                                                } else {
                                                    it3 = it4;
                                                }
                                            } else {
                                                z3 = false;
                                            }
                                        }
                                    } else {
                                        it3 = value2.iterator();
                                        while (true) {
                                            if (it3.hasNext()) {
                                                segmentEntity = (SegmentEntity) it3.next();
                                                it4 = it3;
                                                if (Intrinsics.areEqual(segmentEntity.getId(), str)) {
                                                    z4 = false;
                                                } else {
                                                    z4 = false;
                                                }
                                                if (z4) {
                                                    z3 = true;
                                                } else {
                                                    it3 = it4;
                                                }
                                            } else {
                                                z3 = false;
                                            }
                                        }
                                    }
                                    if (z3) {
                                        z2 = true;
                                    } else {
                                        homeViewModel = this;
                                        segmentIds = iterable;
                                        it = it2;
                                    }
                                } else {
                                    z2 = false;
                                }
                            }
                        } else {
                            it = segmentIds.iterator();
                            while (true) {
                                if (it.hasNext()) {
                                    iterable = segmentIds;
                                    str = (String) it.next();
                                    it2 = it;
                                    value2 = homeViewModel._daySegments.getValue();
                                    if (value2 instanceof Collection) {
                                        it3 = value2.iterator();
                                        while (true) {
                                            if (it3.hasNext()) {
                                                segmentEntity = (SegmentEntity) it3.next();
                                                it4 = it3;
                                                if (Intrinsics.areEqual(segmentEntity.getId(), str)) {
                                                    z4 = false;
                                                } else {
                                                    z4 = false;
                                                }
                                                if (z4) {
                                                    z3 = true;
                                                } else {
                                                    it3 = it4;
                                                }
                                            } else {
                                                z3 = false;
                                            }
                                        }
                                    } else {
                                        it3 = value2.iterator();
                                        while (true) {
                                            if (it3.hasNext()) {
                                                segmentEntity = (SegmentEntity) it3.next();
                                                it4 = it3;
                                                if (Intrinsics.areEqual(segmentEntity.getId(), str)) {
                                                    z4 = false;
                                                } else {
                                                    z4 = false;
                                                }
                                                if (z4) {
                                                    z3 = true;
                                                } else {
                                                    it3 = it4;
                                                }
                                            } else {
                                                z3 = false;
                                            }
                                        }
                                    }
                                    if (z3) {
                                        z2 = true;
                                    } else {
                                        homeViewModel = this;
                                        segmentIds = iterable;
                                        it = it2;
                                    }
                                } else {
                                    z2 = false;
                                }
                            }
                        }
                        if (z2) {
                            z = true;
                        } else {
                            z = false;
                        }
                    } else {
                        z = false;
                    }
                    if (z) {
                        arrayList.add(obj);
                    }
                    homeViewModel = this;
                    value = iterable3;
                    dayEnd2 = dayEnd6;
                }
                mutableStateFlow5.setValue((List) arrayList);
                return Unit.INSTANCE;
            case 2:
                dayEnd2 = c06962.J$1;
                long j = c06962.J$0;
                mutableStateFlow2 = (MutableStateFlow) c06962.L$0;
                ResultKt.throwOnFailure($result);
                meetingsOverlapping = $result;
                mutableStateFlow2.setValue(meetingsOverlapping);
                MutableStateFlow<List<GapClusterer.Proposal>> mutableStateFlow6 = homeViewModel._proposals;
                value = homeViewModel._proposals.getValue();
                arrayList = new ArrayList();
                while (r15.hasNext()) {
                    proposal = (GapClusterer.Proposal) obj;
                    Iterable iterable4 = value;
                    long dayEnd7 = dayEnd2;
                    if (homeViewModel.dismissedProposalIds.contains(proposal.getId())) {
                        segmentIds = proposal.getSegmentIds();
                        if (segmentIds instanceof Collection) {
                            it = segmentIds.iterator();
                            while (true) {
                                if (it.hasNext()) {
                                    iterable = segmentIds;
                                    str = (String) it.next();
                                    it2 = it;
                                    value2 = homeViewModel._daySegments.getValue();
                                    if (value2 instanceof Collection) {
                                        it3 = value2.iterator();
                                        while (true) {
                                            if (it3.hasNext()) {
                                                segmentEntity = (SegmentEntity) it3.next();
                                                it4 = it3;
                                                if (Intrinsics.areEqual(segmentEntity.getId(), str)) {
                                                    z4 = false;
                                                } else {
                                                    z4 = false;
                                                }
                                                if (z4) {
                                                    z3 = true;
                                                } else {
                                                    it3 = it4;
                                                }
                                            } else {
                                                z3 = false;
                                            }
                                        }
                                    } else {
                                        it3 = value2.iterator();
                                        while (true) {
                                            if (it3.hasNext()) {
                                                segmentEntity = (SegmentEntity) it3.next();
                                                it4 = it3;
                                                if (Intrinsics.areEqual(segmentEntity.getId(), str)) {
                                                    z4 = false;
                                                } else {
                                                    z4 = false;
                                                }
                                                if (z4) {
                                                    z3 = true;
                                                } else {
                                                    it3 = it4;
                                                }
                                            } else {
                                                z3 = false;
                                            }
                                        }
                                    }
                                    if (z3) {
                                        z2 = true;
                                    } else {
                                        homeViewModel = this;
                                        segmentIds = iterable;
                                        it = it2;
                                    }
                                } else {
                                    z2 = false;
                                }
                            }
                        } else {
                            it = segmentIds.iterator();
                            while (true) {
                                if (it.hasNext()) {
                                    iterable = segmentIds;
                                    str = (String) it.next();
                                    it2 = it;
                                    value2 = homeViewModel._daySegments.getValue();
                                    if (value2 instanceof Collection) {
                                        it3 = value2.iterator();
                                        while (true) {
                                            if (it3.hasNext()) {
                                                segmentEntity = (SegmentEntity) it3.next();
                                                it4 = it3;
                                                if (Intrinsics.areEqual(segmentEntity.getId(), str)) {
                                                    z4 = false;
                                                } else {
                                                    z4 = false;
                                                }
                                                if (z4) {
                                                    z3 = true;
                                                } else {
                                                    it3 = it4;
                                                }
                                            } else {
                                                z3 = false;
                                            }
                                        }
                                    } else {
                                        it3 = value2.iterator();
                                        while (true) {
                                            if (it3.hasNext()) {
                                                segmentEntity = (SegmentEntity) it3.next();
                                                it4 = it3;
                                                if (Intrinsics.areEqual(segmentEntity.getId(), str)) {
                                                    z4 = false;
                                                } else {
                                                    z4 = false;
                                                }
                                                if (z4) {
                                                    z3 = true;
                                                } else {
                                                    it3 = it4;
                                                }
                                            } else {
                                                z3 = false;
                                            }
                                        }
                                    }
                                    if (z3) {
                                        z2 = true;
                                    } else {
                                        homeViewModel = this;
                                        segmentIds = iterable;
                                        it = it2;
                                    }
                                } else {
                                    z2 = false;
                                }
                            }
                        }
                        if (z2) {
                            z = true;
                        } else {
                            z = false;
                        }
                    } else {
                        z = false;
                    }
                    if (z) {
                        arrayList.add(obj);
                    }
                    homeViewModel = this;
                    value = iterable4;
                    dayEnd2 = dayEnd7;
                }
                mutableStateFlow6.setValue((List) arrayList);
                return Unit.INSTANCE;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
    }

    public final void setBrowseMode(HomeBrowseMode mode) {
        Intrinsics.checkNotNullParameter(mode, "mode");
        this._browseMode.setValue(mode);
    }

    public final void shiftDay(int deltaDays) {
        MutableStateFlow<Long> mutableStateFlow = this._dayStartMs;
        mutableStateFlow.setValue(Long.valueOf(mutableStateFlow.getValue().longValue() + TimeUnit.DAYS.toMillis(deltaDays)));
        clearSelection();
        this._proposals.setValue(CollectionsKt.emptyList());
    }

    public final void goToToday() {
        this._dayStartMs.setValue(Long.valueOf(DayTimelineKt.startOfDayMs$default(0L, 1, null)));
        clearSelection();
        this._proposals.setValue(CollectionsKt.emptyList());
    }

    public final StateFlow<Boolean> getDetectingMeetings() {
        return this.detectingMeetings;
    }

    /* JADX INFO: renamed from: com.varun.pocketassistant.ui.HomeViewModel$suggestMeetingsForDay$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: HomeViewModel.kt */
    @Metadata(d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H\n"}, d2 = {"<anonymous>", "", "Lkotlinx/coroutines/CoroutineScope;"}, k = 3, mv = {2, 2, 0}, xi = 48)
    @DebugMetadata(c = "com.varun.pocketassistant.ui.HomeViewModel$suggestMeetingsForDay$1", f = "HomeViewModel.kt", i = {0, 0, 0}, l = {157}, m = "invokeSuspend", n = {"refine", "day", "dayEnd"}, s = {"L$0", "J$0", "J$1"})
    static final class C06991 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
        long J$0;
        long J$1;
        Object L$0;
        int label;

        C06991(Continuation<? super C06991> continuation) {
            super(2, continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
            return HomeViewModel.this.new C06991(continuation);
        }

        @Override // kotlin.jvm.functions.Function2
        public final Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
            return ((C06991) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object $result) throws Throwable {
            boolean z;
            Function2 homeViewModel$suggestMeetingsForDay$1$refine$1;
            Object objDetectMeetingProposals;
            Function2 refine;
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            try {
                switch (this.label) {
                    case 0:
                        ResultKt.throwOnFailure($result);
                        HomeViewModel.this._detectingMeetings.setValue(Boxing.boxBoolean(true));
                        long day = ((Number) HomeViewModel.this._dayStartMs.getValue()).longValue();
                        long dayEnd = day + TimeUnit.DAYS.toMillis(1L);
                        if (HomeViewModel.this.app.getContainer().getPipelineConfig().cloudConfigured()) {
                            homeViewModel$suggestMeetingsForDay$1$refine$1 = new HomeViewModel$suggestMeetingsForDay$1$refine$1(HomeViewModel.this, null);
                        } else {
                            homeViewModel$suggestMeetingsForDay$1$refine$1 = null;
                        }
                        this.L$0 = SpillingKt.nullOutSpilledVariable(homeViewModel$suggestMeetingsForDay$1$refine$1);
                        this.J$0 = day;
                        this.J$1 = dayEnd;
                        this.label = 1;
                        objDetectMeetingProposals = HomeViewModel.this.meetingRepo.detectMeetingProposals(day, dayEnd, homeViewModel$suggestMeetingsForDay$1$refine$1, this);
                        if (objDetectMeetingProposals == coroutine_suspended) {
                            return coroutine_suspended;
                        }
                        refine = homeViewModel$suggestMeetingsForDay$1$refine$1;
                        break;
                        break;
                    case 1:
                        long j = this.J$1;
                        long j2 = this.J$0;
                        refine = (Function2) this.L$0;
                        ResultKt.throwOnFailure($result);
                        objDetectMeetingProposals = $result;
                        break;
                    default:
                        throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
                Iterable proposals = (List) objDetectMeetingProposals;
                MutableStateFlow mutableStateFlow = HomeViewModel.this._proposals;
                HomeViewModel homeViewModel = HomeViewModel.this;
                Collection arrayList = new ArrayList();
                for (Object obj : proposals) {
                    z = false;
                    try {
                        Function2 function2 = refine;
                        if (!homeViewModel.dismissedProposalIds.contains(((GapClusterer.Proposal) obj).getId())) {
                            arrayList.add(obj);
                        }
                        refine = function2;
                    } catch (Throwable th) {
                        th = th;
                        HomeViewModel.this._detectingMeetings.setValue(Boxing.boxBoolean(z));
                        throw th;
                    }
                }
                z = false;
                mutableStateFlow.setValue((List) arrayList);
                HomeViewModel.this._detectingMeetings.setValue(Boxing.boxBoolean(false));
                return Unit.INSTANCE;
            } catch (Throwable th2) {
                th = th2;
                z = false;
            }
        }
    }

    public final void suggestMeetingsForDay() {
        BuildersKt__Builders_commonKt.launch$default(ViewModelKt.getViewModelScope(this), null, null, new C06991(null), 3, null);
    }

    /* JADX INFO: renamed from: com.varun.pocketassistant.ui.HomeViewModel$acceptProposal$2, reason: invalid class name */
    /* JADX INFO: compiled from: HomeViewModel.kt */
    @Metadata(d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H\n"}, d2 = {"<anonymous>", "", "Lkotlinx/coroutines/CoroutineScope;"}, k = 3, mv = {2, 2, 0}, xi = 48)
    @DebugMetadata(c = "com.varun.pocketassistant.ui.HomeViewModel$acceptProposal$2", f = "HomeViewModel.kt", i = {1}, l = {167, 170}, m = "invokeSuspend", n = {"meeting"}, s = {"L$0"})
    static final class AnonymousClass2 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
        final /* synthetic */ Function1<String, Unit> $onCreated;
        final /* synthetic */ GapClusterer.Proposal $proposal;
        Object L$0;
        int label;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        /* JADX WARN: Multi-variable type inference failed */
        AnonymousClass2(GapClusterer.Proposal proposal, Function1<? super String, Unit> function1, Continuation<? super AnonymousClass2> continuation) {
            super(2, continuation);
            this.$proposal = proposal;
            this.$onCreated = function1;
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
            return HomeViewModel.this.new AnonymousClass2(this.$proposal, this.$onCreated, continuation);
        }

        @Override // kotlin.jvm.functions.Function2
        public final Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
            return ((AnonymousClass2) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
        }

        /* JADX WARN: Code duplicated, block: B:14:0x007f  */
        /* JADX WARN: Code duplicated, block: B:19:0x00c1 A[RETURN] */
        /* JADX WARN: Code duplicated, block: B:20:0x00c2  */
        /* JADX WARN: Code duplicated, block: B:24:0x0095 A[SYNTHETIC] */
        /* JADX WARN: Code duplicated, block: B:26:0x0079 A[SYNTHETIC] */
        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object $result) {
            Object objCreateMeeting;
            MeetingEntity meeting;
            GapClusterer.Proposal proposal;
            Collection arrayList;
            MeetingEntity meeting2;
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    this.label = 1;
                    objCreateMeeting = HomeViewModel.this.meetingRepo.createMeeting(this.$proposal.getStartMs(), this.$proposal.getEndMs(), this);
                    if (objCreateMeeting == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    meeting = (MeetingEntity) objCreateMeeting;
                    HomeViewModel.this.dismissedProposalIds.add(this.$proposal.getId());
                    MutableStateFlow mutableStateFlow = HomeViewModel.this._proposals;
                    Iterable iterable = (Iterable) HomeViewModel.this._proposals.getValue();
                    proposal = this.$proposal;
                    arrayList = new ArrayList();
                    for (Object obj : iterable) {
                        if (!Intrinsics.areEqual(((GapClusterer.Proposal) obj).getId(), proposal.getId())) {
                            arrayList.add(obj);
                        }
                    }
                    mutableStateFlow.setValue((List) arrayList);
                    this.L$0 = meeting;
                    this.label = 2;
                    if (HomeViewModel.this.refreshDay(((Number) HomeViewModel.this._dayStartMs.getValue()).longValue(), this) == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    meeting2 = meeting;
                    this.$onCreated.invoke(meeting2.getId());
                    return Unit.INSTANCE;
                case 1:
                    ResultKt.throwOnFailure($result);
                    objCreateMeeting = $result;
                    meeting = (MeetingEntity) objCreateMeeting;
                    HomeViewModel.this.dismissedProposalIds.add(this.$proposal.getId());
                    MutableStateFlow mutableStateFlow2 = HomeViewModel.this._proposals;
                    Iterable iterable2 = (Iterable) HomeViewModel.this._proposals.getValue();
                    proposal = this.$proposal;
                    arrayList = new ArrayList();
                    while (r10.hasNext()) {
                        if (!Intrinsics.areEqual(((GapClusterer.Proposal) obj).getId(), proposal.getId())) {
                            arrayList.add(obj);
                        }
                    }
                    mutableStateFlow2.setValue((List) arrayList);
                    this.L$0 = meeting;
                    this.label = 2;
                    if (HomeViewModel.this.refreshDay(((Number) HomeViewModel.this._dayStartMs.getValue()).longValue(), this) == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    meeting2 = meeting;
                    this.$onCreated.invoke(meeting2.getId());
                    return Unit.INSTANCE;
                case 2:
                    meeting2 = (MeetingEntity) this.L$0;
                    ResultKt.throwOnFailure($result);
                    this.$onCreated.invoke(meeting2.getId());
                    return Unit.INSTANCE;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static /* synthetic */ void acceptProposal$default(HomeViewModel homeViewModel, GapClusterer.Proposal proposal, Function1 function1, int i, Object obj) {
        if ((i & 2) != 0) {
            function1 = new Function1() { // from class: com.varun.pocketassistant.ui.HomeViewModel$$ExternalSyntheticLambda1
                @Override // kotlin.jvm.functions.Function1
                public final Object invoke(Object obj2) {
                    return HomeViewModel.acceptProposal$lambda$5((String) obj2);
                }
            };
        }
        homeViewModel.acceptProposal(proposal, function1);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Unit acceptProposal$lambda$5(String it) {
        Intrinsics.checkNotNullParameter(it, "it");
        return Unit.INSTANCE;
    }

    public final void acceptProposal(GapClusterer.Proposal proposal, Function1<? super String, Unit> onCreated) {
        Intrinsics.checkNotNullParameter(proposal, "proposal");
        Intrinsics.checkNotNullParameter(onCreated, "onCreated");
        BuildersKt__Builders_commonKt.launch$default(ViewModelKt.getViewModelScope(this), null, null, new AnonymousClass2(proposal, onCreated, null), 3, null);
    }

    public final void dismissProposal(GapClusterer.Proposal proposal) {
        Intrinsics.checkNotNullParameter(proposal, "proposal");
        this.dismissedProposalIds.add(proposal.getId());
        MutableStateFlow<List<GapClusterer.Proposal>> mutableStateFlow = this._proposals;
        Iterable value = this._proposals.getValue();
        Collection arrayList = new ArrayList();
        for (Object obj : value) {
            if (!Intrinsics.areEqual(((GapClusterer.Proposal) obj).getId(), proposal.getId())) {
                arrayList.add(obj);
            }
        }
        mutableStateFlow.setValue((List) arrayList);
    }

    public final void setTimelineSelection(Long startMs, Long endMs) {
        this._selectionStartMs.setValue(startMs);
        this._selectionEndMs.setValue(endMs);
    }

    public final void clearSelection() {
        this._selectionStartMs.setValue(null);
        this._selectionEndMs.setValue(null);
    }

    public final void createMeetingFromSelection(final Function1<? super String, Unit> onCreated) {
        Intrinsics.checkNotNullParameter(onCreated, "onCreated");
        Long value = this._selectionStartMs.getValue();
        if (value == null) {
            return;
        }
        long a = value.longValue();
        Long value2 = this._selectionEndMs.getValue();
        if (value2 == null) {
            return;
        }
        long b = value2.longValue();
        long start = Math.min(a, b);
        long end = Math.max(a, b);
        if (Math.abs(end - start) < PipelineTelemetry.ASR_HTTP_WRITE_TIMEOUT_MS) {
            return;
        }
        createMeeting(start, end, new Function1() { // from class: com.varun.pocketassistant.ui.HomeViewModel$$ExternalSyntheticLambda2
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return HomeViewModel.createMeetingFromSelection$lambda$7(this.f$0, onCreated, (String) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Unit createMeetingFromSelection$lambda$7(HomeViewModel this$0, Function1 $onCreated, String id) {
        Intrinsics.checkNotNullParameter(id, "id");
        this$0.clearSelection();
        $onCreated.invoke(id);
        return Unit.INSTANCE;
    }

    public final void setMeetingSearch(String query) {
        Intrinsics.checkNotNullParameter(query, "query");
        this._meetingSearch.setValue(query);
    }

    public final void startRecording() {
        RecordingService.INSTANCE.start(getApplication());
    }

    public final void pauseRecording() {
        RecordingService.INSTANCE.pause(getApplication());
    }

    public final void resumeRecording() {
        RecordingService.INSTANCE.resume(getApplication());
    }

    public final void stopRecording() {
        RecordingService.INSTANCE.stop(getApplication());
    }

    /* JADX INFO: renamed from: com.varun.pocketassistant.ui.HomeViewModel$deleteSession$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: HomeViewModel.kt */
    @Metadata(d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H\n"}, d2 = {"<anonymous>", "", "Lkotlinx/coroutines/CoroutineScope;"}, k = 3, mv = {2, 2, 0}, xi = 48)
    @DebugMetadata(c = "com.varun.pocketassistant.ui.HomeViewModel$deleteSession$1", f = "HomeViewModel.kt", i = {}, l = {225}, m = "invokeSuspend", n = {}, s = {})
    static final class C06941 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
        final /* synthetic */ String $sessionId;
        int label;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        C06941(String str, Continuation<? super C06941> continuation) {
            super(2, continuation);
            this.$sessionId = str;
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
            return HomeViewModel.this.new C06941(this.$sessionId, continuation);
        }

        @Override // kotlin.jvm.functions.Function2
        public final Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
            return ((C06941) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object $result) {
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    if (((PlaybackState) HomeViewModel.this._playback.getValue()).getSegmentId() != null) {
                        HomeViewModel.this.stopPlayback();
                    }
                    this.label = 1;
                    if (HomeViewModel.this.repo.deleteSession(this.$sessionId, this) == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    break;
                case 1:
                    ResultKt.throwOnFailure($result);
                    break;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
            return Unit.INSTANCE;
        }
    }

    public final void deleteSession(String sessionId) {
        Intrinsics.checkNotNullParameter(sessionId, "sessionId");
        BuildersKt__Builders_commonKt.launch$default(ViewModelKt.getViewModelScope(this), null, null, new C06941(sessionId, null), 3, null);
    }

    public final StateFlow<List<SegmentEntity>> observeSegments(String sessionId) {
        StateFlow<List<SegmentEntity>> stateFlowStateIn;
        Intrinsics.checkNotNullParameter(sessionId, "sessionId");
        Map<String, StateFlow<List<SegmentEntity>>> map = this.segmentFlows;
        StateFlow<List<SegmentEntity>> stateFlow = map.get(sessionId);
        if (stateFlow == null) {
            stateFlowStateIn = FlowKt.stateIn(this.repo.observeSegments(sessionId), ViewModelKt.getViewModelScope(this), SharingStarted.Companion.WhileSubscribed$default(SharingStarted.INSTANCE, 5000L, 0L, 2, null), CollectionsKt.emptyList());
            map.put(sessionId, stateFlowStateIn);
        } else {
            stateFlowStateIn = stateFlow;
        }
        return stateFlowStateIn;
    }

    public final StateFlow<List<SegmentEntity>> observeMeetingRecordings(String meetingId) {
        StateFlow<List<SegmentEntity>> stateFlowStateIn;
        Intrinsics.checkNotNullParameter(meetingId, "meetingId");
        Map<String, StateFlow<List<SegmentEntity>>> map = this.meetingRecordingFlows;
        StateFlow<List<SegmentEntity>> stateFlow = map.get(meetingId);
        if (stateFlow == null) {
            stateFlowStateIn = FlowKt.stateIn(this.meetingRepo.observeMeetingRecordings(meetingId), ViewModelKt.getViewModelScope(this), SharingStarted.Companion.WhileSubscribed$default(SharingStarted.INSTANCE, 5000L, 0L, 2, null), CollectionsKt.emptyList());
            map.put(meetingId, stateFlowStateIn);
        } else {
            stateFlowStateIn = stateFlow;
        }
        return stateFlowStateIn;
    }

    public final StateFlow<MeetingEntity> observeMeeting(String meetingId) {
        StateFlow<MeetingEntity> stateFlowStateIn;
        Object next;
        Intrinsics.checkNotNullParameter(meetingId, "meetingId");
        Map<String, StateFlow<MeetingEntity>> map = this.meetingFlows;
        StateFlow<MeetingEntity> stateFlow = map.get(meetingId);
        if (stateFlow == null) {
            Flow<MeetingEntity> flowObserveMeeting = this.meetingRepo.observeMeeting(meetingId);
            CoroutineScope viewModelScope = ViewModelKt.getViewModelScope(this);
            SharingStarted sharingStartedWhileSubscribed$default = SharingStarted.Companion.WhileSubscribed$default(SharingStarted.INSTANCE, 5000L, 0L, 2, null);
            Iterator it = this.meetings.getValue().iterator();
            do {
                if (!it.hasNext()) {
                    next = null;
                    break;
                }
                next = it.next();
            } while (!Intrinsics.areEqual(((MeetingEntity) next).getId(), meetingId));
            stateFlowStateIn = FlowKt.stateIn(flowObserveMeeting, viewModelScope, sharingStartedWhileSubscribed$default, next);
            map.put(meetingId, stateFlowStateIn);
        } else {
            stateFlowStateIn = stateFlow;
        }
        return stateFlowStateIn;
    }

    /* JADX INFO: renamed from: com.varun.pocketassistant.ui.HomeViewModel$previewMeetingRange$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: HomeViewModel.kt */
    @Metadata(d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H\n"}, d2 = {"<anonymous>", "", "Lkotlinx/coroutines/CoroutineScope;"}, k = 3, mv = {2, 2, 0}, xi = 48)
    @DebugMetadata(c = "com.varun.pocketassistant.ui.HomeViewModel$previewMeetingRange$1", f = "HomeViewModel.kt", i = {}, l = {258}, m = "invokeSuspend", n = {}, s = {})
    static final class C06951 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
        final /* synthetic */ long $endMs;
        final /* synthetic */ long $startMs;
        Object L$0;
        int label;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        C06951(long j, long j2, Continuation<? super C06951> continuation) {
            super(2, continuation);
            this.$endMs = j;
            this.$startMs = j2;
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
            return HomeViewModel.this.new C06951(this.$endMs, this.$startMs, continuation);
        }

        @Override // kotlin.jvm.functions.Function2
        public final Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
            return ((C06951) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object $result) {
            MutableStateFlow mutableStateFlow;
            OverlapPreview overlapPreview;
            Object objPreviewOverlap;
            MutableStateFlow mutableStateFlow2;
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    mutableStateFlow = HomeViewModel.this._createPreview;
                    if (this.$endMs > this.$startMs) {
                        this.L$0 = mutableStateFlow;
                        this.label = 1;
                        objPreviewOverlap = HomeViewModel.this.meetingRepo.previewOverlap(this.$startMs, this.$endMs, this);
                        if (objPreviewOverlap == coroutine_suspended) {
                            return coroutine_suspended;
                        }
                        mutableStateFlow2 = mutableStateFlow;
                        overlapPreview = (OverlapPreview) objPreviewOverlap;
                        mutableStateFlow = mutableStateFlow2;
                    } else {
                        overlapPreview = null;
                    }
                    mutableStateFlow.setValue(overlapPreview);
                    return Unit.INSTANCE;
                case 1:
                    mutableStateFlow2 = (MutableStateFlow) this.L$0;
                    ResultKt.throwOnFailure($result);
                    objPreviewOverlap = $result;
                    overlapPreview = (OverlapPreview) objPreviewOverlap;
                    mutableStateFlow = mutableStateFlow2;
                    mutableStateFlow.setValue(overlapPreview);
                    return Unit.INSTANCE;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }

    public final void previewMeetingRange(long startMs, long endMs) {
        BuildersKt__Builders_commonKt.launch$default(ViewModelKt.getViewModelScope(this), null, null, new C06951(endMs, startMs, null), 3, null);
    }

    public final void clearCreatePreview() {
        this._createPreview.setValue(null);
    }

    /* JADX INFO: renamed from: com.varun.pocketassistant.ui.HomeViewModel$createMeeting$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: HomeViewModel.kt */
    @Metadata(d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H\n"}, d2 = {"<anonymous>", "", "Lkotlinx/coroutines/CoroutineScope;"}, k = 3, mv = {2, 2, 0}, xi = 48)
    @DebugMetadata(c = "com.varun.pocketassistant.ui.HomeViewModel$createMeeting$1", f = "HomeViewModel.kt", i = {1}, l = {271, 272}, m = "invokeSuspend", n = {"meeting"}, s = {"L$0"})
    static final class C06911 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
        final /* synthetic */ long $endMs;
        final /* synthetic */ Function1<String, Unit> $onCreated;
        final /* synthetic */ long $startMs;
        Object L$0;
        int label;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        /* JADX WARN: Multi-variable type inference failed */
        C06911(long j, long j2, Function1<? super String, Unit> function1, Continuation<? super C06911> continuation) {
            super(2, continuation);
            this.$startMs = j;
            this.$endMs = j2;
            this.$onCreated = function1;
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
            return HomeViewModel.this.new C06911(this.$startMs, this.$endMs, this.$onCreated, continuation);
        }

        @Override // kotlin.jvm.functions.Function2
        public final Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
            return ((C06911) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
        }

        /* JADX WARN: Code duplicated, block: B:13:0x005a A[RETURN] */
        /* JADX WARN: Code duplicated, block: B:14:0x005b  */
        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object $result) {
            Object objCreateMeeting;
            MeetingEntity meeting;
            MeetingEntity meeting2;
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    this.label = 1;
                    objCreateMeeting = HomeViewModel.this.meetingRepo.createMeeting(this.$startMs, this.$endMs, this);
                    if (objCreateMeeting == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    meeting = (MeetingEntity) objCreateMeeting;
                    this.L$0 = meeting;
                    this.label = 2;
                    if (HomeViewModel.this.refreshDay(((Number) HomeViewModel.this._dayStartMs.getValue()).longValue(), this) == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    meeting2 = meeting;
                    this.$onCreated.invoke(meeting2.getId());
                    return Unit.INSTANCE;
                case 1:
                    ResultKt.throwOnFailure($result);
                    objCreateMeeting = $result;
                    meeting = (MeetingEntity) objCreateMeeting;
                    this.L$0 = meeting;
                    this.label = 2;
                    if (HomeViewModel.this.refreshDay(((Number) HomeViewModel.this._dayStartMs.getValue()).longValue(), this) == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    meeting2 = meeting;
                    this.$onCreated.invoke(meeting2.getId());
                    return Unit.INSTANCE;
                case 2:
                    meeting2 = (MeetingEntity) this.L$0;
                    ResultKt.throwOnFailure($result);
                    this.$onCreated.invoke(meeting2.getId());
                    return Unit.INSTANCE;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }

    public final void createMeeting(long startMs, long endMs, Function1<? super String, Unit> onCreated) {
        Intrinsics.checkNotNullParameter(onCreated, "onCreated");
        BuildersKt__Builders_commonKt.launch$default(ViewModelKt.getViewModelScope(this), null, null, new C06911(startMs, endMs, onCreated, null), 3, null);
    }

    /* JADX INFO: renamed from: com.varun.pocketassistant.ui.HomeViewModel$updateMeetingRange$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: HomeViewModel.kt */
    @Metadata(d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H\n"}, d2 = {"<anonymous>", "", "Lkotlinx/coroutines/CoroutineScope;"}, k = 3, mv = {2, 2, 0}, xi = 48)
    @DebugMetadata(c = "com.varun.pocketassistant.ui.HomeViewModel$updateMeetingRange$1", f = "HomeViewModel.kt", i = {}, l = {279}, m = "invokeSuspend", n = {}, s = {})
    static final class C07011 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
        final /* synthetic */ long $endMs;
        final /* synthetic */ String $meetingId;
        final /* synthetic */ long $startMs;
        int label;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        C07011(String str, long j, long j2, Continuation<? super C07011> continuation) {
            super(2, continuation);
            this.$meetingId = str;
            this.$startMs = j;
            this.$endMs = j2;
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
            return HomeViewModel.this.new C07011(this.$meetingId, this.$startMs, this.$endMs, continuation);
        }

        @Override // kotlin.jvm.functions.Function2
        public final Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
            return ((C07011) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object $result) {
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    this.label = 1;
                    if (HomeViewModel.this.meetingRepo.updateMeetingRange(this.$meetingId, this.$startMs, this.$endMs, this) == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    break;
                case 1:
                    ResultKt.throwOnFailure($result);
                    break;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
            return Unit.INSTANCE;
        }
    }

    public final void updateMeetingRange(String meetingId, long startMs, long endMs) {
        Intrinsics.checkNotNullParameter(meetingId, "meetingId");
        BuildersKt__Builders_commonKt.launch$default(ViewModelKt.getViewModelScope(this), null, null, new C07011(meetingId, startMs, endMs, null), 3, null);
    }

    /* JADX INFO: renamed from: com.varun.pocketassistant.ui.HomeViewModel$deleteMeeting$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: HomeViewModel.kt */
    @Metadata(d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H\n"}, d2 = {"<anonymous>", "", "Lkotlinx/coroutines/CoroutineScope;"}, k = 3, mv = {2, 2, 0}, xi = 48)
    @DebugMetadata(c = "com.varun.pocketassistant.ui.HomeViewModel$deleteMeeting$1", f = "HomeViewModel.kt", i = {}, l = {285}, m = "invokeSuspend", n = {}, s = {})
    static final class C06921 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
        final /* synthetic */ String $meetingId;
        int label;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        C06921(String str, Continuation<? super C06921> continuation) {
            super(2, continuation);
            this.$meetingId = str;
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
            return HomeViewModel.this.new C06921(this.$meetingId, continuation);
        }

        @Override // kotlin.jvm.functions.Function2
        public final Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
            return ((C06921) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object $result) {
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    this.label = 1;
                    if (HomeViewModel.this.meetingRepo.deleteMeeting(this.$meetingId, this) == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    break;
                case 1:
                    ResultKt.throwOnFailure($result);
                    break;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
            return Unit.INSTANCE;
        }
    }

    public final void deleteMeeting(String meetingId) {
        Intrinsics.checkNotNullParameter(meetingId, "meetingId");
        BuildersKt__Builders_commonKt.launch$default(ViewModelKt.getViewModelScope(this), null, null, new C06921(meetingId, null), 3, null);
    }

    /* JADX INFO: renamed from: com.varun.pocketassistant.ui.HomeViewModel$retryMeetingCleanup$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: HomeViewModel.kt */
    @Metadata(d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H\n"}, d2 = {"<anonymous>", "", "Lkotlinx/coroutines/CoroutineScope;"}, k = 3, mv = {2, 2, 0}, xi = 48)
    @DebugMetadata(c = "com.varun.pocketassistant.ui.HomeViewModel$retryMeetingCleanup$1", f = "HomeViewModel.kt", i = {}, l = {291}, m = "invokeSuspend", n = {}, s = {})
    static final class C06981 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
        final /* synthetic */ String $meetingId;
        int label;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        C06981(String str, Continuation<? super C06981> continuation) {
            super(2, continuation);
            this.$meetingId = str;
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
            return HomeViewModel.this.new C06981(this.$meetingId, continuation);
        }

        @Override // kotlin.jvm.functions.Function2
        public final Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
            return ((C06981) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object $result) {
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    this.label = 1;
                    if (HomeViewModel.this.meetingRepo.retryCleanup(this.$meetingId, this) == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    break;
                case 1:
                    ResultKt.throwOnFailure($result);
                    break;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
            return Unit.INSTANCE;
        }
    }

    public final void retryMeetingCleanup(String meetingId) {
        Intrinsics.checkNotNullParameter(meetingId, "meetingId");
        BuildersKt__Builders_commonKt.launch$default(ViewModelKt.getViewModelScope(this), null, null, new C06981(meetingId, null), 3, null);
    }

    /* JADX INFO: renamed from: com.varun.pocketassistant.ui.HomeViewModel$assignRecordingToMeeting$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: HomeViewModel.kt */
    @Metadata(d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H\n"}, d2 = {"<anonymous>", "", "Lkotlinx/coroutines/CoroutineScope;"}, k = 3, mv = {2, 2, 0}, xi = 48)
    @DebugMetadata(c = "com.varun.pocketassistant.ui.HomeViewModel$assignRecordingToMeeting$1", f = "HomeViewModel.kt", i = {}, l = {298, 299}, m = "invokeSuspend", n = {}, s = {})
    static final class C06891 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
        final /* synthetic */ String $meetingId;
        final /* synthetic */ String $segmentId;
        int label;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        C06891(String str, String str2, Continuation<? super C06891> continuation) {
            super(2, continuation);
            this.$segmentId = str;
            this.$meetingId = str2;
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
            return HomeViewModel.this.new C06891(this.$segmentId, this.$meetingId, continuation);
        }

        @Override // kotlin.jvm.functions.Function2
        public final Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
            return ((C06891) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
        }

        /* JADX WARN: Code duplicated, block: B:16:0x006e A[RETURN] */
        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object $result) {
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    if (Intrinsics.areEqual(((PlaybackState) HomeViewModel.this._playback.getValue()).getSegmentId(), this.$segmentId)) {
                        HomeViewModel.this.stopPlayback();
                    }
                    this.label = 1;
                    if (HomeViewModel.this.meetingRepo.assignRecording(this.$segmentId, this.$meetingId, this) == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    this.label = 2;
                    if (HomeViewModel.this.refreshDay(((Number) HomeViewModel.this._dayStartMs.getValue()).longValue(), this) == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    return Unit.INSTANCE;
                case 1:
                    ResultKt.throwOnFailure($result);
                    this.label = 2;
                    if (HomeViewModel.this.refreshDay(((Number) HomeViewModel.this._dayStartMs.getValue()).longValue(), this) == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    return Unit.INSTANCE;
                case 2:
                    ResultKt.throwOnFailure($result);
                    return Unit.INSTANCE;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }

    public final void assignRecordingToMeeting(String segmentId, String meetingId) {
        Intrinsics.checkNotNullParameter(segmentId, "segmentId");
        Intrinsics.checkNotNullParameter(meetingId, "meetingId");
        BuildersKt__Builders_commonKt.launch$default(ViewModelKt.getViewModelScope(this), null, null, new C06891(segmentId, meetingId, null), 3, null);
    }

    /* JADX INFO: renamed from: com.varun.pocketassistant.ui.HomeViewModel$unassignRecording$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: HomeViewModel.kt */
    @Metadata(d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H\n"}, d2 = {"<anonymous>", "", "Lkotlinx/coroutines/CoroutineScope;"}, k = 3, mv = {2, 2, 0}, xi = 48)
    @DebugMetadata(c = "com.varun.pocketassistant.ui.HomeViewModel$unassignRecording$1", f = "HomeViewModel.kt", i = {}, l = {305}, m = "invokeSuspend", n = {}, s = {})
    static final class C07001 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
        final /* synthetic */ String $segmentId;
        int label;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        C07001(String str, Continuation<? super C07001> continuation) {
            super(2, continuation);
            this.$segmentId = str;
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
            return HomeViewModel.this.new C07001(this.$segmentId, continuation);
        }

        @Override // kotlin.jvm.functions.Function2
        public final Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
            return ((C07001) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object $result) {
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    this.label = 1;
                    if (HomeViewModel.this.meetingRepo.unassignRecording(this.$segmentId, this) == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    break;
                case 1:
                    ResultKt.throwOnFailure($result);
                    break;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
            return Unit.INSTANCE;
        }
    }

    public final void unassignRecording(String segmentId) {
        Intrinsics.checkNotNullParameter(segmentId, "segmentId");
        BuildersKt__Builders_commonKt.launch$default(ViewModelKt.getViewModelScope(this), null, null, new C07001(segmentId, null), 3, null);
    }

    /* JADX INFO: renamed from: com.varun.pocketassistant.ui.HomeViewModel$deleteRecording$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: HomeViewModel.kt */
    @Metadata(d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H\n"}, d2 = {"<anonymous>", "", "Lkotlinx/coroutines/CoroutineScope;"}, k = 3, mv = {2, 2, 0}, xi = 48)
    @DebugMetadata(c = "com.varun.pocketassistant.ui.HomeViewModel$deleteRecording$1", f = "HomeViewModel.kt", i = {}, l = {312}, m = "invokeSuspend", n = {}, s = {})
    static final class C06931 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
        final /* synthetic */ String $segmentId;
        int label;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        C06931(String str, Continuation<? super C06931> continuation) {
            super(2, continuation);
            this.$segmentId = str;
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
            return HomeViewModel.this.new C06931(this.$segmentId, continuation);
        }

        @Override // kotlin.jvm.functions.Function2
        public final Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
            return ((C06931) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object $result) {
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    if (Intrinsics.areEqual(((PlaybackState) HomeViewModel.this._playback.getValue()).getSegmentId(), this.$segmentId)) {
                        HomeViewModel.this.stopPlayback();
                    }
                    this.label = 1;
                    if (HomeViewModel.this.meetingRepo.deleteRecording(this.$segmentId, this) == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    break;
                case 1:
                    ResultKt.throwOnFailure($result);
                    break;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
            return Unit.INSTANCE;
        }
    }

    public final void deleteRecording(String segmentId) {
        Intrinsics.checkNotNullParameter(segmentId, "segmentId");
        BuildersKt__Builders_commonKt.launch$default(ViewModelKt.getViewModelScope(this), null, null, new C06931(segmentId, null), 3, null);
    }

    /* JADX INFO: renamed from: com.varun.pocketassistant.ui.HomeViewModel$retranscribeRecording$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: HomeViewModel.kt */
    @Metadata(d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H\n"}, d2 = {"<anonymous>", "", "Lkotlinx/coroutines/CoroutineScope;"}, k = 3, mv = {2, 2, 0}, xi = 48)
    @DebugMetadata(c = "com.varun.pocketassistant.ui.HomeViewModel$retranscribeRecording$1", f = "HomeViewModel.kt", i = {}, l = {318}, m = "invokeSuspend", n = {}, s = {})
    static final class C06971 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
        final /* synthetic */ String $segmentId;
        int label;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        C06971(String str, Continuation<? super C06971> continuation) {
            super(2, continuation);
            this.$segmentId = str;
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
            return HomeViewModel.this.new C06971(this.$segmentId, continuation);
        }

        @Override // kotlin.jvm.functions.Function2
        public final Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
            return ((C06971) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object $result) {
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    this.label = 1;
                    if (HomeViewModel.this.repo.requeueForAsr(this.$segmentId, this) == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    break;
                case 1:
                    ResultKt.throwOnFailure($result);
                    break;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
            return Unit.INSTANCE;
        }
    }

    public final void retranscribeRecording(String segmentId) {
        Intrinsics.checkNotNullParameter(segmentId, "segmentId");
        BuildersKt__Builders_commonKt.launch$default(ViewModelKt.getViewModelScope(this), null, null, new C06971(segmentId, null), 3, null);
    }

    /* JADX INFO: renamed from: com.varun.pocketassistant.ui.HomeViewModel$confirmYouSpeaker$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: HomeViewModel.kt */
    @Metadata(d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H\n"}, d2 = {"<anonymous>", "", "Lkotlinx/coroutines/CoroutineScope;"}, k = 3, mv = {2, 2, 0}, xi = 48)
    @DebugMetadata(c = "com.varun.pocketassistant.ui.HomeViewModel$confirmYouSpeaker$1", f = "HomeViewModel.kt", i = {}, l = {325, 326}, m = "invokeSuspend", n = {}, s = {})
    static final class C06901 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
        final /* synthetic */ String $segmentId;
        final /* synthetic */ int $youSpeakerId;
        int label;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        C06901(String str, int i, Continuation<? super C06901> continuation) {
            super(2, continuation);
            this.$segmentId = str;
            this.$youSpeakerId = i;
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
            return HomeViewModel.this.new C06901(this.$segmentId, this.$youSpeakerId, continuation);
        }

        @Override // kotlin.jvm.functions.Function2
        public final Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
            return ((C06901) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
        }

        /* JADX WARN: Code duplicated, block: B:13:0x003c  */
        /* JADX WARN: Code duplicated, block: B:15:0x003f  */
        /* JADX WARN: Code duplicated, block: B:17:0x0053 A[RETURN] */
        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object $result) {
            Object objConfirmYouSpeaker;
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    this.label = 1;
                    objConfirmYouSpeaker = HomeViewModel.this.repo.confirmYouSpeaker(this.$segmentId, this.$youSpeakerId, this);
                    if (objConfirmYouSpeaker == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    if (!((Boolean) objConfirmYouSpeaker).booleanValue()) {
                        return Unit.INSTANCE;
                    }
                    this.label = 2;
                    if (HomeViewModel.this.meetingRepo.onSegmentSpeakersConfirmed(this.$segmentId, this) == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    return Unit.INSTANCE;
                case 1:
                    ResultKt.throwOnFailure($result);
                    objConfirmYouSpeaker = $result;
                    if (!((Boolean) objConfirmYouSpeaker).booleanValue()) {
                        return Unit.INSTANCE;
                    }
                    this.label = 2;
                    if (HomeViewModel.this.meetingRepo.onSegmentSpeakersConfirmed(this.$segmentId, this) == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    return Unit.INSTANCE;
                case 2:
                    ResultKt.throwOnFailure($result);
                    return Unit.INSTANCE;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }

    public final void confirmYouSpeaker(String segmentId, int youSpeakerId) {
        Intrinsics.checkNotNullParameter(segmentId, "segmentId");
        BuildersKt__Builders_commonKt.launch$default(ViewModelKt.getViewModelScope(this), null, null, new C06901(segmentId, youSpeakerId, null), 3, null);
    }

    public final LiveData<List<WorkInfo>> observeAsrAttempts(String segmentId) {
        Intrinsics.checkNotNullParameter(segmentId, "segmentId");
        return this.app.getContainer().getPipelineScheduler().observeAsr(segmentId);
    }

    public final LiveData<List<WorkInfo>> observeMeetingWork(String meetingId) {
        Intrinsics.checkNotNullParameter(meetingId, "meetingId");
        return this.app.getContainer().getPipelineScheduler().observeMeeting(meetingId);
    }

    public final void togglePlayback(SegmentEntity segment) {
        Intrinsics.checkNotNullParameter(segment, "segment");
        PlaybackState current = this._playback.getValue();
        if (Intrinsics.areEqual(current.getSegmentId(), segment.getId()) && current.isPlaying()) {
            stopPlayback();
            return;
        }
        stopPlayback();
        try {
            MediaPlayer player = new MediaPlayer();
            player.setDataSource(segment.getFilePath());
            player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() { // from class: com.varun.pocketassistant.ui.HomeViewModel$$ExternalSyntheticLambda0
                @Override // android.media.MediaPlayer.OnCompletionListener
                public final void onCompletion(MediaPlayer mediaPlayer) {
                    this.f$0.stopPlayback();
                }
            });
            player.prepare();
            player.start();
            this.mediaPlayer = player;
            this._playback.setValue(new PlaybackState(segment.getId(), true));
        } catch (Throwable th) {
            this._playback.setValue(new PlaybackState(null, false, 3, null));
        }
    }

    public final void stopPlayback() {
        MediaPlayer mediaPlayer = this.mediaPlayer;
        if (mediaPlayer != null) {
            try {
                Result.Companion companion = Result.INSTANCE;
                mediaPlayer.stop();
                Result.m8304constructorimpl(Unit.INSTANCE);
            } catch (Throwable th) {
                Result.Companion companion2 = Result.INSTANCE;
                Result.m8304constructorimpl(ResultKt.createFailure(th));
            }
            mediaPlayer.release();
        }
        this.mediaPlayer = null;
        this._playback.setValue(new PlaybackState(null, false, 3, null));
    }

    @Override // androidx.lifecycle.ViewModel
    protected void onCleared() {
        stopPlayback();
        super.onCleared();
    }
}
