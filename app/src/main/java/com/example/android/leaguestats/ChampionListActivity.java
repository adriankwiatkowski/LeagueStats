package com.example.android.leaguestats;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.android.leaguestats.database.models.ListChampionEntry;
import com.example.android.leaguestats.interfaces.IdClickListener;
import com.example.android.leaguestats.interfaces.StringIdClickListener;
import com.example.android.leaguestats.utilities.InjectorUtils;
import com.example.android.leaguestats.viewModels.ChampionListModel;
import com.example.android.leaguestats.viewModels.ChampionListModelFactory;
import com.example.android.leaguestats.adapters.ChampionAdapter;
import com.example.android.leaguestats.utilities.LocaleUtils;
import com.example.android.leaguestats.utilities.PreferencesUtils;

import java.util.ArrayList;
import java.util.List;

public class ChampionListActivity extends AppCompatActivity implements StringIdClickListener {

    private static final String LOG_TAG = ChampionListActivity.class.getSimpleName();
    private RecyclerView mRecyclerView;
    private ChampionAdapter mChampionAdapter;
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
        mChampionAdapter = new ChampionAdapter(this, new ArrayList<ListChampionEntry>(), this, patchVersion);
        mRecyclerView.setAdapter(mChampionAdapter);

        if (savedInstanceState != null && savedInstanceState.containsKey(LAYOUT_MANAGER_STATE_KEY)) {
            mRecyclerView.getLayoutManager().onRestoreInstanceState(savedInstanceState.getParcelable(LAYOUT_MANAGER_STATE_KEY));
        }

        setupViewModel();
    }

    private void setupViewModel() {
        ChampionListModelFactory factory = InjectorUtils.provideChampionListModelFactory(this.getApplicationContext());
        final ChampionListModel viewModel =
                ViewModelProviders.of(this, factory).get(ChampionListModel.class);

        viewModel.getChampions().observe(this, new Observer<List<ListChampionEntry>>() {
            @Override
            public void onChanged(@Nullable List<ListChampionEntry> listChampionEntries) {
                Log.d(LOG_TAG, "Receiving database update from LiveData");
                mChampionAdapter.setData(listChampionEntries);
            }
        });
    }

    @Override
    public void onClickListener(String id) {
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

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable(LAYOUT_MANAGER_STATE_KEY, mRecyclerView.getLayoutManager().onSaveInstanceState());
    }
}
