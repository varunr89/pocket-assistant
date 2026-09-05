package com.varun.pocketassistant;

import android.app.Application;
import android.util.Log;
import androidx.core.view.MotionEventCompat;
import androidx.work.Configuration;
import androidx.work.WorkManager;
import com.varun.pocketassistant.data.AppContainer;
import com.varun.pocketassistant.data.MeetingEntity;
import com.varun.pocketassistant.data.OrphanSessionImporter;
import com.varun.pocketassistant.data.SegmentEntity;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import kotlin.Metadata;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SpillingKt;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.BuildersKt__Builders_commonKt;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.CoroutineScopeKt;
import kotlinx.coroutines.Dispatchers;
import kotlinx.coroutines.Job;
import kotlinx.coroutines.SupervisorKt;

/* JADX INFO: compiled from: PocketAssistantApp.kt */
/* JADX INFO: loaded from: classes4.dex */
@Metadata(d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u00012\u00020\u0002B\u0007¢\u0006\u0004\b\u0003\u0010\u0004J\b\u0010\u0010\u001a\u00020\u0011H\u0016R\u001e\u0010\u0007\u001a\u00020\u00062\u0006\u0010\u0005\u001a\u00020\u0006@BX\u0086.¢\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR\u000e\u0010\n\u001a\u00020\u000bX\u0082\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\f\u001a\u00020\r8VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\u000e\u0010\u000f¨\u0006\u0012"}, d2 = {"Lcom/varun/pocketassistant/PocketAssistantApp;", "Landroid/app/Application;", "Landroidx/work/Configuration$Provider;", "<init>", "()V", "value", "Lcom/varun/pocketassistant/data/AppContainer;", "container", "getContainer", "()Lcom/varun/pocketassistant/data/AppContainer;", "appScope", "Lkotlinx/coroutines/CoroutineScope;", "workManagerConfiguration", "Landroidx/work/Configuration;", "getWorkManagerConfiguration", "()Landroidx/work/Configuration;", "onCreate", "", "app_debug"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class PocketAssistantApp extends Application implements Configuration.Provider {
    public static final int $stable = 8;
    private final CoroutineScope appScope = CoroutineScopeKt.CoroutineScope(SupervisorKt.SupervisorJob$default((Job) null, 1, (Object) null).plus(Dispatchers.getIO()));
    private AppContainer container;

    public final AppContainer getContainer() {
        AppContainer appContainer = this.container;
        if (appContainer != null) {
            return appContainer;
        }
        Intrinsics.throwUninitializedPropertyAccessException("container");
        return null;
    }

    @Override // androidx.work.Configuration.Provider
    public Configuration getWorkManagerConfiguration() {
        Configuration.Builder minimumLoggingLevel = new Configuration.Builder().setMinimumLoggingLevel(4);
        ExecutorService executorServiceNewSingleThreadExecutor = Executors.newSingleThreadExecutor();
        Intrinsics.checkNotNullExpressionValue(executorServiceNewSingleThreadExecutor, "newSingleThreadExecutor(...)");
        Configuration.Builder executor = minimumLoggingLevel.setExecutor(executorServiceNewSingleThreadExecutor);
        ExecutorService executorServiceNewSingleThreadExecutor2 = Executors.newSingleThreadExecutor();
        Intrinsics.checkNotNullExpressionValue(executorServiceNewSingleThreadExecutor2, "newSingleThreadExecutor(...)");
        return executor.setTaskExecutor(executorServiceNewSingleThreadExecutor2).setMaxSchedulerLimit(20).build();
    }

    @Override // android.app.Application
    public void onCreate() {
        super.onCreate();
        WorkManager.INSTANCE.initialize(this, getWorkManagerConfiguration());
        this.container = new AppContainer(this);
        BuildersKt__Builders_commonKt.launch$default(this.appScope, null, null, new AnonymousClass1(null), 3, null);
    }

    /* JADX INFO: renamed from: com.varun.pocketassistant.PocketAssistantApp$onCreate$1, reason: invalid class name */
    /* JADX INFO: compiled from: PocketAssistantApp.kt */
    @Metadata(d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H\n"}, d2 = {"<anonymous>", "", "Lkotlinx/coroutines/CoroutineScope;"}, k = 3, mv = {2, 2, 0}, xi = 48)
    @DebugMetadata(c = "com.varun.pocketassistant.PocketAssistantApp$onCreate$1", f = "PocketAssistantApp.kt", i = {2, 3}, l = {MotionEventCompat.AXIS_GENERIC_14, 53, 55, 56}, m = "invokeSuspend", n = {"pendingAsr", "pendingAsr"}, s = {"L$0", "L$0"})
    static final class AnonymousClass1 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
        Object L$0;
        Object L$1;
        int label;

        AnonymousClass1(Continuation<? super AnonymousClass1> continuation) {
            super(2, continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
            return PocketAssistantApp.this.new AnonymousClass1(continuation);
        }

        @Override // kotlin.jvm.functions.Function2
        public final Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
            return ((AnonymousClass1) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
        }

        /* JADX WARN: Code duplicated, block: B:25:0x008f A[Catch: all -> 0x003e, TRY_LEAVE, TryCatch #1 {all -> 0x003e, blocks: (B:15:0x0039, B:23:0x0086, B:25:0x008f, B:20:0x0044), top: B:52:0x000c }] */
        /* JADX WARN: Code duplicated, block: B:31:0x00c8 A[RETURN] */
        /* JADX WARN: Code duplicated, block: B:35:0x00e3 A[Catch: all -> 0x0036, LOOP:1: B:33:0x00dd->B:35:0x00e3, LOOP_END, TryCatch #0 {all -> 0x0036, blocks: (B:7:0x001f, B:43:0x0145, B:44:0x0159, B:46:0x015f, B:47:0x016f, B:10:0x0029, B:39:0x0122, B:11:0x0030, B:32:0x00c9, B:33:0x00dd, B:35:0x00e3, B:36:0x00f3, B:29:0x00b2), top: B:52:0x000c }] */
        /* JADX WARN: Code duplicated, block: B:38:0x0121 A[RETURN] */
        /* JADX WARN: Code duplicated, block: B:41:0x0142 A[RETURN] */
        /* JADX WARN: Code duplicated, block: B:42:0x0143  */
        /* JADX WARN: Code duplicated, block: B:46:0x015f A[Catch: all -> 0x0036, LOOP:0: B:44:0x0159->B:46:0x015f, LOOP_END, TryCatch #0 {all -> 0x0036, blocks: (B:7:0x001f, B:43:0x0145, B:44:0x0159, B:46:0x015f, B:47:0x016f, B:10:0x0029, B:39:0x0122, B:11:0x0030, B:32:0x00c9, B:33:0x00dd, B:35:0x00e3, B:36:0x00f3, B:29:0x00b2), top: B:52:0x000c }] */
        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object $result) {
            Object pendingWork;
            Collection arrayList;
            Iterator it;
            List<String> list;
            Object objRecoverStuckCleaning;
            Collection collection;
            Object pendingCleanup;
            Collection collection2;
            Collection arrayList2;
            Iterator it2;
            Object objImportMissing;
            int imported;
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            try {
                try {
                    switch (this.label) {
                        case 0:
                            ResultKt.throwOnFailure($result);
                            this.label = 1;
                            objImportMissing = new OrphanSessionImporter(PocketAssistantApp.this.getContainer().getAudioStorage(), PocketAssistantApp.this.getContainer().getDatabase().sessionDao(), PocketAssistantApp.this.getContainer().getDatabase().segmentDao(), PocketAssistantApp.this.getContainer().getSessionRepository()).importMissing(this);
                            if (objImportMissing == coroutine_suspended) {
                                return coroutine_suspended;
                            }
                            imported = ((Number) objImportMissing).intValue();
                            if (imported > 0) {
                                Log.i("PocketAssistantApp", "Recovered " + imported + " orphan segment(s) for transcription");
                                break;
                            }
                            this.label = 2;
                            pendingWork = PocketAssistantApp.this.getContainer().getSessionRepository().getPendingWork(50, this);
                            if (pendingWork == coroutine_suspended) {
                                return coroutine_suspended;
                            }
                            Iterable iterable = (Iterable) pendingWork;
                            arrayList = new ArrayList(CollectionsKt.collectionSizeOrDefault(iterable, 10));
                            it = iterable.iterator();
                            while (it.hasNext()) {
                                arrayList.add(((SegmentEntity) it.next()).getId());
                            }
                            list = (List) arrayList;
                            PocketAssistantApp.this.getContainer().getPipelineScheduler().requeuePendingAsr(list);
                            this.L$0 = SpillingKt.nullOutSpilledVariable(list);
                            this.label = 3;
                            objRecoverStuckCleaning = PocketAssistantApp.this.getContainer().getMeetingRepository().recoverStuckCleaning(this);
                            if (objRecoverStuckCleaning == coroutine_suspended) {
                                return coroutine_suspended;
                            }
                            collection = (Collection) objRecoverStuckCleaning;
                            this.L$0 = SpillingKt.nullOutSpilledVariable(list);
                            this.L$1 = collection;
                            this.label = 4;
                            pendingCleanup = PocketAssistantApp.this.getContainer().getMeetingRepository().getPendingCleanup(50, this);
                            if (pendingCleanup == coroutine_suspended) {
                                return coroutine_suspended;
                            }
                            collection2 = collection;
                            Iterable iterable2 = (Iterable) pendingCleanup;
                            arrayList2 = new ArrayList(CollectionsKt.collectionSizeOrDefault(iterable2, 10));
                            it2 = iterable2.iterator();
                            while (it2.hasNext()) {
                                arrayList2.add(((MeetingEntity) it2.next()).getId());
                            }
                            List pendingMeetings = CollectionsKt.plus(collection2, arrayList2);
                            PocketAssistantApp.this.getContainer().getPipelineScheduler().requeuePendingMeetings(CollectionsKt.distinct(pendingMeetings));
                            return Unit.INSTANCE;
                        case 1:
                            ResultKt.throwOnFailure($result);
                            objImportMissing = $result;
                            imported = ((Number) objImportMissing).intValue();
                            if (imported > 0) {
                                Log.i("PocketAssistantApp", "Recovered " + imported + " orphan segment(s) for transcription");
                                break;
                            }
                            this.label = 2;
                            pendingWork = PocketAssistantApp.this.getContainer().getSessionRepository().getPendingWork(50, this);
                            if (pendingWork == coroutine_suspended) {
                                return coroutine_suspended;
                            }
                            Iterable iterable3 = (Iterable) pendingWork;
                            arrayList = new ArrayList(CollectionsKt.collectionSizeOrDefault(iterable3, 10));
                            it = iterable3.iterator();
                            while (it.hasNext()) {
                                arrayList.add(((SegmentEntity) it.next()).getId());
                            }
                            list = (List) arrayList;
                            PocketAssistantApp.this.getContainer().getPipelineScheduler().requeuePendingAsr(list);
                            this.L$0 = SpillingKt.nullOutSpilledVariable(list);
                            this.label = 3;
                            objRecoverStuckCleaning = PocketAssistantApp.this.getContainer().getMeetingRepository().recoverStuckCleaning(this);
                            if (objRecoverStuckCleaning == coroutine_suspended) {
                                return coroutine_suspended;
                            }
                            collection = (Collection) objRecoverStuckCleaning;
                            this.L$0 = SpillingKt.nullOutSpilledVariable(list);
                            this.L$1 = collection;
                            this.label = 4;
                            pendingCleanup = PocketAssistantApp.this.getContainer().getMeetingRepository().getPendingCleanup(50, this);
                            if (pendingCleanup == coroutine_suspended) {
                                return coroutine_suspended;
                            }
                            collection2 = collection;
                            Iterable iterable4 = (Iterable) pendingCleanup;
                            arrayList2 = new ArrayList(CollectionsKt.collectionSizeOrDefault(iterable4, 10));
                            it2 = iterable4.iterator();
                            while (it2.hasNext()) {
                                arrayList2.add(((MeetingEntity) it2.next()).getId());
                            }
                            List pendingMeetings2 = CollectionsKt.plus(collection2, arrayList2);
                            PocketAssistantApp.this.getContainer().getPipelineScheduler().requeuePendingMeetings(CollectionsKt.distinct(pendingMeetings2));
                            return Unit.INSTANCE;
                        case 2:
                            ResultKt.throwOnFailure($result);
                            pendingWork = $result;
                            Iterable iterable5 = (Iterable) pendingWork;
                            arrayList = new ArrayList(CollectionsKt.collectionSizeOrDefault(iterable5, 10));
                            it = iterable5.iterator();
                            while (it.hasNext()) {
                                arrayList.add(((SegmentEntity) it.next()).getId());
                            }
                            list = (List) arrayList;
                            PocketAssistantApp.this.getContainer().getPipelineScheduler().requeuePendingAsr(list);
                            this.L$0 = SpillingKt.nullOutSpilledVariable(list);
                            this.label = 3;
                            objRecoverStuckCleaning = PocketAssistantApp.this.getContainer().getMeetingRepository().recoverStuckCleaning(this);
                            if (objRecoverStuckCleaning == coroutine_suspended) {
                                return coroutine_suspended;
                            }
                            collection = (Collection) objRecoverStuckCleaning;
                            this.L$0 = SpillingKt.nullOutSpilledVariable(list);
                            this.L$1 = collection;
                            this.label = 4;
                            pendingCleanup = PocketAssistantApp.this.getContainer().getMeetingRepository().getPendingCleanup(50, this);
                            if (pendingCleanup == coroutine_suspended) {
                                return coroutine_suspended;
                            }
                            collection2 = collection;
                            Iterable iterable6 = (Iterable) pendingCleanup;
                            arrayList2 = new ArrayList(CollectionsKt.collectionSizeOrDefault(iterable6, 10));
                            it2 = iterable6.iterator();
                            while (it2.hasNext()) {
                                arrayList2.add(((MeetingEntity) it2.next()).getId());
                            }
                            List pendingMeetings3 = CollectionsKt.plus(collection2, arrayList2);
                            PocketAssistantApp.this.getContainer().getPipelineScheduler().requeuePendingMeetings(CollectionsKt.distinct(pendingMeetings3));
                            return Unit.INSTANCE;
                        case 3:
                            List<String> list2 = (List) this.L$0;
                            ResultKt.throwOnFailure($result);
                            list = list2;
                            objRecoverStuckCleaning = $result;
                            collection = (Collection) objRecoverStuckCleaning;
                            this.L$0 = SpillingKt.nullOutSpilledVariable(list);
                            this.L$1 = collection;
                            this.label = 4;
                            pendingCleanup = PocketAssistantApp.this.getContainer().getMeetingRepository().getPendingCleanup(50, this);
                            if (pendingCleanup == coroutine_suspended) {
                                return coroutine_suspended;
                            }
                            collection2 = collection;
                            Iterable iterable7 = (Iterable) pendingCleanup;
                            arrayList2 = new ArrayList(CollectionsKt.collectionSizeOrDefault(iterable7, 10));
                            it2 = iterable7.iterator();
                            while (it2.hasNext()) {
                                arrayList2.add(((MeetingEntity) it2.next()).getId());
                            }
                            List pendingMeetings4 = CollectionsKt.plus(collection2, arrayList2);
                            PocketAssistantApp.this.getContainer().getPipelineScheduler().requeuePendingMeetings(CollectionsKt.distinct(pendingMeetings4));
                            return Unit.INSTANCE;
                        case 4:
                            collection2 = (Collection) this.L$1;
                            ResultKt.throwOnFailure($result);
                            pendingCleanup = $result;
                            Iterable iterable8 = (Iterable) pendingCleanup;
                            arrayList2 = new ArrayList(CollectionsKt.collectionSizeOrDefault(iterable8, 10));
                            it2 = iterable8.iterator();
                            while (it2.hasNext()) {
                                arrayList2.add(((MeetingEntity) it2.next()).getId());
                            }
                            List pendingMeetings5 = CollectionsKt.plus(collection2, arrayList2);
                            PocketAssistantApp.this.getContainer().getPipelineScheduler().requeuePendingMeetings(CollectionsKt.distinct(pendingMeetings5));
                            return Unit.INSTANCE;
                        default:
                            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                    }
                } catch (Throwable t) {
                    Log.e("PocketAssistantApp", "Pipeline requeue failed", t);
                }
            } catch (Throwable t2) {
                Log.e("PocketAssistantApp", "Orphan import failed", t2);
            }
        }
    }
}
