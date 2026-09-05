package com.varun.pocketassistant.speech;

import android.util.Log;
import androidx.core.app.NotificationCompat;
import androidx.core.view.MotionEventCompat;
import com.varun.pocketassistant.data.SegmentEntity;
import com.varun.pocketassistant.data.SessionRepository;
import com.varun.pocketassistant.data.SkipReason;
import com.varun.pocketassistant.data.TranscriptStatus;
import com.varun.pocketassistant.meeting.GapClusterer;
import com.varun.pocketassistant.pipeline.AsrResult;
import com.varun.pocketassistant.pipeline.ProviderRouter;
import com.varun.pocketassistant.pipeline.work.AsrWorker;
import java.io.File;
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
import kotlin.text.Regex;
import kotlin.text.StringsKt;
import kotlinx.coroutines.internal.LockFreeTaskQueueCore;

/* JADX INFO: compiled from: AsrStage.kt */
/* JADX INFO: loaded from: classes3.dex */
@Metadata(d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\b\u0007\u0018\u0000 \r2\u00020\u0001:\u0001\rB\u0017\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0004\b\u0006\u0010\u0007J\u0016\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000bH\u0086@¢\u0006\u0002\u0010\fR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u000e"}, d2 = {"Lcom/varun/pocketassistant/speech/AsrStage;", "", "repository", "Lcom/varun/pocketassistant/data/SessionRepository;", "router", "Lcom/varun/pocketassistant/pipeline/ProviderRouter;", "<init>", "(Lcom/varun/pocketassistant/data/SessionRepository;Lcom/varun/pocketassistant/pipeline/ProviderRouter;)V", "process", "", AsrWorker.KEY_SEGMENT_ID, "", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "Companion", "app_debug"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class AsrStage {
    private static final String TAG = "AsrStage";
    private final SessionRepository repository;
    private final ProviderRouter router;
    public static final int $stable = 8;

    /* JADX INFO: renamed from: com.varun.pocketassistant.speech.AsrStage$process$1, reason: invalid class name */
    /* JADX INFO: compiled from: AsrStage.kt */
    @Metadata(k = 3, mv = {2, 2, 0}, xi = 48)
    @DebugMetadata(c = "com.varun.pocketassistant.speech.AsrStage", f = "AsrStage.kt", i = {0, 1, 1, 1, 1, 2, 2, 2, 2, 2, 3, 3, 3, 3, 3, 3, 4, 4, 4, 4, 4, 4, 5, 5, 5, 5, 5, 5, 5, 6, 6, 6, 6, 6, 6, 6, 6, 6, 7, 7, 7, 7, 7, 7, 7, 7}, l = {22, MotionEventCompat.AXIS_GENERIC_8, 51, 54, 58, LockFreeTaskQueueCore.FROZEN_SHIFT, 67, 91}, m = "process", n = {AsrWorker.KEY_SEGMENT_ID, AsrWorker.KEY_SEGMENT_ID, "segment", NotificationCompat.CATEGORY_STATUS, "needsAsr", AsrWorker.KEY_SEGMENT_ID, "segment", NotificationCompat.CATEGORY_STATUS, "needsAsr", "started", AsrWorker.KEY_SEGMENT_ID, "segment", NotificationCompat.CATEGORY_STATUS, "wav", "needsAsr", "started", AsrWorker.KEY_SEGMENT_ID, "segment", NotificationCompat.CATEGORY_STATUS, "wav", "needsAsr", "started", AsrWorker.KEY_SEGMENT_ID, "segment", NotificationCompat.CATEGORY_STATUS, "wav", "result", "needsAsr", "started", AsrWorker.KEY_SEGMENT_ID, "segment", NotificationCompat.CATEGORY_STATUS, "wav", "result", "candidates", "needsAsr", "started", "autoConfirmed", AsrWorker.KEY_SEGMENT_ID, "segment", NotificationCompat.CATEGORY_STATUS, "wav", "t", "needsAsr", "started", "elapsed"}, s = {"L$0", "L$0", "L$1", "L$2", "I$0", "L$0", "L$1", "L$2", "I$0", "J$0", "L$0", "L$1", "L$2", "L$3", "I$0", "J$0", "L$0", "L$1", "L$2", "L$3", "I$0", "J$0", "L$0", "L$1", "L$2", "L$3", "L$4", "I$0", "J$0", "L$0", "L$1", "L$2", "L$3", "L$4", "L$5", "I$0", "J$0", "I$1", "L$0", "L$1", "L$2", "L$3", "L$4", "I$0", "J$0", "J$1"})
    static final class AnonymousClass1 extends ContinuationImpl {
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

        AnonymousClass1(Continuation<? super AnonymousClass1> continuation) {
            super(continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            this.result = obj;
            this.label |= Integer.MIN_VALUE;
            return AsrStage.this.process(null, this);
        }
    }

    public AsrStage(SessionRepository repository, ProviderRouter router) {
        Intrinsics.checkNotNullParameter(repository, "repository");
        Intrinsics.checkNotNullParameter(router, "router");
        this.repository = repository;
        this.router = router;
    }

    /* JADX WARN: Code duplicated, block: B:101:0x02ad  */
    /* JADX WARN: Code duplicated, block: B:103:0x02b0  */
    /* JADX WARN: Code duplicated, block: B:105:0x0300 A[RETURN] */
    /* JADX WARN: Code duplicated, block: B:106:0x0301  */
    /* JADX WARN: Code duplicated, block: B:109:0x0318  */
    /* JADX WARN: Code duplicated, block: B:119:0x035f  */
    /* JADX WARN: Code duplicated, block: B:125:0x03b9 A[RETURN] */
    /* JADX WARN: Code duplicated, block: B:126:0x03ba  */
    /* JADX WARN: Code duplicated, block: B:136:0x03f0  */
    /* JADX WARN: Code duplicated, block: B:141:0x040a A[Catch: all -> 0x0421, TryCatch #1 {all -> 0x0421, blocks: (B:139:0x03fe, B:141:0x040a, B:143:0x0412, B:156:0x0449, B:168:0x0466), top: B:235:0x03fe }] */
    /* JADX WARN: Code duplicated, block: B:147:0x041c  */
    /* JADX WARN: Code duplicated, block: B:149:0x041f  */
    /* JADX WARN: Code duplicated, block: B:152:0x0428  */
    /* JADX WARN: Code duplicated, block: B:156:0x0449 A[Catch: all -> 0x0421, TRY_ENTER, TRY_LEAVE, TryCatch #1 {all -> 0x0421, blocks: (B:139:0x03fe, B:141:0x040a, B:143:0x0412, B:156:0x0449, B:168:0x0466), top: B:235:0x03fe }] */
    /* JADX WARN: Code duplicated, block: B:160:0x0453  */
    /* JADX WARN: Code duplicated, block: B:162:0x0456 A[ADDED_TO_REGION, REMOVE] */
    /* JADX WARN: Code duplicated, block: B:163:0x0459 A[ADDED_TO_REGION] */
    /* JADX WARN: Code duplicated, block: B:168:0x0466 A[Catch: all -> 0x0421, TRY_ENTER, TRY_LEAVE, TryCatch #1 {all -> 0x0421, blocks: (B:139:0x03fe, B:141:0x040a, B:143:0x0412, B:156:0x0449, B:168:0x0466), top: B:235:0x03fe }] */
    /* JADX WARN: Code duplicated, block: B:171:0x046f  */
    /* JADX WARN: Code duplicated, block: B:181:0x04bf A[RETURN] */
    /* JADX WARN: Code duplicated, block: B:182:0x04c0  */
    /* JADX WARN: Code duplicated, block: B:221:0x05f4  */
    /* JADX WARN: Code duplicated, block: B:224:0x0649 A[RETURN] */
    /* JADX WARN: Code duplicated, block: B:225:0x064a  */
    /* JADX WARN: Code duplicated, block: B:229:0x06ab A[RETURN] */
    /* JADX WARN: Code duplicated, block: B:230:0x06ac  */
    /* JADX WARN: Code duplicated, block: B:235:0x03fe A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:37:0x0183  */
    /* JADX WARN: Code duplicated, block: B:39:0x0186  */
    /* JADX WARN: Code duplicated, block: B:41:0x0192  */
    /* JADX WARN: Code duplicated, block: B:43:0x0195  */
    /* JADX WARN: Code duplicated, block: B:45:0x019d  */
    /* JADX WARN: Code duplicated, block: B:51:0x01af  */
    /* JADX WARN: Code duplicated, block: B:53:0x01b2  */
    /* JADX WARN: Code duplicated, block: B:57:0x01bd  */
    /* JADX WARN: Code duplicated, block: B:63:0x01cf  */
    /* JADX WARN: Code duplicated, block: B:65:0x01d2  */
    /* JADX WARN: Code duplicated, block: B:69:0x01db  */
    /* JADX WARN: Code duplicated, block: B:71:0x01de  */
    /* JADX WARN: Code duplicated, block: B:7:0x001a  */
    /* JADX WARN: Code duplicated, block: B:87:0x020f  */
    /* JADX WARN: Code duplicated, block: B:90:0x021c  */
    /* JADX WARN: Code duplicated, block: B:92:0x0237  */
    /* JADX WARN: Code duplicated, block: B:94:0x0290 A[RETURN] */
    /* JADX WARN: Code duplicated, block: B:95:0x0291  */
    /* JADX WARN: Code duplicated, block: B:98:0x0299  */
    /* JADX WARN: Code duplicated, block: B:99:0x02a2  */
    /* JADX WARN: Multi-variable type inference failed */
    public final Object process(String segmentId, Continuation<? super Unit> continuation) throws Throwable {
        AnonymousClass1 anonymousClass1;
        Object segment;
        SegmentEntity segment2;
        String status;
        int i;
        Object obj;
        long started;
        SessionRepository sessionRepository;
        TranscriptStatus transcriptStatus;
        AnonymousClass1 anonymousClass2;
        String str;
        int i2;
        int i3;
        AnonymousClass1 anonymousClass3;
        long started2;
        SegmentEntity segment3;
        int i4;
        String status2;
        SessionRepository sessionRepository2;
        TranscriptStatus transcriptStatus2;
        Continuation $continuation;
        String transcript;
        boolean z;
        String transcript2;
        boolean z2;
        File wav;
        SessionRepository sessionRepository3;
        TranscriptStatus transcriptStatus3;
        Continuation $continuation2;
        String status3;
        int i5;
        SegmentEntity segment4;
        String status4;
        File wav2;
        int i6;
        SegmentEntity segment5;
        long started3;
        AsrResult result;
        int i7;
        long started4;
        String candidates;
        String diarized;
        int i8;
        int i9;
        int i10;
        SessionRepository sessionRepository4;
        TranscriptStatus transcriptStatus4;
        String plain;
        String diarized2;
        String providerId;
        String diarized3;
        int i11;
        boolean z3;
        Boolean boolBoxBoolean;
        boolean z4;
        long started5;
        int i12;
        AnonymousClass1 anonymousClass4;
        AsrResult result2;
        SegmentEntity segment6;
        String segmentId2;
        File wav3;
        String status5;
        int i13;
        long started6;
        SessionRepository sessionRepository5;
        TranscriptStatus transcriptStatus5;
        AnonymousClass1 anonymousClass5;
        SegmentEntity segment7;
        String str2;
        SegmentEntity segment8;
        String segmentId3;
        File wav4;
        String status6;
        long started7;
        SessionRepository sessionRepository6;
        TranscriptStatus transcriptStatus6;
        String message;
        Continuation $continuation3;
        SegmentEntity segment9;
        String segmentId4 = segmentId;
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
                SessionRepository sessionRepository7 = this.repository;
                anonymousClass1.L$0 = segmentId4;
                anonymousClass1.label = 1;
                segment = sessionRepository7.getSegment(segmentId4, anonymousClass1);
                if (segment == coroutine_suspended) {
                    return coroutine_suspended;
                }
                segment2 = (SegmentEntity) segment;
                if (segment2 == null) {
                    return Unit.INSTANCE;
                }
                status = segment2.getTranscriptStatus();
                if (Intrinsics.areEqual(status, "SKIPPED_SILENCE")) {
                    return Unit.INSTANCE;
                }
                if (Intrinsics.areEqual(status, "READY")) {
                    transcript2 = segment2.getTranscript();
                    if (transcript2 != null || StringsKt.isBlank(transcript2)) {
                        z2 = true;
                    } else {
                        z2 = false;
                    }
                    if (!z2) {
                        return Unit.INSTANCE;
                    }
                }
                if (Intrinsics.areEqual(status, "CLEANED")) {
                    transcript = segment2.getTranscript();
                    if (transcript != null || StringsKt.isBlank(transcript)) {
                        z = true;
                    } else {
                        z = false;
                    }
                    if (!z) {
                        return Unit.INSTANCE;
                    }
                }
                if (segment2.getSkipReason() != null) {
                    return Unit.INSTANCE;
                }
                if (!Intrinsics.areEqual(status, "PENDING") || Intrinsics.areEqual(status, "FAILED") || Intrinsics.areEqual(status, "PROCESSING")) {
                    i = 1;
                } else {
                    String transcript3 = segment2.getTranscript();
                    if (transcript3 == null || StringsKt.isBlank(transcript3)) {
                        i = 1;
                    } else {
                        i = 0;
                    }
                }
                if (segment2.getDurationMs() > GapClusterer.DEFAULT_GAP_MS) {
                    obj = null;
                    if (new Regex("speech_\\d+\\.wav").matches(StringsKt.substringAfterLast$default(segment2.getFilePath(), '/', (String) null, 2, (Object) null))) {
                        sessionRepository2 = this.repository;
                        transcriptStatus2 = TranscriptStatus.SKIPPED_SILENCE;
                        anonymousClass1.L$0 = SpillingKt.nullOutSpilledVariable(segmentId4);
                        anonymousClass1.L$1 = SpillingKt.nullOutSpilledVariable(segment2);
                        anonymousClass1.L$2 = SpillingKt.nullOutSpilledVariable(status);
                        anonymousClass1.I$0 = i;
                        anonymousClass1.label = 2;
                        $continuation = anonymousClass1;
                        if (sessionRepository2.updateTranscript(segmentId4, transcriptStatus2, (8060 & 4) != 0 ? null : null, (8060 & 8) != 0 ? null : null, (8060 & 16) != 0 ? null : null, (8060 & 32) != 0 ? null : null, (8060 & 64) != 0 ? null : null, (8060 & 128) != 0 ? null : null, (8060 & 256) != 0 ? null : SkipReason.LEGACY_TOO_LONG, (8060 & 512) != 0 ? null : null, (8060 & 1024) != 0 ? null : null, (8060 & 2048) != 0 ? false : true, (8060 & 4096) != 0 ? false : false, $continuation) == coroutine_suspended) {
                            return coroutine_suspended;
                        }
                        return Unit.INSTANCE;
                    }
                } else {
                    obj = null;
                }
                if (i == 0) {
                    return Unit.INSTANCE;
                }
                started = System.currentTimeMillis();
                sessionRepository = this.repository;
                transcriptStatus = TranscriptStatus.PROCESSING;
                anonymousClass1.L$0 = segmentId4;
                anonymousClass1.L$1 = segment2;
                anonymousClass1.L$2 = SpillingKt.nullOutSpilledVariable(status);
                anonymousClass1.I$0 = i;
                anonymousClass1.J$0 = started;
                anonymousClass1.label = 3;
                anonymousClass2 = anonymousClass1;
                str = TAG;
                i2 = i;
                i3 = 1;
                anonymousClass3 = anonymousClass2;
                if (sessionRepository.updateTranscript(segmentId4, transcriptStatus, (8060 & 4) != 0 ? null : null, (8060 & 8) != 0 ? null : null, (8060 & 16) != 0 ? null : null, (8060 & 32) != 0 ? null : null, (8060 & 64) != 0 ? null : null, (8060 & 128) != 0 ? null : null, (8060 & 256) != 0 ? null : null, (8060 & 512) != 0 ? null : null, (8060 & 1024) != 0 ? null : null, (8060 & 2048) != 0 ? false : true, (8060 & 4096) != 0 ? false : false, anonymousClass2) == coroutine_suspended) {
                    return coroutine_suspended;
                }
                started2 = started;
                segment3 = segment2;
                i4 = i2;
                status2 = status;
                wav = new File(segment3.getFilePath());
                if (wav.exists() || wav.length() < 16044) {
                    sessionRepository3 = this.repository;
                    transcriptStatus3 = TranscriptStatus.SKIPPED_SILENCE;
                    anonymousClass3.L$0 = SpillingKt.nullOutSpilledVariable(segmentId4);
                    anonymousClass3.L$1 = SpillingKt.nullOutSpilledVariable(segment3);
                    anonymousClass3.L$2 = SpillingKt.nullOutSpilledVariable(status2);
                    anonymousClass3.L$3 = SpillingKt.nullOutSpilledVariable(wav);
                    anonymousClass3.I$0 = i4;
                    anonymousClass3.J$0 = started2;
                    anonymousClass3.label = 4;
                    $continuation2 = anonymousClass3;
                    if (sessionRepository3.updateTranscript(segmentId4, transcriptStatus3, (8060 & 4) != 0 ? null : null, (8060 & 8) != 0 ? null : null, (8060 & 16) != 0 ? null : null, (8060 & 32) != 0 ? null : null, (8060 & 64) != 0 ? null : null, (8060 & 128) != 0 ? null : null, (8060 & 256) != 0 ? null : null, (8060 & 512) != 0 ? null : null, (8060 & 1024) != 0 ? null : null, (8060 & 2048) != 0 ? false : false, (8060 & 4096) != 0 ? false : false, $continuation2) == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    return Unit.INSTANCE;
                }
                try {
                    ProviderRouter providerRouter = this.router;
                    anonymousClass3.L$0 = segmentId4;
                    anonymousClass3.L$1 = segment3;
                    anonymousClass3.L$2 = SpillingKt.nullOutSpilledVariable(status2);
                    anonymousClass3.L$3 = SpillingKt.nullOutSpilledVariable(wav);
                    anonymousClass3.I$0 = i4;
                    anonymousClass3.J$0 = started2;
                    anonymousClass3.label = 5;
                    Object objTranscribe = providerRouter.transcribe(wav, anonymousClass3);
                    if (objTranscribe == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    segmentId4 = segmentId4;
                    status4 = status2;
                    i6 = i4;
                    wav2 = wav;
                    $result = objTranscribe;
                    segment5 = segment3;
                    started3 = started2;
                    try {
                        result = (AsrResult) $result;
                        if (!StringsKt.isBlank(result.getPlain())) {
                            status3 = str;
                            i7 = i6;
                            segment4 = segment5;
                            started4 = started3;
                            segmentId4 = segmentId4;
                            try {
                                candidates = result.getSpeakerCandidatesJson();
                                if (candidates != null) {
                                    i9 = 0;
                                    i10 = i9;
                                    sessionRepository4 = this.repository;
                                    transcriptStatus4 = TranscriptStatus.READY;
                                    plain = result.getPlain();
                                    diarized2 = result.getDiarized();
                                    providerId = result.getProviderId();
                                    diarized3 = result.getDiarized();
                                    if (diarized3 != null) {
                                        i11 = i3;
                                    } else {
                                        i11 = i3;
                                    }
                                    if (i11 == 0) {
                                        z3 = 0;
                                    }
                                    boolBoxBoolean = Boxing.boxBoolean(z3);
                                    if (candidates == null) {
                                        z4 = 0;
                                    } else {
                                        z4 = 0;
                                    }
                                    anonymousClass3.L$0 = segmentId4;
                                    anonymousClass3.L$1 = segment4;
                                    anonymousClass3.L$2 = SpillingKt.nullOutSpilledVariable(status4);
                                    anonymousClass3.L$3 = SpillingKt.nullOutSpilledVariable(wav2);
                                    anonymousClass3.L$4 = result;
                                    anonymousClass3.L$5 = SpillingKt.nullOutSpilledVariable(candidates);
                                    anonymousClass3.I$0 = i7;
                                    anonymousClass3.J$0 = started4;
                                    anonymousClass3.I$1 = i10;
                                    anonymousClass3.label = 7;
                                    started5 = started4;
                                    i12 = i7;
                                    anonymousClass4 = anonymousClass3;
                                    anonymousClass3 = anonymousClass4;
                                    if (sessionRepository4.updateTranscript(segmentId4, transcriptStatus4, (8060 & 4) != 0 ? null : plain, (8060 & 8) != 0 ? null : diarized2, (8060 & 16) != 0 ? null : null, (8060 & 32) != 0 ? null : providerId, (8060 & 64) != 0 ? null : null, (8060 & 128) != 0 ? null : null, (8060 & 256) != 0 ? null : null, (8060 & 512) != 0 ? null : candidates, (8060 & 1024) != 0 ? null : boolBoxBoolean, (8060 & 2048) != 0 ? false : true, (8060 & 4096) != 0 ? false : z4, anonymousClass4) == coroutine_suspended) {
                                        return coroutine_suspended;
                                    }
                                    result2 = result;
                                    segment6 = segment4;
                                    segmentId2 = segmentId4;
                                    wav3 = wav2;
                                    status5 = status4;
                                    i13 = i12;
                                    started6 = started5;
                                    long elapsed = System.currentTimeMillis() - started6;
                                    segment9 = segment6;
                                    long started8 = started6;
                                    int i14 = i13;
                                    status3 = status3;
                                    Log.i(status3, "ASR " + segmentId2 + " via " + result2.getProviderId() + " audioMs=" + segment9.getDurationMs() + " chars=" + result2.getPlain().length() + " wallMs=" + elapsed);
                                    return Unit.INSTANCE;
                                }
                                try {
                                    if (DiarizationLabels.INSTANCE.parseCandidates(candidates).size() > i3) {
                                        i9 = 0;
                                    } else {
                                        diarized = result.getDiarized();
                                        if (diarized != null || StringsKt.isBlank(diarized)) {
                                            i8 = i3;
                                        } else {
                                            i8 = 0;
                                        }
                                        if (i8 == 0) {
                                            i9 = i3;
                                        } else {
                                            i9 = 0;
                                        }
                                    }
                                    i10 = i9;
                                    sessionRepository4 = this.repository;
                                    transcriptStatus4 = TranscriptStatus.READY;
                                    plain = result.getPlain();
                                    diarized2 = result.getDiarized();
                                    providerId = result.getProviderId();
                                    diarized3 = result.getDiarized();
                                    if (diarized3 != null || StringsKt.isBlank(diarized3)) {
                                        i11 = i3;
                                    } else {
                                        i11 = 0;
                                    }
                                    z3 = (i11 == 0 || i10 == 0) ? 0 : i3;
                                    boolBoxBoolean = Boxing.boxBoolean(z3);
                                    if (candidates == null || result.getDiarized() != null) {
                                        z4 = 0;
                                    } else {
                                        z4 = i3;
                                    }
                                    anonymousClass3.L$0 = segmentId4;
                                    anonymousClass3.L$1 = segment4;
                                    anonymousClass3.L$2 = SpillingKt.nullOutSpilledVariable(status4);
                                    anonymousClass3.L$3 = SpillingKt.nullOutSpilledVariable(wav2);
                                    anonymousClass3.L$4 = result;
                                    anonymousClass3.L$5 = SpillingKt.nullOutSpilledVariable(candidates);
                                    try {
                                        anonymousClass3.I$0 = i7;
                                        try {
                                            anonymousClass3.J$0 = started4;
                                            anonymousClass3.I$1 = i10;
                                            anonymousClass3.label = 7;
                                            started5 = started4;
                                            i12 = i7;
                                            anonymousClass4 = anonymousClass3;
                                            try {
                                                anonymousClass3 = anonymousClass4;
                                                if (sessionRepository4.updateTranscript(segmentId4, transcriptStatus4, (8060 & 4) != 0 ? null : plain, (8060 & 8) != 0 ? null : diarized2, (8060 & 16) != 0 ? null : null, (8060 & 32) != 0 ? null : providerId, (8060 & 64) != 0 ? null : null, (8060 & 128) != 0 ? null : null, (8060 & 256) != 0 ? null : null, (8060 & 512) != 0 ? null : candidates, (8060 & 1024) != 0 ? null : boolBoxBoolean, (8060 & 2048) != 0 ? false : true, (8060 & 4096) != 0 ? false : z4, anonymousClass4) == coroutine_suspended) {
                                                    return coroutine_suspended;
                                                }
                                                result2 = result;
                                                segment6 = segment4;
                                                segmentId2 = segmentId4;
                                                wav3 = wav2;
                                                status5 = status4;
                                                i13 = i12;
                                                started6 = started5;
                                                try {
                                                    long elapsed2 = System.currentTimeMillis() - started6;
                                                    segment9 = segment6;
                                                    try {
                                                        long started9 = started6;
                                                        try {
                                                            int i15 = i13;
                                                            try {
                                                                status3 = status3;
                                                                try {
                                                                    Log.i(status3, "ASR " + segmentId2 + " via " + result2.getProviderId() + " audioMs=" + segment9.getDurationMs() + " chars=" + result2.getPlain().length() + " wallMs=" + elapsed2);
                                                                    return Unit.INSTANCE;
                                                                } catch (Throwable th) {
                                                                    t = th;
                                                                    segment4 = segment9;
                                                                    segmentId4 = segmentId2;
                                                                    wav2 = wav3;
                                                                    status4 = status5;
                                                                    i5 = i15;
                                                                    started2 = started9;
                                                                    long elapsed3 = System.currentTimeMillis() - started2;
                                                                    Log.e(status3, "ASR failed for " + segmentId4 + " after " + elapsed3 + "ms", t);
                                                                    sessionRepository6 = this.repository;
                                                                    transcriptStatus6 = TranscriptStatus.FAILED;
                                                                    message = t.getMessage();
                                                                    if (message == null) {
                                                                        message = t.getClass().getSimpleName();
                                                                    }
                                                                    anonymousClass3.L$0 = SpillingKt.nullOutSpilledVariable(segmentId4);
                                                                    anonymousClass3.L$1 = SpillingKt.nullOutSpilledVariable(segment4);
                                                                    anonymousClass3.L$2 = SpillingKt.nullOutSpilledVariable(status4);
                                                                    anonymousClass3.L$3 = SpillingKt.nullOutSpilledVariable(wav2);
                                                                    anonymousClass3.L$4 = t;
                                                                    anonymousClass3.L$5 = null;
                                                                    anonymousClass3.I$0 = i5;
                                                                    anonymousClass3.J$0 = started2;
                                                                    anonymousClass3.J$1 = elapsed3;
                                                                    anonymousClass3.label = 8;
                                                                    $continuation3 = anonymousClass3;
                                                                    if (sessionRepository6.updateTranscript(segmentId4, transcriptStatus6, (8060 & 4) != 0 ? null : null, (8060 & 8) != 0 ? null : null, (8060 & 16) != 0 ? null : null, (8060 & 32) != 0 ? null : null, (8060 & 64) != 0 ? null : null, (8060 & 128) != 0 ? null : message, (8060 & 256) != 0 ? null : null, (8060 & 512) != 0 ? null : null, (8060 & 1024) != 0 ? null : null, (8060 & 2048) != 0 ? false : false, (8060 & 4096) != 0 ? false : false, $continuation3) == coroutine_suspended) {
                                                                        return coroutine_suspended;
                                                                    }
                                                                    throw t;
                                                                }
                                                            } catch (Throwable th2) {
                                                                t = th2;
                                                                status3 = status3;
                                                            }
                                                        } catch (Throwable th3) {
                                                            t = th3;
                                                            int i16 = i13;
                                                            status3 = status3;
                                                            segment4 = segment9;
                                                            segmentId4 = segmentId2;
                                                            wav2 = wav3;
                                                            status4 = status5;
                                                            i5 = i16;
                                                            started2 = started9;
                                                        }
                                                    } catch (Throwable th4) {
                                                        t = th4;
                                                        int i17 = i13;
                                                        status3 = status3;
                                                        segment4 = segment9;
                                                        segmentId4 = segmentId2;
                                                        wav2 = wav3;
                                                        status4 = status5;
                                                        i5 = i17;
                                                        started2 = started6;
                                                    }
                                                } catch (Throwable th5) {
                                                    t = th5;
                                                    int i18 = i13;
                                                    status3 = status3;
                                                    segment4 = segment6;
                                                    segmentId4 = segmentId2;
                                                    wav2 = wav3;
                                                    status4 = status5;
                                                    i5 = i18;
                                                    started2 = started6;
                                                }
                                            } catch (Throwable th6) {
                                                t = th6;
                                                anonymousClass3 = anonymousClass4;
                                                status3 = status3;
                                                segment4 = segment4;
                                                i5 = i12;
                                                started2 = started5;
                                            }
                                        } catch (Throwable th7) {
                                            t = th7;
                                            status3 = status3;
                                            segment4 = segment4;
                                            i5 = i7;
                                            started2 = started4;
                                        }
                                    } catch (Throwable th8) {
                                        t = th8;
                                        status3 = status3;
                                        segment4 = segment4;
                                        i5 = i7;
                                        started2 = started4;
                                    }
                                } catch (Throwable th9) {
                                    t = th9;
                                    i5 = i7;
                                    started2 = started4;
                                }
                            } catch (Throwable th10) {
                                t = th10;
                            }
                            long elapsed4 = System.currentTimeMillis() - started2;
                            Log.e(status3, "ASR failed for " + segmentId4 + " after " + elapsed4 + "ms", t);
                            sessionRepository6 = this.repository;
                            transcriptStatus6 = TranscriptStatus.FAILED;
                            message = t.getMessage();
                            if (message == null) {
                                message = t.getClass().getSimpleName();
                            }
                            anonymousClass3.L$0 = SpillingKt.nullOutSpilledVariable(segmentId4);
                            anonymousClass3.L$1 = SpillingKt.nullOutSpilledVariable(segment4);
                            anonymousClass3.L$2 = SpillingKt.nullOutSpilledVariable(status4);
                            anonymousClass3.L$3 = SpillingKt.nullOutSpilledVariable(wav2);
                            anonymousClass3.L$4 = t;
                            anonymousClass3.L$5 = null;
                            anonymousClass3.I$0 = i5;
                            anonymousClass3.J$0 = started2;
                            anonymousClass3.J$1 = elapsed4;
                            anonymousClass3.label = 8;
                            $continuation3 = anonymousClass3;
                            if (sessionRepository6.updateTranscript(segmentId4, transcriptStatus6, (8060 & 4) != 0 ? null : null, (8060 & 8) != 0 ? null : null, (8060 & 16) != 0 ? null : null, (8060 & 32) != 0 ? null : null, (8060 & 64) != 0 ? null : null, (8060 & 128) != 0 ? null : message, (8060 & 256) != 0 ? null : null, (8060 & 512) != 0 ? null : null, (8060 & 1024) != 0 ? null : null, (8060 & 2048) != 0 ? false : false, (8060 & 4096) != 0 ? false : false, $continuation3) == coroutine_suspended) {
                                return coroutine_suspended;
                            }
                            throw t;
                        }
                        try {
                            sessionRepository5 = this.repository;
                            transcriptStatus5 = TranscriptStatus.SKIPPED_SILENCE;
                            anonymousClass3.L$0 = segmentId4;
                            anonymousClass3.L$1 = SpillingKt.nullOutSpilledVariable(segment5);
                            anonymousClass3.L$2 = SpillingKt.nullOutSpilledVariable(status4);
                            anonymousClass3.L$3 = SpillingKt.nullOutSpilledVariable(wav2);
                            anonymousClass3.L$4 = SpillingKt.nullOutSpilledVariable(result);
                            anonymousClass3.I$0 = i6;
                            anonymousClass3.J$0 = started3;
                            anonymousClass3.label = 6;
                            anonymousClass5 = anonymousClass3;
                            segment7 = segment5;
                            int i19 = i6;
                            segmentId4 = segmentId4;
                            i12 = i19;
                            started5 = started3;
                            str2 = str;
                            try {
                                anonymousClass3 = anonymousClass5;
                                if (sessionRepository5.updateTranscript(segmentId4, transcriptStatus5, (8060 & 4) != 0 ? null : null, (8060 & 8) != 0 ? null : null, (8060 & 16) != 0 ? null : null, (8060 & 32) != 0 ? null : null, (8060 & 64) != 0 ? null : null, (8060 & 128) != 0 ? null : null, (8060 & 256) != 0 ? null : null, (8060 & 512) != 0 ? null : null, (8060 & 1024) != 0 ? null : null, (8060 & 2048) != 0 ? false : false, (8060 & 4096) != 0 ? false : false, anonymousClass5) == coroutine_suspended) {
                                    return coroutine_suspended;
                                }
                                segment8 = segment7;
                                segmentId3 = segmentId4;
                                wav4 = wav2;
                                status6 = status4;
                                started7 = started5;
                                try {
                                    return Unit.INSTANCE;
                                } catch (Throwable th11) {
                                    t = th11;
                                    segmentId4 = segmentId3;
                                    started2 = started7;
                                    wav2 = wav4;
                                    status4 = status6;
                                    segment4 = segment8;
                                    status3 = str2;
                                    i5 = i12;
                                }
                            } catch (Throwable th12) {
                                t = th12;
                                anonymousClass3 = anonymousClass5;
                                segment4 = segment7;
                                status3 = str2;
                                i5 = i12;
                                started2 = started5;
                            }
                        } catch (Throwable th13) {
                            t = th13;
                            segment4 = segment5;
                            status3 = str;
                            i5 = i6;
                            started2 = started3;
                        }
                        i5 = i12;
                        started2 = started5;
                    } catch (Throwable th14) {
                        t = th14;
                        status3 = str;
                        segment4 = segment5;
                    }
                    long elapsed5 = System.currentTimeMillis() - started2;
                    Log.e(status3, "ASR failed for " + segmentId4 + " after " + elapsed5 + "ms", t);
                    sessionRepository6 = this.repository;
                    transcriptStatus6 = TranscriptStatus.FAILED;
                    message = t.getMessage();
                    if (message == null) {
                        message = t.getClass().getSimpleName();
                    }
                    anonymousClass3.L$0 = SpillingKt.nullOutSpilledVariable(segmentId4);
                    anonymousClass3.L$1 = SpillingKt.nullOutSpilledVariable(segment4);
                    anonymousClass3.L$2 = SpillingKt.nullOutSpilledVariable(status4);
                    anonymousClass3.L$3 = SpillingKt.nullOutSpilledVariable(wav2);
                    anonymousClass3.L$4 = t;
                    anonymousClass3.L$5 = null;
                    anonymousClass3.I$0 = i5;
                    anonymousClass3.J$0 = started2;
                    anonymousClass3.J$1 = elapsed5;
                    anonymousClass3.label = 8;
                    $continuation3 = anonymousClass3;
                    if (sessionRepository6.updateTranscript(segmentId4, transcriptStatus6, (8060 & 4) != 0 ? null : null, (8060 & 8) != 0 ? null : null, (8060 & 16) != 0 ? null : null, (8060 & 32) != 0 ? null : null, (8060 & 64) != 0 ? null : null, (8060 & 128) != 0 ? null : message, (8060 & 256) != 0 ? null : null, (8060 & 512) != 0 ? null : null, (8060 & 1024) != 0 ? null : null, (8060 & 2048) != 0 ? false : false, (8060 & 4096) != 0 ? false : false, $continuation3) == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    throw t;
                } catch (Throwable th15) {
                    t = th15;
                    status3 = str;
                    i5 = i4;
                    segment4 = segment3;
                    status4 = status2;
                    wav2 = wav;
                }
                break;
            case 1:
                segmentId4 = (String) anonymousClass1.L$0;
                ResultKt.throwOnFailure($result);
                segment = $result;
                segment2 = (SegmentEntity) segment;
                if (segment2 == null) {
                    return Unit.INSTANCE;
                }
                status = segment2.getTranscriptStatus();
                if (Intrinsics.areEqual(status, "SKIPPED_SILENCE")) {
                    return Unit.INSTANCE;
                }
                if (Intrinsics.areEqual(status, "READY")) {
                    transcript2 = segment2.getTranscript();
                    if (transcript2 != null) {
                        z2 = true;
                    } else {
                        z2 = true;
                    }
                    if (!z2) {
                        return Unit.INSTANCE;
                    }
                }
                if (Intrinsics.areEqual(status, "CLEANED")) {
                    transcript = segment2.getTranscript();
                    if (transcript != null) {
                        z = true;
                    } else {
                        z = true;
                    }
                    if (!z) {
                        return Unit.INSTANCE;
                    }
                }
                if (segment2.getSkipReason() != null) {
                    return Unit.INSTANCE;
                }
                if (Intrinsics.areEqual(status, "PENDING")) {
                    i = 1;
                } else {
                    i = 1;
                }
                if (segment2.getDurationMs() > GapClusterer.DEFAULT_GAP_MS) {
                    obj = null;
                    if (new Regex("speech_\\d+\\.wav").matches(StringsKt.substringAfterLast$default(segment2.getFilePath(), '/', (String) null, 2, (Object) null))) {
                        sessionRepository2 = this.repository;
                        transcriptStatus2 = TranscriptStatus.SKIPPED_SILENCE;
                        anonymousClass1.L$0 = SpillingKt.nullOutSpilledVariable(segmentId4);
                        anonymousClass1.L$1 = SpillingKt.nullOutSpilledVariable(segment2);
                        anonymousClass1.L$2 = SpillingKt.nullOutSpilledVariable(status);
                        anonymousClass1.I$0 = i;
                        anonymousClass1.label = 2;
                        $continuation = anonymousClass1;
                        if (sessionRepository2.updateTranscript(segmentId4, transcriptStatus2, (8060 & 4) != 0 ? null : null, (8060 & 8) != 0 ? null : null, (8060 & 16) != 0 ? null : null, (8060 & 32) != 0 ? null : null, (8060 & 64) != 0 ? null : null, (8060 & 128) != 0 ? null : null, (8060 & 256) != 0 ? null : SkipReason.LEGACY_TOO_LONG, (8060 & 512) != 0 ? null : null, (8060 & 1024) != 0 ? null : null, (8060 & 2048) != 0 ? false : true, (8060 & 4096) != 0 ? false : false, $continuation) == coroutine_suspended) {
                            return coroutine_suspended;
                        }
                        return Unit.INSTANCE;
                    }
                } else {
                    obj = null;
                }
                if (i == 0) {
                    return Unit.INSTANCE;
                }
                started = System.currentTimeMillis();
                sessionRepository = this.repository;
                transcriptStatus = TranscriptStatus.PROCESSING;
                anonymousClass1.L$0 = segmentId4;
                anonymousClass1.L$1 = segment2;
                anonymousClass1.L$2 = SpillingKt.nullOutSpilledVariable(status);
                anonymousClass1.I$0 = i;
                anonymousClass1.J$0 = started;
                anonymousClass1.label = 3;
                anonymousClass2 = anonymousClass1;
                str = TAG;
                i2 = i;
                i3 = 1;
                anonymousClass3 = anonymousClass2;
                if (sessionRepository.updateTranscript(segmentId4, transcriptStatus, (8060 & 4) != 0 ? null : null, (8060 & 8) != 0 ? null : null, (8060 & 16) != 0 ? null : null, (8060 & 32) != 0 ? null : null, (8060 & 64) != 0 ? null : null, (8060 & 128) != 0 ? null : null, (8060 & 256) != 0 ? null : null, (8060 & 512) != 0 ? null : null, (8060 & 1024) != 0 ? null : null, (8060 & 2048) != 0 ? false : true, (8060 & 4096) != 0 ? false : false, anonymousClass2) == coroutine_suspended) {
                    return coroutine_suspended;
                }
                started2 = started;
                segment3 = segment2;
                i4 = i2;
                status2 = status;
                wav = new File(segment3.getFilePath());
                if (wav.exists()) {
                    break;
                }
                sessionRepository3 = this.repository;
                transcriptStatus3 = TranscriptStatus.SKIPPED_SILENCE;
                anonymousClass3.L$0 = SpillingKt.nullOutSpilledVariable(segmentId4);
                anonymousClass3.L$1 = SpillingKt.nullOutSpilledVariable(segment3);
                anonymousClass3.L$2 = SpillingKt.nullOutSpilledVariable(status2);
                anonymousClass3.L$3 = SpillingKt.nullOutSpilledVariable(wav);
                anonymousClass3.I$0 = i4;
                anonymousClass3.J$0 = started2;
                anonymousClass3.label = 4;
                $continuation2 = anonymousClass3;
                if (sessionRepository3.updateTranscript(segmentId4, transcriptStatus3, (8060 & 4) != 0 ? null : null, (8060 & 8) != 0 ? null : null, (8060 & 16) != 0 ? null : null, (8060 & 32) != 0 ? null : null, (8060 & 64) != 0 ? null : null, (8060 & 128) != 0 ? null : null, (8060 & 256) != 0 ? null : null, (8060 & 512) != 0 ? null : null, (8060 & 1024) != 0 ? null : null, (8060 & 2048) != 0 ? false : false, (8060 & 4096) != 0 ? false : false, $continuation2) == coroutine_suspended) {
                    return coroutine_suspended;
                }
                return Unit.INSTANCE;
            case 2:
                int i20 = anonymousClass1.I$0;
                ResultKt.throwOnFailure($result);
                return Unit.INSTANCE;
            case 3:
                long started10 = anonymousClass1.J$0;
                int i21 = anonymousClass1.I$0;
                String status7 = (String) anonymousClass1.L$2;
                SegmentEntity segment10 = (SegmentEntity) anonymousClass1.L$1;
                String segmentId5 = (String) anonymousClass1.L$0;
                ResultKt.throwOnFailure($result);
                status2 = status7;
                i4 = i21;
                anonymousClass3 = anonymousClass1;
                coroutine_suspended = coroutine_suspended;
                str = TAG;
                segmentId4 = segmentId5;
                i3 = 1;
                segment3 = segment10;
                started2 = started10;
                wav = new File(segment3.getFilePath());
                if (wav.exists()) {
                    break;
                }
                sessionRepository3 = this.repository;
                transcriptStatus3 = TranscriptStatus.SKIPPED_SILENCE;
                anonymousClass3.L$0 = SpillingKt.nullOutSpilledVariable(segmentId4);
                anonymousClass3.L$1 = SpillingKt.nullOutSpilledVariable(segment3);
                anonymousClass3.L$2 = SpillingKt.nullOutSpilledVariable(status2);
                anonymousClass3.L$3 = SpillingKt.nullOutSpilledVariable(wav);
                anonymousClass3.I$0 = i4;
                anonymousClass3.J$0 = started2;
                anonymousClass3.label = 4;
                $continuation2 = anonymousClass3;
                if (sessionRepository3.updateTranscript(segmentId4, transcriptStatus3, (8060 & 4) != 0 ? null : null, (8060 & 8) != 0 ? null : null, (8060 & 16) != 0 ? null : null, (8060 & 32) != 0 ? null : null, (8060 & 64) != 0 ? null : null, (8060 & 128) != 0 ? null : null, (8060 & 256) != 0 ? null : null, (8060 & 512) != 0 ? null : null, (8060 & 1024) != 0 ? null : null, (8060 & 2048) != 0 ? false : false, (8060 & 4096) != 0 ? false : false, $continuation2) == coroutine_suspended) {
                    return coroutine_suspended;
                }
                return Unit.INSTANCE;
            case 4:
                long j = anonymousClass1.J$0;
                int i22 = anonymousClass1.I$0;
                ResultKt.throwOnFailure($result);
                return Unit.INSTANCE;
            case 5:
                long started11 = anonymousClass1.J$0;
                i5 = anonymousClass1.I$0;
                File wav5 = (File) anonymousClass1.L$3;
                String status8 = (String) anonymousClass1.L$2;
                SegmentEntity segment11 = (SegmentEntity) anonymousClass1.L$1;
                segmentId4 = (String) anonymousClass1.L$0;
                try {
                    ResultKt.throwOnFailure($result);
                    wav2 = wav5;
                    status4 = status8;
                    i3 = 1;
                    started3 = started11;
                    segment5 = segment11;
                    anonymousClass3 = anonymousClass1;
                    coroutine_suspended = coroutine_suspended;
                    i6 = i5;
                    str = TAG;
                    result = (AsrResult) $result;
                    if (!StringsKt.isBlank(result.getPlain())) {
                        sessionRepository5 = this.repository;
                        transcriptStatus5 = TranscriptStatus.SKIPPED_SILENCE;
                        anonymousClass3.L$0 = segmentId4;
                        anonymousClass3.L$1 = SpillingKt.nullOutSpilledVariable(segment5);
                        anonymousClass3.L$2 = SpillingKt.nullOutSpilledVariable(status4);
                        anonymousClass3.L$3 = SpillingKt.nullOutSpilledVariable(wav2);
                        anonymousClass3.L$4 = SpillingKt.nullOutSpilledVariable(result);
                        anonymousClass3.I$0 = i6;
                        anonymousClass3.J$0 = started3;
                        anonymousClass3.label = 6;
                        anonymousClass5 = anonymousClass3;
                        segment7 = segment5;
                        int i110 = i6;
                        segmentId4 = segmentId4;
                        i12 = i110;
                        started5 = started3;
                        str2 = str;
                        anonymousClass3 = anonymousClass5;
                        if (sessionRepository5.updateTranscript(segmentId4, transcriptStatus5, (8060 & 4) != 0 ? null : null, (8060 & 8) != 0 ? null : null, (8060 & 16) != 0 ? null : null, (8060 & 32) != 0 ? null : null, (8060 & 64) != 0 ? null : null, (8060 & 128) != 0 ? null : null, (8060 & 256) != 0 ? null : null, (8060 & 512) != 0 ? null : null, (8060 & 1024) != 0 ? null : null, (8060 & 2048) != 0 ? false : false, (8060 & 4096) != 0 ? false : false, anonymousClass5) == coroutine_suspended) {
                            return coroutine_suspended;
                        }
                        segment8 = segment7;
                        segmentId3 = segmentId4;
                        wav4 = wav2;
                        status6 = status4;
                        started7 = started5;
                        return Unit.INSTANCE;
                    }
                    status3 = str;
                    i7 = i6;
                    segment4 = segment5;
                    started4 = started3;
                    segmentId4 = segmentId4;
                    candidates = result.getSpeakerCandidatesJson();
                    if (candidates != null) {
                        i9 = 0;
                        i10 = i9;
                        sessionRepository4 = this.repository;
                        transcriptStatus4 = TranscriptStatus.READY;
                        plain = result.getPlain();
                        diarized2 = result.getDiarized();
                        providerId = result.getProviderId();
                        diarized3 = result.getDiarized();
                        if (diarized3 != null) {
                            i11 = i3;
                        } else {
                            i11 = i3;
                        }
                        if (i11 == 0) {
                            z3 = 0;
                        }
                        boolBoxBoolean = Boxing.boxBoolean(z3);
                        if (candidates == null) {
                            z4 = 0;
                        } else {
                            z4 = 0;
                        }
                        anonymousClass3.L$0 = segmentId4;
                        anonymousClass3.L$1 = segment4;
                        anonymousClass3.L$2 = SpillingKt.nullOutSpilledVariable(status4);
                        anonymousClass3.L$3 = SpillingKt.nullOutSpilledVariable(wav2);
                        anonymousClass3.L$4 = result;
                        anonymousClass3.L$5 = SpillingKt.nullOutSpilledVariable(candidates);
                        anonymousClass3.I$0 = i7;
                        anonymousClass3.J$0 = started4;
                        anonymousClass3.I$1 = i10;
                        anonymousClass3.label = 7;
                        started5 = started4;
                        i12 = i7;
                        anonymousClass4 = anonymousClass3;
                        anonymousClass3 = anonymousClass4;
                        if (sessionRepository4.updateTranscript(segmentId4, transcriptStatus4, (8060 & 4) != 0 ? null : plain, (8060 & 8) != 0 ? null : diarized2, (8060 & 16) != 0 ? null : null, (8060 & 32) != 0 ? null : providerId, (8060 & 64) != 0 ? null : null, (8060 & 128) != 0 ? null : null, (8060 & 256) != 0 ? null : null, (8060 & 512) != 0 ? null : candidates, (8060 & 1024) != 0 ? null : boolBoxBoolean, (8060 & 2048) != 0 ? false : true, (8060 & 4096) != 0 ? false : z4, anonymousClass4) == coroutine_suspended) {
                            return coroutine_suspended;
                        }
                        result2 = result;
                        segment6 = segment4;
                        segmentId2 = segmentId4;
                        wav3 = wav2;
                        status5 = status4;
                        i13 = i12;
                        started6 = started5;
                        long elapsed6 = System.currentTimeMillis() - started6;
                        segment9 = segment6;
                        long started12 = started6;
                        int i111 = i13;
                        status3 = status3;
                        Log.i(status3, "ASR " + segmentId2 + " via " + result2.getProviderId() + " audioMs=" + segment9.getDurationMs() + " chars=" + result2.getPlain().length() + " wallMs=" + elapsed6);
                        return Unit.INSTANCE;
                    }
                    if (DiarizationLabels.INSTANCE.parseCandidates(candidates).size() > i3) {
                        i9 = 0;
                    } else {
                        diarized = result.getDiarized();
                        if (diarized != null) {
                            i8 = i3;
                        } else {
                            i8 = i3;
                        }
                        if (i8 == 0) {
                            i9 = i3;
                        } else {
                            i9 = 0;
                        }
                    }
                    i10 = i9;
                    sessionRepository4 = this.repository;
                    transcriptStatus4 = TranscriptStatus.READY;
                    plain = result.getPlain();
                    diarized2 = result.getDiarized();
                    providerId = result.getProviderId();
                    diarized3 = result.getDiarized();
                    if (diarized3 != null) {
                        i11 = i3;
                    } else {
                        i11 = i3;
                    }
                    if (i11 == 0) {
                        z3 = 0;
                    }
                    boolBoxBoolean = Boxing.boxBoolean(z3);
                    if (candidates == null) {
                        z4 = 0;
                    } else {
                        z4 = 0;
                    }
                    anonymousClass3.L$0 = segmentId4;
                    anonymousClass3.L$1 = segment4;
                    anonymousClass3.L$2 = SpillingKt.nullOutSpilledVariable(status4);
                    anonymousClass3.L$3 = SpillingKt.nullOutSpilledVariable(wav2);
                    anonymousClass3.L$4 = result;
                    anonymousClass3.L$5 = SpillingKt.nullOutSpilledVariable(candidates);
                    anonymousClass3.I$0 = i7;
                    anonymousClass3.J$0 = started4;
                    anonymousClass3.I$1 = i10;
                    anonymousClass3.label = 7;
                    started5 = started4;
                    i12 = i7;
                    anonymousClass4 = anonymousClass3;
                    anonymousClass3 = anonymousClass4;
                    if (sessionRepository4.updateTranscript(segmentId4, transcriptStatus4, (8060 & 4) != 0 ? null : plain, (8060 & 8) != 0 ? null : diarized2, (8060 & 16) != 0 ? null : null, (8060 & 32) != 0 ? null : providerId, (8060 & 64) != 0 ? null : null, (8060 & 128) != 0 ? null : null, (8060 & 256) != 0 ? null : null, (8060 & 512) != 0 ? null : candidates, (8060 & 1024) != 0 ? null : boolBoxBoolean, (8060 & 2048) != 0 ? false : true, (8060 & 4096) != 0 ? false : z4, anonymousClass4) == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    result2 = result;
                    segment6 = segment4;
                    segmentId2 = segmentId4;
                    wav3 = wav2;
                    status5 = status4;
                    i13 = i12;
                    started6 = started5;
                    long elapsed7 = System.currentTimeMillis() - started6;
                    segment9 = segment6;
                    long started13 = started6;
                    int i112 = i13;
                    status3 = status3;
                    Log.i(status3, "ASR " + segmentId2 + " via " + result2.getProviderId() + " audioMs=" + segment9.getDurationMs() + " chars=" + result2.getPlain().length() + " wallMs=" + elapsed7);
                    return Unit.INSTANCE;
                    long elapsed8 = System.currentTimeMillis() - started2;
                    Log.e(status3, "ASR failed for " + segmentId4 + " after " + elapsed8 + "ms", t);
                    sessionRepository6 = this.repository;
                    transcriptStatus6 = TranscriptStatus.FAILED;
                    message = t.getMessage();
                    if (message == null) {
                        message = t.getClass().getSimpleName();
                    }
                    anonymousClass3.L$0 = SpillingKt.nullOutSpilledVariable(segmentId4);
                    anonymousClass3.L$1 = SpillingKt.nullOutSpilledVariable(segment4);
                    anonymousClass3.L$2 = SpillingKt.nullOutSpilledVariable(status4);
                    anonymousClass3.L$3 = SpillingKt.nullOutSpilledVariable(wav2);
                    anonymousClass3.L$4 = t;
                    anonymousClass3.L$5 = null;
                    anonymousClass3.I$0 = i5;
                    anonymousClass3.J$0 = started2;
                    anonymousClass3.J$1 = elapsed8;
                    anonymousClass3.label = 8;
                    $continuation3 = anonymousClass3;
                    if (sessionRepository6.updateTranscript(segmentId4, transcriptStatus6, (8060 & 4) != 0 ? null : null, (8060 & 8) != 0 ? null : null, (8060 & 16) != 0 ? null : null, (8060 & 32) != 0 ? null : null, (8060 & 64) != 0 ? null : null, (8060 & 128) != 0 ? null : message, (8060 & 256) != 0 ? null : null, (8060 & 512) != 0 ? null : null, (8060 & 1024) != 0 ? null : null, (8060 & 2048) != 0 ? false : false, (8060 & 4096) != 0 ? false : false, $continuation3) == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    throw t;
                    i5 = i12;
                    started2 = started5;
                } catch (Throwable th16) {
                    t = th16;
                    wav2 = wav5;
                    status4 = status8;
                    segment4 = segment11;
                    anonymousClass3 = anonymousClass1;
                    coroutine_suspended = coroutine_suspended;
                    segmentId4 = segmentId4;
                    status3 = TAG;
                    started2 = started11;
                }
                long elapsed9 = System.currentTimeMillis() - started2;
                Log.e(status3, "ASR failed for " + segmentId4 + " after " + elapsed9 + "ms", t);
                sessionRepository6 = this.repository;
                transcriptStatus6 = TranscriptStatus.FAILED;
                message = t.getMessage();
                if (message == null) {
                    message = t.getClass().getSimpleName();
                }
                anonymousClass3.L$0 = SpillingKt.nullOutSpilledVariable(segmentId4);
                anonymousClass3.L$1 = SpillingKt.nullOutSpilledVariable(segment4);
                anonymousClass3.L$2 = SpillingKt.nullOutSpilledVariable(status4);
                anonymousClass3.L$3 = SpillingKt.nullOutSpilledVariable(wav2);
                anonymousClass3.L$4 = t;
                anonymousClass3.L$5 = null;
                anonymousClass3.I$0 = i5;
                anonymousClass3.J$0 = started2;
                anonymousClass3.J$1 = elapsed9;
                anonymousClass3.label = 8;
                $continuation3 = anonymousClass3;
                if (sessionRepository6.updateTranscript(segmentId4, transcriptStatus6, (8060 & 4) != 0 ? null : null, (8060 & 8) != 0 ? null : null, (8060 & 16) != 0 ? null : null, (8060 & 32) != 0 ? null : null, (8060 & 64) != 0 ? null : null, (8060 & 128) != 0 ? null : message, (8060 & 256) != 0 ? null : null, (8060 & 512) != 0 ? null : null, (8060 & 1024) != 0 ? null : null, (8060 & 2048) != 0 ? false : false, (8060 & 4096) != 0 ? false : false, $continuation3) == coroutine_suspended) {
                    return coroutine_suspended;
                }
                throw t;
            case 6:
                started7 = anonymousClass1.J$0;
                i5 = anonymousClass1.I$0;
                wav4 = (File) anonymousClass1.L$3;
                status6 = (String) anonymousClass1.L$2;
                segment8 = (SegmentEntity) anonymousClass1.L$1;
                String segmentId6 = (String) anonymousClass1.L$0;
                try {
                    ResultKt.throwOnFailure($result);
                    i12 = i5;
                    str2 = TAG;
                    segmentId3 = segmentId6;
                    anonymousClass3 = anonymousClass1;
                    coroutine_suspended = coroutine_suspended;
                    return Unit.INSTANCE;
                } catch (Throwable th17) {
                    t = th17;
                    anonymousClass3 = anonymousClass1;
                    coroutine_suspended = coroutine_suspended;
                    wav2 = wav4;
                    status4 = status6;
                    segment4 = segment8;
                    segmentId4 = segmentId6;
                    status3 = TAG;
                    started2 = started7;
                }
                break;
            case 7:
                int i23 = anonymousClass1.I$1;
                started6 = anonymousClass1.J$0;
                i13 = anonymousClass1.I$0;
                result2 = (AsrResult) anonymousClass1.L$4;
                wav3 = (File) anonymousClass1.L$3;
                status5 = (String) anonymousClass1.L$2;
                segment6 = (SegmentEntity) anonymousClass1.L$1;
                segmentId2 = (String) anonymousClass1.L$0;
                try {
                    ResultKt.throwOnFailure($result);
                    status3 = TAG;
                    anonymousClass3 = anonymousClass1;
                    coroutine_suspended = coroutine_suspended;
                    long elapsed10 = System.currentTimeMillis() - started6;
                    segment9 = segment6;
                    long started14 = started6;
                    int i113 = i13;
                    status3 = status3;
                    Log.i(status3, "ASR " + segmentId2 + " via " + result2.getProviderId() + " audioMs=" + segment9.getDurationMs() + " chars=" + result2.getPlain().length() + " wallMs=" + elapsed10);
                    return Unit.INSTANCE;
                } catch (Throwable th18) {
                    t = th18;
                    anonymousClass3 = anonymousClass1;
                    coroutine_suspended = coroutine_suspended;
                    segment4 = segment6;
                    wav2 = wav3;
                    status4 = status5;
                    segmentId4 = segmentId2;
                    i5 = i13;
                    status3 = TAG;
                    started2 = started6;
                }
                break;
            case 8:
                long j2 = anonymousClass1.J$1;
                long j3 = anonymousClass1.J$0;
                int i24 = anonymousClass1.I$0;
                Throwable t = (Throwable) anonymousClass1.L$4;
                ResultKt.throwOnFailure($result);
                throw t;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
    }
}
