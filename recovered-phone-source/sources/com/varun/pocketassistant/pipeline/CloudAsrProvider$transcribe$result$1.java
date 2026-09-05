package com.varun.pocketassistant.pipeline;

import android.util.Log;
import java.io.File;
import kotlin.Metadata;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SpillingKt;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Ref;

/* JADX INFO: compiled from: CloudAndLocalProviders.kt */
/* JADX INFO: loaded from: classes8.dex */
@Metadata(d1 = {"\u0000\u0006\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001H\n"}, d2 = {"<anonymous>", "Lcom/varun/pocketassistant/pipeline/OpenAiCompatibleClient$TranscriptionResult;"}, k = 3, mv = {2, 2, 0}, xi = 48)
@DebugMetadata(c = "com.varun.pocketassistant.pipeline.CloudAsrProvider$transcribe$result$1", f = "CloudAndLocalProviders.kt", i = {1}, l = {683, 689}, m = "invokeSuspend", n = {"t"}, s = {"L$0"})
final class CloudAsrProvider$transcribe$result$1 extends SuspendLambda implements Function1<Continuation<? super OpenAiCompatibleClient.TranscriptionResult>, Object> {
    final /* synthetic */ File $chunk;
    final /* synthetic */ int $i;
    final /* synthetic */ Ref.BooleanRef $usedDiarize;
    final /* synthetic */ boolean $wantDiarize;
    Object L$0;
    int label;
    final /* synthetic */ CloudAsrProvider this$0;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    CloudAsrProvider$transcribe$result$1(CloudAsrProvider cloudAsrProvider, File file, boolean z, int i, Ref.BooleanRef booleanRef, Continuation<? super CloudAsrProvider$transcribe$result$1> continuation) {
        super(1, continuation);
        this.this$0 = cloudAsrProvider;
        this.$chunk = file;
        this.$wantDiarize = z;
        this.$i = i;
        this.$usedDiarize = booleanRef;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Continuation<Unit> create(Continuation<?> continuation) {
        return new CloudAsrProvider$transcribe$result$1(this.this$0, this.$chunk, this.$wantDiarize, this.$i, this.$usedDiarize, continuation);
    }

    @Override // kotlin.jvm.functions.Function1
    public final Object invoke(Continuation<? super OpenAiCompatibleClient.TranscriptionResult> continuation) {
        return ((CloudAsrProvider$transcribe$result$1) create(continuation)).invokeSuspend(Unit.INSTANCE);
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Object invokeSuspend(Object $result) {
        Object objTranscribeAudio;
        Object objTranscribeAudio2;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        try {
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    this.label = 1;
                    objTranscribeAudio2 = this.this$0.client.transcribeAudio(this.$chunk, this.$wantDiarize, this);
                    if (objTranscribeAudio2 == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    return (OpenAiCompatibleClient.TranscriptionResult) objTranscribeAudio2;
                case 1:
                    ResultKt.throwOnFailure($result);
                    objTranscribeAudio2 = $result;
                    return (OpenAiCompatibleClient.TranscriptionResult) objTranscribeAudio2;
                case 2:
                    ResultKt.throwOnFailure($result);
                    objTranscribeAudio = $result;
                    return (OpenAiCompatibleClient.TranscriptionResult) objTranscribeAudio;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        } catch (Throwable t) {
            if (!this.$wantDiarize) {
                throw t;
            }
            Log.w("CloudAsr", "diarize failed for chunk " + (this.$i + 1) + ", retrying plain: " + t.getMessage());
            this.$usedDiarize.element = false;
            this.L$0 = SpillingKt.nullOutSpilledVariable(t);
            this.label = 2;
            objTranscribeAudio = this.this$0.client.transcribeAudio(this.$chunk, false, this);
            if (objTranscribeAudio == coroutine_suspended) {
                return coroutine_suspended;
            }
        }
    }
}
