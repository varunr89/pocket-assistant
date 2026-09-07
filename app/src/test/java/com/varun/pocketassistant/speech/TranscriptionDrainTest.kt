package com.varun.pocketassistant.speech

import com.varun.pocketassistant.data.TranscriptStatus
import java.io.File
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Foreground gating + catch-up queue drain logic (M1 2026-09-06 option A),
 * with the recognizer boundary mocked so everything runs on the host JVM.
 * Production engine/queue/foreground wiring is exercised on-device.
 */
class TranscriptionDrainTest {

    // ---- fakes ----

    private class FakeGate : ForegroundGate {
        var foreground = true
        override fun isForeground(): Boolean = foreground
    }

    private data class Outcome(val text: String? = null, val failure: Throwable? = null)

    private class FakeEngine : ForegroundAsrEngine {
        override val id: String = "fake_engine"
        val outcomes = ArrayDeque<Outcome>()
        val calls = mutableListOf<String>()
        var onTranscribe: (() -> Unit)? = null

        override suspend fun transcribe(wav: File, localeTag: String): String {
            calls += wav.name
            onTranscribe?.invoke()
            val outcome = outcomes.removeFirstOrNull() ?: return ""
            outcome.failure?.let { throw it }
            return outcome.text.orEmpty()
        }
    }

    private class FakeQueue : CatchUpQueue {
        private class Entry(var status: TranscriptStatus, var text: String? = null) {
            var provider: String? = null
            var error: String? = null
        }

        val entries = LinkedHashMap<String, Entry>()
        val claimCalls = mutableListOf<String>()

        fun add(id: String, status: TranscriptStatus = TranscriptStatus.PENDING) {
            entries[id] = Entry(status)
        }

        override suspend fun pending(limit: Int): List<PendingSegment> =
            entries.filterValues { it.status == TranscriptStatus.PENDING }
                .keys.map { PendingSegment(id = it, filePath = "$it.wav", startedAtMs = 0, durationMs = 5_000) }

        override suspend fun claim(segmentId: String): Boolean {
            claimCalls += segmentId
            val entry = entries[segmentId] ?: return false
            if (entry.status != TranscriptStatus.PENDING) return false
            entry.status = TranscriptStatus.PROCESSING
            return true
        }

        override suspend fun markReady(segmentId: String, text: String, providerId: String) {
            val entry = entries.getValue(segmentId)
            entry.status = TranscriptStatus.READY
            entry.text = text
            entry.provider = providerId
        }

        override suspend fun markSkippedSilence(segmentId: String) {
            entries.getValue(segmentId).status = TranscriptStatus.SKIPPED_SILENCE
        }

        override suspend fun markFailed(segmentId: String, error: String) {
            val entry = entries.getValue(segmentId)
            entry.status = TranscriptStatus.FAILED
            entry.error = error
        }

        override suspend fun markWaiting(segmentId: String) {
            entries.getValue(segmentId).status = TranscriptStatus.PENDING
        }

        fun status(id: String): TranscriptStatus = entries.getValue(id).status
        fun text(id: String): String? = entries.getValue(id).text
        fun provider(id: String): String? = entries.getValue(id).provider
        fun error(id: String): String? = entries.getValue(id).error
    }

    private class Fixture {
        val engine = FakeEngine()
        val queue = FakeQueue()
        val gate = FakeGate()
        var enabled = true
        val sleeps = mutableListOf<Long>()
        var dayEpoch = 7L
        val snapshots = mutableListOf<DrainDiagnostics>()

        fun drain(): TranscriptionDrain = TranscriptionDrain(
            engine = engine,
            queue = queue,
            gate = gate,
            enabled = { enabled },
            localeTag = { "en-US" },
            sleeper = { sleeps += it },
            dayEpoch = { dayEpoch },
            diagnosticsListener = { snapshots += it },
        )
    }

    private fun fixture(): Fixture = Fixture()

    // ---- gating ----

    @Test
    fun notForegroundIsANoOpAndWaits() = runBlocking {
        val f = fixture()
        f.gate.foreground = false
        f.queue.add("s1")

        val result = f.drain().drainOnce()

        assertTrue(result.notForeground)
        assertEquals(0, result.settled)
        assertTrue(f.engine.calls.isEmpty())
        assertTrue(f.queue.claimCalls.isEmpty())
        assertEquals(TranscriptStatus.PENDING, f.queue.status("s1"))
    }

    @Test
    fun disabledByConfigIsANoOp() = runBlocking {
        val f = fixture()
        f.enabled = false
        f.queue.add("s1")

        val result = f.drain().drainOnce()

        assertTrue(result.disabled)
        assertEquals(0, result.settled)
        assertTrue(f.engine.calls.isEmpty())
    }

    @Test
    fun quietWhenQueueIsEmpty() = runBlocking {
        val f = fixture()

        val result = f.drain().drainOnce()

        assertTrue(result.quiet)
        assertEquals(0, result.settled)
    }

    // ---- happy path / persistence ----

    @Test
    fun drainsPendingInOrderAndPersistsReadyTranscripts() = runBlocking {
        val f = fixture()
        f.queue.add("s1")
        f.queue.add("s2")
        f.engine.outcomes += Outcome(text = "hello there")
        f.engine.outcomes += Outcome(text = "general kenobi")

        val result = f.drain().drainOnce()

        assertEquals(2, result.processed)
        assertEquals(0, result.failed)
        assertEquals(listOf("s1.wav", "s2.wav"), f.engine.calls)
        assertEquals(TranscriptStatus.READY, f.queue.status("s1"))
        assertEquals(TranscriptStatus.READY, f.queue.status("s2"))
        assertEquals("hello there", f.queue.text("s1"))
        assertEquals("fake_engine", f.queue.provider("s1"))
        assertEquals(2L, f.snapshots.last().processed)
    }

    @Test
    fun blankTranscriptBecomesSkippedSilence() = runBlocking {
        val f = fixture()
        f.queue.add("s1")
        f.engine.outcomes += Outcome(text = "   ")

        val result = f.drain().drainOnce()

        assertEquals(0, result.processed)
        assertEquals(1, result.skippedSilence)
        assertEquals(TranscriptStatus.SKIPPED_SILENCE, f.queue.status("s1"))
        assertEquals(1L, f.snapshots.last().skippedSilence)
    }

    // ---- typed failures ----

    @Test
    fun unavailableFailureMarksFailedWithMessage() = runBlocking {
        val f = fixture()
        f.queue.add("s1")
        f.engine.outcomes += Outcome(failure = ForegroundAsrFailure.Unavailable("model missing"))

        val result = f.drain().drainOnce()

        assertEquals(1, result.failed)
        assertEquals(TranscriptStatus.FAILED, f.queue.status("s1"))
        assertEquals("model missing", f.queue.error("s1"))
        assertEquals(1L, f.snapshots.last().failed)
    }

    @Test
    fun invalidAudioFailureIsVisibleAndNeverReachesTheModel() = runBlocking {
        val f = fixture()
        f.queue.add("s1")
        f.engine.outcomes += Outcome(failure = ForegroundAsrFailure.InvalidAudio("bad wav"))

        val result = f.drain().drainOnce()

        assertEquals(1, result.failed)
        assertEquals(TranscriptStatus.FAILED, f.queue.status("s1"))
        assertEquals("bad wav", f.queue.error("s1"))
    }

    @Test
    fun busyBacksOffExponentiallyAndSegmentWaits() = runBlocking {
        val f = fixture()
        f.queue.add("s1")
        f.queue.add("s2")
        f.engine.outcomes += Outcome(failure = ForegroundAsrFailure.Busy("quota window"))
        f.engine.outcomes += Outcome(text = "second works")

        val result = f.drain().drainOnce()

        assertEquals(listOf(2_000L), f.sleeps)
        assertEquals(1, result.waiting)
        assertEquals(1, result.processed)
        assertEquals(TranscriptStatus.PENDING, f.queue.status("s1"))
        assertEquals(TranscriptStatus.READY, f.queue.status("s2"))
        assertEquals(1L, f.snapshots.last().busyRetries)
    }

    @Test
    fun busyBudgetExhaustionStopsThePass() = runBlocking {
        val f = fixture()
        repeat(7) { f.queue.add("s$it") }
        repeat(7) {
            f.engine.outcomes += Outcome(failure = ForegroundAsrFailure.Busy("quota window"))
        }

        val result = f.drain().drainOnce()

        assertEquals(listOf(2_000L, 4_000L, 8_000L, 16_000L, 30_000L), f.sleeps)
        assertEquals(5, result.waiting)
        assertEquals(0, result.failed)
        // The 6th and 7th segments were never claimed.
        assertEquals(5, f.queue.claimCalls.size)
        assertEquals(TranscriptStatus.PENDING, f.queue.status("s5"))
        assertEquals(TranscriptStatus.PENDING, f.queue.status("s6"))
    }

    @Test
    fun dailyQuotaPausesDrainUntilTheNextDay() = runBlocking {
        val f = fixture()
        f.queue.add("s1")
        f.queue.add("s2")
        f.engine.outcomes += Outcome(failure = ForegroundAsrFailure.DailyQuotaExceeded("daily budget"))
        f.engine.outcomes += Outcome(text = "after midnight")

        val drain = f.drain()
        val first = drain.drainOnce()

        assertEquals(1, first.waiting)
        assertTrue(first.quotaPaused)
        assertEquals(TranscriptStatus.PENDING, f.queue.status("s1"))
        assertEquals(7L, f.snapshots.last().quotaPausedDayEpoch)

        // Same day: an immediate no-op, no engine call.
        val second = drain.drainOnce()
        assertTrue(second.quotaPaused)
        assertEquals(1, f.engine.calls.size)

        // Next day: drain resumes. s1 was rolled back to PENDING by
        // markWaiting, so it drains first with the next outcome; s2 has no
        // outcome left, so the fake engine returns blank -> SKIPPED_SILENCE.
        f.dayEpoch = 8
        val third = drain.drainOnce()
        assertEquals(1, third.processed)
        assertEquals(1, third.skippedSilence)
        assertEquals(TranscriptStatus.READY, f.queue.status("s1"))
        assertEquals("after midnight", f.queue.text("s1"))
        assertEquals(TranscriptStatus.SKIPPED_SILENCE, f.queue.status("s2"))
        assertEquals(3, f.engine.calls.size)
    }

    @Test
    fun backgroundBlockedIsExpectedStateNotAFailure() = runBlocking {
        val f = fixture()
        f.queue.add("s1")
        f.engine.outcomes += Outcome(failure = ForegroundAsrFailure.BackgroundUseBlocked("bg"))

        val result = f.drain().drainOnce()

        assertTrue(result.notForeground)
        assertEquals(1, result.waiting)
        assertEquals(0, result.failed)
        assertEquals(TranscriptStatus.PENDING, f.queue.status("s1"))
        assertEquals(1L, f.snapshots.last().backgroundBlockedHits)
        assertEquals(0L, f.snapshots.last().failed)
    }

    @Test
    fun gateFlipMidBatchStopsBeforeTheNextClaim() = runBlocking {
        val f = fixture()
        f.queue.add("s1")
        f.queue.add("s2")
        f.engine.outcomes += Outcome(text = "first")
        f.engine.outcomes += Outcome(text = "second")
        f.engine.onTranscribe = { f.gate.foreground = false }

        val result = f.drain().drainOnce()

        assertEquals(1, result.processed)
        assertTrue(result.notForeground)
        assertEquals(TranscriptStatus.READY, f.queue.status("s1"))
        assertEquals(TranscriptStatus.PENDING, f.queue.status("s2"))
        assertEquals(listOf("s1.wav"), f.engine.calls)
    }

    // ---- cancellation + unexpected errors ----

    @Test
    fun cancellationPropagatesAndLeavesTheClaimForRecovery() {
        val f = fixture()
        f.queue.add("s1")
        f.engine.outcomes += Outcome(failure = CancellationException("service stopped"))

        expectThrowable<CancellationException> {
            runBlocking { f.drain().drainOnce() }
        }
        // No failure recorded; the claim stays PROCESSING so the next pass's
        // pending() recovery folds it back to PENDING.
        assertEquals(TranscriptStatus.PROCESSING, f.queue.status("s1"))
        assertNull(f.queue.error("s1"))
        assertEquals(0L, f.snapshots.last().failed)
    }

    @Test
    fun unexpectedThrowableBecomesAVisibleFailure() = runBlocking {
        val f = fixture()
        f.queue.add("s1")
        f.engine.outcomes += Outcome(failure = RuntimeException("boom"))

        val result = f.drain().drainOnce()

        assertEquals(1, result.failed)
        assertEquals(TranscriptStatus.FAILED, f.queue.status("s1"))
        assertEquals("unexpected: boom", f.queue.error("s1"))
    }

    @Test
    fun alreadyClaimedSegmentsAreSkipped() = runBlocking {
        val f = fixture()
        f.queue.add("s1", status = TranscriptStatus.PROCESSING)
        f.queue.add("s2")
        f.engine.outcomes += Outcome(text = "only s2")

        val result = f.drain().drainOnce()

        assertEquals(1, result.processed)
        assertEquals(listOf("s2"), f.queue.claimCalls)
        assertEquals(TranscriptStatus.READY, f.queue.status("s2"))
    }

    @Test
    fun backoffScheduleCapsAtThirtySeconds() {
        assertEquals(2_000L, TranscriptionDrain.backoffFor(1))
        assertEquals(4_000L, TranscriptionDrain.backoffFor(2))
        assertEquals(8_000L, TranscriptionDrain.backoffFor(3))
        assertEquals(16_000L, TranscriptionDrain.backoffFor(4))
        assertEquals(30_000L, TranscriptionDrain.backoffFor(5))
        assertEquals(30_000L, TranscriptionDrain.backoffFor(42))
    }

    @Test
    fun keepPollingSignalsAreExplicit() {
        assertTrue(DrainResult(processed = 1).shouldKeepPolling())
        assertTrue(DrainResult(waiting = 1).shouldKeepPolling())
        assertFalse(DrainResult(quiet = true).shouldKeepPolling())
        assertFalse(DrainResult(notForeground = true).shouldKeepPolling())
        assertFalse(DrainResult(disabled = true).shouldKeepPolling())
        assertFalse(DrainResult(quotaPaused = true).shouldKeepPolling())
    }

    /**
     * Local stand-in for JUnit 4.13's assertThrows: the unit-test classpath is
     * shadowed by libs/jlibrosa-...-jar-with-dependencies.jar, which bundles a
     * 2011-era JUnit whose Assert has no assertThrows.
     */
    private inline fun <reified T : Throwable> expectThrowable(block: () -> Unit): T {
        try {
            block()
        } catch (t: Throwable) {
            if (t is T) return t
            throw AssertionError("expected ${T::class.java.simpleName}, got ${t::class.java.name}", t)
        }
        throw AssertionError("expected ${T::class.java.simpleName}, nothing was thrown")
    }
}