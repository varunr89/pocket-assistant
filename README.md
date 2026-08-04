# Pocket Assistant

Always-on personal capture for Pixel 10: ambient conversations and speakerphone meetings.

## Phase 0–1 (this build)

- Start / Pause / Resume / Stop from the app
- Microphone foreground service (survives screen-off)
- Persistent notification with pause/stop actions
- Energy VAD + pre-roll ring buffer — only speech segments are saved as WAV
- Crash-safe segment flushing
- Session list + per-segment playback
- 14-day retention cleanup on stop

## Tonight: install on your Pixel

Debug APK is already built:

`app/build/outputs/apk/debug/app-debug.apk`

### 1. Enable wireless debugging on the Pixel

Settings → About phone → tap **Build number** 7× → Developer options → enable **Wireless debugging** → Pair device with pairing code.

### 2. Pair from the Mac

```bash
export PATH="$HOME/Library/Android/sdk/platform-tools:$PATH"
adb pair <PIXEL_IP>:<PAIRING_PORT>
# enter the 6-digit code, then:
adb connect <PIXEL_IP>:<DEBUG_PORT>
adb devices
```

### 3. Install and launch

```bash
export JAVA_HOME="$HOME/tools/jdk-17.0.19+10/Contents/Home"
export PATH="$JAVA_HOME/bin:$HOME/Library/Android/sdk/platform-tools:$PATH"
cd ~/projects/pocket-assistant
adb install -r app/build/outputs/apk/debug/app-debug.apk
adb shell am start -n com.varun.pocketassistant/.MainActivity
```

### Verify Phase 0–1

`Start → leave the app / turn screen off → talk → Stop → open session → play speech clips`

Quiet stretches should produce few/no clips; talking should create WAV segments.

## Optional: Android Studio (needs your Mac password)

```bash
brew install --cask temurin@17 android-studio
open -a "Android Studio" ~/projects/pocket-assistant
```

A portable JDK + Android SDK are already set up under `~/tools` and `~/Library/Android/sdk` so CLI builds work without Studio.

Rebuild anytime:

```bash
export JAVA_HOME="$HOME/tools/jdk-17.0.19+10/Contents/Home"
./gradlew assembleDebug
```

## Project layout

- `capture/` — AudioRecord engine, VAD, WAV writer, RecordingService
- `data/` — Room sessions/segments + retention
- `ui/` — Compose home + session detail

Next: swap energy VAD for Silero ONNX, then Phase 2 local transcription.
