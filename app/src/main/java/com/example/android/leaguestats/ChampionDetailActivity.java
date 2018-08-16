package com.example.android.leaguestats;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.NavUtils;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.android.leaguestats.viewModels.ChampionViewModelShared;
import com.example.android.leaguestats.viewModels.ChampionViewModelSharedFactory;
import com.example.android.leaguestats.adapters.ChampionPagerAdapter;
import com.example.android.leaguestats.database.AppDatabase;
import com.example.android.leaguestats.database.ChampionEntry;
import com.example.android.leaguestats.utilities.LocaleUtils;

public class ChampionDetailActivity extends AppCompatActivity {

    private final String LOG_TAG = ChampionDetailActivity.class.getSimpleName();
    public static final String CHAMPION_ID_KEY = "champion_id_key";
    private long mChampionId;
    private static final long DEFAULT_CHAMPION_ID = -1;
    private AppDatabase mDb;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_champion_detail);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        if (intent.hasExtra(CHAMPION_ID_KEY)) {
            mChampionId = intent.getLongExtra(CHAMPION_ID_KEY, DEFAULT_CHAMPION_ID);
        }

        mDb = AppDatabase.getInstance(getApplicationContext());
        setupViewModel();
    }

    private void setupViewModel() {
        ChampionViewModelSharedFactory factory = new ChampionViewModelSharedFactory(mDb);
        final ChampionViewModelShared viewModel =
                ViewModelProviders.of(this, factory).get(ChampionViewModelShared.class);
        viewModel.getChampionById(mChampionId).observe(this, new Observer<ChampionEntry>() {
            @Override
            public void onChanged(@Nullable ChampionEntry championEntry) {
                viewModel.getChampionById(mChampionId).removeObserver(this);
                viewModel.select(championEntry);
                Log.d(LOG_TAG, "Receiving database update from LiveData");
                updateUi(championEntry);
            }
        });
    }

    private void updateUi(ChampionEntry championEntry) {

        ViewPager viewPager = findViewById(R.id.champion_viewpager);
        ChampionPagerAdapter championPagerAdapter =
                new ChampionPagerAdapter(this, getSupportFragmentManager());
        viewPager.setAdapter(championPagerAdapter);

        TabLayout tabLayout = findViewById(R.id.champion_tab);
        tabLayout.setupWithViewPager(viewPager);

        String championName = championEntry.getName();
        String championTitle = championEntry.getTitle();
        getSupportActionBar().setTitle(championName);
        getSupportActionBar().setSubtitle(championTitle);
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
