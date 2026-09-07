package com.varun.pocketassistant.pipeline

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext

/**
 * Serializes access to a non-thread-safe native resource (e.g. a LiteRT
 * CompiledModel). Single-flight via [Mutex]; all work is pinned to a single
 * [dispatcher] thread so the native handle is never touched concurrently.
 *
 * This is the concurrency contract that kills the SIGSEGV crash class: the
 * engine's load / preprocess / decode / close all run through
 * [withExclusiveAccess] or [close], so a shared handle is never closed while
 * another caller is still invoking it.
 */
class NativeEngineGate(
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO.limitedParallelism(1),
) {
    private val mutex = Mutex()

    /**
     * Runs [block] exclusively: waits for any in-flight work, then runs on the
     * serial [dispatcher]. Cancellation while waiting does not acquire the lock;
     * cancellation while [block] runs does not interrupt it — the native work
     * completes before the lock is released.
     */
    suspend fun <T> withExclusiveAccess(block: () -> T): T =
        mutex.withLock { withContext(dispatcher) { block() } }

    /**
     * Blocks until in-flight work completes, then runs [dispose] on the serial
     * [dispatcher]. Never disposes while a handle is in use.
     */
    fun close(dispose: () -> Unit) {
        runBlocking {
            mutex.withLock { withContext(dispatcher) { dispose() } }
        }
    }
}
