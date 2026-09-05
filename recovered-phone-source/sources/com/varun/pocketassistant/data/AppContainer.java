package com.varun.pocketassistant.data;

import android.content.Context;
import androidx.room.Room;
import com.varun.pocketassistant.capture.AudioStorage;
import com.varun.pocketassistant.capture.RetentionPolicy;
import com.varun.pocketassistant.pipeline.CloudAsrProvider;
import com.varun.pocketassistant.pipeline.CloudCircuitBreaker;
import com.varun.pocketassistant.pipeline.CloudCleanupProvider;
import com.varun.pocketassistant.pipeline.GemmaCleanupProvider;
import com.varun.pocketassistant.pipeline.OpenAiCompatibleClient;
import com.varun.pocketassistant.pipeline.ParakeetAsrProvider;
import com.varun.pocketassistant.pipeline.PipelineConfig;
import com.varun.pocketassistant.pipeline.ProviderRouter;
import com.varun.pocketassistant.pipeline.work.PipelineScheduler;
import com.varun.pocketassistant.speech.AsrStage;
import com.varun.pocketassistant.speech.MeetingStage;
import com.varun.pocketassistant.speech.SpeakerProfileStore;
import java.io.File;
import java.util.Iterator;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.io.FilesKt;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: AppContainer.kt */
/* JADX INFO: loaded from: classes9.dex */
@Metadata(d1 = {"\u0000\u0080\u0001\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u000f\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0004\b\u0004\u0010\u0005J\b\u0010<\u001a\u00020=H\u0002R\u0016\u0010\u0006\u001a\n \u0007*\u0004\u0018\u00010\u00030\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R\u0011\u0010\b\u001a\u00020\t¢\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000bR\u0011\u0010\f\u001a\u00020\r¢\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000fR\u0011\u0010\u0010\u001a\u00020\u0011¢\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0013R\u0011\u0010\u0014\u001a\u00020\u0015¢\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\u0017R\u0011\u0010\u0018\u001a\u00020\u0019¢\u0006\b\n\u0000\u001a\u0004\b\u001a\u0010\u001bR\u0011\u0010\u001c\u001a\u00020\u001d¢\u0006\b\n\u0000\u001a\u0004\b\u001e\u0010\u001fR\u0011\u0010 \u001a\u00020!¢\u0006\b\n\u0000\u001a\u0004\b\"\u0010#R\u0011\u0010$\u001a\u00020%¢\u0006\b\n\u0000\u001a\u0004\b&\u0010'R\u0011\u0010(\u001a\u00020)¢\u0006\b\n\u0000\u001a\u0004\b*\u0010+R\u0011\u0010,\u001a\u00020-¢\u0006\b\n\u0000\u001a\u0004\b.\u0010/R\u0011\u00100\u001a\u000201¢\u0006\b\n\u0000\u001a\u0004\b2\u00103R\u0011\u00104\u001a\u000205¢\u0006\b\n\u0000\u001a\u0004\b6\u00107R\u0011\u00108\u001a\u000209¢\u0006\b\n\u0000\u001a\u0004\b:\u0010;¨\u0006>"}, d2 = {"Lcom/varun/pocketassistant/data/AppContainer;", "", "context", "Landroid/content/Context;", "<init>", "(Landroid/content/Context;)V", "appContext", "kotlin.jvm.PlatformType", "database", "Lcom/varun/pocketassistant/data/AppDatabase;", "getDatabase", "()Lcom/varun/pocketassistant/data/AppDatabase;", "audioStorage", "Lcom/varun/pocketassistant/capture/AudioStorage;", "getAudioStorage", "()Lcom/varun/pocketassistant/capture/AudioStorage;", "retentionPolicy", "Lcom/varun/pocketassistant/capture/RetentionPolicy;", "getRetentionPolicy", "()Lcom/varun/pocketassistant/capture/RetentionPolicy;", "pipelineConfig", "Lcom/varun/pocketassistant/pipeline/PipelineConfig;", "getPipelineConfig", "()Lcom/varun/pocketassistant/pipeline/PipelineConfig;", "speakerStore", "Lcom/varun/pocketassistant/speech/SpeakerProfileStore;", "getSpeakerStore", "()Lcom/varun/pocketassistant/speech/SpeakerProfileStore;", "cloudCircuitBreaker", "Lcom/varun/pocketassistant/pipeline/CloudCircuitBreaker;", "getCloudCircuitBreaker", "()Lcom/varun/pocketassistant/pipeline/CloudCircuitBreaker;", "openAiClient", "Lcom/varun/pocketassistant/pipeline/OpenAiCompatibleClient;", "getOpenAiClient", "()Lcom/varun/pocketassistant/pipeline/OpenAiCompatibleClient;", "providerRouter", "Lcom/varun/pocketassistant/pipeline/ProviderRouter;", "getProviderRouter", "()Lcom/varun/pocketassistant/pipeline/ProviderRouter;", "sessionRepository", "Lcom/varun/pocketassistant/data/SessionRepository;", "getSessionRepository", "()Lcom/varun/pocketassistant/data/SessionRepository;", "meetingRepository", "Lcom/varun/pocketassistant/data/MeetingRepository;", "getMeetingRepository", "()Lcom/varun/pocketassistant/data/MeetingRepository;", "asrStage", "Lcom/varun/pocketassistant/speech/AsrStage;", "getAsrStage", "()Lcom/varun/pocketassistant/speech/AsrStage;", "meetingStage", "Lcom/varun/pocketassistant/speech/MeetingStage;", "getMeetingStage", "()Lcom/varun/pocketassistant/speech/MeetingStage;", "pipelineScheduler", "Lcom/varun/pocketassistant/pipeline/work/PipelineScheduler;", "getPipelineScheduler", "()Lcom/varun/pocketassistant/pipeline/work/PipelineScheduler;", "sweepPrepCaches", "", "app_debug"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class AppContainer {
    public static final int $stable = 8;
    private final Context appContext;
    private final AsrStage asrStage;
    private final AudioStorage audioStorage;
    private final CloudCircuitBreaker cloudCircuitBreaker;
    private final AppDatabase database;
    private final MeetingRepository meetingRepository;
    private final MeetingStage meetingStage;
    private final OpenAiCompatibleClient openAiClient;
    private final PipelineConfig pipelineConfig;
    private final PipelineScheduler pipelineScheduler;
    private final ProviderRouter providerRouter;
    private final RetentionPolicy retentionPolicy;
    private final SessionRepository sessionRepository;
    private final SpeakerProfileStore speakerStore;

    public AppContainer(Context context) {
        Intrinsics.checkNotNullParameter(context, "context");
        this.appContext = context.getApplicationContext();
        Context appContext = this.appContext;
        Intrinsics.checkNotNullExpressionValue(appContext, "appContext");
        this.database = (AppDatabase) Room.databaseBuilder(appContext, AppDatabase.class, "pocket_assistant.db").addMigrations(AppDatabaseKt.getMIGRATION_4_5(), AppDatabaseKt.getMIGRATION_5_6(), AppDatabaseKt.getMIGRATION_6_7(), AppDatabaseKt.getMIGRATION_7_8()).build();
        Context appContext2 = this.appContext;
        Intrinsics.checkNotNullExpressionValue(appContext2, "appContext");
        this.audioStorage = new AudioStorage(appContext2);
        this.retentionPolicy = new RetentionPolicy(14);
        Context appContext3 = this.appContext;
        Intrinsics.checkNotNullExpressionValue(appContext3, "appContext");
        this.pipelineConfig = new PipelineConfig(appContext3);
        Context appContext4 = this.appContext;
        Intrinsics.checkNotNullExpressionValue(appContext4, "appContext");
        this.speakerStore = new SpeakerProfileStore(appContext4);
        this.cloudCircuitBreaker = new CloudCircuitBreaker(0, 0L, 3, null);
        this.openAiClient = new OpenAiCompatibleClient(this.pipelineConfig, this.cloudCircuitBreaker);
        PipelineConfig pipelineConfig = this.pipelineConfig;
        Context appContext5 = this.appContext;
        Intrinsics.checkNotNullExpressionValue(appContext5, "appContext");
        ParakeetAsrProvider parakeetAsrProvider = new ParakeetAsrProvider(appContext5);
        Context appContext6 = this.appContext;
        Intrinsics.checkNotNullExpressionValue(appContext6, "appContext");
        CloudAsrProvider cloudAsrProvider = new CloudAsrProvider(appContext6, this.pipelineConfig, this.openAiClient);
        Context appContext7 = this.appContext;
        Intrinsics.checkNotNullExpressionValue(appContext7, "appContext");
        this.providerRouter = new ProviderRouter(pipelineConfig, parakeetAsrProvider, cloudAsrProvider, new GemmaCleanupProvider(appContext7, this.pipelineConfig), new CloudCleanupProvider(this.pipelineConfig, this.openAiClient), this.cloudCircuitBreaker);
        this.sessionRepository = new SessionRepository(this.database.sessionDao(), this.database.segmentDao(), this.audioStorage, this.retentionPolicy);
        this.meetingRepository = new MeetingRepository(this.database.meetingDao(), this.database.segmentDao());
        this.asrStage = new AsrStage(this.sessionRepository, this.providerRouter);
        this.meetingStage = new MeetingStage(this.meetingRepository, this.pipelineConfig, this.providerRouter);
        Context appContext8 = this.appContext;
        Intrinsics.checkNotNullExpressionValue(appContext8, "appContext");
        PipelineScheduler pipelineScheduler = new PipelineScheduler(appContext8);
        this.sessionRepository.setPipelineScheduler(pipelineScheduler);
        this.meetingRepository.setPipelineScheduler(pipelineScheduler);
        this.pipelineScheduler = pipelineScheduler;
        sweepPrepCaches();
    }

    public final AppDatabase getDatabase() {
        return this.database;
    }

    public final AudioStorage getAudioStorage() {
        return this.audioStorage;
    }

    public final RetentionPolicy getRetentionPolicy() {
        return this.retentionPolicy;
    }

    public final PipelineConfig getPipelineConfig() {
        return this.pipelineConfig;
    }

    public final SpeakerProfileStore getSpeakerStore() {
        return this.speakerStore;
    }

    public final CloudCircuitBreaker getCloudCircuitBreaker() {
        return this.cloudCircuitBreaker;
    }

    public final OpenAiCompatibleClient getOpenAiClient() {
        return this.openAiClient;
    }

    public final ProviderRouter getProviderRouter() {
        return this.providerRouter;
    }

    public final SessionRepository getSessionRepository() {
        return this.sessionRepository;
    }

    public final MeetingRepository getMeetingRepository() {
        return this.meetingRepository;
    }

    public final AsrStage getAsrStage() {
        return this.asrStage;
    }

    public final MeetingStage getMeetingStage() {
        return this.meetingStage;
    }

    public final PipelineScheduler getPipelineScheduler() {
        return this.pipelineScheduler;
    }

    private final void sweepPrepCaches() {
        Iterator it = CollectionsKt.listOf((Object[]) new String[]{"cloud_asr_prep", "asr_prep"}).iterator();
        while (it.hasNext()) {
            File file = new File(this.appContext.getCacheDir(), (String) it.next());
            if (file.isDirectory()) {
                FilesKt.deleteRecursively(file);
            }
        }
    }
}
