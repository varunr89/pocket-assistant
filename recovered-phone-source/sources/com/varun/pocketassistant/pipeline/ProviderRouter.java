package com.varun.pocketassistant.pipeline;

import android.util.Log;
import androidx.compose.runtime.ComposerKt;
import com.google.android.gms.common.internal.ImagesContract;
import com.varun.pocketassistant.pipeline.work.MeetingStageWorker;
import java.io.File;
import java.util.concurrent.CancellationException;
import kotlin.KotlinNothingValueException;
import kotlin.Metadata;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.ContinuationImpl;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SpillingKt;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import okhttp3.internal.ws.WebSocketProtocol;

/* JADX INFO: compiled from: Providers.kt */
/* JADX INFO: loaded from: classes8.dex */
@Metadata(d1 = {"\u0000^\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u000b\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\b\u0007\u0018\u0000 .2\u00020\u0001:\u0001.B;\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0005\u0012\u0006\u0010\u0007\u001a\u00020\b\u0012\u0006\u0010\t\u001a\u00020\b\u0012\n\b\u0002\u0010\n\u001a\u0004\u0018\u00010\u000b¢\u0006\u0004\b\f\u0010\rJ\u0016\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u0011H\u0086@¢\u0006\u0002\u0010\u0012J\u001e\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020\u0018H\u0086@¢\u0006\u0002\u0010\u0019J\u0016\u0010\u001a\u001a\u00020\u00142\u0006\u0010\u001b\u001a\u00020\u0016H\u0086@¢\u0006\u0002\u0010\u001cJ \u0010\u001d\u001a\u00020\u00142\u0006\u0010\u001e\u001a\u00020\u00162\b\b\u0002\u0010\u001f\u001a\u00020\u0016H\u0086@¢\u0006\u0002\u0010 Jx\u0010!\u001a\u0002H\"\"\u0004\b\u0000\u0010\"2\u0006\u0010#\u001a\u00020$2\u0006\u0010%\u001a\u00020\u00182\u0006\u0010&\u001a\u00020\u00182\u0006\u0010'\u001a\u00020\u00182\u001c\u0010(\u001a\u0018\b\u0001\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\"0*\u0012\u0006\u0012\u0004\u0018\u00010\u00010)2\u001c\u0010+\u001a\u0018\b\u0001\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\"0*\u0012\u0006\u0012\u0004\u0018\u00010\u00010)2\u0006\u0010,\u001a\u00020\u0016H\u0082@¢\u0006\u0002\u0010-R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0005X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\bX\u0082\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\n\u001a\u0004\u0018\u00010\u000bX\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006/"}, d2 = {"Lcom/varun/pocketassistant/pipeline/ProviderRouter;", "", "config", "Lcom/varun/pocketassistant/pipeline/PipelineConfig;", "localAsr", "Lcom/varun/pocketassistant/pipeline/AsrProvider;", "cloudAsr", "localText", "Lcom/varun/pocketassistant/pipeline/MeetingTextProvider;", "cloudText", "circuitBreaker", "Lcom/varun/pocketassistant/pipeline/CloudCircuitBreaker;", "<init>", "(Lcom/varun/pocketassistant/pipeline/PipelineConfig;Lcom/varun/pocketassistant/pipeline/AsrProvider;Lcom/varun/pocketassistant/pipeline/AsrProvider;Lcom/varun/pocketassistant/pipeline/MeetingTextProvider;Lcom/varun/pocketassistant/pipeline/MeetingTextProvider;Lcom/varun/pocketassistant/pipeline/CloudCircuitBreaker;)V", "transcribe", "Lcom/varun/pocketassistant/pipeline/AsrResult;", "wav", "Ljava/io/File;", "(Ljava/io/File;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "clean", "Lcom/varun/pocketassistant/pipeline/TextStageResult;", "raw", "", "diarized", "", "(Ljava/lang/String;ZLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "summarize", "cleanedTranscript", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "runActions", "prompt", "system", "(Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "route", "T", "mode", "Lcom/varun/pocketassistant/pipeline/ProviderMode;", "allowFallback", "localAvailable", "cloudAvailable", "runLocal", "Lkotlin/Function1;", "Lkotlin/coroutines/Continuation;", "runCloud", MeetingStageWorker.KEY_STAGE, "(Lcom/varun/pocketassistant/pipeline/ProviderMode;ZZZLkotlin/jvm/functions/Function1;Lkotlin/jvm/functions/Function1;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "Companion", "app_debug"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class ProviderRouter {
    private static final String TAG = "ProviderRouter";
    private final CloudCircuitBreaker circuitBreaker;
    private final AsrProvider cloudAsr;
    private final MeetingTextProvider cloudText;
    private final PipelineConfig config;
    private final AsrProvider localAsr;
    private final MeetingTextProvider localText;
    public static final int $stable = 8;

    /* JADX INFO: renamed from: com.varun.pocketassistant.pipeline.ProviderRouter$route$1, reason: invalid class name */
    /* JADX INFO: compiled from: Providers.kt */
    @Metadata(k = 3, mv = {2, 2, 0}, xi = 48)
    @DebugMetadata(c = "com.varun.pocketassistant.pipeline.ProviderRouter", f = "Providers.kt", i = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2}, l = {188, ComposerKt.referenceKey, 214}, m = "route", n = {"mode", "runLocal", "runCloud", MeetingStageWorker.KEY_STAGE, "runPrimary", "runSecondary", "primaryLabel", "secondaryLabel", "allowFallback", "localAvailable", "cloudAvailable", "primaryIsCloud", "primaryAvailable", "secondaryAvailable", "mode", "runLocal", "runCloud", MeetingStageWorker.KEY_STAGE, "runPrimary", "runSecondary", "primaryLabel", "secondaryLabel", "primaryErr", "allowFallback", "localAvailable", "cloudAvailable", "primaryIsCloud", "primaryAvailable", "secondaryAvailable", "mode", "runLocal", "runCloud", MeetingStageWorker.KEY_STAGE, "runPrimary", "runSecondary", "primaryLabel", "secondaryLabel", "allowFallback", "localAvailable", "cloudAvailable", "primaryIsCloud", "primaryAvailable", "secondaryAvailable"}, s = {"L$0", "L$1", "L$2", "L$3", "L$4", "L$5", "L$6", "L$7", "Z$0", "Z$1", "Z$2", "I$0", "Z$3", "Z$4", "L$0", "L$1", "L$2", "L$3", "L$4", "L$5", "L$6", "L$7", "L$8", "Z$0", "Z$1", "Z$2", "I$0", "Z$3", "Z$4", "L$0", "L$1", "L$2", "L$3", "L$4", "L$5", "L$6", "L$7", "Z$0", "Z$1", "Z$2", "I$0", "Z$3", "Z$4"})
    static final class AnonymousClass1<T> extends ContinuationImpl {
        int I$0;
        Object L$0;
        Object L$1;
        Object L$2;
        Object L$3;
        Object L$4;
        Object L$5;
        Object L$6;
        Object L$7;
        Object L$8;
        boolean Z$0;
        boolean Z$1;
        boolean Z$2;
        boolean Z$3;
        boolean Z$4;
        int label;
        /* synthetic */ Object result;

        AnonymousClass1(Continuation<? super AnonymousClass1> continuation) {
            super(continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            this.result = obj;
            this.label |= Integer.MIN_VALUE;
            return ProviderRouter.this.route(null, false, false, false, null, null, null, this);
        }
    }

    public ProviderRouter(PipelineConfig config, AsrProvider localAsr, AsrProvider cloudAsr, MeetingTextProvider localText, MeetingTextProvider cloudText, CloudCircuitBreaker circuitBreaker) {
        Intrinsics.checkNotNullParameter(config, "config");
        Intrinsics.checkNotNullParameter(localAsr, "localAsr");
        Intrinsics.checkNotNullParameter(cloudAsr, "cloudAsr");
        Intrinsics.checkNotNullParameter(localText, "localText");
        Intrinsics.checkNotNullParameter(cloudText, "cloudText");
        this.config = config;
        this.localAsr = localAsr;
        this.cloudAsr = cloudAsr;
        this.localText = localText;
        this.cloudText = cloudText;
        this.circuitBreaker = circuitBreaker;
    }

    /* JADX WARN: Illegal instructions before constructor call */
    public /* synthetic */ ProviderRouter(PipelineConfig pipelineConfig, AsrProvider asrProvider, AsrProvider asrProvider2, MeetingTextProvider meetingTextProvider, MeetingTextProvider meetingTextProvider2, CloudCircuitBreaker cloudCircuitBreaker, int i, DefaultConstructorMarker defaultConstructorMarker) {
        CloudCircuitBreaker cloudCircuitBreaker2;
        if ((i & 32) == 0) {
            cloudCircuitBreaker2 = cloudCircuitBreaker;
        } else {
            cloudCircuitBreaker2 = null;
        }
        this(pipelineConfig, asrProvider, asrProvider2, meetingTextProvider, meetingTextProvider2, cloudCircuitBreaker2);
    }

    public final Object transcribe(File wav, Continuation<? super AsrResult> continuation) {
        PipelineSettings settings = this.config.load();
        String detailBase = "file=" + wav.getName() + " bytes=" + wav.length();
        ProviderMode asrMode = settings.getAsrMode();
        boolean allowFallback = settings.getAllowFallback();
        boolean zIsAvailable = this.localAsr.isAvailable();
        boolean z = false;
        if (this.cloudAsr.isAvailable()) {
            CloudCircuitBreaker cloudCircuitBreaker = this.circuitBreaker;
            if (!((cloudCircuitBreaker == null || cloudCircuitBreaker.isHealthy()) ? false : true)) {
                z = true;
            }
        }
        return route(asrMode, allowFallback, zIsAvailable, z, new C06852(detailBase, this, wav, null), new C06863(wav, null), "ASR", continuation);
    }

    /* JADX INFO: renamed from: com.varun.pocketassistant.pipeline.ProviderRouter$transcribe$2, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: Providers.kt */
    @Metadata(d1 = {"\u0000\u0006\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001H\n"}, d2 = {"<anonymous>", "Lcom/varun/pocketassistant/pipeline/AsrResult;"}, k = 3, mv = {2, 2, 0}, xi = 48)
    @DebugMetadata(c = "com.varun.pocketassistant.pipeline.ProviderRouter$transcribe$2", f = "Providers.kt", i = {}, l = {59}, m = "invokeSuspend", n = {}, s = {})
    static final class C06852 extends SuspendLambda implements Function1<Continuation<? super AsrResult>, Object> {
        final /* synthetic */ String $detailBase;
        final /* synthetic */ File $wav;
        int label;
        final /* synthetic */ ProviderRouter this$0;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        C06852(String str, ProviderRouter providerRouter, File file, Continuation<? super C06852> continuation) {
            super(1, continuation);
            this.$detailBase = str;
            this.this$0 = providerRouter;
            this.$wav = file;
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Continuation<Unit> create(Continuation<?> continuation) {
            return new C06852(this.$detailBase, this.this$0, this.$wav, continuation);
        }

        @Override // kotlin.jvm.functions.Function1
        public final Object invoke(Continuation<? super AsrResult> continuation) {
            return ((C06852) create(continuation)).invokeSuspend(Unit.INSTANCE);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object $result) throws Throwable {
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    PipelineTelemetry pipelineTelemetry = PipelineTelemetry.INSTANCE;
                    String str = this.$detailBase;
                    String id = this.this$0.localAsr.getId();
                    this.label = 1;
                    Object objTimed = pipelineTelemetry.timed("ASR", str + " provider=" + id, 300000L, new AnonymousClass1(this.this$0, this.$wav, null), this);
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

        /* JADX INFO: renamed from: com.varun.pocketassistant.pipeline.ProviderRouter$transcribe$2$1, reason: invalid class name */
        /* JADX INFO: compiled from: Providers.kt */
        @Metadata(d1 = {"\u0000\u0006\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001H\n"}, d2 = {"<anonymous>", "Lcom/varun/pocketassistant/pipeline/AsrResult;"}, k = 3, mv = {2, 2, 0}, xi = 48)
        @DebugMetadata(c = "com.varun.pocketassistant.pipeline.ProviderRouter$transcribe$2$1", f = "Providers.kt", i = {}, l = {64}, m = "invokeSuspend", n = {}, s = {})
        static final class AnonymousClass1 extends SuspendLambda implements Function1<Continuation<? super AsrResult>, Object> {
            final /* synthetic */ File $wav;
            int label;
            final /* synthetic */ ProviderRouter this$0;

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            AnonymousClass1(ProviderRouter providerRouter, File file, Continuation<? super AnonymousClass1> continuation) {
                super(1, continuation);
                this.this$0 = providerRouter;
                this.$wav = file;
            }

            @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
            public final Continuation<Unit> create(Continuation<?> continuation) {
                return new AnonymousClass1(this.this$0, this.$wav, continuation);
            }

            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Continuation<? super AsrResult> continuation) {
                return ((AnonymousClass1) create(continuation)).invokeSuspend(Unit.INSTANCE);
            }

            @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
            public final Object invokeSuspend(Object $result) {
                Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                switch (this.label) {
                    case 0:
                        ResultKt.throwOnFailure($result);
                        this.label = 1;
                        Object objTranscribe = this.this$0.localAsr.transcribe(this.$wav, this);
                        if (objTranscribe == coroutine_suspended) {
                            return coroutine_suspended;
                        }
                        return objTranscribe;
                    case 1:
                        ResultKt.throwOnFailure($result);
                        return $result;
                    default:
                        throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
            }
        }
    }

    /* JADX INFO: renamed from: com.varun.pocketassistant.pipeline.ProviderRouter$transcribe$3, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: Providers.kt */
    @Metadata(d1 = {"\u0000\u0006\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001H\n"}, d2 = {"<anonymous>", "Lcom/varun/pocketassistant/pipeline/AsrResult;"}, k = 3, mv = {2, 2, 0}, xi = 48)
    @DebugMetadata(c = "com.varun.pocketassistant.pipeline.ProviderRouter$transcribe$3", f = "Providers.kt", i = {}, l = {68}, m = "invokeSuspend", n = {}, s = {})
    static final class C06863 extends SuspendLambda implements Function1<Continuation<? super AsrResult>, Object> {
        final /* synthetic */ File $wav;
        int label;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        C06863(File file, Continuation<? super C06863> continuation) {
            super(1, continuation);
            this.$wav = file;
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Continuation<Unit> create(Continuation<?> continuation) {
            return ProviderRouter.this.new C06863(this.$wav, continuation);
        }

        @Override // kotlin.jvm.functions.Function1
        public final Object invoke(Continuation<? super AsrResult> continuation) {
            return ((C06863) create(continuation)).invokeSuspend(Unit.INSTANCE);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object $result) {
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    this.label = 1;
                    Object objTranscribe = ProviderRouter.this.cloudAsr.transcribe(this.$wav, this);
                    if (objTranscribe == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    return objTranscribe;
                case 1:
                    ResultKt.throwOnFailure($result);
                    return $result;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }

    /* JADX WARN: Code duplicated, block: B:12:0x0052  */
    public final Object clean(String raw, boolean diarized, Continuation<? super TextStageResult> continuation) {
        boolean z;
        PipelineSettings settings = this.config.load();
        String detailBase = "inChars=" + raw.length() + " diarized=" + diarized;
        ProviderMode cleanupMode = settings.getCleanupMode();
        boolean allowFallback = settings.getAllowFallback();
        boolean zIsAvailable = this.localText.isAvailable();
        if (this.cloudText.isAvailable()) {
            CloudCircuitBreaker cloudCircuitBreaker = this.circuitBreaker;
            if ((cloudCircuitBreaker == null || cloudCircuitBreaker.isHealthy()) ? false : true) {
                z = false;
            } else {
                z = true;
            }
        } else {
            z = false;
        }
        return route(cleanupMode, allowFallback, zIsAvailable, z, new AnonymousClass2(detailBase, this, raw, diarized, null), new AnonymousClass3(raw, diarized, null), MeetingStageWorker.STAGE_CLEANUP, continuation);
    }

    /* JADX INFO: renamed from: com.varun.pocketassistant.pipeline.ProviderRouter$clean$2, reason: invalid class name */
    /* JADX INFO: compiled from: Providers.kt */
    @Metadata(d1 = {"\u0000\u0006\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001H\n"}, d2 = {"<anonymous>", "Lcom/varun/pocketassistant/pipeline/TextStageResult;"}, k = 3, mv = {2, 2, 0}, xi = 48)
    @DebugMetadata(c = "com.varun.pocketassistant.pipeline.ProviderRouter$clean$2", f = "Providers.kt", i = {}, l = {83}, m = "invokeSuspend", n = {}, s = {})
    static final class AnonymousClass2 extends SuspendLambda implements Function1<Continuation<? super TextStageResult>, Object> {
        final /* synthetic */ String $detailBase;
        final /* synthetic */ boolean $diarized;
        final /* synthetic */ String $raw;
        int label;
        final /* synthetic */ ProviderRouter this$0;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass2(String str, ProviderRouter providerRouter, String str2, boolean z, Continuation<? super AnonymousClass2> continuation) {
            super(1, continuation);
            this.$detailBase = str;
            this.this$0 = providerRouter;
            this.$raw = str2;
            this.$diarized = z;
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Continuation<Unit> create(Continuation<?> continuation) {
            return new AnonymousClass2(this.$detailBase, this.this$0, this.$raw, this.$diarized, continuation);
        }

        @Override // kotlin.jvm.functions.Function1
        public final Object invoke(Continuation<? super TextStageResult> continuation) {
            return ((AnonymousClass2) create(continuation)).invokeSuspend(Unit.INSTANCE);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object $result) throws Throwable {
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    PipelineTelemetry pipelineTelemetry = PipelineTelemetry.INSTANCE;
                    String str = this.$detailBase;
                    String id = this.this$0.localText.getId();
                    this.label = 1;
                    Object objTimed = pipelineTelemetry.timed(MeetingStageWorker.STAGE_CLEANUP, str + " provider=" + id, PipelineTelemetry.CHAT_TIMEOUT_MS, new AnonymousClass1(this.this$0, this.$raw, this.$diarized, null), this);
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

        /* JADX INFO: renamed from: com.varun.pocketassistant.pipeline.ProviderRouter$clean$2$1, reason: invalid class name */
        /* JADX INFO: compiled from: Providers.kt */
        @Metadata(d1 = {"\u0000\u0006\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001H\n"}, d2 = {"<anonymous>", "Lcom/varun/pocketassistant/pipeline/TextStageResult;"}, k = 3, mv = {2, 2, 0}, xi = 48)
        @DebugMetadata(c = "com.varun.pocketassistant.pipeline.ProviderRouter$clean$2$1", f = "Providers.kt", i = {}, l = {89}, m = "invokeSuspend", n = {}, s = {})
        static final class AnonymousClass1 extends SuspendLambda implements Function1<Continuation<? super TextStageResult>, Object> {
            final /* synthetic */ boolean $diarized;
            final /* synthetic */ String $raw;
            int label;
            final /* synthetic */ ProviderRouter this$0;

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            AnonymousClass1(ProviderRouter providerRouter, String str, boolean z, Continuation<? super AnonymousClass1> continuation) {
                super(1, continuation);
                this.this$0 = providerRouter;
                this.$raw = str;
                this.$diarized = z;
            }

            @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
            public final Continuation<Unit> create(Continuation<?> continuation) {
                return new AnonymousClass1(this.this$0, this.$raw, this.$diarized, continuation);
            }

            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Continuation<? super TextStageResult> continuation) {
                return ((AnonymousClass1) create(continuation)).invokeSuspend(Unit.INSTANCE);
            }

            @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
            public final Object invokeSuspend(Object $result) {
                Object objCleanTranscript;
                Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                switch (this.label) {
                    case 0:
                        ResultKt.throwOnFailure($result);
                        this.label = 1;
                        objCleanTranscript = this.this$0.localText.cleanTranscript(this.$raw, this.$diarized, this);
                        if (objCleanTranscript == coroutine_suspended) {
                            return coroutine_suspended;
                        }
                        break;
                    case 1:
                        ResultKt.throwOnFailure($result);
                        objCleanTranscript = $result;
                        break;
                    default:
                        throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
                return new TextStageResult((String) objCleanTranscript, this.this$0.localText.getId());
            }
        }
    }

    /* JADX INFO: renamed from: com.varun.pocketassistant.pipeline.ProviderRouter$clean$3, reason: invalid class name */
    /* JADX INFO: compiled from: Providers.kt */
    @Metadata(d1 = {"\u0000\u0006\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001H\n"}, d2 = {"<anonymous>", "Lcom/varun/pocketassistant/pipeline/TextStageResult;"}, k = 3, mv = {2, 2, 0}, xi = 48)
    @DebugMetadata(c = "com.varun.pocketassistant.pipeline.ProviderRouter$clean$3", f = "Providers.kt", i = {}, l = {96}, m = "invokeSuspend", n = {}, s = {})
    static final class AnonymousClass3 extends SuspendLambda implements Function1<Continuation<? super TextStageResult>, Object> {
        final /* synthetic */ boolean $diarized;
        final /* synthetic */ String $raw;
        int label;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass3(String str, boolean z, Continuation<? super AnonymousClass3> continuation) {
            super(1, continuation);
            this.$raw = str;
            this.$diarized = z;
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Continuation<Unit> create(Continuation<?> continuation) {
            return ProviderRouter.this.new AnonymousClass3(this.$raw, this.$diarized, continuation);
        }

        @Override // kotlin.jvm.functions.Function1
        public final Object invoke(Continuation<? super TextStageResult> continuation) {
            return ((AnonymousClass3) create(continuation)).invokeSuspend(Unit.INSTANCE);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object $result) {
            Object objCleanTranscript;
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    this.label = 1;
                    objCleanTranscript = ProviderRouter.this.cloudText.cleanTranscript(this.$raw, this.$diarized, this);
                    if (objCleanTranscript == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    break;
                case 1:
                    ResultKt.throwOnFailure($result);
                    objCleanTranscript = $result;
                    break;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
            return new TextStageResult((String) objCleanTranscript, ProviderRouter.this.cloudText.getId());
        }
    }

    public final Object summarize(String cleanedTranscript, Continuation<? super TextStageResult> continuation) {
        PipelineSettings settings = this.config.load();
        String detailBase = "inChars=" + cleanedTranscript.length();
        ProviderMode summaryMode = settings.getSummaryMode();
        boolean allowFallback = settings.getAllowFallback();
        boolean zIsAvailable = this.localText.isAvailable();
        boolean z = false;
        if (this.cloudText.isAvailable()) {
            CloudCircuitBreaker cloudCircuitBreaker = this.circuitBreaker;
            if (!((cloudCircuitBreaker == null || cloudCircuitBreaker.isHealthy()) ? false : true)) {
                z = true;
            }
        }
        return route(summaryMode, allowFallback, zIsAvailable, z, new C06832(detailBase, this, cleanedTranscript, null), new C06843(cleanedTranscript, null), MeetingStageWorker.STAGE_SUMMARY, continuation);
    }

    /* JADX INFO: renamed from: com.varun.pocketassistant.pipeline.ProviderRouter$summarize$2, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: Providers.kt */
    @Metadata(d1 = {"\u0000\u0006\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001H\n"}, d2 = {"<anonymous>", "Lcom/varun/pocketassistant/pipeline/TextStageResult;"}, k = 3, mv = {2, 2, 0}, xi = 48)
    @DebugMetadata(c = "com.varun.pocketassistant.pipeline.ProviderRouter$summarize$2", f = "Providers.kt", i = {}, l = {113}, m = "invokeSuspend", n = {}, s = {})
    static final class C06832 extends SuspendLambda implements Function1<Continuation<? super TextStageResult>, Object> {
        final /* synthetic */ String $cleanedTranscript;
        final /* synthetic */ String $detailBase;
        int label;
        final /* synthetic */ ProviderRouter this$0;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        C06832(String str, ProviderRouter providerRouter, String str2, Continuation<? super C06832> continuation) {
            super(1, continuation);
            this.$detailBase = str;
            this.this$0 = providerRouter;
            this.$cleanedTranscript = str2;
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Continuation<Unit> create(Continuation<?> continuation) {
            return new C06832(this.$detailBase, this.this$0, this.$cleanedTranscript, continuation);
        }

        @Override // kotlin.jvm.functions.Function1
        public final Object invoke(Continuation<? super TextStageResult> continuation) {
            return ((C06832) create(continuation)).invokeSuspend(Unit.INSTANCE);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object $result) throws Throwable {
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    PipelineTelemetry pipelineTelemetry = PipelineTelemetry.INSTANCE;
                    String str = this.$detailBase;
                    String id = this.this$0.localText.getId();
                    this.label = 1;
                    Object objTimed = pipelineTelemetry.timed(MeetingStageWorker.STAGE_SUMMARY, str + " provider=" + id, PipelineTelemetry.CHAT_TIMEOUT_MS, new AnonymousClass1(this.this$0, this.$cleanedTranscript, null), this);
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

        /* JADX INFO: renamed from: com.varun.pocketassistant.pipeline.ProviderRouter$summarize$2$1, reason: invalid class name */
        /* JADX INFO: compiled from: Providers.kt */
        @Metadata(d1 = {"\u0000\u0006\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001H\n"}, d2 = {"<anonymous>", "Lcom/varun/pocketassistant/pipeline/TextStageResult;"}, k = 3, mv = {2, 2, 0}, xi = 48)
        @DebugMetadata(c = "com.varun.pocketassistant.pipeline.ProviderRouter$summarize$2$1", f = "Providers.kt", i = {}, l = {119}, m = "invokeSuspend", n = {}, s = {})
        static final class AnonymousClass1 extends SuspendLambda implements Function1<Continuation<? super TextStageResult>, Object> {
            final /* synthetic */ String $cleanedTranscript;
            int label;
            final /* synthetic */ ProviderRouter this$0;

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            AnonymousClass1(ProviderRouter providerRouter, String str, Continuation<? super AnonymousClass1> continuation) {
                super(1, continuation);
                this.this$0 = providerRouter;
                this.$cleanedTranscript = str;
            }

            @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
            public final Continuation<Unit> create(Continuation<?> continuation) {
                return new AnonymousClass1(this.this$0, this.$cleanedTranscript, continuation);
            }

            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Continuation<? super TextStageResult> continuation) {
                return ((AnonymousClass1) create(continuation)).invokeSuspend(Unit.INSTANCE);
            }

            @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
            public final Object invokeSuspend(Object $result) {
                Object objSummarize;
                Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                switch (this.label) {
                    case 0:
                        ResultKt.throwOnFailure($result);
                        this.label = 1;
                        objSummarize = this.this$0.localText.summarize(this.$cleanedTranscript, this);
                        if (objSummarize == coroutine_suspended) {
                            return coroutine_suspended;
                        }
                        break;
                    case 1:
                        ResultKt.throwOnFailure($result);
                        objSummarize = $result;
                        break;
                    default:
                        throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
                return new TextStageResult((String) objSummarize, this.this$0.localText.getId());
            }
        }
    }

    /* JADX INFO: renamed from: com.varun.pocketassistant.pipeline.ProviderRouter$summarize$3, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: Providers.kt */
    @Metadata(d1 = {"\u0000\u0006\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001H\n"}, d2 = {"<anonymous>", "Lcom/varun/pocketassistant/pipeline/TextStageResult;"}, k = 3, mv = {2, 2, 0}, xi = 48)
    @DebugMetadata(c = "com.varun.pocketassistant.pipeline.ProviderRouter$summarize$3", f = "Providers.kt", i = {}, l = {WebSocketProtocol.PAYLOAD_SHORT}, m = "invokeSuspend", n = {}, s = {})
    static final class C06843 extends SuspendLambda implements Function1<Continuation<? super TextStageResult>, Object> {
        final /* synthetic */ String $cleanedTranscript;
        int label;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        C06843(String str, Continuation<? super C06843> continuation) {
            super(1, continuation);
            this.$cleanedTranscript = str;
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Continuation<Unit> create(Continuation<?> continuation) {
            return ProviderRouter.this.new C06843(this.$cleanedTranscript, continuation);
        }

        @Override // kotlin.jvm.functions.Function1
        public final Object invoke(Continuation<? super TextStageResult> continuation) {
            return ((C06843) create(continuation)).invokeSuspend(Unit.INSTANCE);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object $result) {
            Object objSummarize;
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    this.label = 1;
                    objSummarize = ProviderRouter.this.cloudText.summarize(this.$cleanedTranscript, this);
                    if (objSummarize == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    break;
                case 1:
                    ResultKt.throwOnFailure($result);
                    objSummarize = $result;
                    break;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
            return new TextStageResult((String) objSummarize, ProviderRouter.this.cloudText.getId());
        }
    }

    public static /* synthetic */ Object runActions$default(ProviderRouter providerRouter, String str, String str2, Continuation continuation, int i, Object obj) {
        if ((i & 2) != 0) {
            str2 = "You extract meeting action items. Output only the requested text.";
        }
        return providerRouter.runActions(str, str2, continuation);
    }

    /* JADX WARN: Code duplicated, block: B:12:0x0047  */
    public final Object runActions(String prompt, String system, Continuation<? super TextStageResult> continuation) {
        boolean z;
        PipelineSettings settings = this.config.load();
        String detailBase = "inChars=" + prompt.length();
        ProviderMode actionsMode = settings.getActionsMode();
        boolean allowFallback = settings.getAllowFallback();
        boolean zIsAvailable = this.localText.isAvailable();
        if (this.cloudText.isAvailable()) {
            CloudCircuitBreaker cloudCircuitBreaker = this.circuitBreaker;
            if ((cloudCircuitBreaker == null || cloudCircuitBreaker.isHealthy()) ? false : true) {
                z = false;
            } else {
                z = true;
            }
        } else {
            z = false;
        }
        return route(actionsMode, allowFallback, zIsAvailable, z, new C06812(detailBase, this, system, prompt, null), new C06823(system, prompt, null), "actions", continuation);
    }

    /* JADX INFO: renamed from: com.varun.pocketassistant.pipeline.ProviderRouter$runActions$2, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: Providers.kt */
    @Metadata(d1 = {"\u0000\u0006\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001H\n"}, d2 = {"<anonymous>", "Lcom/varun/pocketassistant/pipeline/TextStageResult;"}, k = 3, mv = {2, 2, 0}, xi = 48)
    @DebugMetadata(c = "com.varun.pocketassistant.pipeline.ProviderRouter$runActions$2", f = "Providers.kt", i = {}, l = {147}, m = "invokeSuspend", n = {}, s = {})
    static final class C06812 extends SuspendLambda implements Function1<Continuation<? super TextStageResult>, Object> {
        final /* synthetic */ String $detailBase;
        final /* synthetic */ String $prompt;
        final /* synthetic */ String $system;
        int label;
        final /* synthetic */ ProviderRouter this$0;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        C06812(String str, ProviderRouter providerRouter, String str2, String str3, Continuation<? super C06812> continuation) {
            super(1, continuation);
            this.$detailBase = str;
            this.this$0 = providerRouter;
            this.$system = str2;
            this.$prompt = str3;
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Continuation<Unit> create(Continuation<?> continuation) {
            return new C06812(this.$detailBase, this.this$0, this.$system, this.$prompt, continuation);
        }

        @Override // kotlin.jvm.functions.Function1
        public final Object invoke(Continuation<? super TextStageResult> continuation) {
            return ((C06812) create(continuation)).invokeSuspend(Unit.INSTANCE);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object $result) throws Throwable {
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    PipelineTelemetry pipelineTelemetry = PipelineTelemetry.INSTANCE;
                    String str = this.$detailBase;
                    String id = this.this$0.localText.getId();
                    this.label = 1;
                    Object objTimed = pipelineTelemetry.timed("actions", str + " provider=" + id, PipelineTelemetry.CHAT_TIMEOUT_MS, new AnonymousClass1(this.this$0, this.$system, this.$prompt, null), this);
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

        /* JADX INFO: renamed from: com.varun.pocketassistant.pipeline.ProviderRouter$runActions$2$1, reason: invalid class name */
        /* JADX INFO: compiled from: Providers.kt */
        @Metadata(d1 = {"\u0000\u0006\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001H\n"}, d2 = {"<anonymous>", "Lcom/varun/pocketassistant/pipeline/TextStageResult;"}, k = 3, mv = {2, 2, 0}, xi = 48)
        @DebugMetadata(c = "com.varun.pocketassistant.pipeline.ProviderRouter$runActions$2$1", f = "Providers.kt", i = {}, l = {153}, m = "invokeSuspend", n = {}, s = {})
        static final class AnonymousClass1 extends SuspendLambda implements Function1<Continuation<? super TextStageResult>, Object> {
            final /* synthetic */ String $prompt;
            final /* synthetic */ String $system;
            int label;
            final /* synthetic */ ProviderRouter this$0;

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            AnonymousClass1(ProviderRouter providerRouter, String str, String str2, Continuation<? super AnonymousClass1> continuation) {
                super(1, continuation);
                this.this$0 = providerRouter;
                this.$system = str;
                this.$prompt = str2;
            }

            @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
            public final Continuation<Unit> create(Continuation<?> continuation) {
                return new AnonymousClass1(this.this$0, this.$system, this.$prompt, continuation);
            }

            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Continuation<? super TextStageResult> continuation) {
                return ((AnonymousClass1) create(continuation)).invokeSuspend(Unit.INSTANCE);
            }

            @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
            public final Object invokeSuspend(Object $result) {
                Object objPrompt;
                Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                switch (this.label) {
                    case 0:
                        ResultKt.throwOnFailure($result);
                        this.label = 1;
                        objPrompt = this.this$0.localText.prompt(this.$system, this.$prompt, this);
                        if (objPrompt == coroutine_suspended) {
                            return coroutine_suspended;
                        }
                        break;
                    case 1:
                        ResultKt.throwOnFailure($result);
                        objPrompt = $result;
                        break;
                    default:
                        throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
                return new TextStageResult((String) objPrompt, this.this$0.localText.getId());
            }
        }
    }

    /* JADX INFO: renamed from: com.varun.pocketassistant.pipeline.ProviderRouter$runActions$3, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: Providers.kt */
    @Metadata(d1 = {"\u0000\u0006\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001H\n"}, d2 = {"<anonymous>", "Lcom/varun/pocketassistant/pipeline/TextStageResult;"}, k = 3, mv = {2, 2, 0}, xi = 48)
    @DebugMetadata(c = "com.varun.pocketassistant.pipeline.ProviderRouter$runActions$3", f = "Providers.kt", i = {}, l = {160}, m = "invokeSuspend", n = {}, s = {})
    static final class C06823 extends SuspendLambda implements Function1<Continuation<? super TextStageResult>, Object> {
        final /* synthetic */ String $prompt;
        final /* synthetic */ String $system;
        int label;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        C06823(String str, String str2, Continuation<? super C06823> continuation) {
            super(1, continuation);
            this.$system = str;
            this.$prompt = str2;
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Continuation<Unit> create(Continuation<?> continuation) {
            return ProviderRouter.this.new C06823(this.$system, this.$prompt, continuation);
        }

        @Override // kotlin.jvm.functions.Function1
        public final Object invoke(Continuation<? super TextStageResult> continuation) {
            return ((C06823) create(continuation)).invokeSuspend(Unit.INSTANCE);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object $result) {
            Object objPrompt;
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    this.label = 1;
                    objPrompt = ProviderRouter.this.cloudText.prompt(this.$system, this.$prompt, this);
                    if (objPrompt == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    break;
                case 1:
                    ResultKt.throwOnFailure($result);
                    objPrompt = $result;
                    break;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
            return new TextStageResult((String) objPrompt, ProviderRouter.this.cloudText.getId());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code duplicated, block: B:7:0x0020  */
    public final <T> Object route(ProviderMode mode, boolean allowFallback, boolean localAvailable, boolean cloudAvailable, Function1<? super Continuation<? super T>, ? extends Object> function1, Function1<? super Continuation<? super T>, ? extends Object> function2, String stage, Continuation<? super T> continuation) throws Throwable {
        AnonymousClass1 anonymousClass1;
        int i;
        boolean primaryAvailable;
        boolean secondaryAvailable;
        String primaryLabel;
        Function1<? super Continuation<? super T>, ? extends Object> function3;
        String secondaryLabel;
        Function1<? super Continuation<? super T>, ? extends Object> function4;
        ProviderMode mode2;
        Function1<? super Continuation<? super T>, ? extends Object> function5;
        Function1<? super Continuation<? super T>, ? extends Object> function6;
        boolean allowFallback2 = allowFallback;
        boolean localAvailable2 = localAvailable;
        boolean cloudAvailable2 = cloudAvailable;
        String stage2 = stage;
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
        AnonymousClass1 anonymousClass2 = anonymousClass1;
        Object $result = anonymousClass2.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (anonymousClass2.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                i = mode == ProviderMode.PREFER_CLOUD ? 1 : 0;
                primaryAvailable = i != 0 ? cloudAvailable2 : localAvailable2;
                boolean secondaryAvailable2 = i != 0 ? localAvailable2 : cloudAvailable2;
                Function1<? super Continuation<? super T>, ? extends Object> function7 = i != 0 ? function2 : function1;
                Function1<? super Continuation<? super T>, ? extends Object> function8 = i != 0 ? function1 : function2;
                String primaryLabel2 = i != 0 ? "cloud" : ImagesContract.LOCAL;
                String secondaryLabel2 = i != 0 ? ImagesContract.LOCAL : "cloud";
                if (!primaryAvailable) {
                    if (!allowFallback2 || !secondaryAvailable2) {
                        if (primaryAvailable || secondaryAvailable2) {
                            route$fail(stage2, primaryLabel2 + " unavailable (enable fallback or configure the other side)");
                            throw new KotlinNothingValueException();
                        }
                        route$fail(stage2, "no local or cloud provider available");
                        throw new KotlinNothingValueException();
                    }
                    Log.w(TAG, stage2 + " " + primaryLabel2 + " unavailable — using " + secondaryLabel2);
                    anonymousClass2.L$0 = SpillingKt.nullOutSpilledVariable(mode);
                    anonymousClass2.L$1 = SpillingKt.nullOutSpilledVariable(function1);
                    anonymousClass2.L$2 = SpillingKt.nullOutSpilledVariable(function2);
                    anonymousClass2.L$3 = SpillingKt.nullOutSpilledVariable(stage2);
                    anonymousClass2.L$4 = SpillingKt.nullOutSpilledVariable(function7);
                    anonymousClass2.L$5 = SpillingKt.nullOutSpilledVariable(function8);
                    anonymousClass2.L$6 = SpillingKt.nullOutSpilledVariable(primaryLabel2);
                    anonymousClass2.L$7 = SpillingKt.nullOutSpilledVariable(secondaryLabel2);
                    anonymousClass2.Z$0 = allowFallback2;
                    anonymousClass2.Z$1 = localAvailable2;
                    anonymousClass2.Z$2 = cloudAvailable2;
                    anonymousClass2.I$0 = i;
                    anonymousClass2.Z$3 = primaryAvailable;
                    anonymousClass2.Z$4 = secondaryAvailable2;
                    anonymousClass2.label = 3;
                    Object objInvoke = function8.invoke(anonymousClass2);
                    return objInvoke == coroutine_suspended ? coroutine_suspended : objInvoke;
                }
                try {
                    anonymousClass2.L$0 = SpillingKt.nullOutSpilledVariable(mode);
                    anonymousClass2.L$1 = SpillingKt.nullOutSpilledVariable(function1);
                    anonymousClass2.L$2 = SpillingKt.nullOutSpilledVariable(function2);
                    anonymousClass2.L$3 = stage2;
                    anonymousClass2.L$4 = SpillingKt.nullOutSpilledVariable(function7);
                    try {
                        anonymousClass2.L$5 = function8;
                        try {
                            anonymousClass2.L$6 = primaryLabel2;
                            try {
                                anonymousClass2.L$7 = secondaryLabel2;
                                anonymousClass2.Z$0 = allowFallback2;
                                anonymousClass2.Z$1 = localAvailable2;
                                anonymousClass2.Z$2 = cloudAvailable2;
                                anonymousClass2.I$0 = i;
                                anonymousClass2.Z$3 = primaryAvailable;
                                secondaryAvailable = secondaryAvailable2;
                                try {
                                    anonymousClass2.Z$4 = secondaryAvailable;
                                    anonymousClass2.label = 1;
                                    try {
                                        Object objInvoke2 = function7.invoke(anonymousClass2);
                                        return objInvoke2 == coroutine_suspended ? coroutine_suspended : objInvoke2;
                                    } catch (CancellationException ce) {
                                        throw ce;
                                    } catch (Throwable th) {
                                        primaryErr = th;
                                        primaryLabel = primaryLabel2;
                                        function3 = function8;
                                        secondaryLabel = secondaryLabel2;
                                        function4 = function7;
                                        mode2 = mode;
                                        function5 = function1;
                                        function6 = function2;
                                    }
                                } catch (CancellationException ce2) {
                                    throw ce2;
                                } catch (Throwable th2) {
                                    primaryErr = th2;
                                    primaryLabel = primaryLabel2;
                                    function3 = function8;
                                    secondaryLabel = secondaryLabel2;
                                    function4 = function7;
                                    mode2 = mode;
                                    function5 = function1;
                                    function6 = function2;
                                }
                            } catch (CancellationException ce3) {
                                throw ce3;
                            } catch (Throwable th3) {
                                primaryErr = th3;
                                secondaryAvailable = secondaryAvailable2;
                                primaryLabel = primaryLabel2;
                                function3 = function8;
                                secondaryLabel = secondaryLabel2;
                                function4 = function7;
                                mode2 = mode;
                                function5 = function1;
                                function6 = function2;
                            }
                        } catch (CancellationException ce4) {
                            throw ce4;
                        } catch (Throwable th4) {
                            primaryErr = th4;
                            secondaryAvailable = secondaryAvailable2;
                            primaryLabel = primaryLabel2;
                            function3 = function8;
                            secondaryLabel = secondaryLabel2;
                            function4 = function7;
                            mode2 = mode;
                            function5 = function1;
                            function6 = function2;
                        }
                    } catch (CancellationException ce5) {
                        throw ce5;
                    } catch (Throwable th5) {
                        primaryErr = th5;
                        secondaryAvailable = secondaryAvailable2;
                        primaryLabel = primaryLabel2;
                        function3 = function8;
                        secondaryLabel = secondaryLabel2;
                        function4 = function7;
                        mode2 = mode;
                        function5 = function1;
                        function6 = function2;
                    }
                } catch (CancellationException ce6) {
                    throw ce6;
                } catch (Throwable th6) {
                    primaryErr = th6;
                    secondaryAvailable = secondaryAvailable2;
                    primaryLabel = primaryLabel2;
                    function3 = function8;
                    secondaryLabel = secondaryLabel2;
                    function4 = function7;
                    mode2 = mode;
                    function5 = function1;
                    function6 = function2;
                }
                break;
                break;
            case 1:
                boolean secondaryAvailable3 = anonymousClass2.Z$4;
                boolean primaryAvailable2 = anonymousClass2.Z$3;
                i = anonymousClass2.I$0;
                cloudAvailable2 = anonymousClass2.Z$2;
                localAvailable2 = anonymousClass2.Z$1;
                allowFallback2 = anonymousClass2.Z$0;
                String secondaryLabel3 = (String) anonymousClass2.L$7;
                String primaryLabel3 = (String) anonymousClass2.L$6;
                Function1<? super Continuation<? super T>, ? extends Object> function9 = (Function1) anonymousClass2.L$5;
                Function1<? super Continuation<? super T>, ? extends Object> function10 = (Function1) anonymousClass2.L$4;
                stage2 = (String) anonymousClass2.L$3;
                function6 = (Function1) anonymousClass2.L$2;
                function5 = (Function1) anonymousClass2.L$1;
                mode2 = (ProviderMode) anonymousClass2.L$0;
                try {
                    ResultKt.throwOnFailure($result);
                    return $result;
                } catch (CancellationException ce7) {
                    throw ce7;
                } catch (Throwable th7) {
                    primaryErr = th7;
                    function3 = function9;
                    function4 = function10;
                    secondaryAvailable = secondaryAvailable3;
                    secondaryLabel = secondaryLabel3;
                    primaryAvailable = primaryAvailable2;
                    primaryLabel = primaryLabel3;
                }
                break;
            case 2:
                boolean z = anonymousClass2.Z$4;
                boolean z2 = anonymousClass2.Z$3;
                int i2 = anonymousClass2.I$0;
                boolean z3 = anonymousClass2.Z$2;
                boolean z4 = anonymousClass2.Z$1;
                boolean z5 = anonymousClass2.Z$0;
                ResultKt.throwOnFailure($result);
                return $result;
            case 3:
                boolean z6 = anonymousClass2.Z$4;
                boolean z7 = anonymousClass2.Z$3;
                int i3 = anonymousClass2.I$0;
                boolean z8 = anonymousClass2.Z$2;
                boolean z9 = anonymousClass2.Z$1;
                boolean z10 = anonymousClass2.Z$0;
                ResultKt.throwOnFailure($result);
                return $result;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
        if (allowFallback2 && secondaryAvailable) {
            Function1<? super Continuation<? super T>, ? extends Object> function11 = function4;
            if (!OpenAiCompatibleClient.INSTANCE.isTransientTransportError(primaryErr)) {
                Log.w(TAG, stage2 + " " + primaryLabel + " failed non-transient (" + primaryErr.getMessage() + "), falling back to " + secondaryLabel, primaryErr);
                anonymousClass2.L$0 = SpillingKt.nullOutSpilledVariable(mode2);
                anonymousClass2.L$1 = SpillingKt.nullOutSpilledVariable(function5);
                anonymousClass2.L$2 = SpillingKt.nullOutSpilledVariable(function6);
                anonymousClass2.L$3 = SpillingKt.nullOutSpilledVariable(stage2);
                anonymousClass2.L$4 = SpillingKt.nullOutSpilledVariable(function11);
                anonymousClass2.L$5 = SpillingKt.nullOutSpilledVariable(function3);
                anonymousClass2.L$6 = SpillingKt.nullOutSpilledVariable(primaryLabel);
                anonymousClass2.L$7 = SpillingKt.nullOutSpilledVariable(secondaryLabel);
                anonymousClass2.L$8 = SpillingKt.nullOutSpilledVariable(primaryErr);
                anonymousClass2.Z$0 = allowFallback2;
                anonymousClass2.Z$1 = localAvailable2;
                anonymousClass2.Z$2 = cloudAvailable2;
                anonymousClass2.I$0 = i;
                anonymousClass2.Z$3 = primaryAvailable;
                anonymousClass2.Z$4 = secondaryAvailable;
                anonymousClass2.label = 2;
                Object objInvoke3 = function3.invoke(anonymousClass2);
                return objInvoke3 == coroutine_suspended ? coroutine_suspended : objInvoke3;
            }
        }
        throw primaryErr;
    }

    private static final Void route$fail(String $stage, String msg) {
        throw new IllegalStateException(($stage + ": " + msg).toString());
    }
}
