# Feature Spec: Meeting Transcription UX (foreground live transcription + prioritizable queue)

## Problem / why now (from brainstorm)
The MS Kit GenAI ASR pivot made transcription foreground-gated and ≈1×-paced: a capture backlog drains only while the app is open. Today there is no surface for that wait-and-prioritize experience, no unit the user actually cares about (segments are noise; the *meeting* is the thing), and no way to say "this meeting first." Users need to know what's captured, what's transcribing, and what's safe — without becoming queue managers.

## Goal
Done = on open, the user sees today's meetings as a reorderable list with per-meeting transcription state, a live 'Transcribing now' meeting at top, and a capture window that quietly obeys their schedule. They can skip ahead (drag), cancel the current meeting without losing progress, and never lose data: any interruption resumes from the exact point left off.

## UX shape
Two surfaces: **Today screen** ("Transcribing now", option B) and **Settings → Capture schedule**. Today screen = timeline list of today's meetings, oldest-first by default, the transcribing meeting pinned at top with live status ("transcribing 37%" = audio-seconds drained / total). Each row: start time, status chip (waiting / transcribing / done), short descriptor — displayed as a time-range title "Meet 09:30–10:30" (10 min granularity) as the default label; enrichment keywords replace the time-range label once a meeting has text. No per-word caption strip (option A rejected). Drag to reorder — allowed on any row including the transcribing one, but reordering never interrupts the in-flight meeting and only affects *next* selection; the user may Cancel the in-flight meeting instead. Empty state: "Nothing captured yet today" + mic schedule summary. A manual "+" button on the Today screen sets a simple `manual_start_marker` flag on the current segment (defines a meeting boundary; deliberately no richer mechanism).

**Meeting model** (unit of attention; segments are NEVER shown as status units):
- Auto-grouping: segments roll into a meeting using the existing VAD on/off rolloff behavior (a meeting starts when VAD first triggers with no active meeting; a segment begins a new segment on rolloff; a manual `+` marker forces a new meeting boundary). The long-silence boundary threshold is intentionally the existing VAD rolloff — NOT a new 30-minute rule; a tunable silence-gap value is deferred (see out-of-scope: thresholds are a device flag, not a user setting, and 30 min was a PM-invented assumption, not a locked decision). Meetings are Room-persisted (`meetings` table at DB v8; a meeting is a *grouping* over segments via the nullable `segments.meetingId` — NOT the same as a session; sessions are the durable disk/sync unit written through in small chunks via `segments.sessionId`; split/combine re-assigns `segments.meetingId`, never session membership).
- Split/combine in the UI edits that membership; both persist and re-render the queue.
- Queue order: persisted per-meeting `priority` + `started_at`. New meetings insert in chronological (oldest-first) position. Manual drag = explicit priority for existing rows.
- Cancel (the supported interruption): current *segment* finishes to completion, its partial transcript is saved, the meeting is marked `waiting` again and inserted at slot #2 (just below top; if it was the only meeting it simply remains top), the new top meeting starts next. No meeting is ever restarted from zero.
- Statuses: waiting → transcribing → done (+ `blocked/day-quota` recorded in diagnostics, shown as "waiting (paused today)").
- Persistence = no data loss: partial transcript text is written per segment as it finalizes. On app reopen the queue rebuilds from `meetings` + segment state: done segments stay done, the in-flight meeting resumes from the first unfinished segment.

**Capture schedule**: user-editable weekly window (default weekdays 08:00–17:00); outside it the mic is off — nothing captured (battery + privacy). "Mic on outside schedule" manual override toggle (Today screen + Settings) for one-offs; switching it on/off is the only start/stop interaction — the schedule replaces tap-to-start. Capture stack inside the window is unchanged: passive mic → VAD → 16k mono segments → on-device raw audio, 30-day Opus48k rolling window.

**Foreground truth (platform limit, not a bug)**: ML Kit GenAI inference only runs while the app is the top foreground app. When it is, capture feeds the queue live (near-real-time); when it isn't, transcription pauses and capture (scheduled + VAD) keeps running — everything already captured is safe. Error states per `architecture-final.md` §5: `ErrorCode.BUSY` → exponential backoff; `PER_APP_BATTERY_USE_QUOTA_EXCEEDED` → meeting paused for the day, recorded; `BACKGROUND_USE_BLOCKED` → expected not-foreground, recorded in diagnostics per no-silent-failure, never surfaced as a user error.

**Tiers (same UI, different engine)**: free = foreground on-device ML Kit GenAI path above; paid = cloud background transcription (existing cloud path) with the *same* meeting, queue, persistence, and status semantics — the queue/UI layer is tier-agnostic; only the transcribing engine differs. No billing UI in this feature.

## Integration with the whole product
- **Drain (TranscriptionDrainService, m1-increment 2)**: selection now walks the *queue* (priority-sorted meetings → their segments oldest-first) instead of raw PENDING segments; claim/persist/typed-error logic unchanged; adds Cancel-reader (finish current segment, stop claims).
- **Capture (RecordingService/VAD)**: inputs = schedule gate (mic on/off) + boundary marker writes; no stack changes. **Flag:** schedule-gated mic is an amendment to "capture EXACTLY as before" in `architecture-final.md` §1 / m1 plan — docs must be amended in the same increment that lands this spec.
- **Data layer (Room)**: +`meetings` (+ priority, status, manual flag, descriptor cache); mirror into the Neon `sessions` row (no schema change — meeting metadata rides on `sessions`).
- **Settings**: capture-schedule editor (the one net-new settings surface).
- **Diagnostics**: same screen used for BUSY/quota must now show per-meeting blocked states (no new silo).

## Drift check vs product vision
Serves the vision (verbatim, timed, trustworthy transcripts; zero silent failure) but two flags: (1) foreground gating means "zero-thought" capture now has a visible wait-and-prioritize step post-capture — accepted cost of option A, mitigated by oldest-first default and scheduled hours, and the queue guarantees agents still get everything *eventually*; (2) a user-facing Today screen is a modest step toward "app screens" — it is justified as the unavoidable foreground interaction surface and remains deliberately free of search/reading UI (agents remain the readers). Capture becoming schedule-gated narrows "always listening" to scheduled hours; user-chosen, battery/privacy-driven — specified, not relitigated.

## Acceptance criteria (small)
1. With default schedule, zero segments/raw-audio bytes are written outside weekdays 08:00–17:00 unless override is on; toggling override on/off is the only start/stop action.
2. A scripted 3-meeting morning yields exactly 3 meeting rows on the Today screen with start times; a >30-min silence gap splits meetings, and split/combine/`+` edits persist across app restarts.
3. Drag-reordering a waiting meeting above the transcribing one changes *next* selection only; status never regresses below "transcribing 12%" for the in-flight meeting.
4. Cancel: current segment completes, partial text persists, meeting appears at slot #2 marked waiting, top meeting's transcription starts.
5. Kill the app mid-meeting-transcription → reopen → in-flight meeting resumes from the first unfinished segment; completed segments are never re-transcribed (device↔Room reconciliation audit passes).
6. Not-foreground produces zero retries and no user-facing error; diagnostics record each `BACKGROUND_USE_BLOCKED`; day-quota shows "paused today" visibly.

## Explicitly out of scope (this iteration)
- Google Calendar sync for the capture schedule (noted future; schedule is manual-only now).
- Paid-tier billing surface; cloud-background transcription wiring beyond the existing path.
- Live per-word caption strip (option A), meeting titles/notes editing, any in-app search/reading UI.
- Non-default meeting boundary thresholds as a user setting (flag-only tunable).