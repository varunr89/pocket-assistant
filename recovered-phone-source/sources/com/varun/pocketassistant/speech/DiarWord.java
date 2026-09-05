package com.varun.pocketassistant.speech;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: DiarizationLabels.kt */
/* JADX INFO: loaded from: classes3.dex */
@Metadata(d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0006\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u000f\n\u0002\u0010\u000b\n\u0002\b\u0004\b\u0087\b\u0018\u00002\u00020\u0001B'\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0005\u0012\u0006\u0010\u0007\u001a\u00020\b¢\u0006\u0004\b\t\u0010\nJ\t\u0010\u0012\u001a\u00020\u0003HÆ\u0003J\t\u0010\u0013\u001a\u00020\u0005HÆ\u0003J\t\u0010\u0014\u001a\u00020\u0005HÆ\u0003J\t\u0010\u0015\u001a\u00020\bHÆ\u0003J1\u0010\u0016\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00052\b\b\u0002\u0010\u0007\u001a\u00020\bHÆ\u0001J\u0013\u0010\u0017\u001a\u00020\u00182\b\u0010\u0019\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u001a\u001a\u00020\bHÖ\u0001J\t\u0010\u001b\u001a\u00020\u0003HÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000eR\u0011\u0010\u0006\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u000eR\u0011\u0010\u0007\u001a\u00020\b¢\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011¨\u0006\u001c"}, d2 = {"Lcom/varun/pocketassistant/speech/DiarWord;", "", "word", "", "start", "", "end", "speaker", "", "<init>", "(Ljava/lang/String;DDI)V", "getWord", "()Ljava/lang/String;", "getStart", "()D", "getEnd", "getSpeaker", "()I", "component1", "component2", "component3", "component4", "copy", "equals", "", "other", "hashCode", "toString", "app_debug"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final /* data */ class DiarWord {
    public static final int $stable = 0;
    private final double end;
    private final int speaker;
    private final double start;
    private final String word;

    public static /* synthetic */ DiarWord copy$default(DiarWord diarWord, String str, double d, double d2, int i, int i2, Object obj) {
        if ((i2 & 1) != 0) {
            str = diarWord.word;
        }
        if ((i2 & 2) != 0) {
            d = diarWord.start;
        }
        if ((i2 & 4) != 0) {
            d2 = diarWord.end;
        }
        if ((i2 & 8) != 0) {
            i = diarWord.speaker;
        }
        int i3 = i;
        return diarWord.copy(str, d, d2, i3);
    }

    /* JADX INFO: renamed from: component1, reason: from getter */
    public final String getWord() {
        return this.word;
    }

    /* JADX INFO: renamed from: component2, reason: from getter */
    public final double getStart() {
        return this.start;
    }

    /* JADX INFO: renamed from: component3, reason: from getter */
    public final double getEnd() {
        return this.end;
    }

    /* JADX INFO: renamed from: component4, reason: from getter */
    public final int getSpeaker() {
        return this.speaker;
    }

    public final DiarWord copy(String word, double start, double end, int speaker) {
        Intrinsics.checkNotNullParameter(word, "word");
        return new DiarWord(word, start, end, speaker);
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof DiarWord)) {
            return false;
        }
        DiarWord diarWord = (DiarWord) other;
        return Intrinsics.areEqual(this.word, diarWord.word) && Double.compare(this.start, diarWord.start) == 0 && Double.compare(this.end, diarWord.end) == 0 && this.speaker == diarWord.speaker;
    }

    public int hashCode() {
        return (((((this.word.hashCode() * 31) + Double.hashCode(this.start)) * 31) + Double.hashCode(this.end)) * 31) + Integer.hashCode(this.speaker);
    }

    public String toString() {
        return "DiarWord(word=" + this.word + ", start=" + this.start + ", end=" + this.end + ", speaker=" + this.speaker + ")";
    }

    public DiarWord(String word, double start, double end, int speaker) {
        Intrinsics.checkNotNullParameter(word, "word");
        this.word = word;
        this.start = start;
        this.end = end;
        this.speaker = speaker;
    }

    public final String getWord() {
        return this.word;
    }

    public final double getStart() {
        return this.start;
    }

    public final double getEnd() {
        return this.end;
    }

    public final int getSpeaker() {
        return this.speaker;
    }
}
