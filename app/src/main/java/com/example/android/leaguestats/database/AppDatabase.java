package com.example.android.leaguestats.room;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

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

    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE summoner_spell RENAME TO summoner_spell_old");

            database.execSQL("CREATE TABLE summoner_spell (id INTEGER, summoner_spell_key TEXT, " +
                    "name TEXT, description TEXT, image TEXT, cost INTEGER, cooldown INTEGER, " +
                    "range INTEGER, modes TEXT)");

            database.execSQL("INSERT INTO summoner_spell (id, summoner_spell_key, name, description, " +
                    "image, cost, cooldown, range, modes) SELECT mId, mKey, mName, mDescription, mImage, " +
                    "mCost, mCooldown, mRange, mModes FROM summoner_spell_old");

            database.execSQL("DROP TABLE summoner_spell_old");
        }
    };
}
