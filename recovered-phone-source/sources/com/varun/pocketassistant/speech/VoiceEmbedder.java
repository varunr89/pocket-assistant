package com.varun.pocketassistant.speech;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt;

/* JADX INFO: compiled from: SpeakerProfile.kt */
/* JADX INFO: loaded from: classes3.dex */
@Metadata(d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0014\n\u0000\n\u0002\u0010\u0017\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0010\u0006\n\u0002\b\u0003\bÇ\u0002\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003J\u0018\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\t2\b\b\u0002\u0010\n\u001a\u00020\u0005J\u0016\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u00072\u0006\u0010\u000e\u001a\u00020\u0007J(\u0010\u000f\u001a\u00020\u00102\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\u0011\u001a\u00020\u00052\u0006\u0010\u0012\u001a\u00020\u00052\u0006\u0010\u0013\u001a\u00020\u0007H\u0002J \u0010\u0014\u001a\u00020\u00152\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\u0011\u001a\u00020\u00052\u0006\u0010\u0012\u001a\u00020\u0005H\u0002J\u0010\u0010\u0016\u001a\u00020\u00072\u0006\u0010\u0017\u001a\u00020\u0007H\u0002R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082T¢\u0006\u0002\n\u0000¨\u0006\u0018"}, d2 = {"Lcom/varun/pocketassistant/speech/VoiceEmbedder;", "", "<init>", "()V", "BANDS", "", "embed", "", "samples", "", "sampleRate", "cosine", "", "a", "b", "accumulateBands", "", "offset", "length", "acc", "frameRms", "", "l2Normalize", "v", "app_debug"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class VoiceEmbedder {
    public static final int $stable = 0;
    private static final int BANDS = 32;
    public static final VoiceEmbedder INSTANCE = new VoiceEmbedder();

    private VoiceEmbedder() {
    }

    public static /* synthetic */ float[] embed$default(VoiceEmbedder voiceEmbedder, short[] sArr, int i, int i2, Object obj) {
        if ((i2 & 2) != 0) {
            i = 16000;
        }
        return voiceEmbedder.embed(sArr, i);
    }

    public final float[] embed(short[] samples, int sampleRate) {
        Intrinsics.checkNotNullParameter(samples, "samples");
        int frame = sampleRate / 50;
        int hop = frame / 2;
        float[] acc = new float[32];
        int frames = 0;
        for (int i = 0; i + frame <= samples.length; i += hop) {
            if (frameRms(samples, i, frame) >= 160.0d) {
                accumulateBands(samples, i, frame, acc);
                frames++;
            }
        }
        if (frames == 0) {
            accumulateBands(samples, 0, Math.min(frame, samples.length), acc);
            frames = 1;
        }
        int length = acc.length;
        for (int b = 0; b < length; b++) {
            acc[b] = acc[b] / frames;
        }
        return l2Normalize(acc);
    }

    public final float cosine(float[] a, float[] b) {
        Intrinsics.checkNotNullParameter(a, "a");
        Intrinsics.checkNotNullParameter(b, "b");
        int n = Math.min(a.length, b.length);
        float dot = 0.0f;
        for (int i = 0; i < n; i++) {
            dot += a[i] * b[i];
        }
        return dot;
    }

    private final void accumulateBands(short[] samples, int offset, int length, float[] acc) {
        int bandSize = RangesKt.coerceAtLeast(length / 32, 1);
        for (int b = 0; b < 32; b++) {
            double sum = 0.0d;
            int start = (b * bandSize) + offset;
            int end = Math.min(offset + length, start + bandSize);
            for (int i = start; i < end; i++) {
                float v = samples[i] / 32768.0f;
                sum += (double) (v * v);
            }
            acc[b] = acc[b] + ((float) Math.log(1.0d + sum));
        }
    }

    private final double frameRms(short[] samples, int offset, int length) {
        double sum = 0.0d;
        int i = offset + length;
        for (int i2 = offset; i2 < i; i2++) {
            double v = samples[i2];
            sum += v * v;
        }
        return Math.sqrt(sum / ((double) length));
    }

    private final float[] l2Normalize(float[] v) {
        float sum = 0.0f;
        for (float x : v) {
            sum += x * x;
        }
        float norm = (float) Math.sqrt(sum);
        if (norm < 1.0E-6f) {
            return v;
        }
        int length = v.length;
        float[] fArr = new float[length];
        for (int i = 0; i < length; i++) {
            fArr[i] = v[i] / norm;
        }
        return fArr;
    }
}
