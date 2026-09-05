package com.varun.pocketassistant.data;

import androidx.core.app.NotificationCompat;
import androidx.room.EntityDeleteOrUpdateAdapter;
import androidx.room.EntityInsertAdapter;
import androidx.room.RoomDatabase;
import androidx.room.coroutines.FlowUtil;
import androidx.room.util.DBUtil;
import androidx.room.util.SQLiteStatementUtil;
import androidx.room.util.StringUtil;
import androidx.sqlite.SQLiteConnection;
import androidx.sqlite.SQLiteStatement;
import com.varun.pocketassistant.pipeline.work.MeetingStageWorker;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.reflect.KClass;
import kotlinx.coroutines.flow.Flow;

/* JADX INFO: compiled from: SegmentDao_Impl.kt */
/* JADX INFO: loaded from: classes9.dex */
@Metadata(d1 = {"\u0000L\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0000\n\u0002\u0010\u000e\n\u0002\b\b\n\u0002\u0010\t\n\u0002\b\b\n\u0002\u0010\b\n\u0002\b\u0012\b\u0007\u0018\u0000 72\u00020\u0001:\u00017B\u000f\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0004\b\u0004\u0010\u0005J\u0016\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\bH\u0096@¢\u0006\u0002\u0010\u000eJ\u0016\u0010\u000f\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\bH\u0096@¢\u0006\u0002\u0010\u000eJ\u001c\u0010\u0010\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00120\u00112\u0006\u0010\u0013\u001a\u00020\u0014H\u0016J\u001c\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\b0\u00122\u0006\u0010\u0013\u001a\u00020\u0014H\u0096@¢\u0006\u0002\u0010\u0016J\u0014\u0010\u0017\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00120\u0011H\u0016J\u0014\u0010\u0018\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00120\u0011H\u0016J\u0014\u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\b0\u0012H\u0096@¢\u0006\u0002\u0010\u001aJ$\u0010\u001b\u001a\b\u0012\u0004\u0012\u00020\b0\u00122\u0006\u0010\u001c\u001a\u00020\u001d2\u0006\u0010\u001e\u001a\u00020\u001dH\u0096@¢\u0006\u0002\u0010\u001fJ\u001c\u0010 \u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00120\u00112\u0006\u0010!\u001a\u00020\u0014H\u0016J\u001c\u0010\"\u001a\b\u0012\u0004\u0012\u00020\b0\u00122\u0006\u0010!\u001a\u00020\u0014H\u0096@¢\u0006\u0002\u0010\u0016J$\u0010#\u001a\b\u0012\u0004\u0012\u00020\b0\u00122\u0006\u0010$\u001a\u00020\u00142\u0006\u0010%\u001a\u00020&H\u0096@¢\u0006\u0002\u0010'J,\u0010(\u001a\b\u0012\u0004\u0012\u00020\b0\u00122\u0006\u0010)\u001a\u00020\u00142\u0006\u0010*\u001a\u00020\u00142\u0006\u0010%\u001a\u00020&H\u0096@¢\u0006\u0002\u0010+J\u0018\u0010,\u001a\u0004\u0018\u00010\b2\u0006\u0010-\u001a\u00020\u0014H\u0096@¢\u0006\u0002\u0010\u0016J$\u0010.\u001a\u00020\f2\u0006\u0010!\u001a\u00020\u00142\f\u0010/\u001a\b\u0012\u0004\u0012\u00020\u00140\u0012H\u0096@¢\u0006\u0002\u00100J\u001e\u00101\u001a\u00020\f2\u0006\u0010-\u001a\u00020\u00142\u0006\u0010!\u001a\u00020\u0014H\u0096@¢\u0006\u0002\u00102J\u0016\u00103\u001a\u00020\f2\u0006\u0010-\u001a\u00020\u0014H\u0096@¢\u0006\u0002\u0010\u0016J\u0016\u00104\u001a\u00020\f2\u0006\u0010!\u001a\u00020\u0014H\u0096@¢\u0006\u0002\u0010\u0016J\u0016\u00105\u001a\u00020\f2\u0006\u0010-\u001a\u00020\u0014H\u0096@¢\u0006\u0002\u0010\u0016J\u0016\u00106\u001a\u00020\f2\u0006\u0010\u0013\u001a\u00020\u0014H\u0096@¢\u0006\u0002\u0010\u0016R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\b0\u0007X\u0082\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\t\u001a\b\u0012\u0004\u0012\u00020\b0\nX\u0082\u0004¢\u0006\u0002\n\u0000¨\u00068"}, d2 = {"Lcom/varun/pocketassistant/data/SegmentDao_Impl;", "Lcom/varun/pocketassistant/data/SegmentDao;", "__db", "Landroidx/room/RoomDatabase;", "<init>", "(Landroidx/room/RoomDatabase;)V", "__insertAdapterOfSegmentEntity", "Landroidx/room/EntityInsertAdapter;", "Lcom/varun/pocketassistant/data/SegmentEntity;", "__updateAdapterOfSegmentEntity", "Landroidx/room/EntityDeleteOrUpdateAdapter;", "insert", "", "segment", "(Lcom/varun/pocketassistant/data/SegmentEntity;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "update", "observeForSession", "Lkotlinx/coroutines/flow/Flow;", "", "sessionId", "", "getForSession", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "observeAll", "observeUncategorized", "getAll", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getOverlapping", "startMs", "", "endMs", "(JJLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "observeForMeeting", MeetingStageWorker.KEY_MEETING_ID, "getForMeeting", "getByTranscriptStatus", NotificationCompat.CATEGORY_STATUS, "limit", "", "(Ljava/lang/String;ILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getNeedingAsr", "a", "b", "(Ljava/lang/String;Ljava/lang/String;ILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getById", "id", "assignMeeting", "ids", "(Ljava/lang/String;Ljava/util/List;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "setMeetingId", "(Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "clearMeetingId", "clearMeeting", "deleteById", "deleteForSession", "Companion", "app_debug"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class SegmentDao_Impl implements SegmentDao {
    private final RoomDatabase __db;
    private final EntityInsertAdapter<SegmentEntity> __insertAdapterOfSegmentEntity;
    private final EntityDeleteOrUpdateAdapter<SegmentEntity> __updateAdapterOfSegmentEntity;

    /* JADX INFO: renamed from: Companion, reason: from kotlin metadata */
    public static final Companion INSTANCE = new Companion(null);
    public static final int $stable = 8;

    public SegmentDao_Impl(RoomDatabase __db) {
        Intrinsics.checkNotNullParameter(__db, "__db");
        this.__db = __db;
        this.__insertAdapterOfSegmentEntity = new EntityInsertAdapter<SegmentEntity>() { // from class: com.varun.pocketassistant.data.SegmentDao_Impl.1
            @Override // androidx.room.EntityInsertAdapter
            protected String createQuery() {
                return "INSERT OR ABORT INTO `segments` (`id`,`sessionId`,`filePath`,`startedAtMs`,`endedAtMs`,`durationMs`,`byteSize`,`transcriptStatus`,`transcript`,`diarizedTranscript`,`cleanedTranscript`,`asrProvider`,`cleanupProvider`,`meetingId`,`asrLastError`,`skipReason`,`updatedAtMs`,`speakerCandidatesJson`,`youConfirmed`,`endReason`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // androidx.room.EntityInsertAdapter
            public void bind(SQLiteStatement statement, SegmentEntity entity) {
                Intrinsics.checkNotNullParameter(statement, "statement");
                Intrinsics.checkNotNullParameter(entity, "entity");
                statement.mo7910bindText(1, entity.getId());
                statement.mo7910bindText(2, entity.getSessionId());
                statement.mo7910bindText(3, entity.getFilePath());
                statement.mo7908bindLong(4, entity.getStartedAtMs());
                statement.mo7908bindLong(5, entity.getEndedAtMs());
                statement.mo7908bindLong(6, entity.getDurationMs());
                statement.mo7908bindLong(7, entity.getByteSize());
                statement.mo7910bindText(8, entity.getTranscriptStatus());
                String transcript = entity.getTranscript();
                if (transcript == null) {
                    statement.mo7909bindNull(9);
                } else {
                    statement.mo7910bindText(9, transcript);
                }
                String diarizedTranscript = entity.getDiarizedTranscript();
                if (diarizedTranscript == null) {
                    statement.mo7909bindNull(10);
                } else {
                    statement.mo7910bindText(10, diarizedTranscript);
                }
                String cleanedTranscript = entity.getCleanedTranscript();
                if (cleanedTranscript == null) {
                    statement.mo7909bindNull(11);
                } else {
                    statement.mo7910bindText(11, cleanedTranscript);
                }
                String asrProvider = entity.getAsrProvider();
                if (asrProvider == null) {
                    statement.mo7909bindNull(12);
                } else {
                    statement.mo7910bindText(12, asrProvider);
                }
                String cleanupProvider = entity.getCleanupProvider();
                if (cleanupProvider == null) {
                    statement.mo7909bindNull(13);
                } else {
                    statement.mo7910bindText(13, cleanupProvider);
                }
                String meetingId = entity.getMeetingId();
                if (meetingId == null) {
                    statement.mo7909bindNull(14);
                } else {
                    statement.mo7910bindText(14, meetingId);
                }
                String asrLastError = entity.getAsrLastError();
                if (asrLastError == null) {
                    statement.mo7909bindNull(15);
                } else {
                    statement.mo7910bindText(15, asrLastError);
                }
                String skipReason = entity.getSkipReason();
                if (skipReason == null) {
                    statement.mo7909bindNull(16);
                } else {
                    statement.mo7910bindText(16, skipReason);
                }
                statement.mo7908bindLong(17, entity.getUpdatedAtMs());
                String speakerCandidatesJson = entity.getSpeakerCandidatesJson();
                if (speakerCandidatesJson == null) {
                    statement.mo7909bindNull(18);
                } else {
                    statement.mo7910bindText(18, speakerCandidatesJson);
                }
                statement.mo7908bindLong(19, entity.getYouConfirmed() ? 1L : 0L);
                String endReason = entity.getEndReason();
                if (endReason == null) {
                    statement.mo7909bindNull(20);
                } else {
                    statement.mo7910bindText(20, endReason);
                }
            }
        };
        this.__updateAdapterOfSegmentEntity = new EntityDeleteOrUpdateAdapter<SegmentEntity>() { // from class: com.varun.pocketassistant.data.SegmentDao_Impl.2
            @Override // androidx.room.EntityDeleteOrUpdateAdapter
            protected String createQuery() {
                return "UPDATE OR ABORT `segments` SET `id` = ?,`sessionId` = ?,`filePath` = ?,`startedAtMs` = ?,`endedAtMs` = ?,`durationMs` = ?,`byteSize` = ?,`transcriptStatus` = ?,`transcript` = ?,`diarizedTranscript` = ?,`cleanedTranscript` = ?,`asrProvider` = ?,`cleanupProvider` = ?,`meetingId` = ?,`asrLastError` = ?,`skipReason` = ?,`updatedAtMs` = ?,`speakerCandidatesJson` = ?,`youConfirmed` = ?,`endReason` = ? WHERE `id` = ?";
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // androidx.room.EntityDeleteOrUpdateAdapter
            public void bind(SQLiteStatement statement, SegmentEntity entity) {
                Intrinsics.checkNotNullParameter(statement, "statement");
                Intrinsics.checkNotNullParameter(entity, "entity");
                statement.mo7910bindText(1, entity.getId());
                statement.mo7910bindText(2, entity.getSessionId());
                statement.mo7910bindText(3, entity.getFilePath());
                statement.mo7908bindLong(4, entity.getStartedAtMs());
                statement.mo7908bindLong(5, entity.getEndedAtMs());
                statement.mo7908bindLong(6, entity.getDurationMs());
                statement.mo7908bindLong(7, entity.getByteSize());
                statement.mo7910bindText(8, entity.getTranscriptStatus());
                String transcript = entity.getTranscript();
                if (transcript == null) {
                    statement.mo7909bindNull(9);
                } else {
                    statement.mo7910bindText(9, transcript);
                }
                String diarizedTranscript = entity.getDiarizedTranscript();
                if (diarizedTranscript == null) {
                    statement.mo7909bindNull(10);
                } else {
                    statement.mo7910bindText(10, diarizedTranscript);
                }
                String cleanedTranscript = entity.getCleanedTranscript();
                if (cleanedTranscript == null) {
                    statement.mo7909bindNull(11);
                } else {
                    statement.mo7910bindText(11, cleanedTranscript);
                }
                String asrProvider = entity.getAsrProvider();
                if (asrProvider == null) {
                    statement.mo7909bindNull(12);
                } else {
                    statement.mo7910bindText(12, asrProvider);
                }
                String cleanupProvider = entity.getCleanupProvider();
                if (cleanupProvider == null) {
                    statement.mo7909bindNull(13);
                } else {
                    statement.mo7910bindText(13, cleanupProvider);
                }
                String meetingId = entity.getMeetingId();
                if (meetingId == null) {
                    statement.mo7909bindNull(14);
                } else {
                    statement.mo7910bindText(14, meetingId);
                }
                String asrLastError = entity.getAsrLastError();
                if (asrLastError == null) {
                    statement.mo7909bindNull(15);
                } else {
                    statement.mo7910bindText(15, asrLastError);
                }
                String skipReason = entity.getSkipReason();
                if (skipReason == null) {
                    statement.mo7909bindNull(16);
                } else {
                    statement.mo7910bindText(16, skipReason);
                }
                statement.mo7908bindLong(17, entity.getUpdatedAtMs());
                String speakerCandidatesJson = entity.getSpeakerCandidatesJson();
                if (speakerCandidatesJson == null) {
                    statement.mo7909bindNull(18);
                } else {
                    statement.mo7910bindText(18, speakerCandidatesJson);
                }
                statement.mo7908bindLong(19, entity.getYouConfirmed() ? 1L : 0L);
                String endReason = entity.getEndReason();
                if (endReason == null) {
                    statement.mo7909bindNull(20);
                } else {
                    statement.mo7910bindText(20, endReason);
                }
                statement.mo7910bindText(21, entity.getId());
            }
        };
    }

    @Override // com.varun.pocketassistant.data.SegmentDao
    public Object insert(final SegmentEntity segment, Continuation<? super Unit> continuation) {
        Object objPerformSuspending = DBUtil.performSuspending(this.__db, false, true, new Function1() { // from class: com.varun.pocketassistant.data.SegmentDao_Impl$$ExternalSyntheticLambda18
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return SegmentDao_Impl.insert$lambda$0(this.f$0, segment, (SQLiteConnection) obj);
            }
        }, continuation);
        return objPerformSuspending == IntrinsicsKt.getCOROUTINE_SUSPENDED() ? objPerformSuspending : Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Unit insert$lambda$0(SegmentDao_Impl this$0, SegmentEntity $segment, SQLiteConnection _connection) throws Exception {
        Intrinsics.checkNotNullParameter(_connection, "_connection");
        this$0.__insertAdapterOfSegmentEntity.insert(_connection, $segment);
        return Unit.INSTANCE;
    }

    @Override // com.varun.pocketassistant.data.SegmentDao
    public Object update(final SegmentEntity segment, Continuation<? super Unit> continuation) {
        Object objPerformSuspending = DBUtil.performSuspending(this.__db, false, true, new Function1() { // from class: com.varun.pocketassistant.data.SegmentDao_Impl$$ExternalSyntheticLambda16
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return SegmentDao_Impl.update$lambda$1(this.f$0, segment, (SQLiteConnection) obj);
            }
        }, continuation);
        return objPerformSuspending == IntrinsicsKt.getCOROUTINE_SUSPENDED() ? objPerformSuspending : Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Unit update$lambda$1(SegmentDao_Impl this$0, SegmentEntity $segment, SQLiteConnection _connection) throws Exception {
        Intrinsics.checkNotNullParameter(_connection, "_connection");
        this$0.__updateAdapterOfSegmentEntity.handle(_connection, $segment);
        return Unit.INSTANCE;
    }

    @Override // com.varun.pocketassistant.data.SegmentDao
    public Flow<List<SegmentEntity>> observeForSession(final String sessionId) {
        Intrinsics.checkNotNullParameter(sessionId, "sessionId");
        final String _sql = "SELECT * FROM segments WHERE sessionId = ? ORDER BY startedAtMs ASC";
        return FlowUtil.createFlow(this.__db, false, new String[]{"segments"}, new Function1() { // from class: com.varun.pocketassistant.data.SegmentDao_Impl$$ExternalSyntheticLambda2
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return SegmentDao_Impl.observeForSession$lambda$2(_sql, sessionId, (SQLiteConnection) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final List observeForSession$lambda$2(String $_sql, String $sessionId, SQLiteConnection _connection) {
        String _tmpTranscript;
        String _tmpDiarizedTranscript;
        String _tmpCleanedTranscript;
        String _tmpAsrProvider;
        String _tmpCleanupProvider;
        String _tmpMeetingId;
        String _tmpAsrLastError;
        String _tmpSkipReason;
        String _tmpSpeakerCandidatesJson;
        String _tmpEndReason;
        Intrinsics.checkNotNullParameter(_connection, "_connection");
        SQLiteStatement _stmt = _connection.prepare($_sql);
        try {
            _stmt.mo7910bindText(1, $sessionId);
            int _columnIndexOfId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "id");
            int _columnIndexOfSessionId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "sessionId");
            int _columnIndexOfFilePath = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "filePath");
            int _columnIndexOfStartedAtMs = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "startedAtMs");
            int _columnIndexOfEndedAtMs = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "endedAtMs");
            int _columnIndexOfDurationMs = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "durationMs");
            int _columnIndexOfByteSize = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "byteSize");
            int _columnIndexOfTranscriptStatus = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "transcriptStatus");
            int _columnIndexOfTranscript = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "transcript");
            int _columnIndexOfDiarizedTranscript = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "diarizedTranscript");
            int _columnIndexOfCleanedTranscript = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "cleanedTranscript");
            int _columnIndexOfAsrProvider = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "asrProvider");
            int _tmp = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "cleanupProvider");
            int _columnIndexOfMeetingId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, MeetingStageWorker.KEY_MEETING_ID);
            int _columnIndexOfYouConfirmed = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "asrLastError");
            int _columnIndexOfSkipReason = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "skipReason");
            int _columnIndexOfSkipReason2 = _columnIndexOfSkipReason;
            int _columnIndexOfUpdatedAtMs = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "updatedAtMs");
            int _columnIndexOfUpdatedAtMs2 = _columnIndexOfUpdatedAtMs;
            int _columnIndexOfSpeakerCandidatesJson = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "speakerCandidatesJson");
            int _columnIndexOfSpeakerCandidatesJson2 = _columnIndexOfSpeakerCandidatesJson;
            int _columnIndexOfYouConfirmed2 = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "youConfirmed");
            int _columnIndexOfCleanupProvider = _columnIndexOfYouConfirmed2;
            int _columnIndexOfEndReason = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "endReason");
            List _result = new ArrayList();
            while (_stmt.step()) {
                String _tmpId = _stmt.getText(_columnIndexOfId);
                String _tmpSessionId = _stmt.getText(_columnIndexOfSessionId);
                String _tmpFilePath = _stmt.getText(_columnIndexOfFilePath);
                long _tmpStartedAtMs = _stmt.getLong(_columnIndexOfStartedAtMs);
                long _tmpEndedAtMs = _stmt.getLong(_columnIndexOfEndedAtMs);
                long _tmpDurationMs = _stmt.getLong(_columnIndexOfDurationMs);
                long _tmpByteSize = _stmt.getLong(_columnIndexOfByteSize);
                String _tmpTranscriptStatus = _stmt.getText(_columnIndexOfTranscriptStatus);
                if (_stmt.isNull(_columnIndexOfTranscript)) {
                    _tmpTranscript = null;
                } else {
                    String _tmpTranscript2 = _stmt.getText(_columnIndexOfTranscript);
                    _tmpTranscript = _tmpTranscript2;
                }
                if (_stmt.isNull(_columnIndexOfDiarizedTranscript)) {
                    _tmpDiarizedTranscript = null;
                } else {
                    String _tmpDiarizedTranscript2 = _stmt.getText(_columnIndexOfDiarizedTranscript);
                    _tmpDiarizedTranscript = _tmpDiarizedTranscript2;
                }
                if (_stmt.isNull(_columnIndexOfCleanedTranscript)) {
                    _tmpCleanedTranscript = null;
                } else {
                    String _tmpCleanedTranscript2 = _stmt.getText(_columnIndexOfCleanedTranscript);
                    _tmpCleanedTranscript = _tmpCleanedTranscript2;
                }
                if (_stmt.isNull(_columnIndexOfAsrProvider)) {
                    _tmpAsrProvider = null;
                } else {
                    String _tmpAsrProvider2 = _stmt.getText(_columnIndexOfAsrProvider);
                    _tmpAsrProvider = _tmpAsrProvider2;
                }
                if (_stmt.isNull(_tmp)) {
                    _tmpCleanupProvider = null;
                } else {
                    String _tmpCleanupProvider2 = _stmt.getText(_tmp);
                    _tmpCleanupProvider = _tmpCleanupProvider2;
                }
                if (_stmt.isNull(_columnIndexOfMeetingId)) {
                    _tmpMeetingId = null;
                } else {
                    String _tmpMeetingId2 = _stmt.getText(_columnIndexOfMeetingId);
                    _tmpMeetingId = _tmpMeetingId2;
                }
                int _columnIndexOfAsrProvider2 = _columnIndexOfAsrProvider;
                int _columnIndexOfAsrProvider3 = _columnIndexOfYouConfirmed;
                if (_stmt.isNull(_columnIndexOfAsrProvider3)) {
                    _tmpAsrLastError = null;
                } else {
                    String _tmpAsrLastError2 = _stmt.getText(_columnIndexOfAsrProvider3);
                    _tmpAsrLastError = _tmpAsrLastError2;
                }
                int _columnIndexOfAsrLastError = _columnIndexOfSkipReason2;
                if (_stmt.isNull(_columnIndexOfAsrLastError)) {
                    _tmpSkipReason = null;
                } else {
                    String _tmpSkipReason2 = _stmt.getText(_columnIndexOfAsrLastError);
                    _tmpSkipReason = _tmpSkipReason2;
                }
                int _columnIndexOfSkipReason3 = _columnIndexOfUpdatedAtMs2;
                long _tmpUpdatedAtMs = _stmt.getLong(_columnIndexOfSkipReason3);
                _columnIndexOfUpdatedAtMs2 = _columnIndexOfSkipReason3;
                int _columnIndexOfUpdatedAtMs3 = _columnIndexOfSpeakerCandidatesJson2;
                if (_stmt.isNull(_columnIndexOfUpdatedAtMs3)) {
                    _tmpSpeakerCandidatesJson = null;
                } else {
                    String _tmpSpeakerCandidatesJson2 = _stmt.getText(_columnIndexOfUpdatedAtMs3);
                    _tmpSpeakerCandidatesJson = _tmpSpeakerCandidatesJson2;
                }
                _columnIndexOfSpeakerCandidatesJson2 = _columnIndexOfUpdatedAtMs3;
                int _columnIndexOfMeetingId2 = _columnIndexOfMeetingId;
                int _columnIndexOfSpeakerCandidatesJson3 = _columnIndexOfCleanupProvider;
                int _columnIndexOfYouConfirmed3 = _tmp;
                int _tmp2 = (int) _stmt.getLong(_columnIndexOfSpeakerCandidatesJson3);
                boolean _tmpYouConfirmed = _tmp2 != 0;
                if (_stmt.isNull(_columnIndexOfEndReason)) {
                    _tmpEndReason = null;
                } else {
                    String _tmpEndReason2 = _stmt.getText(_columnIndexOfEndReason);
                    _tmpEndReason = _tmpEndReason2;
                }
                SegmentEntity _item = new SegmentEntity(_tmpId, _tmpSessionId, _tmpFilePath, _tmpStartedAtMs, _tmpEndedAtMs, _tmpDurationMs, _tmpByteSize, _tmpTranscriptStatus, _tmpTranscript, _tmpDiarizedTranscript, _tmpCleanedTranscript, _tmpAsrProvider, _tmpCleanupProvider, _tmpMeetingId, _tmpAsrLastError, _tmpSkipReason, _tmpUpdatedAtMs, _tmpSpeakerCandidatesJson, _tmpYouConfirmed, _tmpEndReason);
                List _result2 = _result;
                _result2.add(_item);
                _result = _result2;
                _tmp = _columnIndexOfYouConfirmed3;
                _columnIndexOfAsrProvider = _columnIndexOfAsrProvider2;
                _columnIndexOfSkipReason2 = _columnIndexOfAsrLastError;
                _columnIndexOfMeetingId = _columnIndexOfMeetingId2;
                _columnIndexOfCleanupProvider = _columnIndexOfSpeakerCandidatesJson3;
                _columnIndexOfYouConfirmed = _columnIndexOfAsrProvider3;
            }
            return _result;
        } finally {
            _stmt.close();
        }
    }

    @Override // com.varun.pocketassistant.data.SegmentDao
    public Object getForSession(final String sessionId, Continuation<? super List<SegmentEntity>> continuation) {
        final String _sql = "SELECT * FROM segments WHERE sessionId = ? ORDER BY startedAtMs ASC";
        return DBUtil.performSuspending(this.__db, true, false, new Function1() { // from class: com.varun.pocketassistant.data.SegmentDao_Impl$$ExternalSyntheticLambda15
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return SegmentDao_Impl.getForSession$lambda$3(_sql, sessionId, (SQLiteConnection) obj);
            }
        }, continuation);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final List getForSession$lambda$3(String $_sql, String $sessionId, SQLiteConnection _connection) {
        String _tmpTranscript;
        String _tmpDiarizedTranscript;
        String _tmpCleanedTranscript;
        String _tmpAsrProvider;
        String _tmpCleanupProvider;
        String _tmpMeetingId;
        String _tmpAsrLastError;
        String _tmpSkipReason;
        String _tmpSpeakerCandidatesJson;
        String _tmpEndReason;
        Intrinsics.checkNotNullParameter(_connection, "_connection");
        SQLiteStatement _stmt = _connection.prepare($_sql);
        try {
            _stmt.mo7910bindText(1, $sessionId);
            int _columnIndexOfId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "id");
            int _columnIndexOfSessionId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "sessionId");
            int _columnIndexOfFilePath = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "filePath");
            int _columnIndexOfStartedAtMs = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "startedAtMs");
            int _columnIndexOfEndedAtMs = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "endedAtMs");
            int _columnIndexOfDurationMs = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "durationMs");
            int _columnIndexOfByteSize = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "byteSize");
            int _columnIndexOfTranscriptStatus = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "transcriptStatus");
            int _columnIndexOfTranscript = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "transcript");
            int _columnIndexOfDiarizedTranscript = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "diarizedTranscript");
            int _columnIndexOfCleanedTranscript = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "cleanedTranscript");
            int _columnIndexOfAsrProvider = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "asrProvider");
            int _tmp = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "cleanupProvider");
            int _columnIndexOfMeetingId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, MeetingStageWorker.KEY_MEETING_ID);
            int _columnIndexOfYouConfirmed = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "asrLastError");
            int _columnIndexOfSkipReason = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "skipReason");
            int _columnIndexOfSkipReason2 = _columnIndexOfSkipReason;
            int _columnIndexOfUpdatedAtMs = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "updatedAtMs");
            int _columnIndexOfUpdatedAtMs2 = _columnIndexOfUpdatedAtMs;
            int _columnIndexOfSpeakerCandidatesJson = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "speakerCandidatesJson");
            int _columnIndexOfSpeakerCandidatesJson2 = _columnIndexOfSpeakerCandidatesJson;
            int _columnIndexOfYouConfirmed2 = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "youConfirmed");
            int _columnIndexOfCleanupProvider = _columnIndexOfYouConfirmed2;
            int _columnIndexOfEndReason = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "endReason");
            List _result = new ArrayList();
            while (_stmt.step()) {
                String _tmpId = _stmt.getText(_columnIndexOfId);
                String _tmpSessionId = _stmt.getText(_columnIndexOfSessionId);
                String _tmpFilePath = _stmt.getText(_columnIndexOfFilePath);
                long _tmpStartedAtMs = _stmt.getLong(_columnIndexOfStartedAtMs);
                long _tmpEndedAtMs = _stmt.getLong(_columnIndexOfEndedAtMs);
                long _tmpDurationMs = _stmt.getLong(_columnIndexOfDurationMs);
                long _tmpByteSize = _stmt.getLong(_columnIndexOfByteSize);
                String _tmpTranscriptStatus = _stmt.getText(_columnIndexOfTranscriptStatus);
                if (_stmt.isNull(_columnIndexOfTranscript)) {
                    _tmpTranscript = null;
                } else {
                    String _tmpTranscript2 = _stmt.getText(_columnIndexOfTranscript);
                    _tmpTranscript = _tmpTranscript2;
                }
                if (_stmt.isNull(_columnIndexOfDiarizedTranscript)) {
                    _tmpDiarizedTranscript = null;
                } else {
                    String _tmpDiarizedTranscript2 = _stmt.getText(_columnIndexOfDiarizedTranscript);
                    _tmpDiarizedTranscript = _tmpDiarizedTranscript2;
                }
                if (_stmt.isNull(_columnIndexOfCleanedTranscript)) {
                    _tmpCleanedTranscript = null;
                } else {
                    String _tmpCleanedTranscript2 = _stmt.getText(_columnIndexOfCleanedTranscript);
                    _tmpCleanedTranscript = _tmpCleanedTranscript2;
                }
                if (_stmt.isNull(_columnIndexOfAsrProvider)) {
                    _tmpAsrProvider = null;
                } else {
                    String _tmpAsrProvider2 = _stmt.getText(_columnIndexOfAsrProvider);
                    _tmpAsrProvider = _tmpAsrProvider2;
                }
                if (_stmt.isNull(_tmp)) {
                    _tmpCleanupProvider = null;
                } else {
                    String _tmpCleanupProvider2 = _stmt.getText(_tmp);
                    _tmpCleanupProvider = _tmpCleanupProvider2;
                }
                if (_stmt.isNull(_columnIndexOfMeetingId)) {
                    _tmpMeetingId = null;
                } else {
                    String _tmpMeetingId2 = _stmt.getText(_columnIndexOfMeetingId);
                    _tmpMeetingId = _tmpMeetingId2;
                }
                int _columnIndexOfAsrProvider2 = _columnIndexOfAsrProvider;
                int _columnIndexOfAsrProvider3 = _columnIndexOfYouConfirmed;
                if (_stmt.isNull(_columnIndexOfAsrProvider3)) {
                    _tmpAsrLastError = null;
                } else {
                    String _tmpAsrLastError2 = _stmt.getText(_columnIndexOfAsrProvider3);
                    _tmpAsrLastError = _tmpAsrLastError2;
                }
                int _columnIndexOfAsrLastError = _columnIndexOfSkipReason2;
                if (_stmt.isNull(_columnIndexOfAsrLastError)) {
                    _tmpSkipReason = null;
                } else {
                    String _tmpSkipReason2 = _stmt.getText(_columnIndexOfAsrLastError);
                    _tmpSkipReason = _tmpSkipReason2;
                }
                int _columnIndexOfSkipReason3 = _columnIndexOfUpdatedAtMs2;
                long _tmpUpdatedAtMs = _stmt.getLong(_columnIndexOfSkipReason3);
                _columnIndexOfUpdatedAtMs2 = _columnIndexOfSkipReason3;
                int _columnIndexOfUpdatedAtMs3 = _columnIndexOfSpeakerCandidatesJson2;
                if (_stmt.isNull(_columnIndexOfUpdatedAtMs3)) {
                    _tmpSpeakerCandidatesJson = null;
                } else {
                    String _tmpSpeakerCandidatesJson2 = _stmt.getText(_columnIndexOfUpdatedAtMs3);
                    _tmpSpeakerCandidatesJson = _tmpSpeakerCandidatesJson2;
                }
                _columnIndexOfSpeakerCandidatesJson2 = _columnIndexOfUpdatedAtMs3;
                int _columnIndexOfMeetingId2 = _columnIndexOfMeetingId;
                int _columnIndexOfSpeakerCandidatesJson3 = _columnIndexOfCleanupProvider;
                int _columnIndexOfYouConfirmed3 = _tmp;
                int _tmp2 = (int) _stmt.getLong(_columnIndexOfSpeakerCandidatesJson3);
                boolean _tmpYouConfirmed = _tmp2 != 0;
                if (_stmt.isNull(_columnIndexOfEndReason)) {
                    _tmpEndReason = null;
                } else {
                    String _tmpEndReason2 = _stmt.getText(_columnIndexOfEndReason);
                    _tmpEndReason = _tmpEndReason2;
                }
                SegmentEntity _item = new SegmentEntity(_tmpId, _tmpSessionId, _tmpFilePath, _tmpStartedAtMs, _tmpEndedAtMs, _tmpDurationMs, _tmpByteSize, _tmpTranscriptStatus, _tmpTranscript, _tmpDiarizedTranscript, _tmpCleanedTranscript, _tmpAsrProvider, _tmpCleanupProvider, _tmpMeetingId, _tmpAsrLastError, _tmpSkipReason, _tmpUpdatedAtMs, _tmpSpeakerCandidatesJson, _tmpYouConfirmed, _tmpEndReason);
                List _result2 = _result;
                _result2.add(_item);
                _result = _result2;
                _tmp = _columnIndexOfYouConfirmed3;
                _columnIndexOfAsrProvider = _columnIndexOfAsrProvider2;
                _columnIndexOfSkipReason2 = _columnIndexOfAsrLastError;
                _columnIndexOfMeetingId = _columnIndexOfMeetingId2;
                _columnIndexOfCleanupProvider = _columnIndexOfSpeakerCandidatesJson3;
                _columnIndexOfYouConfirmed = _columnIndexOfAsrProvider3;
            }
            return _result;
        } finally {
            _stmt.close();
        }
    }

    @Override // com.varun.pocketassistant.data.SegmentDao
    public Flow<List<SegmentEntity>> observeAll() {
        final String _sql = "SELECT * FROM segments ORDER BY startedAtMs DESC";
        return FlowUtil.createFlow(this.__db, false, new String[]{"segments"}, new Function1() { // from class: com.varun.pocketassistant.data.SegmentDao_Impl$$ExternalSyntheticLambda4
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return SegmentDao_Impl.observeAll$lambda$4(_sql, (SQLiteConnection) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final List observeAll$lambda$4(String $_sql, SQLiteConnection _connection) {
        String _tmpTranscript;
        String _tmpDiarizedTranscript;
        String _tmpCleanedTranscript;
        String _tmpAsrProvider;
        String _tmpCleanupProvider;
        String _tmpMeetingId;
        String _tmpAsrLastError;
        String _tmpSkipReason;
        String _tmpSpeakerCandidatesJson;
        String _tmpEndReason;
        Intrinsics.checkNotNullParameter(_connection, "_connection");
        SQLiteStatement _stmt = _connection.prepare($_sql);
        try {
            int _columnIndexOfId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "id");
            int _tmp = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "sessionId");
            int _columnIndexOfFilePath = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "filePath");
            int _columnIndexOfStartedAtMs = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "startedAtMs");
            int _columnIndexOfEndedAtMs = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "endedAtMs");
            int _columnIndexOfDurationMs = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "durationMs");
            int _columnIndexOfByteSize = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "byteSize");
            int _columnIndexOfTranscriptStatus = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "transcriptStatus");
            int _columnIndexOfTranscript = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "transcript");
            int _columnIndexOfDiarizedTranscript = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "diarizedTranscript");
            int _columnIndexOfCleanedTranscript = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "cleanedTranscript");
            int _columnIndexOfAsrProvider = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "asrProvider");
            int _columnIndexOfCleanupProvider = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "cleanupProvider");
            int _columnIndexOfMeetingId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, MeetingStageWorker.KEY_MEETING_ID);
            int _columnIndexOfYouConfirmed = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "asrLastError");
            int _columnIndexOfSkipReason = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "skipReason");
            int _columnIndexOfSkipReason2 = _columnIndexOfSkipReason;
            int _columnIndexOfUpdatedAtMs = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "updatedAtMs");
            int _columnIndexOfUpdatedAtMs2 = _columnIndexOfUpdatedAtMs;
            int _columnIndexOfSpeakerCandidatesJson = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "speakerCandidatesJson");
            int _columnIndexOfSpeakerCandidatesJson2 = _columnIndexOfSpeakerCandidatesJson;
            int _columnIndexOfYouConfirmed2 = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "youConfirmed");
            int _columnIndexOfSessionId = _columnIndexOfYouConfirmed2;
            int _columnIndexOfEndReason = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "endReason");
            List _result = new ArrayList();
            while (_stmt.step()) {
                String _tmpId = _stmt.getText(_columnIndexOfId);
                String _tmpSessionId = _stmt.getText(_tmp);
                String _tmpFilePath = _stmt.getText(_columnIndexOfFilePath);
                long _tmpStartedAtMs = _stmt.getLong(_columnIndexOfStartedAtMs);
                long _tmpEndedAtMs = _stmt.getLong(_columnIndexOfEndedAtMs);
                long _tmpDurationMs = _stmt.getLong(_columnIndexOfDurationMs);
                long _tmpByteSize = _stmt.getLong(_columnIndexOfByteSize);
                String _tmpTranscriptStatus = _stmt.getText(_columnIndexOfTranscriptStatus);
                if (_stmt.isNull(_columnIndexOfTranscript)) {
                    _tmpTranscript = null;
                } else {
                    String _tmpTranscript2 = _stmt.getText(_columnIndexOfTranscript);
                    _tmpTranscript = _tmpTranscript2;
                }
                if (_stmt.isNull(_columnIndexOfDiarizedTranscript)) {
                    _tmpDiarizedTranscript = null;
                } else {
                    String _tmpDiarizedTranscript2 = _stmt.getText(_columnIndexOfDiarizedTranscript);
                    _tmpDiarizedTranscript = _tmpDiarizedTranscript2;
                }
                if (_stmt.isNull(_columnIndexOfCleanedTranscript)) {
                    _tmpCleanedTranscript = null;
                } else {
                    String _tmpCleanedTranscript2 = _stmt.getText(_columnIndexOfCleanedTranscript);
                    _tmpCleanedTranscript = _tmpCleanedTranscript2;
                }
                if (_stmt.isNull(_columnIndexOfAsrProvider)) {
                    _tmpAsrProvider = null;
                } else {
                    String _tmpAsrProvider2 = _stmt.getText(_columnIndexOfAsrProvider);
                    _tmpAsrProvider = _tmpAsrProvider2;
                }
                if (_stmt.isNull(_columnIndexOfCleanupProvider)) {
                    _tmpCleanupProvider = null;
                } else {
                    String _tmpCleanupProvider2 = _stmt.getText(_columnIndexOfCleanupProvider);
                    _tmpCleanupProvider = _tmpCleanupProvider2;
                }
                if (_stmt.isNull(_columnIndexOfMeetingId)) {
                    _tmpMeetingId = null;
                } else {
                    String _tmpMeetingId2 = _stmt.getText(_columnIndexOfMeetingId);
                    _tmpMeetingId = _tmpMeetingId2;
                }
                int _columnIndexOfId2 = _columnIndexOfId;
                int _columnIndexOfId3 = _columnIndexOfYouConfirmed;
                if (_stmt.isNull(_columnIndexOfId3)) {
                    _tmpAsrLastError = null;
                } else {
                    String _tmpAsrLastError2 = _stmt.getText(_columnIndexOfId3);
                    _tmpAsrLastError = _tmpAsrLastError2;
                }
                int _columnIndexOfAsrLastError = _columnIndexOfSkipReason2;
                if (_stmt.isNull(_columnIndexOfAsrLastError)) {
                    _tmpSkipReason = null;
                } else {
                    String _tmpSkipReason2 = _stmt.getText(_columnIndexOfAsrLastError);
                    _tmpSkipReason = _tmpSkipReason2;
                }
                int _columnIndexOfSkipReason3 = _columnIndexOfUpdatedAtMs2;
                long _tmpUpdatedAtMs = _stmt.getLong(_columnIndexOfSkipReason3);
                _columnIndexOfUpdatedAtMs2 = _columnIndexOfSkipReason3;
                int _columnIndexOfUpdatedAtMs3 = _columnIndexOfSpeakerCandidatesJson2;
                if (_stmt.isNull(_columnIndexOfUpdatedAtMs3)) {
                    _tmpSpeakerCandidatesJson = null;
                } else {
                    String _tmpSpeakerCandidatesJson2 = _stmt.getText(_columnIndexOfUpdatedAtMs3);
                    _tmpSpeakerCandidatesJson = _tmpSpeakerCandidatesJson2;
                }
                _columnIndexOfSpeakerCandidatesJson2 = _columnIndexOfUpdatedAtMs3;
                int _columnIndexOfFilePath2 = _columnIndexOfFilePath;
                int _columnIndexOfSpeakerCandidatesJson3 = _columnIndexOfSessionId;
                int _columnIndexOfYouConfirmed3 = _tmp;
                int _tmp2 = (int) _stmt.getLong(_columnIndexOfSpeakerCandidatesJson3);
                boolean _tmpYouConfirmed = _tmp2 != 0;
                if (_stmt.isNull(_columnIndexOfEndReason)) {
                    _tmpEndReason = null;
                } else {
                    String _tmpEndReason2 = _stmt.getText(_columnIndexOfEndReason);
                    _tmpEndReason = _tmpEndReason2;
                }
                SegmentEntity _item = new SegmentEntity(_tmpId, _tmpSessionId, _tmpFilePath, _tmpStartedAtMs, _tmpEndedAtMs, _tmpDurationMs, _tmpByteSize, _tmpTranscriptStatus, _tmpTranscript, _tmpDiarizedTranscript, _tmpCleanedTranscript, _tmpAsrProvider, _tmpCleanupProvider, _tmpMeetingId, _tmpAsrLastError, _tmpSkipReason, _tmpUpdatedAtMs, _tmpSpeakerCandidatesJson, _tmpYouConfirmed, _tmpEndReason);
                List _result2 = _result;
                _result2.add(_item);
                _result = _result2;
                _tmp = _columnIndexOfYouConfirmed3;
                _columnIndexOfId = _columnIndexOfId2;
                _columnIndexOfSkipReason2 = _columnIndexOfAsrLastError;
                _columnIndexOfFilePath = _columnIndexOfFilePath2;
                _columnIndexOfSessionId = _columnIndexOfSpeakerCandidatesJson3;
                _columnIndexOfYouConfirmed = _columnIndexOfId3;
            }
            return _result;
        } finally {
            _stmt.close();
        }
    }

    @Override // com.varun.pocketassistant.data.SegmentDao
    public Flow<List<SegmentEntity>> observeUncategorized() {
        final String _sql = "\n        SELECT * FROM segments\n        WHERE meetingId IS NULL\n        ORDER BY startedAtMs DESC\n        ";
        return FlowUtil.createFlow(this.__db, false, new String[]{"segments"}, new Function1() { // from class: com.varun.pocketassistant.data.SegmentDao_Impl$$ExternalSyntheticLambda10
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return SegmentDao_Impl.observeUncategorized$lambda$5(_sql, (SQLiteConnection) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final List observeUncategorized$lambda$5(String $_sql, SQLiteConnection _connection) {
        String _tmpTranscript;
        String _tmpDiarizedTranscript;
        String _tmpCleanedTranscript;
        String _tmpAsrProvider;
        String _tmpCleanupProvider;
        String _tmpMeetingId;
        String _tmpAsrLastError;
        String _tmpSkipReason;
        String _tmpSpeakerCandidatesJson;
        String _tmpEndReason;
        Intrinsics.checkNotNullParameter(_connection, "_connection");
        SQLiteStatement _stmt = _connection.prepare($_sql);
        try {
            int _columnIndexOfId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "id");
            int _tmp = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "sessionId");
            int _columnIndexOfFilePath = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "filePath");
            int _columnIndexOfStartedAtMs = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "startedAtMs");
            int _columnIndexOfEndedAtMs = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "endedAtMs");
            int _columnIndexOfDurationMs = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "durationMs");
            int _columnIndexOfByteSize = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "byteSize");
            int _columnIndexOfTranscriptStatus = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "transcriptStatus");
            int _columnIndexOfTranscript = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "transcript");
            int _columnIndexOfDiarizedTranscript = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "diarizedTranscript");
            int _columnIndexOfCleanedTranscript = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "cleanedTranscript");
            int _columnIndexOfAsrProvider = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "asrProvider");
            int _columnIndexOfCleanupProvider = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "cleanupProvider");
            int _columnIndexOfMeetingId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, MeetingStageWorker.KEY_MEETING_ID);
            int _columnIndexOfYouConfirmed = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "asrLastError");
            int _columnIndexOfSkipReason = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "skipReason");
            int _columnIndexOfSkipReason2 = _columnIndexOfSkipReason;
            int _columnIndexOfUpdatedAtMs = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "updatedAtMs");
            int _columnIndexOfUpdatedAtMs2 = _columnIndexOfUpdatedAtMs;
            int _columnIndexOfSpeakerCandidatesJson = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "speakerCandidatesJson");
            int _columnIndexOfSpeakerCandidatesJson2 = _columnIndexOfSpeakerCandidatesJson;
            int _columnIndexOfYouConfirmed2 = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "youConfirmed");
            int _columnIndexOfSessionId = _columnIndexOfYouConfirmed2;
            int _columnIndexOfEndReason = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "endReason");
            List _result = new ArrayList();
            while (_stmt.step()) {
                String _tmpId = _stmt.getText(_columnIndexOfId);
                String _tmpSessionId = _stmt.getText(_tmp);
                String _tmpFilePath = _stmt.getText(_columnIndexOfFilePath);
                long _tmpStartedAtMs = _stmt.getLong(_columnIndexOfStartedAtMs);
                long _tmpEndedAtMs = _stmt.getLong(_columnIndexOfEndedAtMs);
                long _tmpDurationMs = _stmt.getLong(_columnIndexOfDurationMs);
                long _tmpByteSize = _stmt.getLong(_columnIndexOfByteSize);
                String _tmpTranscriptStatus = _stmt.getText(_columnIndexOfTranscriptStatus);
                if (_stmt.isNull(_columnIndexOfTranscript)) {
                    _tmpTranscript = null;
                } else {
                    String _tmpTranscript2 = _stmt.getText(_columnIndexOfTranscript);
                    _tmpTranscript = _tmpTranscript2;
                }
                if (_stmt.isNull(_columnIndexOfDiarizedTranscript)) {
                    _tmpDiarizedTranscript = null;
                } else {
                    String _tmpDiarizedTranscript2 = _stmt.getText(_columnIndexOfDiarizedTranscript);
                    _tmpDiarizedTranscript = _tmpDiarizedTranscript2;
                }
                if (_stmt.isNull(_columnIndexOfCleanedTranscript)) {
                    _tmpCleanedTranscript = null;
                } else {
                    String _tmpCleanedTranscript2 = _stmt.getText(_columnIndexOfCleanedTranscript);
                    _tmpCleanedTranscript = _tmpCleanedTranscript2;
                }
                if (_stmt.isNull(_columnIndexOfAsrProvider)) {
                    _tmpAsrProvider = null;
                } else {
                    String _tmpAsrProvider2 = _stmt.getText(_columnIndexOfAsrProvider);
                    _tmpAsrProvider = _tmpAsrProvider2;
                }
                if (_stmt.isNull(_columnIndexOfCleanupProvider)) {
                    _tmpCleanupProvider = null;
                } else {
                    String _tmpCleanupProvider2 = _stmt.getText(_columnIndexOfCleanupProvider);
                    _tmpCleanupProvider = _tmpCleanupProvider2;
                }
                if (_stmt.isNull(_columnIndexOfMeetingId)) {
                    _tmpMeetingId = null;
                } else {
                    String _tmpMeetingId2 = _stmt.getText(_columnIndexOfMeetingId);
                    _tmpMeetingId = _tmpMeetingId2;
                }
                int _columnIndexOfId2 = _columnIndexOfId;
                int _columnIndexOfId3 = _columnIndexOfYouConfirmed;
                if (_stmt.isNull(_columnIndexOfId3)) {
                    _tmpAsrLastError = null;
                } else {
                    String _tmpAsrLastError2 = _stmt.getText(_columnIndexOfId3);
                    _tmpAsrLastError = _tmpAsrLastError2;
                }
                int _columnIndexOfAsrLastError = _columnIndexOfSkipReason2;
                if (_stmt.isNull(_columnIndexOfAsrLastError)) {
                    _tmpSkipReason = null;
                } else {
                    String _tmpSkipReason2 = _stmt.getText(_columnIndexOfAsrLastError);
                    _tmpSkipReason = _tmpSkipReason2;
                }
                int _columnIndexOfSkipReason3 = _columnIndexOfUpdatedAtMs2;
                long _tmpUpdatedAtMs = _stmt.getLong(_columnIndexOfSkipReason3);
                _columnIndexOfUpdatedAtMs2 = _columnIndexOfSkipReason3;
                int _columnIndexOfUpdatedAtMs3 = _columnIndexOfSpeakerCandidatesJson2;
                if (_stmt.isNull(_columnIndexOfUpdatedAtMs3)) {
                    _tmpSpeakerCandidatesJson = null;
                } else {
                    String _tmpSpeakerCandidatesJson2 = _stmt.getText(_columnIndexOfUpdatedAtMs3);
                    _tmpSpeakerCandidatesJson = _tmpSpeakerCandidatesJson2;
                }
                _columnIndexOfSpeakerCandidatesJson2 = _columnIndexOfUpdatedAtMs3;
                int _columnIndexOfFilePath2 = _columnIndexOfFilePath;
                int _columnIndexOfSpeakerCandidatesJson3 = _columnIndexOfSessionId;
                int _columnIndexOfYouConfirmed3 = _tmp;
                int _tmp2 = (int) _stmt.getLong(_columnIndexOfSpeakerCandidatesJson3);
                boolean _tmpYouConfirmed = _tmp2 != 0;
                if (_stmt.isNull(_columnIndexOfEndReason)) {
                    _tmpEndReason = null;
                } else {
                    String _tmpEndReason2 = _stmt.getText(_columnIndexOfEndReason);
                    _tmpEndReason = _tmpEndReason2;
                }
                SegmentEntity _item = new SegmentEntity(_tmpId, _tmpSessionId, _tmpFilePath, _tmpStartedAtMs, _tmpEndedAtMs, _tmpDurationMs, _tmpByteSize, _tmpTranscriptStatus, _tmpTranscript, _tmpDiarizedTranscript, _tmpCleanedTranscript, _tmpAsrProvider, _tmpCleanupProvider, _tmpMeetingId, _tmpAsrLastError, _tmpSkipReason, _tmpUpdatedAtMs, _tmpSpeakerCandidatesJson, _tmpYouConfirmed, _tmpEndReason);
                List _result2 = _result;
                _result2.add(_item);
                _result = _result2;
                _tmp = _columnIndexOfYouConfirmed3;
                _columnIndexOfId = _columnIndexOfId2;
                _columnIndexOfSkipReason2 = _columnIndexOfAsrLastError;
                _columnIndexOfFilePath = _columnIndexOfFilePath2;
                _columnIndexOfSessionId = _columnIndexOfSpeakerCandidatesJson3;
                _columnIndexOfYouConfirmed = _columnIndexOfId3;
            }
            return _result;
        } finally {
            _stmt.close();
        }
    }

    @Override // com.varun.pocketassistant.data.SegmentDao
    public Object getAll(Continuation<? super List<SegmentEntity>> continuation) {
        final String _sql = "SELECT * FROM segments ORDER BY startedAtMs DESC";
        return DBUtil.performSuspending(this.__db, true, false, new Function1() { // from class: com.varun.pocketassistant.data.SegmentDao_Impl$$ExternalSyntheticLambda14
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return SegmentDao_Impl.getAll$lambda$6(_sql, (SQLiteConnection) obj);
            }
        }, continuation);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final List getAll$lambda$6(String $_sql, SQLiteConnection _connection) {
        String _tmpTranscript;
        String _tmpDiarizedTranscript;
        String _tmpCleanedTranscript;
        String _tmpAsrProvider;
        String _tmpCleanupProvider;
        String _tmpMeetingId;
        String _tmpAsrLastError;
        String _tmpSkipReason;
        String _tmpSpeakerCandidatesJson;
        String _tmpEndReason;
        Intrinsics.checkNotNullParameter(_connection, "_connection");
        SQLiteStatement _stmt = _connection.prepare($_sql);
        try {
            int _columnIndexOfId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "id");
            int _tmp = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "sessionId");
            int _columnIndexOfFilePath = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "filePath");
            int _columnIndexOfStartedAtMs = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "startedAtMs");
            int _columnIndexOfEndedAtMs = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "endedAtMs");
            int _columnIndexOfDurationMs = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "durationMs");
            int _columnIndexOfByteSize = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "byteSize");
            int _columnIndexOfTranscriptStatus = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "transcriptStatus");
            int _columnIndexOfTranscript = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "transcript");
            int _columnIndexOfDiarizedTranscript = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "diarizedTranscript");
            int _columnIndexOfCleanedTranscript = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "cleanedTranscript");
            int _columnIndexOfAsrProvider = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "asrProvider");
            int _columnIndexOfCleanupProvider = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "cleanupProvider");
            int _columnIndexOfMeetingId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, MeetingStageWorker.KEY_MEETING_ID);
            int _columnIndexOfYouConfirmed = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "asrLastError");
            int _columnIndexOfSkipReason = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "skipReason");
            int _columnIndexOfSkipReason2 = _columnIndexOfSkipReason;
            int _columnIndexOfUpdatedAtMs = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "updatedAtMs");
            int _columnIndexOfUpdatedAtMs2 = _columnIndexOfUpdatedAtMs;
            int _columnIndexOfSpeakerCandidatesJson = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "speakerCandidatesJson");
            int _columnIndexOfSpeakerCandidatesJson2 = _columnIndexOfSpeakerCandidatesJson;
            int _columnIndexOfYouConfirmed2 = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "youConfirmed");
            int _columnIndexOfSessionId = _columnIndexOfYouConfirmed2;
            int _columnIndexOfEndReason = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "endReason");
            List _result = new ArrayList();
            while (_stmt.step()) {
                String _tmpId = _stmt.getText(_columnIndexOfId);
                String _tmpSessionId = _stmt.getText(_tmp);
                String _tmpFilePath = _stmt.getText(_columnIndexOfFilePath);
                long _tmpStartedAtMs = _stmt.getLong(_columnIndexOfStartedAtMs);
                long _tmpEndedAtMs = _stmt.getLong(_columnIndexOfEndedAtMs);
                long _tmpDurationMs = _stmt.getLong(_columnIndexOfDurationMs);
                long _tmpByteSize = _stmt.getLong(_columnIndexOfByteSize);
                String _tmpTranscriptStatus = _stmt.getText(_columnIndexOfTranscriptStatus);
                if (_stmt.isNull(_columnIndexOfTranscript)) {
                    _tmpTranscript = null;
                } else {
                    String _tmpTranscript2 = _stmt.getText(_columnIndexOfTranscript);
                    _tmpTranscript = _tmpTranscript2;
                }
                if (_stmt.isNull(_columnIndexOfDiarizedTranscript)) {
                    _tmpDiarizedTranscript = null;
                } else {
                    String _tmpDiarizedTranscript2 = _stmt.getText(_columnIndexOfDiarizedTranscript);
                    _tmpDiarizedTranscript = _tmpDiarizedTranscript2;
                }
                if (_stmt.isNull(_columnIndexOfCleanedTranscript)) {
                    _tmpCleanedTranscript = null;
                } else {
                    String _tmpCleanedTranscript2 = _stmt.getText(_columnIndexOfCleanedTranscript);
                    _tmpCleanedTranscript = _tmpCleanedTranscript2;
                }
                if (_stmt.isNull(_columnIndexOfAsrProvider)) {
                    _tmpAsrProvider = null;
                } else {
                    String _tmpAsrProvider2 = _stmt.getText(_columnIndexOfAsrProvider);
                    _tmpAsrProvider = _tmpAsrProvider2;
                }
                if (_stmt.isNull(_columnIndexOfCleanupProvider)) {
                    _tmpCleanupProvider = null;
                } else {
                    String _tmpCleanupProvider2 = _stmt.getText(_columnIndexOfCleanupProvider);
                    _tmpCleanupProvider = _tmpCleanupProvider2;
                }
                if (_stmt.isNull(_columnIndexOfMeetingId)) {
                    _tmpMeetingId = null;
                } else {
                    String _tmpMeetingId2 = _stmt.getText(_columnIndexOfMeetingId);
                    _tmpMeetingId = _tmpMeetingId2;
                }
                int _columnIndexOfId2 = _columnIndexOfId;
                int _columnIndexOfId3 = _columnIndexOfYouConfirmed;
                if (_stmt.isNull(_columnIndexOfId3)) {
                    _tmpAsrLastError = null;
                } else {
                    String _tmpAsrLastError2 = _stmt.getText(_columnIndexOfId3);
                    _tmpAsrLastError = _tmpAsrLastError2;
                }
                int _columnIndexOfAsrLastError = _columnIndexOfSkipReason2;
                if (_stmt.isNull(_columnIndexOfAsrLastError)) {
                    _tmpSkipReason = null;
                } else {
                    String _tmpSkipReason2 = _stmt.getText(_columnIndexOfAsrLastError);
                    _tmpSkipReason = _tmpSkipReason2;
                }
                int _columnIndexOfSkipReason3 = _columnIndexOfUpdatedAtMs2;
                long _tmpUpdatedAtMs = _stmt.getLong(_columnIndexOfSkipReason3);
                _columnIndexOfUpdatedAtMs2 = _columnIndexOfSkipReason3;
                int _columnIndexOfUpdatedAtMs3 = _columnIndexOfSpeakerCandidatesJson2;
                if (_stmt.isNull(_columnIndexOfUpdatedAtMs3)) {
                    _tmpSpeakerCandidatesJson = null;
                } else {
                    String _tmpSpeakerCandidatesJson2 = _stmt.getText(_columnIndexOfUpdatedAtMs3);
                    _tmpSpeakerCandidatesJson = _tmpSpeakerCandidatesJson2;
                }
                _columnIndexOfSpeakerCandidatesJson2 = _columnIndexOfUpdatedAtMs3;
                int _columnIndexOfFilePath2 = _columnIndexOfFilePath;
                int _columnIndexOfSpeakerCandidatesJson3 = _columnIndexOfSessionId;
                int _columnIndexOfYouConfirmed3 = _tmp;
                int _tmp2 = (int) _stmt.getLong(_columnIndexOfSpeakerCandidatesJson3);
                boolean _tmpYouConfirmed = _tmp2 != 0;
                if (_stmt.isNull(_columnIndexOfEndReason)) {
                    _tmpEndReason = null;
                } else {
                    String _tmpEndReason2 = _stmt.getText(_columnIndexOfEndReason);
                    _tmpEndReason = _tmpEndReason2;
                }
                SegmentEntity _item = new SegmentEntity(_tmpId, _tmpSessionId, _tmpFilePath, _tmpStartedAtMs, _tmpEndedAtMs, _tmpDurationMs, _tmpByteSize, _tmpTranscriptStatus, _tmpTranscript, _tmpDiarizedTranscript, _tmpCleanedTranscript, _tmpAsrProvider, _tmpCleanupProvider, _tmpMeetingId, _tmpAsrLastError, _tmpSkipReason, _tmpUpdatedAtMs, _tmpSpeakerCandidatesJson, _tmpYouConfirmed, _tmpEndReason);
                List _result2 = _result;
                _result2.add(_item);
                _result = _result2;
                _tmp = _columnIndexOfYouConfirmed3;
                _columnIndexOfId = _columnIndexOfId2;
                _columnIndexOfSkipReason2 = _columnIndexOfAsrLastError;
                _columnIndexOfFilePath = _columnIndexOfFilePath2;
                _columnIndexOfSessionId = _columnIndexOfSpeakerCandidatesJson3;
                _columnIndexOfYouConfirmed = _columnIndexOfId3;
            }
            return _result;
        } finally {
            _stmt.close();
        }
    }

    @Override // com.varun.pocketassistant.data.SegmentDao
    public Object getOverlapping(final long startMs, final long endMs, Continuation<? super List<SegmentEntity>> continuation) {
        final String _sql = "\n        SELECT * FROM segments\n        WHERE startedAtMs < ? AND endedAtMs > ?\n        ORDER BY startedAtMs ASC\n        ";
        return DBUtil.performSuspending(this.__db, true, false, new Function1() { // from class: com.varun.pocketassistant.data.SegmentDao_Impl$$ExternalSyntheticLambda17
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return SegmentDao_Impl.getOverlapping$lambda$7(_sql, endMs, startMs, (SQLiteConnection) obj);
            }
        }, continuation);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final List getOverlapping$lambda$7(String $_sql, long $endMs, long $startMs, SQLiteConnection _connection) {
        String _tmpTranscript;
        String _tmpDiarizedTranscript;
        String _tmpCleanedTranscript;
        String _tmpAsrProvider;
        String _tmpCleanupProvider;
        String _tmpMeetingId;
        String _tmpAsrLastError;
        String _tmpSkipReason;
        String _tmpSpeakerCandidatesJson;
        String _tmpEndReason;
        Intrinsics.checkNotNullParameter(_connection, "_connection");
        SQLiteStatement _stmt = _connection.prepare($_sql);
        try {
            _stmt.mo7908bindLong(1, $endMs);
            _stmt.mo7908bindLong(2, $startMs);
            int _columnIndexOfId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "id");
            int _columnIndexOfSessionId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "sessionId");
            int _columnIndexOfFilePath = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "filePath");
            int _columnIndexOfStartedAtMs = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "startedAtMs");
            int _columnIndexOfEndedAtMs = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "endedAtMs");
            int _columnIndexOfDurationMs = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "durationMs");
            int _columnIndexOfByteSize = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "byteSize");
            int _columnIndexOfTranscriptStatus = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "transcriptStatus");
            int _columnIndexOfTranscript = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "transcript");
            int _tmp = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "diarizedTranscript");
            int _columnIndexOfCleanedTranscript = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "cleanedTranscript");
            int _columnIndexOfAsrProvider = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "asrProvider");
            int _columnIndexOfCleanupProvider = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "cleanupProvider");
            int _columnIndexOfMeetingId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, MeetingStageWorker.KEY_MEETING_ID);
            int _columnIndexOfYouConfirmed = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "asrLastError");
            int _columnIndexOfSkipReason = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "skipReason");
            int _columnIndexOfSkipReason2 = _columnIndexOfSkipReason;
            int _columnIndexOfUpdatedAtMs = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "updatedAtMs");
            int _columnIndexOfUpdatedAtMs2 = _columnIndexOfUpdatedAtMs;
            int _columnIndexOfSpeakerCandidatesJson = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "speakerCandidatesJson");
            int _columnIndexOfSpeakerCandidatesJson2 = _columnIndexOfSpeakerCandidatesJson;
            int _columnIndexOfYouConfirmed2 = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "youConfirmed");
            int _columnIndexOfDiarizedTranscript = _columnIndexOfYouConfirmed2;
            int _columnIndexOfEndReason = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "endReason");
            List _result = new ArrayList();
            while (_stmt.step()) {
                String _tmpId = _stmt.getText(_columnIndexOfId);
                String _tmpSessionId = _stmt.getText(_columnIndexOfSessionId);
                String _tmpFilePath = _stmt.getText(_columnIndexOfFilePath);
                long _tmpStartedAtMs = _stmt.getLong(_columnIndexOfStartedAtMs);
                long _tmpEndedAtMs = _stmt.getLong(_columnIndexOfEndedAtMs);
                long _tmpDurationMs = _stmt.getLong(_columnIndexOfDurationMs);
                long _tmpByteSize = _stmt.getLong(_columnIndexOfByteSize);
                String _tmpTranscriptStatus = _stmt.getText(_columnIndexOfTranscriptStatus);
                if (_stmt.isNull(_columnIndexOfTranscript)) {
                    _tmpTranscript = null;
                } else {
                    String _tmpTranscript2 = _stmt.getText(_columnIndexOfTranscript);
                    _tmpTranscript = _tmpTranscript2;
                }
                if (_stmt.isNull(_tmp)) {
                    _tmpDiarizedTranscript = null;
                } else {
                    String _tmpDiarizedTranscript2 = _stmt.getText(_tmp);
                    _tmpDiarizedTranscript = _tmpDiarizedTranscript2;
                }
                if (_stmt.isNull(_columnIndexOfCleanedTranscript)) {
                    _tmpCleanedTranscript = null;
                } else {
                    String _tmpCleanedTranscript2 = _stmt.getText(_columnIndexOfCleanedTranscript);
                    _tmpCleanedTranscript = _tmpCleanedTranscript2;
                }
                if (_stmt.isNull(_columnIndexOfAsrProvider)) {
                    _tmpAsrProvider = null;
                } else {
                    String _tmpAsrProvider2 = _stmt.getText(_columnIndexOfAsrProvider);
                    _tmpAsrProvider = _tmpAsrProvider2;
                }
                if (_stmt.isNull(_columnIndexOfCleanupProvider)) {
                    _tmpCleanupProvider = null;
                } else {
                    String _tmpCleanupProvider2 = _stmt.getText(_columnIndexOfCleanupProvider);
                    _tmpCleanupProvider = _tmpCleanupProvider2;
                }
                if (_stmt.isNull(_columnIndexOfMeetingId)) {
                    _tmpMeetingId = null;
                } else {
                    String _tmpMeetingId2 = _stmt.getText(_columnIndexOfMeetingId);
                    _tmpMeetingId = _tmpMeetingId2;
                }
                int _columnIndexOfTranscript2 = _columnIndexOfTranscript;
                int _columnIndexOfTranscript3 = _columnIndexOfYouConfirmed;
                if (_stmt.isNull(_columnIndexOfTranscript3)) {
                    _tmpAsrLastError = null;
                } else {
                    String _tmpAsrLastError2 = _stmt.getText(_columnIndexOfTranscript3);
                    _tmpAsrLastError = _tmpAsrLastError2;
                }
                int _columnIndexOfAsrLastError = _columnIndexOfSkipReason2;
                if (_stmt.isNull(_columnIndexOfAsrLastError)) {
                    _tmpSkipReason = null;
                } else {
                    String _tmpSkipReason2 = _stmt.getText(_columnIndexOfAsrLastError);
                    _tmpSkipReason = _tmpSkipReason2;
                }
                int _columnIndexOfSkipReason3 = _columnIndexOfUpdatedAtMs2;
                long _tmpUpdatedAtMs = _stmt.getLong(_columnIndexOfSkipReason3);
                _columnIndexOfUpdatedAtMs2 = _columnIndexOfSkipReason3;
                int _columnIndexOfUpdatedAtMs3 = _columnIndexOfSpeakerCandidatesJson2;
                if (_stmt.isNull(_columnIndexOfUpdatedAtMs3)) {
                    _tmpSpeakerCandidatesJson = null;
                } else {
                    String _tmpSpeakerCandidatesJson2 = _stmt.getText(_columnIndexOfUpdatedAtMs3);
                    _tmpSpeakerCandidatesJson = _tmpSpeakerCandidatesJson2;
                }
                _columnIndexOfSpeakerCandidatesJson2 = _columnIndexOfUpdatedAtMs3;
                int _columnIndexOfCleanedTranscript2 = _columnIndexOfCleanedTranscript;
                int _columnIndexOfSpeakerCandidatesJson3 = _columnIndexOfDiarizedTranscript;
                int _columnIndexOfYouConfirmed3 = _tmp;
                int _tmp2 = (int) _stmt.getLong(_columnIndexOfSpeakerCandidatesJson3);
                boolean _tmpYouConfirmed = _tmp2 != 0;
                if (_stmt.isNull(_columnIndexOfEndReason)) {
                    _tmpEndReason = null;
                } else {
                    String _tmpEndReason2 = _stmt.getText(_columnIndexOfEndReason);
                    _tmpEndReason = _tmpEndReason2;
                }
                SegmentEntity _item = new SegmentEntity(_tmpId, _tmpSessionId, _tmpFilePath, _tmpStartedAtMs, _tmpEndedAtMs, _tmpDurationMs, _tmpByteSize, _tmpTranscriptStatus, _tmpTranscript, _tmpDiarizedTranscript, _tmpCleanedTranscript, _tmpAsrProvider, _tmpCleanupProvider, _tmpMeetingId, _tmpAsrLastError, _tmpSkipReason, _tmpUpdatedAtMs, _tmpSpeakerCandidatesJson, _tmpYouConfirmed, _tmpEndReason);
                List _result2 = _result;
                _result2.add(_item);
                _result = _result2;
                _tmp = _columnIndexOfYouConfirmed3;
                _columnIndexOfTranscript = _columnIndexOfTranscript2;
                _columnIndexOfSkipReason2 = _columnIndexOfAsrLastError;
                _columnIndexOfCleanedTranscript = _columnIndexOfCleanedTranscript2;
                _columnIndexOfDiarizedTranscript = _columnIndexOfSpeakerCandidatesJson3;
                _columnIndexOfYouConfirmed = _columnIndexOfTranscript3;
            }
            return _result;
        } finally {
            _stmt.close();
        }
    }

    @Override // com.varun.pocketassistant.data.SegmentDao
    public Flow<List<SegmentEntity>> observeForMeeting(final String meetingId) {
        Intrinsics.checkNotNullParameter(meetingId, "meetingId");
        final String _sql = "SELECT * FROM segments WHERE meetingId = ? ORDER BY startedAtMs ASC";
        return FlowUtil.createFlow(this.__db, false, new String[]{"segments"}, new Function1() { // from class: com.varun.pocketassistant.data.SegmentDao_Impl$$ExternalSyntheticLambda6
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return SegmentDao_Impl.observeForMeeting$lambda$8(_sql, meetingId, (SQLiteConnection) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final List observeForMeeting$lambda$8(String $_sql, String $meetingId, SQLiteConnection _connection) {
        String _tmpTranscript;
        String _tmpDiarizedTranscript;
        String _tmpCleanedTranscript;
        String _tmpAsrProvider;
        String _tmpCleanupProvider;
        String _tmpMeetingId;
        String _tmpAsrLastError;
        String _tmpSkipReason;
        String _tmpSpeakerCandidatesJson;
        String _tmpEndReason;
        Intrinsics.checkNotNullParameter(_connection, "_connection");
        SQLiteStatement _stmt = _connection.prepare($_sql);
        try {
            _stmt.mo7910bindText(1, $meetingId);
            int _columnIndexOfId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "id");
            int _columnIndexOfSessionId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "sessionId");
            int _columnIndexOfFilePath = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "filePath");
            int _columnIndexOfStartedAtMs = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "startedAtMs");
            int _columnIndexOfEndedAtMs = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "endedAtMs");
            int _columnIndexOfDurationMs = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "durationMs");
            int _columnIndexOfByteSize = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "byteSize");
            int _columnIndexOfTranscriptStatus = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "transcriptStatus");
            int _columnIndexOfTranscript = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "transcript");
            int _columnIndexOfDiarizedTranscript = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "diarizedTranscript");
            int _columnIndexOfCleanedTranscript = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "cleanedTranscript");
            int _columnIndexOfAsrProvider = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "asrProvider");
            int _tmp = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "cleanupProvider");
            int _columnIndexOfMeetingId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, MeetingStageWorker.KEY_MEETING_ID);
            int _columnIndexOfYouConfirmed = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "asrLastError");
            int _columnIndexOfSkipReason = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "skipReason");
            int _columnIndexOfSkipReason2 = _columnIndexOfSkipReason;
            int _columnIndexOfUpdatedAtMs = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "updatedAtMs");
            int _columnIndexOfUpdatedAtMs2 = _columnIndexOfUpdatedAtMs;
            int _columnIndexOfSpeakerCandidatesJson = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "speakerCandidatesJson");
            int _columnIndexOfSpeakerCandidatesJson2 = _columnIndexOfSpeakerCandidatesJson;
            int _columnIndexOfYouConfirmed2 = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "youConfirmed");
            int _columnIndexOfCleanupProvider = _columnIndexOfYouConfirmed2;
            int _columnIndexOfEndReason = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "endReason");
            List _result = new ArrayList();
            while (_stmt.step()) {
                String _tmpId = _stmt.getText(_columnIndexOfId);
                String _tmpSessionId = _stmt.getText(_columnIndexOfSessionId);
                String _tmpFilePath = _stmt.getText(_columnIndexOfFilePath);
                long _tmpStartedAtMs = _stmt.getLong(_columnIndexOfStartedAtMs);
                long _tmpEndedAtMs = _stmt.getLong(_columnIndexOfEndedAtMs);
                long _tmpDurationMs = _stmt.getLong(_columnIndexOfDurationMs);
                long _tmpByteSize = _stmt.getLong(_columnIndexOfByteSize);
                String _tmpTranscriptStatus = _stmt.getText(_columnIndexOfTranscriptStatus);
                if (_stmt.isNull(_columnIndexOfTranscript)) {
                    _tmpTranscript = null;
                } else {
                    String _tmpTranscript2 = _stmt.getText(_columnIndexOfTranscript);
                    _tmpTranscript = _tmpTranscript2;
                }
                if (_stmt.isNull(_columnIndexOfDiarizedTranscript)) {
                    _tmpDiarizedTranscript = null;
                } else {
                    String _tmpDiarizedTranscript2 = _stmt.getText(_columnIndexOfDiarizedTranscript);
                    _tmpDiarizedTranscript = _tmpDiarizedTranscript2;
                }
                if (_stmt.isNull(_columnIndexOfCleanedTranscript)) {
                    _tmpCleanedTranscript = null;
                } else {
                    String _tmpCleanedTranscript2 = _stmt.getText(_columnIndexOfCleanedTranscript);
                    _tmpCleanedTranscript = _tmpCleanedTranscript2;
                }
                if (_stmt.isNull(_columnIndexOfAsrProvider)) {
                    _tmpAsrProvider = null;
                } else {
                    String _tmpAsrProvider2 = _stmt.getText(_columnIndexOfAsrProvider);
                    _tmpAsrProvider = _tmpAsrProvider2;
                }
                if (_stmt.isNull(_tmp)) {
                    _tmpCleanupProvider = null;
                } else {
                    String _tmpCleanupProvider2 = _stmt.getText(_tmp);
                    _tmpCleanupProvider = _tmpCleanupProvider2;
                }
                if (_stmt.isNull(_columnIndexOfMeetingId)) {
                    _tmpMeetingId = null;
                } else {
                    String _tmpMeetingId2 = _stmt.getText(_columnIndexOfMeetingId);
                    _tmpMeetingId = _tmpMeetingId2;
                }
                int _columnIndexOfAsrProvider2 = _columnIndexOfAsrProvider;
                int _columnIndexOfAsrProvider3 = _columnIndexOfYouConfirmed;
                if (_stmt.isNull(_columnIndexOfAsrProvider3)) {
                    _tmpAsrLastError = null;
                } else {
                    String _tmpAsrLastError2 = _stmt.getText(_columnIndexOfAsrProvider3);
                    _tmpAsrLastError = _tmpAsrLastError2;
                }
                int _columnIndexOfAsrLastError = _columnIndexOfSkipReason2;
                if (_stmt.isNull(_columnIndexOfAsrLastError)) {
                    _tmpSkipReason = null;
                } else {
                    String _tmpSkipReason2 = _stmt.getText(_columnIndexOfAsrLastError);
                    _tmpSkipReason = _tmpSkipReason2;
                }
                int _columnIndexOfSkipReason3 = _columnIndexOfUpdatedAtMs2;
                long _tmpUpdatedAtMs = _stmt.getLong(_columnIndexOfSkipReason3);
                _columnIndexOfUpdatedAtMs2 = _columnIndexOfSkipReason3;
                int _columnIndexOfUpdatedAtMs3 = _columnIndexOfSpeakerCandidatesJson2;
                if (_stmt.isNull(_columnIndexOfUpdatedAtMs3)) {
                    _tmpSpeakerCandidatesJson = null;
                } else {
                    String _tmpSpeakerCandidatesJson2 = _stmt.getText(_columnIndexOfUpdatedAtMs3);
                    _tmpSpeakerCandidatesJson = _tmpSpeakerCandidatesJson2;
                }
                _columnIndexOfSpeakerCandidatesJson2 = _columnIndexOfUpdatedAtMs3;
                int _columnIndexOfMeetingId2 = _columnIndexOfMeetingId;
                int _columnIndexOfSpeakerCandidatesJson3 = _columnIndexOfCleanupProvider;
                int _columnIndexOfYouConfirmed3 = _tmp;
                int _tmp2 = (int) _stmt.getLong(_columnIndexOfSpeakerCandidatesJson3);
                boolean _tmpYouConfirmed = _tmp2 != 0;
                if (_stmt.isNull(_columnIndexOfEndReason)) {
                    _tmpEndReason = null;
                } else {
                    String _tmpEndReason2 = _stmt.getText(_columnIndexOfEndReason);
                    _tmpEndReason = _tmpEndReason2;
                }
                SegmentEntity _item = new SegmentEntity(_tmpId, _tmpSessionId, _tmpFilePath, _tmpStartedAtMs, _tmpEndedAtMs, _tmpDurationMs, _tmpByteSize, _tmpTranscriptStatus, _tmpTranscript, _tmpDiarizedTranscript, _tmpCleanedTranscript, _tmpAsrProvider, _tmpCleanupProvider, _tmpMeetingId, _tmpAsrLastError, _tmpSkipReason, _tmpUpdatedAtMs, _tmpSpeakerCandidatesJson, _tmpYouConfirmed, _tmpEndReason);
                List _result2 = _result;
                _result2.add(_item);
                _result = _result2;
                _tmp = _columnIndexOfYouConfirmed3;
                _columnIndexOfAsrProvider = _columnIndexOfAsrProvider2;
                _columnIndexOfSkipReason2 = _columnIndexOfAsrLastError;
                _columnIndexOfMeetingId = _columnIndexOfMeetingId2;
                _columnIndexOfCleanupProvider = _columnIndexOfSpeakerCandidatesJson3;
                _columnIndexOfYouConfirmed = _columnIndexOfAsrProvider3;
            }
            return _result;
        } finally {
            _stmt.close();
        }
    }

    @Override // com.varun.pocketassistant.data.SegmentDao
    public Object getForMeeting(final String meetingId, Continuation<? super List<SegmentEntity>> continuation) {
        final String _sql = "SELECT * FROM segments WHERE meetingId = ? ORDER BY startedAtMs ASC";
        return DBUtil.performSuspending(this.__db, true, false, new Function1() { // from class: com.varun.pocketassistant.data.SegmentDao_Impl$$ExternalSyntheticLambda8
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return SegmentDao_Impl.getForMeeting$lambda$9(_sql, meetingId, (SQLiteConnection) obj);
            }
        }, continuation);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final List getForMeeting$lambda$9(String $_sql, String $meetingId, SQLiteConnection _connection) {
        String _tmpTranscript;
        String _tmpDiarizedTranscript;
        String _tmpCleanedTranscript;
        String _tmpAsrProvider;
        String _tmpCleanupProvider;
        String _tmpMeetingId;
        String _tmpAsrLastError;
        String _tmpSkipReason;
        String _tmpSpeakerCandidatesJson;
        String _tmpEndReason;
        Intrinsics.checkNotNullParameter(_connection, "_connection");
        SQLiteStatement _stmt = _connection.prepare($_sql);
        try {
            _stmt.mo7910bindText(1, $meetingId);
            int _columnIndexOfId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "id");
            int _columnIndexOfSessionId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "sessionId");
            int _columnIndexOfFilePath = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "filePath");
            int _columnIndexOfStartedAtMs = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "startedAtMs");
            int _columnIndexOfEndedAtMs = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "endedAtMs");
            int _columnIndexOfDurationMs = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "durationMs");
            int _columnIndexOfByteSize = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "byteSize");
            int _columnIndexOfTranscriptStatus = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "transcriptStatus");
            int _columnIndexOfTranscript = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "transcript");
            int _columnIndexOfDiarizedTranscript = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "diarizedTranscript");
            int _columnIndexOfCleanedTranscript = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "cleanedTranscript");
            int _columnIndexOfAsrProvider = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "asrProvider");
            int _tmp = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "cleanupProvider");
            int _columnIndexOfMeetingId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, MeetingStageWorker.KEY_MEETING_ID);
            int _columnIndexOfYouConfirmed = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "asrLastError");
            int _columnIndexOfSkipReason = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "skipReason");
            int _columnIndexOfSkipReason2 = _columnIndexOfSkipReason;
            int _columnIndexOfUpdatedAtMs = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "updatedAtMs");
            int _columnIndexOfUpdatedAtMs2 = _columnIndexOfUpdatedAtMs;
            int _columnIndexOfSpeakerCandidatesJson = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "speakerCandidatesJson");
            int _columnIndexOfSpeakerCandidatesJson2 = _columnIndexOfSpeakerCandidatesJson;
            int _columnIndexOfYouConfirmed2 = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "youConfirmed");
            int _columnIndexOfCleanupProvider = _columnIndexOfYouConfirmed2;
            int _columnIndexOfEndReason = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "endReason");
            List _result = new ArrayList();
            while (_stmt.step()) {
                String _tmpId = _stmt.getText(_columnIndexOfId);
                String _tmpSessionId = _stmt.getText(_columnIndexOfSessionId);
                String _tmpFilePath = _stmt.getText(_columnIndexOfFilePath);
                long _tmpStartedAtMs = _stmt.getLong(_columnIndexOfStartedAtMs);
                long _tmpEndedAtMs = _stmt.getLong(_columnIndexOfEndedAtMs);
                long _tmpDurationMs = _stmt.getLong(_columnIndexOfDurationMs);
                long _tmpByteSize = _stmt.getLong(_columnIndexOfByteSize);
                String _tmpTranscriptStatus = _stmt.getText(_columnIndexOfTranscriptStatus);
                if (_stmt.isNull(_columnIndexOfTranscript)) {
                    _tmpTranscript = null;
                } else {
                    String _tmpTranscript2 = _stmt.getText(_columnIndexOfTranscript);
                    _tmpTranscript = _tmpTranscript2;
                }
                if (_stmt.isNull(_columnIndexOfDiarizedTranscript)) {
                    _tmpDiarizedTranscript = null;
                } else {
                    String _tmpDiarizedTranscript2 = _stmt.getText(_columnIndexOfDiarizedTranscript);
                    _tmpDiarizedTranscript = _tmpDiarizedTranscript2;
                }
                if (_stmt.isNull(_columnIndexOfCleanedTranscript)) {
                    _tmpCleanedTranscript = null;
                } else {
                    String _tmpCleanedTranscript2 = _stmt.getText(_columnIndexOfCleanedTranscript);
                    _tmpCleanedTranscript = _tmpCleanedTranscript2;
                }
                if (_stmt.isNull(_columnIndexOfAsrProvider)) {
                    _tmpAsrProvider = null;
                } else {
                    String _tmpAsrProvider2 = _stmt.getText(_columnIndexOfAsrProvider);
                    _tmpAsrProvider = _tmpAsrProvider2;
                }
                if (_stmt.isNull(_tmp)) {
                    _tmpCleanupProvider = null;
                } else {
                    String _tmpCleanupProvider2 = _stmt.getText(_tmp);
                    _tmpCleanupProvider = _tmpCleanupProvider2;
                }
                if (_stmt.isNull(_columnIndexOfMeetingId)) {
                    _tmpMeetingId = null;
                } else {
                    String _tmpMeetingId2 = _stmt.getText(_columnIndexOfMeetingId);
                    _tmpMeetingId = _tmpMeetingId2;
                }
                int _columnIndexOfAsrProvider2 = _columnIndexOfAsrProvider;
                int _columnIndexOfAsrProvider3 = _columnIndexOfYouConfirmed;
                if (_stmt.isNull(_columnIndexOfAsrProvider3)) {
                    _tmpAsrLastError = null;
                } else {
                    String _tmpAsrLastError2 = _stmt.getText(_columnIndexOfAsrProvider3);
                    _tmpAsrLastError = _tmpAsrLastError2;
                }
                int _columnIndexOfAsrLastError = _columnIndexOfSkipReason2;
                if (_stmt.isNull(_columnIndexOfAsrLastError)) {
                    _tmpSkipReason = null;
                } else {
                    String _tmpSkipReason2 = _stmt.getText(_columnIndexOfAsrLastError);
                    _tmpSkipReason = _tmpSkipReason2;
                }
                int _columnIndexOfSkipReason3 = _columnIndexOfUpdatedAtMs2;
                long _tmpUpdatedAtMs = _stmt.getLong(_columnIndexOfSkipReason3);
                _columnIndexOfUpdatedAtMs2 = _columnIndexOfSkipReason3;
                int _columnIndexOfUpdatedAtMs3 = _columnIndexOfSpeakerCandidatesJson2;
                if (_stmt.isNull(_columnIndexOfUpdatedAtMs3)) {
                    _tmpSpeakerCandidatesJson = null;
                } else {
                    String _tmpSpeakerCandidatesJson2 = _stmt.getText(_columnIndexOfUpdatedAtMs3);
                    _tmpSpeakerCandidatesJson = _tmpSpeakerCandidatesJson2;
                }
                _columnIndexOfSpeakerCandidatesJson2 = _columnIndexOfUpdatedAtMs3;
                int _columnIndexOfMeetingId2 = _columnIndexOfMeetingId;
                int _columnIndexOfSpeakerCandidatesJson3 = _columnIndexOfCleanupProvider;
                int _columnIndexOfYouConfirmed3 = _tmp;
                int _tmp2 = (int) _stmt.getLong(_columnIndexOfSpeakerCandidatesJson3);
                boolean _tmpYouConfirmed = _tmp2 != 0;
                if (_stmt.isNull(_columnIndexOfEndReason)) {
                    _tmpEndReason = null;
                } else {
                    String _tmpEndReason2 = _stmt.getText(_columnIndexOfEndReason);
                    _tmpEndReason = _tmpEndReason2;
                }
                SegmentEntity _item = new SegmentEntity(_tmpId, _tmpSessionId, _tmpFilePath, _tmpStartedAtMs, _tmpEndedAtMs, _tmpDurationMs, _tmpByteSize, _tmpTranscriptStatus, _tmpTranscript, _tmpDiarizedTranscript, _tmpCleanedTranscript, _tmpAsrProvider, _tmpCleanupProvider, _tmpMeetingId, _tmpAsrLastError, _tmpSkipReason, _tmpUpdatedAtMs, _tmpSpeakerCandidatesJson, _tmpYouConfirmed, _tmpEndReason);
                List _result2 = _result;
                _result2.add(_item);
                _result = _result2;
                _tmp = _columnIndexOfYouConfirmed3;
                _columnIndexOfAsrProvider = _columnIndexOfAsrProvider2;
                _columnIndexOfSkipReason2 = _columnIndexOfAsrLastError;
                _columnIndexOfMeetingId = _columnIndexOfMeetingId2;
                _columnIndexOfCleanupProvider = _columnIndexOfSpeakerCandidatesJson3;
                _columnIndexOfYouConfirmed = _columnIndexOfAsrProvider3;
            }
            return _result;
        } finally {
            _stmt.close();
        }
    }

    @Override // com.varun.pocketassistant.data.SegmentDao
    public Object getByTranscriptStatus(final String status, final int limit, Continuation<? super List<SegmentEntity>> continuation) {
        final String _sql = "SELECT * FROM segments WHERE transcriptStatus = ? ORDER BY startedAtMs ASC LIMIT ?";
        return DBUtil.performSuspending(this.__db, true, false, new Function1() { // from class: com.varun.pocketassistant.data.SegmentDao_Impl$$ExternalSyntheticLambda9
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return SegmentDao_Impl.getByTranscriptStatus$lambda$10(_sql, status, limit, (SQLiteConnection) obj);
            }
        }, continuation);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final List getByTranscriptStatus$lambda$10(String $_sql, String $status, int $limit, SQLiteConnection _connection) {
        String _tmpTranscript;
        String _tmpDiarizedTranscript;
        String _tmpCleanedTranscript;
        String _tmpAsrProvider;
        String _tmpCleanupProvider;
        String _tmpMeetingId;
        String _tmpAsrLastError;
        String _tmpSkipReason;
        String _tmpSpeakerCandidatesJson;
        String _tmpEndReason;
        Intrinsics.checkNotNullParameter(_connection, "_connection");
        SQLiteStatement _stmt = _connection.prepare($_sql);
        try {
            _stmt.mo7910bindText(1, $status);
            _stmt.mo7908bindLong(2, $limit);
            int _columnIndexOfId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "id");
            int _columnIndexOfSessionId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "sessionId");
            int _columnIndexOfFilePath = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "filePath");
            int _columnIndexOfStartedAtMs = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "startedAtMs");
            int _columnIndexOfEndedAtMs = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "endedAtMs");
            int _columnIndexOfDurationMs = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "durationMs");
            int _columnIndexOfByteSize = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "byteSize");
            int _columnIndexOfTranscriptStatus = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "transcriptStatus");
            int _columnIndexOfTranscript = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "transcript");
            int _columnIndexOfDiarizedTranscript = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "diarizedTranscript");
            int _columnIndexOfCleanedTranscript = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "cleanedTranscript");
            int _tmp = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "asrProvider");
            int _columnIndexOfCleanupProvider = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "cleanupProvider");
            int _columnIndexOfMeetingId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, MeetingStageWorker.KEY_MEETING_ID);
            int _columnIndexOfYouConfirmed = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "asrLastError");
            int _columnIndexOfSkipReason = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "skipReason");
            int _columnIndexOfSkipReason2 = _columnIndexOfSkipReason;
            int _columnIndexOfUpdatedAtMs = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "updatedAtMs");
            int _columnIndexOfUpdatedAtMs2 = _columnIndexOfUpdatedAtMs;
            int _columnIndexOfSpeakerCandidatesJson = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "speakerCandidatesJson");
            int _columnIndexOfSpeakerCandidatesJson2 = _columnIndexOfSpeakerCandidatesJson;
            int _columnIndexOfYouConfirmed2 = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "youConfirmed");
            int _columnIndexOfAsrProvider = _columnIndexOfYouConfirmed2;
            int _columnIndexOfEndReason = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "endReason");
            List _result = new ArrayList();
            while (_stmt.step()) {
                String _tmpId = _stmt.getText(_columnIndexOfId);
                String _tmpSessionId = _stmt.getText(_columnIndexOfSessionId);
                String _tmpFilePath = _stmt.getText(_columnIndexOfFilePath);
                long _tmpStartedAtMs = _stmt.getLong(_columnIndexOfStartedAtMs);
                long _tmpEndedAtMs = _stmt.getLong(_columnIndexOfEndedAtMs);
                long _tmpDurationMs = _stmt.getLong(_columnIndexOfDurationMs);
                long _tmpByteSize = _stmt.getLong(_columnIndexOfByteSize);
                String _tmpTranscriptStatus = _stmt.getText(_columnIndexOfTranscriptStatus);
                if (_stmt.isNull(_columnIndexOfTranscript)) {
                    _tmpTranscript = null;
                } else {
                    String _tmpTranscript2 = _stmt.getText(_columnIndexOfTranscript);
                    _tmpTranscript = _tmpTranscript2;
                }
                if (_stmt.isNull(_columnIndexOfDiarizedTranscript)) {
                    _tmpDiarizedTranscript = null;
                } else {
                    String _tmpDiarizedTranscript2 = _stmt.getText(_columnIndexOfDiarizedTranscript);
                    _tmpDiarizedTranscript = _tmpDiarizedTranscript2;
                }
                if (_stmt.isNull(_columnIndexOfCleanedTranscript)) {
                    _tmpCleanedTranscript = null;
                } else {
                    String _tmpCleanedTranscript2 = _stmt.getText(_columnIndexOfCleanedTranscript);
                    _tmpCleanedTranscript = _tmpCleanedTranscript2;
                }
                if (_stmt.isNull(_tmp)) {
                    _tmpAsrProvider = null;
                } else {
                    String _tmpAsrProvider2 = _stmt.getText(_tmp);
                    _tmpAsrProvider = _tmpAsrProvider2;
                }
                if (_stmt.isNull(_columnIndexOfCleanupProvider)) {
                    _tmpCleanupProvider = null;
                } else {
                    String _tmpCleanupProvider2 = _stmt.getText(_columnIndexOfCleanupProvider);
                    _tmpCleanupProvider = _tmpCleanupProvider2;
                }
                if (_stmt.isNull(_columnIndexOfMeetingId)) {
                    _tmpMeetingId = null;
                } else {
                    String _tmpMeetingId2 = _stmt.getText(_columnIndexOfMeetingId);
                    _tmpMeetingId = _tmpMeetingId2;
                }
                int _columnIndexOfCleanedTranscript2 = _columnIndexOfCleanedTranscript;
                int _columnIndexOfCleanedTranscript3 = _columnIndexOfYouConfirmed;
                if (_stmt.isNull(_columnIndexOfCleanedTranscript3)) {
                    _tmpAsrLastError = null;
                } else {
                    String _tmpAsrLastError2 = _stmt.getText(_columnIndexOfCleanedTranscript3);
                    _tmpAsrLastError = _tmpAsrLastError2;
                }
                int _columnIndexOfAsrLastError = _columnIndexOfSkipReason2;
                if (_stmt.isNull(_columnIndexOfAsrLastError)) {
                    _tmpSkipReason = null;
                } else {
                    String _tmpSkipReason2 = _stmt.getText(_columnIndexOfAsrLastError);
                    _tmpSkipReason = _tmpSkipReason2;
                }
                int _columnIndexOfSkipReason3 = _columnIndexOfUpdatedAtMs2;
                long _tmpUpdatedAtMs = _stmt.getLong(_columnIndexOfSkipReason3);
                _columnIndexOfUpdatedAtMs2 = _columnIndexOfSkipReason3;
                int _columnIndexOfUpdatedAtMs3 = _columnIndexOfSpeakerCandidatesJson2;
                if (_stmt.isNull(_columnIndexOfUpdatedAtMs3)) {
                    _tmpSpeakerCandidatesJson = null;
                } else {
                    String _tmpSpeakerCandidatesJson2 = _stmt.getText(_columnIndexOfUpdatedAtMs3);
                    _tmpSpeakerCandidatesJson = _tmpSpeakerCandidatesJson2;
                }
                _columnIndexOfSpeakerCandidatesJson2 = _columnIndexOfUpdatedAtMs3;
                int _columnIndexOfCleanupProvider2 = _columnIndexOfCleanupProvider;
                int _columnIndexOfSpeakerCandidatesJson3 = _columnIndexOfAsrProvider;
                int _columnIndexOfYouConfirmed3 = _tmp;
                int _tmp2 = (int) _stmt.getLong(_columnIndexOfSpeakerCandidatesJson3);
                boolean _tmpYouConfirmed = _tmp2 != 0;
                if (_stmt.isNull(_columnIndexOfEndReason)) {
                    _tmpEndReason = null;
                } else {
                    String _tmpEndReason2 = _stmt.getText(_columnIndexOfEndReason);
                    _tmpEndReason = _tmpEndReason2;
                }
                SegmentEntity _item = new SegmentEntity(_tmpId, _tmpSessionId, _tmpFilePath, _tmpStartedAtMs, _tmpEndedAtMs, _tmpDurationMs, _tmpByteSize, _tmpTranscriptStatus, _tmpTranscript, _tmpDiarizedTranscript, _tmpCleanedTranscript, _tmpAsrProvider, _tmpCleanupProvider, _tmpMeetingId, _tmpAsrLastError, _tmpSkipReason, _tmpUpdatedAtMs, _tmpSpeakerCandidatesJson, _tmpYouConfirmed, _tmpEndReason);
                List _result2 = _result;
                _result2.add(_item);
                _result = _result2;
                _tmp = _columnIndexOfYouConfirmed3;
                _columnIndexOfCleanedTranscript = _columnIndexOfCleanedTranscript2;
                _columnIndexOfSkipReason2 = _columnIndexOfAsrLastError;
                _columnIndexOfCleanupProvider = _columnIndexOfCleanupProvider2;
                _columnIndexOfAsrProvider = _columnIndexOfSpeakerCandidatesJson3;
                _columnIndexOfYouConfirmed = _columnIndexOfCleanedTranscript3;
            }
            return _result;
        } finally {
            _stmt.close();
        }
    }

    @Override // com.varun.pocketassistant.data.SegmentDao
    public Object getNeedingAsr(final String a, final String b, final int limit, Continuation<? super List<SegmentEntity>> continuation) {
        final String _sql = "\n        SELECT * FROM segments\n        WHERE transcriptStatus IN (?, ?)\n        ORDER BY startedAtMs ASC\n        LIMIT ?\n        ";
        return DBUtil.performSuspending(this.__db, true, false, new Function1() { // from class: com.varun.pocketassistant.data.SegmentDao_Impl$$ExternalSyntheticLambda3
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return SegmentDao_Impl.getNeedingAsr$lambda$11(_sql, a, b, limit, (SQLiteConnection) obj);
            }
        }, continuation);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final List getNeedingAsr$lambda$11(String $_sql, String $a, String $b, int $limit, SQLiteConnection _connection) {
        String _tmpTranscript;
        String _tmpDiarizedTranscript;
        String _tmpCleanedTranscript;
        String _tmpAsrProvider;
        String _tmpCleanupProvider;
        String _tmpMeetingId;
        String _tmpAsrLastError;
        String _tmpSkipReason;
        String _tmpSpeakerCandidatesJson;
        String _tmpEndReason;
        Intrinsics.checkNotNullParameter(_connection, "_connection");
        SQLiteStatement _stmt = _connection.prepare($_sql);
        try {
            _stmt.mo7910bindText(1, $a);
            _stmt.mo7910bindText(2, $b);
            _stmt.mo7908bindLong(3, $limit);
            int _columnIndexOfId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "id");
            int _columnIndexOfSessionId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "sessionId");
            int _columnIndexOfFilePath = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "filePath");
            int _columnIndexOfStartedAtMs = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "startedAtMs");
            int _columnIndexOfEndedAtMs = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "endedAtMs");
            int _columnIndexOfDurationMs = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "durationMs");
            int _columnIndexOfByteSize = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "byteSize");
            int _columnIndexOfTranscriptStatus = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "transcriptStatus");
            int _columnIndexOfTranscript = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "transcript");
            int _columnIndexOfDiarizedTranscript = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "diarizedTranscript");
            int _tmp = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "cleanedTranscript");
            int _columnIndexOfAsrProvider = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "asrProvider");
            int _columnIndexOfCleanupProvider = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "cleanupProvider");
            int _columnIndexOfMeetingId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, MeetingStageWorker.KEY_MEETING_ID);
            int _columnIndexOfYouConfirmed = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "asrLastError");
            int _columnIndexOfSkipReason = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "skipReason");
            int _columnIndexOfSkipReason2 = _columnIndexOfSkipReason;
            int _columnIndexOfUpdatedAtMs = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "updatedAtMs");
            int _columnIndexOfUpdatedAtMs2 = _columnIndexOfUpdatedAtMs;
            int _columnIndexOfSpeakerCandidatesJson = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "speakerCandidatesJson");
            int _columnIndexOfSpeakerCandidatesJson2 = _columnIndexOfSpeakerCandidatesJson;
            int _columnIndexOfYouConfirmed2 = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "youConfirmed");
            int _columnIndexOfCleanedTranscript = _columnIndexOfYouConfirmed2;
            int _columnIndexOfEndReason = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "endReason");
            List _result = new ArrayList();
            while (_stmt.step()) {
                String _tmpId = _stmt.getText(_columnIndexOfId);
                String _tmpSessionId = _stmt.getText(_columnIndexOfSessionId);
                String _tmpFilePath = _stmt.getText(_columnIndexOfFilePath);
                long _tmpStartedAtMs = _stmt.getLong(_columnIndexOfStartedAtMs);
                long _tmpEndedAtMs = _stmt.getLong(_columnIndexOfEndedAtMs);
                long _tmpDurationMs = _stmt.getLong(_columnIndexOfDurationMs);
                long _tmpByteSize = _stmt.getLong(_columnIndexOfByteSize);
                String _tmpTranscriptStatus = _stmt.getText(_columnIndexOfTranscriptStatus);
                if (_stmt.isNull(_columnIndexOfTranscript)) {
                    _tmpTranscript = null;
                } else {
                    String _tmpTranscript2 = _stmt.getText(_columnIndexOfTranscript);
                    _tmpTranscript = _tmpTranscript2;
                }
                if (_stmt.isNull(_columnIndexOfDiarizedTranscript)) {
                    _tmpDiarizedTranscript = null;
                } else {
                    String _tmpDiarizedTranscript2 = _stmt.getText(_columnIndexOfDiarizedTranscript);
                    _tmpDiarizedTranscript = _tmpDiarizedTranscript2;
                }
                if (_stmt.isNull(_tmp)) {
                    _tmpCleanedTranscript = null;
                } else {
                    String _tmpCleanedTranscript2 = _stmt.getText(_tmp);
                    _tmpCleanedTranscript = _tmpCleanedTranscript2;
                }
                if (_stmt.isNull(_columnIndexOfAsrProvider)) {
                    _tmpAsrProvider = null;
                } else {
                    String _tmpAsrProvider2 = _stmt.getText(_columnIndexOfAsrProvider);
                    _tmpAsrProvider = _tmpAsrProvider2;
                }
                if (_stmt.isNull(_columnIndexOfCleanupProvider)) {
                    _tmpCleanupProvider = null;
                } else {
                    String _tmpCleanupProvider2 = _stmt.getText(_columnIndexOfCleanupProvider);
                    _tmpCleanupProvider = _tmpCleanupProvider2;
                }
                if (_stmt.isNull(_columnIndexOfMeetingId)) {
                    _tmpMeetingId = null;
                } else {
                    String _tmpMeetingId2 = _stmt.getText(_columnIndexOfMeetingId);
                    _tmpMeetingId = _tmpMeetingId2;
                }
                int _columnIndexOfDiarizedTranscript2 = _columnIndexOfDiarizedTranscript;
                int _columnIndexOfDiarizedTranscript3 = _columnIndexOfYouConfirmed;
                if (_stmt.isNull(_columnIndexOfDiarizedTranscript3)) {
                    _tmpAsrLastError = null;
                } else {
                    String _tmpAsrLastError2 = _stmt.getText(_columnIndexOfDiarizedTranscript3);
                    _tmpAsrLastError = _tmpAsrLastError2;
                }
                int _columnIndexOfAsrLastError = _columnIndexOfSkipReason2;
                if (_stmt.isNull(_columnIndexOfAsrLastError)) {
                    _tmpSkipReason = null;
                } else {
                    String _tmpSkipReason2 = _stmt.getText(_columnIndexOfAsrLastError);
                    _tmpSkipReason = _tmpSkipReason2;
                }
                int _columnIndexOfSkipReason3 = _columnIndexOfUpdatedAtMs2;
                long _tmpUpdatedAtMs = _stmt.getLong(_columnIndexOfSkipReason3);
                _columnIndexOfUpdatedAtMs2 = _columnIndexOfSkipReason3;
                int _columnIndexOfUpdatedAtMs3 = _columnIndexOfSpeakerCandidatesJson2;
                if (_stmt.isNull(_columnIndexOfUpdatedAtMs3)) {
                    _tmpSpeakerCandidatesJson = null;
                } else {
                    String _tmpSpeakerCandidatesJson2 = _stmt.getText(_columnIndexOfUpdatedAtMs3);
                    _tmpSpeakerCandidatesJson = _tmpSpeakerCandidatesJson2;
                }
                _columnIndexOfSpeakerCandidatesJson2 = _columnIndexOfUpdatedAtMs3;
                int _columnIndexOfAsrProvider2 = _columnIndexOfAsrProvider;
                int _columnIndexOfSpeakerCandidatesJson3 = _columnIndexOfCleanedTranscript;
                int _columnIndexOfYouConfirmed3 = _tmp;
                int _tmp2 = (int) _stmt.getLong(_columnIndexOfSpeakerCandidatesJson3);
                boolean _tmpYouConfirmed = _tmp2 != 0;
                if (_stmt.isNull(_columnIndexOfEndReason)) {
                    _tmpEndReason = null;
                } else {
                    String _tmpEndReason2 = _stmt.getText(_columnIndexOfEndReason);
                    _tmpEndReason = _tmpEndReason2;
                }
                SegmentEntity _item = new SegmentEntity(_tmpId, _tmpSessionId, _tmpFilePath, _tmpStartedAtMs, _tmpEndedAtMs, _tmpDurationMs, _tmpByteSize, _tmpTranscriptStatus, _tmpTranscript, _tmpDiarizedTranscript, _tmpCleanedTranscript, _tmpAsrProvider, _tmpCleanupProvider, _tmpMeetingId, _tmpAsrLastError, _tmpSkipReason, _tmpUpdatedAtMs, _tmpSpeakerCandidatesJson, _tmpYouConfirmed, _tmpEndReason);
                List _result2 = _result;
                _result2.add(_item);
                _result = _result2;
                _tmp = _columnIndexOfYouConfirmed3;
                _columnIndexOfDiarizedTranscript = _columnIndexOfDiarizedTranscript2;
                _columnIndexOfSkipReason2 = _columnIndexOfAsrLastError;
                _columnIndexOfAsrProvider = _columnIndexOfAsrProvider2;
                _columnIndexOfCleanedTranscript = _columnIndexOfSpeakerCandidatesJson3;
                _columnIndexOfYouConfirmed = _columnIndexOfDiarizedTranscript3;
            }
            return _result;
        } finally {
            _stmt.close();
        }
    }

    @Override // com.varun.pocketassistant.data.SegmentDao
    public Object getById(final String id, Continuation<? super SegmentEntity> continuation) {
        final String _sql = "SELECT * FROM segments WHERE id = ?";
        return DBUtil.performSuspending(this.__db, true, false, new Function1() { // from class: com.varun.pocketassistant.data.SegmentDao_Impl$$ExternalSyntheticLambda0
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return SegmentDao_Impl.getById$lambda$12(_sql, id, (SQLiteConnection) obj);
            }
        }, continuation);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final SegmentEntity getById$lambda$12(String $_sql, String $id, SQLiteConnection _connection) {
        SegmentEntity _result;
        String _tmpTranscript;
        String _tmpDiarizedTranscript;
        String _tmpCleanedTranscript;
        String _tmpAsrProvider;
        String _tmpCleanupProvider;
        String _tmpMeetingId;
        String _tmpAsrLastError;
        String _tmpSkipReason;
        String _tmpSpeakerCandidatesJson;
        String _tmpEndReason;
        Intrinsics.checkNotNullParameter(_connection, "_connection");
        SQLiteStatement _stmt = _connection.prepare($_sql);
        try {
            _stmt.mo7910bindText(1, $id);
            int _columnIndexOfId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "id");
            int _columnIndexOfSessionId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "sessionId");
            int _columnIndexOfFilePath = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "filePath");
            int _columnIndexOfStartedAtMs = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "startedAtMs");
            int _columnIndexOfEndedAtMs = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "endedAtMs");
            int _columnIndexOfDurationMs = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "durationMs");
            int _columnIndexOfByteSize = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "byteSize");
            int _columnIndexOfTranscriptStatus = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "transcriptStatus");
            int _columnIndexOfTranscript = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "transcript");
            int _columnIndexOfDiarizedTranscript = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "diarizedTranscript");
            int _columnIndexOfCleanedTranscript = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "cleanedTranscript");
            int _columnIndexOfAsrProvider = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "asrProvider");
            int _columnIndexOfCleanupProvider = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "cleanupProvider");
            int _columnIndexOfMeetingId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, MeetingStageWorker.KEY_MEETING_ID);
            int _columnIndexOfAsrLastError = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "asrLastError");
            int _columnIndexOfSkipReason = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "skipReason");
            int _columnIndexOfUpdatedAtMs = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "updatedAtMs");
            int _columnIndexOfSpeakerCandidatesJson = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "speakerCandidatesJson");
            int _columnIndexOfYouConfirmed = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "youConfirmed");
            int _columnIndexOfEndReason = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "endReason");
            if (_stmt.step()) {
                String _tmpId = _stmt.getText(_columnIndexOfId);
                String _tmpSessionId = _stmt.getText(_columnIndexOfSessionId);
                String _tmpFilePath = _stmt.getText(_columnIndexOfFilePath);
                long _tmpStartedAtMs = _stmt.getLong(_columnIndexOfStartedAtMs);
                long _tmpEndedAtMs = _stmt.getLong(_columnIndexOfEndedAtMs);
                long _tmpDurationMs = _stmt.getLong(_columnIndexOfDurationMs);
                long _tmpByteSize = _stmt.getLong(_columnIndexOfByteSize);
                String _tmpTranscriptStatus = _stmt.getText(_columnIndexOfTranscriptStatus);
                if (_stmt.isNull(_columnIndexOfTranscript)) {
                    _tmpTranscript = null;
                } else {
                    String _tmpTranscript2 = _stmt.getText(_columnIndexOfTranscript);
                    _tmpTranscript = _tmpTranscript2;
                }
                if (_stmt.isNull(_columnIndexOfDiarizedTranscript)) {
                    _tmpDiarizedTranscript = null;
                } else {
                    String _tmpDiarizedTranscript2 = _stmt.getText(_columnIndexOfDiarizedTranscript);
                    _tmpDiarizedTranscript = _tmpDiarizedTranscript2;
                }
                if (_stmt.isNull(_columnIndexOfCleanedTranscript)) {
                    _tmpCleanedTranscript = null;
                } else {
                    String _tmpCleanedTranscript2 = _stmt.getText(_columnIndexOfCleanedTranscript);
                    _tmpCleanedTranscript = _tmpCleanedTranscript2;
                }
                if (_stmt.isNull(_columnIndexOfAsrProvider)) {
                    _tmpAsrProvider = null;
                } else {
                    String _tmpAsrProvider2 = _stmt.getText(_columnIndexOfAsrProvider);
                    _tmpAsrProvider = _tmpAsrProvider2;
                }
                if (_stmt.isNull(_columnIndexOfCleanupProvider)) {
                    _tmpCleanupProvider = null;
                } else {
                    String _tmpCleanupProvider2 = _stmt.getText(_columnIndexOfCleanupProvider);
                    _tmpCleanupProvider = _tmpCleanupProvider2;
                }
                if (_stmt.isNull(_columnIndexOfMeetingId)) {
                    _tmpMeetingId = null;
                } else {
                    String _tmpMeetingId2 = _stmt.getText(_columnIndexOfMeetingId);
                    _tmpMeetingId = _tmpMeetingId2;
                }
                if (_stmt.isNull(_columnIndexOfAsrLastError)) {
                    _tmpAsrLastError = null;
                } else {
                    String _tmpAsrLastError2 = _stmt.getText(_columnIndexOfAsrLastError);
                    _tmpAsrLastError = _tmpAsrLastError2;
                }
                if (_stmt.isNull(_columnIndexOfSkipReason)) {
                    _tmpSkipReason = null;
                } else {
                    String _tmpSkipReason2 = _stmt.getText(_columnIndexOfSkipReason);
                    _tmpSkipReason = _tmpSkipReason2;
                }
                long _tmpUpdatedAtMs = _stmt.getLong(_columnIndexOfUpdatedAtMs);
                if (_stmt.isNull(_columnIndexOfSpeakerCandidatesJson)) {
                    _tmpSpeakerCandidatesJson = null;
                } else {
                    String _tmpSpeakerCandidatesJson2 = _stmt.getText(_columnIndexOfSpeakerCandidatesJson);
                    _tmpSpeakerCandidatesJson = _tmpSpeakerCandidatesJson2;
                }
                int _tmp = (int) _stmt.getLong(_columnIndexOfYouConfirmed);
                boolean _tmpYouConfirmed = _tmp != 0;
                if (_stmt.isNull(_columnIndexOfEndReason)) {
                    _tmpEndReason = null;
                } else {
                    String _tmpEndReason2 = _stmt.getText(_columnIndexOfEndReason);
                    _tmpEndReason = _tmpEndReason2;
                }
                _result = new SegmentEntity(_tmpId, _tmpSessionId, _tmpFilePath, _tmpStartedAtMs, _tmpEndedAtMs, _tmpDurationMs, _tmpByteSize, _tmpTranscriptStatus, _tmpTranscript, _tmpDiarizedTranscript, _tmpCleanedTranscript, _tmpAsrProvider, _tmpCleanupProvider, _tmpMeetingId, _tmpAsrLastError, _tmpSkipReason, _tmpUpdatedAtMs, _tmpSpeakerCandidatesJson, _tmpYouConfirmed, _tmpEndReason);
            } else {
                _result = null;
            }
            return _result;
        } finally {
            _stmt.close();
        }
    }

    @Override // com.varun.pocketassistant.data.SegmentDao
    public Object assignMeeting(final String meetingId, final List<String> list, Continuation<? super Unit> continuation) {
        StringBuilder _stringBuilder = new StringBuilder();
        _stringBuilder.append("UPDATE segments SET meetingId = ");
        _stringBuilder.append("?");
        _stringBuilder.append(" WHERE id IN (");
        int _inputSize = list.size();
        StringUtil.appendPlaceholders(_stringBuilder, _inputSize);
        _stringBuilder.append(")");
        final String _sql = _stringBuilder.toString();
        Intrinsics.checkNotNullExpressionValue(_sql, "toString(...)");
        Object objPerformSuspending = DBUtil.performSuspending(this.__db, false, true, new Function1() { // from class: com.varun.pocketassistant.data.SegmentDao_Impl$$ExternalSyntheticLambda12
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return SegmentDao_Impl.assignMeeting$lambda$13(_sql, meetingId, list, (SQLiteConnection) obj);
            }
        }, continuation);
        return objPerformSuspending == IntrinsicsKt.getCOROUTINE_SUSPENDED() ? objPerformSuspending : Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Unit assignMeeting$lambda$13(String $_sql, String $meetingId, List $ids, SQLiteConnection _connection) {
        Intrinsics.checkNotNullParameter(_connection, "_connection");
        SQLiteStatement _stmt = _connection.prepare($_sql);
        try {
            _stmt.mo7910bindText(1, $meetingId);
            int _argIndex = 2;
            Iterator it = $ids.iterator();
            while (it.hasNext()) {
                String _item = (String) it.next();
                _stmt.mo7910bindText(_argIndex, _item);
                _argIndex++;
            }
            _stmt.step();
            return Unit.INSTANCE;
        } finally {
            _stmt.close();
        }
    }

    @Override // com.varun.pocketassistant.data.SegmentDao
    public Object setMeetingId(final String id, final String meetingId, Continuation<? super Unit> continuation) {
        final String _sql = "UPDATE segments SET meetingId = ? WHERE id = ?";
        Object objPerformSuspending = DBUtil.performSuspending(this.__db, false, true, new Function1() { // from class: com.varun.pocketassistant.data.SegmentDao_Impl$$ExternalSyntheticLambda7
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return SegmentDao_Impl.setMeetingId$lambda$14(_sql, meetingId, id, (SQLiteConnection) obj);
            }
        }, continuation);
        return objPerformSuspending == IntrinsicsKt.getCOROUTINE_SUSPENDED() ? objPerformSuspending : Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Unit setMeetingId$lambda$14(String $_sql, String $meetingId, String $id, SQLiteConnection _connection) {
        Intrinsics.checkNotNullParameter(_connection, "_connection");
        SQLiteStatement _stmt = _connection.prepare($_sql);
        try {
            _stmt.mo7910bindText(1, $meetingId);
            _stmt.mo7910bindText(2, $id);
            _stmt.step();
            return Unit.INSTANCE;
        } finally {
            _stmt.close();
        }
    }

    @Override // com.varun.pocketassistant.data.SegmentDao
    public Object clearMeetingId(final String id, Continuation<? super Unit> continuation) {
        final String _sql = "UPDATE segments SET meetingId = NULL WHERE id = ?";
        Object objPerformSuspending = DBUtil.performSuspending(this.__db, false, true, new Function1() { // from class: com.varun.pocketassistant.data.SegmentDao_Impl$$ExternalSyntheticLambda5
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return SegmentDao_Impl.clearMeetingId$lambda$15(_sql, id, (SQLiteConnection) obj);
            }
        }, continuation);
        return objPerformSuspending == IntrinsicsKt.getCOROUTINE_SUSPENDED() ? objPerformSuspending : Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Unit clearMeetingId$lambda$15(String $_sql, String $id, SQLiteConnection _connection) {
        Intrinsics.checkNotNullParameter(_connection, "_connection");
        SQLiteStatement _stmt = _connection.prepare($_sql);
        try {
            _stmt.mo7910bindText(1, $id);
            _stmt.step();
            return Unit.INSTANCE;
        } finally {
            _stmt.close();
        }
    }

    @Override // com.varun.pocketassistant.data.SegmentDao
    public Object clearMeeting(final String meetingId, Continuation<? super Unit> continuation) {
        final String _sql = "UPDATE segments SET meetingId = NULL WHERE meetingId = ?";
        Object objPerformSuspending = DBUtil.performSuspending(this.__db, false, true, new Function1() { // from class: com.varun.pocketassistant.data.SegmentDao_Impl$$ExternalSyntheticLambda11
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return SegmentDao_Impl.clearMeeting$lambda$16(_sql, meetingId, (SQLiteConnection) obj);
            }
        }, continuation);
        return objPerformSuspending == IntrinsicsKt.getCOROUTINE_SUSPENDED() ? objPerformSuspending : Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Unit clearMeeting$lambda$16(String $_sql, String $meetingId, SQLiteConnection _connection) {
        Intrinsics.checkNotNullParameter(_connection, "_connection");
        SQLiteStatement _stmt = _connection.prepare($_sql);
        try {
            _stmt.mo7910bindText(1, $meetingId);
            _stmt.step();
            return Unit.INSTANCE;
        } finally {
            _stmt.close();
        }
    }

    @Override // com.varun.pocketassistant.data.SegmentDao
    public Object deleteById(final String id, Continuation<? super Unit> continuation) {
        final String _sql = "DELETE FROM segments WHERE id = ?";
        Object objPerformSuspending = DBUtil.performSuspending(this.__db, false, true, new Function1() { // from class: com.varun.pocketassistant.data.SegmentDao_Impl$$ExternalSyntheticLambda13
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return SegmentDao_Impl.deleteById$lambda$17(_sql, id, (SQLiteConnection) obj);
            }
        }, continuation);
        return objPerformSuspending == IntrinsicsKt.getCOROUTINE_SUSPENDED() ? objPerformSuspending : Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Unit deleteById$lambda$17(String $_sql, String $id, SQLiteConnection _connection) {
        Intrinsics.checkNotNullParameter(_connection, "_connection");
        SQLiteStatement _stmt = _connection.prepare($_sql);
        try {
            _stmt.mo7910bindText(1, $id);
            _stmt.step();
            return Unit.INSTANCE;
        } finally {
            _stmt.close();
        }
    }

    @Override // com.varun.pocketassistant.data.SegmentDao
    public Object deleteForSession(final String sessionId, Continuation<? super Unit> continuation) {
        final String _sql = "DELETE FROM segments WHERE sessionId = ?";
        Object objPerformSuspending = DBUtil.performSuspending(this.__db, false, true, new Function1() { // from class: com.varun.pocketassistant.data.SegmentDao_Impl$$ExternalSyntheticLambda1
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return SegmentDao_Impl.deleteForSession$lambda$18(_sql, sessionId, (SQLiteConnection) obj);
            }
        }, continuation);
        return objPerformSuspending == IntrinsicsKt.getCOROUTINE_SUSPENDED() ? objPerformSuspending : Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Unit deleteForSession$lambda$18(String $_sql, String $sessionId, SQLiteConnection _connection) {
        Intrinsics.checkNotNullParameter(_connection, "_connection");
        SQLiteStatement _stmt = _connection.prepare($_sql);
        try {
            _stmt.mo7910bindText(1, $sessionId);
            _stmt.step();
            return Unit.INSTANCE;
        } finally {
            _stmt.close();
        }
    }

    /* JADX INFO: compiled from: SegmentDao_Impl.kt */
    @Metadata(d1 = {"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003J\u0010\u0010\u0004\u001a\f\u0012\b\u0012\u0006\u0012\u0002\b\u00030\u00060\u0005¨\u0006\u0007"}, d2 = {"Lcom/varun/pocketassistant/data/SegmentDao_Impl$Companion;", "", "<init>", "()V", "getRequiredConverters", "", "Lkotlin/reflect/KClass;", "app_debug"}, k = 1, mv = {2, 2, 0}, xi = 48)
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        public final List<KClass<?>> getRequiredConverters() {
            return CollectionsKt.emptyList();
        }
    }
}
