package com.example.android.leaguestats.ui;

import android.app.SearchManager;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;

import com.example.android.leaguestats.R;
import com.example.android.leaguestats.utilities.InjectorUtils;
import com.example.android.leaguestats.viewmodels.SummonerSpellModel;
import com.example.android.leaguestats.viewmodels.SummonerSpellModelFactory;

public class SummonerSpellActivity extends AppCompatActivity implements SummonerSpellListFragment.OnSummonerSpellSelected {

    private static final String SUMMONER_SPELL_LIST_FRAGMENT_TAG = "SUMMONER_SPELL_LIST_FRAGMENT_TAG";
    private static final String SUMMONER_SPELL_DETAIL_FRAGMENT_TAG = "SUMMONER_SPELL_DETAIL_FRAGMENT_TAG";

    private NavigationController mNavigationController;

    private SummonerSpellModel mViewModel;
    private Toolbar mToolbar;
    private boolean mTwoPane;

    private SearchView mSearchView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summoner_spell);

        mNavigationController = new NavigationController(this);

        mTwoPane = findViewById(R.id.summoner_spell_detail_container) != null;
        mSearchView = findViewById(R.id.search_view);
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setupViewModel();
        setupSearchView();

        if (savedInstanceState == null) {
            if (mTwoPane) {
                mNavigationController.addFragment(R.id.summoner_spell_container, new SummonerSpellListFragment(), SUMMONER_SPELL_LIST_FRAGMENT_TAG);
                mNavigationController.addFragment(R.id.summoner_spell_detail_container, new SummonerSpellDetailFragment(), SUMMONER_SPELL_DETAIL_FRAGMENT_TAG);
            } else {
                mNavigationController.addFragment(R.id.summoner_spell_container, new SummonerSpellListFragment(), SUMMONER_SPELL_LIST_FRAGMENT_TAG);
            }
        }
    }

    private void setupViewModel() {
        SummonerSpellModelFactory factory = InjectorUtils.provideSummonerSpellModelFactory(getApplicationContext());
        mViewModel = ViewModelProviders.of(this, factory).get(SummonerSpellModel.class);
    }

    private void setupSearchView() {
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        mSearchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        mSearchView.onActionViewExpanded();
        mSearchView.clearFocus();
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mViewModel.initSummonerSpell(query);
                addDetailFragment();
                mSearchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                SummonerSpellListFragment summonerSpellListFragment =
                        (SummonerSpellListFragment) getSupportFragmentManager().findFragmentByTag(SUMMONER_SPELL_LIST_FRAGMENT_TAG);
                if (summonerSpellListFragment != null && summonerSpellListFragment.isVisible()) {
                    summonerSpellListFragment.filterResults(query);
                }
                return false;
            }
        });
    }

    @Override
    public void onSummonerSpellSelected(int id) {
        mViewModel.initSummonerSpell(id);
        addDetailFragment();
    }

    private void addDetailFragment() {
        if (mTwoPane) {
            mNavigationController.addDetailFragment(
                    R.id.summoner_spell_detail_container, new SummonerSpellDetailFragment(),
                    SUMMONER_SPELL_DETAIL_FRAGMENT_TAG, mTwoPane);
        } else {
            mNavigationController.addDetailFragment(
                    R.id.summoner_spell_container, new SummonerSpellDetailFragment(),
                    SUMMONER_SPELL_DETAIL_FRAGMENT_TAG, mTwoPane);
        }
    }
}
