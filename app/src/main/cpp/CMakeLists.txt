# Copyright 2026 Google LLC.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

cmake_minimum_required(VERSION 3.22.1)
project(pocket_assistant_jni)

# --- HuggingFace tokenizer JNI (Parakeet) ---
add_library(tokenizer_jni SHARED
    tokenizer_jni.cc
)

target_include_directories(tokenizer_jni PRIVATE
    ${CMAKE_CURRENT_SOURCE_DIR}
    ${CMAKE_CURRENT_SOURCE_DIR}/include
)

set(JNILIBS_DIR ${CMAKE_CURRENT_SOURCE_DIR}/lib/${ANDROID_ABI})

add_library(tokenizers_c STATIC IMPORTED)
set_target_properties(tokenizers_c PROPERTIES
    IMPORTED_LOCATION ${JNILIBS_DIR}/libtokenizers_c.a
)

target_link_libraries(tokenizer_jni
    tokenizers_c
    log
)

# Pixel 10+ uses 16 KB pages; ELF LOAD segments must align to 16384.
target_link_options(tokenizer_jni PRIVATE
    "-Wl,-z,max-page-size=16384"
    "-Wl,-z,common-page-size=16384"
)

# --- TEN VAD JNI ---
# Prebuilt libten_vad.so lives in jniLibs/arm64-v8a (already 16KB-aligned).
set(TEN_VAD_SO
    ${CMAKE_CURRENT_SOURCE_DIR}/../jniLibs/${ANDROID_ABI}/libten_vad.so
)

add_library(ten_vad SHARED IMPORTED)
set_target_properties(ten_vad PROPERTIES
    IMPORTED_LOCATION ${TEN_VAD_SO}
)

add_library(ten_vad_jni SHARED
    ten_vad_jni.cc
)

target_include_directories(ten_vad_jni PRIVATE
    ${CMAKE_CURRENT_SOURCE_DIR}
)

target_link_libraries(ten_vad_jni
    ten_vad
    log
)

target_link_options(ten_vad_jni PRIVATE
    "-Wl,-z,max-page-size=16384"
    "-Wl,-z,common-page-size=16384"
)