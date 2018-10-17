package com.example.android.leaguestats.data.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.util.Log;

import com.example.android.leaguestats.data.database.dao.ChampionDao;
import com.example.android.leaguestats.data.database.dao.IconDao;
import com.example.android.leaguestats.data.database.dao.ItemDao;
import com.example.android.leaguestats.data.database.dao.SummonerSpellDao;
import com.example.android.leaguestats.data.database.entity.ChampionEntry;
import com.example.android.leaguestats.data.database.entity.IconEntry;
import com.example.android.leaguestats.data.database.entity.ItemEntry;
import com.example.android.leaguestats.data.database.entity.SummonerSpellEntry;

@Database(entities = {SummonerSpellEntry.class, ItemEntry.class, ChampionEntry.class, IconEntry.class}, version = 1, exportSchema = false)
@TypeConverters(DataConverter.class)
public abstract class LeagueDatabase extends RoomDatabase {

    private static final String LOG_TAG = LeagueDatabase.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "leaguestats";
    private static LeagueDatabase sInstance;

    public static LeagueDatabase getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                Log.d(LOG_TAG, "Creating new database instance");
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        LeagueDatabase.class, LeagueDatabase.DATABASE_NAME)
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
