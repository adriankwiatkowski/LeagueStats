package com.example.android.leaguestats.ui;

import android.app.SearchManager;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;

import com.example.android.leaguestats.R;
import com.example.android.leaguestats.utilities.InjectorUtils;
import com.example.android.leaguestats.viewmodels.ChampionDetailModel;
import com.example.android.leaguestats.viewmodels.ChampionDetailModelFactory;
import com.example.android.leaguestats.adapters.ChampionPagerAdapter;
import com.example.android.leaguestats.data.database.entity.ChampionEntry;

public class ChampionDetailActivity extends AppCompatActivity {

    private final String LOG_TAG = ChampionDetailActivity.class.getSimpleName();
    public static final String CHAMPION_ID_KEY = "champion_id_key";
    private ChampionDetailModel mViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_champion_detail);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setupViewPager();
        setupViewModel();

        handleIntent(getIntent());
    }

    private void setupViewModel() {
        ChampionDetailModelFactory factory =
                InjectorUtils.provideChampionDetailModelFactory(this.getApplicationContext());
        mViewModel = ViewModelProviders.of(this, factory).get(ChampionDetailModel.class);
        mViewModel.getChampion().observe(this, new Observer<ChampionEntry>() {
            @Override
            public void onChanged(@Nullable ChampionEntry championEntry) {
                Log.d(LOG_TAG, "Receiving database update from LiveData");
                updateUi(championEntry);
            }
        });
    }

    private void updateUi(ChampionEntry championEntry) {
        if (championEntry != null) {
            String championName = championEntry.getName();
            String championTitle = championEntry.getTitle();

            getSupportActionBar().setTitle(championName);
            getSupportActionBar().setSubtitle(championTitle);
        }
    }

    private void setupViewPager() {
        ViewPager viewPager = findViewById(R.id.champion_viewpager);
        ChampionPagerAdapter championPagerAdapter =
                new ChampionPagerAdapter(this, getSupportFragmentManager());
        viewPager.setAdapter(championPagerAdapter);

        TabLayout tabLayout = findViewById(R.id.champion_tab);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            mViewModel.initChampion(query);
        } else if (intent.hasExtra(CHAMPION_ID_KEY)) {
            long championId = intent.getLongExtra(CHAMPION_ID_KEY, -1);
            if (championId != - 1) {
                mViewModel.initChampion(championId);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);

        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        return true;
    }
}
