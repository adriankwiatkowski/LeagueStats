package com.example.android.leaguestats;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.android.leaguestats.models.Summoner;
import com.example.android.leaguestats.utilities.LocaleUtils;
import com.example.android.leaguestats.utilities.PreferencesUtils;

public class NavigationActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView mBackgroundImage;
    private Button mChampionListButton;
    private Button mSummonerButton;
    private Button mItemsButton;
    private Button mSummonerSpellButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        mBackgroundImage = findViewById(R.id.navigation_background_image);
        mChampionListButton = findViewById(R.id.champion_list_button);
        mSummonerButton = findViewById(R.id.summoner_button);
        mItemsButton = findViewById(R.id.item_button);
        mSummonerSpellButton = findViewById(R.id.summoner_spell_button);

        mChampionListButton.setOnClickListener(this);
        mSummonerButton.setOnClickListener(this);
        mItemsButton.setOnClickListener(this);
        mSummonerSpellButton.setOnClickListener(this);

        setBackground();
    }

    private void setBackground() {
        String language = PreferencesUtils.getUserLanguage(this);
        switch (language) {
            case "pl":
                mBackgroundImage.setImageResource(R.drawable.polish_flag);
                break;
            case "en":
                mBackgroundImage.setImageResource(R.drawable.usa_flag);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.champion_list_button:
                showChampions();
                break;
            case R.id.summoner_button:
                showMastery();
                break;
            case R.id.item_button:
                showItems();
                break;
            case R.id.summoner_spell_button:
                showSummonerSpells();
                break;
        }
    }

    private void showChampions() {
        Intent intent = new Intent(NavigationActivity.this, ChampionListActivity.class);
        startActivity(intent);
    }

    private void showMastery() {
        Intent intent = new Intent(NavigationActivity.this, SummonerActivity.class);
        startActivity(intent);
    }

    private void showItems() {
        Intent intent = new Intent(NavigationActivity.this, ItemActivity.class);
        startActivity(intent);
    }

    private void showSummonerSpells() {
        Intent intent = new Intent(NavigationActivity.this, SummonerSpellActivity.class);
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
                LocaleUtils.changeLanguage(NavigationActivity.this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
