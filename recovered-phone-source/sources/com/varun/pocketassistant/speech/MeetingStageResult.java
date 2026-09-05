package com.varun.pocketassistant.speech;

import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: MeetingStage.kt */
/* JADX INFO: loaded from: classes3.dex */
@Metadata(d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\b7\u0018\u00002\u00020\u0001:\u0003\u0004\u0005\u0006B\t\b\u0004¢\u0006\u0004\b\u0002\u0010\u0003\u0082\u0001\u0003\u0007\b\t¨\u0006\n"}, d2 = {"Lcom/varun/pocketassistant/speech/MeetingStageResult;", "", "<init>", "()V", "Success", "WaitingAsr", "Failed", "Lcom/varun/pocketassistant/speech/MeetingStageResult$Failed;", "Lcom/varun/pocketassistant/speech/MeetingStageResult$Success;", "Lcom/varun/pocketassistant/speech/MeetingStageResult$WaitingAsr;", "app_debug"}, k = 1, mv = {2, 2, 0}, xi = 48)
public abstract class MeetingStageResult {
    public static final int $stable = 0;

    public /* synthetic */ MeetingStageResult(DefaultConstructorMarker defaultConstructorMarker) {
        this();
    }

    /* JADX INFO: compiled from: MeetingStage.kt */
    @Metadata(d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\bÇ\n\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003J\u0013\u0010\u0004\u001a\u00020\u00052\b\u0010\u0006\u001a\u0004\u0018\u00010\u0007HÖ\u0003J\t\u0010\b\u001a\u00020\tHÖ\u0001J\t\u0010\n\u001a\u00020\u000bHÖ\u0001¨\u0006\f"}, d2 = {"Lcom/varun/pocketassistant/speech/MeetingStageResult$Success;", "Lcom/varun/pocketassistant/speech/MeetingStageResult;", "<init>", "()V", "equals", "", "other", "", "hashCode", "", "toString", "", "app_debug"}, k = 1, mv = {2, 2, 0}, xi = 48)
    public static final /* data */ class Success extends MeetingStageResult {
        public static final int $stable = 0;
        public static final Success INSTANCE = new Success();

        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof Success)) {
                return false;
            }
            return true;
        }

        public int hashCode() {
            return 996568544;
        }

        public String toString() {
            return "Success";
        }

        private Success() {
            super(null);
        }
    }

    private MeetingStageResult() {
    }

    /* JADX INFO: compiled from: MeetingStage.kt */
    @Metadata(d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\bÇ\n\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003J\u0013\u0010\u0004\u001a\u00020\u00052\b\u0010\u0006\u001a\u0004\u0018\u00010\u0007HÖ\u0003J\t\u0010\b\u001a\u00020\tHÖ\u0001J\t\u0010\n\u001a\u00020\u000bHÖ\u0001¨\u0006\f"}, d2 = {"Lcom/varun/pocketassistant/speech/MeetingStageResult$WaitingAsr;", "Lcom/varun/pocketassistant/speech/MeetingStageResult;", "<init>", "()V", "equals", "", "other", "", "hashCode", "", "toString", "", "app_debug"}, k = 1, mv = {2, 2, 0}, xi = 48)
    public static final /* data */ class WaitingAsr extends MeetingStageResult {
        public static final int $stable = 0;
        public static final WaitingAsr INSTANCE = new WaitingAsr();

        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof WaitingAsr)) {
                return false;
            }
            return true;
        }

        public int hashCode() {
            return -1447897610;
        }

        public String toString() {
            return "WaitingAsr";
        }

        private WaitingAsr() {
            super(null);
        }
    }

    /* JADX INFO: compiled from: MeetingStage.kt */
    @Metadata(d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u000b\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\b\u0087\b\u0018\u00002\u00020\u0001B\u0019\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0005¢\u0006\u0004\b\u0006\u0010\u0007J\t\u0010\f\u001a\u00020\u0003HÆ\u0003J\t\u0010\r\u001a\u00020\u0005HÆ\u0003J\u001d\u0010\u000e\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u0005HÆ\u0001J\u0013\u0010\u000f\u001a\u00020\u00052\b\u0010\u0010\u001a\u0004\u0018\u00010\u0011HÖ\u0003J\t\u0010\u0012\u001a\u00020\u0013HÖ\u0001J\t\u0010\u0014\u001a\u00020\u0003HÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000b¨\u0006\u0015"}, d2 = {"Lcom/varun/pocketassistant/speech/MeetingStageResult$Failed;", "Lcom/varun/pocketassistant/speech/MeetingStageResult;", "message", "", "retryable", "", "<init>", "(Ljava/lang/String;Z)V", "getMessage", "()Ljava/lang/String;", "getRetryable", "()Z", "component1", "component2", "copy", "equals", "other", "", "hashCode", "", "toString", "app_debug"}, k = 1, mv = {2, 2, 0}, xi = 48)
    public static final /* data */ class Failed extends MeetingStageResult {
        public static final int $stable = 0;
        private final String message;
        private final boolean retryable;

        public static /* synthetic */ Failed copy$default(Failed failed, String str, boolean z, int i, Object obj) {
            if ((i & 1) != 0) {
                str = failed.message;
            }
            if ((i & 2) != 0) {
                z = failed.retryable;
            }
            return failed.copy(str, z);
        }

        /* JADX INFO: renamed from: component1, reason: from getter */
        public final String getMessage() {
            return this.message;
        }

        /* JADX INFO: renamed from: component2, reason: from getter */
        public final boolean getRetryable() {
            return this.retryable;
        }

        public final Failed copy(String message, boolean retryable) {
            Intrinsics.checkNotNullParameter(message, "message");
            return new Failed(message, retryable);
        }

        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof Failed)) {
                return false;
            }
            Failed failed = (Failed) other;
            return Intrinsics.areEqual(this.message, failed.message) && this.retryable == failed.retryable;
        }

        public int hashCode() {
            return (this.message.hashCode() * 31) + Boolean.hashCode(this.retryable);
        }

        public String toString() {
            return "Failed(message=" + this.message + ", retryable=" + this.retryable + ")";
        }

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public Failed(String message, boolean retryable) {
            super(null);
            Intrinsics.checkNotNullParameter(message, "message");
            this.message = message;
            this.retryable = retryable;
        }

        public /* synthetic */ Failed(String str, boolean z, int i, DefaultConstructorMarker defaultConstructorMarker) {
            this(str, (i & 2) != 0 ? true : z);
        }

        public final String getMessage() {
            return this.message;
        }

        public final boolean getRetryable() {
            return this.retryable;
        }
    }
}
