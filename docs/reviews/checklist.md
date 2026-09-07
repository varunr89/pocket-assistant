# Code Review Checklist (reviewer's contract)

The code-review subagent uses this checklist for EVERY increment review. The
reviewer is autonomous: findings go back to the coder to fix; loop until satisfied.

## Quality
- [ ] Build is green: `./gradlew :app:testDebugUnitTest :app:lintDebug :app:assembleDebug`
- [ ] Tests added/updated for the change; not just happy-path (edge/error cases)
- [ ] No dead code, no commented-out code, no debug leftovers
- [ ] Naming/signatures match the codebase conventions; no drive-by refactors
- [ ] Complexity is appropriate; no over-engineering (YAGNI)

## Contracts & correctness
- [ ] Implements the spec / accepted increments exactly; no scope creep
- [ ] Data model / API surface matches the locked contracts (docs/specs, architecture)
- [ ] Concurrency/lifecycle discipline preserved (native handles, engine gates)
- [ ] Error paths are handled (no swallowed exceptions; typed failures)
- [ ] No silent failures — aligns with no-silent-failure SLO

## Security & privacy
- [ ] No secrets/keys/tokens in code, logs, or tests
- [ ] Data handling matches privacy contract (local-first; no accidental exfiltration)
- [ ] Permissions (Android) are appropriate and least-privilege

## Regressions
- [ ] No regression to prior increments/features; check git log + docs/qa results for related areas

## Output format (report back to coder)
1. **Blocking issues** (must fix before this passes review) — with file:line + why
2. **Non-blocking suggestions** — quality/cleanup, optional
3. **Verified-good** — what the reviewer checked and confirmed

Reviewer verdict: APPROVE / CHANGES REQUESTED. Loop until APPROVE.