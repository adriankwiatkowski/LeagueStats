package com.example.android.leaguestats.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import com.example.android.leaguestats.R;
import com.example.android.leaguestats.utilities.InjectorUtils;
import com.example.android.leaguestats.viewmodels.ChampionModel;
import com.example.android.leaguestats.viewmodels.ChampionModelFactory;

public class ChampionActivity extends AppCompatActivity {

    public static final String CHAMPION_ID_KEY = "champion-id-key";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_champion);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(CHAMPION_ID_KEY)) {
            int id = intent.getIntExtra(CHAMPION_ID_KEY, -1);
            if (id == -1) {
                try {
                    throw new Exception("Couldnt retrieve championId");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            setupViewPager(id);
        }

        if (savedInstanceState == null) {
            ChampionFragment championFragment = new ChampionFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.champion_container, championFragment)
                    .commit();
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setupViewPager(int id) {
        ChampionModelFactory factory =
                InjectorUtils.provideChampionModelFactory(getApplicationContext());
        ChampionModel viewModel =
                ViewModelProviders.of(this, factory).get(ChampionModel.class);
        viewModel.initChampion(id);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
