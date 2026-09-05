package com.varun.pocketassistant.ui;

import com.varun.pocketassistant.pipeline.OpenRouterModelInfo;
import com.varun.pocketassistant.pipeline.OpenRouterModelsClient;
import java.util.List;
import kotlin.Metadata;
import kotlin.Result;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SpillingKt;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlinx.coroutines.CoroutineScope;

/* JADX INFO: compiled from: PipelineSettingsScreen.kt */
/* JADX INFO: loaded from: classes4.dex */
@Metadata(d1 = {"\u0000\u0012\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\u0018\u0002\u0010\u0000\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00030\u00020\u0001*\u00020\u0004H\n"}, d2 = {"<anonymous>", "Lkotlin/Result;", "", "Lcom/varun/pocketassistant/pipeline/OpenRouterModelInfo;", "Lkotlinx/coroutines/CoroutineScope;"}, k = 3, mv = {2, 2, 0}, xi = 48)
@DebugMetadata(c = "com.varun.pocketassistant.ui.PipelineSettingsScreenKt$PipelineSettingsScreen$refreshModels$1$sttResult$1", f = "PipelineSettingsScreen.kt", i = {0, 0, 0}, l = {106}, m = "invokeSuspend", n = {"$this$withContext", "$this$invokeSuspend_u24lambda_u240\\1", "$i$a$-runCatching-PipelineSettingsScreenKt$PipelineSettingsScreen$refreshModels$1$sttResult$1$1\\1\\106\\0"}, s = {"L$0", "L$1", "I$0"})
final class PipelineSettingsScreenKt$PipelineSettingsScreen$refreshModels$1$sttResult$1 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Result<? extends List<? extends OpenRouterModelInfo>>>, Object> {
    final /* synthetic */ String $base;
    final /* synthetic */ String $key;
    final /* synthetic */ OpenRouterModelsClient $modelsClient;
    int I$0;
    private /* synthetic */ Object L$0;
    Object L$1;
    int label;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    PipelineSettingsScreenKt$PipelineSettingsScreen$refreshModels$1$sttResult$1(OpenRouterModelsClient openRouterModelsClient, String str, String str2, Continuation<? super PipelineSettingsScreenKt$PipelineSettingsScreen$refreshModels$1$sttResult$1> continuation) {
        super(2, continuation);
        this.$modelsClient = openRouterModelsClient;
        this.$key = str;
        this.$base = str2;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
        PipelineSettingsScreenKt$PipelineSettingsScreen$refreshModels$1$sttResult$1 pipelineSettingsScreenKt$PipelineSettingsScreen$refreshModels$1$sttResult$1 = new PipelineSettingsScreenKt$PipelineSettingsScreen$refreshModels$1$sttResult$1(this.$modelsClient, this.$key, this.$base, continuation);
        pipelineSettingsScreenKt$PipelineSettingsScreen$refreshModels$1$sttResult$1.L$0 = obj;
        return pipelineSettingsScreenKt$PipelineSettingsScreen$refreshModels$1$sttResult$1;
    }

    @Override // kotlin.jvm.functions.Function2
    public /* bridge */ /* synthetic */ Object invoke(CoroutineScope coroutineScope, Continuation<? super Result<? extends List<? extends OpenRouterModelInfo>>> continuation) {
        return invoke2(coroutineScope, (Continuation<? super Result<? extends List<OpenRouterModelInfo>>>) continuation);
    }

    /* JADX INFO: renamed from: invoke, reason: avoid collision after fix types in other method */
    public final Object invoke2(CoroutineScope coroutineScope, Continuation<? super Result<? extends List<OpenRouterModelInfo>>> continuation) {
        return ((PipelineSettingsScreenKt$PipelineSettingsScreen$refreshModels$1$sttResult$1) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Object invokeSuspend(Object $result) {
        Object objM8304constructorimpl;
        Object objListTranscriptionModels;
        CoroutineScope $this$withContext = (CoroutineScope) this.L$0;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        try {
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    OpenRouterModelsClient openRouterModelsClient = this.$modelsClient;
                    String str = this.$key;
                    String str2 = this.$base;
                    Result.Companion companion = Result.INSTANCE;
                    this.L$0 = SpillingKt.nullOutSpilledVariable($this$withContext);
                    this.L$1 = SpillingKt.nullOutSpilledVariable($this$withContext);
                    this.I$0 = 0;
                    this.label = 1;
                    objListTranscriptionModels = openRouterModelsClient.listTranscriptionModels(str, str2, this);
                    if (objListTranscriptionModels == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    break;
                case 1:
                    int i = this.I$0;
                    ResultKt.throwOnFailure($result);
                    objListTranscriptionModels = $result;
                    break;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
            objM8304constructorimpl = Result.m8304constructorimpl((List) objListTranscriptionModels);
        } catch (Throwable th) {
            Result.Companion companion2 = Result.INSTANCE;
            objM8304constructorimpl = Result.m8304constructorimpl(ResultKt.createFailure(th));
        }
        return Result.m8303boximpl(objM8304constructorimpl);
    }
}
