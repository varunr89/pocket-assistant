package com.varun.pocketassistant.ui;

import com.varun.pocketassistant.pipeline.work.AsrWorker;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: HomeViewModel.kt */
/* JADX INFO: loaded from: classes4.dex */
@Metadata(d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u000b\n\u0002\u0010\b\n\u0002\b\u0002\b\u0087\b\u0018\u00002\u00020\u0001B\u001d\u0012\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0005¢\u0006\u0004\b\u0006\u0010\u0007J\u000b\u0010\u000b\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\t\u0010\f\u001a\u00020\u0005HÆ\u0003J\u001f\u0010\r\u001a\u00020\u00002\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u0005HÆ\u0001J\u0013\u0010\u000e\u001a\u00020\u00052\b\u0010\u000f\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u0010\u001a\u00020\u0011HÖ\u0001J\t\u0010\u0012\u001a\u00020\u0003HÖ\u0001R\u0013\u0010\u0002\u001a\u0004\u0018\u00010\u0003¢\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u0004\u0010\n¨\u0006\u0013"}, d2 = {"Lcom/varun/pocketassistant/ui/PlaybackState;", "", AsrWorker.KEY_SEGMENT_ID, "", "isPlaying", "", "<init>", "(Ljava/lang/String;Z)V", "getSegmentId", "()Ljava/lang/String;", "()Z", "component1", "component2", "copy", "equals", "other", "hashCode", "", "toString", "app_debug"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final /* data */ class PlaybackState {
    public static final int $stable = 0;
    private final boolean isPlaying;
    private final String segmentId;

    /* JADX WARN: Multi-variable type inference failed */
    public PlaybackState() {
        this(null, false, 3, 0 == true ? 1 : 0);
    }

    public static /* synthetic */ PlaybackState copy$default(PlaybackState playbackState, String str, boolean z, int i, Object obj) {
        if ((i & 1) != 0) {
            str = playbackState.segmentId;
        }
        if ((i & 2) != 0) {
            z = playbackState.isPlaying;
        }
        return playbackState.copy(str, z);
    }

    /* JADX INFO: renamed from: component1, reason: from getter */
    public final String getSegmentId() {
        return this.segmentId;
    }

    /* JADX INFO: renamed from: component2, reason: from getter */
    public final boolean getIsPlaying() {
        return this.isPlaying;
    }

    public final PlaybackState copy(String segmentId, boolean isPlaying) {
        return new PlaybackState(segmentId, isPlaying);
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof PlaybackState)) {
            return false;
        }
        PlaybackState playbackState = (PlaybackState) other;
        return Intrinsics.areEqual(this.segmentId, playbackState.segmentId) && this.isPlaying == playbackState.isPlaying;
    }

    public int hashCode() {
        return ((this.segmentId == null ? 0 : this.segmentId.hashCode()) * 31) + Boolean.hashCode(this.isPlaying);
    }

    public String toString() {
        return "PlaybackState(segmentId=" + this.segmentId + ", isPlaying=" + this.isPlaying + ")";
    }

    public PlaybackState(String segmentId, boolean isPlaying) {
        this.segmentId = segmentId;
        this.isPlaying = isPlaying;
    }

    public /* synthetic */ PlaybackState(String str, boolean z, int i, DefaultConstructorMarker defaultConstructorMarker) {
        this((i & 1) != 0 ? null : str, (i & 2) != 0 ? false : z);
    }

    public final String getSegmentId() {
        return this.segmentId;
    }

    public final boolean isPlaying() {
        return this.isPlaying;
    }
}
