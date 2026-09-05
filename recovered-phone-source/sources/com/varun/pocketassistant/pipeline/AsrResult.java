package com.varun.pocketassistant.pipeline;

import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: Providers.kt */
/* JADX INFO: loaded from: classes8.dex */
@Metadata(d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0010\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\b\u0087\b\u0018\u00002\u00020\u0001B/\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0003\u0012\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u0003¢\u0006\u0004\b\u0007\u0010\bJ\t\u0010\u000e\u001a\u00020\u0003HÆ\u0003J\u000b\u0010\u000f\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\t\u0010\u0010\u001a\u00020\u0003HÆ\u0003J\u000b\u0010\u0011\u001a\u0004\u0018\u00010\u0003HÆ\u0003J5\u0010\u0012\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00032\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u0003HÆ\u0001J\u0013\u0010\u0013\u001a\u00020\u00142\b\u0010\u0015\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u0016\u001a\u00020\u0017HÖ\u0001J\t\u0010\u0018\u001a\u00020\u0003HÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u0013\u0010\u0004\u001a\u0004\u0018\u00010\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\nR\u0011\u0010\u0005\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\f\u0010\nR\u0013\u0010\u0006\u001a\u0004\u0018\u00010\u0003¢\u0006\b\n\u0000\u001a\u0004\b\r\u0010\n¨\u0006\u0019"}, d2 = {"Lcom/varun/pocketassistant/pipeline/AsrResult;", "", "plain", "", "diarized", "providerId", "speakerCandidatesJson", "<init>", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V", "getPlain", "()Ljava/lang/String;", "getDiarized", "getProviderId", "getSpeakerCandidatesJson", "component1", "component2", "component3", "component4", "copy", "equals", "", "other", "hashCode", "", "toString", "app_debug"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final /* data */ class AsrResult {
    public static final int $stable = 0;
    private final String diarized;
    private final String plain;
    private final String providerId;
    private final String speakerCandidatesJson;

    public static /* synthetic */ AsrResult copy$default(AsrResult asrResult, String str, String str2, String str3, String str4, int i, Object obj) {
        if ((i & 1) != 0) {
            str = asrResult.plain;
        }
        if ((i & 2) != 0) {
            str2 = asrResult.diarized;
        }
        if ((i & 4) != 0) {
            str3 = asrResult.providerId;
        }
        if ((i & 8) != 0) {
            str4 = asrResult.speakerCandidatesJson;
        }
        return asrResult.copy(str, str2, str3, str4);
    }

    /* JADX INFO: renamed from: component1, reason: from getter */
    public final String getPlain() {
        return this.plain;
    }

    /* JADX INFO: renamed from: component2, reason: from getter */
    public final String getDiarized() {
        return this.diarized;
    }

    /* JADX INFO: renamed from: component3, reason: from getter */
    public final String getProviderId() {
        return this.providerId;
    }

    /* JADX INFO: renamed from: component4, reason: from getter */
    public final String getSpeakerCandidatesJson() {
        return this.speakerCandidatesJson;
    }

    public final AsrResult copy(String plain, String diarized, String providerId, String speakerCandidatesJson) {
        Intrinsics.checkNotNullParameter(plain, "plain");
        Intrinsics.checkNotNullParameter(providerId, "providerId");
        return new AsrResult(plain, diarized, providerId, speakerCandidatesJson);
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof AsrResult)) {
            return false;
        }
        AsrResult asrResult = (AsrResult) other;
        return Intrinsics.areEqual(this.plain, asrResult.plain) && Intrinsics.areEqual(this.diarized, asrResult.diarized) && Intrinsics.areEqual(this.providerId, asrResult.providerId) && Intrinsics.areEqual(this.speakerCandidatesJson, asrResult.speakerCandidatesJson);
    }

    public int hashCode() {
        return (((((this.plain.hashCode() * 31) + (this.diarized == null ? 0 : this.diarized.hashCode())) * 31) + this.providerId.hashCode()) * 31) + (this.speakerCandidatesJson != null ? this.speakerCandidatesJson.hashCode() : 0);
    }

    public String toString() {
        return "AsrResult(plain=" + this.plain + ", diarized=" + this.diarized + ", providerId=" + this.providerId + ", speakerCandidatesJson=" + this.speakerCandidatesJson + ")";
    }

    public AsrResult(String plain, String diarized, String providerId, String speakerCandidatesJson) {
        Intrinsics.checkNotNullParameter(plain, "plain");
        Intrinsics.checkNotNullParameter(providerId, "providerId");
        this.plain = plain;
        this.diarized = diarized;
        this.providerId = providerId;
        this.speakerCandidatesJson = speakerCandidatesJson;
    }

    public /* synthetic */ AsrResult(String str, String str2, String str3, String str4, int i, DefaultConstructorMarker defaultConstructorMarker) {
        this(str, (i & 2) != 0 ? null : str2, str3, (i & 8) != 0 ? null : str4);
    }

    public final String getPlain() {
        return this.plain;
    }

    public final String getDiarized() {
        return this.diarized;
    }

    public final String getProviderId() {
        return this.providerId;
    }

    public final String getSpeakerCandidatesJson() {
        return this.speakerCandidatesJson;
    }
}
