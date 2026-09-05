package com.varun.pocketassistant.capture;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import androidx.core.app.NotificationCompat;
import androidx.core.app.ServiceCompat;
import com.varun.pocketassistant.MainActivity;
import com.varun.pocketassistant.PocketAssistantApp;
import com.varun.pocketassistant.R;
import java.util.Arrays;
import java.util.concurrent.CancellationException;
import kotlin.Metadata;
import kotlin.NoWhenBranchMatchedException;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.BuildersKt__Builders_commonKt;
import kotlinx.coroutines.CompletableJob;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.CoroutineScopeKt;
import kotlinx.coroutines.Dispatchers;
import kotlinx.coroutines.Job;
import kotlinx.coroutines.SupervisorKt;
import kotlinx.coroutines.flow.FlowKt;

/* JADX INFO: compiled from: RecordingService.kt */
/* JADX INFO: loaded from: classes5.dex */
@Metadata(d1 = {"\u0000Z\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0007\u0018\u0000 $2\u00020\u0001:\u0001$B\u0007¢\u0006\u0004\b\u0002\u0010\u0003J\b\u0010\u000e\u001a\u00020\u000fH\u0016J\"\u0010\u0010\u001a\u00020\u00112\b\u0010\u0012\u001a\u0004\u0018\u00010\u00132\u0006\u0010\u0014\u001a\u00020\u00112\u0006\u0010\u0015\u001a\u00020\u0011H\u0016J\b\u0010\u0016\u001a\u00020\u000fH\u0002J\u0010\u0010\u0017\u001a\u00020\u000f2\u0006\u0010\u0018\u001a\u00020\u0019H\u0002J\u0010\u0010\u001a\u001a\u00020\u001b2\u0006\u0010\u0018\u001a\u00020\u0019H\u0002J\u0018\u0010\u001c\u001a\u00020\u001d2\u0006\u0010\u001e\u001a\u00020\r2\u0006\u0010\u001f\u001a\u00020\u0011H\u0002J\b\u0010 \u001a\u00020\u000fH\u0002J\b\u0010!\u001a\u00020\u000fH\u0016J\u0014\u0010\"\u001a\u0004\u0018\u00010#2\b\u0010\u0012\u001a\u0004\u0018\u00010\u0013H\u0016R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082.¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\f\u001a\u0004\u0018\u00010\rX\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006%"}, d2 = {"Lcom/varun/pocketassistant/capture/RecordingService;", "Landroid/app/Service;", "<init>", "()V", "serviceJob", "Lkotlinx/coroutines/CompletableJob;", "serviceScope", "Lkotlinx/coroutines/CoroutineScope;", "engine", "Lcom/varun/pocketassistant/capture/AudioCaptureEngine;", "lastNotifyAtMs", "", "lastNotifyKey", "", "onCreate", "", "onStartCommand", "", "intent", "Landroid/content/Intent;", "flags", "startId", "startAsForeground", "maybeUpdateNotification", "stats", "Lcom/varun/pocketassistant/capture/CaptureStats;", "buildNotification", "Landroid/app/Notification;", "servicePendingIntent", "Landroid/app/PendingIntent;", "action", "requestCode", "createChannel", "onDestroy", "onBind", "Landroid/os/IBinder;", "Companion", "app_debug"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class RecordingService extends Service {
    public static final String ACTION_PAUSE = "com.varun.pocketassistant.action.PAUSE";
    public static final String ACTION_RESUME = "com.varun.pocketassistant.action.RESUME";
    public static final String ACTION_START = "com.varun.pocketassistant.action.START";
    public static final String ACTION_STOP = "com.varun.pocketassistant.action.STOP";
    public static final String CHANNEL_ID = "recording";
    public static final int NOTIFICATION_ID = 42;
    private AudioCaptureEngine engine;
    private long lastNotifyAtMs;
    private String lastNotifyKey;
    private final CompletableJob serviceJob = SupervisorKt.SupervisorJob$default((Job) null, 1, (Object) null);
    private final CoroutineScope serviceScope = CoroutineScopeKt.CoroutineScope(this.serviceJob.plus(Dispatchers.getMain().getImmediate()));

    /* JADX INFO: renamed from: Companion, reason: from kotlin metadata */
    public static final Companion INSTANCE = new Companion(null);
    public static final int $stable = 8;

    /* JADX INFO: compiled from: RecordingService.kt */
    @Metadata(k = 3, mv = {2, 2, 0}, xi = 48)
    public static final /* synthetic */ class WhenMappings {
        public static final /* synthetic */ int[] $EnumSwitchMapping$0;

        static {
            int[] iArr = new int[CapturePhase.values().length];
            try {
                iArr[CapturePhase.LIVE_SPEECH.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                iArr[CapturePhase.PRE_ROLL_FLUSH.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                iArr[CapturePhase.POST_ROLL.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                iArr[CapturePhase.LISTENING.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                iArr[CapturePhase.PAUSED.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            try {
                iArr[CapturePhase.IDLE.ordinal()] = 6;
            } catch (NoSuchFieldError e6) {
            }
            $EnumSwitchMapping$0 = iArr;
        }
    }

    @Override // android.app.Service
    public void onCreate() {
        super.onCreate();
        createChannel();
        Application application = getApplication();
        Intrinsics.checkNotNull(application, "null cannot be cast to non-null type com.varun.pocketassistant.PocketAssistantApp");
        PocketAssistantApp app = (PocketAssistantApp) application;
        CoroutineScope ioScope = CoroutineScopeKt.CoroutineScope(this.serviceJob.plus(Dispatchers.getIO()));
        this.engine = new AudioCaptureEngine(this, app.getContainer().getSessionRepository(), app.getContainer().getAudioStorage(), ioScope, app.getContainer().getPipelineConfig(), null, 32, null);
        RecordingHub recordingHub = RecordingHub.INSTANCE;
        AudioCaptureEngine audioCaptureEngine = this.engine;
        if (audioCaptureEngine == null) {
            Intrinsics.throwUninitializedPropertyAccessException("engine");
            audioCaptureEngine = null;
        }
        recordingHub.bind(audioCaptureEngine);
        BuildersKt__Builders_commonKt.launch$default(this.serviceScope, null, null, new AnonymousClass1(null), 3, null);
    }

    /* JADX INFO: renamed from: com.varun.pocketassistant.capture.RecordingService$onCreate$1, reason: invalid class name */
    /* JADX INFO: compiled from: RecordingService.kt */
    @Metadata(d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H\n"}, d2 = {"<anonymous>", "", "Lkotlinx/coroutines/CoroutineScope;"}, k = 3, mv = {2, 2, 0}, xi = 48)
    @DebugMetadata(c = "com.varun.pocketassistant.capture.RecordingService$onCreate$1", f = "RecordingService.kt", i = {}, l = {50}, m = "invokeSuspend", n = {}, s = {})
    static final class AnonymousClass1 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
        int label;

        AnonymousClass1(Continuation<? super AnonymousClass1> continuation) {
            super(2, continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
            return RecordingService.this.new AnonymousClass1(continuation);
        }

        @Override // kotlin.jvm.functions.Function2
        public final Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
            return ((AnonymousClass1) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
        }

        /* JADX INFO: renamed from: com.varun.pocketassistant.capture.RecordingService$onCreate$1$1, reason: invalid class name and collision with other inner class name */
        /* JADX INFO: compiled from: RecordingService.kt */
        @Metadata(d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\n"}, d2 = {"<anonymous>", "", "stats", "Lcom/varun/pocketassistant/capture/CaptureStats;"}, k = 3, mv = {2, 2, 0}, xi = 48)
        @DebugMetadata(c = "com.varun.pocketassistant.capture.RecordingService$onCreate$1$1", f = "RecordingService.kt", i = {}, l = {}, m = "invokeSuspend", n = {}, s = {})
        static final class C01581 extends SuspendLambda implements Function2<CaptureStats, Continuation<? super Unit>, Object> {
            /* synthetic */ Object L$0;
            int label;
            final /* synthetic */ RecordingService this$0;

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            C01581(RecordingService recordingService, Continuation<? super C01581> continuation) {
                super(2, continuation);
                this.this$0 = recordingService;
            }

            @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
            public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
                C01581 c01581 = new C01581(this.this$0, continuation);
                c01581.L$0 = obj;
                return c01581;
            }

            @Override // kotlin.jvm.functions.Function2
            public final Object invoke(CaptureStats captureStats, Continuation<? super Unit> continuation) {
                return ((C01581) create(captureStats, continuation)).invokeSuspend(Unit.INSTANCE);
            }

            @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
            public final Object invokeSuspend(Object $result) {
                CaptureStats stats = (CaptureStats) this.L$0;
                IntrinsicsKt.getCOROUTINE_SUSPENDED();
                switch (this.label) {
                    case 0:
                        ResultKt.throwOnFailure($result);
                        RecordingHub.INSTANCE.publish(stats);
                        this.this$0.maybeUpdateNotification(stats);
                        return Unit.INSTANCE;
                    default:
                        throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
            }
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object $result) {
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    AudioCaptureEngine audioCaptureEngine = RecordingService.this.engine;
                    if (audioCaptureEngine == null) {
                        Intrinsics.throwUninitializedPropertyAccessException("engine");
                        audioCaptureEngine = null;
                    }
                    this.label = 1;
                    if (FlowKt.collectLatest(audioCaptureEngine.getStats(), new C01581(RecordingService.this, null), this) == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    break;
                case 1:
                    ResultKt.throwOnFailure($result);
                    break;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
            return Unit.INSTANCE;
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code duplicated, block: B:43:0x007d  */
    /* JADX WARN: Code duplicated, block: B:45:0x0084  */
    /* JADX WARN: Code duplicated, block: B:46:0x0088  */
    @Override // android.app.Service
    public int onStartCommand(Intent intent, int flags, int startId) {
        AudioCaptureEngine audioCaptureEngine;
        AudioCaptureEngine audioCaptureEngine2 = null;
        String action = intent != null ? intent.getAction() : null;
        if (action != null) {
            switch (action) {
                case "com.varun.pocketassistant.action.STOP":
                    AudioCaptureEngine audioCaptureEngine3 = this.engine;
                    if (audioCaptureEngine3 == null) {
                        Intrinsics.throwUninitializedPropertyAccessException("engine");
                    } else {
                        audioCaptureEngine2 = audioCaptureEngine3;
                    }
                    audioCaptureEngine2.stop();
                    stopForeground(1);
                    stopSelf();
                    break;
                case "com.varun.pocketassistant.action.RESUME":
                    startAsForeground();
                    AudioCaptureEngine audioCaptureEngine4 = this.engine;
                    if (audioCaptureEngine4 == null) {
                        Intrinsics.throwUninitializedPropertyAccessException("engine");
                    } else {
                        audioCaptureEngine2 = audioCaptureEngine4;
                    }
                    audioCaptureEngine2.resume();
                    break;
                case "com.varun.pocketassistant.action.PAUSE":
                    AudioCaptureEngine audioCaptureEngine5 = this.engine;
                    if (audioCaptureEngine5 == null) {
                        Intrinsics.throwUninitializedPropertyAccessException("engine");
                    } else {
                        audioCaptureEngine2 = audioCaptureEngine5;
                    }
                    audioCaptureEngine2.pause();
                    break;
                case "com.varun.pocketassistant.action.START":
                    startAsForeground();
                    AudioCaptureEngine audioCaptureEngine6 = this.engine;
                    if (audioCaptureEngine6 == null) {
                        Intrinsics.throwUninitializedPropertyAccessException("engine");
                    } else {
                        audioCaptureEngine2 = audioCaptureEngine6;
                    }
                    audioCaptureEngine2.start();
                    break;
                default:
                    startAsForeground();
                    audioCaptureEngine = this.engine;
                    if (audioCaptureEngine == null) {
                        Intrinsics.throwUninitializedPropertyAccessException("engine");
                    } else {
                        audioCaptureEngine2 = audioCaptureEngine;
                    }
                    audioCaptureEngine2.start();
                    break;
            }
        } else {
            startAsForeground();
            audioCaptureEngine = this.engine;
            if (audioCaptureEngine == null) {
                Intrinsics.throwUninitializedPropertyAccessException("engine");
            } else {
                audioCaptureEngine2 = audioCaptureEngine;
            }
            audioCaptureEngine2.start();
        }
        return 1;
    }

    private final void startAsForeground() {
        RecordingService recordingService = this;
        AudioCaptureEngine audioCaptureEngine = this.engine;
        if (audioCaptureEngine == null) {
            Intrinsics.throwUninitializedPropertyAccessException("engine");
            audioCaptureEngine = null;
        }
        ServiceCompat.startForeground(recordingService, 42, buildNotification(audioCaptureEngine.getStats().getValue()), 128);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void maybeUpdateNotification(CaptureStats stats) {
        String key = stats.getState() + "|" + stats.getPhase() + "|" + stats.getSegmentCount() + "|" + stats.getCaptionsStatus() + "|" + (stats.getCurrentSegmentBytes() / ((long) 262144));
        long now = System.currentTimeMillis();
        if (!Intrinsics.areEqual(key, this.lastNotifyKey) || now - this.lastNotifyAtMs >= 1000) {
            this.lastNotifyKey = key;
            this.lastNotifyAtMs = now;
            ((NotificationManager) getSystemService(NotificationManager.class)).notify(42, buildNotification(stats));
        }
    }

    private final Notification buildNotification(CaptureStats stats) {
        String title;
        String text;
        PendingIntent openIntent = PendingIntent.getActivity(this, 0, new Intent(this, (Class<?>) MainActivity.class), 201326592);
        switch (WhenMappings.$EnumSwitchMapping$0[stats.getPhase().ordinal()]) {
            case 1:
                title = "Live speech — writing";
                break;
            case 2:
                title = "Flushing pre-roll buffer";
                break;
            case 3:
                title = "Post-roll (" + (stats.getHangoverRemainingMs() / ((long) 1000)) + "s left)";
                break;
            case 4:
                title = "Listening — buffering " + (stats.getPreRollBufferedMs() / ((long) 1000)) + "s";
                break;
            case 5:
                title = getString(R.string.notification_paused);
                Intrinsics.checkNotNullExpressionValue(title, "getString(...)");
                break;
            case 6:
                title = getString(R.string.app_name);
                Intrinsics.checkNotNullExpressionValue(title, "getString(...)");
                break;
            default:
                throw new NoWhenBranchMatchedException();
        }
        String openMb = AudioCaptureEngine.INSTANCE.formatBytes(stats.getCurrentSegmentBytes());
        if (stats.getSpeechProbability() >= 0.0f) {
            text = String.format("VAD p=%.2f · seg %d · open %s · %s", Arrays.copyOf(new Object[]{Float.valueOf(stats.getSpeechProbability()), Integer.valueOf(stats.getSegmentCount()), openMb, stats.getPhase().name()}, 4));
            Intrinsics.checkNotNullExpressionValue(text, "format(...)");
        } else {
            text = "RMS " + ((int) stats.getRms()) + "/" + ((int) stats.getSpeechThreshold()) + " · seg " + stats.getSegmentCount() + " · open " + openMb + " · " + stats.getPhase().name();
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID).setContentTitle(title).setContentText(text).setStyle(new NotificationCompat.BigTextStyle().bigText(text)).setSmallIcon(R.drawable.ic_mic).setContentIntent(openIntent).setOngoing(stats.getState() != CaptureState.IDLE).setOnlyAlertOnce(true).setForegroundServiceBehavior(1).setCategory(NotificationCompat.CATEGORY_SERVICE);
        Intrinsics.checkNotNullExpressionValue(builder, "setCategory(...)");
        if (stats.getState() == CaptureState.RECORDING) {
            builder.addAction(0, "Pause", servicePendingIntent(ACTION_PAUSE, 1));
        } else if (stats.getState() == CaptureState.PAUSED) {
            builder.addAction(0, "Resume", servicePendingIntent(ACTION_RESUME, 2));
        }
        builder.addAction(0, "Stop", servicePendingIntent(ACTION_STOP, 3));
        Notification notificationBuild = builder.build();
        Intrinsics.checkNotNullExpressionValue(notificationBuild, "build(...)");
        return notificationBuild;
    }

    private final PendingIntent servicePendingIntent(String action, int requestCode) {
        Intent intent = new Intent(this, (Class<?>) RecordingService.class).setAction(action);
        Intrinsics.checkNotNullExpressionValue(intent, "setAction(...)");
        PendingIntent service = PendingIntent.getService(this, requestCode, intent, 201326592);
        Intrinsics.checkNotNullExpressionValue(service, "getService(...)");
        return service;
    }

    private final void createChannel() {
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, getString(R.string.notification_channel_name), 2);
        channel.setDescription(getString(R.string.notification_channel_desc));
        ((NotificationManager) getSystemService(NotificationManager.class)).createNotificationChannel(channel);
    }

    @Override // android.app.Service
    public void onDestroy() {
        RecordingHub recordingHub = RecordingHub.INSTANCE;
        AudioCaptureEngine audioCaptureEngine = this.engine;
        if (audioCaptureEngine == null) {
            Intrinsics.throwUninitializedPropertyAccessException("engine");
            audioCaptureEngine = null;
        }
        recordingHub.unbind(audioCaptureEngine);
        CoroutineScopeKt.cancel$default(this.serviceScope, null, 1, null);
        Job.DefaultImpls.cancel$default((Job) this.serviceJob, (CancellationException) null, 1, (Object) null);
        super.onDestroy();
    }

    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        return null;
    }

    /* JADX INFO: compiled from: RecordingService.kt */
    @Metadata(d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0086\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003J\u000e\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000fJ\u000e\u0010\u0010\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000fJ\u000e\u0010\u0011\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000fJ\u000e\u0010\u0012\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000fR\u000e\u0010\u0004\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000¨\u0006\u0013"}, d2 = {"Lcom/varun/pocketassistant/capture/RecordingService$Companion;", "", "<init>", "()V", "CHANNEL_ID", "", "NOTIFICATION_ID", "", "ACTION_START", "ACTION_PAUSE", "ACTION_RESUME", "ACTION_STOP", "start", "", "context", "Landroid/content/Context;", "pause", "resume", "stop", "app_debug"}, k = 1, mv = {2, 2, 0}, xi = 48)
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        public final void start(Context context) {
            Intrinsics.checkNotNullParameter(context, "context");
            context.startForegroundService(new Intent(context, (Class<?>) RecordingService.class).setAction(RecordingService.ACTION_START));
        }

        public final void pause(Context context) {
            Intrinsics.checkNotNullParameter(context, "context");
            context.startService(new Intent(context, (Class<?>) RecordingService.class).setAction(RecordingService.ACTION_PAUSE));
        }

        public final void resume(Context context) {
            Intrinsics.checkNotNullParameter(context, "context");
            context.startForegroundService(new Intent(context, (Class<?>) RecordingService.class).setAction(RecordingService.ACTION_RESUME));
        }

        public final void stop(Context context) {
            Intrinsics.checkNotNullParameter(context, "context");
            context.startService(new Intent(context, (Class<?>) RecordingService.class).setAction(RecordingService.ACTION_STOP));
        }
    }
}
