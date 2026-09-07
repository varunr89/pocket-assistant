#!/usr/bin/env bash
# Download Tensor G5 local models and push into Pocket Assistant filesDir.
#
# Pinned artifact manifest (M1 increment 4). Every revision + sha256 below was
# read from the HuggingFace API / GitHub release digest; do not bump without a
# deliberate re-pin.
#
# litert-community/parakeet-tdt-0.6b-v3            (Apache-2.0)
#   revision 50dae0cb8c7b39dda477966eff7150cd7fe206ae
#   parakeet_tdt_0.6b_v3_5s_f32_stateful_Google_Tensor_G5.tflite  (NPU f32 export, staged as model_npu.tflite)
#     sha256 89a6d8c0f6b6f242fa704a5788229636946f1953  1292830512 bytes
#   parakeet_tdt_0.6b_v3_5s_i8_stateful.tflite                   (CPU i8 export, staged as model.tflite)
#     sha256 5671b23606902759401b0431cc3ac5c980bae753  614261072 bytes
# nvidia/parakeet-tdt-0.6b-v3                     (CC-BY-4.0)
#   revision 8844af98cdfda411074894c417dfaf0cfb9c6a73
#   tokenizer.json
#     sha256 a10a554cf61e38108756650018d5781ae9675c1d
# litert-community/gemma-4-E2B-it-litert-lm       (Apache-2.0)
#   revision 361a4010ad6d88fc5c86e148e333c0342b99763d
#   gemma-4-E2B-it_Google_Tensor_G5.litertlm
#     sha256 93dd8ec880c18bcf11d2ae8a4da8fbf3922bbdc1  3113545589 bytes
# k2-fsa/sherpa-onnx @ release tag "asr-models"   (converted from NVIDIA CC-BY-4.0)
#   sherpa-onnx-nemo-parakeet-tdt-0.6b-v3-int8.tar.bz2          (benchmark-only, staged under models/sherpa/)
#     sha256 5793d0fd397c5778d2cf2126994d58e9d56b1be7c04d13c7a15bb1b4eafb16bf  487170055 bytes
# sherpa-onnx runtime (app/libs/sherpa-onnx-1.12.27.aar, Apache-2.0, benchmark-only,
# pinned in app/build.gradle.kts):
#   sha256 ef0466b4e66fa950e7f5a395e69dbe1a54c9f6425c73c724474e195e2b676e3c
#
# Usage: ./scripts/push-local-models.sh [--with-sherpa]
#   --with-sherpa also downloads + stages the ~487 MB sherpa int8 Parakeet model
#   used by the androidTest CPU-first benchmark (M1 increment 4), NOT used by
#   the production provider.
set -euo pipefail

WITH_SHERPA=0
[ $# -eq 0 ] || { [ "$1" = "--with-sherpa" ] && WITH_SHERPA=1 || { echo "usage: $0 [--with-sherpa]"; exit 1; }; }

PKG="${PKG:-com.varun.pocketassistant}"
CACHE="${CACHE:-$HOME/.cache/pocket-assistant-models}"
mkdir -p "$CACHE/parakeet" "$CACHE/gemma"

export PATH="/Library/Frameworks/Python.framework/Versions/3.11/bin:$HOME/.local/bin:$PATH"

need() { command -v "$1" >/dev/null || { echo "Missing: $1"; exit 1; }; }
need adb
need shasum

if command -v hf >/dev/null; then
  HF=(hf download)
elif command -v huggingface-cli >/dev/null; then
  HF=(huggingface-cli download)
else
  echo "Missing hf (pip install -U huggingface_hub)"
  exit 1
fi

verify() { # $1=file $2=expected_sha256 $3=label
  local actual
  actual=$(shasum -a 256 "$1" | awk '{print $1}')
  [ "$actual" = "$2" ] || { echo "CHECKSUM MISMATCH for $3: got $actual want $2"; exit 1; }
  echo "ok   $3 ($(du -h "$1" | awk '{print $1}'))"
}

echo "==> Checking device"
adb get-state >/dev/null

echo "==> Downloading Parakeet TDT 0.6B v3 (Tensor G5 NPU f32 + CPU i8 + tokenizer; pinned revisions)"
"${HF[@]}" litert-community/parakeet-tdt-0.6b-v3 \
  --revision 50dae0cb8c7b39dda477966eff7150cd7fe206ae \
  parakeet_tdt_0.6b_v3_5s_f32_stateful_Google_Tensor_G5.tflite \
  parakeet_tdt_0.6b_v3_5s_i8_stateful.tflite \
  --local-dir "$CACHE/parakeet"
"${HF[@]}" nvidia/parakeet-tdt-0.6b-v3 \
  --revision 8844af98cdfda411074894c417dfaf0cfb9c6a73 \
  tokenizer.json \
  --local-dir "$CACHE/parakeet"

verify "$CACHE/parakeet/parakeet_tdt_0.6b_v3_5s_f32_stateful_Google_Tensor_G5.tflite" \
  89a6d8c0f6b6f242fa704a5788229636946f1953 "parakeet NPU f32 G5 export"
verify "$CACHE/parakeet/parakeet_tdt_0.6b_v3_5s_i8_stateful.tflite" \
  5671b23606902759401b0431cc3ac5c980bae753 "parakeet CPU i8 export"
verify "$CACHE/parakeet/tokenizer.json" \
  a10a554cf61e38108756650018d5781ae9675c1d "parakeet tokenizer"

echo "==> Downloading Gemma 4 E2B Tensor G5 (~3GB; accept Gemma license on HF if prompted)"
"${HF[@]}" litert-community/gemma-4-E2B-it-litert-lm \
  --revision 361a4010ad6d88fc5c86e148e333c0342b99763d \
  gemma-4-E2B-it_Google_Tensor_G5.litertlm \
  --local-dir "$CACHE/gemma"
verify "$CACHE/gemma/gemma-4-E2B-it_Google_Tensor_G5.litertlm" \
  93dd8ec880c18bcf11d2ae8a4da8fbf3922bbdc1 "gemma G5 litertlm"

# Stage with expected filenames
STAGE="$CACHE/stage"
rm -rf "$STAGE"
mkdir -p "$STAGE/models/parakeet" "$STAGE/models/gemma4b"
cp "$CACHE/parakeet/parakeet_tdt_0.6b_v3_5s_f32_stateful_Google_Tensor_G5.tflite" \
  "$STAGE/models/parakeet/model_npu.tflite"
cp "$CACHE/parakeet/parakeet_tdt_0.6b_v3_5s_i8_stateful.tflite" \
  "$STAGE/models/parakeet/model.tflite"
cp "$CACHE/parakeet/tokenizer.json" "$STAGE/models/parakeet/tokenizer.json"
cp "$CACHE/gemma/gemma-4-E2B-it_Google_Tensor_G5.litertlm" \
  "$STAGE/models/gemma4b/gemma-4-E2B-it_Google_Tensor_G5.litertlm"

# Benchmark-only sherpa int8 Parakeet model (M1 increment 4). Staged under
# models/sherpa/; consumed by SherpaAsrBenchmarkAdapter (androidTest only).
if [ "$WITH_SHERPA" = "1" ]; then
  mkdir -p "$CACHE/sherpa"
  SHERPA_TAR="$CACHE/sherpa/sherpa-onnx-nemo-parakeet-tdt-0.6b-v3-int8.tar.bz2"
  if [ ! -f "$SHERPA_TAR" ]; then
    echo "==> Downloading sherpa-onnx parakeet-tdt-0.6b-v3 int8 (~487MB, benchmark-only)"
    curl -L --fail -o "$SHERPA_TAR.part" \
      https://github.com/k2-fsa/sherpa-onnx/releases/download/asr-models/sherpa-onnx-nemo-parakeet-tdt-0.6b-v3-int8.tar.bz2
    mv "$SHERPA_TAR.part" "$SHERPA_TAR"
  fi
  verify "$SHERPA_TAR" \
    5793d0fd397c5778d2cf2126994d58e9d56b1be7c04d13c7a15bb1b4eafb16bf "sherpa parakeet int8 tarball"

  SHERPA_SRC="$CACHE/sherpa/sherpa-onnx-nemo-parakeet-tdt-0.6b-v3-int8"
  if [ ! -d "$SHERPA_SRC" ]; then
    mkdir -p "$SHERPA_SRC"
    tar xjf "$SHERPA_TAR" -C "$CACHE/sherpa"
  fi
  # Quantization sanity check: this export must be int8 (QuantizeLinear ops),
  # not an f32 model renamed. Full onnx inspection when the onnx package is
  # available; otherwise fall back to name check + file sizes.
  echo "==> Quantization check for sherpa int8 export"
  if python3 -c "import onnx" 2>/dev/null; then
    python3 - "$SHERPA_SRC" <<'PY'
import sys, glob, onnx
ok = True
for p in ["encoder.int8.onnx", "decoder.int8.onnx", "joiner.int8.onnx"]:
    m = onnx.load(sys.argv[1] + "/" + p)
    nodes = {n.op_type for n in m.graph.node}
    if "QuantizeLinear" not in nodes and "DynamicQuantizeLinear" not in nodes:
        print(f"FAIL: {p} has no quantize op (ops sample: {sorted(nodes)[:5]})")
        ok = False
    else:
        print(f"ok   {p}: int8 quantized ({p} ops={len(nodes)})")
sys.exit(0 if ok else 1)
PY
  else
    echo "warn: python3 onnx not installed; verifying int8 by filename only"
    for f in encoder.int8.onnx decoder.int8.onnx joiner.int8.onnx; do
      [ -f "$SHERPA_SRC/$f" ] || { echo "FAIL: missing $f"; exit 1; }
      echo "ok   $f ($(du -h "$SHERPA_SRC/$f" | awk '{print $1}'))"
    done
  fi
  mkdir -p "$STAGE/models/sherpa"
  cp "$SHERPA_SRC/encoder.int8.onnx" "$STAGE/models/sherpa/encoder.int8.onnx"
  cp "$SHERPA_SRC/decoder.int8.onnx" "$STAGE/models/sherpa/decoder.int8.onnx"
  cp "$SHERPA_SRC/joiner.int8.onnx" "$STAGE/models/sherpa/joiner.int8.onnx"
  cp "$SHERPA_SRC/tokens.txt" "$STAGE/models/sherpa/tokens.txt"
fi

echo "==> Pushing into app files (debuggable run-as)"
adb shell run-as "$PKG" mkdir -p files/models/parakeet files/models/gemma4b
[ "$WITH_SHERPA" = "1" ] && adb shell run-as "$PKG" mkdir -p files/models/sherpa
TMP=/data/local/tmp/pocket-models
adb shell rm -rf "$TMP"
adb shell mkdir -p "$TMP/parakeet" "$TMP/gemma4b"
[ "$WITH_SHERPA" = "1" ] && adb shell mkdir -p "$TMP/sherpa"
adb push "$STAGE/models/parakeet/." "$TMP/parakeet/"
adb push "$STAGE/models/gemma4b/." "$TMP/gemma4b/"
if [ "$WITH_SHERPA" = "1" ]; then
  adb push "$STAGE/models/sherpa/." "$TMP/sherpa/"
  adb shell "run-as $PKG sh -c 'cp -f $TMP/parakeet/* files/models/parakeet/ && cp -f $TMP/gemma4b/* files/models/gemma4b/ && cp -f $TMP/sherpa/* files/models/sherpa/'"
else
  adb shell "run-as $PKG sh -c 'cp -f $TMP/parakeet/* files/models/parakeet/ && cp -f $TMP/gemma4b/* files/models/gemma4b/'"
fi
adb shell rm -rf "$TMP"

echo "==> Verifying"
adb shell run-as "$PKG" ls -lh files/models/parakeet
adb shell run-as "$PKG" ls -lh files/models/gemma4b
[ "$WITH_SHERPA" = "1" ] && adb shell run-as "$PKG" ls -lh files/models/sherpa
echo "Done. Open Pipeline → Prefer local → Reprocess all segments."
[ "$WITH_SHERPA" = "1" ] && echo "Sherpa benchmark models staged (androidTest-only)."
