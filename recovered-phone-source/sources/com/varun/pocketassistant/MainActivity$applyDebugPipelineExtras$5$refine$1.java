package com.varun.pocketassistant;

import com.varun.pocketassistant.meeting.MeetingBoundaryRefiner;
import com.varun.pocketassistant.pipeline.OpenAiCompatibleClient;
import kotlin.Metadata;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SpillingKt;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function2;

/* JADX INFO: compiled from: MainActivity.kt */
/* JADX INFO: loaded from: classes4.dex */
@Metadata(d1 = {"\u0000\b\n\u0000\n\u0002\u0010\u000e\n\u0000\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0001H\n"}, d2 = {"<anonymous>", "", "prompt"}, k = 3, mv = {2, 2, 0}, xi = 48)
@DebugMetadata(c = "com.varun.pocketassistant.MainActivity$applyDebugPipelineExtras$5$refine$1", f = "MainActivity.kt", i = {0}, l = {213}, m = "invokeSuspend", n = {"prompt"}, s = {"L$0"})
final class MainActivity$applyDebugPipelineExtras$5$refine$1 extends SuspendLambda implements Function2<String, Continuation<? super String>, Object> {
    final /* synthetic */ PocketAssistantApp $app;
    /* synthetic */ Object L$0;
    int label;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    MainActivity$applyDebugPipelineExtras$5$refine$1(PocketAssistantApp pocketAssistantApp, Continuation<? super MainActivity$applyDebugPipelineExtras$5$refine$1> continuation) {
        super(2, continuation);
        this.$app = pocketAssistantApp;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
        MainActivity$applyDebugPipelineExtras$5$refine$1 mainActivity$applyDebugPipelineExtras$5$refine$1 = new MainActivity$applyDebugPipelineExtras$5$refine$1(this.$app, continuation);
        mainActivity$applyDebugPipelineExtras$5$refine$1.L$0 = obj;
        return mainActivity$applyDebugPipelineExtras$5$refine$1;
    }

    @Override // kotlin.jvm.functions.Function2
    public final Object invoke(String str, Continuation<? super String> continuation) {
        return ((MainActivity$applyDebugPipelineExtras$5$refine$1) create(str, continuation)).invokeSuspend(Unit.INSTANCE);
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Object invokeSuspend(Object $result) {
        String prompt = (String) this.L$0;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (this.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                this.L$0 = SpillingKt.nullOutSpilledVariable(prompt);
                this.label = 1;
                Object objChat$default = OpenAiCompatibleClient.chat$default(this.$app.getContainer().getOpenAiClient(), prompt, MeetingBoundaryRefiner.SYSTEM, null, false, "meeting_detect", this, 4, null);
                if (objChat$default == coroutine_suspended) {
                    return coroutine_suspended;
                }
                return objChat$default;
            case 1:
                ResultKt.throwOnFailure($result);
                return $result;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
    }
}
