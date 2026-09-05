package com.varun.pocketassistant.data;

import androidx.core.app.NotificationCompat;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: AppDatabase.kt */
/* JADX INFO: loaded from: classes9.dex */
@Metadata(d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b'\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\b\u0087\b\u0018\u00002\u00020\u0001B\u0085\u0001\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\u0006\u0010\u0007\u001a\u00020\u0006\u0012\b\b\u0002\u0010\b\u001a\u00020\u0003\u0012\n\b\u0002\u0010\t\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\n\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u000b\u001a\u0004\u0018\u00010\u0003\u0012\b\b\u0002\u0010\f\u001a\u00020\u0006\u0012\n\b\u0002\u0010\r\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u000e\u001a\u0004\u0018\u00010\u0003\u0012\b\b\u0002\u0010\u000f\u001a\u00020\u0006¢\u0006\u0004\b\u0010\u0010\u0011J\t\u0010 \u001a\u00020\u0003HÆ\u0003J\u000b\u0010!\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\t\u0010\"\u001a\u00020\u0006HÆ\u0003J\t\u0010#\u001a\u00020\u0006HÆ\u0003J\t\u0010$\u001a\u00020\u0003HÆ\u0003J\u000b\u0010%\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\u000b\u0010&\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\u000b\u0010'\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\t\u0010(\u001a\u00020\u0006HÆ\u0003J\u000b\u0010)\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\u000b\u0010*\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\t\u0010+\u001a\u00020\u0006HÆ\u0003J\u008d\u0001\u0010,\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00062\b\b\u0002\u0010\u0007\u001a\u00020\u00062\b\b\u0002\u0010\b\u001a\u00020\u00032\n\b\u0002\u0010\t\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\n\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u000b\u001a\u0004\u0018\u00010\u00032\b\b\u0002\u0010\f\u001a\u00020\u00062\n\b\u0002\u0010\r\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u000e\u001a\u0004\u0018\u00010\u00032\b\b\u0002\u0010\u000f\u001a\u00020\u0006HÆ\u0001J\u0013\u0010-\u001a\u00020.2\b\u0010/\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u00100\u001a\u000201HÖ\u0001J\t\u00102\u001a\u00020\u0003HÖ\u0001R\u0016\u0010\u0002\u001a\u00020\u00038\u0006X\u0087\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0013R\u0013\u0010\u0004\u001a\u0004\u0018\u00010\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0013R\u0011\u0010\u0005\u001a\u00020\u0006¢\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0016R\u0011\u0010\u0007\u001a\u00020\u0006¢\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u0016R\u0011\u0010\b\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0018\u0010\u0013R\u0013\u0010\t\u001a\u0004\u0018\u00010\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0019\u0010\u0013R\u0013\u0010\n\u001a\u0004\u0018\u00010\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u001a\u0010\u0013R\u0013\u0010\u000b\u001a\u0004\u0018\u00010\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u001b\u0010\u0013R\u0011\u0010\f\u001a\u00020\u0006¢\u0006\b\n\u0000\u001a\u0004\b\u001c\u0010\u0016R\u0013\u0010\r\u001a\u0004\u0018\u00010\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u001d\u0010\u0013R\u0013\u0010\u000e\u001a\u0004\u0018\u00010\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u001e\u0010\u0013R\u0011\u0010\u000f\u001a\u00020\u0006¢\u0006\b\n\u0000\u001a\u0004\b\u001f\u0010\u0016¨\u00063"}, d2 = {"Lcom/varun/pocketassistant/data/MeetingEntity;", "", "id", "", "title", "startedAtMs", "", "endedAtMs", NotificationCompat.CATEGORY_STATUS, "cleanedTranscript", "metadataJson", "cleanupProvider", "createdAtMs", "cleanTextOnly", "lastError", "updatedAtMs", "<init>", "(Ljava/lang/String;Ljava/lang/String;JJLjava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;JLjava/lang/String;Ljava/lang/String;J)V", "getId", "()Ljava/lang/String;", "getTitle", "getStartedAtMs", "()J", "getEndedAtMs", "getStatus", "getCleanedTranscript", "getMetadataJson", "getCleanupProvider", "getCreatedAtMs", "getCleanTextOnly", "getLastError", "getUpdatedAtMs", "component1", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "component10", "component11", "component12", "copy", "equals", "", "other", "hashCode", "", "toString", "app_debug"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final /* data */ class MeetingEntity {
    public static final int $stable = 0;
    private final String cleanTextOnly;
    private final String cleanedTranscript;
    private final String cleanupProvider;
    private final long createdAtMs;
    private final long endedAtMs;
    private final String id;
    private final String lastError;
    private final String metadataJson;
    private final long startedAtMs;
    private final String status;
    private final String title;
    private final long updatedAtMs;

    public static /* synthetic */ MeetingEntity copy$default(MeetingEntity meetingEntity, String str, String str2, long j, long j2, String str3, String str4, String str5, String str6, long j3, String str7, String str8, long j4, int i, Object obj) {
        long j5;
        String str9;
        String str10 = (i & 1) != 0 ? meetingEntity.id : str;
        String str11 = (i & 2) != 0 ? meetingEntity.title : str2;
        long j6 = (i & 4) != 0 ? meetingEntity.startedAtMs : j;
        long j7 = (i & 8) != 0 ? meetingEntity.endedAtMs : j2;
        String str12 = (i & 16) != 0 ? meetingEntity.status : str3;
        String str13 = (i & 32) != 0 ? meetingEntity.cleanedTranscript : str4;
        String str14 = (i & 64) != 0 ? meetingEntity.metadataJson : str5;
        String str15 = (i & 128) != 0 ? meetingEntity.cleanupProvider : str6;
        long j8 = (i & 256) != 0 ? meetingEntity.createdAtMs : j3;
        String str16 = (i & 512) != 0 ? meetingEntity.cleanTextOnly : str7;
        String str17 = (i & 1024) != 0 ? meetingEntity.lastError : str8;
        if ((i & 2048) != 0) {
            str9 = str10;
            j5 = meetingEntity.updatedAtMs;
        } else {
            j5 = j4;
            str9 = str10;
        }
        return meetingEntity.copy(str9, str11, j6, j7, str12, str13, str14, str15, j8, str16, str17, j5);
    }

    /* JADX INFO: renamed from: component1, reason: from getter */
    public final String getId() {
        return this.id;
    }

    /* JADX INFO: renamed from: component10, reason: from getter */
    public final String getCleanTextOnly() {
        return this.cleanTextOnly;
    }

    /* JADX INFO: renamed from: component11, reason: from getter */
    public final String getLastError() {
        return this.lastError;
    }

    /* JADX INFO: renamed from: component12, reason: from getter */
    public final long getUpdatedAtMs() {
        return this.updatedAtMs;
    }

    /* JADX INFO: renamed from: component2, reason: from getter */
    public final String getTitle() {
        return this.title;
    }

    /* JADX INFO: renamed from: component3, reason: from getter */
    public final long getStartedAtMs() {
        return this.startedAtMs;
    }

    /* JADX INFO: renamed from: component4, reason: from getter */
    public final long getEndedAtMs() {
        return this.endedAtMs;
    }

    /* JADX INFO: renamed from: component5, reason: from getter */
    public final String getStatus() {
        return this.status;
    }

    /* JADX INFO: renamed from: component6, reason: from getter */
    public final String getCleanedTranscript() {
        return this.cleanedTranscript;
    }

    /* JADX INFO: renamed from: component7, reason: from getter */
    public final String getMetadataJson() {
        return this.metadataJson;
    }

    /* JADX INFO: renamed from: component8, reason: from getter */
    public final String getCleanupProvider() {
        return this.cleanupProvider;
    }

    /* JADX INFO: renamed from: component9, reason: from getter */
    public final long getCreatedAtMs() {
        return this.createdAtMs;
    }

    public final MeetingEntity copy(String id, String title, long startedAtMs, long endedAtMs, String status, String cleanedTranscript, String metadataJson, String cleanupProvider, long createdAtMs, String cleanTextOnly, String lastError, long updatedAtMs) {
        Intrinsics.checkNotNullParameter(id, "id");
        Intrinsics.checkNotNullParameter(status, "status");
        return new MeetingEntity(id, title, startedAtMs, endedAtMs, status, cleanedTranscript, metadataJson, cleanupProvider, createdAtMs, cleanTextOnly, lastError, updatedAtMs);
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof MeetingEntity)) {
            return false;
        }
        MeetingEntity meetingEntity = (MeetingEntity) other;
        return Intrinsics.areEqual(this.id, meetingEntity.id) && Intrinsics.areEqual(this.title, meetingEntity.title) && this.startedAtMs == meetingEntity.startedAtMs && this.endedAtMs == meetingEntity.endedAtMs && Intrinsics.areEqual(this.status, meetingEntity.status) && Intrinsics.areEqual(this.cleanedTranscript, meetingEntity.cleanedTranscript) && Intrinsics.areEqual(this.metadataJson, meetingEntity.metadataJson) && Intrinsics.areEqual(this.cleanupProvider, meetingEntity.cleanupProvider) && this.createdAtMs == meetingEntity.createdAtMs && Intrinsics.areEqual(this.cleanTextOnly, meetingEntity.cleanTextOnly) && Intrinsics.areEqual(this.lastError, meetingEntity.lastError) && this.updatedAtMs == meetingEntity.updatedAtMs;
    }

    public int hashCode() {
        return (((((((((((((((((((((this.id.hashCode() * 31) + (this.title == null ? 0 : this.title.hashCode())) * 31) + Long.hashCode(this.startedAtMs)) * 31) + Long.hashCode(this.endedAtMs)) * 31) + this.status.hashCode()) * 31) + (this.cleanedTranscript == null ? 0 : this.cleanedTranscript.hashCode())) * 31) + (this.metadataJson == null ? 0 : this.metadataJson.hashCode())) * 31) + (this.cleanupProvider == null ? 0 : this.cleanupProvider.hashCode())) * 31) + Long.hashCode(this.createdAtMs)) * 31) + (this.cleanTextOnly == null ? 0 : this.cleanTextOnly.hashCode())) * 31) + (this.lastError != null ? this.lastError.hashCode() : 0)) * 31) + Long.hashCode(this.updatedAtMs);
    }

    public String toString() {
        return "MeetingEntity(id=" + this.id + ", title=" + this.title + ", startedAtMs=" + this.startedAtMs + ", endedAtMs=" + this.endedAtMs + ", status=" + this.status + ", cleanedTranscript=" + this.cleanedTranscript + ", metadataJson=" + this.metadataJson + ", cleanupProvider=" + this.cleanupProvider + ", createdAtMs=" + this.createdAtMs + ", cleanTextOnly=" + this.cleanTextOnly + ", lastError=" + this.lastError + ", updatedAtMs=" + this.updatedAtMs + ")";
    }

    public MeetingEntity(String id, String title, long startedAtMs, long endedAtMs, String status, String cleanedTranscript, String metadataJson, String cleanupProvider, long createdAtMs, String cleanTextOnly, String lastError, long updatedAtMs) {
        Intrinsics.checkNotNullParameter(id, "id");
        Intrinsics.checkNotNullParameter(status, "status");
        this.id = id;
        this.title = title;
        this.startedAtMs = startedAtMs;
        this.endedAtMs = endedAtMs;
        this.status = status;
        this.cleanedTranscript = cleanedTranscript;
        this.metadataJson = metadataJson;
        this.cleanupProvider = cleanupProvider;
        this.createdAtMs = createdAtMs;
        this.cleanTextOnly = cleanTextOnly;
        this.lastError = lastError;
        this.updatedAtMs = updatedAtMs;
    }

    public /* synthetic */ MeetingEntity(String str, String str2, long j, long j2, String str3, String str4, String str5, String str6, long j3, String str7, String str8, long j4, int i, DefaultConstructorMarker defaultConstructorMarker) {
        this(str, (i & 2) != 0 ? null : str2, j, j2, (i & 16) != 0 ? "PENDING_CLEANUP" : str3, (i & 32) != 0 ? null : str4, (i & 64) != 0 ? null : str5, (i & 128) != 0 ? null : str6, (i & 256) != 0 ? System.currentTimeMillis() : j3, (i & 512) != 0 ? null : str7, (i & 1024) != 0 ? null : str8, (i & 2048) != 0 ? 0L : j4);
    }

    public final String getId() {
        return this.id;
    }

    public final String getTitle() {
        return this.title;
    }

    public final long getStartedAtMs() {
        return this.startedAtMs;
    }

    public final long getEndedAtMs() {
        return this.endedAtMs;
    }

    public final String getStatus() {
        return this.status;
    }

    public final String getCleanedTranscript() {
        return this.cleanedTranscript;
    }

    public final String getMetadataJson() {
        return this.metadataJson;
    }

    public final String getCleanupProvider() {
        return this.cleanupProvider;
    }

    public final long getCreatedAtMs() {
        return this.createdAtMs;
    }

    public final String getCleanTextOnly() {
        return this.cleanTextOnly;
    }

    public final String getLastError() {
        return this.lastError;
    }

    public final long getUpdatedAtMs() {
        return this.updatedAtMs;
    }
}
