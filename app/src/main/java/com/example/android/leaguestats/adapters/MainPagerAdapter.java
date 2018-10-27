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
import com.example.android.leaguestats.ui.MasterFragment;
import com.example.android.leaguestats.ui.SummonerSearchFragment;

public class MainPagerAdapter extends FragmentPagerAdapter {

    public static final int SUMMONER_POSITION = 0;
    public static final int CHAMPION_POSITION = 1;
    public static final int ITEM_POSITION = 2;
    public static final int SPELL_POSITION = 3;

    private static final int FRAGMENT_COUNT = 4;

    private SparseArray<Fragment> mRegisteredFragments = new SparseArray<>();

    private Context mContext;

    public MainPagerAdapter(Context context, FragmentManager fm) {
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
            case SUMMONER_POSITION:
                return new SummonerSearchFragment();
            case CHAMPION_POSITION:
                return MasterFragment.newInstance(MasterFragment.CHAMPION_LIST_TAG);
            case ITEM_POSITION:
                return MasterFragment.newInstance(MasterFragment.ITEM_LIST_TAG);
            case SPELL_POSITION:
                return MasterFragment.newInstance(MasterFragment.SUMMONER_SPELL_TAG);
            default:
                return null;
        }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case SUMMONER_POSITION:
                return mContext.getString(R.string.summoner);
            case CHAMPION_POSITION:
                return mContext.getString(R.string.champions);
            case ITEM_POSITION:
                return mContext.getString(R.string.items);
            case SPELL_POSITION:
                return mContext.getString(R.string.summoner_spells);
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
