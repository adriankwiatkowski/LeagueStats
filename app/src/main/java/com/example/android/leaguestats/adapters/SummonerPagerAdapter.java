package com.example.android.leaguestats.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.example.android.leaguestats.R;
import com.example.android.leaguestats.ui.SummonerHistoryFragment;
import com.example.android.leaguestats.ui.SummonerMasteryFragment;

public class SummonerPagerAdapter extends FragmentPagerAdapter {

    public static final int SUMMONER_MASTERY_POSITION = 0;
    public static final int SUMMONER_HISTORY_POSITION = 1;
    private Context mContext;
    private static final int FRAGMENT_COUNT = 2;
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
            case SUMMONER_MASTERY_POSITION:
                return new SummonerMasteryFragment();
            case SUMMONER_HISTORY_POSITION:
                return new SummonerHistoryFragment();
            default:
                return null;
        }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case SUMMONER_MASTERY_POSITION:
                return mContext.getString(R.string.mastery_points);
            case SUMMONER_HISTORY_POSITION:
                return mContext.getString(R.string.match_history);
            default:
                return super.getPageTitle(position);
        }
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        mRegisteredFragments.put(position, fragment);
        return fragment;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        mRegisteredFragments.remove(position);
        super.destroyItem(container, position, object);
    }

    public Fragment getFragment(int position) {
        return mRegisteredFragments.get(position);
    }
}
