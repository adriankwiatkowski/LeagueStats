package com.example.android.leaguestats;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.leaguestats.viewModels.SummonerSpellSharedViewModel;
import com.example.android.leaguestats.viewModels.SummonerSpellSharedViewModelFactory;
import com.example.android.leaguestats.adapters.SummonerSpellAdapter;
import com.example.android.leaguestats.database.AppDatabase;
import com.example.android.leaguestats.database.SummonerSpellEntry;
import com.example.android.leaguestats.utilities.PreferencesUtils;

import java.util.ArrayList;
import java.util.List;

public class SummonerSpellListFragment extends Fragment
        implements SummonerSpellAdapter.SummonerSpellClickListener {

    public interface OnSummonerSpellSelected {
        void onSummonerSpellSelected();
    }

    private static final String LOG_TAG = SummonerSpellListFragment.class.getSimpleName();
    private OnSummonerSpellSelected mCallback;
    private RecyclerView mRecyclerView;
    private SummonerSpellAdapter mAdapter;
    private String LAYOUT_MANAGER_STATE_KEY = "layoutManagerStateKey";
    private Parcelable mLayoutManagerViewState;
    private SummonerSpellSharedViewModel mViewModel;
    private AppDatabase mDb;

    public SummonerSpellListFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i(LOG_TAG, "onCreateView");
        View rootView = inflater.inflate(R.layout.fragment_summoner_spell_list, container, false);

        mRecyclerView = rootView.findViewById(R.id.summoner_spell_recycler);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(LOG_TAG, "onActivityCreated");

        int gridLayoutColumnCount;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            gridLayoutColumnCount = 3;
        } else {
            gridLayoutColumnCount = 2;
        }
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), gridLayoutColumnCount));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        String patchVersion = PreferencesUtils.getPatchVersion(getContext());
        mAdapter = new SummonerSpellAdapter(getContext(), new ArrayList<SummonerSpellEntry>(), this, patchVersion);
        mRecyclerView.setAdapter(mAdapter);

        if (savedInstanceState != null && savedInstanceState.containsKey(LAYOUT_MANAGER_STATE_KEY)) {
            mLayoutManagerViewState = savedInstanceState.getParcelable(LAYOUT_MANAGER_STATE_KEY);
        }

        mDb = AppDatabase.getInstance(getActivity().getApplicationContext());

        setupViewModel();
    }

    private void setupViewModel() {
        SummonerSpellSharedViewModelFactory factory = new SummonerSpellSharedViewModelFactory(mDb);
        mViewModel = ViewModelProviders.of(getActivity(), factory).get(SummonerSpellSharedViewModel.class);
        mViewModel.getSummonerSpells().observe(this, new Observer<List<SummonerSpellEntry>>() {
            @Override
            public void onChanged(@Nullable List<SummonerSpellEntry> list) {
                Log.d(LOG_TAG, "Receiving database update from LiveData");
                mAdapter.setData(list);
            }
        });
    }

    @Override
    public void onSummonerSpellClick(SummonerSpellEntry summonerSpellEntry) {
        mViewModel.select(summonerSpellEntry);
        mCallback.onSummonerSpellSelected();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mCallback = (OnSummonerSpellSelected) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnSummonerSpellSelected");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallback = null;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        mLayoutManagerViewState =  mRecyclerView.getLayoutManager().onSaveInstanceState();
        outState.putParcelable(LAYOUT_MANAGER_STATE_KEY, mLayoutManagerViewState);
    }
}
