package com.example.android.leaguestats.ui;

import android.app.SearchManager;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.android.leaguestats.R;
import com.example.android.leaguestats.adapters.SummonerPagerAdapter;
import com.example.android.leaguestats.models.Summoner;
import com.example.android.leaguestats.utilities.DataUtils;
import com.example.android.leaguestats.utilities.InjectorUtils;
import com.example.android.leaguestats.viewmodels.HistoryModel;
import com.example.android.leaguestats.viewmodels.HistoryModelFactory;
import com.example.android.leaguestats.viewmodels.MasteryModel;
import com.example.android.leaguestats.viewmodels.MasteryModelFactory;
import com.example.android.leaguestats.viewmodels.SummonerModel;
import com.example.android.leaguestats.viewmodels.SummonerModelFactory;
import com.example.android.leaguestats.viewmodels.querymodels.HistoryQuery;
import com.example.android.leaguestats.viewmodels.querymodels.MasteryQuery;
import com.example.android.leaguestats.viewmodels.querymodels.SummonerQuery;

public class SummonerActivity extends AppCompatActivity
        implements SummonerMasteryFragment.MasteryListener,
        SummonerHistoryFragment.HistoryListener {

    private static final String LOG_TAG = SummonerActivity.class.getSimpleName();
    private SummonerModel mSummonerModel;
    private MasteryModel mMasteryModel;
    private HistoryModel mHistoryModel;
    private String mEntryUrlString;
    private FragmentManager mFragmentManager;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summoner);

        mFragmentManager = getSupportFragmentManager();

        setupViewPager();
        setupSummonerViewModel();
        setupMasteryViewModel();
        setupHistoryViewModel();

        initSummonerInfoFragment();
        handleIntent(getIntent());
    }

    private void initSummoner(String entryUrlString, String summonerName) {
        SummonerInfoFragment summonerInfoFragment =
                (SummonerInfoFragment) mFragmentManager.findFragmentByTag(SummonerInfoFragment.TAG);
        if (summonerInfoFragment != null && summonerInfoFragment.isVisible()) {
            summonerInfoFragment.showIndicator();
        }

        if (TextUtils.isEmpty(summonerName)) {
            Toast.makeText(this, R.string.enter_summoner_name, Toast.LENGTH_LONG).show();
        }

        mSummonerModel.searchSummoner(new SummonerQuery(entryUrlString, summonerName));
    }

    private void setupViewPager() {
        mViewPager = findViewById(R.id.summoner_viewpager);
        SummonerPagerAdapter summonerPagerAdapter =
                new SummonerPagerAdapter(this, mFragmentManager);
        mViewPager.setAdapter(summonerPagerAdapter);

        TabLayout tabLayout = findViewById(R.id.summoner_tab);
        tabLayout.setupWithViewPager(mViewPager);
    }

    private void setupSummonerViewModel() {

        if (mSummonerModel == null) {
            SummonerModelFactory factory =
                    InjectorUtils.provideSummonerModelFactory(this.getApplicationContext());
            mSummonerModel = ViewModelProviders.of(this, factory).get(SummonerModel.class);
            Log.d(LOG_TAG, "SummonerModel provided");
        }

        mSummonerModel.getSummoner().observe(this, new Observer<Summoner>() {
            @Override
            public void onChanged(@Nullable Summoner summoner) {
                Log.d(LOG_TAG, "Receiving new summoner");
                updateUi(summoner);
            }
        });
    }

    private void setupMasteryViewModel() {

        if (mMasteryModel == null) {
            MasteryModelFactory factory =
                    InjectorUtils.provideMasteryModelFactory(this.getApplicationContext());
            mMasteryModel = ViewModelProviders.of(this, factory).get(MasteryModel.class);
            Log.d(LOG_TAG, "MasteryModel provided");
        }
    }

    private void setupHistoryViewModel() {

        if (mHistoryModel == null) {
            HistoryModelFactory factory =
                    InjectorUtils.provideHistoryModelFactory(this.getApplicationContext());
            mHistoryModel = ViewModelProviders.of(this, factory).get(HistoryModel.class);
            Log.d(LOG_TAG, "HistoryModel provided");
        }
    }

    private void updateUi(Summoner summoner) {
        if (summoner != null) {
            Log.d(LOG_TAG, "Summoner not null");

            updateMasteryFragment(summoner);

            updateHistoryFragment(summoner);
        } else {
            Log.d(LOG_TAG, "Summoner null");
            Toast.makeText(SummonerActivity.this, getString(R.string.summoner_not_found)
                    + " " + getString(R.string.check_spelling_and_region), Toast.LENGTH_LONG).show();
        }
    }

    private void initSummonerInfoFragment() {
        SummonerInfoFragment summonerInfoFragment =
                (SummonerInfoFragment) mFragmentManager.findFragmentByTag(SummonerInfoFragment.TAG);

        if (summonerInfoFragment == null) {
            SummonerInfoFragment newSummonerInfoFragment = new SummonerInfoFragment();
            FragmentTransaction transaction = mFragmentManager.beginTransaction();
            transaction.add(R.id.summoner_container, newSummonerInfoFragment, SummonerInfoFragment.TAG);
            transaction.commit();
        }
    }

    private void updateMasteryFragment(Summoner summoner) {

        SummonerPagerAdapter summonerPagerAdapter = (SummonerPagerAdapter) mViewPager.getAdapter();

        SummonerMasteryFragment summonerMasteryFragment = (SummonerMasteryFragment)
                summonerPagerAdapter.getFragment(SummonerPagerAdapter.SUMMONER_MASTERY_POSITION);

        if (summonerMasteryFragment != null && summonerMasteryFragment.isVisible()) {
            Log.d(LOG_TAG, "summonerMasteryFragment not null");
            summonerMasteryFragment.showMasteryIndicator();
        } else {
            Log.d(LOG_TAG, "summonerMasteryFragment null");
        }

        mMasteryModel.searchMasteries(new MasteryQuery(summoner.getEntryUrl(), summoner.getSummonerId()));
    }

    private void updateHistoryFragment(Summoner summoner) {

        SummonerPagerAdapter summonerPagerAdapter = (SummonerPagerAdapter) mViewPager.getAdapter();

        SummonerHistoryFragment summonerHistoryFragment = (SummonerHistoryFragment)
                summonerPagerAdapter.getFragment(SummonerPagerAdapter.SUMMONER_HISTORY_POSITION);

        if (summonerHistoryFragment != null && summonerHistoryFragment.isVisible()) {
            Log.d(LOG_TAG, "summonerHistoryFragment not null");
            summonerHistoryFragment.showHistoryIndicator();
        } else {
            Log.d(LOG_TAG, "summonerHistoryFragment null");
        }

        mHistoryModel.searchHistoryMatches(new HistoryQuery(summoner.getEntryUrl(), summoner.getAccountId(), summoner.getSummonerId()));
    }

    @Override
    public void onMasteryChampionListener(long championId) {
        Intent intent = new Intent(SummonerActivity.this, ChampionDetailActivity.class);
        intent.putExtra(ChampionDetailActivity.CHAMPION_ID_KEY, championId);
        startActivity(intent);
    }

    @Override
    public void onHistorySummonerListener(String entryUrlString, String summonerName) {
        initSummoner(entryUrlString, summonerName);
    }

    @Override
    public void onHighestRank(String highestAchievedSeasonTier) {
        SummonerInfoFragment summonerInfoFragment =
                (SummonerInfoFragment) mFragmentManager.findFragmentByTag(SummonerInfoFragment.TAG);
        if (summonerInfoFragment != null) {
            summonerInfoFragment.showHighestRank(highestAchievedSeasonTier);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            Bundle bundle = intent.getExtras();
            String entryUrlString = (String) bundle.get("user_query");
            initSummoner(entryUrlString, query);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_summoner_menu, menu);

        setupMenuSpinner(menu);
        setupSearchView(menu);

        return true;
    }

    private void setupMenuSpinner(Menu menu) {
        Spinner spinner = (Spinner) menu.findItem(R.id.action_region).getActionView();

        ArrayAdapter regionSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.string_region_array, android.R.layout.simple_spinner_item);

        regionSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        spinner.setAdapter(regionSpinnerAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    mEntryUrlString = DataUtils.ENTRY_URL_SUMMONER_ARRAY[position];
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setupSearchView(Menu menu) {
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        final SearchView searchView =
                (SearchView) menu.findItem(R.id.search_summoner).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.setQuery(mEntryUrlString, false);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
    }
}