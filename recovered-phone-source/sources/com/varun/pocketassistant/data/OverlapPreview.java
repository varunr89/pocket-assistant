package com.varun.pocketassistant.data;

import java.util.List;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: MeetingRepository.kt */
/* JADX INFO: loaded from: classes9.dex */
@Metadata(d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\t\n\u0002\b\u0016\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\b\u0087\b\u0018\u00002\u00020\u0001BC\u0012\f\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\u0006\u0010\u0007\u001a\u00020\b\u0012\b\b\u0002\u0010\t\u001a\u00020\u0006\u0012\b\b\u0002\u0010\n\u001a\u00020\b\u0012\b\b\u0002\u0010\u000b\u001a\u00020\u0006¢\u0006\u0004\b\f\u0010\rJ\u000f\u0010\u0017\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003HÆ\u0003J\t\u0010\u0018\u001a\u00020\u0006HÆ\u0003J\t\u0010\u0019\u001a\u00020\bHÆ\u0003J\t\u0010\u001a\u001a\u00020\u0006HÆ\u0003J\t\u0010\u001b\u001a\u00020\bHÆ\u0003J\t\u0010\u001c\u001a\u00020\u0006HÆ\u0003JK\u0010\u001d\u001a\u00020\u00002\u000e\b\u0002\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00062\b\b\u0002\u0010\u0007\u001a\u00020\b2\b\b\u0002\u0010\t\u001a\u00020\u00062\b\b\u0002\u0010\n\u001a\u00020\b2\b\b\u0002\u0010\u000b\u001a\u00020\u0006HÆ\u0001J\u0013\u0010\u001e\u001a\u00020\u001f2\b\u0010 \u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010!\u001a\u00020\u0006HÖ\u0001J\t\u0010\"\u001a\u00020#HÖ\u0001R\u0017\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000fR\u0011\u0010\u0005\u001a\u00020\u0006¢\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011R\u0011\u0010\u0007\u001a\u00020\b¢\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0013R\u0011\u0010\t\u001a\u00020\u0006¢\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0011R\u0011\u0010\n\u001a\u00020\b¢\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0013R\u0011\u0010\u000b\u001a\u00020\u0006¢\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\u0011¨\u0006$"}, d2 = {"Lcom/varun/pocketassistant/data/OverlapPreview;", "", "recordings", "", "Lcom/varun/pocketassistant/data/SegmentEntity;", "count", "", "speechDurationMs", "", "readyCount", "readyDurationMs", "pendingAsrCount", "<init>", "(Ljava/util/List;IJIJI)V", "getRecordings", "()Ljava/util/List;", "getCount", "()I", "getSpeechDurationMs", "()J", "getReadyCount", "getReadyDurationMs", "getPendingAsrCount", "component1", "component2", "component3", "component4", "component5", "component6", "copy", "equals", "", "other", "hashCode", "toString", "", "app_debug"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final /* data */ class OverlapPreview {
    public static final int $stable = 8;
    private final int count;
    private final int pendingAsrCount;
    private final int readyCount;
    private final long readyDurationMs;
    private final List<SegmentEntity> recordings;
    private final long speechDurationMs;

    /* JADX WARN: Multi-variable type inference failed */
    public static /* synthetic */ OverlapPreview copy$default(OverlapPreview overlapPreview, List list, int i, long j, int i2, long j2, int i3, int i4, Object obj) {
        if ((i4 & 1) != 0) {
            list = overlapPreview.recordings;
        }
        if ((i4 & 2) != 0) {
            i = overlapPreview.count;
        }
        if ((i4 & 4) != 0) {
            j = overlapPreview.speechDurationMs;
        }
        if ((i4 & 8) != 0) {
            i2 = overlapPreview.readyCount;
        }
        if ((i4 & 16) != 0) {
            j2 = overlapPreview.readyDurationMs;
        }
        if ((i4 & 32) != 0) {
            i3 = overlapPreview.pendingAsrCount;
        }
        int i5 = i2;
        long j3 = j;
        return overlapPreview.copy(list, i, j3, i5, j2, i3);
    }

    public final List<SegmentEntity> component1() {
        return this.recordings;
    }

    /* JADX INFO: renamed from: component2, reason: from getter */
    public final int getCount() {
        return this.count;
    }

    /* JADX INFO: renamed from: component3, reason: from getter */
    public final long getSpeechDurationMs() {
        return this.speechDurationMs;
    }

    /* JADX INFO: renamed from: component4, reason: from getter */
    public final int getReadyCount() {
        return this.readyCount;
    }

    /* JADX INFO: renamed from: component5, reason: from getter */
    public final long getReadyDurationMs() {
        return this.readyDurationMs;
    }

    /* JADX INFO: renamed from: component6, reason: from getter */
    public final int getPendingAsrCount() {
        return this.pendingAsrCount;
    }

    public final OverlapPreview copy(List<SegmentEntity> recordings, int count, long speechDurationMs, int readyCount, long readyDurationMs, int pendingAsrCount) {
        Intrinsics.checkNotNullParameter(recordings, "recordings");
        return new OverlapPreview(recordings, count, speechDurationMs, readyCount, readyDurationMs, pendingAsrCount);
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof OverlapPreview)) {
            return false;
        }
        OverlapPreview overlapPreview = (OverlapPreview) other;
        return Intrinsics.areEqual(this.recordings, overlapPreview.recordings) && this.count == overlapPreview.count && this.speechDurationMs == overlapPreview.speechDurationMs && this.readyCount == overlapPreview.readyCount && this.readyDurationMs == overlapPreview.readyDurationMs && this.pendingAsrCount == overlapPreview.pendingAsrCount;
    }

    public int hashCode() {
        return (((((((((this.recordings.hashCode() * 31) + Integer.hashCode(this.count)) * 31) + Long.hashCode(this.speechDurationMs)) * 31) + Integer.hashCode(this.readyCount)) * 31) + Long.hashCode(this.readyDurationMs)) * 31) + Integer.hashCode(this.pendingAsrCount);
    }

    public String toString() {
        return "OverlapPreview(recordings=" + this.recordings + ", count=" + this.count + ", speechDurationMs=" + this.speechDurationMs + ", readyCount=" + this.readyCount + ", readyDurationMs=" + this.readyDurationMs + ", pendingAsrCount=" + this.pendingAsrCount + ")";
    }

    public OverlapPreview(List<SegmentEntity> recordings, int count, long speechDurationMs, int readyCount, long readyDurationMs, int pendingAsrCount) {
        Intrinsics.checkNotNullParameter(recordings, "recordings");
        this.recordings = recordings;
        this.count = count;
        this.speechDurationMs = speechDurationMs;
        this.readyCount = readyCount;
        this.readyDurationMs = readyDurationMs;
        this.pendingAsrCount = pendingAsrCount;
    }

    public /* synthetic */ OverlapPreview(List list, int i, long j, int i2, long j2, int i3, int i4, DefaultConstructorMarker defaultConstructorMarker) {
        this(list, i, j, (i4 & 8) != 0 ? 0 : i2, (i4 & 16) != 0 ? 0L : j2, (i4 & 32) != 0 ? 0 : i3);
    }

    public final List<SegmentEntity> getRecordings() {
        return this.recordings;
    }

    public final int getCount() {
        return this.count;
    }

    public final long getSpeechDurationMs() {
        return this.speechDurationMs;
    }

    public final int getReadyCount() {
        return this.readyCount;
    }

    public final long getReadyDurationMs() {
        return this.readyDurationMs;
    }

    public final int getPendingAsrCount() {
        return this.pendingAsrCount;
    }
}
