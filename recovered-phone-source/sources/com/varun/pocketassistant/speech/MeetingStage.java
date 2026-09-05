package com.varun.pocketassistant.speech;

import android.util.Log;
import androidx.compose.runtime.ComposerKt;
import androidx.core.app.NotificationCompat;
import androidx.core.view.MotionEventCompat;
import com.varun.pocketassistant.data.MeetingEntity;
import com.varun.pocketassistant.data.MeetingRepository;
import com.varun.pocketassistant.data.SegmentEntity;
import com.varun.pocketassistant.pipeline.CleanupPrompts;
import com.varun.pocketassistant.pipeline.MeetingCleanupParsed;
import com.varun.pocketassistant.pipeline.MeetingCleanupParser;
import com.varun.pocketassistant.pipeline.PipelineConfig;
import com.varun.pocketassistant.pipeline.PipelineSettings;
import com.varun.pocketassistant.pipeline.ProviderRouter;
import com.varun.pocketassistant.pipeline.TextStageResult;
import com.varun.pocketassistant.pipeline.work.MeetingStageWorker;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import kotlin.Metadata;
import kotlin.ResultKt;
import kotlin.collections.CollectionsKt;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.ContinuationImpl;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SpillingKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;

/* JADX INFO: compiled from: MeetingStage.kt */
/* JADX INFO: loaded from: classes3.dex */
@Metadata(d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\b\b\u0007\u0018\u0000 \u00172\u00020\u0001:\u0001\u0017B\u001f\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007¢\u0006\u0004\b\b\u0010\tJ\u0016\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u0010H\u0086@¢\u0006\u0002\u0010\u0011J\u0016\u0010\u0012\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u0010H\u0086@¢\u0006\u0002\u0010\u0011J(\u0010\u0013\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0014\u001a\u00020\u00102\b\u0010\u0015\u001a\u0004\u0018\u00010\u0010H\u0082@¢\u0006\u0002\u0010\u0016R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004¢\u0006\u0002\n\u0000R\u0016\u0010\n\u001a\n \f*\u0004\u0018\u00010\u000b0\u000bX\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u0018"}, d2 = {"Lcom/varun/pocketassistant/speech/MeetingStage;", "", "repository", "Lcom/varun/pocketassistant/data/MeetingRepository;", "pipelineConfig", "Lcom/varun/pocketassistant/pipeline/PipelineConfig;", "router", "Lcom/varun/pocketassistant/pipeline/ProviderRouter;", "<init>", "(Lcom/varun/pocketassistant/data/MeetingRepository;Lcom/varun/pocketassistant/pipeline/PipelineConfig;Lcom/varun/pocketassistant/pipeline/ProviderRouter;)V", "timeFmt", "Ljava/text/DateFormat;", "kotlin.jvm.PlatformType", "runCleanup", "Lcom/varun/pocketassistant/speech/MeetingStageResult;", MeetingStageWorker.KEY_MEETING_ID, "", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "runSummary", "summarizeWithText", "cleanedText", "existingProvider", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "Companion", "app_debug"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class MeetingStage {
    private static final String TAG = "MeetingStage";
    private final PipelineConfig pipelineConfig;
    private final MeetingRepository repository;
    private final ProviderRouter router;
    private final DateFormat timeFmt;
    public static final int $stable = 8;

    /* JADX INFO: renamed from: com.varun.pocketassistant.speech.MeetingStage$runCleanup$1, reason: invalid class name */
    /* JADX INFO: compiled from: MeetingStage.kt */
    @Metadata(k = 3, mv = {2, 2, 0}, xi = 48)
    @DebugMetadata(c = "com.varun.pocketassistant.speech.MeetingStage", f = "MeetingStage.kt", i = {0, 1, 1, 1, 2, 2, 2, 2, 3, 3, 3, 3, 4, 4, 4, 4, 4, 5, 5, 5, 5, 5, 5, 6, 6, 6, 6, 6, 6, 6, 7, 7, 7, 7, 7, 7, 7, 7, 8, 8, 8, 8, 8, 8, 8, 8, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 10, 10, 10, 10, 10, 10, 10, 10, 10}, l = {33, MotionEventCompat.AXIS_GENERIC_15, 48, 58, 65, 73, 98, 110, 113, 121, 137}, m = "runCleanup", n = {MeetingStageWorker.KEY_MEETING_ID, MeetingStageWorker.KEY_MEETING_ID, "meeting", "settings", MeetingStageWorker.KEY_MEETING_ID, "meeting", "settings", "linked", MeetingStageWorker.KEY_MEETING_ID, "meeting", "settings", "linked", MeetingStageWorker.KEY_MEETING_ID, "meeting", "settings", "linked", "pendingAsr", MeetingStageWorker.KEY_MEETING_ID, "meeting", "settings", "linked", "recordings", NotificationCompat.CATEGORY_MESSAGE, MeetingStageWorker.KEY_MEETING_ID, "meeting", "settings", "linked", "recordings", "combined", NotificationCompat.CATEGORY_MESSAGE, MeetingStageWorker.KEY_MEETING_ID, "meeting", "settings", "linked", "recordings", "combined", "diarized", "cleanStarted", MeetingStageWorker.KEY_MEETING_ID, "meeting", "settings", "linked", "recordings", "combined", "diarized", "cleanStarted", MeetingStageWorker.KEY_MEETING_ID, "meeting", "settings", "linked", "recordings", "combined", "cleaned", "providerLabel", "diarized", "cleanStarted", MeetingStageWorker.KEY_MEETING_ID, "meeting", "settings", "linked", "recordings", "combined", "t", "diarized", "cleanStarted"}, s = {"L$0", "L$0", "L$1", "L$2", "L$0", "L$1", "L$2", "L$3", "L$0", "L$1", "L$2", "L$3", "L$0", "L$1", "L$2", "L$3", "I$0", "L$0", "L$1", "L$2", "L$3", "L$4", "L$5", "L$0", "L$1", "L$2", "L$3", "L$4", "L$5", "L$6", "L$0", "L$1", "L$2", "L$3", "L$4", "L$5", "I$0", "J$0", "L$0", "L$1", "L$2", "L$3", "L$4", "L$5", "I$0", "J$0", "L$0", "L$1", "L$2", "L$3", "L$4", "L$5", "L$6", "L$7", "I$0", "J$0", "L$0", "L$1", "L$2", "L$3", "L$4", "L$5", "L$6", "I$0", "J$0"})
    static final class AnonymousClass1 extends ContinuationImpl {
        int I$0;
        long J$0;
        Object L$0;
        Object L$1;
        Object L$2;
        Object L$3;
        Object L$4;
        Object L$5;
        Object L$6;
        Object L$7;
        int label;
        /* synthetic */ Object result;

        AnonymousClass1(Continuation<? super AnonymousClass1> continuation) {
            super(continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            this.result = obj;
            this.label |= Integer.MIN_VALUE;
            return MeetingStage.this.runCleanup(null, this);
        }
    }

    /* JADX INFO: renamed from: com.varun.pocketassistant.speech.MeetingStage$runSummary$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: MeetingStage.kt */
    @Metadata(k = 3, mv = {2, 2, 0}, xi = 48)
    @DebugMetadata(c = "com.varun.pocketassistant.speech.MeetingStage", f = "MeetingStage.kt", i = {0, 1, 1, 1, 2, 2, 2, 2, 3, 3, 3, 3, 3, 3, 4, 4, 4}, l = {148, 154, 156, 159, 161}, m = "runSummary", n = {MeetingStageWorker.KEY_MEETING_ID, MeetingStageWorker.KEY_MEETING_ID, "meeting", "cleanedText", MeetingStageWorker.KEY_MEETING_ID, "meeting", "cleanedText", "cleanResult", MeetingStageWorker.KEY_MEETING_ID, "meeting", "cleanedText", "cleanResult", "refreshed", "text", MeetingStageWorker.KEY_MEETING_ID, "meeting", "cleanedText"}, s = {"L$0", "L$0", "L$1", "L$2", "L$0", "L$1", "L$2", "L$3", "L$0", "L$1", "L$2", "L$3", "L$4", "L$5", "L$0", "L$1", "L$2"})
    static final class C06871 extends ContinuationImpl {
        Object L$0;
        Object L$1;
        Object L$2;
        Object L$3;
        Object L$4;
        Object L$5;
        int label;
        /* synthetic */ Object result;

        C06871(Continuation<? super C06871> continuation) {
            super(continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            this.result = obj;
            this.label |= Integer.MIN_VALUE;
            return MeetingStage.this.runSummary(null, this);
        }
    }

    /* JADX INFO: renamed from: com.varun.pocketassistant.speech.MeetingStage$summarizeWithText$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: MeetingStage.kt */
    @Metadata(k = 3, mv = {2, 2, 0}, xi = 48)
    @DebugMetadata(c = "com.varun.pocketassistant.speech.MeetingStage", f = "MeetingStage.kt", i = {0, 0, 0, 1, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 4, 4, 4, 4, 4, 4, 4}, l = {169, 173, 176, 187, ComposerKt.referenceKey}, m = "summarizeWithText", n = {MeetingStageWorker.KEY_MEETING_ID, "cleanedText", "existingProvider", MeetingStageWorker.KEY_MEETING_ID, "cleanedText", "existingProvider", "meeting", "settings", "summaryStarted", MeetingStageWorker.KEY_MEETING_ID, "cleanedText", "existingProvider", "meeting", "settings", "summaryStarted", MeetingStageWorker.KEY_MEETING_ID, "cleanedText", "existingProvider", "meeting", "settings", MeetingStageWorker.STAGE_SUMMARY, "assembled", "parsed", "providerLabel", "summaryStarted", MeetingStageWorker.KEY_MEETING_ID, "cleanedText", "existingProvider", "meeting", "settings", "t", "summaryStarted"}, s = {"L$0", "L$1", "L$2", "L$0", "L$1", "L$2", "L$3", "L$4", "J$0", "L$0", "L$1", "L$2", "L$3", "L$4", "J$0", "L$0", "L$1", "L$2", "L$3", "L$4", "L$5", "L$6", "L$7", "L$8", "J$0", "L$0", "L$1", "L$2", "L$3", "L$4", "L$5", "J$0"})
    static final class C06881 extends ContinuationImpl {
        long J$0;
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

        C06881(Continuation<? super C06881> continuation) {
            super(continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            this.result = obj;
            this.label |= Integer.MIN_VALUE;
            return MeetingStage.this.summarizeWithText(null, null, null, this);
        }
    }

    public MeetingStage(MeetingRepository repository, PipelineConfig pipelineConfig, ProviderRouter router) {
        Intrinsics.checkNotNullParameter(repository, "repository");
        Intrinsics.checkNotNullParameter(pipelineConfig, "pipelineConfig");
        Intrinsics.checkNotNullParameter(router, "router");
        this.repository = repository;
        this.pipelineConfig = pipelineConfig;
        this.router = router;
        this.timeFmt = DateFormat.getDateTimeInstance(3, 2);
    }

    /* JADX WARN: Code duplicated, block: B:101:0x03cd  */
    /* JADX WARN: Code duplicated, block: B:104:0x03e6  */
    /* JADX WARN: Code duplicated, block: B:106:0x03fe  */
    /* JADX WARN: Code duplicated, block: B:110:0x0415  */
    /* JADX WARN: Code duplicated, block: B:112:0x0467 A[RETURN] */
    /* JADX WARN: Code duplicated, block: B:113:0x0468  */
    /* JADX WARN: Code duplicated, block: B:116:0x0475  */
    /* JADX WARN: Code duplicated, block: B:119:0x0486  */
    /* JADX WARN: Code duplicated, block: B:121:0x0492  */
    /* JADX WARN: Code duplicated, block: B:124:0x04a1  */
    /* JADX WARN: Code duplicated, block: B:126:0x04a4  */
    /* JADX WARN: Code duplicated, block: B:128:0x04aa  */
    /* JADX WARN: Code duplicated, block: B:132:0x04bb A[ADDED_TO_REGION, REMOVE] */
    /* JADX WARN: Code duplicated, block: B:138:0x051d  */
    /* JADX WARN: Code duplicated, block: B:140:0x0575 A[RETURN] */
    /* JADX WARN: Code duplicated, block: B:141:0x0576  */
    /* JADX WARN: Code duplicated, block: B:144:0x0584  */
    /* JADX WARN: Code duplicated, block: B:146:0x058c  */
    /* JADX WARN: Code duplicated, block: B:149:0x0597  */
    /* JADX WARN: Code duplicated, block: B:152:0x05a1  */
    /* JADX WARN: Code duplicated, block: B:154:0x05b1  */
    /* JADX WARN: Code duplicated, block: B:158:0x05bb  */
    /* JADX WARN: Code duplicated, block: B:164:0x060f A[RETURN] */
    /* JADX WARN: Code duplicated, block: B:165:0x0610  */
    /* JADX WARN: Code duplicated, block: B:169:0x0618  */
    /* JADX WARN: Code duplicated, block: B:170:0x061a  */
    /* JADX WARN: Code duplicated, block: B:175:0x0643 A[RETURN] */
    /* JADX WARN: Code duplicated, block: B:176:0x0644  */
    /* JADX WARN: Code duplicated, block: B:179:0x065b A[Catch: all -> 0x07f6, TRY_LEAVE, TryCatch #8 {all -> 0x07f6, blocks: (B:177:0x064c, B:179:0x065b), top: B:258:0x064c }] */
    /* JADX WARN: Code duplicated, block: B:196:0x0723 A[RETURN] */
    /* JADX WARN: Code duplicated, block: B:197:0x0724  */
    /* JADX WARN: Code duplicated, block: B:217:0x07d6  */
    /* JADX WARN: Code duplicated, block: B:230:0x0846  */
    /* JADX WARN: Code duplicated, block: B:231:0x0849  */
    /* JADX WARN: Code duplicated, block: B:234:0x08a0 A[RETURN] */
    /* JADX WARN: Code duplicated, block: B:235:0x08a1  */
    /* JADX WARN: Code duplicated, block: B:238:0x08b2  */
    /* JADX WARN: Code duplicated, block: B:239:0x08b5  */
    /* JADX WARN: Code duplicated, block: B:260:0x0696 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:268:0x033f A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:269:0x0333 A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:275:0x0401 A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:277:0x04bf A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:278:0x04bf A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:281:0x0480 A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:282:0x05c1 A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:283:0x05bf A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:286:? A[LOOP:3: B:150:0x059b->B:286:?, LOOP_END, SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:48:0x0205  */
    /* JADX WARN: Code duplicated, block: B:50:0x0208  */
    /* JADX WARN: Code duplicated, block: B:52:0x020b  */
    /* JADX WARN: Code duplicated, block: B:54:0x0263 A[RETURN] */
    /* JADX WARN: Code duplicated, block: B:55:0x0264  */
    /* JADX WARN: Code duplicated, block: B:58:0x026f  */
    /* JADX WARN: Code duplicated, block: B:60:0x02b9 A[RETURN] */
    /* JADX WARN: Code duplicated, block: B:61:0x02ba  */
    /* JADX WARN: Code duplicated, block: B:64:0x02c6  */
    /* JADX WARN: Code duplicated, block: B:66:0x02db A[RETURN] */
    /* JADX WARN: Code duplicated, block: B:67:0x02dc  */
    /* JADX WARN: Code duplicated, block: B:70:0x02e8  */
    /* JADX WARN: Code duplicated, block: B:72:0x02f0  */
    /* JADX WARN: Code duplicated, block: B:75:0x02fe  */
    /* JADX WARN: Code duplicated, block: B:78:0x0309  */
    /* JADX WARN: Code duplicated, block: B:7:0x001a  */
    /* JADX WARN: Code duplicated, block: B:80:0x0321  */
    /* JADX WARN: Code duplicated, block: B:84:0x0330  */
    /* JADX WARN: Code duplicated, block: B:88:0x0337  */
    /* JADX WARN: Code duplicated, block: B:94:0x037c  */
    /* JADX WARN: Code duplicated, block: B:96:0x03c1 A[RETURN] */
    /* JADX WARN: Code duplicated, block: B:97:0x03c2  */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Not initialized variable reg: 16, insn: 0x07ed: MOVE (r12 I:??[OBJECT, ARRAY]) = (r16 I:??[OBJECT, ARRAY] A[D('linked' java.util.List)]), block:B:221:0x07ed */
    /* JADX WARN: Not initialized variable reg: 17, insn: 0x07f3: MOVE (r15 I:??[OBJECT, ARRAY] A[D('meeting' com.varun.pocketassistant.data.MeetingEntity)]) = (r17 I:??[OBJECT, ARRAY] A[D('combined' java.lang.String)]), block:B:221:0x07ed */
    /* JADX WARN: Not initialized variable reg: 39, insn: 0x07ef: MOVE (r14 I:??[OBJECT, ARRAY]) = (r39 I:??[OBJECT, ARRAY] A[D('settings' com.varun.pocketassistant.pipeline.PipelineSettings)]), block:B:221:0x07ed */
    public final Object runCleanup(String meetingId, Continuation<? super MeetingStageResult> continuation) {
        AnonymousClass1 anonymousClass1;
        Object meeting;
        MeetingEntity meeting2;
        String cleanTextOnly;
        boolean z;
        PipelineSettings settings;
        Object recordings;
        MeetingEntity meeting3;
        List linked;
        MeetingEntity meeting4;
        Object objAllRecordingsAsrSettled;
        PipelineSettings settings2;
        List linked2;
        MeetingEntity meeting5;
        MeetingRepository meetingRepository;
        MeetingEntity meetingEntityCopy$default;
        List list;
        int i;
        Collection arrayList;
        List recordings2;
        StringBuilder sb;
        int i2;
        Iterator it;
        String str;
        String combined;
        List list2;
        Iterator it2;
        int i3;
        String diarizedTranscript;
        boolean z2;
        int i4;
        long cleanStarted;
        MeetingRepository meetingRepository2;
        MeetingEntity meetingEntityCopy$default2;
        MeetingEntity meeting6;
        String meetingId2;
        int i5;
        String meetingId3;
        MeetingRepository meetingRepository3;
        MeetingEntity meetingEntityCopy$default3;
        String msg;
        SegmentEntity segmentEntity;
        String diarizedTranscript2;
        MeetingRepository meetingRepository4;
        MeetingEntity meetingEntityCopy$default4;
        String msg2;
        List<SegmentEntity> list3;
        int i6;
        int i7;
        boolean z3;
        int pendingAsr;
        MeetingRepository meetingRepository5;
        MeetingEntity meetingEntityCopy$default5;
        List linked3;
        PipelineSettings settings3;
        MeetingEntity meeting7;
        List recordings3;
        List linked4;
        MeetingEntity meetingEntity;
        String combined2;
        int i8;
        String combined3;
        boolean z4;
        String meetingId4;
        Object objClean;
        MeetingEntity meeting8;
        String combined4;
        PipelineSettings settings4;
        TextStageResult cleaned;
        List linked5;
        PipelineSettings settings5;
        String combined5;
        StringBuilder sb2;
        PipelineSettings settings6;
        List linked6;
        String combined6;
        MeetingRepository meetingRepository6;
        MeetingEntity meetingEntityCopy$default6;
        MeetingEntity meeting9;
        String meetingId5;
        String $result;
        PipelineSettings settings7;
        List recordings4;
        MeetingRepository meetingRepository7;
        String message;
        String str2;
        MeetingEntity meetingEntityCopy$default7;
        long cleanStarted2;
        int i9;
        String combined7;
        List recordings5;
        Throwable t;
        String meetingId6;
        long cleanStarted3;
        String message2;
        String str3;
        String meetingId7 = meetingId;
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
        Object $result2 = anonymousClass1.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        String str4 = "Cleanup failed";
        switch (anonymousClass1.label) {
            case 0:
                ResultKt.throwOnFailure($result2);
                MeetingRepository meetingRepository8 = this.repository;
                anonymousClass1.L$0 = meetingId7;
                anonymousClass1.label = 1;
                meeting = meetingRepository8.getMeeting(meetingId7, anonymousClass1);
                if (meeting == coroutine_suspended) {
                    return coroutine_suspended;
                }
                meeting2 = (MeetingEntity) meeting;
                if (meeting2 == null && !Intrinsics.areEqual(meeting2.getStatus(), "READY")) {
                    cleanTextOnly = meeting2.getCleanTextOnly();
                    if (cleanTextOnly != null || StringsKt.isBlank(cleanTextOnly)) {
                        z = true;
                    } else {
                        z = false;
                    }
                    if (!z) {
                        return MeetingStageResult.Success.INSTANCE;
                    }
                    settings = this.pipelineConfig.load();
                    Log.i(TAG, "Meeting " + meetingId7 + " cleanupMode=" + settings.getCleanupMode() + " cloudConfigured=" + this.pipelineConfig.cloudConfigured() + " cleanupModel=" + settings.getCloudCleanupModel());
                    MeetingRepository meetingRepository9 = this.repository;
                    anonymousClass1.L$0 = meetingId7;
                    anonymousClass1.L$1 = meeting2;
                    anonymousClass1.L$2 = settings;
                    anonymousClass1.label = 2;
                    recordings = meetingRepository9.getRecordings(meetingId7, anonymousClass1);
                    if (recordings == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    meeting3 = meeting2;
                    linked = (List) recordings;
                    if (linked.isEmpty()) {
                        meetingRepository = this.repository;
                        meetingEntityCopy$default = MeetingEntity.copy$default(meeting3, null, null, 0L, 0L, "FAILED", "No recordings in this time range.", null, null, 0L, null, "No recordings in this time range.", 0L, 3023, null);
                        anonymousClass1.L$0 = SpillingKt.nullOutSpilledVariable(meetingId7);
                        anonymousClass1.L$1 = SpillingKt.nullOutSpilledVariable(meeting3);
                        anonymousClass1.L$2 = SpillingKt.nullOutSpilledVariable(settings);
                        anonymousClass1.L$3 = SpillingKt.nullOutSpilledVariable(linked);
                        anonymousClass1.label = 3;
                        if (meetingRepository.updateMeeting(meetingEntityCopy$default, anonymousClass1) == coroutine_suspended) {
                            return coroutine_suspended;
                        }
                        return new MeetingStageResult.Failed("No recordings", false);
                    }
                    meeting4 = meeting3;
                    MeetingRepository meetingRepository10 = this.repository;
                    anonymousClass1.L$0 = meetingId7;
                    anonymousClass1.L$1 = meeting4;
                    anonymousClass1.L$2 = settings;
                    anonymousClass1.L$3 = linked;
                    anonymousClass1.label = 4;
                    objAllRecordingsAsrSettled = meetingRepository10.allRecordingsAsrSettled(meetingId7, anonymousClass1);
                    if (objAllRecordingsAsrSettled == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    settings2 = settings;
                    linked2 = linked;
                    meeting5 = meeting4;
                    if (!((Boolean) objAllRecordingsAsrSettled).booleanValue()) {
                        list3 = linked2;
                        if ((list3 instanceof Collection) || !list3.isEmpty()) {
                            i6 = 0;
                            for (SegmentEntity segmentEntity2 : list3) {
                                list3 = list3;
                                $result2 = $result2;
                                if (!Intrinsics.areEqual(segmentEntity2.getTranscriptStatus(), "PENDING") || Intrinsics.areEqual(segmentEntity2.getTranscriptStatus(), "PROCESSING")) {
                                    z3 = true;
                                } else {
                                    z3 = false;
                                }
                                if (z3) {
                                    i6++;
                                    if (i6 < 0) {
                                        CollectionsKt.throwCountOverflow();
                                    }
                                }
                            }
                            i7 = i6;
                        } else {
                            i7 = 0;
                        }
                        pendingAsr = i7;
                        Log.i(TAG, "Meeting " + meetingId7 + " waiting on ASR (" + pendingAsr + " still in progress)");
                        if (!Intrinsics.areEqual(meeting5.getStatus(), "PENDING_CLEANUP")) {
                            meetingRepository5 = this.repository;
                            meetingEntityCopy$default5 = MeetingEntity.copy$default(meeting5, null, null, 0L, 0L, "PENDING_CLEANUP", null, null, null, 0L, null, null, 0L, 4079, null);
                            anonymousClass1.L$0 = SpillingKt.nullOutSpilledVariable(meetingId7);
                            anonymousClass1.L$1 = SpillingKt.nullOutSpilledVariable(meeting5);
                            anonymousClass1.L$2 = SpillingKt.nullOutSpilledVariable(settings2);
                            anonymousClass1.L$3 = SpillingKt.nullOutSpilledVariable(linked2);
                            anonymousClass1.I$0 = pendingAsr;
                            anonymousClass1.label = 5;
                            if (meetingRepository5.updateMeeting(meetingEntityCopy$default5, anonymousClass1) == coroutine_suspended) {
                                return coroutine_suspended;
                            }
                            linked3 = linked2;
                            settings3 = settings2;
                            meeting7 = meeting5;
                        }
                        return MeetingStageResult.WaitingAsr.INSTANCE;
                    }
                    list = linked2;
                    i = 0;
                    arrayList = new ArrayList();
                    for (Object obj : list) {
                        Iterable iterable = list;
                        int i10 = i;
                        if (Intrinsics.areEqual(((SegmentEntity) obj).getTranscriptStatus(), "READY")) {
                            arrayList.add(obj);
                        }
                        list = iterable;
                        i = i10;
                    }
                    recordings2 = (List) arrayList;
                    if (recordings2.isEmpty()) {
                        meetingRepository4 = this.repository;
                        meetingEntityCopy$default4 = MeetingEntity.copy$default(meeting5, null, null, 0L, 0L, "FAILED", "No usable transcripts in this time range (ASR skipped or failed).", null, null, 0L, null, "No usable transcripts in this time range (ASR skipped or failed).", 0L, 3023, null);
                        anonymousClass1.L$0 = SpillingKt.nullOutSpilledVariable(meetingId7);
                        anonymousClass1.L$1 = SpillingKt.nullOutSpilledVariable(meeting5);
                        anonymousClass1.L$2 = SpillingKt.nullOutSpilledVariable(settings2);
                        anonymousClass1.L$3 = SpillingKt.nullOutSpilledVariable(linked2);
                        anonymousClass1.L$4 = SpillingKt.nullOutSpilledVariable(recordings2);
                        anonymousClass1.L$5 = "No usable transcripts in this time range (ASR skipped or failed).";
                        anonymousClass1.label = 6;
                        if (meetingRepository4.updateMeeting(meetingEntityCopy$default4, anonymousClass1) == coroutine_suspended) {
                            return coroutine_suspended;
                        }
                        msg2 = "No usable transcripts in this time range (ASR skipped or failed).";
                        return new MeetingStageResult.Failed(msg2, false);
                    }
                    sb = new StringBuilder();
                    i2 = 0;
                    it = recordings2.iterator();
                    while (it.hasNext()) {
                        segmentEntity = (SegmentEntity) it.next();
                        diarizedTranscript2 = segmentEntity.getDiarizedTranscript();
                        if (diarizedTranscript2 == null) {
                            diarizedTranscript2 = segmentEntity.getTranscript();
                            if (diarizedTranscript2 != null || StringsKt.isBlank(diarizedTranscript2)) {
                                diarizedTranscript2 = null;
                            }
                            if (diarizedTranscript2 == null) {
                                sb.append("--- ");
                                sb.append(this.timeFmt.format(new Date(segmentEntity.getStartedAtMs())));
                                sb.append(" ---\n");
                                sb.append(StringsKt.trim((CharSequence) diarizedTranscript2).toString());
                                sb.append("\n\n");
                                sb = sb;
                                i2 = i2;
                                it = it;
                                str4 = str4;
                            }
                        } else {
                            if (StringsKt.isBlank(diarizedTranscript2)) {
                                diarizedTranscript2 = null;
                            }
                            if (diarizedTranscript2 == null) {
                                diarizedTranscript2 = segmentEntity.getTranscript();
                                if (diarizedTranscript2 != null) {
                                    diarizedTranscript2 = null;
                                } else {
                                    diarizedTranscript2 = null;
                                }
                                if (diarizedTranscript2 == null) {
                                }
                            }
                            sb.append("--- ");
                            sb.append(this.timeFmt.format(new Date(segmentEntity.getStartedAtMs())));
                            sb.append(" ---\n");
                            sb.append(StringsKt.trim((CharSequence) diarizedTranscript2).toString());
                            sb.append("\n\n");
                            sb = sb;
                            i2 = i2;
                            it = it;
                            str4 = str4;
                        }
                    }
                    str = str4;
                    combined = StringsKt.trim((CharSequence) sb.toString()).toString();
                    if (StringsKt.isBlank(combined)) {
                        meetingRepository3 = this.repository;
                        meetingEntityCopy$default3 = MeetingEntity.copy$default(meeting5, null, null, 0L, 0L, "FAILED", "Recordings had empty transcripts.", null, null, 0L, null, "Recordings had empty transcripts.", 0L, 3023, null);
                        anonymousClass1.L$0 = SpillingKt.nullOutSpilledVariable(meetingId7);
                        anonymousClass1.L$1 = SpillingKt.nullOutSpilledVariable(meeting5);
                        anonymousClass1.L$2 = SpillingKt.nullOutSpilledVariable(settings2);
                        anonymousClass1.L$3 = SpillingKt.nullOutSpilledVariable(linked2);
                        anonymousClass1.L$4 = SpillingKt.nullOutSpilledVariable(recordings2);
                        anonymousClass1.L$5 = SpillingKt.nullOutSpilledVariable(combined);
                        anonymousClass1.L$6 = "Recordings had empty transcripts.";
                        anonymousClass1.label = 7;
                        if (meetingRepository3.updateMeeting(meetingEntityCopy$default3, anonymousClass1) == coroutine_suspended) {
                            return coroutine_suspended;
                        }
                        msg = "Recordings had empty transcripts.";
                        return new MeetingStageResult.Failed(msg, false);
                    }
                    list2 = recordings2;
                    if ((list2 instanceof Collection) || !list2.isEmpty()) {
                        it2 = list2.iterator();
                        while (true) {
                            if (it2.hasNext()) {
                                diarizedTranscript = ((SegmentEntity) it2.next()).getDiarizedTranscript();
                                if (diarizedTranscript != null || StringsKt.isBlank(diarizedTranscript)) {
                                    z2 = true;
                                } else {
                                    z2 = false;
                                }
                                if (!z2) {
                                    i3 = 1;
                                }
                            } else {
                                i3 = 0;
                            }
                        }
                    } else {
                        i3 = 0;
                    }
                    i4 = i3;
                    cleanStarted = System.currentTimeMillis();
                    meetingRepository2 = this.repository;
                    meetingEntityCopy$default2 = MeetingEntity.copy$default(meeting5, null, null, 0L, 0L, "CLEANING", null, null, null, 0L, null, null, 0L, 3055, null);
                    meeting6 = meeting5;
                    anonymousClass1.L$0 = meetingId7;
                    anonymousClass1.L$1 = meeting6;
                    anonymousClass1.L$2 = settings2;
                    meetingId2 = meetingId7;
                    anonymousClass1.L$3 = SpillingKt.nullOutSpilledVariable(linked2);
                    anonymousClass1.L$4 = SpillingKt.nullOutSpilledVariable(recordings2);
                    anonymousClass1.L$5 = combined;
                    anonymousClass1.I$0 = i4;
                    anonymousClass1.J$0 = cleanStarted;
                    anonymousClass1.label = 8;
                    if (meetingRepository2.updateMeeting(meetingEntityCopy$default2, anonymousClass1) == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    i5 = i4;
                    meetingId3 = meetingId2;
                    try {
                        ProviderRouter providerRouter = this.router;
                        if (i5 != 0) {
                            z4 = true;
                        } else {
                            z4 = false;
                        }
                        anonymousClass1.L$0 = meetingId3;
                        anonymousClass1.L$1 = meeting6;
                        anonymousClass1.L$2 = settings2;
                        meetingId4 = meetingId3;
                        try {
                            anonymousClass1.L$3 = SpillingKt.nullOutSpilledVariable(linked2);
                            anonymousClass1.L$4 = SpillingKt.nullOutSpilledVariable(recordings2);
                            anonymousClass1.L$5 = SpillingKt.nullOutSpilledVariable(combined);
                            anonymousClass1.I$0 = i5;
                            anonymousClass1.J$0 = cleanStarted;
                            anonymousClass1.label = 9;
                            objClean = providerRouter.clean(combined, z4, anonymousClass1);
                            if (objClean == coroutine_suspended) {
                                return coroutine_suspended;
                            }
                            String str5 = combined;
                            i8 = i5;
                            combined3 = meetingId4;
                            meeting8 = meeting6;
                            combined4 = str5;
                            settings4 = settings2;
                            try {
                                cleaned = (TextStageResult) objClean;
                                try {
                                    if (!StringsKt.isBlank(cleaned.getText())) {
                                        throw new IllegalStateException("cleanup returned empty text".toString());
                                    }
                                    sb2 = new StringBuilder();
                                    settings6 = settings4;
                                    try {
                                        recordings3 = recordings2;
                                        try {
                                            sb2.append("clean=" + cleaned.getProviderId());
                                            linked6 = linked2;
                                            combined6 = combined4;
                                            try {
                                                if (StringsKt.startsWith$default(cleaned.getProviderId(), "cloud", false, 2, (Object) null)) {
                                                    try {
                                                        sb2.append("(" + settings6.getCloudCleanupModel() + ")");
                                                    } catch (Throwable th) {
                                                        t = th;
                                                        linked4 = linked6;
                                                        combined2 = combined6;
                                                        meetingEntity = meeting8;
                                                        settings2 = settings6;
                                                    }
                                                }
                                                String providerLabel = sb2.toString();
                                                meetingRepository6 = this.repository;
                                                meetingEntityCopy$default6 = MeetingEntity.copy$default(meeting8, null, null, 0L, 0L, "PENDING_CLEANUP", null, null, providerLabel, 0L, cleaned.getText(), null, 0L, 2415, null);
                                                meeting9 = meeting8;
                                                anonymousClass1.L$0 = combined3;
                                                anonymousClass1.L$1 = meeting9;
                                                anonymousClass1.L$2 = SpillingKt.nullOutSpilledVariable(settings6);
                                                anonymousClass1.L$3 = SpillingKt.nullOutSpilledVariable(linked6);
                                                anonymousClass1.L$4 = SpillingKt.nullOutSpilledVariable(recordings3);
                                                anonymousClass1.L$5 = SpillingKt.nullOutSpilledVariable(combined6);
                                                anonymousClass1.L$6 = cleaned;
                                                anonymousClass1.L$7 = SpillingKt.nullOutSpilledVariable(providerLabel);
                                                anonymousClass1.I$0 = i8;
                                                anonymousClass1.J$0 = cleanStarted;
                                                anonymousClass1.label = 10;
                                                if (meetingRepository6.updateMeeting(meetingEntityCopy$default6, anonymousClass1) == coroutine_suspended) {
                                                    return coroutine_suspended;
                                                }
                                                meetingId5 = combined3;
                                                $result = combined6;
                                                settings7 = settings6;
                                                recordings4 = recordings3;
                                                try {
                                                    cleanStarted3 = cleanStarted;
                                                    try {
                                                        String meetingId8 = meetingId5;
                                                        try {
                                                            Log.i(TAG, "Meeting " + meetingId5 + " cleanup ok chars=" + cleaned.getText().length() + " ms=" + (System.currentTimeMillis() - cleanStarted3));
                                                            return MeetingStageResult.Success.INSTANCE;
                                                        } catch (Throwable th2) {
                                                            t = th2;
                                                            recordings3 = recordings4;
                                                            linked4 = linked6;
                                                            settings2 = settings7;
                                                            cleanStarted = cleanStarted3;
                                                            meetingEntity = meeting9;
                                                            combined2 = $result;
                                                            combined3 = meetingId8;
                                                        }
                                                    } catch (Throwable th3) {
                                                        t = th3;
                                                        recordings3 = recordings4;
                                                        linked4 = linked6;
                                                        settings2 = settings7;
                                                        cleanStarted = cleanStarted3;
                                                        meetingEntity = meeting9;
                                                        combined2 = $result;
                                                        combined3 = meetingId5;
                                                    }
                                                } catch (Throwable th4) {
                                                    t = th4;
                                                    recordings3 = recordings4;
                                                    linked4 = linked6;
                                                    settings2 = settings7;
                                                    meetingEntity = meeting9;
                                                    combined2 = $result;
                                                    combined3 = meetingId5;
                                                }
                                            } catch (Throwable th5) {
                                                t = th5;
                                                linked4 = linked6;
                                                settings2 = settings6;
                                                meetingEntity = meeting8;
                                                combined2 = combined6;
                                            }
                                        } catch (Throwable th6) {
                                            t = th6;
                                            linked4 = linked2;
                                            settings2 = settings6;
                                            meetingEntity = meeting8;
                                            combined2 = combined4;
                                        }
                                    } catch (Throwable th7) {
                                        t = th7;
                                        recordings3 = recordings2;
                                        linked4 = linked2;
                                        settings2 = settings6;
                                        meetingEntity = meeting8;
                                        combined2 = combined4;
                                    }
                                } catch (Throwable th8) {
                                    t = th8;
                                    linked4 = linked5;
                                    settings2 = settings5;
                                    meetingEntity = combined4;
                                    combined2 = combined5;
                                }
                                break;
                            } catch (Throwable th9) {
                                t = th9;
                                recordings3 = recordings2;
                                linked4 = linked2;
                                settings2 = settings4;
                                meetingEntity = meeting8;
                                combined2 = combined4;
                            }
                            Log.e(TAG, "Meeting " + combined3 + " cleanup failed", t);
                            meetingRepository7 = this.repository;
                            message = t.getMessage();
                            if (message == null) {
                                str2 = str;
                            } else {
                                str2 = message;
                            }
                            meetingEntityCopy$default7 = MeetingEntity.copy$default(meetingEntity, null, null, 0L, 0L, "FAILED", null, null, null, 0L, null, str2, 0L, 3055, null);
                            anonymousClass1.L$0 = SpillingKt.nullOutSpilledVariable(combined3);
                            anonymousClass1.L$1 = SpillingKt.nullOutSpilledVariable(meetingEntity);
                            anonymousClass1.L$2 = SpillingKt.nullOutSpilledVariable(settings2);
                            anonymousClass1.L$3 = SpillingKt.nullOutSpilledVariable(linked4);
                            anonymousClass1.L$4 = SpillingKt.nullOutSpilledVariable(recordings3);
                            anonymousClass1.L$5 = SpillingKt.nullOutSpilledVariable(combined2);
                            anonymousClass1.L$6 = t;
                            anonymousClass1.L$7 = null;
                            anonymousClass1.I$0 = i8;
                            anonymousClass1.J$0 = cleanStarted;
                            anonymousClass1.label = 11;
                            if (meetingRepository7.updateMeeting(meetingEntityCopy$default7, anonymousClass1) == coroutine_suspended) {
                                return coroutine_suspended;
                            }
                            cleanStarted2 = cleanStarted;
                            i9 = i8;
                            combined7 = combined2;
                            recordings5 = recordings3;
                            t = t;
                            meetingId6 = combined3;
                            message2 = t.getMessage();
                            if (message2 == null) {
                                str3 = str;
                            } else {
                                str3 = message2;
                            }
                            return new MeetingStageResult.Failed(str3, true);
                        } catch (Throwable th10) {
                            t = th10;
                            recordings3 = recordings2;
                            linked4 = linked2;
                            meetingEntity = meeting6;
                            combined2 = combined;
                            i8 = i5;
                            combined3 = meetingId4;
                        }
                    } catch (Throwable th11) {
                        t = th11;
                        recordings3 = recordings2;
                        linked4 = linked2;
                        meetingEntity = meeting6;
                        combined2 = combined;
                        i8 = i5;
                        combined3 = meetingId3;
                    }
                }
                return MeetingStageResult.Success.INSTANCE;
            case 1:
                meetingId7 = (String) anonymousClass1.L$0;
                ResultKt.throwOnFailure($result2);
                meeting = $result2;
                meeting2 = (MeetingEntity) meeting;
                if (meeting2 == null) {
                    return MeetingStageResult.Success.INSTANCE;
                }
                cleanTextOnly = meeting2.getCleanTextOnly();
                if (cleanTextOnly != null) {
                    z = true;
                } else {
                    z = true;
                }
                if (!z) {
                    return MeetingStageResult.Success.INSTANCE;
                }
                settings = this.pipelineConfig.load();
                Log.i(TAG, "Meeting " + meetingId7 + " cleanupMode=" + settings.getCleanupMode() + " cloudConfigured=" + this.pipelineConfig.cloudConfigured() + " cleanupModel=" + settings.getCloudCleanupModel());
                MeetingRepository meetingRepository11 = this.repository;
                anonymousClass1.L$0 = meetingId7;
                anonymousClass1.L$1 = meeting2;
                anonymousClass1.L$2 = settings;
                anonymousClass1.label = 2;
                recordings = meetingRepository11.getRecordings(meetingId7, anonymousClass1);
                if (recordings == coroutine_suspended) {
                    return coroutine_suspended;
                }
                meeting3 = meeting2;
                linked = (List) recordings;
                if (linked.isEmpty()) {
                    meetingRepository = this.repository;
                    meetingEntityCopy$default = MeetingEntity.copy$default(meeting3, null, null, 0L, 0L, "FAILED", "No recordings in this time range.", null, null, 0L, null, "No recordings in this time range.", 0L, 3023, null);
                    anonymousClass1.L$0 = SpillingKt.nullOutSpilledVariable(meetingId7);
                    anonymousClass1.L$1 = SpillingKt.nullOutSpilledVariable(meeting3);
                    anonymousClass1.L$2 = SpillingKt.nullOutSpilledVariable(settings);
                    anonymousClass1.L$3 = SpillingKt.nullOutSpilledVariable(linked);
                    anonymousClass1.label = 3;
                    if (meetingRepository.updateMeeting(meetingEntityCopy$default, anonymousClass1) == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    return new MeetingStageResult.Failed("No recordings", false);
                }
                meeting4 = meeting3;
                MeetingRepository meetingRepository12 = this.repository;
                anonymousClass1.L$0 = meetingId7;
                anonymousClass1.L$1 = meeting4;
                anonymousClass1.L$2 = settings;
                anonymousClass1.L$3 = linked;
                anonymousClass1.label = 4;
                objAllRecordingsAsrSettled = meetingRepository12.allRecordingsAsrSettled(meetingId7, anonymousClass1);
                if (objAllRecordingsAsrSettled == coroutine_suspended) {
                    return coroutine_suspended;
                }
                settings2 = settings;
                linked2 = linked;
                meeting5 = meeting4;
                if (!((Boolean) objAllRecordingsAsrSettled).booleanValue()) {
                    list3 = linked2;
                    if (list3 instanceof Collection) {
                        i6 = 0;
                        while (r8.hasNext()) {
                            list3 = list3;
                            $result2 = $result2;
                            if (Intrinsics.areEqual(segmentEntity2.getTranscriptStatus(), "PENDING")) {
                                z3 = true;
                            } else {
                                z3 = true;
                            }
                            if (z3) {
                                i6++;
                                if (i6 < 0) {
                                    CollectionsKt.throwCountOverflow();
                                }
                            }
                        }
                        i7 = i6;
                    } else {
                        i6 = 0;
                        while (r8.hasNext()) {
                            list3 = list3;
                            $result2 = $result2;
                            if (Intrinsics.areEqual(segmentEntity2.getTranscriptStatus(), "PENDING")) {
                                z3 = true;
                            } else {
                                z3 = true;
                            }
                            if (z3) {
                                i6++;
                                if (i6 < 0) {
                                    CollectionsKt.throwCountOverflow();
                                }
                            }
                        }
                        i7 = i6;
                    }
                    pendingAsr = i7;
                    Log.i(TAG, "Meeting " + meetingId7 + " waiting on ASR (" + pendingAsr + " still in progress)");
                    if (!Intrinsics.areEqual(meeting5.getStatus(), "PENDING_CLEANUP")) {
                        meetingRepository5 = this.repository;
                        meetingEntityCopy$default5 = MeetingEntity.copy$default(meeting5, null, null, 0L, 0L, "PENDING_CLEANUP", null, null, null, 0L, null, null, 0L, 4079, null);
                        anonymousClass1.L$0 = SpillingKt.nullOutSpilledVariable(meetingId7);
                        anonymousClass1.L$1 = SpillingKt.nullOutSpilledVariable(meeting5);
                        anonymousClass1.L$2 = SpillingKt.nullOutSpilledVariable(settings2);
                        anonymousClass1.L$3 = SpillingKt.nullOutSpilledVariable(linked2);
                        anonymousClass1.I$0 = pendingAsr;
                        anonymousClass1.label = 5;
                        if (meetingRepository5.updateMeeting(meetingEntityCopy$default5, anonymousClass1) == coroutine_suspended) {
                            return coroutine_suspended;
                        }
                        linked3 = linked2;
                        settings3 = settings2;
                        meeting7 = meeting5;
                    }
                    return MeetingStageResult.WaitingAsr.INSTANCE;
                }
                list = linked2;
                i = 0;
                arrayList = new ArrayList();
                while (r15.hasNext()) {
                    Iterable iterable2 = list;
                    int i11 = i;
                    if (Intrinsics.areEqual(((SegmentEntity) obj).getTranscriptStatus(), "READY")) {
                        arrayList.add(obj);
                    }
                    list = iterable2;
                    i = i11;
                }
                recordings2 = (List) arrayList;
                if (recordings2.isEmpty()) {
                    meetingRepository4 = this.repository;
                    meetingEntityCopy$default4 = MeetingEntity.copy$default(meeting5, null, null, 0L, 0L, "FAILED", "No usable transcripts in this time range (ASR skipped or failed).", null, null, 0L, null, "No usable transcripts in this time range (ASR skipped or failed).", 0L, 3023, null);
                    anonymousClass1.L$0 = SpillingKt.nullOutSpilledVariable(meetingId7);
                    anonymousClass1.L$1 = SpillingKt.nullOutSpilledVariable(meeting5);
                    anonymousClass1.L$2 = SpillingKt.nullOutSpilledVariable(settings2);
                    anonymousClass1.L$3 = SpillingKt.nullOutSpilledVariable(linked2);
                    anonymousClass1.L$4 = SpillingKt.nullOutSpilledVariable(recordings2);
                    anonymousClass1.L$5 = "No usable transcripts in this time range (ASR skipped or failed).";
                    anonymousClass1.label = 6;
                    if (meetingRepository4.updateMeeting(meetingEntityCopy$default4, anonymousClass1) == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    msg2 = "No usable transcripts in this time range (ASR skipped or failed).";
                    return new MeetingStageResult.Failed(msg2, false);
                }
                sb = new StringBuilder();
                i2 = 0;
                it = recordings2.iterator();
                while (it.hasNext()) {
                    segmentEntity = (SegmentEntity) it.next();
                    diarizedTranscript2 = segmentEntity.getDiarizedTranscript();
                    if (diarizedTranscript2 == null) {
                        diarizedTranscript2 = segmentEntity.getTranscript();
                        if (diarizedTranscript2 != null) {
                            diarizedTranscript2 = null;
                        } else {
                            diarizedTranscript2 = null;
                        }
                        if (diarizedTranscript2 == null) {
                            sb.append("--- ");
                            sb.append(this.timeFmt.format(new Date(segmentEntity.getStartedAtMs())));
                            sb.append(" ---\n");
                            sb.append(StringsKt.trim((CharSequence) diarizedTranscript2).toString());
                            sb.append("\n\n");
                            sb = sb;
                            i2 = i2;
                            it = it;
                            str4 = str4;
                        }
                    } else {
                        if (StringsKt.isBlank(diarizedTranscript2)) {
                            diarizedTranscript2 = null;
                        }
                        if (diarizedTranscript2 == null) {
                            diarizedTranscript2 = segmentEntity.getTranscript();
                            if (diarizedTranscript2 != null) {
                                diarizedTranscript2 = null;
                            } else {
                                diarizedTranscript2 = null;
                            }
                            if (diarizedTranscript2 == null) {
                            }
                        }
                        sb.append("--- ");
                        sb.append(this.timeFmt.format(new Date(segmentEntity.getStartedAtMs())));
                        sb.append(" ---\n");
                        sb.append(StringsKt.trim((CharSequence) diarizedTranscript2).toString());
                        sb.append("\n\n");
                        sb = sb;
                        i2 = i2;
                        it = it;
                        str4 = str4;
                    }
                }
                str = str4;
                combined = StringsKt.trim((CharSequence) sb.toString()).toString();
                if (StringsKt.isBlank(combined)) {
                    meetingRepository3 = this.repository;
                    meetingEntityCopy$default3 = MeetingEntity.copy$default(meeting5, null, null, 0L, 0L, "FAILED", "Recordings had empty transcripts.", null, null, 0L, null, "Recordings had empty transcripts.", 0L, 3023, null);
                    anonymousClass1.L$0 = SpillingKt.nullOutSpilledVariable(meetingId7);
                    anonymousClass1.L$1 = SpillingKt.nullOutSpilledVariable(meeting5);
                    anonymousClass1.L$2 = SpillingKt.nullOutSpilledVariable(settings2);
                    anonymousClass1.L$3 = SpillingKt.nullOutSpilledVariable(linked2);
                    anonymousClass1.L$4 = SpillingKt.nullOutSpilledVariable(recordings2);
                    anonymousClass1.L$5 = SpillingKt.nullOutSpilledVariable(combined);
                    anonymousClass1.L$6 = "Recordings had empty transcripts.";
                    anonymousClass1.label = 7;
                    if (meetingRepository3.updateMeeting(meetingEntityCopy$default3, anonymousClass1) == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    msg = "Recordings had empty transcripts.";
                    return new MeetingStageResult.Failed(msg, false);
                }
                list2 = recordings2;
                if (list2 instanceof Collection) {
                    it2 = list2.iterator();
                    while (true) {
                        if (it2.hasNext()) {
                            diarizedTranscript = ((SegmentEntity) it2.next()).getDiarizedTranscript();
                            if (diarizedTranscript != null) {
                                z2 = true;
                            } else {
                                z2 = true;
                            }
                            if (!z2) {
                                i3 = 1;
                            }
                        } else {
                            i3 = 0;
                        }
                    }
                } else {
                    it2 = list2.iterator();
                    while (true) {
                        if (it2.hasNext()) {
                            diarizedTranscript = ((SegmentEntity) it2.next()).getDiarizedTranscript();
                            if (diarizedTranscript != null) {
                                z2 = true;
                            } else {
                                z2 = true;
                            }
                            if (!z2) {
                                i3 = 1;
                            }
                        } else {
                            i3 = 0;
                        }
                    }
                }
                i4 = i3;
                cleanStarted = System.currentTimeMillis();
                meetingRepository2 = this.repository;
                meetingEntityCopy$default2 = MeetingEntity.copy$default(meeting5, null, null, 0L, 0L, "CLEANING", null, null, null, 0L, null, null, 0L, 3055, null);
                meeting6 = meeting5;
                anonymousClass1.L$0 = meetingId7;
                anonymousClass1.L$1 = meeting6;
                anonymousClass1.L$2 = settings2;
                meetingId2 = meetingId7;
                anonymousClass1.L$3 = SpillingKt.nullOutSpilledVariable(linked2);
                anonymousClass1.L$4 = SpillingKt.nullOutSpilledVariable(recordings2);
                anonymousClass1.L$5 = combined;
                anonymousClass1.I$0 = i4;
                anonymousClass1.J$0 = cleanStarted;
                anonymousClass1.label = 8;
                if (meetingRepository2.updateMeeting(meetingEntityCopy$default2, anonymousClass1) == coroutine_suspended) {
                    return coroutine_suspended;
                }
                i5 = i4;
                meetingId3 = meetingId2;
                ProviderRouter providerRouter2 = this.router;
                if (i5 != 0) {
                    z4 = true;
                } else {
                    z4 = false;
                }
                anonymousClass1.L$0 = meetingId3;
                anonymousClass1.L$1 = meeting6;
                anonymousClass1.L$2 = settings2;
                meetingId4 = meetingId3;
                anonymousClass1.L$3 = SpillingKt.nullOutSpilledVariable(linked2);
                anonymousClass1.L$4 = SpillingKt.nullOutSpilledVariable(recordings2);
                anonymousClass1.L$5 = SpillingKt.nullOutSpilledVariable(combined);
                anonymousClass1.I$0 = i5;
                anonymousClass1.J$0 = cleanStarted;
                anonymousClass1.label = 9;
                objClean = providerRouter2.clean(combined, z4, anonymousClass1);
                if (objClean == coroutine_suspended) {
                    return coroutine_suspended;
                }
                String str6 = combined;
                i8 = i5;
                combined3 = meetingId4;
                meeting8 = meeting6;
                combined4 = str6;
                settings4 = settings2;
                cleaned = (TextStageResult) objClean;
                if (!StringsKt.isBlank(cleaned.getText())) {
                    throw new IllegalStateException("cleanup returned empty text".toString());
                }
                sb2 = new StringBuilder();
                settings6 = settings4;
                recordings3 = recordings2;
                sb2.append("clean=" + cleaned.getProviderId());
                linked6 = linked2;
                combined6 = combined4;
                if (StringsKt.startsWith$default(cleaned.getProviderId(), "cloud", false, 2, (Object) null)) {
                    sb2.append("(" + settings6.getCloudCleanupModel() + ")");
                    break;
                }
                String providerLabel2 = sb2.toString();
                meetingRepository6 = this.repository;
                meetingEntityCopy$default6 = MeetingEntity.copy$default(meeting8, null, null, 0L, 0L, "PENDING_CLEANUP", null, null, providerLabel2, 0L, cleaned.getText(), null, 0L, 2415, null);
                meeting9 = meeting8;
                anonymousClass1.L$0 = combined3;
                anonymousClass1.L$1 = meeting9;
                anonymousClass1.L$2 = SpillingKt.nullOutSpilledVariable(settings6);
                anonymousClass1.L$3 = SpillingKt.nullOutSpilledVariable(linked6);
                anonymousClass1.L$4 = SpillingKt.nullOutSpilledVariable(recordings3);
                anonymousClass1.L$5 = SpillingKt.nullOutSpilledVariable(combined6);
                anonymousClass1.L$6 = cleaned;
                anonymousClass1.L$7 = SpillingKt.nullOutSpilledVariable(providerLabel2);
                anonymousClass1.I$0 = i8;
                anonymousClass1.J$0 = cleanStarted;
                anonymousClass1.label = 10;
                if (meetingRepository6.updateMeeting(meetingEntityCopy$default6, anonymousClass1) == coroutine_suspended) {
                    return coroutine_suspended;
                }
                meetingId5 = combined3;
                $result = combined6;
                settings7 = settings6;
                recordings4 = recordings3;
                cleanStarted3 = cleanStarted;
                String meetingId9 = meetingId5;
                Log.i(TAG, "Meeting " + meetingId5 + " cleanup ok chars=" + cleaned.getText().length() + " ms=" + (System.currentTimeMillis() - cleanStarted3));
                return MeetingStageResult.Success.INSTANCE;
                Log.e(TAG, "Meeting " + combined3 + " cleanup failed", t);
                meetingRepository7 = this.repository;
                message = t.getMessage();
                if (message == null) {
                    str2 = str;
                } else {
                    str2 = message;
                }
                meetingEntityCopy$default7 = MeetingEntity.copy$default(meetingEntity, null, null, 0L, 0L, "FAILED", null, null, null, 0L, null, str2, 0L, 3055, null);
                anonymousClass1.L$0 = SpillingKt.nullOutSpilledVariable(combined3);
                anonymousClass1.L$1 = SpillingKt.nullOutSpilledVariable(meetingEntity);
                anonymousClass1.L$2 = SpillingKt.nullOutSpilledVariable(settings2);
                anonymousClass1.L$3 = SpillingKt.nullOutSpilledVariable(linked4);
                anonymousClass1.L$4 = SpillingKt.nullOutSpilledVariable(recordings3);
                anonymousClass1.L$5 = SpillingKt.nullOutSpilledVariable(combined2);
                anonymousClass1.L$6 = t;
                anonymousClass1.L$7 = null;
                anonymousClass1.I$0 = i8;
                anonymousClass1.J$0 = cleanStarted;
                anonymousClass1.label = 11;
                if (meetingRepository7.updateMeeting(meetingEntityCopy$default7, anonymousClass1) == coroutine_suspended) {
                    return coroutine_suspended;
                }
                cleanStarted2 = cleanStarted;
                i9 = i8;
                combined7 = combined2;
                recordings5 = recordings3;
                t = t;
                meetingId6 = combined3;
                message2 = t.getMessage();
                if (message2 == null) {
                    str3 = str;
                } else {
                    str3 = message2;
                }
                return new MeetingStageResult.Failed(str3, true);
            case 2:
                settings = (PipelineSettings) anonymousClass1.L$2;
                MeetingEntity meeting10 = (MeetingEntity) anonymousClass1.L$1;
                meetingId7 = (String) anonymousClass1.L$0;
                ResultKt.throwOnFailure($result2);
                recordings = $result2;
                meeting3 = meeting10;
                linked = (List) recordings;
                if (linked.isEmpty()) {
                    meetingRepository = this.repository;
                    meetingEntityCopy$default = MeetingEntity.copy$default(meeting3, null, null, 0L, 0L, "FAILED", "No recordings in this time range.", null, null, 0L, null, "No recordings in this time range.", 0L, 3023, null);
                    anonymousClass1.L$0 = SpillingKt.nullOutSpilledVariable(meetingId7);
                    anonymousClass1.L$1 = SpillingKt.nullOutSpilledVariable(meeting3);
                    anonymousClass1.L$2 = SpillingKt.nullOutSpilledVariable(settings);
                    anonymousClass1.L$3 = SpillingKt.nullOutSpilledVariable(linked);
                    anonymousClass1.label = 3;
                    if (meetingRepository.updateMeeting(meetingEntityCopy$default, anonymousClass1) == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    return new MeetingStageResult.Failed("No recordings", false);
                }
                meeting4 = meeting3;
                MeetingRepository meetingRepository13 = this.repository;
                anonymousClass1.L$0 = meetingId7;
                anonymousClass1.L$1 = meeting4;
                anonymousClass1.L$2 = settings;
                anonymousClass1.L$3 = linked;
                anonymousClass1.label = 4;
                objAllRecordingsAsrSettled = meetingRepository13.allRecordingsAsrSettled(meetingId7, anonymousClass1);
                if (objAllRecordingsAsrSettled == coroutine_suspended) {
                    return coroutine_suspended;
                }
                settings2 = settings;
                linked2 = linked;
                meeting5 = meeting4;
                if (!((Boolean) objAllRecordingsAsrSettled).booleanValue()) {
                    list3 = linked2;
                    if (list3 instanceof Collection) {
                        i6 = 0;
                        while (r8.hasNext()) {
                            list3 = list3;
                            $result2 = $result2;
                            if (Intrinsics.areEqual(segmentEntity2.getTranscriptStatus(), "PENDING")) {
                                z3 = true;
                            } else {
                                z3 = true;
                            }
                            if (z3) {
                                i6++;
                                if (i6 < 0) {
                                    CollectionsKt.throwCountOverflow();
                                }
                            }
                        }
                        i7 = i6;
                    } else {
                        i6 = 0;
                        while (r8.hasNext()) {
                            list3 = list3;
                            $result2 = $result2;
                            if (Intrinsics.areEqual(segmentEntity2.getTranscriptStatus(), "PENDING")) {
                                z3 = true;
                            } else {
                                z3 = true;
                            }
                            if (z3) {
                                i6++;
                                if (i6 < 0) {
                                    CollectionsKt.throwCountOverflow();
                                }
                            }
                        }
                        i7 = i6;
                    }
                    pendingAsr = i7;
                    Log.i(TAG, "Meeting " + meetingId7 + " waiting on ASR (" + pendingAsr + " still in progress)");
                    if (!Intrinsics.areEqual(meeting5.getStatus(), "PENDING_CLEANUP")) {
                        meetingRepository5 = this.repository;
                        meetingEntityCopy$default5 = MeetingEntity.copy$default(meeting5, null, null, 0L, 0L, "PENDING_CLEANUP", null, null, null, 0L, null, null, 0L, 4079, null);
                        anonymousClass1.L$0 = SpillingKt.nullOutSpilledVariable(meetingId7);
                        anonymousClass1.L$1 = SpillingKt.nullOutSpilledVariable(meeting5);
                        anonymousClass1.L$2 = SpillingKt.nullOutSpilledVariable(settings2);
                        anonymousClass1.L$3 = SpillingKt.nullOutSpilledVariable(linked2);
                        anonymousClass1.I$0 = pendingAsr;
                        anonymousClass1.label = 5;
                        if (meetingRepository5.updateMeeting(meetingEntityCopy$default5, anonymousClass1) == coroutine_suspended) {
                            return coroutine_suspended;
                        }
                        linked3 = linked2;
                        settings3 = settings2;
                        meeting7 = meeting5;
                    }
                    return MeetingStageResult.WaitingAsr.INSTANCE;
                }
                list = linked2;
                i = 0;
                arrayList = new ArrayList();
                while (r15.hasNext()) {
                    Iterable iterable3 = list;
                    int i12 = i;
                    if (Intrinsics.areEqual(((SegmentEntity) obj).getTranscriptStatus(), "READY")) {
                        arrayList.add(obj);
                    }
                    list = iterable3;
                    i = i12;
                }
                recordings2 = (List) arrayList;
                if (recordings2.isEmpty()) {
                    meetingRepository4 = this.repository;
                    meetingEntityCopy$default4 = MeetingEntity.copy$default(meeting5, null, null, 0L, 0L, "FAILED", "No usable transcripts in this time range (ASR skipped or failed).", null, null, 0L, null, "No usable transcripts in this time range (ASR skipped or failed).", 0L, 3023, null);
                    anonymousClass1.L$0 = SpillingKt.nullOutSpilledVariable(meetingId7);
                    anonymousClass1.L$1 = SpillingKt.nullOutSpilledVariable(meeting5);
                    anonymousClass1.L$2 = SpillingKt.nullOutSpilledVariable(settings2);
                    anonymousClass1.L$3 = SpillingKt.nullOutSpilledVariable(linked2);
                    anonymousClass1.L$4 = SpillingKt.nullOutSpilledVariable(recordings2);
                    anonymousClass1.L$5 = "No usable transcripts in this time range (ASR skipped or failed).";
                    anonymousClass1.label = 6;
                    if (meetingRepository4.updateMeeting(meetingEntityCopy$default4, anonymousClass1) == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    msg2 = "No usable transcripts in this time range (ASR skipped or failed).";
                    return new MeetingStageResult.Failed(msg2, false);
                }
                sb = new StringBuilder();
                i2 = 0;
                it = recordings2.iterator();
                while (it.hasNext()) {
                    segmentEntity = (SegmentEntity) it.next();
                    diarizedTranscript2 = segmentEntity.getDiarizedTranscript();
                    if (diarizedTranscript2 == null) {
                        diarizedTranscript2 = segmentEntity.getTranscript();
                        if (diarizedTranscript2 != null) {
                            diarizedTranscript2 = null;
                        } else {
                            diarizedTranscript2 = null;
                        }
                        if (diarizedTranscript2 == null) {
                            sb.append("--- ");
                            sb.append(this.timeFmt.format(new Date(segmentEntity.getStartedAtMs())));
                            sb.append(" ---\n");
                            sb.append(StringsKt.trim((CharSequence) diarizedTranscript2).toString());
                            sb.append("\n\n");
                            sb = sb;
                            i2 = i2;
                            it = it;
                            str4 = str4;
                        }
                    } else {
                        if (StringsKt.isBlank(diarizedTranscript2)) {
                            diarizedTranscript2 = null;
                        }
                        if (diarizedTranscript2 == null) {
                            diarizedTranscript2 = segmentEntity.getTranscript();
                            if (diarizedTranscript2 != null) {
                                diarizedTranscript2 = null;
                            } else {
                                diarizedTranscript2 = null;
                            }
                            if (diarizedTranscript2 == null) {
                            }
                        }
                        sb.append("--- ");
                        sb.append(this.timeFmt.format(new Date(segmentEntity.getStartedAtMs())));
                        sb.append(" ---\n");
                        sb.append(StringsKt.trim((CharSequence) diarizedTranscript2).toString());
                        sb.append("\n\n");
                        sb = sb;
                        i2 = i2;
                        it = it;
                        str4 = str4;
                    }
                }
                str = str4;
                combined = StringsKt.trim((CharSequence) sb.toString()).toString();
                if (StringsKt.isBlank(combined)) {
                    meetingRepository3 = this.repository;
                    meetingEntityCopy$default3 = MeetingEntity.copy$default(meeting5, null, null, 0L, 0L, "FAILED", "Recordings had empty transcripts.", null, null, 0L, null, "Recordings had empty transcripts.", 0L, 3023, null);
                    anonymousClass1.L$0 = SpillingKt.nullOutSpilledVariable(meetingId7);
                    anonymousClass1.L$1 = SpillingKt.nullOutSpilledVariable(meeting5);
                    anonymousClass1.L$2 = SpillingKt.nullOutSpilledVariable(settings2);
                    anonymousClass1.L$3 = SpillingKt.nullOutSpilledVariable(linked2);
                    anonymousClass1.L$4 = SpillingKt.nullOutSpilledVariable(recordings2);
                    anonymousClass1.L$5 = SpillingKt.nullOutSpilledVariable(combined);
                    anonymousClass1.L$6 = "Recordings had empty transcripts.";
                    anonymousClass1.label = 7;
                    if (meetingRepository3.updateMeeting(meetingEntityCopy$default3, anonymousClass1) == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    msg = "Recordings had empty transcripts.";
                    return new MeetingStageResult.Failed(msg, false);
                }
                list2 = recordings2;
                if (list2 instanceof Collection) {
                    it2 = list2.iterator();
                    while (true) {
                        if (it2.hasNext()) {
                            diarizedTranscript = ((SegmentEntity) it2.next()).getDiarizedTranscript();
                            if (diarizedTranscript != null) {
                                z2 = true;
                            } else {
                                z2 = true;
                            }
                            if (!z2) {
                                i3 = 1;
                            }
                        } else {
                            i3 = 0;
                        }
                    }
                } else {
                    it2 = list2.iterator();
                    while (true) {
                        if (it2.hasNext()) {
                            diarizedTranscript = ((SegmentEntity) it2.next()).getDiarizedTranscript();
                            if (diarizedTranscript != null) {
                                z2 = true;
                            } else {
                                z2 = true;
                            }
                            if (!z2) {
                                i3 = 1;
                            }
                        } else {
                            i3 = 0;
                        }
                    }
                }
                i4 = i3;
                cleanStarted = System.currentTimeMillis();
                meetingRepository2 = this.repository;
                meetingEntityCopy$default2 = MeetingEntity.copy$default(meeting5, null, null, 0L, 0L, "CLEANING", null, null, null, 0L, null, null, 0L, 3055, null);
                meeting6 = meeting5;
                anonymousClass1.L$0 = meetingId7;
                anonymousClass1.L$1 = meeting6;
                anonymousClass1.L$2 = settings2;
                meetingId2 = meetingId7;
                anonymousClass1.L$3 = SpillingKt.nullOutSpilledVariable(linked2);
                anonymousClass1.L$4 = SpillingKt.nullOutSpilledVariable(recordings2);
                anonymousClass1.L$5 = combined;
                anonymousClass1.I$0 = i4;
                anonymousClass1.J$0 = cleanStarted;
                anonymousClass1.label = 8;
                if (meetingRepository2.updateMeeting(meetingEntityCopy$default2, anonymousClass1) == coroutine_suspended) {
                    return coroutine_suspended;
                }
                i5 = i4;
                meetingId3 = meetingId2;
                ProviderRouter providerRouter3 = this.router;
                if (i5 != 0) {
                    z4 = true;
                } else {
                    z4 = false;
                }
                anonymousClass1.L$0 = meetingId3;
                anonymousClass1.L$1 = meeting6;
                anonymousClass1.L$2 = settings2;
                meetingId4 = meetingId3;
                anonymousClass1.L$3 = SpillingKt.nullOutSpilledVariable(linked2);
                anonymousClass1.L$4 = SpillingKt.nullOutSpilledVariable(recordings2);
                anonymousClass1.L$5 = SpillingKt.nullOutSpilledVariable(combined);
                anonymousClass1.I$0 = i5;
                anonymousClass1.J$0 = cleanStarted;
                anonymousClass1.label = 9;
                objClean = providerRouter3.clean(combined, z4, anonymousClass1);
                if (objClean == coroutine_suspended) {
                    return coroutine_suspended;
                }
                String str7 = combined;
                i8 = i5;
                combined3 = meetingId4;
                meeting8 = meeting6;
                combined4 = str7;
                settings4 = settings2;
                cleaned = (TextStageResult) objClean;
                if (!StringsKt.isBlank(cleaned.getText())) {
                    throw new IllegalStateException("cleanup returned empty text".toString());
                }
                sb2 = new StringBuilder();
                settings6 = settings4;
                recordings3 = recordings2;
                sb2.append("clean=" + cleaned.getProviderId());
                linked6 = linked2;
                combined6 = combined4;
                if (StringsKt.startsWith$default(cleaned.getProviderId(), "cloud", false, 2, (Object) null)) {
                    sb2.append("(" + settings6.getCloudCleanupModel() + ")");
                    break;
                }
                String providerLabel3 = sb2.toString();
                meetingRepository6 = this.repository;
                meetingEntityCopy$default6 = MeetingEntity.copy$default(meeting8, null, null, 0L, 0L, "PENDING_CLEANUP", null, null, providerLabel3, 0L, cleaned.getText(), null, 0L, 2415, null);
                meeting9 = meeting8;
                anonymousClass1.L$0 = combined3;
                anonymousClass1.L$1 = meeting9;
                anonymousClass1.L$2 = SpillingKt.nullOutSpilledVariable(settings6);
                anonymousClass1.L$3 = SpillingKt.nullOutSpilledVariable(linked6);
                anonymousClass1.L$4 = SpillingKt.nullOutSpilledVariable(recordings3);
                anonymousClass1.L$5 = SpillingKt.nullOutSpilledVariable(combined6);
                anonymousClass1.L$6 = cleaned;
                anonymousClass1.L$7 = SpillingKt.nullOutSpilledVariable(providerLabel3);
                anonymousClass1.I$0 = i8;
                anonymousClass1.J$0 = cleanStarted;
                anonymousClass1.label = 10;
                if (meetingRepository6.updateMeeting(meetingEntityCopy$default6, anonymousClass1) == coroutine_suspended) {
                    return coroutine_suspended;
                }
                meetingId5 = combined3;
                $result = combined6;
                settings7 = settings6;
                recordings4 = recordings3;
                cleanStarted3 = cleanStarted;
                String meetingId10 = meetingId5;
                Log.i(TAG, "Meeting " + meetingId5 + " cleanup ok chars=" + cleaned.getText().length() + " ms=" + (System.currentTimeMillis() - cleanStarted3));
                return MeetingStageResult.Success.INSTANCE;
                Log.e(TAG, "Meeting " + combined3 + " cleanup failed", t);
                meetingRepository7 = this.repository;
                message = t.getMessage();
                if (message == null) {
                    str2 = str;
                } else {
                    str2 = message;
                }
                meetingEntityCopy$default7 = MeetingEntity.copy$default(meetingEntity, null, null, 0L, 0L, "FAILED", null, null, null, 0L, null, str2, 0L, 3055, null);
                anonymousClass1.L$0 = SpillingKt.nullOutSpilledVariable(combined3);
                anonymousClass1.L$1 = SpillingKt.nullOutSpilledVariable(meetingEntity);
                anonymousClass1.L$2 = SpillingKt.nullOutSpilledVariable(settings2);
                anonymousClass1.L$3 = SpillingKt.nullOutSpilledVariable(linked4);
                anonymousClass1.L$4 = SpillingKt.nullOutSpilledVariable(recordings3);
                anonymousClass1.L$5 = SpillingKt.nullOutSpilledVariable(combined2);
                anonymousClass1.L$6 = t;
                anonymousClass1.L$7 = null;
                anonymousClass1.I$0 = i8;
                anonymousClass1.J$0 = cleanStarted;
                anonymousClass1.label = 11;
                if (meetingRepository7.updateMeeting(meetingEntityCopy$default7, anonymousClass1) == coroutine_suspended) {
                    return coroutine_suspended;
                }
                cleanStarted2 = cleanStarted;
                i9 = i8;
                combined7 = combined2;
                recordings5 = recordings3;
                t = t;
                meetingId6 = combined3;
                message2 = t.getMessage();
                if (message2 == null) {
                    str3 = str;
                } else {
                    str3 = message2;
                }
                return new MeetingStageResult.Failed(str3, true);
            case 3:
                ResultKt.throwOnFailure($result2);
                return new MeetingStageResult.Failed("No recordings", false);
            case 4:
                List linked7 = (List) anonymousClass1.L$3;
                PipelineSettings settings8 = (PipelineSettings) anonymousClass1.L$2;
                MeetingEntity meeting11 = (MeetingEntity) anonymousClass1.L$1;
                meetingId7 = (String) anonymousClass1.L$0;
                ResultKt.throwOnFailure($result2);
                linked2 = linked7;
                settings2 = settings8;
                meeting5 = meeting11;
                objAllRecordingsAsrSettled = $result2;
                if (!((Boolean) objAllRecordingsAsrSettled).booleanValue()) {
                    list3 = linked2;
                    if (list3 instanceof Collection) {
                        i6 = 0;
                        while (r8.hasNext()) {
                            list3 = list3;
                            $result2 = $result2;
                            if (Intrinsics.areEqual(segmentEntity2.getTranscriptStatus(), "PENDING")) {
                                z3 = true;
                            } else {
                                z3 = true;
                            }
                            if (z3) {
                                i6++;
                                if (i6 < 0) {
                                    CollectionsKt.throwCountOverflow();
                                }
                            }
                        }
                        i7 = i6;
                    } else {
                        i6 = 0;
                        while (r8.hasNext()) {
                            list3 = list3;
                            $result2 = $result2;
                            if (Intrinsics.areEqual(segmentEntity2.getTranscriptStatus(), "PENDING")) {
                                z3 = true;
                            } else {
                                z3 = true;
                            }
                            if (z3) {
                                i6++;
                                if (i6 < 0) {
                                    CollectionsKt.throwCountOverflow();
                                }
                            }
                        }
                        i7 = i6;
                    }
                    pendingAsr = i7;
                    Log.i(TAG, "Meeting " + meetingId7 + " waiting on ASR (" + pendingAsr + " still in progress)");
                    if (!Intrinsics.areEqual(meeting5.getStatus(), "PENDING_CLEANUP")) {
                        meetingRepository5 = this.repository;
                        meetingEntityCopy$default5 = MeetingEntity.copy$default(meeting5, null, null, 0L, 0L, "PENDING_CLEANUP", null, null, null, 0L, null, null, 0L, 4079, null);
                        anonymousClass1.L$0 = SpillingKt.nullOutSpilledVariable(meetingId7);
                        anonymousClass1.L$1 = SpillingKt.nullOutSpilledVariable(meeting5);
                        anonymousClass1.L$2 = SpillingKt.nullOutSpilledVariable(settings2);
                        anonymousClass1.L$3 = SpillingKt.nullOutSpilledVariable(linked2);
                        anonymousClass1.I$0 = pendingAsr;
                        anonymousClass1.label = 5;
                        if (meetingRepository5.updateMeeting(meetingEntityCopy$default5, anonymousClass1) == coroutine_suspended) {
                            return coroutine_suspended;
                        }
                        linked3 = linked2;
                        settings3 = settings2;
                        meeting7 = meeting5;
                    }
                    return MeetingStageResult.WaitingAsr.INSTANCE;
                }
                list = linked2;
                i = 0;
                arrayList = new ArrayList();
                while (r15.hasNext()) {
                    Iterable iterable4 = list;
                    int i13 = i;
                    if (Intrinsics.areEqual(((SegmentEntity) obj).getTranscriptStatus(), "READY")) {
                        arrayList.add(obj);
                    }
                    list = iterable4;
                    i = i13;
                }
                recordings2 = (List) arrayList;
                if (recordings2.isEmpty()) {
                    meetingRepository4 = this.repository;
                    meetingEntityCopy$default4 = MeetingEntity.copy$default(meeting5, null, null, 0L, 0L, "FAILED", "No usable transcripts in this time range (ASR skipped or failed).", null, null, 0L, null, "No usable transcripts in this time range (ASR skipped or failed).", 0L, 3023, null);
                    anonymousClass1.L$0 = SpillingKt.nullOutSpilledVariable(meetingId7);
                    anonymousClass1.L$1 = SpillingKt.nullOutSpilledVariable(meeting5);
                    anonymousClass1.L$2 = SpillingKt.nullOutSpilledVariable(settings2);
                    anonymousClass1.L$3 = SpillingKt.nullOutSpilledVariable(linked2);
                    anonymousClass1.L$4 = SpillingKt.nullOutSpilledVariable(recordings2);
                    anonymousClass1.L$5 = "No usable transcripts in this time range (ASR skipped or failed).";
                    anonymousClass1.label = 6;
                    if (meetingRepository4.updateMeeting(meetingEntityCopy$default4, anonymousClass1) == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    msg2 = "No usable transcripts in this time range (ASR skipped or failed).";
                    return new MeetingStageResult.Failed(msg2, false);
                }
                sb = new StringBuilder();
                i2 = 0;
                it = recordings2.iterator();
                while (it.hasNext()) {
                    segmentEntity = (SegmentEntity) it.next();
                    diarizedTranscript2 = segmentEntity.getDiarizedTranscript();
                    if (diarizedTranscript2 == null) {
                        diarizedTranscript2 = segmentEntity.getTranscript();
                        if (diarizedTranscript2 != null) {
                            diarizedTranscript2 = null;
                        } else {
                            diarizedTranscript2 = null;
                        }
                        if (diarizedTranscript2 == null) {
                            sb.append("--- ");
                            sb.append(this.timeFmt.format(new Date(segmentEntity.getStartedAtMs())));
                            sb.append(" ---\n");
                            sb.append(StringsKt.trim((CharSequence) diarizedTranscript2).toString());
                            sb.append("\n\n");
                            sb = sb;
                            i2 = i2;
                            it = it;
                            str4 = str4;
                        }
                    } else {
                        if (StringsKt.isBlank(diarizedTranscript2)) {
                            diarizedTranscript2 = null;
                        }
                        if (diarizedTranscript2 == null) {
                            diarizedTranscript2 = segmentEntity.getTranscript();
                            if (diarizedTranscript2 != null) {
                                diarizedTranscript2 = null;
                            } else {
                                diarizedTranscript2 = null;
                            }
                            if (diarizedTranscript2 == null) {
                            }
                        }
                        sb.append("--- ");
                        sb.append(this.timeFmt.format(new Date(segmentEntity.getStartedAtMs())));
                        sb.append(" ---\n");
                        sb.append(StringsKt.trim((CharSequence) diarizedTranscript2).toString());
                        sb.append("\n\n");
                        sb = sb;
                        i2 = i2;
                        it = it;
                        str4 = str4;
                    }
                }
                str = str4;
                combined = StringsKt.trim((CharSequence) sb.toString()).toString();
                if (StringsKt.isBlank(combined)) {
                    meetingRepository3 = this.repository;
                    meetingEntityCopy$default3 = MeetingEntity.copy$default(meeting5, null, null, 0L, 0L, "FAILED", "Recordings had empty transcripts.", null, null, 0L, null, "Recordings had empty transcripts.", 0L, 3023, null);
                    anonymousClass1.L$0 = SpillingKt.nullOutSpilledVariable(meetingId7);
                    anonymousClass1.L$1 = SpillingKt.nullOutSpilledVariable(meeting5);
                    anonymousClass1.L$2 = SpillingKt.nullOutSpilledVariable(settings2);
                    anonymousClass1.L$3 = SpillingKt.nullOutSpilledVariable(linked2);
                    anonymousClass1.L$4 = SpillingKt.nullOutSpilledVariable(recordings2);
                    anonymousClass1.L$5 = SpillingKt.nullOutSpilledVariable(combined);
                    anonymousClass1.L$6 = "Recordings had empty transcripts.";
                    anonymousClass1.label = 7;
                    if (meetingRepository3.updateMeeting(meetingEntityCopy$default3, anonymousClass1) == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    msg = "Recordings had empty transcripts.";
                    return new MeetingStageResult.Failed(msg, false);
                }
                list2 = recordings2;
                if (list2 instanceof Collection) {
                    it2 = list2.iterator();
                    while (true) {
                        if (it2.hasNext()) {
                            diarizedTranscript = ((SegmentEntity) it2.next()).getDiarizedTranscript();
                            if (diarizedTranscript != null) {
                                z2 = true;
                            } else {
                                z2 = true;
                            }
                            if (!z2) {
                                i3 = 1;
                            }
                        } else {
                            i3 = 0;
                        }
                    }
                } else {
                    it2 = list2.iterator();
                    while (true) {
                        if (it2.hasNext()) {
                            diarizedTranscript = ((SegmentEntity) it2.next()).getDiarizedTranscript();
                            if (diarizedTranscript != null) {
                                z2 = true;
                            } else {
                                z2 = true;
                            }
                            if (!z2) {
                                i3 = 1;
                            }
                        } else {
                            i3 = 0;
                        }
                    }
                }
                i4 = i3;
                cleanStarted = System.currentTimeMillis();
                meetingRepository2 = this.repository;
                meetingEntityCopy$default2 = MeetingEntity.copy$default(meeting5, null, null, 0L, 0L, "CLEANING", null, null, null, 0L, null, null, 0L, 3055, null);
                meeting6 = meeting5;
                anonymousClass1.L$0 = meetingId7;
                anonymousClass1.L$1 = meeting6;
                anonymousClass1.L$2 = settings2;
                meetingId2 = meetingId7;
                anonymousClass1.L$3 = SpillingKt.nullOutSpilledVariable(linked2);
                anonymousClass1.L$4 = SpillingKt.nullOutSpilledVariable(recordings2);
                anonymousClass1.L$5 = combined;
                anonymousClass1.I$0 = i4;
                anonymousClass1.J$0 = cleanStarted;
                anonymousClass1.label = 8;
                if (meetingRepository2.updateMeeting(meetingEntityCopy$default2, anonymousClass1) == coroutine_suspended) {
                    return coroutine_suspended;
                }
                i5 = i4;
                meetingId3 = meetingId2;
                ProviderRouter providerRouter4 = this.router;
                if (i5 != 0) {
                    z4 = true;
                } else {
                    z4 = false;
                }
                anonymousClass1.L$0 = meetingId3;
                anonymousClass1.L$1 = meeting6;
                anonymousClass1.L$2 = settings2;
                meetingId4 = meetingId3;
                anonymousClass1.L$3 = SpillingKt.nullOutSpilledVariable(linked2);
                anonymousClass1.L$4 = SpillingKt.nullOutSpilledVariable(recordings2);
                anonymousClass1.L$5 = SpillingKt.nullOutSpilledVariable(combined);
                anonymousClass1.I$0 = i5;
                anonymousClass1.J$0 = cleanStarted;
                anonymousClass1.label = 9;
                objClean = providerRouter4.clean(combined, z4, anonymousClass1);
                if (objClean == coroutine_suspended) {
                    return coroutine_suspended;
                }
                String str8 = combined;
                i8 = i5;
                combined3 = meetingId4;
                meeting8 = meeting6;
                combined4 = str8;
                settings4 = settings2;
                cleaned = (TextStageResult) objClean;
                if (!StringsKt.isBlank(cleaned.getText())) {
                    throw new IllegalStateException("cleanup returned empty text".toString());
                }
                sb2 = new StringBuilder();
                settings6 = settings4;
                recordings3 = recordings2;
                sb2.append("clean=" + cleaned.getProviderId());
                linked6 = linked2;
                combined6 = combined4;
                if (StringsKt.startsWith$default(cleaned.getProviderId(), "cloud", false, 2, (Object) null)) {
                    sb2.append("(" + settings6.getCloudCleanupModel() + ")");
                    break;
                }
                String providerLabel4 = sb2.toString();
                meetingRepository6 = this.repository;
                meetingEntityCopy$default6 = MeetingEntity.copy$default(meeting8, null, null, 0L, 0L, "PENDING_CLEANUP", null, null, providerLabel4, 0L, cleaned.getText(), null, 0L, 2415, null);
                meeting9 = meeting8;
                anonymousClass1.L$0 = combined3;
                anonymousClass1.L$1 = meeting9;
                anonymousClass1.L$2 = SpillingKt.nullOutSpilledVariable(settings6);
                anonymousClass1.L$3 = SpillingKt.nullOutSpilledVariable(linked6);
                anonymousClass1.L$4 = SpillingKt.nullOutSpilledVariable(recordings3);
                anonymousClass1.L$5 = SpillingKt.nullOutSpilledVariable(combined6);
                anonymousClass1.L$6 = cleaned;
                anonymousClass1.L$7 = SpillingKt.nullOutSpilledVariable(providerLabel4);
                anonymousClass1.I$0 = i8;
                anonymousClass1.J$0 = cleanStarted;
                anonymousClass1.label = 10;
                if (meetingRepository6.updateMeeting(meetingEntityCopy$default6, anonymousClass1) == coroutine_suspended) {
                    return coroutine_suspended;
                }
                meetingId5 = combined3;
                $result = combined6;
                settings7 = settings6;
                recordings4 = recordings3;
                cleanStarted3 = cleanStarted;
                String meetingId11 = meetingId5;
                Log.i(TAG, "Meeting " + meetingId5 + " cleanup ok chars=" + cleaned.getText().length() + " ms=" + (System.currentTimeMillis() - cleanStarted3));
                return MeetingStageResult.Success.INSTANCE;
                Log.e(TAG, "Meeting " + combined3 + " cleanup failed", t);
                meetingRepository7 = this.repository;
                message = t.getMessage();
                if (message == null) {
                    str2 = str;
                } else {
                    str2 = message;
                }
                meetingEntityCopy$default7 = MeetingEntity.copy$default(meetingEntity, null, null, 0L, 0L, "FAILED", null, null, null, 0L, null, str2, 0L, 3055, null);
                anonymousClass1.L$0 = SpillingKt.nullOutSpilledVariable(combined3);
                anonymousClass1.L$1 = SpillingKt.nullOutSpilledVariable(meetingEntity);
                anonymousClass1.L$2 = SpillingKt.nullOutSpilledVariable(settings2);
                anonymousClass1.L$3 = SpillingKt.nullOutSpilledVariable(linked4);
                anonymousClass1.L$4 = SpillingKt.nullOutSpilledVariable(recordings3);
                anonymousClass1.L$5 = SpillingKt.nullOutSpilledVariable(combined2);
                anonymousClass1.L$6 = t;
                anonymousClass1.L$7 = null;
                anonymousClass1.I$0 = i8;
                anonymousClass1.J$0 = cleanStarted;
                anonymousClass1.label = 11;
                if (meetingRepository7.updateMeeting(meetingEntityCopy$default7, anonymousClass1) == coroutine_suspended) {
                    return coroutine_suspended;
                }
                cleanStarted2 = cleanStarted;
                i9 = i8;
                combined7 = combined2;
                recordings5 = recordings3;
                t = t;
                meetingId6 = combined3;
                message2 = t.getMessage();
                if (message2 == null) {
                    str3 = str;
                } else {
                    str3 = message2;
                }
                return new MeetingStageResult.Failed(str3, true);
            case 5:
                int i14 = anonymousClass1.I$0;
                linked3 = (List) anonymousClass1.L$3;
                settings3 = (PipelineSettings) anonymousClass1.L$2;
                meeting7 = (MeetingEntity) anonymousClass1.L$1;
                ResultKt.throwOnFailure($result2);
                return MeetingStageResult.WaitingAsr.INSTANCE;
            case 6:
                msg2 = (String) anonymousClass1.L$5;
                ResultKt.throwOnFailure($result2);
                return new MeetingStageResult.Failed(msg2, false);
            case 7:
                msg = (String) anonymousClass1.L$6;
                ResultKt.throwOnFailure($result2);
                return new MeetingStageResult.Failed(msg, false);
            case 8:
                cleanStarted = anonymousClass1.J$0;
                int i15 = anonymousClass1.I$0;
                combined = (String) anonymousClass1.L$5;
                recordings2 = (List) anonymousClass1.L$4;
                linked2 = (List) anonymousClass1.L$3;
                settings2 = (PipelineSettings) anonymousClass1.L$2;
                meeting6 = (MeetingEntity) anonymousClass1.L$1;
                String meetingId12 = (String) anonymousClass1.L$0;
                ResultKt.throwOnFailure($result2);
                meetingId3 = meetingId12;
                str = "Cleanup failed";
                i5 = i15;
                ProviderRouter providerRouter5 = this.router;
                if (i5 != 0) {
                    z4 = true;
                } else {
                    z4 = false;
                }
                anonymousClass1.L$0 = meetingId3;
                anonymousClass1.L$1 = meeting6;
                anonymousClass1.L$2 = settings2;
                meetingId4 = meetingId3;
                anonymousClass1.L$3 = SpillingKt.nullOutSpilledVariable(linked2);
                anonymousClass1.L$4 = SpillingKt.nullOutSpilledVariable(recordings2);
                anonymousClass1.L$5 = SpillingKt.nullOutSpilledVariable(combined);
                anonymousClass1.I$0 = i5;
                anonymousClass1.J$0 = cleanStarted;
                anonymousClass1.label = 9;
                objClean = providerRouter5.clean(combined, z4, anonymousClass1);
                if (objClean == coroutine_suspended) {
                    return coroutine_suspended;
                }
                String str9 = combined;
                i8 = i5;
                combined3 = meetingId4;
                meeting8 = meeting6;
                combined4 = str9;
                settings4 = settings2;
                cleaned = (TextStageResult) objClean;
                if (!StringsKt.isBlank(cleaned.getText())) {
                    throw new IllegalStateException("cleanup returned empty text".toString());
                }
                sb2 = new StringBuilder();
                settings6 = settings4;
                recordings3 = recordings2;
                sb2.append("clean=" + cleaned.getProviderId());
                linked6 = linked2;
                combined6 = combined4;
                if (StringsKt.startsWith$default(cleaned.getProviderId(), "cloud", false, 2, (Object) null)) {
                    sb2.append("(" + settings6.getCloudCleanupModel() + ")");
                    break;
                }
                String providerLabel5 = sb2.toString();
                meetingRepository6 = this.repository;
                meetingEntityCopy$default6 = MeetingEntity.copy$default(meeting8, null, null, 0L, 0L, "PENDING_CLEANUP", null, null, providerLabel5, 0L, cleaned.getText(), null, 0L, 2415, null);
                meeting9 = meeting8;
                anonymousClass1.L$0 = combined3;
                anonymousClass1.L$1 = meeting9;
                anonymousClass1.L$2 = SpillingKt.nullOutSpilledVariable(settings6);
                anonymousClass1.L$3 = SpillingKt.nullOutSpilledVariable(linked6);
                anonymousClass1.L$4 = SpillingKt.nullOutSpilledVariable(recordings3);
                anonymousClass1.L$5 = SpillingKt.nullOutSpilledVariable(combined6);
                anonymousClass1.L$6 = cleaned;
                anonymousClass1.L$7 = SpillingKt.nullOutSpilledVariable(providerLabel5);
                anonymousClass1.I$0 = i8;
                anonymousClass1.J$0 = cleanStarted;
                anonymousClass1.label = 10;
                if (meetingRepository6.updateMeeting(meetingEntityCopy$default6, anonymousClass1) == coroutine_suspended) {
                    return coroutine_suspended;
                }
                meetingId5 = combined3;
                $result = combined6;
                settings7 = settings6;
                recordings4 = recordings3;
                cleanStarted3 = cleanStarted;
                String meetingId13 = meetingId5;
                Log.i(TAG, "Meeting " + meetingId5 + " cleanup ok chars=" + cleaned.getText().length() + " ms=" + (System.currentTimeMillis() - cleanStarted3));
                return MeetingStageResult.Success.INSTANCE;
                Log.e(TAG, "Meeting " + combined3 + " cleanup failed", t);
                meetingRepository7 = this.repository;
                message = t.getMessage();
                if (message == null) {
                    str2 = str;
                } else {
                    str2 = message;
                }
                meetingEntityCopy$default7 = MeetingEntity.copy$default(meetingEntity, null, null, 0L, 0L, "FAILED", null, null, null, 0L, null, str2, 0L, 3055, null);
                anonymousClass1.L$0 = SpillingKt.nullOutSpilledVariable(combined3);
                anonymousClass1.L$1 = SpillingKt.nullOutSpilledVariable(meetingEntity);
                anonymousClass1.L$2 = SpillingKt.nullOutSpilledVariable(settings2);
                anonymousClass1.L$3 = SpillingKt.nullOutSpilledVariable(linked4);
                anonymousClass1.L$4 = SpillingKt.nullOutSpilledVariable(recordings3);
                anonymousClass1.L$5 = SpillingKt.nullOutSpilledVariable(combined2);
                anonymousClass1.L$6 = t;
                anonymousClass1.L$7 = null;
                anonymousClass1.I$0 = i8;
                anonymousClass1.J$0 = cleanStarted;
                anonymousClass1.label = 11;
                if (meetingRepository7.updateMeeting(meetingEntityCopy$default7, anonymousClass1) == coroutine_suspended) {
                    return coroutine_suspended;
                }
                cleanStarted2 = cleanStarted;
                i9 = i8;
                combined7 = combined2;
                recordings5 = recordings3;
                t = t;
                meetingId6 = combined3;
                message2 = t.getMessage();
                if (message2 == null) {
                    str3 = str;
                } else {
                    str3 = message2;
                }
                return new MeetingStageResult.Failed(str3, true);
            case 9:
                long cleanStarted4 = anonymousClass1.J$0;
                int i16 = anonymousClass1.I$0;
                String combined8 = (String) anonymousClass1.L$5;
                List recordings6 = (List) anonymousClass1.L$4;
                List linked8 = (List) anonymousClass1.L$3;
                settings2 = (PipelineSettings) anonymousClass1.L$2;
                MeetingEntity meeting12 = (MeetingEntity) anonymousClass1.L$1;
                String meetingId14 = (String) anonymousClass1.L$0;
                try {
                    ResultKt.throwOnFailure($result2);
                    objClean = $result2;
                    str = "Cleanup failed";
                    combined3 = meetingId14;
                    combined4 = combined8;
                    meeting8 = meeting12;
                    cleanStarted = cleanStarted4;
                    linked2 = linked8;
                    recordings2 = recordings6;
                    i8 = i16;
                    settings4 = settings2;
                    cleaned = (TextStageResult) objClean;
                    if (!StringsKt.isBlank(cleaned.getText())) {
                        throw new IllegalStateException("cleanup returned empty text".toString());
                    }
                    sb2 = new StringBuilder();
                    settings6 = settings4;
                    recordings3 = recordings2;
                    sb2.append("clean=" + cleaned.getProviderId());
                    linked6 = linked2;
                    combined6 = combined4;
                    if (StringsKt.startsWith$default(cleaned.getProviderId(), "cloud", false, 2, (Object) null)) {
                        sb2.append("(" + settings6.getCloudCleanupModel() + ")");
                        break;
                    }
                    String providerLabel6 = sb2.toString();
                    meetingRepository6 = this.repository;
                    meetingEntityCopy$default6 = MeetingEntity.copy$default(meeting8, null, null, 0L, 0L, "PENDING_CLEANUP", null, null, providerLabel6, 0L, cleaned.getText(), null, 0L, 2415, null);
                    meeting9 = meeting8;
                    anonymousClass1.L$0 = combined3;
                    anonymousClass1.L$1 = meeting9;
                    anonymousClass1.L$2 = SpillingKt.nullOutSpilledVariable(settings6);
                    anonymousClass1.L$3 = SpillingKt.nullOutSpilledVariable(linked6);
                    anonymousClass1.L$4 = SpillingKt.nullOutSpilledVariable(recordings3);
                    anonymousClass1.L$5 = SpillingKt.nullOutSpilledVariable(combined6);
                    anonymousClass1.L$6 = cleaned;
                    anonymousClass1.L$7 = SpillingKt.nullOutSpilledVariable(providerLabel6);
                    anonymousClass1.I$0 = i8;
                    anonymousClass1.J$0 = cleanStarted;
                    anonymousClass1.label = 10;
                    if (meetingRepository6.updateMeeting(meetingEntityCopy$default6, anonymousClass1) == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    meetingId5 = combined3;
                    $result = combined6;
                    settings7 = settings6;
                    recordings4 = recordings3;
                    cleanStarted3 = cleanStarted;
                    String meetingId15 = meetingId5;
                    Log.i(TAG, "Meeting " + meetingId5 + " cleanup ok chars=" + cleaned.getText().length() + " ms=" + (System.currentTimeMillis() - cleanStarted3));
                    return MeetingStageResult.Success.INSTANCE;
                } catch (Throwable th12) {
                    t = th12;
                    meetingEntity = meeting12;
                    str = "Cleanup failed";
                    recordings3 = recordings6;
                    i8 = i16;
                    combined3 = meetingId14;
                    combined2 = combined8;
                    cleanStarted = cleanStarted4;
                    linked4 = linked8;
                }
                Log.e(TAG, "Meeting " + combined3 + " cleanup failed", t);
                meetingRepository7 = this.repository;
                message = t.getMessage();
                if (message == null) {
                    str2 = str;
                } else {
                    str2 = message;
                }
                meetingEntityCopy$default7 = MeetingEntity.copy$default(meetingEntity, null, null, 0L, 0L, "FAILED", null, null, null, 0L, null, str2, 0L, 3055, null);
                anonymousClass1.L$0 = SpillingKt.nullOutSpilledVariable(combined3);
                anonymousClass1.L$1 = SpillingKt.nullOutSpilledVariable(meetingEntity);
                anonymousClass1.L$2 = SpillingKt.nullOutSpilledVariable(settings2);
                anonymousClass1.L$3 = SpillingKt.nullOutSpilledVariable(linked4);
                anonymousClass1.L$4 = SpillingKt.nullOutSpilledVariable(recordings3);
                anonymousClass1.L$5 = SpillingKt.nullOutSpilledVariable(combined2);
                anonymousClass1.L$6 = t;
                anonymousClass1.L$7 = null;
                anonymousClass1.I$0 = i8;
                anonymousClass1.J$0 = cleanStarted;
                anonymousClass1.label = 11;
                if (meetingRepository7.updateMeeting(meetingEntityCopy$default7, anonymousClass1) == coroutine_suspended) {
                    return coroutine_suspended;
                }
                cleanStarted2 = cleanStarted;
                i9 = i8;
                combined7 = combined2;
                recordings5 = recordings3;
                t = t;
                meetingId6 = combined3;
                message2 = t.getMessage();
                if (message2 == null) {
                    str3 = str;
                } else {
                    str3 = message2;
                }
                return new MeetingStageResult.Failed(str3, true);
            case 10:
                cleanStarted = anonymousClass1.J$0;
                i8 = anonymousClass1.I$0;
                cleaned = (TextStageResult) anonymousClass1.L$6;
                combined2 = (String) anonymousClass1.L$5;
                recordings4 = (List) anonymousClass1.L$4;
                linked6 = (List) anonymousClass1.L$3;
                settings7 = (PipelineSettings) anonymousClass1.L$2;
                MeetingEntity meeting13 = (MeetingEntity) anonymousClass1.L$1;
                meetingId5 = (String) anonymousClass1.L$0;
                try {
                    ResultKt.throwOnFailure($result2);
                    str = "Cleanup failed";
                    $result = combined2;
                    meeting9 = meeting13;
                    cleanStarted3 = cleanStarted;
                    String meetingId16 = meetingId5;
                    Log.i(TAG, "Meeting " + meetingId5 + " cleanup ok chars=" + cleaned.getText().length() + " ms=" + (System.currentTimeMillis() - cleanStarted3));
                    return MeetingStageResult.Success.INSTANCE;
                } catch (Throwable th13) {
                    t = th13;
                    str = "Cleanup failed";
                    recordings3 = recordings4;
                    linked4 = linked6;
                    settings2 = settings7;
                    meetingEntity = meeting13;
                    combined3 = meetingId5;
                }
                break;
            case 11:
                cleanStarted2 = anonymousClass1.J$0;
                i9 = anonymousClass1.I$0;
                t = (Throwable) anonymousClass1.L$6;
                combined7 = (String) anonymousClass1.L$5;
                recordings5 = (List) anonymousClass1.L$4;
                linked4 = (List) anonymousClass1.L$3;
                settings2 = (PipelineSettings) anonymousClass1.L$2;
                meetingId6 = (String) anonymousClass1.L$0;
                ResultKt.throwOnFailure($result2);
                str = "Cleanup failed";
                message2 = t.getMessage();
                if (message2 == null) {
                    str3 = str;
                } else {
                    str3 = message2;
                }
                return new MeetingStageResult.Failed(str3, true);
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
    }

    /* JADX WARN: Code duplicated, block: B:33:0x00d3  */
    /* JADX WARN: Code duplicated, block: B:35:0x00d6  */
    /* JADX WARN: Code duplicated, block: B:37:0x00ed A[RETURN] */
    /* JADX WARN: Code duplicated, block: B:38:0x00ee  */
    /* JADX WARN: Code duplicated, block: B:41:0x00f9 A[RETURN] */
    /* JADX WARN: Code duplicated, block: B:42:0x00fa  */
    /* JADX WARN: Code duplicated, block: B:44:0x0119 A[RETURN] */
    /* JADX WARN: Code duplicated, block: B:45:0x011a  */
    /* JADX WARN: Code duplicated, block: B:48:0x0124  */
    /* JADX WARN: Code duplicated, block: B:52:0x0135  */
    /* JADX WARN: Code duplicated, block: B:55:0x0139  */
    /* JADX WARN: Code duplicated, block: B:57:0x016a A[RETURN] */
    /* JADX WARN: Code duplicated, block: B:58:0x016b  */
    /* JADX WARN: Code duplicated, block: B:62:0x0178  */
    /* JADX WARN: Code duplicated, block: B:64:0x0197 A[RETURN] */
    /* JADX WARN: Code duplicated, block: B:65:0x0198  */
    /* JADX WARN: Code duplicated, block: B:7:0x001a  */
    public final Object runSummary(String meetingId, Continuation<? super MeetingStageResult> continuation) {
        C06871 c06871;
        Object meeting;
        MeetingEntity meeting2;
        String cleanedText;
        Object objSummarizeWithText;
        Object objRunCleanup;
        MeetingEntity meeting3;
        String cleanedText2;
        MeetingStageResult cleanResult;
        Object meeting4;
        String cleanedText3;
        MeetingStageResult cleanResult2;
        MeetingEntity meeting5;
        MeetingEntity refreshed;
        String cleanTextOnly;
        String text;
        Object objSummarizeWithText2;
        String meetingId2 = meetingId;
        if (continuation instanceof C06871) {
            c06871 = (C06871) continuation;
            if ((c06871.label & Integer.MIN_VALUE) != 0) {
                c06871.label -= Integer.MIN_VALUE;
            } else {
                c06871 = new C06871(continuation);
            }
        } else {
            c06871 = new C06871(continuation);
        }
        Object $result = c06871.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (c06871.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                MeetingRepository meetingRepository = this.repository;
                c06871.L$0 = meetingId2;
                c06871.label = 1;
                meeting = meetingRepository.getMeeting(meetingId2, c06871);
                if (meeting == coroutine_suspended) {
                    return coroutine_suspended;
                }
                meeting2 = (MeetingEntity) meeting;
                if (meeting2 == null && !Intrinsics.areEqual(meeting2.getStatus(), "READY")) {
                    cleanedText = meeting2.getCleanTextOnly();
                    if (cleanedText != null || StringsKt.isBlank(cleanedText)) {
                        cleanedText = null;
                    }
                    if (cleanedText == null) {
                        String cleanupProvider = meeting2.getCleanupProvider();
                        c06871.L$0 = SpillingKt.nullOutSpilledVariable(meetingId2);
                        c06871.L$1 = SpillingKt.nullOutSpilledVariable(meeting2);
                        c06871.L$2 = SpillingKt.nullOutSpilledVariable(cleanedText);
                        c06871.label = 5;
                        objSummarizeWithText = summarizeWithText(meetingId2, cleanedText, cleanupProvider, c06871);
                        if (objSummarizeWithText == coroutine_suspended) {
                            return coroutine_suspended;
                        }
                        return objSummarizeWithText;
                    }
                    c06871.L$0 = meetingId2;
                    c06871.L$1 = SpillingKt.nullOutSpilledVariable(meeting2);
                    c06871.L$2 = SpillingKt.nullOutSpilledVariable(cleanedText);
                    c06871.label = 2;
                    objRunCleanup = runCleanup(meetingId2, c06871);
                    if (objRunCleanup == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    String str = cleanedText;
                    meeting3 = meeting2;
                    cleanedText2 = str;
                    cleanResult = (MeetingStageResult) objRunCleanup;
                    if (!(cleanResult instanceof MeetingStageResult.Success)) {
                        return cleanResult;
                    }
                    MeetingRepository meetingRepository2 = this.repository;
                    c06871.L$0 = meetingId2;
                    c06871.L$1 = SpillingKt.nullOutSpilledVariable(meeting3);
                    c06871.L$2 = SpillingKt.nullOutSpilledVariable(cleanedText2);
                    c06871.L$3 = SpillingKt.nullOutSpilledVariable(cleanResult);
                    c06871.label = 3;
                    meeting4 = meetingRepository2.getMeeting(meetingId2, c06871);
                    if (meeting4 == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    MeetingEntity meetingEntity = meeting3;
                    cleanedText3 = cleanedText2;
                    cleanResult2 = cleanResult;
                    meeting5 = meetingEntity;
                    refreshed = (MeetingEntity) meeting4;
                    if (refreshed != null && (cleanTextOnly = refreshed.getCleanTextOnly()) != null) {
                        text = StringsKt.isBlank(cleanTextOnly) ? null : cleanTextOnly;
                        if (text != null) {
                            String cleanupProvider2 = refreshed.getCleanupProvider();
                            c06871.L$0 = SpillingKt.nullOutSpilledVariable(meetingId2);
                            c06871.L$1 = SpillingKt.nullOutSpilledVariable(meeting5);
                            c06871.L$2 = SpillingKt.nullOutSpilledVariable(cleanedText3);
                            c06871.L$3 = SpillingKt.nullOutSpilledVariable(cleanResult2);
                            c06871.L$4 = SpillingKt.nullOutSpilledVariable(refreshed);
                            c06871.L$5 = SpillingKt.nullOutSpilledVariable(text);
                            c06871.label = 4;
                            objSummarizeWithText2 = summarizeWithText(meetingId2, text, cleanupProvider2, c06871);
                            if (objSummarizeWithText2 == coroutine_suspended) {
                                return coroutine_suspended;
                            }
                            return objSummarizeWithText2;
                        }
                    }
                    return new MeetingStageResult.Failed("No cleaned text after cleanup", true);
                }
                return MeetingStageResult.Success.INSTANCE;
            case 1:
                meetingId2 = (String) c06871.L$0;
                ResultKt.throwOnFailure($result);
                meeting = $result;
                meeting2 = (MeetingEntity) meeting;
                if (meeting2 == null) {
                    return MeetingStageResult.Success.INSTANCE;
                }
                cleanedText = meeting2.getCleanTextOnly();
                if (cleanedText != null) {
                    cleanedText = null;
                } else {
                    cleanedText = null;
                }
                if (cleanedText == null) {
                    String cleanupProvider3 = meeting2.getCleanupProvider();
                    c06871.L$0 = SpillingKt.nullOutSpilledVariable(meetingId2);
                    c06871.L$1 = SpillingKt.nullOutSpilledVariable(meeting2);
                    c06871.L$2 = SpillingKt.nullOutSpilledVariable(cleanedText);
                    c06871.label = 5;
                    objSummarizeWithText = summarizeWithText(meetingId2, cleanedText, cleanupProvider3, c06871);
                    if (objSummarizeWithText == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    return objSummarizeWithText;
                }
                c06871.L$0 = meetingId2;
                c06871.L$1 = SpillingKt.nullOutSpilledVariable(meeting2);
                c06871.L$2 = SpillingKt.nullOutSpilledVariable(cleanedText);
                c06871.label = 2;
                objRunCleanup = runCleanup(meetingId2, c06871);
                if (objRunCleanup == coroutine_suspended) {
                    return coroutine_suspended;
                }
                String str2 = cleanedText;
                meeting3 = meeting2;
                cleanedText2 = str2;
                cleanResult = (MeetingStageResult) objRunCleanup;
                if (!(cleanResult instanceof MeetingStageResult.Success)) {
                    return cleanResult;
                }
                MeetingRepository meetingRepository3 = this.repository;
                c06871.L$0 = meetingId2;
                c06871.L$1 = SpillingKt.nullOutSpilledVariable(meeting3);
                c06871.L$2 = SpillingKt.nullOutSpilledVariable(cleanedText2);
                c06871.L$3 = SpillingKt.nullOutSpilledVariable(cleanResult);
                c06871.label = 3;
                meeting4 = meetingRepository3.getMeeting(meetingId2, c06871);
                if (meeting4 == coroutine_suspended) {
                    return coroutine_suspended;
                }
                MeetingEntity meetingEntity2 = meeting3;
                cleanedText3 = cleanedText2;
                cleanResult2 = cleanResult;
                meeting5 = meetingEntity2;
                refreshed = (MeetingEntity) meeting4;
                if (refreshed != null) {
                    if (StringsKt.isBlank(cleanTextOnly)) {
                    }
                    if (text != null) {
                        String cleanupProvider4 = refreshed.getCleanupProvider();
                        c06871.L$0 = SpillingKt.nullOutSpilledVariable(meetingId2);
                        c06871.L$1 = SpillingKt.nullOutSpilledVariable(meeting5);
                        c06871.L$2 = SpillingKt.nullOutSpilledVariable(cleanedText3);
                        c06871.L$3 = SpillingKt.nullOutSpilledVariable(cleanResult2);
                        c06871.L$4 = SpillingKt.nullOutSpilledVariable(refreshed);
                        c06871.L$5 = SpillingKt.nullOutSpilledVariable(text);
                        c06871.label = 4;
                        objSummarizeWithText2 = summarizeWithText(meetingId2, text, cleanupProvider4, c06871);
                        if (objSummarizeWithText2 == coroutine_suspended) {
                            return coroutine_suspended;
                        }
                        return objSummarizeWithText2;
                    }
                }
                return new MeetingStageResult.Failed("No cleaned text after cleanup", true);
            case 2:
                cleanedText2 = (String) c06871.L$2;
                meeting3 = (MeetingEntity) c06871.L$1;
                meetingId2 = (String) c06871.L$0;
                ResultKt.throwOnFailure($result);
                objRunCleanup = $result;
                cleanResult = (MeetingStageResult) objRunCleanup;
                if (!(cleanResult instanceof MeetingStageResult.Success)) {
                    return cleanResult;
                }
                MeetingRepository meetingRepository4 = this.repository;
                c06871.L$0 = meetingId2;
                c06871.L$1 = SpillingKt.nullOutSpilledVariable(meeting3);
                c06871.L$2 = SpillingKt.nullOutSpilledVariable(cleanedText2);
                c06871.L$3 = SpillingKt.nullOutSpilledVariable(cleanResult);
                c06871.label = 3;
                meeting4 = meetingRepository4.getMeeting(meetingId2, c06871);
                if (meeting4 == coroutine_suspended) {
                    return coroutine_suspended;
                }
                MeetingEntity meetingEntity3 = meeting3;
                cleanedText3 = cleanedText2;
                cleanResult2 = cleanResult;
                meeting5 = meetingEntity3;
                refreshed = (MeetingEntity) meeting4;
                if (refreshed != null) {
                    if (StringsKt.isBlank(cleanTextOnly)) {
                    }
                    if (text != null) {
                        String cleanupProvider5 = refreshed.getCleanupProvider();
                        c06871.L$0 = SpillingKt.nullOutSpilledVariable(meetingId2);
                        c06871.L$1 = SpillingKt.nullOutSpilledVariable(meeting5);
                        c06871.L$2 = SpillingKt.nullOutSpilledVariable(cleanedText3);
                        c06871.L$3 = SpillingKt.nullOutSpilledVariable(cleanResult2);
                        c06871.L$4 = SpillingKt.nullOutSpilledVariable(refreshed);
                        c06871.L$5 = SpillingKt.nullOutSpilledVariable(text);
                        c06871.label = 4;
                        objSummarizeWithText2 = summarizeWithText(meetingId2, text, cleanupProvider5, c06871);
                        if (objSummarizeWithText2 == coroutine_suspended) {
                            return coroutine_suspended;
                        }
                        return objSummarizeWithText2;
                    }
                }
                return new MeetingStageResult.Failed("No cleaned text after cleanup", true);
            case 3:
                cleanResult2 = (MeetingStageResult) c06871.L$3;
                cleanedText3 = (String) c06871.L$2;
                meeting5 = (MeetingEntity) c06871.L$1;
                meetingId2 = (String) c06871.L$0;
                ResultKt.throwOnFailure($result);
                meeting4 = $result;
                refreshed = (MeetingEntity) meeting4;
                if (refreshed != null) {
                    if (StringsKt.isBlank(cleanTextOnly)) {
                    }
                    if (text != null) {
                        String cleanupProvider6 = refreshed.getCleanupProvider();
                        c06871.L$0 = SpillingKt.nullOutSpilledVariable(meetingId2);
                        c06871.L$1 = SpillingKt.nullOutSpilledVariable(meeting5);
                        c06871.L$2 = SpillingKt.nullOutSpilledVariable(cleanedText3);
                        c06871.L$3 = SpillingKt.nullOutSpilledVariable(cleanResult2);
                        c06871.L$4 = SpillingKt.nullOutSpilledVariable(refreshed);
                        c06871.L$5 = SpillingKt.nullOutSpilledVariable(text);
                        c06871.label = 4;
                        objSummarizeWithText2 = summarizeWithText(meetingId2, text, cleanupProvider6, c06871);
                        if (objSummarizeWithText2 == coroutine_suspended) {
                            return coroutine_suspended;
                        }
                        return objSummarizeWithText2;
                    }
                }
                return new MeetingStageResult.Failed("No cleaned text after cleanup", true);
            case 4:
                ResultKt.throwOnFailure($result);
                return $result;
            case 5:
                ResultKt.throwOnFailure($result);
                return $result;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code duplicated, block: B:102:0x03c7  */
    /* JADX WARN: Code duplicated, block: B:103:0x03ca  */
    /* JADX WARN: Code duplicated, block: B:105:0x03ce  */
    /* JADX WARN: Code duplicated, block: B:106:0x03d1  */
    /* JADX WARN: Code duplicated, block: B:109:0x0439 A[RETURN] */
    /* JADX WARN: Code duplicated, block: B:110:0x043a  */
    /* JADX WARN: Code duplicated, block: B:113:0x044b  */
    /* JADX WARN: Code duplicated, block: B:114:0x044e  */
    /* JADX WARN: Code duplicated, block: B:134:0x021d A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:30:0x012d  */
    /* JADX WARN: Code duplicated, block: B:32:0x0135  */
    /* JADX WARN: Code duplicated, block: B:34:0x017c A[RETURN] */
    /* JADX WARN: Code duplicated, block: B:35:0x017d  */
    /* JADX WARN: Code duplicated, block: B:39:0x019f A[RETURN] */
    /* JADX WARN: Code duplicated, block: B:40:0x01a0  */
    /* JADX WARN: Code duplicated, block: B:43:0x01b7 A[Catch: all -> 0x0364, TRY_LEAVE, TryCatch #2 {all -> 0x0364, blocks: (B:41:0x01a9, B:43:0x01b7), top: B:121:0x01a9 }] */
    /* JADX WARN: Code duplicated, block: B:46:0x01d2  */
    /* JADX WARN: Code duplicated, block: B:50:0x01e2  */
    /* JADX WARN: Code duplicated, block: B:67:0x02aa A[RETURN] */
    /* JADX WARN: Code duplicated, block: B:68:0x02ab  */
    /* JADX WARN: Code duplicated, block: B:7:0x001a  */
    /* JADX WARN: Code duplicated, block: B:88:0x0346  */
    /* JADX WARN: Code duplicated, block: B:99:0x03a1  */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r6v12, types: [boolean] */
    /* JADX WARN: Type inference fix 'apply assigned field type' failed
    java.lang.UnsupportedOperationException: ArgType.getObject(), call class: class jadx.core.dex.instructions.args.ArgType$UnknownArg
    	at jadx.core.dex.instructions.args.ArgType.getObject(ArgType.java:596)
    	at jadx.core.dex.attributes.nodes.ClassTypeVarsAttr.getTypeVarsMapFor(ClassTypeVarsAttr.java:35)
    	at jadx.core.dex.nodes.utils.TypeUtils.replaceClassGenerics(TypeUtils.java:177)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.insertExplicitUseCast(FixTypesVisitor.java:397)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryFieldTypeWithNewCasts(FixTypesVisitor.java:359)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.applyFieldType(FixTypesVisitor.java:309)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
     */
    /* JADX WARN: Unreachable blocks removed: 2, instructions: 4 */
    public final Object summarizeWithText(String str, String str2, String str3, Continuation<? super MeetingStageResult> continuation) {
        C06881 c06881;
        String str4;
        String str5;
        Object meeting;
        MeetingEntity meetingEntity;
        PipelineSettings pipelineSettingsLoad;
        long jCurrentTimeMillis;
        MeetingRepository meetingRepository;
        MeetingEntity meetingEntityCopy$default;
        String str6;
        String str7;
        long j;
        String str8;
        MeetingEntity meetingEntity2;
        PipelineSettings pipelineSettings;
        String str9;
        String str10;
        String str11;
        String str12;
        long j2;
        MeetingEntity meetingEntity3;
        Object obj;
        Object objSummarize;
        String str13;
        PipelineSettings pipelineSettings2;
        MeetingEntity meetingEntity4;
        TextStageResult textStageResult;
        ?? IsBlank;
        MeetingCleanupParsed meetingCleanupParsed;
        StringBuilder sb;
        String str14;
        String str15;
        PipelineSettings pipelineSettings3;
        MeetingRepository meetingRepository2;
        MeetingEntity meetingEntityCopy$default2;
        MeetingEntity meetingEntity5;
        String str16;
        TextStageResult textStageResult2;
        PipelineSettings pipelineSettings4;
        String str17;
        MeetingCleanupParsed meetingCleanupParsed2;
        long j3;
        MeetingRepository meetingRepository3;
        String cleanedTranscript;
        String message;
        String str18;
        String str19;
        String str20;
        MeetingEntity meetingEntityCopy$default3;
        Throwable th;
        String str21;
        Object obj2;
        String str22;
        long j4;
        Object obj3;
        Object obj4;
        String str23;
        long j5;
        String message2;
        String str24;
        String str25 = str;
        if (continuation instanceof C06881) {
            c06881 = (C06881) continuation;
            if ((c06881.label & Integer.MIN_VALUE) != 0) {
                c06881.label -= Integer.MIN_VALUE;
            } else {
                c06881 = new C06881(continuation);
            }
        } else {
            c06881 = new C06881(continuation);
        }
        Object obj5 = c06881.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (c06881.label) {
            case 0:
                ResultKt.throwOnFailure(obj5);
                MeetingRepository meetingRepository4 = this.repository;
                c06881.L$0 = str25;
                str4 = str2;
                c06881.L$1 = str4;
                str5 = str3;
                c06881.L$2 = str5;
                c06881.label = 1;
                meeting = meetingRepository4.getMeeting(str25, c06881);
                if (meeting == coroutine_suspended) {
                    return coroutine_suspended;
                }
                meetingEntity = (MeetingEntity) meeting;
                if (meetingEntity == null) {
                    return new MeetingStageResult.Failed("Meeting gone", false);
                }
                pipelineSettingsLoad = this.pipelineConfig.load();
                jCurrentTimeMillis = System.currentTimeMillis();
                meetingRepository = this.repository;
                meetingEntityCopy$default = MeetingEntity.copy$default(meetingEntity, null, null, 0L, 0L, "CLEANING", null, null, null, 0L, null, null, 0L, 3055, null);
                c06881.L$0 = str25;
                c06881.L$1 = str4;
                c06881.L$2 = str5;
                c06881.L$3 = meetingEntity;
                c06881.L$4 = pipelineSettingsLoad;
                c06881.J$0 = jCurrentTimeMillis;
                str6 = str25;
                c06881.label = 2;
                if (meetingRepository.updateMeeting(meetingEntityCopy$default, c06881) == coroutine_suspended) {
                    return coroutine_suspended;
                }
                str7 = str5;
                j = jCurrentTimeMillis;
                str8 = str4;
                meetingEntity2 = meetingEntity;
                pipelineSettings = pipelineSettingsLoad;
                str9 = str6;
                try {
                    ProviderRouter providerRouter = this.router;
                    c06881.L$0 = str9;
                    c06881.L$1 = str8;
                    c06881.L$2 = str7;
                    c06881.L$3 = meetingEntity2;
                    c06881.L$4 = pipelineSettings;
                    c06881.J$0 = j;
                    c06881.label = 3;
                    objSummarize = providerRouter.summarize(str8, c06881);
                    if (objSummarize == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    long j6 = j;
                    str11 = str9;
                    str13 = str8;
                    pipelineSettings2 = pipelineSettings;
                    j2 = j6;
                    meetingEntity4 = meetingEntity2;
                    try {
                        textStageResult = (TextStageResult) objSummarize;
                        IsBlank = StringsKt.isBlank(textStageResult.getText());
                        try {
                            if (IsBlank == 0) {
                                throw new IllegalStateException("summary returned empty text".toString());
                            }
                            String strAssembleMeetingMarkdown = CleanupPrompts.INSTANCE.assembleMeetingMarkdown(textStageResult.getText(), str13);
                            meetingCleanupParsed = MeetingCleanupParser.INSTANCE.parse(strAssembleMeetingMarkdown);
                            sb = new StringBuilder();
                            if (str7 == null) {
                                str14 = "clean=?";
                            } else {
                                str14 = str7;
                            }
                            str15 = str13;
                            try {
                                sb.append(str14);
                                try {
                                    sb.append(";summary=" + textStageResult.getProviderId());
                                    str10 = "Summary failed";
                                    pipelineSettings3 = pipelineSettings2;
                                    try {
                                        if (StringsKt.startsWith$default(textStageResult.getProviderId(), "cloud", false, 2, (Object) null)) {
                                            try {
                                                sb.append("(" + pipelineSettings3.getCloudSummaryModel() + ")");
                                            } catch (Throwable th2) {
                                                th = th2;
                                                obj = pipelineSettings3;
                                                meetingEntity3 = meetingEntity4;
                                                str12 = str15;
                                            }
                                        }
                                        String string = sb.toString();
                                        meetingRepository2 = this.repository;
                                        meetingEntityCopy$default2 = MeetingEntity.copy$default(meetingEntity4, null, meetingCleanupParsed.getTitle(), 0L, 0L, "READY", meetingCleanupParsed.getCleanedMarkdown(), meetingCleanupParsed.toMetadataJson(), string, 0L, str15, null, 0L, 2317, null);
                                        meetingEntity5 = meetingEntity4;
                                        str16 = str15;
                                        c06881.L$0 = str11;
                                        c06881.L$1 = str16;
                                        c06881.L$2 = str7;
                                        c06881.L$3 = meetingEntity5;
                                        c06881.L$4 = SpillingKt.nullOutSpilledVariable(pipelineSettings3);
                                        c06881.L$5 = SpillingKt.nullOutSpilledVariable(textStageResult);
                                        c06881.L$6 = SpillingKt.nullOutSpilledVariable(strAssembleMeetingMarkdown);
                                        c06881.L$7 = meetingCleanupParsed;
                                        c06881.L$8 = SpillingKt.nullOutSpilledVariable(string);
                                        c06881.J$0 = j2;
                                        c06881.label = 4;
                                        if (meetingRepository2.updateMeeting(meetingEntityCopy$default2, c06881) == coroutine_suspended) {
                                            return coroutine_suspended;
                                        }
                                        textStageResult2 = textStageResult;
                                        pipelineSettings4 = pipelineSettings3;
                                        str17 = str11;
                                        long j7 = j2;
                                        meetingCleanupParsed2 = meetingCleanupParsed;
                                        j3 = j7;
                                        str23 = str16;
                                        try {
                                            j5 = j3;
                                            try {
                                                String str26 = str17;
                                                try {
                                                    Log.i(TAG, "Meeting " + str17 + " summary ok title=" + meetingCleanupParsed2.getTitle() + " ms=" + (System.currentTimeMillis() - j5));
                                                    return MeetingStageResult.Success.INSTANCE;
                                                } catch (Throwable th3) {
                                                    th = th3;
                                                    j2 = j5;
                                                    str12 = str23;
                                                    str11 = str26;
                                                    meetingEntity3 = meetingEntity5;
                                                    obj = pipelineSettings4;
                                                }
                                            } catch (Throwable th4) {
                                                th = th4;
                                                String str27 = str17;
                                                j2 = j5;
                                                str12 = str23;
                                                str11 = str27;
                                                meetingEntity3 = meetingEntity5;
                                                obj = pipelineSettings4;
                                            }
                                        } catch (Throwable th5) {
                                            th = th5;
                                            String str28 = str17;
                                            j2 = j3;
                                            str12 = str23;
                                            str11 = str28;
                                            meetingEntity3 = meetingEntity5;
                                            obj = pipelineSettings4;
                                        }
                                    } catch (Throwable th6) {
                                        th = th6;
                                        obj = pipelineSettings3;
                                        str12 = str15;
                                        meetingEntity3 = meetingEntity4;
                                    }
                                } catch (Throwable th7) {
                                    th = th7;
                                    str10 = "Summary failed";
                                    str12 = str15;
                                    meetingEntity3 = meetingEntity4;
                                    obj = pipelineSettings2;
                                }
                            } catch (Throwable th8) {
                                th = th8;
                                str10 = "Summary failed";
                                str12 = str15;
                                meetingEntity3 = meetingEntity4;
                                obj = pipelineSettings2;
                            }
                        } catch (Throwable th9) {
                            th = th9;
                            obj = str3;
                            str12 = obj5;
                            meetingEntity3 = IsBlank;
                        }
                        break;
                    } catch (Throwable th10) {
                        th = th10;
                        str10 = "Summary failed";
                        meetingEntity3 = meetingEntity4;
                        str12 = str13;
                        obj = pipelineSettings2;
                    }
                    Log.e(TAG, "Meeting " + str11 + " summary failed — keeping cleanTextOnly", th);
                    meetingRepository3 = this.repository;
                    cleanedTranscript = meetingEntity3.getCleanedTranscript();
                    if (cleanedTranscript == null) {
                        cleanedTranscript = CleanupPrompts.INSTANCE.assembleMeetingMarkdown("Title: (summary failed)\n\nOverview:\n" + th.getMessage(), str12);
                    }
                    String str29 = cleanedTranscript;
                    message = th.getMessage();
                    if (message == null) {
                        str18 = str10;
                    } else {
                        str18 = message;
                    }
                    if (str7 == null) {
                        str19 = "";
                    } else {
                        str19 = str7;
                    }
                    str20 = str12;
                    meetingEntityCopy$default3 = MeetingEntity.copy$default(meetingEntity3, null, null, 0L, 0L, "FAILED", str29, null, str19 + ";summary=FAILED", 0L, str20, str18, 0L, 2383, null);
                    c06881.L$0 = SpillingKt.nullOutSpilledVariable(str11);
                    c06881.L$1 = SpillingKt.nullOutSpilledVariable(str20);
                    c06881.L$2 = SpillingKt.nullOutSpilledVariable(str7);
                    c06881.L$3 = SpillingKt.nullOutSpilledVariable(meetingEntity3);
                    c06881.L$4 = SpillingKt.nullOutSpilledVariable(obj);
                    c06881.L$5 = th;
                    c06881.L$6 = null;
                    c06881.L$7 = null;
                    c06881.L$8 = null;
                    c06881.J$0 = j2;
                    c06881.label = 5;
                    if (meetingRepository3.updateMeeting(meetingEntityCopy$default3, c06881) == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    th = th;
                    str21 = str11;
                    obj2 = obj;
                    str22 = str7;
                    j4 = j2;
                    obj3 = meetingEntity3;
                    obj4 = str20;
                    message2 = th.getMessage();
                    if (message2 == null) {
                        str24 = str10;
                    } else {
                        str24 = message2;
                    }
                    return new MeetingStageResult.Failed(str24, true);
                } catch (Throwable th11) {
                    th = th11;
                    str10 = "Summary failed";
                    long j8 = j;
                    str11 = str9;
                    str12 = str8;
                    j2 = j8;
                    meetingEntity3 = meetingEntity2;
                    obj = pipelineSettings;
                }
                break;
            case 1:
                String str30 = (String) c06881.L$2;
                str4 = (String) c06881.L$1;
                str25 = (String) c06881.L$0;
                ResultKt.throwOnFailure(obj5);
                str5 = str30;
                meeting = obj5;
                meetingEntity = (MeetingEntity) meeting;
                if (meetingEntity == null) {
                    return new MeetingStageResult.Failed("Meeting gone", false);
                }
                pipelineSettingsLoad = this.pipelineConfig.load();
                jCurrentTimeMillis = System.currentTimeMillis();
                meetingRepository = this.repository;
                meetingEntityCopy$default = MeetingEntity.copy$default(meetingEntity, null, null, 0L, 0L, "CLEANING", null, null, null, 0L, null, null, 0L, 3055, null);
                c06881.L$0 = str25;
                c06881.L$1 = str4;
                c06881.L$2 = str5;
                c06881.L$3 = meetingEntity;
                c06881.L$4 = pipelineSettingsLoad;
                c06881.J$0 = jCurrentTimeMillis;
                str6 = str25;
                c06881.label = 2;
                if (meetingRepository.updateMeeting(meetingEntityCopy$default, c06881) == coroutine_suspended) {
                    return coroutine_suspended;
                }
                str7 = str5;
                j = jCurrentTimeMillis;
                str8 = str4;
                meetingEntity2 = meetingEntity;
                pipelineSettings = pipelineSettingsLoad;
                str9 = str6;
                ProviderRouter providerRouter2 = this.router;
                c06881.L$0 = str9;
                c06881.L$1 = str8;
                c06881.L$2 = str7;
                c06881.L$3 = meetingEntity2;
                c06881.L$4 = pipelineSettings;
                c06881.J$0 = j;
                c06881.label = 3;
                objSummarize = providerRouter2.summarize(str8, c06881);
                if (objSummarize == coroutine_suspended) {
                    return coroutine_suspended;
                }
                long j9 = j;
                str11 = str9;
                str13 = str8;
                pipelineSettings2 = pipelineSettings;
                j2 = j9;
                meetingEntity4 = meetingEntity2;
                textStageResult = (TextStageResult) objSummarize;
                IsBlank = StringsKt.isBlank(textStageResult.getText());
                if (IsBlank == 0) {
                    throw new IllegalStateException("summary returned empty text".toString());
                }
                String strAssembleMeetingMarkdown2 = CleanupPrompts.INSTANCE.assembleMeetingMarkdown(textStageResult.getText(), str13);
                meetingCleanupParsed = MeetingCleanupParser.INSTANCE.parse(strAssembleMeetingMarkdown2);
                sb = new StringBuilder();
                if (str7 == null) {
                    str14 = "clean=?";
                } else {
                    str14 = str7;
                }
                str15 = str13;
                sb.append(str14);
                sb.append(";summary=" + textStageResult.getProviderId());
                str10 = "Summary failed";
                pipelineSettings3 = pipelineSettings2;
                if (StringsKt.startsWith$default(textStageResult.getProviderId(), "cloud", false, 2, (Object) null)) {
                    sb.append("(" + pipelineSettings3.getCloudSummaryModel() + ")");
                    break;
                }
                String string2 = sb.toString();
                meetingRepository2 = this.repository;
                meetingEntityCopy$default2 = MeetingEntity.copy$default(meetingEntity4, null, meetingCleanupParsed.getTitle(), 0L, 0L, "READY", meetingCleanupParsed.getCleanedMarkdown(), meetingCleanupParsed.toMetadataJson(), string2, 0L, str15, null, 0L, 2317, null);
                meetingEntity5 = meetingEntity4;
                str16 = str15;
                c06881.L$0 = str11;
                c06881.L$1 = str16;
                c06881.L$2 = str7;
                c06881.L$3 = meetingEntity5;
                c06881.L$4 = SpillingKt.nullOutSpilledVariable(pipelineSettings3);
                c06881.L$5 = SpillingKt.nullOutSpilledVariable(textStageResult);
                c06881.L$6 = SpillingKt.nullOutSpilledVariable(strAssembleMeetingMarkdown2);
                c06881.L$7 = meetingCleanupParsed;
                c06881.L$8 = SpillingKt.nullOutSpilledVariable(string2);
                c06881.J$0 = j2;
                c06881.label = 4;
                if (meetingRepository2.updateMeeting(meetingEntityCopy$default2, c06881) == coroutine_suspended) {
                    return coroutine_suspended;
                }
                textStageResult2 = textStageResult;
                pipelineSettings4 = pipelineSettings3;
                str17 = str11;
                long j10 = j2;
                meetingCleanupParsed2 = meetingCleanupParsed;
                j3 = j10;
                str23 = str16;
                j5 = j3;
                String str210 = str17;
                Log.i(TAG, "Meeting " + str17 + " summary ok title=" + meetingCleanupParsed2.getTitle() + " ms=" + (System.currentTimeMillis() - j5));
                return MeetingStageResult.Success.INSTANCE;
                Log.e(TAG, "Meeting " + str11 + " summary failed — keeping cleanTextOnly", th);
                meetingRepository3 = this.repository;
                cleanedTranscript = meetingEntity3.getCleanedTranscript();
                if (cleanedTranscript == null) {
                    cleanedTranscript = CleanupPrompts.INSTANCE.assembleMeetingMarkdown("Title: (summary failed)\n\nOverview:\n" + th.getMessage(), str12);
                }
                String str211 = cleanedTranscript;
                message = th.getMessage();
                if (message == null) {
                    str18 = str10;
                } else {
                    str18 = message;
                }
                if (str7 == null) {
                    str19 = "";
                } else {
                    str19 = str7;
                }
                str20 = str12;
                meetingEntityCopy$default3 = MeetingEntity.copy$default(meetingEntity3, null, null, 0L, 0L, "FAILED", str211, null, str19 + ";summary=FAILED", 0L, str20, str18, 0L, 2383, null);
                c06881.L$0 = SpillingKt.nullOutSpilledVariable(str11);
                c06881.L$1 = SpillingKt.nullOutSpilledVariable(str20);
                c06881.L$2 = SpillingKt.nullOutSpilledVariable(str7);
                c06881.L$3 = SpillingKt.nullOutSpilledVariable(meetingEntity3);
                c06881.L$4 = SpillingKt.nullOutSpilledVariable(obj);
                c06881.L$5 = th;
                c06881.L$6 = null;
                c06881.L$7 = null;
                c06881.L$8 = null;
                c06881.J$0 = j2;
                c06881.label = 5;
                if (meetingRepository3.updateMeeting(meetingEntityCopy$default3, c06881) == coroutine_suspended) {
                    return coroutine_suspended;
                }
                th = th;
                str21 = str11;
                obj2 = obj;
                str22 = str7;
                j4 = j2;
                obj3 = meetingEntity3;
                obj4 = str20;
                message2 = th.getMessage();
                if (message2 == null) {
                    str24 = str10;
                } else {
                    str24 = message2;
                }
                return new MeetingStageResult.Failed(str24, true);
            case 2:
                j = c06881.J$0;
                PipelineSettings pipelineSettings5 = (PipelineSettings) c06881.L$4;
                meetingEntity2 = (MeetingEntity) c06881.L$3;
                str7 = (String) c06881.L$2;
                str8 = (String) c06881.L$1;
                String str31 = (String) c06881.L$0;
                ResultKt.throwOnFailure(obj5);
                pipelineSettings = pipelineSettings5;
                str9 = str31;
                ProviderRouter providerRouter3 = this.router;
                c06881.L$0 = str9;
                c06881.L$1 = str8;
                c06881.L$2 = str7;
                c06881.L$3 = meetingEntity2;
                c06881.L$4 = pipelineSettings;
                c06881.J$0 = j;
                c06881.label = 3;
                objSummarize = providerRouter3.summarize(str8, c06881);
                if (objSummarize == coroutine_suspended) {
                    return coroutine_suspended;
                }
                long j11 = j;
                str11 = str9;
                str13 = str8;
                pipelineSettings2 = pipelineSettings;
                j2 = j11;
                meetingEntity4 = meetingEntity2;
                textStageResult = (TextStageResult) objSummarize;
                IsBlank = StringsKt.isBlank(textStageResult.getText());
                if (IsBlank == 0) {
                    throw new IllegalStateException("summary returned empty text".toString());
                }
                String strAssembleMeetingMarkdown3 = CleanupPrompts.INSTANCE.assembleMeetingMarkdown(textStageResult.getText(), str13);
                meetingCleanupParsed = MeetingCleanupParser.INSTANCE.parse(strAssembleMeetingMarkdown3);
                sb = new StringBuilder();
                if (str7 == null) {
                    str14 = "clean=?";
                } else {
                    str14 = str7;
                }
                str15 = str13;
                sb.append(str14);
                sb.append(";summary=" + textStageResult.getProviderId());
                str10 = "Summary failed";
                pipelineSettings3 = pipelineSettings2;
                if (StringsKt.startsWith$default(textStageResult.getProviderId(), "cloud", false, 2, (Object) null)) {
                    sb.append("(" + pipelineSettings3.getCloudSummaryModel() + ")");
                    break;
                }
                String string3 = sb.toString();
                meetingRepository2 = this.repository;
                meetingEntityCopy$default2 = MeetingEntity.copy$default(meetingEntity4, null, meetingCleanupParsed.getTitle(), 0L, 0L, "READY", meetingCleanupParsed.getCleanedMarkdown(), meetingCleanupParsed.toMetadataJson(), string3, 0L, str15, null, 0L, 2317, null);
                meetingEntity5 = meetingEntity4;
                str16 = str15;
                c06881.L$0 = str11;
                c06881.L$1 = str16;
                c06881.L$2 = str7;
                c06881.L$3 = meetingEntity5;
                c06881.L$4 = SpillingKt.nullOutSpilledVariable(pipelineSettings3);
                c06881.L$5 = SpillingKt.nullOutSpilledVariable(textStageResult);
                c06881.L$6 = SpillingKt.nullOutSpilledVariable(strAssembleMeetingMarkdown3);
                c06881.L$7 = meetingCleanupParsed;
                c06881.L$8 = SpillingKt.nullOutSpilledVariable(string3);
                c06881.J$0 = j2;
                c06881.label = 4;
                if (meetingRepository2.updateMeeting(meetingEntityCopy$default2, c06881) == coroutine_suspended) {
                    return coroutine_suspended;
                }
                textStageResult2 = textStageResult;
                pipelineSettings4 = pipelineSettings3;
                str17 = str11;
                long j12 = j2;
                meetingCleanupParsed2 = meetingCleanupParsed;
                j3 = j12;
                str23 = str16;
                j5 = j3;
                String str212 = str17;
                Log.i(TAG, "Meeting " + str17 + " summary ok title=" + meetingCleanupParsed2.getTitle() + " ms=" + (System.currentTimeMillis() - j5));
                return MeetingStageResult.Success.INSTANCE;
                Log.e(TAG, "Meeting " + str11 + " summary failed — keeping cleanTextOnly", th);
                meetingRepository3 = this.repository;
                cleanedTranscript = meetingEntity3.getCleanedTranscript();
                if (cleanedTranscript == null) {
                    cleanedTranscript = CleanupPrompts.INSTANCE.assembleMeetingMarkdown("Title: (summary failed)\n\nOverview:\n" + th.getMessage(), str12);
                }
                String str213 = cleanedTranscript;
                message = th.getMessage();
                if (message == null) {
                    str18 = str10;
                } else {
                    str18 = message;
                }
                if (str7 == null) {
                    str19 = "";
                } else {
                    str19 = str7;
                }
                str20 = str12;
                meetingEntityCopy$default3 = MeetingEntity.copy$default(meetingEntity3, null, null, 0L, 0L, "FAILED", str213, null, str19 + ";summary=FAILED", 0L, str20, str18, 0L, 2383, null);
                c06881.L$0 = SpillingKt.nullOutSpilledVariable(str11);
                c06881.L$1 = SpillingKt.nullOutSpilledVariable(str20);
                c06881.L$2 = SpillingKt.nullOutSpilledVariable(str7);
                c06881.L$3 = SpillingKt.nullOutSpilledVariable(meetingEntity3);
                c06881.L$4 = SpillingKt.nullOutSpilledVariable(obj);
                c06881.L$5 = th;
                c06881.L$6 = null;
                c06881.L$7 = null;
                c06881.L$8 = null;
                c06881.J$0 = j2;
                c06881.label = 5;
                if (meetingRepository3.updateMeeting(meetingEntityCopy$default3, c06881) == coroutine_suspended) {
                    return coroutine_suspended;
                }
                th = th;
                str21 = str11;
                obj2 = obj;
                str22 = str7;
                j4 = j2;
                obj3 = meetingEntity3;
                obj4 = str20;
                message2 = th.getMessage();
                if (message2 == null) {
                    str24 = str10;
                } else {
                    str24 = message2;
                }
                return new MeetingStageResult.Failed(str24, true);
            case 3:
                long j13 = c06881.J$0;
                PipelineSettings pipelineSettings6 = (PipelineSettings) c06881.L$4;
                MeetingEntity meetingEntity6 = (MeetingEntity) c06881.L$3;
                String str32 = (String) c06881.L$2;
                String str33 = (String) c06881.L$1;
                str11 = (String) c06881.L$0;
                try {
                    ResultKt.throwOnFailure(obj5);
                    objSummarize = obj5;
                    pipelineSettings2 = pipelineSettings6;
                    str7 = str32;
                    str13 = str33;
                    meetingEntity4 = meetingEntity6;
                    j2 = j13;
                    textStageResult = (TextStageResult) objSummarize;
                    IsBlank = StringsKt.isBlank(textStageResult.getText());
                    if (IsBlank == 0) {
                        throw new IllegalStateException("summary returned empty text".toString());
                    }
                    String strAssembleMeetingMarkdown4 = CleanupPrompts.INSTANCE.assembleMeetingMarkdown(textStageResult.getText(), str13);
                    meetingCleanupParsed = MeetingCleanupParser.INSTANCE.parse(strAssembleMeetingMarkdown4);
                    sb = new StringBuilder();
                    if (str7 == null) {
                        str14 = "clean=?";
                    } else {
                        str14 = str7;
                    }
                    str15 = str13;
                    sb.append(str14);
                    sb.append(";summary=" + textStageResult.getProviderId());
                    str10 = "Summary failed";
                    pipelineSettings3 = pipelineSettings2;
                    if (StringsKt.startsWith$default(textStageResult.getProviderId(), "cloud", false, 2, (Object) null)) {
                        sb.append("(" + pipelineSettings3.getCloudSummaryModel() + ")");
                        break;
                    }
                    String string4 = sb.toString();
                    meetingRepository2 = this.repository;
                    meetingEntityCopy$default2 = MeetingEntity.copy$default(meetingEntity4, null, meetingCleanupParsed.getTitle(), 0L, 0L, "READY", meetingCleanupParsed.getCleanedMarkdown(), meetingCleanupParsed.toMetadataJson(), string4, 0L, str15, null, 0L, 2317, null);
                    meetingEntity5 = meetingEntity4;
                    str16 = str15;
                    c06881.L$0 = str11;
                    c06881.L$1 = str16;
                    c06881.L$2 = str7;
                    c06881.L$3 = meetingEntity5;
                    c06881.L$4 = SpillingKt.nullOutSpilledVariable(pipelineSettings3);
                    c06881.L$5 = SpillingKt.nullOutSpilledVariable(textStageResult);
                    c06881.L$6 = SpillingKt.nullOutSpilledVariable(strAssembleMeetingMarkdown4);
                    c06881.L$7 = meetingCleanupParsed;
                    c06881.L$8 = SpillingKt.nullOutSpilledVariable(string4);
                    c06881.J$0 = j2;
                    c06881.label = 4;
                    if (meetingRepository2.updateMeeting(meetingEntityCopy$default2, c06881) == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    textStageResult2 = textStageResult;
                    pipelineSettings4 = pipelineSettings3;
                    str17 = str11;
                    long j14 = j2;
                    meetingCleanupParsed2 = meetingCleanupParsed;
                    j3 = j14;
                    str23 = str16;
                    j5 = j3;
                    String str214 = str17;
                    Log.i(TAG, "Meeting " + str17 + " summary ok title=" + meetingCleanupParsed2.getTitle() + " ms=" + (System.currentTimeMillis() - j5));
                    return MeetingStageResult.Success.INSTANCE;
                } catch (Throwable th12) {
                    th = th12;
                    meetingEntity3 = meetingEntity6;
                    str10 = "Summary failed";
                    obj = pipelineSettings6;
                    str7 = str32;
                    str12 = str33;
                    j2 = j13;
                }
                Log.e(TAG, "Meeting " + str11 + " summary failed — keeping cleanTextOnly", th);
                meetingRepository3 = this.repository;
                cleanedTranscript = meetingEntity3.getCleanedTranscript();
                if (cleanedTranscript == null) {
                    cleanedTranscript = CleanupPrompts.INSTANCE.assembleMeetingMarkdown("Title: (summary failed)\n\nOverview:\n" + th.getMessage(), str12);
                }
                String str215 = cleanedTranscript;
                message = th.getMessage();
                if (message == null) {
                    str18 = str10;
                } else {
                    str18 = message;
                }
                if (str7 == null) {
                    str19 = "";
                } else {
                    str19 = str7;
                }
                str20 = str12;
                meetingEntityCopy$default3 = MeetingEntity.copy$default(meetingEntity3, null, null, 0L, 0L, "FAILED", str215, null, str19 + ";summary=FAILED", 0L, str20, str18, 0L, 2383, null);
                c06881.L$0 = SpillingKt.nullOutSpilledVariable(str11);
                c06881.L$1 = SpillingKt.nullOutSpilledVariable(str20);
                c06881.L$2 = SpillingKt.nullOutSpilledVariable(str7);
                c06881.L$3 = SpillingKt.nullOutSpilledVariable(meetingEntity3);
                c06881.L$4 = SpillingKt.nullOutSpilledVariable(obj);
                c06881.L$5 = th;
                c06881.L$6 = null;
                c06881.L$7 = null;
                c06881.L$8 = null;
                c06881.J$0 = j2;
                c06881.label = 5;
                if (meetingRepository3.updateMeeting(meetingEntityCopy$default3, c06881) == coroutine_suspended) {
                    return coroutine_suspended;
                }
                th = th;
                str21 = str11;
                obj2 = obj;
                str22 = str7;
                j4 = j2;
                obj3 = meetingEntity3;
                obj4 = str20;
                message2 = th.getMessage();
                if (message2 == null) {
                    str24 = str10;
                } else {
                    str24 = message2;
                }
                return new MeetingStageResult.Failed(str24, true);
            case 4:
                j3 = c06881.J$0;
                meetingCleanupParsed2 = (MeetingCleanupParsed) c06881.L$7;
                TextStageResult textStageResult3 = (TextStageResult) c06881.L$5;
                PipelineSettings pipelineSettings7 = (PipelineSettings) c06881.L$4;
                MeetingEntity meetingEntity7 = (MeetingEntity) c06881.L$3;
                String str34 = (String) c06881.L$2;
                String str35 = (String) c06881.L$1;
                str17 = (String) c06881.L$0;
                try {
                    ResultKt.throwOnFailure(obj5);
                    textStageResult2 = textStageResult3;
                    meetingEntity5 = meetingEntity7;
                    str23 = str35;
                    str7 = str34;
                    str10 = "Summary failed";
                    pipelineSettings4 = pipelineSettings7;
                    j5 = j3;
                    String str216 = str17;
                    Log.i(TAG, "Meeting " + str17 + " summary ok title=" + meetingCleanupParsed2.getTitle() + " ms=" + (System.currentTimeMillis() - j5));
                    return MeetingStageResult.Success.INSTANCE;
                } catch (Throwable th13) {
                    th = th13;
                    str7 = str34;
                    j2 = j3;
                    meetingEntity3 = meetingEntity7;
                    str11 = str17;
                    str10 = "Summary failed";
                    str12 = str35;
                    obj = pipelineSettings7;
                }
                break;
            case 5:
                j4 = c06881.J$0;
                th = (Throwable) c06881.L$5;
                PipelineSettings pipelineSettings8 = (PipelineSettings) c06881.L$4;
                Object obj6 = (MeetingEntity) c06881.L$3;
                str22 = (String) c06881.L$2;
                Object obj7 = (String) c06881.L$1;
                str21 = (String) c06881.L$0;
                ResultKt.throwOnFailure(obj5);
                str10 = "Summary failed";
                obj2 = pipelineSettings8;
                obj3 = obj6;
                obj4 = obj7;
                message2 = th.getMessage();
                if (message2 == null) {
                    str24 = str10;
                } else {
                    str24 = message2;
                }
                return new MeetingStageResult.Failed(str24, true);
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
    }
}
