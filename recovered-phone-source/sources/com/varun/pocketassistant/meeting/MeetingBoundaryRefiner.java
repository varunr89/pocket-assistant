package com.varun.pocketassistant.meeting;

import android.util.Log;
import com.varun.pocketassistant.data.SegmentEntity;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.TreeSet;
import java.util.UUID;
import kotlin.Metadata;
import kotlin.Result;
import kotlin.ResultKt;
import kotlin.collections.CollectionsKt;
import kotlin.collections.SetsKt;
import kotlin.comparisons.ComparisonsKt;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.Boxing;
import kotlin.coroutines.jvm.internal.ContinuationImpl;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SpillingKt;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.Charsets;
import kotlin.text.StringsKt;
import kotlinx.serialization.json.internal.AbstractJsonLexerKt;
import org.apache.commons.math3.geometry.VectorFormat;
import org.json.JSONArray;
import org.json.JSONObject;

/* JADX INFO: compiled from: MeetingBoundaryRefiner.kt */
/* JADX INFO: loaded from: classes7.dex */
@Metadata(d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u000e\bÇ\u0002\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003JF\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\r0\f2\f\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u000f0\f2\"\u0010\u0010\u001a\u001e\b\u0001\u0012\u0004\u0012\u00020\u0005\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\u0012\u0012\u0006\u0012\u0004\u0018\u00010\u00010\u0011H\u0086@¢\u0006\u0002\u0010\u0013J&\u0010\u0014\u001a\u00020\u00052\f\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\u000f0\f2\u000e\u0010\u0016\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00050\fH\u0002J\u0012\u0010\u0017\u001a\u0004\u0018\u00010\u00052\u0006\u0010\u0018\u001a\u00020\u000fH\u0002J\u0016\u0010\u0019\u001a\u00020\r2\f\u0010\u001a\u001a\b\u0012\u0004\u0012\u00020\u000f0\fH\u0002J\u0016\u0010\u001b\u001a\u00020\u00052\f\u0010\u001c\u001a\b\u0012\u0004\u0012\u00020\u00050\fH\u0002J\u001e\u0010\u001d\u001a\b\u0012\u0004\u0012\u00020\u00070\f2\u0006\u0010\u001e\u001a\u00020\u00052\u0006\u0010\u001f\u001a\u00020\u0007H\u0002R\u000e\u0010\u0004\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0007X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0007X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0005X\u0082T¢\u0006\u0002\n\u0000¨\u0006 "}, d2 = {"Lcom/varun/pocketassistant/meeting/MeetingBoundaryRefiner;", "", "<init>", "()V", "SYSTEM", "", "MIN_TRANSCRIBED_CHUNKS", "", "SNIPPET_CHARS", "MAX_CHUNKS", "TAG", "refine", "", "Lcom/varun/pocketassistant/meeting/GapClusterer$Proposal;", "segments", "Lcom/varun/pocketassistant/data/SegmentEntity;", "chat", "Lkotlin/Function2;", "Lkotlin/coroutines/Continuation;", "(Ljava/util/List;Lkotlin/jvm/functions/Function2;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "buildPrompt", "sorted", "texts", "snippetOf", "seg", "proposalOf", "group", "stableId", "ids", "parseStarts", "response", "size", "app_debug"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class MeetingBoundaryRefiner {
    public static final int $stable = 0;
    public static final MeetingBoundaryRefiner INSTANCE = new MeetingBoundaryRefiner();
    private static final int MAX_CHUNKS = 20;
    private static final int MIN_TRANSCRIBED_CHUNKS = 2;
    private static final int SNIPPET_CHARS = 500;
    public static final String SYSTEM = "You detect meeting boundaries in a continuous recording that was auto-split into timed chunks. Each chunk lists its clock start time and the opening of its transcript (speaker-labeled). Group consecutive chunks into distinct meetings. A new meeting begins only when a chunk clearly starts a fresh conversation: greetings or introductions (e.g. 'hi', 'hey', 'thanks for joining', 'let me share my screen'), a sign-off followed by a new intro, or a clear change of participants/topic with no continuity from the previous chunk. If chunks flow continuously, keep them together. Output ONLY JSON.";
    private static final String TAG = "MeetingRefiner";

    /* JADX INFO: renamed from: com.varun.pocketassistant.meeting.MeetingBoundaryRefiner$refine$1, reason: invalid class name */
    /* JADX INFO: compiled from: MeetingBoundaryRefiner.kt */
    @Metadata(k = 3, mv = {2, 2, 0}, xi = 48)
    @DebugMetadata(c = "com.varun.pocketassistant.meeting.MeetingBoundaryRefiner", f = "MeetingBoundaryRefiner.kt", i = {0, 0, 0, 0, 0, 0}, l = {67}, m = "refine", n = {"segments", "chat", "sorted", "texts", "prompt", "transcribed"}, s = {"L$0", "L$1", "L$2", "L$3", "L$4", "I$0"})
    static final class AnonymousClass1 extends ContinuationImpl {
        int I$0;
        Object L$0;
        Object L$1;
        Object L$2;
        Object L$3;
        Object L$4;
        Object L$5;
        int label;
        /* synthetic */ Object result;

        AnonymousClass1(Continuation<? super AnonymousClass1> continuation) {
            super(continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            this.result = obj;
            this.label |= Integer.MIN_VALUE;
            return MeetingBoundaryRefiner.this.refine(null, null, this);
        }
    }

    private MeetingBoundaryRefiner() {
    }

    /* JADX WARN: Code duplicated, block: B:61:0x014c  */
    /* JADX WARN: Code duplicated, block: B:63:0x0155  */
    /* JADX WARN: Code duplicated, block: B:65:0x0182  */
    /* JADX WARN: Code duplicated, block: B:67:0x01a7  */
    /* JADX WARN: Code duplicated, block: B:68:0x01b4  */
    /* JADX WARN: Code duplicated, block: B:7:0x0018  */
    public final Object refine(List<SegmentEntity> list, Function2<? super String, ? super Continuation<? super String>, ? extends Object> function2, Continuation<? super List<GapClusterer.Proposal>> continuation) {
        AnonymousClass1 anonymousClass1;
        List<SegmentEntity> listSortedWith;
        Object objInvoke;
        MeetingBoundaryRefiner meetingBoundaryRefiner;
        List<Integer> starts;
        List bounds;
        List out;
        int i;
        int size;
        List<SegmentEntity> listSubList;
        if (continuation instanceof AnonymousClass1) {
            anonymousClass1 = (AnonymousClass1) continuation;
            if ((anonymousClass1.label & Integer.MIN_VALUE) != 0) {
                anonymousClass1.label -= Integer.MIN_VALUE;
            } else {
                anonymousClass1 = new AnonymousClass1(continuation);
            }
        } else {
            anonymousClass1 = new AnonymousClass1(continuation);
        }
        AnonymousClass1 anonymousClass2 = anonymousClass1;
        Object $result = anonymousClass2.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (anonymousClass2.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                listSortedWith = CollectionsKt.sortedWith(list, new Comparator() { // from class: com.varun.pocketassistant.meeting.MeetingBoundaryRefiner$refine$$inlined$sortedBy$1
                    /* JADX WARN: Multi-variable type inference failed */
                    @Override // java.util.Comparator
                    public final int compare(T t, T t2) {
                        return ComparisonsKt.compareValues(Long.valueOf(((SegmentEntity) t).getStartedAtMs()), Long.valueOf(((SegmentEntity) t2).getStartedAtMs()));
                    }
                });
                if (listSortedWith.isEmpty()) {
                    return CollectionsKt.emptyList();
                }
                if (listSortedWith.size() == 1) {
                    return CollectionsKt.listOf(proposalOf(listSortedWith));
                }
                List<SegmentEntity> list2 = listSortedWith;
                Collection arrayList = new ArrayList(CollectionsKt.collectionSizeOrDefault(list2, 10));
                Iterator it = list2.iterator();
                while (it.hasNext()) {
                    arrayList.add(INSTANCE.snippetOf((SegmentEntity) it.next()));
                }
                List texts = (List) arrayList;
                List list3 = texts;
                int i2 = 0;
                if (!(list3 instanceof Collection) || !list3.isEmpty()) {
                    int i3 = 0;
                    Iterator it2 = list3.iterator();
                    while (it2.hasNext()) {
                        if ((((String) it2.next()) != null) && (i3 = i3 + 1) < 0) {
                            CollectionsKt.throwCountOverflow();
                        }
                    }
                    i2 = i3;
                }
                int transcribed = i2;
                if (transcribed < 2 || listSortedWith.size() > 20) {
                    return CollectionsKt.listOf(proposalOf(listSortedWith));
                }
                String prompt = buildPrompt(listSortedWith, texts);
                try {
                    anonymousClass2.L$0 = SpillingKt.nullOutSpilledVariable(list);
                    anonymousClass2.L$1 = SpillingKt.nullOutSpilledVariable(function2);
                    anonymousClass2.L$2 = listSortedWith;
                    anonymousClass2.L$3 = SpillingKt.nullOutSpilledVariable(texts);
                    anonymousClass2.L$4 = SpillingKt.nullOutSpilledVariable(prompt);
                    anonymousClass2.L$5 = this;
                    anonymousClass2.I$0 = transcribed;
                    anonymousClass2.label = 1;
                    try {
                        objInvoke = function2.invoke(prompt, anonymousClass2);
                        if (objInvoke == coroutine_suspended) {
                            return coroutine_suspended;
                        }
                        meetingBoundaryRefiner = this;
                        try {
                            starts = meetingBoundaryRefiner.parseStarts((String) objInvoke, listSortedWith.size());
                            if (starts.size() <= 1) {
                                return CollectionsKt.listOf(proposalOf(listSortedWith));
                            }
                            bounds = CollectionsKt.sorted(CollectionsKt.distinct(CollectionsKt.plus((Collection<? extends Integer>) starts, Boxing.boxInt(listSortedWith.size()))));
                            out = new ArrayList();
                            i = 0;
                            size = bounds.size() - 1;
                            while (i < size) {
                                List<Integer> list4 = starts;
                                listSubList = listSortedWith.subList(((Number) bounds.get(i)).intValue(), ((Number) bounds.get(i + 1)).intValue());
                                if (!listSubList.isEmpty()) {
                                    out.add(proposalOf(listSubList));
                                }
                                i++;
                                starts = list4;
                                anonymousClass2 = anonymousClass2;
                            }
                            Log.i(TAG, "refined block of " + listSortedWith.size() + " chunks into " + out.size() + " meeting(s)");
                            return out;
                        } catch (Throwable th) {
                            t = th;
                            Log.w(TAG, "boundary refine failed, keeping single block: " + t.getMessage());
                            return CollectionsKt.listOf(proposalOf(listSortedWith));
                        }
                    } catch (Throwable th2) {
                        t = th2;
                        Log.w(TAG, "boundary refine failed, keeping single block: " + t.getMessage());
                        return CollectionsKt.listOf(proposalOf(listSortedWith));
                    }
                } catch (Throwable th3) {
                    t = th3;
                }
                break;
            case 1:
                int i4 = anonymousClass2.I$0;
                meetingBoundaryRefiner = (MeetingBoundaryRefiner) anonymousClass2.L$5;
                listSortedWith = (List) anonymousClass2.L$2;
                try {
                    ResultKt.throwOnFailure($result);
                    objInvoke = $result;
                    starts = meetingBoundaryRefiner.parseStarts((String) objInvoke, listSortedWith.size());
                    if (starts.size() <= 1) {
                        return CollectionsKt.listOf(proposalOf(listSortedWith));
                    }
                    bounds = CollectionsKt.sorted(CollectionsKt.distinct(CollectionsKt.plus((Collection<? extends Integer>) starts, Boxing.boxInt(listSortedWith.size()))));
                    out = new ArrayList();
                    i = 0;
                    size = bounds.size() - 1;
                    while (i < size) {
                        List<Integer> list5 = starts;
                        listSubList = listSortedWith.subList(((Number) bounds.get(i)).intValue(), ((Number) bounds.get(i + 1)).intValue());
                        if (!listSubList.isEmpty()) {
                            out.add(proposalOf(listSubList));
                        }
                        i++;
                        starts = list5;
                        anonymousClass2 = anonymousClass2;
                    }
                    Log.i(TAG, "refined block of " + listSortedWith.size() + " chunks into " + out.size() + " meeting(s)");
                    return out;
                } catch (Throwable th4) {
                    t = th4;
                    Log.w(TAG, "boundary refine failed, keeping single block: " + t.getMessage());
                    return CollectionsKt.listOf(proposalOf(listSortedWith));
                }
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
    }

    private final String buildPrompt(List<SegmentEntity> sorted, List<String> texts) {
        StringBuilder sb = new StringBuilder();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm", Locale.US);
        sb.append("Chunks:\n");
        int i = 0;
        for (Object obj : sorted) {
            int i2 = i + 1;
            if (i < 0) {
                CollectionsKt.throwIndexOverflow();
            }
            sb.append("[").append(i).append("] ").append(simpleDateFormat.format(new Date(((SegmentEntity) obj).getStartedAtMs())));
            StringBuilder sbAppend = sb.append(" — ");
            String str = texts.get(i);
            if (str == null) {
                str = "(no transcript yet)";
            }
            sbAppend.append(str).append("\n");
            i = i2;
        }
        sb.append("\nReturn JSON only: {\"starts\":[chunk indices that begin a new meeting]}. Always include 0. Example: {\"starts\":[0,3]}.");
        return sb.toString();
    }

    /* JADX WARN: Code duplicated, block: B:17:0x002d A[RETURN] */
    /* JADX WARN: Code duplicated, block: B:9:0x0016  */
    private final String snippetOf(SegmentEntity seg) {
        String diarizedTranscript = seg.getDiarizedTranscript();
        if (diarizedTranscript == null) {
            diarizedTranscript = seg.getTranscript();
            if (diarizedTranscript != null || StringsKt.isBlank(diarizedTranscript)) {
            }
            if (diarizedTranscript == null) {
                return null;
            }
        } else {
            if (StringsKt.isBlank(diarizedTranscript)) {
                diarizedTranscript = null;
            }
            if (diarizedTranscript == null) {
                diarizedTranscript = seg.getTranscript();
                diarizedTranscript = diarizedTranscript != null ? null : null;
                if (diarizedTranscript == null) {
                    return null;
                }
            }
        }
        String raw = diarizedTranscript;
        String flat = StringsKt.trim((CharSequence) StringsKt.replace$default(raw, '\n', ' ', false, 4, (Object) null)).toString();
        if (flat.length() > SNIPPET_CHARS) {
            return StringsKt.take(flat, SNIPPET_CHARS) + "…";
        }
        return flat;
    }

    private final GapClusterer.Proposal proposalOf(List<SegmentEntity> group) {
        List<SegmentEntity> list = group;
        Collection arrayList = new ArrayList(CollectionsKt.collectionSizeOrDefault(list, 10));
        Iterator it = list.iterator();
        while (it.hasNext()) {
            arrayList.add(((SegmentEntity) it.next()).getId());
        }
        List ids = (List) arrayList;
        String strStableId = stableId(ids);
        Iterator<T> it2 = group.iterator();
        if (!it2.hasNext()) {
            throw new NoSuchElementException();
        }
        long startedAtMs = ((SegmentEntity) it2.next()).getStartedAtMs();
        while (it2.hasNext()) {
            long startedAtMs2 = ((SegmentEntity) it2.next()).getStartedAtMs();
            if (startedAtMs > startedAtMs2) {
                startedAtMs = startedAtMs2;
            }
        }
        Iterator<T> it3 = group.iterator();
        if (!it3.hasNext()) {
            throw new NoSuchElementException();
        }
        long endedAtMs = ((SegmentEntity) it3.next()).getEndedAtMs();
        while (it3.hasNext()) {
            long endedAtMs2 = ((SegmentEntity) it3.next()).getEndedAtMs();
            if (endedAtMs < endedAtMs2) {
                endedAtMs = endedAtMs2;
            }
        }
        return new GapClusterer.Proposal(strStableId, startedAtMs, endedAtMs, ids);
    }

    private final String stableId(List<String> ids) {
        byte[] bytes = CollectionsKt.joinToString$default(CollectionsKt.sorted(ids), "|", null, null, 0, null, null, 62, null).getBytes(Charsets.UTF_8);
        Intrinsics.checkNotNullExpressionValue(bytes, "getBytes(...)");
        String string = UUID.nameUUIDFromBytes(bytes).toString();
        Intrinsics.checkNotNullExpressionValue(string, "toString(...)");
        return string;
    }

    private final List<Integer> parseStarts(String response, int size) {
        Object objM8304constructorimpl;
        JSONArray arr;
        String json = "";
        String strSubstringAfter = StringsKt.substringAfter(response, AbstractJsonLexerKt.BEGIN_OBJ, "");
        String strSubstringBeforeLast = StringsKt.substringBeforeLast(strSubstringAfter.length() == 0 ? "" : VectorFormat.DEFAULT_PREFIX + strSubstringAfter, AbstractJsonLexerKt.END_OBJ, "");
        if (!(strSubstringBeforeLast.length() == 0)) {
            json = ((Object) strSubstringBeforeLast) + VectorFormat.DEFAULT_SUFFIX;
        }
        try {
            Result.Companion companion = Result.INSTANCE;
            MeetingBoundaryRefiner meetingBoundaryRefiner = this;
            String str = json;
            if (StringsKt.isBlank(str)) {
                str = response;
            }
            objM8304constructorimpl = Result.m8304constructorimpl(new JSONObject(str));
        } catch (Throwable th) {
            Result.Companion companion2 = Result.INSTANCE;
            objM8304constructorimpl = Result.m8304constructorimpl(ResultKt.createFailure(th));
        }
        if (Result.m8310isFailureimpl(objM8304constructorimpl)) {
            objM8304constructorimpl = null;
        }
        JSONObject obj = (JSONObject) objM8304constructorimpl;
        if (obj != null && (arr = obj.optJSONArray("starts")) != null) {
            TreeSet set = SetsKt.sortedSetOf(0);
            int length = arr.length();
            for (int i = 0; i < length; i++) {
                int v = arr.optInt(i, -1);
                if (v >= 0 && v < size) {
                    set.add(Integer.valueOf(v));
                }
            }
            return CollectionsKt.toList(set);
        }
        return CollectionsKt.listOf(0);
    }
}
