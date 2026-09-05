package com.varun.pocketassistant.pipeline;

import android.util.Log;
import com.varun.pocketassistant.pipeline.work.MeetingStageWorker;
import kotlin.Metadata;
import kotlin.ResultKt;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.ContinuationImpl;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SpillingKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import kotlinx.coroutines.TimeoutCancellationException;
import kotlinx.coroutines.TimeoutKt;

/* JADX INFO: compiled from: PipelineTelemetry.kt */
/* JADX INFO: loaded from: classes8.dex */
@Metadata(d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\t\n\u0002\b\f\n\u0002\u0010\b\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\bÇ\u0002\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003JN\u0010\u0018\u001a\u0002H\u0019\"\u0004\b\u0000\u0010\u00192\u0006\u0010\u001a\u001a\u00020\u00052\b\b\u0002\u0010\u001b\u001a\u00020\u00052\b\b\u0002\u0010\u001c\u001a\u00020\u00072\u001c\u0010\u001d\u001a\u0018\b\u0001\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00190\u001f\u0012\u0006\u0012\u0004\u0018\u00010\u00010\u001eH\u0086@¢\u0006\u0002\u0010 J1\u0010!\u001a\u0002H\u0019\"\u0004\b\u0000\u0010\u00192\u0006\u0010\u001a\u001a\u00020\u00052\b\b\u0002\u0010\u001b\u001a\u00020\u00052\f\u0010\u001d\u001a\b\u0012\u0004\u0012\u0002H\u00190\"¢\u0006\u0002\u0010#R\u000e\u0010\u0004\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0007X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0007X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0007X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\u0007X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u0007X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u0007X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u0007X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0007X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0007X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0007X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u0007X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u0014X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\u0014X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0016\u001a\u00020\u0014X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0017\u001a\u00020\u0014X\u0086T¢\u0006\u0002\n\u0000¨\u0006$"}, d2 = {"Lcom/varun/pocketassistant/pipeline/PipelineTelemetry;", "", "<init>", "()V", "TAG", "", "ASR_CHUNK_TIMEOUT_MS", "", "ASR_TIMEOUT_MS", "CHAT_ATTEMPT_TIMEOUT_MS", "CHAT_TIMEOUT_MS", "OPERATION_TIMEOUT_MS", "HTTP_CONNECT_TIMEOUT_MS", "CHAT_HTTP_READ_TIMEOUT_MS", "CHAT_HTTP_WRITE_TIMEOUT_MS", "CHAT_HTTP_CALL_TIMEOUT_MS", "ASR_HTTP_READ_TIMEOUT_MS", "ASR_HTTP_WRITE_TIMEOUT_MS", "HTTP_READ_TIMEOUT_MS", "ASR_HTTP_MAX_ATTEMPTS", "", "CLEANUP_MAX_ATTEMPTS", "SUMMARY_MAX_ATTEMPTS", "CHAT_HTTP_MAX_ATTEMPTS", "timed", "T", MeetingStageWorker.KEY_STAGE, "detail", "timeoutMs", "block", "Lkotlin/Function1;", "Lkotlin/coroutines/Continuation;", "(Ljava/lang/String;Ljava/lang/String;JLkotlin/jvm/functions/Function1;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "measure", "Lkotlin/Function0;", "(Ljava/lang/String;Ljava/lang/String;Lkotlin/jvm/functions/Function0;)Ljava/lang/Object;", "app_debug"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class PipelineTelemetry {
    public static final int $stable = 0;
    public static final long ASR_CHUNK_TIMEOUT_MS = 300000;
    public static final int ASR_HTTP_MAX_ATTEMPTS = 3;
    public static final long ASR_HTTP_READ_TIMEOUT_MS = 90000;
    public static final long ASR_HTTP_WRITE_TIMEOUT_MS = 60000;
    public static final long ASR_TIMEOUT_MS = 300000;
    public static final long CHAT_ATTEMPT_TIMEOUT_MS = 150000;
    public static final long CHAT_HTTP_CALL_TIMEOUT_MS = 150000;
    public static final int CHAT_HTTP_MAX_ATTEMPTS = 3;
    public static final long CHAT_HTTP_READ_TIMEOUT_MS = 140000;
    public static final long CHAT_HTTP_WRITE_TIMEOUT_MS = 30000;
    public static final long CHAT_TIMEOUT_MS = 180000;
    public static final int CLEANUP_MAX_ATTEMPTS = 3;
    public static final long HTTP_CONNECT_TIMEOUT_MS = 8000;
    public static final long HTTP_READ_TIMEOUT_MS = 90000;
    public static final PipelineTelemetry INSTANCE = new PipelineTelemetry();
    public static final long OPERATION_TIMEOUT_MS = 300000;
    public static final int SUMMARY_MAX_ATTEMPTS = 3;
    public static final String TAG = "PipelineTelemetry";

    /* JADX INFO: renamed from: com.varun.pocketassistant.pipeline.PipelineTelemetry$timed$1, reason: invalid class name */
    /* JADX INFO: compiled from: PipelineTelemetry.kt */
    @Metadata(k = 3, mv = {2, 2, 0}, xi = 48)
    @DebugMetadata(c = "com.varun.pocketassistant.pipeline.PipelineTelemetry", f = "PipelineTelemetry.kt", i = {0, 0, 0, 0, 0}, l = {79}, m = "timed", n = {MeetingStageWorker.KEY_STAGE, "detail", "block", "timeoutMs", "start"}, s = {"L$0", "L$1", "L$2", "J$0", "J$1"})
    static final class AnonymousClass1<T> extends ContinuationImpl {
        long J$0;
        long J$1;
        Object L$0;
        Object L$1;
        Object L$2;
        int label;
        /* synthetic */ Object result;

        AnonymousClass1(Continuation<? super AnonymousClass1> continuation) {
            super(continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            this.result = obj;
            this.label |= Integer.MIN_VALUE;
            return PipelineTelemetry.this.timed(null, null, 0L, null, this);
        }
    }

    private PipelineTelemetry() {
    }

    public static /* synthetic */ Object timed$default(PipelineTelemetry pipelineTelemetry, String str, String str2, long j, Function1 function1, Continuation continuation, int i, Object obj) {
        String str3;
        long j2;
        if ((i & 2) == 0) {
            str3 = str2;
        } else {
            str3 = "";
        }
        if ((i & 4) == 0) {
            j2 = j;
        } else {
            j2 = 300000;
        }
        return pipelineTelemetry.timed(str, str3, j2, function1, continuation);
    }

    /* JADX WARN: Code duplicated, block: B:7:0x001a  */
    public final <T> Object timed(String stage, String detail, long timeoutMs, Function1<? super Continuation<? super T>, ? extends Object> function1, Continuation<? super T> continuation) throws Throwable {
        AnonymousClass1 anonymousClass1;
        long start;
        String detail2;
        String detail3;
        Object result;
        long timeoutMs2 = timeoutMs;
        if (continuation instanceof AnonymousClass1) {
            anonymousClass1 = (AnonymousClass1) continuation;
            if ((anonymousClass1.label & Integer.MIN_VALUE) != 0) {
                anonymousClass1.label -= Integer.MIN_VALUE;
            } else {
                anonymousClass1 = new AnonymousClass1(continuation);
            }
        } else {
            anonymousClass1 = new AnonymousClass1(continuation);
        }
        AnonymousClass1 anonymousClass2 = anonymousClass1;
        Object $result = anonymousClass2.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (anonymousClass2.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                start = System.currentTimeMillis();
                try {
                    try {
                        PipelineTelemetry$timed$result$1 pipelineTelemetry$timed$result$1 = new PipelineTelemetry$timed$result$1(function1, null);
                        detail2 = stage;
                        try {
                            anonymousClass2.L$0 = detail2;
                            detail3 = detail;
                            try {
                                anonymousClass2.L$1 = detail3;
                                anonymousClass2.L$2 = SpillingKt.nullOutSpilledVariable(function1);
                                anonymousClass2.J$0 = timeoutMs2;
                                anonymousClass2.J$1 = start;
                                anonymousClass2.label = 1;
                                result = TimeoutKt.withTimeout(timeoutMs2, pipelineTelemetry$timed$result$1, anonymousClass2);
                                if (result == coroutine_suspended) {
                                    return coroutine_suspended;
                                }
                                try {
                                    Object result2 = result;
                                    long elapsed = System.currentTimeMillis() - start;
                                    try {
                                        Log.i(TAG, detail2 + " ok " + StringsKt.trim((CharSequence) detail3).toString() + " elapsedMs=" + elapsed + " timeoutMs=" + timeoutMs2);
                                        return result2;
                                    } catch (TimeoutCancellationException e) {
                                        t = e;
                                        long elapsed2 = System.currentTimeMillis() - start;
                                        TimeoutCancellationException t = t;
                                        Log.e(TAG, detail2 + " TIMEOUT " + StringsKt.trim((CharSequence) detail3).toString() + " elapsedMs=" + elapsed2 + " timeoutMs=" + timeoutMs2);
                                        throw new IllegalStateException(detail2 + " timed out after " + elapsed2 + "ms (limit " + timeoutMs2 + "ms)", t);
                                    } catch (Throwable th) {
                                        t = th;
                                        long elapsed3 = System.currentTimeMillis() - start;
                                        Log.e(TAG, detail2 + " FAIL " + StringsKt.trim((CharSequence) detail3).toString() + " elapsedMs=" + elapsed3 + " error=" + t.getMessage(), t);
                                        throw t;
                                    }
                                } catch (TimeoutCancellationException e2) {
                                    t = e2;
                                } catch (Throwable th2) {
                                    t = th2;
                                }
                            } catch (TimeoutCancellationException e3) {
                                t = e3;
                                long elapsed4 = System.currentTimeMillis() - start;
                                TimeoutCancellationException t2 = t;
                                Log.e(TAG, detail2 + " TIMEOUT " + StringsKt.trim((CharSequence) detail3).toString() + " elapsedMs=" + elapsed4 + " timeoutMs=" + timeoutMs2);
                                throw new IllegalStateException(detail2 + " timed out after " + elapsed4 + "ms (limit " + timeoutMs2 + "ms)", t2);
                            } catch (Throwable th3) {
                                t = th3;
                                long elapsed5 = System.currentTimeMillis() - start;
                                Log.e(TAG, detail2 + " FAIL " + StringsKt.trim((CharSequence) detail3).toString() + " elapsedMs=" + elapsed5 + " error=" + t.getMessage(), t);
                                throw t;
                            }
                        } catch (TimeoutCancellationException e4) {
                            t = e4;
                            detail3 = detail;
                        } catch (Throwable th4) {
                            t = th4;
                            detail3 = detail;
                        }
                    } catch (TimeoutCancellationException e5) {
                        t = e5;
                        detail2 = stage;
                        detail3 = detail;
                    } catch (Throwable th5) {
                        t = th5;
                        detail2 = stage;
                        detail3 = detail;
                    }
                } catch (TimeoutCancellationException e6) {
                    t = e6;
                    detail2 = stage;
                    detail3 = detail;
                } catch (Throwable th6) {
                    t = th6;
                    detail2 = stage;
                    detail3 = detail;
                }
                break;
            case 1:
                start = anonymousClass2.J$1;
                timeoutMs2 = anonymousClass2.J$0;
                String detail4 = (String) anonymousClass2.L$1;
                String stage2 = (String) anonymousClass2.L$0;
                try {
                    ResultKt.throwOnFailure($result);
                    result = $result;
                    detail3 = detail4;
                    detail2 = stage2;
                    Object result3 = result;
                    long elapsed6 = System.currentTimeMillis() - start;
                    Log.i(TAG, detail2 + " ok " + StringsKt.trim((CharSequence) detail3).toString() + " elapsedMs=" + elapsed6 + " timeoutMs=" + timeoutMs2);
                    return result3;
                } catch (TimeoutCancellationException e7) {
                    t = e7;
                    detail3 = detail4;
                    detail2 = stage2;
                    long elapsed7 = System.currentTimeMillis() - start;
                    TimeoutCancellationException t3 = t;
                    Log.e(TAG, detail2 + " TIMEOUT " + StringsKt.trim((CharSequence) detail3).toString() + " elapsedMs=" + elapsed7 + " timeoutMs=" + timeoutMs2);
                    throw new IllegalStateException(detail2 + " timed out after " + elapsed7 + "ms (limit " + timeoutMs2 + "ms)", t3);
                } catch (Throwable th7) {
                    t = th7;
                    detail3 = detail4;
                    detail2 = stage2;
                    long elapsed8 = System.currentTimeMillis() - start;
                    Log.e(TAG, detail2 + " FAIL " + StringsKt.trim((CharSequence) detail3).toString() + " elapsedMs=" + elapsed8 + " error=" + t.getMessage(), t);
                    throw t;
                }
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
    }

    public static /* synthetic */ Object measure$default(PipelineTelemetry pipelineTelemetry, String str, String str2, Function0 function0, int i, Object obj) {
        if ((i & 2) != 0) {
            str2 = "";
        }
        return pipelineTelemetry.measure(str, str2, function0);
    }

    public final <T> T measure(String stage, String detail, Function0<? extends T> block) {
        Intrinsics.checkNotNullParameter(stage, "stage");
        Intrinsics.checkNotNullParameter(detail, "detail");
        Intrinsics.checkNotNullParameter(block, "block");
        long jCurrentTimeMillis = System.currentTimeMillis();
        T tInvoke = block.invoke();
        long elapsed = System.currentTimeMillis() - jCurrentTimeMillis;
        Log.i(TAG, stage + " ok " + StringsKt.trim((CharSequence) detail).toString() + " elapsedMs=" + elapsed);
        return tInvoke;
    }
}
