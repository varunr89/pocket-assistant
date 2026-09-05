# Recovered phone source (reference)

Decompiled (jadx) source recovered from the installed on-device APK
(`com.varun.pocketassistant`) as of 2026-09-05. This is NOT the original
Kotlin source — it is decompiled Java used as an authoritative *reference*
for porting the phone build's advanced features back into the buildable
Kotlin tree in `app/src/main/java`.

Kept because the GitHub repo (`main`) was stale relative to the on-device
build (~46 classes ahead): speaker diarization (`speech/DiarizationLabels`,
`SpeakerCandidate`), `meeting/MeetingBoundaryRefiner`,
`pipeline/BenchEventListener` + `benchChat`, and Room DB migrations
`5->6, 6->7, 7->8`. These are recoverable reference artifacts — see
`DEVELOPMENT.md` for the version-control constitution this project follows.

Do not edit these files as if they were source of truth; port the logic
into Kotlin and commit that.