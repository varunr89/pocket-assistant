package com.varun.pocketassistant.data;

import androidx.core.location.LocationRequestCompat;
import androidx.core.view.InputDeviceCompat;
import androidx.core.view.MotionEventCompat;
import com.google.android.gms.actions.SearchIntents;
import com.varun.pocketassistant.pipeline.work.AsrWorker;
import com.varun.pocketassistant.pipeline.work.MeetingStageWorker;
import com.varun.pocketassistant.pipeline.work.PipelineScheduler;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
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
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import kotlinx.coroutines.flow.Flow;

/* JADX INFO: compiled from: MeetingRepository.kt */
/* JADX INFO: loaded from: classes9.dex */
@Metadata(d1 = {"\u0000v\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\n\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0002\b\n\n\u0002\u0010\b\n\u0002\b\b\n\u0002\u0010\u000b\n\u0002\b\u0003\b\u0007\u0018\u0000 G2\u00020\u0001:\u0001GB\u0017\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0004\b\u0006\u0010\u0007JL\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00100\u000f2\u0006\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u00122&\b\u0002\u0010\u0014\u001a \b\u0001\u0012\u0004\u0012\u00020\u0016\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00160\u0017\u0012\u0006\u0012\u0004\u0018\u00010\u0001\u0018\u00010\u0015H\u0086@¢\u0006\u0002\u0010\u0018J\u0012\u0010\u0019\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u001b0\u000f0\u001aJ\u0016\u0010\u001c\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u001b0\u001a2\u0006\u0010\u001d\u001a\u00020\u0016J\u001a\u0010\u001e\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u001b0\u000f0\u001a2\u0006\u0010\u001f\u001a\u00020\u0016J\u001a\u0010 \u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020!0\u000f0\u001a2\u0006\u0010\"\u001a\u00020\u0016J\u0018\u0010#\u001a\u0004\u0018\u00010\u001b2\u0006\u0010\u001d\u001a\u00020\u0016H\u0086@¢\u0006\u0002\u0010$J\u001c\u0010%\u001a\b\u0012\u0004\u0012\u00020!0\u000f2\u0006\u0010\"\u001a\u00020\u0016H\u0086@¢\u0006\u0002\u0010$J$\u0010&\u001a\b\u0012\u0004\u0012\u00020!0\u000f2\u0006\u0010'\u001a\u00020\u00122\u0006\u0010(\u001a\u00020\u0012H\u0086@¢\u0006\u0002\u0010)J$\u0010*\u001a\b\u0012\u0004\u0012\u00020\u001b0\u000f2\u0006\u0010'\u001a\u00020\u00122\u0006\u0010(\u001a\u00020\u0012H\u0086@¢\u0006\u0002\u0010)J\u001e\u0010+\u001a\u00020,2\u0006\u0010'\u001a\u00020\u00122\u0006\u0010(\u001a\u00020\u0012H\u0086@¢\u0006\u0002\u0010)J\u001e\u0010-\u001a\u00020\u001b2\u0006\u0010'\u001a\u00020\u00122\u0006\u0010(\u001a\u00020\u0012H\u0086@¢\u0006\u0002\u0010)J(\u0010.\u001a\u0004\u0018\u00010\u001b2\u0006\u0010\"\u001a\u00020\u00162\u0006\u0010'\u001a\u00020\u00122\u0006\u0010(\u001a\u00020\u0012H\u0086@¢\u0006\u0002\u0010/J\u0016\u00100\u001a\u0002012\u0006\u0010\"\u001a\u00020\u0016H\u0086@¢\u0006\u0002\u0010$J\u001e\u00102\u001a\u0002012\u0006\u00103\u001a\u00020\u00162\u0006\u0010\"\u001a\u00020\u0016H\u0086@¢\u0006\u0002\u00104J\u0016\u00105\u001a\u0002012\u0006\u00103\u001a\u00020\u0016H\u0086@¢\u0006\u0002\u0010$J\u0016\u00106\u001a\u0002012\u0006\u00103\u001a\u00020\u0016H\u0086@¢\u0006\u0002\u0010$J\u0016\u00107\u001a\u0002012\u0006\u0010\"\u001a\u00020\u0016H\u0086@¢\u0006\u0002\u0010$J\u0016\u00108\u001a\u0002012\u0006\u00103\u001a\u00020\u0016H\u0086@¢\u0006\u0002\u0010$J\u0016\u00109\u001a\u0002012\u0006\u0010\"\u001a\u00020\u0016H\u0082@¢\u0006\u0002\u0010$J\u001c\u0010:\u001a\b\u0012\u0004\u0012\u00020\u001b0\u000f2\u0006\u0010;\u001a\u00020<H\u0086@¢\u0006\u0002\u0010=J\u001e\u0010>\u001a\b\u0012\u0004\u0012\u00020\u00160\u000f2\b\b\u0002\u0010;\u001a\u00020<H\u0086@¢\u0006\u0002\u0010=J\u0014\u0010?\u001a\b\u0012\u0004\u0012\u00020\u00160\u000fH\u0086@¢\u0006\u0002\u0010@J\u0016\u0010A\u001a\u0002012\u0006\u0010B\u001a\u00020\u001bH\u0086@¢\u0006\u0002\u0010CJ\u0016\u0010D\u001a\u00020E2\u0006\u0010\"\u001a\u00020\u0016H\u0086@¢\u0006\u0002\u0010$J&\u0010F\u001a\u0002012\u0006\u0010\"\u001a\u00020\u00162\u0006\u0010'\u001a\u00020\u00122\u0006\u0010(\u001a\u00020\u0012H\u0082@¢\u0006\u0002\u0010/R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004¢\u0006\u0002\n\u0000R\u001c\u0010\b\u001a\u0004\u0018\u00010\tX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\n\u0010\u000b\"\u0004\b\f\u0010\r¨\u0006H"}, d2 = {"Lcom/varun/pocketassistant/data/MeetingRepository;", "", "meetingDao", "Lcom/varun/pocketassistant/data/MeetingDao;", "segmentDao", "Lcom/varun/pocketassistant/data/SegmentDao;", "<init>", "(Lcom/varun/pocketassistant/data/MeetingDao;Lcom/varun/pocketassistant/data/SegmentDao;)V", "pipelineScheduler", "Lcom/varun/pocketassistant/pipeline/work/PipelineScheduler;", "getPipelineScheduler", "()Lcom/varun/pocketassistant/pipeline/work/PipelineScheduler;", "setPipelineScheduler", "(Lcom/varun/pocketassistant/pipeline/work/PipelineScheduler;)V", "detectMeetingProposals", "", "Lcom/varun/pocketassistant/meeting/GapClusterer$Proposal;", "dayStartMs", "", "dayEndMs", "refine", "Lkotlin/Function2;", "", "Lkotlin/coroutines/Continuation;", "(JJLkotlin/jvm/functions/Function2;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "observeMeetings", "Lkotlinx/coroutines/flow/Flow;", "Lcom/varun/pocketassistant/data/MeetingEntity;", "observeMeeting", "id", "searchMeetings", SearchIntents.EXTRA_QUERY, "observeMeetingRecordings", "Lcom/varun/pocketassistant/data/SegmentEntity;", MeetingStageWorker.KEY_MEETING_ID, "getMeeting", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getRecordings", "getSegmentsOverlapping", "startMs", "endMs", "(JJLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getMeetingsOverlapping", "previewOverlap", "Lcom/varun/pocketassistant/data/OverlapPreview;", "createMeeting", "updateMeetingRange", "(Ljava/lang/String;JJLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteMeeting", "", "assignRecording", AsrWorker.KEY_SEGMENT_ID, "(Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "unassignRecording", "deleteRecording", "retryCleanup", "onSegmentSpeakersConfirmed", "markMeetingNeedsCleanup", "getPendingCleanup", "limit", "", "(ILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "requeueAllForCleanup", "recoverStuckCleaning", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "updateMeeting", "meeting", "(Lcom/varun/pocketassistant/data/MeetingEntity;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "allRecordingsAsrSettled", "", "assignOverlapping", "Companion", "app_debug"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class MeetingRepository {
    private final MeetingDao meetingDao;
    private volatile PipelineScheduler pipelineScheduler;
    private final SegmentDao segmentDao;

    /* JADX INFO: renamed from: Companion, reason: from kotlin metadata */
    public static final Companion INSTANCE = new Companion(null);
    public static final int $stable = 8;

    /* JADX INFO: renamed from: com.varun.pocketassistant.data.MeetingRepository$allRecordingsAsrSettled$1, reason: invalid class name */
    /* JADX INFO: compiled from: MeetingRepository.kt */
    @Metadata(k = 3, mv = {2, 2, 0}, xi = 48)
    @DebugMetadata(c = "com.varun.pocketassistant.data.MeetingRepository", f = "MeetingRepository.kt", i = {0}, l = {261}, m = "allRecordingsAsrSettled", n = {MeetingStageWorker.KEY_MEETING_ID}, s = {"L$0"})
    static final class AnonymousClass1 extends ContinuationImpl {
        Object L$0;
        int label;
        /* synthetic */ Object result;

        AnonymousClass1(Continuation<? super AnonymousClass1> continuation) {
            super(continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            this.result = obj;
            this.label |= Integer.MIN_VALUE;
            return MeetingRepository.this.allRecordingsAsrSettled(null, this);
        }
    }

    /* JADX INFO: renamed from: com.varun.pocketassistant.data.MeetingRepository$assignOverlapping$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: MeetingRepository.kt */
    @Metadata(k = 3, mv = {2, 2, 0}, xi = 48)
    @DebugMetadata(c = "com.varun.pocketassistant.data.MeetingRepository", f = "MeetingRepository.kt", i = {0, 0, 0, 1, 1, 1, 1}, l = {270, 274}, m = "assignOverlapping", n = {MeetingStageWorker.KEY_MEETING_ID, "startMs", "endMs", MeetingStageWorker.KEY_MEETING_ID, "ids", "startMs", "endMs"}, s = {"L$0", "J$0", "J$1", "L$0", "L$1", "J$0", "J$1"})
    static final class C06501 extends ContinuationImpl {
        long J$0;
        long J$1;
        Object L$0;
        Object L$1;
        int label;
        /* synthetic */ Object result;

        C06501(Continuation<? super C06501> continuation) {
            super(continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            this.result = obj;
            this.label |= Integer.MIN_VALUE;
            return MeetingRepository.this.assignOverlapping(null, 0L, 0L, this);
        }
    }

    /* JADX INFO: renamed from: com.varun.pocketassistant.data.MeetingRepository$assignRecording$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: MeetingRepository.kt */
    @Metadata(k = 3, mv = {2, 2, 0}, xi = 48)
    @DebugMetadata(c = "com.varun.pocketassistant.data.MeetingRepository", f = "MeetingRepository.kt", i = {0, 0, 1, 1, 1, 1, 2, 2, 2, 2, 3, 3, 3, 3}, l = {135, 137, 139, 141}, m = "assignRecording", n = {AsrWorker.KEY_SEGMENT_ID, MeetingStageWorker.KEY_MEETING_ID, AsrWorker.KEY_SEGMENT_ID, MeetingStageWorker.KEY_MEETING_ID, "segment", "previousMeetingId", AsrWorker.KEY_SEGMENT_ID, MeetingStageWorker.KEY_MEETING_ID, "segment", "previousMeetingId", AsrWorker.KEY_SEGMENT_ID, MeetingStageWorker.KEY_MEETING_ID, "segment", "previousMeetingId"}, s = {"L$0", "L$1", "L$0", "L$1", "L$2", "L$3", "L$0", "L$1", "L$2", "L$3", "L$0", "L$1", "L$2", "L$3"})
    static final class C06511 extends ContinuationImpl {
        Object L$0;
        Object L$1;
        Object L$2;
        Object L$3;
        int label;
        /* synthetic */ Object result;

        C06511(Continuation<? super C06511> continuation) {
            super(continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            this.result = obj;
            this.label |= Integer.MIN_VALUE;
            return MeetingRepository.this.assignRecording(null, null, this);
        }
    }

    /* JADX INFO: renamed from: com.varun.pocketassistant.data.MeetingRepository$createMeeting$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: MeetingRepository.kt */
    @Metadata(k = 3, mv = {2, 2, 0}, xi = 48)
    @DebugMetadata(c = "com.varun.pocketassistant.data.MeetingRepository", f = "MeetingRepository.kt", i = {0, 0, 0, 0, 1, 1, 1, 1}, l = {101, LocationRequestCompat.QUALITY_BALANCED_POWER_ACCURACY}, m = "createMeeting", n = {"meeting", "startMs", "endMs", "now", "meeting", "startMs", "endMs", "now"}, s = {"L$0", "J$0", "J$1", "J$2", "L$0", "J$0", "J$1", "J$2"})
    static final class C06521 extends ContinuationImpl {
        long J$0;
        long J$1;
        long J$2;
        Object L$0;
        int label;
        /* synthetic */ Object result;

        C06521(Continuation<? super C06521> continuation) {
            super(continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            this.result = obj;
            this.label |= Integer.MIN_VALUE;
            return MeetingRepository.this.createMeeting(0L, 0L, this);
        }
    }

    /* JADX INFO: renamed from: com.varun.pocketassistant.data.MeetingRepository$deleteMeeting$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: MeetingRepository.kt */
    @Metadata(k = 3, mv = {2, 2, 0}, xi = 48)
    @DebugMetadata(c = "com.varun.pocketassistant.data.MeetingRepository", f = "MeetingRepository.kt", i = {0, 1}, l = {130, 131}, m = "deleteMeeting", n = {MeetingStageWorker.KEY_MEETING_ID, MeetingStageWorker.KEY_MEETING_ID}, s = {"L$0", "L$0"})
    static final class C06531 extends ContinuationImpl {
        Object L$0;
        int label;
        /* synthetic */ Object result;

        C06531(Continuation<? super C06531> continuation) {
            super(continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            this.result = obj;
            this.label |= Integer.MIN_VALUE;
            return MeetingRepository.this.deleteMeeting(null, this);
        }
    }

    /* JADX INFO: renamed from: com.varun.pocketassistant.data.MeetingRepository$deleteRecording$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: MeetingRepository.kt */
    @Metadata(k = 3, mv = {2, 2, 0}, xi = 48)
    @DebugMetadata(c = "com.varun.pocketassistant.data.MeetingRepository", f = "MeetingRepository.kt", i = {0, 1, 1, 1, 2, 2, 2}, l = {152, 154, 157}, m = "deleteRecording", n = {AsrWorker.KEY_SEGMENT_ID, AsrWorker.KEY_SEGMENT_ID, "segment", "previousMeetingId", AsrWorker.KEY_SEGMENT_ID, "segment", "previousMeetingId"}, s = {"L$0", "L$0", "L$1", "L$2", "L$0", "L$1", "L$2"})
    static final class C06541 extends ContinuationImpl {
        Object L$0;
        Object L$1;
        Object L$2;
        int label;
        /* synthetic */ Object result;

        C06541(Continuation<? super C06541> continuation) {
            super(continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            this.result = obj;
            this.label |= Integer.MIN_VALUE;
            return MeetingRepository.this.deleteRecording(null, this);
        }
    }

    /* JADX INFO: renamed from: com.varun.pocketassistant.data.MeetingRepository$detectMeetingProposals$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: MeetingRepository.kt */
    @Metadata(k = 3, mv = {2, 2, 0}, xi = 48)
    @DebugMetadata(c = "com.varun.pocketassistant.data.MeetingRepository", f = "MeetingRepository.kt", i = {0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1}, l = {29, MotionEventCompat.AXIS_GENERIC_9}, m = "detectMeetingProposals", n = {"refine", "dayStartMs", "dayEndMs", "refine", "segs", "coarse", "byId", "out", "p", "pSegs", "dayStartMs", "dayEndMs"}, s = {"L$0", "J$0", "J$1", "L$0", "L$1", "L$2", "L$3", "L$4", "L$6", "L$7", "J$0", "J$1"})
    static final class C06551 extends ContinuationImpl {
        long J$0;
        long J$1;
        Object L$0;
        Object L$1;
        Object L$2;
        Object L$3;
        Object L$4;
        Object L$5;
        Object L$6;
        Object L$7;
        Object L$8;
        int label;
        /* synthetic */ Object result;

        C06551(Continuation<? super C06551> continuation) {
            super(continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            this.result = obj;
            this.label |= Integer.MIN_VALUE;
            return MeetingRepository.this.detectMeetingProposals(0L, 0L, null, this);
        }
    }

    /* JADX INFO: renamed from: com.varun.pocketassistant.data.MeetingRepository$getMeetingsOverlapping$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: MeetingRepository.kt */
    @Metadata(k = 3, mv = {2, 2, 0}, xi = 48)
    @DebugMetadata(c = "com.varun.pocketassistant.data.MeetingRepository", f = "MeetingRepository.kt", i = {0, 0}, l = {69}, m = "getMeetingsOverlapping", n = {"startMs", "endMs"}, s = {"J$0", "J$1"})
    static final class C06561 extends ContinuationImpl {
        long J$0;
        long J$1;
        int label;
        /* synthetic */ Object result;

        C06561(Continuation<? super C06561> continuation) {
            super(continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            this.result = obj;
            this.label |= Integer.MIN_VALUE;
            return MeetingRepository.this.getMeetingsOverlapping(0L, 0L, this);
        }
    }

    /* JADX INFO: renamed from: com.varun.pocketassistant.data.MeetingRepository$markMeetingNeedsCleanup$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: MeetingRepository.kt */
    @Metadata(k = 3, mv = {2, 2, 0}, xi = 48)
    @DebugMetadata(c = "com.varun.pocketassistant.data.MeetingRepository", f = "MeetingRepository.kt", i = {0, 1, 1}, l = {191, 192}, m = "markMeetingNeedsCleanup", n = {MeetingStageWorker.KEY_MEETING_ID, MeetingStageWorker.KEY_MEETING_ID, "meeting"}, s = {"L$0", "L$0", "L$1"})
    static final class C06571 extends ContinuationImpl {
        Object L$0;
        Object L$1;
        int label;
        /* synthetic */ Object result;

        C06571(Continuation<? super C06571> continuation) {
            super(continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            this.result = obj;
            this.label |= Integer.MIN_VALUE;
            return MeetingRepository.this.markMeetingNeedsCleanup(null, this);
        }
    }

    /* JADX INFO: renamed from: com.varun.pocketassistant.data.MeetingRepository$onSegmentSpeakersConfirmed$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: MeetingRepository.kt */
    @Metadata(k = 3, mv = {2, 2, 0}, xi = 48)
    @DebugMetadata(c = "com.varun.pocketassistant.data.MeetingRepository", f = "MeetingRepository.kt", i = {0, 1, 1}, l = {186, 187}, m = "onSegmentSpeakersConfirmed", n = {AsrWorker.KEY_SEGMENT_ID, AsrWorker.KEY_SEGMENT_ID, MeetingStageWorker.KEY_MEETING_ID}, s = {"L$0", "L$0", "L$1"})
    static final class C06581 extends ContinuationImpl {
        Object L$0;
        Object L$1;
        int label;
        /* synthetic */ Object result;

        C06581(Continuation<? super C06581> continuation) {
            super(continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            this.result = obj;
            this.label |= Integer.MIN_VALUE;
            return MeetingRepository.this.onSegmentSpeakersConfirmed(null, this);
        }
    }

    /* JADX INFO: renamed from: com.varun.pocketassistant.data.MeetingRepository$previewOverlap$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: MeetingRepository.kt */
    @Metadata(k = 3, mv = {2, 2, 0}, xi = 48)
    @DebugMetadata(c = "com.varun.pocketassistant.data.MeetingRepository", f = "MeetingRepository.kt", i = {0, 0}, l = {73}, m = "previewOverlap", n = {"startMs", "endMs"}, s = {"J$0", "J$1"})
    static final class C06591 extends ContinuationImpl {
        long J$0;
        long J$1;
        int label;
        /* synthetic */ Object result;

        C06591(Continuation<? super C06591> continuation) {
            super(continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            this.result = obj;
            this.label |= Integer.MIN_VALUE;
            return MeetingRepository.this.previewOverlap(0L, 0L, this);
        }
    }

    /* JADX INFO: renamed from: com.varun.pocketassistant.data.MeetingRepository$recoverStuckCleaning$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: MeetingRepository.kt */
    @Metadata(k = 3, mv = {2, 2, 0}, xi = 48)
    @DebugMetadata(c = "com.varun.pocketassistant.data.MeetingRepository", f = "MeetingRepository.kt", i = {1, 1, 1}, l = {242, 245}, m = "recoverStuckCleaning", n = {"stuck", "meeting", "now"}, s = {"L$0", "L$2", "J$0"})
    static final class C06601 extends ContinuationImpl {
        long J$0;
        Object L$0;
        Object L$1;
        Object L$2;
        int label;
        /* synthetic */ Object result;

        C06601(Continuation<? super C06601> continuation) {
            super(continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            this.result = obj;
            this.label |= Integer.MIN_VALUE;
            return MeetingRepository.this.recoverStuckCleaning(this);
        }
    }

    /* JADX INFO: renamed from: com.varun.pocketassistant.data.MeetingRepository$requeueAllForCleanup$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: MeetingRepository.kt */
    @Metadata(k = 3, mv = {2, 2, 0}, xi = 48)
    @DebugMetadata(c = "com.varun.pocketassistant.data.MeetingRepository", f = "MeetingRepository.kt", i = {0, 1, 1, 1, 1, 1}, l = {219, 223}, m = "requeueAllForCleanup", n = {"limit", "meetings", "ids", "meeting", "limit", "now"}, s = {"I$0", "L$0", "L$1", "L$3", "I$0", "J$0"})
    static final class C06611 extends ContinuationImpl {
        int I$0;
        long J$0;
        Object L$0;
        Object L$1;
        Object L$2;
        Object L$3;
        int label;
        /* synthetic */ Object result;

        C06611(Continuation<? super C06611> continuation) {
            super(continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            this.result = obj;
            this.label |= Integer.MIN_VALUE;
            return MeetingRepository.this.requeueAllForCleanup(0, this);
        }
    }

    /* JADX INFO: renamed from: com.varun.pocketassistant.data.MeetingRepository$retryCleanup$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: MeetingRepository.kt */
    @Metadata(k = 3, mv = {2, 2, 0}, xi = 48)
    @DebugMetadata(c = "com.varun.pocketassistant.data.MeetingRepository", f = "MeetingRepository.kt", i = {0, 1, 1, 1}, l = {162, 165}, m = "retryCleanup", n = {MeetingStageWorker.KEY_MEETING_ID, MeetingStageWorker.KEY_MEETING_ID, "meeting", "keepClean"}, s = {"L$0", "L$0", "L$1", "I$0"})
    static final class C06621 extends ContinuationImpl {
        int I$0;
        Object L$0;
        Object L$1;
        int label;
        /* synthetic */ Object result;

        C06621(Continuation<? super C06621> continuation) {
            super(continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            this.result = obj;
            this.label |= Integer.MIN_VALUE;
            return MeetingRepository.this.retryCleanup(null, this);
        }
    }

    /* JADX INFO: renamed from: com.varun.pocketassistant.data.MeetingRepository$unassignRecording$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: MeetingRepository.kt */
    @Metadata(k = 3, mv = {2, 2, 0}, xi = 48)
    @DebugMetadata(c = "com.varun.pocketassistant.data.MeetingRepository", f = "MeetingRepository.kt", i = {0, 1, 1, 1, 2, 2, 2}, l = {145, 147, 148}, m = "unassignRecording", n = {AsrWorker.KEY_SEGMENT_ID, AsrWorker.KEY_SEGMENT_ID, "segment", "previousMeetingId", AsrWorker.KEY_SEGMENT_ID, "segment", "previousMeetingId"}, s = {"L$0", "L$0", "L$1", "L$2", "L$0", "L$1", "L$2"})
    static final class C06631 extends ContinuationImpl {
        Object L$0;
        Object L$1;
        Object L$2;
        int label;
        /* synthetic */ Object result;

        C06631(Continuation<? super C06631> continuation) {
            super(continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            this.result = obj;
            this.label |= Integer.MIN_VALUE;
            return MeetingRepository.this.unassignRecording(null, this);
        }
    }

    /* JADX INFO: renamed from: com.varun.pocketassistant.data.MeetingRepository$updateMeetingRange$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: MeetingRepository.kt */
    @Metadata(k = 3, mv = {2, 2, 0}, xi = 48)
    @DebugMetadata(c = "com.varun.pocketassistant.data.MeetingRepository", f = "MeetingRepository.kt", i = {0, 0, 0, 1, 1, 1, 1, 2, 2, 2, 2, 2, 3, 3, 3, 3, 3}, l = {109, 110, 123, 124}, m = "updateMeetingRange", n = {MeetingStageWorker.KEY_MEETING_ID, "startMs", "endMs", MeetingStageWorker.KEY_MEETING_ID, "current", "startMs", "endMs", MeetingStageWorker.KEY_MEETING_ID, "current", "updated", "startMs", "endMs", MeetingStageWorker.KEY_MEETING_ID, "current", "updated", "startMs", "endMs"}, s = {"L$0", "J$0", "J$1", "L$0", "L$1", "J$0", "J$1", "L$0", "L$1", "L$2", "J$0", "J$1", "L$0", "L$1", "L$2", "J$0", "J$1"})
    static final class C06641 extends ContinuationImpl {
        long J$0;
        long J$1;
        Object L$0;
        Object L$1;
        Object L$2;
        int label;
        /* synthetic */ Object result;

        C06641(Continuation<? super C06641> continuation) {
            super(continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            this.result = obj;
            this.label |= Integer.MIN_VALUE;
            return MeetingRepository.this.updateMeetingRange(null, 0L, 0L, this);
        }
    }

    public MeetingRepository(MeetingDao meetingDao, SegmentDao segmentDao) {
        Intrinsics.checkNotNullParameter(meetingDao, "meetingDao");
        Intrinsics.checkNotNullParameter(segmentDao, "segmentDao");
        this.meetingDao = meetingDao;
        this.segmentDao = segmentDao;
    }

    public final PipelineScheduler getPipelineScheduler() {
        return this.pipelineScheduler;
    }

    public final void setPipelineScheduler(PipelineScheduler pipelineScheduler) {
        this.pipelineScheduler = pipelineScheduler;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static /* synthetic */ Object detectMeetingProposals$default(MeetingRepository meetingRepository, long j, long j2, Function2 function2, Continuation continuation, int i, Object obj) {
        Function2 function3;
        if ((i & 4) == 0) {
            function3 = function2;
        } else {
            function3 = null;
        }
        return meetingRepository.detectMeetingProposals(j, j2, function3, continuation);
    }

    /* JADX WARN: Code duplicated, block: B:38:0x014d  */
    /* JADX WARN: Code duplicated, block: B:41:0x0176  */
    /* JADX WARN: Code duplicated, block: B:43:0x018e  */
    /* JADX WARN: Code duplicated, block: B:44:0x019a  */
    /* JADX WARN: Code duplicated, block: B:48:0x01ba A[LOOP:0: B:36:0x0147->B:48:0x01ba, LOOP_END] */
    /* JADX WARN: Code duplicated, block: B:51:0x01f9 A[RETURN] */
    /* JADX WARN: Code duplicated, block: B:52:0x01fa  */
    /* JADX WARN: Code duplicated, block: B:55:0x021a  */
    /* JADX WARN: Code duplicated, block: B:59:0x01c7 A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:7:0x0018  */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:52:0x01fa -> B:53:0x0212). Please report as a decompilation issue!!! */
    /*  JADX ERROR: JadxOverflowException in pass: RegionMakerVisitor
        jadx.core.utils.exceptions.JadxOverflowException: Regions stack size limit reached
        	at jadx.core.utils.ErrorsCounter.addError(ErrorsCounter.java:59)
        	at jadx.core.utils.ErrorsCounter.error(ErrorsCounter.java:31)
        	at jadx.core.dex.attributes.nodes.NotificationAttrNode.addError(NotificationAttrNode.java:19)
        */
    public final java.lang.Object detectMeetingProposals(long r29, long r31, kotlin.jvm.functions.Function2<? super java.lang.String, ? super kotlin.coroutines.Continuation<? super java.lang.String>, ? extends java.lang.Object> r33, kotlin.coroutines.Continuation<? super java.util.List<com.varun.pocketassistant.meeting.GapClusterer.Proposal>> r34) {
        /*
            Method dump skipped, instruction units count: 576
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.varun.pocketassistant.data.MeetingRepository.detectMeetingProposals(long, long, kotlin.jvm.functions.Function2, kotlin.coroutines.Continuation):java.lang.Object");
    }

    public final Flow<List<MeetingEntity>> observeMeetings() {
        return this.meetingDao.observeAll();
    }

    public final Flow<MeetingEntity> observeMeeting(String id) {
        Intrinsics.checkNotNullParameter(id, "id");
        return this.meetingDao.observeById(id);
    }

    public final Flow<List<MeetingEntity>> searchMeetings(String query) {
        Intrinsics.checkNotNullParameter(query, "query");
        String q = StringsKt.trim((CharSequence) query).toString();
        return q.length() == 0 ? this.meetingDao.observeAll() : this.meetingDao.search(q);
    }

    public final Flow<List<SegmentEntity>> observeMeetingRecordings(String meetingId) {
        Intrinsics.checkNotNullParameter(meetingId, "meetingId");
        return this.segmentDao.observeForMeeting(meetingId);
    }

    public final Object getMeeting(String id, Continuation<? super MeetingEntity> continuation) {
        return this.meetingDao.getById(id, continuation);
    }

    public final Object getRecordings(String meetingId, Continuation<? super List<SegmentEntity>> continuation) {
        return this.segmentDao.getForMeeting(meetingId, continuation);
    }

    public final Object getSegmentsOverlapping(long startMs, long endMs, Continuation<? super List<SegmentEntity>> continuation) {
        return this.segmentDao.getOverlapping(startMs, endMs, continuation);
    }

    /* JADX WARN: Code duplicated, block: B:7:0x0018  */
    public final Object getMeetingsOverlapping(long startMs, long endMs, Continuation<? super List<MeetingEntity>> continuation) {
        C06561 c06561;
        long startMs2;
        Object obj;
        long endMs2;
        if (continuation instanceof C06561) {
            c06561 = (C06561) continuation;
            if ((c06561.label & Integer.MIN_VALUE) != 0) {
                c06561.label -= Integer.MIN_VALUE;
            } else {
                c06561 = new C06561(continuation);
            }
        } else {
            c06561 = new C06561(continuation);
        }
        Object $result = c06561.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (c06561.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                MeetingDao meetingDao = this.meetingDao;
                startMs2 = startMs;
                c06561.J$0 = startMs2;
                c06561.J$1 = endMs;
                c06561.label = 1;
                Object all = meetingDao.getAll(500, c06561);
                if (all == coroutine_suspended) {
                    return coroutine_suspended;
                }
                obj = all;
                endMs2 = endMs;
                break;
                break;
            case 1:
                endMs2 = c06561.J$1;
                startMs2 = c06561.J$0;
                ResultKt.throwOnFailure($result);
                obj = $result;
                break;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
        Collection arrayList = new ArrayList();
        for (Object obj2 : (Iterable) obj) {
            MeetingEntity meetingEntity = (MeetingEntity) obj2;
            if (meetingEntity.getStartedAtMs() < endMs2 && meetingEntity.getEndedAtMs() > startMs2) {
                arrayList.add(obj2);
            }
        }
        return CollectionsKt.sortedWith((List) arrayList, new Comparator() { // from class: com.varun.pocketassistant.data.MeetingRepository$getMeetingsOverlapping$$inlined$sortedBy$1
            /* JADX WARN: Multi-variable type inference failed */
            @Override // java.util.Comparator
            public final int compare(T t, T t2) {
                return ComparisonsKt.compareValues(Long.valueOf(((MeetingEntity) t).getStartedAtMs()), Long.valueOf(((MeetingEntity) t2).getStartedAtMs()));
            }
        });
    }

    /* JADX WARN: Code duplicated, block: B:7:0x0018  */
    public final Object previewOverlap(long startMs, long endMs, Continuation<? super OverlapPreview> continuation) {
        C06591 c06591;
        Object obj;
        if (continuation instanceof C06591) {
            c06591 = (C06591) continuation;
            if ((c06591.label & Integer.MIN_VALUE) != 0) {
                c06591.label -= Integer.MIN_VALUE;
            } else {
                c06591 = new C06591(continuation);
            }
        } else {
            c06591 = new C06591(continuation);
        }
        C06591 c06592 = c06591;
        Object $result = c06592.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (c06592.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                SegmentDao segmentDao = this.segmentDao;
                c06592.J$0 = startMs;
                c06592.J$1 = endMs;
                c06592.label = 1;
                Object overlapping = segmentDao.getOverlapping(startMs, endMs, c06592);
                if (overlapping == coroutine_suspended) {
                    return coroutine_suspended;
                }
                obj = overlapping;
                break;
                break;
            case 1:
                long j = c06592.J$1;
                long j2 = c06592.J$0;
                ResultKt.throwOnFailure($result);
                obj = $result;
                break;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
        Collection arrayList = new ArrayList();
        for (Object obj2 : (Iterable) obj) {
            if (INSTANCE.isAssignableRecording((SegmentEntity) obj2)) {
                arrayList.add(obj2);
            }
        }
        List segs = (List) arrayList;
        List list = segs;
        Collection arrayList2 = new ArrayList();
        for (Object obj3 : list) {
            Iterable iterable = list;
            if (Intrinsics.areEqual(((SegmentEntity) obj3).getTranscriptStatus(), "READY")) {
                arrayList2.add(obj3);
            }
            list = iterable;
        }
        List ready = (List) arrayList2;
        List list2 = segs;
        Collection arrayList3 = new ArrayList();
        for (Object obj4 : list2) {
            SegmentEntity segmentEntity = (SegmentEntity) obj4;
            List ready2 = ready;
            Iterable iterable2 = list2;
            if (Intrinsics.areEqual(segmentEntity.getTranscriptStatus(), "PENDING") || Intrinsics.areEqual(segmentEntity.getTranscriptStatus(), "PROCESSING")) {
                arrayList3.add(obj4);
            }
            ready = ready2;
            list2 = iterable2;
        }
        List ready3 = ready;
        List pendingAsr = (List) arrayList3;
        int size = segs.size();
        Iterator it = segs.iterator();
        long durationMs = 0;
        while (it.hasNext()) {
            durationMs += ((SegmentEntity) it.next()).getDurationMs();
        }
        int size2 = ready3.size();
        Iterator it2 = ready3.iterator();
        long durationMs2 = 0;
        while (it2.hasNext()) {
            durationMs2 += ((SegmentEntity) it2.next()).getDurationMs();
        }
        return new OverlapPreview(segs, size, durationMs, size2, durationMs2, pendingAsr.size());
    }

    /* JADX WARN: Code duplicated, block: B:25:0x00d6 A[RETURN] */
    /* JADX WARN: Code duplicated, block: B:26:0x00d7  */
    /* JADX WARN: Code duplicated, block: B:29:0x00de  */
    /* JADX WARN: Code duplicated, block: B:7:0x0018  */
    public final Object createMeeting(long startMs, long endMs, Continuation<? super MeetingEntity> continuation) {
        C06521 c06521;
        long now;
        long startMs2;
        long endMs2;
        C06521 c06522;
        Object obj;
        MeetingEntity meeting;
        String id;
        long endMs3;
        Continuation<? super Unit> $continuation;
        PipelineScheduler pipelineScheduler;
        if (continuation instanceof C06521) {
            c06521 = (C06521) continuation;
            if ((c06521.label & Integer.MIN_VALUE) != 0) {
                c06521.label -= Integer.MIN_VALUE;
            } else {
                c06521 = new C06521(continuation);
            }
        } else {
            c06521 = new C06521(continuation);
        }
        Object $result = c06521.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (c06521.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                if (!(endMs > startMs)) {
                    throw new IllegalArgumentException("Meeting end must be after start".toString());
                }
                now = System.currentTimeMillis();
                String string = UUID.randomUUID().toString();
                Intrinsics.checkNotNullExpressionValue(string, "toString(...)");
                C06521 c06523 = c06521;
                startMs2 = startMs;
                endMs2 = endMs;
                MeetingEntity meeting2 = new MeetingEntity(string, null, startMs2, endMs2, "PENDING_CLEANUP", null, null, null, now, null, null, now, 1762, null);
                MeetingDao meetingDao = this.meetingDao;
                c06522 = c06523;
                c06522.L$0 = meeting2;
                c06522.J$0 = startMs2;
                c06522.J$1 = endMs2;
                c06522.J$2 = now;
                c06522.label = 1;
                obj = coroutine_suspended;
                if (meetingDao.insert(meeting2, c06522) == obj) {
                    return obj;
                }
                meeting = meeting2;
                id = meeting.getId();
                c06522.L$0 = meeting;
                c06522.J$0 = startMs2;
                c06522.J$1 = endMs2;
                c06522.J$2 = now;
                c06522.label = 2;
                endMs3 = endMs2;
                $continuation = c06522;
                if (assignOverlapping(id, startMs2, endMs3, $continuation) == obj) {
                    return obj;
                }
                pipelineScheduler = this.pipelineScheduler;
                if (pipelineScheduler != null) {
                    PipelineScheduler.enqueueMeeting$default(pipelineScheduler, meeting.getId(), false, false, 6, null);
                }
                return meeting;
            case 1:
                long now2 = c06521.J$2;
                long endMs4 = c06521.J$1;
                long startMs3 = c06521.J$0;
                MeetingEntity meeting3 = (MeetingEntity) c06521.L$0;
                ResultKt.throwOnFailure($result);
                now = now2;
                endMs2 = endMs4;
                c06522 = c06521;
                obj = coroutine_suspended;
                startMs2 = startMs3;
                meeting = meeting3;
                id = meeting.getId();
                c06522.L$0 = meeting;
                c06522.J$0 = startMs2;
                c06522.J$1 = endMs2;
                c06522.J$2 = now;
                c06522.label = 2;
                endMs3 = endMs2;
                $continuation = c06522;
                if (assignOverlapping(id, startMs2, endMs3, $continuation) == obj) {
                    return obj;
                }
                pipelineScheduler = this.pipelineScheduler;
                if (pipelineScheduler != null) {
                    PipelineScheduler.enqueueMeeting$default(pipelineScheduler, meeting.getId(), false, false, 6, null);
                }
                return meeting;
            case 2:
                long j = c06521.J$2;
                long j2 = c06521.J$1;
                long j3 = c06521.J$0;
                meeting = (MeetingEntity) c06521.L$0;
                ResultKt.throwOnFailure($result);
                pipelineScheduler = this.pipelineScheduler;
                if (pipelineScheduler != null) {
                    PipelineScheduler.enqueueMeeting$default(pipelineScheduler, meeting.getId(), false, false, 6, null);
                }
                return meeting;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
    }

    /* JADX WARN: Code duplicated, block: B:26:0x00a6 A[RETURN] */
    /* JADX WARN: Code duplicated, block: B:28:0x00a8  */
    /* JADX WARN: Code duplicated, block: B:30:0x00bb A[RETURN] */
    /* JADX WARN: Code duplicated, block: B:31:0x00bc  */
    /* JADX WARN: Code duplicated, block: B:34:0x0100 A[RETURN] */
    /* JADX WARN: Code duplicated, block: B:35:0x0101  */
    /* JADX WARN: Code duplicated, block: B:38:0x011b A[RETURN] */
    /* JADX WARN: Code duplicated, block: B:39:0x011c  */
    /* JADX WARN: Code duplicated, block: B:42:0x0123  */
    /* JADX WARN: Code duplicated, block: B:7:0x001e  */
    public final Object updateMeetingRange(String meetingId, long startMs, long endMs, Continuation<? super MeetingEntity> continuation) {
        C06641 c06641;
        Object byId;
        MeetingEntity current;
        SegmentDao segmentDao;
        long startMs2;
        long endMs2;
        MeetingEntity updated;
        long endMs3;
        MeetingDao meetingDao;
        MeetingEntity current2;
        MeetingEntity current3;
        long startMs3;
        MeetingEntity updated2;
        String meetingId2;
        PipelineScheduler pipelineScheduler;
        String meetingId3 = meetingId;
        long startMs4 = startMs;
        long endMs4 = endMs;
        if (continuation instanceof C06641) {
            c06641 = (C06641) continuation;
            if ((c06641.label & Integer.MIN_VALUE) != 0) {
                c06641.label -= Integer.MIN_VALUE;
            } else {
                c06641 = new C06641(continuation);
            }
        } else {
            c06641 = new C06641(continuation);
        }
        Object $result = c06641.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (c06641.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                if (!(endMs4 > startMs4)) {
                    throw new IllegalArgumentException("Meeting end must be after start".toString());
                }
                MeetingDao meetingDao2 = this.meetingDao;
                c06641.L$0 = meetingId3;
                c06641.J$0 = startMs4;
                c06641.J$1 = endMs4;
                c06641.label = 1;
                byId = meetingDao2.getById(meetingId3, c06641);
                if (byId == coroutine_suspended) {
                    return coroutine_suspended;
                }
                current = (MeetingEntity) byId;
                if (current == null) {
                    return null;
                }
                segmentDao = this.segmentDao;
                c06641.L$0 = meetingId3;
                c06641.L$1 = current;
                c06641.J$0 = startMs4;
                c06641.J$1 = endMs4;
                c06641.label = 2;
                if (segmentDao.clearMeeting(meetingId3, c06641) == coroutine_suspended) {
                    return coroutine_suspended;
                }
                startMs2 = startMs4;
                endMs2 = endMs4;
                updated = MeetingEntity.copy$default(current, null, null, startMs2, endMs2, "PENDING_CLEANUP", null, null, null, 0L, null, null, System.currentTimeMillis(), InputDeviceCompat.SOURCE_KEYBOARD, null);
                endMs3 = endMs2;
                meetingDao = this.meetingDao;
                c06641.L$0 = meetingId3;
                c06641.L$1 = SpillingKt.nullOutSpilledVariable(current);
                c06641.L$2 = updated;
                c06641.J$0 = startMs2;
                c06641.J$1 = endMs3;
                c06641.label = 3;
                if (meetingDao.update(updated, c06641) == coroutine_suspended) {
                    return coroutine_suspended;
                }
                current2 = current;
                current3 = updated;
                startMs3 = startMs2;
                c06641.L$0 = meetingId3;
                c06641.L$1 = SpillingKt.nullOutSpilledVariable(current2);
                c06641.L$2 = current3;
                c06641.J$0 = startMs3;
                c06641.J$1 = endMs3;
                c06641.label = 4;
                if (assignOverlapping(meetingId3, startMs3, endMs3, c06641) == coroutine_suspended) {
                    return coroutine_suspended;
                }
                updated2 = current3;
                meetingId2 = meetingId3;
                pipelineScheduler = this.pipelineScheduler;
                if (pipelineScheduler != null) {
                    PipelineScheduler.enqueueMeeting$default(pipelineScheduler, meetingId2, true, false, 4, null);
                }
                return updated2;
            case 1:
                endMs4 = c06641.J$1;
                startMs4 = c06641.J$0;
                meetingId3 = (String) c06641.L$0;
                ResultKt.throwOnFailure($result);
                byId = $result;
                current = (MeetingEntity) byId;
                if (current == null) {
                    return null;
                }
                segmentDao = this.segmentDao;
                c06641.L$0 = meetingId3;
                c06641.L$1 = current;
                c06641.J$0 = startMs4;
                c06641.J$1 = endMs4;
                c06641.label = 2;
                if (segmentDao.clearMeeting(meetingId3, c06641) == coroutine_suspended) {
                    return coroutine_suspended;
                }
                startMs2 = startMs4;
                endMs2 = endMs4;
                updated = MeetingEntity.copy$default(current, null, null, startMs2, endMs2, "PENDING_CLEANUP", null, null, null, 0L, null, null, System.currentTimeMillis(), InputDeviceCompat.SOURCE_KEYBOARD, null);
                endMs3 = endMs2;
                meetingDao = this.meetingDao;
                c06641.L$0 = meetingId3;
                c06641.L$1 = SpillingKt.nullOutSpilledVariable(current);
                c06641.L$2 = updated;
                c06641.J$0 = startMs2;
                c06641.J$1 = endMs3;
                c06641.label = 3;
                if (meetingDao.update(updated, c06641) == coroutine_suspended) {
                    return coroutine_suspended;
                }
                current2 = current;
                current3 = updated;
                startMs3 = startMs2;
                c06641.L$0 = meetingId3;
                c06641.L$1 = SpillingKt.nullOutSpilledVariable(current2);
                c06641.L$2 = current3;
                c06641.J$0 = startMs3;
                c06641.J$1 = endMs3;
                c06641.label = 4;
                if (assignOverlapping(meetingId3, startMs3, endMs3, c06641) == coroutine_suspended) {
                    return coroutine_suspended;
                }
                updated2 = current3;
                meetingId2 = meetingId3;
                pipelineScheduler = this.pipelineScheduler;
                if (pipelineScheduler != null) {
                    PipelineScheduler.enqueueMeeting$default(pipelineScheduler, meetingId2, true, false, 4, null);
                }
                return updated2;
            case 2:
                long endMs5 = c06641.J$1;
                long startMs5 = c06641.J$0;
                current = (MeetingEntity) c06641.L$1;
                meetingId3 = (String) c06641.L$0;
                ResultKt.throwOnFailure($result);
                startMs2 = startMs5;
                endMs2 = endMs5;
                updated = MeetingEntity.copy$default(current, null, null, startMs2, endMs2, "PENDING_CLEANUP", null, null, null, 0L, null, null, System.currentTimeMillis(), InputDeviceCompat.SOURCE_KEYBOARD, null);
                endMs3 = endMs2;
                meetingDao = this.meetingDao;
                c06641.L$0 = meetingId3;
                c06641.L$1 = SpillingKt.nullOutSpilledVariable(current);
                c06641.L$2 = updated;
                c06641.J$0 = startMs2;
                c06641.J$1 = endMs3;
                c06641.label = 3;
                if (meetingDao.update(updated, c06641) == coroutine_suspended) {
                    return coroutine_suspended;
                }
                current2 = current;
                current3 = updated;
                startMs3 = startMs2;
                c06641.L$0 = meetingId3;
                c06641.L$1 = SpillingKt.nullOutSpilledVariable(current2);
                c06641.L$2 = current3;
                c06641.J$0 = startMs3;
                c06641.J$1 = endMs3;
                c06641.label = 4;
                if (assignOverlapping(meetingId3, startMs3, endMs3, c06641) == coroutine_suspended) {
                    return coroutine_suspended;
                }
                updated2 = current3;
                meetingId2 = meetingId3;
                pipelineScheduler = this.pipelineScheduler;
                if (pipelineScheduler != null) {
                    PipelineScheduler.enqueueMeeting$default(pipelineScheduler, meetingId2, true, false, 4, null);
                }
                return updated2;
            case 3:
                endMs3 = c06641.J$1;
                startMs3 = c06641.J$0;
                current3 = (MeetingEntity) c06641.L$2;
                current2 = (MeetingEntity) c06641.L$1;
                meetingId3 = (String) c06641.L$0;
                ResultKt.throwOnFailure($result);
                c06641.L$0 = meetingId3;
                c06641.L$1 = SpillingKt.nullOutSpilledVariable(current2);
                c06641.L$2 = current3;
                c06641.J$0 = startMs3;
                c06641.J$1 = endMs3;
                c06641.label = 4;
                if (assignOverlapping(meetingId3, startMs3, endMs3, c06641) == coroutine_suspended) {
                    return coroutine_suspended;
                }
                updated2 = current3;
                meetingId2 = meetingId3;
                pipelineScheduler = this.pipelineScheduler;
                if (pipelineScheduler != null) {
                    PipelineScheduler.enqueueMeeting$default(pipelineScheduler, meetingId2, true, false, 4, null);
                }
                return updated2;
            case 4:
                long j = c06641.J$1;
                long j2 = c06641.J$0;
                updated2 = (MeetingEntity) c06641.L$2;
                String meetingId4 = (String) c06641.L$0;
                ResultKt.throwOnFailure($result);
                meetingId2 = meetingId4;
                pipelineScheduler = this.pipelineScheduler;
                if (pipelineScheduler != null) {
                    PipelineScheduler.enqueueMeeting$default(pipelineScheduler, meetingId2, true, false, 4, null);
                }
                return updated2;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
    }

    /* JADX WARN: Code duplicated, block: B:19:0x0060 A[RETURN] */
    /* JADX WARN: Code duplicated, block: B:7:0x0014  */
    public final Object deleteMeeting(String meetingId, Continuation<? super Unit> continuation) {
        C06531 c06531;
        MeetingDao meetingDao;
        if (continuation instanceof C06531) {
            c06531 = (C06531) continuation;
            if ((c06531.label & Integer.MIN_VALUE) != 0) {
                c06531.label -= Integer.MIN_VALUE;
            } else {
                c06531 = new C06531(continuation);
            }
        } else {
            c06531 = new C06531(continuation);
        }
        Object $result = c06531.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (c06531.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                SegmentDao segmentDao = this.segmentDao;
                c06531.L$0 = meetingId;
                c06531.label = 1;
                if (segmentDao.clearMeeting(meetingId, c06531) == coroutine_suspended) {
                    return coroutine_suspended;
                }
                meetingDao = this.meetingDao;
                c06531.L$0 = SpillingKt.nullOutSpilledVariable(meetingId);
                c06531.label = 2;
                if (meetingDao.delete(meetingId, c06531) == coroutine_suspended) {
                    return coroutine_suspended;
                }
                return Unit.INSTANCE;
            case 1:
                meetingId = (String) c06531.L$0;
                ResultKt.throwOnFailure($result);
                meetingDao = this.meetingDao;
                c06531.L$0 = SpillingKt.nullOutSpilledVariable(meetingId);
                c06531.label = 2;
                if (meetingDao.delete(meetingId, c06531) == coroutine_suspended) {
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

    /* JADX WARN: Code duplicated, block: B:21:0x0096  */
    /* JADX WARN: Code duplicated, block: B:23:0x0099  */
    /* JADX WARN: Code duplicated, block: B:25:0x00b8 A[RETURN] */
    /* JADX WARN: Code duplicated, block: B:26:0x00b9  */
    /* JADX WARN: Code duplicated, block: B:28:0x00be  */
    /* JADX WARN: Code duplicated, block: B:32:0x00e1 A[RETURN] */
    /* JADX WARN: Code duplicated, block: B:36:0x0104 A[RETURN] */
    /* JADX WARN: Code duplicated, block: B:37:0x0105  */
    /* JADX WARN: Code duplicated, block: B:7:0x0014  */
    public final Object assignRecording(String segmentId, String meetingId, Continuation<? super Unit> continuation) {
        C06511 c06511;
        Object byId;
        SegmentEntity segment;
        String previousMeetingId;
        SegmentDao segmentDao;
        SegmentEntity segment2;
        String previousMeetingId2;
        if (continuation instanceof C06511) {
            c06511 = (C06511) continuation;
            if ((c06511.label & Integer.MIN_VALUE) != 0) {
                c06511.label -= Integer.MIN_VALUE;
            } else {
                c06511 = new C06511(continuation);
            }
        } else {
            c06511 = new C06511(continuation);
        }
        Object $result = c06511.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (c06511.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                SegmentDao segmentDao2 = this.segmentDao;
                c06511.L$0 = segmentId;
                c06511.L$1 = meetingId;
                c06511.label = 1;
                byId = segmentDao2.getById(segmentId, c06511);
                if (byId == coroutine_suspended) {
                    return coroutine_suspended;
                }
                segment = (SegmentEntity) byId;
                if (segment == null) {
                    return Unit.INSTANCE;
                }
                previousMeetingId = segment.getMeetingId();
                segmentDao = this.segmentDao;
                c06511.L$0 = SpillingKt.nullOutSpilledVariable(segmentId);
                c06511.L$1 = meetingId;
                c06511.L$2 = SpillingKt.nullOutSpilledVariable(segment);
                c06511.L$3 = previousMeetingId;
                c06511.label = 2;
                if (segmentDao.setMeetingId(segmentId, meetingId, c06511) == coroutine_suspended) {
                    return coroutine_suspended;
                }
                segment2 = segment;
                previousMeetingId2 = previousMeetingId;
                if (previousMeetingId2 != null && !Intrinsics.areEqual(previousMeetingId2, meetingId)) {
                    c06511.L$0 = SpillingKt.nullOutSpilledVariable(segmentId);
                    c06511.L$1 = meetingId;
                    c06511.L$2 = SpillingKt.nullOutSpilledVariable(segment2);
                    c06511.L$3 = SpillingKt.nullOutSpilledVariable(previousMeetingId2);
                    c06511.label = 3;
                    if (markMeetingNeedsCleanup(previousMeetingId2, c06511) == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                }
                c06511.L$0 = SpillingKt.nullOutSpilledVariable(segmentId);
                c06511.L$1 = SpillingKt.nullOutSpilledVariable(meetingId);
                c06511.L$2 = SpillingKt.nullOutSpilledVariable(segment2);
                c06511.L$3 = SpillingKt.nullOutSpilledVariable(previousMeetingId2);
                c06511.label = 4;
                if (markMeetingNeedsCleanup(meetingId, c06511) == coroutine_suspended) {
                    return coroutine_suspended;
                }
                return Unit.INSTANCE;
            case 1:
                meetingId = (String) c06511.L$1;
                segmentId = (String) c06511.L$0;
                ResultKt.throwOnFailure($result);
                byId = $result;
                segment = (SegmentEntity) byId;
                if (segment == null) {
                    return Unit.INSTANCE;
                }
                previousMeetingId = segment.getMeetingId();
                segmentDao = this.segmentDao;
                c06511.L$0 = SpillingKt.nullOutSpilledVariable(segmentId);
                c06511.L$1 = meetingId;
                c06511.L$2 = SpillingKt.nullOutSpilledVariable(segment);
                c06511.L$3 = previousMeetingId;
                c06511.label = 2;
                if (segmentDao.setMeetingId(segmentId, meetingId, c06511) == coroutine_suspended) {
                    return coroutine_suspended;
                }
                segment2 = segment;
                previousMeetingId2 = previousMeetingId;
                if (previousMeetingId2 != null) {
                    c06511.L$0 = SpillingKt.nullOutSpilledVariable(segmentId);
                    c06511.L$1 = meetingId;
                    c06511.L$2 = SpillingKt.nullOutSpilledVariable(segment2);
                    c06511.L$3 = SpillingKt.nullOutSpilledVariable(previousMeetingId2);
                    c06511.label = 3;
                    if (markMeetingNeedsCleanup(previousMeetingId2, c06511) == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                }
                c06511.L$0 = SpillingKt.nullOutSpilledVariable(segmentId);
                c06511.L$1 = SpillingKt.nullOutSpilledVariable(meetingId);
                c06511.L$2 = SpillingKt.nullOutSpilledVariable(segment2);
                c06511.L$3 = SpillingKt.nullOutSpilledVariable(previousMeetingId2);
                c06511.label = 4;
                if (markMeetingNeedsCleanup(meetingId, c06511) == coroutine_suspended) {
                    return coroutine_suspended;
                }
                return Unit.INSTANCE;
            case 2:
                previousMeetingId2 = (String) c06511.L$3;
                segment2 = (SegmentEntity) c06511.L$2;
                meetingId = (String) c06511.L$1;
                segmentId = (String) c06511.L$0;
                ResultKt.throwOnFailure($result);
                if (previousMeetingId2 != null) {
                    c06511.L$0 = SpillingKt.nullOutSpilledVariable(segmentId);
                    c06511.L$1 = meetingId;
                    c06511.L$2 = SpillingKt.nullOutSpilledVariable(segment2);
                    c06511.L$3 = SpillingKt.nullOutSpilledVariable(previousMeetingId2);
                    c06511.label = 3;
                    if (markMeetingNeedsCleanup(previousMeetingId2, c06511) == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                }
                c06511.L$0 = SpillingKt.nullOutSpilledVariable(segmentId);
                c06511.L$1 = SpillingKt.nullOutSpilledVariable(meetingId);
                c06511.L$2 = SpillingKt.nullOutSpilledVariable(segment2);
                c06511.L$3 = SpillingKt.nullOutSpilledVariable(previousMeetingId2);
                c06511.label = 4;
                if (markMeetingNeedsCleanup(meetingId, c06511) == coroutine_suspended) {
                    return coroutine_suspended;
                }
                return Unit.INSTANCE;
            case 3:
                previousMeetingId2 = (String) c06511.L$3;
                segment2 = (SegmentEntity) c06511.L$2;
                meetingId = (String) c06511.L$1;
                segmentId = (String) c06511.L$0;
                ResultKt.throwOnFailure($result);
                c06511.L$0 = SpillingKt.nullOutSpilledVariable(segmentId);
                c06511.L$1 = SpillingKt.nullOutSpilledVariable(meetingId);
                c06511.L$2 = SpillingKt.nullOutSpilledVariable(segment2);
                c06511.L$3 = SpillingKt.nullOutSpilledVariable(previousMeetingId2);
                c06511.label = 4;
                if (markMeetingNeedsCleanup(meetingId, c06511) == coroutine_suspended) {
                    return coroutine_suspended;
                }
                return Unit.INSTANCE;
            case 4:
                ResultKt.throwOnFailure($result);
                return Unit.INSTANCE;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
    }

    /* JADX WARN: Code duplicated, block: B:28:0x0093 A[RETURN] */
    /* JADX WARN: Code duplicated, block: B:29:0x0094  */
    /* JADX WARN: Code duplicated, block: B:32:0x00b2 A[RETURN] */
    /* JADX WARN: Code duplicated, block: B:33:0x00b3  */
    /* JADX WARN: Code duplicated, block: B:7:0x0014  */
    public final Object unassignRecording(String segmentId, Continuation<? super Unit> continuation) {
        C06631 c06631;
        Object byId;
        SegmentEntity segment;
        String previousMeetingId;
        SegmentDao segmentDao;
        SegmentEntity segment2;
        String previousMeetingId2;
        if (continuation instanceof C06631) {
            c06631 = (C06631) continuation;
            if ((c06631.label & Integer.MIN_VALUE) != 0) {
                c06631.label -= Integer.MIN_VALUE;
            } else {
                c06631 = new C06631(continuation);
            }
        } else {
            c06631 = new C06631(continuation);
        }
        Object $result = c06631.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (c06631.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                SegmentDao segmentDao2 = this.segmentDao;
                c06631.L$0 = segmentId;
                c06631.label = 1;
                byId = segmentDao2.getById(segmentId, c06631);
                if (byId == coroutine_suspended) {
                    return coroutine_suspended;
                }
                segment = (SegmentEntity) byId;
                if (segment == null && (previousMeetingId = segment.getMeetingId()) != null) {
                    segmentDao = this.segmentDao;
                    c06631.L$0 = SpillingKt.nullOutSpilledVariable(segmentId);
                    c06631.L$1 = SpillingKt.nullOutSpilledVariable(segment);
                    c06631.L$2 = previousMeetingId;
                    c06631.label = 2;
                    if (segmentDao.clearMeetingId(segmentId, c06631) == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    segment2 = segment;
                    previousMeetingId2 = previousMeetingId;
                    c06631.L$0 = SpillingKt.nullOutSpilledVariable(segmentId);
                    c06631.L$1 = SpillingKt.nullOutSpilledVariable(segment2);
                    c06631.L$2 = SpillingKt.nullOutSpilledVariable(previousMeetingId2);
                    c06631.label = 3;
                    if (markMeetingNeedsCleanup(previousMeetingId2, c06631) == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    return Unit.INSTANCE;
                }
                return Unit.INSTANCE;
            case 1:
                segmentId = (String) c06631.L$0;
                ResultKt.throwOnFailure($result);
                byId = $result;
                segment = (SegmentEntity) byId;
                if (segment == null) {
                    return Unit.INSTANCE;
                }
                segmentDao = this.segmentDao;
                c06631.L$0 = SpillingKt.nullOutSpilledVariable(segmentId);
                c06631.L$1 = SpillingKt.nullOutSpilledVariable(segment);
                c06631.L$2 = previousMeetingId;
                c06631.label = 2;
                if (segmentDao.clearMeetingId(segmentId, c06631) == coroutine_suspended) {
                    return coroutine_suspended;
                }
                segment2 = segment;
                previousMeetingId2 = previousMeetingId;
                c06631.L$0 = SpillingKt.nullOutSpilledVariable(segmentId);
                c06631.L$1 = SpillingKt.nullOutSpilledVariable(segment2);
                c06631.L$2 = SpillingKt.nullOutSpilledVariable(previousMeetingId2);
                c06631.label = 3;
                if (markMeetingNeedsCleanup(previousMeetingId2, c06631) == coroutine_suspended) {
                    return coroutine_suspended;
                }
                return Unit.INSTANCE;
            case 2:
                previousMeetingId2 = (String) c06631.L$2;
                segment2 = (SegmentEntity) c06631.L$1;
                segmentId = (String) c06631.L$0;
                ResultKt.throwOnFailure($result);
                c06631.L$0 = SpillingKt.nullOutSpilledVariable(segmentId);
                c06631.L$1 = SpillingKt.nullOutSpilledVariable(segment2);
                c06631.L$2 = SpillingKt.nullOutSpilledVariable(previousMeetingId2);
                c06631.label = 3;
                if (markMeetingNeedsCleanup(previousMeetingId2, c06631) == coroutine_suspended) {
                    return coroutine_suspended;
                }
                return Unit.INSTANCE;
            case 3:
                ResultKt.throwOnFailure($result);
                return Unit.INSTANCE;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
    }

    /* JADX WARN: Code duplicated, block: B:20:0x006e  */
    /* JADX WARN: Code duplicated, block: B:22:0x0071  */
    /* JADX WARN: Code duplicated, block: B:24:0x008a A[RETURN] */
    /* JADX WARN: Code duplicated, block: B:25:0x008b  */
    /* JADX WARN: Code duplicated, block: B:31:0x00b5  */
    /* JADX WARN: Code duplicated, block: B:33:0x00d0 A[RETURN] */
    /* JADX WARN: Code duplicated, block: B:34:0x00d1  */
    /* JADX WARN: Code duplicated, block: B:37:0x00d6  */
    /* JADX WARN: Code duplicated, block: B:7:0x0014  */
    public final Object deleteRecording(String segmentId, Continuation<? super Unit> continuation) {
        C06541 c06541;
        Object byId;
        SegmentEntity segment;
        String previousMeetingId;
        SegmentDao segmentDao;
        SegmentEntity segment2;
        String previousMeetingId2;
        if (continuation instanceof C06541) {
            c06541 = (C06541) continuation;
            if ((c06541.label & Integer.MIN_VALUE) != 0) {
                c06541.label -= Integer.MIN_VALUE;
            } else {
                c06541 = new C06541(continuation);
            }
        } else {
            c06541 = new C06541(continuation);
        }
        Object $result = c06541.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (c06541.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                SegmentDao segmentDao2 = this.segmentDao;
                c06541.L$0 = segmentId;
                c06541.label = 1;
                byId = segmentDao2.getById(segmentId, c06541);
                if (byId == coroutine_suspended) {
                    return coroutine_suspended;
                }
                segment = (SegmentEntity) byId;
                if (segment == null) {
                    return Unit.INSTANCE;
                }
                previousMeetingId = segment.getMeetingId();
                segmentDao = this.segmentDao;
                c06541.L$0 = SpillingKt.nullOutSpilledVariable(segmentId);
                c06541.L$1 = segment;
                c06541.L$2 = previousMeetingId;
                c06541.label = 2;
                if (segmentDao.deleteById(segmentId, c06541) == coroutine_suspended) {
                    return coroutine_suspended;
                }
                segment2 = segment;
                previousMeetingId2 = previousMeetingId;
                try {
                    Result.Companion companion = Result.INSTANCE;
                    MeetingRepository meetingRepository = this;
                    Result.m8304constructorimpl(Boxing.boxBoolean(new File(segment2.getFilePath()).delete()));
                    break;
                } catch (Throwable th) {
                    Result.Companion companion2 = Result.INSTANCE;
                    Result.m8304constructorimpl(ResultKt.createFailure(th));
                }
                if (previousMeetingId2 == null) {
                    return Unit.INSTANCE;
                }
                c06541.L$0 = SpillingKt.nullOutSpilledVariable(segmentId);
                c06541.L$1 = SpillingKt.nullOutSpilledVariable(segment2);
                c06541.L$2 = SpillingKt.nullOutSpilledVariable(previousMeetingId2);
                c06541.label = 3;
                if (markMeetingNeedsCleanup(previousMeetingId2, c06541) == coroutine_suspended) {
                    return coroutine_suspended;
                }
                return Unit.INSTANCE;
            case 1:
                segmentId = (String) c06541.L$0;
                ResultKt.throwOnFailure($result);
                byId = $result;
                segment = (SegmentEntity) byId;
                if (segment == null) {
                    return Unit.INSTANCE;
                }
                previousMeetingId = segment.getMeetingId();
                segmentDao = this.segmentDao;
                c06541.L$0 = SpillingKt.nullOutSpilledVariable(segmentId);
                c06541.L$1 = segment;
                c06541.L$2 = previousMeetingId;
                c06541.label = 2;
                if (segmentDao.deleteById(segmentId, c06541) == coroutine_suspended) {
                    return coroutine_suspended;
                }
                segment2 = segment;
                previousMeetingId2 = previousMeetingId;
                Result.Companion companion3 = Result.INSTANCE;
                MeetingRepository meetingRepository2 = this;
                Result.m8304constructorimpl(Boxing.boxBoolean(new File(segment2.getFilePath()).delete()));
                if (previousMeetingId2 == null) {
                    return Unit.INSTANCE;
                }
                c06541.L$0 = SpillingKt.nullOutSpilledVariable(segmentId);
                c06541.L$1 = SpillingKt.nullOutSpilledVariable(segment2);
                c06541.L$2 = SpillingKt.nullOutSpilledVariable(previousMeetingId2);
                c06541.label = 3;
                if (markMeetingNeedsCleanup(previousMeetingId2, c06541) == coroutine_suspended) {
                    return coroutine_suspended;
                }
                return Unit.INSTANCE;
            case 2:
                previousMeetingId2 = (String) c06541.L$2;
                segment2 = (SegmentEntity) c06541.L$1;
                segmentId = (String) c06541.L$0;
                ResultKt.throwOnFailure($result);
                Result.Companion companion4 = Result.INSTANCE;
                MeetingRepository meetingRepository3 = this;
                Result.m8304constructorimpl(Boxing.boxBoolean(new File(segment2.getFilePath()).delete()));
                if (previousMeetingId2 == null) {
                    return Unit.INSTANCE;
                }
                c06541.L$0 = SpillingKt.nullOutSpilledVariable(segmentId);
                c06541.L$1 = SpillingKt.nullOutSpilledVariable(segment2);
                c06541.L$2 = SpillingKt.nullOutSpilledVariable(previousMeetingId2);
                c06541.label = 3;
                if (markMeetingNeedsCleanup(previousMeetingId2, c06541) == coroutine_suspended) {
                    return coroutine_suspended;
                }
                return Unit.INSTANCE;
            case 3:
                ResultKt.throwOnFailure($result);
                return Unit.INSTANCE;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
    }

    /* JADX WARN: Code duplicated, block: B:19:0x0062  */
    /* JADX WARN: Code duplicated, block: B:21:0x0065  */
    /* JADX WARN: Code duplicated, block: B:27:0x0077  */
    /* JADX WARN: Code duplicated, block: B:30:0x007e  */
    /* JADX WARN: Code duplicated, block: B:31:0x0083  */
    /* JADX WARN: Code duplicated, block: B:33:0x0086  */
    /* JADX WARN: Code duplicated, block: B:34:0x008d  */
    /* JADX WARN: Code duplicated, block: B:37:0x0095  */
    /* JADX WARN: Code duplicated, block: B:38:0x009c  */
    /* JADX WARN: Code duplicated, block: B:40:0x00a0  */
    /* JADX WARN: Code duplicated, block: B:43:0x00e7 A[RETURN] */
    /* JADX WARN: Code duplicated, block: B:44:0x00e8  */
    /* JADX WARN: Code duplicated, block: B:46:0x00ed  */
    /* JADX WARN: Code duplicated, block: B:48:0x00f1  */
    /* JADX WARN: Code duplicated, block: B:49:0x00f6  */
    /* JADX WARN: Code duplicated, block: B:51:0x00fa  */
    /* JADX WARN: Code duplicated, block: B:7:0x001a  */
    public final Object retryCleanup(String meetingId, Continuation<? super Unit> continuation) {
        C06621 c06621;
        Object byId;
        MeetingEntity meeting;
        String cleanTextOnly;
        int i;
        int i2;
        MeetingDao meetingDao;
        String title;
        String cleanedTranscript;
        String metadataJson;
        MeetingEntity meetingEntityCopy$default;
        int i3;
        String meetingId2;
        PipelineScheduler pipelineScheduler;
        PipelineScheduler pipelineScheduler2;
        String meetingId3 = meetingId;
        if (continuation instanceof C06621) {
            c06621 = (C06621) continuation;
            if ((c06621.label & Integer.MIN_VALUE) != 0) {
                c06621.label -= Integer.MIN_VALUE;
            } else {
                c06621 = new C06621(continuation);
            }
        } else {
            c06621 = new C06621(continuation);
        }
        Object $result = c06621.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (c06621.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                MeetingDao meetingDao2 = this.meetingDao;
                c06621.L$0 = meetingId3;
                c06621.label = 1;
                byId = meetingDao2.getById(meetingId3, c06621);
                if (byId == coroutine_suspended) {
                    return coroutine_suspended;
                }
                meeting = (MeetingEntity) byId;
                if (meeting == null) {
                    return Unit.INSTANCE;
                }
                cleanTextOnly = meeting.getCleanTextOnly();
                if (cleanTextOnly != null || StringsKt.isBlank(cleanTextOnly)) {
                    i = 1;
                } else {
                    i = 0;
                }
                i2 = i ^ 1;
                meetingDao = this.meetingDao;
                if (i2 != 0) {
                    title = meeting.getTitle();
                } else {
                    title = null;
                }
                if (i2 != 0) {
                    cleanedTranscript = meeting.getCleanedTranscript();
                } else {
                    cleanedTranscript = null;
                }
                String cleanTextOnly2 = meeting.getCleanTextOnly();
                if (i2 != 0) {
                    metadataJson = meeting.getMetadataJson();
                } else {
                    metadataJson = null;
                }
                meetingEntityCopy$default = MeetingEntity.copy$default(meeting, null, title, 0L, 0L, "PENDING_CLEANUP", cleanedTranscript, metadataJson, i2 != 0 ? meeting.getCleanupProvider() : null, 0L, cleanTextOnly2, null, System.currentTimeMillis(), 269, null);
                c06621.L$0 = meetingId3;
                c06621.L$1 = SpillingKt.nullOutSpilledVariable(meeting);
                c06621.I$0 = i2;
                c06621.label = 2;
                if (meetingDao.update(meetingEntityCopy$default, c06621) == coroutine_suspended) {
                    return coroutine_suspended;
                }
                i3 = i2;
                meetingId2 = meetingId3;
                if (i3 != 0) {
                    pipelineScheduler2 = this.pipelineScheduler;
                    if (pipelineScheduler2 != null) {
                        pipelineScheduler2.enqueueMeetingSummary(meetingId2, true);
                    }
                } else {
                    pipelineScheduler = this.pipelineScheduler;
                    if (pipelineScheduler != null) {
                        PipelineScheduler.enqueueMeeting$default(pipelineScheduler, meetingId2, true, false, 4, null);
                    }
                }
                return Unit.INSTANCE;
            case 1:
                meetingId3 = (String) c06621.L$0;
                ResultKt.throwOnFailure($result);
                byId = $result;
                meeting = (MeetingEntity) byId;
                if (meeting == null) {
                    return Unit.INSTANCE;
                }
                cleanTextOnly = meeting.getCleanTextOnly();
                if (cleanTextOnly != null) {
                    i = 1;
                } else {
                    i = 1;
                }
                i2 = i ^ 1;
                meetingDao = this.meetingDao;
                if (i2 != 0) {
                    title = meeting.getTitle();
                } else {
                    title = null;
                }
                if (i2 != 0) {
                    cleanedTranscript = meeting.getCleanedTranscript();
                } else {
                    cleanedTranscript = null;
                }
                String cleanTextOnly3 = meeting.getCleanTextOnly();
                if (i2 != 0) {
                    metadataJson = meeting.getMetadataJson();
                } else {
                    metadataJson = null;
                }
                meetingEntityCopy$default = MeetingEntity.copy$default(meeting, null, title, 0L, 0L, "PENDING_CLEANUP", cleanedTranscript, metadataJson, i2 != 0 ? meeting.getCleanupProvider() : null, 0L, cleanTextOnly3, null, System.currentTimeMillis(), 269, null);
                c06621.L$0 = meetingId3;
                c06621.L$1 = SpillingKt.nullOutSpilledVariable(meeting);
                c06621.I$0 = i2;
                c06621.label = 2;
                if (meetingDao.update(meetingEntityCopy$default, c06621) == coroutine_suspended) {
                    return coroutine_suspended;
                }
                i3 = i2;
                meetingId2 = meetingId3;
                if (i3 != 0) {
                    pipelineScheduler2 = this.pipelineScheduler;
                    if (pipelineScheduler2 != null) {
                        pipelineScheduler2.enqueueMeetingSummary(meetingId2, true);
                    }
                } else {
                    pipelineScheduler = this.pipelineScheduler;
                    if (pipelineScheduler != null) {
                        PipelineScheduler.enqueueMeeting$default(pipelineScheduler, meetingId2, true, false, 4, null);
                    }
                }
                return Unit.INSTANCE;
            case 2:
                i3 = c06621.I$0;
                String meetingId4 = (String) c06621.L$0;
                ResultKt.throwOnFailure($result);
                meetingId2 = meetingId4;
                if (i3 != 0) {
                    pipelineScheduler2 = this.pipelineScheduler;
                    if (pipelineScheduler2 != null) {
                        pipelineScheduler2.enqueueMeetingSummary(meetingId2, true);
                    }
                } else {
                    pipelineScheduler = this.pipelineScheduler;
                    if (pipelineScheduler != null) {
                        PipelineScheduler.enqueueMeeting$default(pipelineScheduler, meetingId2, true, false, 4, null);
                    }
                }
                return Unit.INSTANCE;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
    }

    /* JADX WARN: Code duplicated, block: B:7:0x0014  */
    public final Object onSegmentSpeakersConfirmed(String segmentId, Continuation<? super Unit> continuation) {
        C06581 c06581;
        Object byId;
        SegmentEntity segmentEntity;
        String meetingId;
        if (continuation instanceof C06581) {
            c06581 = (C06581) continuation;
            if ((c06581.label & Integer.MIN_VALUE) != 0) {
                c06581.label -= Integer.MIN_VALUE;
            } else {
                c06581 = new C06581(continuation);
            }
        } else {
            c06581 = new C06581(continuation);
        }
        Object $result = c06581.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (c06581.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                SegmentDao segmentDao = this.segmentDao;
                c06581.L$0 = SpillingKt.nullOutSpilledVariable(segmentId);
                c06581.label = 1;
                byId = segmentDao.getById(segmentId, c06581);
                if (byId == coroutine_suspended) {
                    return coroutine_suspended;
                }
                segmentEntity = (SegmentEntity) byId;
                if (segmentEntity != null || (meetingId = segmentEntity.getMeetingId()) == null) {
                    return Unit.INSTANCE;
                }
                c06581.L$0 = SpillingKt.nullOutSpilledVariable(segmentId);
                c06581.L$1 = SpillingKt.nullOutSpilledVariable(meetingId);
                c06581.label = 2;
                if (markMeetingNeedsCleanup(meetingId, c06581) == coroutine_suspended) {
                    return coroutine_suspended;
                }
                return Unit.INSTANCE;
            case 1:
                segmentId = (String) c06581.L$0;
                ResultKt.throwOnFailure($result);
                byId = $result;
                segmentEntity = (SegmentEntity) byId;
                if (segmentEntity != null) {
                    break;
                }
                return Unit.INSTANCE;
            case 2:
                ResultKt.throwOnFailure($result);
                return Unit.INSTANCE;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code duplicated, block: B:19:0x0060  */
    /* JADX WARN: Code duplicated, block: B:21:0x0063  */
    /* JADX WARN: Code duplicated, block: B:23:0x00b1 A[RETURN] */
    /* JADX WARN: Code duplicated, block: B:24:0x00b2  */
    /* JADX WARN: Code duplicated, block: B:27:0x00b8  */
    /* JADX WARN: Code duplicated, block: B:7:0x001a  */
    public final Object markMeetingNeedsCleanup(String meetingId, Continuation<? super Unit> continuation) {
        C06571 c06571;
        Object byId;
        MeetingEntity meeting;
        MeetingDao meetingDao;
        MeetingEntity meetingEntityCopy$default;
        String meetingId2;
        PipelineScheduler pipelineScheduler;
        String meetingId3 = meetingId;
        if (continuation instanceof C06571) {
            c06571 = (C06571) continuation;
            if ((c06571.label & Integer.MIN_VALUE) != 0) {
                c06571.label -= Integer.MIN_VALUE;
            } else {
                c06571 = new C06571(continuation);
            }
        } else {
            c06571 = new C06571(continuation);
        }
        Object $result = c06571.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (c06571.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                MeetingDao meetingDao2 = this.meetingDao;
                c06571.L$0 = meetingId3;
                c06571.label = 1;
                byId = meetingDao2.getById(meetingId3, c06571);
                if (byId == coroutine_suspended) {
                    return coroutine_suspended;
                }
                meeting = (MeetingEntity) byId;
                if (meeting == null) {
                    return Unit.INSTANCE;
                }
                meetingDao = this.meetingDao;
                meetingEntityCopy$default = MeetingEntity.copy$default(meeting, null, null, 0L, 0L, "PENDING_CLEANUP", null, null, null, 0L, null, null, System.currentTimeMillis(), 269, null);
                c06571.L$0 = meetingId3;
                c06571.L$1 = SpillingKt.nullOutSpilledVariable(meeting);
                c06571.label = 2;
                if (meetingDao.update(meetingEntityCopy$default, c06571) == coroutine_suspended) {
                    return coroutine_suspended;
                }
                meetingId2 = meetingId3;
                pipelineScheduler = this.pipelineScheduler;
                if (pipelineScheduler != null) {
                    PipelineScheduler.enqueueMeeting$default(pipelineScheduler, meetingId2, true, false, 4, null);
                }
                return Unit.INSTANCE;
            case 1:
                meetingId3 = (String) c06571.L$0;
                ResultKt.throwOnFailure($result);
                byId = $result;
                meeting = (MeetingEntity) byId;
                if (meeting == null) {
                    return Unit.INSTANCE;
                }
                meetingDao = this.meetingDao;
                meetingEntityCopy$default = MeetingEntity.copy$default(meeting, null, null, 0L, 0L, "PENDING_CLEANUP", null, null, null, 0L, null, null, System.currentTimeMillis(), 269, null);
                c06571.L$0 = meetingId3;
                c06571.L$1 = SpillingKt.nullOutSpilledVariable(meeting);
                c06571.label = 2;
                if (meetingDao.update(meetingEntityCopy$default, c06571) == coroutine_suspended) {
                    return coroutine_suspended;
                }
                meetingId2 = meetingId3;
                pipelineScheduler = this.pipelineScheduler;
                if (pipelineScheduler != null) {
                    PipelineScheduler.enqueueMeeting$default(pipelineScheduler, meetingId2, true, false, 4, null);
                }
                return Unit.INSTANCE;
            case 2:
                String meetingId4 = (String) c06571.L$0;
                ResultKt.throwOnFailure($result);
                meetingId2 = meetingId4;
                pipelineScheduler = this.pipelineScheduler;
                if (pipelineScheduler != null) {
                    PipelineScheduler.enqueueMeeting$default(pipelineScheduler, meetingId2, true, false, 4, null);
                }
                return Unit.INSTANCE;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
    }

    public final Object getPendingCleanup(int limit, Continuation<? super List<MeetingEntity>> continuation) {
        return this.meetingDao.getNeedingCleanup("PENDING_CLEANUP", "FAILED", limit, continuation);
    }

    public static /* synthetic */ Object requeueAllForCleanup$default(MeetingRepository meetingRepository, int i, Continuation continuation, int i2, Object obj) {
        if ((i2 & 1) != 0) {
            i = 100;
        }
        return meetingRepository.requeueAllForCleanup(i, continuation);
    }

    /* JADX WARN: Code duplicated, block: B:20:0x0084  */
    /* JADX WARN: Code duplicated, block: B:22:0x00ee A[RETURN] */
    /* JADX WARN: Code duplicated, block: B:23:0x00ef  */
    /* JADX WARN: Code duplicated, block: B:7:0x001a  */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:23:0x00ef -> B:24:0x00fa). Please report as a decompilation issue!!! */
    /*  JADX ERROR: JadxOverflowException in pass: RegionMakerVisitor
        jadx.core.utils.exceptions.JadxOverflowException: Regions stack size limit reached
        	at jadx.core.utils.ErrorsCounter.addError(ErrorsCounter.java:59)
        	at jadx.core.utils.ErrorsCounter.error(ErrorsCounter.java:31)
        	at jadx.core.dex.attributes.nodes.NotificationAttrNode.addError(NotificationAttrNode.java:19)
        */
    public final java.lang.Object requeueAllForCleanup(int r30, kotlin.coroutines.Continuation<? super java.util.List<java.lang.String>> r31) {
        /*
            Method dump skipped, instruction units count: 276
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.varun.pocketassistant.data.MeetingRepository.requeueAllForCleanup(int, kotlin.coroutines.Continuation):java.lang.Object");
    }

    /* JADX WARN: Code duplicated, block: B:20:0x0071  */
    /* JADX WARN: Code duplicated, block: B:26:0x009f  */
    /* JADX WARN: Code duplicated, block: B:29:0x00ff A[LOOP:0: B:24:0x0099->B:29:0x00ff, LOOP_END] */
    /* JADX WARN: Code duplicated, block: B:34:0x0131 A[LOOP:1: B:32:0x012b->B:34:0x0131, LOOP_END] */
    /* JADX WARN: Code duplicated, block: B:38:0x00fe A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:42:0x0085 A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:44:0x006b A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:7:0x0018  */
    public final Object recoverStuckCleaning(Continuation<? super List<String>> continuation) {
        C06601 c06601;
        Object all;
        Collection arrayList;
        Iterator it;
        long now;
        List stuck;
        Collection arrayList2;
        Iterator it2;
        MeetingDao meetingDao;
        Iterator it3;
        List stuck2;
        Object $result;
        MeetingRepository meetingRepository;
        Continuation<? super List<String>> continuation2;
        MeetingEntity meetingEntityCopy$default;
        long now2;
        MeetingRepository meetingRepository2 = this;
        Continuation<? super List<String>> continuation3 = continuation;
        if (continuation3 instanceof C06601) {
            c06601 = (C06601) continuation3;
            if ((c06601.label & Integer.MIN_VALUE) != 0) {
                c06601.label -= Integer.MIN_VALUE;
            } else {
                c06601 = meetingRepository2.new C06601(continuation3);
            }
        } else {
            c06601 = meetingRepository2.new C06601(continuation3);
        }
        Object $result2 = c06601.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (c06601.label) {
            case 0:
                ResultKt.throwOnFailure($result2);
                MeetingDao meetingDao2 = meetingRepository2.meetingDao;
                c06601.label = 1;
                all = meetingDao2.getAll(200, c06601);
                if (all == coroutine_suspended) {
                    return coroutine_suspended;
                }
                arrayList = new ArrayList();
                for (Object obj : (Iterable) all) {
                    if (Intrinsics.areEqual(((MeetingEntity) obj).getStatus(), "CLEANING")) {
                        arrayList.add(obj);
                    }
                }
                List stuck3 = (List) arrayList;
                long now3 = System.currentTimeMillis();
                it = stuck3.iterator();
                now = now3;
                stuck = stuck3;
                while (it.hasNext()) {
                    MeetingEntity meeting = (MeetingEntity) it.next();
                    meetingDao = meetingRepository2.meetingDao;
                    it3 = it;
                    stuck2 = stuck;
                    $result = $result2;
                    meetingRepository = meetingRepository2;
                    continuation2 = continuation3;
                    meetingEntityCopy$default = MeetingEntity.copy$default(meeting, null, null, 0L, 0L, "PENDING_CLEANUP", null, null, null, 0L, null, null, now, 2031, null);
                    now2 = now;
                    c06601.L$0 = stuck2;
                    c06601.L$1 = it3;
                    c06601.L$2 = SpillingKt.nullOutSpilledVariable(meeting);
                    c06601.J$0 = now2;
                    c06601.label = 2;
                    if (meetingDao.update(meetingEntityCopy$default, c06601) == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    stuck = stuck2;
                    now = now2;
                    $result2 = $result;
                    continuation3 = continuation2;
                    it = it3;
                    meetingRepository2 = meetingRepository;
                }
                List list = stuck;
                arrayList2 = new ArrayList(CollectionsKt.collectionSizeOrDefault(list, 10));
                it2 = list.iterator();
                while (it2.hasNext()) {
                    arrayList2.add(((MeetingEntity) it2.next()).getId());
                }
                return (List) arrayList2;
            case 1:
                ResultKt.throwOnFailure($result2);
                all = $result2;
                arrayList = new ArrayList();
                while (r10.hasNext()) {
                    if (Intrinsics.areEqual(((MeetingEntity) obj).getStatus(), "CLEANING")) {
                        arrayList.add(obj);
                    }
                }
                List stuck4 = (List) arrayList;
                long now4 = System.currentTimeMillis();
                it = stuck4.iterator();
                now = now4;
                stuck = stuck4;
                while (it.hasNext()) {
                    MeetingEntity meeting2 = (MeetingEntity) it.next();
                    meetingDao = meetingRepository2.meetingDao;
                    it3 = it;
                    stuck2 = stuck;
                    $result = $result2;
                    meetingRepository = meetingRepository2;
                    continuation2 = continuation3;
                    meetingEntityCopy$default = MeetingEntity.copy$default(meeting2, null, null, 0L, 0L, "PENDING_CLEANUP", null, null, null, 0L, null, null, now, 2031, null);
                    now2 = now;
                    c06601.L$0 = stuck2;
                    c06601.L$1 = it3;
                    c06601.L$2 = SpillingKt.nullOutSpilledVariable(meeting2);
                    c06601.J$0 = now2;
                    c06601.label = 2;
                    if (meetingDao.update(meetingEntityCopy$default, c06601) == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    stuck = stuck2;
                    now = now2;
                    $result2 = $result;
                    continuation3 = continuation2;
                    it = it3;
                    meetingRepository2 = meetingRepository;
                }
                List list2 = stuck;
                arrayList2 = new ArrayList(CollectionsKt.collectionSizeOrDefault(list2, 10));
                it2 = list2.iterator();
                while (it2.hasNext()) {
                    arrayList2.add(((MeetingEntity) it2.next()).getId());
                }
                return (List) arrayList2;
            case 2:
                long now5 = c06601.J$0;
                it = (Iterator) c06601.L$1;
                stuck = (List) c06601.L$0;
                ResultKt.throwOnFailure($result2);
                now = now5;
                while (it.hasNext()) {
                    MeetingEntity meeting3 = (MeetingEntity) it.next();
                    meetingDao = meetingRepository2.meetingDao;
                    it3 = it;
                    stuck2 = stuck;
                    $result = $result2;
                    meetingRepository = meetingRepository2;
                    continuation2 = continuation3;
                    meetingEntityCopy$default = MeetingEntity.copy$default(meeting3, null, null, 0L, 0L, "PENDING_CLEANUP", null, null, null, 0L, null, null, now, 2031, null);
                    now2 = now;
                    c06601.L$0 = stuck2;
                    c06601.L$1 = it3;
                    c06601.L$2 = SpillingKt.nullOutSpilledVariable(meeting3);
                    c06601.J$0 = now2;
                    c06601.label = 2;
                    if (meetingDao.update(meetingEntityCopy$default, c06601) == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    stuck = stuck2;
                    now = now2;
                    $result2 = $result;
                    continuation3 = continuation2;
                    it = it3;
                    meetingRepository2 = meetingRepository;
                }
                List list3 = stuck;
                arrayList2 = new ArrayList(CollectionsKt.collectionSizeOrDefault(list3, 10));
                it2 = list3.iterator();
                while (it2.hasNext()) {
                    arrayList2.add(((MeetingEntity) it2.next()).getId());
                }
                return (List) arrayList2;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
    }

    public final Object updateMeeting(MeetingEntity meeting, Continuation<? super Unit> continuation) {
        Object objUpdate = this.meetingDao.update(MeetingEntity.copy$default(meeting, null, null, 0L, 0L, null, null, null, null, 0L, null, null, System.currentTimeMillis(), 2047, null), continuation);
        return objUpdate == IntrinsicsKt.getCOROUTINE_SUSPENDED() ? objUpdate : Unit.INSTANCE;
    }

    /* JADX WARN: Code duplicated, block: B:7:0x0014  */
    public final Object allRecordingsAsrSettled(String meetingId, Continuation<? super Boolean> continuation) {
        AnonymousClass1 anonymousClass1;
        Object forMeeting;
        if (continuation instanceof AnonymousClass1) {
            anonymousClass1 = (AnonymousClass1) continuation;
            if ((anonymousClass1.label & Integer.MIN_VALUE) != 0) {
                anonymousClass1.label -= Integer.MIN_VALUE;
            } else {
                anonymousClass1 = new AnonymousClass1(continuation);
            }
        } else {
            anonymousClass1 = new AnonymousClass1(continuation);
        }
        Object $result = anonymousClass1.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        boolean z = true;
        switch (anonymousClass1.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                SegmentDao segmentDao = this.segmentDao;
                anonymousClass1.L$0 = SpillingKt.nullOutSpilledVariable(meetingId);
                anonymousClass1.label = 1;
                forMeeting = segmentDao.getForMeeting(meetingId, anonymousClass1);
                if (forMeeting == coroutine_suspended) {
                    return coroutine_suspended;
                }
                break;
            case 1:
                ResultKt.throwOnFailure($result);
                forMeeting = $result;
                break;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
        List recordings = (List) forMeeting;
        if (recordings.isEmpty()) {
            return Boxing.boxBoolean(true);
        }
        List<SegmentEntity> list = recordings;
        if (!(list instanceof Collection) || !list.isEmpty()) {
            for (SegmentEntity segmentEntity : list) {
                if (Intrinsics.areEqual(segmentEntity.getTranscriptStatus(), "PENDING") || Intrinsics.areEqual(segmentEntity.getTranscriptStatus(), "PROCESSING")) {
                    z = false;
                }
            }
        }
        return Boxing.boxBoolean(z);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code duplicated, block: B:21:0x008b  */
    /* JADX WARN: Code duplicated, block: B:23:0x00a0  */
    /* JADX WARN: Code duplicated, block: B:28:0x00cf A[LOOP:1: B:26:0x00c9->B:28:0x00cf, LOOP_END] */
    /* JADX WARN: Code duplicated, block: B:31:0x00ef  */
    /* JADX WARN: Code duplicated, block: B:33:0x010a A[RETURN] */
    /* JADX WARN: Code duplicated, block: B:34:0x010b  */
    /* JADX WARN: Code duplicated, block: B:37:0x010f  */
    /* JADX WARN: Code duplicated, block: B:41:0x00a3 A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:7:0x0018  */
    public final Object assignOverlapping(String meetingId, long startMs, long endMs, Continuation<? super Unit> continuation) {
        C06501 c06501;
        String meetingId2;
        long startMs2;
        Object obj;
        long endMs2;
        Iterable iterable;
        Collection arrayList;
        Collection arrayList2;
        Iterator it;
        List ids;
        SegmentDao segmentDao;
        if (continuation instanceof C06501) {
            c06501 = (C06501) continuation;
            if ((c06501.label & Integer.MIN_VALUE) != 0) {
                c06501.label -= Integer.MIN_VALUE;
            } else {
                c06501 = new C06501(continuation);
            }
        } else {
            c06501 = new C06501(continuation);
        }
        C06501 c06502 = c06501;
        Object $result = c06502.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (c06502.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                SegmentDao segmentDao2 = this.segmentDao;
                meetingId2 = meetingId;
                c06502.L$0 = meetingId2;
                c06502.J$0 = startMs;
                c06502.J$1 = endMs;
                c06502.label = 1;
                Object overlapping = segmentDao2.getOverlapping(startMs, endMs, c06502);
                if (overlapping == coroutine_suspended) {
                    return coroutine_suspended;
                }
                startMs2 = startMs;
                obj = overlapping;
                endMs2 = endMs;
                iterable = (Iterable) obj;
                arrayList = new ArrayList();
                for (Object obj2 : iterable) {
                    Object $result2 = $result;
                    Iterable iterable2 = iterable;
                    if (INSTANCE.isAssignableRecording((SegmentEntity) obj2)) {
                        arrayList.add(obj2);
                    }
                    iterable = iterable2;
                    $result = $result2;
                }
                Iterable iterable3 = (List) arrayList;
                arrayList2 = new ArrayList(CollectionsKt.collectionSizeOrDefault(iterable3, 10));
                it = iterable3.iterator();
                while (it.hasNext()) {
                    arrayList2.add(((SegmentEntity) it.next()).getId());
                }
                ids = (List) arrayList2;
                if (!ids.isEmpty()) {
                    return Unit.INSTANCE;
                }
                segmentDao = this.segmentDao;
                c06502.L$0 = SpillingKt.nullOutSpilledVariable(meetingId2);
                c06502.L$1 = SpillingKt.nullOutSpilledVariable(ids);
                c06502.J$0 = startMs2;
                c06502.J$1 = endMs2;
                c06502.label = 2;
                if (segmentDao.assignMeeting(meetingId2, ids, c06502) == coroutine_suspended) {
                    return coroutine_suspended;
                }
                return Unit.INSTANCE;
            case 1:
                endMs2 = c06502.J$1;
                startMs2 = c06502.J$0;
                String meetingId3 = (String) c06502.L$0;
                ResultKt.throwOnFailure($result);
                meetingId2 = meetingId3;
                obj = $result;
                iterable = (Iterable) obj;
                arrayList = new ArrayList();
                while (r15.hasNext()) {
                    Object $result3 = $result;
                    Iterable iterable4 = iterable;
                    if (INSTANCE.isAssignableRecording((SegmentEntity) obj2)) {
                        arrayList.add(obj2);
                    }
                    iterable = iterable4;
                    $result = $result3;
                }
                Iterable iterable5 = (List) arrayList;
                arrayList2 = new ArrayList(CollectionsKt.collectionSizeOrDefault(iterable5, 10));
                it = iterable5.iterator();
                while (it.hasNext()) {
                    arrayList2.add(((SegmentEntity) it.next()).getId());
                }
                ids = (List) arrayList2;
                if (!ids.isEmpty()) {
                    return Unit.INSTANCE;
                }
                segmentDao = this.segmentDao;
                c06502.L$0 = SpillingKt.nullOutSpilledVariable(meetingId2);
                c06502.L$1 = SpillingKt.nullOutSpilledVariable(ids);
                c06502.J$0 = startMs2;
                c06502.J$1 = endMs2;
                c06502.label = 2;
                if (segmentDao.assignMeeting(meetingId2, ids, c06502) == coroutine_suspended) {
                    return coroutine_suspended;
                }
                return Unit.INSTANCE;
            case 2:
                long j = c06502.J$1;
                long j2 = c06502.J$0;
                ResultKt.throwOnFailure($result);
                return Unit.INSTANCE;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
    }

    /* JADX INFO: compiled from: MeetingRepository.kt */
    @Metadata(d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003J\u000e\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0007¨\u0006\b"}, d2 = {"Lcom/varun/pocketassistant/data/MeetingRepository$Companion;", "", "<init>", "()V", "isAssignableRecording", "", "segment", "Lcom/varun/pocketassistant/data/SegmentEntity;", "app_debug"}, k = 1, mv = {2, 2, 0}, xi = 48)
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        public final boolean isAssignableRecording(SegmentEntity segment) {
            Intrinsics.checkNotNullParameter(segment, "segment");
            return (Intrinsics.areEqual(segment.getTranscriptStatus(), "SKIPPED_SILENCE") || Intrinsics.areEqual(segment.getSkipReason(), SkipReason.SUPERSEDED)) ? false : true;
        }
    }
}
