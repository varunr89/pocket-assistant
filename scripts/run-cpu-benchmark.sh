#!/usr/bin/env bash
# CPU-first runtime comparison runner (M1 increment 4).
#
# Stages the benchmark-only sherpa int8 model + a WAV corpus onto the device,
# installs the app + androidTest APKs built from the CURRENT CLEAN git tree,
# runs CpuFirstBenchmarkTest (sherpa CPU int8 vs LiteRT CPU vs LiteRT NPU on
# identical original 16 kHz mono WAVs), and pulls per-run results together with
# pre/post dumpsys snapshots (battery / thermal / meminfo / batterystats).
#
# NOT the increment-8 SLO protocol: this records raw snapshots + run JSON only.
# WER scoring against human/cloud references and the held-out corpus arrive
# with increment 2's scripts/m1-slo.py; do not read WER from these outputs.
#
# Usage:
#   CORPUS=/path/to/wavs ./scripts/run-cpu-benchmark.sh [--with-sherpa-download]
#
#   CORPUS   local dir with 16 kHz mono PCM16 .wav files (default: /tmp/pa_invest/wavs)
#   Results land in ./benchmark-results/<git-sha>/
set -euo pipefail

cd "$(dirname "$0")/.."

PKG="${PKG:-com.varun.pocketassistant}"
TEST_PKG="$PKG.test"
RUNNER="androidx.test.runner.AndroidJUnitRunner"
TEST_CLASS="com.varun.pocketassistant.pipeline.CpuFirstBenchmarkTest"
CORPUS="${CORPUS:-/tmp/pa_invest/wavs}"
WITH_SHERPA_DOWNLOAD=0
[ $# -eq 0 ] || { [ "$1" = "--with-sherpa-download" ] && WITH_SHERPA_DOWNLOAD=1 || { echo "usage: $0 [--with-sherpa-download]"; exit 1; }; }

export JAVA_HOME="${JAVA_HOME:-/opt/homebrew/opt/openjdk@17}"
export ANDROID_HOME="${ANDROID_HOME:-/opt/homebrew/share/android-commandlinetools}"
export ANDROID_SDK_ROOT="$ANDROID_HOME"
export PATH="$JAVA_HOME/bin:$ANDROID_HOME/cmdline-tools/latest/bin:$ANDROID_HOME/platform-tools:$PATH"

need() { command -v "$1" >/dev/null || { echo "Missing: $1"; exit 1; }; }
need adb
need python3

[ -d "$CORPUS" ] || { echo "No corpus at $CORPUS (set CORPUS=/path/to/wavs)"; exit 1; }
WAVS=$(find "$CORPUS" -maxdepth 1 -name '*.wav' | sort)
[ -n "$WAVS" ] || { echo "No .wav files in $CORPUS"; exit 1; }

# Constitution: only Git-tracked builds reach a device. Build from a clean tree.
if [ -n "$(git status --porcelain)" ]; then
  echo "Working tree is dirty — commit or stash before benchmarking."
  git status --short
  exit 1
fi
GIT_SHA=$(git rev-parse HEAD)
echo "==> Building app + androidTest APKs from clean tree @ $GIT_SHA"
./gradlew :app:assembleDebug :app:assembleDebugAndroidTest --console=plain -q

APP_APK="app/build/outputs/apk/debug/app-debug.apk"
TEST_APK="app/build/outputs/apk/androidTest/debug/app-debug-androidTest.apk"
[ -f "$APP_APK" ] && [ -f "$TEST_APK" ] || { echo "APK build output missing"; exit 1; }

echo "==> Checking device"
adb get-state >/dev/null
DEVICE=$(adb shell getprop ro.product.model | tr -d '\r')
SOC=$(adb shell getprop ro.soc.model | tr -d '\r')
BUILD=$(adb shell getprop ro.build.display.id | tr -d '\r')
echo "device=$DEVICE soc=$SOC build=$BUILD"

echo "==> Installing app + test APKs"
adb install -r "$APP_APK" >/dev/null
adb install -r "$TEST_APK" >/dev/null

echo "==> Staging models (parakeet NPU/CPU + tokenizer + gemma; sherpa if not staged)"
if [ "$WITH_SHERPA_DOWNLOAD" = "1" ]; then
  ./scripts/push-local-models.sh --with-sherpa
elif ! adb shell run-as "$PKG" test -f files/models/sherpa/encoder.int8.onnx; then
  ./scripts/push-local-models.sh --with-sherpa
else
  ./scripts/push-local-models.sh
fi

echo "==> Staging benchmark corpus ($(echo "$WAVS" | wc -l | tr -d ' ') wavs)"
adb shell run-as "$PKG" rm -rf files/benchmark_wavs files/benchmark_out
adb shell run-as "$PKG" mkdir -p files/benchmark_wavs
for w in $WAVS; do
  adb push "$w" "/data/local/tmp/bwav_$(basename "$w")" >/dev/null
  adb shell "run-as $PKG sh -c 'cp /data/local/tmp/bwav_$(basename "$w") files/benchmark_wavs/$(basename "$w")'"
  adb shell rm "/data/local/tmp/bwav_$(basename "$w")"
done
echo "Corpus on device:"
adb shell run-as "$PKG" ls files/benchmark_wavs

RESULTS="benchmark-results/$GIT_SHA"
mkdir -p "$RESULTS"
echo "==> Pre-run snapshots -> $RESULTS"
adb shell dumpsys battery > "$RESULTS/pre_dumpsys_battery.txt"
adb shell dumpsys thermalservice > "$RESULTS/pre_dumpsys_thermalservice.txt" 2>/dev/null || echo "(thermalservice unavailable)" > "$RESULTS/pre_dumpsys_thermalservice.txt"
adb shell dumpsys meminfo "$PKG" > "$RESULTS/pre_dumpsys_meminfo.txt" 2>/dev/null || echo "(app not running yet)" > "$RESULTS/pre_dumpsys_meminfo.txt"
adb shell dumpsys batterystats --reset >/dev/null

echo "==> Running CpuFirstBenchmarkTest (this is the long step)"
START_EPOCH=$(date +%s)
adb shell am instrument -w -e class "$TEST_CLASS" "$TEST_PKG/$RUNNER" | tee "$RESULTS/instrument_raw.txt"
END_EPOCH=$(date +%s)

echo "==> Post-run snapshots -> $RESULTS"
adb shell dumpsys battery > "$RESULTS/post_dumpsys_battery.txt"
adb shell dumpsys thermalservice > "$RESULTS/post_dumpsys_thermalservice.txt" 2>/dev/null || echo "(thermalservice unavailable)" > "$RESULTS/post_dumpsys_thermalservice.txt"
adb shell dumpsys meminfo "$PKG" > "$RESULTS/post_dumpsys_meminfo.txt"
adb shell dumpsys batterystats > "$RESULTS/post_dumpsys_batterystats.txt"
adb shell dumpsys batterystats "$PKG" > "$RESULTS/post_dumpsys_batterystats_pkg.txt" 2>/dev/null || true

echo "==> Pulling results JSON"
adb shell run-as "$PKG" cat files/benchmark_out/benchmark_results.json > "$RESULTS/benchmark_results.json"

shasum -a 256 "$APP_APK" "$TEST_APK" > "$RESULTS/apk_sha256.txt"
cat > "$RESULTS/run_metadata.json" <<EOF
{
  "gitSha": "$GIT_SHA",
  "device": "$DEVICE",
  "soc": "$SOC",
  "build": "$BUILD",
  "startEpoch": $START_EPOCH,
  "endEpoch": $END_EPOCH,
  "note": "Raw snapshot + run harness only. WER vs references arrives with scripts/m1-slo.py (increment 2); energy SLO protocol is increment 8. LiteRT rows use production preprocessing (1.35x + trim) until increment 5."
}
EOF

echo "==> Summary"
python3 - "$RESULTS/benchmark_results.json" <<'PY'
import json, sys
d = json.load(open(sys.argv[1]))
print(f"git=metadata:{d.get('generatedAtMs')} soc={d.get('soc')} model={d.get('model')}")
print(f"{'backend':32} {'pass':5} {'wav':28} {'ok':5} {'wallMs':>8} {'loadMs':>8} {'rtf':>7} {'chars':>6}")
for r in d.get("runs", []):
    print(f"{r.get('backend','?'):32} {r.get('pass','?'):5} {str(r.get('wav','')):28} "
          f"{str(r.get('ok')):5} {r.get('wallMs',0):>8} {r.get('loadMs',0):>8} "
          f"{r.get('rtf',0):>7.3f} {len(r.get('text','')):>6}")
PY
echo "Results: $RESULTS"
