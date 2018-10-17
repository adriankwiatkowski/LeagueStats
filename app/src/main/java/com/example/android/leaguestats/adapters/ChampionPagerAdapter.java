package com.example.android.leaguestats.adapters;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.android.leaguestats.ui.ChampionInfoFragment;
import com.example.android.leaguestats.ui.ChampionOverviewFragment;
import com.example.android.leaguestats.ui.ChampionTipsFragment;
import com.example.android.leaguestats.R;

public class ChampionPagerAdapter extends FragmentPagerAdapter {

    private Context mContext;

    public ChampionPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new ChampionOverviewFragment();
            case 1:
                return new ChampionInfoFragment();
            case 2:
                return new ChampionTipsFragment();
            default:
                return null;
        }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return mContext.getString(R.string.overview);
            case 1:
                return mContext.getString(R.string.info);
            case 2:
                return mContext.getString(R.string.tips);
            default:
                return super.getPageTitle(position);
        }
    }
}
