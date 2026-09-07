# M1 Investigation — On-Device Transcription Reliability (Pixel 10 Pro)

Status: ROOT-CAUSE FINDINGS ONLY. No fixes, nothing committed. All claims observed on-device (logcat, live DB, dumpsys, tombstones) on the HEAD build; device restored to pre-test state after.

## (a) Measured on-device reality

Provider actually used: live DB (191 segments) shows 114/116 READY segments
went cloud ASR (grok-stt / mai-transcribe / fish-audio); only 2 ever used
local Parakeet. Saved mode is PREFER_CLOUD — the freemium local path has
essentially never run in real use; unproven by data, not just by quality.
Local Parakeet driven on-device against real session WAVs (PREFER_LOCAL via settings; segments re-queued one-at-a-time):

| file | result | flaked windows | wall/RTF | WER vs cloud ref |
|---|---|---|---|---|
| 99.6s clean meeting | 782 ch | 6/17 (35%) | 19.8s / 0.20 | 46.6% (32% words lost) |
| 600s loud rollover | 1755 ch | 33/148 (22%) | 196s / 0.33 | 84.9% (68% lost) |
| 462s loud | 1389 ch | 29/114 (25%) | 150s / 0.33 | 81.2% (62% lost) |
| 120s clean (rerun) | 1060 ch | 7/25 (28%) | 27.5s / 0.23 | byte-identical to Aug 28 run |
| 600s near-silent | FAILED | — | 3 retries | "Parakeet returned empty transcript" |

- Every success hit 22–35% NPU invoke flakes; each closes + reloads the 1.3 GB CompiledModel mid-file. Output is deterministic (byte-identical 10 days apart) — poor quality is stable, not random.
- 4 native SIGSEGVs (tombstones, ParakeetAsrEngine.transcribe) in ~14 min of local use: whenever >1 worker overlaps (retry + new capture in real life) the app crash-loops. Silent capture: local marks the segment FAILED + 3 retries; identical audio via cloud → SKIPPED_SILENCE (asymmetry confirmed); the 75/191 SKIPPED_SILENCE rows measured near-zero RMS — correctly silent.

## (b) Ranked root-cause hypotheses (each observed on-device)

1. Unsafe shared-engine concurrency — IMPLEMENTATION BUG (confirmed). The lazy
   singleton engine is unsynchronized; the flake handler closes the shared
   CompiledModel another worker is invoking → "handle has been destroyed" →
   SIGSEGV. Seen: 6 concurrent NPU loads at startup, 4 tombstones, crash-loop;
   AsrWorker serialization bypassed by withContext(Dispatchers.Default), ParakeetAsrEngine.kt:97-143,188-204,280.
2. NPU invoke instability — MODEL/RUNTIME (dominant on solo runs). 22–35% of
   windows fail per file even solo; reload-per-flake resets decoder state
   mid-utterance. Needs LiteRT/NPU-side diagnosis (firmware, AOT pack, SoC).
3. Local output quality far off-target — measured. WER-vs-cloud 47/85/81% vs
   the ≤10% SLO; 32–68% word loss; hallucinatory fragments, worst on loud
   audio. Attribution open between (i) Parakeet 0.6B TDT on real room audio,
   (ii) 1.35× time-compress + aggressive trim damaging input, (iii) flake/
   reload corrupting windows. Determinism rules out randomness.
4. Silent-segment asymmetry — IMPLEMENTATION BUG (confirmed): local empty →
   FAILED + retries wastes NPU/battery, shows false failures. Also: seconds-
   arithmetic bug in chunk logs/errors (off 62.5×; cosmetic), providers:470.

## (c) SLO status

- Local ≤10% WER: FAILS — 47–85% measured (vs cloud refs).
- Failure visibility: PARTIAL — WorkManager, asrLastError, dataSync FGS, 120s
  cloud chunking (the 7 old 600s failures now succeed), PROCESSING reclaim all
  verified on-device. Native crashes leave only tombstones (no in-app trace), and silent files surface as FAILED.
- Battery 1–3%/hr: UNVERIFIED — device on charger (level stuck 100%); temp
  27.2→29.2°C during a 3.3-min transcription; needs unplugged drain test.
  Time-to-value: 1 min audio ≈ 20s compute + ~2-4s cold model load (fine for 2-min rolls; 10-min file: 3.3 min compute).

## (d) Fix options (none implemented — input to the fix plan)

- F1 Serialize engine use + crash-safe reload (Mutex; never close an in-use
  handle; single-flight local ASR). Kills class #1. Risk low, size S.
- F2 Diagnose NPU flake root cause: pin LiteRT/SoC versions, CPU fallback for
  flaked windows, persist per-window flake counter. Risk low, size S–M.
- F3 Quality ablation matrix on-device: {speed 1.0 vs 1.35} × {trim on/off} ×
  {clean, loud, silent} vs saved refs — separates model quality from preprocessing damage. Risk none, size S.
- F4 Map local empty → SKIPPED_SILENCE (match cloud semantics); stop retrying
  silence. Risk low, size XS.  F5 Fix chunk seconds arithmetic. Size XS.

## (e) Further on-device measurement needed before fixing

- Unplugged battery drain: 1h VAD-gated capture + one 10-min local
  transcription, batterystats per-UID delta (the unverified SLO gate).
- Human-reference transcripts for 3–5 test WAVs (cloud refs are proxies).
- F3 ablation matrix — highest-value measurement to attribute quality failure.
- Controlled pair-overlap concurrency repro with tombstones, re-run after F1;
  LiteRT verbose logging during a flake; record SoC firmware/build.

Evidence: /tmp/pa_invest/ (ref_*.txt, out_*_parakeet.txt, tombstone_13.txt,
logcat_evidence_1.txt, wavs/). Device restored: prefs + DB byte-checked to
pre-test state (116 READY, 75 SKIPPED).
