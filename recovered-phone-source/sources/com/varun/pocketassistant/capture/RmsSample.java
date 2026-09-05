package com.varun.pocketassistant.capture;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: CaptureTelemetry.kt */
/* JADX INFO: loaded from: classes5.dex */
@Metadata(d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u0007\n\u0000\n\u0002\u0018\u0002\n\u0002\b\r\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0087\b\u0018\u00002\u00020\u0001B\u001f\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007¢\u0006\u0004\b\b\u0010\tJ\t\u0010\u0010\u001a\u00020\u0003HÆ\u0003J\t\u0010\u0011\u001a\u00020\u0005HÆ\u0003J\t\u0010\u0012\u001a\u00020\u0007HÆ\u0003J'\u0010\u0013\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u0007HÆ\u0001J\u0013\u0010\u0014\u001a\u00020\u00152\b\u0010\u0016\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u0017\u001a\u00020\u0018HÖ\u0001J\t\u0010\u0019\u001a\u00020\u001aHÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000bR\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\f\u0010\rR\u0011\u0010\u0006\u001a\u00020\u0007¢\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000f¨\u0006\u001b"}, d2 = {"Lcom/varun/pocketassistant/capture/RmsSample;", "", "atMs", "", "rms", "", "phase", "Lcom/varun/pocketassistant/capture/CapturePhase;", "<init>", "(JFLcom/varun/pocketassistant/capture/CapturePhase;)V", "getAtMs", "()J", "getRms", "()F", "getPhase", "()Lcom/varun/pocketassistant/capture/CapturePhase;", "component1", "component2", "component3", "copy", "equals", "", "other", "hashCode", "", "toString", "", "app_debug"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final /* data */ class RmsSample {
    public static final int $stable = 0;
    private final long atMs;
    private final CapturePhase phase;
    private final float rms;

    public static /* synthetic */ RmsSample copy$default(RmsSample rmsSample, long j, float f, CapturePhase capturePhase, int i, Object obj) {
        if ((i & 1) != 0) {
            j = rmsSample.atMs;
        }
        if ((i & 2) != 0) {
            f = rmsSample.rms;
        }
        if ((i & 4) != 0) {
            capturePhase = rmsSample.phase;
        }
        return rmsSample.copy(j, f, capturePhase);
    }

    /* JADX INFO: renamed from: component1, reason: from getter */
    public final long getAtMs() {
        return this.atMs;
    }

    /* JADX INFO: renamed from: component2, reason: from getter */
    public final float getRms() {
        return this.rms;
    }

    /* JADX INFO: renamed from: component3, reason: from getter */
    public final CapturePhase getPhase() {
        return this.phase;
    }

    public final RmsSample copy(long atMs, float rms, CapturePhase phase) {
        Intrinsics.checkNotNullParameter(phase, "phase");
        return new RmsSample(atMs, rms, phase);
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof RmsSample)) {
            return false;
        }
        RmsSample rmsSample = (RmsSample) other;
        return this.atMs == rmsSample.atMs && Float.compare(this.rms, rmsSample.rms) == 0 && this.phase == rmsSample.phase;
    }

    public int hashCode() {
        return (((Long.hashCode(this.atMs) * 31) + Float.hashCode(this.rms)) * 31) + this.phase.hashCode();
    }

    public String toString() {
        return "RmsSample(atMs=" + this.atMs + ", rms=" + this.rms + ", phase=" + this.phase + ")";
    }

    public RmsSample(long atMs, float rms, CapturePhase phase) {
        Intrinsics.checkNotNullParameter(phase, "phase");
        this.atMs = atMs;
        this.rms = rms;
        this.phase = phase;
    }

    public final long getAtMs() {
        return this.atMs;
    }

    public final float getRms() {
        return this.rms;
    }

    public final CapturePhase getPhase() {
        return this.phase;
    }
}
