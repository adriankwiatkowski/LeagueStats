package com.example.android.leaguestats.database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.android.leaguestats.database.dao.ChampionDao;
import com.example.android.leaguestats.database.dao.IconDao;
import com.example.android.leaguestats.database.dao.ItemDao;
import com.example.android.leaguestats.database.dao.SummonerSpellDao;
import com.example.android.leaguestats.database.entity.ChampionEntry;
import com.example.android.leaguestats.database.entity.IconEntry;
import com.example.android.leaguestats.database.entity.ItemEntry;
import com.example.android.leaguestats.database.entity.SummonerSpellEntry;

@Database(entities = {SummonerSpellEntry.class, ItemEntry.class, ChampionEntry.class, IconEntry.class}, version = 1, exportSchema = false)
@TypeConverters(DataConverter.class)
public abstract class AppDatabase extends RoomDatabase {

    private static final String LOG_TAG = AppDatabase.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "leaguestats";
    private static AppDatabase sInstance;

    public static AppDatabase getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                Log.d(LOG_TAG, "Creating new database instance");
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        AppDatabase.class, AppDatabase.DATABASE_NAME)
                        .build();
            }
        }
        Log.d(LOG_TAG, "Getting the database instance");
        return sInstance;
    }

    public abstract SummonerSpellDao summonerSpellDao();
    public abstract ItemDao itemDao();
    public abstract ChampionDao championDao();
    public abstract IconDao iconDao();
}
