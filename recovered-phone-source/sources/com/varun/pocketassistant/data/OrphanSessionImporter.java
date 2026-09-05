package com.varun.pocketassistant.data;

import androidx.autofill.HintConstants;
import androidx.core.view.MotionEventCompat;
import com.varun.pocketassistant.capture.AudioStorage;
import java.io.File;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import kotlin.Metadata;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.jvm.internal.ContinuationImpl;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.io.CloseableKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt;
import kotlin.text.MatchResult;
import kotlin.text.Regex;
import kotlin.text.StringsKt;

/* JADX INFO: compiled from: OrphanSessionImporter.kt */
/* JADX INFO: loaded from: classes9.dex */
@Metadata(d1 = {"\u0000B\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0007\u0018\u0000 \u00172\u00020\u0001:\u0001\u0017B'\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t¢\u0006\u0004\b\n\u0010\u000bJ\u000e\u0010\f\u001a\u00020\rH\u0086@¢\u0006\u0002\u0010\u000eJ\u0017\u0010\u000f\u001a\u0004\u0018\u00010\u00102\u0006\u0010\u0011\u001a\u00020\u0012H\u0002¢\u0006\u0002\u0010\u0013J\u0010\u0010\u0014\u001a\u00020\u00102\u0006\u0010\u0015\u001a\u00020\u0016H\u0002R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u0018"}, d2 = {"Lcom/varun/pocketassistant/data/OrphanSessionImporter;", "", "audioStorage", "Lcom/varun/pocketassistant/capture/AudioStorage;", "sessionDao", "Lcom/varun/pocketassistant/data/SessionDao;", "segmentDao", "Lcom/varun/pocketassistant/data/SegmentDao;", "repository", "Lcom/varun/pocketassistant/data/SessionRepository;", "<init>", "(Lcom/varun/pocketassistant/capture/AudioStorage;Lcom/varun/pocketassistant/data/SessionDao;Lcom/varun/pocketassistant/data/SegmentDao;Lcom/varun/pocketassistant/data/SessionRepository;)V", "importMissing", "", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "parseStartMs", "", HintConstants.AUTOFILL_HINT_NAME, "", "(Ljava/lang/String;)Ljava/lang/Long;", "estimateDurationMs", "wav", "Ljava/io/File;", "Companion", "app_debug"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class OrphanSessionImporter {
    private static final String TAG = "OrphanImporter";
    private final AudioStorage audioStorage;
    private final SessionRepository repository;
    private final SegmentDao segmentDao;
    private final SessionDao sessionDao;
    public static final int $stable = 8;

    /* JADX INFO: renamed from: com.varun.pocketassistant.data.OrphanSessionImporter$importMissing$1, reason: invalid class name */
    /* JADX INFO: compiled from: OrphanSessionImporter.kt */
    @Metadata(k = 3, mv = {2, 2, 0}, xi = 48)
    @DebugMetadata(c = "com.varun.pocketassistant.data.OrphanSessionImporter", f = "OrphanSessionImporter.kt", i = {0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4}, l = {MotionEventCompat.AXIS_GENERIC_3, MotionEventCompat.AXIS_GENERIC_6, 50, 72, 81}, m = "importMissing", n = {"root", "dirs", "dir", "wavs", "imported", "root", "dirs", "dir", "wavs", "already", "existing", "imported", "root", "dirs", "dir", "wavs", "already", "existing", "session", "imported", "started", "ended", "root", "dirs", "dir", "wavs", "already", "existing", "session", "wav", "segment", "imported", "segmentCount", "speechMs", "startedAt", "durationMs", "endedAt", "root", "dirs", "dir", "wavs", "already", "existing", "session", "imported", "segmentCount", "speechMs"}, s = {"L$0", "L$1", "L$3", "L$4", "I$0", "L$0", "L$1", "L$3", "L$4", "L$5", "L$6", "I$0", "L$0", "L$1", "L$3", "L$4", "L$5", "L$6", "L$7", "I$0", "J$0", "J$1", "L$0", "L$1", "L$3", "L$4", "L$5", "L$6", "L$7", "L$9", "L$10", "I$0", "I$1", "J$0", "J$1", "J$2", "J$3", "L$0", "L$1", "L$3", "L$4", "L$5", "L$6", "L$7", "I$0", "I$1", "J$0"})
    static final class AnonymousClass1 extends ContinuationImpl {
        int I$0;
        int I$1;
        long J$0;
        long J$1;
        long J$2;
        long J$3;
        Object L$0;
        Object L$1;
        Object L$10;
        Object L$2;
        Object L$3;
        Object L$4;
        Object L$5;
        Object L$6;
        Object L$7;
        Object L$8;
        Object L$9;
        int label;
        /* synthetic */ Object result;

        AnonymousClass1(Continuation<? super AnonymousClass1> continuation) {
            super(continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            this.result = obj;
            this.label |= Integer.MIN_VALUE;
            return OrphanSessionImporter.this.importMissing(this);
        }
    }

    public OrphanSessionImporter(AudioStorage audioStorage, SessionDao sessionDao, SegmentDao segmentDao, SessionRepository repository) {
        Intrinsics.checkNotNullParameter(audioStorage, "audioStorage");
        Intrinsics.checkNotNullParameter(sessionDao, "sessionDao");
        Intrinsics.checkNotNullParameter(segmentDao, "segmentDao");
        Intrinsics.checkNotNullParameter(repository, "repository");
        this.audioStorage = audioStorage;
        this.sessionDao = sessionDao;
        this.segmentDao = segmentDao;
        this.repository = repository;
    }

    /* JADX WARN: Code duplicated, block: B:100:0x05c2  */
    /* JADX WARN: Code duplicated, block: B:103:0x0626 A[RETURN] */
    /* JADX WARN: Code duplicated, block: B:104:0x0627  */
    /* JADX WARN: Code duplicated, block: B:123:0x0252 A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:125:0x05a5 A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:126:0x0454 A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:48:0x024f  */
    /* JADX WARN: Code duplicated, block: B:7:0x0018  */
    /* JADX WARN: Code duplicated, block: B:82:0x043e  */
    /* JADX WARN: Code duplicated, block: B:86:0x0461  */
    /* JADX WARN: Code duplicated, block: B:87:0x0466  */
    /* JADX WARN: Code duplicated, block: B:90:0x0515 A[RETURN] */
    /* JADX WARN: Code duplicated, block: B:91:0x0516  */
    /* JADX WARN: Code duplicated, block: B:94:0x053b  */
    /* JADX WARN: Code duplicated, block: B:95:0x054c  */
    /* JADX WARN: Code duplicated, block: B:96:0x0596 A[LOOP:3: B:80:0x0438->B:96:0x0596, LOOP_END] */
    /* JADX WARN: Code duplicated, block: B:99:0x05bd  */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:104:0x0627 -> B:105:0x063a). Please report as a decompilation issue!!! */
    /*  JADX ERROR: JadxOverflowException in pass: RegionMakerVisitor
        jadx.core.utils.exceptions.JadxOverflowException: Regions count limit reached at block B:126:0x0454
        	at jadx.core.utils.ErrorsCounter.addError(ErrorsCounter.java:59)
        	at jadx.core.utils.ErrorsCounter.error(ErrorsCounter.java:31)
        	at jadx.core.dex.attributes.nodes.NotificationAttrNode.addError(NotificationAttrNode.java:19)
        */
    public final java.lang.Object importMissing(kotlin.coroutines.Continuation<? super java.lang.Integer> r67) {
        /*
            Method dump skipped, instruction units count: 1672
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.varun.pocketassistant.data.OrphanSessionImporter.importMissing(kotlin.coroutines.Continuation):java.lang.Object");
    }

    private final Long parseStartMs(String name) {
        String stem = StringsKt.removeSuffix(StringsKt.removePrefix(name, (CharSequence) "speech_"), (CharSequence) ".wav");
        MatchResult partMatch = new Regex("^(\\d+)_part(\\d+)$").matchEntire(stem);
        if (partMatch != null) {
            Long longOrNull = StringsKt.toLongOrNull(partMatch.getGroupValues().get(1));
            if (longOrNull == null) {
                return null;
            }
            long base = longOrNull.longValue();
            Integer intOrNull = StringsKt.toIntOrNull(partMatch.getGroupValues().get(2));
            if (intOrNull == null) {
                return null;
            }
            int part = intOrNull.intValue();
            return Long.valueOf((((long) part) * 120000) + base);
        }
        return StringsKt.toLongOrNull(stem);
    }

    private final long estimateDurationMs(File wav) {
        File file;
        long j;
        long j2;
        try {
            file = wav;
            try {
                try {
                    RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r");
                    try {
                        byte[] bArr = new byte[44];
                        randomAccessFile.readFully(bArr);
                        ByteBuffer byteBufferOrder = ByteBuffer.wrap(bArr).order(ByteOrder.LITTLE_ENDIAN);
                        byteBufferOrder.position(24);
                        int i = byteBufferOrder.getInt();
                        byteBufferOrder.position(22);
                        int i2 = byteBufferOrder.getShort();
                        byteBufferOrder.position(34);
                        int i3 = byteBufferOrder.getShort();
                        byteBufferOrder.position(40);
                        j2 = 1000;
                        j = 0;
                        try {
                            long j3 = ((long) i) * ((long) i2) * ((long) (i3 / 8));
                            long length = j3 <= 0 ? file.length() / ((long) 32) : (RangesKt.coerceAtLeast(byteBufferOrder.getInt(), 0L) * 1000) / j3;
                            CloseableKt.closeFinally(randomAccessFile, null);
                            return length;
                        } catch (Throwable th) {
                            th = th;
                            Throwable th2 = th;
                            try {
                                throw th2;
                            } catch (Throwable th3) {
                                CloseableKt.closeFinally(randomAccessFile, th2);
                                throw th3;
                            }
                        }
                    } catch (Throwable th4) {
                        th = th4;
                        j = 0;
                        j2 = 1000;
                    }
                } catch (Throwable th5) {
                    return (RangesKt.coerceAtLeast(file.length() - ((long) 44), j) * j2) / 32000;
                }
            } catch (Throwable th6) {
                j = 0;
                j2 = 1000;
                return (RangesKt.coerceAtLeast(file.length() - ((long) 44), j) * j2) / 32000;
            }
        } catch (Throwable th7) {
            file = wav;
        }
    }
}
