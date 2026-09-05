package com.varun.pocketassistant.capture;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: CaptureTelemetry.kt */
/* JADX INFO: loaded from: classes5.dex */
@Metadata(d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u000e\n\u0002\b\n\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\b\u0087\b\u0018\u00002\u00020\u0001B\u0017\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0004\b\u0006\u0010\u0007J\t\u0010\f\u001a\u00020\u0003HÆ\u0003J\t\u0010\r\u001a\u00020\u0005HÆ\u0003J\u001d\u0010\u000e\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u0005HÆ\u0001J\u0013\u0010\u000f\u001a\u00020\u00102\b\u0010\u0011\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u0012\u001a\u00020\u0013HÖ\u0001J\t\u0010\u0014\u001a\u00020\u0005HÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000b¨\u0006\u0015"}, d2 = {"Lcom/varun/pocketassistant/capture/CaptureEvent;", "", "atMs", "", "message", "", "<init>", "(JLjava/lang/String;)V", "getAtMs", "()J", "getMessage", "()Ljava/lang/String;", "component1", "component2", "copy", "equals", "", "other", "hashCode", "", "toString", "app_debug"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final /* data */ class CaptureEvent {
    public static final int $stable = 0;
    private final long atMs;
    private final String message;

    public static /* synthetic */ CaptureEvent copy$default(CaptureEvent captureEvent, long j, String str, int i, Object obj) {
        if ((i & 1) != 0) {
            j = captureEvent.atMs;
        }
        if ((i & 2) != 0) {
            str = captureEvent.message;
        }
        return captureEvent.copy(j, str);
    }

    /* JADX INFO: renamed from: component1, reason: from getter */
    public final long getAtMs() {
        return this.atMs;
    }

    /* JADX INFO: renamed from: component2, reason: from getter */
    public final String getMessage() {
        return this.message;
    }

    public final CaptureEvent copy(long atMs, String message) {
        Intrinsics.checkNotNullParameter(message, "message");
        return new CaptureEvent(atMs, message);
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof CaptureEvent)) {
            return false;
        }
        CaptureEvent captureEvent = (CaptureEvent) other;
        return this.atMs == captureEvent.atMs && Intrinsics.areEqual(this.message, captureEvent.message);
    }

    public int hashCode() {
        return (Long.hashCode(this.atMs) * 31) + this.message.hashCode();
    }

    public String toString() {
        return "CaptureEvent(atMs=" + this.atMs + ", message=" + this.message + ")";
    }

    public CaptureEvent(long atMs, String message) {
        Intrinsics.checkNotNullParameter(message, "message");
        this.atMs = atMs;
        this.message = message;
    }

    public final long getAtMs() {
        return this.atMs;
    }

    public final String getMessage() {
        return this.message;
    }
}
