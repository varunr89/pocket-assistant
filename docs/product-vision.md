# Product Vision — Pocket Assistant

## One-line north star
Ambient, reliable memory for the conversations that otherwise vanish — bringing
the unrecorded parts of your day within your AI agents' reach.

## Who it's for
Varun, and professionals like him: people who live in real conversations —
hallway chats, corridor decisions, meetings that never make it onto Zoom —
where the important things are said but never recorded.

## Core experience we want
Capture is zero-thought. The phone quietly listens in the background, turns
in-person conversation into clean, reliable transcripts, and keeps a stable,
searchable record: raw audio for a rolling window, transcripts forever. The
consumers are AI agents, not app screens — when another agent asks "what's the
status on X?", it gets an answer grounded in verbatim quotes, exact times, and
the real conversation they happened in, not a paraphrase. You stop holding
every detail in your head; you show up to the meeting, and the memory problem
is handled.

## What it is NOT (explicitly out of scope)
- A notes app with an in-app search UI — the primary interface is agent-facing.
- A Zoom/Glean competitor — those cover recorded meetings; this covers the
  conversations they never see.
- A general voice assistant or to-do app.
- A hoarder of raw audio — retention is a rolling window; transcripts carry
  the long-term memory.

## Success signals
- Capture is complete and zero-thought: conversations land without friction.
- Transcription is trustworthy: failures are rare, visible, diagnosable.
- Agent queries return cited, verbatim answers with exact time/context
  (the "what's the status on X?" flow works end-to-end).
- The user's reports and work are enriched from real-life context they no
  longer have to remember.

## Non-negotiables (quality bar)
- Transcription reliability over speed; failures are diagnosed, never hidden.
- Transcripts are clean, chronologically sound, and stable as long-term memory.
- Raw audio retained a rolling window for verification; transcripts continue.
- Architecture (cloud vs local ASR, storage) is a means, not an identity —
  chosen for performance, reliability, and cost as the product evolves.
- Green, version-controlled builds only; recoverable at all times.