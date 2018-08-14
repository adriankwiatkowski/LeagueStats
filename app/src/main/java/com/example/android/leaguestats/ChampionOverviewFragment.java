package com.example.android.leaguestats;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.leaguestats.ViewModels.ChampionViewModelShared;
import com.example.android.leaguestats.ViewModels.ChampionViewModelSharedFactory;
import com.example.android.leaguestats.adapters.SplashArtAdapter;
import com.example.android.leaguestats.room.AppDatabase;
import com.example.android.leaguestats.room.ChampionEntry;
import com.example.android.leaguestats.utilities.DataUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChampionOverviewFragment extends Fragment {

    private static final String LOG_TAG = ChampionOverviewFragment.class.getSimpleName();
    private RecyclerView mSplashArtRecyclerView;
    private SplashArtAdapter mAdapter;
    private List<String> mSplashArtArray;
    private List<String> mSplashArtNameArray;
    private TextView mChampionLoreTv;
    private ProgressBar mDifficultyProgressBar;
    private ProgressBar mAttackProgressBar;
    private ProgressBar mDefenseProgressBar;
    private ProgressBar mMagicProgressBar;
    private String SPLASH_ART_LAYOUT_MANAGER_STATE_KEY = "splashArtLayoutManagerStateKey";
    private AppDatabase mDb;

    public ChampionOverviewFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_champion_overview, container, false);
        Log.d(LOG_TAG, "onCreateView");

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
        Log.d(LOG_TAG, "onActivityCreated");

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(SPLASH_ART_LAYOUT_MANAGER_STATE_KEY)) {
                mSplashArtRecyclerView.getLayoutManager().onRestoreInstanceState(savedInstanceState.getParcelable(SPLASH_ART_LAYOUT_MANAGER_STATE_KEY));
            }
        }

        mSplashArtRecyclerView.setNestedScrollingEnabled(false);

        mDb = AppDatabase.getInstance(getActivity().getApplicationContext());
        setupViewModel();
    }

    private void setupViewModel() {
        ChampionViewModelSharedFactory factory = new ChampionViewModelSharedFactory(mDb);
        final ChampionViewModelShared viewModel =
                ViewModelProviders.of(getActivity(), factory).get(ChampionViewModelShared.class);
        viewModel.getSelected().observe(this, new Observer<ChampionEntry>() {
            @Override
            public void onChanged(@Nullable ChampionEntry championEntry) {
                Log.d(LOG_TAG, "Receiving database update from LiveData");
                updateUi(championEntry);
            }
        });
    }

    private void updateUi(ChampionEntry championEntry) {
        List<String> splashArtList = championEntry.getSplashArt();;

        List<String> splashArtNameList = championEntry.getSplashArtName();;

        mAdapter.setData(splashArtList, splashArtNameList);

        mChampionLoreTv.setText(championEntry.getLore());
        mDifficultyProgressBar.setProgress(championEntry.getDifficulty());
        mAttackProgressBar.setProgress(championEntry.getAttack());
        mDefenseProgressBar.setProgress(championEntry.getDefense());
        mMagicProgressBar.setProgress(championEntry.getMagic());
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable(SPLASH_ART_LAYOUT_MANAGER_STATE_KEY, mSplashArtRecyclerView.getLayoutManager().onSaveInstanceState());
    }
}
