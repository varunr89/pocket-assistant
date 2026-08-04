package com.varun.pocketassistant.capture

/**
 * JNI facade for [TEN VAD](https://github.com/TEN-framework/ten-vad).
 * Expects `libten_vad.so` + `libten_vad_jni.so` in the APK jniLibs.
 */
internal object TenVadNative {
    init {
        System.loadLibrary("ten_vad")
        System.loadLibrary("ten_vad_jni")
    }

    external fun nativeCreate(hopSize: Int, threshold: Float): Long

    external fun nativeProcess(
        handle: Long,
        audio: ShortArray,
        outProbability: FloatArray,
        outFlag: IntArray,
    ): Int

    external fun nativeDestroy(handle: Long)

    external fun nativeVersion(): String
}
