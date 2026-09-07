# Pocket Assistant — Final Locked Architecture

**Status: LOCKED.** Consolidated reference for the M1 feature spec. Supersedes `architecture.md` and `hosting-options*.md` wherever they conflict; absorbs `product-vision.md`, `roadmap.md`, and `research/*`. Do not relitigate anything here except §7. Amended 2026-09-06 by product decision: ASR provider (§1) and SLO 6 time-to-value (§5) — see `m1-implementation-plan.md` for the re-scoped plan.

## 1. System shape — one private pipeline per user, three layers

```
PHONE (system of record)              MANAGED QUERY STORE + AGENT EDGE       USER'S AGENTS
mic → energy-VAD → 16k mono →         Neon Postgres (ONE project/user):      Claude / Cursor / Hermes
ML Kit GenAI Speech Recognition       sessions, segments, keywords, FTS;     ↓ MCP tools
(Advanced mode, Gemini Nano via       pgvector column present, unused in v1  Cloudflare Worker (hosted
AICore; on-device; foreground-        ↑ sync: clean transcripts + metadata   MCP layer; stdio + Streamable
gated) → ENRICHMENT (clean +          ONLY (no audio, no raw ASR dumps)      HTTP OAuth; reads Neon,
keywords) → SQLite+FTS5 (source   ──────→                                        serves + rate-limits agents)
of truth); raw audio: on-device
rolling window — never leaves
the device
```

- Android-first; iOS later seam. First ship = 3–6 trusted users, each an identical private instance. No shared data, no central accounts/billing; sharing = user-initiated export ONLY. No in-app search UI — AI agents are the only consumers.
- Privacy invariant: the only data that leaves a device is enriched transcripts + metadata. Raw audio dies in the rolling window.
- **ASR provider (product decision 2026-09-06, option A):** primary local ASR is Google's official **ML Kit GenAI Speech Recognition API in Advanced mode** (Gemini Nano via the AICore Android system service; natively supported on Pixel 10 and Pixel 11). Capture stays passive/background and is EXACTLY as before; transcription is **foreground-gated** — the background capture backlog is drained by a foreground catch-up queue when the app is next opened, and the user accepts that segments transcribe at that point. The previous on-device candidates (Parakeet/LiteRT int8, sherpa-onnx CPU) are **demoted to parked plan-B**: their code and benchmark harness remain in the tree, marked parked, not deleted.
- **AICore privacy/data note:** ML Kit GenAI ASR executes on-device through AICore, a Google system service — input audio, inference and transcripts are processed locally and never leave the device on this path. AICore enforces per-app inference quotas and foreground-only use: short-window saturation returns `ErrorCode.BUSY`, the long-duration (per-day) budget returns `PER_APP_BATTERY_USE_QUOTA_EXCEEDED`, and use while not the top foreground app returns `BACKGROUND_USE_BLOCKED` — the latter is an *expected state* for this design, surfaced in diagnostics (no-silent-failure), not an error to retry.

## 2. Data model / enrichment schema (sync surface)

```
NEON (per-user project)                         STAYS ON-DEVICE (never syncs)
sessions(id, started_at, ended_at, tz)          raw audio files (rolling window)
segments(id, session_id, start_ms, end_ms,      ASR raw output + confidence (v1)
  speaker, raw_text, clean_text, confidence,    consent/onboarding state, BYOK keys
  embedding vector(384|768) NULL — day-one
  pgvector column, no code path in v1)
keywords(segment_id, keyword, entity_type)
```

Enrichment is a pipeline stage that CLEANS transcripts AND extracts keywords/entities ("Wallei"→"Walley", "Contena"→"Container") per segment. FTS indexes `clean_text + keywords` — search never touches raw ASR output. Phone SQLite holds the same tables + audio; the Neon schema mirrors it one-to-one (portability by construction).

## 3. Retrieval — LLM-as-retriever over time windows (the core pattern)

- MCP tools return RANGES of verbatim context, never answers: the user's own agent LLM reasons over what it fetches. No vector/semantic work in v1.
- Tool surface: `search_ranges(query, time_from?, time_to?, session_id?, keyword_hint?)` → candidate time-windows via FTS-over-enrichment + coarse filters; `fetch_range(range, limit?)` → verbatim segments with timestamps/session/citation; `list_sessions`, `list_keywords` as discovery aids.
- pgvector stays dormant until real zero-hit queries demand it (instrumented — see §5.2). Adding vector/hybrid later is additive SQL, no redesign.

## 4. Hosting + auth

- Neon per-user free projects (physical isolation, standard Postgres, pgvector available) BEHIND a Cloudflare Worker as the hosted MCP/agent-access layer (Workers Paid $5/mo; 6 users load fits included limits). Posture: **~$5–7/mo total @ 6 users**; hard caps + usage alerts; AI billing stays BYOK per user.
- Worker serves BOTH transports from day one: stdio (local agents, env creds) and Streamable HTTP with OAuth 2.1 (DCR/PKCE, RFC 7591/9728). `Principal{user, scopes}` derived from token; per-user enforcement even though projects are physically separate. Agents never hold DB credentials.

## 5. SLO contract — gates the M1 "A+" ship

| SLO | Contract |
|---|---|
| Transcription | two-tier: local ≤10% WER; BYOK cloud opt-in ≤5% WER — measured on real recordings |
| Capture integrity | zero silent loss — device↔store reconciliation after crash/reboot/offline |
| Battery | 1–3%/hr VAD-gated capture on target Pixel (measured, not assumed) |
| Failure visibility | no silent failures — every segment/session reaches a visible terminal state |
| Time-to-value (SLO 6) | foreground catch-up: the background capture backlog is transcribed when the app is next in foreground; the contract is a measured foreground catch-up rate (audio-minutes drained per foreground-minute), baselined on device — NOT a background 15–60 min eventual window |
| Cost | ≤$7/mo hosted @ 6 users; hard caps + metering; no central AI-billing surface |

**Pricing-tier mapping (product thesis, locked):**
- **Freemium tier = on-device transcription.** The privacy + zero-cost story: audio never leaves the device. On-device = **ML Kit GenAI Speech Recognition (Advanced mode, foreground-gated)** per the 2026-09-06 decision. M1's on-device reliability work is the *go-to-market proof* for this tier — if the ML Kit local ASR cannot hit ≤10% WER on real recordings, the freemium value proposition is not yet proven, and M1 does not ship.
- **Paid tier = cloud-worker transcription (BYOK/cloud, ≤5% WER, opt-in).** The accuracy tier, gated behind the product's future billing surface.
- M1's A+ gate is explicitly the on-device "prove it" milestone; the paid tier rides on it, never before.

### AICore runtime contract (ML Kit GenAI Speech Recognition, Advanced mode)
- **Data locality:** AICore is a Google system service on the device; audio, inference and transcripts are processed locally. SDK is subject to Google's ML Kit ToS; this path adds no app-side audio egress.
- **Foreground-only:** AICore permits inference only while the app is the top foreground app (a foreground *service* is not enough). The app checks the gate before every recognition and treats `ErrorCode.BACKGROUND_USE_BLOCKED` as the expected not-foreground state — recorded in diagnostics per the no-silent-failure SLO, never retried as a transient error.
- **Per-app quotas:** short-window quota exhaustion returns `ErrorCode.BUSY` → exponential backoff. Long-duration (e.g. daily) budget exhaustion returns `PER_APP_BATTERY_USE_QUOTA_EXCEEDED` → drain pauses for the day for the affected work and records it. Quotas are opaque (observable only by hitting them); drain metrics make them visible.
- **Device availability:** Advanced mode is available on Pixel 10/11. The app requests `preferredMode = MODE_ADVANCED`; fallback behavior on unsupported devices (Basic-mode model) is a verified-on-device TODO, not assumed.

## 6. M1 E2E test gates (each must pass before ship)

1. Scripted conversation → capture → enrich → sync → agent query loop: assert enrichment corrections ("Wallei"→"Walley"), all verbatim segments arrive in Neon per the SLO 6 catch-up contract, `search_ranges`+`fetch_range` return them with citations, agent's answer is quote-grounded with exact times.
2. "What's the status on X?" against two seeded sessions: answer contains verbatim quotes + resolvable citations (no paraphrase-only drift).
3. Zero-hit instrumentation: queries with terms absent from enrichment produce recorded zero-hit counters — the metric that would ever justify vectors.
4. Fault injection: kill app mid-capture, airplane-mode mid-sync, device reboot → row counts reconcile device↔Neon; unauthenticated MCP calls rejected; user A cannot read user B's project.
5. Escape hatch: `pg_dump` Neon → restore into empty stock Postgres → identical query results.

## 7. Compatibility + escape hatches

- Phone SQLite is the system of record, always; Neon is a mirror. Re-sync from device rebuilds the store after any host failure or switch (minutes at current data volumes).
- Neon = standard Postgres: `pg_dump`/restore to any Postgres or VPS; logical replication out; no vendor-specific query surface.
- Worker and Neon are decoupled: Worker reads with read-scoped credentials, device writes over its own sync path — losing either side leaves the other untouched.

## 8. Remaining open decisions (only these — need Varun)

1. **Enrichment placement:** on-device pass vs Worker-hosted — cost/privacy/latency of the enrich step (either way data stays within the user's own instance).
2. **Raw-audio window + codec (on-device only now):** 14/30/90 days; WAV (~18 GB @30d) vs Opus (~1.7 GB @30d) — storage vs transcript-verification tradeoff.
3. **Neon free-tier graduation:** when a busy user exceeds the free limit, who triggers/what policy promotes them to Launch (~$15/mo) — policy, not technology.

### RESOLVED (2026-09-07)
1. **Enrichment placement → Hybrid (on-device default, hosted optional).** Build the hosted path now as a fallback, keep on-device default, positioned to drop cloud dependency as local models improve.
2. **Raw-audio window + codec → 30-day Opus 48k (on-device only).** Kept for re-transcription; ~3.5 GB steady-state; speech-grade lossy adequate for ASR re-runs and verification.
3. **Neon free-tier graduation → Deferred.** Revisit when the circle grows; a manual/export path suffices now.
4. **Primary on-device ASR → ML Kit GenAI Speech Recognition, Advanced mode, foreground-gated (2026-09-06).** Capture unchanged; transcription drains in the foreground on next app open (option A). Parakeet/LiteRT + sherpa-CPU demoted to parked plan-B; their code/harness stay in the tree, marked parked, not deleted.