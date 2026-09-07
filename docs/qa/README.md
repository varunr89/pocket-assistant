# QA (pocket-assistant)

The QA profile (hermes -p qa) owns this directory.

- scenarios/  — living E2E suite derived from vision/roadmap/architecture/specs
- results/    — dated run history (the regression ledger)

Process: QA proposes a test plan -> Varun approves -> QA executes on-device (Pixel via adb/CUA) -> report with evidence -> Varun adjudicates (coder fix vs. deeper design decision).
