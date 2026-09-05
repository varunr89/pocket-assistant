package com.varun.pocketassistant.pipeline;

import com.varun.pocketassistant.pipeline.work.MeetingStageWorker;
import kotlin.Metadata;
import kotlin.coroutines.Continuation;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: CloudAndLocalProviders.kt */
/* JADX INFO: loaded from: classes8.dex */
@Metadata(d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\f\b\u0007\u0018\u00002\u00020\u0001B\u0017\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0004\b\u0006\u0010\u0007J\b\u0010\f\u001a\u00020\rH\u0016J\u001e\u0010\u000e\u001a\u00020\t2\u0006\u0010\u000f\u001a\u00020\t2\u0006\u0010\u0010\u001a\u00020\rH\u0096@¢\u0006\u0002\u0010\u0011J\u0016\u0010\u0012\u001a\u00020\t2\u0006\u0010\u0013\u001a\u00020\tH\u0096@¢\u0006\u0002\u0010\u0014J\u001e\u0010\u0015\u001a\u00020\t2\u0006\u0010\u0016\u001a\u00020\t2\u0006\u0010\u0017\u001a\u00020\tH\u0096@¢\u0006\u0002\u0010\u0018R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\b\u001a\u00020\tX\u0096D¢\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000b¨\u0006\u0019"}, d2 = {"Lcom/varun/pocketassistant/pipeline/CloudCleanupProvider;", "Lcom/varun/pocketassistant/pipeline/MeetingTextProvider;", "config", "Lcom/varun/pocketassistant/pipeline/PipelineConfig;", "client", "Lcom/varun/pocketassistant/pipeline/OpenAiCompatibleClient;", "<init>", "(Lcom/varun/pocketassistant/pipeline/PipelineConfig;Lcom/varun/pocketassistant/pipeline/OpenAiCompatibleClient;)V", "id", "", "getId", "()Ljava/lang/String;", "isAvailable", "", "cleanTranscript", "rawTranscript", "diarized", "(Ljava/lang/String;ZLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "summarize", "cleanedTranscript", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "prompt", "system", "user", "(Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class CloudCleanupProvider implements MeetingTextProvider {
    public static final int $stable = 8;
    private final OpenAiCompatibleClient client;
    private final PipelineConfig config;
    private final String id;

    public CloudCleanupProvider(PipelineConfig config, OpenAiCompatibleClient client) {
        Intrinsics.checkNotNullParameter(config, "config");
        Intrinsics.checkNotNullParameter(client, "client");
        this.config = config;
        this.client = client;
        this.id = "cloud_chat";
    }

    @Override // com.varun.pocketassistant.pipeline.MeetingTextProvider
    public String getId() {
        return this.id;
    }

    @Override // com.varun.pocketassistant.pipeline.MeetingTextProvider
    public boolean isAvailable() {
        return this.config.cloudConfigured();
    }

    @Override // com.varun.pocketassistant.pipeline.MeetingTextProvider
    public Object cleanTranscript(String rawTranscript, boolean diarized, Continuation<? super String> continuation) {
        PipelineSettings settings = this.config.load();
        return this.client.chat(CleanupPrompts.INSTANCE.buildCleanup(settings, rawTranscript, diarized), "You clean speech transcripts. Output only the cleaned transcript text.", settings.getCloudCleanupModel(), false, MeetingStageWorker.STAGE_CLEANUP, continuation);
    }

    @Override // com.varun.pocketassistant.pipeline.MeetingTextProvider
    public Object summarize(String cleanedTranscript, Continuation<? super String> continuation) {
        PipelineSettings settings = this.config.load();
        return this.client.chat(CleanupPrompts.INSTANCE.buildSummary(settings, cleanedTranscript), "You summarize meetings. Output only the requested Markdown sections.", settings.getCloudSummaryModel(), true, MeetingStageWorker.STAGE_SUMMARY, continuation);
    }

    @Override // com.varun.pocketassistant.pipeline.MeetingTextProvider
    public Object prompt(String system, String user, Continuation<? super String> continuation) {
        PipelineSettings settings = this.config.load();
        return this.client.chat(user, system, settings.getCloudSummaryModel(), true, "actions", continuation);
    }
}
