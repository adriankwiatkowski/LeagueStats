package com.example.android.leaguestats.database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import com.example.android.leaguestats.database.Contract.ChampionEntry;
import com.example.android.leaguestats.database.Contract.IconEntry;
import com.example.android.leaguestats.database.Contract.ItemEntry;
import com.example.android.leaguestats.database.Contract.SummonerSpellEntry;

import static com.example.android.leaguestats.database.Contract.CONTENT_AUTHORITY;
import static com.example.android.leaguestats.database.Contract.PATH_CHAMPION;
import static com.example.android.leaguestats.database.Contract.PATH_ICON;
import static com.example.android.leaguestats.database.Contract.PATH_ITEM;
import static com.example.android.leaguestats.database.Contract.PATH_SUMMONER_SPELL;

public class Provider extends ContentProvider {

    public static final String LOG_TAG = Provider.class.getSimpleName();
    private static final int CHAMPIONS = 100;
    private static final int CHAMPIONS_ID = 101;
    private static final int ICONS = 200;
    private static final int ICON_ID = 201;
    private static final int ITEMS = 300;
    private static final int ITEM_ID = 301;
    private static final int SUMMONER_SPELLS = 400;
    private static final int SUMMONER_SPELL_ID = 401;
    private static final String UNKNOWN_URI = "Query failed, unknown URI: ";
    private static final String MATCH = ". Match: ";
    private static final String INSERTION_NOT_SUPPORTED = "Insertion is not supported for ";
    private static final String FAILED_TO_INSERT_ROW = "Failed to insert row for ";
    private static final String UPDATED_NOT_SUPPORTED = "Update is not supported for ";
    private static final String DB_SIGN = " = ?";

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(CONTENT_AUTHORITY, PATH_CHAMPION, CHAMPIONS);
        sUriMatcher.addURI(CONTENT_AUTHORITY, PATH_CHAMPION + "/#", CHAMPIONS_ID);

        sUriMatcher.addURI(CONTENT_AUTHORITY, PATH_ICON, ICONS);
        sUriMatcher.addURI(CONTENT_AUTHORITY, PATH_ICON + "#", ICON_ID);

        sUriMatcher.addURI(CONTENT_AUTHORITY, PATH_ITEM, ITEMS);
        sUriMatcher.addURI(CONTENT_AUTHORITY, PATH_ITEM + "#", ICON_ID);

        sUriMatcher.addURI(CONTENT_AUTHORITY, PATH_SUMMONER_SPELL, SUMMONER_SPELLS);
        sUriMatcher.addURI(CONTENT_AUTHORITY, PATH_SUMMONER_SPELL + "#", SUMMONER_SPELL_ID);
    }

    private Helper mHelper;

    @Override
    public boolean onCreate() {
        mHelper = new Helper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {

        SQLiteDatabase database = mHelper.getReadableDatabase();
        Cursor cursor = null;
        int match = sUriMatcher.match(uri);

        switch (match) {
            case CHAMPIONS:
                cursor = database.query(
                        ChampionEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case CHAMPIONS_ID:
                selection = ChampionEntry._ID + DB_SIGN;
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = database.query(
                        ChampionEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case ICONS:
                cursor = database.query(
                        IconEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case ICON_ID:
                selection = IconEntry._ID + DB_SIGN;
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = database.query(
                        IconEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case ITEMS:
                cursor = database.query(
                        ItemEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case ITEM_ID:
                selection = ItemEntry._ID + DB_SIGN;
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = database.query(
                        ItemEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case SUMMONER_SPELLS:
                cursor = database.query(
                        SummonerSpellEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case SUMMONER_SPELL_ID:
                selection = SummonerSpellEntry._ID + DB_SIGN;
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = database.query(
                        SummonerSpellEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            default:
                Log.e(LOG_TAG, UNKNOWN_URI + uri);
        }

        if (cursor != null) cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final int match = sUriMatcher.match(uri);
        SQLiteDatabase database = mHelper.getWritableDatabase();

        long id;

        switch (match) {
            case CHAMPIONS:
                id = database.insert(ChampionEntry.TABLE_NAME, null, values);

                if (id == -1) {
                    Log.e(LOG_TAG, FAILED_TO_INSERT_ROW + uri);
                    return null;
                }

                getContext().getContentResolver().notifyChange(uri, null);

                return ChampionEntry.buildChampionUri(id);
            case ICONS:
                id = database.insert(IconEntry.TABLE_NAME, null, values);

                if (id == -1) {
                    Log.e(LOG_TAG, FAILED_TO_INSERT_ROW + uri);
                    return null;
                }

                getContext().getContentResolver().notifyChange(uri, null);

                return IconEntry.buildIconUri(id);
            case ITEMS:
                id = database.insert(ItemEntry.TABLE_NAME, null, values);

                if (id == -1) {
                    Log.e(LOG_TAG, FAILED_TO_INSERT_ROW + uri);
                    return null;
                }

                getContext().getContentResolver().notifyChange(uri, null);

                return ItemEntry.buildItemUri(id);
            case SUMMONER_SPELLS:
                id = database.insert(SummonerSpellEntry.TABLE_NAME, null, values);

                if (id == -1) {
                    Log.e(LOG_TAG, FAILED_TO_INSERT_ROW + uri);
                    return null;
                }

                getContext().getContentResolver().notifyChange(uri, null);

                return SummonerSpellEntry.buildSummonerSpellUri(id);
            default:
                Log.e(LOG_TAG, INSERTION_NOT_SUPPORTED + uri);
                return null;
        }
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case CHAMPIONS:
                return updateItem(uri, values, selection, selectionArgs);
            case CHAMPIONS_ID:
                selection = ChampionEntry._ID + DB_SIGN;
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updateItem(uri, values, selection, selectionArgs);
            case ICONS:
                return updateItem(uri, values, selection, selectionArgs);
            case ICON_ID:
                selection = IconEntry._ID + DB_SIGN;
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updateItem(uri, values, selection, selectionArgs);
            case ITEMS:
                return updateItem(uri, values, selection, selectionArgs);
            case ITEM_ID:
                selection = ItemEntry._ID + DB_SIGN;
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updateItem(uri, values, selection, selectionArgs);
            case SUMMONER_SPELLS:
                return updateItem(uri, values, selection, selectionArgs);
            case SUMMONER_SPELL_ID:
                selection = SummonerSpellEntry._ID + DB_SIGN;
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updateItem(uri, values, selection, selectionArgs);
            default:
                Log.e(LOG_TAG, UPDATED_NOT_SUPPORTED + uri);
                return -1;
        }
    }

    private int updateItem(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        SQLiteDatabase database = mHelper.getWritableDatabase();

        int numOfRows;

        if (uri == ChampionEntry.CONTENT_URI) {
            numOfRows = database.update(ChampionEntry.TABLE_NAME, values, selection, selectionArgs);
        } else if (uri == IconEntry.CONTENT_URI){
            numOfRows = database.update(IconEntry.TABLE_NAME, values, selection, selectionArgs);
        } else if (uri == ItemEntry.CONTENT_URI){
            numOfRows = database.update(ItemEntry.TABLE_NAME, values, selection, selectionArgs);
        } else if (uri == SummonerSpellEntry.CONTENT_URI) {
            numOfRows = database.update(SummonerSpellEntry.TABLE_NAME, values, selection, selectionArgs);
        } else {
            Log.e(LOG_TAG, UNKNOWN_URI + uri);
            return 0;
        }

        if (numOfRows != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return numOfRows;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        final SQLiteDatabase database = mHelper.getWritableDatabase();

        int rowsDeleted;

        switch (match) {
            case CHAMPIONS:
                rowsDeleted = database.delete(ChampionEntry.TABLE_NAME, selection, selectionArgs);
                if (rowsDeleted != 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return rowsDeleted;
            case CHAMPIONS_ID:
                selection = ChampionEntry._ID + DB_SIGN;
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = database.delete(ChampionEntry.TABLE_NAME, selection, selectionArgs);
                if (rowsDeleted != 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return rowsDeleted;
            case ICONS:
                rowsDeleted = database.delete(IconEntry.TABLE_NAME, selection, selectionArgs);
                if (rowsDeleted != 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return rowsDeleted;
            case ICON_ID:
                selection = IconEntry._ID + DB_SIGN;
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = database.delete(IconEntry.TABLE_NAME, selection, selectionArgs);
                if (rowsDeleted != 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return rowsDeleted;
            case ITEMS:
                rowsDeleted = database.delete(ItemEntry.TABLE_NAME, selection, selectionArgs);
                if (rowsDeleted != 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return rowsDeleted;
            case ITEM_ID:
                selection = ItemEntry._ID + DB_SIGN;
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = database.delete(ItemEntry.TABLE_NAME, selection, selectionArgs);
                if (rowsDeleted != 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return rowsDeleted;
            case SUMMONER_SPELLS:
                rowsDeleted = database.delete(SummonerSpellEntry.TABLE_NAME, selection, selectionArgs);
                if (rowsDeleted != 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return rowsDeleted;
            case SUMMONER_SPELL_ID:
                selection = SummonerSpellEntry._ID + DB_SIGN;
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = database.delete(SummonerSpellEntry.TABLE_NAME, selection, selectionArgs);
                if (rowsDeleted != 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return rowsDeleted;
            default:
                Log.e(LOG_TAG, UNKNOWN_URI + uri);
                return -1;
        }
    }

    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case CHAMPIONS:
                return ChampionEntry.CONTENT_TYPE;
            case CHAMPIONS_ID:
                return ChampionEntry.CONTENT_ITEM_TYPE;
            case ICONS:
                return IconEntry.CONTENT_TYPE;
            case ICON_ID:
                return IconEntry.CONTENT_ITEM_TYPE;
            case ITEMS:
                return ItemEntry.CONTENT_TYPE;
            case ITEM_ID:
                return ItemEntry.CONTENT_ITEM_TYPE;
            case SUMMONER_SPELLS:
                return SummonerSpellEntry.CONTENT_TYPE;
            case SUMMONER_SPELL_ID:
                return SummonerSpellEntry.CONTENT_ITEM_TYPE;
            default:
                Log.e(LOG_TAG, UNKNOWN_URI + uri + MATCH + match);
                return null;
        }
    }
}
