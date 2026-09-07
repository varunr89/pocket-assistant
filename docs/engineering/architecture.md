# Pocket Assistant — Engineering Architecture

**Status:** v1 for review (Phase 5 gate). Decided independently from first principles — privacy, cost, battery, reliability, ease of use. The current codebase is treated as evidence of what exists, not as a decision input. Basis: docs/research/* + Sep-2026 verification (sources inline).

## (a) System architecture — components & data flow

One fully private pipeline per user; no shared infrastructure anywhere in M1–M3.

```
PHONE (system of record)                  USER'S OWN ALWAYS-ON MACHINE            USER'S AGENTS
mic → energy-VAD gate → 16kHz mono ──┐   encrypted store mirror (SQLite)         Claude / Cursor / Hermes
  → utterance WAV segments           ├→ app-managed encrypted sync (Tailscale) → thin MCP server (stdio) ─→
  → on-device ASR (Parakeet 0.6B v3) │   (M1: explicit export only)              search_transcripts, get_segment,
  → encrypted SQLite: segments+FTS5  └─────────────────────────────────────────→   list_sessions (verbatim, cited)
  raw audio: rolling window (see g)
```

- Capture never touches the network; transcription is on-device by default; post-processing (assembly, retries) runs under WorkManager.
- Deliberate reversals vs the current code: cloud-default ASR → on-device default; 10-min monolithic chunks → utterance-level segments; no agent path → MCP + encrypted sync.

## (b) Recommended tech stack + models + costs

| Layer | Pick | Rationale (first principles) | Cost @160 speech-hr/mo* |
|---|---|---|---|
| ASR, default | Parakeet TDT 0.6B v3 int8 via sherpa-onnx | 6.3% avg WER, 11.3% on AMI meeting audio (HF card) — beats whisper-large-v3 (~16% AMI, 10.3% avg per Groq docs) on the domain that matters; non-autoregressive ⇒ zero hallucination; offline, instant, private | **$0** |
| ASR, opt-in BYOK | whisper-large-v3-turbo via OpenRouter ≈$0.011/hr (DeepInfra route; Groq $0.04/hr); gpt-4o-transcribe $0.006/min for high-stakes | Cloud Whisper-class is not proven better on noisy meetings; only gpt-4o-class (~4.1% AA-WER) is a real accuracy lever | $2–6 (turbo) … $58 (gpt-4o, everything) |
| Capture | energy VAD + 16 kHz mono | discards 60–90% of wall time; shrinks ASR work and storage | $0 |
| Store | per-instance SQLite + FTS5 verbatim index | zero infra, per-instance privacy by construction, millions of segments OK | $0 |
| Vector search (post-MVP) | sqlite-vec (int8) or brute-force KNN on-device | sqlite-vec is pre-v1 on Android (loadable builds exist); a personal corpus (~100K seg/yr) is brute-force-fast; gate on real queries | $0 |
| Embeddings | bge-small-en ONNX int8 on-device (default); text-embedding-3-small $0.02/1M tok opt-in | $0 + private vs pennies/mo | $0–0.05 |
| Agent interface | MCP server on user's machine, **stdio** transport (Streamable HTTP later if remote) | MCP 2026-07-28 spec is the de-facto agent API; stdio = no auth surface on a personal machine | $0 |
| Sync | app-managed encrypted push over Tailscale → user's own box | Syncthing-Android discontinued (v1.28.1 last); Tailscale free = 6 users, unlimited devices (Apr-2026 plans) | $0; box ≈$150 one-time mini-PC or ~$5/mo VPS |
| Android runtime | microphone FGS (capture) + WorkManager dataSync (pipeline) | Android 15 caps dataSync FGS at 6 h/day (ample); WorkManager survives Doze/process death | — |

*≈160 speech-hr/mo sustained ambient (tech-landscape). **Totals: M1 = $0/mo. M2 = $0 default; BYOK opt-in $0–6/mo; optional box ~$5/mo. M3 (6 users) = $0 central — each user's own key + own box; no billing platform exists (deliberate).**

## (c) Storage/retrieval + agent-interface decision

- **Retrieval:** SQLite + FTS5 is the MVP retriever — verbatim phrases, timestamps, segment ranges; exactly what "what's the status on X?" needs. Hybrid FTS5+vec (RRF fusion) lands only when real agent queries show semantic need. Postgres+pgvector appears only in the hypothetical team-shared tier (M4+), purely additive.
- **Agent interface:** MCP stdio on the user's always-on machine over the synced store; file-backed (SQLite snapshot), stateless tools per the 2026-07-28 spec; a thin adapter isolates us from spec churn (HTTP+SSE, Roots, Sampling already deprecated). M1 floor = explicit export (consent-safe; product-locked). The phone never serves queries (battery/Doze).

## (d) Privacy/security architecture

- Defense in depth: Android FBE → hardware Keystore AES-GCM data key (non-exportable) → SQLCipher DB + per-segment encrypted audio; `allowBackup=false` (no cloud-backup leak of transcripts).
- Threat model (ranked): casual access → lock screen/FBE; loss/theft → Keystore + SQLCipher + backup exclusion; forensic/rooted → hardware-backed keys + short rolling window.
- **Consent UX is a security control** (WA all-party state): persistent notification, mic indicator, lock-screen pause/delete, opt-in onboarding, auto-purge of no-speech segments, export-only sharing.
- BYOK: user keys never touch a service we run (we run none); sync is E2E-encrypted to the user's own infra.

## (e) Battery/perf envelope

- Target **≈1–3%/hr** VAD-gated capture on the target Pixel (estimate; must be measured in M1 — this number sets the energy budget). Silence ≈ ~1% CPU; ASR only on speech; no network during capture.
- Android realities: microphone FGS + persistent notification are mandatory (and double as the consent disclosure); battery=Unrestricted prompt required; mic FGS can't start from BOOT_COMPLETED → user-initiated sessions (fits the consent model).
- NPU (Tensor G5 AICore) = future lever, not today's default (SDK beta).

## (f) Scaling: personal → 3–6 → team

1. **Personal:** all on-device; agents via export + local MCP. 
2. **3–6 trusted users:** N identical private instances — no shared fabric, no accounts; each adds BYOK cloud ASR (own key, own bill) + own sync box (Tailscale free covers it). Sharing stays export-only.
3. **Team (later, only if shared queries are wanted):** one self-hosted service (Postgres + pgvector, OIDC), per-user stores isolated, MCP moves to Streamable HTTP. Additive — nothing in M1–M3 requires it.

## (g) Open decisions — need your judgment

1. **ASR accuracy floor:** on-device Parakeet everywhere (my recommendation) vs cloud-default for the circle. On meeting audio Parakeet ≈11–17% WER; gpt-4o-class ≈4% at 9–30× cost and audio leaves the device. Settle after M1 benchmarks real recordings.
2. **Agent data path:** export-only (max privacy, manual friction) vs automatic encrypted sync to your own machine (zero-thought, but a standing disclosure pipeline — the consent-legal research flags this distinction). Pins M2 scope.
3. **Raw-audio window + codec:** 30-day WAV ≈10–18 GB on-device (3–5 speech-hr/day) vs Opus-compressed (~10× smaller, 90-day feasible) vs 14-day window. Storage vs verification tradeoff.

*Next per workflow: user review/approval → milestone feature specs. No code written.*
