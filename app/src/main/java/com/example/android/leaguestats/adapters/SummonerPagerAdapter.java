package com.example.android.leaguestats.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.example.android.leaguestats.R;
import com.example.android.leaguestats.SummonerHistoryFragment;
import com.example.android.leaguestats.SummonerMasteryFragment;

import java.util.HashMap;
import java.util.Map;

public class SummonerPagerAdapter extends FragmentPagerAdapter {

    private Context mContext;
    private Map<Integer, String> mFragmentsTag;
    private FragmentManager mFragmentManager;
    private String mSummonerName;
    private String mEntryRegionString;

    public SummonerPagerAdapter(Context context, FragmentManager fm,
                                String summonerName, String entryRegionString) {
        super(fm);
        mContext = context;
        mFragmentManager = fm;
        mFragmentsTag = new HashMap<>();
        mSummonerName = summonerName;
        mEntryRegionString = entryRegionString;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return SummonerMasteryFragment.newInstance(mSummonerName, mEntryRegionString);
            case 1:
                return SummonerHistoryFragment.newInstance(mSummonerName, mEntryRegionString);
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

    @NonNull
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Object object = super.instantiateItem(container, position);
        if (object instanceof Fragment) {
            Fragment fragment = (Fragment) object;
            String tag = fragment.getTag();
            mFragmentsTag.put(position, tag);
        }
        return object;
    }

    public Fragment getFragment(int position) {
        Fragment fragment = null;
        String tag = mFragmentsTag.get(position);
        if (tag != null) {
            fragment = mFragmentManager.findFragmentByTag(tag);
        }
        return fragment;
    }
}
