# Pocket Assistant — Technical Landscape (Sep 2026)

Research snapshot for the architecture phase: ambient capture that stays private per-instance, queryable by the user's own AI agents, scaling from personal → 3-6 trusted users → small team without building a platform.

## 1. ASR: on-device vs cloud
**On-device is viable for English ambient capture in 2026.** sherpa-onnx (k2-fsa) ships Android builds of Parakeet TDT 0.6B int8 (~0.3 GB, punctuation + casing) plus simulated-streaming variants that run well under realtime on a modern phone; Whisper tiny/base and Moonshine tiny are lighter but worse. Edge-ASR benchmark (arXiv 2604.14493): compact streaming models land ~6–10% WER vs 5.9% (Qwen3-ASR-1.7B) / 6.3% (Parakeet-0.6B-v3) for batch cloud-class models — a real but small accuracy gap on noisy in-person audio.
**NPU is a future lever, not today's default.** Tensor G5 (Pixel 10) adds the TPU/AICore + Gemini Nano, but Google Tensor SDK access is beta and model-curated; sherpa-onnx's QNN path targets Snapdragon, not Tensor. Plan: int8 CPU/GPU via ONNX Runtime; revisit NPU when the SDK opens.
**Cloud = accuracy/cost lever:** Groq whisper-large-v3-turbo ≈ $0.04/hr, large-v3 ≈ $0.11/hr (~200–230x realtime); OpenRouter routes the same models (~$0.03/hr, provider failover). Sustained ambient (~160 speech-hr/mo) ≈ $6–18/mo on Groq — affordable, but audio leaves the device.
**What comparable products do:** Granola captures on-device, transcribes (cloud), deletes raw audio; Limitless = cloud-first transcription + sync + AI search/chat; Omi = open-source, self-hostable pipeline; "Minutes" = fully on-device. The field is split — privacy tiering is a product choice, not forced by tech.
**Shape:** energy-VAD-gated capture → on-device Parakeet int8 per utterance (default) → optional cloud tier per privacy setting; raw-audio rolling window stands. VAD discards ~60–90% of wall time, keeping cloud cost marginal.

## 2. Retrieval & agent interfaces
**FTS5 (built into Room) is the MVP retriever** — verbatim phrase/quote lookup, timestamps, segment ranges, zero new deps. **sqlite-vec** adds KNN vectors in the same SQLite file (binary/int8 quant, Android arm64 prebuilts); the proven pattern is **hybrid FTS5 + vec + RRF fusion** (OpenClaw, dnomia-knowledge). Embeddings: on-device all-MiniLM-L6-V2 / bge-small / snowflake-arctic via ONNX Runtime (MediaPipe USE is notably weak) or cloud text-embedding-3-small (~$0.02/M tokens) on permissive tiers.
**Agent interface — MCP is the de-facto standard in 2026:** Claude Desktop, Cursor, OpenAI Agents SDK all consume MCP (stdio or streamable HTTP). Options: (a) a small MCP server on the user's always-on machine exposing memory tools (search_transcripts, get_segment, list_sessions) over the synced/exported store — reference servers (@modelcontextprotocol/server-memory, sqlite, filesystem) show the pattern; (b) plain local HTTP; (c) file export (already product-decided) as the portable floor. Keep a thin adapter layer — the MCP spec is still moving.
**Scale ceiling:** per-instance SQLite handles millions of segments; team-scale migration is the same schema on Postgres + pgvector, or Turso/D1 embedded replicas (per-tenant SQLite is a recognized multi-tenant model).

## 3. Battery reality of always-on audio
- Continuous mic capture keeps the phone out of deep Doze: mic + ADC + DSP sits in the tens of mA (AOSP power model puts display-ambient alone near 100 mA). Realistic envelope: **≈1–3%/hr on a 4–5,000 mAh phone with VAD gating** (silence ≈ negligible compute); running the ASR model continuously roughly doubles it. An 8–16 hr day ≈ 8–25%.
- Minimize: 16 kHz mono; energy VAD (Silero/sherpa) at ~1% CPU wakes the ASR model only on speech; write audio in chunks; no network during capture.
- Android realities: `foregroundServiceType=microphone` (mandatory on 14+), persistent notification + mic indicator (good consent UX), and users must set battery = **Unrestricted** on Pixel or the service gets killed.
- **Shape:** acceptable at 1–3%/hr with gentle controls — one-tap Pause on the notification, auto-pause during calls, optional day-parting. Must be measured on the target Pixel early; this number decides the whole energy budget.

## 4. Privacy/security fundamentals
- **App-level encryption is defense-in-depth, not the first line:** Android FBE already encrypts app-private storage at rest. Recommended stack: hardware-backed Android Keystore (AES-GCM, non-exportable) wrapping a per-install data key → Tink (androidx security-crypto's EncryptedSharedPreferences/EncryptedFile are deprecated) → SQLCipher for the Room DB + encrypted audio segments. Set `allowBackup=false` (or exclude DB/audio) so cloud backup never leaks transcripts.
- Keys never leave the TEE; StrongBox keyspace for high-assurance. Device-loss recovery: Find My Device wipe + restore from an *encrypted* export is enough for a personal recorder.
- **Threat model (ranked):** casual/curious → lock screen + FBE; device loss/theft → Keystore + app encryption + backup exclusion; forensic/rooted extraction → hardware-backed keys, no plaintext at rest, short audio window. The realistic top risk is social, not cryptographic — guests hearing "their" conversation replayed — so consent UX (notification, mic indicator, per-session pause) is a security control, not a nicety.

## 5. Scale path without building a platform
1. **Personal (now):** all on-device; export = JSONL transcripts to the user's own disk; agent access via a small MCP server on the user's always-on machine reading that store.
2. **3–6 trusted users:** per-instance stays identical; add optional **BYOK cloud ASR** (each user's own OpenRouter/Groq key — billing never passes through you); sharing stays export-only. Backup: encrypted push to each user's own box over Tailscale (Android Syncthing is unstable post-2024; prefer app-managed sync into a folder).
3. **Small team later:** only if shared queries are wanted — one Docker Compose service (Postgres + pgvector, or SQLite + Litestream) behind OIDC, per-user stores isolated, MCP moves to the host. Nothing in v1 (accounts, auth, billing, sync server) is required by this path — all additive.

## Open decisions for the architecture phase
1. **ASR primary tier:** on-device Parakeet int8 everywhere vs per-conversation privacy tiers (device default, cloud opt-in for accuracy)? Sets accuracy, battery, and the entire cloud-billing story.
2. **Agent interface for first ship:** MCP server on the user's machine vs file-export-only + local HTTP? MCP needs a store the agent's host can read — this pins the sync/export shape.
3. **Vector search in MVP or post-MVP:** FTS5 alone covers verbatim quotes; semantic recall needs embeddings (on-device ONNX vs cloud API). Gate on real query patterns rather than building both upfront.