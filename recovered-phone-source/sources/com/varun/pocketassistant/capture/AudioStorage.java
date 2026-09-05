package com.varun.pocketassistant.capture;

import android.content.Context;
import java.io.File;
import kotlin.Metadata;
import kotlin.io.FilesKt;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: AudioStorage.kt */
/* JADX INFO: loaded from: classes5.dex */
@Metadata(d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u000f\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0004\b\u0004\u0010\u0005J\u0006\u0010\b\u001a\u00020\u0007J\u000e\u0010\t\u001a\u00020\u00072\u0006\u0010\n\u001a\u00020\u000bJ\u000e\u0010\f\u001a\u00020\u00072\u0006\u0010\n\u001a\u00020\u000bJ\u0016\u0010\r\u001a\u00020\u00072\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\u000e\u001a\u00020\u000fJ\u000e\u0010\u0010\u001a\u00020\u00112\u0006\u0010\n\u001a\u00020\u000bR\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u0012"}, d2 = {"Lcom/varun/pocketassistant/capture/AudioStorage;", "", "context", "Landroid/content/Context;", "<init>", "(Landroid/content/Context;)V", "root", "Ljava/io/File;", "sessionsRoot", "sessionDir", "sessionId", "", "ensureSessionDir", "newSegmentFile", "startedAtMs", "", "deleteSessionDir", "", "app_debug"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class AudioStorage {
    public static final int $stable = 8;
    private final File root;

    public AudioStorage(Context context) {
        Intrinsics.checkNotNullParameter(context, "context");
        File file = new File(context.getFilesDir(), "sessions");
        file.mkdirs();
        this.root = file;
    }

    /* JADX INFO: renamed from: sessionsRoot, reason: from getter */
    public final File getRoot() {
        return this.root;
    }

    public final File sessionDir(String sessionId) {
        Intrinsics.checkNotNullParameter(sessionId, "sessionId");
        File file = new File(this.root, sessionId);
        file.mkdirs();
        return file;
    }

    public final File ensureSessionDir(String sessionId) {
        Intrinsics.checkNotNullParameter(sessionId, "sessionId");
        return sessionDir(sessionId);
    }

    public final File newSegmentFile(String sessionId, long startedAtMs) {
        Intrinsics.checkNotNullParameter(sessionId, "sessionId");
        return new File(sessionDir(sessionId), "speech_" + startedAtMs + ".wav");
    }

    public final void deleteSessionDir(String sessionId) {
        Intrinsics.checkNotNullParameter(sessionId, "sessionId");
        FilesKt.deleteRecursively(sessionDir(sessionId));
    }
}
