package com.varun.pocketassistant.pipeline;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;
import kotlin.Lazy;
import kotlin.LazyKt;
import kotlin.Metadata;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;

/* JADX INFO: compiled from: PipelineConfig.kt */
/* JADX INFO: loaded from: classes8.dex */
@Metadata(d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\b\u0007\u0018\u0000 \u00152\u00020\u0001:\u0001\u0015B\u000f\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0004\b\u0004\u0010\u0005J\u0006\u0010\u000e\u001a\u00020\u000fJ\u000e\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u000fJ\u0006\u0010\u0013\u001a\u00020\u0014R\u0016\u0010\u0006\u001a\n \u0007*\u0004\u0018\u00010\u00030\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R#\u0010\b\u001a\n \u0007*\u0004\u0018\u00010\t0\t8BX\u0082\u0084\u0002¢\u0006\f\n\u0004\b\f\u0010\r\u001a\u0004\b\n\u0010\u000b¨\u0006\u0016"}, d2 = {"Lcom/varun/pocketassistant/pipeline/PipelineConfig;", "", "context", "Landroid/content/Context;", "<init>", "(Landroid/content/Context;)V", "appContext", "kotlin.jvm.PlatformType", "prefs", "Landroid/content/SharedPreferences;", "getPrefs", "()Landroid/content/SharedPreferences;", "prefs$delegate", "Lkotlin/Lazy;", "load", "Lcom/varun/pocketassistant/pipeline/PipelineSettings;", "save", "", "settings", "cloudConfigured", "", "Companion", "app_debug"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class PipelineConfig {
    private static final String KEY_ACTIONS_MODE = "actions_mode";
    private static final String KEY_ALLOW_FALLBACK = "allow_fallback";
    private static final String KEY_API_KEY = "cloud_api_key";
    private static final String KEY_ASR_DIARIZE = "asr_diarize";
    private static final String KEY_ASR_MODE = "asr_mode";
    private static final String KEY_ASR_MODEL = "cloud_asr_model";
    private static final String KEY_BASE_URL = "cloud_base_url";
    private static final String KEY_CHAT_MODEL = "cloud_chat_model";
    private static final String KEY_CLEANUP_MODE = "cleanup_mode";
    private static final String KEY_CLEANUP_MODEL = "cloud_cleanup_model";
    private static final String KEY_CLEANUP_PROMPT = "cleanup_prompt";
    private static final String KEY_EXCLUDE_REASONING = "exclude_reasoning";
    private static final String KEY_GLOSSARY = "glossary";
    private static final String KEY_LOCAL_ASR = "local_asr_model";
    private static final String KEY_LOCAL_CLEANUP = "local_cleanup_model";
    private static final String KEY_REASONING_EFFORT = "reasoning_effort";
    private static final String KEY_STT_LANGUAGE = "stt_language";
    private static final String KEY_SUMMARY_MODE = "summary_mode";
    private static final String KEY_SUMMARY_MODEL = "cloud_summary_model";
    private static final String KEY_SUMMARY_PROMPT = "summary_prompt";
    private final Context appContext;

    /* JADX INFO: renamed from: prefs$delegate, reason: from kotlin metadata */
    private final Lazy prefs;

    /* JADX INFO: renamed from: Companion, reason: from kotlin metadata */
    public static final Companion INSTANCE = new Companion(null);
    public static final int $stable = 8;

    public PipelineConfig(Context context) {
        Intrinsics.checkNotNullParameter(context, "context");
        this.appContext = context.getApplicationContext();
        this.prefs = LazyKt.lazy(new Function0() { // from class: com.varun.pocketassistant.pipeline.PipelineConfig$$ExternalSyntheticLambda0
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return PipelineConfig.prefs_delegate$lambda$0(this.f$0);
            }
        });
    }

    private final SharedPreferences getPrefs() {
        return (SharedPreferences) this.prefs.getValue();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final SharedPreferences prefs_delegate$lambda$0(PipelineConfig this$0) {
        try {
            MasterKey masterKey = new MasterKey.Builder(this$0.appContext).setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build();
            Intrinsics.checkNotNullExpressionValue(masterKey, "build(...)");
            return EncryptedSharedPreferences.create(this$0.appContext, "pipeline_secure", masterKey, EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV, EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM);
        } catch (Throwable th) {
            return this$0.appContext.getSharedPreferences("pipeline_secure_fallback", 0);
        }
    }

    public final PipelineSettings load() {
        ProviderMode mode = INSTANCE.parseMode(getPrefs().getString(KEY_ASR_MODE, null));
        ProviderMode mode2 = INSTANCE.parseMode(getPrefs().getString(KEY_CLEANUP_MODE, null));
        Companion companion = INSTANCE;
        String string = getPrefs().getString(KEY_SUMMARY_MODE, null);
        if (string == null) {
            string = getPrefs().getString(KEY_CLEANUP_MODE, null);
        }
        ProviderMode mode3 = companion.parseMode(string);
        Companion companion2 = INSTANCE;
        String string2 = getPrefs().getString(KEY_ACTIONS_MODE, null);
        if (string2 == null && (string2 = getPrefs().getString(KEY_SUMMARY_MODE, null)) == null) {
            string2 = getPrefs().getString(KEY_CLEANUP_MODE, null);
        }
        ProviderMode mode4 = companion2.parseMode(string2);
        boolean z = getPrefs().getBoolean(KEY_ALLOW_FALLBACK, false);
        String string3 = getPrefs().getString(KEY_API_KEY, null);
        if (string3 == null) {
            string3 = "";
        }
        OpenRouterModels openRouterModels = OpenRouterModels.INSTANCE;
        String string4 = getPrefs().getString(KEY_ASR_MODEL, null);
        if (string4 == null) {
            string4 = PipelineSettings.DEFAULT_ASR_MODEL;
        }
        String strNormalizeId = openRouterModels.normalizeId(string4);
        OpenRouterModels openRouterModels2 = OpenRouterModels.INSTANCE;
        String string5 = getPrefs().getString(KEY_CLEANUP_MODEL, null);
        if (string5 == null && (string5 = getPrefs().getString(KEY_CHAT_MODEL, null)) == null) {
            string5 = "google/gemini-2.5-flash";
        }
        String strNormalizeId2 = openRouterModels2.normalizeId(string5);
        OpenRouterModels openRouterModels3 = OpenRouterModels.INSTANCE;
        String string6 = getPrefs().getString(KEY_SUMMARY_MODEL, null);
        if (string6 == null && (string6 = getPrefs().getString(KEY_CHAT_MODEL, null)) == null) {
            string6 = PipelineSettings.DEFAULT_SUMMARY_MODEL;
        }
        String strNormalizeId3 = openRouterModels3.normalizeId(string6);
        String string7 = getPrefs().getString(KEY_REASONING_EFFORT, null);
        if (string7 == null) {
            string7 = "";
        }
        boolean z2 = getPrefs().getBoolean(KEY_EXCLUDE_REASONING, true);
        String string8 = getPrefs().getString(KEY_STT_LANGUAGE, null);
        if (string8 == null) {
            string8 = "";
        }
        boolean z3 = getPrefs().getBoolean(KEY_ASR_DIARIZE, true);
        String string9 = getPrefs().getString(KEY_LOCAL_ASR, null);
        if (string9 == null) {
            string9 = "parakeet_tdt";
        }
        String str = string9;
        String string10 = getPrefs().getString(KEY_LOCAL_CLEANUP, null);
        if (string10 == null) {
            string10 = "gemma-4b";
        }
        String str2 = string10;
        String string11 = getPrefs().getString(KEY_CLEANUP_PROMPT, null);
        if (string11 == null) {
            string11 = "";
        }
        String str3 = string11;
        String string12 = getPrefs().getString(KEY_SUMMARY_PROMPT, null);
        if (string12 == null) {
            string12 = "";
        }
        String str4 = string12;
        String string13 = getPrefs().getString(KEY_GLOSSARY, null);
        return new PipelineSettings(mode, mode2, mode3, mode4, z, PipelineSettings.DEFAULT_BASE_URL, string3, strNormalizeId, strNormalizeId2, strNormalizeId3, string7, z2, string8, z3, str, str2, str3, str4, string13 != null ? string13 : "");
    }

    public final void save(PipelineSettings settings) {
        Intrinsics.checkNotNullParameter(settings, "settings");
        SharedPreferences prefs = getPrefs();
        Intrinsics.checkNotNullExpressionValue(prefs, "<get-prefs>(...)");
        SharedPreferences.Editor editorEdit = prefs.edit();
        editorEdit.putString(KEY_ASR_MODE, settings.getAsrMode().name());
        editorEdit.putString(KEY_CLEANUP_MODE, settings.getCleanupMode().name());
        editorEdit.putString(KEY_SUMMARY_MODE, settings.getSummaryMode().name());
        editorEdit.putString(KEY_ACTIONS_MODE, settings.getActionsMode().name());
        editorEdit.putBoolean(KEY_ALLOW_FALLBACK, settings.getAllowFallback());
        editorEdit.putString(KEY_BASE_URL, PipelineSettings.DEFAULT_BASE_URL);
        editorEdit.putString(KEY_API_KEY, StringsKt.trim((CharSequence) settings.getCloudApiKey()).toString());
        editorEdit.putString(KEY_ASR_MODEL, OpenRouterModels.INSTANCE.normalizeId(settings.getCloudAsrModel()));
        editorEdit.putString(KEY_CLEANUP_MODEL, OpenRouterModels.INSTANCE.normalizeId(settings.getCloudCleanupModel()));
        editorEdit.putString(KEY_SUMMARY_MODEL, OpenRouterModels.INSTANCE.normalizeId(settings.getCloudSummaryModel()));
        editorEdit.putString(KEY_CHAT_MODEL, OpenRouterModels.INSTANCE.normalizeId(settings.getCloudCleanupModel()));
        editorEdit.putString(KEY_REASONING_EFFORT, StringsKt.trim((CharSequence) settings.getReasoningEffort()).toString());
        editorEdit.putBoolean(KEY_EXCLUDE_REASONING, settings.getExcludeReasoningFromResponse());
        editorEdit.putString(KEY_STT_LANGUAGE, StringsKt.trim((CharSequence) settings.getSttLanguage()).toString());
        editorEdit.putBoolean(KEY_ASR_DIARIZE, settings.getAsrDiarizeEnabled());
        editorEdit.putString(KEY_LOCAL_ASR, StringsKt.trim((CharSequence) settings.getLocalAsrModelId()).toString());
        editorEdit.putString(KEY_LOCAL_CLEANUP, StringsKt.trim((CharSequence) settings.getLocalCleanupModelId()).toString());
        editorEdit.putString(KEY_CLEANUP_PROMPT, settings.getCleanupPrompt());
        editorEdit.putString(KEY_SUMMARY_PROMPT, settings.getSummaryPrompt());
        editorEdit.putString(KEY_GLOSSARY, settings.getGlossary());
        editorEdit.apply();
    }

    public final boolean cloudConfigured() {
        PipelineSettings s = load();
        return (StringsKt.isBlank(s.getCloudApiKey()) || StringsKt.isBlank(s.getCloudBaseUrl())) ? false : true;
    }

    /* JADX INFO: compiled from: PipelineConfig.kt */
    @Metadata(d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0014\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003J\u0010\u0010\u0019\u001a\u00020\u001a2\b\u0010\u001b\u001a\u0004\u0018\u00010\u0005J\u0010\u0010\u001c\u001a\u00020\u001d2\b\u0010\u001b\u001a\u0004\u0018\u00010\u0005R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0005X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0005X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0005X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0005X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0005X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\u0005X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u0005X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u0005X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u0005X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0005X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0005X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0005X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u0005X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u0005X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\u0005X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\u0005X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0016\u001a\u00020\u0005X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0017\u001a\u00020\u0005X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0018\u001a\u00020\u0005X\u0082T¢\u0006\u0002\n\u0000¨\u0006\u001e"}, d2 = {"Lcom/varun/pocketassistant/pipeline/PipelineConfig$Companion;", "", "<init>", "()V", "KEY_ASR_MODE", "", "KEY_CLEANUP_MODE", "KEY_SUMMARY_MODE", "KEY_ACTIONS_MODE", "KEY_ALLOW_FALLBACK", "KEY_BASE_URL", "KEY_API_KEY", "KEY_ASR_MODEL", "KEY_CLEANUP_MODEL", "KEY_SUMMARY_MODEL", "KEY_CHAT_MODEL", "KEY_REASONING_EFFORT", "KEY_EXCLUDE_REASONING", "KEY_STT_LANGUAGE", "KEY_ASR_DIARIZE", "KEY_LOCAL_ASR", "KEY_LOCAL_CLEANUP", "KEY_CLEANUP_PROMPT", "KEY_SUMMARY_PROMPT", "KEY_GLOSSARY", "parseMode", "Lcom/varun/pocketassistant/pipeline/ProviderMode;", "raw", "legacyImpliesNoFallback", "", "app_debug"}, k = 1, mv = {2, 2, 0}, xi = 48)
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
        /* JADX WARN: Code restructure failed: missing block: B:10:0x0019, code lost:
        
            if (r4.equals("ONLY_CLOUD") == false) goto L33;
         */
        /* JADX WARN: Code restructure failed: missing block: B:13:0x0022, code lost:
        
            if (r4.equals("PREFER_LOCAL") == false) goto L33;
         */
        /* JADX WARN: Code restructure failed: missing block: B:17:0x002e, code lost:
        
            if (r4.equals("PREFER_CLOUD") == false) goto L33;
         */
        /* JADX WARN: Code restructure failed: missing block: B:21:0x003a, code lost:
        
            if (r4.equals("") == false) goto L33;
         */
        /* JADX WARN: Code restructure failed: missing block: B:35:?, code lost:
        
            return com.varun.pocketassistant.pipeline.ProviderMode.PREFER_LOCAL;
         */
        /* JADX WARN: Code restructure failed: missing block: B:36:?, code lost:
        
            return com.varun.pocketassistant.pipeline.ProviderMode.PREFER_CLOUD;
         */
        /* JADX WARN: Code restructure failed: missing block: B:7:0x0010, code lost:
        
            if (r4.equals("ONLY_LOCAL") == false) goto L33;
         */
        /* JADX WARN: Failed to restore switch over string. Please report as a decompilation issue */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public final com.varun.pocketassistant.pipeline.ProviderMode parseMode(java.lang.String r4) {
            /*
                r3 = this;
                if (r4 == 0) goto L63
                int r0 = r4.hashCode()
                switch(r0) {
                    case 0: goto L34;
                    case 312943398: goto L28;
                    case 321332316: goto L1c;
                    case 1884811202: goto L13;
                    case 1893200120: goto La;
                    default: goto L9;
                }
            L9:
                goto L3d
            La:
                java.lang.String r0 = "ONLY_LOCAL"
                boolean r0 = r4.equals(r0)
                if (r0 != 0) goto L25
                goto L9
            L13:
                java.lang.String r0 = "ONLY_CLOUD"
                boolean r0 = r4.equals(r0)
                if (r0 != 0) goto L31
                goto L9
            L1c:
                java.lang.String r0 = "PREFER_LOCAL"
                boolean r0 = r4.equals(r0)
                if (r0 != 0) goto L25
                goto L9
            L25:
                com.varun.pocketassistant.pipeline.ProviderMode r0 = com.varun.pocketassistant.pipeline.ProviderMode.PREFER_LOCAL
                goto L65
            L28:
                java.lang.String r0 = "PREFER_CLOUD"
                boolean r0 = r4.equals(r0)
                if (r0 != 0) goto L31
                goto L9
            L31:
                com.varun.pocketassistant.pipeline.ProviderMode r0 = com.varun.pocketassistant.pipeline.ProviderMode.PREFER_CLOUD
                goto L65
            L34:
                java.lang.String r0 = ""
                boolean r0 = r4.equals(r0)
                if (r0 != 0) goto L63
                goto L9
            L3d:
                kotlin.Result$Companion r0 = kotlin.Result.INSTANCE     // Catch: java.lang.Throwable -> L4c
                r0 = r3
                com.varun.pocketassistant.pipeline.PipelineConfig$Companion r0 = (com.varun.pocketassistant.pipeline.PipelineConfig.Companion) r0     // Catch: java.lang.Throwable -> L4c
                r1 = 0
                com.varun.pocketassistant.pipeline.ProviderMode r2 = com.varun.pocketassistant.pipeline.ProviderMode.valueOf(r4)     // Catch: java.lang.Throwable -> L4c
                java.lang.Object r0 = kotlin.Result.m8304constructorimpl(r2)     // Catch: java.lang.Throwable -> L4c
                goto L57
            L4c:
                r0 = move-exception
                kotlin.Result$Companion r1 = kotlin.Result.INSTANCE
                java.lang.Object r0 = kotlin.ResultKt.createFailure(r0)
                java.lang.Object r0 = kotlin.Result.m8304constructorimpl(r0)
            L57:
                com.varun.pocketassistant.pipeline.ProviderMode r1 = com.varun.pocketassistant.pipeline.ProviderMode.PREFER_CLOUD
                boolean r2 = kotlin.Result.m8310isFailureimpl(r0)
                if (r2 == 0) goto L60
                r0 = r1
            L60:
                com.varun.pocketassistant.pipeline.ProviderMode r0 = (com.varun.pocketassistant.pipeline.ProviderMode) r0
                goto L65
            L63:
                com.varun.pocketassistant.pipeline.ProviderMode r0 = com.varun.pocketassistant.pipeline.ProviderMode.PREFER_CLOUD
            L65:
                return r0
            */
            throw new UnsupportedOperationException("Method not decompiled: com.varun.pocketassistant.pipeline.PipelineConfig.Companion.parseMode(java.lang.String):com.varun.pocketassistant.pipeline.ProviderMode");
        }

        public final boolean legacyImpliesNoFallback(String raw) {
            return Intrinsics.areEqual(raw, "ONLY_CLOUD") || Intrinsics.areEqual(raw, "ONLY_LOCAL");
        }
    }
}
