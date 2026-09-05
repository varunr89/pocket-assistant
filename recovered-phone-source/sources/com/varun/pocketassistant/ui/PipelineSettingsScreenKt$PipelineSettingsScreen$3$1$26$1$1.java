package com.varun.pocketassistant.ui;

import androidx.compose.runtime.MutableState;
import com.varun.pocketassistant.PocketAssistantApp;
import java.util.List;
import kotlin.Metadata;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlinx.coroutines.BuildersKt;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.Dispatchers;

/* JADX INFO: compiled from: PipelineSettingsScreen.kt */
/* JADX INFO: loaded from: classes4.dex */
@Metadata(d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H\n"}, d2 = {"<anonymous>", "", "Lkotlinx/coroutines/CoroutineScope;"}, k = 3, mv = {2, 2, 0}, xi = 48)
@DebugMetadata(c = "com.varun.pocketassistant.ui.PipelineSettingsScreenKt$PipelineSettingsScreen$3$1$26$1$1", f = "PipelineSettingsScreen.kt", i = {}, l = {500}, m = "invokeSuspend", n = {}, s = {})
final class PipelineSettingsScreenKt$PipelineSettingsScreen$3$1$26$1$1 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
    final /* synthetic */ PocketAssistantApp $app;
    final /* synthetic */ MutableState<String> $status$delegate;
    int label;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    PipelineSettingsScreenKt$PipelineSettingsScreen$3$1$26$1$1(PocketAssistantApp pocketAssistantApp, MutableState<String> mutableState, Continuation<? super PipelineSettingsScreenKt$PipelineSettingsScreen$3$1$26$1$1> continuation) {
        super(2, continuation);
        this.$app = pocketAssistantApp;
        this.$status$delegate = mutableState;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
        return new PipelineSettingsScreenKt$PipelineSettingsScreen$3$1$26$1$1(this.$app, this.$status$delegate, continuation);
    }

    @Override // kotlin.jvm.functions.Function2
    public final Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
        return ((PipelineSettingsScreenKt$PipelineSettingsScreen$3$1$26$1$1) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
    }

    /* JADX INFO: renamed from: com.varun.pocketassistant.ui.PipelineSettingsScreenKt$PipelineSettingsScreen$3$1$26$1$1$1, reason: invalid class name */
    /* JADX INFO: compiled from: PipelineSettingsScreen.kt */
    @Metadata(d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H\n"}, d2 = {"<anonymous>", "", "Lkotlinx/coroutines/CoroutineScope;"}, k = 3, mv = {2, 2, 0}, xi = 48)
    @DebugMetadata(c = "com.varun.pocketassistant.ui.PipelineSettingsScreenKt$PipelineSettingsScreen$3$1$26$1$1$1", f = "PipelineSettingsScreen.kt", i = {}, l = {501}, m = "invokeSuspend", n = {}, s = {})
    static final class AnonymousClass1 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
        final /* synthetic */ PocketAssistantApp $app;
        int label;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass1(PocketAssistantApp pocketAssistantApp, Continuation<? super AnonymousClass1> continuation) {
            super(2, continuation);
            this.$app = pocketAssistantApp;
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
            return new AnonymousClass1(this.$app, continuation);
        }

        @Override // kotlin.jvm.functions.Function2
        public final Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
            return ((AnonymousClass1) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object $result) {
            Object objRequeueAllForCleanup;
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    this.label = 1;
                    objRequeueAllForCleanup = this.$app.getContainer().getMeetingRepository().requeueAllForCleanup(100, this);
                    if (objRequeueAllForCleanup == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    break;
                case 1:
                    ResultKt.throwOnFailure($result);
                    objRequeueAllForCleanup = $result;
                    break;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
            this.$app.getContainer().getPipelineScheduler().requeuePendingMeetings((List) objRequeueAllForCleanup);
            return Unit.INSTANCE;
        }
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Object invokeSuspend(Object $result) {
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (this.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                this.label = 1;
                if (BuildersKt.withContext(Dispatchers.getIO(), new AnonymousClass1(this.$app, null), this) == coroutine_suspended) {
                    return coroutine_suspended;
                }
                break;
            case 1:
                ResultKt.throwOnFailure($result);
                break;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
        this.$status$delegate.setValue("Saved settings · re-queued all meetings for cleanup/summary");
        return Unit.INSTANCE;
    }
}
