package com.varun.pocketassistant.capture;

import kotlin.Metadata;

/* JADX INFO: compiled from: TenVadNative.kt */
/* JADX INFO: loaded from: classes5.dex */
@Metadata(d1 = {"\u0000>\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\t\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0007\n\u0002\b\u0003\n\u0002\u0010\u0017\n\u0000\n\u0002\u0010\u0014\n\u0000\n\u0002\u0010\u0015\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\bÁ\u0002\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003J\u0019\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\tH\u0086 J)\u0010\n\u001a\u00020\u00072\u0006\u0010\u000b\u001a\u00020\u00052\u0006\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u0011H\u0086 J\u0011\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u000b\u001a\u00020\u0005H\u0086 J\t\u0010\u0014\u001a\u00020\u0015H\u0086 ¨\u0006\u0016"}, d2 = {"Lcom/varun/pocketassistant/capture/TenVadNative;", "", "<init>", "()V", "nativeCreate", "", "hopSize", "", "threshold", "", "nativeProcess", "handle", "audio", "", "outProbability", "", "outFlag", "", "nativeDestroy", "", "nativeVersion", "", "app_debug"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class TenVadNative {
    public static final int $stable = 0;
    public static final TenVadNative INSTANCE = new TenVadNative();

    public final native long nativeCreate(int hopSize, float threshold);

    public final native void nativeDestroy(long handle);

    public final native int nativeProcess(long handle, short[] audio, float[] outProbability, int[] outFlag);

    public final native String nativeVersion();

    private TenVadNative() {
    }

    static {
        System.loadLibrary("ten_vad");
        System.loadLibrary("ten_vad_jni");
    }
}
