package com.example.android.leaguestats.ui;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.example.android.leaguestats.R;
import com.example.android.leaguestats.utilities.DataUtils;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mChampionListButton;
    private Button mSummonerButton;
    private Button mItemsButton;
    private Button mSummonerSpellButton;
    private String mEntryUrlString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.champion_list_button:
                showChampions();
                break;
            case R.id.summoner_button:
                showSummoner();
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
        Intent intent = new Intent(MainActivity.this, ChampionListActivity.class);
        startActivity(intent);
    }

    private void showSummoner() {
        Intent intent = new Intent(MainActivity.this, SummonerActivity.class);
        startActivity(intent);
    }

    private void showItems() {
        Intent intent = new Intent(MainActivity.this, ItemListActivity.class);
        startActivity(intent);
    }

    private void showSummonerSpells() {
        Intent intent = new Intent(MainActivity.this, SummonerSpellActivity.class);
        startActivity(intent);
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
        searchView.setSearchableInfo(searchManager.getSearchableInfo(new ComponentName(this, SummonerActivity.class)));
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id) {
            case R.id.action_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initViews() {
        mChampionListButton = findViewById(R.id.champion_list_button);
        mSummonerButton = findViewById(R.id.summoner_button);
        mItemsButton = findViewById(R.id.item_button);
        mSummonerSpellButton = findViewById(R.id.summoner_spell_button);

        mChampionListButton.setOnClickListener(this);
        mSummonerButton.setOnClickListener(this);
        mItemsButton.setOnClickListener(this);
        mSummonerSpellButton.setOnClickListener(this);
    }
}
