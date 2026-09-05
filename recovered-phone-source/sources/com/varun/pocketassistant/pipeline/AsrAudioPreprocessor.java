package com.varun.pocketassistant.pipeline;

import android.util.Log;
import com.google.ai.edge.examples.asr.FileAudioSource;
import java.io.File;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.collections.ArraysKt;
import kotlin.collections.CollectionsKt;
import kotlin.io.CloseableKt;
import kotlin.io.FilesKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt;
import kotlin.text.Charsets;
import kotlin.text.StringsKt;

/* JADX INFO: compiled from: AsrAudioPreprocessor.kt */
/* JADX INFO: loaded from: classes8.dex */
@Metadata(d1 = {"\u0000P\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010 \n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0010\u0017\n\u0002\b\n\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\f\bÇ\u0002\u0018\u00002\u00020\u0001:\u0001/B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003J>\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\u00072\b\b\u0002\u0010\t\u001a\u00020\n2\b\b\u0002\u0010\u000b\u001a\u00020\n2\b\b\u0002\u0010\f\u001a\u00020\r2\b\b\u0002\u0010\u000e\u001a\u00020\rJ\u0016\u0010\u000f\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\u0007J&\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u00070\u00112\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\u00072\b\b\u0002\u0010\u0012\u001a\u00020\u0013J \u0010\u0014\u001a\u00020\r2\u0006\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020\r2\u0006\u0010\u0018\u001a\u00020\rH\u0002J(\u0010\u0019\u001a\u00020\u00162\u0006\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u000b\u001a\u00020\n2\u0006\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\rH\u0002J\u0018\u0010\u001a\u001a\u00020\u00162\u0006\u0010\u0015\u001a\u00020\u00162\u0006\u0010\t\u001a\u00020\nH\u0002J \u0010\u001b\u001a\u00020\n2\u0006\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u001c\u001a\u00020\r2\u0006\u0010\u001d\u001a\u00020\rH\u0002J\u0010\u0010\u001e\u001a\u00020\u00162\u0006\u0010\u001f\u001a\u00020\u0007H\u0002J\u0018\u0010 \u001a\u00020!2\u0006\u0010\"\u001a\u00020\u00072\u0006\u0010\u0015\u001a\u00020\u0016H\u0002R\u000e\u0010#\u001a\u00020$X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010%\u001a\u00020\rX\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010&\u001a\u00020\rX\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010'\u001a\u00020\nX\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010(\u001a\u00020\rX\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010)\u001a\u00020\rX\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010*\u001a\u00020\nX\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010+\u001a\u00020\nX\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010,\u001a\u00020\u0013X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010-\u001a\u00020\u0013X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010.\u001a\u00020\u0013X\u0086T¢\u0006\u0002\n\u0000¨\u00060"}, d2 = {"Lcom/varun/pocketassistant/pipeline/AsrAudioPreprocessor;", "", "<init>", "()V", "prepare", "Lcom/varun/pocketassistant/pipeline/AsrAudioPreprocessor$Result;", "inputWav", "Ljava/io/File;", "outputDir", "speed", "", "silenceRms", "minSilenceFramesToDrop", "", "padFrames", "prepareForCloud", "splitByDurationMs", "", "maxDurationMs", "", "findNearestSilenceSplit", "pcm", "", "targetSample", "radius", "trimSilence", "timeCompress", "frameRms", "offset", "length", "readPcm16Mono16k", "wav", "writeWav", "", "file", "TAG", "", "SAMPLE_RATE", "FRAME_SAMPLES", "SILENCE_RMS", "MIN_SILENCE_FRAMES_TO_DROP", "PAD_FRAMES", "DEFAULT_SPEED", "CLOUD_SPEED", "CLOUD_CHUNK_MS", "CLOUD_MAX_SEGMENT_MS", "LOCAL_MAX_SEGMENT_MS", "Result", "app_debug"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class AsrAudioPreprocessor {
    public static final int $stable = 0;
    public static final long CLOUD_CHUNK_MS = 120000;
    public static final long CLOUD_MAX_SEGMENT_MS = 600000;
    public static final float CLOUD_SPEED = 1.0f;
    public static final float DEFAULT_SPEED = 1.35f;
    private static final int FRAME_SAMPLES = 480;
    public static final AsrAudioPreprocessor INSTANCE = new AsrAudioPreprocessor();
    public static final long LOCAL_MAX_SEGMENT_MS = 120000;
    private static final int MIN_SILENCE_FRAMES_TO_DROP = 8;
    private static final int PAD_FRAMES = 4;
    public static final int SAMPLE_RATE = 16000;
    public static final float SILENCE_RMS = 0.012f;
    private static final String TAG = "AsrAudioPreprocessor";

    /* JADX INFO: compiled from: AsrAudioPreprocessor.kt */
    @Metadata(d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0002\b\u0014\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0087\b\u0018\u00002\u00020\u0001B/\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0005\u0012\u0006\u0010\u0007\u001a\u00020\b\u0012\u0006\u0010\t\u001a\u00020\b¢\u0006\u0004\b\n\u0010\u000bJ\t\u0010\u0016\u001a\u00020\u0003HÆ\u0003J\t\u0010\u0017\u001a\u00020\u0005HÆ\u0003J\t\u0010\u0018\u001a\u00020\u0005HÆ\u0003J\t\u0010\u0019\u001a\u00020\bHÆ\u0003J\t\u0010\u001a\u001a\u00020\bHÆ\u0003J;\u0010\u001b\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00052\b\b\u0002\u0010\u0007\u001a\u00020\b2\b\b\u0002\u0010\t\u001a\u00020\bHÆ\u0001J\u0013\u0010\u001c\u001a\u00020\u001d2\b\u0010\u001e\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u001f\u001a\u00020 HÖ\u0001J\t\u0010!\u001a\u00020\"HÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\f\u0010\rR\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000fR\u0011\u0010\u0006\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u000fR\u0011\u0010\u0007\u001a\u00020\b¢\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0012R\u0011\u0010\t\u001a\u00020\b¢\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0012R\u0011\u0010\u0014\u001a\u00020\b8F¢\u0006\u0006\u001a\u0004\b\u0015\u0010\u0012¨\u0006#"}, d2 = {"Lcom/varun/pocketassistant/pipeline/AsrAudioPreprocessor$Result;", "", "file", "Ljava/io/File;", "originalDurationMs", "", "processedDurationMs", "keptSpeechRatio", "", "speed", "<init>", "(Ljava/io/File;JJFF)V", "getFile", "()Ljava/io/File;", "getOriginalDurationMs", "()J", "getProcessedDurationMs", "getKeptSpeechRatio", "()F", "getSpeed", "compressionRatio", "getCompressionRatio", "component1", "component2", "component3", "component4", "component5", "copy", "equals", "", "other", "hashCode", "", "toString", "", "app_debug"}, k = 1, mv = {2, 2, 0}, xi = 48)
    public static final /* data */ class Result {
        public static final int $stable = 8;
        private final File file;
        private final float keptSpeechRatio;
        private final long originalDurationMs;
        private final long processedDurationMs;
        private final float speed;

        public static /* synthetic */ Result copy$default(Result result, File file, long j, long j2, float f, float f2, int i, Object obj) {
            if ((i & 1) != 0) {
                file = result.file;
            }
            if ((i & 2) != 0) {
                j = result.originalDurationMs;
            }
            if ((i & 4) != 0) {
                j2 = result.processedDurationMs;
            }
            if ((i & 8) != 0) {
                f = result.keptSpeechRatio;
            }
            if ((i & 16) != 0) {
                f2 = result.speed;
            }
            long j3 = j2;
            return result.copy(file, j, j3, f, f2);
        }

        /* JADX INFO: renamed from: component1, reason: from getter */
        public final File getFile() {
            return this.file;
        }

        /* JADX INFO: renamed from: component2, reason: from getter */
        public final long getOriginalDurationMs() {
            return this.originalDurationMs;
        }

        /* JADX INFO: renamed from: component3, reason: from getter */
        public final long getProcessedDurationMs() {
            return this.processedDurationMs;
        }

        /* JADX INFO: renamed from: component4, reason: from getter */
        public final float getKeptSpeechRatio() {
            return this.keptSpeechRatio;
        }

        /* JADX INFO: renamed from: component5, reason: from getter */
        public final float getSpeed() {
            return this.speed;
        }

        public final Result copy(File file, long originalDurationMs, long processedDurationMs, float keptSpeechRatio, float speed) {
            Intrinsics.checkNotNullParameter(file, "file");
            return new Result(file, originalDurationMs, processedDurationMs, keptSpeechRatio, speed);
        }

        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof Result)) {
                return false;
            }
            Result result = (Result) other;
            return Intrinsics.areEqual(this.file, result.file) && this.originalDurationMs == result.originalDurationMs && this.processedDurationMs == result.processedDurationMs && Float.compare(this.keptSpeechRatio, result.keptSpeechRatio) == 0 && Float.compare(this.speed, result.speed) == 0;
        }

        public int hashCode() {
            return (((((((this.file.hashCode() * 31) + Long.hashCode(this.originalDurationMs)) * 31) + Long.hashCode(this.processedDurationMs)) * 31) + Float.hashCode(this.keptSpeechRatio)) * 31) + Float.hashCode(this.speed);
        }

        public String toString() {
            return "Result(file=" + this.file + ", originalDurationMs=" + this.originalDurationMs + ", processedDurationMs=" + this.processedDurationMs + ", keptSpeechRatio=" + this.keptSpeechRatio + ", speed=" + this.speed + ")";
        }

        public Result(File file, long originalDurationMs, long processedDurationMs, float keptSpeechRatio, float speed) {
            Intrinsics.checkNotNullParameter(file, "file");
            this.file = file;
            this.originalDurationMs = originalDurationMs;
            this.processedDurationMs = processedDurationMs;
            this.keptSpeechRatio = keptSpeechRatio;
            this.speed = speed;
        }

        public final File getFile() {
            return this.file;
        }

        public final long getOriginalDurationMs() {
            return this.originalDurationMs;
        }

        public final long getProcessedDurationMs() {
            return this.processedDurationMs;
        }

        public final float getKeptSpeechRatio() {
            return this.keptSpeechRatio;
        }

        public final float getSpeed() {
            return this.speed;
        }

        public final float getCompressionRatio() {
            if (this.processedDurationMs <= 0) {
                return 0.0f;
            }
            return this.originalDurationMs / this.processedDurationMs;
        }
    }

    private AsrAudioPreprocessor() {
    }

    public static /* synthetic */ Result prepare$default(AsrAudioPreprocessor asrAudioPreprocessor, File file, File file2, float f, float f2, int i, int i2, int i3, Object obj) {
        return asrAudioPreprocessor.prepare(file, file2, (i3 & 4) != 0 ? 1.35f : f, (i3 & 8) != 0 ? 0.012f : f2, (i3 & 16) != 0 ? 8 : i, (i3 & 32) != 0 ? 4 : i2);
    }

    public final Result prepare(File inputWav, File outputDir, float speed, float silenceRms, int minSilenceFramesToDrop, int padFrames) {
        Intrinsics.checkNotNullParameter(inputWav, "inputWav");
        Intrinsics.checkNotNullParameter(outputDir, "outputDir");
        if (1.0f <= speed && speed <= 2.0f) {
            short[] pcm = readPcm16Mono16k(inputWav);
            long j = 16000;
            long originalMs = (((long) pcm.length) * 1000) / j;
            if (pcm.length == 0) {
                File empty = new File(outputDir, "asr_prep_empty_" + inputWav.getName());
                writeWav(empty, new short[0]);
                return new Result(empty, originalMs, 0L, 0.0f, speed);
            }
            short[] trimmed = trimSilence(pcm, silenceRms, minSilenceFramesToDrop, padFrames);
            float keptRatio = pcm.length == 0 ? 0.0f : trimmed.length / pcm.length;
            short[] sped = speed <= 1.001f ? trimmed : timeCompress(trimmed, speed);
            outputDir.mkdirs();
            File out = new File(outputDir, "asr_prep_" + FilesKt.getNameWithoutExtension(inputWav) + "_" + ((int) (100 * speed)) + "x.wav");
            writeWav(out, sped);
            long processedMs = (((long) sped.length) * 1000) / j;
            String name = inputWav.getName();
            String str = String.format("%.2f", Arrays.copyOf(new Object[]{Float.valueOf(keptRatio)}, 1));
            Intrinsics.checkNotNullExpressionValue(str, "format(...)");
            String str2 = String.format("%.2f", Arrays.copyOf(new Object[]{Float.valueOf(speed)}, 1));
            Intrinsics.checkNotNullExpressionValue(str2, "format(...)");
            String str3 = String.format("%.2f", Arrays.copyOf(new Object[]{Float.valueOf(originalMs / RangesKt.coerceAtLeast(processedMs, 1L))}, 1));
            Intrinsics.checkNotNullExpressionValue(str3, "format(...)");
            Log.i(TAG, "prep " + name + ": origMs=" + originalMs + " keptSpeech=" + str + " speed=" + str2 + " outMs=" + processedMs + " compression=" + str3 + "x bytes " + inputWav.length() + "→" + out.length());
            return new Result(out, originalMs, processedMs, keptRatio, speed);
        }
        throw new IllegalArgumentException(("speed must be in 1.0..2.0, got " + speed).toString());
    }

    public final Result prepareForCloud(File inputWav, File outputDir) {
        Intrinsics.checkNotNullParameter(inputWav, "inputWav");
        Intrinsics.checkNotNullParameter(outputDir, "outputDir");
        return prepare$default(this, inputWav, outputDir, 1.0f, 0.0f, 0, 0, 56, null);
    }

    public static /* synthetic */ List splitByDurationMs$default(AsrAudioPreprocessor asrAudioPreprocessor, File file, File file2, long j, int i, Object obj) {
        if ((i & 4) != 0) {
            j = 120000;
        }
        return asrAudioPreprocessor.splitByDurationMs(file, file2, j);
    }

    public final List<File> splitByDurationMs(File inputWav, File outputDir, long maxDurationMs) {
        ArrayList out;
        Intrinsics.checkNotNullParameter(inputWav, "inputWav");
        Intrinsics.checkNotNullParameter(outputDir, "outputDir");
        if (!(maxDurationMs >= 30000)) {
            throw new IllegalArgumentException(("maxDurationMs too small: " + maxDurationMs).toString());
        }
        short[] pcm = readPcm16Mono16k(inputWav);
        if (pcm.length == 0) {
            return CollectionsKt.emptyList();
        }
        int maxSamples = RangesKt.coerceAtLeast((int) ((((long) 16000) * maxDurationMs) / 1000), 1);
        if (pcm.length <= maxSamples) {
            return CollectionsKt.listOf(inputWav);
        }
        outputDir.mkdirs();
        int silenceSearchRadius = RangesKt.coerceAtMost(240000, maxSamples / 4);
        ArrayList out2 = new ArrayList();
        int offset = 0;
        int idx = 0;
        while (offset < pcm.length) {
            int remaining = pcm.length - offset;
            if (remaining <= maxSamples) {
                short[] slice = ArraysKt.copyOfRange(pcm, offset, pcm.length);
                out = out2;
                File file = new File(outputDir, "asr_chunk_" + FilesKt.getNameWithoutExtension(inputWav) + "_" + StringsKt.padStart(String.valueOf(idx), 3, '0') + ".wav");
                writeWav(file, slice);
                out.add(file);
                Log.i(TAG, "split " + inputWav.getName() + " into " + out.size() + " chunks ≤" + maxDurationMs + "ms (silence-aware)");
                return out;
            }
            int maxSamples2 = maxSamples;
            ArrayList out3 = out2;
            int idx2 = idx;
            int splitAt = RangesKt.coerceIn(findNearestSilenceSplit(pcm, offset + maxSamples2, silenceSearchRadius), (maxSamples2 / 2) + offset, RangesKt.coerceAtMost(offset + maxSamples2, pcm.length));
            short[] slice2 = ArraysKt.copyOfRange(pcm, offset, splitAt);
            File file2 = new File(outputDir, "asr_chunk_" + FilesKt.getNameWithoutExtension(inputWav) + "_" + StringsKt.padStart(String.valueOf(idx2), 3, '0') + ".wav");
            writeWav(file2, slice2);
            out3.add(file2);
            offset = splitAt;
            idx = idx2 + 1;
            maxSamples = maxSamples2;
            out2 = out3;
            pcm = pcm;
        }
        out = out2;
        Log.i(TAG, "split " + inputWav.getName() + " into " + out.size() + " chunks ≤" + maxDurationMs + "ms (silence-aware)");
        return out;
    }

    private final int findNearestSilenceSplit(short[] pcm, int targetSample, int radius) {
        int start = RangesKt.coerceAtLeast(targetSample - radius, 0);
        int end = RangesKt.coerceAtMost(targetSample + radius, pcm.length);
        int best = RangesKt.coerceIn(targetSample, 0, pcm.length);
        float bestRms = Float.MAX_VALUE;
        for (int pos = start; pos + FRAME_SAMPLES <= end; pos += FRAME_SAMPLES) {
            float rms = frameRms(pcm, pos, FRAME_SAMPLES);
            if (rms < 0.012f && rms < bestRms) {
                bestRms = rms;
                best = pos + FRAME_SAMPLES;
            }
        }
        return RangesKt.coerceIn(best, 0, pcm.length);
    }

    private final short[] trimSilence(short[] pcm, float silenceRms, int minSilenceFramesToDrop, int padFrames) {
        int i;
        if (pcm.length < FRAME_SAMPLES) {
            return pcm;
        }
        int length = pcm.length / FRAME_SAMPLES;
        boolean[] zArr = new boolean[length];
        int i2 = 0;
        while (true) {
            boolean z = false;
            if (i2 >= length) {
                break;
            }
            if (frameRms(pcm, i2 * FRAME_SAMPLES, FRAME_SAMPLES) >= silenceRms) {
                z = true;
            }
            zArr[i2] = z;
            i2++;
        }
        boolean[] zArr2 = new boolean[length];
        for (int i3 = 0; i3 < length; i3++) {
            if (zArr[i3]) {
                int iCoerceAtLeast = RangesKt.coerceAtLeast(i3 - padFrames, 0);
                int iCoerceAtMost = RangesKt.coerceAtMost(i3 + padFrames, length - 1);
                int i4 = iCoerceAtLeast;
                if (i4 <= iCoerceAtMost) {
                    while (true) {
                        zArr2[i4] = true;
                        if (i4 != iCoerceAtMost) {
                            i4++;
                        }
                    }
                }
            }
        }
        int i5 = 0;
        while (i5 < length) {
            if (zArr2[i5]) {
                i5++;
            } else {
                int i6 = i5;
                while (i6 < length && !zArr2[i6]) {
                    i6++;
                }
                if (i6 - i5 < minSilenceFramesToDrop) {
                    for (int i7 = i5; i7 < i6; i7++) {
                        zArr2[i7] = true;
                    }
                }
                i5 = i6;
            }
        }
        ArrayList arrayList = new ArrayList(pcm.length);
        for (int i8 = 0; i8 < length; i8++) {
            if (zArr2[i8]) {
                int i9 = i8 * FRAME_SAMPLES;
                for (int i10 = 0; i10 < FRAME_SAMPLES; i10++) {
                    arrayList.add(Short.valueOf(pcm[i9 + i10]));
                }
            }
        }
        int i11 = length * FRAME_SAMPLES;
        if (i11 < pcm.length && (length == 0 || zArr2[length - 1])) {
            int length2 = pcm.length;
            for (int i12 = i11; i12 < length2; i12++) {
                arrayList.add(Short.valueOf(pcm[i12]));
            }
        }
        if (arrayList.isEmpty()) {
            return new short[0];
        }
        int size = arrayList.size();
        short[] sArr = new short[size];
        for (i = 0; i < size; i++) {
            Object obj = arrayList.get(i);
            Intrinsics.checkNotNullExpressionValue(obj, "get(...)");
            sArr[i] = ((Number) obj).shortValue();
        }
        return sArr;
    }

    private final short[] timeCompress(short[] pcm, float speed) {
        int outLen = RangesKt.coerceAtLeast((int) (pcm.length / speed), 1);
        short[] out = new short[outLen];
        for (int i = 0; i < outLen; i++) {
            float src = i * speed;
            int i0 = RangesKt.coerceIn((int) src, 0, pcm.length - 1);
            int i1 = RangesKt.coerceAtMost(i0 + 1, pcm.length - 1);
            float frac = src - i0;
            float v = (pcm[i0] * (1.0f - frac)) + (pcm[i1] * frac);
            out[i] = (short) RangesKt.coerceIn((int) v, -32768, 32767);
        }
        return out;
    }

    private final float frameRms(short[] pcm, int offset, int length) {
        double sum = 0.0d;
        int end = RangesKt.coerceAtMost(offset + length, pcm.length);
        int n = end - offset;
        if (n <= 0) {
            return 0.0f;
        }
        for (int i = offset; i < end; i++) {
            double x = ((double) pcm[i]) / 32768.0d;
            sum += x * x;
        }
        return (float) Math.sqrt(sum / ((double) n));
    }

    private final short[] readPcm16Mono16k(File wav) {
        RandomAccessFile randomAccessFile = new RandomAccessFile(wav, "r");
        try {
            RandomAccessFile randomAccessFile2 = randomAccessFile;
            byte[] bArr = new byte[44];
            if (randomAccessFile2.read(bArr) < 44) {
                short[] sArr = new short[0];
                CloseableKt.closeFinally(randomAccessFile, null);
                return sArr;
            }
            ByteBuffer byteBufferOrder = ByteBuffer.wrap(bArr).order(ByteOrder.LITTLE_ENDIAN);
            int i = 4;
            if (!Intrinsics.areEqual(new String(bArr, 0, 4, Charsets.UTF_8), "RIFF")) {
                throw new IllegalArgumentException(("not RIFF: " + wav.getName()).toString());
            }
            byteBufferOrder.position(22);
            int i2 = byteBufferOrder.getShort();
            int i3 = byteBufferOrder.getInt();
            byteBufferOrder.position(34);
            int i4 = byteBufferOrder.getShort();
            boolean z = true;
            if (i2 != 1 || i3 != 16000 || i4 != 16) {
                z = false;
            }
            if (!z) {
                throw new IllegalArgumentException(("expected 16kHz mono PCM16, got ch=" + i2 + " rate=" + i3 + " bits=" + i4 + " (" + wav.getName() + ")").toString());
            }
            randomAccessFile2.seek(12L);
            int i5 = -1;
            while (true) {
                int i6 = i3;
                if (randomAccessFile2.getFilePointer() + ((long) 8) > randomAccessFile2.length()) {
                    break;
                }
                byte[] bArr2 = new byte[i];
                randomAccessFile2.readFully(bArr2);
                byte[] bArr3 = new byte[i];
                randomAccessFile2.readFully(bArr3);
                int i7 = ByteBuffer.wrap(bArr3).order(ByteOrder.LITTLE_ENDIAN).getInt();
                if (Intrinsics.areEqual(new String(bArr2, Charsets.UTF_8), FileAudioSource.DATA_CHUNK_ID)) {
                    i5 = i7;
                    break;
                }
                randomAccessFile2.seek(randomAccessFile2.getFilePointer() + ((long) i7));
                i4 = i4;
                i3 = i6;
                i = 4;
            }
            if (i5 <= 0) {
                short[] sArr2 = new short[0];
                CloseableKt.closeFinally(randomAccessFile, null);
                return sArr2;
            }
            byte[] bArr4 = new byte[i5];
            randomAccessFile2.readFully(bArr4);
            short[] sArr3 = new short[i5 / 2];
            ByteBuffer.wrap(bArr4).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().get(sArr3);
            CloseableKt.closeFinally(randomAccessFile, null);
            return sArr3;
        } catch (Throwable th) {
            try {
                throw th;
            } catch (Throwable th2) {
                CloseableKt.closeFinally(randomAccessFile, th);
                throw th2;
            }
        }
    }

    private final void writeWav(File file, short[] pcm) {
        int dataSize = pcm.length * 2;
        ByteBuffer header = ByteBuffer.allocate(44).order(ByteOrder.LITTLE_ENDIAN);
        byte[] bytes = "RIFF".getBytes(Charsets.UTF_8);
        Intrinsics.checkNotNullExpressionValue(bytes, "getBytes(...)");
        header.put(bytes);
        header.putInt(dataSize + 36);
        byte[] bytes2 = "WAVE".getBytes(Charsets.UTF_8);
        Intrinsics.checkNotNullExpressionValue(bytes2, "getBytes(...)");
        header.put(bytes2);
        byte[] bytes3 = FileAudioSource.FMT_CHUNK_ID.getBytes(Charsets.UTF_8);
        Intrinsics.checkNotNullExpressionValue(bytes3, "getBytes(...)");
        header.put(bytes3);
        header.putInt(16);
        header.putShort((short) 1);
        header.putShort((short) 1);
        header.putInt(16000);
        header.putInt(32000);
        header.putShort((short) 2);
        header.putShort((short) 16);
        byte[] bytes4 = FileAudioSource.DATA_CHUNK_ID.getBytes(Charsets.UTF_8);
        Intrinsics.checkNotNullExpressionValue(bytes4, "getBytes(...)");
        header.put(bytes4);
        header.putInt(dataSize);
        RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
        try {
            RandomAccessFile randomAccessFile2 = randomAccessFile;
            randomAccessFile2.setLength(0L);
            randomAccessFile2.write(header.array());
            ByteBuffer byteBufferOrder = ByteBuffer.allocate(dataSize).order(ByteOrder.LITTLE_ENDIAN);
            for (short s : pcm) {
                byteBufferOrder.putShort(s);
            }
            randomAccessFile2.write(byteBufferOrder.array());
            Unit unit = Unit.INSTANCE;
            CloseableKt.closeFinally(randomAccessFile, null);
        } catch (Throwable th) {
            try {
                throw th;
            } catch (Throwable th2) {
                CloseableKt.closeFinally(randomAccessFile, th);
                throw th2;
            }
        }
    }
}
