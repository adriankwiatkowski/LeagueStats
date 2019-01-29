package com.example.android.leaguestats.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.CardView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.leaguestats.R;
import com.example.android.leaguestats.utilities.DataUtils;
import com.example.android.leaguestats.utilities.InjectorUtils;
import com.example.android.leaguestats.viewmodels.MainModel;
import com.example.android.leaguestats.viewmodels.MainModelFactory;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private MainModel mViewModel;
    private AppBarLayout mAppBar;
    private Toolbar mToolbar;
    private CardView mSummonerCard, mChampionCard, mItemCard, mSummonerSpellCard, mSettingsCard;

    private ProgressBar mIndicator;
    private TextView mIndicatorTv;
    private ImageView mIndicatorImage;

    private long mLastClickTime = 0;

    private SearchView mSearchView;
    private String mEntryUrlString;

    private ViewGroup mContentLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();

        setSupportActionBar(mToolbar);

        setupViewModel();
        setupSearchView();
        setupSpinner();
    }

    private void setupViewModel() {
        MainModelFactory factory =
                InjectorUtils.provideMainModelFactory(getApplicationContext());
        mViewModel = ViewModelProviders.of(this, factory).get(MainModel.class);
        mViewModel.isNotEmpty().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean isNotEmpty) {
                updateUi(isNotEmpty);
            }
        });
    }

    private void updateUi(Boolean isNotEmpty) {
        if (isNotEmpty != null && isNotEmpty) {
            showViews();
        } else {
            hideViews();
        }
    }

    private void showViews() {
        mIndicator.setVisibility(View.INVISIBLE);
        mIndicatorTv.setVisibility(View.INVISIBLE);
        mIndicatorImage.setVisibility(View.INVISIBLE);
        mToolbar.setVisibility(View.VISIBLE);
        mContentLayout.setVisibility(View.VISIBLE);
    }

    private void hideViews() {
        mIndicator.setVisibility(View.VISIBLE);
        mIndicatorTv.setVisibility(View.VISIBLE);
        mIndicatorImage.setVisibility(View.VISIBLE);
        mToolbar.setVisibility(View.INVISIBLE);
        mContentLayout.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onClick(View v) {
        if (SystemClock.elapsedRealtime() - mLastClickTime < 2000) {
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();
        switch (v.getId()) {
            case R.id.summoner_card:
                startActivity(new Intent(this, SummonerActivity.class));
                break;
            case R.id.champion_card:
                startActivity(new Intent(this, ChampionActivity.class));
                break;
            case R.id.item_card:
                startActivity(new Intent(this, ItemActivity.class));
                break;
            case R.id.summoner_spell_card:
                startActivity(new Intent(this, SummonerSpellActivity.class));
                break;
            case R.id.settings_card:
                startActivity(new Intent(this, SettingsActivity.class));
                break;
        }
    }

    private void setupSearchView() {
        mSearchView.onActionViewExpanded();
        mSearchView.clearFocus();
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!TextUtils.isEmpty(query)) {
                    mSearchView.clearFocus();
                    Intent intent = new Intent(MainActivity.this, SummonerActivity.class)
                            .setAction(SummonerActivity.ACTION_SEARCH_SUMMONER)
                            .putExtra(SummonerActivity.ENTRY_URL_STRING_KEY, mEntryUrlString)
                            .putExtra(SummonerActivity.SUMMONER_NAME_KEY, query);
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, getString(R.string.enter_summoner_name), Toast.LENGTH_LONG).show();
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                return false;
            }
        });
    }

    private void setupSpinner() {
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initViews() {
        mSummonerCard = findViewById(R.id.summoner_card);
        mSummonerCard.setOnClickListener(this);
        mChampionCard = findViewById(R.id.champion_card);
        mChampionCard.setOnClickListener(this);
        mItemCard = findViewById(R.id.item_card);
        mItemCard.setOnClickListener(this);
        mSummonerSpellCard = findViewById(R.id.summoner_spell_card);
        mSummonerSpellCard.setOnClickListener(this);
        mSettingsCard = findViewById(R.id.settings_card);
        mSettingsCard.setOnClickListener(this);
        mIndicator = findViewById(R.id.main_indicator);
        mIndicatorTv = findViewById(R.id.main_indicator_tv);
        mIndicatorImage = findViewById(R.id.main_indicator_image);
        mAppBar = findViewById(R.id.appbar);
        mToolbar = findViewById(R.id.toolbar);
        mSearchView = findViewById(R.id.search_view);
        mContentLayout = findViewById(R.id.content_layout);
    }
}
