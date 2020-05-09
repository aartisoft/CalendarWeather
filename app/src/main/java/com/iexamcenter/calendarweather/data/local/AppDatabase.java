package com.iexamcenter.calendarweather.data.local;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.iexamcenter.calendarweather.data.local.dao.EphemerisDao;
import com.iexamcenter.calendarweather.data.local.dao.NewsDao;
import com.iexamcenter.calendarweather.data.local.dao.RssDao;
import com.iexamcenter.calendarweather.data.local.entity.EphemerisEntity;
import com.iexamcenter.calendarweather.data.local.entity.NewsEntity;
import com.iexamcenter.calendarweather.data.local.entity.RssEntity;


@Database(entities = {EphemerisEntity.class, NewsEntity.class, RssEntity.class}, version = 116, exportSchema = false)

public abstract class AppDatabase extends RoomDatabase {
    private static final String DATABASE_NAME = "com.iexamcenter.calendarweather.db";
    private static final Object LOCK = new Object();
    private static volatile AppDatabase sInstance;
    static final Migration MIGRATION = new Migration(103, 104) { // From version 1 to version 2
        @Override
        public void migrate(SupportSQLiteDatabase db) {
         // database.execSQL("CREATE TABLE IF NOT EXISTS my_table (id INTEGER, PRIMARY KEY(id), ...)")


            db.execSQL("DROP TABLE IF EXISTS COREDATA");
            db.execSQL("DROP TABLE IF EXISTS RSS");
            db.execSQL("DROP TABLE IF EXISTS NEWS");

        }

    };

    public static AppDatabase getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                if (sInstance == null) {
                    sInstance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, AppDatabase.DATABASE_NAME)
                            .fallbackToDestructiveMigration()
                            .build();

                }
            }
        }
        return sInstance;
    }

    public abstract EphemerisDao ephemerisDao();

    public abstract RssDao rssDao();

    public abstract NewsDao newsDao();


}
