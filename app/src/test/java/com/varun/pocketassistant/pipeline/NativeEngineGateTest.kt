package com.varun.pocketassistant.pipeline

import java.util.concurrent.CountDownLatch
import java.util.concurrent.CyclicBarrier
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Engine-concurrency contract behind [ParakeetAsrEngine] (M1 increment 1):
 * the non-thread-safe native handle must never be used by two callers at once,
 * disposal must wait for in-flight native work, and cancellation must not
 * interrupt native code mid-invoke.
 */
class NativeEngineGateTest {

    @Test
    fun concurrentCallersAreSerialized() = runBlocking {
        val gate = NativeEngineGate()
        val callers = 8
        // All callers slam the gate at once; the gate must let exactly one through.
        val startBarrier = CyclicBarrier(callers)
        val inFlight = AtomicInteger(0)
        val maxInFlight = AtomicInteger(0)

        val jobs = (0 until callers).map {
            launch(Dispatchers.Default) {
                startBarrier.await()
                gate.withExclusiveAccess {
                    val now = inFlight.incrementAndGet()
                    maxInFlight.updateAndGet { max -> maxOf(max, now) }
                    Thread.sleep(20)
                    inFlight.decrementAndGet()
                }
            }
        }
        jobs.forEach { it.join() }

        assertEquals("native work must be single-flight", 1, maxInFlight.get())
        assertEquals(0, inFlight.get())
    }

    @Test
    fun closeWaitsForInFlightWork() {
        val gate = NativeEngineGate()
        val entered = CountDownLatch(1)
        val release = CountDownLatch(1)
        val disposed = AtomicInteger(0)

        val worker = Thread {
            runBlocking {
                gate.withExclusiveAccess {
                    entered.countDown()
                    release.await(5, TimeUnit.SECONDS)
                }
            }
        }
        worker.start()
        assertTrue("worker should enter the gate", entered.await(5, TimeUnit.SECONDS))

        val closer = Thread { gate.close { disposed.incrementAndGet() } }
        closer.start()

        // dispose must not run while the worker holds the gate.
        Thread.sleep(100)
        assertEquals("close must not dispose while work is in-flight", 0, disposed.get())
        assertTrue("close must block until in-flight work completes", closer.isAlive)

        release.countDown()
        worker.join(5_000)
        closer.join(5_000)

        assertFalse("worker should finish", worker.isAlive)
        assertFalse("close should finish after in-flight work completes", closer.isAlive)
        assertEquals("dispose must run exactly once, after in-flight work completes", 1, disposed.get())
    }

    @Test
    fun cancellationDoesNotInterruptInFlightNativeWork() = runBlocking {
        val gate = NativeEngineGate()
        val entered = CountDownLatch(1)
        val release = CountDownLatch(1)
        val completed = AtomicInteger(0)

        val job = launch(Dispatchers.Default) {
            gate.withExclusiveAccess {
                entered.countDown()
                release.await(5, TimeUnit.SECONDS)
                completed.incrementAndGet()
            }
        }
        assertTrue("worker should enter the gate", entered.await(5, TimeUnit.SECONDS))

        // Cancelling the caller must not unlock/dispose while native code runs:
        // the blocking body still completes, then the lock is released.
        job.cancel()
        release.countDown()
        job.join()

        assertEquals("native work must complete even when the caller is cancelled", 1, completed.get())
    }
}
