package com.varun.pocketassistant.pipeline;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.sequences.Sequence;
import kotlin.sequences.SequencesKt;
import kotlin.text.MatchResult;
import kotlin.text.Regex;
import kotlin.text.RegexOption;
import kotlin.text.StringsKt;

/* JADX INFO: compiled from: CleanupPrompts.kt */
/* JADX INFO: loaded from: classes8.dex */
@Metadata(d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\b\u0003\bÇ\u0002\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003J\u000e\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0007J\u001a\u0010\b\u001a\u0004\u0018\u00010\u00072\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\t\u001a\u00020\u0007H\u0002J\u001e\u0010\n\u001a\b\u0012\u0004\u0012\u00020\u00070\u000b2\u0006\u0010\f\u001a\u00020\u00072\u0006\u0010\r\u001a\u00020\u0007H\u0002¨\u0006\u000e"}, d2 = {"Lcom/varun/pocketassistant/pipeline/MeetingCleanupParser;", "", "<init>", "()V", "parse", "Lcom/varun/pocketassistant/pipeline/MeetingCleanupParsed;", "markdown", "", "sectionBody", "heading", "bulletsUnder", "", "overview", "label", "app_debug"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class MeetingCleanupParser {
    public static final int $stable = 0;
    public static final MeetingCleanupParser INSTANCE = new MeetingCleanupParser();

    private MeetingCleanupParser() {
    }

    public final MeetingCleanupParsed parse(String markdown) {
        String title;
        Sequence<String> sequenceLineSequence;
        Sequence map;
        Object next;
        Intrinsics.checkNotNullParameter(markdown, "markdown");
        String strSectionBody = sectionBody(markdown, "Title");
        if (strSectionBody == null || (sequenceLineSequence = StringsKt.lineSequence(strSectionBody)) == null || (map = SequencesKt.map(sequenceLineSequence, new Function1() { // from class: com.varun.pocketassistant.pipeline.MeetingCleanupParser$$ExternalSyntheticLambda0
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return MeetingCleanupParser.parse$lambda$0((String) obj);
            }
        })) == null) {
            title = null;
        } else {
            Iterator it = map.iterator();
            do {
                if (!it.hasNext()) {
                    next = null;
                    break;
                }
                next = it.next();
            } while (StringsKt.isBlank((String) next));
            title = (String) next;
        }
        String overview = sectionBody(markdown, "Overview");
        if (overview == null) {
            overview = "";
        }
        return new MeetingCleanupParsed((title == null || StringsKt.isBlank(title)) ? null : title, StringsKt.trim((CharSequence) markdown).toString(), bulletsUnder(overview, "Topics"), bulletsUnder(overview, "People"), bulletsUnder(overview, "Decisions"), bulletsUnder(overview, "Open questions"));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final String parse$lambda$0(String it) {
        Intrinsics.checkNotNullParameter(it, "it");
        return StringsKt.trim(StringsKt.trim((CharSequence) StringsKt.removePrefix(StringsKt.trim((CharSequence) it).toString(), (CharSequence) "#")).toString(), '\"', '\'');
    }

    private final String sectionBody(String markdown, String heading) {
        List<String> groupValues;
        String str;
        Regex pattern = new Regex("(?im)^##\\s+" + Regex.INSTANCE.escape(heading) + "\\s*\\n(.*?)(?=^##\\s+|\\z)", RegexOption.DOT_MATCHES_ALL);
        MatchResult matchResultFind$default = Regex.find$default(pattern, markdown, 0, 2, null);
        if (matchResultFind$default == null || (groupValues = matchResultFind$default.getGroupValues()) == null || (str = (String) CollectionsKt.getOrNull(groupValues, 1)) == null) {
            return null;
        }
        return StringsKt.trim((CharSequence) str).toString();
    }

    /* JADX WARN: Code duplicated, block: B:13:0x00b3  */
    private final List<String> bulletsUnder(String overview, String label) {
        boolean z;
        List<String> listLines = StringsKt.lines(overview);
        int start = 0;
        Iterator<String> it = listLines.iterator();
        while (true) {
            if (it.hasNext()) {
                String str = (String) it.next();
                if (new Regex("(?i)^[-*]\\s*\\*\\*" + label + ":\\*\\*.*").matches(StringsKt.trim((CharSequence) str).toString())) {
                    z = true;
                } else {
                    if (new Regex("(?i)^\\*\\*" + label + ":\\*\\*.*").matches(StringsKt.trim((CharSequence) str).toString())) {
                        z = true;
                    } else {
                        if (new Regex("(?i)^" + label + ":\\s*.*").matches(StringsKt.trim((CharSequence) str).toString())) {
                            z = true;
                        } else {
                            z = false;
                        }
                    }
                }
                if (z) {
                    break;
                }
                start++;
            } else {
                start = -1;
                break;
            }
        }
        if (start < 0) {
            return CollectionsKt.emptyList();
        }
        String first = StringsKt.trim((CharSequence) listLines.get(start)).toString();
        String inline = StringsKt.trim((CharSequence) new Regex("(?i)^" + label + ":\\s*").replace(new Regex("(?i)^\\*\\*" + label + ":\\*\\*\\s*").replace(new Regex("(?i)^[-*]\\s*\\*\\*" + label + ":\\*\\*\\s*").replace(first, ""), ""), "")).toString();
        List items = new ArrayList();
        if (!StringsKt.isBlank(inline) && !StringsKt.equals(inline, "none", true)) {
            List list = items;
            Iterable iterableSplit = new Regex("\\s*[;|]\\s*|\\s*,\\s+(?=[A-Z])").split(inline, 0);
            Collection arrayList = new ArrayList(CollectionsKt.collectionSizeOrDefault(iterableSplit, 10));
            Iterator it2 = iterableSplit.iterator();
            while (it2.hasNext()) {
                arrayList.add(StringsKt.trimStart(StringsKt.trim((CharSequence) it2.next()).toString(), '-', '*', ' '));
                first = first;
            }
            Collection arrayList2 = new ArrayList();
            for (Object obj : (List) arrayList) {
                if (!StringsKt.isBlank((String) obj)) {
                    arrayList2.add(obj);
                }
            }
            CollectionsKt.addAll(list, (List) arrayList2);
        }
        for (int i = start + 1; i < listLines.size(); i++) {
            String line = StringsKt.trim((CharSequence) listLines.get(i)).toString();
            if (new Regex("(?i)^[-*]\\s*\\*\\*.+:\\*\\*.*").matches(line)) {
                break;
            }
            if (new Regex("(?i)^\\*\\*.+:\\*\\*.*").matches(line)) {
                break;
            }
            if (StringsKt.startsWith$default(line, "-", false, 2, (Object) null) || StringsKt.startsWith$default(line, "*", false, 2, (Object) null)) {
                String item = StringsKt.trim((CharSequence) StringsKt.trimStart(line, '-', '*', ' ')).toString();
                if (!StringsKt.isBlank(item)) {
                    items.add(item);
                }
            } else if (!StringsKt.isBlank(line) && !items.isEmpty()) {
                break;
            }
        }
        return CollectionsKt.distinct(items);
    }
}
