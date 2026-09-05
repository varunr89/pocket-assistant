package com.varun.pocketassistant.pipeline;

import kotlin.Metadata;
import kotlin.coroutines.Continuation;

/* JADX INFO: compiled from: Providers.kt */
/* JADX INFO: loaded from: classes8.dex */
@Metadata(d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\f\bf\u0018\u00002\u00020\u0001J\b\u0010\u0006\u001a\u00020\u0007H&J\u001e\u0010\b\u001a\u00020\u00032\u0006\u0010\t\u001a\u00020\u00032\u0006\u0010\n\u001a\u00020\u0007H¦@¢\u0006\u0002\u0010\u000bJ\u0016\u0010\f\u001a\u00020\u00032\u0006\u0010\r\u001a\u00020\u0003H¦@¢\u0006\u0002\u0010\u000eJ\u001e\u0010\u000f\u001a\u00020\u00032\u0006\u0010\u0010\u001a\u00020\u00032\u0006\u0010\u0011\u001a\u00020\u0003H¦@¢\u0006\u0002\u0010\u0012R\u0012\u0010\u0002\u001a\u00020\u0003X¦\u0004¢\u0006\u0006\u001a\u0004\b\u0004\u0010\u0005¨\u0006\u0013À\u0006\u0003"}, d2 = {"Lcom/varun/pocketassistant/pipeline/MeetingTextProvider;", "", "id", "", "getId", "()Ljava/lang/String;", "isAvailable", "", "cleanTranscript", "rawTranscript", "diarized", "(Ljava/lang/String;ZLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "summarize", "cleanedTranscript", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "prompt", "system", "user", "(Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"}, k = 1, mv = {2, 2, 0}, xi = 48)
public interface MeetingTextProvider {
    Object cleanTranscript(String str, boolean z, Continuation<? super String> continuation);

    String getId();

    boolean isAvailable();

    Object prompt(String str, String str2, Continuation<? super String> continuation);

    Object summarize(String str, Continuation<? super String> continuation);
}
