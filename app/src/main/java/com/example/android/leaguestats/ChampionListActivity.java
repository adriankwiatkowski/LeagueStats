package com.example.android.leaguestats;

import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.android.leaguestats.adapters.ChampionAdapter;
import com.example.android.leaguestats.database.Contract;
import com.example.android.leaguestats.utilities.LocaleUtils;
import com.example.android.leaguestats.utilities.PreferencesUtils;

public class ChampionListActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor>, ChampionAdapter.ChampionClickListener {

    private static final String LOG_TAG = ChampionListActivity.class.getSimpleName();
    private RecyclerView mRecyclerView;
    private ChampionAdapter mChampionAdapter;
    private static final int CHAMPIONS_LOADER = 0;
    private String LAYOUT_MANAGER_STATE_KEY = "layoutManagerStateKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_champion_list);

        int gridLayoutColumnCount;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            gridLayoutColumnCount = 3;
        } else {
            gridLayoutColumnCount = 2;
        }

        mRecyclerView = findViewById(R.id.champion_recycler);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, gridLayoutColumnCount));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        String patchVersion = PreferencesUtils.getPatchVersion(this);
        mChampionAdapter = new ChampionAdapter(this, null, this, patchVersion);
        mRecyclerView.setAdapter(mChampionAdapter);

        if (savedInstanceState != null && savedInstanceState.containsKey(LAYOUT_MANAGER_STATE_KEY)) {
            mRecyclerView.getLayoutManager().onRestoreInstanceState(savedInstanceState.getParcelable(LAYOUT_MANAGER_STATE_KEY));
        }

        getSupportLoaderManager().initLoader(CHAMPIONS_LOADER, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        final String[] PROJECTION = {
                Contract.ChampionEntry._ID,
                Contract.ChampionEntry.COLUMN_CHAMPION_NAME,
                Contract.ChampionEntry.COLUMN_CHAMPION_TITLE,
                Contract.ChampionEntry.COLUMN_CHAMPION_THUMBNAIL};

        return new CursorLoader(this,
                Contract.ChampionEntry.CONTENT_URI,
                PROJECTION,
                null,
                null,
                Contract.ChampionEntry.COLUMN_CHAMPION_NAME);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mChampionAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mChampionAdapter.swapCursor(null);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable(LAYOUT_MANAGER_STATE_KEY, mRecyclerView.getLayoutManager().onSaveInstanceState());
    }

    @Override
    public void onChampionClick(int championId) {
        Intent intent = new Intent(ChampionListActivity.this, ChampionDetailActivity.class);
        Uri championUri = Contract.ChampionEntry.buildChampionUri(championId);
        intent.setData(championUri);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.change_language:
                LocaleUtils.changeLanguage(ChampionListActivity.this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
