package com.varun.pocketassistant;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import androidx.activity.ComponentActivity;
import androidx.activity.EdgeToEdge;
import androidx.activity.compose.ComponentActivityKt;
import com.varun.pocketassistant.data.MeetingEntity;
import com.varun.pocketassistant.meeting.GapClusterer;
import com.varun.pocketassistant.pipeline.PipelineConfig;
import com.varun.pocketassistant.pipeline.PipelineSettings;
import com.varun.pocketassistant.pipeline.ProviderMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import kotlin.Metadata;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SpillingKt;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import kotlinx.coroutines.BuildersKt__Builders_commonKt;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.CoroutineScopeKt;
import kotlinx.coroutines.Dispatchers;

/* JADX INFO: compiled from: MainActivity.kt */
/* JADX INFO: loaded from: classes4.dex */
@Metadata(d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0007\u0018\u0000 \f2\u00020\u0001:\u0001\fB\u0007¢\u0006\u0004\b\u0002\u0010\u0003J\u0012\u0010\u0004\u001a\u00020\u00052\b\u0010\u0006\u001a\u0004\u0018\u00010\u0007H\u0014J\u0010\u0010\b\u001a\u00020\u00052\u0006\u0010\t\u001a\u00020\nH\u0014J\b\u0010\u000b\u001a\u00020\u0005H\u0002¨\u0006\r"}, d2 = {"Lcom/varun/pocketassistant/MainActivity;", "Landroidx/activity/ComponentActivity;", "<init>", "()V", "onCreate", "", "savedInstanceState", "Landroid/os/Bundle;", "onNewIntent", "intent", "Landroid/content/Intent;", "applyDebugPipelineExtras", "Companion", "app_debug"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class MainActivity extends ComponentActivity {
    public static final String EXTRA_ALLOW_FALLBACK = "pipeline_allow_fallback";
    public static final String EXTRA_API_KEY = "pipeline_api_key";
    public static final String EXTRA_ASR_MODE = "pipeline_asr_mode";
    public static final String EXTRA_ASR_MODEL = "pipeline_asr_model";
    public static final String EXTRA_BASE_URL = "pipeline_base_url";
    public static final String EXTRA_BENCH_CLEANUP = "pipeline_bench_cleanup";
    public static final String EXTRA_BENCH_MEETING = "pipeline_bench_meeting";
    public static final String EXTRA_BENCH_MODEL = "pipeline_bench_model";
    public static final String EXTRA_BENCH_NO_REASONING = "pipeline_bench_no_reasoning";
    public static final String EXTRA_BENCH_TIMEOUT_MS = "pipeline_bench_timeout_ms";
    public static final String EXTRA_CHAT_MODEL = "pipeline_chat_model";
    public static final String EXTRA_CLEANUP_MODE = "pipeline_cleanup_mode";
    public static final String EXTRA_CLEANUP_MODEL = "pipeline_cleanup_model";
    public static final String EXTRA_DETECT_TODAY = "pipeline_detect_today";
    public static final String EXTRA_REASONING = "pipeline_reasoning";
    public static final String EXTRA_REQUEUE_ASR = "pipeline_requeue_asr";
    public static final String EXTRA_REQUEUE_ASR_TODAY = "pipeline_requeue_asr_today";
    public static final String EXTRA_REQUEUE_MEETINGS = "pipeline_requeue_meetings";
    public static final String EXTRA_STT_LANG = "pipeline_stt_lang";
    public static final String EXTRA_SUMMARY_MODE = "pipeline_summary_mode";
    public static final String EXTRA_SUMMARY_MODEL = "pipeline_summary_model";
    private static final String TAG = "MainActivity";
    public static final int $stable = 8;

    @Override // androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        applyDebugPipelineExtras();
        EdgeToEdge.enable$default(this, null, null, 3, null);
        ComponentActivityKt.setContent$default(this, null, ComposableSingletons$MainActivityKt.INSTANCE.getLambda$2088334923$app_debug(), 1, null);
    }

    @Override // androidx.activity.ComponentActivity, android.app.Activity
    protected void onNewIntent(Intent intent) {
        Intrinsics.checkNotNullParameter(intent, "intent");
        super.onNewIntent(intent);
        setIntent(intent);
        applyDebugPipelineExtras();
    }

    /* JADX WARN: Code duplicated, block: B:133:0x01d2  */
    /* JADX WARN: Code duplicated, block: B:135:0x01d6  */
    /* JADX WARN: Code duplicated, block: B:136:0x01d8  */
    /* JADX WARN: Code duplicated, block: B:138:0x01e4  */
    /* JADX WARN: Code duplicated, block: B:139:0x01e7  */
    /* JADX WARN: Code duplicated, block: B:157:0x0233  */
    /* JADX WARN: Code duplicated, block: B:159:0x023f  */
    /* JADX WARN: Code duplicated, block: B:174:0x027a  */
    /* JADX WARN: Code duplicated, block: B:176:0x0286  */
    /* JADX WARN: Code duplicated, block: B:67:0x00d6  */
    private final void applyDebugPipelineExtras() {
        boolean z;
        CoroutineScope scope;
        boolean z2;
        String wantMeeting;
        String string;
        ProviderMode asrMode;
        ProviderMode cleanupMode;
        ProviderMode summaryMode;
        boolean allowFallback;
        String str;
        String cloudAsrModel;
        String asrModel;
        String cloudCleanupModel;
        String str2;
        String cloudSummaryModel;
        String str3;
        String string2;
        String string3;
        String string4;
        String stringExtra;
        if (BuildConfig.DEBUG) {
            Application application = getApplication();
            Intrinsics.checkNotNull(application, "null cannot be cast to non-null type com.varun.pocketassistant.PocketAssistantApp");
            PocketAssistantApp app = (PocketAssistantApp) application;
            PipelineConfig config = app.getContainer().getPipelineConfig();
            PipelineSettings cur = config.load();
            Intent intent = getIntent();
            String key = (intent == null || (stringExtra = intent.getStringExtra(EXTRA_API_KEY)) == null) ? null : StringsKt.trim((CharSequence) stringExtra).toString();
            if (key == null) {
                key = "";
            }
            Intent intent2 = getIntent();
            boolean requeueToday = intent2 != null && intent2.getBooleanExtra(EXTRA_REQUEUE_ASR_TODAY, false);
            Intent intent3 = getIntent();
            boolean requeueAll = intent3 != null && intent3.getBooleanExtra(EXTRA_REQUEUE_ASR, false);
            Intent intent4 = getIntent();
            boolean requeueMeetings = intent4 != null && intent4.getBooleanExtra(EXTRA_REQUEUE_MEETINGS, false);
            Intent intent5 = getIntent();
            boolean detectToday = intent5 != null && intent5.getBooleanExtra(EXTRA_DETECT_TODAY, false);
            Intent intent6 = getIntent();
            boolean benchCleanup = intent6 != null && intent6.getBooleanExtra(EXTRA_BENCH_CLEANUP, false);
            if (key.length() > 0) {
                z = true;
            } else {
                Intent intent7 = getIntent();
                if (intent7 != null && intent7.hasExtra(EXTRA_ASR_MODEL)) {
                    z = true;
                } else {
                    Intent intent8 = getIntent();
                    if ((intent8 != null && intent8.hasExtra(EXTRA_ASR_MODE)) || requeueToday) {
                        z = true;
                    } else {
                        z = false;
                    }
                }
            }
            boolean hasConfigExtras = z;
            if (hasConfigExtras || requeueAll || requeueMeetings || detectToday || benchCleanup) {
                if (hasConfigExtras) {
                    String stringExtra2 = getIntent().getStringExtra(EXTRA_ASR_MODE);
                    if (stringExtra2 == null || (asrMode = PipelineConfig.INSTANCE.parseMode(stringExtra2)) == null) {
                        asrMode = key.length() > 0 ? ProviderMode.PREFER_CLOUD : cur.getAsrMode();
                    }
                    String stringExtra3 = getIntent().getStringExtra(EXTRA_CLEANUP_MODE);
                    if (stringExtra3 == null || (cleanupMode = PipelineConfig.INSTANCE.parseMode(stringExtra3)) == null) {
                        cleanupMode = key.length() > 0 ? ProviderMode.PREFER_CLOUD : cur.getCleanupMode();
                    }
                    String stringExtra4 = getIntent().getStringExtra(EXTRA_SUMMARY_MODE);
                    if (stringExtra4 == null || (summaryMode = PipelineConfig.INSTANCE.parseMode(stringExtra4)) == null) {
                        summaryMode = key.length() > 0 ? cleanupMode : cur.getSummaryMode();
                    }
                    if (getIntent().hasExtra(EXTRA_ALLOW_FALLBACK)) {
                        allowFallback = getIntent().getBooleanExtra(EXTRA_ALLOW_FALLBACK, false);
                    } else {
                        allowFallback = (PipelineConfig.INSTANCE.legacyImpliesNoFallback(getIntent().getStringExtra(EXTRA_ASR_MODE)) || PipelineConfig.INSTANCE.legacyImpliesNoFallback(getIntent().getStringExtra(EXTRA_CLEANUP_MODE))) ? false : cur.getAllowFallback();
                    }
                    String stringExtra5 = getIntent().getStringExtra(EXTRA_ASR_MODEL);
                    if (stringExtra5 == null || (string4 = StringsKt.trim((CharSequence) stringExtra5).toString()) == null) {
                        str = PipelineSettings.DEFAULT_ASR_MODEL;
                        if (requeueToday) {
                            asrModel = PipelineSettings.DEFAULT_ASR_MODEL;
                        } else {
                            cloudAsrModel = cur.getCloudAsrModel();
                            if (StringsKt.isBlank(cloudAsrModel)) {
                                str = cloudAsrModel;
                            }
                            asrModel = str;
                        }
                    } else {
                        String str4 = string4;
                        if (StringsKt.isBlank(str4)) {
                            str4 = null;
                        }
                        String str5 = str4;
                        if (str5 == null) {
                            str = PipelineSettings.DEFAULT_ASR_MODEL;
                            if (requeueToday) {
                                asrModel = PipelineSettings.DEFAULT_ASR_MODEL;
                            } else {
                                cloudAsrModel = cur.getCloudAsrModel();
                                if (StringsKt.isBlank(cloudAsrModel)) {
                                    str = cloudAsrModel;
                                }
                                asrModel = str;
                            }
                        } else {
                            asrModel = str5;
                        }
                    }
                    String cloudApiKey = key;
                    if (StringsKt.isBlank(cloudApiKey)) {
                        cloudApiKey = cur.getCloudApiKey();
                    }
                    String str6 = cloudApiKey;
                    String stringExtra6 = getIntent().getStringExtra(EXTRA_CLEANUP_MODEL);
                    if (stringExtra6 == null) {
                        stringExtra6 = getIntent().getStringExtra(EXTRA_CHAT_MODEL);
                    }
                    if (stringExtra6 == null || (string3 = StringsKt.trim((CharSequence) stringExtra6).toString()) == null) {
                        cloudCleanupModel = cur.getCloudCleanupModel();
                        if (StringsKt.isBlank(cloudCleanupModel)) {
                            cloudCleanupModel = "google/gemini-2.5-flash";
                        }
                        str2 = cloudCleanupModel;
                    } else {
                        String str7 = string3;
                        if (StringsKt.isBlank(str7)) {
                            str7 = null;
                        }
                        str2 = str7;
                        if (str2 == null) {
                            cloudCleanupModel = cur.getCloudCleanupModel();
                            if (StringsKt.isBlank(cloudCleanupModel)) {
                                cloudCleanupModel = "google/gemini-2.5-flash";
                            }
                            str2 = cloudCleanupModel;
                        }
                    }
                    String stringExtra7 = getIntent().getStringExtra(EXTRA_SUMMARY_MODEL);
                    if (stringExtra7 == null) {
                        stringExtra7 = getIntent().getStringExtra(EXTRA_CHAT_MODEL);
                    }
                    if (stringExtra7 == null || (string2 = StringsKt.trim((CharSequence) stringExtra7).toString()) == null) {
                        cloudSummaryModel = cur.getCloudSummaryModel();
                        if (StringsKt.isBlank(cloudSummaryModel)) {
                            cloudSummaryModel = PipelineSettings.DEFAULT_SUMMARY_MODEL;
                        }
                        str3 = cloudSummaryModel;
                    } else {
                        String str8 = string2;
                        if (StringsKt.isBlank(str8)) {
                            str8 = null;
                        }
                        str3 = str8;
                        if (str3 == null) {
                            cloudSummaryModel = cur.getCloudSummaryModel();
                            if (StringsKt.isBlank(cloudSummaryModel)) {
                                cloudSummaryModel = PipelineSettings.DEFAULT_SUMMARY_MODEL;
                            }
                            str3 = cloudSummaryModel;
                        }
                    }
                    String stringExtra8 = getIntent().getStringExtra(EXTRA_REASONING);
                    String string5 = stringExtra8 != null ? StringsKt.trim((CharSequence) stringExtra8).toString() : null;
                    if (string5 == null) {
                        string5 = "";
                    }
                    String reasoningEffort = string5;
                    if (StringsKt.isBlank(reasoningEffort)) {
                        reasoningEffort = cur.getReasoningEffort();
                    }
                    String str9 = reasoningEffort;
                    String stringExtra9 = getIntent().getStringExtra(EXTRA_STT_LANG);
                    String string6 = stringExtra9 != null ? StringsKt.trim((CharSequence) stringExtra9).toString() : null;
                    String str10 = string6 != null ? string6 : "";
                    if (StringsKt.isBlank(str10)) {
                        String sttLanguage = cur.getSttLanguage();
                        if (StringsKt.isBlank(sttLanguage)) {
                            sttLanguage = "en";
                        }
                        str10 = sttLanguage;
                    }
                    ProviderMode summaryMode2 = summaryMode;
                    ProviderMode summaryMode3 = asrMode;
                    PipelineSettings next = PipelineSettings.copy$default(cur, summaryMode3, cleanupMode, summaryMode2, summaryMode, allowFallback, PipelineSettings.DEFAULT_BASE_URL, str6, asrModel, str2, str3, str9, false, str10, requeueToday ? true : cur.getAsrDiarizeEnabled(), null, null, null, null, null, 509952, null);
                    config.save(next);
                    Log.i(TAG, "Debug pipeline configured: asr=" + next.getCloudAsrModel() + " diarize=" + next.getAsrDiarizeEnabled() + " cleanup=" + next.getCloudCleanupModel() + " summary=" + next.getCloudSummaryModel() + " modes=" + next.getAsrMode() + "/" + next.getCleanupMode() + "/" + next.getSummaryMode() + " fallback=" + next.getAllowFallback());
                }
                CoroutineScope scope2 = CoroutineScopeKt.CoroutineScope(Dispatchers.getIO());
                if (requeueAll) {
                    BuildersKt__Builders_commonKt.launch$default(scope2, null, null, new AnonymousClass1(app, null), 3, null);
                }
                if (requeueToday) {
                    BuildersKt__Builders_commonKt.launch$default(scope2, null, null, new AnonymousClass2(app, null), 3, null);
                }
                if (requeueMeetings) {
                    BuildersKt__Builders_commonKt.launch$default(scope2, null, null, new AnonymousClass3(app, null), 3, null);
                }
                if (benchCleanup) {
                    long timeoutMs = getIntent().getLongExtra(EXTRA_BENCH_TIMEOUT_MS, 300000L);
                    String stringExtra10 = getIntent().getStringExtra(EXTRA_BENCH_MEETING);
                    if (stringExtra10 == null || (string = StringsKt.trim((CharSequence) stringExtra10).toString()) == null) {
                        wantMeeting = null;
                    } else {
                        String str11 = string;
                        if (StringsKt.isBlank(str11)) {
                            str11 = null;
                        }
                        wantMeeting = str11;
                    }
                    z2 = false;
                    AnonymousClass4 anonymousClass4 = new AnonymousClass4(app, wantMeeting, config, this, timeoutMs, null);
                    scope = scope2;
                    BuildersKt__Builders_commonKt.launch$default(scope, null, null, anonymousClass4, 3, null);
                } else {
                    scope = scope2;
                    z2 = false;
                }
                Intent intent9 = getIntent();
                if ((intent9 == null || !intent9.getBooleanExtra(EXTRA_DETECT_TODAY, z2)) ? z2 : true) {
                    BuildersKt__Builders_commonKt.launch$default(scope, null, null, new AnonymousClass5(app, null), 3, null);
                }
            }
        }
    }

    /* JADX INFO: renamed from: com.varun.pocketassistant.MainActivity$applyDebugPipelineExtras$1, reason: invalid class name */
    /* JADX INFO: compiled from: MainActivity.kt */
    @Metadata(d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H\n"}, d2 = {"<anonymous>", "", "Lkotlinx/coroutines/CoroutineScope;"}, k = 3, mv = {2, 2, 0}, xi = 48)
    @DebugMetadata(c = "com.varun.pocketassistant.MainActivity$applyDebugPipelineExtras$1", f = "MainActivity.kt", i = {}, l = {121}, m = "invokeSuspend", n = {}, s = {})
    static final class AnonymousClass1 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
        final /* synthetic */ PocketAssistantApp $app;
        int label;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass1(PocketAssistantApp pocketAssistantApp, Continuation<? super AnonymousClass1> continuation) {
            super(2, continuation);
            this.$app = pocketAssistantApp;
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
            return new AnonymousClass1(this.$app, continuation);
        }

        @Override // kotlin.jvm.functions.Function2
        public final Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
            return ((AnonymousClass1) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object $result) {
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    this.label = 1;
                    if (this.$app.getContainer().getSessionRepository().requeueAllForAsr(this) == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    break;
                case 1:
                    ResultKt.throwOnFailure($result);
                    break;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
            Log.i(MainActivity.TAG, "Debug: requeued ASR");
            return Unit.INSTANCE;
        }
    }

    /* JADX INFO: renamed from: com.varun.pocketassistant.MainActivity$applyDebugPipelineExtras$2, reason: invalid class name */
    /* JADX INFO: compiled from: MainActivity.kt */
    @Metadata(d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H\n"}, d2 = {"<anonymous>", "", "Lkotlinx/coroutines/CoroutineScope;"}, k = 3, mv = {2, 2, 0}, xi = 48)
    @DebugMetadata(c = "com.varun.pocketassistant.MainActivity$applyDebugPipelineExtras$2", f = "MainActivity.kt", i = {0, 0, 0}, l = {135}, m = "invokeSuspend", n = {"cal", "start", "end"}, s = {"L$0", "J$0", "J$1"})
    static final class AnonymousClass2 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
        final /* synthetic */ PocketAssistantApp $app;
        long J$0;
        long J$1;
        Object L$0;
        int label;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass2(PocketAssistantApp pocketAssistantApp, Continuation<? super AnonymousClass2> continuation) {
            super(2, continuation);
            this.$app = pocketAssistantApp;
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
            return new AnonymousClass2(this.$app, continuation);
        }

        @Override // kotlin.jvm.functions.Function2
        public final Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
            return ((AnonymousClass2) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object $result) {
            long start;
            Object obj;
            long end;
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
                    calendar.set(11, 0);
                    calendar.set(12, 0);
                    calendar.set(13, 0);
                    calendar.set(14, 0);
                    start = calendar.getTimeInMillis();
                    long end2 = start + TimeUnit.DAYS.toMillis(1L);
                    this.L$0 = SpillingKt.nullOutSpilledVariable(calendar);
                    this.J$0 = start;
                    this.J$1 = end2;
                    this.label = 1;
                    Object objRequeueAsrInRange = this.$app.getContainer().getSessionRepository().requeueAsrInRange(start, end2, this);
                    if (objRequeueAsrInRange == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    obj = objRequeueAsrInRange;
                    end = end2;
                    break;
                    break;
                case 1:
                    end = this.J$1;
                    long start2 = this.J$0;
                    ResultKt.throwOnFailure($result);
                    start = start2;
                    obj = $result;
                    break;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
            int n = ((Number) obj).intValue();
            Log.i(MainActivity.TAG, "Debug: requeued ASR for today (" + n + " segments, " + start + ".." + end + ")");
            return Unit.INSTANCE;
        }
    }

    /* JADX INFO: renamed from: com.varun.pocketassistant.MainActivity$applyDebugPipelineExtras$3, reason: invalid class name */
    /* JADX INFO: compiled from: MainActivity.kt */
    @Metadata(d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H\n"}, d2 = {"<anonymous>", "", "Lkotlinx/coroutines/CoroutineScope;"}, k = 3, mv = {2, 2, 0}, xi = 48)
    @DebugMetadata(c = "com.varun.pocketassistant.MainActivity$applyDebugPipelineExtras$3", f = "MainActivity.kt", i = {}, l = {141}, m = "invokeSuspend", n = {}, s = {})
    static final class AnonymousClass3 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
        final /* synthetic */ PocketAssistantApp $app;
        int label;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass3(PocketAssistantApp pocketAssistantApp, Continuation<? super AnonymousClass3> continuation) {
            super(2, continuation);
            this.$app = pocketAssistantApp;
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
            return new AnonymousClass3(this.$app, continuation);
        }

        @Override // kotlin.jvm.functions.Function2
        public final Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
            return ((AnonymousClass3) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object $result) {
            Object pendingCleanup;
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    this.label = 1;
                    pendingCleanup = this.$app.getContainer().getMeetingRepository().getPendingCleanup(50, this);
                    if (pendingCleanup == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    break;
                case 1:
                    ResultKt.throwOnFailure($result);
                    pendingCleanup = $result;
                    break;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
            Iterable iterable = (Iterable) pendingCleanup;
            Collection arrayList = new ArrayList(CollectionsKt.collectionSizeOrDefault(iterable, 10));
            Iterator it = iterable.iterator();
            while (it.hasNext()) {
                arrayList.add(((MeetingEntity) it.next()).getId());
            }
            List ids = (List) arrayList;
            this.$app.getContainer().getPipelineScheduler().requeuePendingMeetings(ids);
            Log.i(MainActivity.TAG, "Debug: requeued meetings");
            return Unit.INSTANCE;
        }
    }

    /* JADX INFO: renamed from: com.varun.pocketassistant.MainActivity$applyDebugPipelineExtras$4, reason: invalid class name */
    /* JADX INFO: compiled from: MainActivity.kt */
    @Metadata(d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H\n"}, d2 = {"<anonymous>", "", "Lkotlinx/coroutines/CoroutineScope;"}, k = 3, mv = {2, 2, 0}, xi = 48)
    @DebugMetadata(c = "com.varun.pocketassistant.MainActivity$applyDebugPipelineExtras$4", f = "MainActivity.kt", i = {0, 0, 0, 1, 2, 2, 3, 3, 3, 3, 3, 3, 3, 3, 3}, l = {151, 152, 157, 187}, m = "invokeSuspend", n = {"repo", "id\\1", "$i$a$-let-MainActivity$applyDebugPipelineExtras$4$meeting$1\\1\\151\\0", "repo", "repo", "meeting", "repo", "meeting", "recordings", "fmt", "combined", "settings", "benchModel", "prompt", "diarized"}, s = {"L$0", "L$1", "I$0", "L$0", "L$0", "L$1", "L$0", "L$1", "L$2", "L$3", "L$4", "L$5", "L$6", "L$7", "I$0"})
    static final class AnonymousClass4 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
        final /* synthetic */ PocketAssistantApp $app;
        final /* synthetic */ PipelineConfig $config;
        final /* synthetic */ long $timeoutMs;
        final /* synthetic */ String $wantMeeting;
        int I$0;
        Object L$0;
        Object L$1;
        Object L$2;
        Object L$3;
        Object L$4;
        Object L$5;
        Object L$6;
        Object L$7;
        int label;
        final /* synthetic */ MainActivity this$0;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass4(PocketAssistantApp pocketAssistantApp, String str, PipelineConfig pipelineConfig, MainActivity mainActivity, long j, Continuation<? super AnonymousClass4> continuation) {
            super(2, continuation);
            this.$app = pocketAssistantApp;
            this.$wantMeeting = str;
            this.$config = pipelineConfig;
            this.this$0 = mainActivity;
            this.$timeoutMs = j;
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
            return new AnonymousClass4(this.$app, this.$wantMeeting, this.$config, this.this$0, this.$timeoutMs, continuation);
        }

        @Override // kotlin.jvm.functions.Function2
        public final Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
            return ((AnonymousClass4) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
        }

        /* JADX WARN: Code duplicated, block: B:102:0x030d A[RETURN] */
        /* JADX WARN: Code duplicated, block: B:103:0x030e  */
        /* JADX WARN: Code duplicated, block: B:125:0x0117 A[SYNTHETIC] */
        /* JADX WARN: Code duplicated, block: B:127:0x016a A[SYNTHETIC] */
        /* JADX WARN: Code duplicated, block: B:128:0x016a A[SYNTHETIC] */
        /* JADX WARN: Code duplicated, block: B:131:0x0131 A[SYNTHETIC] */
        /* JADX WARN: Code duplicated, block: B:132:0x0218 A[SYNTHETIC] */
        /* JADX WARN: Code duplicated, block: B:133:0x0216 A[SYNTHETIC] */
        /* JADX WARN: Code duplicated, block: B:136:? A[LOOP:2: B:70:0x01f4->B:136:?, LOOP_END, SYNTHETIC] */
        /* JADX WARN: Code duplicated, block: B:27:0x00c6  */
        /* JADX WARN: Code duplicated, block: B:29:0x00ce  */
        /* JADX WARN: Code duplicated, block: B:31:0x00e5 A[RETURN] */
        /* JADX WARN: Code duplicated, block: B:32:0x00e6  */
        /* JADX WARN: Code duplicated, block: B:36:0x00fe  */
        /* JADX WARN: Code duplicated, block: B:38:0x0114  */
        /* JADX WARN: Code duplicated, block: B:43:0x0137  */
        /* JADX WARN: Code duplicated, block: B:45:0x0143  */
        /* JADX WARN: Code duplicated, block: B:48:0x014f  */
        /* JADX WARN: Code duplicated, block: B:50:0x0152  */
        /* JADX WARN: Code duplicated, block: B:52:0x0158  */
        /* JADX WARN: Code duplicated, block: B:56:0x0166 A[ADDED_TO_REGION, REMOVE] */
        /* JADX WARN: Code duplicated, block: B:62:0x01ba  */
        /* JADX WARN: Code duplicated, block: B:64:0x01dd  */
        /* JADX WARN: Code duplicated, block: B:66:0x01e5  */
        /* JADX WARN: Code duplicated, block: B:69:0x01f0  */
        /* JADX WARN: Code duplicated, block: B:72:0x01fa  */
        /* JADX WARN: Code duplicated, block: B:74:0x020a  */
        /* JADX WARN: Code duplicated, block: B:78:0x0213  */
        /* JADX WARN: Code duplicated, block: B:84:0x022e  */
        /* JADX WARN: Code duplicated, block: B:92:0x024c  */
        /* JADX WARN: Code duplicated, block: B:95:0x0256  */
        /* JADX WARN: Code duplicated, block: B:96:0x0258  */
        /* JADX WARN: Code restructure failed: missing block: B:20:0x00a4, code lost:
        
            if (r10 == null) goto L21;
         */
        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public final java.lang.Object invokeSuspend(java.lang.Object r29) {
            /*
                Method dump skipped, instruction units count: 914
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: com.varun.pocketassistant.MainActivity.AnonymousClass4.invokeSuspend(java.lang.Object):java.lang.Object");
        }
    }

    /* JADX INFO: renamed from: com.varun.pocketassistant.MainActivity$applyDebugPipelineExtras$5, reason: invalid class name */
    /* JADX INFO: compiled from: MainActivity.kt */
    @Metadata(d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H\n"}, d2 = {"<anonymous>", "", "Lkotlinx/coroutines/CoroutineScope;"}, k = 3, mv = {2, 2, 0}, xi = 48)
    @DebugMetadata(c = "com.varun.pocketassistant.MainActivity$applyDebugPipelineExtras$5", f = "MainActivity.kt", i = {0, 0, 0, 0}, l = {225}, m = "invokeSuspend", n = {"cal", "refine", "start", "end"}, s = {"L$0", "L$1", "J$0", "J$1"})
    static final class AnonymousClass5 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
        final /* synthetic */ PocketAssistantApp $app;
        long J$0;
        long J$1;
        Object L$0;
        Object L$1;
        int label;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass5(PocketAssistantApp pocketAssistantApp, Continuation<? super AnonymousClass5> continuation) {
            super(2, continuation);
            this.$app = pocketAssistantApp;
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
            return new AnonymousClass5(this.$app, continuation);
        }

        @Override // kotlin.jvm.functions.Function2
        public final Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
            return ((AnonymousClass5) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object $result) {
            Function2 refine;
            Object obj;
            long end;
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
                    calendar.set(11, 0);
                    calendar.set(12, 0);
                    calendar.set(13, 0);
                    calendar.set(14, 0);
                    long start = calendar.getTimeInMillis();
                    long end2 = start + TimeUnit.DAYS.toMillis(1L);
                    if (this.$app.getContainer().getPipelineConfig().cloudConfigured()) {
                        refine = new MainActivity$applyDebugPipelineExtras$5$refine$1(this.$app, null);
                    } else {
                        refine = null;
                    }
                    this.L$0 = SpillingKt.nullOutSpilledVariable(calendar);
                    this.L$1 = SpillingKt.nullOutSpilledVariable(refine);
                    this.J$0 = start;
                    this.J$1 = end2;
                    this.label = 1;
                    Object objDetectMeetingProposals = this.$app.getContainer().getMeetingRepository().detectMeetingProposals(start, end2, refine, this);
                    if (objDetectMeetingProposals == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    obj = objDetectMeetingProposals;
                    end = end2;
                    break;
                    break;
                case 1:
                    end = this.J$1;
                    long j = this.J$0;
                    ResultKt.throwOnFailure($result);
                    obj = $result;
                    break;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
            List<GapClusterer.Proposal> proposals = (List) obj;
            SimpleDateFormat fmt = new SimpleDateFormat("HH:mm", Locale.US);
            Log.i(MainActivity.TAG, "Debug: detected " + proposals.size() + " meeting(s) for today");
            for (GapClusterer.Proposal proposal : proposals) {
                long end3 = end;
                Log.i(MainActivity.TAG, "  meeting " + fmt.format(new Date(proposal.getStartMs())) + "-" + fmt.format(new Date(proposal.getEndMs())) + " (" + proposal.getSegmentIds().size() + " clips)");
                end = end3;
                proposals = proposals;
                fmt = fmt;
            }
            return Unit.INSTANCE;
        }
    }
}
