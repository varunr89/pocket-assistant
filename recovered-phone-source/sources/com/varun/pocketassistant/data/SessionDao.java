package com.varun.pocketassistant.data;

import androidx.core.app.NotificationCompat;
import java.util.List;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

/* JADX INFO: compiled from: AppDatabase.kt */
/* JADX INFO: loaded from: classes9.dex */
@Metadata(d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0002\b\u0006\n\u0002\u0010\t\n\u0002\b\u0002\bg\u0018\u00002\u00020\u0001J\u0014\u0010\u0002\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\u00040\u0003H'J\u0018\u0010\u0006\u001a\u0004\u0018\u00010\u00052\u0006\u0010\u0007\u001a\u00020\bH§@¢\u0006\u0002\u0010\tJ\u0018\u0010\n\u001a\u0004\u0018\u00010\u00052\u0006\u0010\u000b\u001a\u00020\bH§@¢\u0006\u0002\u0010\tJ\u0016\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u0005H§@¢\u0006\u0002\u0010\u000fJ\u0016\u0010\u0010\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u0005H§@¢\u0006\u0002\u0010\u000fJ\u0016\u0010\u0011\u001a\u00020\r2\u0006\u0010\u0007\u001a\u00020\bH§@¢\u0006\u0002\u0010\tJ\u001c\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u00050\u00042\u0006\u0010\u0013\u001a\u00020\u0014H§@¢\u0006\u0002\u0010\u0015¨\u0006\u0016À\u0006\u0003"}, d2 = {"Lcom/varun/pocketassistant/data/SessionDao;", "", "observeSessions", "Lkotlinx/coroutines/flow/Flow;", "", "Lcom/varun/pocketassistant/data/SessionEntity;", "getById", "id", "", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getByStatus", NotificationCompat.CATEGORY_STATUS, "insert", "", "session", "(Lcom/varun/pocketassistant/data/SessionEntity;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "update", "delete", "getEndedBefore", "cutoffMs", "", "(JLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"}, k = 1, mv = {2, 2, 0}, xi = 48)
public interface SessionDao {
    Object delete(String str, Continuation<? super Unit> continuation);

    Object getById(String str, Continuation<? super SessionEntity> continuation);

    Object getByStatus(String str, Continuation<? super SessionEntity> continuation);

    Object getEndedBefore(long j, Continuation<? super List<SessionEntity>> continuation);

    Object insert(SessionEntity sessionEntity, Continuation<? super Unit> continuation);

    Flow<List<SessionEntity>> observeSessions();

    Object update(SessionEntity sessionEntity, Continuation<? super Unit> continuation);
}
