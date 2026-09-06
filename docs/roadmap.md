# Product Roadmap & Milestones — Pocket Assistant

*Status: brainstormed with Varun + grounded in research (docs/research/*). Approved direction.
Companion to docs/product-vision.md; read that first.*

## Confirmed product shape (from brainstorm)
- **Vision**: ambient memory for unrecorded conversations; agent-queryable, verbatim + timed, private.
- **Privacy**: each instance fully private. No shared data, no shared memory fabric. Sharing ONLY via explicit export (e.g. export transcript) — user-initiated, never automatic.
- **Multi-user = "N private instances"**: first ship = Varun + 3-6 trusted colleagues, each running their OWN private instance. "The circle" is the initial user base, not a shared-data model.
- **Retention**: raw audio rolling window (~N days); transcripts retained forever.
- **Consumers**: AI agents (primary), not an in-app search UI.
- **Fundamentals to design in (scale-ready, small footprint)**: privacy, security, battery life, ease of use, AI billing (BYOK-first), data location (self-host-first).

## Research foundation (committed in docs/research/)
- **market-landscape.md**: ambient life-memory is provably open — Limitless (ex-Rewind) acquired by Meta + shut down Dec 2025; no one occupies "Android-first + private + agent-native ambient capture." 57% of meetings are ad hoc (invisible to bots). Position validated.
- **tech-landscape.md**: on-device ASR is viable (Parakeet 0.6B int8 ~6-10% WER, ~1-3%/hr battery with VAD gating); NPU is future; FTS5/hybrid + MCP is the retrieval/agent pattern; per-tenant SQLite → Postgres scale path; BYOK cloud tier for accuracy/cost.
- **consent-legal.md**: WA is all-party consent (strictest US case) — audible/visible announcement ≈ consent (Townsend); Otter's implied-consent model under litigation; design consent UX (indicator, pause/delete, opt-in onboarding, participant-share repair) as product features = legal defense. Local-first + export-only is the trust + legal story.

## Roadmap (milestones, max value order)

### M1 — "A+" Reliable Capture + Transcription (CURRENT FOCUS)
Goal: make the existing capture→transcribe core WORK reliably, performant, no silent errors. Quality-gated: ships to nobody until it meets the bar.
- [ ] **Transcription reliability root-cause** (highest priority): evaluate real on-device local ASR (Parakeet) quality against Varun's actual recording WAVs (phone-data-backup/). Determine: is "poor transcription" a model-quality problem (WER on room audio), a chunking/rollover problem (10-min duration_cap segments failing cloud ASR), or the cloud-fallback masking everything? Evidence to date points at rollover segments + a diagnostic gap (real per-chunk errors were discarded).
- [ ] **Fix the diagnostic gap**: surface real per-chunk ASR errors into segment error state (observed failures were logged only, not persisted).
- [ ] **Capture robustness**: session/save reliability, no data loss, crash-safe.
- [ ] **Local ASR quality bar**: WER target on real recordings; decide on-device vs optional cloud tier per privacy setting (accuracy lever, BYOK).
- [ ] **Battery reality check**: measure ~1-3%/hr on the target Pixel (VAD-gated); set the energy budget.
- [ ] **Privacy/security fundamentals**: encryption at rest (Keystore + SQLCipher), backup exclusion, delete controls. Consent UX baseline (indicator, onboarding opt-in, pause/delete).
- [ ] **Explicit export** (the one sharing path): transcript/meeting export — already designed; lands here as the only share mechanism.
Gate: A+ passes (reliable, performant, no silent failures, measured WER) before any ship.

### M2 — Agent-Queryable Memory (the differentiator)
Goal: expose the private transcript memory to the user's OWN agents with verbatim+temporal retrieval + citations.
- [ ] Retrieval layer: FTS5 verbatim + timestamps (MVP); hybrid embeddings later, gated on real queries.
- [ ] Agent interface: MCP server (or local HTTP) on the user's always-on machine exposing search_transcripts/get_segment/list_sessions over the exported/local store.
- [ ] "What's the status on X" end-to-end loop working with citations.
- [ ] Decide data-access architecture here (what the research flags): local files vs self-hosted host vs BYOK cloud; cost + options documented.

### M3 — Trusted-Circle Release (3-6 users)
Goal: ship to the circle.
- [ ] Onboarding for each user's own private instance (no accounts/auth for sharing — still private per-instance).
- [ ] BYOK cloud-ASR tier + usage metering (billing stays per-user; no central billing platform).
- [ ] Ease-of-use polish: install, first-capture, consent defaults.
- [ ] Closed/internal Play testing track for distribution.

### M4 — Scale Foundations (later; design-in now, build later)
- [ ] Optional self-host sync/backup to user's own hardware (Tailscale-friendly).
- [ ] Optional hosted tier IF shared queries ever wanted (Postgres pgvector, OIDC) — additive, nothing in v1 requires it.
- [ ] Metered-AI billing pattern (industry norm: AI credits above seat).

## Open decisions (to resolve in architecture phase, not blocking M1)
- ASR primary tier: on-device everywhere vs per-conversation privacy tiers.
- Data-access architecture for agents (file export / MCP local / self-host) — decided in M2.
- Vector search in MVP or post-MVP.
- Exact raw-audio rolling window length.