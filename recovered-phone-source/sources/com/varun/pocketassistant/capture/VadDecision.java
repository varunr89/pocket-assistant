package com.varun.pocketassistant.capture;

import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;

/* JADX INFO: compiled from: CaptureTelemetry.kt */
/* JADX INFO: loaded from: classes5.dex */
@Metadata(d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0010\u0007\n\u0002\b\u0015\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0087\b\u0018\u00002\u00020\u0001B=\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\b\b\u0002\u0010\u0007\u001a\u00020\u0006\u0012\b\b\u0002\u0010\b\u001a\u00020\u0006\u0012\b\b\u0002\u0010\t\u001a\u00020\n¢\u0006\u0004\b\u000b\u0010\fJ\t\u0010\u0016\u001a\u00020\u0003HÆ\u0003J\t\u0010\u0017\u001a\u00020\u0003HÆ\u0003J\t\u0010\u0018\u001a\u00020\u0006HÆ\u0003J\t\u0010\u0019\u001a\u00020\u0006HÆ\u0003J\t\u0010\u001a\u001a\u00020\u0006HÆ\u0003J\t\u0010\u001b\u001a\u00020\nHÆ\u0003JE\u0010\u001c\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00062\b\b\u0002\u0010\u0007\u001a\u00020\u00062\b\b\u0002\u0010\b\u001a\u00020\u00062\b\b\u0002\u0010\t\u001a\u00020\nHÆ\u0001J\u0013\u0010\u001d\u001a\u00020\u00032\b\u0010\u001e\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u001f\u001a\u00020 HÖ\u0001J\t\u0010!\u001a\u00020\"HÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000eR\u0011\u0010\u0004\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u000eR\u0011\u0010\u0005\u001a\u00020\u0006¢\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011R\u0011\u0010\u0007\u001a\u00020\u0006¢\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0011R\u0011\u0010\b\u001a\u00020\u0006¢\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0011R\u0011\u0010\t\u001a\u00020\n¢\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0015¨\u0006#"}, d2 = {"Lcom/varun/pocketassistant/capture/VadDecision;", "", "windowOpen", "", "rawSpeech", "hangoverRemainingMs", "", "openGateProgressMs", "openGateRequiredMs", "speechProbability", "", "<init>", "(ZZJJJF)V", "getWindowOpen", "()Z", "getRawSpeech", "getHangoverRemainingMs", "()J", "getOpenGateProgressMs", "getOpenGateRequiredMs", "getSpeechProbability", "()F", "component1", "component2", "component3", "component4", "component5", "component6", "copy", "equals", "other", "hashCode", "", "toString", "", "app_debug"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final /* data */ class VadDecision {
    public static final int $stable = 0;
    private final long hangoverRemainingMs;
    private final long openGateProgressMs;
    private final long openGateRequiredMs;
    private final boolean rawSpeech;
    private final float speechProbability;
    private final boolean windowOpen;

    public static /* synthetic */ VadDecision copy$default(VadDecision vadDecision, boolean z, boolean z2, long j, long j2, long j3, float f, int i, Object obj) {
        if ((i & 1) != 0) {
            z = vadDecision.windowOpen;
        }
        if ((i & 2) != 0) {
            z2 = vadDecision.rawSpeech;
        }
        if ((i & 4) != 0) {
            j = vadDecision.hangoverRemainingMs;
        }
        if ((i & 8) != 0) {
            j2 = vadDecision.openGateProgressMs;
        }
        if ((i & 16) != 0) {
            j3 = vadDecision.openGateRequiredMs;
        }
        if ((i & 32) != 0) {
            f = vadDecision.speechProbability;
        }
        float f2 = f;
        long j4 = j3;
        long j5 = j2;
        return vadDecision.copy(z, z2, j, j5, j4, f2);
    }

    /* JADX INFO: renamed from: component1, reason: from getter */
    public final boolean getWindowOpen() {
        return this.windowOpen;
    }

    /* JADX INFO: renamed from: component2, reason: from getter */
    public final boolean getRawSpeech() {
        return this.rawSpeech;
    }

    /* JADX INFO: renamed from: component3, reason: from getter */
    public final long getHangoverRemainingMs() {
        return this.hangoverRemainingMs;
    }

    /* JADX INFO: renamed from: component4, reason: from getter */
    public final long getOpenGateProgressMs() {
        return this.openGateProgressMs;
    }

    /* JADX INFO: renamed from: component5, reason: from getter */
    public final long getOpenGateRequiredMs() {
        return this.openGateRequiredMs;
    }

    /* JADX INFO: renamed from: component6, reason: from getter */
    public final float getSpeechProbability() {
        return this.speechProbability;
    }

    public final VadDecision copy(boolean windowOpen, boolean rawSpeech, long hangoverRemainingMs, long openGateProgressMs, long openGateRequiredMs, float speechProbability) {
        return new VadDecision(windowOpen, rawSpeech, hangoverRemainingMs, openGateProgressMs, openGateRequiredMs, speechProbability);
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof VadDecision)) {
            return false;
        }
        VadDecision vadDecision = (VadDecision) other;
        return this.windowOpen == vadDecision.windowOpen && this.rawSpeech == vadDecision.rawSpeech && this.hangoverRemainingMs == vadDecision.hangoverRemainingMs && this.openGateProgressMs == vadDecision.openGateProgressMs && this.openGateRequiredMs == vadDecision.openGateRequiredMs && Float.compare(this.speechProbability, vadDecision.speechProbability) == 0;
    }

    public int hashCode() {
        return (((((((((Boolean.hashCode(this.windowOpen) * 31) + Boolean.hashCode(this.rawSpeech)) * 31) + Long.hashCode(this.hangoverRemainingMs)) * 31) + Long.hashCode(this.openGateProgressMs)) * 31) + Long.hashCode(this.openGateRequiredMs)) * 31) + Float.hashCode(this.speechProbability);
    }

    public String toString() {
        return "VadDecision(windowOpen=" + this.windowOpen + ", rawSpeech=" + this.rawSpeech + ", hangoverRemainingMs=" + this.hangoverRemainingMs + ", openGateProgressMs=" + this.openGateProgressMs + ", openGateRequiredMs=" + this.openGateRequiredMs + ", speechProbability=" + this.speechProbability + ")";
    }

    public VadDecision(boolean windowOpen, boolean rawSpeech, long hangoverRemainingMs, long openGateProgressMs, long openGateRequiredMs, float speechProbability) {
        this.windowOpen = windowOpen;
        this.rawSpeech = rawSpeech;
        this.hangoverRemainingMs = hangoverRemainingMs;
        this.openGateProgressMs = openGateProgressMs;
        this.openGateRequiredMs = openGateRequiredMs;
        this.speechProbability = speechProbability;
    }

    public /* synthetic */ VadDecision(boolean z, boolean z2, long j, long j2, long j3, float f, int i, DefaultConstructorMarker defaultConstructorMarker) {
        this(z, z2, j, (i & 8) != 0 ? 0L : j2, (i & 16) != 0 ? 1500L : j3, (i & 32) != 0 ? -1.0f : f);
    }

    public final boolean getWindowOpen() {
        return this.windowOpen;
    }

    public final boolean getRawSpeech() {
        return this.rawSpeech;
    }

    public final long getHangoverRemainingMs() {
        return this.hangoverRemainingMs;
    }

    public final long getOpenGateProgressMs() {
        return this.openGateProgressMs;
    }

    public final long getOpenGateRequiredMs() {
        return this.openGateRequiredMs;
    }

    public final float getSpeechProbability() {
        return this.speechProbability;
    }
}
