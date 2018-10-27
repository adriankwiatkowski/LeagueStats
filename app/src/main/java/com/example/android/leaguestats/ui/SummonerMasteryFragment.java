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
import android.widget.TextView;

import com.example.android.leaguestats.R;
import com.example.android.leaguestats.adapters.MasteryAdapter;
import com.example.android.leaguestats.interfaces.IdClickListener;
import com.example.android.leaguestats.models.Mastery;
import com.example.android.leaguestats.utilities.InjectorUtils;
import com.example.android.leaguestats.utilities.LeaguePreferences;
import com.example.android.leaguestats.viewmodels.SummonerModel;
import com.example.android.leaguestats.viewmodels.SummonerModelFactory;

import java.util.ArrayList;
import java.util.List;

public class SummonerMasteryFragment extends Fragment implements IdClickListener {

    private MasteryListener mCallback;

    public interface MasteryListener {
        void onMasteryChampionListener(int championId);
    }

    private static final String LOG_TAG = SummonerMasteryFragment.class.getSimpleName();
    private RecyclerView mRecyclerView;
    private MasteryAdapter mAdapter;
    private SummonerModel mSummonerModel;
    private String mPatchVersion;
    private TextView mEmptyViewTv;
    private ProgressBar mRecyclerIndicator;

    public SummonerMasteryFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_summoner_mastery, container, false);

        mRecyclerView = rootView.findViewById(R.id.summoner_recycler_view);
        mEmptyViewTv = rootView.findViewById(R.id.summoner_empty_view_tv);
        mRecyclerIndicator = rootView.findViewById(R.id.summoner_recycler_indicator);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mPatchVersion = LeaguePreferences.getPatchVersion(getContext());

        mAdapter = new MasteryAdapter(getContext(), new ArrayList<Mastery>(), SummonerMasteryFragment.this, mPatchVersion);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        setupViewModel();
    }

    private void setupViewModel() {
        SummonerModelFactory factory =
                InjectorUtils.provideSummonerModelFactory(getActivity().getApplicationContext());
        mSummonerModel = ViewModelProviders.of(getActivity(), factory).get(SummonerModel.class);
        mSummonerModel.getMasteries().observe(getActivity(), new Observer<List<Mastery>>() {
            @Override
            public void onChanged(@Nullable List<Mastery> masteries) {
                Log.d(LOG_TAG, "Getting masteries");
                updateUi(masteries);
            }
        });
    }

    private void updateUi(@Nullable List<Mastery> masteries) {
        if (masteries != null && !masteries.isEmpty()) {
            mAdapter.swapData(masteries);
        } else {
            mEmptyViewTv.setText(getString(R.string.no_masteries_found));
            mEmptyViewTv.setVisibility(View.VISIBLE);
        }
        mRecyclerIndicator.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onClickListener(int id) {
        mCallback.onMasteryChampionListener(id);
    }

    // Called from SummonerActivity.
    public void showMasteryIndicator() {
        mRecyclerIndicator.setVisibility(View.VISIBLE);
        mEmptyViewTv.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mCallback = (MasteryListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement MasteryListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallback = null;
    }
}
