# Pipeline Reliability Redesign

**Status:** Approved, not yet implemented
**Date:** 2026-08-05
**Device under investigation:** Pixel 10 Pro, `58111FDCH001EY`, Android 15 (`targetSdk 35`, `minSdk 34`)
**App:** `com.varun.pocketassistant` (pid 31862 during investigation)

---

## Executive summary

The transcription / cleanup / summary pipeline times out constantly. Previous fixes targeted
coroutine timeout values, which was the wrong layer. The timeouts are a **symptom**; the causes
are architectural.

The single dominant cause: **all post-recording network work runs in an Application-scoped
coroutine with no foreground service.** `RecordingService` is a foreground service only while the
microphone is live, and it does not own the pipeline scope. The moment recording stops, the
backlog drains inside a *cached process* that Android Doze is free to throttle and kill. This was
verified directly on the device — no `ServiceRecord` exists, and the app is not
battery-optimisation whitelisted.

That explains the reported symptom precisely: *"a few audio transcribed and then the rest
timeout."* The early segments are processed while the microphone foreground service is still
holding the process up. The remainder are not.

Layered on top are eight further defects, including a retry loop with literally **zero** backoff
(measured: 1 ms between attempts), a connect-timeout amplification that burns 86–111 seconds
failing a 56 KB upload, and an unreachable retry code path where the correct exponential-backoff
logic exists but is called with `maxAttempts = 1`.

The redesign re-hosts background work on WorkManager (which collapses five root causes at once),
hardens the transport layer, and fixes the persistence model. Estimated 16 files across four
concerns.

---

## How to use this document

- **Part 1** is raw verified evidence. Do not re-derive it; it cost a full investigation session.
- **Part 2** is the current architecture as it actually is.
- **Part 3** is the ranked root-cause list. Each entry cites file:line and the evidence that proves it.
- **Part 4** records the three design decisions, the options considered, and *why* each was chosen.
- **Part 5** is the phased implementation plan, split for parallel execution.
- **Part 6** is the verification plan. Several checks are specific to bugs found here and would not
  be obvious otherwise (e.g. asserting retry timestamps are seconds apart, not milliseconds).

Everything in Parts 1–3 is grounded in direct observation. Where something is an inference rather
than a measurement, it is labelled **inference**.

---

# Part 1 — Evidence

## 1.1 Process and service state

Captured immediately after the app had been running and failing ASR:

```
$ adb shell dumpsys activity services com.varun.pocketassistant
(empty — no ServiceRecord)

$ adb shell dumpsys activity processes | grep pocketassistant
*APP* UID 10354 ProcessRecord{3d7eb49 31862:com.varun.pocketassistant/u0a354}
  class=com.varun.pocketassistant.PocketAssistantApp

$ adb shell am get-standby-bucket com.varun.pocketassistant
10                        # ACTIVE — but only because adb interaction reset it

$ adb shell dumpsys deviceidle whitelist | grep -i pocket
(no match — NOT whitelisted from battery optimisation)
```

**The process is alive with no foreground service.** It is a cached process, subject to Doze
network restrictions and to being killed under memory pressure.

## 1.2 Network environment

```
$ adb shell settings get global private_dns_mode
null                      # Private DNS not the culprit

$ adb shell ip -o addr
lo       ::1/128
dummy0   fe80::10af:e6ff:fe72:20f8/64
wlan0    192.168.86.37/24
wlan0    fe80::6c:70ff:fe4c:7d07/64
tun0     fddd:dddd:1000:0:809c:34ca:c78b:ffa3/128     # VPN, IPv6-only, NO IPv4
tun0     fe80::c1b5:f14:1a37:346d/64

$ adb shell ping -c 2 -W 3 openrouter.ai
2 packets transmitted, 2 received, 0% packet loss
rtt min/avg/max/mdev = 27.625/28.224/28.824/0.622 ms
```

Two things matter here:

1. A **VPN is active** on `tun0` with an IPv6-only address and no IPv4. The app's DNS fallback
   hands OkHttp hardcoded **IPv4 literals** (see 1.4), which plausibly have no valid route through
   that tunnel. *(inference — not proven, but consistent with the observed connect failures.)*
2. **The network is healthy right now** — 28 ms to `openrouter.ai`. So the failures were not a
   permanent network break. They cluster in time, which points at Doze windows rather than
   connectivity. This is the strongest single argument that §3.1 is the primary cause.

## 1.3 Measured failure timings (logcat)

The decisive capture:

```
08-05 20:28:54.716 E/PipelineTelemetry: ASR FAIL chunk=1/1 attempt=3/3
    file=asr_prep_speech_1785976300441_100x.wav bytes=16255084
    elapsedMs=111393 error=failed to connect to /104.18.3.115 (port 443) after 15000ms
08-05 20:28:54.717 E/CloudAsr: chunk 1/1 exhausted 3 attempts — skipping

08-05 20:28:55.060 I/CloudAsr: chunk 1/1 attempt 1/3: asr_prep_speech_1785975383152_100x.wav 56684 bytes
08-05 20:30:20.930 E/PipelineTelemetry: ASR FAIL chunk=1/1 attempt=1/3
    file=asr_prep_speech_1785975383152_100x.wav bytes=56684
    elapsedMs=85867 error=failed to connect to /104.18.3.115 (port 443) after 15000ms
08-05 20:30:20.931 W/CloudAsr: chunk 1/1 attempt 1/3 failed: failed to connect ...
08-05 20:30:20.931 I/CloudAsr: chunk 1/1 requeued at end (queueLeft=1)
08-05 20:30:20.932 I/CloudAsr: chunk 1/1 attempt 2/3: ... 56684 bytes (queueLeft=0)
08-05 20:30:20.972 I/OpenAiClient: ASR POST json bytes=56684 model=microsoft/mai-transcribe-1.5
```

Read that carefully. Three separate defects are visible in eight lines:

| Observation | What it proves |
|---|---|
| `elapsedMs=85867` for a **56 KB** file | Failure is unrelated to payload size — it is pure connect-retry walking |
| `error=... after 15000ms` but `elapsedMs=111393` | One 15 s connect timeout is being multiplied ~7× by address iteration + `retryOnConnectionFailure` |
| attempt 1 fails `.931`, attempt 2 starts `.932` | **1 millisecond of backoff.** Not a rounding artefact — the requeue log line sits between them |

Failure cost per segment: 3 attempts × ~90–110 s ≈ **5 minutes**, then permanent `FAILED`.
With a single serial worker (§3.5), every queued segment waits behind that.

## 1.4 DNS

`pipeline/ResilientDns.kt:22-24`:

```kotlin
private val staticFallback = mapOf(
    "openrouter.ai" to listOf("104.18.2.115", "104.18.3.115"),
)
```

The IPs in every failure message are `104.18.2.115` and `104.18.3.115` — **exactly** these two
hardcoded entries. `ResilientDns.lookup` returns system DNS first, then DoH, then this fallback.
Reaching the fallback means **both system DNS and DoH failed**. The app then attempted hardcoded
IPv4 literals, through a device whose only VPN route is IPv6.

These are Cloudflare edge IPs. They rotate. Hardcoding them is a latent permanent-failure bug
independent of everything else here.

## 1.5 Payload sizing

On-device WAV inventory (63 files total):

```
112901164  files/sessions/14815cad-.../speech_1785517577542.wav      # 112.9 MB legacy mega (~59 min)
 19215404  files/sessions/d58b95a9-.../speech_1785967511653.wav
 19215404  files/sessions/d58b95a9-.../speech_1785966357637.wav
 19215404  files/sessions/4cfabe85-.../speech_1785881728579.wav
 19212844  files/sessions/d58b95a9-.../speech_1785976300441.wav
 ... (typical segment = 19.2 MB)
```

The arithmetic, from `AsrAudioPreprocessor.kt:247,264,266`:

```
SAMPLE_RATE          = 16_000 Hz, mono, 16-bit  →  32_000 bytes/sec
CLOUD_MAX_SEGMENT_MS = 600_000   (capture roll)
CLOUD_CHUNK_MS       = 600_000   (upload chunk)   ← IDENTICAL

10 min × 32_000 B/s      = 19_200_000 B  = 19.2 MB    ✓ matches disk
base64 (×4/3)            = 25.6 MB
as a JVM String (UTF-16) = ~51 MB
+ payload.toString() copy = ~51 MB more
+ toRequestBody byte[]    = ~25.6 MB
```

Hence `android:largeHeap="true"` in the manifest. `CloudAndLocalProviders.kt:78-80` does
`wav.readBytes()` then `Base64.encodeToString(...)` — the whole file, fully materialised, twice.

**Because `CLOUD_CHUNK_MS == CLOUD_MAX_SEGMENT_MS`, every segment is always exactly one chunk.**
That single fact defeats the entire chunk-requeue design: `pending.addLast(i)` was meant to space
retries by cycling through *other* chunks, but with `n == 1` the deque immediately re-serves the
same element. That is the mechanism behind the 1 ms retry in §1.3.

## 1.6 Cache leak

```
$ ls -l cache/cloud_asr_prep | head
-rw------- 3384044 2026-08-03 11:26 asr_prep_speech_1785517577542_part001_100x.wav
-rw------- 3082604 2026-08-03 11:26 asr_prep_speech_1785517577542_part002_100x.wav
... (71 files, ~330 MB, dated Aug 3 — two days stale)
```

`CloudAsrProvider.transcribe` (`CloudAndLocalProviders.kt:409-420`) creates `cloud_asr_prep`,
writes the preprocessed file plus every chunk, and never deletes any of it. Same pattern for
`asr_prep` in `ParakeetAsrEngine.kt:157`. Grep confirms `deleteRecursively` appears only in
`AudioStorage.kt:20` (session deletion), never for cache.

## 1.7 Persistence model

Room schema **version 4**, `exportSchema = false`.

```kotlin
// AppContainer.kt:18-22
val database: AppDatabase = Room.databaseBuilder(
    appContext, AppDatabase::class.java, "pocket_assistant.db",
).fallbackToDestructiveMigration().build()
```

**No `Migration` objects exist anywhere in the project.** Any schema change wipes the database.
`OrphanSessionImporter` would rediscover the 63 WAVs on disk, but every transcript, meeting,
title, summary and metadata row would be lost.

`SegmentEntity` (`AppDatabase.kt:24-40`) has no attempt counter, no error column, no
`updatedAtMs`. `transcriptStatus` is the only state field. **The `transcript` column is overloaded
as an error channel** — `TranscriptionQueue.kt:126-130` stores the throwable message, and a magic
`"superseded"` string is pattern-matched in at least three places:

- `TranscriptionQueue.kt:77-81`
- `SessionRepository.requeueForAsr` (`SessionRepository.kt:163`)
- `MeetingRepository.isAssignableRecording` (`MeetingRepository.kt:209`)

`MeetingEntity.cleanedTranscript` holds the **assembled** markdown (summary + transcript
concatenated by `CleanupPrompts.assembleMeetingMarkdown`), conflating two pipeline stages. That is
why summary-failure handling at `MeetingProcessor.kt:199-209` has to re-assemble with a
placeholder title rather than simply retrying the summary stage.

Exhaustive grep for `retryCount|retry_count|attemptCount|attempt_count`: **zero matches.** All
retry state is in-memory coroutine state, lost on process death.

## 1.8 Scope ownership

| Work | Scope | Cancelled when recording stops? |
|---|---|---|
| Mic read loop | `ioScope` (child of `RecordingService.serviceJob`) | **Yes** |
| Notification collector | `serviceScope` (child of `serviceJob`) | **Yes** |
| **ASR transcription** | `AppContainer.appScope` | **No** — process death only |
| **Meeting cleanup / summary** | `AppContainer.appScope` | **No** — process death only |
| Orphan import (one-shot) | `PocketAssistantApp.appScope` | No |

```kotlin
// AppContainer.kt:16
private val appScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
```

`appScope` is never cancelled — it lives as long as the process. There is no `onTerminate`
teardown. The pipeline therefore keeps running after the foreground service dies, in a process
Android no longer has any reason to protect.

`RecordingService` declares `FOREGROUND_SERVICE_TYPE_MICROPHONE` only. `WAKE_LOCK` is declared in
the manifest but **no `PowerManager.WakeLock` is ever acquired.** `onStartCommand` returns
`START_STICKY`, so a restart arrives with a null intent and falls through to `engine.start()`,
beginning a *new* session rather than resuming.

**Zero WorkManager usage** — grep for `WorkManager|CoroutineWorker|OneTimeWorkRequest|Worker`
returns nothing.

**Zero connectivity awareness** — `ACCESS_NETWORK_STATE` is declared; `ConnectivityManager`,
`NetworkCallback` and `NetworkCapabilities` appear nowhere.

---

# Part 2 — Current architecture

```
AudioCaptureEngine (ioScope, child of RecordingService.serviceJob)
  │  VAD-gated capture, rolls segment every maxSegmentMs()
  │  10 min if asrMode==PREFER_CLOUD && cloudConfigured(), else 2 min
  │  writes <filesDir>/sessions/<sessionId>/speech_<epochMs>.wav
  ▼
SessionRepository.addSegment  →  status PENDING  →  transcriptionQueue.enqueue(id)
  ▼
TranscriptionQueue                                    ── AppContainer.appScope ──┐
  Channel<String>(UNLIMITED)                                                     │
  init { for (id in channel) processOne(id) }   ◄── SINGLE SERIAL CONSUMER       │
  │                                                                              │
  ├─ ProviderRouter.transcribe(wav)                                              │
  │    route(mode, allowFallback, ...)  ◄── runCatching swallows Cancellation    │
  │    ├─ cloud: CloudAsrProvider.transcribe                                     │
  │    │    prepareForCloud (silence trim, speed 1.0)                            │
  │    │    splitByDurationMs(CLOUD_CHUNK_MS = 600s)  →  always n == 1           │
  │    │    while (pending.isNotEmpty())                                         │
  │    │      PipelineTelemetry.timed(ASR_CHUNK_TIMEOUT_MS = 5 min)              │
  │    │        client.transcribeAudio(chunk)                                    │
  │    │          withRetries(maxAttempts = 1)   ◄── RETRY LOGIC UNREACHABLE     │
  │    │            base64 whole file → JSON → POST                             │
  │    │      on failure: pending.addLast(i)     ◄── ZERO BACKOFF                │
  │    └─ local: ParakeetAsrProvider (timed, 5 min wall)                         │
  │                                                                              │
  └─ status READY | FAILED | SKIPPED_SILENCE                                     │
                                                                                 │
MeetingProcessor                                      ── AppContainer.appScope ──┤
  Channel<String>(UNLIMITED)                                                     │
  init { for (id in channel) processOne(id) }   ◄── SINGLE SERIAL CONSUMER       │
  │                                                                              │
  ├─ if (!allRecordingsAsrSettled) { delay(15_000); channel.trySend(id); return } │
  │      ◄── SPIN LOOP ON THE ONLY WORKER                                        │
  ├─ status CLEANING                                                             │
  ├─ runStageWithRetries(cleanup, 3)  delay(1_000 * attempt)   ◄── linear, tiny  │
  ├─ runStageWithRetries(summary, 3)                                             │
  └─ status READY | FAILED                                                       │
                                                                                 │
  NOTE: TranscriptionQueue and MeetingProcessor each construct their OWN         │
  OpenAiCompatibleClient → two OkHttpClient pairs → two connection pools,        │
  no shared backoff, rate limiting or circuit-breaker state.  ───────────────────┘
```

---

# Part 3 — Root causes, ranked

### 3.1 No foreground service during post-recording processing — **DOMINANT**

**Evidence:** §1.1 (no `ServiceRecord`, not whitelisted), §1.8 (scope table), §1.2 (network is
healthy, so failures are not connectivity).

`TranscriptionQueue` and `MeetingProcessor` run on `AppContainer.appScope`. `RecordingService` is
foreground only for `FOREGROUND_SERVICE_TYPE_MICROPHONE` and its `serviceJob` is cancelled in
`onDestroy` — but the pipeline does not use that scope. After recording stops, the backlog drains
in a cached process. Doze defers network for such processes; no wake lock is held; the process is
a memory-pressure kill candidate.

This is the mechanism behind *"a few audio transcribed and then the rest timeout."* Segments
processed while the mic FGS is alive succeed. Those after it do not.

### 3.2 Zero retry backoff

**Evidence:** §1.3 — 1 ms between attempts, logged.

`CloudAndLocalProviders.kt:458-461` requeues a failed chunk with `pending.addLast(i)` and no
delay. The design assumed multiple chunks would provide natural spacing; §1.5 shows `n` is always
1, so the deque re-serves the same chunk immediately. Three attempts burn back-to-back, then
permanent `FAILED`.

### 3.3 Connect-timeout amplification

**Evidence:** §1.3 — 56 KB file, `elapsedMs=85867`; 16 MB file, `elapsedMs=111393`; both reporting
a 15 s connect timeout.

`HTTP_CONNECT_TIMEOUT_MS = 15_000` (`PipelineTelemetry.kt:38`), multiplied by the number of
addresses OkHttp walks, multiplied again by `retryOnConnectionFailure(true)` route retries.
`ASR_HTTP_CALL_TIMEOUT_MS = 100_000` is the only bound, and it is reached. Payload size is
irrelevant to this failure mode.

### 3.4 Correct retry logic is unreachable

**Evidence:** `PipelineTelemetry.kt:68` — `CHAT_HTTP_MAX_ATTEMPTS = 1`;
`CloudAndLocalProviders.kt:68` — `maxAttempts = 1` for ASR.

`withRetries` (`CloudAndLocalProviders.kt:269-297`) implements exactly what is needed: `isRetryable`
classification covering `SocketException`, `SocketTimeoutException`, `UnknownHostException`,
`SSLException`, HTTP 429/502/503/504; `isDnsFailure` detection; exponential backoff
(`1_000L * (1 shl attempt)`) with a DNS-specific schedule. With `maxAttempts = 1`, `repeat(1)`
runs once and `attempt == maxAttempts - 1` is immediately true, so it throws without ever
retrying. **All of that logic is dead code.** Retries were hoisted to layers with worse backoff
(§3.2) or none.

### 3.5 Single serial worker plus a spin loop

**Evidence:** `TranscriptionQueue.kt:45-47`, `MeetingProcessor.kt:54-56`, `MeetingProcessor.kt:105-117`.

Both components run exactly one consumer coroutine. One failing segment blocks every other for
~5 minutes (§3.2 + §3.3). Worse, `MeetingProcessor.processOne` calls `delay(15_000)` and
re-sends to its own channel while waiting for ASR — occupying the only worker in a polling loop
that also competes with the very ASR work it is waiting on.

### 3.6 `runCatching` swallows `CancellationException`

**Evidence:** `Providers.kt:187`.

```kotlin
return runCatching { runPrimary() }.getOrElse { primaryErr ->
    if (!allowFallback || !secondaryAvailable) throw primaryErr
    runSecondary()
}
```

`runCatching` catches `Throwable`, including `CancellationException`. Consequences: a coroutine
timeout or scope cancellation triggers cross-provider fallback, and `runSecondary()` then executes
inside an already-cancelled coroutine. Structured concurrency is broken. A transient cloud 502 also
diverts to slow local Gemma, which is rarely what the user wants.

### 3.7 No network-availability gating

**Evidence:** grep — `ConnectivityManager` / `NetworkCallback` / `NetworkCapabilities` absent,
despite `ACCESS_NETWORK_STATE` being declared.

The pipeline uploads into a dead network and consumes its finite attempt budget doing so, rather
than waiting for connectivity.

### 3.8 All retry state is in-memory

**Evidence:** §1.7 — zero persisted attempt counters.

Process death loses all progress. Conversely `requeuePending()` (`TranscriptionQueue.kt:57-61`)
re-drives `FAILED` rows with **no attempt ceiling**, because nothing records how many attempts
have already occurred. Both unbounded-retry and lost-progress failure modes exist simultaneously.

### 3.9 Oversized, fully-materialised payloads

**Evidence:** §1.5 — 19.2 MB → 25.6 MB base64 → ~51 MB UTF-16 String, plus copies.

Marginal against any timeout even on a healthy network, and the reason `largeHeap="true"` is
needed. Also makes all-or-nothing failures worse: with `n == 1` there is no partial progress.

### 3.10 Cache leak

**Evidence:** §1.6 — 71 files, ~330 MB, two days stale.

### 3.11 Rotting hardcoded DNS fallback

**Evidence:** §1.4 — the two hardcoded Cloudflare IPs are exactly the ones that failed.

Plus one un-handled outlier: a **112.9 MB legacy WAV** (~59 min). `TranscriptionQueue.kt:84-93`
skips files over 20 min matching `speech_\d+\.wav`, marking them `SKIPPED_SILENCE` with a
free-text note — which then requires the `"superseded"` string-matching hack elsewhere.

---

# Part 4 — Design decisions

## Decision 1: Execution model → **WorkManager, one work request per unit of work**

**Options considered**

| Option | Assessment |
|---|---|
| **WorkManager + foreground worker** | **CHOSEN.** Collapses five root causes at once. |
| Dedicated `ProcessingService` | Solves only 3.1 and partially 3.7. Everything else — backoff, persistence, dedup, spin-loop removal — would be hand-rolled. That is reimplementing WorkManager, worse. |
| Both, staged | Rejected. `ProcessingService` is real work that would then be deleted, since `setForeground()` already provides the FGS. |

**Why WorkManager specifically:**

| Root cause | Mechanism |
|---|---|
| 3.1 no FGS | `setForeground()` → `dataSync` foreground service, Doze-exempt while running |
| 3.2 zero backoff | `BackoffPolicy.EXPONENTIAL` |
| 3.5 spin loop | cleanup worker returns `Result.retry()` when ASR isn't settled — OS-scheduled, occupies no thread |
| 3.5 head-of-line | one request per segment, bounded executor, no shared serial channel |
| 3.7 dead-network uploads | `NetworkType.CONNECTED` constraint |
| 3.8 in-memory retry state | persisted queue survives process death **and reboot**; `runAttemptCount` is durable |

The `Result.retry()` point deserves emphasis: it replaces the `delay(15_000)` polling loop with
OS-scheduled exponential backoff, and the waiting worker holds no thread. That is a direct,
elegant deletion of 3.5 rather than a workaround.

**Design shape.** Do *not* port the long-running serial queue into a single worker. Use **one
`OneTimeWorkRequest` per unit of work**:

- `asr-<segmentId>` — unique work name, `ExistingWorkPolicy.KEEP` for free dedup
- `meeting-cleanup-<meetingId>` → `meeting-summary-<meetingId>` as a `WorkContinuation`

**Implementation notes**
- Requires `FOREGROUND_SERVICE_DATA_SYNC` permission (Android 14+).
- Android 15 caps `dataSync` foreground services at ~6 h/day. This workload is minutes per
  meeting, so there is ample headroom — **verify during implementation**, do not assume.
- `setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)` on the first attempt so
  processing still starts promptly while the user is watching.
- WorkManager's minimum backoff is 10 s, maximum 5 h — appropriate here.
- Also surface a battery-optimisation exemption prompt in settings; the device is not whitelisted
  (§1.1) and that materially affects reliability.

## Decision 2: Upload size → **smaller chunks + streaming body. Opus deferred.**

**Options considered**

| Option | Assessment |
|---|---|
| Opus/compress + smaller chunks | ~10–30× smaller, but changes bytes sent to the ASR model. Deferred — see below. |
| **Smaller chunks + streaming body** | **CHOSEN.** All the reliability benefit, zero WER risk. |
| Chunk size only | Insufficient — leaves the ~51 MB transient allocations in place. |

**Why not Opus now.** The dominant observed failure is TCP connect, not upload body — a 56 KB file
failed at 86 s (§1.3). Compression does not address that. Meanwhile the codebase records a
measured WER result (`CloudAndLocalProviders.kt:411`: *"silence-only (no 1.35×) cut OpenRouter WER
~29% → ~20%"*), so audio-format changes carry real quality risk against a known baseline. The
configured model is `microsoft/mai-transcribe-1.5`, whose Opus support is unverified. Bundling a
quality-affecting codec change into a reliability fix makes any regression unattributable.

Opus remains a genuine ~10× lever and is worth doing **later, as a separate experiment behind a
setting, benchmarked against the existing 20% baseline.**

**What smaller chunks + streaming buys:**
- Peak transient memory ~51 MB → ~64 KB (removes the justification for `largeHeap="true"`)
- Per-request payload ~25.6 MB → ~5 MB
- **Partial progress:** a 10-min segment currently fails wholesale; at 2 min, 4 of 5 chunks survive
- The requeue-at-end mechanism finally works as designed, because `n > 1` — which independently
  fixes the 1 ms retry of §3.2

**Critical caveat: split at silence boundaries.** Naive 120 s hard cuts introduce four new
word-splitting boundaries per segment, which would regress WER — exactly the metric the user
tracks. `AsrAudioPreprocessor` already computes per-frame RMS (`SILENCE_RMS = 0.012f`,
`MIN_SILENCE_FRAMES_TO_DROP = 8`), so `splitByDurationMs` should split at the **nearest silence gap
to the target duration** rather than at a fixed sample offset. This reuses existing machinery and
is the guard against quality regression.

## Decision 3: Retry state → **WorkManager owns attempts; add columns for correctness and UI**

**Options considered**

| Option | Assessment |
|---|---|
| Full ledger (`attempts`, `nextEligibleAtMs`, …) + migration | Redundant — duplicates WorkManager's persisted `runAttemptCount` and backoff scheduling. |
| **WorkManager owns scheduling; minimal columns** | **CHOSEN.** |
| Not yet | Rejected — three schema defects are worth fixing regardless of retries. |

WorkManager already persists attempt counts and performs backoff, so a parallel Room ledger would
be a second source of truth. But three schema problems are independent of retry policy and should
be fixed:

1. **`transcript` is overloaded as an error channel** (§1.7), which is the root of the fragile
   `"superseded"` string-matching in three call sites. Needs `asrLastError` and a structured
   `skipReason`.
2. **`cleanedTranscript` conflates two stages** — it stores assembled markdown, so summary-retry
   cannot cleanly recover just the cleaned text. Needs `cleanTextOnly`.
3. **No `updatedAtMs`** — a stuck row is indistinguishable from a fresh one.

**Blocking prerequisite: `fallbackToDestructiveMigration()` is armed** (§1.7) with 63 real
recordings and a populated meetings table. Any schema change wipes it. A real `Migration(4→5)` is
mandatory, and destructive fallback must be removed.

Expose attempt count to the UI via `WorkInfo.runAttemptCount` rather than mirroring it into Room.

---

# Part 5 — Implementation plan

Four concerns, with Phase 1 as a hard gate because it defines the shared schema contract.

```
Phase 1  Persistence contract        ── must land first, everything depends on it
           │
     ┌─────┴─────┐
Phase 2A        2B                   ── parallel; 2B is fully independent of the schema
Scheduling    Transport
     └─────┬─────┘
           │
Phase 3  Orchestration + UI          ── depends on 1, 2A, 2B
```

## Phase 1 — Persistence contract (gate)

1. `data/AppDatabase.kt` — bump v4 → v5.
   - `segments` += `asrLastError: String?`, `skipReason: String?`, `updatedAtMs: Long`
   - `meetings` += `cleanTextOnly: String?`, `lastError: String?`, `updatedAtMs: Long`
2. Write a real `Migration(4, 5)` with `ALTER TABLE ... ADD COLUMN` for each. **Remove
   `fallbackToDestructiveMigration()`** from `AppContainer.kt:18-22`.
3. Replace `transcript?.contains("superseded")` with the structured `skipReason` at all three
   call sites: `TranscriptionQueue.kt:77-81`, `SessionRepository.kt:163`,
   `MeetingRepository.kt:209`. Also `HomeScreen.kt` (retranscribe gating).
4. Persist cleaned text in `cleanTextOnly` separately from assembled markdown, so summary-retry
   never re-runs cleanup. Simplifies `MeetingProcessor.kt:190-216`.

## Phase 2A — Scheduling and lifecycle

5. `app/build.gradle.kts` — add `androidx.work:work-runtime-ktx`.
6. `AndroidManifest.xml` — add `FOREGROUND_SERVICE_DATA_SYNC`.
7. New `AsrWorker : CoroutineWorker` — one per segment. Unique name `asr-<segmentId>`,
   `ExistingWorkPolicy.KEEP`, `NetworkType.CONNECTED`, `BackoffPolicy.EXPONENTIAL`,
   `setForeground()` with a dataSync notification, `setExpedited(RUN_AS_NON_EXPEDITED_WORK_REQUEST)`.
8. New `MeetingStageWorker` — cleanup and summary as separate chained requests. Returns
   `Result.retry()` when `allRecordingsAsrSettled` is false, **deleting the `delay(15_000)` spin
   loop** at `MeetingProcessor.kt:105-117`.
9. New `PipelineScheduler` — owns enqueue, dedup and `WorkInfo` observation. Single place the rest
   of the app talks to.
10. Reduce `TranscriptionQueue` and `MeetingProcessor` to pure stage logic invoked by workers.
    Remove their `Channel`s, `init` consumer loops, and `appScope` dependency.
11. Verify the Android 15 `dataSync` 6 h/day cap leaves adequate headroom.
12. Add a battery-optimisation exemption prompt in pipeline settings.

## Phase 2B — Transport hardening (independent, parallelisable)

13. `PipelineTelemetry.kt` — restore `ASR_HTTP_MAX_ATTEMPTS` / `CHAT_HTTP_MAX_ATTEMPTS` to 3
    (currently 1, which disables §3.4's logic). Drop `HTTP_CONNECT_TIMEOUT_MS` 15 s → ~8 s.
    Replace the flat ASR `callTimeout` with `write`/`read` socket timeouts, so a *stalled* upload
    trips but a healthy slow one does not.
14. `withRetries` — add jitter to the existing exponential backoff; make it the single retry site
    for transient transport errors. Remove the zero-delay chunk requeue at
    `CloudAndLocalProviders.kt:458-461`.
15. Streaming base64 `RequestBody` — encode from file in blocks. Never materialise the ~51 MB
    String at `CloudAndLocalProviders.kt:78-80`.
16. `AsrAudioPreprocessor.kt` — `CLOUD_CHUNK_MS` 600 s → 120 s, and make `splitByDurationMs` split
    at the **nearest silence gap** to target using existing per-frame RMS. Leave
    `CLOUD_MAX_SEGMENT_MS` at 600 s so cleanup keeps full context.
17. `ResilientDns.kt` — delete the hardcoded Cloudflare IPs (§1.4, §3.11); cache successful
    resolutions instead.
18. Delete `cloud_asr_prep` / `asr_prep` artifacts after each segment, and sweep both on startup
    (§1.6, ~330 MB reclaimed).

## Phase 3 — Orchestration correctness and UI

19. `Providers.kt:187` — replace `runCatching` with explicit try/catch that rethrows
    `CancellationException`.
20. Fall back cross-provider only **after** transport retries are exhausted, and only for
    non-transient failures — so a 502 no longer diverts to slow local Gemma.
21. Share a single `OpenAiCompatibleClient`. Currently `TranscriptionQueue.kt:32` and
    `MeetingProcessor.kt:39` each construct their own → two OkHttpClient pairs, two connection
    pools, no shared state. Add a circuit breaker marking cloud unhealthy for T seconds after N
    consecutive transport failures.
22. Handle the 112.9 MB legacy WAV explicitly via `skipReason` rather than duration+regex plus
    free-text notes.
23. Surface attempt count (`WorkInfo.runAttemptCount`), next retry time, and `lastError` in
    `HomeScreen.kt` / `HomeViewModel.kt`. The user should never need adb to understand why
    something is pending.

---

# Part 6 — Verification

Several of these assert against specific bugs found in this investigation and would not be obvious
otherwise.

**Build**
```bash
./gradlew :app:assembleDebug
```

**Migration preserves data** — critical, given §1.7.
Count segments and meetings before and after upgrade-install. All 63 recordings and every existing
transcript/meeting must survive. A destructive wipe is a hard failure.

**Backoff is real** — directly targets §3.2.
```bash
adb logcat -s CloudAsr:V PipelineTelemetry:V
```
Successive attempt timestamps must be **seconds** apart. Anything resembling
`20:30:20.931 → 20:30:20.932` is a regression.

**Foreground service present during processing** — targets §3.1.
Stop recording, then while the backlog drains:
```bash
adb shell dumpsys activity services com.varun.pocketassistant
```
Must show a `ServiceRecord` with `dataSync`. Empty output means the root cause is unfixed.

**Survives process death** — targets §3.8.
```bash
adb shell am kill com.varun.pocketassistant     # mid-ASR
```
Work must resume with no manual retry.

**Network gating** — targets §3.7.
Enable airplane mode mid-pipeline. Workers must wait on the constraint rather than burn attempts,
then resume automatically on reconnect.

**Doze survival** — targets §3.1.
```bash
adb shell dumpsys deviceidle force-idle
```
ASR must still complete.

**Payload sizing** — targets §3.9.
Confirm per-request bytes ~5 MB, not ~25.6 MB. Heap-profile for absence of ~50 MB spikes, then
attempt removing `largeHeap="true"`.

**WER guard** — targets the Decision 2 caveat.
Re-run the existing benchmark after silence-aware chunking. No regression from the 20% baseline.

**Cache hygiene** — targets §3.10.
```bash
adb exec-out run-as com.varun.pocketassistant ls cache/cloud_asr_prep | wc -l
```
Must be empty after a successful run.

**Connect amplification** — targets §3.3.
Point at an unreachable host and confirm failure in ~10–20 s, not 86–111 s.

---

# Part 7 — Risks

| Risk | Mitigation |
|---|---|
| Migration bug destroys real data | Test upgrade-install on a DB copy first. `OrphanSessionImporter` recovers WAVs but not transcripts/meetings. |
| Android 15 `dataSync` 6 h/day cap | Measure actual usage; workload should be minutes per meeting. Verify, don't assume. |
| WorkManager feels laggy vs. immediate | `setExpedited(RUN_AS_NON_EXPEDITED_WORK_REQUEST)` on first attempt. |
| 120 s chunking regresses WER | Silence-boundary splitting; re-run benchmark before shipping. |
| Removing hardcoded DNS IPs makes broken-DNS worse | Those exact IPs are what failed (§1.4). Replace with a resolution cache so the last known-good answer is reused. |
| VPN (`tun0`, IPv6-only) still breaks connectivity | Honour system DNS/routing; stop handing OkHttp IPv4 literals. Environmental, but the app should degrade gracefully. |

---

## Appendix A — File inventory

| File | Change |
|---|---|
| `data/AppDatabase.kt` | schema v4→v5, `Migration(4,5)` |
| `data/AppContainer.kt` | remove destructive migration; remove pipeline `appScope` ownership |
| `data/SessionRepository.kt` | new columns; remove `"superseded"` matching |
| `data/MeetingRepository.kt` | new columns; remove `"superseded"` matching |
| `speech/TranscriptionQueue.kt` | strip to stage logic; delete channel + consumer loop |
| `speech/MeetingProcessor.kt` | strip to stage logic; delete spin loop + in-memory retries |
| `pipeline/CloudAndLocalProviders.kt` | restore retries, streaming body, timeouts, cache cleanup |
| `pipeline/PipelineTelemetry.kt` | retune all constants |
| `pipeline/AsrAudioPreprocessor.kt` | chunk 600s→120s, silence-boundary splitting |
| `pipeline/Providers.kt` | cancellation correctness; fallback policy |
| `pipeline/ResilientDns.kt` | drop hardcoded IPs; add cache |
| `ui/HomeScreen.kt` | surface attempts / next retry / error |
| `ui/HomeViewModel.kt` | expose `WorkInfo` state |
| `ui/PipelineSettingsScreen.kt` | battery-optimisation prompt |
| `AndroidManifest.xml` | `FOREGROUND_SERVICE_DATA_SYNC` |
| `app/build.gradle.kts` | WorkManager dependency |
| **new** `pipeline/work/AsrWorker.kt` | per-segment ASR worker |
| **new** `pipeline/work/MeetingStageWorker.kt` | cleanup + summary workers |
| **new** `pipeline/work/PipelineScheduler.kt` | enqueue / dedup / observe |

## Appendix B — Key constants, before and after

| Constant | Current | Proposed | Reason |
|---|---|---|---|
| `HTTP_CONNECT_TIMEOUT_MS` | 15 s | ~8 s | §3.3 amplification |
| `ASR_HTTP_CALL_TIMEOUT_MS` | 100 s | replaced by socket timeouts | kills healthy slow uploads |
| `CHAT_HTTP_MAX_ATTEMPTS` | **1** | 3 | §3.4 — 1 disables all retry logic |
| ASR `withRetries` maxAttempts | **1** | 3 | §3.4 |
| `CLOUD_CHUNK_MS` | 600 s | 120 s | §3.9, and fixes §3.2 via `n > 1` |
| `CLOUD_MAX_SEGMENT_MS` | 600 s | 600 s (unchanged) | preserves cleanup context |
| Chunk retry backoff | **0 ms** | exponential + jitter | §3.2 |
| Stage retry backoff | `1_000 × attempt` | WorkManager exponential | §3.2 |
| Room version | 4 (destructive) | 5 (real migration) | §1.7 data loss risk |

## Appendix C — Reproducing the investigation

```bash
export PATH="$PATH:$HOME/Library/Android/sdk/platform-tools"

# Wireless ADB discovery
dns-sd -B _adb-tls-connect._tcp local.
dns-sd -L "<instance>" _adb-tls-connect._tcp local.
dns-sd -G v4 <hostname>.local.
adb connect <ip>:<port>

# Service / process / Doze state
adb shell dumpsys activity services com.varun.pocketassistant
adb shell am get-standby-bucket com.varun.pocketassistant
adb shell dumpsys deviceidle whitelist | grep -i pocket

# Network
adb shell ip -o addr
adb shell settings get global private_dns_mode
adb shell ping -c 2 openrouter.ai

# App-private files (no sqlite3 on device — inspect the filesystem instead)
adb exec-out run-as com.varun.pocketassistant \
  sh -c 'find files/sessions -name "*.wav" -exec ls -l {} \;'
adb exec-out run-as com.varun.pocketassistant ls -l cache/cloud_asr_prep

# Pipeline telemetry, stacktraces suppressed
adb logcat -d -v time | grep -E \
  "PipelineTelemetry.*(ok|TIMEOUT|FAIL) |CloudAsr.*(attempt|partial|exhausted)" | grep -v "^\s*at "
```
