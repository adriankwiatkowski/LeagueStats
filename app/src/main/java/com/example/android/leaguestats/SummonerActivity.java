package com.example.android.leaguestats;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.android.leaguestats.utilities.LocaleUtils;

public class SummonerActivity extends AppCompatActivity
        implements SummonerSearchFragment.OnSubmitListener, SummonerMasteryFragment.OnChampionClickListener {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summoner);

        if (savedInstanceState == null) {
            SummonerSearchFragment summonerSearchFragment = new SummonerSearchFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.summoner_container, summonerSearchFragment).commit();
        }
    }

    @Override
    public void onMasteryListener(String summonerName, String entryRegionString) {
        SummonerMasteryFragment summonerMasteryFragment =
                SummonerMasteryFragment.newInstance(summonerName, entryRegionString);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.summoner_container, summonerMasteryFragment);
        transaction.addToBackStack(null);

        transaction.commit();
    }

    @Override
    public void onHistoryListener(String summonerName, String entryString) {
        SummonerHistoryFragment summonerHistoryFragment =
                SummonerHistoryFragment.newInstance(entryString, summonerName);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.summoner_container, summonerHistoryFragment);
        transaction.addToBackStack(null);

        transaction.commit();
    }

    @Override
    public void onChampionClick(long championId) {
        Intent intent = new Intent(SummonerActivity.this, ChampionDetailActivity.class);
        intent.putExtra(ChampionDetailActivity.CHAMPION_ID_KEY, championId);
        startActivity(intent);
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
            case R.id.change_language:
                LocaleUtils.changeLanguage(SummonerActivity.this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}