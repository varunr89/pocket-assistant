#!/usr/bin/env bash
# Download Tensor G5 local models and push into Pocket Assistant filesDir.
set -euo pipefail

PKG="${PKG:-com.varun.pocketassistant}"
CACHE="${CACHE:-$HOME/.cache/pocket-assistant-models}"
mkdir -p "$CACHE/parakeet" "$CACHE/gemma"

export PATH="/Library/Frameworks/Python.framework/Versions/3.11/bin:$HOME/.local/bin:$PATH"

need() { command -v "$1" >/dev/null || { echo "Missing: $1"; exit 1; }; }
need adb

if command -v hf >/dev/null; then
  HF=(hf download)
elif command -v huggingface-cli >/dev/null; then
  HF=(huggingface-cli download)
else
  echo "Missing hf (pip install -U huggingface_hub)"
  exit 1
fi

echo "==> Checking device"
adb get-state >/dev/null

echo "==> Downloading Parakeet TDT 0.6B v3 (Tensor G5 + CPU/GPU fallback + tokenizer)"
"${HF[@]}" litert-community/parakeet-tdt-0.6b-v3 \
  parakeet_tdt_0.6b_v3_5s_f32_stateful_Google_Tensor_G5.tflite \
  --local-dir "$CACHE/parakeet"
"${HF[@]}" litert-community/parakeet-tdt-0.6b-v3 \
  parakeet_tdt_0.6b_v3_5s_i8_stateful.tflite \
  --local-dir "$CACHE/parakeet"
"${HF[@]}" nvidia/parakeet-tdt-0.6b-v3 \
  tokenizer.json \
  --local-dir "$CACHE/parakeet"

echo "==> Downloading Gemma 4 E2B Tensor G5 (~3GB; accept Gemma license on HF if prompted)"
"${HF[@]}" litert-community/gemma-4-E2B-it-litert-lm \
  gemma-4-E2B-it_Google_Tensor_G5.litertlm \
  --local-dir "$CACHE/gemma"

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

echo "==> Pushing into app files (debuggable run-as)"
adb shell run-as "$PKG" mkdir -p files/models/parakeet files/models/gemma4b
TMP=/data/local/tmp/pocket-models
adb shell rm -rf "$TMP"
adb shell mkdir -p "$TMP/parakeet" "$TMP/gemma4b"
adb push "$STAGE/models/parakeet/." "$TMP/parakeet/"
adb push "$STAGE/models/gemma4b/." "$TMP/gemma4b/"
adb shell "run-as $PKG sh -c 'cp -f $TMP/parakeet/* files/models/parakeet/ && cp -f $TMP/gemma4b/* files/models/gemma4b/'"
adb shell rm -rf "$TMP"

echo "==> Verifying"
adb shell run-as "$PKG" ls -lh files/models/parakeet
adb shell run-as "$PKG" ls -lh files/models/gemma4b
echo "Done. Open Pipeline → Prefer local → Reprocess all segments."
