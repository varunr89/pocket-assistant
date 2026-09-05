package com.varun.pocketassistant.pipeline.work;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.util.Log;
import androidx.core.app.NotificationCompat;
import androidx.core.view.MotionEventCompat;
import androidx.work.CoroutineWorker;
import androidx.work.ForegroundInfo;
import androidx.work.ListenableWorker;
import androidx.work.WorkerParameters;
import com.varun.pocketassistant.PocketAssistantApp;
import com.varun.pocketassistant.R;
import com.varun.pocketassistant.data.SegmentEntity;
import com.varun.pocketassistant.data.SessionRepository;
import com.varun.pocketassistant.data.TranscriptStatus;
import com.varun.pocketassistant.speech.AsrStage;
import kotlin.Metadata;
import kotlin.ResultKt;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.ContinuationImpl;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SpillingKt;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: AsrWorker.kt */
/* JADX INFO: loaded from: classes7.dex */
@Metadata(d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\b\u0007\u0018\u0000 \u00112\u00020\u0001:\u0001\u0011B\u0017\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0004\b\u0006\u0010\u0007J\u000e\u0010\b\u001a\u00020\tH\u0096@¢\u0006\u0002\u0010\nJ\u0010\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000eH\u0002J\b\u0010\u000f\u001a\u00020\u0010H\u0002¨\u0006\u0012"}, d2 = {"Lcom/varun/pocketassistant/pipeline/work/AsrWorker;", "Landroidx/work/CoroutineWorker;", "appContext", "Landroid/content/Context;", "params", "Landroidx/work/WorkerParameters;", "<init>", "(Landroid/content/Context;Landroidx/work/WorkerParameters;)V", "doWork", "Landroidx/work/ListenableWorker$Result;", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "createForegroundInfo", "Landroidx/work/ForegroundInfo;", "text", "", "ensureChannel", "", "Companion", "app_debug"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class AsrWorker extends CoroutineWorker {
    private static final String CHANNEL_ID = "pipeline_processing";
    public static final String KEY_SEGMENT_ID = "segmentId";
    private static final int MAX_ATTEMPTS = 3;
    private static final int NOTIFICATION_ID = 42001;
    private static final String TAG = "AsrWorker";
    public static final int $stable = 8;

    /* JADX INFO: renamed from: com.varun.pocketassistant.pipeline.work.AsrWorker$doWork$1, reason: invalid class name */
    /* JADX INFO: compiled from: AsrWorker.kt */
    @Metadata(k = 3, mv = {2, 2, 0}, xi = 48)
    @DebugMetadata(c = "com.varun.pocketassistant.pipeline.work.AsrWorker", f = "AsrWorker.kt", i = {0, 0, 1, 1, 2, 2, 2, 3, 3, 3, 3}, l = {26, MotionEventCompat.AXIS_RELATIVE_Y, MotionEventCompat.AXIS_GENERIC_6, MotionEventCompat.AXIS_GENERIC_8}, m = "doWork", n = {AsrWorker.KEY_SEGMENT_ID, "app", AsrWorker.KEY_SEGMENT_ID, "app", AsrWorker.KEY_SEGMENT_ID, "app", "t", AsrWorker.KEY_SEGMENT_ID, "app", "t", "seg"}, s = {"L$0", "L$1", "L$0", "L$1", "L$0", "L$1", "L$2", "L$0", "L$1", "L$2", "L$3"})
    static final class AnonymousClass1 extends ContinuationImpl {
        Object L$0;
        Object L$1;
        Object L$2;
        Object L$3;
        int label;
        /* synthetic */ Object result;

        AnonymousClass1(Continuation<? super AnonymousClass1> continuation) {
            super(continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            this.result = obj;
            this.label |= Integer.MIN_VALUE;
            return AsrWorker.this.doWork(this);
        }
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public AsrWorker(Context appContext, WorkerParameters params) {
        super(appContext, params);
        Intrinsics.checkNotNullParameter(appContext, "appContext");
        Intrinsics.checkNotNullParameter(params, "params");
    }

    /* JADX WARN: Code duplicated, block: B:38:0x00de A[RETURN] */
    /* JADX WARN: Code duplicated, block: B:7:0x0018  */
    @Override // androidx.work.CoroutineWorker
    public Object doWork(Continuation<? super ListenableWorker.Result> continuation) {
        AnonymousClass1 anonymousClass1;
        String segmentId;
        Object segment;
        PocketAssistantApp app;
        ListenableWorker.Result resultFailure;
        String segmentId2;
        Throwable t;
        PocketAssistantApp app2;
        SegmentEntity seg;
        PocketAssistantApp app3;
        String segmentId3;
        AsrStage asrStage;
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
        AnonymousClass1 anonymousClass2 = anonymousClass1;
        Object $result = anonymousClass2.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        try {
            switch (anonymousClass2.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    String segmentId4 = getInputData().getString(KEY_SEGMENT_ID);
                    if (segmentId4 == null) {
                        ListenableWorker.Result resultFailure2 = ListenableWorker.Result.failure();
                        Intrinsics.checkNotNullExpressionValue(resultFailure2, "failure(...)");
                        return resultFailure2;
                    }
                    Context applicationContext = getApplicationContext();
                    PocketAssistantApp app4 = applicationContext instanceof PocketAssistantApp ? (PocketAssistantApp) applicationContext : null;
                    if (app4 == null) {
                        ListenableWorker.Result resultFailure3 = ListenableWorker.Result.failure();
                        Intrinsics.checkNotNullExpressionValue(resultFailure3, "failure(...)");
                        return resultFailure3;
                    }
                    ForegroundInfo foregroundInfoCreateForegroundInfo = createForegroundInfo("Transcribing…");
                    anonymousClass2.L$0 = segmentId4;
                    anonymousClass2.L$1 = app4;
                    anonymousClass2.label = 1;
                    if (setForeground(foregroundInfoCreateForegroundInfo, anonymousClass2) == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    app3 = app4;
                    segmentId3 = segmentId4;
                    asrStage = app3.getContainer().getAsrStage();
                    anonymousClass2.L$0 = segmentId3;
                    anonymousClass2.L$1 = app3;
                    anonymousClass2.label = 2;
                    if (asrStage.process(segmentId3, anonymousClass2) == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    resultFailure = ListenableWorker.Result.success();
                    Intrinsics.checkNotNull(resultFailure);
                    return resultFailure;
                case 1:
                    PocketAssistantApp app5 = (PocketAssistantApp) anonymousClass2.L$1;
                    String segmentId5 = (String) anonymousClass2.L$0;
                    ResultKt.throwOnFailure($result);
                    segmentId3 = segmentId5;
                    app3 = app5;
                    asrStage = app3.getContainer().getAsrStage();
                    anonymousClass2.L$0 = segmentId3;
                    anonymousClass2.L$1 = app3;
                    anonymousClass2.label = 2;
                    if (asrStage.process(segmentId3, anonymousClass2) == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    resultFailure = ListenableWorker.Result.success();
                    Intrinsics.checkNotNull(resultFailure);
                    return resultFailure;
                case 2:
                    app3 = (PocketAssistantApp) anonymousClass2.L$1;
                    segmentId3 = (String) anonymousClass2.L$0;
                    ResultKt.throwOnFailure($result);
                    resultFailure = ListenableWorker.Result.success();
                    Intrinsics.checkNotNull(resultFailure);
                    return resultFailure;
                case 3:
                    t = (Throwable) anonymousClass2.L$2;
                    PocketAssistantApp app6 = (PocketAssistantApp) anonymousClass2.L$1;
                    segmentId = (String) anonymousClass2.L$0;
                    ResultKt.throwOnFailure($result);
                    app = app6;
                    segment = $result;
                    SegmentEntity seg2 = (SegmentEntity) segment;
                    if (seg2 == null || !Intrinsics.areEqual(seg2.getTranscriptStatus(), "FAILED")) {
                        segmentId2 = segmentId;
                    } else {
                        SessionRepository sessionRepository = app.getContainer().getSessionRepository();
                        TranscriptStatus transcriptStatus = TranscriptStatus.PENDING;
                        anonymousClass2.L$0 = SpillingKt.nullOutSpilledVariable(segmentId);
                        anonymousClass2.L$1 = SpillingKt.nullOutSpilledVariable(app);
                        anonymousClass2.L$2 = SpillingKt.nullOutSpilledVariable(t);
                        anonymousClass2.L$3 = SpillingKt.nullOutSpilledVariable(seg2);
                        anonymousClass2.label = 4;
                        String segmentId6 = segmentId;
                        if (sessionRepository.updateTranscript(segmentId6, transcriptStatus, (8060 & 4) != 0 ? null : null, (8060 & 8) != 0 ? null : null, (8060 & 16) != 0 ? null : null, (8060 & 32) != 0 ? null : null, (8060 & 64) != 0 ? null : null, (8060 & 128) != 0 ? null : null, (8060 & 256) != 0 ? null : null, (8060 & 512) != 0 ? null : null, (8060 & 1024) != 0 ? null : null, (8060 & 2048) != 0 ? false : false, (8060 & 4096) != 0 ? false : false, anonymousClass2) == coroutine_suspended) {
                            return coroutine_suspended;
                        }
                        t = t;
                        segmentId2 = segmentId6;
                        app2 = app;
                        seg = seg2;
                        t = t;
                        app = app2;
                    }
                    resultFailure = ListenableWorker.Result.retry();
                    Intrinsics.checkNotNull(resultFailure);
                    return resultFailure;
                case 4:
                    seg = (SegmentEntity) anonymousClass2.L$3;
                    t = (Throwable) anonymousClass2.L$2;
                    app2 = (PocketAssistantApp) anonymousClass2.L$1;
                    segmentId2 = (String) anonymousClass2.L$0;
                    ResultKt.throwOnFailure($result);
                    t = t;
                    app = app2;
                    resultFailure = ListenableWorker.Result.retry();
                    Intrinsics.checkNotNull(resultFailure);
                    return resultFailure;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        } catch (Throwable th) {
            t = th;
            String str = segmentId3;
            PocketAssistantApp app7 = app3;
            segmentId = str;
            Log.w(TAG, "ASR work failed segment=" + segmentId + " attempt=" + getRunAttemptCount() + ": " + t.getMessage());
            if (getRunAttemptCount() >= 2) {
                resultFailure = ListenableWorker.Result.failure();
            } else {
                SessionRepository sessionRepository2 = app7.getContainer().getSessionRepository();
                anonymousClass2.L$0 = segmentId;
                anonymousClass2.L$1 = app7;
                anonymousClass2.L$2 = SpillingKt.nullOutSpilledVariable(t);
                anonymousClass2.label = 3;
                segment = sessionRepository2.getSegment(segmentId, anonymousClass2);
                if (segment == coroutine_suspended) {
                    return coroutine_suspended;
                }
                app = app7;
            }
            Intrinsics.checkNotNull(resultFailure);
            return resultFailure;
        }
    }

    private final ForegroundInfo createForegroundInfo(String text) {
        ensureChannel();
        Notification notification = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID).setContentTitle("Pocket Assistant").setContentText(text).setSmallIcon(R.drawable.ic_launcher_foreground).setOngoing(true).setSilent(true).build();
        Intrinsics.checkNotNullExpressionValue(notification, "build(...)");
        return new ForegroundInfo(NOTIFICATION_ID, notification, 1);
    }

    private final void ensureChannel() {
        NotificationManager nm = (NotificationManager) getApplicationContext().getSystemService(NotificationManager.class);
        if (nm == null) {
            return;
        }
        nm.createNotificationChannel(new NotificationChannel(CHANNEL_ID, "Pipeline processing", 2));
    }
}
