package com.varun.pocketassistant.pipeline

import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicLong

/**
 * Marks cloud unhealthy for [cooldownMs] after [failureThreshold] consecutive transport failures.
 * Transient 502s should not divert to slow local Gemma until retries are exhausted and the
 * breaker opens.
 */
class CloudCircuitBreaker(
    private val failureThreshold: Int = 3,
    private val cooldownMs: Long = 60_000L,
) {
    private val consecutiveFailures = AtomicInteger(0)
    private val unhealthyUntilMs = AtomicLong(0L)

    fun isHealthy(): Boolean = System.currentTimeMillis() >= unhealthyUntilMs.get()

    fun recordSuccess() {
        consecutiveFailures.set(0)
        unhealthyUntilMs.set(0L)
    }

    fun recordFailure() {
        val n = consecutiveFailures.incrementAndGet()
        if (n >= failureThreshold) {
            unhealthyUntilMs.set(System.currentTimeMillis() + cooldownMs)
            consecutiveFailures.set(0)
        }
    }
}
