package com.varun.pocketassistant.pipeline;

import android.content.Context;
import android.os.Build;
import android.system.Os;
import android.util.Log;
import androidx.compose.animation.core.AnimationKt;
import com.google.ai.edge.examples.asr.FileAudioSource;
import com.google.ai.edge.examples.asr.HuggingfaceTokenizer;
import com.google.ai.edge.examples.asr.LevenshteinTokenMerger;
import com.google.ai.edge.examples.asr.LiteRtRunner;
import com.google.ai.edge.examples.asr.LogMelSpectroConfig;
import com.google.ai.edge.examples.asr.MelSpectroProcessor;
import com.google.ai.edge.examples.asr.ModelConfig;
import com.google.ai.edge.examples.asr.Postprocessor;
import com.google.ai.edge.examples.asr.TdtDecoder;
import com.google.ai.edge.litert.Accelerator;
import com.google.ai.edge.litert.BuiltinNpuAcceleratorProvider;
import com.google.ai.edge.litert.CompiledModel;
import com.google.ai.edge.litert.LiteRtException;
import com.google.ai.edge.litert.NpuCompatibilityChecker;
import com.varun.pocketassistant.pipeline.work.MeetingStageWorker;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import kotlin.Metadata;
import kotlin.Pair;
import kotlin.Result;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.io.CloseableKt;
import kotlin.io.FilesKt;
import kotlin.jdk7.AutoCloseableKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlin.sequences.Sequence;
import kotlin.sequences.SequencesKt;
import kotlin.text.StringsKt;
import kotlin.time.Duration;
import kotlin.time.DurationKt;
import kotlin.time.DurationUnit;

/* JADX INFO: compiled from: ParakeetAsrEngine.kt */
/* JADX INFO: loaded from: classes8.dex */
@Metadata(d1 = {"\u0000\\\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0003\n\u0002\b\u0002\b\u0007\u0018\u0000 \"2\u00060\u0001j\u0002`\u0002:\u0001\"B\u000f\u0012\u0006\u0010\u0003\u001a\u00020\u0004¢\u0006\u0004\b\u0005\u0010\u0006J\u0006\u0010\u0016\u001a\u00020\u0017J\u000e\u0010\u0018\u001a\u00020\u00132\u0006\u0010\u0019\u001a\u00020\u001aJ\b\u0010\u001b\u001a\u00020\u0017H\u0016J\u001c\u0010\u001c\u001a\u00060\u001dj\u0002`\u001e2\u0006\u0010\u001f\u001a\u00020\u00132\u0006\u0010 \u001a\u00020!H\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u0016\u0010\u0007\u001a\n \b*\u0004\u0018\u00010\u00040\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\t\u001a\u0004\u0018\u00010\nX\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u000b\u001a\u0004\u0018\u00010\fX\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\r\u001a\u0004\u0018\u00010\u000eX\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u000f\u001a\u00020\u0010X\u0082\u000e¢\u0006\u0004\n\u0002\u0010\u0011R\u0014\u0010\u0012\u001a\u00020\u0013X\u0086D¢\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0015¨\u0006#"}, d2 = {"Lcom/varun/pocketassistant/pipeline/ParakeetAsrEngine;", "Ljava/lang/AutoCloseable;", "Lkotlin/AutoCloseable;", "context", "Landroid/content/Context;", "<init>", "(Landroid/content/Context;)V", "appContext", "kotlin.jvm.PlatformType", "recognizer", "Lcom/google/ai/edge/examples/asr/LiteRtRunner;", "tokenizer", "Lcom/google/ai/edge/examples/asr/HuggingfaceTokenizer;", "preprocessor", "Lcom/google/ai/edge/examples/asr/MelSpectroProcessor;", "modelInputInterval", "Lkotlin/time/Duration;", "J", "providerLabel", "", "getProviderLabel", "()Ljava/lang/String;", "ensureLoaded", "", "transcribe", "wav", "Ljava/io/File;", "close", "enrich", "Ljava/lang/IllegalStateException;", "Lkotlin/IllegalStateException;", MeetingStageWorker.KEY_STAGE, "t", "", "Companion", "app_debug"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class ParakeetAsrEngine implements AutoCloseable {
    private static final long FILE_AUDIO_CHUNK_OVERLAP_DURATION;
    private static final int FILE_MAX_LEVENSHTEIN_DISTANCE = 5;
    private static final int SAMPLING_RATE = 16000;
    private static final String TAG = "ParakeetAsrEngine";
    private final Context appContext;
    private final Context context;
    private long modelInputInterval;
    private MelSpectroProcessor preprocessor;
    private final String providerLabel;
    private LiteRtRunner recognizer;
    private HuggingfaceTokenizer tokenizer;
    public static final int $stable = 8;

    public ParakeetAsrEngine(Context context) {
        Intrinsics.checkNotNullParameter(context, "context");
        this.context = context;
        this.appContext = this.context.getApplicationContext();
        Duration.Companion companion = Duration.INSTANCE;
        this.modelInputInterval = DurationKt.toDuration(5, DurationUnit.SECONDS);
        this.providerLabel = "parakeet_tdt_npu";
    }

    public final String getProviderLabel() {
        return this.providerLabel;
    }

    public final void ensureLoaded() {
        Object objM8304constructorimpl;
        Object objM8304constructorimpl2;
        if (this.recognizer != null) {
            return;
        }
        ParakeetLocalModels parakeetLocalModels = ParakeetLocalModels.INSTANCE;
        Context appContext = this.appContext;
        Intrinsics.checkNotNullExpressionValue(appContext, "appContext");
        if (!parakeetLocalModels.isAvailable(appContext)) {
            String str = Build.SOC_MODEL;
            ParakeetLocalModels parakeetLocalModels2 = ParakeetLocalModels.INSTANCE;
            Context appContext2 = this.appContext;
            Intrinsics.checkNotNullExpressionValue(appContext2, "appContext");
            boolean zHasNpuModel = parakeetLocalModels2.hasNpuModel(appContext2);
            ParakeetLocalModels parakeetLocalModels3 = ParakeetLocalModels.INSTANCE;
            Context appContext3 = this.appContext;
            Intrinsics.checkNotNullExpressionValue(appContext3, "appContext");
            throw new IllegalArgumentException(("Parakeet Tensor G5 NPU pack missing or device unsupported (soc=" + str + ", npuModel=" + zHasNpuModel + ", tokenizer=" + parakeetLocalModels3.hasTokenizer(appContext3) + ", googleTensor=" + NpuCompatibilityChecker.INSTANCE.getGoogleTensor().isDeviceSupported() + ")").toString());
        }
        ParakeetLocalModels parakeetLocalModels4 = ParakeetLocalModels.INSTANCE;
        Context appContext4 = this.appContext;
        Intrinsics.checkNotNullExpressionValue(appContext4, "appContext");
        String npuPath = parakeetLocalModels4.npuModel(appContext4).getAbsolutePath();
        String nativeLibDir = this.appContext.getApplicationInfo().nativeLibraryDir;
        Context appContext5 = this.appContext;
        Intrinsics.checkNotNullExpressionValue(appContext5, "appContext");
        BuiltinNpuAcceleratorProvider provider = new BuiltinNpuAcceleratorProvider(appContext5, null, 2, null);
        Log.i(TAG, "NPU load: soc=" + Build.SOC_MODEL + " model=" + npuPath + " size=" + new File(npuPath).length() + " nativeLibDir=" + nativeLibDir + " providerReady=" + provider.isLibraryReady() + " deviceSupported=" + provider.isDeviceSupported() + " dispatchSo=" + new File(nativeLibDir, "libLiteRtDispatch_GoogleTensor.so").exists());
        try {
            Result.Companion companion = Result.INSTANCE;
            ParakeetAsrEngine parakeetAsrEngine = this;
            Os.setenv("ADSP_LIBRARY_PATH", nativeLibDir, true);
            Os.setenv("LD_LIBRARY_PATH", nativeLibDir, true);
            objM8304constructorimpl = Result.m8304constructorimpl(Unit.INSTANCE);
        } catch (Throwable th) {
            Result.Companion companion2 = Result.INSTANCE;
            objM8304constructorimpl = Result.m8304constructorimpl(ResultKt.createFailure(th));
        }
        Throwable thM8307exceptionOrNullimpl = Result.m8307exceptionOrNullimpl(objM8304constructorimpl);
        if (thM8307exceptionOrNullimpl != null) {
            Log.w(TAG, "setenv native lib path failed", thM8307exceptionOrNullimpl);
        }
        try {
            Result.Companion companion3 = Result.INSTANCE;
            ParakeetAsrEngine parakeetAsrEngine2 = this;
            System.loadLibrary("LiteRtDispatch_GoogleTensor");
            objM8304constructorimpl2 = Result.m8304constructorimpl(Integer.valueOf(Log.i(TAG, "Loaded libLiteRtDispatch_GoogleTensor")));
        } catch (Throwable th2) {
            Result.Companion companion4 = Result.INSTANCE;
            objM8304constructorimpl2 = Result.m8304constructorimpl(ResultKt.createFailure(th2));
        }
        Throwable thM8307exceptionOrNullimpl2 = Result.m8307exceptionOrNullimpl(objM8304constructorimpl2);
        if (thM8307exceptionOrNullimpl2 != null) {
            Log.w(TAG, "Explicit load of LiteRtDispatch_GoogleTensor failed: " + thM8307exceptionOrNullimpl2.getMessage());
        }
        ParakeetLocalModels parakeetLocalModels5 = ParakeetLocalModels.INSTANCE;
        Context appContext6 = this.appContext;
        Intrinsics.checkNotNullExpressionValue(appContext6, "appContext");
        ModelConfig config = parakeetLocalModels5.modelConfig(appContext6);
        Context appContext7 = this.appContext;
        Intrinsics.checkNotNullExpressionValue(appContext7, "appContext");
        this.tokenizer = new HuggingfaceTokenizer(appContext7, config);
        LogMelSpectroConfig logMelSpectro = config.getLogMelSpectro();
        Intrinsics.checkNotNull(logMelSpectro);
        this.preprocessor = new MelSpectroProcessor(16000, logMelSpectro, null, 4, null);
        Duration.Companion companion5 = Duration.INSTANCE;
        this.modelInputInterval = DurationKt.toDuration(config.getInputMilliseconds(), DurationUnit.MILLISECONDS);
        try {
            Context appContext8 = this.appContext;
            Intrinsics.checkNotNullExpressionValue(appContext8, "appContext");
            this.recognizer = new LiteRtRunner(appContext8, config, Accelerator.NPU, true, new Function2() { // from class: com.varun.pocketassistant.pipeline.ParakeetAsrEngine$$ExternalSyntheticLambda1
                @Override // kotlin.jvm.functions.Function2
                public final Object invoke(Object obj, Object obj2) {
                    return ParakeetAsrEngine.ensureLoaded$lambda$5((CompiledModel) obj, (ModelConfig) obj2);
                }
            });
            Log.i(TAG, "CompiledModel created on NPU (litert-samples path, npuOnly)");
        } catch (Throwable t) {
            throw enrich("create CompiledModel(NPU)", t);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final LiteRtRunner.Decoder ensureLoaded$lambda$5(CompiledModel model, ModelConfig cfg) {
        Intrinsics.checkNotNullParameter(model, "model");
        Intrinsics.checkNotNullParameter(cfg, "cfg");
        return new TdtDecoder(model, cfg);
    }

    public final String transcribe(File wav) throws Throwable {
        FileInputStream fileInputStream;
        Throwable th;
        FileAudioSource fileAudioSource;
        Throwable th2;
        Throwable th3;
        Throwable th4;
        Throwable th5;
        MelSpectroProcessor mel;
        FileInputStream fileInputStream2;
        double originalDurationMs;
        Collection collection;
        List<Pair> listEmptyList;
        Postprocessor.DecodedText decodedTextDecode;
        List list;
        ParakeetAsrEngine parakeetAsrEngine = this;
        String str = "window=";
        Intrinsics.checkNotNullParameter(wav, "wav");
        parakeetAsrEngine.ensureLoaded();
        MelSpectroProcessor mel2 = parakeetAsrEngine.preprocessor;
        Intrinsics.checkNotNull(mel2);
        HuggingfaceTokenizer tok = parakeetAsrEngine.tokenizer;
        Intrinsics.checkNotNull(tok);
        long overlap = FILE_AUDIO_CHUNK_OVERLAP_DURATION;
        float overlapRatio = (float) Duration.m9676divLRDsOJo(overlap, parakeetAsrEngine.modelInputInterval);
        long startedAt = System.nanoTime();
        File file = new File(parakeetAsrEngine.appContext.getCacheDir(), "asr_prep/" + FilesKt.getNameWithoutExtension(wav) + "_" + System.nanoTime());
        file.mkdirs();
        AsrAudioPreprocessor.Result prep = AsrAudioPreprocessor.prepare$default(AsrAudioPreprocessor.INSTANCE, wav, file, 1.35f, 0.0f, 0, 0, 56, null);
        if (prep.getProcessedDurationMs() < 400) {
            Log.i(TAG, "Transcribe skip: too little speech after prep (" + prep.getProcessedDurationMs() + "ms)");
            FilesKt.deleteRecursively(file);
            return "";
        }
        String absolutePath = wav.getAbsolutePath();
        long length = wav.length();
        long processedDurationMs = prep.getProcessedDurationMs();
        long originalDurationMs2 = prep.getOriginalDurationMs();
        String str2 = String.format("%.2f", Arrays.copyOf(new Object[]{Float.valueOf(prep.getKeptSpeechRatio())}, 1));
        Intrinsics.checkNotNullExpressionValue(str2, "format(...)");
        Log.i(TAG, "Transcribe start wav=" + absolutePath + " bytes=" + length + " prepMs=" + processedDurationMs + " origMs=" + originalDurationMs2 + " kept=" + str2 + " speed=" + prep.getSpeed() + " overlap=" + Duration.m9715toStringimpl(overlap));
        try {
            try {
                FileInputStream fileInputStream3 = new FileInputStream(prep.getFile());
                try {
                    long overlap2 = overlap;
                    try {
                        FileAudioSource fileAudioSource2 = new FileAudioSource(fileInputStream3, 16000, parakeetAsrEngine.modelInputInterval, overlap2, null);
                        try {
                            FileAudioSource fileAudioSource3 = fileAudioSource2;
                            try {
                                LevenshteinTokenMerger levenshteinTokenMerger = new LevenshteinTokenMerger(tok, overlapRatio, 0, 5, 0.0f, 20, null);
                                try {
                                    LevenshteinTokenMerger levenshteinTokenMerger2 = levenshteinTokenMerger;
                                    List arrayList = new ArrayList();
                                    int i = 0;
                                    Iterator<float[]> it = fileAudioSource3.getAudioData().iterator();
                                    int i2 = 0;
                                    String unconfirmedText = "";
                                    while (true) {
                                        try {
                                            long overlap3 = overlap2;
                                            if (!it.hasNext()) {
                                                break;
                                            }
                                            try {
                                                float[] next = it.next();
                                                int i3 = i + 1;
                                                float[] fArrCopyOf = Arrays.copyOf(next, next.length);
                                                Intrinsics.checkNotNullExpressionValue(fArrCopyOf, "copyOf(...)");
                                                float[] fArrProcess = mel2.process(fArrCopyOf);
                                                try {
                                                    LiteRtRunner liteRtRunner = parakeetAsrEngine.recognizer;
                                                    Intrinsics.checkNotNull(liteRtRunner);
                                                    listEmptyList = SequencesKt.toList(liteRtRunner.recognize(fArrProcess));
                                                } catch (Throwable th6) {
                                                    int i4 = i2 + 1;
                                                    Log.e(TAG, str + i3 + " recognize failed (reload+continue): " + th6.getMessage(), th6);
                                                    try {
                                                        LiteRtRunner liteRtRunner2 = parakeetAsrEngine.recognizer;
                                                        if (liteRtRunner2 != null) {
                                                            liteRtRunner2.close();
                                                            Unit unit = Unit.INSTANCE;
                                                        }
                                                    } catch (Throwable th7) {
                                                    }
                                                    parakeetAsrEngine.recognizer = null;
                                                    parakeetAsrEngine.ensureLoaded();
                                                    listEmptyList = CollectionsKt.emptyList();
                                                    i2 = i4;
                                                }
                                                for (Pair pair : listEmptyList) {
                                                    i2 = i2;
                                                    try {
                                                        decodedTextDecode = levenshteinTokenMerger2.decode(((Number) pair.component1()).intValue(), ((Number) pair.component2()).intValue());
                                                    } catch (IllegalArgumentException e) {
                                                        Log.w(TAG, str + i3 + " merger skipped: " + e.getMessage());
                                                        decodedTextDecode = null;
                                                    }
                                                    if (decodedTextDecode != null) {
                                                        if (StringsKt.isBlank(decodedTextDecode.getConfirmedText())) {
                                                            list = arrayList;
                                                        } else {
                                                            list = arrayList;
                                                            list.add(decodedTextDecode.getConfirmedText());
                                                        }
                                                        unconfirmedText = decodedTextDecode.getUnconfirmedText();
                                                        arrayList = list;
                                                    }
                                                }
                                                parakeetAsrEngine = this;
                                                i = i3;
                                                overlap2 = overlap3;
                                                str = str;
                                            } catch (Throwable th8) {
                                                th4 = th8;
                                                fileInputStream = fileInputStream3;
                                                fileAudioSource = fileAudioSource2;
                                            }
                                            th4 = th8;
                                            fileInputStream = fileInputStream3;
                                            fileAudioSource = fileAudioSource2;
                                        } catch (Throwable th9) {
                                            fileInputStream = fileInputStream3;
                                            fileAudioSource = fileAudioSource2;
                                            th4 = th9;
                                        }
                                        try {
                                            throw th4;
                                        } catch (Throwable th10) {
                                            try {
                                                AutoCloseableKt.closeFinally(levenshteinTokenMerger, th4);
                                                throw th10;
                                            } catch (Throwable th11) {
                                                th5 = th11;
                                                th2 = th5;
                                                try {
                                                    throw th2;
                                                } catch (Throwable th12) {
                                                    try {
                                                        AutoCloseableKt.closeFinally(fileAudioSource, th2);
                                                        throw th12;
                                                    } catch (Throwable th13) {
                                                        th3 = th13;
                                                        th = th3;
                                                        try {
                                                            throw th;
                                                        } catch (Throwable th14) {
                                                            CloseableKt.closeFinally(fileInputStream, th);
                                                            throw th14;
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    Object obj = arrayList;
                                    try {
                                        List mutableList = CollectionsKt.toMutableList((Collection) obj);
                                        if (!StringsKt.isBlank(unconfirmedText)) {
                                            mutableList.add(unconfirmedText);
                                        }
                                        Collection arrayList2 = new ArrayList();
                                        for (Object obj2 : mutableList) {
                                            if (StringsKt.isBlank((String) obj2)) {
                                                collection = arrayList2;
                                            } else {
                                                collection = arrayList2;
                                                collection.add(obj2);
                                            }
                                            obj = obj;
                                            arrayList2 = collection;
                                            mutableList = mutableList;
                                        }
                                        String string = StringsKt.trim((CharSequence) CollectionsKt.joinToString$default((List) arrayList2, " ", null, null, 0, null, null, 62, null)).toString();
                                        long jNanoTime = (System.nanoTime() - startedAt) / AnimationKt.MillisToNanos;
                                        double processedDurationMs2 = -1.0d;
                                        if (prep.getOriginalDurationMs() > 0) {
                                            mel = mel2;
                                            fileInputStream2 = fileInputStream3;
                                            try {
                                                originalDurationMs = jNanoTime / prep.getOriginalDurationMs();
                                            } catch (Throwable th15) {
                                                th4 = th15;
                                                fileAudioSource = fileAudioSource2;
                                                fileInputStream = fileInputStream2;
                                            }
                                        } else {
                                            mel = mel2;
                                            fileInputStream2 = fileInputStream3;
                                            originalDurationMs = -1.0d;
                                        }
                                        try {
                                            if (prep.getProcessedDurationMs() > 0) {
                                                processedDurationMs2 = jNanoTime / prep.getProcessedDurationMs();
                                            }
                                            int length2 = string.length();
                                            fileAudioSource = fileAudioSource2;
                                            try {
                                                long originalDurationMs3 = prep.getOriginalDurationMs();
                                                try {
                                                    long processedDurationMs3 = prep.getProcessedDurationMs();
                                                    String str3 = String.format("%.3f", Arrays.copyOf(new Object[]{Double.valueOf(originalDurationMs)}, 1));
                                                    Intrinsics.checkNotNullExpressionValue(str3, "format(...)");
                                                    String str4 = String.format("%.3f", Arrays.copyOf(new Object[]{Double.valueOf(processedDurationMs2)}, 1));
                                                    Intrinsics.checkNotNullExpressionValue(str4, "format(...)");
                                                    Log.i(TAG, "Transcribe done chars=" + length2 + " windows=" + i + " flakes=" + i2 + " wallMs=" + jNanoTime + " origMs=" + originalDurationMs3 + " prepMs=" + processedDurationMs3 + " rtfOrig=" + str3 + " rtfPrep=" + str4 + " (rtfOrig=wall/original; <0.5 means >=2x vs meeting time)");
                                                    try {
                                                        AutoCloseableKt.closeFinally(levenshteinTokenMerger, null);
                                                        try {
                                                            AutoCloseableKt.closeFinally(fileAudioSource, null);
                                                            CloseableKt.closeFinally(fileInputStream2, null);
                                                            FilesKt.deleteRecursively(file);
                                                            return string;
                                                        } catch (Throwable th16) {
                                                            th3 = th16;
                                                            fileInputStream = fileInputStream2;
                                                            th = th3;
                                                            throw th;
                                                        }
                                                    } catch (Throwable th17) {
                                                        th5 = th17;
                                                        fileInputStream = fileInputStream2;
                                                        fileAudioSource = fileAudioSource;
                                                        th2 = th5;
                                                        throw th2;
                                                    }
                                                } catch (Throwable th18) {
                                                    fileInputStream = fileInputStream2;
                                                    levenshteinTokenMerger = levenshteinTokenMerger;
                                                    fileAudioSource = fileAudioSource;
                                                    th4 = th18;
                                                    throw th4;
                                                }
                                            } catch (Throwable th19) {
                                                fileInputStream = fileInputStream2;
                                                th4 = th19;
                                            }
                                        } catch (Throwable th20) {
                                            fileAudioSource = fileAudioSource2;
                                            fileInputStream = fileInputStream2;
                                            th4 = th20;
                                        }
                                    } catch (Throwable th21) {
                                        fileInputStream = fileInputStream3;
                                        fileAudioSource = fileAudioSource2;
                                        th4 = th21;
                                    }
                                } catch (Throwable th22) {
                                    fileInputStream = fileInputStream3;
                                    fileAudioSource = fileAudioSource2;
                                    th4 = th22;
                                }
                            } catch (Throwable th23) {
                                fileInputStream = fileInputStream3;
                                fileAudioSource = fileAudioSource2;
                                th2 = th23;
                            }
                        } catch (Throwable th24) {
                            fileInputStream = fileInputStream3;
                            fileAudioSource = fileAudioSource2;
                            th2 = th24;
                        }
                    } catch (Throwable th25) {
                        fileInputStream = fileInputStream3;
                        th = th25;
                    }
                } catch (Throwable th26) {
                    fileInputStream = fileInputStream3;
                    th = th26;
                }
            } catch (Throwable th27) {
                th = th27;
                FilesKt.deleteRecursively(file);
                throw th;
            }
        } catch (Throwable th28) {
            th = th28;
            FilesKt.deleteRecursively(file);
            throw th;
        }
    }

    @Override // java.lang.AutoCloseable
    public void close() {
        LiteRtRunner liteRtRunner = this.recognizer;
        if (liteRtRunner != null) {
            liteRtRunner.close();
        }
        HuggingfaceTokenizer huggingfaceTokenizer = this.tokenizer;
        if (huggingfaceTokenizer != null) {
            huggingfaceTokenizer.close();
        }
        MelSpectroProcessor melSpectroProcessor = this.preprocessor;
        if (melSpectroProcessor != null) {
            melSpectroProcessor.close();
        }
        this.recognizer = null;
        this.tokenizer = null;
        this.preprocessor = null;
    }

    private final IllegalStateException enrich(String stage, Throwable t) {
        Sequence sequenceFilter = SequencesKt.filter(SequencesKt.generateSequence(t, (Function1<? super Throwable, ? extends Throwable>) new Function1() { // from class: com.varun.pocketassistant.pipeline.ParakeetAsrEngine$$ExternalSyntheticLambda0
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return ParakeetAsrEngine.enrich$lambda$11((Throwable) obj);
            }
        }), new Function1<Object, Boolean>() { // from class: com.varun.pocketassistant.pipeline.ParakeetAsrEngine$enrich$$inlined$filterIsInstance$1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // kotlin.jvm.functions.Function1
            public final Boolean invoke(Object it) {
                return Boolean.valueOf(it instanceof LiteRtException);
            }
        });
        Intrinsics.checkNotNull(sequenceFilter, "null cannot be cast to non-null type kotlin.sequences.Sequence<R of kotlin.sequences.SequencesKt___SequencesKt.filterIsInstance>");
        LiteRtException litert = (LiteRtException) SequencesKt.firstOrNull(sequenceFilter);
        StringBuilder sb = new StringBuilder();
        sb.append("Parakeet NPU failed at " + stage + ": " + t.getMessage());
        if (litert != null) {
            sb.append(" | LiteRtException status=" + litert.getMessage());
        }
        sb.append(" | soc=" + Build.SOC_MODEL);
        ParakeetLocalModels parakeetLocalModels = ParakeetLocalModels.INSTANCE;
        Context appContext = this.appContext;
        Intrinsics.checkNotNullExpressionValue(appContext, "appContext");
        sb.append(" | model=" + parakeetLocalModels.npuModel(appContext).getName());
        String detail = sb.toString();
        Log.e(TAG, detail, t);
        return new IllegalStateException(detail, t);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Throwable enrich$lambda$11(Throwable it) {
        Intrinsics.checkNotNullParameter(it, "it");
        return it.getCause();
    }

    static {
        Duration.Companion companion = Duration.INSTANCE;
        FILE_AUDIO_CHUNK_OVERLAP_DURATION = DurationKt.toDuration(2, DurationUnit.SECONDS);
    }
}
