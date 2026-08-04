/*
 * JNI bindings for TEN VAD (https://github.com/TEN-framework/ten-vad).
 */

#include <jni.h>

#include "ten_vad/ten_vad.h"

extern "C" {

JNIEXPORT jlong JNICALL
Java_com_varun_pocketassistant_capture_TenVadNative_nativeCreate(
    JNIEnv* /* env */, jclass /* clazz */, jint hop_size, jfloat threshold) {
  ten_vad_handle_t handle = nullptr;
  if (ten_vad_create(&handle, static_cast<size_t>(hop_size), threshold) != 0) {
    return 0;
  }
  return reinterpret_cast<jlong>(handle);
}

JNIEXPORT jint JNICALL
Java_com_varun_pocketassistant_capture_TenVadNative_nativeProcess(
    JNIEnv* env, jclass /* clazz */, jlong handle, jshortArray audio,
    jfloatArray out_probability, jintArray out_flag) {
  if (handle == 0) return -1;
  ten_vad_handle_t vad = reinterpret_cast<ten_vad_handle_t>(handle);

  const jsize len = env->GetArrayLength(audio);
  jshort* samples = env->GetShortArrayElements(audio, nullptr);
  if (samples == nullptr) return -1;

  float probability = 0.0f;
  int flag = 0;
  const int rc = ten_vad_process(
      vad, reinterpret_cast<const int16_t*>(samples), static_cast<size_t>(len),
      &probability, &flag);
  env->ReleaseShortArrayElements(audio, samples, JNI_ABORT);
  if (rc != 0) return rc;

  env->SetFloatArrayRegion(out_probability, 0, 1, &probability);
  env->SetIntArrayRegion(out_flag, 0, 1, &flag);
  return 0;
}

JNIEXPORT void JNICALL
Java_com_varun_pocketassistant_capture_TenVadNative_nativeDestroy(
    JNIEnv* /* env */, jclass /* clazz */, jlong handle) {
  if (handle == 0) return;
  ten_vad_handle_t vad = reinterpret_cast<ten_vad_handle_t>(handle);
  ten_vad_destroy(&vad);
}

JNIEXPORT jstring JNICALL
Java_com_varun_pocketassistant_capture_TenVadNative_nativeVersion(
    JNIEnv* env, jclass /* clazz */) {
  const char* version = ten_vad_get_version();
  return env->NewStringUTF(version != nullptr ? version : "unknown");
}

}  // extern "C"
