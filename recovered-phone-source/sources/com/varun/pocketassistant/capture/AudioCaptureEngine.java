package com.varun.pocketassistant.capture;

import android.content.Context;
import android.media.AudioRecord;
import android.util.Log;
import androidx.core.content.ContextCompat;
import com.varun.pocketassistant.data.SegmentEndReason;
import com.varun.pocketassistant.data.SessionEntity;
import com.varun.pocketassistant.data.SessionRepository;
import com.varun.pocketassistant.data.SessionStatus;
import com.varun.pocketassistant.pipeline.AsrAudioPreprocessor;
import com.varun.pocketassistant.pipeline.PipelineConfig;
import com.varun.pocketassistant.pipeline.PipelineSettings;
import com.varun.pocketassistant.pipeline.ProviderMode;
import java.io.File;
import java.util.Arrays;
import java.util.concurrent.CancellationException;
import kotlin.Metadata;
import kotlin.Pair;
import kotlin.Result;
import kotlin.ResultKt;
import kotlin.TuplesKt;
import kotlin.Unit;
import kotlin.collections.ArrayDeque;
import kotlin.collections.CollectionsKt;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.Boxing;
import kotlin.coroutines.jvm.internal.ContinuationImpl;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SpillingKt;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.BuildersKt__Builders_commonKt;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.Dispatchers;
import kotlinx.coroutines.Job;
import kotlinx.coroutines.flow.FlowKt;
import kotlinx.coroutines.flow.MutableStateFlow;
import kotlinx.coroutines.flow.StateFlow;
import kotlinx.coroutines.flow.StateFlowKt;
import kotlinx.coroutines.sync.Mutex;
import kotlinx.coroutines.sync.MutexKt;
import okhttp3.internal.ws.RealWebSocket;

/* JADX INFO: compiled from: AudioCaptureEngine.kt */
/* JADX INFO: loaded from: classes5.dex */
@Metadata(d1 = {"\u0000Ä\u0001\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0005\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\u0017\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0007\n\u0002\b\u000b\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0007\u0018\u0000 \\2\u00020\u0001:\u0001\\B=\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u0012\n\b\u0002\u0010\n\u001a\u0004\u0018\u00010\u000b\u0012\b\b\u0002\u0010\f\u001a\u00020\r¢\u0006\u0004\b\u000e\u0010\u000fJ\u0006\u00108\u001a\u000209J\u0006\u0010:\u001a\u000209J\u0006\u0010;\u001a\u000209J\u0006\u0010<\u001a\u000209J\u0016\u0010=\u001a\u0002092\u0006\u0010>\u001a\u00020?H\u0082@¢\u0006\u0002\u0010@J&\u0010A\u001a\u0002092\u0006\u0010>\u001a\u00020?2\u0006\u0010B\u001a\u00020C2\u0006\u0010D\u001a\u00020EH\u0082@¢\u0006\u0002\u0010FJ.\u0010G\u001a\u0002092\u0006\u0010>\u001a\u00020?2\u0006\u0010B\u001a\u00020C2\u0006\u0010D\u001a\u00020E2\u0006\u0010H\u001a\u00020IH\u0082@¢\u0006\u0002\u0010JJ(\u0010K\u001a\u0002092\u0006\u0010L\u001a\u00020M2\u0006\u0010N\u001a\u00020M2\u0006\u0010H\u001a\u00020I2\u0006\u0010O\u001a\u00020\u001eH\u0002J\"\u0010P\u001a\u0002092\u0006\u0010Q\u001a\u00020\u001e2\n\b\u0002\u0010R\u001a\u0004\u0018\u00010?H\u0082@¢\u0006\u0002\u0010SJ\b\u0010T\u001a\u000209H\u0002J\u0010\u0010U\u001a\u0002092\u0006\u00104\u001a\u000203H\u0002J\u0010\u0010V\u001a\u0002092\u0006\u0010W\u001a\u00020?H\u0002J$\u0010X\u001a\u000e\u0012\u0004\u0012\u00020M\u0012\u0004\u0012\u00020M0Y2\u0006\u0010Z\u001a\u00020C2\u0006\u0010D\u001a\u00020EH\u0002J\b\u0010[\u001a\u00020\u0019H\u0002R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\n\u001a\u0004\u0018\u00010\u000bX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0011X\u0082\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u0012\u001a\u0004\u0018\u00010\u0013X\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u0014\u001a\u0004\u0018\u00010\u0015X\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u0016\u001a\u0004\u0018\u00010\u0017X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0018\u001a\u00020\u0019X\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u001a\u001a\u0004\u0018\u00010\u001bX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u001c\u001a\u00020\u0019X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u001d\u001a\u00020\u001eX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u001f\u001a\u00020\u001eX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010 \u001a\u00020\u0019X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010!\u001a\u00020\u0019X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\"\u001a\u00020\u0019X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010#\u001a\u00020\u0019X\u0082\u000e¢\u0006\u0002\n\u0000R\u0012\u0010$\u001a\u0004\u0018\u00010\u0019X\u0082\u000e¢\u0006\u0004\n\u0002\u0010%R\u000e\u0010&\u001a\u00020\u001eX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010'\u001a\u00020(X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010)\u001a\u00020\u0019X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010*\u001a\u00020+X\u0082\u0004¢\u0006\u0002\n\u0000R\u0014\u0010,\u001a\b\u0012\u0004\u0012\u00020.0-X\u0082\u0004¢\u0006\u0002\n\u0000R\u0014\u0010/\u001a\b\u0012\u0004\u0012\u0002000-X\u0082\u0004¢\u0006\u0002\n\u0000R\u0014\u00101\u001a\b\u0012\u0004\u0012\u00020302X\u0082\u0004¢\u0006\u0002\n\u0000R\u0017\u00104\u001a\b\u0012\u0004\u0012\u00020305¢\u0006\b\n\u0000\u001a\u0004\b6\u00107¨\u0006]"}, d2 = {"Lcom/varun/pocketassistant/capture/AudioCaptureEngine;", "", "context", "Landroid/content/Context;", "sessionRepository", "Lcom/varun/pocketassistant/data/SessionRepository;", "audioStorage", "Lcom/varun/pocketassistant/capture/AudioStorage;", "scope", "Lkotlinx/coroutines/CoroutineScope;", "pipelineConfig", "Lcom/varun/pocketassistant/pipeline/PipelineConfig;", "vad", "Lcom/varun/pocketassistant/capture/VoiceActivityDetector;", "<init>", "(Landroid/content/Context;Lcom/varun/pocketassistant/data/SessionRepository;Lcom/varun/pocketassistant/capture/AudioStorage;Lkotlinx/coroutines/CoroutineScope;Lcom/varun/pocketassistant/pipeline/PipelineConfig;Lcom/varun/pocketassistant/capture/VoiceActivityDetector;)V", "mutex", "Lkotlinx/coroutines/sync/Mutex;", "captureJob", "Lkotlinx/coroutines/Job;", "audioRecord", "Landroid/media/AudioRecord;", "writer", "Lcom/varun/pocketassistant/capture/WavWriter;", "activeSegmentStartMs", "", "activeSegmentFile", "Ljava/io/File;", "activeSegmentBytes", "wasSpeech", "", "paused", "lastFlushAtMs", "sessionStartedAtMs", "totalBytesWritten", "framesRead", "lastSpeechAtMs", "Ljava/lang/Long;", "lastRawSpeech", "phase", "Lcom/varun/pocketassistant/capture/CapturePhase;", "lastGraphPublishMs", "preRoll", "Lcom/varun/pocketassistant/capture/RingPcmBuffer;", "events", "Lkotlin/collections/ArrayDeque;", "Lcom/varun/pocketassistant/capture/CaptureEvent;", "rmsHistory", "Lcom/varun/pocketassistant/capture/RmsSample;", "_stats", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/varun/pocketassistant/capture/CaptureStats;", "stats", "Lkotlinx/coroutines/flow/StateFlow;", "getStats", "()Lkotlinx/coroutines/flow/StateFlow;", "start", "", "pause", "resume", "stop", "runCaptureLoop", "sessionId", "", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "processFrame", "frame", "", "length", "", "(Ljava/lang/String;[SILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "openSegment", "decision", "Lcom/varun/pocketassistant/capture/VadDecision;", "(Ljava/lang/String;[SILcom/varun/pocketassistant/capture/VadDecision;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "publishFrame", "rms", "", "peak", "speechActive", "closeWriterIfNeeded", "finalize", "endReason", "(ZLjava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "releaseRecorder", "publish", "pushEvent", "message", "rmsAndPeak", "Lkotlin/Pair;", "samples", "maxSegmentMs", "Companion", "app_debug"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class AudioCaptureEngine {
    private static final long GRAPH_PUBLISH_MS = 50;
    private static final long GRAPH_WINDOW_MS = 60000;
    private static final long MIN_SEGMENT_BYTES = 9644;
    private static final long MIN_SEGMENT_MS = 400;
    private static final int PRE_ROLL_MS = 10000;
    public static final int SAMPLE_RATE = 16000;
    private static final String TAG = "AudioCaptureEngine";
    private final MutableStateFlow<CaptureStats> _stats;
    private long activeSegmentBytes;
    private File activeSegmentFile;
    private long activeSegmentStartMs;
    private AudioRecord audioRecord;
    private final AudioStorage audioStorage;
    private Job captureJob;
    private final Context context;
    private final ArrayDeque<CaptureEvent> events;
    private long framesRead;
    private long lastFlushAtMs;
    private long lastGraphPublishMs;
    private boolean lastRawSpeech;
    private Long lastSpeechAtMs;
    private final Mutex mutex;
    private boolean paused;
    private CapturePhase phase;
    private final PipelineConfig pipelineConfig;
    private final RingPcmBuffer preRoll;
    private final ArrayDeque<RmsSample> rmsHistory;
    private final CoroutineScope scope;
    private final SessionRepository sessionRepository;
    private long sessionStartedAtMs;
    private final StateFlow<CaptureStats> stats;
    private long totalBytesWritten;
    private final VoiceActivityDetector vad;
    private boolean wasSpeech;
    private WavWriter writer;

    /* JADX INFO: renamed from: Companion, reason: from kotlin metadata */
    public static final Companion INSTANCE = new Companion(null);
    public static final int $stable = 8;

    /* JADX INFO: renamed from: com.varun.pocketassistant.capture.AudioCaptureEngine$closeWriterIfNeeded$1, reason: invalid class name */
    /* JADX INFO: compiled from: AudioCaptureEngine.kt */
    @Metadata(k = 3, mv = {2, 2, 0}, xi = 48)
    @DebugMetadata(c = "com.varun.pocketassistant.capture.AudioCaptureEngine", f = "AudioCaptureEngine.kt", i = {0, 0, 0, 0, 0, 0, 0, 0, 0}, l = {396}, m = "closeWriterIfNeeded", n = {"endReason", "currentWriter", "file", "sessionId", "finalize", "startMs", "bytes", "endMs", "duration"}, s = {"L$0", "L$1", "L$2", "L$3", "Z$0", "J$0", "J$1", "J$2", "J$3"})
    static final class AnonymousClass1 extends ContinuationImpl {
        long J$0;
        long J$1;
        long J$2;
        long J$3;
        Object L$0;
        Object L$1;
        Object L$2;
        Object L$3;
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
            return AudioCaptureEngine.this.closeWriterIfNeeded(false, null, this);
        }
    }

    /* JADX INFO: renamed from: com.varun.pocketassistant.capture.AudioCaptureEngine$processFrame$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: AudioCaptureEngine.kt */
    @Metadata(k = 3, mv = {2, 2, 0}, xi = 48)
    @DebugMetadata(c = "com.varun.pocketassistant.capture.AudioCaptureEngine", f = "AudioCaptureEngine.kt", i = {0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 2, 2, 3, 3, 3, 3, 3, 3, 3, 3}, l = {263, 276, 292, 294}, m = "processFrame", n = {"sessionId", "frame", "decision", "length", "rms", "peak", "sessionId", "frame", "decision", "length", "rms", "peak", "sessionId", "frame", "decision", "length", "rms", "peak", "now", "openForMs", "sessionId", "frame", "decision", "length", "rms", "peak", "now", "openForMs"}, s = {"L$0", "L$1", "L$2", "I$0", "F$0", "F$1", "L$0", "L$1", "L$2", "I$0", "F$0", "F$1", "L$0", "L$1", "L$2", "I$0", "F$0", "F$1", "J$0", "J$1", "L$0", "L$1", "L$2", "I$0", "F$0", "F$1", "J$0", "J$1"})
    static final class C06451 extends ContinuationImpl {
        float F$0;
        float F$1;
        int I$0;
        long J$0;
        long J$1;
        Object L$0;
        Object L$1;
        Object L$2;
        int label;
        /* synthetic */ Object result;

        C06451(Continuation<? super C06451> continuation) {
            super(continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            this.result = obj;
            this.label |= Integer.MIN_VALUE;
            return AudioCaptureEngine.this.processFrame(null, null, 0, this);
        }
    }

    /* JADX INFO: renamed from: com.varun.pocketassistant.capture.AudioCaptureEngine$runCaptureLoop$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: AudioCaptureEngine.kt */
    @Metadata(k = 3, mv = {2, 2, 0}, xi = 48)
    @DebugMetadata(c = "com.varun.pocketassistant.capture.AudioCaptureEngine", f = "AudioCaptureEngine.kt", i = {0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1}, l = {478, 236}, m = "runCaptureLoop", n = {"sessionId", "recorder", "readBuffer", "$this$withLock_u24default\\2", "minBuf", "bufferSize", "read", "$i$f$withLock\\2\\235", "sessionId", "recorder", "readBuffer", "$this$withLock_u24default\\2", "minBuf", "bufferSize", "read", "$i$f$withLock\\2\\235", "$i$a$-withLock$default-AudioCaptureEngine$runCaptureLoop$3\\3\\480\\0"}, s = {"L$0", "L$1", "L$2", "L$3", "I$0", "I$1", "I$2", "I$3", "L$0", "L$1", "L$2", "L$3", "I$0", "I$1", "I$2", "I$3", "I$4"})
    static final class C06471 extends ContinuationImpl {
        int I$0;
        int I$1;
        int I$2;
        int I$3;
        int I$4;
        Object L$0;
        Object L$1;
        Object L$2;
        Object L$3;
        int label;
        /* synthetic */ Object result;

        C06471(Continuation<? super C06471> continuation) {
            super(continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            this.result = obj;
            this.label |= Integer.MIN_VALUE;
            return AudioCaptureEngine.this.runCaptureLoop(null, this);
        }
    }

    public AudioCaptureEngine(Context context, SessionRepository sessionRepository, AudioStorage audioStorage, CoroutineScope scope, PipelineConfig pipelineConfig, VoiceActivityDetector vad) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(sessionRepository, "sessionRepository");
        Intrinsics.checkNotNullParameter(audioStorage, "audioStorage");
        Intrinsics.checkNotNullParameter(scope, "scope");
        Intrinsics.checkNotNullParameter(vad, "vad");
        this.context = context;
        this.sessionRepository = sessionRepository;
        this.audioStorage = audioStorage;
        this.scope = scope;
        this.pipelineConfig = pipelineConfig;
        this.vad = vad;
        this.mutex = MutexKt.Mutex$default(false, 1, null);
        this.phase = CapturePhase.IDLE;
        this.preRoll = new RingPcmBuffer(160000);
        this.events = new ArrayDeque<>();
        this.rmsHistory = new ArrayDeque<>();
        this._stats = StateFlowKt.MutableStateFlow(new CaptureStats(null, null, null, 0, false, 0.0f, 0.0f, this.vad.getSpeechThreshold(), 0.0f, null, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, null, null, null, null, null, null, 33554303, null));
        this.stats = FlowKt.asStateFlow(this._stats);
    }

    /* JADX WARN: Illegal instructions before constructor call */
    public /* synthetic */ AudioCaptureEngine(Context context, SessionRepository sessionRepository, AudioStorage audioStorage, CoroutineScope coroutineScope, PipelineConfig pipelineConfig, VoiceActivityDetector voiceActivityDetector, int i, DefaultConstructorMarker defaultConstructorMarker) {
        PipelineConfig pipelineConfig2;
        VoiceActivityDetector voiceActivityDetectorCreateOrFallback;
        if ((i & 16) == 0) {
            pipelineConfig2 = pipelineConfig;
        } else {
            pipelineConfig2 = null;
        }
        if ((i & 32) == 0) {
            voiceActivityDetectorCreateOrFallback = voiceActivityDetector;
        } else {
            voiceActivityDetectorCreateOrFallback = TenVoiceActivityDetector.INSTANCE.createOrFallback();
        }
        this(context, sessionRepository, audioStorage, coroutineScope, pipelineConfig2, voiceActivityDetectorCreateOrFallback);
    }

    public final StateFlow<CaptureStats> getStats() {
        return this.stats;
    }

    public final void start() {
        Job job = this.captureJob;
        boolean z = false;
        if (job != null && job.isActive()) {
            z = true;
        }
        if (z) {
            return;
        }
        if (ContextCompat.checkSelfPermission(this.context, "android.permission.RECORD_AUDIO") != 0) {
            this._stats.setValue(CaptureStats.copy$default(this._stats.getValue(), null, null, null, 0, false, 0.0f, 0.0f, 0.0f, 0.0f, null, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, null, null, null, null, "Microphone permission missing", null, 25165823, null));
        } else {
            this.captureJob = BuildersKt__Builders_commonKt.launch$default(this.scope, Dispatchers.getIO(), null, new C06481(null), 2, null);
        }
    }

    /* JADX INFO: renamed from: com.varun.pocketassistant.capture.AudioCaptureEngine$start$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: AudioCaptureEngine.kt */
    @Metadata(d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H\n"}, d2 = {"<anonymous>", "", "Lkotlinx/coroutines/CoroutineScope;"}, k = 3, mv = {2, 2, 0}, xi = 48)
    @DebugMetadata(c = "com.varun.pocketassistant.capture.AudioCaptureEngine$start$1", f = "AudioCaptureEngine.kt", i = {1}, l = {83, 100, 113, 113, 113}, m = "invokeSuspend", n = {"session"}, s = {"L$0"})
    static final class C06481 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
        Object L$0;
        int label;

        C06481(Continuation<? super C06481> continuation) {
            super(2, continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
            return AudioCaptureEngine.this.new C06481(continuation);
        }

        @Override // kotlin.jvm.functions.Function2
        public final Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
            return ((C06481) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
        }

        /* JADX WARN: Code duplicated, block: B:21:0x00f2 A[RETURN] */
        /* JADX WARN: Code duplicated, block: B:24:0x0104 A[RETURN] */
        /* JADX WARN: Code duplicated, block: B:27:0x013c  */
        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object $result) throws Throwable {
            Object objStartSession;
            SessionEntity session;
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            try {
                switch (this.label) {
                    case 0:
                        ResultKt.throwOnFailure($result);
                        this.label = 1;
                        objStartSession = AudioCaptureEngine.this.sessionRepository.startSession(this);
                        if (objStartSession == coroutine_suspended) {
                            return coroutine_suspended;
                        }
                        session = (SessionEntity) objStartSession;
                        AudioCaptureEngine.this.sessionStartedAtMs = System.currentTimeMillis();
                        AudioCaptureEngine.this.totalBytesWritten = 0L;
                        AudioCaptureEngine.this.framesRead = 0L;
                        AudioCaptureEngine.this.rmsHistory.clear();
                        AudioCaptureEngine.this.phase = CapturePhase.LISTENING;
                        AudioCaptureEngine.this.pushEvent("Capture started");
                        AudioCaptureEngine.this.publish(new CaptureStats(CaptureState.RECORDING, CapturePhase.LISTENING, session.getId(), session.getSegmentCount(), false, 0.0f, 0.0f, AudioCaptureEngine.this.vad.getSpeechThreshold(), 0.0f, null, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, null, null, null, null, null, CollectionsKt.toList(AudioCaptureEngine.this.events), 16777072, null));
                        this.L$0 = SpillingKt.nullOutSpilledVariable(session);
                        this.label = 2;
                        if (AudioCaptureEngine.this.runCaptureLoop(session.getId(), this) == coroutine_suspended) {
                            return coroutine_suspended;
                        }
                        this.L$0 = null;
                        this.label = 3;
                        if (AudioCaptureEngine.this.closeWriterIfNeeded(true, SegmentEndReason.MANUAL, this) == coroutine_suspended) {
                            return coroutine_suspended;
                        }
                        AudioCaptureEngine.this.releaseRecorder();
                        AudioCaptureEngine.this.vad.reset();
                        AudioCaptureEngine.this.preRoll.clear();
                        AudioCaptureEngine.this.wasSpeech = false;
                        AudioCaptureEngine.this.phase = CapturePhase.IDLE;
                        if (((CaptureStats) AudioCaptureEngine.this._stats.getValue()).getState() != CaptureState.IDLE) {
                            AudioCaptureEngine.this.publish(CaptureStats.copy$default((CaptureStats) AudioCaptureEngine.this._stats.getValue(), CaptureState.IDLE, CapturePhase.IDLE, null, 0, false, 0.0f, 0.0f, 0.0f, 0.0f, null, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, null, null, null, null, null, CollectionsKt.toList(AudioCaptureEngine.this.events), 16777196, null));
                        }
                        return Unit.INSTANCE;
                    case 1:
                        ResultKt.throwOnFailure($result);
                        objStartSession = $result;
                        session = (SessionEntity) objStartSession;
                        AudioCaptureEngine.this.sessionStartedAtMs = System.currentTimeMillis();
                        AudioCaptureEngine.this.totalBytesWritten = 0L;
                        AudioCaptureEngine.this.framesRead = 0L;
                        AudioCaptureEngine.this.rmsHistory.clear();
                        AudioCaptureEngine.this.phase = CapturePhase.LISTENING;
                        AudioCaptureEngine.this.pushEvent("Capture started");
                        AudioCaptureEngine.this.publish(new CaptureStats(CaptureState.RECORDING, CapturePhase.LISTENING, session.getId(), session.getSegmentCount(), false, 0.0f, 0.0f, AudioCaptureEngine.this.vad.getSpeechThreshold(), 0.0f, null, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, null, null, null, null, null, CollectionsKt.toList(AudioCaptureEngine.this.events), 16777072, null));
                        this.L$0 = SpillingKt.nullOutSpilledVariable(session);
                        this.label = 2;
                        if (AudioCaptureEngine.this.runCaptureLoop(session.getId(), this) == coroutine_suspended) {
                            return coroutine_suspended;
                        }
                        this.L$0 = null;
                        this.label = 3;
                        if (AudioCaptureEngine.this.closeWriterIfNeeded(true, SegmentEndReason.MANUAL, this) == coroutine_suspended) {
                            return coroutine_suspended;
                        }
                        AudioCaptureEngine.this.releaseRecorder();
                        AudioCaptureEngine.this.vad.reset();
                        AudioCaptureEngine.this.preRoll.clear();
                        AudioCaptureEngine.this.wasSpeech = false;
                        AudioCaptureEngine.this.phase = CapturePhase.IDLE;
                        if (((CaptureStats) AudioCaptureEngine.this._stats.getValue()).getState() != CaptureState.IDLE) {
                            AudioCaptureEngine.this.publish(CaptureStats.copy$default((CaptureStats) AudioCaptureEngine.this._stats.getValue(), CaptureState.IDLE, CapturePhase.IDLE, null, 0, false, 0.0f, 0.0f, 0.0f, 0.0f, null, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, null, null, null, null, null, CollectionsKt.toList(AudioCaptureEngine.this.events), 16777196, null));
                        }
                        return Unit.INSTANCE;
                    case 2:
                        ResultKt.throwOnFailure($result);
                        this.L$0 = null;
                        this.label = 3;
                        if (AudioCaptureEngine.this.closeWriterIfNeeded(true, SegmentEndReason.MANUAL, this) == coroutine_suspended) {
                            return coroutine_suspended;
                        }
                        AudioCaptureEngine.this.releaseRecorder();
                        AudioCaptureEngine.this.vad.reset();
                        AudioCaptureEngine.this.preRoll.clear();
                        AudioCaptureEngine.this.wasSpeech = false;
                        AudioCaptureEngine.this.phase = CapturePhase.IDLE;
                        if (((CaptureStats) AudioCaptureEngine.this._stats.getValue()).getState() != CaptureState.IDLE) {
                            AudioCaptureEngine.this.publish(CaptureStats.copy$default((CaptureStats) AudioCaptureEngine.this._stats.getValue(), CaptureState.IDLE, CapturePhase.IDLE, null, 0, false, 0.0f, 0.0f, 0.0f, 0.0f, null, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, null, null, null, null, null, CollectionsKt.toList(AudioCaptureEngine.this.events), 16777196, null));
                        }
                        return Unit.INSTANCE;
                    case 3:
                        ResultKt.throwOnFailure($result);
                        AudioCaptureEngine.this.releaseRecorder();
                        AudioCaptureEngine.this.vad.reset();
                        AudioCaptureEngine.this.preRoll.clear();
                        AudioCaptureEngine.this.wasSpeech = false;
                        AudioCaptureEngine.this.phase = CapturePhase.IDLE;
                        if (((CaptureStats) AudioCaptureEngine.this._stats.getValue()).getState() != CaptureState.IDLE) {
                            AudioCaptureEngine.this.publish(CaptureStats.copy$default((CaptureStats) AudioCaptureEngine.this._stats.getValue(), CaptureState.IDLE, CapturePhase.IDLE, null, 0, false, 0.0f, 0.0f, 0.0f, 0.0f, null, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, null, null, null, null, null, CollectionsKt.toList(AudioCaptureEngine.this.events), 16777196, null));
                        }
                        return Unit.INSTANCE;
                    case 4:
                        ResultKt.throwOnFailure($result);
                        AudioCaptureEngine.this.releaseRecorder();
                        AudioCaptureEngine.this.vad.reset();
                        AudioCaptureEngine.this.preRoll.clear();
                        AudioCaptureEngine.this.wasSpeech = false;
                        AudioCaptureEngine.this.phase = CapturePhase.IDLE;
                        if (((CaptureStats) AudioCaptureEngine.this._stats.getValue()).getState() != CaptureState.IDLE) {
                            AudioCaptureEngine.this.publish(CaptureStats.copy$default((CaptureStats) AudioCaptureEngine.this._stats.getValue(), CaptureState.IDLE, CapturePhase.IDLE, null, 0, false, 0.0f, 0.0f, 0.0f, 0.0f, null, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, null, null, null, null, null, CollectionsKt.toList(AudioCaptureEngine.this.events), 16777196, null));
                        }
                        return Unit.INSTANCE;
                    case 5:
                        th = (Throwable) this.L$0;
                        ResultKt.throwOnFailure($result);
                        AudioCaptureEngine.this.releaseRecorder();
                        AudioCaptureEngine.this.vad.reset();
                        AudioCaptureEngine.this.preRoll.clear();
                        AudioCaptureEngine.this.wasSpeech = false;
                        AudioCaptureEngine.this.phase = CapturePhase.IDLE;
                        if (((CaptureStats) AudioCaptureEngine.this._stats.getValue()).getState() != CaptureState.IDLE) {
                            AudioCaptureEngine.this.publish(CaptureStats.copy$default((CaptureStats) AudioCaptureEngine.this._stats.getValue(), CaptureState.IDLE, CapturePhase.IDLE, null, 0, false, 0.0f, 0.0f, 0.0f, 0.0f, null, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, null, null, null, null, null, CollectionsKt.toList(AudioCaptureEngine.this.events), 16777196, null));
                        }
                        throw th;
                    default:
                        throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
            } catch (Throwable t) {
                try {
                    Log.e(AudioCaptureEngine.TAG, "Capture failed", t);
                    AudioCaptureEngine.this.pushEvent("Capture failed: " + t.getMessage());
                    AudioCaptureEngine audioCaptureEngine = AudioCaptureEngine.this;
                    CaptureStats captureStats = (CaptureStats) AudioCaptureEngine.this._stats.getValue();
                    CaptureState captureState = CaptureState.IDLE;
                    CapturePhase capturePhase = CapturePhase.IDLE;
                    String message = t.getMessage();
                    audioCaptureEngine.publish(CaptureStats.copy$default(captureStats, captureState, capturePhase, null, 0, false, 0.0f, 0.0f, 0.0f, 0.0f, null, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, null, null, null, null, message == null ? "Capture failed" : message, CollectionsKt.toList(AudioCaptureEngine.this.events), 8388604, null));
                    this.L$0 = null;
                    this.label = 4;
                    if (AudioCaptureEngine.this.closeWriterIfNeeded(true, SegmentEndReason.MANUAL, this) == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    AudioCaptureEngine.this.releaseRecorder();
                    AudioCaptureEngine.this.vad.reset();
                    AudioCaptureEngine.this.preRoll.clear();
                    AudioCaptureEngine.this.wasSpeech = false;
                    AudioCaptureEngine.this.phase = CapturePhase.IDLE;
                    if (((CaptureStats) AudioCaptureEngine.this._stats.getValue()).getState() != CaptureState.IDLE) {
                        AudioCaptureEngine.this.publish(CaptureStats.copy$default((CaptureStats) AudioCaptureEngine.this._stats.getValue(), CaptureState.IDLE, CapturePhase.IDLE, null, 0, false, 0.0f, 0.0f, 0.0f, 0.0f, null, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, null, null, null, null, null, CollectionsKt.toList(AudioCaptureEngine.this.events), 16777196, null));
                    }
                } catch (Throwable th) {
                    th = th;
                    this.L$0 = th;
                    this.label = 5;
                    if (AudioCaptureEngine.this.closeWriterIfNeeded(true, SegmentEndReason.MANUAL, this) == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                }
            }
        }
    }

    /* JADX INFO: renamed from: com.varun.pocketassistant.capture.AudioCaptureEngine$pause$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: AudioCaptureEngine.kt */
    @Metadata(d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H\n"}, d2 = {"<anonymous>", "", "Lkotlinx/coroutines/CoroutineScope;"}, k = 3, mv = {2, 2, 0}, xi = 48)
    @DebugMetadata(c = "com.varun.pocketassistant.capture.AudioCaptureEngine$pause$1", f = "AudioCaptureEngine.kt", i = {0, 0, 1, 1, 1, 2}, l = {477, 137, 144}, m = "invokeSuspend", n = {"$this$withLock_u24default\\1", "$i$f$withLock\\1\\136", "$this$withLock_u24default\\1", "$i$f$withLock\\1\\136", "$i$a$-withLock$default-AudioCaptureEngine$pause$1$1\\2\\479\\0", "sessionId"}, s = {"L$0", "I$0", "L$0", "I$0", "I$1", "L$0"})
    static final class C06441 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
        int I$0;
        int I$1;
        Object L$0;
        Object L$1;
        int label;

        C06441(Continuation<? super C06441> continuation) {
            super(2, continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
            return AudioCaptureEngine.this.new C06441(continuation);
        }

        @Override // kotlin.jvm.functions.Function2
        public final Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
            return ((C06441) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
        }

        /* JADX WARN: Code duplicated, block: B:20:0x007a A[RETURN] */
        /* JADX WARN: Code duplicated, block: B:21:0x007b  */
        /* JADX WARN: Code duplicated, block: B:26:0x00aa  */
        /* JADX WARN: Code duplicated, block: B:28:0x00c7 A[RETURN] */
        /* JADX WARN: Code duplicated, block: B:29:0x00c8  */
        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object $result) throws Throwable {
            AudioCaptureEngine audioCaptureEngine;
            Object obj;
            Mutex mutex;
            int i;
            String sessionId;
            String sessionId2;
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    Mutex mutex2 = AudioCaptureEngine.this.mutex;
                    audioCaptureEngine = AudioCaptureEngine.this;
                    obj = null;
                    this.L$0 = mutex2;
                    this.L$1 = audioCaptureEngine;
                    this.I$0 = 0;
                    this.label = 1;
                    if (mutex2.lock(null, this) == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    mutex = mutex2;
                    i = 0;
                    try {
                        this.L$0 = mutex;
                        this.L$1 = audioCaptureEngine;
                        this.I$0 = i;
                        this.I$1 = 0;
                        this.label = 2;
                        if (audioCaptureEngine.closeWriterIfNeeded(true, SegmentEndReason.MANUAL, this) == coroutine_suspended) {
                            return coroutine_suspended;
                        }
                        audioCaptureEngine.wasSpeech = false;
                        audioCaptureEngine.vad.reset();
                        audioCaptureEngine.preRoll.clear();
                        Unit unit = Unit.INSTANCE;
                        mutex.unlock(obj);
                        sessionId = ((CaptureStats) AudioCaptureEngine.this._stats.getValue()).getSessionId();
                        if (sessionId != null) {
                            this.L$0 = SpillingKt.nullOutSpilledVariable(sessionId);
                            this.L$1 = null;
                            this.label = 3;
                            if (AudioCaptureEngine.this.sessionRepository.setStatus(sessionId, SessionStatus.PAUSED, this) == coroutine_suspended) {
                                return coroutine_suspended;
                            }
                            sessionId2 = sessionId;
                        }
                        AudioCaptureEngine.this.phase = CapturePhase.PAUSED;
                        AudioCaptureEngine.this.pushEvent("Paused");
                        AudioCaptureEngine.this.publish(CaptureStats.copy$default((CaptureStats) AudioCaptureEngine.this._stats.getValue(), CaptureState.PAUSED, CapturePhase.PAUSED, null, 0, false, 0.0f, 0.0f, 0.0f, 0.0f, null, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, null, null, null, null, null, CollectionsKt.toList(AudioCaptureEngine.this.events), 16777196, null));
                        return Unit.INSTANCE;
                    } catch (Throwable th) {
                        th = th;
                        mutex.unlock(obj);
                        throw th;
                    }
                case 1:
                    i = this.I$0;
                    audioCaptureEngine = (AudioCaptureEngine) this.L$1;
                    obj = null;
                    mutex = (Mutex) this.L$0;
                    ResultKt.throwOnFailure($result);
                    this.L$0 = mutex;
                    this.L$1 = audioCaptureEngine;
                    this.I$0 = i;
                    this.I$1 = 0;
                    this.label = 2;
                    if (audioCaptureEngine.closeWriterIfNeeded(true, SegmentEndReason.MANUAL, this) == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    audioCaptureEngine.wasSpeech = false;
                    audioCaptureEngine.vad.reset();
                    audioCaptureEngine.preRoll.clear();
                    Unit unit2 = Unit.INSTANCE;
                    mutex.unlock(obj);
                    sessionId = ((CaptureStats) AudioCaptureEngine.this._stats.getValue()).getSessionId();
                    if (sessionId != null) {
                        this.L$0 = SpillingKt.nullOutSpilledVariable(sessionId);
                        this.L$1 = null;
                        this.label = 3;
                        if (AudioCaptureEngine.this.sessionRepository.setStatus(sessionId, SessionStatus.PAUSED, this) == coroutine_suspended) {
                            return coroutine_suspended;
                        }
                        sessionId2 = sessionId;
                    }
                    AudioCaptureEngine.this.phase = CapturePhase.PAUSED;
                    AudioCaptureEngine.this.pushEvent("Paused");
                    AudioCaptureEngine.this.publish(CaptureStats.copy$default((CaptureStats) AudioCaptureEngine.this._stats.getValue(), CaptureState.PAUSED, CapturePhase.PAUSED, null, 0, false, 0.0f, 0.0f, 0.0f, 0.0f, null, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, null, null, null, null, null, CollectionsKt.toList(AudioCaptureEngine.this.events), 16777196, null));
                    return Unit.INSTANCE;
                case 2:
                    int i2 = this.I$1;
                    int i3 = this.I$0;
                    audioCaptureEngine = (AudioCaptureEngine) this.L$1;
                    obj = null;
                    mutex = (Mutex) this.L$0;
                    try {
                        ResultKt.throwOnFailure($result);
                        audioCaptureEngine.wasSpeech = false;
                        audioCaptureEngine.vad.reset();
                        audioCaptureEngine.preRoll.clear();
                        Unit unit3 = Unit.INSTANCE;
                        mutex.unlock(obj);
                        sessionId = ((CaptureStats) AudioCaptureEngine.this._stats.getValue()).getSessionId();
                        if (sessionId != null) {
                            this.L$0 = SpillingKt.nullOutSpilledVariable(sessionId);
                            this.L$1 = null;
                            this.label = 3;
                            if (AudioCaptureEngine.this.sessionRepository.setStatus(sessionId, SessionStatus.PAUSED, this) == coroutine_suspended) {
                                return coroutine_suspended;
                            }
                            sessionId2 = sessionId;
                        }
                        AudioCaptureEngine.this.phase = CapturePhase.PAUSED;
                        AudioCaptureEngine.this.pushEvent("Paused");
                        AudioCaptureEngine.this.publish(CaptureStats.copy$default((CaptureStats) AudioCaptureEngine.this._stats.getValue(), CaptureState.PAUSED, CapturePhase.PAUSED, null, 0, false, 0.0f, 0.0f, 0.0f, 0.0f, null, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, null, null, null, null, null, CollectionsKt.toList(AudioCaptureEngine.this.events), 16777196, null));
                        return Unit.INSTANCE;
                    } catch (Throwable th2) {
                        th = th2;
                        mutex.unlock(obj);
                        throw th;
                    }
                case 3:
                    sessionId2 = (String) this.L$0;
                    ResultKt.throwOnFailure($result);
                    AudioCaptureEngine.this.phase = CapturePhase.PAUSED;
                    AudioCaptureEngine.this.pushEvent("Paused");
                    AudioCaptureEngine.this.publish(CaptureStats.copy$default((CaptureStats) AudioCaptureEngine.this._stats.getValue(), CaptureState.PAUSED, CapturePhase.PAUSED, null, 0, false, 0.0f, 0.0f, 0.0f, 0.0f, null, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, null, null, null, null, null, CollectionsKt.toList(AudioCaptureEngine.this.events), 16777196, null));
                    return Unit.INSTANCE;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }

    public final void pause() {
        this.paused = true;
        BuildersKt__Builders_commonKt.launch$default(this.scope, null, null, new C06441(null), 3, null);
    }

    /* JADX INFO: renamed from: com.varun.pocketassistant.capture.AudioCaptureEngine$resume$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: AudioCaptureEngine.kt */
    @Metadata(d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H\n"}, d2 = {"<anonymous>", "", "Lkotlinx/coroutines/CoroutineScope;"}, k = 3, mv = {2, 2, 0}, xi = 48)
    @DebugMetadata(c = "com.varun.pocketassistant.capture.AudioCaptureEngine$resume$1", f = "AudioCaptureEngine.kt", i = {0}, l = {164}, m = "invokeSuspend", n = {"sessionId"}, s = {"L$0"})
    static final class C06461 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
        Object L$0;
        int label;

        C06461(Continuation<? super C06461> continuation) {
            super(2, continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
            return AudioCaptureEngine.this.new C06461(continuation);
        }

        @Override // kotlin.jvm.functions.Function2
        public final Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
            return ((C06461) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object $result) {
            String sessionId;
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    String sessionId2 = ((CaptureStats) AudioCaptureEngine.this._stats.getValue()).getSessionId();
                    if (sessionId2 != null) {
                        this.L$0 = SpillingKt.nullOutSpilledVariable(sessionId2);
                        this.label = 1;
                        if (AudioCaptureEngine.this.sessionRepository.setStatus(sessionId2, SessionStatus.RECORDING, this) == coroutine_suspended) {
                            return coroutine_suspended;
                        }
                        sessionId = sessionId2;
                    }
                    AudioCaptureEngine.this.phase = CapturePhase.LISTENING;
                    AudioCaptureEngine.this.pushEvent("Resumed");
                    AudioCaptureEngine.this.publish(CaptureStats.copy$default((CaptureStats) AudioCaptureEngine.this._stats.getValue(), CaptureState.RECORDING, CapturePhase.LISTENING, null, 0, false, 0.0f, 0.0f, 0.0f, 0.0f, null, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, null, null, null, null, null, CollectionsKt.toList(AudioCaptureEngine.this.events), 16777212, null));
                    return Unit.INSTANCE;
                case 1:
                    sessionId = (String) this.L$0;
                    ResultKt.throwOnFailure($result);
                    AudioCaptureEngine.this.phase = CapturePhase.LISTENING;
                    AudioCaptureEngine.this.pushEvent("Resumed");
                    AudioCaptureEngine.this.publish(CaptureStats.copy$default((CaptureStats) AudioCaptureEngine.this._stats.getValue(), CaptureState.RECORDING, CapturePhase.LISTENING, null, 0, false, 0.0f, 0.0f, 0.0f, 0.0f, null, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, null, null, null, null, null, CollectionsKt.toList(AudioCaptureEngine.this.events), 16777212, null));
                    return Unit.INSTANCE;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }

    public final void resume() {
        this.paused = false;
        BuildersKt__Builders_commonKt.launch$default(this.scope, null, null, new C06461(null), 3, null);
    }

    public final void stop() {
        this.paused = false;
        String sessionId = this._stats.getValue().getSessionId();
        Job job = this.captureJob;
        if (job != null) {
            Job.DefaultImpls.cancel$default(job, (CancellationException) null, 1, (Object) null);
        }
        this.captureJob = null;
        BuildersKt__Builders_commonKt.launch$default(this.scope, null, null, new C06491(sessionId, null), 3, null);
    }

    /* JADX INFO: renamed from: com.varun.pocketassistant.capture.AudioCaptureEngine$stop$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: AudioCaptureEngine.kt */
    @Metadata(d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H\n"}, d2 = {"<anonymous>", "", "Lkotlinx/coroutines/CoroutineScope;"}, k = 3, mv = {2, 2, 0}, xi = 48)
    @DebugMetadata(c = "com.varun.pocketassistant.capture.AudioCaptureEngine$stop$1", f = "AudioCaptureEngine.kt", i = {0, 0, 1, 1, 1}, l = {477, 188, 192, 196}, m = "invokeSuspend", n = {"$this$withLock_u24default\\1", "$i$f$withLock\\1\\187", "$this$withLock_u24default\\1", "$i$f$withLock\\1\\187", "$i$a$-withLock$default-AudioCaptureEngine$stop$1$1\\2\\479\\0"}, s = {"L$0", "I$0", "L$0", "I$0", "I$1"})
    static final class C06491 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
        final /* synthetic */ String $sessionId;
        int I$0;
        int I$1;
        Object L$0;
        Object L$1;
        int label;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        C06491(String str, Continuation<? super C06491> continuation) {
            super(2, continuation);
            this.$sessionId = str;
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
            return AudioCaptureEngine.this.new C06491(this.$sessionId, continuation);
        }

        @Override // kotlin.jvm.functions.Function2
        public final Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
            return ((C06491) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
        }

        /* JADX WARN: Code duplicated, block: B:21:0x0078 A[RETURN] */
        /* JADX WARN: Code duplicated, block: B:22:0x0079  */
        /* JADX WARN: Code duplicated, block: B:26:0x008e  */
        /* JADX WARN: Code duplicated, block: B:28:0x00a6 A[RETURN] */
        /* JADX WARN: Code duplicated, block: B:31:0x00b9 A[RETURN] */
        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object $result) throws Throwable {
            AudioCaptureEngine audioCaptureEngine;
            Object obj;
            Mutex mutex;
            int i;
            Object obj2;
            Mutex mutex2;
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    Mutex mutex3 = AudioCaptureEngine.this.mutex;
                    audioCaptureEngine = AudioCaptureEngine.this;
                    obj = null;
                    this.L$0 = mutex3;
                    this.L$1 = audioCaptureEngine;
                    this.I$0 = 0;
                    this.label = 1;
                    if (mutex3.lock(null, this) == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    mutex = mutex3;
                    i = 0;
                    try {
                        this.L$0 = mutex;
                        this.L$1 = null;
                        this.I$0 = i;
                        this.I$1 = 0;
                        this.label = 2;
                        if (audioCaptureEngine.closeWriterIfNeeded(true, SegmentEndReason.MANUAL, this) == coroutine_suspended) {
                            return coroutine_suspended;
                        }
                        obj2 = obj;
                        mutex2 = mutex;
                        Unit unit = Unit.INSTANCE;
                        mutex2.unlock(obj2);
                        AudioCaptureEngine.this.releaseRecorder();
                        if (this.$sessionId != null) {
                            this.L$0 = null;
                            this.label = 3;
                            if (AudioCaptureEngine.this.sessionRepository.setStatus(this.$sessionId, SessionStatus.COMPLETED, this) == coroutine_suspended) {
                                return coroutine_suspended;
                            }
                            this.label = 4;
                            if (AudioCaptureEngine.this.sessionRepository.applyRetention(this) == coroutine_suspended) {
                                return coroutine_suspended;
                            }
                        }
                        AudioCaptureEngine.this.pushEvent("Stopped");
                        AudioCaptureEngine.this.phase = CapturePhase.IDLE;
                        AudioCaptureEngine.this.publish(new CaptureStats(null, null, null, 0, false, 0.0f, 0.0f, AudioCaptureEngine.this.vad.getSpeechThreshold(), 0.0f, null, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, null, null, null, null, null, CollectionsKt.toList(AudioCaptureEngine.this.events), 16777087, null));
                        return Unit.INSTANCE;
                    } catch (Throwable th) {
                        th = th;
                        obj2 = obj;
                        mutex2 = mutex;
                        mutex2.unlock(obj2);
                        throw th;
                    }
                case 1:
                    i = this.I$0;
                    audioCaptureEngine = (AudioCaptureEngine) this.L$1;
                    obj = null;
                    mutex = (Mutex) this.L$0;
                    ResultKt.throwOnFailure($result);
                    this.L$0 = mutex;
                    this.L$1 = null;
                    this.I$0 = i;
                    this.I$1 = 0;
                    this.label = 2;
                    if (audioCaptureEngine.closeWriterIfNeeded(true, SegmentEndReason.MANUAL, this) == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    obj2 = obj;
                    mutex2 = mutex;
                    Unit unit2 = Unit.INSTANCE;
                    mutex2.unlock(obj2);
                    AudioCaptureEngine.this.releaseRecorder();
                    if (this.$sessionId != null) {
                        this.L$0 = null;
                        this.label = 3;
                        if (AudioCaptureEngine.this.sessionRepository.setStatus(this.$sessionId, SessionStatus.COMPLETED, this) == coroutine_suspended) {
                            return coroutine_suspended;
                        }
                        this.label = 4;
                        if (AudioCaptureEngine.this.sessionRepository.applyRetention(this) == coroutine_suspended) {
                            return coroutine_suspended;
                        }
                    }
                    AudioCaptureEngine.this.pushEvent("Stopped");
                    AudioCaptureEngine.this.phase = CapturePhase.IDLE;
                    AudioCaptureEngine.this.publish(new CaptureStats(null, null, null, 0, false, 0.0f, 0.0f, AudioCaptureEngine.this.vad.getSpeechThreshold(), 0.0f, null, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, null, null, null, null, null, CollectionsKt.toList(AudioCaptureEngine.this.events), 16777087, null));
                    return Unit.INSTANCE;
                case 2:
                    int i2 = this.I$1;
                    int i3 = this.I$0;
                    obj2 = null;
                    mutex2 = (Mutex) this.L$0;
                    try {
                        ResultKt.throwOnFailure($result);
                        Unit unit3 = Unit.INSTANCE;
                        mutex2.unlock(obj2);
                        AudioCaptureEngine.this.releaseRecorder();
                        if (this.$sessionId != null) {
                            this.L$0 = null;
                            this.label = 3;
                            if (AudioCaptureEngine.this.sessionRepository.setStatus(this.$sessionId, SessionStatus.COMPLETED, this) == coroutine_suspended) {
                                return coroutine_suspended;
                            }
                            this.label = 4;
                            if (AudioCaptureEngine.this.sessionRepository.applyRetention(this) == coroutine_suspended) {
                                return coroutine_suspended;
                            }
                        }
                        AudioCaptureEngine.this.pushEvent("Stopped");
                        AudioCaptureEngine.this.phase = CapturePhase.IDLE;
                        AudioCaptureEngine.this.publish(new CaptureStats(null, null, null, 0, false, 0.0f, 0.0f, AudioCaptureEngine.this.vad.getSpeechThreshold(), 0.0f, null, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, null, null, null, null, null, CollectionsKt.toList(AudioCaptureEngine.this.events), 16777087, null));
                        return Unit.INSTANCE;
                    } catch (Throwable th2) {
                        th = th2;
                        mutex2.unlock(obj2);
                        throw th;
                    }
                case 3:
                    ResultKt.throwOnFailure($result);
                    this.label = 4;
                    if (AudioCaptureEngine.this.sessionRepository.applyRetention(this) == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    AudioCaptureEngine.this.pushEvent("Stopped");
                    AudioCaptureEngine.this.phase = CapturePhase.IDLE;
                    AudioCaptureEngine.this.publish(new CaptureStats(null, null, null, 0, false, 0.0f, 0.0f, AudioCaptureEngine.this.vad.getSpeechThreshold(), 0.0f, null, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, null, null, null, null, null, CollectionsKt.toList(AudioCaptureEngine.this.events), 16777087, null));
                    return Unit.INSTANCE;
                case 4:
                    ResultKt.throwOnFailure($result);
                    AudioCaptureEngine.this.pushEvent("Stopped");
                    AudioCaptureEngine.this.phase = CapturePhase.IDLE;
                    AudioCaptureEngine.this.publish(new CaptureStats(null, null, null, 0, false, 0.0f, 0.0f, AudioCaptureEngine.this.vad.getSpeechThreshold(), 0.0f, null, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, null, null, null, null, null, CollectionsKt.toList(AudioCaptureEngine.this.events), 16777087, null));
                    return Unit.INSTANCE;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code duplicated, block: B:28:0x00d5  */
    /* JADX WARN: Code duplicated, block: B:30:0x00d9  */
    /* JADX WARN: Code duplicated, block: B:33:0x00e2  */
    /* JADX WARN: Code duplicated, block: B:35:0x00e5  */
    /* JADX WARN: Code duplicated, block: B:37:0x00ed  */
    /* JADX WARN: Code duplicated, block: B:46:0x0139 A[RETURN] */
    /* JADX WARN: Code duplicated, block: B:47:0x013a  */
    /* JADX WARN: Code duplicated, block: B:68:0x0160 A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:70:0x0160 A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:71:0x015d A[ADDED_TO_REGION, REMOVE, SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:7:0x0018  */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:47:0x013a -> B:62:0x0141). Please report as a decompilation issue!!! */
    /*  JADX ERROR: JadxOverflowException in pass: RegionMakerVisitor
        jadx.core.utils.exceptions.JadxOverflowException: Regions count limit reached at block B:46:0x0139
        	at jadx.core.utils.ErrorsCounter.addError(ErrorsCounter.java:59)
        	at jadx.core.utils.ErrorsCounter.error(ErrorsCounter.java:31)
        	at jadx.core.dex.attributes.nodes.NotificationAttrNode.addError(NotificationAttrNode.java:19)
        */
    public final java.lang.Object runCaptureLoop(java.lang.String r17, kotlin.coroutines.Continuation<? super kotlin.Unit> r18) {
        /*
            Method dump skipped, instruction units count: 416
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.varun.pocketassistant.capture.AudioCaptureEngine.runCaptureLoop(java.lang.String, kotlin.coroutines.Continuation):java.lang.Object");
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code duplicated, block: B:65:0x025b A[RETURN] */
    /* JADX WARN: Code duplicated, block: B:66:0x025c  */
    /* JADX WARN: Code duplicated, block: B:7:0x001c  */
    public final Object processFrame(String sessionId, short[] frame, int length, Continuation<? super Unit> continuation) {
        C06451 c06451;
        float peak;
        VadDecision decision;
        long now;
        long now2;
        float rms;
        VadDecision decision2;
        float rms2;
        String sessionId2;
        VadDecision decision3;
        float rms3;
        float peak2;
        VadDecision decision4;
        String sessionId3;
        float rms4;
        VadDecision decision5;
        float peak3;
        String str;
        VadDecision decision6;
        float rms5;
        float peak4;
        short[] frame2 = frame;
        int length2 = length;
        if (continuation instanceof C06451) {
            c06451 = (C06451) continuation;
            if ((c06451.label & Integer.MIN_VALUE) != 0) {
                c06451.label -= Integer.MIN_VALUE;
            } else {
                c06451 = new C06451(continuation);
            }
        } else {
            c06451 = new C06451(continuation);
        }
        C06451 c06452 = c06451;
        Object $result = c06452.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (c06452.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                this.framesRead++;
                Pair<Float, Float> pairRmsAndPeak = rmsAndPeak(frame2, length2);
                float rms6 = pairRmsAndPeak.component1().floatValue();
                peak = pairRmsAndPeak.component2().floatValue();
                decision = this.vad.accept(frame2, length2);
                if (decision.getRawSpeech()) {
                    this.lastSpeechAtMs = Boxing.boxLong(System.currentTimeMillis());
                    if (!this.lastRawSpeech) {
                        float p = decision.getSpeechProbability();
                        if (p >= 0.0f) {
                            str = String.format("TEN VAD speech (p=%.2f)", Arrays.copyOf(new Object[]{Boxing.boxFloat(p)}, 1));
                            Intrinsics.checkNotNullExpressionValue(str, "format(...)");
                        } else {
                            str = "Speech energy above threshold (RMS " + ((int) rms6) + ")";
                        }
                        pushEvent(str);
                    }
                }
                this.lastRawSpeech = decision.getRawSpeech();
                if (!decision.getWindowOpen()) {
                    if (this.wasSpeech) {
                        c06452.L$0 = SpillingKt.nullOutSpilledVariable(sessionId);
                        c06452.L$1 = SpillingKt.nullOutSpilledVariable(frame2);
                        c06452.L$2 = decision;
                        c06452.I$0 = length2;
                        c06452.F$0 = rms6;
                        c06452.F$1 = peak;
                        c06452.label = 1;
                        if (closeWriterIfNeeded(true, SegmentEndReason.POST_ROLL, c06452) == coroutine_suspended) {
                            return coroutine_suspended;
                        }
                        decision5 = decision;
                        rms4 = rms6;
                        peak3 = peak;
                        this.wasSpeech = false;
                        this.phase = CapturePhase.LISTENING;
                        pushEvent("Segment closed after post-roll");
                        peak = peak3;
                        decision = decision5;
                    } else {
                        this.preRoll.push(frame2, length2);
                        this.phase = CapturePhase.LISTENING;
                        rms4 = rms6;
                    }
                    publishFrame(rms4, peak, decision, false);
                    return Unit.INSTANCE;
                }
                if (this.wasSpeech) {
                    WavWriter wavWriter = this.writer;
                    if (wavWriter != null) {
                        wavWriter.writePcm(frame2, 0, length2);
                    }
                    this.activeSegmentBytes += ((long) length2) * 2;
                    this.phase = decision.getRawSpeech() ? CapturePhase.LIVE_SPEECH : CapturePhase.POST_ROLL;
                    now = System.currentTimeMillis();
                    if (now - this.lastFlushAtMs >= 1000) {
                        WavWriter wavWriter2 = this.writer;
                        if (wavWriter2 != null) {
                            wavWriter2.flush();
                        }
                        this.lastFlushAtMs = now;
                    }
                    now2 = now - this.activeSegmentStartMs;
                    if (now2 >= maxSegmentMs()) {
                        pushEvent("Capture cap " + (maxSegmentMs() / ((long) 1000)) + "s — rolling to new segment");
                        c06452.L$0 = sessionId;
                        c06452.L$1 = frame2;
                        c06452.L$2 = decision;
                        c06452.I$0 = length2;
                        c06452.F$0 = rms6;
                        c06452.F$1 = peak;
                        c06452.J$0 = now;
                        c06452.J$1 = now2;
                        c06452.label = 3;
                        if (closeWriterIfNeeded(true, SegmentEndReason.DURATION_CAP, c06452) == coroutine_suspended) {
                            return coroutine_suspended;
                        }
                        rms2 = rms6;
                        sessionId2 = sessionId;
                        decision3 = decision;
                        c06452.L$0 = SpillingKt.nullOutSpilledVariable(sessionId2);
                        c06452.L$1 = SpillingKt.nullOutSpilledVariable(frame2);
                        c06452.L$2 = decision3;
                        c06452.I$0 = length2;
                        c06452.F$0 = rms2;
                        c06452.F$1 = peak;
                        c06452.J$0 = now;
                        c06452.J$1 = now2;
                        c06452.label = 4;
                        if (openSegment(sessionId2, frame2, length2, decision3, c06452) == coroutine_suspended) {
                            return coroutine_suspended;
                        }
                        decision6 = decision3;
                        rms5 = rms2;
                        peak4 = peak;
                        peak = peak4;
                        rms = rms5;
                        decision2 = decision6;
                    } else {
                        rms = rms6;
                        decision2 = decision;
                    }
                } else {
                    c06452.L$0 = SpillingKt.nullOutSpilledVariable(sessionId);
                    c06452.L$1 = SpillingKt.nullOutSpilledVariable(frame2);
                    c06452.L$2 = decision;
                    c06452.I$0 = length2;
                    c06452.F$0 = rms6;
                    c06452.F$1 = peak;
                    c06452.label = 2;
                    if (openSegment(sessionId, frame2, length2, decision, c06452) == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    rms3 = rms6;
                    peak2 = peak;
                    decision4 = decision;
                    sessionId3 = sessionId;
                    peak = peak2;
                    decision2 = decision4;
                    rms = rms3;
                }
                publishFrame(rms, peak, decision2, true);
                return Unit.INSTANCE;
            case 1:
                peak3 = c06452.F$1;
                rms4 = c06452.F$0;
                int i = c06452.I$0;
                decision5 = (VadDecision) c06452.L$2;
                ResultKt.throwOnFailure($result);
                this.wasSpeech = false;
                this.phase = CapturePhase.LISTENING;
                pushEvent("Segment closed after post-roll");
                peak = peak3;
                decision = decision5;
                publishFrame(rms4, peak, decision, false);
                return Unit.INSTANCE;
            case 2:
                peak2 = c06452.F$1;
                rms3 = c06452.F$0;
                int i2 = c06452.I$0;
                decision4 = (VadDecision) c06452.L$2;
                sessionId3 = (String) c06452.L$0;
                ResultKt.throwOnFailure($result);
                peak = peak2;
                decision2 = decision4;
                rms = rms3;
                publishFrame(rms, peak, decision2, true);
                return Unit.INSTANCE;
            case 3:
                long openForMs = c06452.J$1;
                long now3 = c06452.J$0;
                float peak5 = c06452.F$1;
                float rms7 = c06452.F$0;
                length2 = c06452.I$0;
                VadDecision decision7 = (VadDecision) c06452.L$2;
                frame2 = (short[]) c06452.L$1;
                String sessionId4 = (String) c06452.L$0;
                ResultKt.throwOnFailure($result);
                now2 = openForMs;
                now = now3;
                peak = peak5;
                rms2 = rms7;
                sessionId2 = sessionId4;
                decision3 = decision7;
                c06452.L$0 = SpillingKt.nullOutSpilledVariable(sessionId2);
                c06452.L$1 = SpillingKt.nullOutSpilledVariable(frame2);
                c06452.L$2 = decision3;
                c06452.I$0 = length2;
                c06452.F$0 = rms2;
                c06452.F$1 = peak;
                c06452.J$0 = now;
                c06452.J$1 = now2;
                c06452.label = 4;
                if (openSegment(sessionId2, frame2, length2, decision3, c06452) == coroutine_suspended) {
                    return coroutine_suspended;
                }
                decision6 = decision3;
                rms5 = rms2;
                peak4 = peak;
                peak = peak4;
                rms = rms5;
                decision2 = decision6;
                publishFrame(rms, peak, decision2, true);
                return Unit.INSTANCE;
            case 4:
                long j = c06452.J$1;
                long j2 = c06452.J$0;
                peak4 = c06452.F$1;
                rms5 = c06452.F$0;
                int i3 = c06452.I$0;
                decision6 = (VadDecision) c06452.L$2;
                ResultKt.throwOnFailure($result);
                peak = peak4;
                rms = rms5;
                decision2 = decision6;
                publishFrame(rms, peak, decision2, true);
                return Unit.INSTANCE;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final Object openSegment(String sessionId, short[] frame, int length, VadDecision decision, Continuation<? super Unit> continuation) {
        long j;
        this.phase = CapturePhase.PRE_ROLL_FLUSH;
        long startMs = System.currentTimeMillis();
        File file = this.audioStorage.newSegmentFile(sessionId, startMs);
        WavWriter newWriter = new WavWriter(file, 16000, 0, 4, null);
        short[] rolled = this.preRoll.drainSnapshot();
        long preRollMs = (((long) rolled.length) * 1000) / ((long) 16000);
        this.activeSegmentBytes = 0L;
        if (rolled.length == 0) {
            j = 2;
        } else {
            newWriter.writePcm(rolled, 0, rolled.length);
            j = 2;
            this.activeSegmentBytes = ((long) rolled.length) * 2;
        }
        newWriter.writePcm(frame, 0, length);
        this.activeSegmentBytes += ((long) length) * j;
        this.writer = newWriter;
        this.activeSegmentFile = file;
        this.activeSegmentStartMs = startMs - preRollMs;
        this.wasSpeech = true;
        this.preRoll.clear();
        pushEvent("Opened segment (+" + preRollMs + "ms pre-roll)");
        this.phase = decision.getRawSpeech() ? CapturePhase.LIVE_SPEECH : CapturePhase.POST_ROLL;
        return Unit.INSTANCE;
    }

    private final void publishFrame(float rms, float peak, VadDecision decision, boolean speechActive) {
        boolean z;
        long now = System.currentTimeMillis();
        this.rmsHistory.addLast(new RmsSample(now, rms, this.phase));
        long cutoff = now - 60000;
        while (!this.rmsHistory.isEmpty() && this.rmsHistory.first().getAtMs() < cutoff) {
            this.rmsHistory.removeFirst();
        }
        if (now - this.lastGraphPublishMs < GRAPH_PUBLISH_MS) {
            z = speechActive;
            if (z == this._stats.getValue().getSpeechActive()) {
                return;
            }
        } else {
            z = speechActive;
        }
        this.lastGraphPublishMs = now;
        long currentDur = this.wasSpeech ? now - this.activeSegmentStartMs : 0L;
        publish(CaptureStats.copy$default(this._stats.getValue(), this.paused ? CaptureState.PAUSED : CaptureState.RECORDING, this.phase, null, 0, z, rms, peak, this.vad.getSpeechThreshold(), decision.getSpeechProbability(), CollectionsKt.toList(this.rmsHistory), (((long) this.preRoll.getSize()) * 1000) / ((long) 16000), decision.getHangoverRemainingMs(), decision.getOpenGateProgressMs(), decision.getOpenGateRequiredMs(), this.activeSegmentBytes, currentDur, this.totalBytesWritten + this.activeSegmentBytes, this.framesRead, this.sessionStartedAtMs == 0 ? 0L : now - this.sessionStartedAtMs, this.lastSpeechAtMs, null, null, null, null, CollectionsKt.toList(this.events), 15728652, null));
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code duplicated, block: B:7:0x001a  */
    public final Object closeWriterIfNeeded(boolean finalize, String endReason, Continuation<? super Unit> continuation) {
        AnonymousClass1 anonymousClass1;
        long duration;
        String endReason2;
        long bytes;
        boolean finalize2 = finalize;
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
                WavWriter currentWriter = this.writer;
                if (currentWriter == null) {
                    return Unit.INSTANCE;
                }
                File file = this.activeSegmentFile;
                long startMs = this.activeSegmentStartMs;
                String sessionId = this._stats.getValue().getSessionId();
                long bytes2 = this.activeSegmentBytes;
                this.writer = null;
                this.activeSegmentFile = null;
                this.activeSegmentBytes = 0L;
                currentWriter.close();
                if (!finalize2 || file == null || sessionId == null) {
                    return Unit.INSTANCE;
                }
                long endMs = System.currentTimeMillis();
                long duration2 = endMs - startMs;
                if (duration2 < MIN_SEGMENT_MS || file.length() < MIN_SEGMENT_BYTES) {
                    file.delete();
                    pushEvent("Dropped tiny segment (" + duration2 + "ms)");
                    return Unit.INSTANCE;
                }
                this.totalBytesWritten += bytes2;
                SessionRepository sessionRepository = this.sessionRepository;
                anonymousClass2.L$0 = SpillingKt.nullOutSpilledVariable(endReason);
                anonymousClass2.L$1 = SpillingKt.nullOutSpilledVariable(currentWriter);
                anonymousClass2.L$2 = SpillingKt.nullOutSpilledVariable(file);
                anonymousClass2.L$3 = SpillingKt.nullOutSpilledVariable(sessionId);
                anonymousClass2.Z$0 = finalize2;
                anonymousClass2.J$0 = startMs;
                anonymousClass2.J$1 = bytes2;
                anonymousClass2.J$2 = endMs;
                anonymousClass2.J$3 = duration2;
                anonymousClass2.label = 1;
                if (sessionRepository.addSegment(sessionId, file, startMs, endMs, endReason, anonymousClass2) == coroutine_suspended) {
                    return coroutine_suspended;
                }
                duration = duration2;
                endReason2 = endReason;
                bytes = bytes2;
                break;
            case 1:
                duration = anonymousClass2.J$3;
                long j = anonymousClass2.J$2;
                bytes = anonymousClass2.J$1;
                long j2 = anonymousClass2.J$0;
                boolean finalize3 = anonymousClass2.Z$0;
                String endReason3 = (String) anonymousClass2.L$0;
                ResultKt.throwOnFailure($result);
                endReason2 = endReason3;
                finalize2 = finalize3;
                break;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
        pushEvent("Saved segment " + INSTANCE.formatBytes(bytes) + " / " + (duration / ((long) 1000)) + "s");
        publish(CaptureStats.copy$default(this._stats.getValue(), null, null, null, this._stats.getValue().getSegmentCount() + 1, false, 0.0f, 0.0f, 0.0f, 0.0f, null, 0L, 0L, 0L, 0L, 0L, 0L, this.totalBytesWritten, 0L, 0L, null, null, null, null, null, CollectionsKt.toList(this.events), 16662519, null));
        return Unit.INSTANCE;
    }

    static /* synthetic */ Object closeWriterIfNeeded$default(AudioCaptureEngine audioCaptureEngine, boolean z, String str, Continuation continuation, int i, Object obj) {
        if ((i & 2) != 0) {
            str = null;
        }
        return audioCaptureEngine.closeWriterIfNeeded(z, str, continuation);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void releaseRecorder() {
        try {
            AudioRecord audioRecord = this.audioRecord;
            if (audioRecord != null) {
                audioRecord.stop();
            }
        } catch (IllegalStateException e) {
        }
        AudioRecord audioRecord2 = this.audioRecord;
        if (audioRecord2 != null) {
            audioRecord2.release();
        }
        this.audioRecord = null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void publish(CaptureStats stats) {
        this._stats.setValue(stats);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void pushEvent(String message) {
        this.events.addLast(new CaptureEvent(System.currentTimeMillis(), message));
        while (this.events.size() > 40) {
            this.events.removeFirst();
        }
        Log.i(TAG, message);
    }

    private final Pair<Float, Float> rmsAndPeak(short[] samples, int length) {
        double sum = 0.0d;
        int peak = 0;
        for (int i = 0; i < length; i++) {
            short s = samples[i];
            sum += ((double) s) * ((double) s);
            peak = Math.max(peak, Math.abs((int) s));
        }
        return TuplesKt.to(Float.valueOf((float) Math.sqrt(sum / ((double) length))), Float.valueOf(peak));
    }

    private final long maxSegmentMs() {
        Object objM8304constructorimpl;
        PipelineConfig cfg = this.pipelineConfig;
        if (cfg == null) {
            return 120000L;
        }
        try {
            Result.Companion companion = Result.INSTANCE;
            AudioCaptureEngine audioCaptureEngine = this;
            objM8304constructorimpl = Result.m8304constructorimpl(cfg.load());
        } catch (Throwable th) {
            Result.Companion companion2 = Result.INSTANCE;
            objM8304constructorimpl = Result.m8304constructorimpl(ResultKt.createFailure(th));
        }
        if (Result.m8310isFailureimpl(objM8304constructorimpl)) {
            objM8304constructorimpl = null;
        }
        PipelineSettings settings = (PipelineSettings) objM8304constructorimpl;
        if (settings == null) {
            return 120000L;
        }
        boolean cloudPreferred = settings.getAsrMode() == ProviderMode.PREFER_CLOUD;
        if (cloudPreferred && cfg.cloudConfigured()) {
            return AsrAudioPreprocessor.CLOUD_MAX_SEGMENT_MS;
        }
        return 120000L;
    }

    /* JADX INFO: compiled from: AudioCaptureEngine.kt */
    @Metadata(d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0002\b\u0003\b\u0086\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003J\u000e\u0010\u000e\u001a\u00020\r2\u0006\u0010\u000f\u001a\u00020\bR\u000e\u0010\u0004\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0005X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\bX\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\bX\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\bX\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0082T¢\u0006\u0002\n\u0000¨\u0006\u0010"}, d2 = {"Lcom/varun/pocketassistant/capture/AudioCaptureEngine$Companion;", "", "<init>", "()V", "SAMPLE_RATE", "", "PRE_ROLL_MS", "GRAPH_WINDOW_MS", "", "GRAPH_PUBLISH_MS", "MIN_SEGMENT_MS", "MIN_SEGMENT_BYTES", "TAG", "", "formatBytes", "bytes", "app_debug"}, k = 1, mv = {2, 2, 0}, xi = 48)
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        public final String formatBytes(long bytes) {
            if (bytes < RealWebSocket.DEFAULT_MINIMUM_DEFLATE_SIZE) {
                return bytes + " B";
            }
            double kb = bytes / 1024.0d;
            String str = kb < 1024.0d ? String.format("%.1f KB", Arrays.copyOf(new Object[]{Double.valueOf(kb)}, 1)) : String.format("%.1f MB", Arrays.copyOf(new Object[]{Double.valueOf(kb / 1024.0d)}, 1));
            Intrinsics.checkNotNullExpressionValue(str, "format(...)");
            return str;
        }
    }
}
