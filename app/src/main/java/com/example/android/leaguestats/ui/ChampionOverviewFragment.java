package com.example.android.leaguestats.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
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
import com.example.android.leaguestats.utilities.InjectorUtils;
import com.example.android.leaguestats.viewmodels.ChampionModel;
import com.example.android.leaguestats.viewmodels.ChampionModelFactory;
import com.example.android.leaguestats.adapters.SplashArtAdapter;
import com.example.android.leaguestats.data.database.entity.ChampionEntry;

import java.util.ArrayList;
import java.util.List;

public class ChampionOverviewFragment extends Fragment {

    private static final String LOG_TAG = ChampionOverviewFragment.class.getSimpleName();
    private RecyclerView mSplashArtRecyclerView;
    private SplashArtAdapter mAdapter;
    private TextView mChampionLoreTv;
    private ProgressBar mDifficultyProgressBar;
    private ProgressBar mAttackProgressBar;
    private ProgressBar mDefenseProgressBar;
    private ProgressBar mMagicProgressBar;

    public ChampionOverviewFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_champion_overview, container, false);

        mSplashArtRecyclerView = rootView.findViewById(R.id.splash_art_recycler_view);

        mSplashArtRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        mSplashArtRecyclerView.setHasFixedSize(true);
        mSplashArtRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new SplashArtAdapter(getActivity(), new ArrayList<String>(), new ArrayList<String>());
        mSplashArtRecyclerView.setAdapter(mAdapter);

        mChampionLoreTv = rootView.findViewById(R.id.champion_lore_tv);
        mDifficultyProgressBar = rootView.findViewById(R.id.difficulty_progress_bar);
        mAttackProgressBar = rootView.findViewById(R.id.attack_progress_bar);
        mDefenseProgressBar = rootView.findViewById(R.id.defense_progress_bar);
        mMagicProgressBar = rootView.findViewById(R.id.magic_progress_bar);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mSplashArtRecyclerView.setNestedScrollingEnabled(false);

        setupViewModel();
    }

    private void setupViewModel() {
        ChampionModelFactory factory =
                InjectorUtils.provideChampionModelFactory(getActivity().getApplicationContext());
        final ChampionModel viewModel =
                ViewModelProviders.of(getActivity(), factory).get(ChampionModel.class);
        viewModel.getChampion().observe(getActivity(), new Observer<ChampionEntry>() {
            @Override
            public void onChanged(@Nullable ChampionEntry championEntry) {
                Log.d(LOG_TAG, "Receiving database update from LiveData");
                updateUi(championEntry);
            }
        });
    }

    private void updateUi(ChampionEntry championEntry) {
        if (championEntry != null) {

            List<String> splashArtList = championEntry.getSplashArt();
            List<String> splashArtNameList = championEntry.getSplashArtName();

            mAdapter.setData(splashArtList, splashArtNameList);

            mChampionLoreTv.setText(championEntry.getLore());
            mDifficultyProgressBar.setProgress(championEntry.getDifficulty());
            mAttackProgressBar.setProgress(championEntry.getAttack());
            mDefenseProgressBar.setProgress(championEntry.getDefense());
            mMagicProgressBar.setProgress(championEntry.getMagic());
        }
    }
}
