package com.example.android.leaguestats.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.leaguestats.R;

public class MasterFragment extends Fragment {

    public interface OnFragmentAdded {
        void onMasterBackStackChanged();
    }

    private OnFragmentAdded mCallback;

    public static final String CHAMPION_LIST_TAG = "champion-list-tag";
    public static final String ITEM_LIST_TAG = "item-list-tag";
    public static final String SUMMONER_SPELL_TAG = "summoner-spell-tag";

    private static final String NEW_TAG = "new-tag";

    private boolean mTwoPane;

    public MasterFragment() {}

    public static MasterFragment newInstance(String fragmentTag) {
        MasterFragment masterFragment = new MasterFragment();
        Bundle args = new Bundle();
        args.putString(NEW_TAG, fragmentTag);
        masterFragment.setArguments(args);
        return masterFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_master, container, false);

        mTwoPane = rootView.findViewById(R.id.master_fragment_detail_container) != null;

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        FragmentManager fragmentManager = getChildFragmentManager();

        switch (getArguments().getString(NEW_TAG)) {
            case CHAMPION_LIST_TAG:
                if (fragmentManager.findFragmentByTag(CHAMPION_LIST_TAG) == null) {
                    fragmentManager.beginTransaction()
                            .add(R.id.master_fragment_container, new ChampionListFragment(), CHAMPION_LIST_TAG)
                            .commit();
                }
                break;
            case ITEM_LIST_TAG:
                if (fragmentManager.findFragmentByTag(ITEM_LIST_TAG) == null) {
                    fragmentManager.beginTransaction()
                            .add(R.id.master_fragment_container, new ItemListFragment(), ITEM_LIST_TAG)
                            .commit();
                }
                break;
            case SUMMONER_SPELL_TAG:
                if (fragmentManager.findFragmentByTag(SUMMONER_SPELL_TAG) == null) {
                    fragmentManager.beginTransaction()
                            .add(R.id.master_fragment_container, new SummonerSpellListFragment(), SUMMONER_SPELL_TAG)
                            .commit();
                }
                break;
        }

        fragmentManager.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                mCallback.onMasterBackStackChanged();
            }
        });
    }

    public void addItemDetailFragment(int id) {
        ItemDetailFragment fragment = ItemDetailFragment.newInstance(id);
        if (mTwoPane) {
            getChildFragmentManager().beginTransaction()
                    .replace(R.id.master_fragment_detail_container, fragment)
                    .commit();
        } else {
            getChildFragmentManager().beginTransaction()
                    .addToBackStack(null)
                    .replace(R.id.master_fragment_container, fragment)
                    .commit();
        }
    }

    public void addSummonerSpellDetailFragment() {
        SummonerSpellDetailFragment fragment = new SummonerSpellDetailFragment();
        if (mTwoPane) {
            getChildFragmentManager().beginTransaction()
                    .replace(R.id.master_fragment_detail_container, fragment)
                    .commit();
        } else {
            getChildFragmentManager().beginTransaction()
                    .addToBackStack(null)
                    .replace(R.id.master_fragment_container, fragment)
                    .commit();
        }
    }

    public void addChampionFragment() {
        ChampionFragment championFragment = new ChampionFragment();
        getChildFragmentManager().beginTransaction()
                .replace(R.id.master_fragment_detail_container, championFragment)
                .commit();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mCallback = (OnFragmentAdded) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnFragmentAdded");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallback = null;
    }
}
