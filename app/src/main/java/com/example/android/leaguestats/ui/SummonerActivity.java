package com.example.android.leaguestats.ui;

import android.app.SearchManager;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.leaguestats.R;
import com.example.android.leaguestats.adapters.SummonerPagerAdapter;
import com.example.android.leaguestats.data.glide.GlideApp;
import com.example.android.leaguestats.data.glide.GlideUtils;
import com.example.android.leaguestats.models.Resource;
import com.example.android.leaguestats.models.Summoner;
import com.example.android.leaguestats.utilities.DataUtils;
import com.example.android.leaguestats.utilities.InjectorUtils;
import com.example.android.leaguestats.utilities.LeaguePreferences;
import com.example.android.leaguestats.viewmodels.SummonerModel;
import com.example.android.leaguestats.viewmodels.SummonerModelFactory;

public class SummonerActivity extends AppCompatActivity
        implements SummonerMasteryFragment.MasteryListener,
        SummonerHistoryFragment.HistoryListener, SummonerSearchFragment.OnSummonerListener {

    private static final String LOG_TAG = SummonerActivity.class.getSimpleName();

    public static final String ACTION_SEARCH_SUMMONER = "action-search-summoner";
    public static final String SUMMONER_NAME_KEY = "summoner-name";
    public static final String ENTRY_URL_STRING_KEY = "entry-url-string";
    private static final String SUMMONER_SEARCH_TAG = "SUMMONER_SEARCH_TAG";

    private NavigationController mNavigationController;

    private String mPatchVersion;

    private SummonerModel mSummonerModel;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private ImageView mProfileIcon;
    private TextView mSummonerNameTv;
    private TextView mSummonerLevelTv;
    private TextView mSummonerRankTv;
    private TextView mEmptyTv;
    private ProgressBar mIndicator;
    private Toolbar mToolbar;
    private LinearLayout mSummonerInfoLayout;

    private SearchView mSearchView;
    private String mEntryUrlString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summoner);

        mNavigationController = new NavigationController(this);

        initViews();

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mPatchVersion = LeaguePreferences.getPatchVersion(this);

        setupViewPager();
        setupSummonerViewModel();

        setupMenuSpinner();
        setupSearchView();

        handleIntent(getIntent());

        if (savedInstanceState == null && getIntent().getAction() == null) {
            mTabLayout.setVisibility(View.INVISIBLE);
            mViewPager.setVisibility(View.INVISIBLE);
            mSummonerInfoLayout.setVisibility(View.GONE);
            mNavigationController.addFragment(R.id.summoner_container, new SummonerSearchFragment(), SUMMONER_SEARCH_TAG);
        }
    }

    private void initSummoner(String entryUrlString, String summonerName) {
        if (TextUtils.isEmpty(summonerName)) {
            Toast.makeText(this, R.string.enter_summoner_name, Toast.LENGTH_LONG).show();
            return;
        }
        removeSearchFragment();
        mSummonerModel.searchSummoner(entryUrlString, summonerName);
    }

    private void setupViewPager() {
        SummonerPagerAdapter summonerPagerAdapter = new SummonerPagerAdapter(this, getSupportFragmentManager());
        mViewPager.setAdapter(summonerPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    private void setupSummonerViewModel() {
        SummonerModelFactory factory =
                InjectorUtils.provideSummonerModelFactory(this.getApplicationContext());
        mSummonerModel = ViewModelProviders.of(this, factory).get(SummonerModel.class);
        mSummonerModel.getSummoner().observe(this, new Observer<Resource<Summoner>>() {
            @Override
            public void onChanged(@Nullable Resource<Summoner> summonerResource) {
                Log.d(LOG_TAG, "Getting summoner");
                updateUi(summonerResource);
            }
        });
    }

    private void updateUi(@Nullable Resource<Summoner> summonerResource) {
        if (summonerResource == null) {
            Log.d(LOG_TAG, "SummonerResponse null");
            return;
        }
        switch (summonerResource.status) {
            case LOADING:
                mIndicator.setVisibility(View.VISIBLE);
                mEmptyTv.setVisibility(View.INVISIBLE);
                mTabLayout.setVisibility(View.INVISIBLE);
                mViewPager.setVisibility(View.INVISIBLE);
                mProfileIcon.setImageResource(R.drawable.ic_launcher_background);
                mProfileIcon.setVisibility(View.INVISIBLE);
                mSummonerNameTv.setText("");
                mSummonerNameTv.setVisibility(View.INVISIBLE);
                mSummonerLevelTv.setText("");
                mSummonerLevelTv.setVisibility(View.INVISIBLE);
                mSummonerRankTv.setText("");
                mSummonerRankTv.setVisibility(View.INVISIBLE);
                break;
            case SUCCESS:
                mIndicator.setVisibility(View.INVISIBLE);
                mEmptyTv.setVisibility(View.INVISIBLE);
                mTabLayout.setVisibility(View.VISIBLE);
                mViewPager.setVisibility(View.VISIBLE);
                mProfileIcon.setVisibility(View.VISIBLE);
                mSummonerNameTv.setVisibility(View.VISIBLE);
                mSummonerLevelTv.setVisibility(View.VISIBLE);
                mSummonerRankTv.setVisibility(View.VISIBLE);
                Summoner summoner = summonerResource.data;
                mSummonerNameTv.setText(summoner.getSummonerName());
                mSummonerLevelTv.setText(getString(R.string.level, summoner.getSummonerLevel()));
                GlideApp.with(this)
                        .load(GlideUtils.getProfileIconUrl(mPatchVersion, summoner.getIconId()))
                        .roundedImage()
                        .into(mProfileIcon);
                mToolbar.setTitle(summoner.getSummonerName());
                break;
            case ERROR:
                mIndicator.setVisibility(View.INVISIBLE);
                mEmptyTv.setVisibility(View.VISIBLE);
                String error = summonerResource.message;
                mEmptyTv.setText(error);
                mTabLayout.setVisibility(View.INVISIBLE);
                mViewPager.setVisibility(View.INVISIBLE);
                mProfileIcon.setVisibility(View.INVISIBLE);
                mSummonerNameTv.setVisibility(View.INVISIBLE);
                mSummonerLevelTv.setVisibility(View.INVISIBLE);
                mSummonerRankTv.setVisibility(View.INVISIBLE);
                break;
        }
    }

    @Override
    public void onMasteryChampionListener(int championId) {
        Intent intent = new Intent(SummonerActivity.this, ChampionActivity.class);
        intent.putExtra(ChampionActivity.CHAMPION_ID_KEY, championId);
        startActivity(intent);
    }

    @Override
    public void onHistorySummonerListener(String entryUrlString, String summonerName) {
        initSummoner(entryUrlString, summonerName);
    }

    @Override
    public void onHighestRank(String highestAchievedSeasonTier) {
        mSummonerRankTv.setText(highestAchievedSeasonTier);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String summonerName = intent.getStringExtra(SearchManager.QUERY);
            Bundle appData = intent.getBundleExtra(SearchManager.APP_DATA);
            String entryUrlString = "";
            if (appData != null) {
                entryUrlString = appData.getString(SummonerActivity.ENTRY_URL_STRING_KEY);
            } else if (intent.hasExtra(SummonerActivity.ENTRY_URL_STRING_KEY)) {
                entryUrlString = intent.getStringExtra(SummonerActivity.ENTRY_URL_STRING_KEY);
            }
            initSummoner(entryUrlString, summonerName);
        } else if (ACTION_SEARCH_SUMMONER.equals(intent.getAction())) {
            String summonerName = intent.getStringExtra(SUMMONER_NAME_KEY);
            String entryUrlString = intent.getStringExtra(ENTRY_URL_STRING_KEY);
            initSummoner(entryUrlString, summonerName);
        }
    }

    @Override
    public void onSummonerSearch(String entryUrlString, String summonerName) {
        mEntryUrlString = entryUrlString;
        initSummoner(entryUrlString, summonerName);
    }

    private void removeSearchFragment() {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(SUMMONER_SEARCH_TAG);
        if (fragment != null) {
            mNavigationController.removeFragment(fragment);
        }
        mSummonerInfoLayout.setVisibility(View.VISIBLE);
        mTabLayout.setVisibility(View.VISIBLE);
        mViewPager.setVisibility(View.VISIBLE);
    }

    private void setupMenuSpinner() {
        AppCompatSpinner spinner = findViewById(R.id.region_spinner);
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

    private void setupSearchView() {
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        mSearchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        mSearchView.onActionViewExpanded();
        mSearchView.clearFocus();
    }

    @Override
    public void startActivity(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            intent.putExtra(SummonerActivity.ENTRY_URL_STRING_KEY, mEntryUrlString);
        }
        super.startActivity(intent);
    }

    private void initViews() {
        mTabLayout = findViewById(R.id.summoner_tablayout);
        mViewPager = findViewById(R.id.summoner_viewpager);
        mProfileIcon = findViewById(R.id.summoner_info_profile);
        mSummonerNameTv = findViewById(R.id.summoner_info_name_tv);
        mSummonerLevelTv = findViewById(R.id.summoner_info_level_tv);
        mSummonerRankTv = findViewById(R.id.summoner_info_rank_tv);
        mEmptyTv = findViewById(R.id.summoner_empty_view_tv);
        mIndicator = findViewById(R.id.indicator);
        mToolbar = findViewById(R.id.toolbar);
        mSummonerInfoLayout = findViewById(R.id.summoner_info_layout);
        mSearchView = findViewById(R.id.search_view);
    }
}