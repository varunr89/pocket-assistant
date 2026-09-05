package com.varun.pocketassistant.pipeline;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.Regex;
import kotlin.text.StringsKt;

/* JADX INFO: compiled from: CleanupPrompts.kt */
/* JADX INFO: loaded from: classes8.dex */
@Metadata(d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\r\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u000b\bÇ\u0002\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003J\u000e\u0010\u0011\u001a\u00020\u00052\u0006\u0010\u0012\u001a\u00020\u0013J\u000e\u0010\u0014\u001a\u00020\u00052\u0006\u0010\u0015\u001a\u00020\u0016J(\u0010\u0017\u001a\u00020\u00052\u0006\u0010\u0018\u001a\u00020\u00052\u0006\u0010\u0019\u001a\u00020\u00052\u0006\u0010\u0011\u001a\u00020\u00052\b\b\u0002\u0010\u0014\u001a\u00020\u0005J\u001e\u0010\u001a\u001a\u00020\u00052\u0006\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u001b\u001a\u00020\u00052\u0006\u0010\u0015\u001a\u00020\u0016J\u0016\u0010\u001c\u001a\u00020\u00052\u0006\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u001d\u001a\u00020\u0005J\u0016\u0010\u001e\u001a\u00020\u00052\u0006\u0010\u001b\u001a\u00020\u00052\u0006\u0010\u0015\u001a\u00020\u0016J\u0016\u0010\u001f\u001a\u00020\u00052\u0006\u0010 \u001a\u00020\u00052\u0006\u0010\u001d\u001a\u00020\u0005R\u000e\u0010\u0004\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u0011\u0010\b\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u0011\u0010\u000b\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\f\u0010\nR\u0011\u0010\r\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\nR\u0011\u0010\u000f\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\n¨\u0006!"}, d2 = {"Lcom/varun/pocketassistant/pipeline/CleanupPrompts;", "", "<init>", "()V", "TRANSCRIPT", "", "GLOSSARY", "SPEAKER_NOTE", "DEFAULT_GLOSSARY", "getDEFAULT_GLOSSARY", "()Ljava/lang/String;", "DEFAULT_CLEANUP_PROMPT", "getDEFAULT_CLEANUP_PROMPT", "DEFAULT_SUMMARY_PROMPT", "getDEFAULT_SUMMARY_PROMPT", "DEFAULT_MEETING_COMBINED_PROMPT", "getDEFAULT_MEETING_COMBINED_PROMPT", "glossary", "settings", "Lcom/varun/pocketassistant/pipeline/PipelineSettings;", "speakerNote", "diarized", "", "render", "template", "transcript", "buildCleanup", "rawTranscript", "buildSummary", "cleanedTranscript", "build", "assembleMeetingMarkdown", "summaryMarkdown", "app_debug"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class CleanupPrompts {
    public static final int $stable = 0;
    public static final String GLOSSARY = "{{glossary}}";
    public static final String SPEAKER_NOTE = "{{speaker_note}}";
    public static final String TRANSCRIPT = "{{transcript}}";
    public static final CleanupPrompts INSTANCE = new CleanupPrompts();
    private static final String DEFAULT_GLOSSARY = "Domain glossary — these are frequently mis-transcribed; correct them from context:\n- \"Kate\" / \"Cates\" / \"Kubernetes\" spoken casually → \"K8s\" when it refers to the container platform\n- \"cogs\" / \"cost of goods\" → \"COGS\"\n- \"S3\" / \"has three\" → \"S3\" when about storage\n- \"XP\" → \"XP\" (internal platform name)\n- \"ICS\" → \"ICS\"\n- \"Snowflake\", \"Snowhouse\", \"Cortex\" → keep exact capitalization\n- company/person names: pick ONE consistent spelling and use it throughout\n- expand obvious acronyms on first use only if unambiguous from context";
    private static final String DEFAULT_CLEANUP_PROMPT = "Below is a raw speech-to-text transcript of a work meeting / conversation. It has\ntranscription artifacts: the SAME passages are often duplicated 2-3 times (once garbled, once\ncleaner), no punctuation, filler words, and ASR errors (inconsistent name spellings).\n\n{{glossary}}\n\n{{speaker_note}}\n\nReturn ONLY the cleaned transcript text (no Title/Overview headings):\n- Remove ALL duplicated passages (keep the clearest version of each)\n- Remove filler and fix punctuation, capitalization, grammar, and ASR spelling errors\n  (pick one consistent spelling per name; apply the glossary above)\n- Preserve EVERY work-relevant point, detail, name, number, and decision — do not summarize away substance\n- Strip content not relevant to work (greetings, \"running late\", personal/food/coffee chit-chat);\n  where you drop a non-work stretch, leave a short bracketed marker like [personal aside]\n\nTranscript:\n\\\"\\\"\\\"\n{{transcript}}\n\\\"\\\"\\\"";
    private static final String DEFAULT_SUMMARY_PROMPT = "Below is a cleaned transcript of a work meeting / conversation.\n\n{{glossary}}\n\nReturn Markdown with TWO sections and nothing else:\n\n## Title\nA short, specific meeting title (one line, no quotes). Prefer topic + context over generic names.\n\n## Overview\n- **Topics:** bullet list of topics discussed\n- **Decisions:** any decisions made (omit the bullet if none)\n- **Open questions:** unresolved items (omit the bullet if none)\n- **People:** names of people mentioned or speaking (omit if none / unknown)\n\nTranscript:\n\\\"\\\"\\\"\n{{transcript}}\n\\\"\\\"\\\"\n\nReturn only the Markdown (Title + Overview).";
    private static final String DEFAULT_MEETING_COMBINED_PROMPT = "Below is a raw speech-to-text transcript of a work meeting / conversation. It has\ntranscription artifacts: the SAME passages are often duplicated 2-3 times (once garbled, once\ncleaner), no punctuation, filler words, and ASR errors (inconsistent name spellings).\n\n{{glossary}}\n\n{{speaker_note}}\n\nReturn Markdown with THREE sections and nothing else:\n\n## Title\nA short, specific meeting title (one line, no quotes). Prefer topic + context over generic names.\n\n## Overview\n- **Topics:** bullet list of topics discussed\n- **Decisions:** any decisions made (omit the bullet if none)\n- **Open questions:** unresolved items (omit the bullet if none)\n- **People:** names of people mentioned or speaking (omit if none / unknown)\n\n## Transcript\nA clean, readable version of the full conversation:\n- Remove ALL duplicated passages (keep the clearest version of each)\n- Remove filler and fix punctuation, capitalization, grammar, and ASR spelling errors\n  (pick one consistent spelling per name; apply the glossary above)\n- Preserve EVERY work-relevant point, detail, name, number, and decision — do not summarize away substance\n- Strip content not relevant to work (greetings, \"running late\", personal/food/coffee chit-chat);\n  where you drop a non-work stretch, leave a short bracketed marker like [personal aside]\n\nTranscript:\n\\\"\\\"\\\"\n{{transcript}}\n\\\"\\\"\\\"\n\nReturn only the Markdown.";

    private CleanupPrompts() {
    }

    public final String getDEFAULT_GLOSSARY() {
        return DEFAULT_GLOSSARY;
    }

    public final String getDEFAULT_CLEANUP_PROMPT() {
        return DEFAULT_CLEANUP_PROMPT;
    }

    public final String getDEFAULT_SUMMARY_PROMPT() {
        return DEFAULT_SUMMARY_PROMPT;
    }

    public final String getDEFAULT_MEETING_COMBINED_PROMPT() {
        return DEFAULT_MEETING_COMBINED_PROMPT;
    }

    public final String glossary(PipelineSettings settings) {
        Intrinsics.checkNotNullParameter(settings, "settings");
        String string = StringsKt.trim((CharSequence) settings.getGlossary()).toString();
        if (StringsKt.isBlank(string)) {
            string = DEFAULT_GLOSSARY;
        }
        return string;
    }

    public final String speakerNote(boolean diarized) {
        if (diarized) {
            return "Speaker labels (\"Speaker 1:\", \"You:\", \"Other:\", …) may be present — KEEP them\n(bold labels, one turn per speaker change). Merge consecutive turns from the same speaker.\nSpeaker labels may occasionally be misattributed.";
        }
        return "Reflow into logical paragraphs grouped by topic.";
    }

    public static /* synthetic */ String render$default(CleanupPrompts cleanupPrompts, String str, String str2, String str3, String str4, int i, Object obj) {
        if ((i & 8) != 0) {
            str4 = "";
        }
        return cleanupPrompts.render(str, str2, str3, str4);
    }

    public final String render(String template, String transcript, String glossary, String speakerNote) {
        Intrinsics.checkNotNullParameter(template, "template");
        Intrinsics.checkNotNullParameter(transcript, "transcript");
        Intrinsics.checkNotNullParameter(glossary, "glossary");
        Intrinsics.checkNotNullParameter(speakerNote, "speakerNote");
        return StringsKt.replace$default(StringsKt.replace$default(StringsKt.replace$default(template, GLOSSARY, glossary, false, 4, (Object) null), SPEAKER_NOTE, speakerNote, false, 4, (Object) null), TRANSCRIPT, transcript, false, 4, (Object) null);
    }

    public final String buildCleanup(PipelineSettings settings, String rawTranscript, boolean diarized) {
        Intrinsics.checkNotNullParameter(settings, "settings");
        Intrinsics.checkNotNullParameter(rawTranscript, "rawTranscript");
        String string = StringsKt.trim((CharSequence) settings.getCleanupPrompt()).toString();
        if (StringsKt.isBlank(string)) {
            string = DEFAULT_CLEANUP_PROMPT;
        }
        String template = string;
        return render(template, rawTranscript, glossary(settings), speakerNote(diarized));
    }

    public final String buildSummary(PipelineSettings settings, String cleanedTranscript) {
        Intrinsics.checkNotNullParameter(settings, "settings");
        Intrinsics.checkNotNullParameter(cleanedTranscript, "cleanedTranscript");
        String string = StringsKt.trim((CharSequence) settings.getSummaryPrompt()).toString();
        if (StringsKt.isBlank(string)) {
            string = DEFAULT_SUMMARY_PROMPT;
        }
        String template = string;
        return render(template, cleanedTranscript, glossary(settings), "");
    }

    public final String build(String rawTranscript, boolean diarized) {
        Intrinsics.checkNotNullParameter(rawTranscript, "rawTranscript");
        return render(DEFAULT_MEETING_COMBINED_PROMPT, rawTranscript, DEFAULT_GLOSSARY, speakerNote(diarized));
    }

    public final String assembleMeetingMarkdown(String summaryMarkdown, String cleanedTranscript) {
        Intrinsics.checkNotNullParameter(summaryMarkdown, "summaryMarkdown");
        Intrinsics.checkNotNullParameter(cleanedTranscript, "cleanedTranscript");
        String summary = StringsKt.trim((CharSequence) summaryMarkdown).toString();
        String cleaned = StringsKt.trim((CharSequence) cleanedTranscript).toString();
        if (new Regex("(?im)^##\\s+Transcript\\b").containsMatchIn(summary)) {
            return summary;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(StringsKt.trimEnd((CharSequence) summary).toString());
        sb.append("\n\n## Transcript\n");
        sb.append(cleaned);
        sb.append('\n');
        return sb.toString();
    }
}
