package com.varun.pocketassistant.pipeline;

import kotlin.Metadata;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function1;
import org.json.JSONException;

/* JADX INFO: compiled from: CloudAndLocalProviders.kt */
/* JADX INFO: loaded from: classes8.dex */
@Metadata(d1 = {"\u0000\u0006\n\u0000\n\u0002\u0010\u000e\u0010\u0000\u001a\u00020\u0001H\n"}, d2 = {"<anonymous>", ""}, k = 3, mv = {2, 2, 0}, xi = 48)
@DebugMetadata(c = "com.varun.pocketassistant.pipeline.OpenAiCompatibleClient$chat$3$1", f = "CloudAndLocalProviders.kt", i = {}, l = {148}, m = "invokeSuspend", n = {}, s = {})
final class OpenAiCompatibleClient$chat$3$1 extends SuspendLambda implements Function1<Continuation<? super String>, Object> {
    final /* synthetic */ boolean $applyReasoning;
    final /* synthetic */ String $modelId;
    final /* synthetic */ PipelineSettings $settings;
    final /* synthetic */ String $stage;
    final /* synthetic */ String $systemPrompt;
    final /* synthetic */ String $userPrompt;
    int label;
    final /* synthetic */ OpenAiCompatibleClient this$0;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    OpenAiCompatibleClient$chat$3$1(String str, String str2, String str3, OpenAiCompatibleClient openAiCompatibleClient, PipelineSettings pipelineSettings, String str4, boolean z, Continuation<? super OpenAiCompatibleClient$chat$3$1> continuation) {
        super(1, continuation);
        this.$stage = str;
        this.$modelId = str2;
        this.$userPrompt = str3;
        this.this$0 = openAiCompatibleClient;
        this.$settings = pipelineSettings;
        this.$systemPrompt = str4;
        this.$applyReasoning = z;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Continuation<Unit> create(Continuation<?> continuation) {
        return new OpenAiCompatibleClient$chat$3$1(this.$stage, this.$modelId, this.$userPrompt, this.this$0, this.$settings, this.$systemPrompt, this.$applyReasoning, continuation);
    }

    @Override // kotlin.jvm.functions.Function1
    public final Object invoke(Continuation<? super String> continuation) {
        return ((OpenAiCompatibleClient$chat$3$1) create(continuation)).invokeSuspend(Unit.INSTANCE);
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Object invokeSuspend(Object $result) throws Throwable {
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (this.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                PipelineTelemetry pipelineTelemetry = PipelineTelemetry.INSTANCE;
                String str = this.$stage;
                String str2 = this.$modelId;
                int length = this.$userPrompt.length();
                this.label = 1;
                Object objTimed = pipelineTelemetry.timed(str, "model=" + str2 + " inChars=" + length, 150000L, new AnonymousClass1(this.this$0, this.$settings, this.$modelId, this.$userPrompt, this.$systemPrompt, this.$applyReasoning, null), this);
                if (objTimed == coroutine_suspended) {
                    return coroutine_suspended;
                }
                return objTimed;
            case 1:
                ResultKt.throwOnFailure($result);
                return $result;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
    }

    /* JADX INFO: renamed from: com.varun.pocketassistant.pipeline.OpenAiCompatibleClient$chat$3$1$1, reason: invalid class name */
    /* JADX INFO: compiled from: CloudAndLocalProviders.kt */
    @Metadata(d1 = {"\u0000\u0006\n\u0000\n\u0002\u0010\u000e\u0010\u0000\u001a\u00020\u0001H\n"}, d2 = {"<anonymous>", ""}, k = 3, mv = {2, 2, 0}, xi = 48)
    @DebugMetadata(c = "com.varun.pocketassistant.pipeline.OpenAiCompatibleClient$chat$3$1$1", f = "CloudAndLocalProviders.kt", i = {}, l = {153}, m = "invokeSuspend", n = {}, s = {})
    static final class AnonymousClass1 extends SuspendLambda implements Function1<Continuation<? super String>, Object> {
        final /* synthetic */ boolean $applyReasoning;
        final /* synthetic */ String $modelId;
        final /* synthetic */ PipelineSettings $settings;
        final /* synthetic */ String $systemPrompt;
        final /* synthetic */ String $userPrompt;
        int label;
        final /* synthetic */ OpenAiCompatibleClient this$0;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass1(OpenAiCompatibleClient openAiCompatibleClient, PipelineSettings pipelineSettings, String str, String str2, String str3, boolean z, Continuation<? super AnonymousClass1> continuation) {
            super(1, continuation);
            this.this$0 = openAiCompatibleClient;
            this.$settings = pipelineSettings;
            this.$modelId = str;
            this.$userPrompt = str2;
            this.$systemPrompt = str3;
            this.$applyReasoning = z;
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Continuation<Unit> create(Continuation<?> continuation) {
            return new AnonymousClass1(this.this$0, this.$settings, this.$modelId, this.$userPrompt, this.$systemPrompt, this.$applyReasoning, continuation);
        }

        @Override // kotlin.jvm.functions.Function1
        public final Object invoke(Continuation<? super String> continuation) {
            return ((AnonymousClass1) create(continuation)).invokeSuspend(Unit.INSTANCE);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object $result) throws JSONException {
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    this.label = 1;
                    Object objChatOnce = this.this$0.chatOnce(this.$settings, this.$modelId, this.$userPrompt, this.$systemPrompt, this.$applyReasoning, this);
                    if (objChatOnce == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    return objChatOnce;
                case 1:
                    ResultKt.throwOnFailure($result);
                    return $result;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }
}
