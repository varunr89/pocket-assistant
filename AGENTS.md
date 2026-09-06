# AGENTS.md — pocket-assistant

Personal always-on capture Android app (Kotlin + Compose + Room, NDK). Repo owned by Varun.

## Product development workflow (NON-NEGOTIABLE)
This repo follows the **product-development-workflow** skill:
idea → product vision → feature spec → engineering architecture → iterative
engineering, with a human gate after each phase.

- **Read `docs/product-vision.md` BEFORE any feature discussion.** The vision
  is reconsidered at the start of every feature.
- **Never implement a feature from a one-liner.** Treat a feature idea as the
  start of a brainstorm, then spec → architecture → user approval → small
  increments. No single-shot builds.
- **Trivial fixes** (bug with clear repro, typo, no-behavior refactor) skip the
  funnel.
- **Only green-build, pushed commits reach the device.**

## Roles
- **Default profile** = orchestrator + product thinking. Writes specs, delegates.
- **Coder profile** = implements ONLY from approved engineering designs, one
  increment at a time (`coder chat -q "..."`).
- PM / architect = subagents (delegate_task), not profiles.

## Version control
Everything version-controlled; GitHub (varunr89/pocket-assistant) always current;
commit + push before deploy / end of session. Device builds = clean-tree commits.