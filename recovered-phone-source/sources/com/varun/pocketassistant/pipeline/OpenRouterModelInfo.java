package com.varun.pocketassistant.pipeline;

import androidx.autofill.HintConstants;
import java.util.List;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: OpenRouterModels.kt */
/* JADX INFO: loaded from: classes8.dex */
@Metadata(d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010 \n\u0002\b\u0017\n\u0002\u0010\b\n\u0002\b\u0002\b\u0087\b\u0018\u00002\u00020\u0001BG\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0005\u001a\u00020\u0006\u0012\u000e\b\u0002\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\u00030\b\u0012\n\b\u0002\u0010\t\u001a\u0004\u0018\u00010\u0003\u0012\b\b\u0002\u0010\n\u001a\u00020\u0006¢\u0006\u0004\b\u000b\u0010\fJ\t\u0010\u0016\u001a\u00020\u0003HÆ\u0003J\t\u0010\u0017\u001a\u00020\u0003HÆ\u0003J\t\u0010\u0018\u001a\u00020\u0006HÆ\u0003J\u000f\u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\u00030\bHÆ\u0003J\u000b\u0010\u001a\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\t\u0010\u001b\u001a\u00020\u0006HÆ\u0003JM\u0010\u001c\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00062\u000e\b\u0002\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\u00030\b2\n\b\u0002\u0010\t\u001a\u0004\u0018\u00010\u00032\b\b\u0002\u0010\n\u001a\u00020\u0006HÆ\u0001J\u0013\u0010\u001d\u001a\u00020\u00062\b\u0010\u001e\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u001f\u001a\u00020 HÖ\u0001J\t\u0010!\u001a\u00020\u0003HÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000eR\u0011\u0010\u0004\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u000eR\u0011\u0010\u0005\u001a\u00020\u0006¢\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011R\u0017\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\u00030\b¢\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0013R\u0013\u0010\t\u001a\u0004\u0018\u00010\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u000eR\u0011\u0010\n\u001a\u00020\u0006¢\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0011¨\u0006\""}, d2 = {"Lcom/varun/pocketassistant/pipeline/OpenRouterModelInfo;", "", "id", "", HintConstants.AUTOFILL_HINT_NAME, "supportsReasoning", "", "supportedEfforts", "", "defaultEffort", "reasoningMandatory", "<init>", "(Ljava/lang/String;Ljava/lang/String;ZLjava/util/List;Ljava/lang/String;Z)V", "getId", "()Ljava/lang/String;", "getName", "getSupportsReasoning", "()Z", "getSupportedEfforts", "()Ljava/util/List;", "getDefaultEffort", "getReasoningMandatory", "component1", "component2", "component3", "component4", "component5", "component6", "copy", "equals", "other", "hashCode", "", "toString", "app_debug"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final /* data */ class OpenRouterModelInfo {
    public static final int $stable = 8;
    private final String defaultEffort;
    private final String id;
    private final String name;
    private final boolean reasoningMandatory;
    private final List<String> supportedEfforts;
    private final boolean supportsReasoning;

    /* JADX WARN: Multi-variable type inference failed */
    public static /* synthetic */ OpenRouterModelInfo copy$default(OpenRouterModelInfo openRouterModelInfo, String str, String str2, boolean z, List list, String str3, boolean z2, int i, Object obj) {
        if ((i & 1) != 0) {
            str = openRouterModelInfo.id;
        }
        if ((i & 2) != 0) {
            str2 = openRouterModelInfo.name;
        }
        if ((i & 4) != 0) {
            z = openRouterModelInfo.supportsReasoning;
        }
        if ((i & 8) != 0) {
            list = openRouterModelInfo.supportedEfforts;
        }
        if ((i & 16) != 0) {
            str3 = openRouterModelInfo.defaultEffort;
        }
        if ((i & 32) != 0) {
            z2 = openRouterModelInfo.reasoningMandatory;
        }
        String str4 = str3;
        boolean z3 = z2;
        return openRouterModelInfo.copy(str, str2, z, list, str4, z3);
    }

    /* JADX INFO: renamed from: component1, reason: from getter */
    public final String getId() {
        return this.id;
    }

    /* JADX INFO: renamed from: component2, reason: from getter */
    public final String getName() {
        return this.name;
    }

    /* JADX INFO: renamed from: component3, reason: from getter */
    public final boolean getSupportsReasoning() {
        return this.supportsReasoning;
    }

    public final List<String> component4() {
        return this.supportedEfforts;
    }

    /* JADX INFO: renamed from: component5, reason: from getter */
    public final String getDefaultEffort() {
        return this.defaultEffort;
    }

    /* JADX INFO: renamed from: component6, reason: from getter */
    public final boolean getReasoningMandatory() {
        return this.reasoningMandatory;
    }

    public final OpenRouterModelInfo copy(String id, String name, boolean supportsReasoning, List<String> supportedEfforts, String defaultEffort, boolean reasoningMandatory) {
        Intrinsics.checkNotNullParameter(id, "id");
        Intrinsics.checkNotNullParameter(name, "name");
        Intrinsics.checkNotNullParameter(supportedEfforts, "supportedEfforts");
        return new OpenRouterModelInfo(id, name, supportsReasoning, supportedEfforts, defaultEffort, reasoningMandatory);
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof OpenRouterModelInfo)) {
            return false;
        }
        OpenRouterModelInfo openRouterModelInfo = (OpenRouterModelInfo) other;
        return Intrinsics.areEqual(this.id, openRouterModelInfo.id) && Intrinsics.areEqual(this.name, openRouterModelInfo.name) && this.supportsReasoning == openRouterModelInfo.supportsReasoning && Intrinsics.areEqual(this.supportedEfforts, openRouterModelInfo.supportedEfforts) && Intrinsics.areEqual(this.defaultEffort, openRouterModelInfo.defaultEffort) && this.reasoningMandatory == openRouterModelInfo.reasoningMandatory;
    }

    public int hashCode() {
        return (((((((((this.id.hashCode() * 31) + this.name.hashCode()) * 31) + Boolean.hashCode(this.supportsReasoning)) * 31) + this.supportedEfforts.hashCode()) * 31) + (this.defaultEffort == null ? 0 : this.defaultEffort.hashCode())) * 31) + Boolean.hashCode(this.reasoningMandatory);
    }

    public String toString() {
        return "OpenRouterModelInfo(id=" + this.id + ", name=" + this.name + ", supportsReasoning=" + this.supportsReasoning + ", supportedEfforts=" + this.supportedEfforts + ", defaultEffort=" + this.defaultEffort + ", reasoningMandatory=" + this.reasoningMandatory + ")";
    }

    public OpenRouterModelInfo(String id, String name, boolean supportsReasoning, List<String> supportedEfforts, String defaultEffort, boolean reasoningMandatory) {
        Intrinsics.checkNotNullParameter(id, "id");
        Intrinsics.checkNotNullParameter(name, "name");
        Intrinsics.checkNotNullParameter(supportedEfforts, "supportedEfforts");
        this.id = id;
        this.name = name;
        this.supportsReasoning = supportsReasoning;
        this.supportedEfforts = supportedEfforts;
        this.defaultEffort = defaultEffort;
        this.reasoningMandatory = reasoningMandatory;
    }

    public /* synthetic */ OpenRouterModelInfo(String str, String str2, boolean z, List list, String str3, boolean z2, int i, DefaultConstructorMarker defaultConstructorMarker) {
        this(str, str2, (i & 4) != 0 ? false : z, (i & 8) != 0 ? CollectionsKt.emptyList() : list, (i & 16) != 0 ? null : str3, (i & 32) != 0 ? false : z2);
    }

    public final String getId() {
        return this.id;
    }

    public final String getName() {
        return this.name;
    }

    public final boolean getSupportsReasoning() {
        return this.supportsReasoning;
    }

    public final List<String> getSupportedEfforts() {
        return this.supportedEfforts;
    }

    public final String getDefaultEffort() {
        return this.defaultEffort;
    }

    public final boolean getReasoningMandatory() {
        return this.reasoningMandatory;
    }
}
