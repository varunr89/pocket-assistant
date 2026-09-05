package com.varun.pocketassistant.speech;

import android.content.Context;
import java.io.File;
import java.io.FileOutputStream;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.io.CloseableKt;
import kotlin.io.FilesKt;
import kotlin.jvm.internal.FloatCompanionObject;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: SpeakerProfile.kt */
/* JADX INFO: loaded from: classes3.dex */
@Metadata(d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u0014\n\u0002\b\u0002\b\u0007\u0018\u00002\u00020\u0001B\u000f\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0004\b\u0004\u0010\u0005J\u0006\u0010\n\u001a\u00020\u000bJ\u0006\u0010\f\u001a\u00020\u0007J\u000e\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u0010J\b\u0010\u0011\u001a\u0004\u0018\u00010\u0010R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0007X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0007X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u0012"}, d2 = {"Lcom/varun/pocketassistant/speech/SpeakerProfileStore;", "", "context", "Landroid/content/Context;", "<init>", "(Landroid/content/Context;)V", "dir", "Ljava/io/File;", "embeddingFile", "enrollmentWav", "hasEnrollment", "", "enrollmentWavFile", "saveEmbedding", "", "embedding", "", "loadEmbedding", "app_debug"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class SpeakerProfileStore {
    public static final int $stable = 8;
    private final File dir;
    private final File embeddingFile;
    private final File enrollmentWav;

    public SpeakerProfileStore(Context context) {
        Intrinsics.checkNotNullParameter(context, "context");
        File file = new File(context.getFilesDir(), "speaker");
        file.mkdirs();
        this.dir = file;
        this.embeddingFile = new File(this.dir, "user_embedding.bin");
        this.enrollmentWav = new File(this.dir, "enrollment.wav");
    }

    public final boolean hasEnrollment() {
        return this.embeddingFile.exists() && this.embeddingFile.length() > 0;
    }

    /* JADX INFO: renamed from: enrollmentWavFile, reason: from getter */
    public final File getEnrollmentWav() {
        return this.enrollmentWav;
    }

    public final void saveEmbedding(float[] embedding) {
        Intrinsics.checkNotNullParameter(embedding, "embedding");
        FileOutputStream fileOutputStream = new FileOutputStream(this.embeddingFile);
        try {
            FileOutputStream fileOutputStream2 = fileOutputStream;
            byte[] bArr = new byte[embedding.length * 4];
            int i = 0;
            for (float f : embedding) {
                int iFloatToIntBits = Float.floatToIntBits(f);
                int i2 = i + 1;
                bArr[i] = (byte) (iFloatToIntBits & 255);
                int i3 = i2 + 1;
                bArr[i2] = (byte) ((iFloatToIntBits >> 8) & 255);
                int i4 = i3 + 1;
                bArr[i3] = (byte) ((iFloatToIntBits >> 16) & 255);
                i = i4 + 1;
                bArr[i4] = (byte) ((iFloatToIntBits >> 24) & 255);
            }
            fileOutputStream2.write(bArr);
            Unit unit = Unit.INSTANCE;
            CloseableKt.closeFinally(fileOutputStream, null);
        } catch (Throwable th) {
            try {
                throw th;
            } catch (Throwable th2) {
                CloseableKt.closeFinally(fileOutputStream, th);
                throw th2;
            }
        }
    }

    public final float[] loadEmbedding() {
        if (!this.embeddingFile.exists()) {
            return null;
        }
        byte[] bytes = FilesKt.readBytes(this.embeddingFile);
        float[] out = new float[bytes.length / 4];
        int i = 0;
        int length = out.length;
        for (int idx = 0; idx < length; idx++) {
            int b0 = bytes[i] & 255;
            int b1 = bytes[i + 1] & 255;
            int b2 = bytes[i + 2] & 255;
            int b3 = bytes[i + 3] & 255;
            FloatCompanionObject floatCompanionObject = FloatCompanionObject.INSTANCE;
            out[idx] = Float.intBitsToFloat((b1 << 8) | b0 | (b2 << 16) | (b3 << 24));
            i += 4;
        }
        return out;
    }
}
