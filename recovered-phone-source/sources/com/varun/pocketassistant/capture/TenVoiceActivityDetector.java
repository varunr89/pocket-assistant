package com.varun.pocketassistant.capture;

import android.util.Log;
import com.google.android.gms.common.ConnectionResult;
import kotlin.Metadata;
import kotlin.collections.ArraysKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt;

/* JADX INFO: compiled from: VoiceActivityDetector.kt */
/* JADX INFO: loaded from: classes5.dex */
@Metadata(d1 = {"\u0000R\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0007\n\u0002\b\u000b\n\u0002\u0010\u0017\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u0014\n\u0000\n\u0002\u0010\u0015\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0010\u0018\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\t\b\u0007\u0018\u0000 52\u00020\u0001:\u00015BC\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0005\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0005\u0012\b\b\u0002\u0010\u0007\u001a\u00020\u0003\u0012\b\b\u0002\u0010\b\u001a\u00020\u0005\u0012\b\b\u0002\u0010\t\u001a\u00020\u0003¢\u0006\u0004\b\n\u0010\u000bJ\u0018\u0010(\u001a\u00020)2\u0006\u0010*\u001a\u00020\u00112\u0006\u0010+\u001a\u00020\u0003H\u0016J\b\u0010,\u001a\u00020-H\u0016J\u0006\u0010.\u001a\u00020-J\u0018\u0010/\u001a\u00020\u001a2\u0006\u00100\u001a\u00020\u001a2\u0006\u00101\u001a\u00020\u001aH\u0002J\u0010\u00102\u001a\u00020-2\u0006\u00103\u001a\u00020\u001aH\u0002J\b\u00104\u001a\u00020-H\u0002R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0005X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0005X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0011X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u0003X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u0014X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\u0016X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0017\u001a\u00020\u0018X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0019\u001a\u00020\u001aX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u001b\u001a\u00020\u0003X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u001c\u001a\u00020\u0005X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u001d\u001a\u00020\u001aX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u001e\u001a\u00020\u001fX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010 \u001a\u00020\u0003X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010!\u001a\u00020\u0003X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\"\u001a\u00020\u0003X\u0082\u000e¢\u0006\u0002\n\u0000R\u0014\u0010#\u001a\u00020\u0005X\u0096\u0004¢\u0006\b\n\u0000\u001a\u0004\b$\u0010%R\u0014\u0010&\u001a\u00020\u00058VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b'\u0010%¨\u00066"}, d2 = {"Lcom/varun/pocketassistant/capture/TenVoiceActivityDetector;", "Lcom/varun/pocketassistant/capture/VoiceActivityDetector;", "hopSize", "", "openThreshold", "", "closeThreshold", "openGateMs", "openGateVoicedRatio", "endHangoverMs", "<init>", "(IFFIFI)V", "frameMs", "openGateFrames", "endHangoverFrames", "minVoicedToOpen", "hop", "", "pendingCount", "handle", "", "outProbability", "", "outFlag", "", "inSpeech", "", "hangover", "lastProbability", "lastRawSpeech", "recentVoiced", "", "recentIndex", "recentFilled", "recentVoicedCount", "speechThreshold", "getSpeechThreshold", "()F", "lastSpeechProbability", "getLastSpeechProbability", "accept", "Lcom/varun/pocketassistant/capture/VadDecision;", "frame", "length", "reset", "", "close", "updateState", "rawSpeechForOpen", "speechForHangover", "pushRecent", "voiced", "clearRecent", "Companion", "app_debug"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class TenVoiceActivityDetector implements VoiceActivityDetector {
    public static final int HOP_SIZE = 256;
    private final float closeThreshold;
    private final int endHangoverFrames;
    private final int endHangoverMs;
    private final int frameMs;
    private long handle;
    private int hangover;
    private final short[] hop;
    private final int hopSize;
    private boolean inSpeech;
    private float lastProbability;
    private boolean lastRawSpeech;
    private final int minVoicedToOpen;
    private final int openGateFrames;
    private final int openGateMs;
    private final float openGateVoicedRatio;
    private final float openThreshold;
    private final int[] outFlag;
    private final float[] outProbability;
    private int pendingCount;
    private int recentFilled;
    private int recentIndex;
    private final boolean[] recentVoiced;
    private int recentVoicedCount;
    private final float speechThreshold;

    /* JADX INFO: renamed from: Companion, reason: from kotlin metadata */
    public static final Companion INSTANCE = new Companion(null);
    public static final int $stable = 8;

    public TenVoiceActivityDetector() {
        this(0, 0.0f, 0.0f, 0, 0.0f, 0, 63, null);
    }

    public TenVoiceActivityDetector(int hopSize, float openThreshold, float closeThreshold, int openGateMs, float openGateVoicedRatio, int endHangoverMs) {
        this.hopSize = hopSize;
        this.openThreshold = openThreshold;
        this.closeThreshold = closeThreshold;
        this.openGateMs = openGateMs;
        this.openGateVoicedRatio = openGateVoicedRatio;
        this.endHangoverMs = endHangoverMs;
        this.frameMs = (this.hopSize * 1000) / 16000;
        this.openGateFrames = RangesKt.coerceAtLeast(this.openGateMs / this.frameMs, 1);
        this.endHangoverFrames = RangesKt.coerceAtLeast(this.endHangoverMs / this.frameMs, 1);
        this.minVoicedToOpen = RangesKt.coerceAtLeast((int) (this.openGateFrames * this.openGateVoicedRatio), 1);
        this.hop = new short[this.hopSize];
        this.outProbability = new float[1];
        this.outFlag = new int[1];
        this.recentVoiced = new boolean[this.openGateFrames];
        this.speechThreshold = this.openThreshold;
        this.handle = TenVadNative.INSTANCE.nativeCreate(this.hopSize, this.openThreshold);
        if (this.handle != 0) {
            return;
        }
        throw new IllegalArgumentException(("ten_vad_create failed (hop=" + this.hopSize + " threshold=" + this.openThreshold + ")").toString());
    }

    public /* synthetic */ TenVoiceActivityDetector(int i, float f, float f2, int i2, float f3, int i3, int i4, DefaultConstructorMarker defaultConstructorMarker) {
        this((i4 & 1) != 0 ? 256 : i, (i4 & 2) != 0 ? 0.5f : f, (i4 & 4) != 0 ? 0.5f : f2, (i4 & 8) != 0 ? ConnectionResult.DRIVE_EXTERNAL_STORAGE_REQUIRED : i2, (i4 & 16) != 0 ? 0.6f : f3, (i4 & 32) != 0 ? 10000 : i3);
    }

    @Override // com.varun.pocketassistant.capture.VoiceActivityDetector
    public float getSpeechThreshold() {
        return this.speechThreshold;
    }

    @Override // com.varun.pocketassistant.capture.VoiceActivityDetector
    /* JADX INFO: renamed from: getLastSpeechProbability, reason: from getter */
    public float getLastProbability() {
        return this.lastProbability;
    }

    @Override // com.varun.pocketassistant.capture.VoiceActivityDetector
    public VadDecision accept(short[] frame, int length) {
        long gateProgressMs;
        long j;
        Intrinsics.checkNotNullParameter(frame, "frame");
        int offset = 0;
        boolean windowOpen = this.inSpeech;
        boolean raw = this.lastRawSpeech;
        boolean windowOpen2 = windowOpen;
        while (true) {
            boolean keepSpeech = true;
            if (offset >= length) {
                break;
            }
            int toCopy = Math.min(this.hopSize - this.pendingCount, length - offset);
            System.arraycopy(frame, offset, this.hop, this.pendingCount, toCopy);
            this.pendingCount += toCopy;
            offset += toCopy;
            if (this.pendingCount < this.hopSize) {
                break;
            }
            int rc = TenVadNative.INSTANCE.nativeProcess(this.handle, this.hop, this.outProbability, this.outFlag);
            if (rc != 0) {
                throw new IllegalStateException("ten_vad_process failed code=" + rc);
            }
            this.lastProbability = this.outProbability[0];
            raw = this.lastProbability >= this.openThreshold;
            if (this.lastProbability < this.closeThreshold) {
                keepSpeech = false;
            }
            windowOpen2 = updateState(raw, keepSpeech);
            this.pendingCount = 0;
        }
        this.lastRawSpeech = raw;
        if (!this.inSpeech) {
            int filled = RangesKt.coerceAtLeast(this.recentFilled, 1);
            float ratio = this.recentVoicedCount / filled;
            gateProgressMs = RangesKt.coerceAtMost((long) (this.openGateMs * ratio), this.openGateMs);
        } else {
            gateProgressMs = this.openGateMs;
        }
        if (windowOpen2 && this.lastProbability < this.closeThreshold) {
            j = ((long) this.hangover) * ((long) this.frameMs);
        } else {
            j = windowOpen2 ? this.endHangoverMs : 0L;
        }
        return new VadDecision(windowOpen2, raw, j, gateProgressMs, this.openGateMs, this.lastProbability);
    }

    @Override // com.varun.pocketassistant.capture.VoiceActivityDetector
    public void reset() {
        this.pendingCount = 0;
        this.inSpeech = false;
        this.hangover = 0;
        this.lastProbability = 0.0f;
        this.lastRawSpeech = false;
        this.recentIndex = 0;
        this.recentFilled = 0;
        this.recentVoicedCount = 0;
        ArraysKt.fill$default(this.recentVoiced, false, 0, 0, 6, (Object) null);
        if (this.handle != 0) {
            TenVadNative.INSTANCE.nativeDestroy(this.handle);
            this.handle = 0L;
        }
        this.handle = TenVadNative.INSTANCE.nativeCreate(this.hopSize, this.openThreshold);
        if (!(this.handle != 0)) {
            throw new IllegalArgumentException("ten_vad_create failed on reset".toString());
        }
    }

    public final void close() {
        if (this.handle != 0) {
            TenVadNative.INSTANCE.nativeDestroy(this.handle);
            this.handle = 0L;
        }
    }

    private final boolean updateState(boolean rawSpeechForOpen, boolean speechForHangover) {
        pushRecent(rawSpeechForOpen);
        if (!this.inSpeech) {
            if (this.recentFilled >= this.openGateFrames && this.recentVoicedCount >= this.minVoicedToOpen) {
                this.inSpeech = true;
                this.hangover = this.endHangoverFrames;
            }
        } else if (speechForHangover) {
            this.hangover = this.endHangoverFrames;
        } else {
            this.hangover--;
            if (this.hangover <= 0) {
                this.inSpeech = false;
                clearRecent();
            }
        }
        return this.inSpeech;
    }

    private final void pushRecent(boolean voiced) {
        if (this.recentFilled == this.openGateFrames) {
            if (this.recentVoiced[this.recentIndex]) {
                this.recentVoicedCount--;
            }
        } else {
            this.recentFilled++;
        }
        this.recentVoiced[this.recentIndex] = voiced;
        if (voiced) {
            this.recentVoicedCount++;
        }
        this.recentIndex = (this.recentIndex + 1) % this.openGateFrames;
    }

    private final void clearRecent() {
        this.recentIndex = 0;
        this.recentFilled = 0;
        this.recentVoicedCount = 0;
        ArraysKt.fill$default(this.recentVoiced, false, 0, 0, 6, (Object) null);
    }

    /* JADX INFO: compiled from: VoiceActivityDetector.kt */
    @Metadata(d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003J\u0006\u0010\u0006\u001a\u00020\u0007R\u000e\u0010\u0004\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000¨\u0006\b"}, d2 = {"Lcom/varun/pocketassistant/capture/TenVoiceActivityDetector$Companion;", "", "<init>", "()V", "HOP_SIZE", "", "createOrFallback", "Lcom/varun/pocketassistant/capture/VoiceActivityDetector;", "app_debug"}, k = 1, mv = {2, 2, 0}, xi = 48)
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        public final VoiceActivityDetector createOrFallback() {
            try {
                TenVoiceActivityDetector vad = new TenVoiceActivityDetector(0, 0.0f, 0.0f, 0, 0.0f, 0, 63, null);
                Log.i("TenVAD", "TEN VAD ready version=" + TenVadNative.INSTANCE.nativeVersion() + " open=" + vad.getSpeechThreshold());
                return vad;
            } catch (Throwable t) {
                Log.e("TenVAD", "TEN VAD unavailable; using energy fallback", t);
                return new EnergyVoiceActivityDetector(0, 0, 0, 0, 0.0d, 31, null);
            }
        }
    }
}
