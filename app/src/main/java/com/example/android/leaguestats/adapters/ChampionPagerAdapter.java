package com.example.android.leaguestats.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.example.android.leaguestats.ChampionInfoFragment;
import com.example.android.leaguestats.ChampionOverviewFragment;
import com.example.android.leaguestats.ChampionTipsFragment;
import com.example.android.leaguestats.R;

import java.util.HashMap;
import java.util.Map;

public class ChampionPagerAdapter extends FragmentPagerAdapter {

    private Context mContext;
    private Map<Integer, String> mFragmentsTag;
    private FragmentManager mFragmentManager;
    private Uri mCurrentUri;

    public ChampionPagerAdapter(Context context, FragmentManager fm, Uri uri) {
        super(fm);
        mContext = context;
        mFragmentManager = fm;
        mFragmentsTag = new HashMap<>();
        mCurrentUri = uri;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return ChampionOverviewFragment.newInstance(mCurrentUri);
            case 1:
                return ChampionInfoFragment.newInstance(mCurrentUri);
            case 2:
                return ChampionTipsFragment.newInstance(mCurrentUri);
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
