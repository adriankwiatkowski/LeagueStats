package com.example.android.leaguestats.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.leaguestats.database.Contract.ChampionEntry;

public class Helper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 20;
    public static final String DATABASE_NAME = "champions.db";

    public Helper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_TABLE = "CREATE TABLE " + ChampionEntry.TABLE_NAME + " (" +
                ChampionEntry._ID + " INTEGER PRIMARY KEY, " +
                ChampionEntry.COLUMN_CHAMPION_NAME + " TEXT NOT NULL, " +
                ChampionEntry.COLUMN_KEY + " TEXT NOT NULL, " +
                ChampionEntry.COLUMN_CHAMPION_TITLE + " TEXT NOT NULL, " +
                ChampionEntry.COLUMN_THUMBNAIL + " TEXT NOT NULL, " +
                ChampionEntry.COLUMN_SPLASH_ART + " TEXT NOT NULL, " +
                ChampionEntry.COLUMN_SPLASH_ART_NAME + " TEXT NOT NULL, " +
                ChampionEntry.COLUMN_DIFFICULTY + " INTEGER NOT NULL, " +
                ChampionEntry.COLUMN_ATTACK + " INTEGER NOT NULL, " +
                ChampionEntry.COLUMN_DEFENSE + " INTEGER NOT NULL, " +
                ChampionEntry.COLUMN_MAGIC + " INTEGER NOT NULL, " +
                ChampionEntry.COLUMN_ENEMY_TIPS + " TEXT NOT NULL, " +
                ChampionEntry.COLUMN_ALLY_TIPS + " TEXT NOT NULL, " +
                ChampionEntry.COLUMN_ARMOR_PER_LEVEL + " REAL NOT NULL, " +
                ChampionEntry.COLUMN_ATTACK_DAMAGE + " REAL NOT NULL, " +
                ChampionEntry.COLUMN_MANA_PER_LEVEL + " REAL NOT NULL, " +
                ChampionEntry.COLUMN_ATTACK_SPEED_OFFSET + " REAL NOT NULL, " +
                ChampionEntry.COLUMN_MANA + " REAL NOT NULL, " +
                ChampionEntry.COLUMN_ARMOR + " REAL NOT NULL, " +
                ChampionEntry.COLUMN_HEALTH + " REAL NOT NULL, " +
                ChampionEntry.COLUMN_HEALTH_REGEN_PER_LEVEL + " REAL NOT NULL, " +
                ChampionEntry.COLUMN_ATTACK_SPEED_PER_LEVEL + " REAL NOT NULL, " +
                ChampionEntry.COLUMN_ATTACK_RANGE + " REAL NOT NULL, " +
                ChampionEntry.COLUMN_MOVE_SPEED + " REAL NOT NULL, " +
                ChampionEntry.COLUMN_ATTACK_DAMAGE_PER_LEVEL + " REAL NOT NULL, " +
                ChampionEntry.COLUMN_MANA_REGEN_PER_LEVEL + " REAL NOT NULL, " +
                ChampionEntry.COLUMN_CRIT_PER_LEVEL + " REAL NOT NULL, " +
                ChampionEntry.COLUMN_MAGIC_RESIST_PER_LEVEL + " REAL NOT NULL, " +
                ChampionEntry.COLUMN_CRIT + " REAL NOT NULL, " +
                ChampionEntry.COLUMN_MANA_REGEN + " REAL NOT NULL, " +
                ChampionEntry.COLUMN_MAGIC_RESIST + " REAL NOT NULL, " +
                ChampionEntry.COLUMN_HEALTH_REGEN + " REAL NOT NULL, " +
                ChampionEntry.COLUMN_HEALTH_PER_LEVEL + " REAL NOT NULL, " +
                ChampionEntry.COLUMN_SPELL_NAME + " TEXT NOT NULL, " +
                ChampionEntry.COLUMN_SPELL_DESCRIPTION + " TEXT NOT NULL, " +
                ChampionEntry.COLUMN_SPELL_IMAGE + " TEXT NOT NULL, " +
                ChampionEntry.COLUMN_SPELL_RESOURCE + " TEXT NOT NULL, " +
                ChampionEntry.COLUMN_SPELL_COOLDOWN + " TEXT NOT NULL, " +
                ChampionEntry.COLUMN_SPELL_COST + " TEXT NOT NULL, " +
                ChampionEntry.COLUMN_CHAMPION_LORE + " TEXT NOT NULL);";
        db.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + ChampionEntry.TABLE_NAME);
        onCreate(db);
    }
}
