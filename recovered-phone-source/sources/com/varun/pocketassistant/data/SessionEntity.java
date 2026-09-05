package com.varun.pocketassistant.data;

import androidx.core.app.NotificationCompat;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: AppDatabase.kt */
/* JADX INFO: loaded from: classes9.dex */
@Metadata(d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u001a\n\u0002\u0010\u000b\n\u0002\b\u0004\b\u0087\b\u0018\u00002\u00020\u0001BK\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u0005\u0012\u0006\u0010\u0007\u001a\u00020\u0003\u0012\b\b\u0002\u0010\b\u001a\u00020\t\u0012\b\b\u0002\u0010\n\u001a\u00020\u0005\u0012\n\b\u0002\u0010\u000b\u001a\u0004\u0018\u00010\u0003¢\u0006\u0004\b\f\u0010\rJ\t\u0010\u001a\u001a\u00020\u0003HÆ\u0003J\t\u0010\u001b\u001a\u00020\u0005HÆ\u0003J\u0010\u0010\u001c\u001a\u0004\u0018\u00010\u0005HÆ\u0003¢\u0006\u0002\u0010\u0013J\t\u0010\u001d\u001a\u00020\u0003HÆ\u0003J\t\u0010\u001e\u001a\u00020\tHÆ\u0003J\t\u0010\u001f\u001a\u00020\u0005HÆ\u0003J\u000b\u0010 \u001a\u0004\u0018\u00010\u0003HÆ\u0003JX\u0010!\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u00052\b\b\u0002\u0010\u0007\u001a\u00020\u00032\b\b\u0002\u0010\b\u001a\u00020\t2\b\b\u0002\u0010\n\u001a\u00020\u00052\n\b\u0002\u0010\u000b\u001a\u0004\u0018\u00010\u0003HÆ\u0001¢\u0006\u0002\u0010\"J\u0013\u0010#\u001a\u00020$2\b\u0010%\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010&\u001a\u00020\tHÖ\u0001J\t\u0010'\u001a\u00020\u0003HÖ\u0001R\u0016\u0010\u0002\u001a\u00020\u00038\u0006X\u0087\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000fR\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011R\u0015\u0010\u0006\u001a\u0004\u0018\u00010\u0005¢\u0006\n\n\u0002\u0010\u0014\u001a\u0004\b\u0012\u0010\u0013R\u0011\u0010\u0007\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u000fR\u0011\u0010\b\u001a\u00020\t¢\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\u0017R\u0011\u0010\n\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u0018\u0010\u0011R\u0013\u0010\u000b\u001a\u0004\u0018\u00010\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0019\u0010\u000f¨\u0006("}, d2 = {"Lcom/varun/pocketassistant/data/SessionEntity;", "", "id", "", "startedAtMs", "", "endedAtMs", NotificationCompat.CATEGORY_STATUS, "segmentCount", "", "speechDurationMs", "notes", "<init>", "(Ljava/lang/String;JLjava/lang/Long;Ljava/lang/String;IJLjava/lang/String;)V", "getId", "()Ljava/lang/String;", "getStartedAtMs", "()J", "getEndedAtMs", "()Ljava/lang/Long;", "Ljava/lang/Long;", "getStatus", "getSegmentCount", "()I", "getSpeechDurationMs", "getNotes", "component1", "component2", "component3", "component4", "component5", "component6", "component7", "copy", "(Ljava/lang/String;JLjava/lang/Long;Ljava/lang/String;IJLjava/lang/String;)Lcom/varun/pocketassistant/data/SessionEntity;", "equals", "", "other", "hashCode", "toString", "app_debug"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final /* data */ class SessionEntity {
    public static final int $stable = 0;
    private final Long endedAtMs;
    private final String id;
    private final String notes;
    private final int segmentCount;
    private final long speechDurationMs;
    private final long startedAtMs;
    private final String status;

    public static /* synthetic */ SessionEntity copy$default(SessionEntity sessionEntity, String str, long j, Long l, String str2, int i, long j2, String str3, int i2, Object obj) {
        if ((i2 & 1) != 0) {
            str = sessionEntity.id;
        }
        if ((i2 & 2) != 0) {
            j = sessionEntity.startedAtMs;
        }
        if ((i2 & 4) != 0) {
            l = sessionEntity.endedAtMs;
        }
        if ((i2 & 8) != 0) {
            str2 = sessionEntity.status;
        }
        if ((i2 & 16) != 0) {
            i = sessionEntity.segmentCount;
        }
        if ((i2 & 32) != 0) {
            j2 = sessionEntity.speechDurationMs;
        }
        if ((i2 & 64) != 0) {
            str3 = sessionEntity.notes;
        }
        String str4 = str3;
        int i3 = i;
        Long l2 = l;
        return sessionEntity.copy(str, j, l2, str2, i3, j2, str4);
    }

    /* JADX INFO: renamed from: component1, reason: from getter */
    public final String getId() {
        return this.id;
    }

    /* JADX INFO: renamed from: component2, reason: from getter */
    public final long getStartedAtMs() {
        return this.startedAtMs;
    }

    /* JADX INFO: renamed from: component3, reason: from getter */
    public final Long getEndedAtMs() {
        return this.endedAtMs;
    }

    /* JADX INFO: renamed from: component4, reason: from getter */
    public final String getStatus() {
        return this.status;
    }

    /* JADX INFO: renamed from: component5, reason: from getter */
    public final int getSegmentCount() {
        return this.segmentCount;
    }

    /* JADX INFO: renamed from: component6, reason: from getter */
    public final long getSpeechDurationMs() {
        return this.speechDurationMs;
    }

    /* JADX INFO: renamed from: component7, reason: from getter */
    public final String getNotes() {
        return this.notes;
    }

    public final SessionEntity copy(String id, long startedAtMs, Long endedAtMs, String status, int segmentCount, long speechDurationMs, String notes) {
        Intrinsics.checkNotNullParameter(id, "id");
        Intrinsics.checkNotNullParameter(status, "status");
        return new SessionEntity(id, startedAtMs, endedAtMs, status, segmentCount, speechDurationMs, notes);
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof SessionEntity)) {
            return false;
        }
        SessionEntity sessionEntity = (SessionEntity) other;
        return Intrinsics.areEqual(this.id, sessionEntity.id) && this.startedAtMs == sessionEntity.startedAtMs && Intrinsics.areEqual(this.endedAtMs, sessionEntity.endedAtMs) && Intrinsics.areEqual(this.status, sessionEntity.status) && this.segmentCount == sessionEntity.segmentCount && this.speechDurationMs == sessionEntity.speechDurationMs && Intrinsics.areEqual(this.notes, sessionEntity.notes);
    }

    public int hashCode() {
        return (((((((((((this.id.hashCode() * 31) + Long.hashCode(this.startedAtMs)) * 31) + (this.endedAtMs == null ? 0 : this.endedAtMs.hashCode())) * 31) + this.status.hashCode()) * 31) + Integer.hashCode(this.segmentCount)) * 31) + Long.hashCode(this.speechDurationMs)) * 31) + (this.notes != null ? this.notes.hashCode() : 0);
    }

    public String toString() {
        return "SessionEntity(id=" + this.id + ", startedAtMs=" + this.startedAtMs + ", endedAtMs=" + this.endedAtMs + ", status=" + this.status + ", segmentCount=" + this.segmentCount + ", speechDurationMs=" + this.speechDurationMs + ", notes=" + this.notes + ")";
    }

    public SessionEntity(String id, long startedAtMs, Long endedAtMs, String status, int segmentCount, long speechDurationMs, String notes) {
        Intrinsics.checkNotNullParameter(id, "id");
        Intrinsics.checkNotNullParameter(status, "status");
        this.id = id;
        this.startedAtMs = startedAtMs;
        this.endedAtMs = endedAtMs;
        this.status = status;
        this.segmentCount = segmentCount;
        this.speechDurationMs = speechDurationMs;
        this.notes = notes;
    }

    public /* synthetic */ SessionEntity(String str, long j, Long l, String str2, int i, long j2, String str3, int i2, DefaultConstructorMarker defaultConstructorMarker) {
        this(str, j, (i2 & 4) != 0 ? null : l, str2, (i2 & 16) != 0 ? 0 : i, (i2 & 32) != 0 ? 0L : j2, (i2 & 64) != 0 ? null : str3);
    }

    public final String getId() {
        return this.id;
    }

    public final long getStartedAtMs() {
        return this.startedAtMs;
    }

    public final Long getEndedAtMs() {
        return this.endedAtMs;
    }

    public final String getStatus() {
        return this.status;
    }

    public final int getSegmentCount() {
        return this.segmentCount;
    }

    public final long getSpeechDurationMs() {
        return this.speechDurationMs;
    }

    public final String getNotes() {
        return this.notes;
    }
}
