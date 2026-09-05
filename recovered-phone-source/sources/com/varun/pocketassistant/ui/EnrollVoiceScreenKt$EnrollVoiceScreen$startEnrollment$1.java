package com.varun.pocketassistant.ui;

import android.media.AudioRecord;
import androidx.compose.runtime.MutableIntState;
import androidx.compose.runtime.MutableState;
import com.varun.pocketassistant.capture.WavWriter;
import com.varun.pocketassistant.speech.SpeakerProfileStore;
import com.varun.pocketassistant.speech.VoiceEmbedder;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import kotlin.Metadata;
import kotlin.Result;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.Boxing;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SpillingKt;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlin.ranges.RangesKt;
import kotlinx.coroutines.BuildersKt;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.CoroutineScopeKt;
import kotlinx.coroutines.Dispatchers;
import kotlinx.coroutines.MainCoroutineDispatcher;

/* JADX INFO: compiled from: EnrollVoiceScreen.kt */
/* JADX INFO: loaded from: classes4.dex */
@Metadata(d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H\n"}, d2 = {"<anonymous>", "", "Lkotlinx/coroutines/CoroutineScope;"}, k = 3, mv = {2, 2, 0}, xi = 48)
@DebugMetadata(c = "com.varun.pocketassistant.ui.EnrollVoiceScreenKt$EnrollVoiceScreen$startEnrollment$1", f = "EnrollVoiceScreen.kt", i = {0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2}, l = {103, 122, 148}, m = "invokeSuspend", n = {"$this$launch", "out", "recorder", "minBuf", "$this$launch", "out", "recorder", "writer", "pcm", "buf", "minBuf", "endAt", "n", "left", "$this$launch", "out", "recorder", "writer", "pcm", "buf", "samples", "filtered", "speechish", "embedding", "minBuf", "endAt"}, s = {"L$0", "L$1", "L$2", "I$0", "L$0", "L$1", "L$2", "L$3", "L$4", "L$5", "I$0", "J$0", "I$1", "I$2", "L$0", "L$1", "L$2", "L$3", "L$4", "L$5", "L$6", "L$7", "L$8", "L$9", "I$0", "J$0"})
final class EnrollVoiceScreenKt$EnrollVoiceScreen$startEnrollment$1 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
    final /* synthetic */ MutableState<String> $error$delegate;
    final /* synthetic */ MutableState<Boolean> $recording$delegate;
    final /* synthetic */ MutableIntState $secondsLeft$delegate;
    final /* synthetic */ MutableState<String> $status$delegate;
    final /* synthetic */ SpeakerProfileStore $store;
    int I$0;
    int I$1;
    int I$2;
    long J$0;
    private /* synthetic */ Object L$0;
    Object L$1;
    Object L$2;
    Object L$3;
    Object L$4;
    Object L$5;
    Object L$6;
    Object L$7;
    Object L$8;
    Object L$9;
    int label;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    EnrollVoiceScreenKt$EnrollVoiceScreen$startEnrollment$1(SpeakerProfileStore speakerProfileStore, MutableState<Boolean> mutableState, MutableState<String> mutableState2, MutableIntState mutableIntState, MutableState<String> mutableState3, Continuation<? super EnrollVoiceScreenKt$EnrollVoiceScreen$startEnrollment$1> continuation) {
        super(2, continuation);
        this.$store = speakerProfileStore;
        this.$recording$delegate = mutableState;
        this.$error$delegate = mutableState2;
        this.$secondsLeft$delegate = mutableIntState;
        this.$status$delegate = mutableState3;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
        EnrollVoiceScreenKt$EnrollVoiceScreen$startEnrollment$1 enrollVoiceScreenKt$EnrollVoiceScreen$startEnrollment$1 = new EnrollVoiceScreenKt$EnrollVoiceScreen$startEnrollment$1(this.$store, this.$recording$delegate, this.$error$delegate, this.$secondsLeft$delegate, this.$status$delegate, continuation);
        enrollVoiceScreenKt$EnrollVoiceScreen$startEnrollment$1.L$0 = obj;
        return enrollVoiceScreenKt$EnrollVoiceScreen$startEnrollment$1;
    }

    @Override // kotlin.jvm.functions.Function2
    public final Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
        return ((EnrollVoiceScreenKt$EnrollVoiceScreen$startEnrollment$1) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
    }

    /* JADX WARN: Code duplicated, block: B:24:0x012a A[Catch: all -> 0x034d, TryCatch #4 {all -> 0x034d, blocks: (B:22:0x0123, B:24:0x012a, B:26:0x0132, B:34:0x015d), top: B:104:0x0123 }] */
    /* JADX WARN: Code duplicated, block: B:44:0x01a9 A[RETURN] */
    /* JADX WARN: Code duplicated, block: B:45:0x01aa  */
    /* JADX WARN: Code duplicated, block: B:60:0x023b  */
    /* JADX WARN: Code duplicated, block: B:62:0x0251  */
    /* JADX WARN: Code duplicated, block: B:63:0x0259  */
    /* JADX WARN: Code duplicated, block: B:65:0x0267 A[LOOP:2: B:64:0x0265->B:65:0x0267, LOOP_END] */
    /* JADX WARN: Code duplicated, block: B:68:0x0290  */
    /* JADX WARN: Code duplicated, block: B:69:0x0292  */
    /* JADX WARN: Code duplicated, block: B:72:0x0296  */
    /* JADX WARN: Code duplicated, block: B:73:0x02a1  */
    /* JADX WARN: Code duplicated, block: B:77:0x02c3  */
    /* JADX WARN: Code duplicated, block: B:78:0x02c6  */
    /* JADX WARN: Code duplicated, block: B:80:0x02ca  */
    /* JADX WARN: Code duplicated, block: B:81:0x02cc  */
    /* JADX WARN: Code duplicated, block: B:84:0x033f A[RETURN] */
    /* JADX WARN: Code duplicated, block: B:85:0x0340  */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:45:0x01aa -> B:46:0x01b3). Please report as a decompilation issue!!! */
    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Object invokeSuspend(Object $result) throws Throwable {
        int left;
        ArrayList pcm;
        long endAt;
        AudioRecord recorder;
        File out;
        short[] buf;
        Object obj;
        WavWriter writer;
        CoroutineScope $this$launch;
        Object $result2;
        AudioRecord recorder2;
        WavWriter writer2;
        Throwable th;
        short[] samples;
        Collection arrayList;
        short[] sArr;
        int i;
        short[] sArr2;
        int length;
        int i2;
        short[] filtered;
        boolean z;
        short[] sArr3;
        short s;
        int i3;
        short[] sArr4;
        int iMin;
        double d;
        int i4;
        short[] sArr5;
        int i5;
        CoroutineScope $this$launch2;
        MainCoroutineDispatcher main;
        Object $result3;
        AnonymousClass2 anonymousClass2;
        EnrollVoiceScreenKt$EnrollVoiceScreen$startEnrollment$1 enrollVoiceScreenKt$EnrollVoiceScreen$startEnrollment$1;
        int n;
        File out2;
        int minBuf;
        CoroutineScope $this$launch3;
        CoroutineScope $this$launch4 = (CoroutineScope) this.L$0;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (this.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                File out3 = this.$store.getEnrollmentWav();
                left = AudioRecord.getMinBufferSize(16000, 16, 2);
                AudioRecord recorder3 = new AudioRecord(1, 16000, 16, 2, Math.max(left, 3200) * 2);
                if (recorder3.getState() != 1) {
                    this.L$0 = SpillingKt.nullOutSpilledVariable($this$launch4);
                    this.L$1 = SpillingKt.nullOutSpilledVariable(out3);
                    this.L$2 = SpillingKt.nullOutSpilledVariable(recorder3);
                    this.I$0 = left;
                    this.label = 1;
                    return BuildersKt.withContext(Dispatchers.getMain(), new AnonymousClass1(this.$recording$delegate, this.$error$delegate, null), this) == coroutine_suspended ? coroutine_suspended : Unit.INSTANCE;
                }
                WavWriter writer3 = new WavWriter(out3, 16000, 0, 4, null);
                pcm = new ArrayList(240000);
                short[] buf2 = new short[1600];
                recorder3.startRecording();
                endAt = System.currentTimeMillis() + 15000;
                recorder = recorder3;
                out = out3;
                buf = buf2;
                obj = coroutine_suspended;
                writer = writer3;
                $this$launch = $this$launch4;
                $result2 = $result;
                try {
                    if (CoroutineScopeKt.isActive($this$launch) || System.currentTimeMillis() >= endAt) {
                        CoroutineScope $this$launch5 = $this$launch;
                        try {
                            Result.Companion companion = Result.INSTANCE;
                            recorder.stop();
                            Result.m8304constructorimpl(Unit.INSTANCE);
                            break;
                        } catch (Throwable th2) {
                            Result.Companion companion2 = Result.INSTANCE;
                            Result.m8304constructorimpl(ResultKt.createFailure(th2));
                        }
                        recorder.release();
                        writer.close();
                        samples = CollectionsKt.toShortArray(pcm);
                        arrayList = new ArrayList();
                        sArr = samples;
                        i = 0;
                        sArr2 = sArr;
                        length = sArr2.length;
                        i2 = 0;
                        while (i2 < length) {
                            s = sArr2[i2];
                            int i6 = i + 1;
                            int i7 = length;
                            i3 = i;
                            int i8 = i2;
                            if (i3 % 320 != 0) {
                                sArr4 = sArr2;
                                sArr5 = sArr;
                                i5 = 1;
                            } else {
                                sArr4 = sArr2;
                                iMin = Math.min(samples.length, i3 + 320);
                                d = 0.0d;
                                i4 = i3;
                                while (i4 < iMin) {
                                    int i9 = i4;
                                    double d2 = samples[i4];
                                    d += d2 * d2;
                                    i4 = i9 + 1;
                                    i3 = i3;
                                    sArr = sArr;
                                }
                                sArr5 = sArr;
                                if (Math.sqrt(d / ((double) (iMin - i3))) >= 160.0d) {
                                    i5 = 1;
                                } else {
                                    i5 = 0;
                                }
                            }
                            if (i5 != 0) {
                                arrayList.add(Boxing.boxShort(s));
                            }
                            i2 = i8 + 1;
                            i = i6;
                            length = i7;
                            sArr2 = sArr4;
                            sArr = sArr5;
                        }
                        filtered = CollectionsKt.toShortArray((List) arrayList);
                        if (filtered.length == 0) {
                            z = true;
                        } else {
                            z = false;
                        }
                        if (z) {
                            sArr3 = samples;
                        } else {
                            sArr3 = filtered;
                        }
                        short[] speechish = sArr3;
                        float[] embedding = VoiceEmbedder.INSTANCE.embed(speechish, 16000);
                        this.$store.saveEmbedding(embedding);
                        this.L$0 = SpillingKt.nullOutSpilledVariable($this$launch5);
                        this.L$1 = SpillingKt.nullOutSpilledVariable(out);
                        this.L$2 = SpillingKt.nullOutSpilledVariable(recorder);
                        this.L$3 = SpillingKt.nullOutSpilledVariable(writer);
                        this.L$4 = SpillingKt.nullOutSpilledVariable(pcm);
                        this.L$5 = SpillingKt.nullOutSpilledVariable(buf);
                        this.L$6 = SpillingKt.nullOutSpilledVariable(samples);
                        this.L$7 = SpillingKt.nullOutSpilledVariable(filtered);
                        this.L$8 = SpillingKt.nullOutSpilledVariable(speechish);
                        this.L$9 = SpillingKt.nullOutSpilledVariable(embedding);
                        this.I$0 = left;
                        this.J$0 = endAt;
                        this.label = 3;
                        if (BuildersKt.withContext(Dispatchers.getMain(), new AnonymousClass4(this.$recording$delegate, this.$status$delegate, null), this) == obj) {
                            return obj;
                        }
                        return Unit.INSTANCE;
                    }
                    int n2 = recorder.read(buf, 0, buf.length);
                    if (n2 > 0) {
                        try {
                            try {
                                try {
                                    try {
                                        writer.writePcm(buf, 0, n2);
                                        for (int i10 = 0; i10 < n2; i10++) {
                                            pcm.add(Boxing.boxShort(buf[i10]));
                                        }
                                        $this$launch2 = $this$launch;
                                        int left2 = RangesKt.coerceAtLeast((int) ((endAt - System.currentTimeMillis()) / 1000), 0);
                                        main = Dispatchers.getMain();
                                        $result3 = $result2;
                                        anonymousClass2 = new AnonymousClass2(left2, this.$secondsLeft$delegate, null);
                                        enrollVoiceScreenKt$EnrollVoiceScreen$startEnrollment$1 = this;
                                        $this$launch = $this$launch2;
                                        this.L$0 = $this$launch;
                                        $this$launch2 = $this$launch;
                                        this.L$1 = SpillingKt.nullOutSpilledVariable(out);
                                        this.L$2 = recorder;
                                        this.L$3 = writer;
                                        this.L$4 = pcm;
                                        this.L$5 = buf;
                                        this.I$0 = left;
                                        this.J$0 = endAt;
                                        this.I$1 = n2;
                                        this.I$2 = left2;
                                        n = 2;
                                        this.label = 2;
                                        if (BuildersKt.withContext(main, anonymousClass2, enrollVoiceScreenKt$EnrollVoiceScreen$startEnrollment$1) == obj) {
                                            return obj;
                                        }
                                        out2 = out;
                                        recorder2 = recorder;
                                        writer2 = writer;
                                        minBuf = left;
                                        $this$launch3 = $this$launch2;
                                        $result2 = $result3;
                                        left = minBuf;
                                        writer = writer2;
                                        recorder = recorder2;
                                        out = out2;
                                        $this$launch = $this$launch3;
                                    } catch (Throwable th3) {
                                        $this$launch = $this$launch2;
                                        recorder2 = recorder;
                                        writer2 = writer;
                                        th = th3;
                                    }
                                } catch (Throwable th4) {
                                    $this$launch = $this$launch2;
                                    recorder2 = recorder;
                                    writer2 = writer;
                                    th = th4;
                                }
                            } catch (Throwable th5) {
                                recorder2 = recorder;
                                writer2 = writer;
                                th = th5;
                            }
                        } catch (Throwable th6) {
                            recorder2 = recorder;
                            writer2 = writer;
                            th = th6;
                        }
                    } else {
                        $this$launch2 = $this$launch;
                        int left3 = RangesKt.coerceAtLeast((int) ((endAt - System.currentTimeMillis()) / 1000), 0);
                        main = Dispatchers.getMain();
                        $result3 = $result2;
                        anonymousClass2 = new AnonymousClass2(left3, this.$secondsLeft$delegate, null);
                        enrollVoiceScreenKt$EnrollVoiceScreen$startEnrollment$1 = this;
                        $this$launch = $this$launch2;
                        this.L$0 = $this$launch;
                        $this$launch2 = $this$launch;
                        this.L$1 = SpillingKt.nullOutSpilledVariable(out);
                        this.L$2 = recorder;
                        this.L$3 = writer;
                        this.L$4 = pcm;
                        this.L$5 = buf;
                        this.I$0 = left;
                        this.J$0 = endAt;
                        this.I$1 = n2;
                        this.I$2 = left3;
                        n = 2;
                        this.label = 2;
                        if (BuildersKt.withContext(main, anonymousClass2, enrollVoiceScreenKt$EnrollVoiceScreen$startEnrollment$1) == obj) {
                            return obj;
                        }
                        out2 = out;
                        recorder2 = recorder;
                        writer2 = writer;
                        minBuf = left;
                        $this$launch3 = $this$launch2;
                        $result2 = $result3;
                        left = minBuf;
                        writer = writer2;
                        recorder = recorder2;
                        out = out2;
                        $this$launch = $this$launch3;
                    }
                    if (CoroutineScopeKt.isActive($this$launch)) {
                    }
                    CoroutineScope $this$launch6 = $this$launch;
                    Result.Companion companion3 = Result.INSTANCE;
                    recorder.stop();
                    Result.m8304constructorimpl(Unit.INSTANCE);
                    recorder.release();
                    writer.close();
                    samples = CollectionsKt.toShortArray(pcm);
                    arrayList = new ArrayList();
                    sArr = samples;
                    i = 0;
                    sArr2 = sArr;
                    length = sArr2.length;
                    i2 = 0;
                    while (i2 < length) {
                        s = sArr2[i2];
                        int i11 = i + 1;
                        int i12 = length;
                        i3 = i;
                        int i13 = i2;
                        if (i3 % 320 != 0) {
                            sArr4 = sArr2;
                            sArr5 = sArr;
                            i5 = 1;
                        } else {
                            sArr4 = sArr2;
                            iMin = Math.min(samples.length, i3 + 320);
                            d = 0.0d;
                            i4 = i3;
                            while (i4 < iMin) {
                                int i14 = i4;
                                double d3 = samples[i4];
                                d += d3 * d3;
                                i4 = i14 + 1;
                                i3 = i3;
                                sArr = sArr;
                            }
                            sArr5 = sArr;
                            if (Math.sqrt(d / ((double) (iMin - i3))) >= 160.0d) {
                                i5 = 1;
                            } else {
                                i5 = 0;
                            }
                        }
                        if (i5 != 0) {
                            arrayList.add(Boxing.boxShort(s));
                        }
                        i2 = i13 + 1;
                        i = i11;
                        length = i12;
                        sArr2 = sArr4;
                        sArr = sArr5;
                    }
                    filtered = CollectionsKt.toShortArray((List) arrayList);
                    if (filtered.length == 0) {
                        z = true;
                    } else {
                        z = false;
                    }
                    if (z) {
                        sArr3 = samples;
                    } else {
                        sArr3 = filtered;
                    }
                    short[] speechish2 = sArr3;
                    float[] embedding2 = VoiceEmbedder.INSTANCE.embed(speechish2, 16000);
                    this.$store.saveEmbedding(embedding2);
                    this.L$0 = SpillingKt.nullOutSpilledVariable($this$launch6);
                    this.L$1 = SpillingKt.nullOutSpilledVariable(out);
                    this.L$2 = SpillingKt.nullOutSpilledVariable(recorder);
                    this.L$3 = SpillingKt.nullOutSpilledVariable(writer);
                    this.L$4 = SpillingKt.nullOutSpilledVariable(pcm);
                    this.L$5 = SpillingKt.nullOutSpilledVariable(buf);
                    this.L$6 = SpillingKt.nullOutSpilledVariable(samples);
                    this.L$7 = SpillingKt.nullOutSpilledVariable(filtered);
                    this.L$8 = SpillingKt.nullOutSpilledVariable(speechish2);
                    this.L$9 = SpillingKt.nullOutSpilledVariable(embedding2);
                    this.I$0 = left;
                    this.J$0 = endAt;
                    this.label = 3;
                    if (BuildersKt.withContext(Dispatchers.getMain(), new AnonymousClass4(this.$recording$delegate, this.$status$delegate, null), this) == obj) {
                        return obj;
                    }
                    return Unit.INSTANCE;
                } catch (Throwable th7) {
                    recorder2 = recorder;
                    writer2 = writer;
                    th = th7;
                }
                try {
                    Result.Companion companion4 = Result.INSTANCE;
                    recorder2.stop();
                    Result.m8304constructorimpl(Unit.INSTANCE);
                    break;
                } catch (Throwable th8) {
                    Result.Companion companion5 = Result.INSTANCE;
                    Result.m8304constructorimpl(ResultKt.createFailure(th8));
                }
                recorder2.release();
                writer2.close();
                throw th;
            case 1:
                int i15 = this.I$0;
                ResultKt.throwOnFailure($result);
            case 2:
                long endAt2 = this.I$2;
                int i16 = this.I$1;
                long endAt3 = this.J$0;
                minBuf = this.I$0;
                short[] buf3 = (short[]) this.L$5;
                ArrayList pcm2 = (ArrayList) this.L$4;
                writer2 = (WavWriter) this.L$3;
                recorder2 = (AudioRecord) this.L$2;
                File out4 = (File) this.L$1;
                try {
                    ResultKt.throwOnFailure($result);
                    obj = coroutine_suspended;
                    n = 2;
                    out2 = out4;
                    $this$launch3 = $this$launch4;
                    buf = buf3;
                    pcm = pcm2;
                    endAt = endAt3;
                    $result2 = $result;
                    left = minBuf;
                    writer = writer2;
                    recorder = recorder2;
                    out = out2;
                    $this$launch = $this$launch3;
                    if (CoroutineScopeKt.isActive($this$launch)) {
                        break;
                    }
                    CoroutineScope $this$launch7 = $this$launch;
                    Result.Companion companion6 = Result.INSTANCE;
                    recorder.stop();
                    Result.m8304constructorimpl(Unit.INSTANCE);
                    recorder.release();
                    writer.close();
                    samples = CollectionsKt.toShortArray(pcm);
                    arrayList = new ArrayList();
                    sArr = samples;
                    i = 0;
                    sArr2 = sArr;
                    length = sArr2.length;
                    i2 = 0;
                    while (i2 < length) {
                        s = sArr2[i2];
                        int i17 = i + 1;
                        int i18 = length;
                        i3 = i;
                        int i19 = i2;
                        if (i3 % 320 != 0) {
                            sArr4 = sArr2;
                            sArr5 = sArr;
                            i5 = 1;
                        } else {
                            sArr4 = sArr2;
                            iMin = Math.min(samples.length, i3 + 320);
                            d = 0.0d;
                            i4 = i3;
                            while (i4 < iMin) {
                                int i110 = i4;
                                double d4 = samples[i4];
                                d += d4 * d4;
                                i4 = i110 + 1;
                                i3 = i3;
                                sArr = sArr;
                            }
                            sArr5 = sArr;
                            if (Math.sqrt(d / ((double) (iMin - i3))) >= 160.0d) {
                                i5 = 1;
                            } else {
                                i5 = 0;
                            }
                        }
                        if (i5 != 0) {
                            arrayList.add(Boxing.boxShort(s));
                        }
                        i2 = i19 + 1;
                        i = i17;
                        length = i18;
                        sArr2 = sArr4;
                        sArr = sArr5;
                    }
                    filtered = CollectionsKt.toShortArray((List) arrayList);
                    if (filtered.length == 0) {
                        z = true;
                    } else {
                        z = false;
                    }
                    if (z) {
                        sArr3 = samples;
                    } else {
                        sArr3 = filtered;
                    }
                    short[] speechish3 = sArr3;
                    float[] embedding3 = VoiceEmbedder.INSTANCE.embed(speechish3, 16000);
                    this.$store.saveEmbedding(embedding3);
                    this.L$0 = SpillingKt.nullOutSpilledVariable($this$launch7);
                    this.L$1 = SpillingKt.nullOutSpilledVariable(out);
                    this.L$2 = SpillingKt.nullOutSpilledVariable(recorder);
                    this.L$3 = SpillingKt.nullOutSpilledVariable(writer);
                    this.L$4 = SpillingKt.nullOutSpilledVariable(pcm);
                    this.L$5 = SpillingKt.nullOutSpilledVariable(buf);
                    this.L$6 = SpillingKt.nullOutSpilledVariable(samples);
                    this.L$7 = SpillingKt.nullOutSpilledVariable(filtered);
                    this.L$8 = SpillingKt.nullOutSpilledVariable(speechish3);
                    this.L$9 = SpillingKt.nullOutSpilledVariable(embedding3);
                    this.I$0 = left;
                    this.J$0 = endAt;
                    this.label = 3;
                    if (BuildersKt.withContext(Dispatchers.getMain(), new AnonymousClass4(this.$recording$delegate, this.$status$delegate, null), this) == obj) {
                        return obj;
                    }
                    return Unit.INSTANCE;
                } catch (Throwable th9) {
                    th = th9;
                    $this$launch = $this$launch4;
                }
                break;
            case 3:
                long j = this.J$0;
                int i20 = this.I$0;
                ResultKt.throwOnFailure($result);
                return Unit.INSTANCE;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
    }

    /* JADX INFO: renamed from: com.varun.pocketassistant.ui.EnrollVoiceScreenKt$EnrollVoiceScreen$startEnrollment$1$1, reason: invalid class name */
    /* JADX INFO: compiled from: EnrollVoiceScreen.kt */
    @Metadata(d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H\n"}, d2 = {"<anonymous>", "", "Lkotlinx/coroutines/CoroutineScope;"}, k = 3, mv = {2, 2, 0}, xi = 48)
    @DebugMetadata(c = "com.varun.pocketassistant.ui.EnrollVoiceScreenKt$EnrollVoiceScreen$startEnrollment$1$1", f = "EnrollVoiceScreen.kt", i = {}, l = {}, m = "invokeSuspend", n = {}, s = {})
    static final class AnonymousClass1 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
        final /* synthetic */ MutableState<String> $error$delegate;
        final /* synthetic */ MutableState<Boolean> $recording$delegate;
        int label;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass1(MutableState<Boolean> mutableState, MutableState<String> mutableState2, Continuation<? super AnonymousClass1> continuation) {
            super(2, continuation);
            this.$recording$delegate = mutableState;
            this.$error$delegate = mutableState2;
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
            return new AnonymousClass1(this.$recording$delegate, this.$error$delegate, continuation);
        }

        @Override // kotlin.jvm.functions.Function2
        public final Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
            return ((AnonymousClass1) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object $result) {
            IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    EnrollVoiceScreenKt.EnrollVoiceScreen$lambda$5(this.$recording$delegate, false);
                    this.$error$delegate.setValue("Could not open microphone");
                    return Unit.INSTANCE;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }

    /* JADX INFO: renamed from: com.varun.pocketassistant.ui.EnrollVoiceScreenKt$EnrollVoiceScreen$startEnrollment$1$2, reason: invalid class name */
    /* JADX INFO: compiled from: EnrollVoiceScreen.kt */
    @Metadata(d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H\n"}, d2 = {"<anonymous>", "", "Lkotlinx/coroutines/CoroutineScope;"}, k = 3, mv = {2, 2, 0}, xi = 48)
    @DebugMetadata(c = "com.varun.pocketassistant.ui.EnrollVoiceScreenKt$EnrollVoiceScreen$startEnrollment$1$2", f = "EnrollVoiceScreen.kt", i = {}, l = {}, m = "invokeSuspend", n = {}, s = {})
    static final class AnonymousClass2 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
        final /* synthetic */ int $left;
        final /* synthetic */ MutableIntState $secondsLeft$delegate;
        int label;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass2(int i, MutableIntState mutableIntState, Continuation<? super AnonymousClass2> continuation) {
            super(2, continuation);
            this.$left = i;
            this.$secondsLeft$delegate = mutableIntState;
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
            return new AnonymousClass2(this.$left, this.$secondsLeft$delegate, continuation);
        }

        @Override // kotlin.jvm.functions.Function2
        public final Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
            return ((AnonymousClass2) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object $result) {
            IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    this.$secondsLeft$delegate.setIntValue(this.$left);
                    return Unit.INSTANCE;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }

    /* JADX INFO: renamed from: com.varun.pocketassistant.ui.EnrollVoiceScreenKt$EnrollVoiceScreen$startEnrollment$1$4, reason: invalid class name */
    /* JADX INFO: compiled from: EnrollVoiceScreen.kt */
    @Metadata(d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H\n"}, d2 = {"<anonymous>", "", "Lkotlinx/coroutines/CoroutineScope;"}, k = 3, mv = {2, 2, 0}, xi = 48)
    @DebugMetadata(c = "com.varun.pocketassistant.ui.EnrollVoiceScreenKt$EnrollVoiceScreen$startEnrollment$1$4", f = "EnrollVoiceScreen.kt", i = {}, l = {}, m = "invokeSuspend", n = {}, s = {})
    static final class AnonymousClass4 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
        final /* synthetic */ MutableState<Boolean> $recording$delegate;
        final /* synthetic */ MutableState<String> $status$delegate;
        int label;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass4(MutableState<Boolean> mutableState, MutableState<String> mutableState2, Continuation<? super AnonymousClass4> continuation) {
            super(2, continuation);
            this.$recording$delegate = mutableState;
            this.$status$delegate = mutableState2;
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
            return new AnonymousClass4(this.$recording$delegate, this.$status$delegate, continuation);
        }

        @Override // kotlin.jvm.functions.Function2
        public final Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
            return ((AnonymousClass4) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object $result) {
            IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    EnrollVoiceScreenKt.EnrollVoiceScreen$lambda$5(this.$recording$delegate, false);
                    this.$status$delegate.setValue("Enrolled. New transcripts will label [You] vs [Other].");
                    return Unit.INSTANCE;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }
}
