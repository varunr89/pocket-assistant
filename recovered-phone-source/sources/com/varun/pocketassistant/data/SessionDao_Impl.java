package com.varun.pocketassistant.data;

import androidx.core.app.NotificationCompat;
import androidx.room.EntityDeleteOrUpdateAdapter;
import androidx.room.EntityInsertAdapter;
import androidx.room.RoomDatabase;
import androidx.room.coroutines.FlowUtil;
import androidx.room.util.DBUtil;
import androidx.room.util.SQLiteStatementUtil;
import androidx.sqlite.SQLiteConnection;
import androidx.sqlite.SQLiteStatement;
import java.util.ArrayList;
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

/* JADX INFO: compiled from: SessionDao_Impl.kt */
/* JADX INFO: loaded from: classes9.dex */
@Metadata(d1 = {"\u0000F\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0010\t\n\u0002\b\u0004\b\u0007\u0018\u0000 \u001e2\u00020\u0001:\u0001\u001eB\u000f\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0004\b\u0004\u0010\u0005J\u0016\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\bH\u0096@¢\u0006\u0002\u0010\u000eJ\u0016\u0010\u000f\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\bH\u0096@¢\u0006\u0002\u0010\u000eJ\u0014\u0010\u0010\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00120\u0011H\u0016J\u0018\u0010\u0013\u001a\u0004\u0018\u00010\b2\u0006\u0010\u0014\u001a\u00020\u0015H\u0096@¢\u0006\u0002\u0010\u0016J\u0018\u0010\u0017\u001a\u0004\u0018\u00010\b2\u0006\u0010\u0018\u001a\u00020\u0015H\u0096@¢\u0006\u0002\u0010\u0016J\u001c\u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\b0\u00122\u0006\u0010\u001a\u001a\u00020\u001bH\u0096@¢\u0006\u0002\u0010\u001cJ\u0016\u0010\u001d\u001a\u00020\f2\u0006\u0010\u0014\u001a\u00020\u0015H\u0096@¢\u0006\u0002\u0010\u0016R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\b0\u0007X\u0082\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\t\u001a\b\u0012\u0004\u0012\u00020\b0\nX\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u001f"}, d2 = {"Lcom/varun/pocketassistant/data/SessionDao_Impl;", "Lcom/varun/pocketassistant/data/SessionDao;", "__db", "Landroidx/room/RoomDatabase;", "<init>", "(Landroidx/room/RoomDatabase;)V", "__insertAdapterOfSessionEntity", "Landroidx/room/EntityInsertAdapter;", "Lcom/varun/pocketassistant/data/SessionEntity;", "__updateAdapterOfSessionEntity", "Landroidx/room/EntityDeleteOrUpdateAdapter;", "insert", "", "session", "(Lcom/varun/pocketassistant/data/SessionEntity;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "update", "observeSessions", "Lkotlinx/coroutines/flow/Flow;", "", "getById", "id", "", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getByStatus", NotificationCompat.CATEGORY_STATUS, "getEndedBefore", "cutoffMs", "", "(JLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "delete", "Companion", "app_debug"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class SessionDao_Impl implements SessionDao {
    private final RoomDatabase __db;
    private final EntityInsertAdapter<SessionEntity> __insertAdapterOfSessionEntity;
    private final EntityDeleteOrUpdateAdapter<SessionEntity> __updateAdapterOfSessionEntity;

    /* JADX INFO: renamed from: Companion, reason: from kotlin metadata */
    public static final Companion INSTANCE = new Companion(null);
    public static final int $stable = 8;

    public SessionDao_Impl(RoomDatabase __db) {
        Intrinsics.checkNotNullParameter(__db, "__db");
        this.__db = __db;
        this.__insertAdapterOfSessionEntity = new EntityInsertAdapter<SessionEntity>() { // from class: com.varun.pocketassistant.data.SessionDao_Impl.1
            @Override // androidx.room.EntityInsertAdapter
            protected String createQuery() {
                return "INSERT OR ABORT INTO `sessions` (`id`,`startedAtMs`,`endedAtMs`,`status`,`segmentCount`,`speechDurationMs`,`notes`) VALUES (?,?,?,?,?,?,?)";
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // androidx.room.EntityInsertAdapter
            public void bind(SQLiteStatement statement, SessionEntity entity) {
                Intrinsics.checkNotNullParameter(statement, "statement");
                Intrinsics.checkNotNullParameter(entity, "entity");
                statement.mo7910bindText(1, entity.getId());
                statement.mo7908bindLong(2, entity.getStartedAtMs());
                Long _tmpEndedAtMs = entity.getEndedAtMs();
                if (_tmpEndedAtMs != null) {
                    statement.mo7908bindLong(3, _tmpEndedAtMs.longValue());
                } else {
                    statement.mo7909bindNull(3);
                }
                statement.mo7910bindText(4, entity.getStatus());
                statement.mo7908bindLong(5, entity.getSegmentCount());
                statement.mo7908bindLong(6, entity.getSpeechDurationMs());
                String _tmpNotes = entity.getNotes();
                if (_tmpNotes == null) {
                    statement.mo7909bindNull(7);
                } else {
                    statement.mo7910bindText(7, _tmpNotes);
                }
            }
        };
        this.__updateAdapterOfSessionEntity = new EntityDeleteOrUpdateAdapter<SessionEntity>() { // from class: com.varun.pocketassistant.data.SessionDao_Impl.2
            @Override // androidx.room.EntityDeleteOrUpdateAdapter
            protected String createQuery() {
                return "UPDATE OR ABORT `sessions` SET `id` = ?,`startedAtMs` = ?,`endedAtMs` = ?,`status` = ?,`segmentCount` = ?,`speechDurationMs` = ?,`notes` = ? WHERE `id` = ?";
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // androidx.room.EntityDeleteOrUpdateAdapter
            public void bind(SQLiteStatement statement, SessionEntity entity) {
                Intrinsics.checkNotNullParameter(statement, "statement");
                Intrinsics.checkNotNullParameter(entity, "entity");
                statement.mo7910bindText(1, entity.getId());
                statement.mo7908bindLong(2, entity.getStartedAtMs());
                Long _tmpEndedAtMs = entity.getEndedAtMs();
                if (_tmpEndedAtMs != null) {
                    statement.mo7908bindLong(3, _tmpEndedAtMs.longValue());
                } else {
                    statement.mo7909bindNull(3);
                }
                statement.mo7910bindText(4, entity.getStatus());
                statement.mo7908bindLong(5, entity.getSegmentCount());
                statement.mo7908bindLong(6, entity.getSpeechDurationMs());
                String _tmpNotes = entity.getNotes();
                if (_tmpNotes == null) {
                    statement.mo7909bindNull(7);
                } else {
                    statement.mo7910bindText(7, _tmpNotes);
                }
                statement.mo7910bindText(8, entity.getId());
            }
        };
    }

    @Override // com.varun.pocketassistant.data.SessionDao
    public Object insert(final SessionEntity session, Continuation<? super Unit> continuation) {
        Object objPerformSuspending = DBUtil.performSuspending(this.__db, false, true, new Function1() { // from class: com.varun.pocketassistant.data.SessionDao_Impl$$ExternalSyntheticLambda4
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return SessionDao_Impl.insert$lambda$0(this.f$0, session, (SQLiteConnection) obj);
            }
        }, continuation);
        return objPerformSuspending == IntrinsicsKt.getCOROUTINE_SUSPENDED() ? objPerformSuspending : Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Unit insert$lambda$0(SessionDao_Impl this$0, SessionEntity $session, SQLiteConnection _connection) throws Exception {
        Intrinsics.checkNotNullParameter(_connection, "_connection");
        this$0.__insertAdapterOfSessionEntity.insert(_connection, $session);
        return Unit.INSTANCE;
    }

    @Override // com.varun.pocketassistant.data.SessionDao
    public Object update(final SessionEntity session, Continuation<? super Unit> continuation) {
        Object objPerformSuspending = DBUtil.performSuspending(this.__db, false, true, new Function1() { // from class: com.varun.pocketassistant.data.SessionDao_Impl$$ExternalSyntheticLambda1
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return SessionDao_Impl.update$lambda$1(this.f$0, session, (SQLiteConnection) obj);
            }
        }, continuation);
        return objPerformSuspending == IntrinsicsKt.getCOROUTINE_SUSPENDED() ? objPerformSuspending : Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Unit update$lambda$1(SessionDao_Impl this$0, SessionEntity $session, SQLiteConnection _connection) throws Exception {
        Intrinsics.checkNotNullParameter(_connection, "_connection");
        this$0.__updateAdapterOfSessionEntity.handle(_connection, $session);
        return Unit.INSTANCE;
    }

    @Override // com.varun.pocketassistant.data.SessionDao
    public Flow<List<SessionEntity>> observeSessions() {
        final String _sql = "SELECT * FROM sessions ORDER BY startedAtMs DESC";
        return FlowUtil.createFlow(this.__db, false, new String[]{"sessions"}, new Function1() { // from class: com.varun.pocketassistant.data.SessionDao_Impl$$ExternalSyntheticLambda5
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return SessionDao_Impl.observeSessions$lambda$2(_sql, (SQLiteConnection) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final List observeSessions$lambda$2(String $_sql, SQLiteConnection _connection) {
        Long _tmpEndedAtMs;
        String _tmpNotes;
        Intrinsics.checkNotNullParameter(_connection, "_connection");
        SQLiteStatement _stmt = _connection.prepare($_sql);
        try {
            int _columnIndexOfId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "id");
            int _columnIndexOfStartedAtMs = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "startedAtMs");
            int _columnIndexOfEndedAtMs = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "endedAtMs");
            int _columnIndexOfStatus = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, NotificationCompat.CATEGORY_STATUS);
            int _columnIndexOfSegmentCount = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "segmentCount");
            int _columnIndexOfSpeechDurationMs = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "speechDurationMs");
            int _columnIndexOfNotes = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "notes");
            List _result = new ArrayList();
            while (_stmt.step()) {
                String _tmpId = _stmt.getText(_columnIndexOfId);
                long _tmpStartedAtMs = _stmt.getLong(_columnIndexOfStartedAtMs);
                if (_stmt.isNull(_columnIndexOfEndedAtMs)) {
                    _tmpEndedAtMs = null;
                } else {
                    Long _tmpEndedAtMs2 = Long.valueOf(_stmt.getLong(_columnIndexOfEndedAtMs));
                    _tmpEndedAtMs = _tmpEndedAtMs2;
                }
                String _tmpStatus = _stmt.getText(_columnIndexOfStatus);
                int _tmpSegmentCount = (int) _stmt.getLong(_columnIndexOfSegmentCount);
                long _tmpSpeechDurationMs = _stmt.getLong(_columnIndexOfSpeechDurationMs);
                if (_stmt.isNull(_columnIndexOfNotes)) {
                    _tmpNotes = null;
                } else {
                    String _tmpNotes2 = _stmt.getText(_columnIndexOfNotes);
                    _tmpNotes = _tmpNotes2;
                }
                SessionEntity _item = new SessionEntity(_tmpId, _tmpStartedAtMs, _tmpEndedAtMs, _tmpStatus, _tmpSegmentCount, _tmpSpeechDurationMs, _tmpNotes);
                _result.add(_item);
            }
            return _result;
        } finally {
            _stmt.close();
        }
    }

    @Override // com.varun.pocketassistant.data.SessionDao
    public Object getById(final String id, Continuation<? super SessionEntity> continuation) {
        final String _sql = "SELECT * FROM sessions WHERE id = ?";
        return DBUtil.performSuspending(this.__db, true, false, new Function1() { // from class: com.varun.pocketassistant.data.SessionDao_Impl$$ExternalSyntheticLambda6
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return SessionDao_Impl.getById$lambda$3(_sql, id, (SQLiteConnection) obj);
            }
        }, continuation);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final SessionEntity getById$lambda$3(String $_sql, String $id, SQLiteConnection _connection) {
        SessionEntity _result;
        Long _tmpEndedAtMs;
        String _tmpNotes;
        Intrinsics.checkNotNullParameter(_connection, "_connection");
        SQLiteStatement _stmt = _connection.prepare($_sql);
        try {
            _stmt.mo7910bindText(1, $id);
            int _columnIndexOfId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "id");
            int _columnIndexOfStartedAtMs = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "startedAtMs");
            int _columnIndexOfEndedAtMs = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "endedAtMs");
            int _columnIndexOfStatus = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, NotificationCompat.CATEGORY_STATUS);
            int _columnIndexOfSegmentCount = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "segmentCount");
            int _columnIndexOfSpeechDurationMs = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "speechDurationMs");
            int _columnIndexOfNotes = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "notes");
            if (_stmt.step()) {
                String _tmpId = _stmt.getText(_columnIndexOfId);
                long _tmpStartedAtMs = _stmt.getLong(_columnIndexOfStartedAtMs);
                if (_stmt.isNull(_columnIndexOfEndedAtMs)) {
                    _tmpEndedAtMs = null;
                } else {
                    Long _tmpEndedAtMs2 = Long.valueOf(_stmt.getLong(_columnIndexOfEndedAtMs));
                    _tmpEndedAtMs = _tmpEndedAtMs2;
                }
                String _tmpStatus = _stmt.getText(_columnIndexOfStatus);
                int _tmpSegmentCount = (int) _stmt.getLong(_columnIndexOfSegmentCount);
                long _tmpSpeechDurationMs = _stmt.getLong(_columnIndexOfSpeechDurationMs);
                if (_stmt.isNull(_columnIndexOfNotes)) {
                    _tmpNotes = null;
                } else {
                    String _tmpNotes2 = _stmt.getText(_columnIndexOfNotes);
                    _tmpNotes = _tmpNotes2;
                }
                _result = new SessionEntity(_tmpId, _tmpStartedAtMs, _tmpEndedAtMs, _tmpStatus, _tmpSegmentCount, _tmpSpeechDurationMs, _tmpNotes);
            } else {
                _result = null;
            }
            return _result;
        } finally {
            _stmt.close();
        }
    }

    @Override // com.varun.pocketassistant.data.SessionDao
    public Object getByStatus(final String status, Continuation<? super SessionEntity> continuation) {
        final String _sql = "SELECT * FROM sessions WHERE status = ? LIMIT 1";
        return DBUtil.performSuspending(this.__db, true, false, new Function1() { // from class: com.varun.pocketassistant.data.SessionDao_Impl$$ExternalSyntheticLambda2
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return SessionDao_Impl.getByStatus$lambda$4(_sql, status, (SQLiteConnection) obj);
            }
        }, continuation);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final SessionEntity getByStatus$lambda$4(String $_sql, String $status, SQLiteConnection _connection) {
        SessionEntity _result;
        Long _tmpEndedAtMs;
        String _tmpNotes;
        Intrinsics.checkNotNullParameter(_connection, "_connection");
        SQLiteStatement _stmt = _connection.prepare($_sql);
        try {
            _stmt.mo7910bindText(1, $status);
            int _columnIndexOfId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "id");
            int _columnIndexOfStartedAtMs = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "startedAtMs");
            int _columnIndexOfEndedAtMs = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "endedAtMs");
            int _columnIndexOfStatus = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, NotificationCompat.CATEGORY_STATUS);
            int _columnIndexOfSegmentCount = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "segmentCount");
            int _columnIndexOfSpeechDurationMs = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "speechDurationMs");
            int _columnIndexOfNotes = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "notes");
            if (_stmt.step()) {
                String _tmpId = _stmt.getText(_columnIndexOfId);
                long _tmpStartedAtMs = _stmt.getLong(_columnIndexOfStartedAtMs);
                if (_stmt.isNull(_columnIndexOfEndedAtMs)) {
                    _tmpEndedAtMs = null;
                } else {
                    Long _tmpEndedAtMs2 = Long.valueOf(_stmt.getLong(_columnIndexOfEndedAtMs));
                    _tmpEndedAtMs = _tmpEndedAtMs2;
                }
                String _tmpStatus = _stmt.getText(_columnIndexOfStatus);
                int _tmpSegmentCount = (int) _stmt.getLong(_columnIndexOfSegmentCount);
                long _tmpSpeechDurationMs = _stmt.getLong(_columnIndexOfSpeechDurationMs);
                if (_stmt.isNull(_columnIndexOfNotes)) {
                    _tmpNotes = null;
                } else {
                    String _tmpNotes2 = _stmt.getText(_columnIndexOfNotes);
                    _tmpNotes = _tmpNotes2;
                }
                _result = new SessionEntity(_tmpId, _tmpStartedAtMs, _tmpEndedAtMs, _tmpStatus, _tmpSegmentCount, _tmpSpeechDurationMs, _tmpNotes);
            } else {
                _result = null;
            }
            return _result;
        } finally {
            _stmt.close();
        }
    }

    @Override // com.varun.pocketassistant.data.SessionDao
    public Object getEndedBefore(final long cutoffMs, Continuation<? super List<SessionEntity>> continuation) {
        final String _sql = "SELECT * FROM sessions WHERE endedAtMs IS NOT NULL AND endedAtMs < ?";
        return DBUtil.performSuspending(this.__db, true, false, new Function1() { // from class: com.varun.pocketassistant.data.SessionDao_Impl$$ExternalSyntheticLambda0
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return SessionDao_Impl.getEndedBefore$lambda$5(_sql, cutoffMs, (SQLiteConnection) obj);
            }
        }, continuation);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final List getEndedBefore$lambda$5(String $_sql, long $cutoffMs, SQLiteConnection _connection) {
        Long _tmpEndedAtMs;
        String _tmpNotes;
        Intrinsics.checkNotNullParameter(_connection, "_connection");
        SQLiteStatement _stmt = _connection.prepare($_sql);
        int _argIndex = 1;
        try {
            _stmt.mo7908bindLong(1, $cutoffMs);
            int _columnIndexOfId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "id");
            int _columnIndexOfStartedAtMs = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "startedAtMs");
            int _columnIndexOfEndedAtMs = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "endedAtMs");
            int _columnIndexOfStatus = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, NotificationCompat.CATEGORY_STATUS);
            int _columnIndexOfSegmentCount = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "segmentCount");
            int _columnIndexOfSpeechDurationMs = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "speechDurationMs");
            int _columnIndexOfNotes = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "notes");
            List _result = new ArrayList();
            while (_stmt.step()) {
                String _tmpId = _stmt.getText(_columnIndexOfId);
                long _tmpStartedAtMs = _stmt.getLong(_columnIndexOfStartedAtMs);
                if (_stmt.isNull(_columnIndexOfEndedAtMs)) {
                    _tmpEndedAtMs = null;
                } else {
                    Long _tmpEndedAtMs2 = Long.valueOf(_stmt.getLong(_columnIndexOfEndedAtMs));
                    _tmpEndedAtMs = _tmpEndedAtMs2;
                }
                String _tmpStatus = _stmt.getText(_columnIndexOfStatus);
                int _argIndex2 = _argIndex;
                int _tmpSegmentCount = (int) _stmt.getLong(_columnIndexOfSegmentCount);
                long _tmpSpeechDurationMs = _stmt.getLong(_columnIndexOfSpeechDurationMs);
                if (_stmt.isNull(_columnIndexOfNotes)) {
                    _tmpNotes = null;
                } else {
                    String _tmpNotes2 = _stmt.getText(_columnIndexOfNotes);
                    _tmpNotes = _tmpNotes2;
                }
                SessionEntity _item = new SessionEntity(_tmpId, _tmpStartedAtMs, _tmpEndedAtMs, _tmpStatus, _tmpSegmentCount, _tmpSpeechDurationMs, _tmpNotes);
                _result.add(_item);
                _argIndex = _argIndex2;
            }
            return _result;
        } finally {
            _stmt.close();
        }
    }

    @Override // com.varun.pocketassistant.data.SessionDao
    public Object delete(final String id, Continuation<? super Unit> continuation) {
        final String _sql = "DELETE FROM sessions WHERE id = ?";
        Object objPerformSuspending = DBUtil.performSuspending(this.__db, false, true, new Function1() { // from class: com.varun.pocketassistant.data.SessionDao_Impl$$ExternalSyntheticLambda3
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return SessionDao_Impl.delete$lambda$6(_sql, id, (SQLiteConnection) obj);
            }
        }, continuation);
        return objPerformSuspending == IntrinsicsKt.getCOROUTINE_SUSPENDED() ? objPerformSuspending : Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Unit delete$lambda$6(String $_sql, String $id, SQLiteConnection _connection) {
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

    /* JADX INFO: compiled from: SessionDao_Impl.kt */
    @Metadata(d1 = {"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003J\u0010\u0010\u0004\u001a\f\u0012\b\u0012\u0006\u0012\u0002\b\u00030\u00060\u0005¨\u0006\u0007"}, d2 = {"Lcom/varun/pocketassistant/data/SessionDao_Impl$Companion;", "", "<init>", "()V", "getRequiredConverters", "", "Lkotlin/reflect/KClass;", "app_debug"}, k = 1, mv = {2, 2, 0}, xi = 48)
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
