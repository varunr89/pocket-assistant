package com.varun.pocketassistant.speech;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import kotlin.Metadata;
import kotlin.Pair;
import kotlin.Result;
import kotlin.ResultKt;
import kotlin.Triple;
import kotlin.TuplesKt;
import kotlin.collections.CollectionsKt;
import kotlin.comparisons.ComparisonsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Ref;
import kotlin.ranges.RangesKt;
import kotlin.text.CharsKt;
import kotlin.text.MatchResult;
import kotlin.text.Regex;
import kotlin.text.StringsKt;
import org.json.JSONArray;
import org.json.JSONObject;

/* JADX INFO: compiled from: DiarizationLabels.kt */
/* JADX INFO: loaded from: classes3.dex */
@Metadata(d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u000b\bÇ\u0002\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003J&\u0010\u0004\u001a\u0014\u0012\u0004\u0012\u00020\u0006\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00070\u00052\f\u0010\t\u001a\b\u0012\u0004\u0012\u00020\n0\u0007J\u001b\u0010\u000b\u001a\u0004\u0018\u00010\f2\f\u0010\r\u001a\b\u0012\u0004\u0012\u00020\b0\u0007¢\u0006\u0002\u0010\u000eJ\u001c\u0010\u000f\u001a\u00020\u00102\f\u0010\r\u001a\b\u0012\u0004\u0012\u00020\b0\u00072\u0006\u0010\u0011\u001a\u00020\u0010J\u0016\u0010\u0012\u001a\u00020\u00062\u0006\u0010\u0013\u001a\u00020\u00062\u0006\u0010\u0014\u001a\u00020\fJ\u000e\u0010\u0015\u001a\u00020\u00062\u0006\u0010\u0013\u001a\u00020\u0006J\u0014\u0010\u0016\u001a\u00020\u00062\f\u0010\r\u001a\b\u0012\u0004\u0012\u00020\b0\u0007J\u0016\u0010\u0017\u001a\b\u0012\u0004\u0012\u00020\b0\u00072\b\u0010\u0018\u001a\u0004\u0018\u00010\u0006J\u000e\u0010\u0019\u001a\u00020\u00102\u0006\u0010\u001a\u001a\u00020\u0006¨\u0006\u001b"}, d2 = {"Lcom/varun/pocketassistant/speech/DiarizationLabels;", "", "<init>", "()V", "fromWords", "Lkotlin/Pair;", "", "", "Lcom/varun/pocketassistant/speech/SpeakerCandidate;", "words", "Lcom/varun/pocketassistant/speech/DiarWord;", "suggestedYouId", "", "candidates", "(Ljava/util/List;)Ljava/lang/Integer;", "needsYouConfirm", "", "youConfirmed", "applyYou", "labeled", "youId", "autoLabelSingleSpeaker", "toJson", "parseCandidates", "json", "modelSupportsDiarize", "modelId", "app_debug"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class DiarizationLabels {
    public static final int $stable = 0;
    public static final DiarizationLabels INSTANCE = new DiarizationLabels();

    private DiarizationLabels() {
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v11, types: [T, java.lang.Integer] */
    /* JADX WARN: Type inference failed for: r0v13, types: [T, java.lang.StringBuilder] */
    /* JADX WARN: Type inference failed for: r0v18, types: [T, java.lang.Integer] */
    /* JADX WARN: Type inference failed for: r0v20, types: [T, java.lang.StringBuilder] */
    /* JADX WARN: Type inference failed for: r7v0, types: [T, java.lang.StringBuilder] */
    public final Pair<String, List<SpeakerCandidate>> fromWords(List<DiarWord> words) {
        Intrinsics.checkNotNullParameter(words, "words");
        if (words.isEmpty()) {
            return TuplesKt.to("", CollectionsKt.emptyList());
        }
        LinkedHashMap talkMs = new LinkedHashMap();
        LinkedHashMap sampleBuf = new LinkedHashMap();
        List<Triple> runs = new ArrayList();
        Ref.ObjectRef cur = new Ref.ObjectRef();
        Ref.DoubleRef runStart = new Ref.DoubleRef();
        Ref.ObjectRef buf = new Ref.ObjectRef();
        buf.element = new StringBuilder();
        for (DiarWord w : words) {
            int sp = w.getSpeaker();
            LinkedHashMap linkedHashMap = talkMs;
            Integer numValueOf = Integer.valueOf(sp);
            Long l = (Long) talkMs.get(Integer.valueOf(sp));
            long jLongValue = l != null ? l.longValue() : 0L;
            talkMs = talkMs;
            linkedHashMap.put(numValueOf, Long.valueOf(jLongValue + RangesKt.coerceAtLeast((long) ((w.getEnd() - w.getStart()) * 1000.0d), 0L)));
            if (cur.element == 0) {
                cur.element = Integer.valueOf(sp);
                runStart.element = w.getStart();
                buf.element = new StringBuilder(w.getWord());
            } else {
                Integer num = (Integer) cur.element;
                if (num == null || sp != num.intValue()) {
                    fromWords$flush(cur, buf, runs, runStart, sampleBuf);
                    cur.element = Integer.valueOf(sp);
                    runStart.element = w.getStart();
                    buf.element = new StringBuilder(w.getWord());
                } else {
                    if ((((CharSequence) buf.element).length() > 0) && !CharsKt.isWhitespace(StringsKt.last((CharSequence) buf.element))) {
                        ((StringBuilder) buf.element).append(' ');
                    }
                    ((StringBuilder) buf.element).append(w.getWord());
                }
            }
        }
        LinkedHashMap talkMs2 = talkMs;
        fromWords$flush(cur, buf, runs, runStart, sampleBuf);
        StringBuilder sb = new StringBuilder();
        int i = 0;
        for (Triple triple : runs) {
            int i2 = i;
            i++;
            if (i2 > 0) {
                sb.append("\n\n");
            }
            sb.append("Speaker ").append(((Number) triple.getFirst()).intValue()).append(": ").append((CharSequence) triple.getThird());
        }
        String labeled = sb.toString();
        Iterable iterableEntrySet = talkMs2.entrySet();
        Intrinsics.checkNotNullExpressionValue(iterableEntrySet, "<get-entries>(...)");
        Iterable<Map.Entry> iterableSortedWith = CollectionsKt.sortedWith(iterableEntrySet, new Comparator() { // from class: com.varun.pocketassistant.speech.DiarizationLabels$fromWords$$inlined$sortedByDescending$1
            @Override // java.util.Comparator
            public final int compare(T t, T t2) {
                return ComparisonsKt.compareValues((Long) ((Map.Entry) t2).getValue(), (Long) ((Map.Entry) t).getValue());
            }
        });
        Collection arrayList = new ArrayList(CollectionsKt.collectionSizeOrDefault(iterableSortedWith, 10));
        for (Map.Entry entry : iterableSortedWith) {
            Intrinsics.checkNotNull(entry);
            Iterable iterable = iterableSortedWith;
            Object key = entry.getKey();
            List runs2 = runs;
            Intrinsics.checkNotNullExpressionValue(key, "component1(...)");
            Integer num2 = (Integer) key;
            Object value = entry.getValue();
            Intrinsics.checkNotNullExpressionValue(value, "component2(...)");
            Long l2 = (Long) value;
            int iIntValue = num2.intValue();
            Ref.ObjectRef cur2 = cur;
            Ref.DoubleRef runStart2 = runStart;
            long jLongValue2 = l2.longValue();
            List listEmptyList = (List) sampleBuf.get(num2);
            if (listEmptyList == null) {
                listEmptyList = CollectionsKt.emptyList();
            }
            arrayList.add(new SpeakerCandidate(iIntValue, jLongValue2, listEmptyList));
            iterableSortedWith = iterable;
            runs = runs2;
            cur = cur2;
            runStart = runStart2;
        }
        List candidates = (List) arrayList;
        return TuplesKt.to(labeled, candidates);
    }

    private static final void fromWords$flush(Ref.ObjectRef<Integer> objectRef, Ref.ObjectRef<StringBuilder> objectRef2, List<Triple<Integer, Double, StringBuilder>> list, Ref.DoubleRef runStart, LinkedHashMap<Integer, List<String>> linkedHashMap) {
        ArrayList arrayList;
        Integer num = objectRef.element;
        if (num == null) {
            return;
        }
        int sp = num.intValue();
        String string = objectRef2.element.toString();
        Intrinsics.checkNotNullExpressionValue(string, "toString(...)");
        String text = StringsKt.trim((CharSequence) string).toString();
        if (text.length() > 0) {
            list.add(new Triple<>(Integer.valueOf(sp), Double.valueOf(runStart.element), new StringBuilder(text)));
            LinkedHashMap<Integer, List<String>> linkedHashMap2 = linkedHashMap;
            Integer numValueOf = Integer.valueOf(sp);
            List<String> list2 = linkedHashMap2.get(numValueOf);
            if (list2 == null) {
                arrayList = new ArrayList();
                linkedHashMap2.put(numValueOf, arrayList);
            } else {
                arrayList = list2;
            }
            List<String> list3 = arrayList;
            if (list3.size() < 2) {
                list3.add(StringsKt.take(text, 160));
            }
        }
    }

    public final Integer suggestedYouId(List<SpeakerCandidate> candidates) {
        Object next;
        Intrinsics.checkNotNullParameter(candidates, "candidates");
        Iterator it = candidates.iterator();
        if (it.hasNext()) {
            next = it.next();
            if (it.hasNext()) {
                long talkMs = ((SpeakerCandidate) next).getTalkMs();
                do {
                    Object next2 = it.next();
                    long talkMs2 = ((SpeakerCandidate) next2).getTalkMs();
                    if (talkMs < talkMs2) {
                        next = next2;
                        talkMs = talkMs2;
                    }
                } while (it.hasNext());
            }
        } else {
            next = null;
        }
        SpeakerCandidate speakerCandidate = (SpeakerCandidate) next;
        if (speakerCandidate != null) {
            return Integer.valueOf(speakerCandidate.getId());
        }
        return null;
    }

    public final boolean needsYouConfirm(List<SpeakerCandidate> candidates, boolean youConfirmed) {
        Intrinsics.checkNotNullParameter(candidates, "candidates");
        return !youConfirmed && candidates.size() >= 2;
    }

    public final String applyYou(String labeled, int youId) {
        Intrinsics.checkNotNullParameter(labeled, "labeled");
        if (StringsKt.isBlank(labeled)) {
            return labeled;
        }
        Regex blockRegex = new Regex("(?m)^Speaker (\\d+):\\s*");
        LinkedHashSet order = new LinkedHashSet();
        Iterator it = Regex.findAll$default(blockRegex, labeled, 0, 2, null).iterator();
        while (it.hasNext()) {
            order.add(Integer.valueOf(Integer.parseInt(((MatchResult) it.next()).getGroupValues().get(1))));
        }
        final LinkedHashMap map = new LinkedHashMap();
        int n = 0;
        Iterator it2 = order.iterator();
        Intrinsics.checkNotNullExpressionValue(it2, "iterator(...)");
        while (true) {
            String str = "You";
            if (!it2.hasNext()) {
                break;
            }
            Object next = it2.next();
            Intrinsics.checkNotNullExpressionValue(next, "next(...)");
            int id = ((Number) next).intValue();
            LinkedHashMap linkedHashMap = map;
            Integer numValueOf = Integer.valueOf(id);
            if (id != youId) {
                n++;
                str = "Speaker " + n;
            }
            linkedHashMap.put(numValueOf, str);
        }
        if (!map.containsKey(Integer.valueOf(youId))) {
            map.put(Integer.valueOf(youId), "You");
        }
        return blockRegex.replace(labeled, new Function1() { // from class: com.varun.pocketassistant.speech.DiarizationLabels$$ExternalSyntheticLambda0
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return DiarizationLabels.applyYou$lambda$6(map, (MatchResult) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final CharSequence applyYou$lambda$6(LinkedHashMap $map, MatchResult m) {
        Intrinsics.checkNotNullParameter(m, "m");
        int id = Integer.parseInt(m.getGroupValues().get(1));
        String str = (String) $map.get(Integer.valueOf(id));
        if (str == null) {
            str = "Speaker " + id;
        }
        return str + ": ";
    }

    public final String autoLabelSingleSpeaker(String labeled) {
        Intrinsics.checkNotNullParameter(labeled, "labeled");
        return new Regex("(?m)^Speaker \\d+:\\s*").replace(labeled, "You: ");
    }

    public final String toJson(List<SpeakerCandidate> candidates) {
        Intrinsics.checkNotNullParameter(candidates, "candidates");
        JSONArray arr = new JSONArray();
        for (SpeakerCandidate c : candidates) {
            JSONArray samples = new JSONArray();
            Iterator it = c.getSamples().iterator();
            while (it.hasNext()) {
                samples.put((String) it.next());
            }
            arr.put(new JSONObject().put("id", c.getId()).put("talkMs", c.getTalkMs()).put("samples", samples));
        }
        String string = arr.toString();
        Intrinsics.checkNotNullExpressionValue(string, "toString(...)");
        return string;
    }

    public final List<SpeakerCandidate> parseCandidates(String json) {
        Object objM8304constructorimpl;
        String str = json;
        if (str == null || StringsKt.isBlank(str)) {
            return CollectionsKt.emptyList();
        }
        try {
            Result.Companion companion = Result.INSTANCE;
            DiarizationLabels diarizationLabels = this;
            JSONArray jSONArray = new JSONArray(json);
            List listCreateListBuilder = CollectionsKt.createListBuilder();
            int i = 0;
            int length = jSONArray.length();
            while (i < length) {
                JSONObject jSONObject = jSONArray.getJSONObject(i);
                JSONArray jSONArrayOptJSONArray = jSONObject.optJSONArray("samples");
                if (jSONArrayOptJSONArray == null) {
                    jSONArrayOptJSONArray = new JSONArray();
                }
                List listCreateListBuilder2 = CollectionsKt.createListBuilder();
                int i2 = 0;
                int length2 = jSONArrayOptJSONArray.length();
                while (i2 < length2) {
                    listCreateListBuilder2.add(jSONArrayOptJSONArray.getString(i2));
                    i2++;
                    diarizationLabels = diarizationLabels;
                }
                listCreateListBuilder.add(new SpeakerCandidate(jSONObject.getInt("id"), jSONObject.optLong("talkMs", 0L), CollectionsKt.build(listCreateListBuilder2)));
                i++;
                diarizationLabels = diarizationLabels;
            }
            objM8304constructorimpl = Result.m8304constructorimpl(CollectionsKt.build(listCreateListBuilder));
        } catch (Throwable th) {
            Result.Companion companion2 = Result.INSTANCE;
            objM8304constructorimpl = Result.m8304constructorimpl(ResultKt.createFailure(th));
        }
        List listEmptyList = CollectionsKt.emptyList();
        if (Result.m8310isFailureimpl(objM8304constructorimpl)) {
            objM8304constructorimpl = listEmptyList;
        }
        return (List) objM8304constructorimpl;
    }

    public final boolean modelSupportsDiarize(String modelId) {
        Intrinsics.checkNotNullParameter(modelId, "modelId");
        String m = modelId.toLowerCase(Locale.ROOT);
        Intrinsics.checkNotNullExpressionValue(m, "toLowerCase(...)");
        return StringsKt.contains$default((CharSequence) m, (CharSequence) "grok-stt", false, 2, (Object) null) || StringsKt.contains$default((CharSequence) m, (CharSequence) "grok_stt", false, 2, (Object) null);
    }
}
