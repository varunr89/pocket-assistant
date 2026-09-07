# Pocket Assistant — Final Locked Architecture

**Status: LOCKED.** Consolidated reference for the M1 feature spec. Supersedes `architecture.md` and `hosting-options*.md` wherever they conflict; absorbs `product-vision.md`, `roadmap.md`, and `research/*`. Do not relitigate anything here except §7.

## 1. System shape — one private pipeline per user, three layers

```
PHONE (system of record)              MANAGED QUERY STORE + AGENT EDGE       USER'S AGENTS
mic → energy-VAD → 16k mono →         Neon Postgres (ONE project/user):      Claude / Cursor / Hermes
on-device ASR (Parakeet 0.6B int8;    sessions, segments, keywords, FTS;     ↓ MCP tools
opt-in BYOK cloud tier) →             pgvector column present, unused in v1  Cloudflare Worker (hosted
ENRICHMENT (clean + keywords) →       ↑ sync: clean transcripts + metadata   MCP layer; stdio + Streamable
SQLite+FTS5 (source of truth)   ──────→  ONLY (no audio, no raw ASR dumps)   HTTP OAuth; reads Neon,
raw audio: on-device rolling                                                  serves + rate-limits agents)
window — never leaves the device
```

- Android-first; iOS later seam. First ship = 3–6 trusted users, each an identical private instance. No shared data, no central accounts/billing; sharing = user-initiated export ONLY. No in-app search UI — AI agents are the only consumers.
- Privacy invariant: the only data that leaves a device is enriched transcripts + metadata. Raw audio dies in the rolling window.

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
| Time-to-value | capture → agent-queryable within 15–60 min (enrich + sync, eventual) |
| Cost | ≤$7/mo hosted @ 6 users; hard caps + metering; no central AI-billing surface |

**Pricing-tier mapping (product thesis, locked):**
- **Freemium tier = on-device transcription.** The privacy + zero-cost story: audio never leaves the device. M1's on-device reliability work is the *go-to-market proof* for this tier — if local ASR cannot hit ≤10% WER on real recordings, the freemium value proposition is not yet proven, and M1 does not ship.
- **Paid tier = cloud-worker transcription (BYOK/cloud, ≤5% WER, opt-in).** The accuracy tier, gated behind the product's future billing surface.
- M1's A+ gate is explicitly the on-device "prove it" milestone; the paid tier rides on it, never before.

## 6. M1 E2E test gates (each must pass before ship)

1. Scripted conversation → capture → enrich → sync → agent query loop: assert enrichment corrections ("Wallei"→"Walley"), all verbatim segments arrive in Neon within the TTV window, `search_ranges`+`fetch_range` return them with citations, agent's answer is quote-grounded with exact times.
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