# Market Landscape — Ambient Conversation Capture for Personal AI Memory

*Research date: Sept 2026. Product lens: Pocket Assistant — private, agent-queryable ambient capture of in-person conversation.*

## 1. Landscape overview
- The field splits into four camps: (a) meeting note-takers tied to virtual calls (Otter, Fireflies, Fathom), (b) bot-free desktop/mobile note-takers (Granola, Zoom AI Companion/My Notes), (c) **ambient life-memory** (Limitless — now dead: acquired by Meta Dec 2025, service shut down Dec 19, 2025, pendant discontinued; see rewind.ai/what-happened-to-rewind), and (d) capture infra/API (Recall.ai, MeetingBaas) and hardware wearables (Plaud $159-189, Bee $50, Omi $89, Vibe Dot $199).
- Ambient life-memory is a **proven-but-empty position**: Limitless proved demand ("never forget a thing," 1,200 free min/mo, ~15-20/mo premium) then died — trust in closed ambient capture is at a low ebb and no comparable replacement exists.
- **No incumbent** offers: zero-thought background capture on the phone you already carry + fully private storage + raw timestamp+verbatim access for *your own AI agents*. That combination is unoccupied.

## 2. Competitors (capture / privacy / agent access / pricing)
- **Granola** — bot-free capture on macOS/Windows/iOS (no Android); **deletes audio immediately after transcription** (no transcript-vs-audio verification possible), cloud ASR, SOC 2 Type 2; free=25 notes lifetime, Business $14/user/mo annual, Enterprise $35; searchable "where did we talk about X" chat across meetings (granola.ai/blog/granola-pricing-privacy-tradeoff).
- **Otter.ai** — app records ambient in-person audio too, but **audio uploads to AWS the moment you press record**; AI-training default is opt-in-by-default (email to opt out), no auto-delete on personal plans (speakhapi.com/blog/is-otter-ai-safe-privacy-review); Free 300 min/mo, Pro $8.33/user/mo annual, Business $19.99, Enterprise (otter.ai/pricing); ships an **MCP server** (mcp.otter.ai) giving Claude/ChatGPT search+fetch over transcripts — public API is Enterprise-gated.
- **Fireflies.ai** — visible bot joins calls (the privacy gripe that repeatedly surfaces on Reddit: a 397-upvote r/privacy thread from a candidate who declined interviews rather than be recorded; r/msp calls note-taker bots a HIPAA risk). Free (unlimited transcription, 400 min storage/team, 20 AI credits), Pro $10, Business $19, Enterprise $39 per seat/mo annual; **AI credits = metered AI usage** (fireflies.ai/pricing).
- **Limitless (ex-Rewind)** — the closest precedent to Pocket Assistant's vision: pendant + app ambient capture, "Consent Mode" chime, local processing option; **no developer/agent API**; free 1,200 min/mo; **shut down Dec 19, 2025 after Meta acquisition** — underscore: closed ambient-capture platforms carry existential risk (help.limitless.ai/en/articles/9129649-pricing-plans).
- **Recall.ai** — API/bot infra, not an app: $0.50/recording-hr (prorated to the second) + $0.15/hr transcription, 7 days free storage then $0.05/hr/30d; Desktop Recording SDK captures **without a visible bot** (recall.ai/pricing). Useful cost reference: cloud recording+transcription floor ≈ $0.65/hr.
- **Glean** — enterprise work-AI: meeting notes captured by desktop app become first-class indexed "artifacts" in the Library, queried by Glean agents; usage-based FlexCredits metering; org-wide data fabric — the exact multi-user model Pocket Assistant rejects (docs.glean.com/glean-enterprise-flex-pricing).
- **Zoom AI Companion / My Notes / ZoomMate** — My Notes now captures **in-person conversations via the mobile app**; bundled with Zoom Workplace (Pro ≈$14.16/user/mo); ZoomMate $20/user/mo adds agentic search w/ AI credits; admin-controlled, ecosystem-tied, cloud (zoom.com/en/products/ai-assistant).

## 3. The gap we'd fill — the unrecorded conversation
- Microsoft's 2025 Work Trend Index: **57% of meetings are ad hoc — no calendar invite**, and software note-takers (bots, calendar triggers) never see them (vibe.us/blog/the-meetings-your-ai-note-taker-cant-capture). Hallway/off-calendar conversations carry disproportionate decision weight and are effectively unrecorded everywhere today.
- Hardware wearables (Plaud, Omi, Bee) address it, but require buying+charging a device, are cloud-transcription + app-UI products, and none expose a personal agent API.
- Software in-person capture exists (Otter/Granola mobile) but demands **manual tap-to-record** — visibly awkward: the working norm (LinkedIn: "pull out your phone and record?") is discomfort + disclosure; users explicitly wish for "recordings saved locally and discarded after the conversation was summarized" (linkedin.com/posts/alischwanke_...-7347988498164891648).
- **Pocket Assistant's position: the phone already in your pocket is the device; capture is background and consent-aware; transcripts live for your agents (verbatim + timestamped), raw audio only for a rolling window.** No one else occupies this exact triangle (Android-first + private + agent-native).

## 4. Privacy / consent norms
- US federal baseline is one-party consent; **12 states (CA Penal Code 632, IL, MA, MD…) require all-party consent for in-person and phone conversations**; Illinois treats AI transcription as eavesdropping — Class 4 felony first offense without consent (recordinglaw.com/.../illinois-recording-laws/audio); 2025 suits target AI call analysis without consent.
- Market norms: visible recording indicators, "Consent Mode" chimes (Limitless), disclosure at conversation start, and enterprise upsells for **transcript-only mode, private storage, custom retention** (Fireflies Enterprise). Users punish undisclosed recording (r/privacy threads, candidate refusals) — disclosure/consent features are a license-to-operate, not a checkbox.
- Even in one-party states, *ad-hoc recording of others* is culturally toxic; the defensible pattern is **self-recorded capture with visible/disclosed indicator + local-first processing + no cloud training**. This is Pocket Assistant's built-in advantage over cloud note-takers.

## 5. Pricing / billing patterns
- **Prosumer software**: free tiers with hard caps (minutes/mo 300-1,200; storage caps; AI credits), paid per-seat $8-20/user/mo annual; unlimited storage used as a Pro+ upsell (Otter).
- **Metered-AI creep**: Fireflies AI credits, Glean FlexCredits, Zoom AI credits — advanced AI is billed above the seat; per-use metering is now expected.
- **API/infra**: per-minute metering (Recall.ai ≈$0.65/hr recording+transcription) is the unit-economics floor for cloud capture.
- **Hardware wearables**: $49-199 device + $8-30/mo subscription, free tiers ≈300 min/mo (vibe) to 1,200 (Plaud/Limitless) — bundling device+subscription is the norm.
- **Enterprise**: SOC 2, HIPAA add-ons (Otter/Fireflies), SSO/SCIM, retention controls. Nobody charges for transcript-forever retention per se — storage is absorbed into seats, a fact that supports cheap "transcripts forever."
- Implication: a phone-native app has **zero hardware cost** vs $49-199 wearables, and local ASR eliminates the ~$0.65/hr cloud floor.

## 6. Key takeaways for our roadmap (research-level observations)
1. **Positioning is wide open in the exact spot we chose**: ambient life-memory's flagship died (Limitless); incumbents are meeting-tool- or enterprise-fabric-bound; the "private + Android + agent-native ambient memory" cell is empty.
2. **Consent & disclosure are the gate**: build a visible recording indicator + optional disclosure/consent note into the flow; it's both a legal requirement in 12 states and the social norm Reddit polices hardest.
3. **Local-first is a differentiator, not a constraint**: rivals market cloud round-trips; on-device/private processing + no-training is the answer to the strongest recurring user complaint (permission footprint) and to acquisition risk (Limitless).
4. **Agent access is the emerging standard, not a nicety**: the field is converging on MCP/API retrieval (Otter MCP server, Glean agents, ZoomMate); exposing a local API/MCP surface for one's own agents is table stakes for the agent-consumer thesis — and currently missing from every ambient/phone product.
5. **Pricing headroom**: consumers already pay $8-20/mo seats + $8-30/mo wearable subscriptions for worse privacy and no agent access; hardware-free private capture validates a comparable or lower subscription, and free-tier caps (minutes/mo) are the established pattern to benchmark against.
6. **Verbatim+audio verification is a real differentiator**: Granola explicitly deletes audio and reviewers flag the inability to verify transcripts as a workflow gap — our rolling-audio-window design answers that.

*Sources: vendor pricing pages (granola.ai, otter.ai, fireflies.ai, recall.ai, zoom.com, glean.com, limitless.ai, plaud.ai), publish-date 2025-26 reviews (itsconvo.com, meetjamie.ai, hirekai.ai, vibe.us, revenue.io), Reddit (r/privacy, r/msp, r/sysadmin), NCSL/recording-law references.*