package com.varun.pocketassistant.speech;

import java.util.List;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: DiarizationLabels.kt */
/* JADX INFO: loaded from: classes3.dex */
@Metadata(d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010 \n\u0002\u0010\u000e\n\u0002\b\r\n\u0002\u0010\u000b\n\u0002\b\u0004\b\u0087\b\u0018\u00002\u00020\u0001B%\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\f\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\b0\u0007¢\u0006\u0004\b\t\u0010\nJ\t\u0010\u0011\u001a\u00020\u0003HÆ\u0003J\t\u0010\u0012\u001a\u00020\u0005HÆ\u0003J\u000f\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\b0\u0007HÆ\u0003J-\u0010\u0014\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\u000e\b\u0002\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\b0\u0007HÆ\u0001J\u0013\u0010\u0015\u001a\u00020\u00162\b\u0010\u0017\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u0018\u001a\u00020\u0003HÖ\u0001J\t\u0010\u0019\u001a\u00020\bHÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000eR\u0017\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\b0\u0007¢\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010¨\u0006\u001a"}, d2 = {"Lcom/varun/pocketassistant/speech/SpeakerCandidate;", "", "id", "", "talkMs", "", "samples", "", "", "<init>", "(IJLjava/util/List;)V", "getId", "()I", "getTalkMs", "()J", "getSamples", "()Ljava/util/List;", "component1", "component2", "component3", "copy", "equals", "", "other", "hashCode", "toString", "app_debug"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final /* data */ class SpeakerCandidate {
    public static final int $stable = 8;
    private final int id;
    private final List<String> samples;
    private final long talkMs;

    /* JADX WARN: Multi-variable type inference failed */
    public static /* synthetic */ SpeakerCandidate copy$default(SpeakerCandidate speakerCandidate, int i, long j, List list, int i2, Object obj) {
        if ((i2 & 1) != 0) {
            i = speakerCandidate.id;
        }
        if ((i2 & 2) != 0) {
            j = speakerCandidate.talkMs;
        }
        if ((i2 & 4) != 0) {
            list = speakerCandidate.samples;
        }
        return speakerCandidate.copy(i, j, list);
    }

    /* JADX INFO: renamed from: component1, reason: from getter */
    public final int getId() {
        return this.id;
    }

    /* JADX INFO: renamed from: component2, reason: from getter */
    public final long getTalkMs() {
        return this.talkMs;
    }

    public final List<String> component3() {
        return this.samples;
    }

    public final SpeakerCandidate copy(int id, long talkMs, List<String> samples) {
        Intrinsics.checkNotNullParameter(samples, "samples");
        return new SpeakerCandidate(id, talkMs, samples);
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof SpeakerCandidate)) {
            return false;
        }
        SpeakerCandidate speakerCandidate = (SpeakerCandidate) other;
        return this.id == speakerCandidate.id && this.talkMs == speakerCandidate.talkMs && Intrinsics.areEqual(this.samples, speakerCandidate.samples);
    }

    public int hashCode() {
        return (((Integer.hashCode(this.id) * 31) + Long.hashCode(this.talkMs)) * 31) + this.samples.hashCode();
    }

    public String toString() {
        return "SpeakerCandidate(id=" + this.id + ", talkMs=" + this.talkMs + ", samples=" + this.samples + ")";
    }

    public SpeakerCandidate(int id, long talkMs, List<String> samples) {
        Intrinsics.checkNotNullParameter(samples, "samples");
        this.id = id;
        this.talkMs = talkMs;
        this.samples = samples;
    }

    public final int getId() {
        return this.id;
    }

    public final long getTalkMs() {
        return this.talkMs;
    }

    public final List<String> getSamples() {
        return this.samples;
    }
}
