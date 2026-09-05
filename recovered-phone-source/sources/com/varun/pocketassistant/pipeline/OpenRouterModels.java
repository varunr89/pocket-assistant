package com.varun.pocketassistant.pipeline;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import kotlinx.serialization.json.internal.AbstractJsonLexerKt;

/* JADX INFO: compiled from: OpenRouterModels.kt */
/* JADX INFO: loaded from: classes8.dex */
@Metadata(d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0003\bÇ\u0002\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003J\u000e\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0005J\u0016\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\u00052\u0006\u0010\n\u001a\u00020\u0005¨\u0006\u000b"}, d2 = {"Lcom/varun/pocketassistant/pipeline/OpenRouterModels;", "", "<init>", "()V", "normalizeId", "", "raw", "matches", "", "a", "b", "app_debug"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class OpenRouterModels {
    public static final int $stable = 0;
    public static final OpenRouterModels INSTANCE = new OpenRouterModels();

    private OpenRouterModels() {
    }

    public final String normalizeId(String raw) {
        String id;
        Intrinsics.checkNotNullParameter(raw, "raw");
        String trimmed = StringsKt.trim((CharSequence) raw).toString();
        if (trimmed.length() == 0) {
            return trimmed;
        }
        if (StringsKt.startsWith$default((CharSequence) trimmed, AbstractJsonLexerKt.COLON, false, 2, (Object) null) && !StringsKt.startsWith$default(trimmed, "://", false, 2, (Object) null)) {
            id = StringsKt.removePrefix(trimmed, (CharSequence) ":");
        } else {
            id = trimmed;
        }
        if (!StringsKt.startsWith$default((CharSequence) id, '~', false, 2, (Object) null) && StringsKt.contains$default((CharSequence) id, (CharSequence) "-latest", false, 2, (Object) null)) {
            return "~" + id;
        }
        return id;
    }

    public final boolean matches(String a, String b) {
        Intrinsics.checkNotNullParameter(a, "a");
        Intrinsics.checkNotNullParameter(b, "b");
        String left = normalizeId(a);
        String right = normalizeId(b);
        if (Intrinsics.areEqual(left, right)) {
            return true;
        }
        return Intrinsics.areEqual(StringsKt.trimStart(left, '~'), StringsKt.trimStart(right, '~'));
    }
}
