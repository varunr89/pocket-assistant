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
import com.varun.pocketassistant.speech.MeetingStage;
import com.varun.pocketassistant.speech.MeetingStageResult;
import kotlin.Metadata;
import kotlin.NoWhenBranchMatchedException;
import kotlin.ResultKt;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.ContinuationImpl;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SpillingKt;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: MeetingStageWorker.kt */
/* JADX INFO: loaded from: classes7.dex */
@Metadata(d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\b\u0007\u0018\u0000 \u00112\u00020\u0001:\u0001\u0011B\u0017\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0004\b\u0006\u0010\u0007J\u000e\u0010\b\u001a\u00020\tH\u0096@¢\u0006\u0002\u0010\nJ\u0010\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000eH\u0002J\b\u0010\u000f\u001a\u00020\u0010H\u0002¨\u0006\u0012"}, d2 = {"Lcom/varun/pocketassistant/pipeline/work/MeetingStageWorker;", "Landroidx/work/CoroutineWorker;", "appContext", "Landroid/content/Context;", "params", "Landroidx/work/WorkerParameters;", "<init>", "(Landroid/content/Context;Landroidx/work/WorkerParameters;)V", "doWork", "Landroidx/work/ListenableWorker$Result;", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "createForegroundInfo", "Landroidx/work/ForegroundInfo;", "text", "", "ensureChannel", "", "Companion", "app_debug"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class MeetingStageWorker extends CoroutineWorker {
    private static final String CHANNEL_ID = "pipeline_processing";
    public static final String KEY_MEETING_ID = "meetingId";
    public static final String KEY_STAGE = "stage";
    private static final int MAX_ATTEMPTS = 3;
    private static final int NOTIFICATION_ID = 42002;
    public static final String STAGE_CLEANUP = "cleanup";
    public static final String STAGE_SUMMARY = "summary";
    private static final String TAG = "MeetingStageWorker";
    public static final int $stable = 8;

    /* JADX INFO: renamed from: com.varun.pocketassistant.pipeline.work.MeetingStageWorker$doWork$1, reason: invalid class name */
    /* JADX INFO: compiled from: MeetingStageWorker.kt */
    @Metadata(k = 3, mv = {2, 2, 0}, xi = 48)
    @DebugMetadata(c = "com.varun.pocketassistant.pipeline.work.MeetingStageWorker", f = "MeetingStageWorker.kt", i = {0, 0, 0, 0, 1, 1, 1, 1, 2, 2, 2, 2}, l = {MotionEventCompat.AXIS_RELATIVE_Y, 31, 32}, m = "doWork", n = {MeetingStageWorker.KEY_MEETING_ID, MeetingStageWorker.KEY_STAGE, "app", "label", MeetingStageWorker.KEY_MEETING_ID, MeetingStageWorker.KEY_STAGE, "app", "label", MeetingStageWorker.KEY_MEETING_ID, MeetingStageWorker.KEY_STAGE, "app", "label"}, s = {"L$0", "L$1", "L$2", "L$3", "L$0", "L$1", "L$2", "L$3", "L$0", "L$1", "L$2", "L$3"})
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
            return MeetingStageWorker.this.doWork(this);
        }
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public MeetingStageWorker(Context appContext, WorkerParameters params) {
        super(appContext, params);
        Intrinsics.checkNotNullParameter(appContext, "appContext");
        Intrinsics.checkNotNullParameter(params, "params");
    }

    /* JADX WARN: Code duplicated, block: B:39:0x00de  */
    /* JADX WARN: Code duplicated, block: B:41:0x00fe A[RETURN] */
    /* JADX WARN: Code duplicated, block: B:42:0x00ff  */
    /* JADX WARN: Code duplicated, block: B:44:0x0105  */
    /* JADX WARN: Code duplicated, block: B:46:0x0126 A[RETURN] */
    /* JADX WARN: Code duplicated, block: B:47:0x0127  */
    /* JADX WARN: Code duplicated, block: B:51:0x0132  */
    /* JADX WARN: Code duplicated, block: B:52:0x013d  */
    /* JADX WARN: Code duplicated, block: B:54:0x0145  */
    /* JADX WARN: Code duplicated, block: B:55:0x0168  */
    /* JADX WARN: Code duplicated, block: B:57:0x016c  */
    /* JADX WARN: Code duplicated, block: B:59:0x01a4  */
    /* JADX WARN: Code duplicated, block: B:65:0x01bb  */
    /* JADX WARN: Code duplicated, block: B:7:0x0014  */
    @Override // androidx.work.CoroutineWorker
    public Object doWork(Continuation<? super ListenableWorker.Result> continuation) {
        AnonymousClass1 anonymousClass1;
        String stage;
        String meetingId;
        String meetingId2;
        PocketAssistantApp app;
        Object objRunCleanup;
        String meetingId3;
        Object objRunSummary;
        MeetingStageResult outcome;
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
                String meetingId4 = getInputData().getString(KEY_MEETING_ID);
                if (meetingId4 == null) {
                    ListenableWorker.Result resultFailure = ListenableWorker.Result.failure();
                    Intrinsics.checkNotNullExpressionValue(resultFailure, "failure(...)");
                    return resultFailure;
                }
                stage = getInputData().getString(KEY_STAGE);
                if (stage == null) {
                    stage = STAGE_CLEANUP;
                }
                Context applicationContext = getApplicationContext();
                PocketAssistantApp app2 = applicationContext instanceof PocketAssistantApp ? (PocketAssistantApp) applicationContext : null;
                if (app2 == null) {
                    ListenableWorker.Result resultFailure2 = ListenableWorker.Result.failure();
                    Intrinsics.checkNotNullExpressionValue(resultFailure2, "failure(...)");
                    return resultFailure2;
                }
                String label = Intrinsics.areEqual(stage, STAGE_SUMMARY) ? "Summarizing meeting…" : "Cleaning meeting…";
                ForegroundInfo foregroundInfoCreateForegroundInfo = createForegroundInfo(label);
                anonymousClass1.L$0 = meetingId4;
                anonymousClass1.L$1 = stage;
                anonymousClass1.L$2 = app2;
                anonymousClass1.L$3 = SpillingKt.nullOutSpilledVariable(label);
                anonymousClass1.label = 1;
                if (setForeground(foregroundInfoCreateForegroundInfo, anonymousClass1) == coroutine_suspended) {
                    return coroutine_suspended;
                }
                PocketAssistantApp pocketAssistantApp = app2;
                meetingId = meetingId4;
                meetingId2 = label;
                app = pocketAssistantApp;
                if (!Intrinsics.areEqual(stage, STAGE_SUMMARY)) {
                    MeetingStage meetingStage = app.getContainer().getMeetingStage();
                    anonymousClass1.L$0 = meetingId;
                    anonymousClass1.L$1 = stage;
                    anonymousClass1.L$2 = SpillingKt.nullOutSpilledVariable(app);
                    anonymousClass1.L$3 = SpillingKt.nullOutSpilledVariable(meetingId2);
                    anonymousClass1.label = 3;
                    objRunCleanup = meetingStage.runCleanup(meetingId, anonymousClass1);
                    if (objRunCleanup == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    meetingId3 = meetingId;
                    outcome = (MeetingStageResult) objRunCleanup;
                    if (outcome instanceof MeetingStageResult.Success) {
                        ListenableWorker.Result resultSuccess = ListenableWorker.Result.success();
                        Intrinsics.checkNotNullExpressionValue(resultSuccess, "success(...)");
                        return resultSuccess;
                    }
                    if (outcome instanceof MeetingStageResult.WaitingAsr) {
                        Log.i(TAG, "Meeting " + meetingId3 + " waiting on ASR — Result.retry()");
                        ListenableWorker.Result resultRetry = ListenableWorker.Result.retry();
                        Intrinsics.checkNotNull(resultRetry);
                        return resultRetry;
                    }
                    if (outcome instanceof MeetingStageResult.Failed) {
                        throw new NoWhenBranchMatchedException();
                    }
                    Log.w(TAG, "Meeting " + meetingId3 + " " + stage + " failed: " + ((MeetingStageResult.Failed) outcome).getMessage());
                    if (((MeetingStageResult.Failed) outcome).getRetryable()) {
                    }
                    ListenableWorker.Result resultFailure3 = ListenableWorker.Result.failure();
                    Intrinsics.checkNotNull(resultFailure3);
                    return resultFailure3;
                }
                MeetingStage meetingStage2 = app.getContainer().getMeetingStage();
                anonymousClass1.L$0 = meetingId;
                anonymousClass1.L$1 = stage;
                anonymousClass1.L$2 = SpillingKt.nullOutSpilledVariable(app);
                anonymousClass1.L$3 = SpillingKt.nullOutSpilledVariable(meetingId2);
                anonymousClass1.label = 2;
                objRunSummary = meetingStage2.runSummary(meetingId, anonymousClass1);
                if (objRunSummary == coroutine_suspended) {
                    return coroutine_suspended;
                }
                meetingId3 = meetingId;
                outcome = (MeetingStageResult) objRunSummary;
                if (outcome instanceof MeetingStageResult.Success) {
                    ListenableWorker.Result resultSuccess2 = ListenableWorker.Result.success();
                    Intrinsics.checkNotNullExpressionValue(resultSuccess2, "success(...)");
                    return resultSuccess2;
                }
                if (outcome instanceof MeetingStageResult.WaitingAsr) {
                    Log.i(TAG, "Meeting " + meetingId3 + " waiting on ASR — Result.retry()");
                    ListenableWorker.Result resultRetry2 = ListenableWorker.Result.retry();
                    Intrinsics.checkNotNull(resultRetry2);
                    return resultRetry2;
                }
                if (outcome instanceof MeetingStageResult.Failed) {
                    throw new NoWhenBranchMatchedException();
                }
                Log.w(TAG, "Meeting " + meetingId3 + " " + stage + " failed: " + ((MeetingStageResult.Failed) outcome).getMessage());
                if (((MeetingStageResult.Failed) outcome).getRetryable() || getRunAttemptCount() >= 2) {
                    ListenableWorker.Result resultFailure4 = ListenableWorker.Result.failure();
                    Intrinsics.checkNotNull(resultFailure4);
                    return resultFailure4;
                }
                ListenableWorker.Result resultRetry3 = ListenableWorker.Result.retry();
                Intrinsics.checkNotNull(resultRetry3);
                return resultRetry3;
            case 1:
                meetingId2 = (String) anonymousClass1.L$3;
                app = (PocketAssistantApp) anonymousClass1.L$2;
                stage = (String) anonymousClass1.L$1;
                meetingId = (String) anonymousClass1.L$0;
                ResultKt.throwOnFailure($result);
                if (!Intrinsics.areEqual(stage, STAGE_SUMMARY)) {
                    MeetingStage meetingStage3 = app.getContainer().getMeetingStage();
                    anonymousClass1.L$0 = meetingId;
                    anonymousClass1.L$1 = stage;
                    anonymousClass1.L$2 = SpillingKt.nullOutSpilledVariable(app);
                    anonymousClass1.L$3 = SpillingKt.nullOutSpilledVariable(meetingId2);
                    anonymousClass1.label = 2;
                    objRunSummary = meetingStage3.runSummary(meetingId, anonymousClass1);
                    if (objRunSummary == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    meetingId3 = meetingId;
                    outcome = (MeetingStageResult) objRunSummary;
                    if (outcome instanceof MeetingStageResult.Success) {
                        ListenableWorker.Result resultSuccess3 = ListenableWorker.Result.success();
                        Intrinsics.checkNotNullExpressionValue(resultSuccess3, "success(...)");
                        return resultSuccess3;
                    }
                    if (outcome instanceof MeetingStageResult.WaitingAsr) {
                        Log.i(TAG, "Meeting " + meetingId3 + " waiting on ASR — Result.retry()");
                        ListenableWorker.Result resultRetry4 = ListenableWorker.Result.retry();
                        Intrinsics.checkNotNull(resultRetry4);
                        return resultRetry4;
                    }
                    if (outcome instanceof MeetingStageResult.Failed) {
                        throw new NoWhenBranchMatchedException();
                    }
                    Log.w(TAG, "Meeting " + meetingId3 + " " + stage + " failed: " + ((MeetingStageResult.Failed) outcome).getMessage());
                    if (((MeetingStageResult.Failed) outcome).getRetryable()) {
                        break;
                    }
                    ListenableWorker.Result resultFailure5 = ListenableWorker.Result.failure();
                    Intrinsics.checkNotNull(resultFailure5);
                    return resultFailure5;
                }
                MeetingStage meetingStage4 = app.getContainer().getMeetingStage();
                anonymousClass1.L$0 = meetingId;
                anonymousClass1.L$1 = stage;
                anonymousClass1.L$2 = SpillingKt.nullOutSpilledVariable(app);
                anonymousClass1.L$3 = SpillingKt.nullOutSpilledVariable(meetingId2);
                anonymousClass1.label = 3;
                objRunCleanup = meetingStage4.runCleanup(meetingId, anonymousClass1);
                if (objRunCleanup == coroutine_suspended) {
                    return coroutine_suspended;
                }
                meetingId3 = meetingId;
                outcome = (MeetingStageResult) objRunCleanup;
                if (outcome instanceof MeetingStageResult.Success) {
                    ListenableWorker.Result resultSuccess4 = ListenableWorker.Result.success();
                    Intrinsics.checkNotNullExpressionValue(resultSuccess4, "success(...)");
                    return resultSuccess4;
                }
                if (outcome instanceof MeetingStageResult.WaitingAsr) {
                    Log.i(TAG, "Meeting " + meetingId3 + " waiting on ASR — Result.retry()");
                    ListenableWorker.Result resultRetry5 = ListenableWorker.Result.retry();
                    Intrinsics.checkNotNull(resultRetry5);
                    return resultRetry5;
                }
                if (outcome instanceof MeetingStageResult.Failed) {
                    throw new NoWhenBranchMatchedException();
                }
                Log.w(TAG, "Meeting " + meetingId3 + " " + stage + " failed: " + ((MeetingStageResult.Failed) outcome).getMessage());
                if (((MeetingStageResult.Failed) outcome).getRetryable()) {
                    break;
                }
                ListenableWorker.Result resultFailure6 = ListenableWorker.Result.failure();
                Intrinsics.checkNotNull(resultFailure6);
                return resultFailure6;
            case 2:
                String stage2 = (String) anonymousClass1.L$1;
                meetingId3 = (String) anonymousClass1.L$0;
                ResultKt.throwOnFailure($result);
                stage = stage2;
                objRunSummary = $result;
                outcome = (MeetingStageResult) objRunSummary;
                if (outcome instanceof MeetingStageResult.Success) {
                    ListenableWorker.Result resultSuccess5 = ListenableWorker.Result.success();
                    Intrinsics.checkNotNullExpressionValue(resultSuccess5, "success(...)");
                    return resultSuccess5;
                }
                if (outcome instanceof MeetingStageResult.WaitingAsr) {
                    Log.i(TAG, "Meeting " + meetingId3 + " waiting on ASR — Result.retry()");
                    ListenableWorker.Result resultRetry6 = ListenableWorker.Result.retry();
                    Intrinsics.checkNotNull(resultRetry6);
                    return resultRetry6;
                }
                if (outcome instanceof MeetingStageResult.Failed) {
                    throw new NoWhenBranchMatchedException();
                }
                Log.w(TAG, "Meeting " + meetingId3 + " " + stage + " failed: " + ((MeetingStageResult.Failed) outcome).getMessage());
                if (((MeetingStageResult.Failed) outcome).getRetryable()) {
                    break;
                }
                ListenableWorker.Result resultFailure7 = ListenableWorker.Result.failure();
                Intrinsics.checkNotNull(resultFailure7);
                return resultFailure7;
            case 3:
                String stage3 = (String) anonymousClass1.L$1;
                meetingId3 = (String) anonymousClass1.L$0;
                ResultKt.throwOnFailure($result);
                stage = stage3;
                objRunCleanup = $result;
                outcome = (MeetingStageResult) objRunCleanup;
                if (outcome instanceof MeetingStageResult.Success) {
                    ListenableWorker.Result resultSuccess6 = ListenableWorker.Result.success();
                    Intrinsics.checkNotNullExpressionValue(resultSuccess6, "success(...)");
                    return resultSuccess6;
                }
                if (outcome instanceof MeetingStageResult.WaitingAsr) {
                    Log.i(TAG, "Meeting " + meetingId3 + " waiting on ASR — Result.retry()");
                    ListenableWorker.Result resultRetry7 = ListenableWorker.Result.retry();
                    Intrinsics.checkNotNull(resultRetry7);
                    return resultRetry7;
                }
                if (outcome instanceof MeetingStageResult.Failed) {
                    throw new NoWhenBranchMatchedException();
                }
                Log.w(TAG, "Meeting " + meetingId3 + " " + stage + " failed: " + ((MeetingStageResult.Failed) outcome).getMessage());
                if (((MeetingStageResult.Failed) outcome).getRetryable()) {
                    break;
                }
                ListenableWorker.Result resultFailure8 = ListenableWorker.Result.failure();
                Intrinsics.checkNotNull(resultFailure8);
                return resultFailure8;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
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
