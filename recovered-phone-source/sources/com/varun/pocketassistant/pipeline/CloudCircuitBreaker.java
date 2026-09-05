package com.varun.pocketassistant.pipeline;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;

/* JADX INFO: compiled from: CloudCircuitBreaker.kt */
/* JADX INFO: loaded from: classes8.dex */
@Metadata(d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\b\u0007\u0018\u00002\u00020\u0001B\u001b\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0005¢\u0006\u0004\b\u0006\u0010\u0007J\u0006\u0010\f\u001a\u00020\rJ\u0006\u0010\u000e\u001a\u00020\u000fJ\u0006\u0010\u0010\u001a\u00020\u000fR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u0011"}, d2 = {"Lcom/varun/pocketassistant/pipeline/CloudCircuitBreaker;", "", "failureThreshold", "", "cooldownMs", "", "<init>", "(IJ)V", "consecutiveFailures", "Ljava/util/concurrent/atomic/AtomicInteger;", "unhealthyUntilMs", "Ljava/util/concurrent/atomic/AtomicLong;", "isHealthy", "", "recordSuccess", "", "recordFailure", "app_debug"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class CloudCircuitBreaker {
    public static final int $stable = 8;
    private final AtomicInteger consecutiveFailures;
    private final long cooldownMs;
    private final int failureThreshold;
    private final AtomicLong unhealthyUntilMs;

    public CloudCircuitBreaker() {
        this(0, 0L, 3, null);
    }

    public CloudCircuitBreaker(int failureThreshold, long cooldownMs) {
        this.failureThreshold = failureThreshold;
        this.cooldownMs = cooldownMs;
        this.consecutiveFailures = new AtomicInteger(0);
        this.unhealthyUntilMs = new AtomicLong(0L);
    }

    public /* synthetic */ CloudCircuitBreaker(int i, long j, int i2, DefaultConstructorMarker defaultConstructorMarker) {
        this((i2 & 1) != 0 ? 3 : i, (i2 & 2) != 0 ? PipelineTelemetry.ASR_HTTP_WRITE_TIMEOUT_MS : j);
    }

    public final boolean isHealthy() {
        return System.currentTimeMillis() >= this.unhealthyUntilMs.get();
    }

    public final void recordSuccess() {
        this.consecutiveFailures.set(0);
        this.unhealthyUntilMs.set(0L);
    }

    public final void recordFailure() {
        int n = this.consecutiveFailures.incrementAndGet();
        if (n >= this.failureThreshold) {
            this.unhealthyUntilMs.set(System.currentTimeMillis() + this.cooldownMs);
            this.consecutiveFailures.set(0);
        }
    }
}
