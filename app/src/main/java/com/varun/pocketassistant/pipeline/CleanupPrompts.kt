package com.varun.pocketassistant.pipeline

/**
 * Configurable cleanup + meeting summarization prompts.
 *
 * Meeting processing is two-pass:
 * 1) Transcript cleanup → readable conversation text
 * 2) Meeting summarization → Title + Overview markdown
 * Then both are assembled into the Markdown parsed by [MeetingCleanupParser].
 *
 * Templates may use:
 * - {{transcript}} — raw or cleaned transcript body
 * - {{glossary}} — domain glossary
 * - {{speaker_note}} — diarization guidance (cleanup prompt only)
 */
object CleanupPrompts {
    const val TRANSCRIPT = "{{transcript}}"
    const val GLOSSARY = "{{glossary}}"
    const val SPEAKER_NOTE = "{{speaker_note}}"

    val DEFAULT_GLOSSARY = """
Domain glossary — these are frequently mis-transcribed; correct them from context:
- "Kate" / "Cates" / "Kubernetes" spoken casually → "K8s" when it refers to the container platform
- "cogs" / "cost of goods" → "COGS"
- "S3" / "has three" → "S3" when about storage
- "XP" → "XP" (internal platform name)
- "ICS" → "ICS"
- "Snowflake", "Snowhouse", "Cortex" → keep exact capitalization
- company/person names: pick ONE consistent spelling and use it throughout
- expand obvious acronyms on first use only if unambiguous from context
""".trimIndent()

    val DEFAULT_CLEANUP_PROMPT = """
Below is a raw speech-to-text transcript of a work meeting / conversation. It has
transcription artifacts: the SAME passages are often duplicated 2-3 times (once garbled, once
cleaner), no punctuation, filler words, and ASR errors (inconsistent name spellings).

$GLOSSARY

$SPEAKER_NOTE

Return ONLY the cleaned transcript text (no Title/Overview headings):
- Remove ALL duplicated passages (keep the clearest version of each)
- Remove filler and fix punctuation, capitalization, grammar, and ASR spelling errors
  (pick one consistent spelling per name; apply the glossary above)
- Preserve EVERY work-relevant point, detail, name, number, and decision — do not summarize away substance
- Strip content not relevant to work (greetings, "running late", personal/food/coffee chit-chat);
  where you drop a non-work stretch, leave a short bracketed marker like [personal aside]

Transcript:
\"\"\"
$TRANSCRIPT
\"\"\"
""".trimIndent()

    val DEFAULT_SUMMARY_PROMPT = """
Below is a cleaned transcript of a work meeting / conversation.

$GLOSSARY

Return Markdown with TWO sections and nothing else:

## Title
A short, specific meeting title (one line, no quotes). Prefer topic + context over generic names.

## Overview
- **Topics:** bullet list of topics discussed
- **Decisions:** any decisions made (omit the bullet if none)
- **Open questions:** unresolved items (omit the bullet if none)
- **People:** names of people mentioned or speaking (omit if none / unknown)

Transcript:
\"\"\"
$TRANSCRIPT
\"\"\"

Return only the Markdown (Title + Overview).
""".trimIndent()

    /** Legacy single-pass meeting prompt (kept for reference / reset). */
    val DEFAULT_MEETING_COMBINED_PROMPT = """
Below is a raw speech-to-text transcript of a work meeting / conversation. It has
transcription artifacts: the SAME passages are often duplicated 2-3 times (once garbled, once
cleaner), no punctuation, filler words, and ASR errors (inconsistent name spellings).

$GLOSSARY

$SPEAKER_NOTE

Return Markdown with THREE sections and nothing else:

## Title
A short, specific meeting title (one line, no quotes). Prefer topic + context over generic names.

## Overview
- **Topics:** bullet list of topics discussed
- **Decisions:** any decisions made (omit the bullet if none)
- **Open questions:** unresolved items (omit the bullet if none)
- **People:** names of people mentioned or speaking (omit if none / unknown)

## Transcript
A clean, readable version of the full conversation:
- Remove ALL duplicated passages (keep the clearest version of each)
- Remove filler and fix punctuation, capitalization, grammar, and ASR spelling errors
  (pick one consistent spelling per name; apply the glossary above)
- Preserve EVERY work-relevant point, detail, name, number, and decision — do not summarize away substance
- Strip content not relevant to work (greetings, "running late", personal/food/coffee chit-chat);
  where you drop a non-work stretch, leave a short bracketed marker like [personal aside]

Transcript:
\"\"\"
$TRANSCRIPT
\"\"\"

Return only the Markdown.
""".trimIndent()

    fun glossary(settings: PipelineSettings): String =
        settings.glossary.trim().ifBlank { DEFAULT_GLOSSARY }

    fun speakerNote(diarized: Boolean): String =
        if (diarized) {
            """
Speaker labels ("Speaker 1:", "You:", "Other:", …) may be present — KEEP them
(bold labels, one turn per speaker change). Merge consecutive turns from the same speaker.
Speaker labels may occasionally be misattributed.
""".trimIndent()
        } else {
            "Reflow into logical paragraphs grouped by topic."
        }

    fun render(
        template: String,
        transcript: String,
        glossary: String,
        speakerNote: String = "",
    ): String =
        template
            .replace(GLOSSARY, glossary)
            .replace(SPEAKER_NOTE, speakerNote)
            .replace(TRANSCRIPT, transcript)

    fun buildCleanup(settings: PipelineSettings, rawTranscript: String, diarized: Boolean): String {
        val template = settings.cleanupPrompt.trim().ifBlank { DEFAULT_CLEANUP_PROMPT }
        return render(
            template = template,
            transcript = rawTranscript,
            glossary = glossary(settings),
            speakerNote = speakerNote(diarized),
        )
    }

    fun buildSummary(settings: PipelineSettings, cleanedTranscript: String): String {
        val template = settings.summaryPrompt.trim().ifBlank { DEFAULT_SUMMARY_PROMPT }
        return render(
            template = template,
            transcript = cleanedTranscript,
            glossary = glossary(settings),
            speakerNote = "",
        )
    }

    /** Legacy single-prompt builder used by older call sites. */
    fun build(rawTranscript: String, diarized: Boolean): String =
        render(
            template = DEFAULT_MEETING_COMBINED_PROMPT,
            transcript = rawTranscript,
            glossary = DEFAULT_GLOSSARY,
            speakerNote = speakerNote(diarized),
        )

    fun assembleMeetingMarkdown(summaryMarkdown: String, cleanedTranscript: String): String {
        val summary = summaryMarkdown.trim()
        val cleaned = cleanedTranscript.trim()
        if (summary.contains(Regex("""(?im)^##\s+Transcript\b"""))) {
            return summary
        }
        return buildString {
            append(summary.trimEnd())
            append("\n\n## Transcript\n")
            append(cleaned)
            append('\n')
        }
    }
}

data class MeetingCleanupParsed(
    val title: String?,
    val cleanedMarkdown: String,
    val topics: List<String>,
    val people: List<String>,
    val decisions: List<String>,
    val openQuestions: List<String>,
) {
    fun toMetadataJson(): String {
        fun esc(s: String) = s.replace("\\", "\\\\").replace("\"", "\\\"")
        fun arr(items: List<String>) =
            items.joinToString(prefix = "[", postfix = "]") { "\"${esc(it)}\"" }
        return """{"topics":${arr(topics)},"people":${arr(people)},"decisions":${arr(decisions)},"openQuestions":${arr(openQuestions)}}"""
    }
}

object MeetingCleanupParser {
    fun parse(markdown: String): MeetingCleanupParsed {
        val title = sectionBody(markdown, "Title")
            ?.lineSequence()
            ?.map { it.trim().removePrefix("#").trim().trim('"', '\'') }
            ?.firstOrNull { it.isNotBlank() }

        val overview = sectionBody(markdown, "Overview").orEmpty()
        val topics = bulletsUnder(overview, "Topics")
        val decisions = bulletsUnder(overview, "Decisions")
        val openQuestions = bulletsUnder(overview, "Open questions")
        val people = bulletsUnder(overview, "People")

        return MeetingCleanupParsed(
            title = title?.takeIf { it.isNotBlank() },
            cleanedMarkdown = markdown.trim(),
            topics = topics,
            people = people,
            decisions = decisions,
            openQuestions = openQuestions,
        )
    }

    private fun sectionBody(markdown: String, heading: String): String? {
        val pattern = Regex(
            """(?im)^##\s+${Regex.escape(heading)}\s*\n(.*?)(?=^##\s+|\z)""",
            RegexOption.DOT_MATCHES_ALL,
        )
        return pattern.find(markdown)?.groupValues?.getOrNull(1)?.trim()
    }

    private fun bulletsUnder(overview: String, label: String): List<String> {
        val lines = overview.lines()
        val start = lines.indexOfFirst {
            it.trim().matches(Regex("""(?i)^[-*]\s*\*\*$label:\*\*.*""")) ||
                it.trim().matches(Regex("""(?i)^\*\*$label:\*\*.*""")) ||
                it.trim().matches(Regex("""(?i)^$label:\s*.*"""))
        }
        if (start < 0) return emptyList()

        val first = lines[start].trim()
        val inline = first
            .replace(Regex("""(?i)^[-*]\s*\*\*$label:\*\*\s*"""), "")
            .replace(Regex("""(?i)^\*\*$label:\*\*\s*"""), "")
            .replace(Regex("""(?i)^$label:\s*"""), "")
            .trim()

        val items = mutableListOf<String>()
        if (inline.isNotBlank() && !inline.equals("none", ignoreCase = true)) {
            items += inline.split(Regex("""\s*[;|]\s*|\s*,\s+(?=[A-Z])"""))
                .map { it.trim().trimStart('-', '*', ' ') }
                .filter { it.isNotBlank() }
        }

        var i = start + 1
        while (i < lines.size) {
            val line = lines[i].trim()
            if (line.matches(Regex("""(?i)^[-*]\s*\*\*.+:\*\*.*""")) ||
                line.matches(Regex("""(?i)^\*\*.+:\*\*.*"""))
            ) {
                break
            }
            if (line.startsWith("-") || line.startsWith("*")) {
                val item = line.trimStart('-', '*', ' ').trim()
                if (item.isNotBlank()) items += item
            } else if (line.isBlank()) {
                // continue
            } else if (items.isNotEmpty()) {
                break
            }
            i++
        }
        return items.distinct()
    }
}
