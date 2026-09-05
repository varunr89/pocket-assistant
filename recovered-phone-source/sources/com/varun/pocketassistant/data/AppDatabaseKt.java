package com.varun.pocketassistant.data;

import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: AppDatabase.kt */
/* JADX INFO: loaded from: classes9.dex */
@Metadata(d1 = {"\u0000\u0016\n\u0000\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u001a\u0010\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\rH\u0002\"\u0011\u0010\u0000\u001a\u00020\u0001¢\u0006\b\n\u0000\u001a\u0004\b\u0002\u0010\u0003\"\u0011\u0010\u0004\u001a\u00020\u0001¢\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0003\"\u0011\u0010\u0006\u001a\u00020\u0001¢\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\u0003\"\u0011\u0010\b\u001a\u00020\u0001¢\u0006\b\n\u0000\u001a\u0004\b\t\u0010\u0003¨\u0006\u000e"}, d2 = {"MIGRATION_4_5", "Landroidx/room/migration/Migration;", "getMIGRATION_4_5", "()Landroidx/room/migration/Migration;", "MIGRATION_5_6", "getMIGRATION_5_6", "MIGRATION_6_7", "getMIGRATION_6_7", "MIGRATION_7_8", "getMIGRATION_7_8", "backfillDurationCapEndReason", "", "db", "Landroidx/sqlite/db/SupportSQLiteDatabase;", "app_debug"}, k = 2, mv = {2, 2, 0}, xi = 48)
public final class AppDatabaseKt {
    private static final Migration MIGRATION_4_5 = new Migration() { // from class: com.varun.pocketassistant.data.AppDatabaseKt$MIGRATION_4_5$1
        @Override // androidx.room.migration.Migration
        public void migrate(SupportSQLiteDatabase db) {
            Intrinsics.checkNotNullParameter(db, "db");
            db.execSQL("ALTER TABLE segments ADD COLUMN asrLastError TEXT");
            db.execSQL("ALTER TABLE segments ADD COLUMN skipReason TEXT");
            db.execSQL("ALTER TABLE segments ADD COLUMN updatedAtMs INTEGER NOT NULL DEFAULT 0");
            db.execSQL("ALTER TABLE meetings ADD COLUMN cleanTextOnly TEXT");
            db.execSQL("ALTER TABLE meetings ADD COLUMN lastError TEXT");
            db.execSQL("ALTER TABLE meetings ADD COLUMN updatedAtMs INTEGER NOT NULL DEFAULT 0");
            db.execSQL("UPDATE segments SET skipReason = 'superseded'\nWHERE transcriptStatus = 'FAILED'\n  AND transcript IS NOT NULL\n  AND lower(transcript) LIKE '%superseded%'");
            db.execSQL("UPDATE segments SET skipReason = 'legacy_too_long'\nWHERE transcriptStatus = 'SKIPPED_SILENCE'\n  AND transcript IS NOT NULL\n  AND lower(transcript) LIKE '%mega wav%'");
        }
    };
    private static final Migration MIGRATION_5_6 = new Migration() { // from class: com.varun.pocketassistant.data.AppDatabaseKt$MIGRATION_5_6$1
        @Override // androidx.room.migration.Migration
        public void migrate(SupportSQLiteDatabase db) {
            Intrinsics.checkNotNullParameter(db, "db");
            db.execSQL("ALTER TABLE segments ADD COLUMN speakerCandidatesJson TEXT");
            db.execSQL("ALTER TABLE segments ADD COLUMN youConfirmed INTEGER NOT NULL DEFAULT 0");
        }
    };
    private static final Migration MIGRATION_6_7 = new Migration() { // from class: com.varun.pocketassistant.data.AppDatabaseKt$MIGRATION_6_7$1
        @Override // androidx.room.migration.Migration
        public void migrate(SupportSQLiteDatabase db) {
            Intrinsics.checkNotNullParameter(db, "db");
            db.execSQL("ALTER TABLE segments ADD COLUMN endReason TEXT");
            AppDatabaseKt.backfillDurationCapEndReason(db);
        }
    };
    private static final Migration MIGRATION_7_8 = new Migration() { // from class: com.varun.pocketassistant.data.AppDatabaseKt$MIGRATION_7_8$1
        @Override // androidx.room.migration.Migration
        public void migrate(SupportSQLiteDatabase db) {
            Intrinsics.checkNotNullParameter(db, "db");
            AppDatabaseKt.backfillDurationCapEndReason(db);
        }
    };

    public static final Migration getMIGRATION_4_5() {
        return MIGRATION_4_5;
    }

    public static final Migration getMIGRATION_5_6() {
        return MIGRATION_5_6;
    }

    public static final Migration getMIGRATION_6_7() {
        return MIGRATION_6_7;
    }

    public static final Migration getMIGRATION_7_8() {
        return MIGRATION_7_8;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void backfillDurationCapEndReason(SupportSQLiteDatabase db) {
        db.execSQL("UPDATE segments SET endReason = 'duration_cap'\nWHERE endReason IS NULL\n  AND durationMs >= 105000\n  AND EXISTS (\n    SELECT 1 FROM segments s2\n    WHERE s2.sessionId = segments.sessionId\n      AND s2.id != segments.id\n      AND s2.startedAtMs >= segments.startedAtMs\n      AND (s2.startedAtMs - segments.endedAtMs) <= 5000\n      AND (s2.startedAtMs - segments.endedAtMs) >= -15000\n  )");
    }
}
