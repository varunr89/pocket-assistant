package com.varun.pocketassistant.pipeline;

import java.util.List;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import org.apache.commons.math3.geometry.VectorFormat;

/* JADX INFO: compiled from: CleanupPrompts.kt */
/* JADX INFO: loaded from: classes8.dex */
@Metadata(d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\b\u0016\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\b\u0087\b\u0018\u00002\u00020\u0001BQ\u0012\b\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00030\u0006\u0012\f\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\u00030\u0006\u0012\f\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00030\u0006\u0012\f\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u00030\u0006¢\u0006\u0004\b\n\u0010\u000bJ\u0006\u0010\u0014\u001a\u00020\u0003J\u000b\u0010\u0015\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\t\u0010\u0016\u001a\u00020\u0003HÆ\u0003J\u000f\u0010\u0017\u001a\b\u0012\u0004\u0012\u00020\u00030\u0006HÆ\u0003J\u000f\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\u00030\u0006HÆ\u0003J\u000f\u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\u00030\u0006HÆ\u0003J\u000f\u0010\u001a\u001a\b\u0012\u0004\u0012\u00020\u00030\u0006HÆ\u0003J_\u0010\u001b\u001a\u00020\u00002\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\u000e\b\u0002\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00030\u00062\u000e\b\u0002\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\u00030\u00062\u000e\b\u0002\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00030\u00062\u000e\b\u0002\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u00030\u0006HÆ\u0001J\u0013\u0010\u001c\u001a\u00020\u001d2\b\u0010\u001e\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u001f\u001a\u00020 HÖ\u0001J\t\u0010!\u001a\u00020\u0003HÖ\u0001R\u0013\u0010\u0002\u001a\u0004\u0018\u00010\u0003¢\u0006\b\n\u0000\u001a\u0004\b\f\u0010\rR\u0011\u0010\u0004\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\rR\u0017\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00030\u0006¢\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010R\u0017\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\u00030\u0006¢\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0010R\u0017\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00030\u0006¢\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0010R\u0017\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u00030\u0006¢\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0010¨\u0006\""}, d2 = {"Lcom/varun/pocketassistant/pipeline/MeetingCleanupParsed;", "", "title", "", "cleanedMarkdown", "topics", "", "people", "decisions", "openQuestions", "<init>", "(Ljava/lang/String;Ljava/lang/String;Ljava/util/List;Ljava/util/List;Ljava/util/List;Ljava/util/List;)V", "getTitle", "()Ljava/lang/String;", "getCleanedMarkdown", "getTopics", "()Ljava/util/List;", "getPeople", "getDecisions", "getOpenQuestions", "toMetadataJson", "component1", "component2", "component3", "component4", "component5", "component6", "copy", "equals", "", "other", "hashCode", "", "toString", "app_debug"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final /* data */ class MeetingCleanupParsed {
    public static final int $stable = 8;
    private final String cleanedMarkdown;
    private final List<String> decisions;
    private final List<String> openQuestions;
    private final List<String> people;
    private final String title;
    private final List<String> topics;

    /* JADX WARN: Multi-variable type inference failed */
    public static /* synthetic */ MeetingCleanupParsed copy$default(MeetingCleanupParsed meetingCleanupParsed, String str, String str2, List list, List list2, List list3, List list4, int i, Object obj) {
        if ((i & 1) != 0) {
            str = meetingCleanupParsed.title;
        }
        if ((i & 2) != 0) {
            str2 = meetingCleanupParsed.cleanedMarkdown;
        }
        if ((i & 4) != 0) {
            list = meetingCleanupParsed.topics;
        }
        if ((i & 8) != 0) {
            list2 = meetingCleanupParsed.people;
        }
        if ((i & 16) != 0) {
            list3 = meetingCleanupParsed.decisions;
        }
        if ((i & 32) != 0) {
            list4 = meetingCleanupParsed.openQuestions;
        }
        List list5 = list3;
        List list6 = list4;
        return meetingCleanupParsed.copy(str, str2, list, list2, list5, list6);
    }

    /* JADX INFO: renamed from: component1, reason: from getter */
    public final String getTitle() {
        return this.title;
    }

    /* JADX INFO: renamed from: component2, reason: from getter */
    public final String getCleanedMarkdown() {
        return this.cleanedMarkdown;
    }

    public final List<String> component3() {
        return this.topics;
    }

    public final List<String> component4() {
        return this.people;
    }

    public final List<String> component5() {
        return this.decisions;
    }

    public final List<String> component6() {
        return this.openQuestions;
    }

    public final MeetingCleanupParsed copy(String title, String cleanedMarkdown, List<String> topics, List<String> people, List<String> decisions, List<String> openQuestions) {
        Intrinsics.checkNotNullParameter(cleanedMarkdown, "cleanedMarkdown");
        Intrinsics.checkNotNullParameter(topics, "topics");
        Intrinsics.checkNotNullParameter(people, "people");
        Intrinsics.checkNotNullParameter(decisions, "decisions");
        Intrinsics.checkNotNullParameter(openQuestions, "openQuestions");
        return new MeetingCleanupParsed(title, cleanedMarkdown, topics, people, decisions, openQuestions);
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof MeetingCleanupParsed)) {
            return false;
        }
        MeetingCleanupParsed meetingCleanupParsed = (MeetingCleanupParsed) other;
        return Intrinsics.areEqual(this.title, meetingCleanupParsed.title) && Intrinsics.areEqual(this.cleanedMarkdown, meetingCleanupParsed.cleanedMarkdown) && Intrinsics.areEqual(this.topics, meetingCleanupParsed.topics) && Intrinsics.areEqual(this.people, meetingCleanupParsed.people) && Intrinsics.areEqual(this.decisions, meetingCleanupParsed.decisions) && Intrinsics.areEqual(this.openQuestions, meetingCleanupParsed.openQuestions);
    }

    public int hashCode() {
        return ((((((((((this.title == null ? 0 : this.title.hashCode()) * 31) + this.cleanedMarkdown.hashCode()) * 31) + this.topics.hashCode()) * 31) + this.people.hashCode()) * 31) + this.decisions.hashCode()) * 31) + this.openQuestions.hashCode();
    }

    public String toString() {
        return "MeetingCleanupParsed(title=" + this.title + ", cleanedMarkdown=" + this.cleanedMarkdown + ", topics=" + this.topics + ", people=" + this.people + ", decisions=" + this.decisions + ", openQuestions=" + this.openQuestions + ")";
    }

    public MeetingCleanupParsed(String title, String cleanedMarkdown, List<String> topics, List<String> people, List<String> decisions, List<String> openQuestions) {
        Intrinsics.checkNotNullParameter(cleanedMarkdown, "cleanedMarkdown");
        Intrinsics.checkNotNullParameter(topics, "topics");
        Intrinsics.checkNotNullParameter(people, "people");
        Intrinsics.checkNotNullParameter(decisions, "decisions");
        Intrinsics.checkNotNullParameter(openQuestions, "openQuestions");
        this.title = title;
        this.cleanedMarkdown = cleanedMarkdown;
        this.topics = topics;
        this.people = people;
        this.decisions = decisions;
        this.openQuestions = openQuestions;
    }

    public final String getTitle() {
        return this.title;
    }

    public final String getCleanedMarkdown() {
        return this.cleanedMarkdown;
    }

    public final List<String> getTopics() {
        return this.topics;
    }

    public final List<String> getPeople() {
        return this.people;
    }

    public final List<String> getDecisions() {
        return this.decisions;
    }

    public final List<String> getOpenQuestions() {
        return this.openQuestions;
    }

    private static final String toMetadataJson$esc(String s) {
        return StringsKt.replace$default(StringsKt.replace$default(s, "\\", "\\\\", false, 4, (Object) null), "\"", "\\\"", false, 4, (Object) null);
    }

    private static final String toMetadataJson$arr(List<String> list) {
        return CollectionsKt.joinToString$default(list, null, "[", "]", 0, null, new Function1() { // from class: com.varun.pocketassistant.pipeline.MeetingCleanupParsed$$ExternalSyntheticLambda0
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return MeetingCleanupParsed.toMetadataJson$arr$lambda$0((String) obj);
            }
        }, 25, null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final CharSequence toMetadataJson$arr$lambda$0(String it) {
        Intrinsics.checkNotNullParameter(it, "it");
        return "\"" + toMetadataJson$esc(it) + "\"";
    }

    public final String toMetadataJson() {
        return "{\"topics\":" + toMetadataJson$arr(this.topics) + ",\"people\":" + toMetadataJson$arr(this.people) + ",\"decisions\":" + toMetadataJson$arr(this.decisions) + ",\"openQuestions\":" + toMetadataJson$arr(this.openQuestions) + VectorFormat.DEFAULT_SUFFIX;
    }
}
