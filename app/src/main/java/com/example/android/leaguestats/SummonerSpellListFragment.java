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

import com.example.android.leaguestats.database.models.ListSummonerSpellEntry;
import com.example.android.leaguestats.interfaces.IdClickListener;
import com.example.android.leaguestats.interfaces.StringIdClickListener;
import com.example.android.leaguestats.utilities.InjectorUtils;
import com.example.android.leaguestats.viewModels.SummonerSpellListModel;
import com.example.android.leaguestats.viewModels.SummonerSpellListModelFactory;
import com.example.android.leaguestats.adapters.SummonerSpellAdapter;
import com.example.android.leaguestats.database.AppDatabase;
import com.example.android.leaguestats.utilities.PreferencesUtils;

import java.util.ArrayList;
import java.util.List;

public class SummonerSpellListFragment extends Fragment implements StringIdClickListener {

    public interface OnSummonerSpellSelected {
        void onSummonerSpellSelected(String id);
    }

    private static final String LOG_TAG = SummonerSpellListFragment.class.getSimpleName();
    private OnSummonerSpellSelected mCallback;
    private RecyclerView mRecyclerView;
    private SummonerSpellAdapter mAdapter;
    private String LAYOUT_MANAGER_STATE_KEY = "layoutManagerStateKey";
    private Parcelable mLayoutManagerViewState;
    private SummonerSpellListModel mViewModel;

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
        mAdapter = new SummonerSpellAdapter(getContext(), new ArrayList<ListSummonerSpellEntry>(), this, patchVersion);
        mRecyclerView.setAdapter(mAdapter);

        if (savedInstanceState != null && savedInstanceState.containsKey(LAYOUT_MANAGER_STATE_KEY)) {
            mLayoutManagerViewState = savedInstanceState.getParcelable(LAYOUT_MANAGER_STATE_KEY);
        }

        setupViewModel();
    }

    private void setupViewModel() {
        SummonerSpellListModelFactory factory =
                InjectorUtils.provideSummonerSpellListModelFactory(getActivity().getApplicationContext());
        mViewModel = ViewModelProviders.of(this, factory).get(SummonerSpellListModel.class);
        mViewModel.getSpellList().observe(this, new Observer<List<ListSummonerSpellEntry>>() {
            @Override
            public void onChanged(@Nullable List<ListSummonerSpellEntry> listSummonerSpellEntries) {
                Log.d(LOG_TAG, "Receiving database update from LiveData");
                mViewModel.getSpellList().removeObserver(this);
                mAdapter.setData(listSummonerSpellEntries);
            }
        });
    }

    @Override
    public void onClickListener(String id) {
        mCallback.onSummonerSpellSelected(id);
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
