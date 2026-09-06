# Consent, Privacy & Legal Landscape — Pocket Assistant (always-on ambient audio)

*Research scan for product design — not legal advice. Bellingham, WA first ship: 3–6 users, private instances, raw audio rolling window, transcripts retained forever, sharing only via explicit export.*

## 1. Consent law: WA + US

- **WA is an all-party ("two-party") consent state.** RCW 9.73.030 makes it illegal to record a *private communication* — including in-person conversations where parties have a reasonable expectation of privacy — without the consent of **every** participant. Violations are crimes, carry civil liability (RCW 9.73.060), and recordings become inadmissible in court (RCW 9.73.050). (apps.leg.wa.gov/RCW/default.aspx?cite=9.73.030; washingtonlawhelp.org/en/recording-someone)
- **Consent ≠ explicit sign-off in WA.** The WA Supreme Court held a party "is deemed to have consented if aware the recording is taking place" (*State v. Townsend*, 2002), and a "reasonably effective" announcement that recording is occurring satisfies notice — i.e., **an audible, visible recording announcement is the legally load-bearing gesture**. (rcfp.org/reporters-recording-guide/washington)
- **Consent is revocable**: if anyone says stop, recording must stop (and ideally the segment be deleted). Exceptions: criminal acts/threats of bodily harm; conversations in settings where no reasonable expectation of privacy exists (public, noisy venues). Recording *part* of a conversation still needs all-party consent.
- **US-wide**: ~38 states are one-party (federal wiretap law is one-party); ~11–12 are all-party (CA, CT, FL, IL, MD, MA, MI, MT, NV, NH, PA, WA, incl. DC). The stricter state law governs wherever the recording happens — **WA users are the strictest case**, so designing for all-party consent covers the worst case nationally.
- **"Private communication" is situational**: a phone in the pocket at a restaurant or coffee shop may capture conversations the user isn't party to, with parties who never consented. This is the core legal risk of ambient capture; product design must assume it happens and make purge easy.

## 2. How incumbents handle always-on consent

- **Limitless (ex-Rewind)**: hardware white LED, non-dimmable, must face outward; ToS *requires* users to give verbal notice and obtain consent, providing exact scripts ("Is it ok if I use this pendant to capture the conversation? I'm happy to share the transcript & summary with you"); turn off if anyone declines; delete audio captured without consent. "Consent Mode" concept: device keeps capture but only *saves* if it hears consent, deleting everything else. (help.limitless.ai/en/articles/10540861)
- **Rewind (pre-Limitless)**: local-only, "privacy-first on-device" pitch; placed the consent burden entirely on the user with documentation, no in-product enforcement. Lesson: *local-only storage is a marketing/trust asset, not a consent mechanism.*
- **Otter**: visible OtterPilot bot + join notification = notice; continued participation = implied consent. This model is now **under active litigation** (*In re Otter.AI Privacy Litigation*) — participants claimed no practical opt-out, and speech trained the model by default. Lesson: implied-consent-by-notice is weaker than it looks; explicit opt-in is the defensible posture. (getvoibe.com/resources/is-otter-safe)
- **Fireflies**: consent varies per platform (bot-join opt-in or opt-out), always notifies, supports pause/remove via chat commands, and offers transcript-only or summary-only "compliance" modes that never store audio. (guide.fireflies.ai/articles/7003995379)

## 3. Trust UX patterns for an always-on mic

- **Never rely on the OS discreetly**: Android 12+ shows a green mic privacy indicator whenever the mic is in use (can't be suppressed), and an FGS notification is mandatory — treat those as the *baseline*, then add an in-product "Recording" chip/banner so it's visible on the phone's own screen too. (developer.android.com/privacy-and-security/privacy-indicators)
- **Visible indicator the *other party* can see**: Limitless LED facing out; the phone equivalent is showing the recording screen/banner to the person you're talking to and an explicit spoken announcement. In WA, visible awareness ≈ consent (Townsend).
- **Opt-in onboarding, not opt-in-out**: capture on only after the user affirms a screen that states *what* is recorded, *how long* raw audio is kept, *who* is recorded, and that they're legally responsible for notifying others. One-tap **pause** (mic off) and a prominent **delete** (this segment / raw audio now) must be reachable from the lock screen and the persistent notification.
- **Trusted-circle defaults**: invite-only; no shared data; export-with-a-purpose as the only sharing path; treat "I'm happy to share the transcript or delete it" as the social script (per Limitless) — sharing the transcript with the recorded person is the strongest consent-repair gesture.
- **Small-circle specifics that build trust**: participant-facing info page ("what happens when you're recorded"), per-conversation "others were present" acknowledgment, auto-purge of segments with no speech or no identified speaker (data minimization), and sensitive-location heuristics (doctor's office, bathroom, bank — don't hoard).

## 4. Platform constraints: Google Play + Android

- **RECORD_AUDIO** is a runtime (dangerous) permission; Android 12+ privacy indicator is mandatory and un-suppressible; the Data Safety form must declare mic/audio use with a privacy policy.
- **Always-on capture requires a foreground service of type `microphone`**; since Android 11 you can't *start* a mic FGS from the background (capture begins while app is foreground), and since Android 14 every FGS type must be declared in Play Console (Policy > App content). A mandatory persistent "Pocket Assistant is recording" notification is enforced by the OS — it's a feature: it *is* the disclosure. (developer.android.com/about/versions/14/changes/fgs-types-required)
- **Call recording is effectively banned**: Play prohibits using the Accessibility API for call audio recording (policy, May 2022); accessibility services must serve disability purposes. Ambient in-person capture via the mic is *not* call recording and remains allowed — but any "record my phone calls" feature would violate Play policy. (cnet.com/tech/services-and-software/google-policy-effectively-bans-outside-call-recording-apps)
- **Play's deceptive-behavior policy** prohibits apps that record without user awareness/control; a UI that hides recording would be a violation. Small closed circles can distribute via the **internal/closed testing track** (100/1000 testers, no public store listing) — policy still applies, but exposure and review friction are minimal for the first ship.

## 5. Export-vs-sync privacy implications

- **Export is point-in-time, user-vetted disclosure**: one transcript, explicitly chosen, visible before it leaves the device, with no standing pipeline. **Sync is structural disclosure**: ambient audio/transcripts flowing to a server continuously creates a record of everyone the mic ever heard, at every moment, persistable/compromisable systemically. For an all-party-consent state, sync widens the audience beyond the conversation participants on an ongoing basis; export keeps disclosure bounded to what the user consciously shares.
- **Consent to record ≠ consent to disclose**: WA law governs interception; sharing recordings with non-participants is a separate scope question. Exports *to the recorded parties themselves* are low-risk; exports to third parties are where consent scope gets murky — default to participant-only sharing and require deliberate action beyond that.
- **Data minimization**: rolling-window raw audio + local-only storage until export shrinks the surface (no cloud breach can leak conversations that never left the device) and makes the "private by architecture" claim truthful — which is also the strongest trust story for a 3–6 person circle.
- **Legal risk posture** (scan-level, not advice): an auditable trail of consent announcements (recorded notice at conversation start per Townsend), instant-delete of non-consenting captures, revocable consent honored by purge = the practical compliance minimum; all-party-consent design in WA satisfies even California/Illinois-grade strictness.

## 6. Implications for product design

- Always-on capture is *legally viable in WA* if notice is audible/visible at start, consent is revocable (pause + delete), and non-party captures are purged promptly.
- Build the consent surface into the product (onboarding affirmation, recording banner, share-transcript-with-participant, one-tap mute/delete) rather than leaving it to docs — Otter shows the cost of implied consent, Limitless shows notice-by-design.
- Keep the architecture private-by-default: local-first, export-only sharing; raw audio rolling window; transcripts deletable. These are product *features* that double as the legal defense.
- Play distribution: internal/closed testing track for the launch circle; declare `microphone` FGS + RECORD_AUDIO honestly; never touch Accessibility API for audio.