package com.example.android.leaguestats;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
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
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.android.leaguestats.ViewModels.ChampionViewModelShared;
import com.example.android.leaguestats.ViewModels.ChampionViewModelSharedFactory;
import com.example.android.leaguestats.adapters.ChampionAdapter;
import com.example.android.leaguestats.interfaces.ChampionTaskCompleted;
import com.example.android.leaguestats.interfaces.ResultTask;
import com.example.android.leaguestats.room.AppDatabase;
import com.example.android.leaguestats.room.ChampionEntry;
import com.example.android.leaguestats.utilities.AsyncTasks.ChampionsAsyncTask;
import com.example.android.leaguestats.utilities.LocaleUtils;
import com.example.android.leaguestats.utilities.PreferencesUtils;

import java.util.ArrayList;
import java.util.List;

public class ChampionListActivity extends AppCompatActivity
        implements ChampionAdapter.ChampionClickListener {

    private static final String LOG_TAG = ChampionListActivity.class.getSimpleName();
    private RecyclerView mRecyclerView;
    private ChampionAdapter mChampionAdapter;
    private String LAYOUT_MANAGER_STATE_KEY = "layoutManagerStateKey";
    private ChampionViewModelShared mViewModel;
    private AppDatabase mDb;

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
        mChampionAdapter = new ChampionAdapter(this, new ArrayList<ChampionEntry>(), this, patchVersion);
        mRecyclerView.setAdapter(mChampionAdapter);

        if (savedInstanceState != null && savedInstanceState.containsKey(LAYOUT_MANAGER_STATE_KEY)) {
            mRecyclerView.getLayoutManager().onRestoreInstanceState(savedInstanceState.getParcelable(LAYOUT_MANAGER_STATE_KEY));
        }

        mDb = AppDatabase.getInstance(getApplicationContext());

        setupViewModel();
    }

    private void setupViewModel() {
        ChampionViewModelSharedFactory factory = new ChampionViewModelSharedFactory(mDb);
        mViewModel = ViewModelProviders.of(this, factory).get(ChampionViewModelShared.class);
        mViewModel.getChampions().observe(this, new Observer<List<ChampionEntry>>() {
            @Override
            public void onChanged(@Nullable List<ChampionEntry> championEntries) {
                mViewModel.getChampions().removeObserver(this);
                Log.d(LOG_TAG, "Receiving database update from LiveData");
                mChampionAdapter.setData(championEntries);
            }
        });
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable(LAYOUT_MANAGER_STATE_KEY, mRecyclerView.getLayoutManager().onSaveInstanceState());
    }

    @Override
    public void onChampionClick(long id) {
        Intent intent = new Intent(ChampionListActivity.this, ChampionDetailActivity.class);
        intent.putExtra(ChampionDetailActivity.CHAMPION_ID_KEY, id);
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
