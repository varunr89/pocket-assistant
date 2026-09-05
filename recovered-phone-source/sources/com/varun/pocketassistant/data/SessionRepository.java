package com.varun.pocketassistant.data;

import androidx.core.app.NotificationCompat;
import androidx.core.view.MotionEventCompat;
import com.varun.pocketassistant.capture.AudioStorage;
import com.varun.pocketassistant.capture.RetentionPolicy;
import com.varun.pocketassistant.pipeline.work.AsrWorker;
import com.varun.pocketassistant.pipeline.work.PipelineScheduler;
import com.varun.pocketassistant.speech.DiarizationLabels;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import kotlin.Metadata;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.Boxing;
import kotlin.coroutines.jvm.internal.ContinuationImpl;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SpillingKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import kotlinx.coroutines.flow.Flow;
import kotlinx.coroutines.internal.LockFreeTaskQueueCore;

/* JADX INFO: compiled from: SessionRepository.kt */
/* JADX INFO: loaded from: classes9.dex */
@Metadata(d1 = {"\u0000|\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\t\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0010\u000b\n\u0002\b\u000f\b\u0007\u0018\u00002\u00020\u0001B'\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t¢\u0006\u0004\b\n\u0010\u000bJ\u0012\u0010\u0012\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00150\u00140\u0013J\u001a\u0010\u0016\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00170\u00140\u00132\u0006\u0010\u0018\u001a\u00020\u0019J\u0012\u0010\u001a\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00170\u00140\u0013J\u0012\u0010\u001b\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00170\u00140\u0013J\u0018\u0010\u001c\u001a\u0004\u0018\u00010\u00152\u0006\u0010\u001d\u001a\u00020\u0019H\u0086@¢\u0006\u0002\u0010\u001eJ\u001c\u0010\u001f\u001a\b\u0012\u0004\u0012\u00020\u00170\u00142\u0006\u0010\u0018\u001a\u00020\u0019H\u0086@¢\u0006\u0002\u0010\u001eJ\u0018\u0010 \u001a\u0004\u0018\u00010\u00172\u0006\u0010\u001d\u001a\u00020\u0019H\u0086@¢\u0006\u0002\u0010\u001eJ\u001c\u0010!\u001a\b\u0012\u0004\u0012\u00020\u00170\u00142\u0006\u0010\"\u001a\u00020#H\u0086@¢\u0006\u0002\u0010$J\u000e\u0010%\u001a\u00020\u0015H\u0086@¢\u0006\u0002\u0010&J\u001e\u0010'\u001a\u00020(2\u0006\u0010\u0018\u001a\u00020\u00192\u0006\u0010)\u001a\u00020*H\u0086@¢\u0006\u0002\u0010+J:\u0010,\u001a\u00020\u00172\u0006\u0010\u0018\u001a\u00020\u00192\u0006\u0010-\u001a\u00020.2\u0006\u0010/\u001a\u0002002\u0006\u00101\u001a\u0002002\n\b\u0002\u00102\u001a\u0004\u0018\u00010\u0019H\u0086@¢\u0006\u0002\u00103J\u009e\u0001\u00104\u001a\u00020(2\u0006\u00105\u001a\u00020\u00192\u0006\u0010)\u001a\u0002062\n\b\u0002\u00107\u001a\u0004\u0018\u00010\u00192\n\b\u0002\u00108\u001a\u0004\u0018\u00010\u00192\n\b\u0002\u00109\u001a\u0004\u0018\u00010\u00192\n\b\u0002\u0010:\u001a\u0004\u0018\u00010\u00192\n\b\u0002\u0010;\u001a\u0004\u0018\u00010\u00192\n\b\u0002\u0010<\u001a\u0004\u0018\u00010\u00192\n\b\u0002\u0010=\u001a\u0004\u0018\u00010\u00192\n\b\u0002\u0010>\u001a\u0004\u0018\u00010\u00192\n\b\u0002\u0010?\u001a\u0004\u0018\u00010@2\b\b\u0002\u0010A\u001a\u00020@2\b\b\u0002\u0010B\u001a\u00020@H\u0086@¢\u0006\u0002\u0010CJ\u001e\u0010D\u001a\u00020@2\u0006\u00105\u001a\u00020\u00192\u0006\u0010E\u001a\u00020#H\u0086@¢\u0006\u0002\u0010FJ\u000e\u0010G\u001a\u00020(H\u0086@¢\u0006\u0002\u0010&J\u001e\u0010H\u001a\u00020#2\u0006\u0010I\u001a\u0002002\u0006\u0010J\u001a\u000200H\u0086@¢\u0006\u0002\u0010KJ\u0016\u0010L\u001a\u00020@2\u0006\u00105\u001a\u00020\u0019H\u0086@¢\u0006\u0002\u0010\u001eJ\u0016\u0010M\u001a\u00020(2\u0006\u0010\u0018\u001a\u00020\u0019H\u0086@¢\u0006\u0002\u0010\u001eJ\u000e\u0010N\u001a\u00020(H\u0086@¢\u0006\u0002\u0010&R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004¢\u0006\u0002\n\u0000R\u001c\u0010\f\u001a\u0004\u0018\u00010\rX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u000e\u0010\u000f\"\u0004\b\u0010\u0010\u0011¨\u0006O"}, d2 = {"Lcom/varun/pocketassistant/data/SessionRepository;", "", "sessionDao", "Lcom/varun/pocketassistant/data/SessionDao;", "segmentDao", "Lcom/varun/pocketassistant/data/SegmentDao;", "audioStorage", "Lcom/varun/pocketassistant/capture/AudioStorage;", "retentionPolicy", "Lcom/varun/pocketassistant/capture/RetentionPolicy;", "<init>", "(Lcom/varun/pocketassistant/data/SessionDao;Lcom/varun/pocketassistant/data/SegmentDao;Lcom/varun/pocketassistant/capture/AudioStorage;Lcom/varun/pocketassistant/capture/RetentionPolicy;)V", "pipelineScheduler", "Lcom/varun/pocketassistant/pipeline/work/PipelineScheduler;", "getPipelineScheduler", "()Lcom/varun/pocketassistant/pipeline/work/PipelineScheduler;", "setPipelineScheduler", "(Lcom/varun/pocketassistant/pipeline/work/PipelineScheduler;)V", "observeSessions", "Lkotlinx/coroutines/flow/Flow;", "", "Lcom/varun/pocketassistant/data/SessionEntity;", "observeSegments", "Lcom/varun/pocketassistant/data/SegmentEntity;", "sessionId", "", "observeAllRecordings", "observeUncategorizedRecordings", "getSession", "id", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getSegments", "getSegment", "getPendingWork", "limit", "", "(ILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "startSession", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "setStatus", "", NotificationCompat.CATEGORY_STATUS, "Lcom/varun/pocketassistant/data/SessionStatus;", "(Ljava/lang/String;Lcom/varun/pocketassistant/data/SessionStatus;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "addSegment", "file", "Ljava/io/File;", "startedAtMs", "", "endedAtMs", "endReason", "(Ljava/lang/String;Ljava/io/File;JJLjava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "updateTranscript", AsrWorker.KEY_SEGMENT_ID, "Lcom/varun/pocketassistant/data/TranscriptStatus;", "transcript", "diarized", "cleaned", "asrProvider", "cleanupProvider", "asrLastError", "skipReason", "speakerCandidatesJson", "youConfirmed", "", "clearAsrError", "clearSpeakerMeta", "(Ljava/lang/String;Lcom/varun/pocketassistant/data/TranscriptStatus;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/Boolean;ZZLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "confirmYouSpeaker", "youSpeakerId", "(Ljava/lang/String;ILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "requeueAllForAsr", "requeueAsrInRange", "startMs", "endMs", "(JJLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "requeueForAsr", "deleteSession", "applyRetention", "app_debug"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class SessionRepository {
    public static final int $stable = 8;
    private final AudioStorage audioStorage;
    private volatile PipelineScheduler pipelineScheduler;
    private final RetentionPolicy retentionPolicy;
    private final SegmentDao segmentDao;
    private final SessionDao sessionDao;

    /* JADX INFO: renamed from: com.varun.pocketassistant.data.SessionRepository$addSegment$1, reason: invalid class name */
    /* JADX INFO: compiled from: SessionRepository.kt */
    @Metadata(k = 3, mv = {2, 2, 0}, xi = 48)
    @DebugMetadata(c = "com.varun.pocketassistant.data.SessionRepository", f = "SessionRepository.kt", i = {0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 2, 2}, l = {109, 111, 113}, m = "addSegment", n = {"sessionId", "file", "endReason", "segment", "startedAtMs", "endedAtMs", "now", "sessionId", "file", "endReason", "segment", "startedAtMs", "endedAtMs", "now", "sessionId", "file", "endReason", "segment", "session", "startedAtMs", "endedAtMs", "now"}, s = {"L$0", "L$1", "L$2", "L$3", "J$0", "J$1", "J$2", "L$0", "L$1", "L$2", "L$3", "J$0", "J$1", "J$2", "L$0", "L$1", "L$2", "L$3", "L$4", "J$0", "J$1", "J$2"})
    static final class AnonymousClass1 extends ContinuationImpl {
        long J$0;
        long J$1;
        long J$2;
        Object L$0;
        Object L$1;
        Object L$2;
        Object L$3;
        Object L$4;
        int label;
        /* synthetic */ Object result;

        AnonymousClass1(Continuation<? super AnonymousClass1> continuation) {
            super(continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            this.result = obj;
            this.label |= Integer.MIN_VALUE;
            return SessionRepository.this.addSegment(null, null, 0L, 0L, null, this);
        }
    }

    /* JADX INFO: renamed from: com.varun.pocketassistant.data.SessionRepository$applyRetention$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: SessionRepository.kt */
    @Metadata(k = 3, mv = {2, 2, 0}, xi = 48)
    @DebugMetadata(c = "com.varun.pocketassistant.data.SessionRepository", f = "SessionRepository.kt", i = {0, 1, 1, 1, 1, 1, 1}, l = {271, 271}, m = "applyRetention", n = {"cutoff", "$this$forEach\\1", "element\\1", "it\\2", "cutoff", "$i$f$forEach\\1\\271", "$i$a$-forEach-SessionRepository$applyRetention$2\\2\\275\\0"}, s = {"J$0", "L$0", "L$2", "L$3", "J$0", "I$0", "I$1"})
    static final class C06651 extends ContinuationImpl {
        int I$0;
        int I$1;
        long J$0;
        Object L$0;
        Object L$1;
        Object L$2;
        Object L$3;
        int label;
        /* synthetic */ Object result;

        C06651(Continuation<? super C06651> continuation) {
            super(continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            this.result = obj;
            this.label |= Integer.MIN_VALUE;
            return SessionRepository.this.applyRetention(this);
        }
    }

    /* JADX INFO: renamed from: com.varun.pocketassistant.data.SessionRepository$confirmYouSpeaker$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: SessionRepository.kt */
    @Metadata(k = 3, mv = {2, 2, 0}, xi = 48)
    @DebugMetadata(c = "com.varun.pocketassistant.data.SessionRepository", f = "SessionRepository.kt", i = {0, 0, 1, 1, 1, 1, 1}, l = {167, 170}, m = "confirmYouSpeaker", n = {AsrWorker.KEY_SEGMENT_ID, "youSpeakerId", AsrWorker.KEY_SEGMENT_ID, "seg", "raw", "labeled", "youSpeakerId"}, s = {"L$0", "I$0", "L$0", "L$1", "L$2", "L$3", "I$0"})
    static final class C06661 extends ContinuationImpl {
        int I$0;
        Object L$0;
        Object L$1;
        Object L$2;
        Object L$3;
        int label;
        /* synthetic */ Object result;

        C06661(Continuation<? super C06661> continuation) {
            super(continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            this.result = obj;
            this.label |= Integer.MIN_VALUE;
            return SessionRepository.this.confirmYouSpeaker(null, 0, this);
        }
    }

    /* JADX INFO: renamed from: com.varun.pocketassistant.data.SessionRepository$deleteSession$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: SessionRepository.kt */
    @Metadata(k = 3, mv = {2, 2, 0}, xi = 48)
    @DebugMetadata(c = "com.varun.pocketassistant.data.SessionRepository", f = "SessionRepository.kt", i = {0, 1}, l = {264, 265}, m = "deleteSession", n = {"sessionId", "sessionId"}, s = {"L$0", "L$0"})
    static final class C06671 extends ContinuationImpl {
        Object L$0;
        int label;
        /* synthetic */ Object result;

        C06671(Continuation<? super C06671> continuation) {
            super(continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            this.result = obj;
            this.label |= Integer.MIN_VALUE;
            return SessionRepository.this.deleteSession(null, this);
        }
    }

    /* JADX INFO: renamed from: com.varun.pocketassistant.data.SessionRepository$getPendingWork$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: SessionRepository.kt */
    @Metadata(k = 3, mv = {2, 2, 0}, xi = 48)
    @DebugMetadata(c = "com.varun.pocketassistant.data.SessionRepository", f = "SessionRepository.kt", i = {0, 1, 1, 1, 1, 1, 1, 2}, l = {MotionEventCompat.AXIS_GENERIC_14, MotionEventCompat.AXIS_GENERIC_15, 53}, m = "getPendingWork", n = {"limit", "$this$forEach\\1", "element\\1", "seg\\2", "limit", "$i$f$forEach\\1\\45", "$i$a$-forEach-SessionRepository$getPendingWork$2\\2\\275\\0", "limit"}, s = {"I$0", "L$0", "L$2", "L$3", "I$0", "I$1", "I$2", "I$0"})
    static final class C06681 extends ContinuationImpl {
        int I$0;
        int I$1;
        int I$2;
        Object L$0;
        Object L$1;
        Object L$2;
        Object L$3;
        int label;
        /* synthetic */ Object result;

        C06681(Continuation<? super C06681> continuation) {
            super(continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            this.result = obj;
            this.label |= Integer.MIN_VALUE;
            return SessionRepository.this.getPendingWork(0, this);
        }
    }

    /* JADX INFO: renamed from: com.varun.pocketassistant.data.SessionRepository$requeueAllForAsr$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: SessionRepository.kt */
    @Metadata(k = 3, mv = {2, 2, 0}, xi = 48)
    @DebugMetadata(c = "com.varun.pocketassistant.data.SessionRepository", f = "SessionRepository.kt", i = {5, 5, 5, 5, 5, 5}, l = {181, 185, 186, 187, 188, 191}, m = "requeueAllForAsr", n = {"segs", "$this$forEach\\3", "element\\3", "seg\\4", "$i$f$forEach\\3\\189", "$i$a$-forEach-SessionRepository$requeueAllForAsr$3\\4\\283\\0"}, s = {"L$0", "L$1", "L$3", "L$4", "I$0", "I$1"})
    static final class C06691 extends ContinuationImpl {
        int I$0;
        int I$1;
        Object L$0;
        Object L$1;
        Object L$2;
        Object L$3;
        Object L$4;
        int label;
        /* synthetic */ Object result;

        C06691(Continuation<? super C06691> continuation) {
            super(continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            this.result = obj;
            this.label |= Integer.MIN_VALUE;
            return SessionRepository.this.requeueAllForAsr(this);
        }
    }

    /* JADX INFO: renamed from: com.varun.pocketassistant.data.SessionRepository$requeueAsrInRange$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: SessionRepository.kt */
    @Metadata(k = 3, mv = {2, 2, 0}, xi = 48)
    @DebugMetadata(c = "com.varun.pocketassistant.data.SessionRepository", f = "SessionRepository.kt", i = {0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1}, l = {215, 221}, m = "requeueAsrInRange", n = {"startMs", "endMs", "segs", "count", "$this$forEach\\1", "element\\1", "seg\\2", "startMs", "endMs", "$i$f$forEach\\1\\217", "$i$a$-forEach-SessionRepository$requeueAsrInRange$2\\2\\275\\0"}, s = {"J$0", "J$1", "L$0", "L$1", "L$2", "L$4", "L$5", "J$0", "J$1", "I$0", "I$1"})
    static final class C06701 extends ContinuationImpl {
        int I$0;
        int I$1;
        long J$0;
        long J$1;
        Object L$0;
        Object L$1;
        Object L$2;
        Object L$3;
        Object L$4;
        Object L$5;
        int label;
        /* synthetic */ Object result;

        C06701(Continuation<? super C06701> continuation) {
            super(continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            this.result = obj;
            this.label |= Integer.MIN_VALUE;
            return SessionRepository.this.requeueAsrInRange(0L, 0L, this);
        }
    }

    /* JADX INFO: renamed from: com.varun.pocketassistant.data.SessionRepository$requeueForAsr$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: SessionRepository.kt */
    @Metadata(k = 3, mv = {2, 2, 0}, xi = 48)
    @DebugMetadata(c = "com.varun.pocketassistant.data.SessionRepository", f = "SessionRepository.kt", i = {0, 1, 1}, l = {243, 245}, m = "requeueForAsr", n = {AsrWorker.KEY_SEGMENT_ID, AsrWorker.KEY_SEGMENT_ID, "seg"}, s = {"L$0", "L$0", "L$1"})
    static final class C06711 extends ContinuationImpl {
        Object L$0;
        Object L$1;
        int label;
        /* synthetic */ Object result;

        C06711(Continuation<? super C06711> continuation) {
            super(continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            this.result = obj;
            this.label |= Integer.MIN_VALUE;
            return SessionRepository.this.requeueForAsr(null, this);
        }
    }

    /* JADX INFO: renamed from: com.varun.pocketassistant.data.SessionRepository$setStatus$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: SessionRepository.kt */
    @Metadata(k = 3, mv = {2, 2, 0}, xi = 48)
    @DebugMetadata(c = "com.varun.pocketassistant.data.SessionRepository", f = "SessionRepository.kt", i = {0, 0, 1, 1, 1}, l = {76, 77}, m = "setStatus", n = {"sessionId", NotificationCompat.CATEGORY_STATUS, "sessionId", NotificationCompat.CATEGORY_STATUS, "current"}, s = {"L$0", "L$1", "L$0", "L$1", "L$2"})
    static final class C06721 extends ContinuationImpl {
        Object L$0;
        Object L$1;
        Object L$2;
        int label;
        /* synthetic */ Object result;

        C06721(Continuation<? super C06721> continuation) {
            super(continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            this.result = obj;
            this.label |= Integer.MIN_VALUE;
            return SessionRepository.this.setStatus(null, null, this);
        }
    }

    /* JADX INFO: renamed from: com.varun.pocketassistant.data.SessionRepository$startSession$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: SessionRepository.kt */
    @Metadata(k = 3, mv = {2, 2, 0}, xi = 48)
    @DebugMetadata(c = "com.varun.pocketassistant.data.SessionRepository", f = "SessionRepository.kt", i = {2, 2}, l = {LockFreeTaskQueueCore.CLOSED_SHIFT, 62, 70}, m = "startSession", n = {"existing", "session"}, s = {"L$0", "L$1"})
    static final class C06731 extends ContinuationImpl {
        Object L$0;
        Object L$1;
        int label;
        /* synthetic */ Object result;

        C06731(Continuation<? super C06731> continuation) {
            super(continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            this.result = obj;
            this.label |= Integer.MIN_VALUE;
            return SessionRepository.this.startSession(this);
        }
    }

    /* JADX INFO: renamed from: com.varun.pocketassistant.data.SessionRepository$updateTranscript$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: SessionRepository.kt */
    @Metadata(k = 3, mv = {2, 2, 0}, xi = 48)
    @DebugMetadata(c = "com.varun.pocketassistant.data.SessionRepository", f = "SessionRepository.kt", i = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}, l = {139, 140}, m = "updateTranscript", n = {AsrWorker.KEY_SEGMENT_ID, NotificationCompat.CATEGORY_STATUS, "transcript", "diarized", "cleaned", "asrProvider", "cleanupProvider", "asrLastError", "skipReason", "speakerCandidatesJson", "youConfirmed", "clearAsrError", "clearSpeakerMeta", AsrWorker.KEY_SEGMENT_ID, NotificationCompat.CATEGORY_STATUS, "transcript", "diarized", "cleaned", "asrProvider", "cleanupProvider", "asrLastError", "skipReason", "speakerCandidatesJson", "youConfirmed", "current", "clearAsrError", "clearSpeakerMeta"}, s = {"L$0", "L$1", "L$2", "L$3", "L$4", "L$5", "L$6", "L$7", "L$8", "L$9", "L$10", "Z$0", "Z$1", "L$0", "L$1", "L$2", "L$3", "L$4", "L$5", "L$6", "L$7", "L$8", "L$9", "L$10", "L$11", "Z$0", "Z$1"})
    static final class C06741 extends ContinuationImpl {
        Object L$0;
        Object L$1;
        Object L$10;
        Object L$11;
        Object L$2;
        Object L$3;
        Object L$4;
        Object L$5;
        Object L$6;
        Object L$7;
        Object L$8;
        Object L$9;
        boolean Z$0;
        boolean Z$1;
        int label;
        /* synthetic */ Object result;

        C06741(Continuation<? super C06741> continuation) {
            super(continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            this.result = obj;
            this.label |= Integer.MIN_VALUE;
            return SessionRepository.this.updateTranscript(null, null, null, null, null, null, null, null, null, null, null, false, false, this);
        }
    }

    public SessionRepository(SessionDao sessionDao, SegmentDao segmentDao, AudioStorage audioStorage, RetentionPolicy retentionPolicy) {
        Intrinsics.checkNotNullParameter(sessionDao, "sessionDao");
        Intrinsics.checkNotNullParameter(segmentDao, "segmentDao");
        Intrinsics.checkNotNullParameter(audioStorage, "audioStorage");
        Intrinsics.checkNotNullParameter(retentionPolicy, "retentionPolicy");
        this.sessionDao = sessionDao;
        this.segmentDao = segmentDao;
        this.audioStorage = audioStorage;
        this.retentionPolicy = retentionPolicy;
    }

    public final PipelineScheduler getPipelineScheduler() {
        return this.pipelineScheduler;
    }

    public final void setPipelineScheduler(PipelineScheduler pipelineScheduler) {
        this.pipelineScheduler = pipelineScheduler;
    }

    public final Flow<List<SessionEntity>> observeSessions() {
        return this.sessionDao.observeSessions();
    }

    public final Flow<List<SegmentEntity>> observeSegments(String sessionId) {
        Intrinsics.checkNotNullParameter(sessionId, "sessionId");
        return this.segmentDao.observeForSession(sessionId);
    }

    public final Flow<List<SegmentEntity>> observeAllRecordings() {
        return this.segmentDao.observeAll();
    }

    public final Flow<List<SegmentEntity>> observeUncategorizedRecordings() {
        return this.segmentDao.observeUncategorized();
    }

    public final Object getSession(String id, Continuation<? super SessionEntity> continuation) {
        return this.sessionDao.getById(id, continuation);
    }

    public final Object getSegments(String sessionId, Continuation<? super List<SegmentEntity>> continuation) {
        return this.segmentDao.getForSession(sessionId, continuation);
    }

    public final Object getSegment(String id, Continuation<? super SegmentEntity> continuation) {
        return this.segmentDao.getById(id, continuation);
    }

    /* JADX WARN: Code duplicated, block: B:21:0x007c  */
    /* JADX WARN: Code duplicated, block: B:24:0x00de A[LOOP:1: B:19:0x0076->B:24:0x00de, LOOP_END] */
    /* JADX WARN: Code duplicated, block: B:28:0x00ff A[RETURN] */
    /* JADX WARN: Code duplicated, block: B:29:0x0100  */
    /* JADX WARN: Code duplicated, block: B:33:0x0119  */
    /* JADX WARN: Code duplicated, block: B:35:0x0127  */
    /* JADX WARN: Code duplicated, block: B:36:0x0129  */
    /* JADX WARN: Code duplicated, block: B:42:0x012c A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:44:0x0113 A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:47:0x00dd A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:7:0x001a  */
    public final Object getPendingWork(int limit, Continuation<? super List<SegmentEntity>> continuation) {
        C06681 c06681;
        Object byTranscriptStatus;
        int i;
        Iterable iterable;
        Iterator it;
        Object needingAsr;
        SegmentDao segmentDao;
        SegmentEntity segmentEntityCopy$default;
        Collection arrayList;
        boolean z;
        int limit2 = limit;
        if (continuation instanceof C06681) {
            c06681 = (C06681) continuation;
            if ((c06681.label & Integer.MIN_VALUE) != 0) {
                c06681.label -= Integer.MIN_VALUE;
            } else {
                c06681 = new C06681(continuation);
            }
        } else {
            c06681 = new C06681(continuation);
        }
        Object $result = c06681.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (c06681.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                SegmentDao segmentDao2 = this.segmentDao;
                c06681.I$0 = limit2;
                c06681.label = 1;
                byTranscriptStatus = segmentDao2.getByTranscriptStatus("PROCESSING", limit2, c06681);
                if (byTranscriptStatus == coroutine_suspended) {
                    return coroutine_suspended;
                }
                Iterable iterable2 = (Iterable) byTranscriptStatus;
                i = 0;
                iterable = iterable2;
                it = iterable2.iterator();
                while (it.hasNext()) {
                    Object next = it.next();
                    SegmentEntity segmentEntity = (SegmentEntity) next;
                    segmentDao = this.segmentDao;
                    segmentEntityCopy$default = SegmentEntity.copy$default(segmentEntity, null, null, null, 0L, 0L, 0L, 0L, "PENDING", null, null, null, null, null, null, null, null, System.currentTimeMillis(), null, false, null, 982911, null);
                    c06681.L$0 = SpillingKt.nullOutSpilledVariable(iterable);
                    c06681.L$1 = it;
                    c06681.L$2 = SpillingKt.nullOutSpilledVariable(next);
                    c06681.L$3 = SpillingKt.nullOutSpilledVariable(segmentEntity);
                    c06681.I$0 = limit2;
                    c06681.I$1 = i;
                    c06681.I$2 = 0;
                    c06681.label = 2;
                    if (segmentDao.update(segmentEntityCopy$default, c06681) == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                }
                SegmentDao segmentDao3 = this.segmentDao;
                c06681.L$0 = null;
                c06681.L$1 = null;
                c06681.L$2 = null;
                c06681.L$3 = null;
                c06681.I$0 = limit2;
                c06681.label = 3;
                needingAsr = segmentDao3.getNeedingAsr("PENDING", "FAILED", limit2, c06681);
                if (needingAsr == coroutine_suspended) {
                    return coroutine_suspended;
                }
                $result = needingAsr;
                arrayList = new ArrayList();
                for (Object obj : (Iterable) $result) {
                    if (((SegmentEntity) obj).getSkipReason() == null) {
                        z = true;
                    } else {
                        z = false;
                    }
                    if (z) {
                        arrayList.add(obj);
                    }
                }
                return (List) arrayList;
            case 1:
                limit2 = c06681.I$0;
                ResultKt.throwOnFailure($result);
                byTranscriptStatus = $result;
                Iterable iterable3 = (Iterable) byTranscriptStatus;
                i = 0;
                iterable = iterable3;
                it = iterable3.iterator();
                while (it.hasNext()) {
                    Object next2 = it.next();
                    SegmentEntity segmentEntity2 = (SegmentEntity) next2;
                    segmentDao = this.segmentDao;
                    segmentEntityCopy$default = SegmentEntity.copy$default(segmentEntity2, null, null, null, 0L, 0L, 0L, 0L, "PENDING", null, null, null, null, null, null, null, null, System.currentTimeMillis(), null, false, null, 982911, null);
                    c06681.L$0 = SpillingKt.nullOutSpilledVariable(iterable);
                    c06681.L$1 = it;
                    c06681.L$2 = SpillingKt.nullOutSpilledVariable(next2);
                    c06681.L$3 = SpillingKt.nullOutSpilledVariable(segmentEntity2);
                    c06681.I$0 = limit2;
                    c06681.I$1 = i;
                    c06681.I$2 = 0;
                    c06681.label = 2;
                    if (segmentDao.update(segmentEntityCopy$default, c06681) == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                }
                SegmentDao segmentDao4 = this.segmentDao;
                c06681.L$0 = null;
                c06681.L$1 = null;
                c06681.L$2 = null;
                c06681.L$3 = null;
                c06681.I$0 = limit2;
                c06681.label = 3;
                needingAsr = segmentDao4.getNeedingAsr("PENDING", "FAILED", limit2, c06681);
                if (needingAsr == coroutine_suspended) {
                    return coroutine_suspended;
                }
                $result = needingAsr;
                arrayList = new ArrayList();
                while (r11.hasNext()) {
                    if (((SegmentEntity) obj).getSkipReason() == null) {
                        z = true;
                    } else {
                        z = false;
                    }
                    if (z) {
                        arrayList.add(obj);
                    }
                }
                return (List) arrayList;
            case 2:
                int i2 = c06681.I$2;
                i = c06681.I$1;
                limit2 = c06681.I$0;
                Object obj2 = c06681.L$2;
                it = (Iterator) c06681.L$1;
                iterable = (Iterable) c06681.L$0;
                ResultKt.throwOnFailure($result);
                while (it.hasNext()) {
                    Object next3 = it.next();
                    SegmentEntity segmentEntity3 = (SegmentEntity) next3;
                    segmentDao = this.segmentDao;
                    segmentEntityCopy$default = SegmentEntity.copy$default(segmentEntity3, null, null, null, 0L, 0L, 0L, 0L, "PENDING", null, null, null, null, null, null, null, null, System.currentTimeMillis(), null, false, null, 982911, null);
                    c06681.L$0 = SpillingKt.nullOutSpilledVariable(iterable);
                    c06681.L$1 = it;
                    c06681.L$2 = SpillingKt.nullOutSpilledVariable(next3);
                    c06681.L$3 = SpillingKt.nullOutSpilledVariable(segmentEntity3);
                    c06681.I$0 = limit2;
                    c06681.I$1 = i;
                    c06681.I$2 = 0;
                    c06681.label = 2;
                    if (segmentDao.update(segmentEntityCopy$default, c06681) == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                }
                SegmentDao segmentDao5 = this.segmentDao;
                c06681.L$0 = null;
                c06681.L$1 = null;
                c06681.L$2 = null;
                c06681.L$3 = null;
                c06681.I$0 = limit2;
                c06681.label = 3;
                needingAsr = segmentDao5.getNeedingAsr("PENDING", "FAILED", limit2, c06681);
                if (needingAsr == coroutine_suspended) {
                    return coroutine_suspended;
                }
                $result = needingAsr;
                arrayList = new ArrayList();
                while (r11.hasNext()) {
                    if (((SegmentEntity) obj).getSkipReason() == null) {
                        z = true;
                    } else {
                        z = false;
                    }
                    if (z) {
                        arrayList.add(obj);
                    }
                }
                return (List) arrayList;
            case 3:
                int i3 = c06681.I$0;
                ResultKt.throwOnFailure($result);
                arrayList = new ArrayList();
                while (r11.hasNext()) {
                    if (((SegmentEntity) obj).getSkipReason() == null) {
                        z = true;
                    } else {
                        z = false;
                    }
                    if (z) {
                        arrayList.add(obj);
                    }
                }
                return (List) arrayList;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
    }

    /* JADX WARN: Code duplicated, block: B:20:0x005b  */
    /* JADX WARN: Code duplicated, block: B:22:0x0068 A[RETURN] */
    /* JADX WARN: Code duplicated, block: B:26:0x006e A[RETURN] */
    /* JADX WARN: Code duplicated, block: B:27:0x006f  */
    /* JADX WARN: Code duplicated, block: B:29:0x00a5 A[RETURN] */
    /* JADX WARN: Code duplicated, block: B:30:0x00a6  */
    /* JADX WARN: Code duplicated, block: B:7:0x0018  */
    public final Object startSession(Continuation<? super SessionEntity> continuation) {
        C06731 c06731;
        Object byStatus;
        SessionEntity existing;
        Object byStatus2;
        SessionEntity session;
        SessionDao sessionDao;
        SessionEntity session2;
        if (continuation instanceof C06731) {
            c06731 = (C06731) continuation;
            if ((c06731.label & Integer.MIN_VALUE) != 0) {
                c06731.label -= Integer.MIN_VALUE;
            } else {
                c06731 = new C06731(continuation);
            }
        } else {
            c06731 = new C06731(continuation);
        }
        Object $result = c06731.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (c06731.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                SessionDao sessionDao2 = this.sessionDao;
                c06731.label = 1;
                byStatus = sessionDao2.getByStatus("RECORDING", c06731);
                if (byStatus == coroutine_suspended) {
                    return coroutine_suspended;
                }
                existing = (SessionEntity) byStatus;
                if (existing == null) {
                    SessionDao sessionDao3 = this.sessionDao;
                    c06731.label = 2;
                    byStatus2 = sessionDao3.getByStatus("PAUSED", c06731);
                    if (byStatus2 == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    existing = (SessionEntity) byStatus2;
                }
                if (existing != null) {
                    return existing;
                }
                String string = UUID.randomUUID().toString();
                Intrinsics.checkNotNullExpressionValue(string, "toString(...)");
                session = new SessionEntity(string, System.currentTimeMillis(), null, "RECORDING", 0, 0L, null, 116, null);
                sessionDao = this.sessionDao;
                c06731.L$0 = SpillingKt.nullOutSpilledVariable(existing);
                c06731.L$1 = session;
                c06731.label = 3;
                if (sessionDao.insert(session, c06731) == coroutine_suspended) {
                    return coroutine_suspended;
                }
                session2 = session;
                this.audioStorage.ensureSessionDir(session2.getId());
                return session2;
            case 1:
                ResultKt.throwOnFailure($result);
                byStatus = $result;
                existing = (SessionEntity) byStatus;
                if (existing == null) {
                    SessionDao sessionDao4 = this.sessionDao;
                    c06731.label = 2;
                    byStatus2 = sessionDao4.getByStatus("PAUSED", c06731);
                    if (byStatus2 == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    existing = (SessionEntity) byStatus2;
                }
                if (existing != null) {
                    return existing;
                }
                String string2 = UUID.randomUUID().toString();
                Intrinsics.checkNotNullExpressionValue(string2, "toString(...)");
                session = new SessionEntity(string2, System.currentTimeMillis(), null, "RECORDING", 0, 0L, null, 116, null);
                sessionDao = this.sessionDao;
                c06731.L$0 = SpillingKt.nullOutSpilledVariable(existing);
                c06731.L$1 = session;
                c06731.label = 3;
                if (sessionDao.insert(session, c06731) == coroutine_suspended) {
                    return coroutine_suspended;
                }
                session2 = session;
                this.audioStorage.ensureSessionDir(session2.getId());
                return session2;
            case 2:
                ResultKt.throwOnFailure($result);
                byStatus2 = $result;
                existing = (SessionEntity) byStatus2;
                if (existing != null) {
                    return existing;
                }
                String string3 = UUID.randomUUID().toString();
                Intrinsics.checkNotNullExpressionValue(string3, "toString(...)");
                session = new SessionEntity(string3, System.currentTimeMillis(), null, "RECORDING", 0, 0L, null, 116, null);
                sessionDao = this.sessionDao;
                c06731.L$0 = SpillingKt.nullOutSpilledVariable(existing);
                c06731.L$1 = session;
                c06731.label = 3;
                if (sessionDao.insert(session, c06731) == coroutine_suspended) {
                    return coroutine_suspended;
                }
                session2 = session;
                this.audioStorage.ensureSessionDir(session2.getId());
                return session2;
            case 3:
                session2 = (SessionEntity) c06731.L$1;
                ResultKt.throwOnFailure($result);
                this.audioStorage.ensureSessionDir(session2.getId());
                return session2;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
    }

    /* JADX WARN: Code duplicated, block: B:20:0x0077  */
    /* JADX WARN: Code duplicated, block: B:22:0x007a  */
    /* JADX WARN: Code duplicated, block: B:24:0x0084  */
    /* JADX WARN: Code duplicated, block: B:25:0x008e  */
    /* JADX WARN: Code duplicated, block: B:28:0x00cb A[RETURN] */
    /* JADX WARN: Code duplicated, block: B:29:0x00cc  */
    /* JADX WARN: Code duplicated, block: B:7:0x0018  */
    public final Object setStatus(String sessionId, SessionStatus status, Continuation<? super Unit> continuation) {
        C06721 c06721;
        Object obj;
        SessionStatus status2;
        String sessionId2;
        SessionEntity current;
        SessionDao sessionDao;
        Long endedAtMs;
        SessionEntity sessionEntityCopy$default;
        if (continuation instanceof C06721) {
            c06721 = (C06721) continuation;
            if ((c06721.label & Integer.MIN_VALUE) != 0) {
                c06721.label -= Integer.MIN_VALUE;
            } else {
                c06721 = new C06721(continuation);
            }
        } else {
            c06721 = new C06721(continuation);
        }
        Object $result = c06721.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (c06721.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                SessionDao sessionDao2 = this.sessionDao;
                c06721.L$0 = SpillingKt.nullOutSpilledVariable(sessionId);
                c06721.L$1 = status;
                c06721.label = 1;
                Object byId = sessionDao2.getById(sessionId, c06721);
                if (byId == coroutine_suspended) {
                    return coroutine_suspended;
                }
                obj = byId;
                status2 = status;
                sessionId2 = sessionId;
                current = (SessionEntity) obj;
                if (current == null) {
                    return Unit.INSTANCE;
                }
                sessionDao = this.sessionDao;
                String strName = status2.name();
                if (status2 == SessionStatus.COMPLETED) {
                    endedAtMs = Boxing.boxLong(System.currentTimeMillis());
                } else {
                    endedAtMs = current.getEndedAtMs();
                }
                sessionEntityCopy$default = SessionEntity.copy$default(current, null, 0L, endedAtMs, strName, 0, 0L, null, 115, null);
                c06721.L$0 = SpillingKt.nullOutSpilledVariable(sessionId2);
                c06721.L$1 = SpillingKt.nullOutSpilledVariable(status2);
                c06721.L$2 = SpillingKt.nullOutSpilledVariable(current);
                c06721.label = 2;
                if (sessionDao.update(sessionEntityCopy$default, c06721) == coroutine_suspended) {
                    return coroutine_suspended;
                }
                return Unit.INSTANCE;
            case 1:
                status2 = (SessionStatus) c06721.L$1;
                sessionId2 = (String) c06721.L$0;
                ResultKt.throwOnFailure($result);
                obj = $result;
                current = (SessionEntity) obj;
                if (current == null) {
                    return Unit.INSTANCE;
                }
                sessionDao = this.sessionDao;
                String strName2 = status2.name();
                if (status2 == SessionStatus.COMPLETED) {
                    endedAtMs = Boxing.boxLong(System.currentTimeMillis());
                } else {
                    endedAtMs = current.getEndedAtMs();
                }
                sessionEntityCopy$default = SessionEntity.copy$default(current, null, 0L, endedAtMs, strName2, 0, 0L, null, 115, null);
                c06721.L$0 = SpillingKt.nullOutSpilledVariable(sessionId2);
                c06721.L$1 = SpillingKt.nullOutSpilledVariable(status2);
                c06721.L$2 = SpillingKt.nullOutSpilledVariable(current);
                c06721.label = 2;
                if (sessionDao.update(sessionEntityCopy$default, c06721) == coroutine_suspended) {
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

    public static /* synthetic */ Object addSegment$default(SessionRepository sessionRepository, String str, File file, long j, long j2, String str2, Continuation continuation, int i, Object obj) {
        String str3;
        if ((i & 16) == 0) {
            str3 = str2;
        } else {
            str3 = null;
        }
        return sessionRepository.addSegment(str, file, j, j2, str3, continuation);
    }

    /* JADX WARN: Code duplicated, block: B:21:0x0159 A[RETURN] */
    /* JADX WARN: Code duplicated, block: B:22:0x015a  */
    /* JADX WARN: Code duplicated, block: B:25:0x016f  */
    /* JADX WARN: Code duplicated, block: B:27:0x01bd A[RETURN] */
    /* JADX WARN: Code duplicated, block: B:28:0x01be  */
    /* JADX WARN: Code duplicated, block: B:32:0x01ca  */
    /* JADX WARN: Code duplicated, block: B:7:0x0018  */
    public final Object addSegment(String sessionId, File file, long startedAtMs, long endedAtMs, String endReason, Continuation<? super SegmentEntity> continuation) {
        AnonymousClass1 anonymousClass1;
        long startedAtMs2;
        SegmentEntity segment;
        long endedAtMs2;
        long now;
        String sessionId2;
        AnonymousClass1 anonymousClass2;
        Object obj;
        File file2;
        String endReason2;
        Object byId;
        String sessionId3;
        SegmentEntity segment2;
        long now2;
        long endedAtMs3;
        long startedAtMs3;
        SessionEntity session;
        SessionDao sessionDao;
        SessionEntity sessionEntityCopy$default;
        SegmentEntity segment3;
        String endReason3;
        File file3;
        String sessionId4;
        PipelineScheduler pipelineScheduler;
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
        switch (anonymousClass1.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                long now3 = System.currentTimeMillis();
                String string = UUID.randomUUID().toString();
                Intrinsics.checkNotNullExpressionValue(string, "toString(...)");
                String absolutePath = file.getAbsolutePath();
                Intrinsics.checkNotNullExpressionValue(absolutePath, "getAbsolutePath(...)");
                startedAtMs2 = startedAtMs;
                segment = new SegmentEntity(string, sessionId, absolutePath, startedAtMs2, endedAtMs, endedAtMs - startedAtMs, file.length(), "PENDING", null, null, null, null, null, null, null, null, now3, null, false, endReason, 458496, null);
                endedAtMs2 = endedAtMs;
                now = now3;
                SegmentDao segmentDao = this.segmentDao;
                sessionId2 = sessionId;
                anonymousClass2 = anonymousClass1;
                anonymousClass2.L$0 = sessionId2;
                anonymousClass2.L$1 = SpillingKt.nullOutSpilledVariable(file);
                anonymousClass2.L$2 = SpillingKt.nullOutSpilledVariable(endReason);
                anonymousClass2.L$3 = segment;
                anonymousClass2.J$0 = startedAtMs2;
                anonymousClass2.J$1 = endedAtMs2;
                anonymousClass2.J$2 = now;
                anonymousClass2.label = 1;
                obj = coroutine_suspended;
                if (segmentDao.insert(segment, anonymousClass2) == obj) {
                    return obj;
                }
                file2 = file;
                endReason2 = endReason;
                SessionDao sessionDao2 = this.sessionDao;
                anonymousClass2.L$0 = SpillingKt.nullOutSpilledVariable(sessionId2);
                anonymousClass2.L$1 = SpillingKt.nullOutSpilledVariable(file2);
                anonymousClass2.L$2 = SpillingKt.nullOutSpilledVariable(endReason2);
                anonymousClass2.L$3 = segment;
                anonymousClass2.J$0 = startedAtMs2;
                anonymousClass2.J$1 = endedAtMs2;
                anonymousClass2.J$2 = now;
                anonymousClass2.label = 2;
                byId = sessionDao2.getById(sessionId2, anonymousClass2);
                if (byId == obj) {
                    return obj;
                }
                sessionId3 = sessionId2;
                long j = endedAtMs2;
                segment2 = segment;
                $result = byId;
                now2 = now;
                endedAtMs3 = j;
                startedAtMs3 = startedAtMs2;
                session = (SessionEntity) $result;
                if (session != null) {
                    sessionDao = this.sessionDao;
                    sessionEntityCopy$default = SessionEntity.copy$default(session, null, 0L, null, null, session.getSegmentCount() + 1, session.getSpeechDurationMs() + segment2.getDurationMs(), null, 79, null);
                    anonymousClass2.L$0 = SpillingKt.nullOutSpilledVariable(sessionId3);
                    anonymousClass2.L$1 = SpillingKt.nullOutSpilledVariable(file2);
                    anonymousClass2.L$2 = SpillingKt.nullOutSpilledVariable(endReason2);
                    anonymousClass2.L$3 = segment2;
                    anonymousClass2.L$4 = SpillingKt.nullOutSpilledVariable(session);
                    anonymousClass2.J$0 = startedAtMs3;
                    anonymousClass2.J$1 = endedAtMs3;
                    anonymousClass2.J$2 = now2;
                    anonymousClass2.label = 3;
                    if (sessionDao.update(sessionEntityCopy$default, anonymousClass2) == obj) {
                        return obj;
                    }
                    segment3 = segment2;
                    endReason3 = endReason2;
                    file3 = file2;
                    sessionId4 = sessionId3;
                    segment2 = segment3;
                }
                pipelineScheduler = this.pipelineScheduler;
                if (pipelineScheduler != null) {
                    PipelineScheduler.enqueueAsr$default(pipelineScheduler, segment2.getId(), false, false, 6, null);
                }
                return segment2;
            case 1:
                now = anonymousClass1.J$2;
                long endedAtMs4 = anonymousClass1.J$1;
                long startedAtMs4 = anonymousClass1.J$0;
                SegmentEntity segment4 = (SegmentEntity) anonymousClass1.L$3;
                endReason2 = (String) anonymousClass1.L$2;
                file2 = (File) anonymousClass1.L$1;
                String sessionId5 = (String) anonymousClass1.L$0;
                ResultKt.throwOnFailure($result);
                anonymousClass2 = anonymousClass1;
                endedAtMs2 = endedAtMs4;
                segment = segment4;
                sessionId2 = sessionId5;
                obj = coroutine_suspended;
                startedAtMs2 = startedAtMs4;
                SessionDao sessionDao3 = this.sessionDao;
                anonymousClass2.L$0 = SpillingKt.nullOutSpilledVariable(sessionId2);
                anonymousClass2.L$1 = SpillingKt.nullOutSpilledVariable(file2);
                anonymousClass2.L$2 = SpillingKt.nullOutSpilledVariable(endReason2);
                anonymousClass2.L$3 = segment;
                anonymousClass2.J$0 = startedAtMs2;
                anonymousClass2.J$1 = endedAtMs2;
                anonymousClass2.J$2 = now;
                anonymousClass2.label = 2;
                byId = sessionDao3.getById(sessionId2, anonymousClass2);
                if (byId == obj) {
                    return obj;
                }
                sessionId3 = sessionId2;
                long j2 = endedAtMs2;
                segment2 = segment;
                $result = byId;
                now2 = now;
                endedAtMs3 = j2;
                startedAtMs3 = startedAtMs2;
                session = (SessionEntity) $result;
                if (session != null) {
                    sessionDao = this.sessionDao;
                    sessionEntityCopy$default = SessionEntity.copy$default(session, null, 0L, null, null, session.getSegmentCount() + 1, session.getSpeechDurationMs() + segment2.getDurationMs(), null, 79, null);
                    anonymousClass2.L$0 = SpillingKt.nullOutSpilledVariable(sessionId3);
                    anonymousClass2.L$1 = SpillingKt.nullOutSpilledVariable(file2);
                    anonymousClass2.L$2 = SpillingKt.nullOutSpilledVariable(endReason2);
                    anonymousClass2.L$3 = segment2;
                    anonymousClass2.L$4 = SpillingKt.nullOutSpilledVariable(session);
                    anonymousClass2.J$0 = startedAtMs3;
                    anonymousClass2.J$1 = endedAtMs3;
                    anonymousClass2.J$2 = now2;
                    anonymousClass2.label = 3;
                    if (sessionDao.update(sessionEntityCopy$default, anonymousClass2) == obj) {
                        return obj;
                    }
                    segment3 = segment2;
                    endReason3 = endReason2;
                    file3 = file2;
                    sessionId4 = sessionId3;
                    segment2 = segment3;
                }
                pipelineScheduler = this.pipelineScheduler;
                if (pipelineScheduler != null) {
                    PipelineScheduler.enqueueAsr$default(pipelineScheduler, segment2.getId(), false, false, 6, null);
                }
                return segment2;
            case 2:
                long now4 = anonymousClass1.J$2;
                long endedAtMs5 = anonymousClass1.J$1;
                long startedAtMs5 = anonymousClass1.J$0;
                SegmentEntity segment5 = (SegmentEntity) anonymousClass1.L$3;
                endReason2 = (String) anonymousClass1.L$2;
                file2 = (File) anonymousClass1.L$1;
                sessionId3 = (String) anonymousClass1.L$0;
                ResultKt.throwOnFailure($result);
                segment2 = segment5;
                anonymousClass2 = anonymousClass1;
                obj = coroutine_suspended;
                now2 = now4;
                endedAtMs3 = endedAtMs5;
                startedAtMs3 = startedAtMs5;
                session = (SessionEntity) $result;
                if (session != null) {
                    sessionDao = this.sessionDao;
                    sessionEntityCopy$default = SessionEntity.copy$default(session, null, 0L, null, null, session.getSegmentCount() + 1, session.getSpeechDurationMs() + segment2.getDurationMs(), null, 79, null);
                    anonymousClass2.L$0 = SpillingKt.nullOutSpilledVariable(sessionId3);
                    anonymousClass2.L$1 = SpillingKt.nullOutSpilledVariable(file2);
                    anonymousClass2.L$2 = SpillingKt.nullOutSpilledVariable(endReason2);
                    anonymousClass2.L$3 = segment2;
                    anonymousClass2.L$4 = SpillingKt.nullOutSpilledVariable(session);
                    anonymousClass2.J$0 = startedAtMs3;
                    anonymousClass2.J$1 = endedAtMs3;
                    anonymousClass2.J$2 = now2;
                    anonymousClass2.label = 3;
                    if (sessionDao.update(sessionEntityCopy$default, anonymousClass2) == obj) {
                        return obj;
                    }
                    segment3 = segment2;
                    endReason3 = endReason2;
                    file3 = file2;
                    sessionId4 = sessionId3;
                    segment2 = segment3;
                }
                pipelineScheduler = this.pipelineScheduler;
                if (pipelineScheduler != null) {
                    PipelineScheduler.enqueueAsr$default(pipelineScheduler, segment2.getId(), false, false, 6, null);
                }
                return segment2;
            case 3:
                long j3 = anonymousClass1.J$2;
                long j4 = anonymousClass1.J$1;
                long j5 = anonymousClass1.J$0;
                segment3 = (SegmentEntity) anonymousClass1.L$3;
                endReason3 = (String) anonymousClass1.L$2;
                file3 = (File) anonymousClass1.L$1;
                sessionId4 = (String) anonymousClass1.L$0;
                ResultKt.throwOnFailure($result);
                segment2 = segment3;
                pipelineScheduler = this.pipelineScheduler;
                if (pipelineScheduler != null) {
                    PipelineScheduler.enqueueAsr$default(pipelineScheduler, segment2.getId(), false, false, 6, null);
                }
                return segment2;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
    }

    /* JADX WARN: Code duplicated, block: B:20:0x0117  */
    /* JADX WARN: Code duplicated, block: B:22:0x011a  */
    /* JADX WARN: Code duplicated, block: B:24:0x0124  */
    /* JADX WARN: Code duplicated, block: B:25:0x012b  */
    /* JADX WARN: Code duplicated, block: B:27:0x012f  */
    /* JADX WARN: Code duplicated, block: B:28:0x0136  */
    /* JADX WARN: Code duplicated, block: B:30:0x013a  */
    /* JADX WARN: Code duplicated, block: B:31:0x0141  */
    /* JADX WARN: Code duplicated, block: B:33:0x0145  */
    /* JADX WARN: Code duplicated, block: B:34:0x014c  */
    /* JADX WARN: Code duplicated, block: B:36:0x0150  */
    /* JADX WARN: Code duplicated, block: B:37:0x0157  */
    /* JADX WARN: Code duplicated, block: B:40:0x015e  */
    /* JADX WARN: Code duplicated, block: B:41:0x0161 A[DONT_INVERT] */
    /* JADX WARN: Code duplicated, block: B:42:0x0163  */
    /* JADX WARN: Code duplicated, block: B:43:0x016a  */
    /* JADX WARN: Code duplicated, block: B:46:0x016f  */
    /* JADX WARN: Code duplicated, block: B:47:0x0176  */
    /* JADX WARN: Code duplicated, block: B:50:0x017b  */
    /* JADX WARN: Code duplicated, block: B:51:0x017e A[DONT_INVERT] */
    /* JADX WARN: Code duplicated, block: B:52:0x0180  */
    /* JADX WARN: Code duplicated, block: B:53:0x0187  */
    /* JADX WARN: Code duplicated, block: B:56:0x018c  */
    /* JADX WARN: Code duplicated, block: B:58:0x0193 A[DONT_INVERT] */
    /* JADX WARN: Code duplicated, block: B:59:0x0195  */
    /* JADX WARN: Code duplicated, block: B:60:0x0198  */
    /* JADX WARN: Code duplicated, block: B:63:0x0220 A[RETURN] */
    /* JADX WARN: Code duplicated, block: B:64:0x0221  */
    /* JADX WARN: Code duplicated, block: B:7:0x0018  */
    public final Object updateTranscript(String segmentId, TranscriptStatus status, String transcript, String diarized, String cleaned, String asrProvider, String cleanupProvider, String asrLastError, String skipReason, String speakerCandidatesJson, Boolean youConfirmed, boolean clearAsrError, boolean clearSpeakerMeta, Continuation<? super Unit> continuation) {
        C06741 c06741;
        TranscriptStatus status2;
        String transcript2;
        String speakerCandidatesJson2;
        String skipReason2;
        String asrLastError2;
        String cleanupProvider2;
        String asrProvider2;
        String cleaned2;
        String diarized2;
        Boolean youConfirmed2;
        boolean clearAsrError2;
        Object obj;
        String segmentId2;
        boolean clearSpeakerMeta2;
        SegmentEntity current;
        SegmentDao segmentDao;
        String transcript3;
        String diarizedTranscript;
        String cleanedTranscript;
        String asrProvider3;
        String cleanupProvider3;
        String asrLastError3;
        String skipReason3;
        String speakerCandidatesJson3;
        boolean youConfirmed3;
        SegmentEntity segmentEntityCopy$default;
        if (continuation instanceof C06741) {
            c06741 = (C06741) continuation;
            if ((c06741.label & Integer.MIN_VALUE) != 0) {
                c06741.label -= Integer.MIN_VALUE;
            } else {
                c06741 = new C06741(continuation);
            }
        } else {
            c06741 = new C06741(continuation);
        }
        Object $result = c06741.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (c06741.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                SegmentDao segmentDao2 = this.segmentDao;
                c06741.L$0 = SpillingKt.nullOutSpilledVariable(segmentId);
                status2 = status;
                c06741.L$1 = status2;
                transcript2 = transcript;
                c06741.L$2 = transcript2;
                speakerCandidatesJson2 = diarized;
                c06741.L$3 = speakerCandidatesJson2;
                skipReason2 = cleaned;
                c06741.L$4 = skipReason2;
                asrLastError2 = asrProvider;
                c06741.L$5 = asrLastError2;
                cleanupProvider2 = cleanupProvider;
                c06741.L$6 = cleanupProvider2;
                asrProvider2 = asrLastError;
                c06741.L$7 = asrProvider2;
                cleaned2 = skipReason;
                c06741.L$8 = cleaned2;
                diarized2 = speakerCandidatesJson;
                c06741.L$9 = diarized2;
                youConfirmed2 = youConfirmed;
                c06741.L$10 = youConfirmed2;
                clearAsrError2 = clearAsrError;
                c06741.Z$0 = clearAsrError2;
                c06741.Z$1 = clearSpeakerMeta;
                c06741.label = 1;
                Object byId = segmentDao2.getById(segmentId, c06741);
                if (byId == coroutine_suspended) {
                    return coroutine_suspended;
                }
                obj = byId;
                segmentId2 = segmentId;
                clearSpeakerMeta2 = clearSpeakerMeta;
                current = (SegmentEntity) obj;
                if (current == null) {
                    return Unit.INSTANCE;
                }
                String segmentId3 = segmentId2;
                segmentDao = this.segmentDao;
                String strName = status2.name();
                if (transcript2 == null) {
                    transcript3 = current.getTranscript();
                } else {
                    transcript3 = transcript2;
                }
                if (speakerCandidatesJson2 == null) {
                    diarizedTranscript = current.getDiarizedTranscript();
                } else {
                    diarizedTranscript = speakerCandidatesJson2;
                }
                if (skipReason2 == null) {
                    cleanedTranscript = current.getCleanedTranscript();
                } else {
                    cleanedTranscript = skipReason2;
                }
                if (asrLastError2 == null) {
                    asrProvider3 = current.getAsrProvider();
                } else {
                    asrProvider3 = asrLastError2;
                }
                if (cleanupProvider2 == null) {
                    cleanupProvider3 = current.getCleanupProvider();
                } else {
                    cleanupProvider3 = cleanupProvider2;
                }
                if (clearAsrError2) {
                    asrLastError3 = null;
                } else if (asrProvider2 == null) {
                    asrLastError3 = asrProvider2;
                } else {
                    asrLastError3 = current.getAsrLastError();
                }
                if (cleaned2 == null) {
                    skipReason3 = current.getSkipReason();
                } else {
                    skipReason3 = cleaned2;
                }
                if (clearSpeakerMeta2) {
                    speakerCandidatesJson3 = null;
                } else if (diarized2 == null) {
                    speakerCandidatesJson3 = diarized2;
                } else {
                    speakerCandidatesJson3 = current.getSpeakerCandidatesJson();
                }
                if (youConfirmed2 != null) {
                    youConfirmed3 = youConfirmed2.booleanValue();
                } else if (clearSpeakerMeta2) {
                    youConfirmed3 = false;
                } else {
                    youConfirmed3 = current.getYouConfirmed();
                }
                segmentEntityCopy$default = SegmentEntity.copy$default(current, null, null, null, 0L, 0L, 0L, 0L, strName, transcript3, diarizedTranscript, cleanedTranscript, asrProvider3, cleanupProvider3, null, asrLastError3, skipReason3, System.currentTimeMillis(), speakerCandidatesJson3, youConfirmed3, null, 532607, null);
                c06741.L$0 = SpillingKt.nullOutSpilledVariable(segmentId3);
                c06741.L$1 = SpillingKt.nullOutSpilledVariable(status2);
                c06741.L$2 = SpillingKt.nullOutSpilledVariable(transcript2);
                c06741.L$3 = SpillingKt.nullOutSpilledVariable(speakerCandidatesJson2);
                c06741.L$4 = SpillingKt.nullOutSpilledVariable(skipReason2);
                c06741.L$5 = SpillingKt.nullOutSpilledVariable(asrLastError2);
                c06741.L$6 = SpillingKt.nullOutSpilledVariable(cleanupProvider2);
                c06741.L$7 = SpillingKt.nullOutSpilledVariable(asrProvider2);
                c06741.L$8 = SpillingKt.nullOutSpilledVariable(cleaned2);
                c06741.L$9 = SpillingKt.nullOutSpilledVariable(diarized2);
                c06741.L$10 = SpillingKt.nullOutSpilledVariable(youConfirmed2);
                c06741.L$11 = SpillingKt.nullOutSpilledVariable(current);
                c06741.Z$0 = clearAsrError2;
                c06741.Z$1 = clearSpeakerMeta2;
                c06741.label = 2;
                if (segmentDao.update(segmentEntityCopy$default, c06741) == coroutine_suspended) {
                    return coroutine_suspended;
                }
                return Unit.INSTANCE;
            case 1:
                boolean clearSpeakerMeta3 = c06741.Z$1;
                boolean clearAsrError3 = c06741.Z$0;
                Boolean youConfirmed4 = (Boolean) c06741.L$10;
                String speakerCandidatesJson4 = (String) c06741.L$9;
                String skipReason4 = (String) c06741.L$8;
                String asrLastError4 = (String) c06741.L$7;
                cleanupProvider2 = (String) c06741.L$6;
                String asrProvider4 = (String) c06741.L$5;
                String cleaned3 = (String) c06741.L$4;
                String diarized3 = (String) c06741.L$3;
                String transcript4 = (String) c06741.L$2;
                TranscriptStatus status3 = (TranscriptStatus) c06741.L$1;
                segmentId2 = (String) c06741.L$0;
                ResultKt.throwOnFailure($result);
                clearAsrError2 = clearAsrError3;
                status2 = status3;
                youConfirmed2 = youConfirmed4;
                transcript2 = transcript4;
                diarized2 = speakerCandidatesJson4;
                speakerCandidatesJson2 = diarized3;
                cleaned2 = skipReason4;
                skipReason2 = cleaned3;
                asrProvider2 = asrLastError4;
                asrLastError2 = asrProvider4;
                obj = $result;
                clearSpeakerMeta2 = clearSpeakerMeta3;
                current = (SegmentEntity) obj;
                if (current == null) {
                    return Unit.INSTANCE;
                }
                String segmentId4 = segmentId2;
                segmentDao = this.segmentDao;
                String strName2 = status2.name();
                if (transcript2 == null) {
                    transcript3 = current.getTranscript();
                } else {
                    transcript3 = transcript2;
                }
                if (speakerCandidatesJson2 == null) {
                    diarizedTranscript = current.getDiarizedTranscript();
                } else {
                    diarizedTranscript = speakerCandidatesJson2;
                }
                if (skipReason2 == null) {
                    cleanedTranscript = current.getCleanedTranscript();
                } else {
                    cleanedTranscript = skipReason2;
                }
                if (asrLastError2 == null) {
                    asrProvider3 = current.getAsrProvider();
                } else {
                    asrProvider3 = asrLastError2;
                }
                if (cleanupProvider2 == null) {
                    cleanupProvider3 = current.getCleanupProvider();
                } else {
                    cleanupProvider3 = cleanupProvider2;
                }
                if (clearAsrError2) {
                    asrLastError3 = null;
                } else if (asrProvider2 == null) {
                    asrLastError3 = asrProvider2;
                } else {
                    asrLastError3 = current.getAsrLastError();
                }
                if (cleaned2 == null) {
                    skipReason3 = current.getSkipReason();
                } else {
                    skipReason3 = cleaned2;
                }
                if (clearSpeakerMeta2) {
                    speakerCandidatesJson3 = null;
                } else if (diarized2 == null) {
                    speakerCandidatesJson3 = diarized2;
                } else {
                    speakerCandidatesJson3 = current.getSpeakerCandidatesJson();
                }
                if (youConfirmed2 != null) {
                    youConfirmed3 = youConfirmed2.booleanValue();
                } else if (clearSpeakerMeta2) {
                    youConfirmed3 = false;
                } else {
                    youConfirmed3 = current.getYouConfirmed();
                }
                segmentEntityCopy$default = SegmentEntity.copy$default(current, null, null, null, 0L, 0L, 0L, 0L, strName2, transcript3, diarizedTranscript, cleanedTranscript, asrProvider3, cleanupProvider3, null, asrLastError3, skipReason3, System.currentTimeMillis(), speakerCandidatesJson3, youConfirmed3, null, 532607, null);
                c06741.L$0 = SpillingKt.nullOutSpilledVariable(segmentId4);
                c06741.L$1 = SpillingKt.nullOutSpilledVariable(status2);
                c06741.L$2 = SpillingKt.nullOutSpilledVariable(transcript2);
                c06741.L$3 = SpillingKt.nullOutSpilledVariable(speakerCandidatesJson2);
                c06741.L$4 = SpillingKt.nullOutSpilledVariable(skipReason2);
                c06741.L$5 = SpillingKt.nullOutSpilledVariable(asrLastError2);
                c06741.L$6 = SpillingKt.nullOutSpilledVariable(cleanupProvider2);
                c06741.L$7 = SpillingKt.nullOutSpilledVariable(asrProvider2);
                c06741.L$8 = SpillingKt.nullOutSpilledVariable(cleaned2);
                c06741.L$9 = SpillingKt.nullOutSpilledVariable(diarized2);
                c06741.L$10 = SpillingKt.nullOutSpilledVariable(youConfirmed2);
                c06741.L$11 = SpillingKt.nullOutSpilledVariable(current);
                c06741.Z$0 = clearAsrError2;
                c06741.Z$1 = clearSpeakerMeta2;
                c06741.label = 2;
                if (segmentDao.update(segmentEntityCopy$default, c06741) == coroutine_suspended) {
                    return coroutine_suspended;
                }
                return Unit.INSTANCE;
            case 2:
                boolean z = c06741.Z$1;
                boolean z2 = c06741.Z$0;
                ResultKt.throwOnFailure($result);
                return Unit.INSTANCE;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
    }

    /* JADX WARN: Code duplicated, block: B:20:0x007e  */
    /* JADX WARN: Code duplicated, block: B:22:0x0083  */
    /* JADX WARN: Code duplicated, block: B:24:0x0089  */
    /* JADX WARN: Code duplicated, block: B:27:0x0095  */
    /* JADX WARN: Code duplicated, block: B:30:0x009a  */
    /* JADX WARN: Code duplicated, block: B:32:0x0127 A[RETURN] */
    /* JADX WARN: Code duplicated, block: B:33:0x0128  */
    /* JADX WARN: Code duplicated, block: B:7:0x0018  */
    public final Object confirmYouSpeaker(String segmentId, int youSpeakerId, Continuation<? super Boolean> continuation) {
        C06661 c06661;
        Object obj;
        int youSpeakerId2;
        String segmentId2;
        SegmentEntity seg;
        String diarizedTranscript;
        SegmentDao segmentDao;
        SegmentEntity segmentEntityCopy$default;
        if (continuation instanceof C06661) {
            c06661 = (C06661) continuation;
            if ((c06661.label & Integer.MIN_VALUE) != 0) {
                c06661.label -= Integer.MIN_VALUE;
            } else {
                c06661 = new C06661(continuation);
            }
        } else {
            c06661 = new C06661(continuation);
        }
        Object $result = c06661.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (c06661.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                SegmentDao segmentDao2 = this.segmentDao;
                c06661.L$0 = SpillingKt.nullOutSpilledVariable(segmentId);
                c06661.I$0 = youSpeakerId;
                c06661.label = 1;
                Object byId = segmentDao2.getById(segmentId, c06661);
                if (byId == coroutine_suspended) {
                    return coroutine_suspended;
                }
                obj = byId;
                youSpeakerId2 = youSpeakerId;
                segmentId2 = segmentId;
                seg = (SegmentEntity) obj;
                if (seg == null) {
                    return Boxing.boxBoolean(false);
                }
                diarizedTranscript = seg.getDiarizedTranscript();
                if (diarizedTranscript != null) {
                    if (StringsKt.isBlank(diarizedTranscript)) {
                        diarizedTranscript = null;
                    }
                    if (diarizedTranscript != null) {
                        String raw = diarizedTranscript;
                        String labeled = DiarizationLabels.INSTANCE.applyYou(raw, youSpeakerId2);
                        segmentDao = this.segmentDao;
                        segmentEntityCopy$default = SegmentEntity.copy$default(seg, null, null, null, 0L, 0L, 0L, 0L, null, null, labeled, null, null, null, null, null, null, System.currentTimeMillis(), null, true, null, 720383, null);
                        c06661.L$0 = SpillingKt.nullOutSpilledVariable(segmentId2);
                        c06661.L$1 = SpillingKt.nullOutSpilledVariable(seg);
                        c06661.L$2 = SpillingKt.nullOutSpilledVariable(raw);
                        c06661.L$3 = SpillingKt.nullOutSpilledVariable(labeled);
                        c06661.I$0 = youSpeakerId2;
                        c06661.label = 2;
                        if (segmentDao.update(segmentEntityCopy$default, c06661) == coroutine_suspended) {
                            return coroutine_suspended;
                        }
                        return Boxing.boxBoolean(true);
                    }
                }
                return Boxing.boxBoolean(false);
            case 1:
                youSpeakerId2 = c06661.I$0;
                segmentId2 = (String) c06661.L$0;
                ResultKt.throwOnFailure($result);
                obj = $result;
                seg = (SegmentEntity) obj;
                if (seg == null) {
                    return Boxing.boxBoolean(false);
                }
                diarizedTranscript = seg.getDiarizedTranscript();
                if (diarizedTranscript != null) {
                    if (StringsKt.isBlank(diarizedTranscript)) {
                        diarizedTranscript = null;
                    }
                    if (diarizedTranscript != null) {
                        String raw2 = diarizedTranscript;
                        String labeled2 = DiarizationLabels.INSTANCE.applyYou(raw2, youSpeakerId2);
                        segmentDao = this.segmentDao;
                        segmentEntityCopy$default = SegmentEntity.copy$default(seg, null, null, null, 0L, 0L, 0L, 0L, null, null, labeled2, null, null, null, null, null, null, System.currentTimeMillis(), null, true, null, 720383, null);
                        c06661.L$0 = SpillingKt.nullOutSpilledVariable(segmentId2);
                        c06661.L$1 = SpillingKt.nullOutSpilledVariable(seg);
                        c06661.L$2 = SpillingKt.nullOutSpilledVariable(raw2);
                        c06661.L$3 = SpillingKt.nullOutSpilledVariable(labeled2);
                        c06661.I$0 = youSpeakerId2;
                        c06661.label = 2;
                        if (segmentDao.update(segmentEntityCopy$default, c06661) == coroutine_suspended) {
                            return coroutine_suspended;
                        }
                        return Boxing.boxBoolean(true);
                    }
                }
                return Boxing.boxBoolean(false);
            case 2:
                int i = c06661.I$0;
                ResultKt.throwOnFailure($result);
                return Boxing.boxBoolean(true);
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
    }

    /* JADX WARN: Code duplicated, block: B:42:0x0132  */
    /* JADX WARN: Code duplicated, block: B:46:0x01a2 A[RETURN] */
    /* JADX WARN: Code duplicated, block: B:47:0x01a3  */
    /* JADX WARN: Code duplicated, block: B:50:0x01a8  */
    /* JADX WARN: Code duplicated, block: B:56:0x0140 A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:58:? A[LOOP:0: B:40:0x012c->B:58:?, LOOP_END, SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:7:0x0018  */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:47:0x01a3 -> B:48:0x01a4). Please report as a decompilation issue!!! */
    /*  JADX ERROR: JadxOverflowException in pass: RegionMakerVisitor
        jadx.core.utils.exceptions.JadxOverflowException: Regions stack size limit reached
        	at jadx.core.utils.ErrorsCounter.addError(ErrorsCounter.java:59)
        	at jadx.core.utils.ErrorsCounter.error(ErrorsCounter.java:31)
        	at jadx.core.dex.attributes.nodes.NotificationAttrNode.addError(NotificationAttrNode.java:19)
        */
    public final java.lang.Object requeueAllForAsr(kotlin.coroutines.Continuation<? super kotlin.Unit> r41) {
        /*
            Method dump skipped, instruction units count: 462
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.varun.pocketassistant.data.SessionRepository.requeueAllForAsr(kotlin.coroutines.Continuation):java.lang.Object");
    }

    /* JADX WARN: Code duplicated, block: B:21:0x00ae  */
    /* JADX WARN: Code duplicated, block: B:23:0x00bd  */
    /* JADX WARN: Code duplicated, block: B:27:0x013e A[RETURN] */
    /* JADX WARN: Code duplicated, block: B:28:0x013f  */
    /* JADX WARN: Code duplicated, block: B:31:0x0156  */
    /* JADX WARN: Code duplicated, block: B:40:0x00cd A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:7:0x0018  */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:28:0x013f -> B:29:0x0150). Please report as a decompilation issue!!! */
    /*  JADX ERROR: JadxOverflowException in pass: RegionMakerVisitor
        jadx.core.utils.exceptions.JadxOverflowException: Regions stack size limit reached
        	at jadx.core.utils.ErrorsCounter.addError(ErrorsCounter.java:59)
        	at jadx.core.utils.ErrorsCounter.error(ErrorsCounter.java:31)
        	at jadx.core.dex.attributes.nodes.NotificationAttrNode.addError(NotificationAttrNode.java:19)
        */
    public final java.lang.Object requeueAsrInRange(long r50, long r52, kotlin.coroutines.Continuation<? super java.lang.Integer> r54) {
        /*
            Method dump skipped, instruction units count: 438
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.varun.pocketassistant.data.SessionRepository.requeueAsrInRange(long, long, kotlin.coroutines.Continuation):java.lang.Object");
    }

    /* JADX WARN: Code duplicated, block: B:27:0x00c6 A[RETURN] */
    /* JADX WARN: Code duplicated, block: B:28:0x00c7  */
    /* JADX WARN: Code duplicated, block: B:31:0x00cd  */
    /* JADX WARN: Code duplicated, block: B:7:0x001a  */
    public final Object requeueForAsr(String segmentId, Continuation<? super Boolean> continuation) {
        C06711 c06711;
        Object byId;
        SegmentEntity seg;
        SegmentDao segmentDao;
        SegmentEntity segmentEntityCopy$default;
        String segmentId2;
        PipelineScheduler pipelineScheduler;
        String segmentId3 = segmentId;
        if (continuation instanceof C06711) {
            c06711 = (C06711) continuation;
            if ((c06711.label & Integer.MIN_VALUE) != 0) {
                c06711.label -= Integer.MIN_VALUE;
            } else {
                c06711 = new C06711(continuation);
            }
        } else {
            c06711 = new C06711(continuation);
        }
        Object $result = c06711.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (c06711.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                SegmentDao segmentDao2 = this.segmentDao;
                c06711.L$0 = segmentId3;
                c06711.label = 1;
                byId = segmentDao2.getById(segmentId3, c06711);
                if (byId == coroutine_suspended) {
                    return coroutine_suspended;
                }
                seg = (SegmentEntity) byId;
                if (seg == null && seg.getSkipReason() == null) {
                    segmentDao = this.segmentDao;
                    segmentEntityCopy$default = SegmentEntity.copy$default(seg, null, null, null, 0L, 0L, 0L, 0L, "PENDING", null, null, null, null, null, null, null, null, System.currentTimeMillis(), null, false, null, 565375, null);
                    c06711.L$0 = segmentId3;
                    c06711.L$1 = SpillingKt.nullOutSpilledVariable(seg);
                    c06711.label = 2;
                    if (segmentDao.update(segmentEntityCopy$default, c06711) == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    segmentId2 = segmentId3;
                    pipelineScheduler = this.pipelineScheduler;
                    if (pipelineScheduler != null) {
                        PipelineScheduler.enqueueAsr$default(pipelineScheduler, segmentId2, true, false, 4, null);
                    }
                    return Boxing.boxBoolean(true);
                }
                return Boxing.boxBoolean(false);
            case 1:
                segmentId3 = (String) c06711.L$0;
                ResultKt.throwOnFailure($result);
                byId = $result;
                seg = (SegmentEntity) byId;
                if (seg == null) {
                    return Boxing.boxBoolean(false);
                }
                segmentDao = this.segmentDao;
                segmentEntityCopy$default = SegmentEntity.copy$default(seg, null, null, null, 0L, 0L, 0L, 0L, "PENDING", null, null, null, null, null, null, null, null, System.currentTimeMillis(), null, false, null, 565375, null);
                c06711.L$0 = segmentId3;
                c06711.L$1 = SpillingKt.nullOutSpilledVariable(seg);
                c06711.label = 2;
                if (segmentDao.update(segmentEntityCopy$default, c06711) == coroutine_suspended) {
                    return coroutine_suspended;
                }
                segmentId2 = segmentId3;
                pipelineScheduler = this.pipelineScheduler;
                if (pipelineScheduler != null) {
                    PipelineScheduler.enqueueAsr$default(pipelineScheduler, segmentId2, true, false, 4, null);
                }
                return Boxing.boxBoolean(true);
            case 2:
                String segmentId4 = (String) c06711.L$0;
                ResultKt.throwOnFailure($result);
                segmentId2 = segmentId4;
                pipelineScheduler = this.pipelineScheduler;
                if (pipelineScheduler != null) {
                    PipelineScheduler.enqueueAsr$default(pipelineScheduler, segmentId2, true, false, 4, null);
                }
                return Boxing.boxBoolean(true);
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
    }

    /* JADX WARN: Code duplicated, block: B:19:0x005c A[RETURN] */
    /* JADX WARN: Code duplicated, block: B:7:0x0014  */
    public final Object deleteSession(String sessionId, Continuation<? super Unit> continuation) {
        C06671 c06671;
        SessionDao sessionDao;
        if (continuation instanceof C06671) {
            c06671 = (C06671) continuation;
            if ((c06671.label & Integer.MIN_VALUE) != 0) {
                c06671.label -= Integer.MIN_VALUE;
            } else {
                c06671 = new C06671(continuation);
            }
        } else {
            c06671 = new C06671(continuation);
        }
        Object $result = c06671.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (c06671.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                SegmentDao segmentDao = this.segmentDao;
                c06671.L$0 = sessionId;
                c06671.label = 1;
                if (segmentDao.deleteForSession(sessionId, c06671) == coroutine_suspended) {
                    return coroutine_suspended;
                }
                sessionDao = this.sessionDao;
                c06671.L$0 = sessionId;
                c06671.label = 2;
                if (sessionDao.delete(sessionId, c06671) == coroutine_suspended) {
                    return coroutine_suspended;
                }
                this.audioStorage.deleteSessionDir(sessionId);
                return Unit.INSTANCE;
            case 1:
                sessionId = (String) c06671.L$0;
                ResultKt.throwOnFailure($result);
                sessionDao = this.sessionDao;
                c06671.L$0 = sessionId;
                c06671.label = 2;
                if (sessionDao.delete(sessionId, c06671) == coroutine_suspended) {
                    return coroutine_suspended;
                }
                this.audioStorage.deleteSessionDir(sessionId);
                return Unit.INSTANCE;
            case 2:
                sessionId = (String) c06671.L$0;
                ResultKt.throwOnFailure($result);
                this.audioStorage.deleteSessionDir(sessionId);
                return Unit.INSTANCE;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
    }

    /* JADX WARN: Code duplicated, block: B:20:0x007c  */
    /* JADX WARN: Code duplicated, block: B:27:0x00ab A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:29:? A[LOOP:0: B:18:0x0076->B:29:?, LOOP_END, SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:7:0x0018  */
    public final Object applyRetention(Continuation<? super Unit> continuation) {
        C06651 c06651;
        long cutoff;
        Object endedBefore;
        Iterable iterable;
        Iterator it;
        int i;
        long cutoff2;
        String id;
        if (continuation instanceof C06651) {
            c06651 = (C06651) continuation;
            if ((c06651.label & Integer.MIN_VALUE) != 0) {
                c06651.label -= Integer.MIN_VALUE;
            } else {
                c06651 = new C06651(continuation);
            }
        } else {
            c06651 = new C06651(continuation);
        }
        Object $result = c06651.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (c06651.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                cutoff = this.retentionPolicy.cutoffEpochMs(System.currentTimeMillis());
                SessionDao sessionDao = this.sessionDao;
                c06651.J$0 = cutoff;
                c06651.label = 1;
                endedBefore = sessionDao.getEndedBefore(cutoff, c06651);
                if (endedBefore == coroutine_suspended) {
                    return coroutine_suspended;
                }
                Iterable iterable2 = (Iterable) endedBefore;
                iterable = iterable2;
                it = iterable2.iterator();
                i = 0;
                cutoff2 = cutoff;
                while (it.hasNext()) {
                    Object next = it.next();
                    SessionEntity sessionEntity = (SessionEntity) next;
                    id = sessionEntity.getId();
                    c06651.L$0 = SpillingKt.nullOutSpilledVariable(iterable);
                    c06651.L$1 = it;
                    c06651.L$2 = SpillingKt.nullOutSpilledVariable(next);
                    c06651.L$3 = SpillingKt.nullOutSpilledVariable(sessionEntity);
                    c06651.J$0 = cutoff2;
                    c06651.I$0 = i;
                    c06651.I$1 = 0;
                    c06651.label = 2;
                    if (deleteSession(id, c06651) == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                }
                return Unit.INSTANCE;
            case 1:
                cutoff = c06651.J$0;
                ResultKt.throwOnFailure($result);
                endedBefore = $result;
                Iterable iterable3 = (Iterable) endedBefore;
                iterable = iterable3;
                it = iterable3.iterator();
                i = 0;
                cutoff2 = cutoff;
                while (it.hasNext()) {
                    Object next2 = it.next();
                    SessionEntity sessionEntity2 = (SessionEntity) next2;
                    id = sessionEntity2.getId();
                    c06651.L$0 = SpillingKt.nullOutSpilledVariable(iterable);
                    c06651.L$1 = it;
                    c06651.L$2 = SpillingKt.nullOutSpilledVariable(next2);
                    c06651.L$3 = SpillingKt.nullOutSpilledVariable(sessionEntity2);
                    c06651.J$0 = cutoff2;
                    c06651.I$0 = i;
                    c06651.I$1 = 0;
                    c06651.label = 2;
                    if (deleteSession(id, c06651) == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                }
                return Unit.INSTANCE;
            case 2:
                int i2 = c06651.I$1;
                i = c06651.I$0;
                cutoff2 = c06651.J$0;
                Object obj = c06651.L$2;
                it = (Iterator) c06651.L$1;
                iterable = (Iterable) c06651.L$0;
                ResultKt.throwOnFailure($result);
                while (it.hasNext()) {
                    Object next3 = it.next();
                    SessionEntity sessionEntity3 = (SessionEntity) next3;
                    id = sessionEntity3.getId();
                    c06651.L$0 = SpillingKt.nullOutSpilledVariable(iterable);
                    c06651.L$1 = it;
                    c06651.L$2 = SpillingKt.nullOutSpilledVariable(next3);
                    c06651.L$3 = SpillingKt.nullOutSpilledVariable(sessionEntity3);
                    c06651.J$0 = cutoff2;
                    c06651.I$0 = i;
                    c06651.I$1 = 0;
                    c06651.label = 2;
                    if (deleteSession(id, c06651) == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                }
                return Unit.INSTANCE;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
    }
}
