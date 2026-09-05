package com.varun.pocketassistant.pipeline;

import android.content.Context;
import kotlin.Metadata;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.jvm.internal.ContinuationImpl;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: CloudAndLocalProviders.kt */
/* JADX INFO: loaded from: classes8.dex */
@Metadata(d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0007\u0018\u0000 \u00152\u00020\u0001:\u0001\u0015B\u001f\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007¢\u0006\u0004\b\b\u0010\tJ\b\u0010\u000e\u001a\u00020\u000fH\u0016J\u0016\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u0013H\u0096@¢\u0006\u0002\u0010\u0014R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\n\u001a\u00020\u000bX\u0096D¢\u0006\b\n\u0000\u001a\u0004\b\f\u0010\r¨\u0006\u0016"}, d2 = {"Lcom/varun/pocketassistant/pipeline/CloudAsrProvider;", "Lcom/varun/pocketassistant/pipeline/AsrProvider;", "context", "Landroid/content/Context;", "config", "Lcom/varun/pocketassistant/pipeline/PipelineConfig;", "client", "Lcom/varun/pocketassistant/pipeline/OpenAiCompatibleClient;", "<init>", "(Landroid/content/Context;Lcom/varun/pocketassistant/pipeline/PipelineConfig;Lcom/varun/pocketassistant/pipeline/OpenAiCompatibleClient;)V", "id", "", "getId", "()Ljava/lang/String;", "isAvailable", "", "transcribe", "Lcom/varun/pocketassistant/pipeline/AsrResult;", "wav", "Ljava/io/File;", "(Ljava/io/File;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "Companion", "app_debug"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class CloudAsrProvider implements AsrProvider {
    private static final long DIARIZE_MAX_SINGLE_MS = 900000;
    private static final String TAG = "CloudAsr";
    private final OpenAiCompatibleClient client;
    private final PipelineConfig config;
    private final Context context;
    private final String id;
    public static final int $stable = 8;

    /* JADX INFO: renamed from: com.varun.pocketassistant.pipeline.CloudAsrProvider$transcribe$1, reason: invalid class name */
    /* JADX INFO: compiled from: CloudAndLocalProviders.kt */
    @Metadata(k = 3, mv = {2, 2, 0}, xi = 48)
    @DebugMetadata(c = "com.varun.pocketassistant.pipeline.CloudAsrProvider", f = "CloudAndLocalProviders.kt", i = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, l = {677}, m = "transcribe", n = {"wav", "app", "settings", "prepDir", "prep", "chunks", "plains", "diarParts", "allCandidates", "chunk", "usedDiarize", "wantDiarize", "maxChunkMs", "n", "emptySpeechChunks", "hardFailChunks", "i"}, s = {"L$0", "L$1", "L$2", "L$3", "L$4", "L$5", "L$6", "L$7", "L$8", "L$9", "L$10", "I$0", "J$0", "I$1", "I$2", "I$3", "I$4"})
    static final class AnonymousClass1 extends ContinuationImpl {
        int I$0;
        int I$1;
        int I$2;
        int I$3;
        int I$4;
        long J$0;
        Object L$0;
        Object L$1;
        Object L$10;
        Object L$2;
        Object L$3;
        Object L$4;
        Object L$5;
        Object L$6;
        Object L$7;
        Object L$8;
        Object L$9;
        int label;
        /* synthetic */ Object result;

        AnonymousClass1(Continuation<? super AnonymousClass1> continuation) {
            super(continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            this.result = obj;
            this.label |= Integer.MIN_VALUE;
            return CloudAsrProvider.this.transcribe(null, this);
        }
    }

    public CloudAsrProvider(Context context, PipelineConfig config, OpenAiCompatibleClient client) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(config, "config");
        Intrinsics.checkNotNullParameter(client, "client");
        this.context = context;
        this.config = config;
        this.client = client;
        this.id = "cloud_stt";
    }

    @Override // com.varun.pocketassistant.pipeline.AsrProvider
    public String getId() {
        return this.id;
    }

    @Override // com.varun.pocketassistant.pipeline.AsrProvider
    public boolean isAvailable() {
        return this.config.cloudConfigured();
    }

    /* JADX WARN: Code duplicated, block: B:111:0x03e7  */
    /* JADX WARN: Code duplicated, block: B:276:0x019b A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:43:0x01b3  */
    /* JADX WARN: Code duplicated, block: B:44:0x01b7  */
    /* JADX WARN: Code duplicated, block: B:49:0x0200  */
    /* JADX WARN: Code duplicated, block: B:50:0x0202  */
    /* JADX WARN: Code duplicated, block: B:53:0x0215  */
    /* JADX WARN: Code duplicated, block: B:54:0x0217  */
    /* JADX WARN: Code duplicated, block: B:59:0x025a  */
    /* JADX WARN: Code duplicated, block: B:60:0x025d  */
    /* JADX WARN: Code duplicated, block: B:7:0x001a  */
    /* JADX WARN: Code duplicated, block: B:80:0x02bd A[RETURN] */
    /* JADX WARN: Code duplicated, block: B:81:0x02be  */
    /* JADX WARN: Code duplicated, block: B:84:0x02f9  */
    /* JADX WARN: Code duplicated, block: B:92:0x0346  */
    /* JADX WARN: Code duplicated, block: B:95:0x0352 A[Catch: all -> 0x0407, TRY_LEAVE, TryCatch #15 {all -> 0x0407, blocks: (B:93:0x034c, B:95:0x0352, B:103:0x036c), top: B:290:0x034c }] */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:147:0x05dc -> B:148:0x05f3). Please report as a decompilation issue!!! */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:81:0x02be -> B:303:0x02df). Please report as a decompilation issue!!! */
    /*  JADX ERROR: JadxOverflowException in pass: RegionMakerVisitor
        jadx.core.utils.exceptions.JadxOverflowException: Regions stack size limit reached
        	at jadx.core.utils.ErrorsCounter.addError(ErrorsCounter.java:59)
        	at jadx.core.utils.ErrorsCounter.error(ErrorsCounter.java:31)
        	at jadx.core.dex.attributes.nodes.NotificationAttrNode.addError(NotificationAttrNode.java:19)
        */
    @Override // com.varun.pocketassistant.pipeline.AsrProvider
    public java.lang.Object transcribe(java.io.File r56, kotlin.coroutines.Continuation<? super com.varun.pocketassistant.pipeline.AsrResult> r57) {
        /*
            Method dump skipped, instruction units count: 2678
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.varun.pocketassistant.pipeline.CloudAsrProvider.transcribe(java.io.File, kotlin.coroutines.Continuation):java.lang.Object");
    }
}
