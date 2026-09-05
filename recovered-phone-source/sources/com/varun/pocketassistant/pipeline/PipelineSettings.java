package com.varun.pocketassistant.pipeline;

import java.util.Collection;
import java.util.List;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;

/* JADX INFO: compiled from: PipelineConfig.kt */
/* JADX INFO: loaded from: classes8.dex */
@Metadata(d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u000e\n\u0002\b=\n\u0002\u0010\b\n\u0002\b\u0003\b\u0087\b\u0018\u0000 J2\u00020\u0001:\u0001JBÅ\u0001\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0005\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0007\u001a\u00020\b\u0012\b\b\u0002\u0010\t\u001a\u00020\n\u0012\b\b\u0002\u0010\u000b\u001a\u00020\n\u0012\b\b\u0002\u0010\f\u001a\u00020\n\u0012\b\b\u0002\u0010\r\u001a\u00020\n\u0012\b\b\u0002\u0010\u000e\u001a\u00020\n\u0012\b\b\u0002\u0010\u000f\u001a\u00020\n\u0012\b\b\u0002\u0010\u0010\u001a\u00020\b\u0012\b\b\u0002\u0010\u0011\u001a\u00020\n\u0012\b\b\u0002\u0010\u0012\u001a\u00020\b\u0012\b\b\u0002\u0010\u0013\u001a\u00020\n\u0012\b\b\u0002\u0010\u0014\u001a\u00020\n\u0012\b\b\u0002\u0010\u0015\u001a\u00020\n\u0012\b\b\u0002\u0010\u0016\u001a\u00020\n\u0012\b\b\u0002\u0010\u0017\u001a\u00020\n¢\u0006\u0004\b\u0018\u0010\u0019J\u0006\u00100\u001a\u00020\bJ\t\u00101\u001a\u00020\u0003HÆ\u0003J\t\u00102\u001a\u00020\u0003HÆ\u0003J\t\u00103\u001a\u00020\u0003HÆ\u0003J\t\u00104\u001a\u00020\u0003HÆ\u0003J\t\u00105\u001a\u00020\bHÆ\u0003J\t\u00106\u001a\u00020\nHÆ\u0003J\t\u00107\u001a\u00020\nHÆ\u0003J\t\u00108\u001a\u00020\nHÆ\u0003J\t\u00109\u001a\u00020\nHÆ\u0003J\t\u0010:\u001a\u00020\nHÆ\u0003J\t\u0010;\u001a\u00020\nHÆ\u0003J\t\u0010<\u001a\u00020\bHÆ\u0003J\t\u0010=\u001a\u00020\nHÆ\u0003J\t\u0010>\u001a\u00020\bHÆ\u0003J\t\u0010?\u001a\u00020\nHÆ\u0003J\t\u0010@\u001a\u00020\nHÆ\u0003J\t\u0010A\u001a\u00020\nHÆ\u0003J\t\u0010B\u001a\u00020\nHÆ\u0003J\t\u0010C\u001a\u00020\nHÆ\u0003JÇ\u0001\u0010D\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00032\b\b\u0002\u0010\u0006\u001a\u00020\u00032\b\b\u0002\u0010\u0007\u001a\u00020\b2\b\b\u0002\u0010\t\u001a\u00020\n2\b\b\u0002\u0010\u000b\u001a\u00020\n2\b\b\u0002\u0010\f\u001a\u00020\n2\b\b\u0002\u0010\r\u001a\u00020\n2\b\b\u0002\u0010\u000e\u001a\u00020\n2\b\b\u0002\u0010\u000f\u001a\u00020\n2\b\b\u0002\u0010\u0010\u001a\u00020\b2\b\b\u0002\u0010\u0011\u001a\u00020\n2\b\b\u0002\u0010\u0012\u001a\u00020\b2\b\b\u0002\u0010\u0013\u001a\u00020\n2\b\b\u0002\u0010\u0014\u001a\u00020\n2\b\b\u0002\u0010\u0015\u001a\u00020\n2\b\b\u0002\u0010\u0016\u001a\u00020\n2\b\b\u0002\u0010\u0017\u001a\u00020\nHÆ\u0001J\u0013\u0010E\u001a\u00020\b2\b\u0010F\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010G\u001a\u00020HHÖ\u0001J\t\u0010I\u001a\u00020\nHÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u001a\u0010\u001bR\u0011\u0010\u0004\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u001c\u0010\u001bR\u0011\u0010\u0005\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u001d\u0010\u001bR\u0011\u0010\u0006\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u001e\u0010\u001bR\u0011\u0010\u0007\u001a\u00020\b¢\u0006\b\n\u0000\u001a\u0004\b\u001f\u0010 R\u0011\u0010\t\u001a\u00020\n¢\u0006\b\n\u0000\u001a\u0004\b!\u0010\"R\u0011\u0010\u000b\u001a\u00020\n¢\u0006\b\n\u0000\u001a\u0004\b#\u0010\"R\u0011\u0010\f\u001a\u00020\n¢\u0006\b\n\u0000\u001a\u0004\b$\u0010\"R\u0011\u0010\r\u001a\u00020\n¢\u0006\b\n\u0000\u001a\u0004\b%\u0010\"R\u0011\u0010\u000e\u001a\u00020\n¢\u0006\b\n\u0000\u001a\u0004\b&\u0010\"R\u0011\u0010\u000f\u001a\u00020\n¢\u0006\b\n\u0000\u001a\u0004\b'\u0010\"R\u0011\u0010\u0010\u001a\u00020\b¢\u0006\b\n\u0000\u001a\u0004\b(\u0010 R\u0011\u0010\u0011\u001a\u00020\n¢\u0006\b\n\u0000\u001a\u0004\b)\u0010\"R\u0011\u0010\u0012\u001a\u00020\b¢\u0006\b\n\u0000\u001a\u0004\b*\u0010 R\u0011\u0010\u0013\u001a\u00020\n¢\u0006\b\n\u0000\u001a\u0004\b+\u0010\"R\u0011\u0010\u0014\u001a\u00020\n¢\u0006\b\n\u0000\u001a\u0004\b,\u0010\"R\u0011\u0010\u0015\u001a\u00020\n¢\u0006\b\n\u0000\u001a\u0004\b-\u0010\"R\u0011\u0010\u0016\u001a\u00020\n¢\u0006\b\n\u0000\u001a\u0004\b.\u0010\"R\u0011\u0010\u0017\u001a\u00020\n¢\u0006\b\n\u0000\u001a\u0004\b/\u0010\"¨\u0006K"}, d2 = {"Lcom/varun/pocketassistant/pipeline/PipelineSettings;", "", "asrMode", "Lcom/varun/pocketassistant/pipeline/ProviderMode;", "cleanupMode", "summaryMode", "actionsMode", "allowFallback", "", "cloudBaseUrl", "", "cloudApiKey", "cloudAsrModel", "cloudCleanupModel", "cloudSummaryModel", "reasoningEffort", "excludeReasoningFromResponse", "sttLanguage", "asrDiarizeEnabled", "localAsrModelId", "localCleanupModelId", "cleanupPrompt", "summaryPrompt", "glossary", "<init>", "(Lcom/varun/pocketassistant/pipeline/ProviderMode;Lcom/varun/pocketassistant/pipeline/ProviderMode;Lcom/varun/pocketassistant/pipeline/ProviderMode;Lcom/varun/pocketassistant/pipeline/ProviderMode;ZLjava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;ZLjava/lang/String;ZLjava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V", "getAsrMode", "()Lcom/varun/pocketassistant/pipeline/ProviderMode;", "getCleanupMode", "getSummaryMode", "getActionsMode", "getAllowFallback", "()Z", "getCloudBaseUrl", "()Ljava/lang/String;", "getCloudApiKey", "getCloudAsrModel", "getCloudCleanupModel", "getCloudSummaryModel", "getReasoningEffort", "getExcludeReasoningFromResponse", "getSttLanguage", "getAsrDiarizeEnabled", "getLocalAsrModelId", "getLocalCleanupModelId", "getCleanupPrompt", "getSummaryPrompt", "getGlossary", "isOpenRouter", "component1", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "component10", "component11", "component12", "component13", "component14", "component15", "component16", "component17", "component18", "component19", "copy", "equals", "other", "hashCode", "", "toString", "Companion", "app_debug"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final /* data */ class PipelineSettings {
    public static final int $stable = 0;
    public static final String DEFAULT_BASE_URL = "https://openrouter.ai/api/v1";
    public static final String DEFAULT_CHAT_MODEL = "google/gemini-2.5-flash";
    public static final String DEFAULT_CLEANUP_MODEL = "google/gemini-2.5-flash";
    private final ProviderMode actionsMode;
    private final boolean allowFallback;
    private final boolean asrDiarizeEnabled;
    private final ProviderMode asrMode;
    private final ProviderMode cleanupMode;
    private final String cleanupPrompt;
    private final String cloudApiKey;
    private final String cloudAsrModel;
    private final String cloudBaseUrl;
    private final String cloudCleanupModel;
    private final String cloudSummaryModel;
    private final boolean excludeReasoningFromResponse;
    private final String glossary;
    private final String localAsrModelId;
    private final String localCleanupModelId;
    private final String reasoningEffort;
    private final String sttLanguage;
    private final ProviderMode summaryMode;
    private final String summaryPrompt;

    /* JADX INFO: renamed from: Companion, reason: from kotlin metadata */
    public static final Companion INSTANCE = new Companion(null);
    public static final String DEFAULT_ASR_MODEL = "x-ai/grok-stt-1.0";
    private static final List<String> SUGGESTED_ASR_MODELS = CollectionsKt.listOf((Object[]) new String[]{DEFAULT_ASR_MODEL, "openai/whisper-1", "openai/whisper-large-v3"});
    private static final List<String> SUGGESTED_CLEANUP_MODELS = CollectionsKt.listOf((Object[]) new String[]{"google/gemini-2.5-flash", "openai/gpt-4o-mini", "google/gemini-2.5-flash-lite"});
    public static final String DEFAULT_SUMMARY_MODEL = "anthropic/claude-sonnet-4";
    private static final List<String> SUGGESTED_SUMMARY_MODELS = CollectionsKt.listOf((Object[]) new String[]{DEFAULT_SUMMARY_MODEL, "openai/gpt-4o", "deepseek/deepseek-r1", "google/gemini-2.5-pro"});
    private static final List<String> SUGGESTED_CHAT_MODELS = CollectionsKt.plus((Collection) SUGGESTED_CLEANUP_MODELS, (Iterable) SUGGESTED_SUMMARY_MODELS);

    public PipelineSettings() {
        this(null, null, null, null, false, null, null, null, null, null, null, false, null, false, null, null, null, null, null, 524287, null);
    }

    public static /* synthetic */ PipelineSettings copy$default(PipelineSettings pipelineSettings, ProviderMode providerMode, ProviderMode providerMode2, ProviderMode providerMode3, ProviderMode providerMode4, boolean z, String str, String str2, String str3, String str4, String str5, String str6, boolean z2, String str7, boolean z3, String str8, String str9, String str10, String str11, String str12, int i, Object obj) {
        String str13;
        String str14;
        ProviderMode providerMode5 = (i & 1) != 0 ? pipelineSettings.asrMode : providerMode;
        ProviderMode providerMode6 = (i & 2) != 0 ? pipelineSettings.cleanupMode : providerMode2;
        ProviderMode providerMode7 = (i & 4) != 0 ? pipelineSettings.summaryMode : providerMode3;
        ProviderMode providerMode8 = (i & 8) != 0 ? pipelineSettings.actionsMode : providerMode4;
        boolean z4 = (i & 16) != 0 ? pipelineSettings.allowFallback : z;
        String str15 = (i & 32) != 0 ? pipelineSettings.cloudBaseUrl : str;
        String str16 = (i & 64) != 0 ? pipelineSettings.cloudApiKey : str2;
        String str17 = (i & 128) != 0 ? pipelineSettings.cloudAsrModel : str3;
        String str18 = (i & 256) != 0 ? pipelineSettings.cloudCleanupModel : str4;
        String str19 = (i & 512) != 0 ? pipelineSettings.cloudSummaryModel : str5;
        String str20 = (i & 1024) != 0 ? pipelineSettings.reasoningEffort : str6;
        boolean z5 = (i & 2048) != 0 ? pipelineSettings.excludeReasoningFromResponse : z2;
        String str21 = (i & 4096) != 0 ? pipelineSettings.sttLanguage : str7;
        boolean z6 = (i & 8192) != 0 ? pipelineSettings.asrDiarizeEnabled : z3;
        ProviderMode providerMode9 = providerMode5;
        String str22 = (i & 16384) != 0 ? pipelineSettings.localAsrModelId : str8;
        String str23 = (i & 32768) != 0 ? pipelineSettings.localCleanupModelId : str9;
        String str24 = (i & 65536) != 0 ? pipelineSettings.cleanupPrompt : str10;
        String str25 = (i & 131072) != 0 ? pipelineSettings.summaryPrompt : str11;
        if ((i & 262144) != 0) {
            str14 = str25;
            str13 = pipelineSettings.glossary;
        } else {
            str13 = str12;
            str14 = str25;
        }
        return pipelineSettings.copy(providerMode9, providerMode6, providerMode7, providerMode8, z4, str15, str16, str17, str18, str19, str20, z5, str21, z6, str22, str23, str24, str14, str13);
    }

    /* JADX INFO: renamed from: component1, reason: from getter */
    public final ProviderMode getAsrMode() {
        return this.asrMode;
    }

    /* JADX INFO: renamed from: component10, reason: from getter */
    public final String getCloudSummaryModel() {
        return this.cloudSummaryModel;
    }

    /* JADX INFO: renamed from: component11, reason: from getter */
    public final String getReasoningEffort() {
        return this.reasoningEffort;
    }

    /* JADX INFO: renamed from: component12, reason: from getter */
    public final boolean getExcludeReasoningFromResponse() {
        return this.excludeReasoningFromResponse;
    }

    /* JADX INFO: renamed from: component13, reason: from getter */
    public final String getSttLanguage() {
        return this.sttLanguage;
    }

    /* JADX INFO: renamed from: component14, reason: from getter */
    public final boolean getAsrDiarizeEnabled() {
        return this.asrDiarizeEnabled;
    }

    /* JADX INFO: renamed from: component15, reason: from getter */
    public final String getLocalAsrModelId() {
        return this.localAsrModelId;
    }

    /* JADX INFO: renamed from: component16, reason: from getter */
    public final String getLocalCleanupModelId() {
        return this.localCleanupModelId;
    }

    /* JADX INFO: renamed from: component17, reason: from getter */
    public final String getCleanupPrompt() {
        return this.cleanupPrompt;
    }

    /* JADX INFO: renamed from: component18, reason: from getter */
    public final String getSummaryPrompt() {
        return this.summaryPrompt;
    }

    /* JADX INFO: renamed from: component19, reason: from getter */
    public final String getGlossary() {
        return this.glossary;
    }

    /* JADX INFO: renamed from: component2, reason: from getter */
    public final ProviderMode getCleanupMode() {
        return this.cleanupMode;
    }

    /* JADX INFO: renamed from: component3, reason: from getter */
    public final ProviderMode getSummaryMode() {
        return this.summaryMode;
    }

    /* JADX INFO: renamed from: component4, reason: from getter */
    public final ProviderMode getActionsMode() {
        return this.actionsMode;
    }

    /* JADX INFO: renamed from: component5, reason: from getter */
    public final boolean getAllowFallback() {
        return this.allowFallback;
    }

    /* JADX INFO: renamed from: component6, reason: from getter */
    public final String getCloudBaseUrl() {
        return this.cloudBaseUrl;
    }

    /* JADX INFO: renamed from: component7, reason: from getter */
    public final String getCloudApiKey() {
        return this.cloudApiKey;
    }

    /* JADX INFO: renamed from: component8, reason: from getter */
    public final String getCloudAsrModel() {
        return this.cloudAsrModel;
    }

    /* JADX INFO: renamed from: component9, reason: from getter */
    public final String getCloudCleanupModel() {
        return this.cloudCleanupModel;
    }

    public final PipelineSettings copy(ProviderMode asrMode, ProviderMode cleanupMode, ProviderMode summaryMode, ProviderMode actionsMode, boolean allowFallback, String cloudBaseUrl, String cloudApiKey, String cloudAsrModel, String cloudCleanupModel, String cloudSummaryModel, String reasoningEffort, boolean excludeReasoningFromResponse, String sttLanguage, boolean asrDiarizeEnabled, String localAsrModelId, String localCleanupModelId, String cleanupPrompt, String summaryPrompt, String glossary) {
        Intrinsics.checkNotNullParameter(asrMode, "asrMode");
        Intrinsics.checkNotNullParameter(cleanupMode, "cleanupMode");
        Intrinsics.checkNotNullParameter(summaryMode, "summaryMode");
        Intrinsics.checkNotNullParameter(actionsMode, "actionsMode");
        Intrinsics.checkNotNullParameter(cloudBaseUrl, "cloudBaseUrl");
        Intrinsics.checkNotNullParameter(cloudApiKey, "cloudApiKey");
        Intrinsics.checkNotNullParameter(cloudAsrModel, "cloudAsrModel");
        Intrinsics.checkNotNullParameter(cloudCleanupModel, "cloudCleanupModel");
        Intrinsics.checkNotNullParameter(cloudSummaryModel, "cloudSummaryModel");
        Intrinsics.checkNotNullParameter(reasoningEffort, "reasoningEffort");
        Intrinsics.checkNotNullParameter(sttLanguage, "sttLanguage");
        Intrinsics.checkNotNullParameter(localAsrModelId, "localAsrModelId");
        Intrinsics.checkNotNullParameter(localCleanupModelId, "localCleanupModelId");
        Intrinsics.checkNotNullParameter(cleanupPrompt, "cleanupPrompt");
        Intrinsics.checkNotNullParameter(summaryPrompt, "summaryPrompt");
        Intrinsics.checkNotNullParameter(glossary, "glossary");
        return new PipelineSettings(asrMode, cleanupMode, summaryMode, actionsMode, allowFallback, cloudBaseUrl, cloudApiKey, cloudAsrModel, cloudCleanupModel, cloudSummaryModel, reasoningEffort, excludeReasoningFromResponse, sttLanguage, asrDiarizeEnabled, localAsrModelId, localCleanupModelId, cleanupPrompt, summaryPrompt, glossary);
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof PipelineSettings)) {
            return false;
        }
        PipelineSettings pipelineSettings = (PipelineSettings) other;
        return this.asrMode == pipelineSettings.asrMode && this.cleanupMode == pipelineSettings.cleanupMode && this.summaryMode == pipelineSettings.summaryMode && this.actionsMode == pipelineSettings.actionsMode && this.allowFallback == pipelineSettings.allowFallback && Intrinsics.areEqual(this.cloudBaseUrl, pipelineSettings.cloudBaseUrl) && Intrinsics.areEqual(this.cloudApiKey, pipelineSettings.cloudApiKey) && Intrinsics.areEqual(this.cloudAsrModel, pipelineSettings.cloudAsrModel) && Intrinsics.areEqual(this.cloudCleanupModel, pipelineSettings.cloudCleanupModel) && Intrinsics.areEqual(this.cloudSummaryModel, pipelineSettings.cloudSummaryModel) && Intrinsics.areEqual(this.reasoningEffort, pipelineSettings.reasoningEffort) && this.excludeReasoningFromResponse == pipelineSettings.excludeReasoningFromResponse && Intrinsics.areEqual(this.sttLanguage, pipelineSettings.sttLanguage) && this.asrDiarizeEnabled == pipelineSettings.asrDiarizeEnabled && Intrinsics.areEqual(this.localAsrModelId, pipelineSettings.localAsrModelId) && Intrinsics.areEqual(this.localCleanupModelId, pipelineSettings.localCleanupModelId) && Intrinsics.areEqual(this.cleanupPrompt, pipelineSettings.cleanupPrompt) && Intrinsics.areEqual(this.summaryPrompt, pipelineSettings.summaryPrompt) && Intrinsics.areEqual(this.glossary, pipelineSettings.glossary);
    }

    public int hashCode() {
        return (((((((((((((((((((((((((((((((((((this.asrMode.hashCode() * 31) + this.cleanupMode.hashCode()) * 31) + this.summaryMode.hashCode()) * 31) + this.actionsMode.hashCode()) * 31) + Boolean.hashCode(this.allowFallback)) * 31) + this.cloudBaseUrl.hashCode()) * 31) + this.cloudApiKey.hashCode()) * 31) + this.cloudAsrModel.hashCode()) * 31) + this.cloudCleanupModel.hashCode()) * 31) + this.cloudSummaryModel.hashCode()) * 31) + this.reasoningEffort.hashCode()) * 31) + Boolean.hashCode(this.excludeReasoningFromResponse)) * 31) + this.sttLanguage.hashCode()) * 31) + Boolean.hashCode(this.asrDiarizeEnabled)) * 31) + this.localAsrModelId.hashCode()) * 31) + this.localCleanupModelId.hashCode()) * 31) + this.cleanupPrompt.hashCode()) * 31) + this.summaryPrompt.hashCode()) * 31) + this.glossary.hashCode();
    }

    public String toString() {
        return "PipelineSettings(asrMode=" + this.asrMode + ", cleanupMode=" + this.cleanupMode + ", summaryMode=" + this.summaryMode + ", actionsMode=" + this.actionsMode + ", allowFallback=" + this.allowFallback + ", cloudBaseUrl=" + this.cloudBaseUrl + ", cloudApiKey=" + this.cloudApiKey + ", cloudAsrModel=" + this.cloudAsrModel + ", cloudCleanupModel=" + this.cloudCleanupModel + ", cloudSummaryModel=" + this.cloudSummaryModel + ", reasoningEffort=" + this.reasoningEffort + ", excludeReasoningFromResponse=" + this.excludeReasoningFromResponse + ", sttLanguage=" + this.sttLanguage + ", asrDiarizeEnabled=" + this.asrDiarizeEnabled + ", localAsrModelId=" + this.localAsrModelId + ", localCleanupModelId=" + this.localCleanupModelId + ", cleanupPrompt=" + this.cleanupPrompt + ", summaryPrompt=" + this.summaryPrompt + ", glossary=" + this.glossary + ")";
    }

    public PipelineSettings(ProviderMode asrMode, ProviderMode cleanupMode, ProviderMode summaryMode, ProviderMode actionsMode, boolean allowFallback, String cloudBaseUrl, String cloudApiKey, String cloudAsrModel, String cloudCleanupModel, String cloudSummaryModel, String reasoningEffort, boolean excludeReasoningFromResponse, String sttLanguage, boolean asrDiarizeEnabled, String localAsrModelId, String localCleanupModelId, String cleanupPrompt, String summaryPrompt, String glossary) {
        Intrinsics.checkNotNullParameter(asrMode, "asrMode");
        Intrinsics.checkNotNullParameter(cleanupMode, "cleanupMode");
        Intrinsics.checkNotNullParameter(summaryMode, "summaryMode");
        Intrinsics.checkNotNullParameter(actionsMode, "actionsMode");
        Intrinsics.checkNotNullParameter(cloudBaseUrl, "cloudBaseUrl");
        Intrinsics.checkNotNullParameter(cloudApiKey, "cloudApiKey");
        Intrinsics.checkNotNullParameter(cloudAsrModel, "cloudAsrModel");
        Intrinsics.checkNotNullParameter(cloudCleanupModel, "cloudCleanupModel");
        Intrinsics.checkNotNullParameter(cloudSummaryModel, "cloudSummaryModel");
        Intrinsics.checkNotNullParameter(reasoningEffort, "reasoningEffort");
        Intrinsics.checkNotNullParameter(sttLanguage, "sttLanguage");
        Intrinsics.checkNotNullParameter(localAsrModelId, "localAsrModelId");
        Intrinsics.checkNotNullParameter(localCleanupModelId, "localCleanupModelId");
        Intrinsics.checkNotNullParameter(cleanupPrompt, "cleanupPrompt");
        Intrinsics.checkNotNullParameter(summaryPrompt, "summaryPrompt");
        Intrinsics.checkNotNullParameter(glossary, "glossary");
        this.asrMode = asrMode;
        this.cleanupMode = cleanupMode;
        this.summaryMode = summaryMode;
        this.actionsMode = actionsMode;
        this.allowFallback = allowFallback;
        this.cloudBaseUrl = cloudBaseUrl;
        this.cloudApiKey = cloudApiKey;
        this.cloudAsrModel = cloudAsrModel;
        this.cloudCleanupModel = cloudCleanupModel;
        this.cloudSummaryModel = cloudSummaryModel;
        this.reasoningEffort = reasoningEffort;
        this.excludeReasoningFromResponse = excludeReasoningFromResponse;
        this.sttLanguage = sttLanguage;
        this.asrDiarizeEnabled = asrDiarizeEnabled;
        this.localAsrModelId = localAsrModelId;
        this.localCleanupModelId = localCleanupModelId;
        this.cleanupPrompt = cleanupPrompt;
        this.summaryPrompt = summaryPrompt;
        this.glossary = glossary;
    }

    /* JADX WARN: Illegal instructions before constructor call */
    public /* synthetic */ PipelineSettings(ProviderMode providerMode, ProviderMode providerMode2, ProviderMode providerMode3, ProviderMode providerMode4, boolean z, String str, String str2, String str3, String str4, String str5, String str6, boolean z2, String str7, boolean z3, String str8, String str9, String str10, String str11, String str12, int i, DefaultConstructorMarker defaultConstructorMarker) {
        ProviderMode providerMode5 = (i & 1) != 0 ? ProviderMode.PREFER_CLOUD : providerMode;
        ProviderMode providerMode6 = (i & 2) != 0 ? ProviderMode.PREFER_CLOUD : providerMode2;
        ProviderMode providerMode7 = (i & 4) != 0 ? ProviderMode.PREFER_CLOUD : providerMode3;
        ProviderMode providerMode8 = (i & 8) != 0 ? ProviderMode.PREFER_CLOUD : providerMode4;
        boolean z4 = (i & 16) != 0 ? false : z;
        String str13 = (i & 32) != 0 ? DEFAULT_BASE_URL : str;
        String str14 = (i & 64) != 0 ? "" : str2;
        String str15 = (i & 128) != 0 ? DEFAULT_ASR_MODEL : str3;
        String str16 = (i & 256) != 0 ? "google/gemini-2.5-flash" : str4;
        String str17 = (i & 512) != 0 ? DEFAULT_SUMMARY_MODEL : str5;
        String str18 = (i & 1024) != 0 ? "" : str6;
        boolean z5 = (i & 2048) != 0 ? true : z2;
        String str19 = (i & 4096) != 0 ? "" : str7;
        boolean z6 = (i & 8192) != 0 ? true : z3;
        this(providerMode5, providerMode6, providerMode7, providerMode8, z4, str13, str14, str15, str16, str17, str18, z5, str19, z6, (i & 16384) != 0 ? "parakeet_tdt" : str8, (i & 32768) != 0 ? "gemma-4b" : str9, (i & 65536) != 0 ? "" : str10, (i & 131072) != 0 ? "" : str11, (i & 262144) == 0 ? str12 : "");
    }

    public final ProviderMode getAsrMode() {
        return this.asrMode;
    }

    public final ProviderMode getCleanupMode() {
        return this.cleanupMode;
    }

    public final ProviderMode getSummaryMode() {
        return this.summaryMode;
    }

    public final ProviderMode getActionsMode() {
        return this.actionsMode;
    }

    public final boolean getAllowFallback() {
        return this.allowFallback;
    }

    public final String getCloudBaseUrl() {
        return this.cloudBaseUrl;
    }

    public final String getCloudApiKey() {
        return this.cloudApiKey;
    }

    public final String getCloudAsrModel() {
        return this.cloudAsrModel;
    }

    public final String getCloudCleanupModel() {
        return this.cloudCleanupModel;
    }

    public final String getCloudSummaryModel() {
        return this.cloudSummaryModel;
    }

    public final String getReasoningEffort() {
        return this.reasoningEffort;
    }

    public final boolean getExcludeReasoningFromResponse() {
        return this.excludeReasoningFromResponse;
    }

    public final String getSttLanguage() {
        return this.sttLanguage;
    }

    public final boolean getAsrDiarizeEnabled() {
        return this.asrDiarizeEnabled;
    }

    public final String getLocalAsrModelId() {
        return this.localAsrModelId;
    }

    public final String getLocalCleanupModelId() {
        return this.localCleanupModelId;
    }

    public final String getCleanupPrompt() {
        return this.cleanupPrompt;
    }

    public final String getSummaryPrompt() {
        return this.summaryPrompt;
    }

    public final String getGlossary() {
        return this.glossary;
    }

    public final boolean isOpenRouter() {
        return StringsKt.contains((CharSequence) this.cloudBaseUrl, (CharSequence) "openrouter.ai", true);
    }

    /* JADX INFO: compiled from: PipelineConfig.kt */
    @Metadata(d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0010 \n\u0002\b\t\b\u0086\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003R\u000e\u0010\u0004\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u0017\u0010\n\u001a\b\u0012\u0004\u0012\u00020\u00050\u000b¢\u0006\b\n\u0000\u001a\u0004\b\f\u0010\rR\u0017\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00050\u000b¢\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\rR\u0017\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u00050\u000b¢\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\rR\u0017\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u00050\u000b¢\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\r¨\u0006\u0014"}, d2 = {"Lcom/varun/pocketassistant/pipeline/PipelineSettings$Companion;", "", "<init>", "()V", "DEFAULT_BASE_URL", "", "DEFAULT_ASR_MODEL", "DEFAULT_CLEANUP_MODEL", "DEFAULT_SUMMARY_MODEL", "DEFAULT_CHAT_MODEL", "SUGGESTED_ASR_MODELS", "", "getSUGGESTED_ASR_MODELS", "()Ljava/util/List;", "SUGGESTED_CLEANUP_MODELS", "getSUGGESTED_CLEANUP_MODELS", "SUGGESTED_SUMMARY_MODELS", "getSUGGESTED_SUMMARY_MODELS", "SUGGESTED_CHAT_MODELS", "getSUGGESTED_CHAT_MODELS", "app_debug"}, k = 1, mv = {2, 2, 0}, xi = 48)
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        public final List<String> getSUGGESTED_ASR_MODELS() {
            return PipelineSettings.SUGGESTED_ASR_MODELS;
        }

        public final List<String> getSUGGESTED_CLEANUP_MODELS() {
            return PipelineSettings.SUGGESTED_CLEANUP_MODELS;
        }

        public final List<String> getSUGGESTED_SUMMARY_MODELS() {
            return PipelineSettings.SUGGESTED_SUMMARY_MODELS;
        }

        public final List<String> getSUGGESTED_CHAT_MODELS() {
            return PipelineSettings.SUGGESTED_CHAT_MODELS;
        }
    }
}
