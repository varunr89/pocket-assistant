package com.varun.pocketassistant.pipeline;

import java.io.File;
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

/* JADX INFO: compiled from: ParakeetAsrEngine.kt */
/* JADX INFO: loaded from: classes8.dex */
@Metadata(d1 = {"\u0000\u0014\n\u0000\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a\u001a\u0010\u0000\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0004H\u0086@¢\u0006\u0002\u0010\u0005¨\u0006\u0006"}, d2 = {"transcribeSuspend", "", "Lcom/varun/pocketassistant/pipeline/ParakeetAsrEngine;", "wav", "Ljava/io/File;", "(Lcom/varun/pocketassistant/pipeline/ParakeetAsrEngine;Ljava/io/File;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"}, k = 2, mv = {2, 2, 0}, xi = 48)
public final class ParakeetAsrEngineKt {

    /* JADX INFO: renamed from: com.varun.pocketassistant.pipeline.ParakeetAsrEngineKt$transcribeSuspend$2, reason: invalid class name */
    /* JADX INFO: compiled from: ParakeetAsrEngine.kt */
    @Metadata(d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u000e\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H\n"}, d2 = {"<anonymous>", "", "Lkotlinx/coroutines/CoroutineScope;"}, k = 3, mv = {2, 2, 0}, xi = 48)
    @DebugMetadata(c = "com.varun.pocketassistant.pipeline.ParakeetAsrEngineKt$transcribeSuspend$2", f = "ParakeetAsrEngine.kt", i = {}, l = {}, m = "invokeSuspend", n = {}, s = {})
    static final class AnonymousClass2 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super String>, Object> {
        final /* synthetic */ ParakeetAsrEngine $this_transcribeSuspend;
        final /* synthetic */ File $wav;
        int label;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass2(ParakeetAsrEngine parakeetAsrEngine, File file, Continuation<? super AnonymousClass2> continuation) {
            super(2, continuation);
            this.$this_transcribeSuspend = parakeetAsrEngine;
            this.$wav = file;
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
            return new AnonymousClass2(this.$this_transcribeSuspend, this.$wav, continuation);
        }

        @Override // kotlin.jvm.functions.Function2
        public final Object invoke(CoroutineScope coroutineScope, Continuation<? super String> continuation) {
            return ((AnonymousClass2) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object $result) {
            IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    return this.$this_transcribeSuspend.transcribe(this.$wav);
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }

    public static final Object transcribeSuspend(ParakeetAsrEngine $this$transcribeSuspend, File wav, Continuation<? super String> continuation) {
        return BuildersKt.withContext(Dispatchers.getDefault(), new AnonymousClass2($this$transcribeSuspend, wav, null), continuation);
    }
}
