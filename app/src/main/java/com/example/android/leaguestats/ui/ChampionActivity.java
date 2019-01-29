package com.example.android.leaguestats.ui;

import android.app.SearchManager;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.android.leaguestats.R;
import com.example.android.leaguestats.data.database.entity.ChampionEntry;
import com.example.android.leaguestats.utilities.InjectorUtils;
import com.example.android.leaguestats.viewmodels.ChampionModel;
import com.example.android.leaguestats.viewmodels.ChampionModelFactory;

public class ChampionActivity extends AppCompatActivity implements ChampionListFragment.OnChampionSelected {

    private static final String CHAMPION_LIST_FRAGMENT_TAG = "CHAMPION_LIST_FRAGMENT_TAG";
    private static final String CHAMPION_DETAIL_FRAGMENT_TAG = "CHAMPION_DETAIL_FRAGMENT_TAG";
    public static final String CHAMPION_ID_KEY = "champion-id-key";

    private NavigationController mNavigationController;

    private ChampionModel mViewModel;
    private Toolbar mToolbar;
    private boolean mTwoPane;
    private boolean mIsSummonerActivity = false;

    private SearchView mSearchView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_champion);

        mNavigationController = new NavigationController(this);

        mTwoPane = findViewById(R.id.champion_detail_container) != null;
        mSearchView = findViewById(R.id.search_view);
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setupViewModel();
        setupSearchView();

        if (savedInstanceState == null) {
            Intent intent = getIntent();
            if (intent != null && intent.hasExtra(CHAMPION_ID_KEY)) {
                int id = intent.getIntExtra(CHAMPION_ID_KEY, -1);
                if (id == -1) {
                    throw new IllegalArgumentException("Couldnt retrieve championId");
                } else {
                    mIsSummonerActivity = true;
                    initChampion(id);
                    if (!mTwoPane) {
                        mNavigationController.addFragment(R.id.champion_container, new ChampionDetailFragment(), CHAMPION_DETAIL_FRAGMENT_TAG);
                    } else {
                        mNavigationController.addFragment(R.id.champion_container, new ChampionListFragment(), CHAMPION_LIST_FRAGMENT_TAG);
                        mNavigationController.addFragment(R.id.champion_detail_container, new ChampionDetailFragment(), CHAMPION_DETAIL_FRAGMENT_TAG);
                    }
                }
            } else if (mTwoPane) {
                mNavigationController.addFragment(R.id.champion_container, new ChampionListFragment(), CHAMPION_LIST_FRAGMENT_TAG);
                mNavigationController.addFragment(R.id.champion_detail_container, new ChampionDetailFragment(), CHAMPION_DETAIL_FRAGMENT_TAG);
            } else {
                mNavigationController.addFragment(R.id.champion_container, new ChampionListFragment(), CHAMPION_LIST_FRAGMENT_TAG);
            }
        }
    }

    private void setupViewModel() {
        ChampionModelFactory factory =
                InjectorUtils.provideChampionModelFactory(getApplicationContext());
        mViewModel = ViewModelProviders.of(this, factory).get(ChampionModel.class);
        mViewModel.getChampion().observe(this, new Observer<ChampionEntry>() {
            @Override
            public void onChanged(@Nullable ChampionEntry championEntry) {
                updateUi(championEntry);
            }
        });
    }

    private void setupSearchView() {
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        mSearchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        mSearchView.onActionViewExpanded();
        mSearchView.clearFocus();
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mViewModel.initChampion(query);
                addDetailFragment();
                mSearchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                ChampionListFragment championListFragment =
                        (ChampionListFragment) getSupportFragmentManager().findFragmentByTag(CHAMPION_LIST_FRAGMENT_TAG);
                if (championListFragment != null && championListFragment.isVisible()) {
                    championListFragment.filterResults(query);
                }
                return false;
            }
        });
    }

    private void initChampion(int id) {
        mViewModel.initChampion(id);
    }

    private void updateUi(ChampionEntry championEntry) {
        if (championEntry != null) {
            mToolbar.setTitle(championEntry.getName());
            mToolbar.setSubtitle(championEntry.getTitle());
        }
    }

    @Override
    public void onChampionSelected(int id) {
        initChampion(id);
        addDetailFragment();
    }

    private void addDetailFragment() {
        if (mIsSummonerActivity && !mTwoPane) {
            mNavigationController.addDetailFragment(
                    R.id.champion_container, new ChampionDetailFragment(),
                    CHAMPION_DETAIL_FRAGMENT_TAG, mTwoPane, false);
        } else if (mTwoPane) {
            mNavigationController.addDetailFragment(
                    R.id.champion_detail_container, new ChampionDetailFragment(),
                    CHAMPION_DETAIL_FRAGMENT_TAG, mTwoPane);

        } else {
            mNavigationController.addDetailFragment(
                    R.id.champion_container, new ChampionDetailFragment(),
                    CHAMPION_DETAIL_FRAGMENT_TAG, mTwoPane);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
