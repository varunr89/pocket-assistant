package com.varun.pocketassistant.data;

import com.varun.pocketassistant.pipeline.work.MeetingStageWorker;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: AppDatabase.kt */
/* JADX INFO: loaded from: classes9.dex */
@Metadata(d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\t\n\u0002\b\u000f\n\u0002\u0010\u000b\n\u0002\b2\n\u0002\u0010\b\n\u0002\b\u0002\b\u0087\b\u0018\u00002\u00020\u0001BÕ\u0001\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0003\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\u0007\u0012\u0006\u0010\t\u001a\u00020\u0007\u0012\u0006\u0010\n\u001a\u00020\u0007\u0012\b\b\u0002\u0010\u000b\u001a\u00020\u0003\u0012\n\b\u0002\u0010\f\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\r\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u000e\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u000f\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0010\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0011\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0012\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0013\u001a\u0004\u0018\u00010\u0003\u0012\b\b\u0002\u0010\u0014\u001a\u00020\u0007\u0012\n\b\u0002\u0010\u0015\u001a\u0004\u0018\u00010\u0003\u0012\b\b\u0002\u0010\u0016\u001a\u00020\u0017\u0012\n\b\u0002\u0010\u0018\u001a\u0004\u0018\u00010\u0003¢\u0006\u0004\b\u0019\u0010\u001aJ\t\u00102\u001a\u00020\u0003HÆ\u0003J\t\u00103\u001a\u00020\u0003HÆ\u0003J\t\u00104\u001a\u00020\u0003HÆ\u0003J\t\u00105\u001a\u00020\u0007HÆ\u0003J\t\u00106\u001a\u00020\u0007HÆ\u0003J\t\u00107\u001a\u00020\u0007HÆ\u0003J\t\u00108\u001a\u00020\u0007HÆ\u0003J\t\u00109\u001a\u00020\u0003HÆ\u0003J\u000b\u0010:\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\u000b\u0010;\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\u000b\u0010<\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\u000b\u0010=\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\u000b\u0010>\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\u000b\u0010?\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\u000b\u0010@\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\u000b\u0010A\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\t\u0010B\u001a\u00020\u0007HÆ\u0003J\u000b\u0010C\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\t\u0010D\u001a\u00020\u0017HÆ\u0003J\u000b\u0010E\u001a\u0004\u0018\u00010\u0003HÆ\u0003Jå\u0001\u0010F\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00032\b\b\u0002\u0010\u0006\u001a\u00020\u00072\b\b\u0002\u0010\b\u001a\u00020\u00072\b\b\u0002\u0010\t\u001a\u00020\u00072\b\b\u0002\u0010\n\u001a\u00020\u00072\b\b\u0002\u0010\u000b\u001a\u00020\u00032\n\b\u0002\u0010\f\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\r\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u000e\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u000f\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0010\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0011\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0012\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0013\u001a\u0004\u0018\u00010\u00032\b\b\u0002\u0010\u0014\u001a\u00020\u00072\n\b\u0002\u0010\u0015\u001a\u0004\u0018\u00010\u00032\b\b\u0002\u0010\u0016\u001a\u00020\u00172\n\b\u0002\u0010\u0018\u001a\u0004\u0018\u00010\u0003HÆ\u0001J\u0013\u0010G\u001a\u00020\u00172\b\u0010H\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010I\u001a\u00020JHÖ\u0001J\t\u0010K\u001a\u00020\u0003HÖ\u0001R\u0016\u0010\u0002\u001a\u00020\u00038\u0006X\u0087\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u001b\u0010\u001cR\u0011\u0010\u0004\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u001d\u0010\u001cR\u0011\u0010\u0005\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u001e\u0010\u001cR\u0011\u0010\u0006\u001a\u00020\u0007¢\u0006\b\n\u0000\u001a\u0004\b\u001f\u0010 R\u0011\u0010\b\u001a\u00020\u0007¢\u0006\b\n\u0000\u001a\u0004\b!\u0010 R\u0011\u0010\t\u001a\u00020\u0007¢\u0006\b\n\u0000\u001a\u0004\b\"\u0010 R\u0011\u0010\n\u001a\u00020\u0007¢\u0006\b\n\u0000\u001a\u0004\b#\u0010 R\u0011\u0010\u000b\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b$\u0010\u001cR\u0013\u0010\f\u001a\u0004\u0018\u00010\u0003¢\u0006\b\n\u0000\u001a\u0004\b%\u0010\u001cR\u0013\u0010\r\u001a\u0004\u0018\u00010\u0003¢\u0006\b\n\u0000\u001a\u0004\b&\u0010\u001cR\u0013\u0010\u000e\u001a\u0004\u0018\u00010\u0003¢\u0006\b\n\u0000\u001a\u0004\b'\u0010\u001cR\u0013\u0010\u000f\u001a\u0004\u0018\u00010\u0003¢\u0006\b\n\u0000\u001a\u0004\b(\u0010\u001cR\u0013\u0010\u0010\u001a\u0004\u0018\u00010\u0003¢\u0006\b\n\u0000\u001a\u0004\b)\u0010\u001cR\u0013\u0010\u0011\u001a\u0004\u0018\u00010\u0003¢\u0006\b\n\u0000\u001a\u0004\b*\u0010\u001cR\u0013\u0010\u0012\u001a\u0004\u0018\u00010\u0003¢\u0006\b\n\u0000\u001a\u0004\b+\u0010\u001cR\u0013\u0010\u0013\u001a\u0004\u0018\u00010\u0003¢\u0006\b\n\u0000\u001a\u0004\b,\u0010\u001cR\u0011\u0010\u0014\u001a\u00020\u0007¢\u0006\b\n\u0000\u001a\u0004\b-\u0010 R\u0013\u0010\u0015\u001a\u0004\u0018\u00010\u0003¢\u0006\b\n\u0000\u001a\u0004\b.\u0010\u001cR\u0011\u0010\u0016\u001a\u00020\u0017¢\u0006\b\n\u0000\u001a\u0004\b/\u00100R\u0013\u0010\u0018\u001a\u0004\u0018\u00010\u0003¢\u0006\b\n\u0000\u001a\u0004\b1\u0010\u001c¨\u0006L"}, d2 = {"Lcom/varun/pocketassistant/data/SegmentEntity;", "", "id", "", "sessionId", "filePath", "startedAtMs", "", "endedAtMs", "durationMs", "byteSize", "transcriptStatus", "transcript", "diarizedTranscript", "cleanedTranscript", "asrProvider", "cleanupProvider", MeetingStageWorker.KEY_MEETING_ID, "asrLastError", "skipReason", "updatedAtMs", "speakerCandidatesJson", "youConfirmed", "", "endReason", "<init>", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;JJJJLjava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;JLjava/lang/String;ZLjava/lang/String;)V", "getId", "()Ljava/lang/String;", "getSessionId", "getFilePath", "getStartedAtMs", "()J", "getEndedAtMs", "getDurationMs", "getByteSize", "getTranscriptStatus", "getTranscript", "getDiarizedTranscript", "getCleanedTranscript", "getAsrProvider", "getCleanupProvider", "getMeetingId", "getAsrLastError", "getSkipReason", "getUpdatedAtMs", "getSpeakerCandidatesJson", "getYouConfirmed", "()Z", "getEndReason", "component1", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "component10", "component11", "component12", "component13", "component14", "component15", "component16", "component17", "component18", "component19", "component20", "copy", "equals", "other", "hashCode", "", "toString", "app_debug"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final /* data */ class SegmentEntity {
    public static final int $stable = 0;
    private final String asrLastError;
    private final String asrProvider;
    private final long byteSize;
    private final String cleanedTranscript;
    private final String cleanupProvider;
    private final String diarizedTranscript;
    private final long durationMs;
    private final String endReason;
    private final long endedAtMs;
    private final String filePath;
    private final String id;
    private final String meetingId;
    private final String sessionId;
    private final String skipReason;
    private final String speakerCandidatesJson;
    private final long startedAtMs;
    private final String transcript;
    private final String transcriptStatus;
    private final long updatedAtMs;
    private final boolean youConfirmed;

    public static /* synthetic */ SegmentEntity copy$default(SegmentEntity segmentEntity, String str, String str2, String str3, long j, long j2, long j3, long j4, String str4, String str5, String str6, String str7, String str8, String str9, String str10, String str11, String str12, long j5, String str13, boolean z, String str14, int i, Object obj) {
        String str15;
        String str16;
        String str17 = (i & 1) != 0 ? segmentEntity.id : str;
        String str18 = (i & 2) != 0 ? segmentEntity.sessionId : str2;
        String str19 = (i & 4) != 0 ? segmentEntity.filePath : str3;
        long j6 = (i & 8) != 0 ? segmentEntity.startedAtMs : j;
        long j7 = (i & 16) != 0 ? segmentEntity.endedAtMs : j2;
        long j8 = (i & 32) != 0 ? segmentEntity.durationMs : j3;
        long j9 = (i & 64) != 0 ? segmentEntity.byteSize : j4;
        String str20 = (i & 128) != 0 ? segmentEntity.transcriptStatus : str4;
        String str21 = (i & 256) != 0 ? segmentEntity.transcript : str5;
        String str22 = (i & 512) != 0 ? segmentEntity.diarizedTranscript : str6;
        String str23 = str17;
        String str24 = (i & 1024) != 0 ? segmentEntity.cleanedTranscript : str7;
        String str25 = (i & 2048) != 0 ? segmentEntity.asrProvider : str8;
        String str26 = (i & 4096) != 0 ? segmentEntity.cleanupProvider : str9;
        String str27 = (i & 8192) != 0 ? segmentEntity.meetingId : str10;
        String str28 = (i & 16384) != 0 ? segmentEntity.asrLastError : str11;
        String str29 = (i & 32768) != 0 ? segmentEntity.skipReason : str12;
        String str30 = str28;
        long j10 = (i & 65536) != 0 ? segmentEntity.updatedAtMs : j5;
        String str31 = (i & 131072) != 0 ? segmentEntity.speakerCandidatesJson : str13;
        boolean z2 = (i & 262144) != 0 ? segmentEntity.youConfirmed : z;
        if ((i & 524288) != 0) {
            str16 = str31;
            str15 = segmentEntity.endReason;
        } else {
            str15 = str14;
            str16 = str31;
        }
        return segmentEntity.copy(str23, str18, str19, j6, j7, j8, j9, str20, str21, str22, str24, str25, str26, str27, str30, str29, j10, str16, z2, str15);
    }

    /* JADX INFO: renamed from: component1, reason: from getter */
    public final String getId() {
        return this.id;
    }

    /* JADX INFO: renamed from: component10, reason: from getter */
    public final String getDiarizedTranscript() {
        return this.diarizedTranscript;
    }

    /* JADX INFO: renamed from: component11, reason: from getter */
    public final String getCleanedTranscript() {
        return this.cleanedTranscript;
    }

    /* JADX INFO: renamed from: component12, reason: from getter */
    public final String getAsrProvider() {
        return this.asrProvider;
    }

    /* JADX INFO: renamed from: component13, reason: from getter */
    public final String getCleanupProvider() {
        return this.cleanupProvider;
    }

    /* JADX INFO: renamed from: component14, reason: from getter */
    public final String getMeetingId() {
        return this.meetingId;
    }

    /* JADX INFO: renamed from: component15, reason: from getter */
    public final String getAsrLastError() {
        return this.asrLastError;
    }

    /* JADX INFO: renamed from: component16, reason: from getter */
    public final String getSkipReason() {
        return this.skipReason;
    }

    /* JADX INFO: renamed from: component17, reason: from getter */
    public final long getUpdatedAtMs() {
        return this.updatedAtMs;
    }

    /* JADX INFO: renamed from: component18, reason: from getter */
    public final String getSpeakerCandidatesJson() {
        return this.speakerCandidatesJson;
    }

    /* JADX INFO: renamed from: component19, reason: from getter */
    public final boolean getYouConfirmed() {
        return this.youConfirmed;
    }

    /* JADX INFO: renamed from: component2, reason: from getter */
    public final String getSessionId() {
        return this.sessionId;
    }

    /* JADX INFO: renamed from: component20, reason: from getter */
    public final String getEndReason() {
        return this.endReason;
    }

    /* JADX INFO: renamed from: component3, reason: from getter */
    public final String getFilePath() {
        return this.filePath;
    }

    /* JADX INFO: renamed from: component4, reason: from getter */
    public final long getStartedAtMs() {
        return this.startedAtMs;
    }

    /* JADX INFO: renamed from: component5, reason: from getter */
    public final long getEndedAtMs() {
        return this.endedAtMs;
    }

    /* JADX INFO: renamed from: component6, reason: from getter */
    public final long getDurationMs() {
        return this.durationMs;
    }

    /* JADX INFO: renamed from: component7, reason: from getter */
    public final long getByteSize() {
        return this.byteSize;
    }

    /* JADX INFO: renamed from: component8, reason: from getter */
    public final String getTranscriptStatus() {
        return this.transcriptStatus;
    }

    /* JADX INFO: renamed from: component9, reason: from getter */
    public final String getTranscript() {
        return this.transcript;
    }

    public final SegmentEntity copy(String id, String sessionId, String filePath, long startedAtMs, long endedAtMs, long durationMs, long byteSize, String transcriptStatus, String transcript, String diarizedTranscript, String cleanedTranscript, String asrProvider, String cleanupProvider, String meetingId, String asrLastError, String skipReason, long updatedAtMs, String speakerCandidatesJson, boolean youConfirmed, String endReason) {
        Intrinsics.checkNotNullParameter(id, "id");
        Intrinsics.checkNotNullParameter(sessionId, "sessionId");
        Intrinsics.checkNotNullParameter(filePath, "filePath");
        Intrinsics.checkNotNullParameter(transcriptStatus, "transcriptStatus");
        return new SegmentEntity(id, sessionId, filePath, startedAtMs, endedAtMs, durationMs, byteSize, transcriptStatus, transcript, diarizedTranscript, cleanedTranscript, asrProvider, cleanupProvider, meetingId, asrLastError, skipReason, updatedAtMs, speakerCandidatesJson, youConfirmed, endReason);
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof SegmentEntity)) {
            return false;
        }
        SegmentEntity segmentEntity = (SegmentEntity) other;
        return Intrinsics.areEqual(this.id, segmentEntity.id) && Intrinsics.areEqual(this.sessionId, segmentEntity.sessionId) && Intrinsics.areEqual(this.filePath, segmentEntity.filePath) && this.startedAtMs == segmentEntity.startedAtMs && this.endedAtMs == segmentEntity.endedAtMs && this.durationMs == segmentEntity.durationMs && this.byteSize == segmentEntity.byteSize && Intrinsics.areEqual(this.transcriptStatus, segmentEntity.transcriptStatus) && Intrinsics.areEqual(this.transcript, segmentEntity.transcript) && Intrinsics.areEqual(this.diarizedTranscript, segmentEntity.diarizedTranscript) && Intrinsics.areEqual(this.cleanedTranscript, segmentEntity.cleanedTranscript) && Intrinsics.areEqual(this.asrProvider, segmentEntity.asrProvider) && Intrinsics.areEqual(this.cleanupProvider, segmentEntity.cleanupProvider) && Intrinsics.areEqual(this.meetingId, segmentEntity.meetingId) && Intrinsics.areEqual(this.asrLastError, segmentEntity.asrLastError) && Intrinsics.areEqual(this.skipReason, segmentEntity.skipReason) && this.updatedAtMs == segmentEntity.updatedAtMs && Intrinsics.areEqual(this.speakerCandidatesJson, segmentEntity.speakerCandidatesJson) && this.youConfirmed == segmentEntity.youConfirmed && Intrinsics.areEqual(this.endReason, segmentEntity.endReason);
    }

    public int hashCode() {
        return (((((((((((((((((((((((((((((((((((((this.id.hashCode() * 31) + this.sessionId.hashCode()) * 31) + this.filePath.hashCode()) * 31) + Long.hashCode(this.startedAtMs)) * 31) + Long.hashCode(this.endedAtMs)) * 31) + Long.hashCode(this.durationMs)) * 31) + Long.hashCode(this.byteSize)) * 31) + this.transcriptStatus.hashCode()) * 31) + (this.transcript == null ? 0 : this.transcript.hashCode())) * 31) + (this.diarizedTranscript == null ? 0 : this.diarizedTranscript.hashCode())) * 31) + (this.cleanedTranscript == null ? 0 : this.cleanedTranscript.hashCode())) * 31) + (this.asrProvider == null ? 0 : this.asrProvider.hashCode())) * 31) + (this.cleanupProvider == null ? 0 : this.cleanupProvider.hashCode())) * 31) + (this.meetingId == null ? 0 : this.meetingId.hashCode())) * 31) + (this.asrLastError == null ? 0 : this.asrLastError.hashCode())) * 31) + (this.skipReason == null ? 0 : this.skipReason.hashCode())) * 31) + Long.hashCode(this.updatedAtMs)) * 31) + (this.speakerCandidatesJson == null ? 0 : this.speakerCandidatesJson.hashCode())) * 31) + Boolean.hashCode(this.youConfirmed)) * 31) + (this.endReason != null ? this.endReason.hashCode() : 0);
    }

    public String toString() {
        return "SegmentEntity(id=" + this.id + ", sessionId=" + this.sessionId + ", filePath=" + this.filePath + ", startedAtMs=" + this.startedAtMs + ", endedAtMs=" + this.endedAtMs + ", durationMs=" + this.durationMs + ", byteSize=" + this.byteSize + ", transcriptStatus=" + this.transcriptStatus + ", transcript=" + this.transcript + ", diarizedTranscript=" + this.diarizedTranscript + ", cleanedTranscript=" + this.cleanedTranscript + ", asrProvider=" + this.asrProvider + ", cleanupProvider=" + this.cleanupProvider + ", meetingId=" + this.meetingId + ", asrLastError=" + this.asrLastError + ", skipReason=" + this.skipReason + ", updatedAtMs=" + this.updatedAtMs + ", speakerCandidatesJson=" + this.speakerCandidatesJson + ", youConfirmed=" + this.youConfirmed + ", endReason=" + this.endReason + ")";
    }

    public SegmentEntity(String id, String sessionId, String filePath, long startedAtMs, long endedAtMs, long durationMs, long byteSize, String transcriptStatus, String transcript, String diarizedTranscript, String cleanedTranscript, String asrProvider, String cleanupProvider, String meetingId, String asrLastError, String skipReason, long updatedAtMs, String speakerCandidatesJson, boolean youConfirmed, String endReason) {
        Intrinsics.checkNotNullParameter(id, "id");
        Intrinsics.checkNotNullParameter(sessionId, "sessionId");
        Intrinsics.checkNotNullParameter(filePath, "filePath");
        Intrinsics.checkNotNullParameter(transcriptStatus, "transcriptStatus");
        this.id = id;
        this.sessionId = sessionId;
        this.filePath = filePath;
        this.startedAtMs = startedAtMs;
        this.endedAtMs = endedAtMs;
        this.durationMs = durationMs;
        this.byteSize = byteSize;
        this.transcriptStatus = transcriptStatus;
        this.transcript = transcript;
        this.diarizedTranscript = diarizedTranscript;
        this.cleanedTranscript = cleanedTranscript;
        this.asrProvider = asrProvider;
        this.cleanupProvider = cleanupProvider;
        this.meetingId = meetingId;
        this.asrLastError = asrLastError;
        this.skipReason = skipReason;
        this.updatedAtMs = updatedAtMs;
        this.speakerCandidatesJson = speakerCandidatesJson;
        this.youConfirmed = youConfirmed;
        this.endReason = endReason;
    }

    public /* synthetic */ SegmentEntity(String str, String str2, String str3, long j, long j2, long j3, long j4, String str4, String str5, String str6, String str7, String str8, String str9, String str10, String str11, String str12, long j5, String str13, boolean z, String str14, int i, DefaultConstructorMarker defaultConstructorMarker) {
        this(str, str2, str3, j, j2, j3, j4, (i & 128) != 0 ? "PENDING" : str4, (i & 256) != 0 ? null : str5, (i & 512) != 0 ? null : str6, (i & 1024) != 0 ? null : str7, (i & 2048) != 0 ? null : str8, (i & 4096) != 0 ? null : str9, (i & 8192) != 0 ? null : str10, (i & 16384) != 0 ? null : str11, (32768 & i) != 0 ? null : str12, (65536 & i) != 0 ? 0L : j5, (131072 & i) != 0 ? null : str13, (262144 & i) != 0 ? false : z, (i & 524288) != 0 ? null : str14);
    }

    public final String getId() {
        return this.id;
    }

    public final String getSessionId() {
        return this.sessionId;
    }

    public final String getFilePath() {
        return this.filePath;
    }

    public final long getStartedAtMs() {
        return this.startedAtMs;
    }

    public final long getEndedAtMs() {
        return this.endedAtMs;
    }

    public final long getDurationMs() {
        return this.durationMs;
    }

    public final long getByteSize() {
        return this.byteSize;
    }

    public final String getTranscriptStatus() {
        return this.transcriptStatus;
    }

    public final String getTranscript() {
        return this.transcript;
    }

    public final String getDiarizedTranscript() {
        return this.diarizedTranscript;
    }

    public final String getCleanedTranscript() {
        return this.cleanedTranscript;
    }

    public final String getAsrProvider() {
        return this.asrProvider;
    }

    public final String getCleanupProvider() {
        return this.cleanupProvider;
    }

    public final String getMeetingId() {
        return this.meetingId;
    }

    public final String getAsrLastError() {
        return this.asrLastError;
    }

    public final String getSkipReason() {
        return this.skipReason;
    }

    public final long getUpdatedAtMs() {
        return this.updatedAtMs;
    }

    public final String getSpeakerCandidatesJson() {
        return this.speakerCandidatesJson;
    }

    public final boolean getYouConfirmed() {
        return this.youConfirmed;
    }

    public final String getEndReason() {
        return this.endReason;
    }
}
