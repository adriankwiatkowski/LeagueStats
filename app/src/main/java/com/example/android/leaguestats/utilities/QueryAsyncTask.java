package com.example.android.leaguestats.utilities;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import com.example.android.leaguestats.database.Contract;
import com.example.android.leaguestats.database.Helper;
import com.example.android.leaguestats.interfaces.NameTaskCompleted;
import com.example.android.leaguestats.interfaces.QueryTaskCompleted;
import com.example.android.leaguestats.models.Mastery;

import java.util.ArrayList;
import java.util.List;

public class QueryAsyncTask extends AsyncTask<List<Mastery>, Void, List<Mastery>> {

    private QueryTaskCompleted mListener;
    private SQLiteDatabase mDb;

    public QueryAsyncTask(QueryTaskCompleted listener, SQLiteDatabase db) {
        mListener = listener;
        mDb = db;
    }

    private static final String LOG_TAG = QueryAsyncTask.class.getSimpleName();

    @Override
    protected List<Mastery> doInBackground(List<Mastery>... masteries) {

        List<Long> champions = new ArrayList<>();
        for (int i = 0; i < masteries[0].size(); i++) {
            long championId = masteries[0].get(i).getChampionId();
            champions.add(championId);
        }

        List<Mastery> masteryList = new ArrayList<>();

        for (int i = 0; i < champions.size(); i++) {
            Cursor cursor = mDb.query(
                    Contract.ChampionEntry.TABLE_NAME,
                    new String[]{Contract.ChampionEntry.COLUMN_CHAMPION_NAME,
                            Contract.ChampionEntry.COLUMN_THUMBNAIL},
                    Contract.ChampionEntry._ID + "=?",
                    new String[]{String.valueOf(champions.get(i))},
                    null,
                    null,
                    null);

            if (cursor != null) {
                cursor.moveToFirst();
            }

            masteryList.add(new Mastery(
                    cursor.getString(cursor.getColumnIndex(Contract.ChampionEntry.COLUMN_CHAMPION_NAME)),
                    cursor.getString(cursor.getColumnIndex(Contract.ChampionEntry.COLUMN_THUMBNAIL)),
                    masteries[0].get(i).getChampionId(), masteries[0].get(i).getChampionLevel(),
                    masteries[0].get(i).getChampionPoints(), masteries[0].get(i).getLastPlayTime(),
                    masteries[0].get(i).isChestGranted()));

        }

        return masteryList;
    }

    @Override
    protected void onPostExecute(List<Mastery> masteries) {
        super.onPostExecute(masteries);

        mListener.queryTaskCompleted(masteries);
    }
}
