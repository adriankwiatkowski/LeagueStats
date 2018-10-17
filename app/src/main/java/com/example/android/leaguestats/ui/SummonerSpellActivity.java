package com.example.android.leaguestats.ui;

import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.android.leaguestats.R;
import com.example.android.leaguestats.utilities.LocaleUtils;

public class SummonerSpellActivity extends AppCompatActivity implements SummonerSpellListFragment.OnSummonerSpellSelected {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summoner_spell);

        if (savedInstanceState == null) {
            SummonerSpellListFragment summonerSpellListFragment = new SummonerSpellListFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.summoner_spell_container, summonerSpellListFragment).commit();
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onSummonerSpellSelected(long id) {
        SummonerSpellDetailFragment summonerSpellDetailFragment =
                SummonerSpellDetailFragment.newInstance((int) id);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.summoner_spell_container, summonerSpellDetailFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
