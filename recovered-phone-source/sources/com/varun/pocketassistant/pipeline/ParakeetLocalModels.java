package com.varun.pocketassistant.pipeline;

import android.content.Context;
import androidx.compose.animation.core.AnimationKt;
import com.google.ai.edge.examples.asr.LogMelSpectroConfig;
import com.google.ai.edge.examples.asr.ModelConfig;
import com.google.ai.edge.litert.NpuCompatibilityChecker;
import java.io.File;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: ParakeetAsrEngine.kt */
/* JADX INFO: loaded from: classes8.dex */
@Metadata(d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\bÇ\u0002\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003J\u000e\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\fJ\u000e\u0010\r\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\fJ\u000e\u0010\u000e\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\fJ\u000e\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u000b\u001a\u00020\fJ\u000e\u0010\u0011\u001a\u00020\u00102\u0006\u0010\u000b\u001a\u00020\fJ\u000e\u0010\u0012\u001a\u00020\u00102\u0006\u0010\u000b\u001a\u00020\fJ\u000e\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u000b\u001a\u00020\fR\u000e\u0010\u0004\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000¨\u0006\u0015"}, d2 = {"Lcom/varun/pocketassistant/pipeline/ParakeetLocalModels;", "", "<init>", "()V", "REL_DIR", "", "CPU_MODEL", "NPU_MODEL", "TOKENIZER", "dir", "Ljava/io/File;", "context", "Landroid/content/Context;", "tokenizerFile", "npuModel", "hasTokenizer", "", "hasNpuModel", "isAvailable", "modelConfig", "Lcom/google/ai/edge/examples/asr/ModelConfig;", "app_debug"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class ParakeetLocalModels {
    public static final int $stable = 0;
    public static final String CPU_MODEL = "model.tflite";
    public static final ParakeetLocalModels INSTANCE = new ParakeetLocalModels();
    public static final String NPU_MODEL = "model_npu.tflite";
    public static final String REL_DIR = "models/parakeet";
    public static final String TOKENIZER = "tokenizer.json";

    private ParakeetLocalModels() {
    }

    public final File dir(Context context) {
        Intrinsics.checkNotNullParameter(context, "context");
        return new File(context.getFilesDir(), REL_DIR);
    }

    public final File tokenizerFile(Context context) {
        Intrinsics.checkNotNullParameter(context, "context");
        return new File(dir(context), "tokenizer.json");
    }

    public final File npuModel(Context context) {
        Intrinsics.checkNotNullParameter(context, "context");
        return new File(dir(context), "model_npu.tflite");
    }

    public final boolean hasTokenizer(Context context) {
        Intrinsics.checkNotNullParameter(context, "context");
        return tokenizerFile(context).exists() && tokenizerFile(context).length() > 1000;
    }

    public final boolean hasNpuModel(Context context) {
        Intrinsics.checkNotNullParameter(context, "context");
        return npuModel(context).exists() && npuModel(context).length() > AnimationKt.MillisToNanos;
    }

    public final boolean isAvailable(Context context) {
        Intrinsics.checkNotNullParameter(context, "context");
        return hasTokenizer(context) && hasNpuModel(context) && NpuCompatibilityChecker.INSTANCE.getGoogleTensor().isDeviceSupported();
    }

    public final ModelConfig modelConfig(Context context) {
        Intrinsics.checkNotNullParameter(context, "context");
        return new ModelConfig("models/parakeet/model.tflite", "", "models/parakeet/model_npu.tflite", "", "models/parakeet/tokenizer.json", "", 5000, new LogMelSpectroConfig(512, 128, 0, 500, false, 0.97f, (String) null, 84, (DefaultConstructorMarker) null), true, 8192, -1, -1, true, true, true);
    }
}
