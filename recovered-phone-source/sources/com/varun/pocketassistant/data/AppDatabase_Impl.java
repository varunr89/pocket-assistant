package com.varun.pocketassistant.data;

import androidx.core.app.NotificationCompat;
import androidx.room.InvalidationTracker;
import androidx.room.RoomMasterTable;
import androidx.room.RoomOpenDelegate;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.SQLite;
import androidx.sqlite.SQLiteConnection;
import com.varun.pocketassistant.pipeline.work.MeetingStageWorker;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import kotlin.Lazy;
import kotlin.LazyKt;
import kotlin.Metadata;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Reflection;
import kotlin.reflect.KClass;

/* JADX INFO: compiled from: AppDatabase_Impl.kt */
/* JADX INFO: loaded from: classes9.dex */
@Metadata(d1 = {"\u0000T\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010$\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0000\n\u0002\u0010\"\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\b\u0007\u0018\u00002\u00020\u0001B\u0007¢\u0006\u0004\b\u0002\u0010\u0003J\b\u0010\u000b\u001a\u00020\fH\u0014J\b\u0010\r\u001a\u00020\u000eH\u0014J\b\u0010\u000f\u001a\u00020\u0010H\u0016J\"\u0010\u0011\u001a\u001c\u0012\b\u0012\u0006\u0012\u0002\b\u00030\u0013\u0012\u000e\u0012\f\u0012\b\u0012\u0006\u0012\u0002\b\u00030\u00130\u00140\u0012H\u0014J\u0016\u0010\u0015\u001a\u0010\u0012\f\u0012\n\u0012\u0006\b\u0001\u0012\u00020\u00170\u00130\u0016H\u0016J*\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\u00190\u00142\u001a\u0010\u001a\u001a\u0016\u0012\f\u0012\n\u0012\u0006\b\u0001\u0012\u00020\u00170\u0013\u0012\u0004\u0012\u00020\u00170\u0012H\u0016J\b\u0010\u001b\u001a\u00020\u0006H\u0016J\b\u0010\u001c\u001a\u00020\bH\u0016J\b\u0010\u001d\u001a\u00020\nH\u0016R\u0014\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005X\u0082\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\b0\u0005X\u0082\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\t\u001a\b\u0012\u0004\u0012\u00020\n0\u0005X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u001e"}, d2 = {"Lcom/varun/pocketassistant/data/AppDatabase_Impl;", "Lcom/varun/pocketassistant/data/AppDatabase;", "<init>", "()V", "_sessionDao", "Lkotlin/Lazy;", "Lcom/varun/pocketassistant/data/SessionDao;", "_segmentDao", "Lcom/varun/pocketassistant/data/SegmentDao;", "_meetingDao", "Lcom/varun/pocketassistant/data/MeetingDao;", "createOpenDelegate", "Landroidx/room/RoomOpenDelegate;", "createInvalidationTracker", "Landroidx/room/InvalidationTracker;", "clearAllTables", "", "getRequiredTypeConverterClasses", "", "Lkotlin/reflect/KClass;", "", "getRequiredAutoMigrationSpecClasses", "", "Landroidx/room/migration/AutoMigrationSpec;", "createAutoMigrations", "Landroidx/room/migration/Migration;", "autoMigrationSpecs", "sessionDao", "segmentDao", "meetingDao", "app_debug"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class AppDatabase_Impl extends AppDatabase {
    public static final int $stable = 8;
    private final Lazy<SessionDao> _sessionDao = LazyKt.lazy(new Function0() { // from class: com.varun.pocketassistant.data.AppDatabase_Impl$$ExternalSyntheticLambda0
        @Override // kotlin.jvm.functions.Function0
        public final Object invoke() {
            return AppDatabase_Impl._sessionDao$lambda$0(this.f$0);
        }
    });
    private final Lazy<SegmentDao> _segmentDao = LazyKt.lazy(new Function0() { // from class: com.varun.pocketassistant.data.AppDatabase_Impl$$ExternalSyntheticLambda1
        @Override // kotlin.jvm.functions.Function0
        public final Object invoke() {
            return AppDatabase_Impl._segmentDao$lambda$1(this.f$0);
        }
    });
    private final Lazy<MeetingDao> _meetingDao = LazyKt.lazy(new Function0() { // from class: com.varun.pocketassistant.data.AppDatabase_Impl$$ExternalSyntheticLambda2
        @Override // kotlin.jvm.functions.Function0
        public final Object invoke() {
            return AppDatabase_Impl._meetingDao$lambda$2(this.f$0);
        }
    });

    /* JADX INFO: Access modifiers changed from: private */
    public static final SessionDao_Impl _sessionDao$lambda$0(AppDatabase_Impl this$0) {
        return new SessionDao_Impl(this$0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final SegmentDao_Impl _segmentDao$lambda$1(AppDatabase_Impl this$0) {
        return new SegmentDao_Impl(this$0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final MeetingDao_Impl _meetingDao$lambda$2(AppDatabase_Impl this$0) {
        return new MeetingDao_Impl(this$0);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.room.RoomDatabase
    public RoomOpenDelegate createOpenDelegate() {
        RoomOpenDelegate _openDelegate = new RoomOpenDelegate() { // from class: com.varun.pocketassistant.data.AppDatabase_Impl$createOpenDelegate$_openDelegate$1
            {
                super(8, "dff0070bfdfeef4ddecc68cf38bfb78d", "22a5bd38aaa3cf515f00cffcd5762690");
            }

            @Override // androidx.room.RoomOpenDelegate
            public void createAllTables(SQLiteConnection connection) throws Exception {
                Intrinsics.checkNotNullParameter(connection, "connection");
                SQLite.execSQL(connection, "CREATE TABLE IF NOT EXISTS `sessions` (`id` TEXT NOT NULL, `startedAtMs` INTEGER NOT NULL, `endedAtMs` INTEGER, `status` TEXT NOT NULL, `segmentCount` INTEGER NOT NULL, `speechDurationMs` INTEGER NOT NULL, `notes` TEXT, PRIMARY KEY(`id`))");
                SQLite.execSQL(connection, "CREATE TABLE IF NOT EXISTS `segments` (`id` TEXT NOT NULL, `sessionId` TEXT NOT NULL, `filePath` TEXT NOT NULL, `startedAtMs` INTEGER NOT NULL, `endedAtMs` INTEGER NOT NULL, `durationMs` INTEGER NOT NULL, `byteSize` INTEGER NOT NULL, `transcriptStatus` TEXT NOT NULL, `transcript` TEXT, `diarizedTranscript` TEXT, `cleanedTranscript` TEXT, `asrProvider` TEXT, `cleanupProvider` TEXT, `meetingId` TEXT, `asrLastError` TEXT, `skipReason` TEXT, `updatedAtMs` INTEGER NOT NULL, `speakerCandidatesJson` TEXT, `youConfirmed` INTEGER NOT NULL, `endReason` TEXT, PRIMARY KEY(`id`))");
                SQLite.execSQL(connection, "CREATE TABLE IF NOT EXISTS `meetings` (`id` TEXT NOT NULL, `title` TEXT, `startedAtMs` INTEGER NOT NULL, `endedAtMs` INTEGER NOT NULL, `status` TEXT NOT NULL, `cleanedTranscript` TEXT, `metadataJson` TEXT, `cleanupProvider` TEXT, `createdAtMs` INTEGER NOT NULL, `cleanTextOnly` TEXT, `lastError` TEXT, `updatedAtMs` INTEGER NOT NULL, PRIMARY KEY(`id`))");
                SQLite.execSQL(connection, RoomMasterTable.CREATE_QUERY);
                SQLite.execSQL(connection, "INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, 'dff0070bfdfeef4ddecc68cf38bfb78d')");
            }

            @Override // androidx.room.RoomOpenDelegate
            public void dropAllTables(SQLiteConnection connection) throws Exception {
                Intrinsics.checkNotNullParameter(connection, "connection");
                SQLite.execSQL(connection, "DROP TABLE IF EXISTS `sessions`");
                SQLite.execSQL(connection, "DROP TABLE IF EXISTS `segments`");
                SQLite.execSQL(connection, "DROP TABLE IF EXISTS `meetings`");
            }

            @Override // androidx.room.RoomOpenDelegate
            public void onCreate(SQLiteConnection connection) {
                Intrinsics.checkNotNullParameter(connection, "connection");
            }

            @Override // androidx.room.RoomOpenDelegate
            public void onOpen(SQLiteConnection connection) {
                Intrinsics.checkNotNullParameter(connection, "connection");
                this.this$0.internalInitInvalidationTracker(connection);
            }

            @Override // androidx.room.RoomOpenDelegate
            public void onPreMigrate(SQLiteConnection connection) {
                Intrinsics.checkNotNullParameter(connection, "connection");
                DBUtil.dropFtsSyncTriggers(connection);
            }

            @Override // androidx.room.RoomOpenDelegate
            public void onPostMigrate(SQLiteConnection connection) {
                Intrinsics.checkNotNullParameter(connection, "connection");
            }

            @Override // androidx.room.RoomOpenDelegate
            public RoomOpenDelegate.ValidationResult onValidateSchema(SQLiteConnection connection) {
                Intrinsics.checkNotNullParameter(connection, "connection");
                Map _columnsSessions = new LinkedHashMap();
                _columnsSessions.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, 1));
                _columnsSessions.put("startedAtMs", new TableInfo.Column("startedAtMs", "INTEGER", true, 0, null, 1));
                _columnsSessions.put("endedAtMs", new TableInfo.Column("endedAtMs", "INTEGER", false, 0, null, 1));
                _columnsSessions.put(NotificationCompat.CATEGORY_STATUS, new TableInfo.Column(NotificationCompat.CATEGORY_STATUS, "TEXT", true, 0, null, 1));
                _columnsSessions.put("segmentCount", new TableInfo.Column("segmentCount", "INTEGER", true, 0, null, 1));
                _columnsSessions.put("speechDurationMs", new TableInfo.Column("speechDurationMs", "INTEGER", true, 0, null, 1));
                _columnsSessions.put("notes", new TableInfo.Column("notes", "TEXT", false, 0, null, 1));
                Set _foreignKeysSessions = new LinkedHashSet();
                Set _indicesSessions = new LinkedHashSet();
                TableInfo _infoSessions = new TableInfo("sessions", _columnsSessions, _foreignKeysSessions, _indicesSessions);
                TableInfo _existingSessions = TableInfo.INSTANCE.read(connection, "sessions");
                if (!_infoSessions.equals(_existingSessions)) {
                    return new RoomOpenDelegate.ValidationResult(false, "sessions(com.varun.pocketassistant.data.SessionEntity).\n Expected:\n" + _infoSessions + "\n Found:\n" + _existingSessions);
                }
                Map _columnsSegments = new LinkedHashMap();
                _columnsSegments.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, 1));
                _columnsSegments.put("sessionId", new TableInfo.Column("sessionId", "TEXT", true, 0, null, 1));
                _columnsSegments.put("filePath", new TableInfo.Column("filePath", "TEXT", true, 0, null, 1));
                _columnsSegments.put("startedAtMs", new TableInfo.Column("startedAtMs", "INTEGER", true, 0, null, 1));
                _columnsSegments.put("endedAtMs", new TableInfo.Column("endedAtMs", "INTEGER", true, 0, null, 1));
                _columnsSegments.put("durationMs", new TableInfo.Column("durationMs", "INTEGER", true, 0, null, 1));
                _columnsSegments.put("byteSize", new TableInfo.Column("byteSize", "INTEGER", true, 0, null, 1));
                _columnsSegments.put("transcriptStatus", new TableInfo.Column("transcriptStatus", "TEXT", true, 0, null, 1));
                _columnsSegments.put("transcript", new TableInfo.Column("transcript", "TEXT", false, 0, null, 1));
                _columnsSegments.put("diarizedTranscript", new TableInfo.Column("diarizedTranscript", "TEXT", false, 0, null, 1));
                _columnsSegments.put("cleanedTranscript", new TableInfo.Column("cleanedTranscript", "TEXT", false, 0, null, 1));
                _columnsSegments.put("asrProvider", new TableInfo.Column("asrProvider", "TEXT", false, 0, null, 1));
                _columnsSegments.put("cleanupProvider", new TableInfo.Column("cleanupProvider", "TEXT", false, 0, null, 1));
                _columnsSegments.put(MeetingStageWorker.KEY_MEETING_ID, new TableInfo.Column(MeetingStageWorker.KEY_MEETING_ID, "TEXT", false, 0, null, 1));
                _columnsSegments.put("asrLastError", new TableInfo.Column("asrLastError", "TEXT", false, 0, null, 1));
                _columnsSegments.put("skipReason", new TableInfo.Column("skipReason", "TEXT", false, 0, null, 1));
                _columnsSegments.put("updatedAtMs", new TableInfo.Column("updatedAtMs", "INTEGER", true, 0, null, 1));
                _columnsSegments.put("speakerCandidatesJson", new TableInfo.Column("speakerCandidatesJson", "TEXT", false, 0, null, 1));
                _columnsSegments.put("youConfirmed", new TableInfo.Column("youConfirmed", "INTEGER", true, 0, null, 1));
                _columnsSegments.put("endReason", new TableInfo.Column("endReason", "TEXT", false, 0, null, 1));
                Set _foreignKeysSegments = new LinkedHashSet();
                Set _indicesSegments = new LinkedHashSet();
                TableInfo _infoSegments = new TableInfo("segments", _columnsSegments, _foreignKeysSegments, _indicesSegments);
                TableInfo _existingSegments = TableInfo.INSTANCE.read(connection, "segments");
                if (!_infoSegments.equals(_existingSegments)) {
                    return new RoomOpenDelegate.ValidationResult(false, "segments(com.varun.pocketassistant.data.SegmentEntity).\n Expected:\n" + _infoSegments + "\n Found:\n" + _existingSegments);
                }
                Map _columnsMeetings = new LinkedHashMap();
                _columnsMeetings.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, 1));
                _columnsMeetings.put("title", new TableInfo.Column("title", "TEXT", false, 0, null, 1));
                _columnsMeetings.put("startedAtMs", new TableInfo.Column("startedAtMs", "INTEGER", true, 0, null, 1));
                _columnsMeetings.put("endedAtMs", new TableInfo.Column("endedAtMs", "INTEGER", true, 0, null, 1));
                _columnsMeetings.put(NotificationCompat.CATEGORY_STATUS, new TableInfo.Column(NotificationCompat.CATEGORY_STATUS, "TEXT", true, 0, null, 1));
                _columnsMeetings.put("cleanedTranscript", new TableInfo.Column("cleanedTranscript", "TEXT", false, 0, null, 1));
                _columnsMeetings.put("metadataJson", new TableInfo.Column("metadataJson", "TEXT", false, 0, null, 1));
                _columnsMeetings.put("cleanupProvider", new TableInfo.Column("cleanupProvider", "TEXT", false, 0, null, 1));
                _columnsMeetings.put("createdAtMs", new TableInfo.Column("createdAtMs", "INTEGER", true, 0, null, 1));
                _columnsMeetings.put("cleanTextOnly", new TableInfo.Column("cleanTextOnly", "TEXT", false, 0, null, 1));
                _columnsMeetings.put("lastError", new TableInfo.Column("lastError", "TEXT", false, 0, null, 1));
                _columnsMeetings.put("updatedAtMs", new TableInfo.Column("updatedAtMs", "INTEGER", true, 0, null, 1));
                Set _foreignKeysMeetings = new LinkedHashSet();
                Set _indicesMeetings = new LinkedHashSet();
                TableInfo _infoMeetings = new TableInfo("meetings", _columnsMeetings, _foreignKeysMeetings, _indicesMeetings);
                TableInfo _existingMeetings = TableInfo.INSTANCE.read(connection, "meetings");
                if (!_infoMeetings.equals(_existingMeetings)) {
                    return new RoomOpenDelegate.ValidationResult(false, "meetings(com.varun.pocketassistant.data.MeetingEntity).\n Expected:\n" + _infoMeetings + "\n Found:\n" + _existingMeetings);
                }
                return new RoomOpenDelegate.ValidationResult(true, null);
            }
        };
        return _openDelegate;
    }

    @Override // androidx.room.RoomDatabase
    protected InvalidationTracker createInvalidationTracker() {
        Map _shadowTablesMap = new LinkedHashMap();
        Map _viewTables = new LinkedHashMap();
        return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "sessions", "segments", "meetings");
    }

    @Override // androidx.room.RoomDatabase
    public void clearAllTables() {
        super.performClear(false, "sessions", "segments", "meetings");
    }

    @Override // androidx.room.RoomDatabase
    protected Map<KClass<?>, List<KClass<?>>> getRequiredTypeConverterClasses() {
        Map _typeConvertersMap = new LinkedHashMap();
        _typeConvertersMap.put(Reflection.getOrCreateKotlinClass(SessionDao.class), SessionDao_Impl.INSTANCE.getRequiredConverters());
        _typeConvertersMap.put(Reflection.getOrCreateKotlinClass(SegmentDao.class), SegmentDao_Impl.INSTANCE.getRequiredConverters());
        _typeConvertersMap.put(Reflection.getOrCreateKotlinClass(MeetingDao.class), MeetingDao_Impl.INSTANCE.getRequiredConverters());
        return _typeConvertersMap;
    }

    @Override // androidx.room.RoomDatabase
    public Set<KClass<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecClasses() {
        Set _autoMigrationSpecsSet = new LinkedHashSet();
        return _autoMigrationSpecsSet;
    }

    @Override // androidx.room.RoomDatabase
    public List<Migration> createAutoMigrations(Map<KClass<? extends AutoMigrationSpec>, ? extends AutoMigrationSpec> autoMigrationSpecs) {
        Intrinsics.checkNotNullParameter(autoMigrationSpecs, "autoMigrationSpecs");
        List _autoMigrations = new ArrayList();
        return _autoMigrations;
    }

    @Override // com.varun.pocketassistant.data.AppDatabase
    public SessionDao sessionDao() {
        return this._sessionDao.getValue();
    }

    @Override // com.varun.pocketassistant.data.AppDatabase
    public SegmentDao segmentDao() {
        return this._segmentDao.getValue();
    }

    @Override // com.varun.pocketassistant.data.AppDatabase
    public MeetingDao meetingDao() {
        return this._meetingDao.getValue();
    }
}
