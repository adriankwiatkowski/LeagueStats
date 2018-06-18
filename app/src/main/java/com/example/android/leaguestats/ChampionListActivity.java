package com.example.android.leaguestats;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.android.leaguestats.adapters.ChampionCursorAdapter;
import com.example.android.leaguestats.database.Contract;

public class ChampionListActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String LOG_TAG = ChampionListActivity.class.getSimpleName();
    private GridView mListView;
    private ChampionCursorAdapter mCursorAdapter;
    private static final int CHAMPIONS_LOADER = 0;
    private long championId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_champion_list);

        mListView = findViewById(R.id.champion_list_view);
        mCursorAdapter = new ChampionCursorAdapter(this, null);
        mListView.setAdapter(mCursorAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                championId = mCursorAdapter.getCursor().getLong(
                        mCursorAdapter.getCursor().getColumnIndex(Contract.ChampionEntry._ID));

                Intent intent = new Intent(ChampionListActivity.this, ChampionDetailActivity.class);

                Uri contentChampionUri = Contract.ChampionEntry.buildChampionUri(championId);

                intent.setData(contentChampionUri);

                startActivity(intent);
            }
        });

        getSupportLoaderManager().initLoader(CHAMPIONS_LOADER, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        final String[] PROJECTION = {
                Contract.ChampionEntry._ID,
                Contract.ChampionEntry.COLUMN_CHAMPION_NAME,
                Contract.ChampionEntry.COLUMN_CHAMPION_TITLE,
                Contract.ChampionEntry.COLUMN_THUMBNAIL};

        return new CursorLoader(this,
                Contract.ChampionEntry.CONTENT_URI,
                PROJECTION,
                null,
                null,
                Contract.ChampionEntry.COLUMN_CHAMPION_NAME);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);
    }
}
