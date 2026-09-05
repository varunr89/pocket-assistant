package com.varun.pocketassistant.pipeline;

import android.content.Context;
import android.util.Log;
import com.google.ai.edge.litertlm.Backend;
import com.google.ai.edge.litertlm.Content;
import com.google.ai.edge.litertlm.Contents;
import com.google.ai.edge.litertlm.Conversation;
import com.google.ai.edge.litertlm.ConversationConfig;
import com.google.ai.edge.litertlm.Engine;
import com.google.ai.edge.litertlm.EngineConfig;
import com.google.ai.edge.litertlm.Message;
import com.google.ai.edge.litertlm.SamplerConfig;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import kotlin.Metadata;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jdk7.AutoCloseableKt;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import kotlinx.coroutines.BuildersKt;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.Dispatchers;
import kotlinx.coroutines.sync.Mutex;
import kotlinx.coroutines.sync.MutexKt;

/* JADX INFO: compiled from: GemmaLocalModels.kt */
/* JADX INFO: loaded from: classes8.dex */
@Metadata(d1 = {"\u0000J\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\f\n\u0002\u0018\u0002\n\u0002\b\u0003\bÇ\u0002\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003J\u000e\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nJ\u000e\u0010\u000b\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nJ\u000e\u0010\f\u001a\u00020\r2\u0006\u0010\t\u001a\u00020\nJ.\u0010\u0013\u001a\u00020\u00052\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u0014\u001a\u00020\u00052\u0006\u0010\u0015\u001a\u00020\r2\u0006\u0010\u0016\u001a\u00020\u0017H\u0086@¢\u0006\u0002\u0010\u0018J&\u0010\u0019\u001a\u00020\u00052\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u001a\u001a\u00020\u00052\u0006\u0010\u0016\u001a\u00020\u0017H\u0086@¢\u0006\u0002\u0010\u001bJ&\u0010\u001c\u001a\u00020\u00052\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u001d\u001a\u00020\u00052\u0006\u0010\u001e\u001a\u00020\u0005H\u0086@¢\u0006\u0002\u0010\u001fJ \u0010 \u001a\u00020\u00052\u0006\u0010!\u001a\u00020\u00102\u0006\u0010\u001d\u001a\u00020\u00052\u0006\u0010\u001e\u001a\u00020\u0005H\u0002J\u0010\u0010\"\u001a\u00020\u00052\u0006\u0010#\u001a\u00020$H\u0002J\u0010\u0010%\u001a\u00020\u00102\u0006\u0010\t\u001a\u00020\nH\u0002R\u000e\u0010\u0004\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u0016\u0010\u000e\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00100\u000fX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0012X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010&\u001a\u00020\u0005X\u0082T¢\u0006\u0002\n\u0000¨\u0006'"}, d2 = {"Lcom/varun/pocketassistant/pipeline/GemmaLocalModels;", "", "<init>", "()V", "REL_DIR", "", "MODEL_NAME", "dir", "Ljava/io/File;", "context", "Landroid/content/Context;", "modelFile", "isAvailable", "", "engineRef", "Ljava/util/concurrent/atomic/AtomicReference;", "Lcom/google/ai/edge/litertlm/Engine;", "mutex", "Lkotlinx/coroutines/sync/Mutex;", "cleanTranscript", "raw", "diarized", "settings", "Lcom/varun/pocketassistant/pipeline/PipelineSettings;", "(Landroid/content/Context;Ljava/lang/String;ZLcom/varun/pocketassistant/pipeline/PipelineSettings;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "summarize", "cleaned", "(Landroid/content/Context;Ljava/lang/String;Lcom/varun/pocketassistant/pipeline/PipelineSettings;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "prompt", "system", "user", "(Landroid/content/Context;Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "runPrompt", "engine", "messageText", "message", "Lcom/google/ai/edge/litertlm/Message;", "createEngine", "TAG", "app_debug"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class GemmaLocalModels {
    public static final String MODEL_NAME = "gemma-4-E2B-it_Google_Tensor_G5.litertlm";
    public static final String REL_DIR = "models/gemma4b";
    private static final String TAG = "GemmaLocalModels";
    public static final GemmaLocalModels INSTANCE = new GemmaLocalModels();
    private static final AtomicReference<Engine> engineRef = new AtomicReference<>(null);
    private static final Mutex mutex = MutexKt.Mutex$default(false, 1, null);
    public static final int $stable = 8;

    private GemmaLocalModels() {
    }

    public final File dir(Context context) {
        Intrinsics.checkNotNullParameter(context, "context");
        return new File(context.getFilesDir(), REL_DIR);
    }

    public final File modelFile(Context context) {
        Intrinsics.checkNotNullParameter(context, "context");
        return new File(dir(context), MODEL_NAME);
    }

    public final boolean isAvailable(Context context) {
        Intrinsics.checkNotNullParameter(context, "context");
        File f = modelFile(context);
        return f.exists() && f.length() > 1000000000;
    }

    public final Object cleanTranscript(Context context, String raw, boolean diarized, PipelineSettings settings, Continuation<? super String> continuation) {
        return prompt(context, "You clean speech transcripts. Output only the cleaned transcript text.", CleanupPrompts.INSTANCE.buildCleanup(settings, raw, diarized), continuation);
    }

    public final Object summarize(Context context, String cleaned, PipelineSettings settings, Continuation<? super String> continuation) {
        return prompt(context, "You summarize meetings. Output only the requested Markdown sections.", CleanupPrompts.INSTANCE.buildSummary(settings, cleaned), continuation);
    }

    /* JADX INFO: renamed from: com.varun.pocketassistant.pipeline.GemmaLocalModels$prompt$2, reason: invalid class name */
    /* JADX INFO: compiled from: GemmaLocalModels.kt */
    @Metadata(d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u000e\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H\n"}, d2 = {"<anonymous>", "", "Lkotlinx/coroutines/CoroutineScope;"}, k = 3, mv = {2, 2, 0}, xi = 48)
    @DebugMetadata(c = "com.varun.pocketassistant.pipeline.GemmaLocalModels$prompt$2", f = "GemmaLocalModels.kt", i = {0, 0}, l = {114}, m = "invokeSuspend", n = {"$this$withLock_u24default\\1", "$i$f$withLock\\1\\62"}, s = {"L$0", "I$0"})
    static final class AnonymousClass2 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super String>, Object> {
        final /* synthetic */ Context $context;
        final /* synthetic */ String $system;
        final /* synthetic */ String $user;
        int I$0;
        Object L$0;
        Object L$1;
        Object L$2;
        Object L$3;
        int label;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass2(Context context, String str, String str2, Continuation<? super AnonymousClass2> continuation) {
            super(2, continuation);
            this.$context = context;
            this.$system = str;
            this.$user = str2;
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
            return new AnonymousClass2(this.$context, this.$system, this.$user, continuation);
        }

        @Override // kotlin.jvm.functions.Function2
        public final Object invoke(CoroutineScope coroutineScope, Continuation<? super String> continuation) {
            return ((AnonymousClass2) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object $result) {
            Mutex mutex;
            Context context;
            String str;
            String str2;
            Object obj;
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    mutex = GemmaLocalModels.mutex;
                    context = this.$context;
                    str = this.$system;
                    str2 = this.$user;
                    obj = null;
                    this.L$0 = mutex;
                    this.L$1 = context;
                    this.L$2 = str;
                    this.L$3 = str2;
                    this.I$0 = 0;
                    this.label = 1;
                    if (mutex.lock(null, this) == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    break;
                case 1:
                    int i = this.I$0;
                    str2 = (String) this.L$3;
                    str = (String) this.L$2;
                    context = (Context) this.L$1;
                    obj = null;
                    mutex = (Mutex) this.L$0;
                    ResultKt.throwOnFailure($result);
                    break;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
            try {
                Context applicationContext = context.getApplicationContext();
                GemmaLocalModels gemmaLocalModels = GemmaLocalModels.INSTANCE;
                Intrinsics.checkNotNull(applicationContext);
                if (gemmaLocalModels.isAvailable(applicationContext)) {
                    Engine engineCreateEngine = (Engine) GemmaLocalModels.engineRef.get();
                    if (engineCreateEngine == null) {
                        engineCreateEngine = GemmaLocalModels.INSTANCE.createEngine(applicationContext);
                        GemmaLocalModels.engineRef.set(engineCreateEngine);
                    }
                    String strRunPrompt = GemmaLocalModels.INSTANCE.runPrompt(engineCreateEngine, str, str2);
                    mutex.unlock(obj);
                    return strRunPrompt;
                }
                throw new IllegalArgumentException("Gemma G5 model pack missing under files/models/gemma4b".toString());
            } catch (Throwable th) {
                mutex.unlock(obj);
                throw th;
            }
        }
    }

    public final Object prompt(Context context, String system, String user, Continuation<? super String> continuation) {
        return BuildersKt.withContext(Dispatchers.getDefault(), new AnonymousClass2(context, system, user, null), continuation);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final String runPrompt(Engine engine, String system, String user) throws Exception {
        Conversation conversationCreateConversation = engine.createConversation(new ConversationConfig(Contents.INSTANCE.of(system), null, null, new SamplerConfig(40, 0.95d, 0.2d, 0), false, null, null, null, 246, null));
        try {
            String strMessageText = INSTANCE.messageText(Conversation.sendMessage$default(conversationCreateConversation, user, (Map) null, 2, (Object) null));
            if (StringsKt.isBlank(strMessageText)) {
                throw new IllegalStateException("Gemma returned empty text".toString());
            }
            String str = strMessageText;
            AutoCloseableKt.closeFinally(conversationCreateConversation, null);
            return str;
        } catch (Throwable th) {
            try {
                throw th;
            } catch (Throwable th2) {
                AutoCloseableKt.closeFinally(conversationCreateConversation, th);
                throw th2;
            }
        }
    }

    private final String messageText(Message message) {
        Iterable<Content> contents = message.getContents().getContents();
        Collection arrayList = new ArrayList();
        for (Content content : contents) {
            Content.Text text = content instanceof Content.Text ? (Content.Text) content : null;
            String text2 = text != null ? text.getText() : null;
            if (text2 != null) {
                arrayList.add(text2);
            }
        }
        return StringsKt.trim((CharSequence) CollectionsKt.joinToString$default((List) arrayList, "", null, null, 0, null, null, 62, null)).toString();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final Engine createEngine(Context context) {
        String path = modelFile(context).getAbsolutePath();
        Log.i(TAG, "Initializing LiteRT-LM NPU engine: " + path);
        Intrinsics.checkNotNull(path);
        String nativeLibraryDir = context.getApplicationInfo().nativeLibraryDir;
        Intrinsics.checkNotNullExpressionValue(nativeLibraryDir, "nativeLibraryDir");
        EngineConfig config = new EngineConfig(path, new Backend.NPU(nativeLibraryDir), null, null, null, null, context.getCacheDir().getAbsolutePath(), 60, null);
        Engine engine = new Engine(config);
        engine.initialize();
        return engine;
    }
}
