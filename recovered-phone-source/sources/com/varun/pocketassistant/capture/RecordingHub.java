package com.varun.pocketassistant.capture;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.flow.FlowKt;
import kotlinx.coroutines.flow.MutableStateFlow;
import kotlinx.coroutines.flow.StateFlow;
import kotlinx.coroutines.flow.StateFlowKt;

/* JADX INFO: compiled from: RecordingService.kt */
/* JADX INFO: loaded from: classes5.dex */
@Metadata(d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0004\bÇ\u0002\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003J\u000e\u0010\r\u001a\u00020\u000e2\u0006\u0010\u0004\u001a\u00020\u0005J\u000e\u0010\u000f\u001a\u00020\u000e2\u0006\u0010\u0004\u001a\u00020\u0005J\u000e\u0010\u0010\u001a\u00020\u000e2\u0006\u0010\t\u001a\u00020\bJ\b\u0010\u0011\u001a\u0004\u0018\u00010\u0005R\u0010\u0010\u0004\u001a\u0004\u0018\u00010\u0005X\u0082\u000e¢\u0006\u0002\n\u0000R\u0014\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\b0\u0007X\u0082\u0004¢\u0006\u0002\n\u0000R\u0017\u0010\t\u001a\b\u0012\u0004\u0012\u00020\b0\n¢\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\f¨\u0006\u0012"}, d2 = {"Lcom/varun/pocketassistant/capture/RecordingHub;", "", "<init>", "()V", "engine", "Lcom/varun/pocketassistant/capture/AudioCaptureEngine;", "_stats", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/varun/pocketassistant/capture/CaptureStats;", "stats", "Lkotlinx/coroutines/flow/StateFlow;", "getStats", "()Lkotlinx/coroutines/flow/StateFlow;", "bind", "", "unbind", "publish", "current", "app_debug"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class RecordingHub {
    private static volatile AudioCaptureEngine engine;
    public static final RecordingHub INSTANCE = new RecordingHub();
    private static final MutableStateFlow<CaptureStats> _stats = StateFlowKt.MutableStateFlow(new CaptureStats(null, null, null, 0, false, 0.0f, 0.0f, 0.0f, 0.0f, null, 0, 0, 0, 0, 0, 0, 0, 0, 0, null, null, null, null, null, null, 33554431, null));
    private static final StateFlow<CaptureStats> stats = FlowKt.asStateFlow(_stats);
    public static final int $stable = 8;

    private RecordingHub() {
    }

    public final StateFlow<CaptureStats> getStats() {
        return stats;
    }

    public final void bind(AudioCaptureEngine engine2) {
        Intrinsics.checkNotNullParameter(engine2, "engine");
        engine = engine2;
    }

    public final void unbind(AudioCaptureEngine engine2) {
        Intrinsics.checkNotNullParameter(engine2, "engine");
        if (engine == engine2) {
            engine = null;
            _stats.setValue(new CaptureStats(null, null, null, 0, false, 0.0f, 0.0f, 0.0f, 0.0f, null, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, null, null, null, null, null, null, 33554431, null));
        }
    }

    public final void publish(CaptureStats stats2) {
        Intrinsics.checkNotNullParameter(stats2, "stats");
        _stats.setValue(stats2);
    }

    public final AudioCaptureEngine current() {
        return engine;
    }
}
