package com.example.android.leaguestats.adapters;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.example.android.leaguestats.R;
import com.example.android.leaguestats.SummonerHistoryFragment;
import com.example.android.leaguestats.SummonerMasteryFragment;

public class SummonerPagerAdapter extends FragmentPagerAdapter {

    private Context mContext;
    private static final int FRAGMENT_COUNT = 2;
    public static final int SUMMONER_HISTORY_POSITION = 0;
    public static final int SUMMONER_MASTERY_POSITION = 1;
    private SparseArray<Fragment> mRegisteredFragments = new SparseArray<>();

    public SummonerPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public int getCount() {
        return FRAGMENT_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case SUMMONER_HISTORY_POSITION:
                return new SummonerHistoryFragment();
            case SUMMONER_MASTERY_POSITION:
                return new SummonerMasteryFragment();
            default:
                return null;
        }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return mContext.getString(R.string.match_history);
            case 1:
                return mContext.getString(R.string.mastery_points);
            default:
                return super.getPageTitle(position);
        }
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        mRegisteredFragments.put(position, fragment);
        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        mRegisteredFragments.remove(position);
        super.destroyItem(container, position, object);
    }

    public Fragment getFragment(int position) {
        return mRegisteredFragments.get(position);
    }
}
