package com.varun.pocketassistant.data;

import kotlin.Metadata;

/* JADX INFO: compiled from: AppDatabase.kt */
/* JADX INFO: loaded from: classes9.dex */
@Metadata(d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0003\bÇ\u0002\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003R\u000e\u0010\u0004\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000¨\u0006\b"}, d2 = {"Lcom/varun/pocketassistant/data/SegmentEndReason;", "", "<init>", "()V", "POST_ROLL", "", "DURATION_CAP", "MANUAL", "app_debug"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class SegmentEndReason {
    public static final int $stable = 0;
    public static final String DURATION_CAP = "duration_cap";
    public static final SegmentEndReason INSTANCE = new SegmentEndReason();
    public static final String MANUAL = "manual";
    public static final String POST_ROLL = "post_roll";

    private SegmentEndReason() {
    }
}
