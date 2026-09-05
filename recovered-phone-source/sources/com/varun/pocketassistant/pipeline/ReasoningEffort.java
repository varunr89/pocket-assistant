package com.varun.pocketassistant.pipeline;

import java.util.Iterator;
import kotlin.Metadata;
import kotlin.enums.EnumEntries;
import kotlin.enums.EnumEntriesKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;

/* JADX INFO: compiled from: OpenRouterModels.kt */
/* JADX INFO: loaded from: classes8.dex */
@Metadata(d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0010\b\u0086\u0081\u0002\u0018\u0000 \u00122\b\u0012\u0004\u0012\u00020\u00000\u0001:\u0001\u0012B\u0019\b\u0002\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003¢\u0006\u0004\b\u0005\u0010\u0006R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u0011\u0010\u0004\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\t\u0010\bj\u0002\b\nj\u0002\b\u000bj\u0002\b\fj\u0002\b\rj\u0002\b\u000ej\u0002\b\u000fj\u0002\b\u0010j\u0002\b\u0011¨\u0006\u0013"}, d2 = {"Lcom/varun/pocketassistant/pipeline/ReasoningEffort;", "", "apiValue", "", "label", "<init>", "(Ljava/lang/String;ILjava/lang/String;Ljava/lang/String;)V", "getApiValue", "()Ljava/lang/String;", "getLabel", "DEFAULT", "NONE", "MINIMAL", "LOW", "MEDIUM", "HIGH", "XHIGH", "MAX", "Companion", "app_debug"}, k = 1, mv = {2, 2, 0}, xi = 48)
public enum ReasoningEffort {
    DEFAULT("", "Model default"),
    NONE("none", "None"),
    MINIMAL("minimal", "Minimal"),
    LOW("low", "Low"),
    MEDIUM("medium", "Medium"),
    HIGH("high", "High"),
    XHIGH("xhigh", "X-High"),
    MAX("max", "Max");

    private final String apiValue;
    private final String label;
    private static final /* synthetic */ EnumEntries $ENTRIES = EnumEntriesKt.enumEntries($VALUES);

    /* JADX INFO: renamed from: Companion, reason: from kotlin metadata */
    public static final Companion INSTANCE = new Companion(null);
    private static final EnumEntries<ReasoningEffort> selectable = getEntries();

    public static EnumEntries<ReasoningEffort> getEntries() {
        return $ENTRIES;
    }

    ReasoningEffort(String apiValue, String label) {
        this.apiValue = apiValue;
        this.label = label;
    }

    public final String getApiValue() {
        return this.apiValue;
    }

    public final String getLabel() {
        return this.label;
    }

    /* JADX INFO: compiled from: OpenRouterModels.kt */
    @Metadata(d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0086\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003J\u000e\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0007R\u0017\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00050\t¢\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000b¨\u0006\f"}, d2 = {"Lcom/varun/pocketassistant/pipeline/ReasoningEffort$Companion;", "", "<init>", "()V", "fromStored", "Lcom/varun/pocketassistant/pipeline/ReasoningEffort;", "value", "", "selectable", "Lkotlin/enums/EnumEntries;", "getSelectable", "()Lkotlin/enums/EnumEntries;", "app_debug"}, k = 1, mv = {2, 2, 0}, xi = 48)
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        public final ReasoningEffort fromStored(String value) {
            Object next;
            Intrinsics.checkNotNullParameter(value, "value");
            Iterator<ReasoningEffort> it = ReasoningEffort.getEntries().iterator();
            do {
                if (!it.hasNext()) {
                    next = null;
                    break;
                }
                next = it.next();
            } while (!Intrinsics.areEqual(((ReasoningEffort) next).getApiValue(), StringsKt.trim((CharSequence) value).toString()));
            ReasoningEffort reasoningEffort = (ReasoningEffort) next;
            return reasoningEffort == null ? ReasoningEffort.DEFAULT : reasoningEffort;
        }

        public final EnumEntries<ReasoningEffort> getSelectable() {
            return ReasoningEffort.selectable;
        }
    }
}
