package com.varun.pocketassistant.pipeline;

import android.util.Log;
import androidx.core.location.LocationRequestCompat;
import com.google.android.gms.common.internal.ImagesContract;
import com.google.common.net.HttpHeaders;
import com.varun.pocketassistant.pipeline.work.MeetingStageWorker;
import com.varun.pocketassistant.speech.DiarWord;
import java.io.File;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.SSLException;
import kotlin.Lazy;
import kotlin.LazyKt;
import kotlin.Metadata;
import kotlin.Result;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.ContinuationImpl;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.DebugProbesKt;
import kotlin.coroutines.jvm.internal.SpillingKt;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.io.CloseableKt;
import kotlin.io.FilesKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.sequences.SequencesKt;
import kotlin.text.Charsets;
import kotlin.text.StringsKt;
import kotlinx.coroutines.CancellableContinuation;
import kotlinx.coroutines.CancellableContinuationImpl;
import kotlinx.coroutines.sync.Semaphore;
import kotlinx.coroutines.sync.SemaphoreKt;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.EventListener;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: compiled from: CloudAndLocalProviders.kt */
/* JADX INFO: loaded from: classes8.dex */
@Metadata(d1 = {"\u0000\u008c\u0001\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\f\n\u0002\u0010\u0012\n\u0002\b\u0004\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0007\u0018\u0000 O2\u00020\u0001:\u0002NOB\u001b\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0005¢\u0006\u0004\b\u0006\u0010\u0007J \u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u00162\b\b\u0002\u0010\u0017\u001a\u00020\u0018H\u0086@¢\u0006\u0002\u0010\u0019J&\u0010\u001a\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u001b\u001a\u00020\u001c2\u0006\u0010\u0017\u001a\u00020\u0018H\u0082@¢\u0006\u0002\u0010\u001dJ\u001e\u0010\u001e\u001a\u00020\u001f2\u0006\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u001b\u001a\u00020\u001cH\u0082@¢\u0006\u0002\u0010 J@\u0010!\u001a\u00020\u001f2\u0006\u0010\"\u001a\u00020\u001f2\b\b\u0002\u0010#\u001a\u00020\u001f2\n\b\u0002\u0010$\u001a\u0004\u0018\u00010\u001f2\b\b\u0002\u0010%\u001a\u00020\u00182\b\b\u0002\u0010&\u001a\u00020\u001fH\u0086@¢\u0006\u0002\u0010'J6\u0010(\u001a\u00020\u001f2\u0006\u0010\u001b\u001a\u00020\u001c2\u0006\u0010)\u001a\u00020\u001f2\u0006\u0010\"\u001a\u00020\u001f2\u0006\u0010#\u001a\u00020\u001f2\u0006\u0010%\u001a\u00020\u0018H\u0082@¢\u0006\u0002\u0010*J0\u0010+\u001a\u00020,2\u0006\u0010\u001b\u001a\u00020\u001c2\u0006\u0010)\u001a\u00020\u001f2\u0006\u0010\"\u001a\u00020\u001f2\u0006\u0010#\u001a\u00020\u001f2\u0006\u0010%\u001a\u00020\u0018H\u0002J\u0010\u0010-\u001a\u00020\u001f2\u0006\u0010.\u001a\u00020\u001fH\u0002J@\u0010/\u001a\u00020\u001f2\u0006\u0010\"\u001a\u00020\u001f2\b\b\u0002\u0010#\u001a\u00020\u001f2\n\b\u0002\u0010$\u001a\u0004\u0018\u00010\u001f2\b\b\u0002\u00100\u001a\u0002012\b\b\u0002\u00102\u001a\u00020\u0018H\u0086@¢\u0006\u0002\u00103J\u0018\u0010%\u001a\u0002042\u0006\u00105\u001a\u0002062\u0006\u0010\u001b\u001a\u00020\u001cH\u0002J\u0016\u00107\u001a\u00020\u001f2\u0006\u00108\u001a\u00020\u001fH\u0086@¢\u0006\u0002\u00109J\u0018\u0010:\u001a\u00020;2\u0006\u0010\u001b\u001a\u00020\u001c2\u0006\u0010<\u001a\u00020\u001fH\u0002J\u001e\u0010=\u001a\u00020\u001f2\u0006\u0010>\u001a\u00020\t2\u0006\u0010?\u001a\u00020@H\u0082@¢\u0006\u0002\u0010AJ \u0010B\u001a\u00020\u00142\u0006\u0010.\u001a\u00020\u001f2\u0006\u0010C\u001a\u00020\u001f2\u0006\u0010\u0017\u001a\u00020\u0018H\u0002J\u0018\u0010D\u001a\u00020\u001f2\u0006\u0010.\u001a\u00020\u001f2\u0006\u0010C\u001a\u00020\u001fH\u0002JL\u0010E\u001a\u0002HF\"\u0004\b\u0000\u0010F2\u0006\u0010C\u001a\u00020\u001f2\u0006\u0010G\u001a\u00020\u001f2\b\b\u0002\u0010H\u001a\u00020I2\u001c\u0010J\u001a\u0018\b\u0001\u0012\n\u0012\b\u0012\u0004\u0012\u0002HF0L\u0012\u0006\u0012\u0004\u0018\u00010\u00010KH\u0082@¢\u0006\u0002\u0010MR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u0004\u001a\u0004\u0018\u00010\u0005X\u0082\u0004¢\u0006\u0002\n\u0000R\u001b\u0010\b\u001a\u00020\t8BX\u0082\u0084\u0002¢\u0006\f\n\u0004\b\f\u0010\r\u001a\u0004\b\n\u0010\u000bR\u000e\u0010\u000e\u001a\u00020\u000fX\u0082\u0004¢\u0006\u0002\n\u0000R\u001b\u0010\u0010\u001a\u00020\t8BX\u0082\u0084\u0002¢\u0006\f\n\u0004\b\u0012\u0010\r\u001a\u0004\b\u0011\u0010\u000b¨\u0006P"}, d2 = {"Lcom/varun/pocketassistant/pipeline/OpenAiCompatibleClient;", "", "config", "Lcom/varun/pocketassistant/pipeline/PipelineConfig;", "circuitBreaker", "Lcom/varun/pocketassistant/pipeline/CloudCircuitBreaker;", "<init>", "(Lcom/varun/pocketassistant/pipeline/PipelineConfig;Lcom/varun/pocketassistant/pipeline/CloudCircuitBreaker;)V", "asrHttp", "Lokhttp3/OkHttpClient;", "getAsrHttp", "()Lokhttp3/OkHttpClient;", "asrHttp$delegate", "Lkotlin/Lazy;", "chatGate", "Lkotlinx/coroutines/sync/Semaphore;", "chatHttp", "getChatHttp", "chatHttp$delegate", "transcribeAudio", "Lcom/varun/pocketassistant/pipeline/OpenAiCompatibleClient$TranscriptionResult;", "wav", "Ljava/io/File;", "diarize", "", "(Ljava/io/File;ZLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "transcribeOpenRouterJson", "settings", "Lcom/varun/pocketassistant/pipeline/PipelineSettings;", "(Ljava/io/File;Lcom/varun/pocketassistant/pipeline/PipelineSettings;ZLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "transcribeMultipart", "", "(Ljava/io/File;Lcom/varun/pocketassistant/pipeline/PipelineSettings;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "chat", "userPrompt", "systemPrompt", "model", "applyReasoning", MeetingStageWorker.KEY_STAGE, "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;ZLjava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "chatOnce", "modelId", "(Lcom/varun/pocketassistant/pipeline/PipelineSettings;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;ZLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "buildChatBodyBytes", "", "parseChatContent", "body", "benchChat", "timeoutMs", "", "disableReasoning", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;JZLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "", "payload", "Lorg/json/JSONObject;", "chatCleanup", "prompt", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "baseRequest", "Lokhttp3/Request$Builder;", ImagesContract.URL, "execute", "client", "request", "Lokhttp3/Request;", "(Lokhttp3/OkHttpClient;Lokhttp3/Request;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "parseTranscriptionResponse", "label", "readTranscriptionResponse", "withRetries", "T", "host", "maxAttempts", "", "block", "Lkotlin/Function1;", "Lkotlin/coroutines/Continuation;", "(Ljava/lang/String;Ljava/lang/String;ILkotlin/jvm/functions/Function1;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "TranscriptionResult", "Companion", "app_debug"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class OpenAiCompatibleClient {
    private static final String DEFAULT_SYSTEM = "You clean speech transcripts and summarize meetings. Output only the requested Markdown or text.";
    private static final int MAX_ATTEMPTS = 3;
    private static final long MULTIPART_SAFE_BYTES = 20971520;
    private static final String OPENROUTER_REFERER = "https://github.com/varunramesh/pocket-assistant";
    private static final String OPENROUTER_TITLE = "Pocket Assistant";
    private static final String TAG = "OpenAiClient";

    /* JADX INFO: renamed from: asrHttp$delegate, reason: from kotlin metadata */
    private final Lazy asrHttp;
    private final Semaphore chatGate;

    /* JADX INFO: renamed from: chatHttp$delegate, reason: from kotlin metadata */
    private final Lazy chatHttp;
    private final CloudCircuitBreaker circuitBreaker;
    private final PipelineConfig config;

    /* JADX INFO: renamed from: Companion, reason: from kotlin metadata */
    public static final Companion INSTANCE = new Companion(null);
    public static final int $stable = 8;
    private static final MediaType JSON = MediaType.INSTANCE.get("application/json; charset=utf-8");

    /* JADX INFO: renamed from: com.varun.pocketassistant.pipeline.OpenAiCompatibleClient$benchChat$1, reason: invalid class name */
    /* JADX INFO: compiled from: CloudAndLocalProviders.kt */
    @Metadata(k = 3, mv = {2, 2, 0}, xi = 48)
    @DebugMetadata(c = "com.varun.pocketassistant.pipeline.OpenAiCompatibleClient", f = "CloudAndLocalProviders.kt", i = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, l = {268}, m = "benchChat", n = {"userPrompt", "systemPrompt", "model", "settings", "modelId", "messages", "payload", "bodyBytes", "request", "client", "timeoutMs", "disableReasoning", "started"}, s = {"L$0", "L$1", "L$2", "L$3", "L$4", "L$5", "L$6", "L$7", "L$8", "L$9", "J$0", "Z$0", "J$1"})
    static final class AnonymousClass1 extends ContinuationImpl {
        long J$0;
        long J$1;
        Object L$0;
        Object L$1;
        Object L$2;
        Object L$3;
        Object L$4;
        Object L$5;
        Object L$6;
        Object L$7;
        Object L$8;
        Object L$9;
        boolean Z$0;
        int label;
        /* synthetic */ Object result;

        AnonymousClass1(Continuation<? super AnonymousClass1> continuation) {
            super(continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            this.result = obj;
            this.label |= Integer.MIN_VALUE;
            return OpenAiCompatibleClient.this.benchChat(null, null, null, 0L, false, this);
        }
    }

    /* JADX INFO: renamed from: com.varun.pocketassistant.pipeline.OpenAiCompatibleClient$chat$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: CloudAndLocalProviders.kt */
    @Metadata(k = 3, mv = {2, 2, 0}, xi = 48)
    @DebugMetadata(c = "com.varun.pocketassistant.pipeline.OpenAiCompatibleClient", f = "CloudAndLocalProviders.kt", i = {0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}, l = {886, 143}, m = "chat", n = {"userPrompt", "systemPrompt", "model", MeetingStageWorker.KEY_STAGE, "settings", "modelId", "$this$withPermit\\5", "applyReasoning", "$i$f$withPermit\\5\\141", "userPrompt", "systemPrompt", "model", MeetingStageWorker.KEY_STAGE, "settings", "modelId", "$this$withPermit\\5", "applyReasoning", "$i$f$withPermit\\5\\141", "$i$a$-withPermit-OpenAiCompatibleClient$chat$3\\6\\888\\0"}, s = {"L$0", "L$1", "L$2", "L$3", "L$4", "L$5", "L$6", "Z$0", "I$0", "L$0", "L$1", "L$2", "L$3", "L$4", "L$5", "L$6", "Z$0", "I$0", "I$1"})
    static final class C06751 extends ContinuationImpl {
        int I$0;
        int I$1;
        Object L$0;
        Object L$1;
        Object L$2;
        Object L$3;
        Object L$4;
        Object L$5;
        Object L$6;
        boolean Z$0;
        int label;
        /* synthetic */ Object result;

        C06751(Continuation<? super C06751> continuation) {
            super(continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            this.result = obj;
            this.label |= Integer.MIN_VALUE;
            return OpenAiCompatibleClient.this.chat(null, null, null, false, null, this);
        }
    }

    /* JADX INFO: renamed from: com.varun.pocketassistant.pipeline.OpenAiCompatibleClient$chatOnce$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: CloudAndLocalProviders.kt */
    @Metadata(k = 3, mv = {2, 2, 0}, xi = 48)
    @DebugMetadata(c = "com.varun.pocketassistant.pipeline.OpenAiCompatibleClient", f = "CloudAndLocalProviders.kt", i = {0, 0, 0, 0, 0, 0, 0, 0}, l = {181}, m = "chatOnce", n = {"settings", "modelId", "userPrompt", "systemPrompt", "bodyBytes", "request", "applyReasoning", "started"}, s = {"L$0", "L$1", "L$2", "L$3", "L$4", "L$5", "Z$0", "J$0"})
    static final class C06761 extends ContinuationImpl {
        long J$0;
        Object L$0;
        Object L$1;
        Object L$2;
        Object L$3;
        Object L$4;
        Object L$5;
        boolean Z$0;
        int label;
        /* synthetic */ Object result;

        C06761(Continuation<? super C06761> continuation) {
            super(continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            this.result = obj;
            this.label |= Integer.MIN_VALUE;
            return OpenAiCompatibleClient.this.chatOnce(null, null, null, null, false, this);
        }
    }

    /* JADX INFO: renamed from: com.varun.pocketassistant.pipeline.OpenAiCompatibleClient$transcribeMultipart$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: CloudAndLocalProviders.kt */
    @Metadata(k = 3, mv = {2, 2, 0}, xi = 48)
    @DebugMetadata(c = "com.varun.pocketassistant.pipeline.OpenAiCompatibleClient", f = "CloudAndLocalProviders.kt", i = {0, 0, 0, 0, 0}, l = {123}, m = "transcribeMultipart", n = {"wav", "settings", "bodyBuilder", "lang", "request"}, s = {"L$0", "L$1", "L$2", "L$3", "L$4"})
    static final class C06771 extends ContinuationImpl {
        Object L$0;
        Object L$1;
        Object L$2;
        Object L$3;
        Object L$4;
        Object L$5;
        int label;
        /* synthetic */ Object result;

        C06771(Continuation<? super C06771> continuation) {
            super(continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            this.result = obj;
            this.label |= Integer.MIN_VALUE;
            return OpenAiCompatibleClient.this.transcribeMultipart(null, null, this);
        }
    }

    /* JADX INFO: renamed from: com.varun.pocketassistant.pipeline.OpenAiCompatibleClient$transcribeOpenRouterJson$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: CloudAndLocalProviders.kt */
    @Metadata(k = 3, mv = {2, 2, 0}, xi = 48)
    @DebugMetadata(c = "com.varun.pocketassistant.pipeline.OpenAiCompatibleClient", f = "CloudAndLocalProviders.kt", i = {0, 0, 0, 0, 0, 0, 0}, l = {LocationRequestCompat.QUALITY_LOW_POWER}, m = "transcribeOpenRouterJson", n = {"wav", "settings", "format", "model", "lang", "request", "diarize"}, s = {"L$0", "L$1", "L$2", "L$3", "L$4", "L$5", "Z$0"})
    static final class C06781 extends ContinuationImpl {
        Object L$0;
        Object L$1;
        Object L$2;
        Object L$3;
        Object L$4;
        Object L$5;
        Object L$6;
        boolean Z$0;
        int label;
        /* synthetic */ Object result;

        C06781(Continuation<? super C06781> continuation) {
            super(continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            this.result = obj;
            this.label |= Integer.MIN_VALUE;
            return OpenAiCompatibleClient.this.transcribeOpenRouterJson(null, null, false, this);
        }
    }

    /* JADX INFO: renamed from: com.varun.pocketassistant.pipeline.OpenAiCompatibleClient$withRetries$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: CloudAndLocalProviders.kt */
    @Metadata(k = 3, mv = {2, 2, 0}, xi = 48)
    @DebugMetadata(c = "com.varun.pocketassistant.pipeline.OpenAiCompatibleClient", f = "CloudAndLocalProviders.kt", i = {0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}, l = {393, 413}, m = "withRetries", n = {"label", "host", "block", "last", "maxAttempts", "attempt\\1", "$i$a$-repeat-OpenAiCompatibleClient$withRetries$2\\1\\391\\0", "label", "host", "block", "last", "t\\1", "maxAttempts", "attempt\\1", "$i$a$-repeat-OpenAiCompatibleClient$withRetries$2\\1\\391\\0", "baseMs\\1", "jitter\\1", "delayMs\\1"}, s = {"L$0", "L$1", "L$2", "L$3", "I$0", "I$2", "I$3", "L$0", "L$1", "L$2", "L$3", "L$4", "I$0", "I$2", "I$3", "J$0", "J$1", "J$2"})
    static final class C06791<T> extends ContinuationImpl {
        int I$0;
        int I$1;
        int I$2;
        int I$3;
        long J$0;
        long J$1;
        long J$2;
        Object L$0;
        Object L$1;
        Object L$2;
        Object L$3;
        Object L$4;
        int label;
        /* synthetic */ Object result;

        C06791(Continuation<? super C06791> continuation) {
            super(continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            this.result = obj;
            this.label |= Integer.MIN_VALUE;
            return OpenAiCompatibleClient.this.withRetries(null, null, 0, null, this);
        }
    }

    public OpenAiCompatibleClient(PipelineConfig config, CloudCircuitBreaker circuitBreaker) {
        Intrinsics.checkNotNullParameter(config, "config");
        this.config = config;
        this.circuitBreaker = circuitBreaker;
        this.asrHttp = LazyKt.lazy(new Function0() { // from class: com.varun.pocketassistant.pipeline.OpenAiCompatibleClient$$ExternalSyntheticLambda1
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return OpenAiCompatibleClient.asrHttp_delegate$lambda$0();
            }
        });
        this.chatGate = SemaphoreKt.Semaphore$default(1, 0, 2, null);
        this.chatHttp = LazyKt.lazy(new Function0() { // from class: com.varun.pocketassistant.pipeline.OpenAiCompatibleClient$$ExternalSyntheticLambda2
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return OpenAiCompatibleClient.chatHttp_delegate$lambda$1();
            }
        });
    }

    public /* synthetic */ OpenAiCompatibleClient(PipelineConfig pipelineConfig, CloudCircuitBreaker cloudCircuitBreaker, int i, DefaultConstructorMarker defaultConstructorMarker) {
        this(pipelineConfig, (i & 2) != 0 ? null : cloudCircuitBreaker);
    }

    private final OkHttpClient getAsrHttp() {
        return (OkHttpClient) this.asrHttp.getValue();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final OkHttpClient asrHttp_delegate$lambda$0() {
        return new OkHttpClient.Builder().dns(ResilientDns.INSTANCE).connectTimeout(PipelineTelemetry.HTTP_CONNECT_TIMEOUT_MS, TimeUnit.MILLISECONDS).readTimeout(90000L, TimeUnit.MILLISECONDS).writeTimeout(PipelineTelemetry.ASR_HTTP_WRITE_TIMEOUT_MS, TimeUnit.MILLISECONDS).retryOnConnectionFailure(false).build();
    }

    private final OkHttpClient getChatHttp() {
        return (OkHttpClient) this.chatHttp.getValue();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final OkHttpClient chatHttp_delegate$lambda$1() {
        return new OkHttpClient.Builder().dns(ResilientDns.INSTANCE).connectTimeout(PipelineTelemetry.HTTP_CONNECT_TIMEOUT_MS, TimeUnit.MILLISECONDS).readTimeout(PipelineTelemetry.CHAT_HTTP_READ_TIMEOUT_MS, TimeUnit.MILLISECONDS).writeTimeout(30000L, TimeUnit.MILLISECONDS).callTimeout(150000L, TimeUnit.MILLISECONDS).retryOnConnectionFailure(false).build();
    }

    /* JADX INFO: compiled from: CloudAndLocalProviders.kt */
    @Metadata(d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\n\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\b\u0087\b\u0018\u00002\u00020\u0001B\u001f\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u000e\b\u0002\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005¢\u0006\u0004\b\u0007\u0010\bJ\t\u0010\r\u001a\u00020\u0003HÆ\u0003J\u000f\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005HÆ\u0003J#\u0010\u000f\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\u000e\b\u0002\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005HÆ\u0001J\u0013\u0010\u0010\u001a\u00020\u00112\b\u0010\u0012\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u0013\u001a\u00020\u0014HÖ\u0001J\t\u0010\u0015\u001a\u00020\u0003HÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u0017\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\f¨\u0006\u0016"}, d2 = {"Lcom/varun/pocketassistant/pipeline/OpenAiCompatibleClient$TranscriptionResult;", "", "text", "", "words", "", "Lcom/varun/pocketassistant/speech/DiarWord;", "<init>", "(Ljava/lang/String;Ljava/util/List;)V", "getText", "()Ljava/lang/String;", "getWords", "()Ljava/util/List;", "component1", "component2", "copy", "equals", "", "other", "hashCode", "", "toString", "app_debug"}, k = 1, mv = {2, 2, 0}, xi = 48)
    public static final /* data */ class TranscriptionResult {
        public static final int $stable = 8;
        private final String text;
        private final List<DiarWord> words;

        /* JADX WARN: Multi-variable type inference failed */
        public static /* synthetic */ TranscriptionResult copy$default(TranscriptionResult transcriptionResult, String str, List list, int i, Object obj) {
            if ((i & 1) != 0) {
                str = transcriptionResult.text;
            }
            if ((i & 2) != 0) {
                list = transcriptionResult.words;
            }
            return transcriptionResult.copy(str, list);
        }

        /* JADX INFO: renamed from: component1, reason: from getter */
        public final String getText() {
            return this.text;
        }

        public final List<DiarWord> component2() {
            return this.words;
        }

        public final TranscriptionResult copy(String text, List<DiarWord> words) {
            Intrinsics.checkNotNullParameter(text, "text");
            Intrinsics.checkNotNullParameter(words, "words");
            return new TranscriptionResult(text, words);
        }

        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof TranscriptionResult)) {
                return false;
            }
            TranscriptionResult transcriptionResult = (TranscriptionResult) other;
            return Intrinsics.areEqual(this.text, transcriptionResult.text) && Intrinsics.areEqual(this.words, transcriptionResult.words);
        }

        public int hashCode() {
            return (this.text.hashCode() * 31) + this.words.hashCode();
        }

        public String toString() {
            return "TranscriptionResult(text=" + this.text + ", words=" + this.words + ")";
        }

        public TranscriptionResult(String text, List<DiarWord> words) {
            Intrinsics.checkNotNullParameter(text, "text");
            Intrinsics.checkNotNullParameter(words, "words");
            this.text = text;
            this.words = words;
        }

        public /* synthetic */ TranscriptionResult(String str, List list, int i, DefaultConstructorMarker defaultConstructorMarker) {
            this(str, (i & 2) != 0 ? CollectionsKt.emptyList() : list);
        }

        public final String getText() {
            return this.text;
        }

        public final List<DiarWord> getWords() {
            return this.words;
        }
    }

    public static /* synthetic */ Object transcribeAudio$default(OpenAiCompatibleClient openAiCompatibleClient, File file, boolean z, Continuation continuation, int i, Object obj) {
        if ((i & 2) != 0) {
            z = false;
        }
        return openAiCompatibleClient.transcribeAudio(file, z, continuation);
    }

    public final Object transcribeAudio(File wav, boolean diarize, Continuation<? super TranscriptionResult> continuation) {
        PipelineSettings settings = this.config.load();
        if (StringsKt.isBlank(settings.getCloudApiKey())) {
            throw new IllegalArgumentException("Cloud API key not set".toString());
        }
        return withRetries("Cloud ASR", INSTANCE.hostOf(settings.getCloudBaseUrl()), 3, new AnonymousClass3(settings, wav, diarize, this, null), continuation);
    }

    /* JADX INFO: renamed from: com.varun.pocketassistant.pipeline.OpenAiCompatibleClient$transcribeAudio$3, reason: invalid class name */
    /* JADX INFO: compiled from: CloudAndLocalProviders.kt */
    @Metadata(d1 = {"\u0000\u0006\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001H\n"}, d2 = {"<anonymous>", "Lcom/varun/pocketassistant/pipeline/OpenAiCompatibleClient$TranscriptionResult;"}, k = 3, mv = {2, 2, 0}, xi = 48)
    @DebugMetadata(c = "com.varun.pocketassistant.pipeline.OpenAiCompatibleClient$transcribeAudio$3", f = "CloudAndLocalProviders.kt", i = {}, l = {82, 84}, m = "invokeSuspend", n = {}, s = {})
    static final class AnonymousClass3 extends SuspendLambda implements Function1<Continuation<? super TranscriptionResult>, Object> {
        final /* synthetic */ boolean $diarize;
        final /* synthetic */ PipelineSettings $settings;
        final /* synthetic */ File $wav;
        int label;
        final /* synthetic */ OpenAiCompatibleClient this$0;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass3(PipelineSettings pipelineSettings, File file, boolean z, OpenAiCompatibleClient openAiCompatibleClient, Continuation<? super AnonymousClass3> continuation) {
            super(1, continuation);
            this.$settings = pipelineSettings;
            this.$wav = file;
            this.$diarize = z;
            this.this$0 = openAiCompatibleClient;
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Continuation<Unit> create(Continuation<?> continuation) {
            return new AnonymousClass3(this.$settings, this.$wav, this.$diarize, this.this$0, continuation);
        }

        @Override // kotlin.jvm.functions.Function1
        public final Object invoke(Continuation<? super TranscriptionResult> continuation) {
            return ((AnonymousClass3) create(continuation)).invokeSuspend(Unit.INSTANCE);
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            Object objTranscribeOpenRouterJson;
            Object objTranscribeMultipart;
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            int i = 2;
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure(obj);
                    if (this.$settings.isOpenRouter() || this.$wav.length() > OpenAiCompatibleClient.MULTIPART_SAFE_BYTES || this.$diarize) {
                        this.label = 1;
                        objTranscribeOpenRouterJson = this.this$0.transcribeOpenRouterJson(this.$wav, this.$settings, this.$diarize, this);
                        if (objTranscribeOpenRouterJson == coroutine_suspended) {
                            return coroutine_suspended;
                        }
                        return (TranscriptionResult) objTranscribeOpenRouterJson;
                    }
                    this.label = 2;
                    objTranscribeMultipart = this.this$0.transcribeMultipart(this.$wav, this.$settings, this);
                    if (objTranscribeMultipart == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    return new TranscriptionResult((String) objTranscribeMultipart, null, i, 0 == true ? 1 : 0);
                case 1:
                    ResultKt.throwOnFailure(obj);
                    objTranscribeOpenRouterJson = obj;
                    return (TranscriptionResult) objTranscribeOpenRouterJson;
                case 2:
                    ResultKt.throwOnFailure(obj);
                    objTranscribeMultipart = obj;
                    return new TranscriptionResult((String) objTranscribeMultipart, null, i, 0 == true ? 1 : 0);
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code duplicated, block: B:7:0x0018  */
    public final Object transcribeOpenRouterJson(File wav, PipelineSettings settings, boolean diarize, Continuation<? super TranscriptionResult> continuation) {
        C06781 c06781;
        boolean diarize2;
        Object obj;
        OpenAiCompatibleClient openAiCompatibleClient;
        if (continuation instanceof C06781) {
            c06781 = (C06781) continuation;
            if ((c06781.label & Integer.MIN_VALUE) != 0) {
                c06781.label -= Integer.MIN_VALUE;
            } else {
                c06781 = new C06781(continuation);
            }
        } else {
            c06781 = new C06781(continuation);
        }
        C06781 c06782 = c06781;
        Object $result = c06782.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (c06782.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                String lowerCase = FilesKt.getExtension(wav).toLowerCase(Locale.ROOT);
                Intrinsics.checkNotNullExpressionValue(lowerCase, "toLowerCase(...)");
                String str = lowerCase;
                if (StringsKt.isBlank(str)) {
                    str = "wav";
                }
                String format = str;
                String model = OpenRouterModels.INSTANCE.normalizeId(settings.getCloudAsrModel());
                String string = StringsKt.trim((CharSequence) settings.getSttLanguage()).toString();
                if (string.length() == 0) {
                    string = null;
                }
                String lang = string;
                Request request = baseRequest(settings, StringsKt.trimEnd(settings.getCloudBaseUrl(), '/') + "/audio/transcriptions").post(new StreamingBase64AudioBody(wav, model, format, lang, diarize)).build();
                Log.i(TAG, "ASR POST json stream bytes=" + wav.length() + " model=" + settings.getCloudAsrModel() + " diarize=" + diarize);
                OkHttpClient asrHttp = getAsrHttp();
                c06782.L$0 = SpillingKt.nullOutSpilledVariable(wav);
                c06782.L$1 = SpillingKt.nullOutSpilledVariable(settings);
                c06782.L$2 = SpillingKt.nullOutSpilledVariable(format);
                c06782.L$3 = SpillingKt.nullOutSpilledVariable(model);
                c06782.L$4 = SpillingKt.nullOutSpilledVariable(lang);
                c06782.L$5 = SpillingKt.nullOutSpilledVariable(request);
                c06782.L$6 = this;
                c06782.Z$0 = diarize;
                c06782.label = 1;
                Object objExecute = execute(asrHttp, request, c06782);
                if (objExecute == coroutine_suspended) {
                    return coroutine_suspended;
                }
                diarize2 = diarize;
                obj = objExecute;
                openAiCompatibleClient = this;
                break;
            case 1:
                diarize2 = c06782.Z$0;
                openAiCompatibleClient = (OpenAiCompatibleClient) c06782.L$6;
                ResultKt.throwOnFailure($result);
                obj = $result;
                break;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
        return openAiCompatibleClient.parseTranscriptionResponse((String) obj, "Cloud ASR", diarize2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code duplicated, block: B:7:0x0014  */
    /* JADX WARN: Multi-variable type inference failed */
    public final Object transcribeMultipart(File file, PipelineSettings pipelineSettings, Continuation<? super String> continuation) {
        C06771 c06771;
        Object objExecute;
        OpenAiCompatibleClient openAiCompatibleClient;
        if (continuation instanceof C06771) {
            c06771 = (C06771) continuation;
            if ((c06771.label & Integer.MIN_VALUE) != 0) {
                c06771.label -= Integer.MIN_VALUE;
            } else {
                c06771 = new C06771(continuation);
            }
        } else {
            c06771 = new C06771(continuation);
        }
        Object obj = c06771.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (c06771.label) {
            case 0:
                ResultKt.throwOnFailure(obj);
                MultipartBody.Builder builderAddFormDataPart = new MultipartBody.Builder(null, 1, 0 == true ? 1 : 0).setType(MultipartBody.FORM).addFormDataPart("model", OpenRouterModels.INSTANCE.normalizeId(pipelineSettings.getCloudAsrModel())).addFormDataPart("response_format", "json");
                String string = StringsKt.trim((CharSequence) pipelineSettings.getSttLanguage()).toString();
                if ((string.length() > 0) != false) {
                    builderAddFormDataPart.addFormDataPart("language", string);
                }
                builderAddFormDataPart.addFormDataPart("file", file.getName(), RequestBody.INSTANCE.create(file, MediaType.INSTANCE.get("audio/wav")));
                Request requestBuild = baseRequest(pipelineSettings, StringsKt.trimEnd(pipelineSettings.getCloudBaseUrl(), '/') + "/audio/transcriptions").post(builderAddFormDataPart.build()).build();
                Log.i(TAG, "ASR POST multipart bytes=" + file.length() + " model=" + pipelineSettings.getCloudAsrModel());
                OkHttpClient asrHttp = getAsrHttp();
                c06771.L$0 = SpillingKt.nullOutSpilledVariable(file);
                c06771.L$1 = SpillingKt.nullOutSpilledVariable(pipelineSettings);
                c06771.L$2 = SpillingKt.nullOutSpilledVariable(builderAddFormDataPart);
                c06771.L$3 = SpillingKt.nullOutSpilledVariable(string);
                c06771.L$4 = SpillingKt.nullOutSpilledVariable(requestBuild);
                c06771.L$5 = this;
                c06771.label = 1;
                objExecute = execute(asrHttp, requestBuild, c06771);
                if (objExecute == coroutine_suspended) {
                    return coroutine_suspended;
                }
                openAiCompatibleClient = this;
                break;
            case 1:
                openAiCompatibleClient = (OpenAiCompatibleClient) c06771.L$5;
                ResultKt.throwOnFailure(obj);
                objExecute = obj;
                break;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
        return openAiCompatibleClient.readTranscriptionResponse((String) objExecute, "Cloud ASR");
    }

    public static /* synthetic */ Object chat$default(OpenAiCompatibleClient openAiCompatibleClient, String str, String str2, String str3, boolean z, String str4, Continuation continuation, int i, Object obj) {
        return openAiCompatibleClient.chat(str, (i & 2) != 0 ? DEFAULT_SYSTEM : str2, (i & 4) != 0 ? null : str3, (i & 8) != 0 ? false : z, (i & 16) != 0 ? "chat" : str4, continuation);
    }

    /* JADX WARN: Code duplicated, block: B:28:0x00c5  */
    /* JADX WARN: Code duplicated, block: B:30:0x00d1  */
    /* JADX WARN: Code duplicated, block: B:39:0x0153  */
    /* JADX WARN: Code duplicated, block: B:40:0x0155  */
    /* JADX WARN: Code duplicated, block: B:49:0x01ae A[RETURN] */
    /* JADX WARN: Code duplicated, block: B:50:0x01af  */
    /* JADX WARN: Code duplicated, block: B:7:0x0018  */
    public final Object chat(String userPrompt, String systemPrompt, String model, boolean applyReasoning, String stage, Continuation<? super String> continuation) throws Throwable {
        C06751 c06751;
        String cloudCleanupModel;
        String str;
        String modelId;
        PipelineSettings settings;
        String systemPrompt2;
        int i;
        String userPrompt2;
        String stage2;
        boolean applyReasoning2;
        String systemPrompt3;
        Semaphore semaphore;
        String string;
        boolean z;
        Semaphore semaphore2;
        Object objWithRetries;
        if (continuation instanceof C06751) {
            c06751 = (C06751) continuation;
            if ((c06751.label & Integer.MIN_VALUE) != 0) {
                c06751.label -= Integer.MIN_VALUE;
            } else {
                c06751 = new C06751(continuation);
            }
        } else {
            c06751 = new C06751(continuation);
        }
        C06751 c06752 = c06751;
        Object $result = c06752.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (c06752.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                PipelineSettings settings2 = this.config.load();
                if (StringsKt.isBlank(settings2.getCloudApiKey())) {
                    throw new IllegalArgumentException("Cloud API key not set".toString());
                }
                if (model == null || (string = StringsKt.trim((CharSequence) model).toString()) == null) {
                    cloudCleanupModel = settings2.getCloudCleanupModel();
                    if (StringsKt.isBlank(cloudCleanupModel)) {
                        cloudCleanupModel = "google/gemini-2.5-flash";
                    }
                    str = cloudCleanupModel;
                } else {
                    String str2 = string;
                    if (StringsKt.isBlank(str2)) {
                        str2 = null;
                    }
                    str = str2;
                    if (str == null) {
                        cloudCleanupModel = settings2.getCloudCleanupModel();
                        if (StringsKt.isBlank(cloudCleanupModel)) {
                            cloudCleanupModel = "google/gemini-2.5-flash";
                        }
                        str = cloudCleanupModel;
                    }
                }
                modelId = OpenRouterModels.INSTANCE.normalizeId(str);
                Semaphore semaphore3 = this.chatGate;
                c06752.L$0 = userPrompt;
                c06752.L$1 = systemPrompt;
                c06752.L$2 = SpillingKt.nullOutSpilledVariable(model);
                c06752.L$3 = stage;
                c06752.L$4 = settings2;
                c06752.L$5 = modelId;
                c06752.L$6 = semaphore3;
                c06752.Z$0 = applyReasoning;
                c06752.I$0 = 0;
                c06752.label = 1;
                if (semaphore3.acquire(c06752) == coroutine_suspended) {
                    return coroutine_suspended;
                }
                settings = settings2;
                systemPrompt2 = systemPrompt;
                i = 0;
                userPrompt2 = userPrompt;
                stage2 = stage;
                applyReasoning2 = applyReasoning;
                systemPrompt3 = model;
                semaphore = semaphore3;
                try {
                    String str3 = "Cloud " + stage2 + " model=" + modelId + " inChars=" + userPrompt2.length();
                    String strHostOf = INSTANCE.hostOf(settings.getCloudBaseUrl());
                    if (applyReasoning2) {
                        z = true;
                    } else {
                        z = false;
                    }
                    PipelineSettings settings3 = settings;
                    boolean z2 = z;
                    String modelId2 = modelId;
                    semaphore2 = semaphore;
                    try {
                        String stage3 = stage2;
                        String userPrompt3 = userPrompt2;
                        String systemPrompt4 = systemPrompt2;
                        try {
                            OpenAiCompatibleClient$chat$3$1 openAiCompatibleClient$chat$3$1 = new OpenAiCompatibleClient$chat$3$1(stage2, modelId2, userPrompt2, this, settings3, systemPrompt2, z2, null);
                            c06752.L$0 = SpillingKt.nullOutSpilledVariable(userPrompt3);
                            c06752.L$1 = SpillingKt.nullOutSpilledVariable(systemPrompt4);
                            c06752.L$2 = SpillingKt.nullOutSpilledVariable(systemPrompt3);
                            c06752.L$3 = SpillingKt.nullOutSpilledVariable(stage3);
                            c06752.L$4 = SpillingKt.nullOutSpilledVariable(settings3);
                            c06752.L$5 = SpillingKt.nullOutSpilledVariable(modelId2);
                            c06752.L$6 = semaphore2;
                            c06752.Z$0 = applyReasoning2;
                            c06752.I$0 = i;
                            c06752.I$1 = 0;
                            c06752.label = 2;
                            try {
                                objWithRetries = withRetries(str3, strHostOf, 3, openAiCompatibleClient$chat$3$1, c06752);
                                if (objWithRetries == coroutine_suspended) {
                                    return coroutine_suspended;
                                }
                                semaphore = semaphore2;
                                try {
                                    String str4 = (String) objWithRetries;
                                    semaphore.release();
                                    return str4;
                                } catch (Throwable th) {
                                    th = th;
                                    semaphore.release();
                                    throw th;
                                }
                            } catch (Throwable th2) {
                                th = th2;
                                semaphore = semaphore2;
                                semaphore.release();
                                throw th;
                            }
                        } catch (Throwable th3) {
                            th = th3;
                            semaphore = semaphore2;
                        }
                    } catch (Throwable th4) {
                        th = th4;
                        semaphore = semaphore2;
                    }
                } catch (Throwable th5) {
                    th = th5;
                }
                break;
            case 1:
                int i2 = c06752.I$0;
                boolean applyReasoning3 = c06752.Z$0;
                semaphore = (Semaphore) c06752.L$6;
                modelId = (String) c06752.L$5;
                PipelineSettings settings4 = (PipelineSettings) c06752.L$4;
                String stage4 = (String) c06752.L$3;
                String model2 = (String) c06752.L$2;
                String systemPrompt5 = (String) c06752.L$1;
                String userPrompt4 = (String) c06752.L$0;
                ResultKt.throwOnFailure($result);
                applyReasoning2 = applyReasoning3;
                userPrompt2 = userPrompt4;
                i = i2;
                stage2 = stage4;
                settings = settings4;
                systemPrompt2 = systemPrompt5;
                systemPrompt3 = model2;
                String str5 = "Cloud " + stage2 + " model=" + modelId + " inChars=" + userPrompt2.length();
                String strHostOf2 = INSTANCE.hostOf(settings.getCloudBaseUrl());
                if (applyReasoning2) {
                    z = true;
                } else {
                    z = false;
                }
                PipelineSettings settings5 = settings;
                boolean z3 = z;
                String modelId3 = modelId;
                semaphore2 = semaphore;
                String stage5 = stage2;
                String userPrompt5 = userPrompt2;
                String systemPrompt6 = systemPrompt2;
                OpenAiCompatibleClient$chat$3$1 openAiCompatibleClient$chat$3$2 = new OpenAiCompatibleClient$chat$3$1(stage2, modelId3, userPrompt2, this, settings5, systemPrompt2, z3, null);
                c06752.L$0 = SpillingKt.nullOutSpilledVariable(userPrompt5);
                c06752.L$1 = SpillingKt.nullOutSpilledVariable(systemPrompt6);
                c06752.L$2 = SpillingKt.nullOutSpilledVariable(systemPrompt3);
                c06752.L$3 = SpillingKt.nullOutSpilledVariable(stage5);
                c06752.L$4 = SpillingKt.nullOutSpilledVariable(settings5);
                c06752.L$5 = SpillingKt.nullOutSpilledVariable(modelId3);
                c06752.L$6 = semaphore2;
                c06752.Z$0 = applyReasoning2;
                c06752.I$0 = i;
                c06752.I$1 = 0;
                c06752.label = 2;
                objWithRetries = withRetries(str5, strHostOf2, 3, openAiCompatibleClient$chat$3$2, c06752);
                if (objWithRetries == coroutine_suspended) {
                    return coroutine_suspended;
                }
                semaphore = semaphore2;
                String str6 = (String) objWithRetries;
                semaphore.release();
                return str6;
            case 2:
                int i3 = c06752.I$1;
                int i4 = c06752.I$0;
                boolean z4 = c06752.Z$0;
                semaphore = (Semaphore) c06752.L$6;
                try {
                    ResultKt.throwOnFailure($result);
                    objWithRetries = $result;
                    String str7 = (String) objWithRetries;
                    semaphore.release();
                    return str7;
                } catch (Throwable th6) {
                    th = th6;
                    semaphore.release();
                    throw th;
                }
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code duplicated, block: B:7:0x001c  */
    public final Object chatOnce(PipelineSettings settings, String modelId, String userPrompt, String systemPrompt, boolean applyReasoning, Continuation<? super String> continuation) throws JSONException {
        C06761 c06761;
        Object objExecute;
        long started;
        String modelId2 = modelId;
        boolean applyReasoning2 = applyReasoning;
        if (continuation instanceof C06761) {
            c06761 = (C06761) continuation;
            if ((c06761.label & Integer.MIN_VALUE) != 0) {
                c06761.label -= Integer.MIN_VALUE;
            } else {
                c06761 = new C06761(continuation);
            }
        } else {
            c06761 = new C06761(continuation);
        }
        Object $result = c06761.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (c06761.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                byte[] bodyBytes = buildChatBodyBytes(settings, modelId, userPrompt, systemPrompt, applyReasoning);
                Request request = baseRequest(settings, StringsKt.trimEnd(settings.getCloudBaseUrl(), '/') + "/chat/completions").post(RequestBody.Companion.create$default(RequestBody.INSTANCE, bodyBytes, JSON, 0, 0, 6, (Object) null)).build();
                long started2 = System.currentTimeMillis();
                Log.i(TAG, "CHAT POST model=" + modelId2 + " promptChars=" + userPrompt.length() + " bodyBytes=" + bodyBytes.length + " reasoning=" + applyReasoning2);
                OkHttpClient chatHttp = getChatHttp();
                c06761.L$0 = SpillingKt.nullOutSpilledVariable(settings);
                c06761.L$1 = modelId2;
                c06761.L$2 = SpillingKt.nullOutSpilledVariable(userPrompt);
                c06761.L$3 = SpillingKt.nullOutSpilledVariable(systemPrompt);
                c06761.L$4 = SpillingKt.nullOutSpilledVariable(bodyBytes);
                c06761.L$5 = SpillingKt.nullOutSpilledVariable(request);
                c06761.Z$0 = applyReasoning2;
                c06761.J$0 = started2;
                c06761.label = 1;
                objExecute = execute(chatHttp, request, c06761);
                if (objExecute == coroutine_suspended) {
                    return coroutine_suspended;
                }
                started = started2;
                break;
            case 1:
                started = c06761.J$0;
                applyReasoning2 = c06761.Z$0;
                modelId2 = (String) c06761.L$1;
                ResultKt.throwOnFailure($result);
                objExecute = $result;
                break;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
        String body = (String) objExecute;
        Log.i(TAG, "CHAT ok model=" + modelId2 + " elapsedMs=" + (System.currentTimeMillis() - started) + " respChars=" + body.length());
        return parseChatContent(body);
    }

    private final byte[] buildChatBodyBytes(PipelineSettings settings, String modelId, String userPrompt, String systemPrompt, boolean applyReasoning) throws JSONException {
        JSONArray messages = new JSONArray().put(new JSONObject().put("role", "system").put("content", systemPrompt)).put(new JSONObject().put("role", "user").put("content", userPrompt));
        JSONObject payload = new JSONObject().put("model", modelId).put("messages", messages).put("temperature", 0.2d);
        if (applyReasoning) {
            Intrinsics.checkNotNull(payload);
            applyReasoning(payload, settings);
        } else {
            payload.put("reasoning", new JSONObject().put("enabled", false));
        }
        String string = payload.toString();
        Intrinsics.checkNotNullExpressionValue(string, "toString(...)");
        byte[] bytes = string.getBytes(Charsets.UTF_8);
        Intrinsics.checkNotNullExpressionValue(bytes, "getBytes(...)");
        return bytes;
    }

    private final String parseChatContent(String body) throws JSONException {
        JSONObject message = new JSONObject(body).getJSONArray("choices").getJSONObject(0).getJSONObject("message");
        String strOptString = message.optString("content");
        Intrinsics.checkNotNullExpressionValue(strOptString, "optString(...)");
        String content = StringsKt.trim((CharSequence) strOptString).toString();
        String str = content;
        if (StringsKt.isBlank(str)) {
            throw new IllegalStateException("Cloud chat returned empty content".toString());
        }
        return str;
    }

    /* JADX WARN: Code duplicated, block: B:27:0x00dc  */
    /* JADX WARN: Code duplicated, block: B:29:0x00e8  */
    /* JADX WARN: Code duplicated, block: B:47:0x02f9 A[Catch: all -> 0x0300, TRY_LEAVE, TryCatch #0 {all -> 0x0300, blocks: (B:45:0x02f3, B:47:0x02f9), top: B:87:0x02f3 }] */
    /* JADX WARN: Code duplicated, block: B:51:0x0311  */
    /* JADX WARN: Code duplicated, block: B:63:0x0397 A[Catch: all -> 0x03a7, TryCatch #5 {all -> 0x03a7, blocks: (B:61:0x034a, B:63:0x0397, B:65:0x039a, B:66:0x03a6), top: B:97:0x034a }] */
    /* JADX WARN: Code duplicated, block: B:65:0x039a A[Catch: all -> 0x03a7, TryCatch #5 {all -> 0x03a7, blocks: (B:61:0x034a, B:63:0x0397, B:65:0x039a, B:66:0x03a6), top: B:97:0x034a }] */
    /* JADX WARN: Code duplicated, block: B:7:0x001c  */
    /* JADX WARN: Code duplicated, block: B:87:0x02f3 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    public final Object benchChat(String userPrompt, String systemPrompt, String model, long timeoutMs, boolean disableReasoning, Continuation<? super String> continuation) throws Throwable {
        AnonymousClass1 anonymousClass1;
        String cloudCleanupModel;
        String str;
        String modelId;
        String str2;
        JSONObject payload;
        OkHttpClient client;
        long started;
        String str3;
        Object objExecute;
        PipelineSettings settings;
        Object $result;
        String string;
        String body;
        long elapsed;
        JSONObject message;
        String content;
        int reasoningChars;
        JSONArray jSONArrayOptJSONArray;
        String string2;
        int length;
        String str4;
        String str5;
        long timeoutMs2 = timeoutMs;
        boolean disableReasoning2 = disableReasoning;
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
        Object $result2 = anonymousClass2.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (anonymousClass2.label) {
            case 0:
                ResultKt.throwOnFailure($result2);
                PipelineSettings settings2 = this.config.load();
                if (StringsKt.isBlank(settings2.getCloudApiKey())) {
                    throw new IllegalArgumentException("Cloud API key not set".toString());
                }
                if (model == null || (string = StringsKt.trim((CharSequence) model).toString()) == null) {
                    cloudCleanupModel = settings2.getCloudCleanupModel();
                    if (StringsKt.isBlank(cloudCleanupModel)) {
                        cloudCleanupModel = "google/gemini-2.5-flash";
                    }
                    str = cloudCleanupModel;
                } else {
                    String str6 = string;
                    if (StringsKt.isBlank(str6)) {
                        str6 = null;
                    }
                    str = str6;
                    if (str == null) {
                        cloudCleanupModel = settings2.getCloudCleanupModel();
                        if (StringsKt.isBlank(cloudCleanupModel)) {
                            cloudCleanupModel = "google/gemini-2.5-flash";
                        }
                        str = cloudCleanupModel;
                    }
                }
                modelId = OpenRouterModels.INSTANCE.normalizeId(str);
                JSONArray messages = new JSONArray().put(new JSONObject().put("role", "system").put("content", systemPrompt)).put(new JSONObject().put("role", "user").put("content", userPrompt));
                str2 = " elapsedMs=";
                JSONObject payload2 = new JSONObject().put("model", modelId).put("messages", messages).put("temperature", 0.2d);
                if (disableReasoning2) {
                    payload2.put("reasoning", new JSONObject().put("enabled", false));
                }
                String string3 = payload2.toString();
                Intrinsics.checkNotNullExpressionValue(string3, "toString(...)");
                byte[] bodyBytes = string3.getBytes(Charsets.UTF_8);
                Intrinsics.checkNotNullExpressionValue(bodyBytes, "getBytes(...)");
                Log.i(TAG, "BENCH disableReasoning=" + disableReasoning2);
                Request request = baseRequest(settings2, StringsKt.trimEnd(settings2.getCloudBaseUrl(), '/') + "/chat/completions").post(RequestBody.Companion.create$default(RequestBody.INSTANCE, bodyBytes, JSON, 0, 0, 6, (Object) null)).build();
                payload = payload2;
                client = new OkHttpClient.Builder().dns(ResilientDns.INSTANCE).connectTimeout(PipelineTelemetry.HTTP_CONNECT_TIMEOUT_MS, TimeUnit.MILLISECONDS).readTimeout(timeoutMs2, TimeUnit.MILLISECONDS).writeTimeout(timeoutMs2, TimeUnit.MILLISECONDS).callTimeout(timeoutMs2, TimeUnit.MILLISECONDS).retryOnConnectionFailure(false).eventListenerFactory(new EventListener.Factory() { // from class: com.varun.pocketassistant.pipeline.OpenAiCompatibleClient$$ExternalSyntheticLambda0
                    @Override // okhttp3.EventListener.Factory
                    public final EventListener create(Call call) {
                        return OpenAiCompatibleClient.benchChat$lambda$15(call);
                    }
                }).build();
                started = System.currentTimeMillis();
                Log.i(TAG, "BENCH POST model=" + modelId + " promptChars=" + userPrompt.length() + " bodyBytes=" + bodyBytes.length + " timeoutMs=" + timeoutMs2);
                try {
                    anonymousClass2.L$0 = SpillingKt.nullOutSpilledVariable(userPrompt);
                    anonymousClass2.L$1 = SpillingKt.nullOutSpilledVariable(systemPrompt);
                    anonymousClass2.L$2 = SpillingKt.nullOutSpilledVariable(model);
                    anonymousClass2.L$3 = SpillingKt.nullOutSpilledVariable(settings2);
                    anonymousClass2.L$4 = modelId;
                    anonymousClass2.L$5 = SpillingKt.nullOutSpilledVariable(messages);
                    anonymousClass2.L$6 = SpillingKt.nullOutSpilledVariable(payload);
                    anonymousClass2.L$7 = SpillingKt.nullOutSpilledVariable(bodyBytes);
                    anonymousClass2.L$8 = SpillingKt.nullOutSpilledVariable(request);
                    anonymousClass2.L$9 = SpillingKt.nullOutSpilledVariable(client);
                    anonymousClass2.J$0 = timeoutMs2;
                    anonymousClass2.Z$0 = disableReasoning2;
                    anonymousClass2.J$1 = started;
                    anonymousClass2.label = 1;
                    objExecute = execute(client, request, anonymousClass2);
                    if (objExecute == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    settings = settings2;
                    $result = bodyBytes;
                    try {
                        body = (String) objExecute;
                        elapsed = System.currentTimeMillis() - started;
                        long timeoutMs3 = timeoutMs2;
                        try {
                            message = new JSONObject(body).getJSONArray("choices").getJSONObject(0).getJSONObject("message");
                            String strOptString = message.optString("content");
                            try {
                                Intrinsics.checkNotNullExpressionValue(strOptString, "optString(...)");
                                content = StringsKt.trim((CharSequence) strOptString).toString();
                                reasoningChars = message.optString("reasoning").length();
                                jSONArrayOptJSONArray = message.optJSONArray("reasoning_details");
                                if (jSONArrayOptJSONArray != null) {
                                    length = 0;
                                    int reasoningDetailsChars = length;
                                    int length2 = body.length();
                                    int length3 = content.length();
                                    Iterator<String> itKeys = message.keys();
                                    Intrinsics.checkNotNullExpressionValue(itKeys, "keys(...)");
                                    str4 = str2;
                                    Log.i(TAG, "BENCH ok model=" + modelId + str4 + elapsed + " respChars=" + length2 + " contentChars=" + length3 + " reasoningChars=" + reasoningChars + " reasoningDetailsChars=" + reasoningDetailsChars + " msgKeys=" + SequencesKt.toList(SequencesKt.asSequence(itKeys)));
                                    str5 = content;
                                    if (StringsKt.isBlank(str5)) {
                                        throw new IllegalStateException("Cloud chat returned empty content".toString());
                                    }
                                    return str5;
                                }
                                try {
                                    string2 = jSONArrayOptJSONArray.toString();
                                    if (string2 != null) {
                                        length = string2.length();
                                    } else {
                                        length = 0;
                                    }
                                    int reasoningDetailsChars2 = length;
                                    int length4 = body.length();
                                    int length5 = content.length();
                                    try {
                                        Iterator<String> itKeys2 = message.keys();
                                        try {
                                            Intrinsics.checkNotNullExpressionValue(itKeys2, "keys(...)");
                                            try {
                                                str4 = str2;
                                                try {
                                                    Log.i(TAG, "BENCH ok model=" + modelId + str4 + elapsed + " respChars=" + length4 + " contentChars=" + length5 + " reasoningChars=" + reasoningChars + " reasoningDetailsChars=" + reasoningDetailsChars2 + " msgKeys=" + SequencesKt.toList(SequencesKt.asSequence(itKeys2)));
                                                    str5 = content;
                                                    if (StringsKt.isBlank(str5)) {
                                                        return str5;
                                                    }
                                                    throw new IllegalStateException("Cloud chat returned empty content".toString());
                                                } catch (Throwable th) {
                                                    t = th;
                                                    timeoutMs2 = timeoutMs3;
                                                    str3 = str4;
                                                }
                                            } catch (Throwable th2) {
                                                t = th2;
                                                timeoutMs2 = timeoutMs3;
                                                str3 = str2;
                                            }
                                        } catch (Throwable th3) {
                                            t = th3;
                                            timeoutMs2 = timeoutMs3;
                                            str3 = str2;
                                        }
                                    } catch (Throwable th4) {
                                        t = th4;
                                        timeoutMs2 = timeoutMs3;
                                        str3 = str2;
                                    }
                                } catch (Throwable th5) {
                                    t = th5;
                                    timeoutMs2 = timeoutMs3;
                                    str3 = str2;
                                }
                            } catch (Throwable th6) {
                                t = th6;
                                timeoutMs2 = timeoutMs3;
                                str3 = str2;
                            }
                        } catch (Throwable th7) {
                            t = th7;
                            timeoutMs2 = timeoutMs3;
                            str3 = str2;
                        }
                    } catch (Throwable th8) {
                        t = th8;
                        str3 = str2;
                    }
                    Log.e(TAG, "BENCH FAIL model=" + modelId + str3 + (System.currentTimeMillis() - started) + " err=" + t.getMessage(), t);
                    throw t;
                } catch (Throwable th9) {
                    t = th9;
                    str3 = str2;
                }
                break;
            case 1:
                started = anonymousClass2.J$1;
                disableReasoning2 = anonymousClass2.Z$0;
                timeoutMs2 = anonymousClass2.J$0;
                client = (OkHttpClient) anonymousClass2.L$9;
                Object bodyBytes2 = (byte[]) anonymousClass2.L$7;
                JSONObject payload3 = (JSONObject) anonymousClass2.L$6;
                String modelId2 = (String) anonymousClass2.L$4;
                PipelineSettings settings3 = (PipelineSettings) anonymousClass2.L$3;
                try {
                    ResultKt.throwOnFailure($result2);
                    objExecute = $result2;
                    modelId = modelId2;
                    $result = bodyBytes2;
                    settings = settings3;
                    payload = payload3;
                    str2 = " elapsedMs=";
                    body = (String) objExecute;
                    elapsed = System.currentTimeMillis() - started;
                    long timeoutMs4 = timeoutMs2;
                    message = new JSONObject(body).getJSONArray("choices").getJSONObject(0).getJSONObject("message");
                    String strOptString2 = message.optString("content");
                    Intrinsics.checkNotNullExpressionValue(strOptString2, "optString(...)");
                    content = StringsKt.trim((CharSequence) strOptString2).toString();
                    reasoningChars = message.optString("reasoning").length();
                    jSONArrayOptJSONArray = message.optJSONArray("reasoning_details");
                    if (jSONArrayOptJSONArray != null) {
                        length = 0;
                        int reasoningDetailsChars3 = length;
                        int length6 = body.length();
                        int length7 = content.length();
                        Iterator<String> itKeys3 = message.keys();
                        Intrinsics.checkNotNullExpressionValue(itKeys3, "keys(...)");
                        str4 = str2;
                        Log.i(TAG, "BENCH ok model=" + modelId + str4 + elapsed + " respChars=" + length6 + " contentChars=" + length7 + " reasoningChars=" + reasoningChars + " reasoningDetailsChars=" + reasoningDetailsChars3 + " msgKeys=" + SequencesKt.toList(SequencesKt.asSequence(itKeys3)));
                        str5 = content;
                        if (StringsKt.isBlank(str5)) {
                            return str5;
                        }
                        throw new IllegalStateException("Cloud chat returned empty content".toString());
                    }
                    string2 = jSONArrayOptJSONArray.toString();
                    if (string2 != null) {
                        length = string2.length();
                    } else {
                        length = 0;
                    }
                    int reasoningDetailsChars4 = length;
                    int length8 = body.length();
                    int length9 = content.length();
                    Iterator<String> itKeys4 = message.keys();
                    Intrinsics.checkNotNullExpressionValue(itKeys4, "keys(...)");
                    str4 = str2;
                    Log.i(TAG, "BENCH ok model=" + modelId + str4 + elapsed + " respChars=" + length8 + " contentChars=" + length9 + " reasoningChars=" + reasoningChars + " reasoningDetailsChars=" + reasoningDetailsChars4 + " msgKeys=" + SequencesKt.toList(SequencesKt.asSequence(itKeys4)));
                    str5 = content;
                    if (StringsKt.isBlank(str5)) {
                        return str5;
                    }
                    throw new IllegalStateException("Cloud chat returned empty content".toString());
                } catch (Throwable th10) {
                    t = th10;
                    str3 = " elapsedMs=";
                    modelId = modelId2;
                }
                Log.e(TAG, "BENCH FAIL model=" + modelId + str3 + (System.currentTimeMillis() - started) + " err=" + t.getMessage(), t);
                throw t;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final EventListener benchChat$lambda$15(Call it) {
        Intrinsics.checkNotNullParameter(it, "it");
        return new BenchEventListener();
    }

    private final void applyReasoning(JSONObject payload, PipelineSettings settings) throws JSONException {
        String effort = StringsKt.trim((CharSequence) settings.getReasoningEffort()).toString();
        JSONObject reasoning = new JSONObject();
        if ((effort.length() == 0) || Intrinsics.areEqual(effort, "none")) {
            reasoning.put("enabled", false);
        } else {
            reasoning.put("effort", effort);
            reasoning.put("enabled", true);
            if (settings.getExcludeReasoningFromResponse()) {
                reasoning.put("exclude", true);
            }
        }
        payload.put("reasoning", reasoning);
    }

    public final Object chatCleanup(String prompt, Continuation<? super String> continuation) {
        return chat$default(this, prompt, null, null, false, null, continuation, 30, null);
    }

    private final Request.Builder baseRequest(PipelineSettings settings, String url) {
        Request.Builder builder = new Request.Builder().url(url).header(HttpHeaders.AUTHORIZATION, "Bearer " + settings.getCloudApiKey());
        if (settings.isOpenRouter()) {
            builder.header("HTTP-Referer", OPENROUTER_REFERER);
            builder.header("X-Title", OPENROUTER_TITLE);
        }
        return builder;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final Object execute(OkHttpClient client, Request request, Continuation<? super String> continuation) {
        CancellableContinuationImpl cancellableContinuationImpl = new CancellableContinuationImpl(IntrinsicsKt.intercepted(continuation), 1);
        cancellableContinuationImpl.initCancellability();
        final CancellableContinuationImpl cancellableContinuationImpl2 = cancellableContinuationImpl;
        final Call callNewCall = client.newCall(request);
        cancellableContinuationImpl2.invokeOnCancellation(new Function1<Throwable, Unit>() { // from class: com.varun.pocketassistant.pipeline.OpenAiCompatibleClient$execute$2$1
            @Override // kotlin.jvm.functions.Function1
            public /* bridge */ /* synthetic */ Unit invoke(Throwable th) {
                invoke2(th);
                return Unit.INSTANCE;
            }

            /* JADX INFO: renamed from: invoke, reason: avoid collision after fix types in other method */
            public final void invoke2(Throwable it) {
                callNewCall.cancel();
            }
        });
        callNewCall.enqueue(new Callback() { // from class: com.varun.pocketassistant.pipeline.OpenAiCompatibleClient$execute$2$2
            @Override // okhttp3.Callback
            public void onFailure(Call call, IOException e) {
                Intrinsics.checkNotNullParameter(call, "call");
                Intrinsics.checkNotNullParameter(e, "e");
                if (cancellableContinuationImpl2.isActive()) {
                    CancellableContinuation<String> cancellableContinuation = cancellableContinuationImpl2;
                    Result.Companion companion = Result.INSTANCE;
                    cancellableContinuation.resumeWith(Result.m8304constructorimpl(ResultKt.createFailure(e)));
                }
            }

            @Override // okhttp3.Callback
            public void onResponse(Call call, Response response) {
                Intrinsics.checkNotNullParameter(call, "call");
                Intrinsics.checkNotNullParameter(response, "response");
                Response response2 = response;
                CancellableContinuation<String> cancellableContinuation = cancellableContinuationImpl2;
                try {
                    Response response3 = response2;
                    ResponseBody responseBodyBody = response3.body();
                    String strString = responseBodyBody != null ? responseBodyBody.string() : null;
                    if (strString == null) {
                        strString = "";
                    }
                    if (!cancellableContinuation.isActive()) {
                        CloseableKt.closeFinally(response2, null);
                        return;
                    }
                    if (!response3.isSuccessful()) {
                        Result.Companion companion = Result.INSTANCE;
                        cancellableContinuation.resumeWith(Result.m8304constructorimpl(ResultKt.createFailure(new IllegalStateException(OpenAiCompatibleClient.INSTANCE.formatCloudHttpError("Cloud", response3.code(), strString)))));
                    } else {
                        Result.Companion companion2 = Result.INSTANCE;
                        cancellableContinuation.resumeWith(Result.m8304constructorimpl(strString));
                    }
                    Unit unit = Unit.INSTANCE;
                    CloseableKt.closeFinally(response2, null);
                } catch (Throwable th) {
                    try {
                        throw th;
                    } catch (Throwable th2) {
                        CloseableKt.closeFinally(response2, th);
                        throw th2;
                    }
                }
            }
        });
        Object result = cancellableContinuationImpl.getResult();
        if (result == IntrinsicsKt.getCOROUTINE_SUSPENDED()) {
            DebugProbesKt.probeCoroutineSuspended(continuation);
        }
        return result;
    }

    /* JADX WARN: Multi-variable type inference failed */
    private final TranscriptionResult parseTranscriptionResponse(String body, String label, boolean diarize) {
        JSONObject jSONObject = new JSONObject(body);
        String strOptString = jSONObject.optString("text");
        Intrinsics.checkNotNullExpressionValue(strOptString, "optString(...)");
        String string = StringsKt.trim((CharSequence) strOptString).toString();
        Log.i(TAG, label + " ok (" + string.length() + " chars)");
        int i = 2;
        List list = null;
        Object[] objArr = 0;
        Object[] objArr2 = 0;
        Object[] objArr3 = 0;
        if (!diarize) {
            return new TranscriptionResult(string, list, i, objArr3 == true ? 1 : 0);
        }
        JSONArray jSONArrayOptJSONArray = jSONObject.optJSONArray("words");
        if (jSONArrayOptJSONArray == null) {
            return new TranscriptionResult(string, objArr2 == true ? 1 : 0, i, objArr == true ? 1 : 0);
        }
        List listCreateListBuilder = CollectionsKt.createListBuilder();
        int i2 = 0;
        int length = jSONArrayOptJSONArray.length();
        while (i2 < length) {
            JSONObject jSONObjectOptJSONObject = jSONArrayOptJSONArray.optJSONObject(i2);
            if (jSONObjectOptJSONObject != null && jSONObjectOptJSONObject.has("speaker")) {
                String strOptString2 = jSONObjectOptJSONObject.optString("word");
                if (StringsKt.isBlank(strOptString2)) {
                    strOptString2 = jSONObjectOptJSONObject.optString("text");
                }
                Intrinsics.checkNotNullExpressionValue(strOptString2, "ifBlank(...)");
                String string2 = StringsKt.trim((CharSequence) strOptString2).toString();
                if (!(string2.length() == 0)) {
                    listCreateListBuilder.add(new DiarWord(string2, jSONObjectOptJSONObject.optDouble("start", 0.0d), jSONObjectOptJSONObject.optDouble("end", 0.0d), jSONObjectOptJSONObject.getInt("speaker")));
                }
            }
            i2++;
            jSONObject = jSONObject;
        }
        List listBuild = CollectionsKt.build(listCreateListBuilder);
        int size = listBuild.size();
        List list2 = listBuild;
        ArrayList arrayList = new ArrayList(CollectionsKt.collectionSizeOrDefault(list2, 10));
        Iterator it = list2.iterator();
        while (it.hasNext()) {
            arrayList.add(Integer.valueOf(((DiarWord) it.next()).getSpeaker()));
        }
        Log.i(TAG, label + " diarize words=" + size + " speakers=" + CollectionsKt.toSet(arrayList).size());
        return new TranscriptionResult(string, listBuild);
    }

    private final String readTranscriptionResponse(String body, String label) {
        return parseTranscriptionResponse(body, label, false).getText();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Can't wrap try/catch for region: R(4:20|63|21|(1:23)(5:24|67|26|(1:28)|29)) */
    /* JADX WARN: Can't wrap try/catch for region: R(5:24|67|26|(1:28)|29) */
    /* JADX WARN: Code duplicated, block: B:20:0x00c8  */
    /* JADX WARN: Code duplicated, block: B:23:0x00e7 A[RETURN] */
    /* JADX WARN: Code duplicated, block: B:24:0x00e8  */
    /* JADX WARN: Code duplicated, block: B:28:0x00f7 A[Catch: all -> 0x00fb, TRY_LEAVE, TryCatch #2 {all -> 0x00fb, blocks: (B:26:0x00f3, B:28:0x00f7), top: B:67:0x00f3 }] */
    /* JADX WARN: Code duplicated, block: B:36:0x0115  */
    /* JADX WARN: Code duplicated, block: B:48:0x0228 A[ADDED_TO_REGION, REMOVE] */
    /* JADX WARN: Code duplicated, block: B:51:0x0237  */
    /* JADX WARN: Code duplicated, block: B:54:0x0243  */
    /* JADX WARN: Code duplicated, block: B:56:0x0247  */
    /* JADX WARN: Code duplicated, block: B:59:0x0252  */
    /* JADX WARN: Code duplicated, block: B:61:0x026f  */
    /* JADX WARN: Code duplicated, block: B:7:0x0018  */
    /* JADX WARN: Code restructure failed: missing block: B:30:0x00fb, code lost:
    
        r0 = move-exception;
        r0 = (T) r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:31:0x00fc, code lost:
    
        r4 = r6;
        r6 = r1;
        r1 = r13;
        r13 = r4;
        r4 = r8;
     */
    /* JADX WARN: Code restructure failed: missing block: B:32:0x0102, code lost:
    
        r0 = move-exception;
        r0 = (T) r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:33:0x0103, code lost:
    
        r14 = r1;
        r1 = r3;
        r3 = r7;
        r7 = r12;
        r12 = r5;
        r5 = r9;
        r9 = r4;
        r4 = r8;
     */
    /* JADX WARN: Code restructure failed: missing block: B:40:0x012a, code lost:
    
        if (com.varun.pocketassistant.pipeline.OpenAiCompatibleClient.INSTANCE.isDnsFailure((java.lang.Throwable) r0) != false) goto L41;
     */
    /* JADX WARN: Code restructure failed: missing block: B:41:0x012c, code lost:
    
        r8 = r7;
        r4 = kotlin.ranges.RangesKt.coerceAtMost(((long) (r7 + 1)) * 2000, com.varun.pocketassistant.pipeline.PipelineTelemetry.HTTP_CONNECT_TIMEOUT_MS);
     */
    /* JADX WARN: Code restructure failed: missing block: B:42:0x0140, code lost:
    
        r8 = r7;
        r4 = kotlin.ranges.RangesKt.coerceAtMost(((long) (1 << r8)) * 1000, 6000L);
     */
    /* JADX WARN: Code restructure failed: missing block: B:43:0x0154, code lost:
    
        r6 = (long) (r4 * ((java.lang.Math.random() * 0.3d) + 0.2d));
        r6 = r4 + r6;
        r28 = r8;
        r30 = r0;
        r19 = r2;
        android.util.Log.w(com.varun.pocketassistant.pipeline.OpenAiCompatibleClient.TAG, r14 + " attempt " + (r28 + 1) + "/" + r9 + " failed (" + r0.getClass().getSimpleName() + ": " + r30.getMessage() + "); retry in " + r6 + "ms");
        r3.L$0 = r14;
        r3.L$1 = r1;
        r3.L$2 = r12;
        r3.L$3 = r11;
        r3.L$4 = kotlin.coroutines.jvm.internal.SpillingKt.nullOutSpilledVariable(r30);
        r3.I$0 = r9;
        r3.I$1 = r10;
        r3.I$2 = r28;
        r3.I$3 = r13;
        r3.J$0 = r4;
        r3.J$1 = r6;
        r3.J$2 = r6;
        r3.label = 2;
        r15 = r5;
     */
    /* JADX WARN: Code restructure failed: missing block: B:44:0x01fd, code lost:
    
        if (kotlinx.coroutines.DelayKt.delay(r6, r3) == r15) goto L45;
     */
    /* JADX WARN: Code restructure failed: missing block: B:45:0x01ff, code lost:
    
        return r15;
     */
    /* JADX WARN: Code restructure failed: missing block: B:46:0x0200, code lost:
    
        r18 = r10;
        r2 = r19;
        r10 = r3;
        r3 = r1;
        r1 = r14;
        r14 = r11;
        r11 = r4;
        r7 = r6;
        r4 = r9;
        r5 = r12;
     */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:46:0x0200 -> B:47:0x021b). Please report as a decompilation issue!!! */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final <T> java.lang.Object withRetries(java.lang.String r27, java.lang.String r28, int r29, kotlin.jvm.functions.Function1<? super kotlin.coroutines.Continuation<? super T>, ? extends java.lang.Object> r30, kotlin.coroutines.Continuation<? super T> r31) throws java.lang.Throwable {
        /*
            Method dump skipped, instruction units count: 638
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.varun.pocketassistant.pipeline.OpenAiCompatibleClient.withRetries(java.lang.String, java.lang.String, int, kotlin.jvm.functions.Function1, kotlin.coroutines.Continuation):java.lang.Object");
    }

    static /* synthetic */ Object withRetries$default(OpenAiCompatibleClient openAiCompatibleClient, String str, String str2, int i, Function1 function1, Continuation continuation, int i2, Object obj) {
        int i3;
        if ((i2 & 4) == 0) {
            i3 = i;
        } else {
            i3 = 3;
        }
        return openAiCompatibleClient.withRetries(str, str2, i3, function1, continuation);
    }

    /* JADX INFO: compiled from: CloudAndLocalProviders.kt */
    @Metadata(d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\t\n\u0002\b\u0004\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0003\n\u0002\b\u000b\b\u0086\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003J\u000e\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u0012J\u0010\u0010\u0013\u001a\u00020\u00052\u0006\u0010\u0014\u001a\u00020\u0005H\u0002J\u0010\u0010\u0015\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u0012H\u0002J\u0010\u0010\u0016\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u0012H\u0002J \u0010\u0017\u001a\u00020\u00122\u0006\u0010\u0018\u001a\u00020\u00052\u0006\u0010\u0019\u001a\u00020\u00052\u0006\u0010\u0011\u001a\u00020\u0012H\u0002J \u0010\u001a\u001a\u00020\u00052\u0006\u0010\u0018\u001a\u00020\u00052\u0006\u0010\u001b\u001a\u00020\f2\u0006\u0010\u001c\u001a\u00020\u0005H\u0002R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0005X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0005X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0005X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u001d"}, d2 = {"Lcom/varun/pocketassistant/pipeline/OpenAiCompatibleClient$Companion;", "", "<init>", "()V", "TAG", "", "MULTIPART_SAFE_BYTES", "", "OPENROUTER_REFERER", "OPENROUTER_TITLE", "DEFAULT_SYSTEM", "MAX_ATTEMPTS", "", "JSON", "Lokhttp3/MediaType;", "isTransientTransportError", "", "t", "", "hostOf", "baseUrl", "isDnsFailure", "isRetryable", "humanizeNetworkError", "label", "host", "formatCloudHttpError", "code", "body", "app_debug"}, k = 1, mv = {2, 2, 0}, xi = 48)
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        public final boolean isTransientTransportError(Throwable t) {
            Intrinsics.checkNotNullParameter(t, "t");
            return isRetryable(t);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public final String hostOf(String baseUrl) {
            String strHost;
            String trimmed = StringsKt.trimEnd(StringsKt.trim((CharSequence) baseUrl).toString(), '/');
            HttpUrl httpUrl = HttpUrl.INSTANCE.parse(trimmed + "/");
            if (httpUrl != null && (strHost = httpUrl.host()) != null) {
                return strHost;
            }
            return StringsKt.substringBefore$default(StringsKt.removePrefix(StringsKt.removePrefix(trimmed, (CharSequence) "https://"), (CharSequence) "http://"), '/', (String) null, 2, (Object) null);
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* JADX WARN: Code duplicated, block: B:18:0x004b  */
        public final boolean isDnsFailure(Throwable t) {
            boolean z;
            Iterator it = SequencesKt.generateSequence(t, (Function1<? super Throwable, ? extends Throwable>) new Function1() { // from class: com.varun.pocketassistant.pipeline.OpenAiCompatibleClient$Companion$$ExternalSyntheticLambda0
                @Override // kotlin.jvm.functions.Function1
                public final Object invoke(Object obj) {
                    return OpenAiCompatibleClient.Companion.isDnsFailure$lambda$0((Throwable) obj);
                }
            }).iterator();
            do {
                z = false;
                if (!it.hasNext()) {
                    return false;
                }
                Throwable th = (Throwable) it.next();
                if (th instanceof UnknownHostException) {
                    z = true;
                } else {
                    String message = th.getMessage();
                    if (message == null) {
                        message = "";
                    }
                    if (StringsKt.contains((CharSequence) message, (CharSequence) "Unable to resolve host", true)) {
                        z = true;
                    } else {
                        String message2 = th.getMessage();
                        if (StringsKt.contains((CharSequence) (message2 != null ? message2 : ""), (CharSequence) "No address associated with hostname", true)) {
                            z = true;
                        }
                    }
                }
            } while (!z);
            return true;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static final Throwable isDnsFailure$lambda$0(Throwable it) {
            Intrinsics.checkNotNullParameter(it, "it");
            return it.getCause();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public final boolean isRetryable(Throwable t) {
            for (Throwable th : SequencesKt.generateSequence(t, (Function1<? super Throwable, ? extends Throwable>) new Function1() { // from class: com.varun.pocketassistant.pipeline.OpenAiCompatibleClient$Companion$$ExternalSyntheticLambda1
                @Override // kotlin.jvm.functions.Function1
                public final Object invoke(Object obj) {
                    return OpenAiCompatibleClient.Companion.isRetryable$lambda$2((Throwable) obj);
                }
            })) {
                if ((th instanceof SocketException) || (th instanceof SocketTimeoutException) || (th instanceof InterruptedIOException) || (th instanceof UnknownHostException) || (th instanceof SSLException)) {
                    return true;
                }
                String message = th.getMessage();
                if (message == null) {
                    message = "";
                }
                if (StringsKt.contains((CharSequence) message, (CharSequence) "connection abort", true) || StringsKt.contains((CharSequence) message, (CharSequence) "Broken pipe", true) || StringsKt.contains((CharSequence) message, (CharSequence) "Connection reset", true) || StringsKt.contains((CharSequence) message, (CharSequence) "failed to connect", true) || StringsKt.contains((CharSequence) message, (CharSequence) "Unable to resolve host", true) || StringsKt.contains((CharSequence) message, (CharSequence) "No address associated with hostname", true)) {
                    return true;
                }
                if ((th instanceof IllegalStateException) && (StringsKt.contains$default((CharSequence) message, (CharSequence) "HTTP 429", false, 2, (Object) null) || StringsKt.contains$default((CharSequence) message, (CharSequence) "HTTP 502", false, 2, (Object) null) || StringsKt.contains$default((CharSequence) message, (CharSequence) "HTTP 503", false, 2, (Object) null) || StringsKt.contains$default((CharSequence) message, (CharSequence) "HTTP 504", false, 2, (Object) null))) {
                    return true;
                }
            }
            return false;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static final Throwable isRetryable$lambda$2(Throwable it) {
            Intrinsics.checkNotNullParameter(it, "it");
            return it.getCause();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public final Throwable humanizeNetworkError(String label, String host, Throwable t) {
            if (isDnsFailure(t)) {
                return new IllegalStateException(label + " can’t resolve " + host + " right now. Toggle Airplane mode, disable Private DNS/VPN, or switch Wi‑Fi↔cellular, then Retry.", t);
            }
            String msg = t.getMessage();
            if (msg == null) {
                msg = "";
            }
            if (StringsKt.contains((CharSequence) msg, (CharSequence) "connection abort", true) || (t instanceof SocketException)) {
                return new IllegalStateException(label + " network interrupted (" + msg + "). Tap Retry — usually a flaky Wi‑Fi/VPN hop.", t);
            }
            return t;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public final String formatCloudHttpError(String label, int code, String body) {
            Object objM8304constructorimpl;
            String strOptString;
            String tip = null;
            try {
                Result.Companion companion = Result.INSTANCE;
                Companion companion2 = this;
                JSONObject jSONObjectOptJSONObject = new JSONObject(body).optJSONObject("error");
                if (jSONObjectOptJSONObject == null || (strOptString = jSONObjectOptJSONObject.optString("message")) == null || StringsKt.isBlank(strOptString)) {
                    strOptString = null;
                }
                objM8304constructorimpl = Result.m8304constructorimpl(strOptString);
            } catch (Throwable th) {
                Result.Companion companion3 = Result.INSTANCE;
                objM8304constructorimpl = Result.m8304constructorimpl(ResultKt.createFailure(th));
            }
            if (Result.m8310isFailureimpl(objM8304constructorimpl)) {
                objM8304constructorimpl = null;
            }
            String message = (String) objM8304constructorimpl;
            if (message != null) {
                if (StringsKt.contains((CharSequence) message, (CharSequence) "not a valid model", true)) {
                    tip = " Re-pick the model in Pipeline (OpenRouter “-latest” aliases need a leading ~).";
                } else if (code == 429) {
                    tip = " Rate limited — retry shortly or pick another model.";
                }
            }
            StringBuilder sb = new StringBuilder();
            sb.append(label);
            sb.append(" HTTP ");
            sb.append(code);
            sb.append(": ");
            sb.append(message == null ? StringsKt.take(body, 240) : message);
            if (tip != null) {
                sb.append(tip);
            }
            return sb.toString();
        }
    }
}
