package com.varun.pocketassistant.capture;

import java.util.List;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.DebugKt;

/* JADX INFO: compiled from: CaptureTelemetry.kt */
/* JADX INFO: loaded from: classes5.dex */
@Metadata(d1 = {"\u0000J\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0007\n\u0002\b\u0004\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u000e\n\u0002\u0018\u0002\n\u0002\bE\b\u0087\b\u0018\u00002\u00020\u0001B\u0093\u0002\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0005\u0012\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u0007\u0012\b\b\u0002\u0010\b\u001a\u00020\t\u0012\b\b\u0002\u0010\n\u001a\u00020\u000b\u0012\b\b\u0002\u0010\f\u001a\u00020\r\u0012\b\b\u0002\u0010\u000e\u001a\u00020\r\u0012\b\b\u0002\u0010\u000f\u001a\u00020\r\u0012\b\b\u0002\u0010\u0010\u001a\u00020\r\u0012\u000e\b\u0002\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00130\u0012\u0012\b\b\u0002\u0010\u0014\u001a\u00020\u0015\u0012\b\b\u0002\u0010\u0016\u001a\u00020\u0015\u0012\b\b\u0002\u0010\u0017\u001a\u00020\u0015\u0012\b\b\u0002\u0010\u0018\u001a\u00020\u0015\u0012\b\b\u0002\u0010\u0019\u001a\u00020\u0015\u0012\b\b\u0002\u0010\u001a\u001a\u00020\u0015\u0012\b\b\u0002\u0010\u001b\u001a\u00020\u0015\u0012\b\b\u0002\u0010\u001c\u001a\u00020\u0015\u0012\b\b\u0002\u0010\u001d\u001a\u00020\u0015\u0012\n\b\u0002\u0010\u001e\u001a\u0004\u0018\u00010\u0015\u0012\b\b\u0002\u0010\u001f\u001a\u00020\u0007\u0012\b\b\u0002\u0010 \u001a\u00020\u0007\u0012\b\b\u0002\u0010!\u001a\u00020\u0007\u0012\n\b\u0002\u0010\"\u001a\u0004\u0018\u00010\u0007\u0012\u000e\b\u0002\u0010#\u001a\b\u0012\u0004\u0012\u00020$0\u0012¢\u0006\u0004\b%\u0010&J\t\u0010J\u001a\u00020\u0003HÆ\u0003J\t\u0010K\u001a\u00020\u0005HÆ\u0003J\u000b\u0010L\u001a\u0004\u0018\u00010\u0007HÆ\u0003J\t\u0010M\u001a\u00020\tHÆ\u0003J\t\u0010N\u001a\u00020\u000bHÆ\u0003J\t\u0010O\u001a\u00020\rHÆ\u0003J\t\u0010P\u001a\u00020\rHÆ\u0003J\t\u0010Q\u001a\u00020\rHÆ\u0003J\t\u0010R\u001a\u00020\rHÆ\u0003J\u000f\u0010S\u001a\b\u0012\u0004\u0012\u00020\u00130\u0012HÆ\u0003J\t\u0010T\u001a\u00020\u0015HÆ\u0003J\t\u0010U\u001a\u00020\u0015HÆ\u0003J\t\u0010V\u001a\u00020\u0015HÆ\u0003J\t\u0010W\u001a\u00020\u0015HÆ\u0003J\t\u0010X\u001a\u00020\u0015HÆ\u0003J\t\u0010Y\u001a\u00020\u0015HÆ\u0003J\t\u0010Z\u001a\u00020\u0015HÆ\u0003J\t\u0010[\u001a\u00020\u0015HÆ\u0003J\t\u0010\\\u001a\u00020\u0015HÆ\u0003J\u0010\u0010]\u001a\u0004\u0018\u00010\u0015HÆ\u0003¢\u0006\u0002\u0010CJ\t\u0010^\u001a\u00020\u0007HÆ\u0003J\t\u0010_\u001a\u00020\u0007HÆ\u0003J\t\u0010`\u001a\u00020\u0007HÆ\u0003J\u000b\u0010a\u001a\u0004\u0018\u00010\u0007HÆ\u0003J\u000f\u0010b\u001a\b\u0012\u0004\u0012\u00020$0\u0012HÆ\u0003J\u009a\u0002\u0010c\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u00072\b\b\u0002\u0010\b\u001a\u00020\t2\b\b\u0002\u0010\n\u001a\u00020\u000b2\b\b\u0002\u0010\f\u001a\u00020\r2\b\b\u0002\u0010\u000e\u001a\u00020\r2\b\b\u0002\u0010\u000f\u001a\u00020\r2\b\b\u0002\u0010\u0010\u001a\u00020\r2\u000e\b\u0002\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00130\u00122\b\b\u0002\u0010\u0014\u001a\u00020\u00152\b\b\u0002\u0010\u0016\u001a\u00020\u00152\b\b\u0002\u0010\u0017\u001a\u00020\u00152\b\b\u0002\u0010\u0018\u001a\u00020\u00152\b\b\u0002\u0010\u0019\u001a\u00020\u00152\b\b\u0002\u0010\u001a\u001a\u00020\u00152\b\b\u0002\u0010\u001b\u001a\u00020\u00152\b\b\u0002\u0010\u001c\u001a\u00020\u00152\b\b\u0002\u0010\u001d\u001a\u00020\u00152\n\b\u0002\u0010\u001e\u001a\u0004\u0018\u00010\u00152\b\b\u0002\u0010\u001f\u001a\u00020\u00072\b\b\u0002\u0010 \u001a\u00020\u00072\b\b\u0002\u0010!\u001a\u00020\u00072\n\b\u0002\u0010\"\u001a\u0004\u0018\u00010\u00072\u000e\b\u0002\u0010#\u001a\b\u0012\u0004\u0012\u00020$0\u0012HÆ\u0001¢\u0006\u0002\u0010dJ\u0013\u0010e\u001a\u00020\u000b2\b\u0010f\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010g\u001a\u00020\tHÖ\u0001J\t\u0010h\u001a\u00020\u0007HÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b'\u0010(R\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b)\u0010*R\u0013\u0010\u0006\u001a\u0004\u0018\u00010\u0007¢\u0006\b\n\u0000\u001a\u0004\b+\u0010,R\u0011\u0010\b\u001a\u00020\t¢\u0006\b\n\u0000\u001a\u0004\b-\u0010.R\u0011\u0010\n\u001a\u00020\u000b¢\u0006\b\n\u0000\u001a\u0004\b/\u00100R\u0011\u0010\f\u001a\u00020\r¢\u0006\b\n\u0000\u001a\u0004\b1\u00102R\u0011\u0010\u000e\u001a\u00020\r¢\u0006\b\n\u0000\u001a\u0004\b3\u00102R\u0011\u0010\u000f\u001a\u00020\r¢\u0006\b\n\u0000\u001a\u0004\b4\u00102R\u0011\u0010\u0010\u001a\u00020\r¢\u0006\b\n\u0000\u001a\u0004\b5\u00102R\u0017\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00130\u0012¢\u0006\b\n\u0000\u001a\u0004\b6\u00107R\u0011\u0010\u0014\u001a\u00020\u0015¢\u0006\b\n\u0000\u001a\u0004\b8\u00109R\u0011\u0010\u0016\u001a\u00020\u0015¢\u0006\b\n\u0000\u001a\u0004\b:\u00109R\u0011\u0010\u0017\u001a\u00020\u0015¢\u0006\b\n\u0000\u001a\u0004\b;\u00109R\u0011\u0010\u0018\u001a\u00020\u0015¢\u0006\b\n\u0000\u001a\u0004\b<\u00109R\u0011\u0010\u0019\u001a\u00020\u0015¢\u0006\b\n\u0000\u001a\u0004\b=\u00109R\u0011\u0010\u001a\u001a\u00020\u0015¢\u0006\b\n\u0000\u001a\u0004\b>\u00109R\u0011\u0010\u001b\u001a\u00020\u0015¢\u0006\b\n\u0000\u001a\u0004\b?\u00109R\u0011\u0010\u001c\u001a\u00020\u0015¢\u0006\b\n\u0000\u001a\u0004\b@\u00109R\u0011\u0010\u001d\u001a\u00020\u0015¢\u0006\b\n\u0000\u001a\u0004\bA\u00109R\u0015\u0010\u001e\u001a\u0004\u0018\u00010\u0015¢\u0006\n\n\u0002\u0010D\u001a\u0004\bB\u0010CR\u0011\u0010\u001f\u001a\u00020\u0007¢\u0006\b\n\u0000\u001a\u0004\bE\u0010,R\u0011\u0010 \u001a\u00020\u0007¢\u0006\b\n\u0000\u001a\u0004\bF\u0010,R\u0011\u0010!\u001a\u00020\u0007¢\u0006\b\n\u0000\u001a\u0004\bG\u0010,R\u0013\u0010\"\u001a\u0004\u0018\u00010\u0007¢\u0006\b\n\u0000\u001a\u0004\bH\u0010,R\u0017\u0010#\u001a\b\u0012\u0004\u0012\u00020$0\u0012¢\u0006\b\n\u0000\u001a\u0004\bI\u00107¨\u0006i"}, d2 = {"Lcom/varun/pocketassistant/capture/CaptureStats;", "", "state", "Lcom/varun/pocketassistant/capture/CaptureState;", "phase", "Lcom/varun/pocketassistant/capture/CapturePhase;", "sessionId", "", "segmentCount", "", "speechActive", "", "rms", "", "peak", "speechThreshold", "speechProbability", "rmsHistory", "", "Lcom/varun/pocketassistant/capture/RmsSample;", "preRollBufferedMs", "", "hangoverRemainingMs", "openGateProgressMs", "openGateRequiredMs", "currentSegmentBytes", "currentSegmentDurationMs", "totalBytesWritten", "framesRead", "uptimeMs", "lastSpeechAtMs", "liveTranscript", "partialTranscript", "captionsStatus", "lastError", "events", "Lcom/varun/pocketassistant/capture/CaptureEvent;", "<init>", "(Lcom/varun/pocketassistant/capture/CaptureState;Lcom/varun/pocketassistant/capture/CapturePhase;Ljava/lang/String;IZFFFFLjava/util/List;JJJJJJJJJLjava/lang/Long;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/util/List;)V", "getState", "()Lcom/varun/pocketassistant/capture/CaptureState;", "getPhase", "()Lcom/varun/pocketassistant/capture/CapturePhase;", "getSessionId", "()Ljava/lang/String;", "getSegmentCount", "()I", "getSpeechActive", "()Z", "getRms", "()F", "getPeak", "getSpeechThreshold", "getSpeechProbability", "getRmsHistory", "()Ljava/util/List;", "getPreRollBufferedMs", "()J", "getHangoverRemainingMs", "getOpenGateProgressMs", "getOpenGateRequiredMs", "getCurrentSegmentBytes", "getCurrentSegmentDurationMs", "getTotalBytesWritten", "getFramesRead", "getUptimeMs", "getLastSpeechAtMs", "()Ljava/lang/Long;", "Ljava/lang/Long;", "getLiveTranscript", "getPartialTranscript", "getCaptionsStatus", "getLastError", "getEvents", "component1", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "component10", "component11", "component12", "component13", "component14", "component15", "component16", "component17", "component18", "component19", "component20", "component21", "component22", "component23", "component24", "component25", "copy", "(Lcom/varun/pocketassistant/capture/CaptureState;Lcom/varun/pocketassistant/capture/CapturePhase;Ljava/lang/String;IZFFFFLjava/util/List;JJJJJJJJJLjava/lang/Long;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/util/List;)Lcom/varun/pocketassistant/capture/CaptureStats;", "equals", "other", "hashCode", "toString", "app_debug"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final /* data */ class CaptureStats {
    public static final int $stable = 8;
    private final String captionsStatus;
    private final long currentSegmentBytes;
    private final long currentSegmentDurationMs;
    private final List<CaptureEvent> events;
    private final long framesRead;
    private final long hangoverRemainingMs;
    private final String lastError;
    private final Long lastSpeechAtMs;
    private final String liveTranscript;
    private final long openGateProgressMs;
    private final long openGateRequiredMs;
    private final String partialTranscript;
    private final float peak;
    private final CapturePhase phase;
    private final long preRollBufferedMs;
    private final float rms;
    private final List<RmsSample> rmsHistory;
    private final int segmentCount;
    private final String sessionId;
    private final boolean speechActive;
    private final float speechProbability;
    private final float speechThreshold;
    private final CaptureState state;
    private final long totalBytesWritten;
    private final long uptimeMs;

    public CaptureStats() {
        this(null, null, null, 0, false, 0.0f, 0.0f, 0.0f, 0.0f, null, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, null, null, null, null, null, null, 33554431, null);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static /* synthetic */ CaptureStats copy$default(CaptureStats captureStats, CaptureState captureState, CapturePhase capturePhase, String str, int i, boolean z, float f, float f2, float f3, float f4, List list, long j, long j2, long j3, long j4, long j5, long j6, long j7, long j8, long j9, Long l, String str2, String str3, String str4, String str5, List list2, int i2, Object obj) {
        List list3;
        String str6;
        CaptureState captureState2 = (i2 & 1) != 0 ? captureStats.state : captureState;
        CapturePhase capturePhase2 = (i2 & 2) != 0 ? captureStats.phase : capturePhase;
        String str7 = (i2 & 4) != 0 ? captureStats.sessionId : str;
        int i3 = (i2 & 8) != 0 ? captureStats.segmentCount : i;
        boolean z2 = (i2 & 16) != 0 ? captureStats.speechActive : z;
        float f5 = (i2 & 32) != 0 ? captureStats.rms : f;
        float f6 = (i2 & 64) != 0 ? captureStats.peak : f2;
        float f7 = (i2 & 128) != 0 ? captureStats.speechThreshold : f3;
        float f8 = (i2 & 256) != 0 ? captureStats.speechProbability : f4;
        List list4 = (i2 & 512) != 0 ? captureStats.rmsHistory : list;
        long j10 = (i2 & 1024) != 0 ? captureStats.preRollBufferedMs : j;
        long j11 = (i2 & 2048) != 0 ? captureStats.hangoverRemainingMs : j2;
        CaptureState captureState3 = captureState2;
        CapturePhase capturePhase3 = capturePhase2;
        long j12 = (i2 & 4096) != 0 ? captureStats.openGateProgressMs : j3;
        long j13 = (i2 & 8192) != 0 ? captureStats.openGateRequiredMs : j4;
        long j14 = (i2 & 16384) != 0 ? captureStats.currentSegmentBytes : j5;
        long j15 = (i2 & 32768) != 0 ? captureStats.currentSegmentDurationMs : j6;
        long j16 = (i2 & 65536) != 0 ? captureStats.totalBytesWritten : j7;
        long j17 = (i2 & 131072) != 0 ? captureStats.framesRead : j8;
        long j18 = (i2 & 262144) != 0 ? captureStats.uptimeMs : j9;
        Long l2 = (i2 & 524288) != 0 ? captureStats.lastSpeechAtMs : l;
        long j19 = j18;
        String str8 = (i2 & 1048576) != 0 ? captureStats.liveTranscript : str2;
        String str9 = (i2 & 2097152) != 0 ? captureStats.partialTranscript : str3;
        String str10 = str8;
        String str11 = (i2 & 4194304) != 0 ? captureStats.captionsStatus : str4;
        String str12 = (i2 & 8388608) != 0 ? captureStats.lastError : str5;
        if ((i2 & 16777216) != 0) {
            str6 = str12;
            list3 = captureStats.events;
        } else {
            list3 = list2;
            str6 = str12;
        }
        return captureStats.copy(captureState3, capturePhase3, str7, i3, z2, f5, f6, f7, f8, list4, j10, j11, j12, j13, j14, j15, j16, j17, j19, l2, str10, str9, str11, str6, list3);
    }

    /* JADX INFO: renamed from: component1, reason: from getter */
    public final CaptureState getState() {
        return this.state;
    }

    public final List<RmsSample> component10() {
        return this.rmsHistory;
    }

    /* JADX INFO: renamed from: component11, reason: from getter */
    public final long getPreRollBufferedMs() {
        return this.preRollBufferedMs;
    }

    /* JADX INFO: renamed from: component12, reason: from getter */
    public final long getHangoverRemainingMs() {
        return this.hangoverRemainingMs;
    }

    /* JADX INFO: renamed from: component13, reason: from getter */
    public final long getOpenGateProgressMs() {
        return this.openGateProgressMs;
    }

    /* JADX INFO: renamed from: component14, reason: from getter */
    public final long getOpenGateRequiredMs() {
        return this.openGateRequiredMs;
    }

    /* JADX INFO: renamed from: component15, reason: from getter */
    public final long getCurrentSegmentBytes() {
        return this.currentSegmentBytes;
    }

    /* JADX INFO: renamed from: component16, reason: from getter */
    public final long getCurrentSegmentDurationMs() {
        return this.currentSegmentDurationMs;
    }

    /* JADX INFO: renamed from: component17, reason: from getter */
    public final long getTotalBytesWritten() {
        return this.totalBytesWritten;
    }

    /* JADX INFO: renamed from: component18, reason: from getter */
    public final long getFramesRead() {
        return this.framesRead;
    }

    /* JADX INFO: renamed from: component19, reason: from getter */
    public final long getUptimeMs() {
        return this.uptimeMs;
    }

    /* JADX INFO: renamed from: component2, reason: from getter */
    public final CapturePhase getPhase() {
        return this.phase;
    }

    /* JADX INFO: renamed from: component20, reason: from getter */
    public final Long getLastSpeechAtMs() {
        return this.lastSpeechAtMs;
    }

    /* JADX INFO: renamed from: component21, reason: from getter */
    public final String getLiveTranscript() {
        return this.liveTranscript;
    }

    /* JADX INFO: renamed from: component22, reason: from getter */
    public final String getPartialTranscript() {
        return this.partialTranscript;
    }

    /* JADX INFO: renamed from: component23, reason: from getter */
    public final String getCaptionsStatus() {
        return this.captionsStatus;
    }

    /* JADX INFO: renamed from: component24, reason: from getter */
    public final String getLastError() {
        return this.lastError;
    }

    public final List<CaptureEvent> component25() {
        return this.events;
    }

    /* JADX INFO: renamed from: component3, reason: from getter */
    public final String getSessionId() {
        return this.sessionId;
    }

    /* JADX INFO: renamed from: component4, reason: from getter */
    public final int getSegmentCount() {
        return this.segmentCount;
    }

    /* JADX INFO: renamed from: component5, reason: from getter */
    public final boolean getSpeechActive() {
        return this.speechActive;
    }

    /* JADX INFO: renamed from: component6, reason: from getter */
    public final float getRms() {
        return this.rms;
    }

    /* JADX INFO: renamed from: component7, reason: from getter */
    public final float getPeak() {
        return this.peak;
    }

    /* JADX INFO: renamed from: component8, reason: from getter */
    public final float getSpeechThreshold() {
        return this.speechThreshold;
    }

    /* JADX INFO: renamed from: component9, reason: from getter */
    public final float getSpeechProbability() {
        return this.speechProbability;
    }

    public final CaptureStats copy(CaptureState state, CapturePhase phase, String sessionId, int segmentCount, boolean speechActive, float rms, float peak, float speechThreshold, float speechProbability, List<RmsSample> rmsHistory, long preRollBufferedMs, long hangoverRemainingMs, long openGateProgressMs, long openGateRequiredMs, long currentSegmentBytes, long currentSegmentDurationMs, long totalBytesWritten, long framesRead, long uptimeMs, Long lastSpeechAtMs, String liveTranscript, String partialTranscript, String captionsStatus, String lastError, List<CaptureEvent> events) {
        Intrinsics.checkNotNullParameter(state, "state");
        Intrinsics.checkNotNullParameter(phase, "phase");
        Intrinsics.checkNotNullParameter(rmsHistory, "rmsHistory");
        Intrinsics.checkNotNullParameter(liveTranscript, "liveTranscript");
        Intrinsics.checkNotNullParameter(partialTranscript, "partialTranscript");
        Intrinsics.checkNotNullParameter(captionsStatus, "captionsStatus");
        Intrinsics.checkNotNullParameter(events, "events");
        return new CaptureStats(state, phase, sessionId, segmentCount, speechActive, rms, peak, speechThreshold, speechProbability, rmsHistory, preRollBufferedMs, hangoverRemainingMs, openGateProgressMs, openGateRequiredMs, currentSegmentBytes, currentSegmentDurationMs, totalBytesWritten, framesRead, uptimeMs, lastSpeechAtMs, liveTranscript, partialTranscript, captionsStatus, lastError, events);
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof CaptureStats)) {
            return false;
        }
        CaptureStats captureStats = (CaptureStats) other;
        return this.state == captureStats.state && this.phase == captureStats.phase && Intrinsics.areEqual(this.sessionId, captureStats.sessionId) && this.segmentCount == captureStats.segmentCount && this.speechActive == captureStats.speechActive && Float.compare(this.rms, captureStats.rms) == 0 && Float.compare(this.peak, captureStats.peak) == 0 && Float.compare(this.speechThreshold, captureStats.speechThreshold) == 0 && Float.compare(this.speechProbability, captureStats.speechProbability) == 0 && Intrinsics.areEqual(this.rmsHistory, captureStats.rmsHistory) && this.preRollBufferedMs == captureStats.preRollBufferedMs && this.hangoverRemainingMs == captureStats.hangoverRemainingMs && this.openGateProgressMs == captureStats.openGateProgressMs && this.openGateRequiredMs == captureStats.openGateRequiredMs && this.currentSegmentBytes == captureStats.currentSegmentBytes && this.currentSegmentDurationMs == captureStats.currentSegmentDurationMs && this.totalBytesWritten == captureStats.totalBytesWritten && this.framesRead == captureStats.framesRead && this.uptimeMs == captureStats.uptimeMs && Intrinsics.areEqual(this.lastSpeechAtMs, captureStats.lastSpeechAtMs) && Intrinsics.areEqual(this.liveTranscript, captureStats.liveTranscript) && Intrinsics.areEqual(this.partialTranscript, captureStats.partialTranscript) && Intrinsics.areEqual(this.captionsStatus, captureStats.captionsStatus) && Intrinsics.areEqual(this.lastError, captureStats.lastError) && Intrinsics.areEqual(this.events, captureStats.events);
    }

    public int hashCode() {
        return (((((((((((((((((((((((((((((((((((((((((((((((this.state.hashCode() * 31) + this.phase.hashCode()) * 31) + (this.sessionId == null ? 0 : this.sessionId.hashCode())) * 31) + Integer.hashCode(this.segmentCount)) * 31) + Boolean.hashCode(this.speechActive)) * 31) + Float.hashCode(this.rms)) * 31) + Float.hashCode(this.peak)) * 31) + Float.hashCode(this.speechThreshold)) * 31) + Float.hashCode(this.speechProbability)) * 31) + this.rmsHistory.hashCode()) * 31) + Long.hashCode(this.preRollBufferedMs)) * 31) + Long.hashCode(this.hangoverRemainingMs)) * 31) + Long.hashCode(this.openGateProgressMs)) * 31) + Long.hashCode(this.openGateRequiredMs)) * 31) + Long.hashCode(this.currentSegmentBytes)) * 31) + Long.hashCode(this.currentSegmentDurationMs)) * 31) + Long.hashCode(this.totalBytesWritten)) * 31) + Long.hashCode(this.framesRead)) * 31) + Long.hashCode(this.uptimeMs)) * 31) + (this.lastSpeechAtMs == null ? 0 : this.lastSpeechAtMs.hashCode())) * 31) + this.liveTranscript.hashCode()) * 31) + this.partialTranscript.hashCode()) * 31) + this.captionsStatus.hashCode()) * 31) + (this.lastError != null ? this.lastError.hashCode() : 0)) * 31) + this.events.hashCode();
    }

    public String toString() {
        return "CaptureStats(state=" + this.state + ", phase=" + this.phase + ", sessionId=" + this.sessionId + ", segmentCount=" + this.segmentCount + ", speechActive=" + this.speechActive + ", rms=" + this.rms + ", peak=" + this.peak + ", speechThreshold=" + this.speechThreshold + ", speechProbability=" + this.speechProbability + ", rmsHistory=" + this.rmsHistory + ", preRollBufferedMs=" + this.preRollBufferedMs + ", hangoverRemainingMs=" + this.hangoverRemainingMs + ", openGateProgressMs=" + this.openGateProgressMs + ", openGateRequiredMs=" + this.openGateRequiredMs + ", currentSegmentBytes=" + this.currentSegmentBytes + ", currentSegmentDurationMs=" + this.currentSegmentDurationMs + ", totalBytesWritten=" + this.totalBytesWritten + ", framesRead=" + this.framesRead + ", uptimeMs=" + this.uptimeMs + ", lastSpeechAtMs=" + this.lastSpeechAtMs + ", liveTranscript=" + this.liveTranscript + ", partialTranscript=" + this.partialTranscript + ", captionsStatus=" + this.captionsStatus + ", lastError=" + this.lastError + ", events=" + this.events + ")";
    }

    public CaptureStats(CaptureState state, CapturePhase phase, String sessionId, int segmentCount, boolean speechActive, float rms, float peak, float speechThreshold, float speechProbability, List<RmsSample> rmsHistory, long preRollBufferedMs, long hangoverRemainingMs, long openGateProgressMs, long openGateRequiredMs, long currentSegmentBytes, long currentSegmentDurationMs, long totalBytesWritten, long framesRead, long uptimeMs, Long lastSpeechAtMs, String liveTranscript, String partialTranscript, String captionsStatus, String lastError, List<CaptureEvent> events) {
        Intrinsics.checkNotNullParameter(state, "state");
        Intrinsics.checkNotNullParameter(phase, "phase");
        Intrinsics.checkNotNullParameter(rmsHistory, "rmsHistory");
        Intrinsics.checkNotNullParameter(liveTranscript, "liveTranscript");
        Intrinsics.checkNotNullParameter(partialTranscript, "partialTranscript");
        Intrinsics.checkNotNullParameter(captionsStatus, "captionsStatus");
        Intrinsics.checkNotNullParameter(events, "events");
        this.state = state;
        this.phase = phase;
        this.sessionId = sessionId;
        this.segmentCount = segmentCount;
        this.speechActive = speechActive;
        this.rms = rms;
        this.peak = peak;
        this.speechThreshold = speechThreshold;
        this.speechProbability = speechProbability;
        this.rmsHistory = rmsHistory;
        this.preRollBufferedMs = preRollBufferedMs;
        this.hangoverRemainingMs = hangoverRemainingMs;
        this.openGateProgressMs = openGateProgressMs;
        this.openGateRequiredMs = openGateRequiredMs;
        this.currentSegmentBytes = currentSegmentBytes;
        this.currentSegmentDurationMs = currentSegmentDurationMs;
        this.totalBytesWritten = totalBytesWritten;
        this.framesRead = framesRead;
        this.uptimeMs = uptimeMs;
        this.lastSpeechAtMs = lastSpeechAtMs;
        this.liveTranscript = liveTranscript;
        this.partialTranscript = partialTranscript;
        this.captionsStatus = captionsStatus;
        this.lastError = lastError;
        this.events = events;
    }

    public /* synthetic */ CaptureStats(CaptureState captureState, CapturePhase capturePhase, String str, int i, boolean z, float f, float f2, float f3, float f4, List list, long j, long j2, long j3, long j4, long j5, long j6, long j7, long j8, long j9, Long l, String str2, String str3, String str4, String str5, List list2, int i2, DefaultConstructorMarker defaultConstructorMarker) {
        this((i2 & 1) != 0 ? CaptureState.IDLE : captureState, (i2 & 2) != 0 ? CapturePhase.IDLE : capturePhase, (i2 & 4) != 0 ? null : str, (i2 & 8) != 0 ? 0 : i, (i2 & 16) == 0 ? z : false, (i2 & 32) != 0 ? 0.0f : f, (i2 & 64) == 0 ? f2 : 0.0f, (i2 & 128) != 0 ? 0.5f : f3, (i2 & 256) != 0 ? -1.0f : f4, (i2 & 512) != 0 ? CollectionsKt.emptyList() : list, (i2 & 1024) != 0 ? 0L : j, (i2 & 2048) != 0 ? 0L : j2, (i2 & 4096) != 0 ? 0L : j3, (i2 & 8192) != 0 ? 1500L : j4, (i2 & 16384) != 0 ? 0L : j5, (32768 & i2) != 0 ? 0L : j6, (65536 & i2) != 0 ? 0L : j7, (131072 & i2) != 0 ? 0L : j8, (262144 & i2) == 0 ? j9 : 0L, (524288 & i2) != 0 ? null : l, (i2 & 1048576) != 0 ? "" : str2, (i2 & 2097152) == 0 ? str3 : "", (i2 & 4194304) != 0 ? DebugKt.DEBUG_PROPERTY_VALUE_OFF : str4, (i2 & 8388608) == 0 ? str5 : null, (i2 & 16777216) != 0 ? CollectionsKt.emptyList() : list2);
    }

    public final CaptureState getState() {
        return this.state;
    }

    public final CapturePhase getPhase() {
        return this.phase;
    }

    public final String getSessionId() {
        return this.sessionId;
    }

    public final int getSegmentCount() {
        return this.segmentCount;
    }

    public final boolean getSpeechActive() {
        return this.speechActive;
    }

    public final float getRms() {
        return this.rms;
    }

    public final float getPeak() {
        return this.peak;
    }

    public final float getSpeechThreshold() {
        return this.speechThreshold;
    }

    public final float getSpeechProbability() {
        return this.speechProbability;
    }

    public final List<RmsSample> getRmsHistory() {
        return this.rmsHistory;
    }

    public final long getPreRollBufferedMs() {
        return this.preRollBufferedMs;
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

    public final long getCurrentSegmentBytes() {
        return this.currentSegmentBytes;
    }

    public final long getCurrentSegmentDurationMs() {
        return this.currentSegmentDurationMs;
    }

    public final long getTotalBytesWritten() {
        return this.totalBytesWritten;
    }

    public final long getFramesRead() {
        return this.framesRead;
    }

    public final long getUptimeMs() {
        return this.uptimeMs;
    }

    public final Long getLastSpeechAtMs() {
        return this.lastSpeechAtMs;
    }

    public final String getLiveTranscript() {
        return this.liveTranscript;
    }

    public final String getPartialTranscript() {
        return this.partialTranscript;
    }

    public final String getCaptionsStatus() {
        return this.captionsStatus;
    }

    public final String getLastError() {
        return this.lastError;
    }

    public final List<CaptureEvent> getEvents() {
        return this.events;
    }
}
