package com.varun.pocketassistant.speech

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Process-wide surface for drain diagnostics (no-silent-failure SLO): the
 * service publishes every snapshot here, and any UI/diagnostics screen can
 * observe it without coupling to the service.
 */
object TranscriptionDrainHub {
    private val _diagnostics = MutableStateFlow(DrainDiagnostics())
    val diagnostics: StateFlow<DrainDiagnostics> = _diagnostics.asStateFlow()

    fun publish(d: DrainDiagnostics) {
        _diagnostics.value = d
    }

    fun reset() {
        _diagnostics.value = DrainDiagnostics()
    }
}