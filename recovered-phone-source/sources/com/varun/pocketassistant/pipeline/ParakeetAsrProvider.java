package com.varun.pocketassistant.pipeline;

import android.content.Context;
import java.io.File;
import kotlin.Lazy;
import kotlin.LazyKt;
import kotlin.Metadata;
import kotlin.ResultKt;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.ContinuationImpl;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SpillingKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;

/* JADX INFO: compiled from: CloudAndLocalProviders.kt */
/* JADX INFO: loaded from: classes8.dex */
@Metadata(d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0007\u0018\u00002\u00020\u0001B\u000f\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0004\b\u0004\u0010\u0005J\b\u0010\u0010\u001a\u00020\u0011H\u0016J\u0016\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u0015H\u0096@¢\u0006\u0002\u0010\u0016R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u0006\u001a\u00020\u0007X\u0096D¢\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR\u001b\u0010\n\u001a\u00020\u000b8BX\u0082\u0084\u0002¢\u0006\f\n\u0004\b\u000e\u0010\u000f\u001a\u0004\b\f\u0010\r¨\u0006\u0017"}, d2 = {"Lcom/varun/pocketassistant/pipeline/ParakeetAsrProvider;", "Lcom/varun/pocketassistant/pipeline/AsrProvider;", "context", "Landroid/content/Context;", "<init>", "(Landroid/content/Context;)V", "id", "", "getId", "()Ljava/lang/String;", "engine", "Lcom/varun/pocketassistant/pipeline/ParakeetAsrEngine;", "getEngine", "()Lcom/varun/pocketassistant/pipeline/ParakeetAsrEngine;", "engine$delegate", "Lkotlin/Lazy;", "isAvailable", "", "transcribe", "Lcom/varun/pocketassistant/pipeline/AsrResult;", "wav", "Ljava/io/File;", "(Ljava/io/File;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class ParakeetAsrProvider implements AsrProvider {
    public static final int $stable = 8;
    private final Context context;

    /* JADX INFO: renamed from: engine$delegate, reason: from kotlin metadata */
    private final Lazy engine;
    private final String id;

    /* JADX INFO: renamed from: com.varun.pocketassistant.pipeline.ParakeetAsrProvider$transcribe$1, reason: invalid class name */
    /* JADX INFO: compiled from: CloudAndLocalProviders.kt */
    @Metadata(k = 3, mv = {2, 2, 0}, xi = 48)
    @DebugMetadata(c = "com.varun.pocketassistant.pipeline.ParakeetAsrProvider", f = "CloudAndLocalProviders.kt", i = {0}, l = {845}, m = "transcribe", n = {"wav"}, s = {"L$0"})
    static final class AnonymousClass1 extends ContinuationImpl {
        Object L$0;
        int label;
        /* synthetic */ Object result;

        AnonymousClass1(Continuation<? super AnonymousClass1> continuation) {
            super(continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            this.result = obj;
            this.label |= Integer.MIN_VALUE;
            return ParakeetAsrProvider.this.transcribe(null, this);
        }
    }

    public ParakeetAsrProvider(Context context) {
        Intrinsics.checkNotNullParameter(context, "context");
        this.context = context;
        this.id = "parakeet_tdt";
        this.engine = LazyKt.lazy(new Function0() { // from class: com.varun.pocketassistant.pipeline.ParakeetAsrProvider$$ExternalSyntheticLambda0
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return ParakeetAsrProvider.engine_delegate$lambda$0(this.f$0);
            }
        });
    }

    @Override // com.varun.pocketassistant.pipeline.AsrProvider
    public String getId() {
        return this.id;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final ParakeetAsrEngine engine_delegate$lambda$0(ParakeetAsrProvider this$0) {
        Context applicationContext = this$0.context.getApplicationContext();
        Intrinsics.checkNotNullExpressionValue(applicationContext, "getApplicationContext(...)");
        return new ParakeetAsrEngine(applicationContext);
    }

    private final ParakeetAsrEngine getEngine() {
        return (ParakeetAsrEngine) this.engine.getValue();
    }

    @Override // com.varun.pocketassistant.pipeline.AsrProvider
    public boolean isAvailable() {
        return false;
    }

    /* JADX WARN: Code duplicated, block: B:7:0x0014  */
    @Override // com.varun.pocketassistant.pipeline.AsrProvider
    public Object transcribe(File wav, Continuation<? super AsrResult> continuation) {
        AnonymousClass1 anonymousClass1;
        Object objTranscribeSuspend;
        if (continuation instanceof AnonymousClass1) {
            anonymousClass1 = (AnonymousClass1) continuation;
            if ((anonymousClass1.label & Integer.MIN_VALUE) != 0) {
                anonymousClass1.label -= Integer.MIN_VALUE;
            } else {
                anonymousClass1 = new AnonymousClass1(continuation);
            }
        } else {
            anonymousClass1 = new AnonymousClass1(continuation);
        }
        Object $result = anonymousClass1.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (anonymousClass1.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                ParakeetAsrEngine engine = getEngine();
                anonymousClass1.L$0 = SpillingKt.nullOutSpilledVariable(wav);
                anonymousClass1.label = 1;
                objTranscribeSuspend = ParakeetAsrEngineKt.transcribeSuspend(engine, wav, anonymousClass1);
                if (objTranscribeSuspend == coroutine_suspended) {
                    return coroutine_suspended;
                }
                break;
            case 1:
                ResultKt.throwOnFailure($result);
                objTranscribeSuspend = $result;
                break;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
        String text = (String) objTranscribeSuspend;
        if (StringsKt.isBlank(text)) {
            throw new IllegalStateException("Parakeet returned empty transcript".toString());
        }
        return new AsrResult(text, null, getEngine().getProviderLabel(), null, 8, null);
    }
}
