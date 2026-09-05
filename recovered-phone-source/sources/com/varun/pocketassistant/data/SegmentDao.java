package com.varun.pocketassistant.data;

import androidx.core.app.NotificationCompat;
import com.varun.pocketassistant.pipeline.work.MeetingStageWorker;
import java.util.List;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

/* JADX INFO: compiled from: AppDatabase.kt */
/* JADX INFO: loaded from: classes9.dex */
@Metadata(d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\b\n\u0002\u0010\t\n\u0002\b\u0006\n\u0002\u0010\u0002\n\u0002\b\u000b\n\u0002\u0010\b\n\u0002\b\f\bg\u0018\u00002\u00020\u0001J\u001c\u0010\u0002\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\u00040\u00032\u0006\u0010\u0006\u001a\u00020\u0007H'J\u001c\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00050\u00042\u0006\u0010\u0006\u001a\u00020\u0007H§@¢\u0006\u0002\u0010\tJ\u0014\u0010\n\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\u00040\u0003H'J\u0014\u0010\u000b\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\u00040\u0003H'J\u0014\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004H§@¢\u0006\u0002\u0010\rJ$\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00050\u00042\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u0010H§@¢\u0006\u0002\u0010\u0012J\u001c\u0010\u0013\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\u00040\u00032\u0006\u0010\u0014\u001a\u00020\u0007H'J\u001c\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\u00050\u00042\u0006\u0010\u0014\u001a\u00020\u0007H§@¢\u0006\u0002\u0010\tJ$\u0010\u0016\u001a\u00020\u00172\u0006\u0010\u0014\u001a\u00020\u00072\f\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\u00070\u0004H§@¢\u0006\u0002\u0010\u0019J\u001e\u0010\u001a\u001a\u00020\u00172\u0006\u0010\u001b\u001a\u00020\u00072\u0006\u0010\u0014\u001a\u00020\u0007H§@¢\u0006\u0002\u0010\u001cJ\u0016\u0010\u001d\u001a\u00020\u00172\u0006\u0010\u001b\u001a\u00020\u0007H§@¢\u0006\u0002\u0010\tJ\u0016\u0010\u001e\u001a\u00020\u00172\u0006\u0010\u0014\u001a\u00020\u0007H§@¢\u0006\u0002\u0010\tJ\u0016\u0010\u001f\u001a\u00020\u00172\u0006\u0010\u001b\u001a\u00020\u0007H§@¢\u0006\u0002\u0010\tJ&\u0010 \u001a\b\u0012\u0004\u0012\u00020\u00050\u00042\u0006\u0010!\u001a\u00020\u00072\b\b\u0002\u0010\"\u001a\u00020#H§@¢\u0006\u0002\u0010$J.\u0010%\u001a\b\u0012\u0004\u0012\u00020\u00050\u00042\u0006\u0010&\u001a\u00020\u00072\u0006\u0010'\u001a\u00020\u00072\b\b\u0002\u0010\"\u001a\u00020#H§@¢\u0006\u0002\u0010(J\u0018\u0010)\u001a\u0004\u0018\u00010\u00052\u0006\u0010\u001b\u001a\u00020\u0007H§@¢\u0006\u0002\u0010\tJ\u0016\u0010*\u001a\u00020\u00172\u0006\u0010+\u001a\u00020\u0005H§@¢\u0006\u0002\u0010,J\u0016\u0010-\u001a\u00020\u00172\u0006\u0010+\u001a\u00020\u0005H§@¢\u0006\u0002\u0010,J\u0016\u0010.\u001a\u00020\u00172\u0006\u0010\u0006\u001a\u00020\u0007H§@¢\u0006\u0002\u0010\t¨\u0006/À\u0006\u0003"}, d2 = {"Lcom/varun/pocketassistant/data/SegmentDao;", "", "observeForSession", "Lkotlinx/coroutines/flow/Flow;", "", "Lcom/varun/pocketassistant/data/SegmentEntity;", "sessionId", "", "getForSession", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "observeAll", "observeUncategorized", "getAll", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getOverlapping", "startMs", "", "endMs", "(JJLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "observeForMeeting", MeetingStageWorker.KEY_MEETING_ID, "getForMeeting", "assignMeeting", "", "ids", "(Ljava/lang/String;Ljava/util/List;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "setMeetingId", "id", "(Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "clearMeetingId", "clearMeeting", "deleteById", "getByTranscriptStatus", NotificationCompat.CATEGORY_STATUS, "limit", "", "(Ljava/lang/String;ILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getNeedingAsr", "a", "b", "(Ljava/lang/String;Ljava/lang/String;ILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getById", "insert", "segment", "(Lcom/varun/pocketassistant/data/SegmentEntity;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "update", "deleteForSession", "app_debug"}, k = 1, mv = {2, 2, 0}, xi = 48)
public interface SegmentDao {
    Object assignMeeting(String str, List<String> list, Continuation<? super Unit> continuation);

    Object clearMeeting(String str, Continuation<? super Unit> continuation);

    Object clearMeetingId(String str, Continuation<? super Unit> continuation);

    Object deleteById(String str, Continuation<? super Unit> continuation);

    Object deleteForSession(String str, Continuation<? super Unit> continuation);

    Object getAll(Continuation<? super List<SegmentEntity>> continuation);

    Object getById(String str, Continuation<? super SegmentEntity> continuation);

    Object getByTranscriptStatus(String str, int i, Continuation<? super List<SegmentEntity>> continuation);

    Object getForMeeting(String str, Continuation<? super List<SegmentEntity>> continuation);

    Object getForSession(String str, Continuation<? super List<SegmentEntity>> continuation);

    Object getNeedingAsr(String str, String str2, int i, Continuation<? super List<SegmentEntity>> continuation);

    Object getOverlapping(long j, long j2, Continuation<? super List<SegmentEntity>> continuation);

    Object insert(SegmentEntity segmentEntity, Continuation<? super Unit> continuation);

    Flow<List<SegmentEntity>> observeAll();

    Flow<List<SegmentEntity>> observeForMeeting(String meetingId);

    Flow<List<SegmentEntity>> observeForSession(String sessionId);

    Flow<List<SegmentEntity>> observeUncategorized();

    Object setMeetingId(String str, String str2, Continuation<? super Unit> continuation);

    Object update(SegmentEntity segmentEntity, Continuation<? super Unit> continuation);

    /* JADX INFO: compiled from: AppDatabase.kt */
    @Metadata(k = 3, mv = {2, 2, 0}, xi = 48)
    public static final class DefaultImpls {
    }

    static /* synthetic */ Object getByTranscriptStatus$default(SegmentDao segmentDao, String str, int i, Continuation continuation, int i2, Object obj) {
        if (obj != null) {
            throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: getByTranscriptStatus");
        }
        if ((i2 & 2) != 0) {
            i = 5;
        }
        return segmentDao.getByTranscriptStatus(str, i, continuation);
    }

    static /* synthetic */ Object getNeedingAsr$default(SegmentDao segmentDao, String str, String str2, int i, Continuation continuation, int i2, Object obj) {
        if (obj != null) {
            throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: getNeedingAsr");
        }
        if ((i2 & 4) != 0) {
            i = 10;
        }
        return segmentDao.getNeedingAsr(str, str2, i, continuation);
    }
}
