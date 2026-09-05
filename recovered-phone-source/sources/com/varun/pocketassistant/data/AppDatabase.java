package com.varun.pocketassistant.data;

import androidx.room.RoomDatabase;
import kotlin.Metadata;

/* JADX INFO: compiled from: AppDatabase.kt */
/* JADX INFO: loaded from: classes9.dex */
@Metadata(d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b'\u0018\u00002\u00020\u0001B\u0007¢\u0006\u0004\b\u0002\u0010\u0003J\b\u0010\u0004\u001a\u00020\u0005H&J\b\u0010\u0006\u001a\u00020\u0007H&J\b\u0010\b\u001a\u00020\tH&¨\u0006\n"}, d2 = {"Lcom/varun/pocketassistant/data/AppDatabase;", "Landroidx/room/RoomDatabase;", "<init>", "()V", "sessionDao", "Lcom/varun/pocketassistant/data/SessionDao;", "segmentDao", "Lcom/varun/pocketassistant/data/SegmentDao;", "meetingDao", "Lcom/varun/pocketassistant/data/MeetingDao;", "app_debug"}, k = 1, mv = {2, 2, 0}, xi = 48)
public abstract class AppDatabase extends RoomDatabase {
    public static final int $stable = 8;

    public abstract MeetingDao meetingDao();

    public abstract SegmentDao segmentDao();

    public abstract SessionDao sessionDao();
}
