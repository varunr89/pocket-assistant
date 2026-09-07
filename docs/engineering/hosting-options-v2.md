# Hosting, Search & DB Options — v2 (Sep 2026)

**Status:** Evidence-grounded options report for the backend decision. NO verdict — the choices are framed for you.
Supersedes/extends `hosting-options.md` v1 §1–2; adds real-product evidence, a search-engine verdict, a 4-option of
Neon/D1/Supabase/VPS, and escape paths. Load math unchanged (20MB transcripts/mo/user; 58K seg/mo/user; ~100 agent
queries/day; audio rolling window in R2). Sources re-verified Sep 2026, cited inline.

## 1. What comparable products actually ship

| Product | DB / store | Search | Agent/API | Where data lives / auth |
|---|---|---|---|---|
| Granola | audio never saved; cloud transcript store, no DB disclosed | cross-meeting "where did we talk about X" chat | Business-tier API+webhooks only | cloud workspace, account auth, SOC 2 [docs.granola.ai transcription] |
| Otter | audio→AWS at record-press; "**mix of vector DBs, full-text engine + relational DB**" (hiring post) | hybrid vector+FTS AI Chat | **MCP server (mcp.otter.ai)**; API Enterprise-gated | cloud account; AI-training opt-in-by-default |
| Fireflies | GCP compute, DB in AWS VPC [security FAQ]; no public schema | sentence+timestamp search, AskFred | REST API (audio-file transcription) | org workspace; bot-consent friction |
| Rewind/Limitless | local-first on-device store [HN 2023]; cloud sync added pre-shutdown | local semantic ask | none | per-seat; Meta-acquired, shut Dec 2025; raw archives not movable [luci.memories.ai] |
| Windows Recall | **SQLite ukg.db (AES-256-GCM) + DiskANN vector files**, 8 embedding variants [TotalRecall GH] | SQLite FTS + DiskANN ANN | Windows Semantic Index API for 3rd-party apps | on-device, Hello-bound |
| Glean | proprietary connector index | **hybrid lexical+vector: "vector alone isn't enough"** [glean.com/blog] | enterprise Assistant API | org-wide fabric (the model we reject) |
| Zoom AI Companion | closed; transcripts in Zoom Cloud | ZoomMate agentic search | Zoom Apps/Teams APIs | admin-controlled tenant |
| Omi (OSS) | **Firestore + Upstash Redis + Pinecone** [backend README] | Pinecone KNN + plugin memory | MCP API keys (`mcp_api_keys`) | self-hostable, per-user |
| AnythingLLM (OSS) | **embedded LanceDB default** (on-disk), pluggable pgvector/Chroma/Milvus/Pinecone [docs] | hybrid FTS+vector | MCP webhook agent | fully local |
| Mem0/Letta (OSS) | pluggable store; default Qdrant (local /tmp); pgvector etc. = "config change" [mem0.ai/blog] | vector + keyword dual-channel | MCP | per-user memory layer |

Read: agent-native players now ship MCP (Otter, Omi, AnythingLLM); the DB pattern is uniformly **FTS+vector hybrid — nobody ships
pure vector**; open-source local stacks default to embedded file-based stores (LanceDB/sqlite-vec), i.e. no server needed at personal scale.

## 2. Search verdict at THIS scale: pgvector vs FTS5 vs sqlite-vec

- **FTS5/BM25 covers the v1 SLO.** "Find the exact quote about X" is lexical by construction: agents return verbatim, cited
  segments. Field evidence: a solo agent-builder dropped vectors at ~1K memories after FTS5 measured ≈ equal quality on his
  queries — "you search using the actual vocabulary in the documents" [dev.to kuro comment]; Glean still runs lexical as a
  co-equal pillar at enterprise scale.
- **Vectors earn their keep only when query vocabulary ≠ transcript vocabulary.** Two real triggers exist for us: (a) paraphrase
  queries, and (b) ~10% WER on-device ASR mis-transcribing the exact words an agent searches. Both are real — and both are
  cheaper to attack first with the mechanical fixes already planned (BYOK ≤5% tier for high-stakes audio; proper nouns mostly
  survive noisy ASR). Gate: instrument zero-hit agent queries; add embeddings when misses are semantic, not spelling.
- **If you pick any Postgres host (Neon/Supabase/VPS), enable pgvector day-one anyway.** Free at this scale, hybrid RRF is one
  SQL query away [rivestack.io hybrid pattern; jkatz.github.io RRF-in-SQL], and vectors ride pg_dump wherever the DB goes.
- **sqlite-vec (on-device):** brute-force KNN measured fast through 100Ks–1M vectors — 3–15 yrs of one user's segments; bit/int8
  quantization stretches it further [alexgarcia.xyz sqlite-vec v0.1.0]. Pre-v1 on Android (matches architecture.md).
- **D1:** FTS5 semantics, but **no in-DB vectors** — Cloudflare Vectorize is a separate paid service (50M queried dims incl.) [CF pricing docs].
- **Honest verdict:** the vector question is *not urgent at 58K seg/mo + 100 q/day*; it is a **host decision** — effectively free on
  Postgres hosts, forced-off on D1, future-readiness on-device (sqlite-vec). Ship FTS5 v1 everywhere; keep the hybrid seam.

## 3. The four host options (6 users, transcripts-only, Android-first, 15–60min eventual, B-seam)

| | **A. Neon per-user Free** | **B. Cloudflare D1+R2+Workers** | **C. Supabase Pro (one project)** | **D. VPS + Postgres (self-host)** |
|---|---|---|---|---|
| **$/mo @6** | **$0–1.50** — 100 projects, 0.5GB + 100 CU-hr *each*, hard-stop at limits (a natural cap); R2 audio ≤$1.47. Busy users → Launch $0.106/CU-hr (~$15/user) [neon.tech/pricing] | **$5–6.50** — Workers Paid $5 (10M req/30M CPU-ms incl.); D1 incl. 25B reads+50M writes/mo ≫ our ~9M/350K; R2 ≤10GB free; overages pennies, egress $0 [CF pricing docs] | **$25 flat** — 8GB DB, daily backups (7d), spend cap ON = the only true hard cap; Pro compute credits cover 1 Micro. Per-user ×6 = +$10/Micro each → ~$75+ [supabase.com/pricing] | **≈$6–12** — one 4GB box serves all 6 (CX22-class €5.5–8.5 after Apr-2026 Hetzner repricing); + offsite backup/domain [betterstack, costgoat] |
| **Vectors** | pgvector built-in | none in D1 (Vectorize add-on) | pgvector built-in | pgvector (apt/docker) |
| **Agent access** | MCP stdio on user machine; Streamable HTTP + per-project secret; Neon Managed Better Auth free ≤60K MAU for later OAuth | **hosted remote MCP** (Worker + CF Access OAuth) — agents reach memory with user laptop OFF | remote MCP with Supabase Auth as OAuth-2.1 server (their documented FastMCP pattern); RLS JWT per user | your own remote MCP on the box (Tailscale/bearer); stdio locally |
| **Lock-in / portability** | pure Postgres: pg_dump + logical replication out — trivial | weakest: SQLite semantics; `wrangler d1 export`/Time-Travel→R2; SQLite→PG conversion = real one-time work | pg_dump out; auth/storage are Supabase-specific (rebuild app-level, cheap here) | best: plain Postgres in any direction, incl. into Neon/Supabase later |
| **Ops burden** | near zero; no SLA, autosuspend cold-starts | near zero; Worker CPU uncapped on paid → rate-limit | lowest of managed; one shared RLS surface = standing correctness burden | all yours: patches, PG upgrades, backups, monitoring |
| **When to pick** | per-user *physical* privacy + Postgres portability at $0 — matches the locked "N private instances" decision best | agents reach memory from anywhere with zero user-host dependency; accept SQLite and one-vendor cohesion | want the full platform (Auth/backups/spend-cap) and the only paid-SLA option; accept logical (RLS) privacy | fixed floor cost, zero vendor dependency, own-infra privacy posture; accept being your own DBA |

## 4. Backup/restore + host-switch escape paths

- **The deepest escape hatch is the product itself:** phone SQLite = system of record, cloud = mirror; re-push from devices after any
  host switch. Transcripts (−14GB/decade @6 users) re-export in minutes; the audio window already lives in R2 (S3-compatible API,
  portable by construction).
- **Neon:** standard Postgres — nightly `pg_dump` to R2's free tier; Neon adds snapshots (1 free) + instant-restore history +
  branches. Escape: dump → VPS/Supabase/any Postgres.
- **Supabase:** `supabase db dump` (CLI-wrapped pg_dump, excludes managed schemas) + restore-to-new-project flows
  [supabase backup/restore docs]; Pro daily backups (7-day retention); PITR is a +$100/mo add-on. Escape: restore into stock
  Postgres; rebuild auth/storage app-level.
- **D1:** Time Travel = free, always-on 30-day PITR with undoable bookmarks [CF D1 docs]; export to R2 via wrangler/Workflows for
  older archives. Escape: one-time SQLite→Postgres schema port (pgloader or by hand — bounded for a ~6-table schema; no vectors to convert).
- **VPS:** `pg_dump` cron + offsite copy (R2 free tier/S3); escape in any direction; zero proprietary surface.

*Bottom line for the decision: all four host FTS5-now / vectors-later identically — the search layer is host-independent by design.
The real axis is where the agent host lives (user machine vs CF edge vs your VPS) and how much physical-vs-logical privacy and
vendor coupling you accept; money is a rounding error on every row.*