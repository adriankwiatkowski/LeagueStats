package com.example.android.leaguestats.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.android.leaguestats.R;
import com.example.android.leaguestats.adapters.MainPagerAdapter;

public class MainActivity extends AppCompatActivity
        implements SummonerSearchFragment.OnSummonerListener,
        ChampionListFragment.OnChampionSelected, MasterFragment.OnFragmentAdded {

    private FragmentManager mFragmentManager;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFragmentManager = getSupportFragmentManager();

        setupViewPager();
    }

    private void setupViewPager() {

        mViewPager = findViewById(R.id.main_view_pager);

        MainPagerAdapter mainPagerAdapter =
                new MainPagerAdapter(this, mFragmentManager);

        mViewPager.setAdapter(mainPagerAdapter);

        TabLayout tabLayout = findViewById(R.id.main_tab);
        tabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    protected void onStart() {
        super.onStart();

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float v, int i1) {
            }

            @Override
            public void onPageSelected(int position) {
                setupUpButton();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    protected void onStop() {
        mViewPager.clearOnPageChangeListeners();
        super.onStop();
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
                onMasterBackStackChanged();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSummonerSearch(String entryUrlString, String summonerName) {
        Intent intent = new Intent(this, SummonerActivity.class)
                .setAction(SummonerActivity.ACTION_SEARCH_SUMMONER)
                .putExtra(SummonerActivity.ENTRY_URL_STRING_KEY, entryUrlString)
                .putExtra(SummonerActivity.SUMMONER_NAME_KEY, summonerName);
        startActivity(intent);
    }

    @Override
    public void onChampionSelected(int id) {
        Intent intent = new Intent(this, ChampionActivity.class);
        intent.putExtra(ChampionActivity.CHAMPION_ID_KEY, id);
        startActivity(intent);
    }

    @Override
    public void onMasterBackStackChanged() {
        setupUpButton();
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getCurrentFragment();
        if (fragment != null) {
            if (fragment.getView() != null) {
                // Pop the backstack on the ChildManager if there is any. If not, close this activity as normal.
                if (!fragment.getChildFragmentManager().popBackStackImmediate()) {
                    finish();
                }
            }
        }
    }

    private void setupUpButton() {
        Fragment fragment = getCurrentFragment();
        if (fragment != null) {
            boolean canBack = fragment.getChildFragmentManager().getBackStackEntryCount() > 0;
            getSupportActionBar().setDisplayHomeAsUpEnabled(canBack);
        }
    }

    private Fragment getCurrentFragment() {
        MainPagerAdapter mainPagerAdapter = (MainPagerAdapter) mViewPager.getAdapter();
        return mainPagerAdapter.getFragment(mViewPager.getCurrentItem());
    }
}
