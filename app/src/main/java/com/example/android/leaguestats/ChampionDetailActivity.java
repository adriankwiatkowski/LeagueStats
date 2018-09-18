package com.example.android.leaguestats;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.android.leaguestats.utilities.InjectorUtils;
import com.example.android.leaguestats.viewModels.ChampionDetailModel;
import com.example.android.leaguestats.viewModels.ChampionDetailModelFactory;
import com.example.android.leaguestats.adapters.ChampionPagerAdapter;
import com.example.android.leaguestats.database.AppDatabase;
import com.example.android.leaguestats.database.entity.ChampionEntry;
import com.example.android.leaguestats.utilities.LocaleUtils;

public class ChampionDetailActivity extends AppCompatActivity {

    private final String LOG_TAG = ChampionDetailActivity.class.getSimpleName();
    public static final String CHAMPION_ID_KEY = "champion_id_key";
    private String mChampionId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_champion_detail);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        if (intent.hasExtra(CHAMPION_ID_KEY)) {
            mChampionId = intent.getStringExtra(CHAMPION_ID_KEY);
            Log.d(LOG_TAG, "ChampionId value = " + mChampionId);
        } else {
            Log.w(LOG_TAG, "championId intent null");
            return;
        }
        setupViewPager();
        setupViewModel();
    }

    private void setupViewModel() {
        ChampionDetailModelFactory factory =
                InjectorUtils.provideChampionDetailModelFactory(this.getApplicationContext());
        final ChampionDetailModel viewModel =
                ViewModelProviders.of(this, factory).get(ChampionDetailModel.class);
        viewModel.initChampion(mChampionId);
        viewModel.getChampion().observe(this, new Observer<ChampionEntry>() {
            @Override
            public void onChanged(@Nullable ChampionEntry championEntry) {
                viewModel.getChampion().removeObserver(this);
                Log.d(LOG_TAG, "Receiving database update from LiveData");
                updateUi(championEntry);
            }
        });
    }

    private void updateUi(@NonNull ChampionEntry championEntry) {
        String championName = championEntry.getName();
        String championTitle = championEntry.getTitle();

        getSupportActionBar().setTitle(championName);
        getSupportActionBar().setSubtitle(championTitle);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            case R.id.change_language:
                LocaleUtils.changeLanguage(ChampionDetailActivity.this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
