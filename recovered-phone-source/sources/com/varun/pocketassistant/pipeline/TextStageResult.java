package com.varun.pocketassistant.pipeline;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: Providers.kt */
/* JADX INFO: loaded from: classes8.dex */
@Metadata(d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\n\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\b\u0087\b\u0018\u00002\u00020\u0001B\u0017\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003¢\u0006\u0004\b\u0005\u0010\u0006J\t\u0010\n\u001a\u00020\u0003HÆ\u0003J\t\u0010\u000b\u001a\u00020\u0003HÆ\u0003J\u001d\u0010\f\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u0003HÆ\u0001J\u0013\u0010\r\u001a\u00020\u000e2\b\u0010\u000f\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u0010\u001a\u00020\u0011HÖ\u0001J\t\u0010\u0012\u001a\u00020\u0003HÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u0011\u0010\u0004\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\t\u0010\b¨\u0006\u0013"}, d2 = {"Lcom/varun/pocketassistant/pipeline/TextStageResult;", "", "text", "", "providerId", "<init>", "(Ljava/lang/String;Ljava/lang/String;)V", "getText", "()Ljava/lang/String;", "getProviderId", "component1", "component2", "copy", "equals", "", "other", "hashCode", "", "toString", "app_debug"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final /* data */ class TextStageResult {
    public static final int $stable = 0;
    private final String providerId;
    private final String text;

    public static /* synthetic */ TextStageResult copy$default(TextStageResult textStageResult, String str, String str2, int i, Object obj) {
        if ((i & 1) != 0) {
            str = textStageResult.text;
        }
        if ((i & 2) != 0) {
            str2 = textStageResult.providerId;
        }
        return textStageResult.copy(str, str2);
    }

    /* JADX INFO: renamed from: component1, reason: from getter */
    public final String getText() {
        return this.text;
    }

    /* JADX INFO: renamed from: component2, reason: from getter */
    public final String getProviderId() {
        return this.providerId;
    }

    public final TextStageResult copy(String text, String providerId) {
        Intrinsics.checkNotNullParameter(text, "text");
        Intrinsics.checkNotNullParameter(providerId, "providerId");
        return new TextStageResult(text, providerId);
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof TextStageResult)) {
            return false;
        }
        TextStageResult textStageResult = (TextStageResult) other;
        return Intrinsics.areEqual(this.text, textStageResult.text) && Intrinsics.areEqual(this.providerId, textStageResult.providerId);
    }

    public int hashCode() {
        return (this.text.hashCode() * 31) + this.providerId.hashCode();
    }

    public String toString() {
        return "TextStageResult(text=" + this.text + ", providerId=" + this.providerId + ")";
    }

    public TextStageResult(String text, String providerId) {
        Intrinsics.checkNotNullParameter(text, "text");
        Intrinsics.checkNotNullParameter(providerId, "providerId");
        this.text = text;
        this.providerId = providerId;
    }

    public final String getText() {
        return this.text;
    }

    public final String getProviderId() {
        return this.providerId;
    }
}
