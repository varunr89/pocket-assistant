package com.varun.pocketassistant.data;

import java.util.List;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

/* JADX INFO: compiled from: AppDatabase.kt */
/* JADX INFO: loaded from: classes9.dex */
@Metadata(d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\b\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0002\b\u0005\bg\u0018\u00002\u00020\u0001J\u0014\u0010\u0002\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\u00040\u0003H'J\u001c\u0010\u0006\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\u00040\u00032\u0006\u0010\u0007\u001a\u00020\bH'J\u0018\u0010\t\u001a\u0004\u0018\u00010\u00052\u0006\u0010\n\u001a\u00020\bH§@¢\u0006\u0002\u0010\u000bJ\u0018\u0010\f\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00050\u00032\u0006\u0010\n\u001a\u00020\bH'J.\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u00050\u00042\u0006\u0010\u000e\u001a\u00020\b2\u0006\u0010\u000f\u001a\u00020\b2\b\b\u0002\u0010\u0010\u001a\u00020\u0011H§@¢\u0006\u0002\u0010\u0012J\u001e\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u00050\u00042\b\b\u0002\u0010\u0010\u001a\u00020\u0011H§@¢\u0006\u0002\u0010\u0014J\u0016\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020\u0005H§@¢\u0006\u0002\u0010\u0018J\u0016\u0010\u0019\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020\u0005H§@¢\u0006\u0002\u0010\u0018J\u0016\u0010\u001a\u001a\u00020\u00162\u0006\u0010\n\u001a\u00020\bH§@¢\u0006\u0002\u0010\u000b¨\u0006\u001bÀ\u0006\u0003"}, d2 = {"Lcom/varun/pocketassistant/data/MeetingDao;", "", "observeAll", "Lkotlinx/coroutines/flow/Flow;", "", "Lcom/varun/pocketassistant/data/MeetingEntity;", "search", "q", "", "getById", "id", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "observeById", "getNeedingCleanup", "a", "b", "limit", "", "(Ljava/lang/String;Ljava/lang/String;ILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getAll", "(ILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "insert", "", "meeting", "(Lcom/varun/pocketassistant/data/MeetingEntity;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "update", "delete", "app_debug"}, k = 1, mv = {2, 2, 0}, xi = 48)
public interface MeetingDao {
    Object delete(String str, Continuation<? super Unit> continuation);

    Object getAll(int i, Continuation<? super List<MeetingEntity>> continuation);

    Object getById(String str, Continuation<? super MeetingEntity> continuation);

    Object getNeedingCleanup(String str, String str2, int i, Continuation<? super List<MeetingEntity>> continuation);

    Object insert(MeetingEntity meetingEntity, Continuation<? super Unit> continuation);

    Flow<List<MeetingEntity>> observeAll();

    Flow<MeetingEntity> observeById(String id);

    Flow<List<MeetingEntity>> search(String q);

    Object update(MeetingEntity meetingEntity, Continuation<? super Unit> continuation);

    /* JADX INFO: compiled from: AppDatabase.kt */
    @Metadata(k = 3, mv = {2, 2, 0}, xi = 48)
    public static final class DefaultImpls {
    }

    static /* synthetic */ Object getNeedingCleanup$default(MeetingDao meetingDao, String str, String str2, int i, Continuation continuation, int i2, Object obj) {
        if (obj != null) {
            throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: getNeedingCleanup");
        }
        if ((i2 & 4) != 0) {
            i = 10;
        }
        return meetingDao.getNeedingCleanup(str, str2, i, continuation);
    }

    static /* synthetic */ Object getAll$default(MeetingDao meetingDao, int i, Continuation continuation, int i2, Object obj) {
        if (obj != null) {
            throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: getAll");
        }
        if ((i2 & 1) != 0) {
            i = 100;
        }
        return meetingDao.getAll(i, continuation);
    }
}
