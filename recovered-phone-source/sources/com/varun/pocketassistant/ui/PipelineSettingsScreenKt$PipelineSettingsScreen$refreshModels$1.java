package com.varun.pocketassistant.ui;

import androidx.compose.runtime.MutableState;
import com.varun.pocketassistant.pipeline.OpenRouterModelInfo;
import com.varun.pocketassistant.pipeline.OpenRouterModelsClient;
import com.varun.pocketassistant.pipeline.PipelineConfig;
import com.varun.pocketassistant.pipeline.PipelineSettings;
import java.util.List;
import kotlin.Metadata;
import kotlin.Result;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.Boxing;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlinx.coroutines.BuildersKt;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.Dispatchers;
import org.apache.commons.math3.geometry.VectorFormat;

/* JADX INFO: compiled from: PipelineSettingsScreen.kt */
/* JADX INFO: loaded from: classes4.dex */
@Metadata(d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H\n"}, d2 = {"<anonymous>", "", "Lkotlinx/coroutines/CoroutineScope;"}, k = 3, mv = {2, 2, 0}, xi = 48)
@DebugMetadata(c = "com.varun.pocketassistant.ui.PipelineSettingsScreenKt$PipelineSettingsScreen$refreshModels$1", f = "PipelineSettingsScreen.kt", i = {1}, l = {105, 108}, m = "invokeSuspend", n = {"sttResult"}, s = {"L$0"})
final class PipelineSettingsScreenKt$PipelineSettingsScreen$refreshModels$1 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
    final /* synthetic */ String $base;
    final /* synthetic */ MutableState<List<OpenRouterModelInfo>> $chatModels$delegate;
    final /* synthetic */ PipelineConfig $config;
    final /* synthetic */ String $key;
    final /* synthetic */ MutableState<Boolean> $loadingModels$delegate;
    final /* synthetic */ OpenRouterModelsClient $modelsClient;
    final /* synthetic */ MutableState<PipelineSettings> $settings$delegate;
    final /* synthetic */ boolean $silent;
    final /* synthetic */ MutableState<String> $status$delegate;
    final /* synthetic */ MutableState<List<OpenRouterModelInfo>> $sttModels$delegate;
    Object L$0;
    int label;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    PipelineSettingsScreenKt$PipelineSettingsScreen$refreshModels$1(boolean z, String str, PipelineConfig pipelineConfig, MutableState<Boolean> mutableState, MutableState<String> mutableState2, OpenRouterModelsClient openRouterModelsClient, String str2, MutableState<List<OpenRouterModelInfo>> mutableState3, MutableState<List<OpenRouterModelInfo>> mutableState4, MutableState<PipelineSettings> mutableState5, Continuation<? super PipelineSettingsScreenKt$PipelineSettingsScreen$refreshModels$1> continuation) {
        super(2, continuation);
        this.$silent = z;
        this.$base = str;
        this.$config = pipelineConfig;
        this.$loadingModels$delegate = mutableState;
        this.$status$delegate = mutableState2;
        this.$modelsClient = openRouterModelsClient;
        this.$key = str2;
        this.$sttModels$delegate = mutableState3;
        this.$chatModels$delegate = mutableState4;
        this.$settings$delegate = mutableState5;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
        return new PipelineSettingsScreenKt$PipelineSettingsScreen$refreshModels$1(this.$silent, this.$base, this.$config, this.$loadingModels$delegate, this.$status$delegate, this.$modelsClient, this.$key, this.$sttModels$delegate, this.$chatModels$delegate, this.$settings$delegate, continuation);
    }

    @Override // kotlin.jvm.functions.Function2
    public final Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
        return ((PipelineSettingsScreenKt$PipelineSettingsScreen$refreshModels$1) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
    }

    /* JADX WARN: Code duplicated, block: B:16:0x007c A[RETURN] */
    /* JADX WARN: Code duplicated, block: B:17:0x007d  */
    /* JADX WARN: Code duplicated, block: B:21:0x0095  */
    /* JADX WARN: Code duplicated, block: B:25:0x00a3  */
    /* JADX WARN: Code duplicated, block: B:28:0x00bc  */
    /* JADX WARN: Code duplicated, block: B:33:0x00e3  */
    /* JADX WARN: Code duplicated, block: B:38:0x010b  */
    /* JADX WARN: Code duplicated, block: B:49:0x01d7  */
    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Object invokeSuspend(Object $result) {
        Object objWithContext;
        Object sttResult;
        Object objWithContext2;
        Object sttResult2;
        Object chatResult;
        Object objEmptyList;
        List stt;
        Object objEmptyList2;
        List chat;
        List listCreateListBuilder;
        Throwable thM8307exceptionOrNullimpl;
        Throwable thM8307exceptionOrNullimpl2;
        List errors;
        StringBuilder sb;
        String str;
        String message;
        String message2;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (this.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                PipelineSettingsScreenKt.PipelineSettingsScreen$lambda$9(this.$loadingModels$delegate, true);
                if (!this.$silent) {
                    this.$status$delegate.setValue("Fetching OpenRouter models…");
                }
                this.label = 1;
                objWithContext = BuildersKt.withContext(Dispatchers.getIO(), new PipelineSettingsScreenKt$PipelineSettingsScreen$refreshModels$1$sttResult$1(this.$modelsClient, this.$key, this.$base, null), this);
                if (objWithContext == coroutine_suspended) {
                    return coroutine_suspended;
                }
                sttResult = ((Result) objWithContext).getValue();
                this.L$0 = sttResult;
                this.label = 2;
                objWithContext2 = BuildersKt.withContext(Dispatchers.getIO(), new PipelineSettingsScreenKt$PipelineSettingsScreen$refreshModels$1$chatResult$1(this.$modelsClient, this.$key, this.$base, null), this);
                if (objWithContext2 == coroutine_suspended) {
                    return coroutine_suspended;
                }
                sttResult2 = sttResult;
                chatResult = ((Result) objWithContext2).getValue();
                PipelineSettingsScreenKt.PipelineSettingsScreen$lambda$9(this.$loadingModels$delegate, false);
                objEmptyList = CollectionsKt.emptyList();
                if (!Result.m8310isFailureimpl(sttResult2)) {
                    objEmptyList = sttResult2;
                }
                stt = (List) objEmptyList;
                objEmptyList2 = CollectionsKt.emptyList();
                if (!Result.m8310isFailureimpl(chatResult)) {
                    objEmptyList2 = chatResult;
                }
                chat = (List) objEmptyList2;
                this.$sttModels$delegate.setValue(stt);
                this.$chatModels$delegate.setValue(chat);
                listCreateListBuilder = CollectionsKt.createListBuilder();
                thM8307exceptionOrNullimpl = Result.m8307exceptionOrNullimpl(sttResult2);
                if (thM8307exceptionOrNullimpl != null && (message2 = thM8307exceptionOrNullimpl.getMessage()) != null) {
                    Boxing.boxBoolean(listCreateListBuilder.add("STT: " + message2));
                }
                thM8307exceptionOrNullimpl2 = Result.m8307exceptionOrNullimpl(chatResult);
                if (thM8307exceptionOrNullimpl2 != null && (message = thM8307exceptionOrNullimpl2.getMessage()) != null) {
                    listCreateListBuilder.add("Chat: " + message);
                }
                errors = CollectionsKt.build(listCreateListBuilder);
                if (stt.isEmpty() || !chat.isEmpty()) {
                    PipelineSettings latest = PipelineSettingsScreenKt.PipelineSettingsScreen$lambda$2(this.$settings$delegate);
                    PipelineSettings next = PipelineSettings.copy$default(latest, null, null, null, null, false, this.$base, null, PipelineSettingsScreenKt.ensureModelId(latest.getCloudAsrModel(), stt, PipelineSettings.DEFAULT_ASR_MODEL), PipelineSettingsScreenKt.ensureModelId(latest.getCloudCleanupModel(), chat, "google/gemini-2.5-flash"), PipelineSettingsScreenKt.ensureModelId(latest.getCloudSummaryModel(), chat, PipelineSettings.DEFAULT_SUMMARY_MODEL), null, false, null, false, null, null, null, null, null, 523359, null);
                    this.$config.save(next);
                    this.$settings$delegate.setValue(this.$config.load());
                    MutableState<String> mutableState = this.$status$delegate;
                    sb = new StringBuilder();
                    sb.append("Loaded " + stt.size() + " STT + " + chat.size() + " text models from OpenRouter (saved)");
                    if (!errors.isEmpty()) {
                        sb.append(" (" + CollectionsKt.joinToString$default(errors, VectorFormat.DEFAULT_SEPARATOR, null, null, 0, null, null, 62, null) + ")");
                    }
                    mutableState.setValue(sb.toString());
                    return Unit.INSTANCE;
                }
                MutableState<String> mutableState2 = this.$status$delegate;
                String str2 = (String) CollectionsKt.firstOrNull(errors);
                if (str2 == null || (str = "Model fetch failed: " + str2) == null) {
                    str = "No models returned — check your OpenRouter API key";
                }
                mutableState2.setValue(str);
                return Unit.INSTANCE;
            case 1:
                ResultKt.throwOnFailure($result);
                objWithContext = $result;
                sttResult = ((Result) objWithContext).getValue();
                this.L$0 = sttResult;
                this.label = 2;
                objWithContext2 = BuildersKt.withContext(Dispatchers.getIO(), new PipelineSettingsScreenKt$PipelineSettingsScreen$refreshModels$1$chatResult$1(this.$modelsClient, this.$key, this.$base, null), this);
                if (objWithContext2 == coroutine_suspended) {
                    return coroutine_suspended;
                }
                sttResult2 = sttResult;
                chatResult = ((Result) objWithContext2).getValue();
                PipelineSettingsScreenKt.PipelineSettingsScreen$lambda$9(this.$loadingModels$delegate, false);
                objEmptyList = CollectionsKt.emptyList();
                if (!Result.m8310isFailureimpl(sttResult2)) {
                    objEmptyList = sttResult2;
                }
                stt = (List) objEmptyList;
                objEmptyList2 = CollectionsKt.emptyList();
                if (!Result.m8310isFailureimpl(chatResult)) {
                    objEmptyList2 = chatResult;
                }
                chat = (List) objEmptyList2;
                this.$sttModels$delegate.setValue(stt);
                this.$chatModels$delegate.setValue(chat);
                listCreateListBuilder = CollectionsKt.createListBuilder();
                thM8307exceptionOrNullimpl = Result.m8307exceptionOrNullimpl(sttResult2);
                if (thM8307exceptionOrNullimpl != null) {
                    Boxing.boxBoolean(listCreateListBuilder.add("STT: " + message2));
                }
                thM8307exceptionOrNullimpl2 = Result.m8307exceptionOrNullimpl(chatResult);
                if (thM8307exceptionOrNullimpl2 != null) {
                    listCreateListBuilder.add("Chat: " + message);
                }
                errors = CollectionsKt.build(listCreateListBuilder);
                if (stt.isEmpty()) {
                    break;
                }
                PipelineSettings latest2 = PipelineSettingsScreenKt.PipelineSettingsScreen$lambda$2(this.$settings$delegate);
                PipelineSettings next2 = PipelineSettings.copy$default(latest2, null, null, null, null, false, this.$base, null, PipelineSettingsScreenKt.ensureModelId(latest2.getCloudAsrModel(), stt, PipelineSettings.DEFAULT_ASR_MODEL), PipelineSettingsScreenKt.ensureModelId(latest2.getCloudCleanupModel(), chat, "google/gemini-2.5-flash"), PipelineSettingsScreenKt.ensureModelId(latest2.getCloudSummaryModel(), chat, PipelineSettings.DEFAULT_SUMMARY_MODEL), null, false, null, false, null, null, null, null, null, 523359, null);
                this.$config.save(next2);
                this.$settings$delegate.setValue(this.$config.load());
                MutableState<String> mutableState3 = this.$status$delegate;
                sb = new StringBuilder();
                sb.append("Loaded " + stt.size() + " STT + " + chat.size() + " text models from OpenRouter (saved)");
                if (!errors.isEmpty()) {
                    sb.append(" (" + CollectionsKt.joinToString$default(errors, VectorFormat.DEFAULT_SEPARATOR, null, null, 0, null, null, 62, null) + ")");
                }
                mutableState3.setValue(sb.toString());
                return Unit.INSTANCE;
            case 2:
                sttResult2 = this.L$0;
                ResultKt.throwOnFailure($result);
                objWithContext2 = $result;
                chatResult = ((Result) objWithContext2).getValue();
                PipelineSettingsScreenKt.PipelineSettingsScreen$lambda$9(this.$loadingModels$delegate, false);
                objEmptyList = CollectionsKt.emptyList();
                if (!Result.m8310isFailureimpl(sttResult2)) {
                    objEmptyList = sttResult2;
                }
                stt = (List) objEmptyList;
                objEmptyList2 = CollectionsKt.emptyList();
                if (!Result.m8310isFailureimpl(chatResult)) {
                    objEmptyList2 = chatResult;
                }
                chat = (List) objEmptyList2;
                this.$sttModels$delegate.setValue(stt);
                this.$chatModels$delegate.setValue(chat);
                listCreateListBuilder = CollectionsKt.createListBuilder();
                thM8307exceptionOrNullimpl = Result.m8307exceptionOrNullimpl(sttResult2);
                if (thM8307exceptionOrNullimpl != null) {
                    Boxing.boxBoolean(listCreateListBuilder.add("STT: " + message2));
                }
                thM8307exceptionOrNullimpl2 = Result.m8307exceptionOrNullimpl(chatResult);
                if (thM8307exceptionOrNullimpl2 != null) {
                    listCreateListBuilder.add("Chat: " + message);
                }
                errors = CollectionsKt.build(listCreateListBuilder);
                if (stt.isEmpty()) {
                    break;
                }
                PipelineSettings latest3 = PipelineSettingsScreenKt.PipelineSettingsScreen$lambda$2(this.$settings$delegate);
                PipelineSettings next3 = PipelineSettings.copy$default(latest3, null, null, null, null, false, this.$base, null, PipelineSettingsScreenKt.ensureModelId(latest3.getCloudAsrModel(), stt, PipelineSettings.DEFAULT_ASR_MODEL), PipelineSettingsScreenKt.ensureModelId(latest3.getCloudCleanupModel(), chat, "google/gemini-2.5-flash"), PipelineSettingsScreenKt.ensureModelId(latest3.getCloudSummaryModel(), chat, PipelineSettings.DEFAULT_SUMMARY_MODEL), null, false, null, false, null, null, null, null, null, 523359, null);
                this.$config.save(next3);
                this.$settings$delegate.setValue(this.$config.load());
                MutableState<String> mutableState4 = this.$status$delegate;
                sb = new StringBuilder();
                sb.append("Loaded " + stt.size() + " STT + " + chat.size() + " text models from OpenRouter (saved)");
                if (!errors.isEmpty()) {
                    sb.append(" (" + CollectionsKt.joinToString$default(errors, VectorFormat.DEFAULT_SEPARATOR, null, null, 0, null, null, 62, null) + ")");
                }
                mutableState4.setValue(sb.toString());
                return Unit.INSTANCE;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
    }
}
