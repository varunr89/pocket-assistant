package com.varun.pocketassistant.meeting;

import com.varun.pocketassistant.data.MeetingRepository;
import com.varun.pocketassistant.data.SegmentEndReason;
import com.varun.pocketassistant.data.SegmentEntity;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.comparisons.ComparisonsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Ref;
import kotlin.text.Charsets;

/* JADX INFO: compiled from: GapClusterer.kt */
/* JADX INFO: loaded from: classes7.dex */
@Metadata(d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0003\bÇ\u0002\u0018\u00002\u00020\u0001:\u0001\u0012B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003J.\u0010\b\u001a\b\u0012\u0004\u0012\u00020\n0\t2\f\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\f0\t2\b\b\u0002\u0010\r\u001a\u00020\u00052\b\b\u0002\u0010\u000e\u001a\u00020\u0005J\u0016\u0010\u000f\u001a\u00020\u00102\f\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00100\tH\u0002R\u000e\u0010\u0004\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000¨\u0006\u0013"}, d2 = {"Lcom/varun/pocketassistant/meeting/GapClusterer;", "", "<init>", "()V", "DEFAULT_GAP_MS", "", "DEFAULT_MAX_SPAN_MS", "CONTINUATION_TOLERANCE_MS", "cluster", "", "Lcom/varun/pocketassistant/meeting/GapClusterer$Proposal;", "segments", "Lcom/varun/pocketassistant/data/SegmentEntity;", "gapMs", "maxSpanMs", "stableId", "", "segmentIds", "Proposal", "app_debug"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class GapClusterer {
    public static final int $stable = 0;
    public static final long CONTINUATION_TOLERANCE_MS = 5000;
    public static final long DEFAULT_GAP_MS = 1200000;
    public static final long DEFAULT_MAX_SPAN_MS = 10800000;
    public static final GapClusterer INSTANCE = new GapClusterer();

    private GapClusterer() {
    }

    /* JADX INFO: compiled from: GapClusterer.kt */
    @Metadata(d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\b\u000f\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\b\u0087\b\u0018\u00002\u00020\u0001B-\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0005\u0012\f\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\u00030\b¢\u0006\u0004\b\t\u0010\nJ\t\u0010\u0012\u001a\u00020\u0003HÆ\u0003J\t\u0010\u0013\u001a\u00020\u0005HÆ\u0003J\t\u0010\u0014\u001a\u00020\u0005HÆ\u0003J\u000f\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\u00030\bHÆ\u0003J7\u0010\u0016\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00052\u000e\b\u0002\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\u00030\bHÆ\u0001J\u0013\u0010\u0017\u001a\u00020\u00182\b\u0010\u0019\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u001a\u001a\u00020\u001bHÖ\u0001J\t\u0010\u001c\u001a\u00020\u0003HÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000eR\u0011\u0010\u0006\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u000eR\u0017\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\u00030\b¢\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011¨\u0006\u001d"}, d2 = {"Lcom/varun/pocketassistant/meeting/GapClusterer$Proposal;", "", "id", "", "startMs", "", "endMs", "segmentIds", "", "<init>", "(Ljava/lang/String;JJLjava/util/List;)V", "getId", "()Ljava/lang/String;", "getStartMs", "()J", "getEndMs", "getSegmentIds", "()Ljava/util/List;", "component1", "component2", "component3", "component4", "copy", "equals", "", "other", "hashCode", "", "toString", "app_debug"}, k = 1, mv = {2, 2, 0}, xi = 48)
    public static final /* data */ class Proposal {
        public static final int $stable = 8;
        private final long endMs;
        private final String id;
        private final List<String> segmentIds;
        private final long startMs;

        /* JADX WARN: Multi-variable type inference failed */
        public static /* synthetic */ Proposal copy$default(Proposal proposal, String str, long j, long j2, List list, int i, Object obj) {
            if ((i & 1) != 0) {
                str = proposal.id;
            }
            if ((i & 2) != 0) {
                j = proposal.startMs;
            }
            if ((i & 4) != 0) {
                j2 = proposal.endMs;
            }
            if ((i & 8) != 0) {
                list = proposal.segmentIds;
            }
            List list2 = list;
            return proposal.copy(str, j, j2, list2);
        }

        /* JADX INFO: renamed from: component1, reason: from getter */
        public final String getId() {
            return this.id;
        }

        /* JADX INFO: renamed from: component2, reason: from getter */
        public final long getStartMs() {
            return this.startMs;
        }

        /* JADX INFO: renamed from: component3, reason: from getter */
        public final long getEndMs() {
            return this.endMs;
        }

        public final List<String> component4() {
            return this.segmentIds;
        }

        public final Proposal copy(String id, long startMs, long endMs, List<String> segmentIds) {
            Intrinsics.checkNotNullParameter(id, "id");
            Intrinsics.checkNotNullParameter(segmentIds, "segmentIds");
            return new Proposal(id, startMs, endMs, segmentIds);
        }

        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof Proposal)) {
                return false;
            }
            Proposal proposal = (Proposal) other;
            return Intrinsics.areEqual(this.id, proposal.id) && this.startMs == proposal.startMs && this.endMs == proposal.endMs && Intrinsics.areEqual(this.segmentIds, proposal.segmentIds);
        }

        public int hashCode() {
            return (((((this.id.hashCode() * 31) + Long.hashCode(this.startMs)) * 31) + Long.hashCode(this.endMs)) * 31) + this.segmentIds.hashCode();
        }

        public String toString() {
            return "Proposal(id=" + this.id + ", startMs=" + this.startMs + ", endMs=" + this.endMs + ", segmentIds=" + this.segmentIds + ")";
        }

        public Proposal(String id, long startMs, long endMs, List<String> segmentIds) {
            Intrinsics.checkNotNullParameter(id, "id");
            Intrinsics.checkNotNullParameter(segmentIds, "segmentIds");
            this.id = id;
            this.startMs = startMs;
            this.endMs = endMs;
            this.segmentIds = segmentIds;
        }

        public final String getId() {
            return this.id;
        }

        public final long getStartMs() {
            return this.startMs;
        }

        public final long getEndMs() {
            return this.endMs;
        }

        public final List<String> getSegmentIds() {
            return this.segmentIds;
        }
    }

    public static /* synthetic */ List cluster$default(GapClusterer gapClusterer, List list, long j, long j2, int i, Object obj) {
        long j3;
        long j4;
        if ((i & 2) == 0) {
            j3 = j;
        } else {
            j3 = 1200000;
        }
        if ((i & 4) == 0) {
            j4 = j2;
        } else {
            j4 = 10800000;
        }
        return gapClusterer.cluster(list, j3, j4);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r4v2, types: [T, java.util.List] */
    public final List<Proposal> cluster(List<SegmentEntity> segments, long gapMs, long maxSpanMs) {
        boolean z;
        boolean z2;
        Intrinsics.checkNotNullParameter(segments, "segments");
        Collection arrayList = new ArrayList();
        Iterator it = segments.iterator();
        while (true) {
            z = false;
            z2 = true;
            if (!it.hasNext()) {
                break;
            }
            Object next = it.next();
            SegmentEntity segmentEntity = (SegmentEntity) next;
            if (MeetingRepository.Companion.isAssignableRecording(segmentEntity) && segmentEntity.getMeetingId() == null) {
                z = true;
            }
            if (z) {
                arrayList.add(next);
            }
        }
        List sorted = CollectionsKt.sortedWith((List) arrayList, new Comparator() { // from class: com.varun.pocketassistant.meeting.GapClusterer$cluster$$inlined$sortedBy$1
            /* JADX WARN: Multi-variable type inference failed */
            @Override // java.util.Comparator
            public final int compare(T t, T t2) {
                return ComparisonsKt.compareValues(Long.valueOf(((SegmentEntity) t).getStartedAtMs()), Long.valueOf(((SegmentEntity) t2).getStartedAtMs()));
            }
        });
        if (sorted.isEmpty()) {
            return CollectionsKt.emptyList();
        }
        List out = new ArrayList();
        Ref.ObjectRef batch = new Ref.ObjectRef();
        batch.element = CollectionsKt.mutableListOf(CollectionsKt.first(sorted));
        Ref.LongRef batchStart = new Ref.LongRef();
        batchStart.element = ((SegmentEntity) CollectionsKt.first(sorted)).getStartedAtMs();
        Ref.LongRef batchEnd = new Ref.LongRef();
        batchEnd.element = ((SegmentEntity) CollectionsKt.first(sorted)).getEndedAtMs();
        int i = 1;
        int size = sorted.size();
        while (i < size) {
            SegmentEntity seg = (SegmentEntity) sorted.get(i);
            SegmentEntity prev = (SegmentEntity) sorted.get(i - 1);
            boolean continuesPrev = (Intrinsics.areEqual(prev.getEndReason(), SegmentEndReason.DURATION_CAP) && Intrinsics.areEqual(prev.getSessionId(), seg.getSessionId()) && seg.getStartedAtMs() - prev.getEndedAtMs() <= 5000) ? z2 : z;
            long gap = seg.getStartedAtMs() - batchEnd.element;
            List sorted2 = sorted;
            long wouldSpan = seg.getEndedAtMs() - batchStart.element;
            if (!continuesPrev && (gap > gapMs || wouldSpan > maxSpanMs)) {
                cluster$flush(batch, out, batchStart, batchEnd);
                batchStart.element = seg.getStartedAtMs();
                batchEnd.element = seg.getEndedAtMs();
                ((List) batch.element).add(seg);
            } else {
                ((List) batch.element).add(seg);
                batchEnd.element = Math.max(batchEnd.element, seg.getEndedAtMs());
            }
            i++;
            sorted = sorted2;
            size = size;
            z = false;
            z2 = true;
        }
        cluster$flush(batch, out, batchStart, batchEnd);
        Collection arrayList2 = new ArrayList();
        for (Object obj : out) {
            Proposal proposal = (Proposal) obj;
            if (proposal.getSegmentIds().size() > 1 || proposal.getEndMs() - proposal.getStartMs() >= 120000) {
                arrayList2.add(obj);
            }
        }
        return (List) arrayList2;
    }

    /* JADX WARN: Type inference failed for: r0v8, types: [T, java.util.List] */
    private static final void cluster$flush(Ref.ObjectRef<List<SegmentEntity>> objectRef, List<Proposal> list, Ref.LongRef batchStart, Ref.LongRef batchEnd) {
        if (objectRef.element.isEmpty()) {
            return;
        }
        Iterable iterable = objectRef.element;
        Collection arrayList = new ArrayList(CollectionsKt.collectionSizeOrDefault(iterable, 10));
        Iterator it = iterable.iterator();
        while (it.hasNext()) {
            arrayList.add(((SegmentEntity) it.next()).getId());
        }
        List ids = (List) arrayList;
        list.add(new Proposal(INSTANCE.stableId(ids), batchStart.element, batchEnd.element, ids));
        objectRef.element = new ArrayList();
    }

    private final String stableId(List<String> segmentIds) {
        String key = CollectionsKt.joinToString$default(CollectionsKt.sorted(segmentIds), "|", null, null, 0, null, null, 62, null);
        byte[] bytes = key.getBytes(Charsets.UTF_8);
        Intrinsics.checkNotNullExpressionValue(bytes, "getBytes(...)");
        String string = UUID.nameUUIDFromBytes(bytes).toString();
        Intrinsics.checkNotNullExpressionValue(string, "toString(...)");
        return string;
    }
}
