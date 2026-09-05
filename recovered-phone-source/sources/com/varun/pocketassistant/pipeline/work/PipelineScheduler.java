package com.varun.pocketassistant.pipeline.work;

import android.content.Context;
import androidx.lifecycle.LiveData;
import androidx.work.BackoffPolicy;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.ExistingWorkPolicy;
import androidx.work.ListenableWorker;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.OutOfQuotaPolicy;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import kotlin.Metadata;
import kotlin.Pair;
import kotlin.TuplesKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: PipelineScheduler.kt */
/* JADX INFO: loaded from: classes7.dex */
@Metadata(d1 = {"\u0000L\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0007\u0018\u0000 !2\u00020\u0001:\u0001!B\u000f\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0004\b\u0004\u0010\u0005J\"\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\r2\b\b\u0002\u0010\u000e\u001a\u00020\u000f2\b\b\u0002\u0010\u0010\u001a\u00020\u000fJ\"\u0010\u0011\u001a\u00020\u000b2\u0006\u0010\u0012\u001a\u00020\r2\b\b\u0002\u0010\u000e\u001a\u00020\u000f2\b\b\u0002\u0010\u0010\u001a\u00020\u000fJ\u0018\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0012\u001a\u00020\r2\u0006\u0010\u0010\u001a\u00020\u000fH\u0002J\u0010\u0010\u0015\u001a\u00020\u00142\u0006\u0010\u0012\u001a\u00020\rH\u0002J\u0018\u0010\u0016\u001a\u00020\u000b2\u0006\u0010\u0012\u001a\u00020\r2\b\b\u0002\u0010\u000e\u001a\u00020\u000fJ\u001a\u0010\u0017\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u001a0\u00190\u00182\u0006\u0010\f\u001a\u00020\rJ\u001a\u0010\u001b\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u001a0\u00190\u00182\u0006\u0010\u0012\u001a\u00020\rJ\u0014\u0010\u001c\u001a\u00020\u000b2\f\u0010\u001d\u001a\b\u0012\u0004\u0012\u00020\r0\u0019J\u0014\u0010\u001e\u001a\u00020\u000b2\f\u0010\u001d\u001a\b\u0012\u0004\u0012\u00020\r0\u0019J\b\u0010\u001f\u001a\u00020 H\u0002R\u0016\u0010\u0006\u001a\n \u0007*\u0004\u0018\u00010\u00030\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\""}, d2 = {"Lcom/varun/pocketassistant/pipeline/work/PipelineScheduler;", "", "context", "Landroid/content/Context;", "<init>", "(Landroid/content/Context;)V", "appContext", "kotlin.jvm.PlatformType", "wm", "Landroidx/work/WorkManager;", "enqueueAsr", "", AsrWorker.KEY_SEGMENT_ID, "", "replace", "", "expedited", "enqueueMeeting", MeetingStageWorker.KEY_MEETING_ID, "cleanupRequest", "Landroidx/work/OneTimeWorkRequest;", "summaryRequest", "enqueueMeetingSummary", "observeAsr", "Landroidx/lifecycle/LiveData;", "", "Landroidx/work/WorkInfo;", "observeMeeting", "requeuePendingAsr", "ids", "requeuePendingMeetings", "connectedConstraint", "Landroidx/work/Constraints;", "Companion", "app_debug"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class PipelineScheduler {
    public static final String TAG_ASR = "pipeline_asr";
    public static final String TAG_MEETING = "pipeline_meeting";
    private final Context appContext;
    private final WorkManager wm;

    /* JADX INFO: renamed from: Companion, reason: from kotlin metadata */
    public static final Companion INSTANCE = new Companion(null);
    public static final int $stable = 8;

    public PipelineScheduler(Context context) {
        Intrinsics.checkNotNullParameter(context, "context");
        this.appContext = context.getApplicationContext();
        WorkManager.Companion companion = WorkManager.INSTANCE;
        Context appContext = this.appContext;
        Intrinsics.checkNotNullExpressionValue(appContext, "appContext");
        this.wm = companion.getInstance(appContext);
    }

    public static /* synthetic */ void enqueueAsr$default(PipelineScheduler pipelineScheduler, String str, boolean z, boolean z2, int i, Object obj) {
        if ((i & 2) != 0) {
            z = false;
        }
        if ((i & 4) != 0) {
            z2 = true;
        }
        pipelineScheduler.enqueueAsr(str, z, z2);
    }

    public final void enqueueAsr(String segmentId, boolean replace, boolean expedited) {
        Intrinsics.checkNotNullParameter(segmentId, "segmentId");
        OneTimeWorkRequest.Builder builder = new OneTimeWorkRequest.Builder((Class<? extends ListenableWorker>) AsrWorker.class);
        Pair[] pairArr = {TuplesKt.to(AsrWorker.KEY_SEGMENT_ID, segmentId)};
        Data.Builder builder2 = new Data.Builder();
        for (Pair pair : pairArr) {
            builder2.put((String) pair.getFirst(), pair.getSecond());
        }
        OneTimeWorkRequest.Builder builder3 = builder.setInputData(builder2.build()).setConstraints(connectedConstraint()).setBackoffCriteria(BackoffPolicy.EXPONENTIAL, 10L, TimeUnit.SECONDS).addTag(TAG_ASR).addTag(INSTANCE.asrTag(segmentId));
        if (expedited) {
            builder3.setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST);
        }
        ExistingWorkPolicy policy = replace ? ExistingWorkPolicy.REPLACE : ExistingWorkPolicy.KEEP;
        this.wm.enqueueUniqueWork(INSTANCE.asrName(segmentId), policy, builder3.build());
    }

    public static /* synthetic */ void enqueueMeeting$default(PipelineScheduler pipelineScheduler, String str, boolean z, boolean z2, int i, Object obj) {
        if ((i & 2) != 0) {
            z = false;
        }
        if ((i & 4) != 0) {
            z2 = true;
        }
        pipelineScheduler.enqueueMeeting(str, z, z2);
    }

    public final void enqueueMeeting(String meetingId, boolean replace, boolean expedited) {
        Intrinsics.checkNotNullParameter(meetingId, "meetingId");
        ExistingWorkPolicy policy = replace ? ExistingWorkPolicy.REPLACE : ExistingWorkPolicy.KEEP;
        this.wm.beginUniqueWork(INSTANCE.meetingCleanupName(meetingId), policy, cleanupRequest(meetingId, expedited)).then(summaryRequest(meetingId)).enqueue();
    }

    private final OneTimeWorkRequest cleanupRequest(String meetingId, boolean expedited) {
        OneTimeWorkRequest.Builder builder = new OneTimeWorkRequest.Builder((Class<? extends ListenableWorker>) MeetingStageWorker.class);
        Pair[] pairArr = {TuplesKt.to(MeetingStageWorker.KEY_MEETING_ID, meetingId), TuplesKt.to(MeetingStageWorker.KEY_STAGE, MeetingStageWorker.STAGE_CLEANUP)};
        Data.Builder builder2 = new Data.Builder();
        for (Pair pair : pairArr) {
            builder2.put((String) pair.getFirst(), pair.getSecond());
        }
        OneTimeWorkRequest.Builder builderAddTag = builder.setInputData(builder2.build()).setConstraints(connectedConstraint()).setBackoffCriteria(BackoffPolicy.EXPONENTIAL, 10L, TimeUnit.SECONDS).addTag(TAG_MEETING).addTag(INSTANCE.meetingTag(meetingId));
        OneTimeWorkRequest.Builder builder3 = builderAddTag;
        if (expedited) {
            builder3.setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST);
        }
        return builderAddTag.build();
    }

    private final OneTimeWorkRequest summaryRequest(String meetingId) {
        OneTimeWorkRequest.Builder builder = new OneTimeWorkRequest.Builder((Class<? extends ListenableWorker>) MeetingStageWorker.class);
        Pair[] pairArr = {TuplesKt.to(MeetingStageWorker.KEY_MEETING_ID, meetingId), TuplesKt.to(MeetingStageWorker.KEY_STAGE, MeetingStageWorker.STAGE_SUMMARY)};
        Data.Builder builder2 = new Data.Builder();
        for (Pair pair : pairArr) {
            builder2.put((String) pair.getFirst(), pair.getSecond());
        }
        return builder.setInputData(builder2.build()).setConstraints(connectedConstraint()).setBackoffCriteria(BackoffPolicy.EXPONENTIAL, 10L, TimeUnit.SECONDS).addTag(TAG_MEETING).addTag(INSTANCE.meetingTag(meetingId)).build();
    }

    public static /* synthetic */ void enqueueMeetingSummary$default(PipelineScheduler pipelineScheduler, String str, boolean z, int i, Object obj) {
        if ((i & 2) != 0) {
            z = false;
        }
        pipelineScheduler.enqueueMeetingSummary(str, z);
    }

    public final void enqueueMeetingSummary(String meetingId, boolean replace) {
        Intrinsics.checkNotNullParameter(meetingId, "meetingId");
        OneTimeWorkRequest.Builder builder = new OneTimeWorkRequest.Builder((Class<? extends ListenableWorker>) MeetingStageWorker.class);
        Pair[] pairArr = {TuplesKt.to(MeetingStageWorker.KEY_MEETING_ID, meetingId), TuplesKt.to(MeetingStageWorker.KEY_STAGE, MeetingStageWorker.STAGE_SUMMARY)};
        Data.Builder builder2 = new Data.Builder();
        for (Pair pair : pairArr) {
            builder2.put((String) pair.getFirst(), pair.getSecond());
        }
        OneTimeWorkRequest summary = builder.setInputData(builder2.build()).setConstraints(connectedConstraint()).setBackoffCriteria(BackoffPolicy.EXPONENTIAL, 10L, TimeUnit.SECONDS).setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST).addTag(TAG_MEETING).addTag(INSTANCE.meetingTag(meetingId)).build();
        ExistingWorkPolicy policy = replace ? ExistingWorkPolicy.REPLACE : ExistingWorkPolicy.KEEP;
        this.wm.enqueueUniqueWork(INSTANCE.meetingSummaryName(meetingId), policy, summary);
    }

    public final LiveData<List<WorkInfo>> observeAsr(String segmentId) {
        Intrinsics.checkNotNullParameter(segmentId, "segmentId");
        return this.wm.getWorkInfosForUniqueWorkLiveData(INSTANCE.asrName(segmentId));
    }

    public final LiveData<List<WorkInfo>> observeMeeting(String meetingId) {
        Intrinsics.checkNotNullParameter(meetingId, "meetingId");
        return this.wm.getWorkInfosByTagLiveData(INSTANCE.meetingTag(meetingId));
    }

    public final void requeuePendingAsr(List<String> ids) {
        Intrinsics.checkNotNullParameter(ids, "ids");
        Iterator it = ids.iterator();
        while (it.hasNext()) {
            enqueueAsr$default(this, (String) it.next(), false, false, 2, null);
        }
    }

    public final void requeuePendingMeetings(List<String> ids) {
        Intrinsics.checkNotNullParameter(ids, "ids");
        Iterator it = ids.iterator();
        while (it.hasNext()) {
            enqueueMeeting$default(this, (String) it.next(), false, false, 2, null);
        }
    }

    private final Constraints connectedConstraint() {
        return new Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build();
    }

    /* JADX INFO: compiled from: PipelineScheduler.kt */
    @Metadata(d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\t\b\u0086\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003J\u000e\u0010\u0007\u001a\u00020\u00052\u0006\u0010\b\u001a\u00020\u0005J\u000e\u0010\t\u001a\u00020\u00052\u0006\u0010\b\u001a\u00020\u0005J\u000e\u0010\n\u001a\u00020\u00052\u0006\u0010\u000b\u001a\u00020\u0005J\u000e\u0010\f\u001a\u00020\u00052\u0006\u0010\u000b\u001a\u00020\u0005J\u000e\u0010\r\u001a\u00020\u00052\u0006\u0010\u000b\u001a\u00020\u0005R\u000e\u0010\u0004\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000¨\u0006\u000e"}, d2 = {"Lcom/varun/pocketassistant/pipeline/work/PipelineScheduler$Companion;", "", "<init>", "()V", "TAG_ASR", "", "TAG_MEETING", "asrName", AsrWorker.KEY_SEGMENT_ID, "asrTag", "meetingCleanupName", MeetingStageWorker.KEY_MEETING_ID, "meetingSummaryName", "meetingTag", "app_debug"}, k = 1, mv = {2, 2, 0}, xi = 48)
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        public final String asrName(String segmentId) {
            Intrinsics.checkNotNullParameter(segmentId, "segmentId");
            return "asr-" + segmentId;
        }

        public final String asrTag(String segmentId) {
            Intrinsics.checkNotNullParameter(segmentId, "segmentId");
            return "asr-tag-" + segmentId;
        }

        public final String meetingCleanupName(String meetingId) {
            Intrinsics.checkNotNullParameter(meetingId, "meetingId");
            return "meeting-cleanup-" + meetingId;
        }

        public final String meetingSummaryName(String meetingId) {
            Intrinsics.checkNotNullParameter(meetingId, "meetingId");
            return "meeting-summary-" + meetingId;
        }

        public final String meetingTag(String meetingId) {
            Intrinsics.checkNotNullParameter(meetingId, "meetingId");
            return "meeting-tag-" + meetingId;
        }
    }
}
