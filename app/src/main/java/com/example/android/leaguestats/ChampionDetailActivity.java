package com.example.android.leaguestats;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.android.leaguestats.adapters.ChampionPagerAdapter;
import com.example.android.leaguestats.utilities.LocaleUtils;

public class ChampionDetailActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {

    private final String LOG_TAG = ChampionDetailActivity.class.getSimpleName();
    private ChampionPagerAdapter mChampionPagerAdapter;
    private Uri mCurrentChampionUri;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_champion_detail);

        mCurrentChampionUri = getIntent().getData();

        ViewPager viewPager = findViewById(R.id.champion_viewpager);
        mChampionPagerAdapter = new ChampionPagerAdapter(this, getSupportFragmentManager(), mCurrentChampionUri);
        viewPager.setAdapter(mChampionPagerAdapter);
        viewPager.addOnPageChangeListener(this);

        TabLayout tabLayout = findViewById(R.id.champion_tab);
        tabLayout.setupWithViewPager(viewPager);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        Fragment fragment = mChampionPagerAdapter.getFragment(position);
        if (fragment != null) {
            fragment.onResume();
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

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
                onBackPressed();
                return true;
            case R.id.change_language:
                LocaleUtils.changeLanguage(ChampionDetailActivity.this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
