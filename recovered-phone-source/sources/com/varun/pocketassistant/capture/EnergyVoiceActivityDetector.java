package com.varun.pocketassistant.capture;

import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt;
import org.apache.commons.math3.dfp.Dfp;

/* JADX INFO: compiled from: VoiceActivityDetector.kt */
/* JADX INFO: loaded from: classes5.dex */
@Metadata(d1 = {"\u0000B\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0010\u0006\n\u0002\b\u0006\n\u0002\u0010\u0017\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0010\u0007\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0005\b\u0007\u0018\u00002\u00020\u0001B9\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0005\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0007\u001a\u00020\b¢\u0006\u0004\b\t\u0010\nJ\u0018\u0010\u001a\u001a\u00020\u001b2\u0006\u0010\u001c\u001a\u00020\u000f2\u0006\u0010\u001d\u001a\u00020\u0003H\u0016J\b\u0010\u001e\u001a\u00020\u001fH\u0016J\u0010\u0010 \u001a\u00020\u00122\u0006\u0010!\u001a\u00020\u0012H\u0002J\u0018\u0010\"\u001a\u00020\b2\u0006\u0010#\u001a\u00020\u000f2\u0006\u0010\u001d\u001a\u00020\u0003H\u0002R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u000fX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0003X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0012X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u0003X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\u0003X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\u0012X\u0082\u000e¢\u0006\u0002\n\u0000R\u0014\u0010\u0016\u001a\u00020\u0017X\u0096\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0018\u0010\u0019¨\u0006$"}, d2 = {"Lcom/varun/pocketassistant/capture/EnergyVoiceActivityDetector;", "Lcom/varun/pocketassistant/capture/VoiceActivityDetector;", "sampleRate", "", "frameMs", "openGateMs", "endHangoverMs", "speechRmsThreshold", "", "<init>", "(IIIID)V", "frameSamples", "openGateFrames", "endHangoverFrames", "pending", "", "pendingCount", "inSpeech", "", "hangover", "speechFrames", "lastRawSpeech", "speechThreshold", "", "getSpeechThreshold", "()F", "accept", "Lcom/varun/pocketassistant/capture/VadDecision;", "frame", "length", "reset", "", "updateState", "isSpeechFrame", "rms", "samples", "app_debug"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class EnergyVoiceActivityDetector implements VoiceActivityDetector {
    public static final int $stable = 8;
    private final int endHangoverFrames;
    private final int endHangoverMs;
    private final int frameMs;
    private final int frameSamples;
    private int hangover;
    private boolean inSpeech;
    private boolean lastRawSpeech;
    private final int openGateFrames;
    private final int openGateMs;
    private final short[] pending;
    private int pendingCount;
    private final int sampleRate;
    private int speechFrames;
    private final double speechRmsThreshold;
    private final float speechThreshold;

    public EnergyVoiceActivityDetector() {
        this(0, 0, 0, 0, 0.0d, 31, null);
    }

    public EnergyVoiceActivityDetector(int sampleRate, int frameMs, int openGateMs, int endHangoverMs, double speechRmsThreshold) {
        this.sampleRate = sampleRate;
        this.frameMs = frameMs;
        this.openGateMs = openGateMs;
        this.endHangoverMs = endHangoverMs;
        this.speechRmsThreshold = speechRmsThreshold;
        this.frameSamples = (this.sampleRate * this.frameMs) / 1000;
        this.openGateFrames = RangesKt.coerceAtLeast(this.openGateMs / this.frameMs, 1);
        this.endHangoverFrames = RangesKt.coerceAtLeast(this.endHangoverMs / this.frameMs, 1);
        this.pending = new short[this.frameSamples * 2];
        this.speechThreshold = (float) this.speechRmsThreshold;
    }

    public /* synthetic */ EnergyVoiceActivityDetector(int i, int i2, int i3, int i4, double d, int i5, DefaultConstructorMarker defaultConstructorMarker) {
        this((i5 & 1) != 0 ? 16000 : i, (i5 & 2) != 0 ? 30 : i2, (i5 & 4) != 0 ? Dfp.RADIX : i3, (i5 & 8) != 0 ? 60000 : i4, (i5 & 16) != 0 ? 160.0d : d);
    }

    @Override // com.varun.pocketassistant.capture.VoiceActivityDetector
    public float getLastSpeechProbability() {
        return super.getLastSpeechProbability();
    }

    @Override // com.varun.pocketassistant.capture.VoiceActivityDetector
    public float getSpeechThreshold() {
        return this.speechThreshold;
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
        while (offset < length) {
            int toCopy = Math.min(this.frameSamples - this.pendingCount, length - offset);
            System.arraycopy(frame, offset, this.pending, this.pendingCount, toCopy);
            this.pendingCount += toCopy;
            offset += toCopy;
            if (this.pendingCount < this.frameSamples) {
                break;
            }
            double rms = rms(this.pending, this.frameSamples);
            raw = rms >= this.speechRmsThreshold;
            windowOpen2 = updateState(raw);
            this.pendingCount = 0;
        }
        this.lastRawSpeech = raw;
        if (!this.inSpeech) {
            gateProgressMs = ((long) this.speechFrames) * ((long) this.frameMs);
        } else {
            gateProgressMs = this.openGateMs;
        }
        if (!windowOpen2 || raw) {
            j = (windowOpen2 && raw) ? this.endHangoverMs : 0L;
        } else {
            j = ((long) this.hangover) * ((long) this.frameMs);
        }
        return new VadDecision(windowOpen2, raw, j, gateProgressMs, this.openGateMs, -1.0f);
    }

    @Override // com.varun.pocketassistant.capture.VoiceActivityDetector
    public void reset() {
        this.pendingCount = 0;
        this.inSpeech = false;
        this.hangover = 0;
        this.speechFrames = 0;
        this.lastRawSpeech = false;
    }

    private final boolean updateState(boolean isSpeechFrame) {
        if (!this.inSpeech) {
            if (isSpeechFrame) {
                this.speechFrames++;
                if (this.speechFrames >= this.openGateFrames) {
                    this.inSpeech = true;
                    this.hangover = this.endHangoverFrames;
                }
            } else {
                this.speechFrames = 0;
            }
        } else if (isSpeechFrame) {
            this.hangover = this.endHangoverFrames;
        } else {
            this.hangover--;
            if (this.hangover <= 0) {
                this.inSpeech = false;
                this.speechFrames = 0;
            }
        }
        return this.inSpeech;
    }

    private final double rms(short[] samples, int length) {
        double sum = 0.0d;
        for (int i = 0; i < length; i++) {
            double v = samples[i];
            sum += v * v;
        }
        return Math.sqrt(sum / ((double) length));
    }
}
