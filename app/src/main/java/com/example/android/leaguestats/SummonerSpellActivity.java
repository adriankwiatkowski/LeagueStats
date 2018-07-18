package com.example.android.leaguestats;

import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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

import com.example.android.leaguestats.adapters.SummonerSpellAdapter;
import com.example.android.leaguestats.database.Contract;
import com.example.android.leaguestats.utilities.LocaleUtils;
import com.example.android.leaguestats.utilities.PreferencesUtils;

public class SummonerSpellActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String LOG_TAG = SummonerSpellActivity.class.getSimpleName();
    private RecyclerView mRecyclerView;
    private SummonerSpellAdapter mAdapter;
    private String LAYOUT_MANAGER_STATE_KEY = "layoutManagerStateKey";
    private Parcelable mLayoutManagerViewState;
    private static final int SUMMONER_SPELL_LOADER = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summoner_spell);

        mRecyclerView = findViewById(R.id.summoner_spell_recycler);

        int gridLayoutColumnCount;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            gridLayoutColumnCount = 3;
        } else {
            gridLayoutColumnCount = 2;
        }
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, gridLayoutColumnCount));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        String patchVersion = PreferencesUtils.getPatchVersion(this);
        mAdapter = new SummonerSpellAdapter(this, null, patchVersion);
        mRecyclerView.setAdapter(mAdapter);

        if (savedInstanceState != null && savedInstanceState.containsKey(LAYOUT_MANAGER_STATE_KEY)) {
            mLayoutManagerViewState = savedInstanceState.getParcelable(LAYOUT_MANAGER_STATE_KEY);
        }

        getSupportLoaderManager().initLoader(SUMMONER_SPELL_LOADER, null, this);
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        final String[] PROJECTION = {
                Contract.SummonerSpellEntry._ID,
                Contract.SummonerSpellEntry.COLUMN_NAME,
                Contract.SummonerSpellEntry.COLUMN_IMAGE,
                Contract.SummonerSpellEntry.COLUMN_COST,
                Contract.SummonerSpellEntry.COLUMN_COOLDOWN};

        return new CursorLoader(this,
                Contract.SummonerSpellEntry.CONTENT_URI,
                PROJECTION,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        mLayoutManagerViewState =  mRecyclerView.getLayoutManager().onSaveInstanceState();
        outState.putParcelable(LAYOUT_MANAGER_STATE_KEY, mLayoutManagerViewState);
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
                LocaleUtils.changeLanguage(SummonerSpellActivity.this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
