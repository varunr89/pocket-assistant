package com.varun.pocketassistant

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.varun.pocketassistant.pipeline.PipelineConfig
import com.varun.pocketassistant.pipeline.PipelineSettings
import com.varun.pocketassistant.pipeline.ProviderMode
import com.varun.pocketassistant.ui.PocketAssistantRoot
import com.varun.pocketassistant.ui.theme.PocketAssistantTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        applyDebugPipelineExtras()
        enableEdgeToEdge()
        setContent {
            PocketAssistantTheme {
                PocketAssistantRoot()
            }
        }
    }

    override fun onNewIntent(intent: android.content.Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        applyDebugPipelineExtras()
    }

    /** Debug-only: adb am start … --es pipeline_api_key … to configure OpenRouter without UI. */
    private fun applyDebugPipelineExtras() {
        if (!BuildConfig.DEBUG) return
        val key = intent?.getStringExtra(EXTRA_API_KEY)?.trim().orEmpty()
        if (key.isEmpty()) return
        val app = application as PocketAssistantApp
        val config = app.container.pipelineConfig
        val cur = config.load()
        val asrMode = intent.getStringExtra(EXTRA_ASR_MODE)
            ?.let { PipelineConfig.parseMode(it) }
            ?: ProviderMode.PREFER_CLOUD
        val cleanupMode = intent.getStringExtra(EXTRA_CLEANUP_MODE)
            ?.let { PipelineConfig.parseMode(it) }
            ?: ProviderMode.PREFER_CLOUD
        val summaryMode = intent.getStringExtra(EXTRA_SUMMARY_MODE)
            ?.let { PipelineConfig.parseMode(it) }
            ?: cleanupMode
        val allowFallback = when {
            intent.hasExtra(EXTRA_ALLOW_FALLBACK) ->
                intent.getBooleanExtra(EXTRA_ALLOW_FALLBACK, false)
            PipelineConfig.legacyImpliesNoFallback(intent.getStringExtra(EXTRA_ASR_MODE)) ||
                PipelineConfig.legacyImpliesNoFallback(intent.getStringExtra(EXTRA_CLEANUP_MODE)) ->
                false
            else -> cur.allowFallback
        }
        val next = cur.copy(
            cloudApiKey = key,
            cloudBaseUrl = PipelineSettings.DEFAULT_BASE_URL,
            cloudAsrModel = intent.getStringExtra(EXTRA_ASR_MODEL)?.trim()
                ?.ifBlank { null }
                ?: cur.cloudAsrModel.ifBlank { PipelineSettings.DEFAULT_ASR_MODEL },
            cloudCleanupModel = (
                intent.getStringExtra(EXTRA_CLEANUP_MODEL)
                    ?: intent.getStringExtra(EXTRA_CHAT_MODEL)
                )?.trim()?.ifBlank { null }
                ?: cur.cloudCleanupModel.ifBlank { PipelineSettings.DEFAULT_CLEANUP_MODEL },
            cloudSummaryModel = (
                intent.getStringExtra(EXTRA_SUMMARY_MODEL)
                    ?: intent.getStringExtra(EXTRA_CHAT_MODEL)
                )?.trim()?.ifBlank { null }
                ?: cur.cloudSummaryModel.ifBlank { PipelineSettings.DEFAULT_SUMMARY_MODEL },
            reasoningEffort = intent.getStringExtra(EXTRA_REASONING)?.trim().orEmpty()
                .ifBlank { cur.reasoningEffort },
            asrMode = asrMode,
            cleanupMode = cleanupMode,
            summaryMode = summaryMode,
            actionsMode = summaryMode,
            allowFallback = allowFallback,
            sttLanguage = intent.getStringExtra(EXTRA_STT_LANG)?.trim().orEmpty()
                .ifBlank { cur.sttLanguage.ifBlank { "en" } },
        )
        config.save(next)
        Log.i(
            TAG,
            "Debug pipeline configured: asr=${next.cloudAsrModel} " +
                "cleanup=${next.cloudCleanupModel} summary=${next.cloudSummaryModel} " +
                "modes=${next.asrMode}/${next.cleanupMode}/${next.summaryMode} " +
                "fallback=${next.allowFallback}",
        )

        val scope = kotlinx.coroutines.CoroutineScope(Dispatchers.IO)
        if (intent.getBooleanExtra(EXTRA_REQUEUE_ASR, false)) {
            scope.launch {
                app.container.sessionRepository.requeueAllForAsr()
                Log.i(TAG, "Debug: requeued ASR")
            }
        }
        if (intent.getBooleanExtra(EXTRA_REQUEUE_MEETINGS, false)) {
            scope.launch {
                val ids = app.container.meetingRepository.getPendingCleanup(50).map { it.id }
                app.container.pipelineScheduler.requeuePendingMeetings(ids)
                Log.i(TAG, "Debug: requeued meetings")
            }
        }
    }

    companion object {
        private const val TAG = "MainActivity"
        const val EXTRA_API_KEY = "pipeline_api_key"
        const val EXTRA_BASE_URL = "pipeline_base_url"
        const val EXTRA_ASR_MODEL = "pipeline_asr_model"
        const val EXTRA_CHAT_MODEL = "pipeline_chat_model"
        const val EXTRA_CLEANUP_MODEL = "pipeline_cleanup_model"
        const val EXTRA_SUMMARY_MODEL = "pipeline_summary_model"
        const val EXTRA_REASONING = "pipeline_reasoning"
        const val EXTRA_ASR_MODE = "pipeline_asr_mode"
        const val EXTRA_CLEANUP_MODE = "pipeline_cleanup_mode"
        const val EXTRA_SUMMARY_MODE = "pipeline_summary_mode"
        const val EXTRA_ALLOW_FALLBACK = "pipeline_allow_fallback"
        const val EXTRA_STT_LANG = "pipeline_stt_lang"
        const val EXTRA_REQUEUE_ASR = "pipeline_requeue_asr"
        const val EXTRA_REQUEUE_MEETINGS = "pipeline_requeue_meetings"
    }
}
