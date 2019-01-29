package com.example.android.leaguestats.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.android.leaguestats.R;
import com.example.android.leaguestats.adapters.SummonerSpellAdapter;
import com.example.android.leaguestats.interfaces.IdClickListener;
import com.example.android.leaguestats.models.SummonerSpell;
import com.example.android.leaguestats.utilities.InjectorUtils;
import com.example.android.leaguestats.utilities.LeaguePreferences;
import com.example.android.leaguestats.viewmodels.SummonerSpellModel;
import com.example.android.leaguestats.viewmodels.SummonerSpellModelFactory;

import java.util.List;

public class SummonerSpellListFragment extends Fragment implements IdClickListener {

    public interface OnSummonerSpellSelected {
        void onSummonerSpellSelected(int id);
    }

    private static final String LOG_TAG = SummonerSpellListFragment.class.getSimpleName();
    private OnSummonerSpellSelected mCallback;
    private RecyclerView mRecyclerView;
    private SummonerSpellAdapter mAdapter;
    private SummonerSpellModel mViewModel;
    private ProgressBar mIndicator;
    private String mPatchVersion;

    public SummonerSpellListFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_summoner_spell_list, container, false);
        mRecyclerView = rootView.findViewById(R.id.recycler_view);
        mIndicator = rootView.findViewById(R.id.indicator);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mPatchVersion = LeaguePreferences.getPatchVersion(getContext());

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new SummonerSpellAdapter(getContext(), this, mPatchVersion);
        mRecyclerView.setAdapter(mAdapter);

        setupViewModel();
    }

    private void setupViewModel() {
        SummonerSpellModelFactory factory =
                InjectorUtils.provideSummonerSpellModelFactory(getActivity().getApplicationContext());
        mViewModel = ViewModelProviders.of(getActivity(), factory).get(SummonerSpellModel.class);
        mViewModel.getSummonerSpells().observe(this, new Observer<List<SummonerSpell>>() {
            @Override
            public void onChanged(@Nullable List<SummonerSpell> summonerSpellList) {
                Log.d(LOG_TAG, "Receiving database update from LiveData");
                if (summonerSpellList != null && !summonerSpellList.isEmpty()) {
                    mAdapter.setData(summonerSpellList);
                    mIndicator.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    public void filterResults(String query) {
        mAdapter.getFilter().filter(query);
    }

    @Override
    public void onClick(int id) {
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
}
