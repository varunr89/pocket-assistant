package com.varun.pocketassistant.capture;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: VoiceActivityDetector.kt */
/* JADX INFO: loaded from: classes5.dex */
@Metadata(d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u0017\n\u0002\b\u0006\n\u0002\u0010\u0002\n\u0002\b\u0005\b\u0007\u0018\u00002\u00020\u0001B\u000f\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0004\b\u0004\u0010\u0005J\u0016\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u00072\u0006\u0010\u0010\u001a\u00020\u0003J\u0006\u0010\u0011\u001a\u00020\u0007J\u0006\u0010\u0012\u001a\u00020\u000eR\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0003X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0003X\u0082\u000e¢\u0006\u0002\n\u0000R\u0011\u0010\n\u001a\u00020\u00038F¢\u0006\u0006\u001a\u0004\b\u000b\u0010\f¨\u0006\u0013"}, d2 = {"Lcom/varun/pocketassistant/capture/RingPcmBuffer;", "", "capacitySamples", "", "<init>", "(I)V", "buffer", "", "writePos", "size", "bufferedSamples", "getBufferedSamples", "()I", "push", "", "samples", "length", "drainSnapshot", "clear", "app_debug"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class RingPcmBuffer {
    public static final int $stable = 8;
    private final short[] buffer;
    private int size;
    private int writePos;

    public RingPcmBuffer(int capacitySamples) {
        this.buffer = new short[capacitySamples];
    }

    /* JADX INFO: renamed from: getBufferedSamples, reason: from getter */
    public final int getSize() {
        return this.size;
    }

    public final void push(short[] samples, int length) {
        Intrinsics.checkNotNullParameter(samples, "samples");
        for (int i = 0; i < length; i++) {
            this.buffer[this.writePos] = samples[i];
            this.writePos = (this.writePos + 1) % this.buffer.length;
            if (this.size < this.buffer.length) {
                this.size++;
            }
        }
    }

    public final short[] drainSnapshot() {
        if (this.size == 0) {
            return new short[0];
        }
        short[] out = new short[this.size];
        int start = this.size == this.buffer.length ? this.writePos : 0;
        int i = this.size;
        for (int i2 = 0; i2 < i; i2++) {
            out[i2] = this.buffer[(start + i2) % this.buffer.length];
        }
        return out;
    }

    public final void clear() {
        this.writePos = 0;
        this.size = 0;
    }
}
