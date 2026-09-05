package com.varun.pocketassistant.ui;

import java.util.List;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: HomeScreen.kt */
/* JADX INFO: loaded from: classes4.dex */
@Metadata(d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010 \n\u0002\u0010\u000e\n\u0002\b\n\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\b\u0082\b\u0018\u00002\u00020\u0001B'\u0012\u000e\b\u0002\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003\u0012\u000e\b\u0002\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003¢\u0006\u0004\b\u0006\u0010\u0007J\u000f\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003HÆ\u0003J\u000f\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003HÆ\u0003J)\u0010\r\u001a\u00020\u00002\u000e\b\u0002\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u00032\u000e\b\u0002\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003HÆ\u0001J\u0013\u0010\u000e\u001a\u00020\u000f2\b\u0010\u0010\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u0011\u001a\u00020\u0012HÖ\u0001J\t\u0010\u0013\u001a\u00020\u0004HÖ\u0001R\u0017\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003¢\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR\u0017\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003¢\u0006\b\n\u0000\u001a\u0004\b\n\u0010\t¨\u0006\u0014"}, d2 = {"Lcom/varun/pocketassistant/ui/MeetingMeta;", "", "topics", "", "", "people", "<init>", "(Ljava/util/List;Ljava/util/List;)V", "getTopics", "()Ljava/util/List;", "getPeople", "component1", "component2", "copy", "equals", "", "other", "hashCode", "", "toString", "app_debug"}, k = 1, mv = {2, 2, 0}, xi = 48)
final /* data */ class MeetingMeta {
    private final List<String> people;
    private final List<String> topics;

    /* JADX WARN: Multi-variable type inference failed */
    public MeetingMeta() {
        this(null, 0 == true ? 1 : 0, 3, 0 == true ? 1 : 0);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static /* synthetic */ MeetingMeta copy$default(MeetingMeta meetingMeta, List list, List list2, int i, Object obj) {
        if ((i & 1) != 0) {
            list = meetingMeta.topics;
        }
        if ((i & 2) != 0) {
            list2 = meetingMeta.people;
        }
        return meetingMeta.copy(list, list2);
    }

    public final List<String> component1() {
        return this.topics;
    }

    public final List<String> component2() {
        return this.people;
    }

    public final MeetingMeta copy(List<String> topics, List<String> people) {
        Intrinsics.checkNotNullParameter(topics, "topics");
        Intrinsics.checkNotNullParameter(people, "people");
        return new MeetingMeta(topics, people);
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof MeetingMeta)) {
            return false;
        }
        MeetingMeta meetingMeta = (MeetingMeta) other;
        return Intrinsics.areEqual(this.topics, meetingMeta.topics) && Intrinsics.areEqual(this.people, meetingMeta.people);
    }

    public int hashCode() {
        return (this.topics.hashCode() * 31) + this.people.hashCode();
    }

    public String toString() {
        return "MeetingMeta(topics=" + this.topics + ", people=" + this.people + ")";
    }

    public MeetingMeta(List<String> topics, List<String> people) {
        Intrinsics.checkNotNullParameter(topics, "topics");
        Intrinsics.checkNotNullParameter(people, "people");
        this.topics = topics;
        this.people = people;
    }

    public /* synthetic */ MeetingMeta(List list, List list2, int i, DefaultConstructorMarker defaultConstructorMarker) {
        this((i & 1) != 0 ? CollectionsKt.emptyList() : list, (i & 2) != 0 ? CollectionsKt.emptyList() : list2);
    }

    public final List<String> getTopics() {
        return this.topics;
    }

    public final List<String> getPeople() {
        return this.people;
    }
}
