# M1 — Foreground-gated ML Kit GenAI ASR: re-scoped implementation plan

Status: ACTIVE after Varun's 2026-09-06 product decision (option A — foreground-only official transcription). Supersedes the CPU-first sherpa plan (committed in 0322ae9) for all not-yet-landed increments. Landed work is reassessed below; nothing is reverted without cause. Amended 2026-09-07 by the meeting-layer spec (75f596b): capture becomes schedule-gated and a meeting queue/UX layer is added on top of the drain (increments 3–7).
Basis: `AGENTS.md`, `docs/product-vision.md`, `docs/engineering/architecture-final.md` (amended same day), and current source.

## Product decision (product intent, not to be relitigated here)
- Capture is now SCHEDULE-GATED (default weekdays 08:00–17:00, user-editable, manual override toggle) but the capture STACK is unchanged: mic → VAD → 16k mono segments → raw audio kept on-device, 30-day Opus48k rolling window.
- Transcription moves to the FOREGROUND using Google's official on-device ASR: the ML Kit GenAI Speech Recognition API, Advanced Mode (Gemini Nano via AICore), natively supported on Pixel 10 and Pixel 11 series.
- Catch-up transcription must be fast so the user doesn't wait long; the user picked option A = foreground-only official transcription, accepting that segments transcribe live while the app is open (foreground). SLO 6 becomes a measured foreground catch-up rate (see `architecture-final.md` §5).

## What changes vs the old plan
- The old leading candidate (sherpa-onnx CPU int8) is demoted to parked plan-B together with Parakeet/LiteRT. Parked harness files are NOT deleted — see "Parked" below.
- Old increments 2 (freemium local default), 3 (silence semantics), 5 (input policy) and 6 (promote winner) are re-scoped onto the ML Kit path and renumbered below. Old increment 7 (capture/save chronology) and 8 (qualification) keep their requirements, renumbered.
- Increment 1 (native lifecycle discipline, d1a89a6) STAYS foundational: the capture engine and any future LiteRT plan-B share the same single-flight lifecycle contract. The ML Kit path does not run through NativeEngineGate (AICore owns its own lifecycle), but the concurrency discipline it forced onto the tree is never regressed.
- Increment 4 (CPU-first benchmark, 485f54d) STAYS as parked measurement tooling, not a production path.
- The 2026-09-07 meeting-layer spec (75f596b) adds a queue/UX layer on top of the drain: Room `meetings` model + reorder/priority, cancel→wind-down, resume-from-progress, schedule-gated capture, and manual `+` boundary + split/combine (increments 3–7). Capture becomes schedule-gated (default weekdays 08:00–17:00, editable, manual override) but the capture stack is unchanged.

## API surface verification (done 2026-09-06, before any code)
- Artifact: `com.google.mlkit:genai-speech-recognition:1.0.0-alpha1` (Google Maven). ALPHA — no SLA or deprecation policy, breaking changes possible; pin exact version, re-verify on bump.
- Verified against the official guide (developers.google.com/ml-kit/genai/speech-recognition/android), the official sample (googlesamples/mlkit, android/speech/SpeechRecognitionActivity.kt) AND bytecode (`javap` on the shipped classes.jar):
  - `SpeechRecognition.getClient(speechRecognizerOptions { locale = …; preferredMode = … })`; modes `SpeechRecognizerOptions.Mode.MODE_BASIC / MODE_ADVANCED`.
  - `SpeechRecognizer`: `suspend checkStatus(): Int?` (vs `FeatureStatus.{AVAILABLE, DOWNLOADABLE, DOWNLOADING, UNAVAILABLE}`), `download(): Flow<DownloadStatus>`, `startRecognition(request): Flow<SpeechRecognizerResponse>`, `suspend stopRecognition()`, `close()`.
  - `speechRecognizerRequest { audioSource = AudioSource.fromPfd(pfd) /* or fromMic() */ }`.
  - `SpeechRecognizerResponse` sealed: `PartialTextResponse(text)` (may change), `FinalTextResponse(text)` (accumulate), `CompletedResponse` (session end), `ErrorResponse(e: GenAiException)`. Non-streaming = collect one flow to completion.
  - `GenAiException.ErrorCode`: `BUSY = 9`, `PER_APP_BATTERY_USE_QUOTA_EXCEEDED = 27`, `BACKGROUND_USE_BLOCKED = 30` (official GenAiException reference; codes are const-verified).
- Hard input contract for file audio (official docs): raw headerless PCM16, mono, 16 kHz, delivered to the file descriptor at a REAL-TIME rate — full-speed reads from a regular file are NOT supported. Consequence: catch-up is paced (≈1× audio duration) via a pipe from the segment WAV (our capture already writes 16k mono PCM16 with a 44-byte RIFF header to strip). The 1× pacing floor means SLO 6 is measured, not assumed — and whether AICore accepts faster feeding is a device-only experiment, never assumed in code.
- AICore runtime contract (recorded in `architecture-final.md` §5): on-device system service, data stays local, per-app quotas, foreground-only inference.

## Ordered increments (small; each = files, green `./gradlew :app:testDebugUnitTest :app:lintDebug :app:assembleDebug`, commit + push main, report)

### 1. Docs re-scope — DONE (this commit)
`architecture-final.md` ASR/SLO-6 rewrite + schedule-gated capture + meeting-layer amendment + this plan. No code.

### 2. Foundation: foreground-gated ML Kit GenAI ASR + catch-up drain — DONE (39bf262)
- NEW `speech/MLKitGenAiAsrEngine.kt`: thin wrapper over the verified surface above. WAV header validation (16k mono PCM16 required — mismatch is a visible failure, never fed to AICore), pipe-based real-time-paced feeding, FinalTextResponse accumulation until CompletedResponse, typed failure classification, model `checkStatus()` + on-demand `download()`.
- NEW foreground gate (activity-lifecycle-based `isForeground()`), drain processor and `TranscriptionDrainService`: claims PENDING segments only while the app is foreground; persists READY/SKIPPED_SILENCE/FAILED exactly like `AsrStage`; BUSY → exponential backoff; PER_APP_BATTERY_USE_QUOTA_EXCEEDED → defer that segment to the next day (recorded); BACKGROUND_USE_BLOCKED → expected state, recorded in diagnostics, drain waits. Not foreground → no-op and wait.
- Unit tests (JVM, mocked recognizer + queue boundaries) for gating, claim/persist, and all four error classifications.
- Untouched: capture, segment persistence, `ProviderRouter`/cloud wiring (`cloud_stt`) — the paid tier later. The drain takes ownership ONLY in local mode (PREFER_LOCAL); until increment 8 removes the legacy path's local-mode enqueue, a rare overlap with the background Parakeet worker is possible (last-write-wins on READY; noted as a known follow-up, not a correctness hazard).
- Do not delete or rewire sherpa increment-4 harness files; mark them parked in docs only (done here).

### 3. Meeting model + queue (Room) — the unit of attention
- EXTEND the existing Room `meetings` table — do NOT create a new one. It already exists in code (`AppDatabase.kt:59`, DB version 8: id, title, startedAtMs, endedAtMs, status, cleanedTranscript, metadataJson, cleanupProvider, cleanTextOnly, lastError, updatedAtMs, createdAtMs) and `segments.meetingId` (nullable FK) already links segments to it. Add `priority`, `manual_start_marker` and a descriptor cache. The existing `status` column is the CLEANUP-pipeline axis (MeetingStatus: PENDING_CLEANUP/CLEANING/READY/FAILED, driven by MeetingStage/MeetingStageWorker); the queue needs a transcription-axis status (waiting/transcribing/done). Introduce the transcription axis ALONGSIDE the cleanup axis and write the explicit reconciliation with MeetingStage (one table, two axes, no transition collisions) in the engineering architecture BEFORE any code. Segments roll into a meeting via the existing VAD on/off rolloff (a meeting starts when VAD first triggers with no active meeting; a segment begins a new segment on rolloff). Segments are NEVER shown as status units — the meeting is the unit.
- Queue order = persisted per-meeting `priority` + `started_at`; new meetings insert oldest-first; manual drag = explicit priority for existing rows. Today screen = reorderable list of today's meetings with per-meeting status chip (waiting / transcribing / done) and a time-range title ("Meet 09:30–10:30", 10-min granularity) as the default label.
- Drain re-route: `TranscriptionDrainService` selection now walks the queue (priority-sorted meetings → their segments oldest-first) instead of raw PENDING segments; claim/persist/typed-error logic unchanged. This is the live-while-open path — while foreground, capture feeds the queue near-real-time.
- Sync rider (spec line 29): meeting metadata mirrors into the Neon `sessions` row — no Neon schema change, metadata rides on `sessions`; the on-device `meetings` table itself never syncs.
- E2E gate: a scripted 3-meeting morning yields exactly 3 meeting rows with start times; drag-reordering a waiting meeting above the transcribing one changes *next* selection only.

### 4. Cancel → graceful wind-down + slot #2
- Cancel (the supported interruption): the current *segment* finishes to completion, its partial transcript is saved, the meeting is marked `waiting` and inserted at slot #2 (just below top; if it was the only meeting it remains top), the new top meeting starts next. No meeting is ever restarted from zero.
- Adds a Cancel-reader to the drain (finish current segment, stop claims); reordering never interrupts the in-flight meeting and only affects *next* selection.
- E2E gate: cancel → current segment completes, partial text persists, meeting appears at slot #2 marked waiting, top meeting's transcription starts.

### 5. Resume-from-progress / no data loss
- Partial transcript text is written per segment as it finalizes. On app reopen the queue rebuilds from `meetings` + segment state: done segments stay done, the in-flight meeting resumes from the first unfinished segment.
- Device↔Room reconciliation audit: completed segments are never re-transcribed.
- E2E gate: kill the app mid-meeting-transcription → reopen → in-flight meeting resumes from the first unfinished segment; completed segments are never re-transcribed.

### 6. Schedule-gated capture — DONE (this commit)
Landed as the "3a" half of the meeting-layer decomposition: weekly schedule + override + settings persistence + engine gate ONLY. The meetings model/queue half ("3b") remains in increments 3-5.
- Weekly user-editable capture window (default weekdays 08:00-17:00); outside it the mic is off — nothing captured (battery + privacy). "Mic on outside schedule" manual override toggle (Today screen + Settings) for one-offs; toggling it is the only start/stop interaction — the schedule replaces tap-to-start.
- Capture stack inside the window is unchanged: passive mic → VAD → 16k mono segments → on-device raw audio, 30-day Opus48k rolling window. Settings capture-schedule editor is the one net-new settings surface.
- E2E gate: with default schedule, zero segments/raw-audio bytes are written outside weekdays 08:00–17:00 unless override is on.
- Tracked follow-ups (from the 3a review; not landed here): (1) boot receiver — after a device reboot capture stays off until the app is opened (START_STICKY does not span reboots); (2) session finalization — engine.stop() and its SessionStatus.COMPLETED + applyRetention call were removed together with the manual stop controls (B1), so end-of-day session completion and the retention trigger return with the day-boundary/boot follow-up; until then sessions roll over via startSession() reuse and retention prunes nothing (data-safe direction); (3) instrumented Room migration test and an engine zero-write seam (prove zero audio bytes outside the window without a full device engine).

### 7. Manual '+' boundary flag + split/combine
- A manual "+" button on the Today screen sets a `manual_start_marker` flag on the current segment (defines a meeting boundary; deliberately no richer mechanism). Split/combine in the UI edits segment session membership; both persist and re-render the queue.
- Diagnostics: the same screen used for BUSY/quota now shows per-meeting blocked states (`blocked/day-quota` → "waiting (paused today)"; `BACKGROUND_USE_BLOCKED` recorded, never surfaced as a user error).
- E2E gate: split/combine/`+` edits persist across app restarts; not-foreground produces zero retries and no user-facing error.

### 8. Routing: freemium = local via the drain
`PipelineConfig` default flip for freemium (PREFER_LOCAL, ASR cloud fallback disabled) WITHOUT touching saved explicit cloud choices; stop enqueueing `AsrWorker` for local-mode segments so the foreground drain is the single local owner; cloud/paid path unchanged (`cloud_stt` still reachable when configured). Removes the increment-2 overlap note.

### 9. Typed outcomes on the ML Kit path
Port old increment 3 semantics: typed complete/no-speech/error outcomes; blank/validated-silence segments become SKIPPED_SILENCE without quota burn; incomplete final text is never published as READY; failures keep diagnostics and audio.

### 10. Model availability + first-run UX
checkStatus/download flow surfacing (FeatureStatus.DOWNLOADABLE → guided download), AICore setup guidance for fresh/reset devices (feature config download can take minutes–hours; bootloader-unlocked devices unsupported), advanced-mode availability surfacing (Pixel 10/11 vs fallback).

### 11. SLO 6 measurement (catch-up rate)
Instrument drain throughput (audio-minutes per foreground-minute), queue delay, BUSY/quota/blocked counters; produce the honest baselines that define the SLO 6 target before qualification. Includes the faster-than-realtime feeding experiment (device-only evidence, gated behind a flag).

### 12. Capture/save chronology gaps (old increment 7, unchanged)
Regression-first fixes in RecordingService destroy, AudioCaptureEngine processFrame/close (engine.stop() left the tree with the manual stop controls in inc 3a), WavWriter, OrphanSessionImporter, SessionRepository.addSegment: awaited finalization, interrupted WAV recovery, idempotent file↔row reconciliation, sample-based boundaries, no duplicated rollover frames. Provider-independent — required regardless of ASR path.

### 13. Qualification (old increment 8, updated)
Same structure, new path: held-out consented corpus, human-scored WER with S/D/I/N and the ≤10% local gate UNCHANGED, energy 1–3%/hr VAD-gated capture, integrity/visibility fault injection, plus foreground catch-up rate and quota-handling e2e proofs. Pixel 10 results do not certify Pixel 11. No automatic corpus upload; no private fixture commits. Measurement-only results do not authorize shipping.

## Parked (retained, not deleted; re-activation needs new approval)
- androidTest `pipeline/SherpaAsrBenchmarkAdapter.kt`, `pipeline/CpuFirstBenchmarkTest.kt`; `libs/sherpa-onnx-1.12.27.aar` (androidTestImplementation dep); `scripts/run-cpu-benchmark.sh`; `scripts/push-local-models.sh` artifact pins; `benchmark-results/` (gitignored).
- Production Parakeet path: `pipeline/ParakeetAsrEngine.kt`, `pipeline/NativeEngineGate.kt` (+ tests), `ParakeetLocalModels`, LiteRT/jlibrosa deps, `U/` reference sources. The native-lifecycle contract stays live as the capture/future-native discipline (increment 1).

## Sources
- Official: https://developers.google.com/ml-kit/genai/speech-recognition/android
- Official overview (quotas/foreground): https://developers.google.com/ml-kit/genai
- Error codes: https://developers.google.com/android/reference/com/google/mlkit/genai/common/GenAiException.ErrorCode
- Sample: https://github.com/googlesamples/mlkit/tree/master/android/speech
- Prior art on quotas measured against bytecode: https://github.com/NagaYu/aicore-radar (cross-check only — primary source is Google's reference above)
- Earlier plan sources: [3] https://ai.google.dev/edge/litert/next/npu · [4] https://alejandrocordon.com/blog/2026/07/19/whisper-parakeet-on-device-in-production · [8] https://huggingface.co/nvidia/parakeet-tdt-0.6b-v3/raw/main/README.md