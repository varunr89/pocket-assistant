package com.varun.pocketassistant.data;

import kotlin.Metadata;
import kotlin.enums.EnumEntries;
import kotlin.enums.EnumEntriesKt;

/* JADX INFO: compiled from: AppDatabase.kt */
/* JADX INFO: loaded from: classes9.dex */
@Metadata(d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\u000b\b\u0086\u0081\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003j\u0002\b\u0004j\u0002\b\u0005j\u0002\b\u0006j\u0002\b\u0007j\u0002\b\bj\u0002\b\tj\u0002\b\nj\u0002\b\u000b¨\u0006\f"}, d2 = {"Lcom/varun/pocketassistant/data/TranscriptStatus;", "", "<init>", "(Ljava/lang/String;I)V", "PENDING", "PROCESSING", "READY", "CLEANING", "CLEANED", "FAILED", "SKIPPED_SILENCE", "CLEAN_FAILED", "app_debug"}, k = 1, mv = {2, 2, 0}, xi = 48)
public enum TranscriptStatus {
    PENDING,
    PROCESSING,
    READY,
    CLEANING,
    CLEANED,
    FAILED,
    SKIPPED_SILENCE,
    CLEAN_FAILED;

    private static final /* synthetic */ EnumEntries $ENTRIES = EnumEntriesKt.enumEntries($VALUES);

    public static EnumEntries<TranscriptStatus> getEntries() {
        return $ENTRIES;
    }
}
