package com.example.android.leaguestats;

import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

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
    public void onSummonerSpellSelected() {
        SummonerSpellDetailFragment summonerSpellDetailFragment = new SummonerSpellDetailFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.summoner_spell_container, summonerSpellDetailFragment);
        transaction.addToBackStack(null);
        transaction.commit();
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
                LocaleUtils.changeLanguage(SummonerSpellActivity.this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
