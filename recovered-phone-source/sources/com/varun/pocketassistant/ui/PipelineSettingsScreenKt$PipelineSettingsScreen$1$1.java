package com.varun.pocketassistant.ui;

import androidx.compose.runtime.MutableState;
import com.varun.pocketassistant.pipeline.OpenRouterModelInfo;
import com.varun.pocketassistant.pipeline.OpenRouterModelsClient;
import com.varun.pocketassistant.pipeline.PipelineConfig;
import com.varun.pocketassistant.pipeline.PipelineSettings;
import java.util.List;
import kotlin.Metadata;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlin.text.StringsKt;
import kotlinx.coroutines.CoroutineScope;

/* JADX INFO: compiled from: PipelineSettingsScreen.kt */
/* JADX INFO: loaded from: classes4.dex */
@Metadata(d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H\n"}, d2 = {"<anonymous>", "", "Lkotlinx/coroutines/CoroutineScope;"}, k = 3, mv = {2, 2, 0}, xi = 48)
@DebugMetadata(c = "com.varun.pocketassistant.ui.PipelineSettingsScreenKt$PipelineSettingsScreen$1$1", f = "PipelineSettingsScreen.kt", i = {}, l = {}, m = "invokeSuspend", n = {}, s = {})
final class PipelineSettingsScreenKt$PipelineSettingsScreen$1$1 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
    final /* synthetic */ MutableState<List<OpenRouterModelInfo>> $chatModels$delegate;
    final /* synthetic */ PipelineConfig $config;
    final /* synthetic */ MutableState<Boolean> $loadingModels$delegate;
    final /* synthetic */ OpenRouterModelsClient $modelsClient;
    final /* synthetic */ CoroutineScope $scope;
    final /* synthetic */ MutableState<PipelineSettings> $settings$delegate;
    final /* synthetic */ MutableState<String> $status$delegate;
    final /* synthetic */ MutableState<List<OpenRouterModelInfo>> $sttModels$delegate;
    int label;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    PipelineSettingsScreenKt$PipelineSettingsScreen$1$1(MutableState<PipelineSettings> mutableState, MutableState<List<OpenRouterModelInfo>> mutableState2, MutableState<List<OpenRouterModelInfo>> mutableState3, CoroutineScope coroutineScope, MutableState<String> mutableState4, PipelineConfig pipelineConfig, MutableState<Boolean> mutableState5, OpenRouterModelsClient openRouterModelsClient, Continuation<? super PipelineSettingsScreenKt$PipelineSettingsScreen$1$1> continuation) {
        super(2, continuation);
        this.$settings$delegate = mutableState;
        this.$sttModels$delegate = mutableState2;
        this.$chatModels$delegate = mutableState3;
        this.$scope = coroutineScope;
        this.$status$delegate = mutableState4;
        this.$config = pipelineConfig;
        this.$loadingModels$delegate = mutableState5;
        this.$modelsClient = openRouterModelsClient;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
        return new PipelineSettingsScreenKt$PipelineSettingsScreen$1$1(this.$settings$delegate, this.$sttModels$delegate, this.$chatModels$delegate, this.$scope, this.$status$delegate, this.$config, this.$loadingModels$delegate, this.$modelsClient, continuation);
    }

    @Override // kotlin.jvm.functions.Function2
    public final Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
        return ((PipelineSettingsScreenKt$PipelineSettingsScreen$1$1) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Object invokeSuspend(Object $result) {
        IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (this.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                if (!StringsKt.isBlank(PipelineSettingsScreenKt.PipelineSettingsScreen$lambda$2(this.$settings$delegate).getCloudApiKey()) && PipelineSettingsScreenKt.PipelineSettingsScreen$lambda$11(this.$sttModels$delegate).isEmpty() && PipelineSettingsScreenKt.PipelineSettingsScreen$lambda$14(this.$chatModels$delegate).isEmpty()) {
                    PipelineSettingsScreenKt.PipelineSettingsScreen$refreshModels(this.$scope, this.$settings$delegate, this.$status$delegate, this.$config, this.$loadingModels$delegate, this.$modelsClient, this.$sttModels$delegate, this.$chatModels$delegate, true);
                }
                return Unit.INSTANCE;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
    }
}
