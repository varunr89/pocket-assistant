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

/* JADX INFO: compiled from: MeetingDao_Impl.kt */
/* JADX INFO: loaded from: classes9.dex */
@Metadata(d1 = {"\u0000F\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\b\n\u0002\u0010\b\n\u0002\b\u0006\b\u0007\u0018\u0000 #2\u00020\u0001:\u0001#B\u000f\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0004\b\u0004\u0010\u0005J\u0016\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\bH\u0096@¢\u0006\u0002\u0010\u000eJ\u0016\u0010\u000f\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\bH\u0096@¢\u0006\u0002\u0010\u000eJ\u0014\u0010\u0010\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00120\u0011H\u0016J\u001c\u0010\u0013\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00120\u00112\u0006\u0010\u0014\u001a\u00020\u0015H\u0016J\u0018\u0010\u0016\u001a\u0004\u0018\u00010\b2\u0006\u0010\u0017\u001a\u00020\u0015H\u0096@¢\u0006\u0002\u0010\u0018J\u0018\u0010\u0019\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\b0\u00112\u0006\u0010\u0017\u001a\u00020\u0015H\u0016J,\u0010\u001a\u001a\b\u0012\u0004\u0012\u00020\b0\u00122\u0006\u0010\u001b\u001a\u00020\u00152\u0006\u0010\u001c\u001a\u00020\u00152\u0006\u0010\u001d\u001a\u00020\u001eH\u0096@¢\u0006\u0002\u0010\u001fJ\u001c\u0010 \u001a\b\u0012\u0004\u0012\u00020\b0\u00122\u0006\u0010\u001d\u001a\u00020\u001eH\u0096@¢\u0006\u0002\u0010!J\u0016\u0010\"\u001a\u00020\f2\u0006\u0010\u0017\u001a\u00020\u0015H\u0096@¢\u0006\u0002\u0010\u0018R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\b0\u0007X\u0082\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\t\u001a\b\u0012\u0004\u0012\u00020\b0\nX\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006$"}, d2 = {"Lcom/varun/pocketassistant/data/MeetingDao_Impl;", "Lcom/varun/pocketassistant/data/MeetingDao;", "__db", "Landroidx/room/RoomDatabase;", "<init>", "(Landroidx/room/RoomDatabase;)V", "__insertAdapterOfMeetingEntity", "Landroidx/room/EntityInsertAdapter;", "Lcom/varun/pocketassistant/data/MeetingEntity;", "__updateAdapterOfMeetingEntity", "Landroidx/room/EntityDeleteOrUpdateAdapter;", "insert", "", "meeting", "(Lcom/varun/pocketassistant/data/MeetingEntity;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "update", "observeAll", "Lkotlinx/coroutines/flow/Flow;", "", "search", "q", "", "getById", "id", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "observeById", "getNeedingCleanup", "a", "b", "limit", "", "(Ljava/lang/String;Ljava/lang/String;ILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getAll", "(ILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "delete", "Companion", "app_debug"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class MeetingDao_Impl implements MeetingDao {
    private final RoomDatabase __db;
    private final EntityInsertAdapter<MeetingEntity> __insertAdapterOfMeetingEntity;
    private final EntityDeleteOrUpdateAdapter<MeetingEntity> __updateAdapterOfMeetingEntity;

    /* JADX INFO: renamed from: Companion, reason: from kotlin metadata */
    public static final Companion INSTANCE = new Companion(null);
    public static final int $stable = 8;

    public MeetingDao_Impl(RoomDatabase __db) {
        Intrinsics.checkNotNullParameter(__db, "__db");
        this.__db = __db;
        this.__insertAdapterOfMeetingEntity = new EntityInsertAdapter<MeetingEntity>() { // from class: com.varun.pocketassistant.data.MeetingDao_Impl.1
            @Override // androidx.room.EntityInsertAdapter
            protected String createQuery() {
                return "INSERT OR ABORT INTO `meetings` (`id`,`title`,`startedAtMs`,`endedAtMs`,`status`,`cleanedTranscript`,`metadataJson`,`cleanupProvider`,`createdAtMs`,`cleanTextOnly`,`lastError`,`updatedAtMs`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // androidx.room.EntityInsertAdapter
            public void bind(SQLiteStatement statement, MeetingEntity entity) {
                Intrinsics.checkNotNullParameter(statement, "statement");
                Intrinsics.checkNotNullParameter(entity, "entity");
                statement.mo7910bindText(1, entity.getId());
                String _tmpTitle = entity.getTitle();
                if (_tmpTitle == null) {
                    statement.mo7909bindNull(2);
                } else {
                    statement.mo7910bindText(2, _tmpTitle);
                }
                statement.mo7908bindLong(3, entity.getStartedAtMs());
                statement.mo7908bindLong(4, entity.getEndedAtMs());
                statement.mo7910bindText(5, entity.getStatus());
                String _tmpCleanedTranscript = entity.getCleanedTranscript();
                if (_tmpCleanedTranscript == null) {
                    statement.mo7909bindNull(6);
                } else {
                    statement.mo7910bindText(6, _tmpCleanedTranscript);
                }
                String _tmpMetadataJson = entity.getMetadataJson();
                if (_tmpMetadataJson == null) {
                    statement.mo7909bindNull(7);
                } else {
                    statement.mo7910bindText(7, _tmpMetadataJson);
                }
                String _tmpCleanupProvider = entity.getCleanupProvider();
                if (_tmpCleanupProvider == null) {
                    statement.mo7909bindNull(8);
                } else {
                    statement.mo7910bindText(8, _tmpCleanupProvider);
                }
                statement.mo7908bindLong(9, entity.getCreatedAtMs());
                String _tmpCleanTextOnly = entity.getCleanTextOnly();
                if (_tmpCleanTextOnly == null) {
                    statement.mo7909bindNull(10);
                } else {
                    statement.mo7910bindText(10, _tmpCleanTextOnly);
                }
                String _tmpLastError = entity.getLastError();
                if (_tmpLastError == null) {
                    statement.mo7909bindNull(11);
                } else {
                    statement.mo7910bindText(11, _tmpLastError);
                }
                statement.mo7908bindLong(12, entity.getUpdatedAtMs());
            }
        };
        this.__updateAdapterOfMeetingEntity = new EntityDeleteOrUpdateAdapter<MeetingEntity>() { // from class: com.varun.pocketassistant.data.MeetingDao_Impl.2
            @Override // androidx.room.EntityDeleteOrUpdateAdapter
            protected String createQuery() {
                return "UPDATE OR ABORT `meetings` SET `id` = ?,`title` = ?,`startedAtMs` = ?,`endedAtMs` = ?,`status` = ?,`cleanedTranscript` = ?,`metadataJson` = ?,`cleanupProvider` = ?,`createdAtMs` = ?,`cleanTextOnly` = ?,`lastError` = ?,`updatedAtMs` = ? WHERE `id` = ?";
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // androidx.room.EntityDeleteOrUpdateAdapter
            public void bind(SQLiteStatement statement, MeetingEntity entity) {
                Intrinsics.checkNotNullParameter(statement, "statement");
                Intrinsics.checkNotNullParameter(entity, "entity");
                statement.mo7910bindText(1, entity.getId());
                String _tmpTitle = entity.getTitle();
                if (_tmpTitle == null) {
                    statement.mo7909bindNull(2);
                } else {
                    statement.mo7910bindText(2, _tmpTitle);
                }
                statement.mo7908bindLong(3, entity.getStartedAtMs());
                statement.mo7908bindLong(4, entity.getEndedAtMs());
                statement.mo7910bindText(5, entity.getStatus());
                String _tmpCleanedTranscript = entity.getCleanedTranscript();
                if (_tmpCleanedTranscript == null) {
                    statement.mo7909bindNull(6);
                } else {
                    statement.mo7910bindText(6, _tmpCleanedTranscript);
                }
                String _tmpMetadataJson = entity.getMetadataJson();
                if (_tmpMetadataJson == null) {
                    statement.mo7909bindNull(7);
                } else {
                    statement.mo7910bindText(7, _tmpMetadataJson);
                }
                String _tmpCleanupProvider = entity.getCleanupProvider();
                if (_tmpCleanupProvider == null) {
                    statement.mo7909bindNull(8);
                } else {
                    statement.mo7910bindText(8, _tmpCleanupProvider);
                }
                statement.mo7908bindLong(9, entity.getCreatedAtMs());
                String _tmpCleanTextOnly = entity.getCleanTextOnly();
                if (_tmpCleanTextOnly == null) {
                    statement.mo7909bindNull(10);
                } else {
                    statement.mo7910bindText(10, _tmpCleanTextOnly);
                }
                String _tmpLastError = entity.getLastError();
                if (_tmpLastError == null) {
                    statement.mo7909bindNull(11);
                } else {
                    statement.mo7910bindText(11, _tmpLastError);
                }
                statement.mo7908bindLong(12, entity.getUpdatedAtMs());
                statement.mo7910bindText(13, entity.getId());
            }
        };
    }

    @Override // com.varun.pocketassistant.data.MeetingDao
    public Object insert(final MeetingEntity meeting, Continuation<? super Unit> continuation) {
        Object objPerformSuspending = DBUtil.performSuspending(this.__db, false, true, new Function1() { // from class: com.varun.pocketassistant.data.MeetingDao_Impl$$ExternalSyntheticLambda4
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return MeetingDao_Impl.insert$lambda$0(this.f$0, meeting, (SQLiteConnection) obj);
            }
        }, continuation);
        return objPerformSuspending == IntrinsicsKt.getCOROUTINE_SUSPENDED() ? objPerformSuspending : Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Unit insert$lambda$0(MeetingDao_Impl this$0, MeetingEntity $meeting, SQLiteConnection _connection) throws Exception {
        Intrinsics.checkNotNullParameter(_connection, "_connection");
        this$0.__insertAdapterOfMeetingEntity.insert(_connection, $meeting);
        return Unit.INSTANCE;
    }

    @Override // com.varun.pocketassistant.data.MeetingDao
    public Object update(final MeetingEntity meeting, Continuation<? super Unit> continuation) {
        Object objPerformSuspending = DBUtil.performSuspending(this.__db, false, true, new Function1() { // from class: com.varun.pocketassistant.data.MeetingDao_Impl$$ExternalSyntheticLambda3
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return MeetingDao_Impl.update$lambda$1(this.f$0, meeting, (SQLiteConnection) obj);
            }
        }, continuation);
        return objPerformSuspending == IntrinsicsKt.getCOROUTINE_SUSPENDED() ? objPerformSuspending : Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Unit update$lambda$1(MeetingDao_Impl this$0, MeetingEntity $meeting, SQLiteConnection _connection) throws Exception {
        Intrinsics.checkNotNullParameter(_connection, "_connection");
        this$0.__updateAdapterOfMeetingEntity.handle(_connection, $meeting);
        return Unit.INSTANCE;
    }

    @Override // com.varun.pocketassistant.data.MeetingDao
    public Flow<List<MeetingEntity>> observeAll() {
        final String _sql = "SELECT * FROM meetings ORDER BY startedAtMs DESC";
        return FlowUtil.createFlow(this.__db, false, new String[]{"meetings"}, new Function1() { // from class: com.varun.pocketassistant.data.MeetingDao_Impl$$ExternalSyntheticLambda8
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return MeetingDao_Impl.observeAll$lambda$2(_sql, (SQLiteConnection) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final List observeAll$lambda$2(String $_sql, SQLiteConnection _connection) {
        String _tmpTitle;
        String _tmpCleanedTranscript;
        String _tmpMetadataJson;
        String _tmpCleanupProvider;
        String _tmpCleanTextOnly;
        String _tmpLastError;
        Intrinsics.checkNotNullParameter(_connection, "_connection");
        SQLiteStatement _stmt = _connection.prepare($_sql);
        try {
            int _columnIndexOfId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "id");
            int _columnIndexOfTitle = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "title");
            int _columnIndexOfStartedAtMs = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "startedAtMs");
            int _columnIndexOfEndedAtMs = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "endedAtMs");
            int _columnIndexOfStatus = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, NotificationCompat.CATEGORY_STATUS);
            int _columnIndexOfCleanedTranscript = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "cleanedTranscript");
            int _columnIndexOfMetadataJson = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "metadataJson");
            int _columnIndexOfCleanupProvider = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "cleanupProvider");
            int _columnIndexOfCreatedAtMs = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "createdAtMs");
            int _columnIndexOfCleanTextOnly = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "cleanTextOnly");
            int _columnIndexOfLastError = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "lastError");
            int _columnIndexOfUpdatedAtMs = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "updatedAtMs");
            List _result = new ArrayList();
            while (_stmt.step()) {
                String _tmpId = _stmt.getText(_columnIndexOfId);
                if (_stmt.isNull(_columnIndexOfTitle)) {
                    _tmpTitle = null;
                } else {
                    String _tmpTitle2 = _stmt.getText(_columnIndexOfTitle);
                    _tmpTitle = _tmpTitle2;
                }
                long _tmpStartedAtMs = _stmt.getLong(_columnIndexOfStartedAtMs);
                long _tmpEndedAtMs = _stmt.getLong(_columnIndexOfEndedAtMs);
                String _tmpStatus = _stmt.getText(_columnIndexOfStatus);
                if (_stmt.isNull(_columnIndexOfCleanedTranscript)) {
                    _tmpCleanedTranscript = null;
                } else {
                    String _tmpCleanedTranscript2 = _stmt.getText(_columnIndexOfCleanedTranscript);
                    _tmpCleanedTranscript = _tmpCleanedTranscript2;
                }
                if (_stmt.isNull(_columnIndexOfMetadataJson)) {
                    _tmpMetadataJson = null;
                } else {
                    String _tmpMetadataJson2 = _stmt.getText(_columnIndexOfMetadataJson);
                    _tmpMetadataJson = _tmpMetadataJson2;
                }
                if (_stmt.isNull(_columnIndexOfCleanupProvider)) {
                    _tmpCleanupProvider = null;
                } else {
                    String _tmpCleanupProvider2 = _stmt.getText(_columnIndexOfCleanupProvider);
                    _tmpCleanupProvider = _tmpCleanupProvider2;
                }
                long _tmpCreatedAtMs = _stmt.getLong(_columnIndexOfCreatedAtMs);
                if (_stmt.isNull(_columnIndexOfCleanTextOnly)) {
                    _tmpCleanTextOnly = null;
                } else {
                    String _tmpCleanTextOnly2 = _stmt.getText(_columnIndexOfCleanTextOnly);
                    _tmpCleanTextOnly = _tmpCleanTextOnly2;
                }
                if (_stmt.isNull(_columnIndexOfLastError)) {
                    _tmpLastError = null;
                } else {
                    String _tmpLastError2 = _stmt.getText(_columnIndexOfLastError);
                    _tmpLastError = _tmpLastError2;
                }
                long _tmpUpdatedAtMs = _stmt.getLong(_columnIndexOfUpdatedAtMs);
                MeetingEntity _item = new MeetingEntity(_tmpId, _tmpTitle, _tmpStartedAtMs, _tmpEndedAtMs, _tmpStatus, _tmpCleanedTranscript, _tmpMetadataJson, _tmpCleanupProvider, _tmpCreatedAtMs, _tmpCleanTextOnly, _tmpLastError, _tmpUpdatedAtMs);
                int _columnIndexOfId2 = _columnIndexOfId;
                _result.add(_item);
                _columnIndexOfId = _columnIndexOfId2;
            }
            return _result;
        } finally {
            _stmt.close();
        }
    }

    @Override // com.varun.pocketassistant.data.MeetingDao
    public Flow<List<MeetingEntity>> search(final String q) {
        Intrinsics.checkNotNullParameter(q, "q");
        final String _sql = "\n        SELECT * FROM meetings\n        WHERE title LIKE '%' || ? || '%'\n           OR metadataJson LIKE '%' || ? || '%'\n           OR cleanedTranscript LIKE '%' || ? || '%'\n        ORDER BY startedAtMs DESC\n        ";
        return FlowUtil.createFlow(this.__db, false, new String[]{"meetings"}, new Function1() { // from class: com.varun.pocketassistant.data.MeetingDao_Impl$$ExternalSyntheticLambda2
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return MeetingDao_Impl.search$lambda$3(_sql, q, (SQLiteConnection) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final List search$lambda$3(String $_sql, String $q, SQLiteConnection _connection) {
        String _tmpTitle;
        String _tmpCleanedTranscript;
        String _tmpMetadataJson;
        String _tmpCleanupProvider;
        String _tmpCleanTextOnly;
        String _tmpLastError;
        Intrinsics.checkNotNullParameter(_connection, "_connection");
        SQLiteStatement _stmt = _connection.prepare($_sql);
        try {
            _stmt.mo7910bindText(1, $q);
            _stmt.mo7910bindText(2, $q);
            _stmt.mo7910bindText(3, $q);
            int _columnIndexOfId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "id");
            int _columnIndexOfTitle = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "title");
            int _columnIndexOfStartedAtMs = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "startedAtMs");
            int _columnIndexOfEndedAtMs = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "endedAtMs");
            int _columnIndexOfStatus = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, NotificationCompat.CATEGORY_STATUS);
            int _columnIndexOfCleanedTranscript = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "cleanedTranscript");
            int _columnIndexOfMetadataJson = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "metadataJson");
            int _columnIndexOfCleanupProvider = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "cleanupProvider");
            int _columnIndexOfCreatedAtMs = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "createdAtMs");
            int _columnIndexOfCleanTextOnly = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "cleanTextOnly");
            int _columnIndexOfLastError = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "lastError");
            int _columnIndexOfUpdatedAtMs = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "updatedAtMs");
            List _result = new ArrayList();
            while (_stmt.step()) {
                String _tmpId = _stmt.getText(_columnIndexOfId);
                if (_stmt.isNull(_columnIndexOfTitle)) {
                    _tmpTitle = null;
                } else {
                    String _tmpTitle2 = _stmt.getText(_columnIndexOfTitle);
                    _tmpTitle = _tmpTitle2;
                }
                long _tmpStartedAtMs = _stmt.getLong(_columnIndexOfStartedAtMs);
                long _tmpEndedAtMs = _stmt.getLong(_columnIndexOfEndedAtMs);
                String _tmpStatus = _stmt.getText(_columnIndexOfStatus);
                if (_stmt.isNull(_columnIndexOfCleanedTranscript)) {
                    _tmpCleanedTranscript = null;
                } else {
                    String _tmpCleanedTranscript2 = _stmt.getText(_columnIndexOfCleanedTranscript);
                    _tmpCleanedTranscript = _tmpCleanedTranscript2;
                }
                if (_stmt.isNull(_columnIndexOfMetadataJson)) {
                    _tmpMetadataJson = null;
                } else {
                    String _tmpMetadataJson2 = _stmt.getText(_columnIndexOfMetadataJson);
                    _tmpMetadataJson = _tmpMetadataJson2;
                }
                if (_stmt.isNull(_columnIndexOfCleanupProvider)) {
                    _tmpCleanupProvider = null;
                } else {
                    String _tmpCleanupProvider2 = _stmt.getText(_columnIndexOfCleanupProvider);
                    _tmpCleanupProvider = _tmpCleanupProvider2;
                }
                long _tmpCreatedAtMs = _stmt.getLong(_columnIndexOfCreatedAtMs);
                if (_stmt.isNull(_columnIndexOfCleanTextOnly)) {
                    _tmpCleanTextOnly = null;
                } else {
                    String _tmpCleanTextOnly2 = _stmt.getText(_columnIndexOfCleanTextOnly);
                    _tmpCleanTextOnly = _tmpCleanTextOnly2;
                }
                if (_stmt.isNull(_columnIndexOfLastError)) {
                    _tmpLastError = null;
                } else {
                    String _tmpLastError2 = _stmt.getText(_columnIndexOfLastError);
                    _tmpLastError = _tmpLastError2;
                }
                long _tmpUpdatedAtMs = _stmt.getLong(_columnIndexOfUpdatedAtMs);
                MeetingEntity _item = new MeetingEntity(_tmpId, _tmpTitle, _tmpStartedAtMs, _tmpEndedAtMs, _tmpStatus, _tmpCleanedTranscript, _tmpMetadataJson, _tmpCleanupProvider, _tmpCreatedAtMs, _tmpCleanTextOnly, _tmpLastError, _tmpUpdatedAtMs);
                int _columnIndexOfUpdatedAtMs2 = _columnIndexOfUpdatedAtMs;
                List _result2 = _result;
                _result2.add(_item);
                _result = _result2;
                _columnIndexOfUpdatedAtMs = _columnIndexOfUpdatedAtMs2;
            }
            return _result;
        } finally {
            _stmt.close();
        }
    }

    @Override // com.varun.pocketassistant.data.MeetingDao
    public Object getById(final String id, Continuation<? super MeetingEntity> continuation) {
        final String _sql = "SELECT * FROM meetings WHERE id = ?";
        return DBUtil.performSuspending(this.__db, true, false, new Function1() { // from class: com.varun.pocketassistant.data.MeetingDao_Impl$$ExternalSyntheticLambda0
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return MeetingDao_Impl.getById$lambda$4(_sql, id, (SQLiteConnection) obj);
            }
        }, continuation);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final MeetingEntity getById$lambda$4(String $_sql, String $id, SQLiteConnection _connection) {
        MeetingEntity _result;
        String _tmpTitle;
        String _tmpCleanedTranscript;
        String _tmpMetadataJson;
        String _tmpCleanupProvider;
        String _tmpCleanTextOnly;
        String _tmpLastError;
        Intrinsics.checkNotNullParameter(_connection, "_connection");
        SQLiteStatement _stmt = _connection.prepare($_sql);
        try {
            _stmt.mo7910bindText(1, $id);
            int _columnIndexOfId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "id");
            int _columnIndexOfTitle = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "title");
            int _columnIndexOfStartedAtMs = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "startedAtMs");
            int _columnIndexOfEndedAtMs = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "endedAtMs");
            int _columnIndexOfStatus = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, NotificationCompat.CATEGORY_STATUS);
            int _columnIndexOfCleanedTranscript = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "cleanedTranscript");
            int _columnIndexOfMetadataJson = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "metadataJson");
            int _columnIndexOfCleanupProvider = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "cleanupProvider");
            int _columnIndexOfCreatedAtMs = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "createdAtMs");
            int _columnIndexOfCleanTextOnly = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "cleanTextOnly");
            int _columnIndexOfLastError = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "lastError");
            int _columnIndexOfUpdatedAtMs = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "updatedAtMs");
            if (_stmt.step()) {
                String _tmpId = _stmt.getText(_columnIndexOfId);
                if (_stmt.isNull(_columnIndexOfTitle)) {
                    _tmpTitle = null;
                } else {
                    String _tmpTitle2 = _stmt.getText(_columnIndexOfTitle);
                    _tmpTitle = _tmpTitle2;
                }
                long _tmpStartedAtMs = _stmt.getLong(_columnIndexOfStartedAtMs);
                long _tmpEndedAtMs = _stmt.getLong(_columnIndexOfEndedAtMs);
                String _tmpStatus = _stmt.getText(_columnIndexOfStatus);
                if (_stmt.isNull(_columnIndexOfCleanedTranscript)) {
                    _tmpCleanedTranscript = null;
                } else {
                    String _tmpCleanedTranscript2 = _stmt.getText(_columnIndexOfCleanedTranscript);
                    _tmpCleanedTranscript = _tmpCleanedTranscript2;
                }
                if (_stmt.isNull(_columnIndexOfMetadataJson)) {
                    _tmpMetadataJson = null;
                } else {
                    String _tmpMetadataJson2 = _stmt.getText(_columnIndexOfMetadataJson);
                    _tmpMetadataJson = _tmpMetadataJson2;
                }
                if (_stmt.isNull(_columnIndexOfCleanupProvider)) {
                    _tmpCleanupProvider = null;
                } else {
                    String _tmpCleanupProvider2 = _stmt.getText(_columnIndexOfCleanupProvider);
                    _tmpCleanupProvider = _tmpCleanupProvider2;
                }
                long _tmpCreatedAtMs = _stmt.getLong(_columnIndexOfCreatedAtMs);
                if (_stmt.isNull(_columnIndexOfCleanTextOnly)) {
                    _tmpCleanTextOnly = null;
                } else {
                    String _tmpCleanTextOnly2 = _stmt.getText(_columnIndexOfCleanTextOnly);
                    _tmpCleanTextOnly = _tmpCleanTextOnly2;
                }
                if (_stmt.isNull(_columnIndexOfLastError)) {
                    _tmpLastError = null;
                } else {
                    String _tmpLastError2 = _stmt.getText(_columnIndexOfLastError);
                    _tmpLastError = _tmpLastError2;
                }
                long _tmpUpdatedAtMs = _stmt.getLong(_columnIndexOfUpdatedAtMs);
                _result = new MeetingEntity(_tmpId, _tmpTitle, _tmpStartedAtMs, _tmpEndedAtMs, _tmpStatus, _tmpCleanedTranscript, _tmpMetadataJson, _tmpCleanupProvider, _tmpCreatedAtMs, _tmpCleanTextOnly, _tmpLastError, _tmpUpdatedAtMs);
            } else {
                _result = null;
            }
            return _result;
        } finally {
            _stmt.close();
        }
    }

    @Override // com.varun.pocketassistant.data.MeetingDao
    public Flow<MeetingEntity> observeById(final String id) {
        Intrinsics.checkNotNullParameter(id, "id");
        final String _sql = "SELECT * FROM meetings WHERE id = ?";
        return FlowUtil.createFlow(this.__db, false, new String[]{"meetings"}, new Function1() { // from class: com.varun.pocketassistant.data.MeetingDao_Impl$$ExternalSyntheticLambda1
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return MeetingDao_Impl.observeById$lambda$5(_sql, id, (SQLiteConnection) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final MeetingEntity observeById$lambda$5(String $_sql, String $id, SQLiteConnection _connection) {
        MeetingEntity _result;
        String _tmpTitle;
        String _tmpCleanedTranscript;
        String _tmpMetadataJson;
        String _tmpCleanupProvider;
        String _tmpCleanTextOnly;
        String _tmpLastError;
        Intrinsics.checkNotNullParameter(_connection, "_connection");
        SQLiteStatement _stmt = _connection.prepare($_sql);
        try {
            _stmt.mo7910bindText(1, $id);
            int _columnIndexOfId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "id");
            int _columnIndexOfTitle = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "title");
            int _columnIndexOfStartedAtMs = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "startedAtMs");
            int _columnIndexOfEndedAtMs = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "endedAtMs");
            int _columnIndexOfStatus = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, NotificationCompat.CATEGORY_STATUS);
            int _columnIndexOfCleanedTranscript = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "cleanedTranscript");
            int _columnIndexOfMetadataJson = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "metadataJson");
            int _columnIndexOfCleanupProvider = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "cleanupProvider");
            int _columnIndexOfCreatedAtMs = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "createdAtMs");
            int _columnIndexOfCleanTextOnly = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "cleanTextOnly");
            int _columnIndexOfLastError = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "lastError");
            int _columnIndexOfUpdatedAtMs = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "updatedAtMs");
            if (_stmt.step()) {
                String _tmpId = _stmt.getText(_columnIndexOfId);
                if (_stmt.isNull(_columnIndexOfTitle)) {
                    _tmpTitle = null;
                } else {
                    String _tmpTitle2 = _stmt.getText(_columnIndexOfTitle);
                    _tmpTitle = _tmpTitle2;
                }
                long _tmpStartedAtMs = _stmt.getLong(_columnIndexOfStartedAtMs);
                long _tmpEndedAtMs = _stmt.getLong(_columnIndexOfEndedAtMs);
                String _tmpStatus = _stmt.getText(_columnIndexOfStatus);
                if (_stmt.isNull(_columnIndexOfCleanedTranscript)) {
                    _tmpCleanedTranscript = null;
                } else {
                    String _tmpCleanedTranscript2 = _stmt.getText(_columnIndexOfCleanedTranscript);
                    _tmpCleanedTranscript = _tmpCleanedTranscript2;
                }
                if (_stmt.isNull(_columnIndexOfMetadataJson)) {
                    _tmpMetadataJson = null;
                } else {
                    String _tmpMetadataJson2 = _stmt.getText(_columnIndexOfMetadataJson);
                    _tmpMetadataJson = _tmpMetadataJson2;
                }
                if (_stmt.isNull(_columnIndexOfCleanupProvider)) {
                    _tmpCleanupProvider = null;
                } else {
                    String _tmpCleanupProvider2 = _stmt.getText(_columnIndexOfCleanupProvider);
                    _tmpCleanupProvider = _tmpCleanupProvider2;
                }
                long _tmpCreatedAtMs = _stmt.getLong(_columnIndexOfCreatedAtMs);
                if (_stmt.isNull(_columnIndexOfCleanTextOnly)) {
                    _tmpCleanTextOnly = null;
                } else {
                    String _tmpCleanTextOnly2 = _stmt.getText(_columnIndexOfCleanTextOnly);
                    _tmpCleanTextOnly = _tmpCleanTextOnly2;
                }
                if (_stmt.isNull(_columnIndexOfLastError)) {
                    _tmpLastError = null;
                } else {
                    String _tmpLastError2 = _stmt.getText(_columnIndexOfLastError);
                    _tmpLastError = _tmpLastError2;
                }
                long _tmpUpdatedAtMs = _stmt.getLong(_columnIndexOfUpdatedAtMs);
                _result = new MeetingEntity(_tmpId, _tmpTitle, _tmpStartedAtMs, _tmpEndedAtMs, _tmpStatus, _tmpCleanedTranscript, _tmpMetadataJson, _tmpCleanupProvider, _tmpCreatedAtMs, _tmpCleanTextOnly, _tmpLastError, _tmpUpdatedAtMs);
            } else {
                _result = null;
            }
            return _result;
        } finally {
            _stmt.close();
        }
    }

    @Override // com.varun.pocketassistant.data.MeetingDao
    public Object getNeedingCleanup(final String a, final String b, final int limit, Continuation<? super List<MeetingEntity>> continuation) {
        final String _sql = "SELECT * FROM meetings WHERE status IN (?, ?) ORDER BY createdAtMs ASC LIMIT ?";
        return DBUtil.performSuspending(this.__db, true, false, new Function1() { // from class: com.varun.pocketassistant.data.MeetingDao_Impl$$ExternalSyntheticLambda5
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return MeetingDao_Impl.getNeedingCleanup$lambda$6(_sql, a, b, limit, (SQLiteConnection) obj);
            }
        }, continuation);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final List getNeedingCleanup$lambda$6(String $_sql, String $a, String $b, int $limit, SQLiteConnection _connection) throws Throwable {
        String _tmpTitle;
        String _tmpCleanedTranscript;
        String _tmpMetadataJson;
        String _tmpCleanupProvider;
        String _tmpCleanTextOnly;
        String _tmpLastError;
        Intrinsics.checkNotNullParameter(_connection, "_connection");
        SQLiteStatement _stmt = _connection.prepare($_sql);
        try {
            _stmt.mo7910bindText(1, $a);
            try {
                _stmt.mo7910bindText(2, $b);
                try {
                    _stmt.mo7908bindLong(3, $limit);
                    int _columnIndexOfId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "id");
                    int _columnIndexOfTitle = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "title");
                    int _columnIndexOfStartedAtMs = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "startedAtMs");
                    int _columnIndexOfEndedAtMs = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "endedAtMs");
                    int _columnIndexOfStatus = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, NotificationCompat.CATEGORY_STATUS);
                    int _columnIndexOfCleanedTranscript = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "cleanedTranscript");
                    int _columnIndexOfMetadataJson = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "metadataJson");
                    int _columnIndexOfCleanupProvider = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "cleanupProvider");
                    int _columnIndexOfCreatedAtMs = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "createdAtMs");
                    int _columnIndexOfCleanTextOnly = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "cleanTextOnly");
                    int _columnIndexOfLastError = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "lastError");
                    int _columnIndexOfUpdatedAtMs = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "updatedAtMs");
                    List _result = new ArrayList();
                    while (_stmt.step()) {
                        String _tmpId = _stmt.getText(_columnIndexOfId);
                        if (_stmt.isNull(_columnIndexOfTitle)) {
                            _tmpTitle = null;
                        } else {
                            String _tmpTitle2 = _stmt.getText(_columnIndexOfTitle);
                            _tmpTitle = _tmpTitle2;
                        }
                        long _tmpStartedAtMs = _stmt.getLong(_columnIndexOfStartedAtMs);
                        long _tmpEndedAtMs = _stmt.getLong(_columnIndexOfEndedAtMs);
                        String _tmpStatus = _stmt.getText(_columnIndexOfStatus);
                        if (_stmt.isNull(_columnIndexOfCleanedTranscript)) {
                            _tmpCleanedTranscript = null;
                        } else {
                            String _tmpCleanedTranscript2 = _stmt.getText(_columnIndexOfCleanedTranscript);
                            _tmpCleanedTranscript = _tmpCleanedTranscript2;
                        }
                        if (_stmt.isNull(_columnIndexOfMetadataJson)) {
                            _tmpMetadataJson = null;
                        } else {
                            String _tmpMetadataJson2 = _stmt.getText(_columnIndexOfMetadataJson);
                            _tmpMetadataJson = _tmpMetadataJson2;
                        }
                        if (_stmt.isNull(_columnIndexOfCleanupProvider)) {
                            _tmpCleanupProvider = null;
                        } else {
                            String _tmpCleanupProvider2 = _stmt.getText(_columnIndexOfCleanupProvider);
                            _tmpCleanupProvider = _tmpCleanupProvider2;
                        }
                        long _tmpCreatedAtMs = _stmt.getLong(_columnIndexOfCreatedAtMs);
                        if (_stmt.isNull(_columnIndexOfCleanTextOnly)) {
                            _tmpCleanTextOnly = null;
                        } else {
                            String _tmpCleanTextOnly2 = _stmt.getText(_columnIndexOfCleanTextOnly);
                            _tmpCleanTextOnly = _tmpCleanTextOnly2;
                        }
                        if (_stmt.isNull(_columnIndexOfLastError)) {
                            _tmpLastError = null;
                        } else {
                            String _tmpLastError2 = _stmt.getText(_columnIndexOfLastError);
                            _tmpLastError = _tmpLastError2;
                        }
                        long _tmpUpdatedAtMs = _stmt.getLong(_columnIndexOfUpdatedAtMs);
                        MeetingEntity _item = new MeetingEntity(_tmpId, _tmpTitle, _tmpStartedAtMs, _tmpEndedAtMs, _tmpStatus, _tmpCleanedTranscript, _tmpMetadataJson, _tmpCleanupProvider, _tmpCreatedAtMs, _tmpCleanTextOnly, _tmpLastError, _tmpUpdatedAtMs);
                        int _columnIndexOfCleanTextOnly2 = _columnIndexOfCleanTextOnly;
                        List _result2 = _result;
                        int _columnIndexOfLastError2 = _columnIndexOfLastError;
                        _result2.add(_item);
                        _columnIndexOfLastError = _columnIndexOfLastError2;
                        _result = _result2;
                        _columnIndexOfCleanTextOnly = _columnIndexOfCleanTextOnly2;
                    }
                    List list = _result;
                    _stmt.close();
                    return list;
                } catch (Throwable th) {
                    th = th;
                    _stmt.close();
                    throw th;
                }
            } catch (Throwable th2) {
                th = th2;
                _stmt.close();
                throw th;
            }
        } catch (Throwable th3) {
            th = th3;
        }
    }

    @Override // com.varun.pocketassistant.data.MeetingDao
    public Object getAll(final int limit, Continuation<? super List<MeetingEntity>> continuation) {
        final String _sql = "SELECT * FROM meetings ORDER BY createdAtMs ASC LIMIT ?";
        return DBUtil.performSuspending(this.__db, true, false, new Function1() { // from class: com.varun.pocketassistant.data.MeetingDao_Impl$$ExternalSyntheticLambda6
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return MeetingDao_Impl.getAll$lambda$7(_sql, limit, (SQLiteConnection) obj);
            }
        }, continuation);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final List getAll$lambda$7(String $_sql, int $limit, SQLiteConnection _connection) {
        String _tmpTitle;
        String _tmpCleanedTranscript;
        String _tmpMetadataJson;
        String _tmpCleanupProvider;
        String _tmpCleanTextOnly;
        String _tmpLastError;
        Intrinsics.checkNotNullParameter(_connection, "_connection");
        SQLiteStatement _stmt = _connection.prepare($_sql);
        try {
            _stmt.mo7908bindLong(1, $limit);
            int _columnIndexOfId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "id");
            int _columnIndexOfTitle = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "title");
            int _columnIndexOfStartedAtMs = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "startedAtMs");
            int _columnIndexOfEndedAtMs = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "endedAtMs");
            int _columnIndexOfStatus = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, NotificationCompat.CATEGORY_STATUS);
            int _columnIndexOfCleanedTranscript = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "cleanedTranscript");
            int _columnIndexOfMetadataJson = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "metadataJson");
            int _columnIndexOfCleanupProvider = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "cleanupProvider");
            int _columnIndexOfCreatedAtMs = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "createdAtMs");
            int _columnIndexOfCleanTextOnly = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "cleanTextOnly");
            int _columnIndexOfLastError = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "lastError");
            int _columnIndexOfUpdatedAtMs = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "updatedAtMs");
            List _result = new ArrayList();
            while (_stmt.step()) {
                String _tmpId = _stmt.getText(_columnIndexOfId);
                if (_stmt.isNull(_columnIndexOfTitle)) {
                    _tmpTitle = null;
                } else {
                    String _tmpTitle2 = _stmt.getText(_columnIndexOfTitle);
                    _tmpTitle = _tmpTitle2;
                }
                long _tmpStartedAtMs = _stmt.getLong(_columnIndexOfStartedAtMs);
                long _tmpEndedAtMs = _stmt.getLong(_columnIndexOfEndedAtMs);
                String _tmpStatus = _stmt.getText(_columnIndexOfStatus);
                if (_stmt.isNull(_columnIndexOfCleanedTranscript)) {
                    _tmpCleanedTranscript = null;
                } else {
                    String _tmpCleanedTranscript2 = _stmt.getText(_columnIndexOfCleanedTranscript);
                    _tmpCleanedTranscript = _tmpCleanedTranscript2;
                }
                if (_stmt.isNull(_columnIndexOfMetadataJson)) {
                    _tmpMetadataJson = null;
                } else {
                    String _tmpMetadataJson2 = _stmt.getText(_columnIndexOfMetadataJson);
                    _tmpMetadataJson = _tmpMetadataJson2;
                }
                if (_stmt.isNull(_columnIndexOfCleanupProvider)) {
                    _tmpCleanupProvider = null;
                } else {
                    String _tmpCleanupProvider2 = _stmt.getText(_columnIndexOfCleanupProvider);
                    _tmpCleanupProvider = _tmpCleanupProvider2;
                }
                long _tmpCreatedAtMs = _stmt.getLong(_columnIndexOfCreatedAtMs);
                if (_stmt.isNull(_columnIndexOfCleanTextOnly)) {
                    _tmpCleanTextOnly = null;
                } else {
                    String _tmpCleanTextOnly2 = _stmt.getText(_columnIndexOfCleanTextOnly);
                    _tmpCleanTextOnly = _tmpCleanTextOnly2;
                }
                if (_stmt.isNull(_columnIndexOfLastError)) {
                    _tmpLastError = null;
                } else {
                    String _tmpLastError2 = _stmt.getText(_columnIndexOfLastError);
                    _tmpLastError = _tmpLastError2;
                }
                long _tmpUpdatedAtMs = _stmt.getLong(_columnIndexOfUpdatedAtMs);
                MeetingEntity _item = new MeetingEntity(_tmpId, _tmpTitle, _tmpStartedAtMs, _tmpEndedAtMs, _tmpStatus, _tmpCleanedTranscript, _tmpMetadataJson, _tmpCleanupProvider, _tmpCreatedAtMs, _tmpCleanTextOnly, _tmpLastError, _tmpUpdatedAtMs);
                int _columnIndexOfUpdatedAtMs2 = _columnIndexOfUpdatedAtMs;
                List _result2 = _result;
                _result2.add(_item);
                _result = _result2;
                _columnIndexOfUpdatedAtMs = _columnIndexOfUpdatedAtMs2;
            }
            return _result;
        } finally {
            _stmt.close();
        }
    }

    @Override // com.varun.pocketassistant.data.MeetingDao
    public Object delete(final String id, Continuation<? super Unit> continuation) {
        final String _sql = "DELETE FROM meetings WHERE id = ?";
        Object objPerformSuspending = DBUtil.performSuspending(this.__db, false, true, new Function1() { // from class: com.varun.pocketassistant.data.MeetingDao_Impl$$ExternalSyntheticLambda7
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return MeetingDao_Impl.delete$lambda$8(_sql, id, (SQLiteConnection) obj);
            }
        }, continuation);
        return objPerformSuspending == IntrinsicsKt.getCOROUTINE_SUSPENDED() ? objPerformSuspending : Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Unit delete$lambda$8(String $_sql, String $id, SQLiteConnection _connection) {
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

    /* JADX INFO: compiled from: MeetingDao_Impl.kt */
    @Metadata(d1 = {"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003J\u0010\u0010\u0004\u001a\f\u0012\b\u0012\u0006\u0012\u0002\b\u00030\u00060\u0005¨\u0006\u0007"}, d2 = {"Lcom/varun/pocketassistant/data/MeetingDao_Impl$Companion;", "", "<init>", "()V", "getRequiredConverters", "", "Lkotlin/reflect/KClass;", "app_debug"}, k = 1, mv = {2, 2, 0}, xi = 48)
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
