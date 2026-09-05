package com.varun.pocketassistant.capture;

import kotlin.Metadata;

/* JADX INFO: compiled from: VoiceActivityDetector.kt */
/* JADX INFO: loaded from: classes5.dex */
@Metadata(d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0017\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u0007\n\u0002\b\u0005\bf\u0018\u00002\u00020\u0001J\u0018\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0007H&J\b\u0010\b\u001a\u00020\tH&R\u0012\u0010\n\u001a\u00020\u000bX¦\u0004¢\u0006\u0006\u001a\u0004\b\f\u0010\rR\u0014\u0010\u000e\u001a\u00020\u000b8VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\u000f\u0010\r¨\u0006\u0010À\u0006\u0003"}, d2 = {"Lcom/varun/pocketassistant/capture/VoiceActivityDetector;", "", "accept", "Lcom/varun/pocketassistant/capture/VadDecision;", "frame", "", "length", "", "reset", "", "speechThreshold", "", "getSpeechThreshold", "()F", "lastSpeechProbability", "getLastSpeechProbability", "app_debug"}, k = 1, mv = {2, 2, 0}, xi = 48)
public interface VoiceActivityDetector {
    VadDecision accept(short[] frame, int length);

    float getSpeechThreshold();

    void reset();

    /* JADX INFO: compiled from: VoiceActivityDetector.kt */
    @Metadata(k = 3, mv = {2, 2, 0}, xi = 48)
    public static final class DefaultImpls {
        @Deprecated
        public static float getLastSpeechProbability(VoiceActivityDetector $this) {
            return VoiceActivityDetector.super.getLastSpeechProbability();
        }
    }

    default float getLastSpeechProbability() {
        return -1.0f;
    }
}
