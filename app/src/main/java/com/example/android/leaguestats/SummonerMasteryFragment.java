package com.example.android.leaguestats;

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

import com.example.android.leaguestats.adapters.MasteryAdapter;
import com.example.android.leaguestats.interfaces.StringIdClickListener;
import com.example.android.leaguestats.models.Mastery;
import com.example.android.leaguestats.utilities.InjectorUtils;
import com.example.android.leaguestats.utilities.PreferencesUtils;
import com.example.android.leaguestats.viewModels.MasteryModel;
import com.example.android.leaguestats.viewModels.MasteryModelFactory;

import java.util.ArrayList;
import java.util.List;

public class SummonerMasteryFragment extends Fragment implements StringIdClickListener {

    private MasteryListener mCallback;

    public interface MasteryListener {
        void onChampionClick(String championId);
    }

    private static final String LOG_TAG = SummonerMasteryFragment.class.getSimpleName();
    private RecyclerView mRecyclerView;
    private MasteryAdapter mAdapter;
    private MasteryModel mMasteryModel;
    private String mPatchVersion;
    private TextView mEmptyViewTv;
    private ProgressBar mRecyclerIndicator;
    private String LAYOUT_MANAGER_STATE_KEY = "layoutManagerStateKey";

    public SummonerMasteryFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_summoner, container, false);
        Log.d(LOG_TAG, "onCreateView");

        mRecyclerView = rootView.findViewById(R.id.summoner_recycler_view);
        mEmptyViewTv = rootView.findViewById(R.id.summoner_empty_view_tv);
        mRecyclerIndicator = rootView.findViewById(R.id.summoner_recycler_indicator);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(LOG_TAG, "onActivityCreated");

        mPatchVersion = PreferencesUtils.getPatchVersion(getContext());

        mAdapter = new MasteryAdapter(getContext(), new ArrayList<Mastery>(), SummonerMasteryFragment.this, mPatchVersion);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(LAYOUT_MANAGER_STATE_KEY)) {
                mRecyclerView.getLayoutManager().onRestoreInstanceState(
                        savedInstanceState.getParcelable(LAYOUT_MANAGER_STATE_KEY));
            }
        }

        setupViewModel();
    }

    private void setupViewModel() {

        if (mMasteryModel != null) {
            observeMasteryModel();
            return;
        }

        MasteryModelFactory factory =
                InjectorUtils.provideMasteryModelFactory(getActivity().getApplicationContext());
        mMasteryModel = ViewModelProviders.of(getActivity(), factory).get(MasteryModel.class);

        observeMasteryModel();
    }

    // Called from SummonerActivity.
    public void initMasteryData(String entryUrlString, long summonerId) {
        Log.d(LOG_TAG, "initMasteryData");

        mRecyclerIndicator.setVisibility(View.VISIBLE);

        mMasteryModel.initMasteries(entryUrlString, summonerId);
        setupViewModel();
    }

    private void observeMasteryModel() {
        if (mMasteryModel.getMasteries() != null) {
            if (!mMasteryModel.getMasteries().hasObservers()) {
                mMasteryModel.getMasteries().observe(getActivity(), new Observer<List<Mastery>>() {
                    @Override
                    public void onChanged(@Nullable List<Mastery> masteries) {
                        updateUi(masteries);
                    }
                });
            }
        }
    }

    private void updateUi(List<Mastery> masteries) {
        mRecyclerIndicator.setVisibility(View.INVISIBLE);
        if (masteries != null && !masteries.isEmpty()) {
            mAdapter.swapData(masteries);
        } else {
            mEmptyViewTv.setText(getString(R.string.no_masteries_found));
            mEmptyViewTv.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClickListener(String championId) {
        Log.d(LOG_TAG, "onClickListener");
        mCallback.onChampionClick(championId);
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
        Log.d(LOG_TAG, "onDetach");
        mRecyclerView.setAdapter(null);
        mCallback = null;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable(LAYOUT_MANAGER_STATE_KEY, mRecyclerView.getLayoutManager().onSaveInstanceState());
    }
}
