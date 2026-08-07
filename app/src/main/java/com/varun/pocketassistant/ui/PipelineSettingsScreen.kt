package com.varun.pocketassistant.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.PowerManager
import android.provider.Settings
import com.varun.pocketassistant.PocketAssistantApp
import com.varun.pocketassistant.pipeline.CleanupPrompts
import com.varun.pocketassistant.pipeline.OpenRouterModelInfo
import com.varun.pocketassistant.pipeline.OpenRouterModels
import com.varun.pocketassistant.pipeline.OpenRouterModelsClient
import com.varun.pocketassistant.pipeline.PipelineSettings
import com.varun.pocketassistant.pipeline.ProviderMode
import com.varun.pocketassistant.pipeline.ReasoningEffort
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PipelineSettingsScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val app = context.applicationContext as PocketAssistantApp
    val config = app.container.pipelineConfig
    val modelsClient = remember { OpenRouterModelsClient(config) }
    val scope = rememberCoroutineScope()

    var settings by remember { mutableStateOf(config.load()) }
    var status by remember { mutableStateOf<String?>(null) }
    var loadingModels by remember { mutableStateOf(false) }
    var sttModels by remember { mutableStateOf<List<OpenRouterModelInfo>>(emptyList()) }
    var chatModels by remember { mutableStateOf<List<OpenRouterModelInfo>>(emptyList()) }

    val selectedSummaryMeta = chatModels.firstOrNull {
        OpenRouterModels.matches(it.id, settings.cloudSummaryModel)
    }
    val effortOptions = remember(selectedSummaryMeta, settings.cloudSummaryModel) {
        val supported = selectedSummaryMeta?.supportedEfforts.orEmpty()
        if (supported.isNotEmpty()) {
            listOf(ReasoningEffort.DEFAULT) +
                ReasoningEffort.entries.filter { it.apiValue in supported }
        } else {
            ReasoningEffort.selectable
        }
    }

    fun refreshModels(silent: Boolean = false) {
        val key = settings.cloudApiKey.trim()
        // Always OpenRouter — ignore any stale saved/typed base URL.
        val base = PipelineSettings.DEFAULT_BASE_URL
        if (key.isBlank()) {
            if (!silent) status = "Set an OpenRouter API key to load models"
            return
        }
        // Keep in-memory settings on the OpenRouter endpoint.
        if (settings.cloudBaseUrl != base) {
            settings = settings.copy(cloudBaseUrl = base)
        }
        scope.launch {
            loadingModels = true
            if (!silent) status = "Fetching OpenRouter models…"
            // Use draft key from the form — do not require Save first.
            val sttResult = withContext(Dispatchers.IO) {
                runCatching { modelsClient.listTranscriptionModels(apiKey = key, baseUrl = base) }
            }
            val chatResult = withContext(Dispatchers.IO) {
                runCatching { modelsClient.listChatModels(apiKey = key, baseUrl = base) }
            }
            loadingModels = false

            val stt = sttResult.getOrDefault(emptyList())
            val chat = chatResult.getOrDefault(emptyList())
            sttModels = stt
            chatModels = chat

            val errors = buildList {
                sttResult.exceptionOrNull()?.message?.let { add("STT: $it") }
                chatResult.exceptionOrNull()?.message?.let { add("Chat: $it") }
            }
            if (stt.isEmpty() && chat.isEmpty()) {
                status = errors.firstOrNull()?.let { "Model fetch failed: $it" }
                    ?: "No models returned — check your OpenRouter API key"
                return@launch
            }

            val latest = settings
            val next = latest.copy(
                cloudBaseUrl = base,
                cloudAsrModel = ensureModelId(latest.cloudAsrModel, stt, PipelineSettings.DEFAULT_ASR_MODEL),
                cloudCleanupModel = ensureModelId(
                    latest.cloudCleanupModel,
                    chat,
                    PipelineSettings.DEFAULT_CLEANUP_MODEL,
                ),
                cloudSummaryModel = ensureModelId(
                    latest.cloudSummaryModel,
                    chat,
                    PipelineSettings.DEFAULT_SUMMARY_MODEL,
                ),
            )
            // Persist so cloudConfigured() is true for ASR/cleanup workers.
            config.save(next)
            settings = config.load()
            status = buildString {
                append("Loaded ${stt.size} STT + ${chat.size} text models from OpenRouter (saved)")
                if (errors.isNotEmpty()) append(" (${errors.joinToString("; ")})")
            }
        }
    }

    // Auto-load once when opening settings if a key is already saved.
    LaunchedEffect(Unit) {
        if (settings.cloudApiKey.isNotBlank() && sttModels.isEmpty() && chatModels.isEmpty()) {
            refreshModels(silent = true)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Pipeline") },
                navigationIcon = {
                    TextButton(onClick = onBack) { Text("Back") }
                },
            )
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(20.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(
                "Cloud (OpenRouter): silence-trim only (no speedup), ~10 min capture rolls when " +
                    "cloud ASR is preferred, and long files are chunked at ~10 min. " +
                    "Local Parakeet still uses trim + 1.35×. Models load from OpenRouter.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            Text("Routing", style = MaterialTheme.typography.titleLarge)
            Text(
                "Pick Cloud or Local per stage. Fallback (below) is one switch for all stages.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Text("Transcription", style = MaterialTheme.typography.titleMedium)
            ModeRow(settings.asrMode) { settings = settings.copy(asrMode = it) }

            Text("Cleanup", style = MaterialTheme.typography.titleMedium)
            ModeRow(settings.cleanupMode) { settings = settings.copy(cleanupMode = it) }

            Text("Summarization", style = MaterialTheme.typography.titleMedium)
            ModeRow(settings.summaryMode) { settings = settings.copy(summaryMode = it) }

            Text("Actions", style = MaterialTheme.typography.titleMedium)
            ModeRow(settings.actionsMode) { settings = settings.copy(actionsMode = it) }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Column(modifier = Modifier.weight(1f).padding(end = 12.dp)) {
                    Text("Allow fallback", style = MaterialTheme.typography.titleMedium)
                    Text(
                        if (settings.allowFallback) {
                            "If the primary side fails or is unavailable, try the other."
                        } else {
                            "Only the selected Cloud/Local side runs — no cross-fallback."
                        },
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                Switch(
                    checked = settings.allowFallback,
                    onCheckedChange = { settings = settings.copy(allowFallback = it) },
                )
            }

            BatteryOptimizationRow()

            Text("OpenRouter", style = MaterialTheme.typography.titleLarge)
            Text(
                "Endpoint: ${PipelineSettings.DEFAULT_BASE_URL}\n" +
                    "Paste an OpenRouter key (sk-or-…). Model ids like openai/whisper-1 are " +
                    "OpenRouter slugs, not the OpenAI API.\n" +
                    "Tap Save after changing key/models/routing — reprocess uses the saved settings.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            OutlinedTextField(
                value = settings.cloudApiKey,
                onValueChange = { settings = settings.copy(cloudApiKey = it) },
                label = { Text("OpenRouter API key") },
                supportingText = { Text("From openrouter.ai/keys") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
            )

            OutlinedButton(
                onClick = { refreshModels() },
                enabled = !loadingModels && settings.cloudApiKey.isNotBlank(),
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(
                    when {
                        loadingModels -> "Fetching models…"
                        sttModels.isEmpty() && chatModels.isEmpty() -> "Load models from OpenRouter"
                        else -> "Refresh models from OpenRouter"
                    },
                )
            }

            Text("Speech-to-text model", style = MaterialTheme.typography.titleMedium)
            ModelDropdown(
                label = "STT model",
                models = sttModels,
                selectedId = settings.cloudAsrModel,
                enabled = sttModels.isNotEmpty() && !loadingModels,
                emptyHint = if (loadingModels) {
                    "Loading models…"
                } else {
                    "Load models to choose an STT model"
                },
                onSelect = { settings = settings.copy(cloudAsrModel = normalizeModelId(it.id)) },
            )

            Text("Cleanup model", style = MaterialTheme.typography.titleMedium)
            Text(
                "Cheap/fast model for deduping and polishing transcripts.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            ModelDropdown(
                label = "Cleanup model",
                models = chatModels,
                selectedId = settings.cloudCleanupModel,
                enabled = chatModels.isNotEmpty() && !loadingModels,
                emptyHint = if (loadingModels) {
                    "Loading models…"
                } else {
                    "Load models to choose a cleanup model"
                },
                preferReasoningFirst = false,
                onSelect = { settings = settings.copy(cloudCleanupModel = normalizeModelId(it.id)) },
            )

            Text("Summary model", style = MaterialTheme.typography.titleMedium)
            Text(
                "Stronger model for Title/Overview (and future multi-meeting synthesis).",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            ModelDropdown(
                label = "Summary model",
                models = chatModels,
                selectedId = settings.cloudSummaryModel,
                enabled = chatModels.isNotEmpty() && !loadingModels,
                emptyHint = if (loadingModels) {
                    "Loading models…"
                } else {
                    "Load models to choose a summary model"
                },
                preferReasoningFirst = true,
                supportingText = when {
                    selectedSummaryMeta?.supportsReasoning == true ->
                        "Supports reasoning · default effort: ${selectedSummaryMeta.defaultEffort ?: "—"}"
                    chatModels.isNotEmpty() -> "Pick from the OpenRouter catalog"
                    else -> null
                },
                onSelect = { model ->
                    val nextEffort = when {
                        settings.reasoningEffort.isNotEmpty() -> settings.reasoningEffort
                        model.supportsReasoning && !model.defaultEffort.isNullOrBlank() ->
                            model.defaultEffort
                        else -> settings.reasoningEffort
                    }
                        settings = settings.copy(
                            cloudSummaryModel = normalizeModelId(model.id),
                            reasoningEffort = nextEffort.orEmpty(),
                        )
                },
            )

            Text("Reasoning effort (summary)", style = MaterialTheme.typography.titleMedium)
            Text(
                "Applied only to summarization. Example: deepseek/deepseek-r1 + High. " +
                    "Reasoning tokens are excluded from the returned text.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                effortOptions.forEach { effort ->
                    FilterChip(
                        selected = ReasoningEffort.fromStored(settings.reasoningEffort) == effort,
                        onClick = {
                            settings = settings.copy(reasoningEffort = effort.apiValue)
                        },
                        label = { Text(effort.label) },
                    )
                }
            }

            OutlinedTextField(
                value = settings.sttLanguage,
                onValueChange = { settings = settings.copy(sttLanguage = it) },
                label = { Text("STT language (optional)") },
                supportingText = { Text("ISO-639-1 like en — leave blank to auto-detect") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
            )

            Text("Prompts", style = MaterialTheme.typography.titleLarge)
            Text(
                "Placeholders: {{transcript}}, {{glossary}}, {{speaker_note}}. " +
                    "Meetings run cleanup first, then summarization.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            OutlinedTextField(
                value = settings.glossary.ifBlank { CleanupPrompts.DEFAULT_GLOSSARY },
                onValueChange = { settings = settings.copy(glossary = it) },
                label = { Text("Glossary") },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 120.dp),
                minLines = 4,
            )
            TextButton(onClick = { settings = settings.copy(glossary = "") }) {
                Text("Reset glossary to default")
            }

            OutlinedTextField(
                value = settings.cleanupPrompt.ifBlank { CleanupPrompts.DEFAULT_CLEANUP_PROMPT },
                onValueChange = { settings = settings.copy(cleanupPrompt = it) },
                label = { Text("Transcript cleanup prompt") },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 160.dp),
                minLines = 6,
            )
            TextButton(onClick = { settings = settings.copy(cleanupPrompt = "") }) {
                Text("Reset cleanup prompt to default")
            }

            OutlinedTextField(
                value = settings.summaryPrompt.ifBlank { CleanupPrompts.DEFAULT_SUMMARY_PROMPT },
                onValueChange = { settings = settings.copy(summaryPrompt = it) },
                label = { Text("Meeting summarization prompt") },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 160.dp),
                minLines = 6,
            )
            TextButton(onClick = { settings = settings.copy(summaryPrompt = "") }) {
                Text("Reset summary prompt to default")
            }

            Button(
                onClick = {
                    val toSave = settings.copy(
                        cloudBaseUrl = PipelineSettings.DEFAULT_BASE_URL,
                        glossary = normalizeTemplate(
                            settings.glossary,
                            CleanupPrompts.DEFAULT_GLOSSARY,
                        ),
                        cleanupPrompt = normalizeTemplate(
                            settings.cleanupPrompt,
                            CleanupPrompts.DEFAULT_CLEANUP_PROMPT,
                        ),
                        summaryPrompt = normalizeTemplate(
                            settings.summaryPrompt,
                            CleanupPrompts.DEFAULT_SUMMARY_PROMPT,
                        ),
                    )
                    config.save(toSave)
                    settings = config.load()
                    status = "Saved"
                },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text("Save")
            }

            Button(
                onClick = {
                    // Persist draft settings first so cloud routing sees the API key.
                    val toSave = settings.copy(cloudBaseUrl = PipelineSettings.DEFAULT_BASE_URL)
                    config.save(toSave)
                    settings = config.load()
                    if (!config.cloudConfigured() &&
                        settings.asrMode == ProviderMode.PREFER_CLOUD
                    ) {
                        status = "Save an OpenRouter API key before cloud ASR reprocess"
                        return@Button
                    }
                    scope.launch {
                        withContext(Dispatchers.IO) {
                            app.container.sessionRepository.requeueAllForAsr()
                        }
                        status = "Saved settings · re-queued recordings for ASR"
                    }
                },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text("Reprocess all recordings (ASR)")
            }

            Button(
                onClick = {
                    val toSave = settings.copy(cloudBaseUrl = PipelineSettings.DEFAULT_BASE_URL)
                    config.save(toSave)
                    settings = config.load()
                    if (!config.cloudConfigured() &&
                        (settings.cleanupMode == ProviderMode.PREFER_CLOUD ||
                            settings.summaryMode == ProviderMode.PREFER_CLOUD)
                    ) {
                        status = "Save an OpenRouter API key before cloud cleanup/summary"
                        return@Button
                    }
                    scope.launch {
                        withContext(Dispatchers.IO) {
                            val ids = app.container.meetingRepository.requeueAllForCleanup(100)
                            app.container.pipelineScheduler.requeuePendingMeetings(ids)
                        }
                        status = "Saved settings · re-queued all meetings for cleanup/summary"
                    }
                },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text("Reprocess all meetings (cleanup + summary)")
            }

            if (status != null) {
                Text(status!!, color = MaterialTheme.colorScheme.secondary)
            }

            Spacer(Modifier.height(8.dp))
            Text(
                "Sideload via scripts/push-local-models.sh:\n" +
                    "files/models/parakeet/{model_npu.tflite,model.tflite,tokenizer.json}\n" +
                    "files/models/gemma4b/gemma-4-E2B-it_Google_Tensor_G5.litertlm",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun ModelDropdown(
    label: String,
    models: List<OpenRouterModelInfo>,
    selectedId: String,
    onSelect: (OpenRouterModelInfo) -> Unit,
    enabled: Boolean,
    emptyHint: String,
    preferReasoningFirst: Boolean = false,
    supportingText: String? = null,
) {
    var showPicker by remember { mutableStateOf(false) }
    val selected = models.firstOrNull { OpenRouterModels.matches(it.id, selectedId) }
    val display = when {
        selected != null -> selected.id
        selectedId.isNotBlank() && enabled -> OpenRouterModels.normalizeId(selectedId)
        enabled -> "Select a model"
        else -> emptyHint
    }

    Box(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = display,
            onValueChange = {},
            readOnly = true,
            enabled = enabled,
            label = { Text(label) },
            supportingText = {
                Text(
                    when {
                        !enabled -> emptyHint
                        supportingText != null -> supportingText
                        else -> "${models.size} models from OpenRouter — tap to choose"
                    },
                )
            },
            trailingIcon = {
                Text(
                    "▼",
                    color = if (enabled) {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    } else {
                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
                    },
                )
            },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                // Avoid the washed-out readOnly/disabled look when models are ready.
                disabledTextColor = MaterialTheme.colorScheme.onSurface,
                disabledBorderColor = MaterialTheme.colorScheme.outline,
                disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                disabledSupportingTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                disabledTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
            ),
        )
        if (enabled) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .clickable { showPicker = true },
            )
        }
    }

    if (showPicker && enabled) {
        ModelPickerDialog(
            title = label,
            models = models,
            selectedId = selectedId,
            preferReasoningFirst = preferReasoningFirst,
            onSelect = {
                onSelect(it)
                showPicker = false
            },
            onDismiss = { showPicker = false },
        )
    }
}

@Composable
private fun ModelPickerDialog(
    title: String,
    models: List<OpenRouterModelInfo>,
    selectedId: String,
    preferReasoningFirst: Boolean,
    onSelect: (OpenRouterModelInfo) -> Unit,
    onDismiss: () -> Unit,
) {
    var query by remember { mutableStateOf("") }
    val ordered = remember(models, preferReasoningFirst) {
        if (!preferReasoningFirst) models
        else models.sortedWith(
            compareByDescending<OpenRouterModelInfo> { it.supportsReasoning }
                .thenBy { it.id.lowercase() },
        )
    }
    val filtered = remember(ordered, query) {
        if (query.isBlank()) ordered
        else ordered.filter {
            it.id.contains(query, ignoreCase = true) ||
                it.name.contains(query, ignoreCase = true)
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 480.dp),
            ) {
                OutlinedTextField(
                    value = query,
                    onValueChange = { query = it },
                    label = { Text("Search") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                )
                Spacer(Modifier.height(8.dp))
                if (filtered.isEmpty()) {
                    Text(
                        "No models match “$query”",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(0.dp),
                    ) {
                        items(filtered, key = { it.id }) { model ->
                            val selected = OpenRouterModels.matches(model.id, selectedId)
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { onSelect(model) }
                                    .padding(vertical = 10.dp, horizontal = 4.dp),
                            ) {
                                Text(
                                    model.id,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = if (selected) {
                                        MaterialTheme.colorScheme.primary
                                    } else {
                                        MaterialTheme.colorScheme.onSurface
                                    },
                                )
                                if (model.name.isNotBlank() && model.name != model.id) {
                                    Text(
                                        model.name,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    )
                                }
                                if (model.supportsReasoning) {
                                    Text(
                                        "reasoning",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.primary,
                                    )
                                }
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) { Text("Close") }
        },
    )
}

private fun ensureModelId(
    current: String,
    catalog: List<OpenRouterModelInfo>,
    fallback: String,
): String {
    val normalized = OpenRouterModels.normalizeId(current)
    if (catalog.isEmpty()) return normalized.ifBlank { fallback }
    catalog.firstOrNull { OpenRouterModels.matches(it.id, normalized) }?.let {
        return OpenRouterModels.normalizeId(it.id)
    }
    catalog.firstOrNull { OpenRouterModels.matches(it.id, fallback) }?.let {
        return OpenRouterModels.normalizeId(it.id)
    }
    return OpenRouterModels.normalizeId(catalog.first().id)
}

private fun normalizeModelId(id: String): String = OpenRouterModels.normalizeId(id)

private fun normalizeTemplate(value: String, default: String): String {
    val trimmed = value.trim()
    if (trimmed.isEmpty() || trimmed == default.trim()) return ""
    return value
}

@Composable
private fun BatteryOptimizationRow() {
    val context = LocalContext.current
    val pm = context.getSystemService(PowerManager::class.java)
    var ignoring by remember {
        mutableStateOf(
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && pm != null) {
                pm.isIgnoringBatteryOptimizations(context.packageName)
            } else {
                true
            },
        )
    }
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Column(modifier = Modifier.weight(1f).padding(end = 12.dp)) {
            Text("Unrestricted battery", style = MaterialTheme.typography.titleMedium)
            Text(
                if (ignoring) {
                    "Battery optimisation is already disabled for this app."
                } else {
                    "Recommended so transcription can finish after recording stops."
                },
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        if (!ignoring) {
            OutlinedButton(
                onClick = {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        val intent = Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS).apply {
                            data = Uri.parse("package:${context.packageName}")
                        }
                        runCatching { context.startActivity(intent) }
                        ignoring = pm?.isIgnoringBatteryOptimizations(context.packageName) == true
                    }
                },
            ) {
                Text("Allow")
            }
        }
    }
}

@Composable
private fun ModeRow(selected: ProviderMode, onSelect: (ProviderMode) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        FilterChip(
            selected = selected == ProviderMode.PREFER_CLOUD,
            onClick = { onSelect(ProviderMode.PREFER_CLOUD) },
            label = { Text("Cloud") },
        )
        FilterChip(
            selected = selected == ProviderMode.PREFER_LOCAL,
            onClick = { onSelect(ProviderMode.PREFER_LOCAL) },
            label = { Text("Local") },
        )
    }
}
