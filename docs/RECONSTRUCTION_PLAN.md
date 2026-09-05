# Reconstruction Plan: phone build → buildable Kotlin

Goal: bring `main` from "stale (Aug 7)" to parity with the on-device build
(~46 classes ahead), as *editable Kotlin* in `app/src/main/java`, then
commit + push per the DEVELOPMENT.md constitution.

## Why not just build the decompiled source?
The recovered Java in `recovered-phone-source/` is jadx output of a
Kotlin + Jetpack Compose + Room + coroutines build. It contains lowered
`SuspendLambda`/`Function`/`Composer`/`*_Impl` forms and 113 coroutines +
26 Compose file references. Recompiling it as a Java module does not build
cleanly (Compose compiler + KSP codegen cannot be re-derived). It is kept
as an authoritative *reference* for porting.

## Port order (highest value, least risk first)
1. Room schema parity — `data/AppDatabase.kt` migrations
   5->6, 6->7, 7->8 (from `AppDatabaseKt.java` + `*_Impl.java`).
   Blocks nothing but prevents DB fallback on upgrade.
2. Speaker diarization — `speech/DiarizationLabels.kt`,
   `SpeakerCandidate.kt` (from jadx ref). New, additive.
3. Meeting boundary refinement — `meeting/MeetingBoundaryRefiner.kt`
   (from jadx ref). Additive pipeline stage.
4. Benchmark events — `pipeline/BenchEventListener.kt`,
   `benchChat`/`chat` in `OpenAiCompatibleClient.kt`.

## Verification gate (per constitution)
- `./gradlew assembleDebug` must succeed (BUILD SUCCESSFUL).
- Diff produced `app-debug.apk` vs committed `phone-apk/` to confirm the
  advanced classes are now present in the Kotlin build.
- Only then: `git add -A && git commit && git push origin main`.
- Only a pushed, clean-tree build may be installed on the device.

## Known stale-relative gap
`git log` shows the last repo commit was a WorkManager pipeline reliability
change. The phone build adds the 4 items above plus sees the UI evolve
(`PipelineSettingsScreenKt` model picker, `HomeViewModel` meeting
suggestion). Port item-by-item; commit after each green build so progress
is never unrecoverable.