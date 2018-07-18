package com.example.android.leaguestats.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.leaguestats.database.Contract.ChampionEntry;
import com.example.android.leaguestats.database.Contract.IconEntry;
import com.example.android.leaguestats.database.Contract.ItemEntry;
import com.example.android.leaguestats.database.Contract.SummonerSpellEntry;

public class Helper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 46;
    public static final String DATABASE_NAME = "champions.db";

    public static final String SQL_CREATE_CHAMPION_TABLE = "CREATE TABLE " + ChampionEntry.TABLE_NAME + " (" +
            ChampionEntry._ID + " INTEGER PRIMARY KEY, " +
            ChampionEntry.COLUMN_CHAMPION_NAME + " TEXT NOT NULL, " +
            ChampionEntry.COLUMN_KEY + " TEXT NOT NULL, " +
            ChampionEntry.COLUMN_CHAMPION_TITLE + " TEXT NOT NULL, " +
            ChampionEntry.COLUMN_CHAMPION_THUMBNAIL + " TEXT NOT NULL, " +
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

    public static final String SQL_CREATE_ICON_TABLE = "CREATE TABLE " + IconEntry.TABLE_NAME + " (" +
            IconEntry._ID + " INTEGER PRIMARY KEY, " +
            IconEntry.COLUMN_ICON + " TEXT NOT NULL, " +
            IconEntry.COLUMN_ICON_ID + " INTEGER NOT NULL);";

    public static final String SQL_CREATE_ITEM_TABLE = "CREATE TABLE " + ItemEntry.TABLE_NAME + " (" +
            ItemEntry._ID + " INTEGER PRIMARY KEY, " +
            ItemEntry.COLUMN_NAME + " TEXT NOT NULL, " +
            ItemEntry.COLUMN_SANITIZED_DESCRIPTION + " TEXT, " +
            ItemEntry.COLUMN_PLAIN_TEXT + " TEXT, " +
            ItemEntry.COLUMN_DEPTH + " INTEGER, " +
            ItemEntry.COLUMN_FROM + " TEXT, " +
            ItemEntry.COLUMN_INTO + " TEXT, " +
            ItemEntry.COLUMN_IMAGE + " TEXT NOT NULL, " +
            ItemEntry.COLUMN_BASE_GOLD + " INTEGER, " +
            ItemEntry.COLUMN_TOTAL_GOLD + " INTEGER, " +
            ItemEntry.COLUMN_SELL_GOLD + " INTEGER, " +
            ItemEntry.COLUMN_PURCHASABLE + " TEXT, " +
            ItemEntry.COLUMN_FLAT_ARMOR + " REAL, " +
            ItemEntry.COLUMN_FLAT_MAGIC_RESIST + " REAL, " +
            ItemEntry.COLUMN_FLAT_HP_POOL + " REAL, " +
            ItemEntry.COLUMN_FLAT_MP_POOL + " REAL, " +
            ItemEntry.COLUMN_FLAT_HP_REGEN + " REAL, " +
            ItemEntry.COLUMN_FLAT_CRIT_CHANCE + " REAL, " +
            ItemEntry.COLUMN_PERCENT_LIFE_STEAL + " REAL, " +
            ItemEntry.COLUMN_FLAT_PHYSICAL_DAMAGE + " REAL, " +
            ItemEntry.COLUMN_FLAT_MAGIC_DAMAGE + " REAL, " +
            ItemEntry.COLUMN_FLAT_MOVEMENT_SPEED + " REAL, " +
            ItemEntry.COLUMN_PERCENT_MOVEMENT_SPEED + " REAL, " +
            ItemEntry.COLUMN_PERCENT_ATTACK_SPEED + " REAL);";

    public static final String SQL_CREATE_SUMMONER_SPELL_TABLE = "CREATE TABLE " + SummonerSpellEntry.TABLE_NAME + " (" +
            SummonerSpellEntry._ID + " INTEGER PRIMARY KEY, " +
            SummonerSpellEntry.COLUMN_KEY + " INTEGER NOT NULL, " +
            SummonerSpellEntry.COLUMN_NAME + " TEXT NOT NULL, " +
            SummonerSpellEntry.COLUMN_DESCRIPTION + " TEXT NOT NULL, " +
            SummonerSpellEntry.COLUMN_IMAGE + " TEXT NOT NULL, " +
            SummonerSpellEntry.COLUMN_COST + " INTEGER NOT NULL, " +
            SummonerSpellEntry.COLUMN_COOLDOWN + " INTEGER NOT NULL, " +
            SummonerSpellEntry.COLUMN_RANGE + " INTEGER NOT NULL, " +
            SummonerSpellEntry.COLUMN_MODES + " TEXT NOT NULL);";

    public Helper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_CHAMPION_TABLE);
        db.execSQL(SQL_CREATE_ICON_TABLE);
        db.execSQL(SQL_CREATE_ITEM_TABLE);
        db.execSQL(SQL_CREATE_SUMMONER_SPELL_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + ChampionEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + IconEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + ItemEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + SummonerSpellEntry.TABLE_NAME);
        onCreate(db);
    }
}
