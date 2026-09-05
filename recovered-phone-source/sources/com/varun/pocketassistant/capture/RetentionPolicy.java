package com.varun.pocketassistant.capture;

import kotlin.Metadata;

/* JADX INFO: compiled from: AudioStorage.kt */
/* JADX INFO: loaded from: classes5.dex */
@Metadata(d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\t\n\u0002\b\u0002\b\u0007\u0018\u00002\u00020\u0001B\u000f\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0004\b\u0004\u0010\u0005J\u000e\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\u0007R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\t"}, d2 = {"Lcom/varun/pocketassistant/capture/RetentionPolicy;", "", "daysToKeep", "", "<init>", "(I)V", "cutoffEpochMs", "", "nowMs", "app_debug"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class RetentionPolicy {
    public static final int $stable = 0;
    private final int daysToKeep;

    public RetentionPolicy(int daysToKeep) {
        this.daysToKeep = daysToKeep;
    }

    public final long cutoffEpochMs(long nowMs) {
        return nowMs - ((((((long) this.daysToKeep) * 24) * 60) * 60) * 1000);
    }
}
