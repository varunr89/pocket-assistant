# Pocket Assistant — Hosting, Storage & Agent-Access: Options (v1, Sep 2026)

**Status:** Research-and-options pass for conversational review — NOT a verdict. Supersedes the `architecture.md` §(b)–(c) hosting/sync row and §(f) step 2 where it conflicts with the locked decision to use a **managed, queryable data store** rather than Mac-mini-only self-hosting. Basis: `docs/research/tech-landscape.md` + pricing pages fetched Sep-2026 (sources inline). Assumptions: 6 users × ~160 speech-hr/mo each (tech-landscape), utterance-level segments, transcripts-forever, raw audio 30-day rolling window.

## 0. Load math first (drives every table)

| Item | Size | Notes |
|---|---|---|
| Transcripts (text+JSON) | **~20 MB/mo/user** | 150 wpm × ~7 B/word; 6 users ≈ 0.12 GB/mo → **14 GB/decade**. Negligible everywhere. |
| Raw audio, WAV 16 kHz mono | **115 MB/hr → 18 GB/user** steady-state (30-day window) | 108 GB @ 6 users |
| Raw audio, Opus 24 kbps mono | **10.8 MB/hr → 1.7 GB/user** | **10 GB @ 6 users** — under R2's free tier. Codec choice is open decision #3 in `architecture.md` §(g). |
| Rows | ~58K segments/mo/user (≈1.9K/day); agent queries ≈ 100/day/user × ≤5K-row FTS scans | ~11.5K writes/day + ≤3M reads/day @ 6 users |

Storage $/mo for the rolling window @ 6 users: **Opus 24k (10 GB): R2 $0.00** (10 GB free incl.), S3 $0.23, GCS $0.20, Azure Blob $0.18. **WAV (108 GB): R2 $1.47, S3 $2.48, GCS $2.16, Blob $1.94.** Audio is the only real cost driver, and even it is single digits.

## 1. Candidate stacks

### Stack A — "Per-instance free Postgres + free-egress object store": Neon (per-user project) + Cloudflare R2 + MCP on user's machine

- **Shape:** 6 private Neon projects (one per user — physical isolation by construction), audio in R2, MCP server on each user's always-on machine (stdio locally; Streamable HTTP over Tailscale/Cloudflare Tunnel for remote agents).
- **Cost @ 6 users:** Neon Free $0 (100 projects, 100 CU-hrs/mo + 0.5 GB storage *per project*; scale-to-zero after 5 min idle) + R2 $0 (Opus) to $1.47 (WAV) + MCP host $0 (user's machine) → **$0–1.50/mo**. A 0.25-CU endpoint active ~6.6 h/day ≈ 50 CU-hrs/mo vs 100 free. Escalation: Neon Launch, usage-based ≈ **$15/mo/user** typical ($0.106/CU-hr, $0.35/GB-mo, 500 GB egress/proj incl.) — only if a user exceeds free limits ([neon.tech/pricing](https://neon.tech/pricing)).
- **Auth model:** per-user DB password/connection string held only by that user's MCP server — no shared secret, no RLS needed for v1. Neon Authorize (RLS driven by third-party JWTs: Clerk/Auth0/Keycloak/etc.) exists for later B-tier ([neon.tech docs](https://neon.com/docs/guides/auth-clerk); [helpnetsecurity.com](https://www.helpnetsecurity.com/2024/10/30/neon-authorize)).
- **Agent access:** MCP **stdio** (spec: env credentials, no OAuth needed locally) → **Streamable HTTP + bearer token** when accessed remotely ([MCP 2025-06-18 spec](https://modelcontextprotocol.io/specification/2025-06-18/basic/authorization)).
- **Privacy:** strongest — separate project/bucket per user; no cross-user surface exists to misconfigure. Transcripts-only in Postgres; audio in R2 can be per-user bucket or prefix.
- **Scale path:** pgvector built in (Neon is Postgres); per-user projects scale horizontally; Neon Object Storage (beta, 5 GB free / $0.023/GB-mo) could replace R2 later.
- **Risks:** free-tier reliance (no SLA, autosuspend cold-start ~seconds, 0.5 GB/proj ≈ 2 yr of transcripts — needs a plan to graduate to Launch); R2/Neon are two vendors.

### Stack B — "One shared platform, logical isolation": Supabase Pro (RLS per user) + Supabase Storage/R2 + OAuth'd MCP

- **Shape:** ONE Pro project for all 6 users; `user_id`-partitioned rows under RLS; agents authenticate via Supabase Auth (OAuth 2.1) to *our* MCP server; audio in Supabase Storage or R2.
- **Cost @ 6 users:** **$25/mo flat** (Pro: 8 GB DB, 100 GB file storage — covers even the WAV window — 250 GB egress, spend cap ON by default; compute credits cover one Micro instance) ([supabase.com/pricing](https://supabase.com/pricing)). Overage: $0.09/GB egress, $0.0213/GB storage, Edge Functions $2/1M invocations beyond 2M.
- **Why not 6 free Supabase projects:** free tier = **2 active projects max**, 1 GB storage, 500 MB DB, and projects **pause after 1 week of inactivity** — structurally incompatible with 6 always-syncing instances ([supabase.com/pricing](https://supabase.com/pricing)).
- **Auth model:** RLS with per-user JWTs; `service_role` key never leaves the backend; Supabase Auth doubles as the OAuth 2.1 authorization server for custom MCP servers (documented pattern incl. FastMCP) ([supabase.com/docs/guides/auth/oauth-server/mcp-authentication](https://supabase.com/docs/guides/auth/oauth-server/mcp-authentication)); official Supabase remote MCP server shows their OAuth2-for-agents approach ([supabase.com/blog/remote-mcp-server](https://supabase.com/blog/remote-mcp-server)).
- **Agent access:** Streamable HTTP MCP endpoint (Edge Functions, 2M invocs/mo incl.) or user-machine stdio; agents get scoped tokens per user.
- **Privacy:** logical isolation only — one project = one breach surface for all 6 users' transcripts; correct RLS is a standing correctness burden. Conflicts with the "each own private instance" locked decision unless the user accepts "private instance = private rowset."
- **Scale path:** best-in-class built-ins — pgvector, Auth, Edge Functions, spend cap; Team plan ($599/mo) adds SSO/SOC2 when B opens to third parties.

### Stack C — "All-in-one edge": Cloudflare D1 (per-user DB) + R2 + Workers-hosted remote MCP

- **Shape:** per-user D1 database (SQLite — schema parity with the on-device Room/SQLite store), audio in R2, MCP server as a Worker with stateful Durable Objects sessions.
- **Cost @ 6 users:** **Workers Paid $5/mo** (10M req + 30M CPU-ms incl.) + D1 $0 (6 DBs well inside free: 5 GB total, 100K writes/day vs our ~12K, 5M reads/day vs ~3M; paid overage only $0.001/1M reads, $1/1M writes, $0.75/GB-mo) + R2 $0–1.47 → **$5–6.50/mo**. Zero egress charges on D1 and R2 ([developers.cloudflare.com/d1/platform/pricing](https://developers.cloudflare.com/d1/platform/pricing/), [r2/pricing](https://developers.cloudflare.com/r2/pricing/), [workers/platform/pricing](https://developers.cloudflare.com/workers/platform/pricing/)).
- **Auth model:** Cloudflare Access as the OAuth provider for remote MCP (50 users free; IdP = Google/GitHub/email); per-DB D1 API tokens; `McpAgent` in the Agents SDK handles OAuth + DCR end-to-end ([developers.cloudflare.com/agents/model-context-protocol/guides/remote-mcp-server](https://developers.cloudflare.com/agents/model-context-protocol/guides/remote-mcp-server), [blog.cloudflare.com/building-ai-agents-with-mcp-authn-authz-and-durable-objects](https://blog.cloudflare.com/building-ai-agents-with-mcp-authn-authz-and-durable-objects)).
- **Agent access:** **hosted remote MCP** — agents reach memory from anywhere with the internet; user's laptop does NOT need to be on. This is the only stack that fully decouples agents from user hardware.
- **Privacy:** physical per-user DBs; but D1 has no external SQL API — every query transits our Worker (we see the traffic). Audio egress free → cheap agent audio access.
- **Scale path:** D1 FTS5 for verbatim search now; vectors need Cloudflare **Vectorize** (separate paid product) — no sqlite-vec in D1. Write latency/limits are the main constraint; fine at this volume.
- **Risks:** Worker compute is uncapped on the paid plan (guard with rate limits + usage alerts); most vendor-coupled of the four.

### Stack D — "SQLite-native everywhere": Turso per-user DBs + R2 + MCP on user's machine

- **Shape:** per-user Turso database (libSQL) with **embedded replicas** on each user's machine (local sub-ms reads, cloud primary for writes); audio in R2.
- **Cost @ 6 users:** Turso Free $0 (**100 databases**, 5 GB storage total, 500M rows read/mo, 10M written/mo, 3 GB sync/mo) + R2 $0–1.47 → **$0–1.50/mo**. Escalation: Developer $4.99/mo (unlimited DBs, 9 GB + $0.75/GB) ([turso.tech/pricing](https://turso.tech/pricing)).
- **Auth model:** per-DB scoped bearer tokens + IP allowlists; no OIDC built-in (bring your own for B).
- **Agent access:** MCP stdio on user's machine against the embedded replica — microseconds, offline-capable, and reads don't hit metered rows ([turso.tech/local-first](https://turso.tech/local-first)).
- **Privacy:** physical per-user DBs; embedded replica just mirrors what the phone already holds locally.
- **Scale path:** built-in vector search (no extension) for post-MVP embeddings; same SQL dialect as on-device SQLite = smallest schema impedance mismatch.
- **Risks:** sync metering (3 GB/mo free — transcripts fit easily; WAL of 20 MB/mo/user is ~0.12 GB/mo); smallest vendor, fewest guardrails (no spend cap; free tier hard-stops).

## 2. Big-3 cloud benchmark (single shared instance + RLS; serverless minimal)

| Platform | Minimal serverless setup | $/mo @ 6 users (shared, RLS) | Per-user ×6 (physical) |
|---|---|---|---|
| **AWS** | RDS `db.t4g.micro` $0.016/hr = **$11.68** + gp3 20 GB **$1.60** + S3 audio $0.23–2.48 + Lambda/API-GW ≈ $0 (free tier) | **≈ $13.50–15.80** (yr-1 free tier: ~$2.50–3, storage only) | ≈ **$85+** |
| **GCP** | Cloud SQL `db-f1-micro` $0.0105/hr = **$7.67** + SSD 10 GB **$1.70** + GCS $0.20–2.16 + Cloud Run (free tier covers MCP) | **≈ $9.60–11.55** | ≈ **$70+** |
| **Azure** | Postgres Flexible `B1ms` ≈ **$12.50** + Premium SSD 32 GB min **$3.68** + Blob hot $0.18–1.94 | **≈ $16.50–18.10** | ≈ **$110+** |

Sources: [aws.amazon.com/rds/postgresql/pricing](https://aws.amazon.com/rds/postgresql/pricing/) (free tier 750 h + 20 GB), [instances.vantage.sh/aws/rds/db.t4g.micro](https://instances.vantage.sh/aws/rds/db.t4g.micro), [aws.amazon.com/s3/pricing](https://aws.amazon.com/s3/pricing/), [cloud.google.com/sql/pricing](https://cloud.google.com/sql/pricing), [azure.microsoft.com/en-us/pricing/details/postgresql/flexible-server](https://azure.microsoft.com/en-us/pricing/details/postgresql/flexible-server).

**Read:** shared-RLS on the big 3 costs ~$10–18/mo — the cheapest *single-tenant* option — but per-user physical isolation ×6 is $70–110/mo (not "lowest practical"), and auth (RDS IAM + RLS, Cloud IAP, Entra ID) plus MCP hosting is all assembly work. **Trap:** AWS "Aurora Serverless v2" minimum 0.5 ACU ≈ **$43.80/mo** — not actually cheap. PlanetScale: Postgres single-node from **$5/db/mo** (10 GB free) → $30/mo @ 6 users; fine but no free per-user tier ([planetscale.com/pricing](https://planetscale.com/pricing)).

## 3. Auth models for agent access (quick map)

| Model | Where it lives | Agent UX | Privacy notes |
|---|---|---|---|
| **Service-role key** | Supabase `service_role` / platform admin keys | Never give to agents — bypasses RLS. Backend-only. | Full-power secret; leak = everything |
| **RLS + user JWT** | Supabase Auth, Neon Authorize (Clerk/Auth0/Keycloak JWTs) | Agent completes an OAuth 2.1 consent flow (MCP-spec DCR/PKCE) | DB enforces ownership; fine-grained; the B-tier shape |
| **Per-user API keys** | Neon conn strings, Turso bearer, D1 tokens, Supabase scoped keys | Static secret in agent config (stdlib env var) | Simple, revocable; leak = that one tenant |
| **OIDC / OAuth 2.1** | MCP spec 2025-06-18: resource server + RFC 9728/8414/7591 discovery, DCR, PKCE, audience-bound tokens | Standard "Login with…" for remote MCP | Required for public B-tier; optional in A |
| **mTLS** | Cloudflare tunnel / private network | Machine-to-machine only; poor DX for consumer agents | Hardening for B, not a v1 requirement |

Transport: **stdio** (MCP spec: credentials from environment, no OAuth) = local/trusted-host agents; **Streamable HTTP** (bearer tokens or full OAuth) = remote agents. Same server can expose both — build one, not two.

## 4. What drives runaway cost + guardrails

1. **Egress.** Audio downloads by agents (`get_segment`) are the only meaningful traffic. Supabase $0.09/GB beyond 250 GB; Neon $0.10/GB beyond 500 GB; S3/RDS data-out $0.09/GB. **R2 + D1: egress $0.** Guard: Opus storage, agent-side caching, range reads; alert at 80% of included egress.
2. **Compute autoscaling.** Neon CU-hrs ($0.106/CU-hr), Supabase compute add-ons, RDS CPU credits ($0.075/vCPU-hr beyond baseline), Cloud Run/Worker CPU. Guard: max autoscale caps + scale-to-zero (Neon), pgbouncer, min-instances=0 (Cloud Run).
3. **Row-metered DBs.** D1 $1/1M writes & $0.001/1M reads beyond included; Turso rows-read billing. Guard: FTS5 indexes bound scans, per-token rate limits in the MCP layer, query caching, pagination.
4. **Request meters.** Workers $0.30/1M beyond 10M; Lambda $0.20/1M; Supabase Edge Functions $2/1M beyond 2M. Guard: client backoff, per-agent token quotas.
5. **Storage drift.** Rolling-window expiry must actually run. S3/R2 have native lifecycle rules (R2 Infrequent-Access $0.01/GB with 30-day min = exact fit for a 30-day window); Supabase Storage/Neon Object Storage need an app-level expiry job.
6. **Hard limits (the non-negotiables):** Supabase Pro **spend cap (default-on, pauses service at cap)**; Neon per-project autoscale ceiling + spend notifications; AWS Budgets + billing alarms; GCP budget alerts; Azure cost alerts; D1/Turso free tiers **hard-stop at limits — no surprise bills**. Cloudflare has zero egress fees but Worker CPU is uncapped on paid → usage alerts mandatory.
7. **Loop amplification.** Agents polling MCP tools in tight loops. Guard: MCP-layer rate limiting + per-token metering counters (rows read, bytes returned) — these same counters become the B-tier billing meter.
8. ASR stays BYOK on each user's own key — we never hold a central AI-billing surface (locked decision; already the biggest cost-firewall).

## 5. The B-expansion seam (build now, additive later)

- **One MCP server, two transports from day one:** Streamable HTTP + stdio. The 2025-06-18 auth spec (OAuth 2.1, RFC 9728/8414/7591, DCR, PKCE, audience-bound tokens) is implemented once and simply not exercised in A. Do NOT ship stdio-only.
- **Auth abstraction:** `Principal{tenant_id, scopes}` derived from bearer API key (v1) or OIDC token (B). Never hand service-role keys to agents.
- **Isolation model chosen now stays valid later:** physical per-user DB (A/C/D) or strict `tenant_id` RLS (B) — both keep a public surface additive, never a migration.
- **Rate limiting + metering hooks in the MCP layer now:** A's runaway guardrail IS B's billing meter.
- **RLS/tenant-scoping written even in per-user DBs:** defense-in-depth, costs nothing, survives the moment a shared tier appears.
- **Later (B-only):** OIDC authorization server (Supabase Auth / CF Access / Auth0 / Keycloak), consent screen, DCR, per-app token scopes, optional mTLS, public MCP registry listing. None of it touches A.

## 6. Recommended for further exploration — NOT a verdict

**Lead: Stack A** (Neon free per-user + R2 + MCP on user's machine/tunnel) at **$0–1.50/mo** — the only stack that is simultaneously (a) genuinely free-tier-viable at 6 users, (b) physically private per instance per the locked decision, (c) Postgres+pgvector for the M4 team tier, and (d) a clean B seam. **Runner-up: Stack C** (Cloudflare all-in, **$5–6.50/mo**) if "agents reach memory from anywhere, laptop off" is a first-ship requirement — it's the only hosted-remote-MCP option with production-grade OAuth at this price. Stack B ($25/mo) wins on platform completeness (Auth, Edge Functions, spend cap, SOC2 path) but softens per-instance privacy; the big-3 shared-instance setups (~$10–18/mo) beat nothing except vendor preference, and their per-user variants (×6) fail the cost posture.

## 7. Open decisions — need your judgment

1. **Free-tier reliance vs paid baseline.** Stacks A/D ride free tiers (no SLA, hard limits, autosuspend cold-starts); Supabase Pro is the only option with a paid support contract AND a hard spend cap at $25/mo. What's the risk appetite for the circle's memory layer?
2. **Does raw audio leave the device at all?** The locked decision says managed hosting for "the data," and the rolling window makes even WAV cheap ($1.47–2.53/mo). But `consent-legal.md` flags any standing sync pipeline as structural disclosure beyond consent-to-record; audio is the *only* data that isn't transcripts. Opus-vs-WAV + window length is `architecture.md` §(g) open decision #3, now doubled with "sync audio at all."
3. **Agent host: user's machine (A/D, $0, Tailscale) vs hosted remote MCP (C, $5/mo, works anywhere).** This pins M2 scope and the auth story — stdio-only-v1 vs streamable-HTTP-from-day-one.

*Next: review conversationally; on direction, fold the winner into `architecture.md` §(b)/(f) and spec M2 around it.*
