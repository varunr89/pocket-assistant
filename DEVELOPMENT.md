# Development Constitution (Pocket Assistant)

These are the hard rules for how code is developed, versioned, and deployed
to devices. They are non-negotiable — every contributor (human or agent)
operates under them.

## 1. Everything is version-controlled
- No source code lives only in a compiled artifact or only on a device.
  If it can be represented as source, it is tracked in this repo.
- Build output (`.apk`, `.aab`, `.dex`, `.class`) and large local backups
  (phone data/model dumps) are NOT committed — they are reproducible or
  already captured on disk.

## 2. Only Git-tracked builds reach a device
- An APK may be installed on a phone/virtual device ONLY if it was produced
  from a committed tree:
  - `git status` is clean (or the deliberate work-in-progress is committed),
  - the build was run from that committed tree.
- We never hand-build / hand-install a "one-off" that isn't the product of
  the repo. The installed APK must correspond to a known commit (`git rev-parse HEAD`).

## 3. GitHub is always up to date
- Before deploying a new build (and at the end of each work session),
  the working tree is committed (`git add`, `git commit`) and pushed
  (`git push origin <branch>`) to `varunr89/pocket-assistant`.
- There is no recoverable-only-locally work. A crash, lost machine, or new
  clone must reproduce the full current state from GitHub alone.

## 4. Recoverability
- The remote repo (with history) is the single source of truth. Devices are
  interchangeable: reinstalling the app from a build of HEAD must reproduce
  the intended behavior, with app data treatable as disposable (and separately
  backed up when it has value).
- Decompiled/recovered reference source, when it informs porting work, is kept
  in `recovered-phone-source/` and committed, never deleted.

## Standard workflow
```bash
cd /Users/varunramesh/projects/pocket-assistant
export JAVA_HOME=/opt/homebrew/opt/openjdk@17
export ANDROID_HOME=/opt/homebrew/share/android-commandlinetools
export ANDROID_SDK_ROOT=$ANDROID_HOME
export PATH="$JAVA_HOME/bin:$ANDROID_HOME/cmdline-tools/latest/bin:$ANDROID_HOME/platform-tools:$PATH"

# edit code...

./gradlew assembleDebug          # build from the working tree
# verify on device
adb install -r app/build/outputs/apk/debug/app-debug.apk
adb shell am start -n com.varun.pocketassistant/.MainActivity

# THEN, before calling it done:
git add -A
git commit -m "describe the change"
git push origin HEAD
```