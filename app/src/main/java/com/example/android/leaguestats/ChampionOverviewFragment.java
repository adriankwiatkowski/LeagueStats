package com.example.android.leaguestats;

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

import com.example.android.leaguestats.adapters.SplashArtAdapter;
import com.example.android.leaguestats.database.Contract;
import com.example.android.leaguestats.utilities.DataUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChampionOverviewFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String LOG_TAG = ChampionOverviewFragment.class.getSimpleName();
    private static final String DB_SIGN = " = ?";
    private RecyclerView mSplashArtRecyclerView;
    private SplashArtAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<String> mSplashArtArray;
    private List<String> mSplashArtNameArray;
    private TextView mChampionNameTv;
    private TextView mChampionTitleTv;
    private TextView mChampionLoreTv;
    private ProgressBar mDifficultyProgressBar;
    private ProgressBar mAttackProgressBar;
    private ProgressBar mDefenseProgressBar;
    private ProgressBar mMagicProgressBar;
    private Uri mCurrentChampionUri;
    private static final int CHAMPION_LOADER_OVERVIEW = 0;
    private static final String CHAMPION_URI_KEY = "CHAMPION_URI_KEY";
    private String SPLASH_ART_LAYOUT_MANAGER_STATE_KEY = "splashArtLayoutManagerStateKey";

    public ChampionOverviewFragment() {
    }

    public static ChampionOverviewFragment newInstance(Uri championUri) {

        ChampionOverviewFragment championOverviewFragment = new ChampionOverviewFragment();

        Bundle args = new Bundle();
        args.putParcelable(CHAMPION_URI_KEY, championUri);
        championOverviewFragment.setArguments(args);

        return championOverviewFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(LOG_TAG, "onCreate");

        Bundle args = getArguments();
        if (args != null && args.containsKey(CHAMPION_URI_KEY)) {
            mCurrentChampionUri = getArguments().getParcelable(CHAMPION_URI_KEY);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_champion_overview, container, false);
        Log.d(LOG_TAG, "onCreateView");

        mSplashArtRecyclerView = rootView.findViewById(R.id.splash_art_recycler_view);

        mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayout.HORIZONTAL, false);

        mAdapter = new SplashArtAdapter(getActivity(), new ArrayList<String>(), new ArrayList<String>());
        mSplashArtRecyclerView.setLayoutManager(mLayoutManager);
        mSplashArtRecyclerView.setHasFixedSize(true);
        mSplashArtRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mSplashArtRecyclerView.setAdapter(mAdapter);

        mChampionNameTv = rootView.findViewById(R.id.champion_name_tv);
        mChampionTitleTv = rootView.findViewById(R.id.champion_title_tv);
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

        if (mCurrentChampionUri == null) {
            Log.d(LOG_TAG, "ChampionUri is null");
        } else {
            getActivity().getSupportLoaderManager().initLoader(CHAMPION_LOADER_OVERVIEW, null, this);
        }

        mSplashArtRecyclerView.setNestedScrollingEnabled(false);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        String championId = String.valueOf(ContentUris.parseId(mCurrentChampionUri));

        final String[] PROJECTION = {
                Contract.ChampionEntry._ID,
                Contract.ChampionEntry.COLUMN_SPLASH_ART,
                Contract.ChampionEntry.COLUMN_SPLASH_ART_NAME,
                Contract.ChampionEntry.COLUMN_CHAMPION_NAME,
                Contract.ChampionEntry.COLUMN_CHAMPION_TITLE,
                Contract.ChampionEntry.COLUMN_DIFFICULTY,
                Contract.ChampionEntry.COLUMN_ATTACK,
                Contract.ChampionEntry.COLUMN_DEFENSE,
                Contract.ChampionEntry.COLUMN_MAGIC,
                Contract.ChampionEntry.COLUMN_ENEMY_TIPS,
                Contract.ChampionEntry.COLUMN_CHAMPION_LORE};

        return new CursorLoader(getActivity(),
                mCurrentChampionUri,
                PROJECTION,
                Contract.ChampionEntry._ID + DB_SIGN,
                new String[]{championId},
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }

        if (cursor.moveToFirst()) {
            String championSplashArtString = cursor.getString(cursor.getColumnIndex(Contract.ChampionEntry.COLUMN_SPLASH_ART));
            mSplashArtArray = Arrays.asList(championSplashArtString.split(DataUtils.STRING_DIVIDER));

            String splashArtNameString = cursor.getString(cursor.getColumnIndex(Contract.ChampionEntry.COLUMN_SPLASH_ART_NAME));
            mSplashArtNameArray = Arrays.asList(splashArtNameString.split(DataUtils.STRING_DIVIDER));

            mAdapter.setData(mSplashArtArray, mSplashArtNameArray);

            String championName = cursor.getString(cursor.getColumnIndex(Contract.ChampionEntry.COLUMN_CHAMPION_NAME));
            String championTitle = cursor.getString(cursor.getColumnIndex(Contract.ChampionEntry.COLUMN_CHAMPION_TITLE));
            String championLore = cursor.getString(cursor.getColumnIndex(Contract.ChampionEntry.COLUMN_CHAMPION_LORE));
            int championDifficulty = cursor.getInt(cursor.getColumnIndex(Contract.ChampionEntry.COLUMN_DIFFICULTY));
            int championAttack = cursor.getInt(cursor.getColumnIndex(Contract.ChampionEntry.COLUMN_ATTACK));
            int championDefense = cursor.getInt(cursor.getColumnIndex(Contract.ChampionEntry.COLUMN_DEFENSE));
            int championMagic = cursor.getInt(cursor.getColumnIndex(Contract.ChampionEntry.COLUMN_MAGIC));

            mChampionNameTv.setText(championName);
            mChampionTitleTv.setText(championTitle);
            mChampionLoreTv.setText(championLore);
            mDifficultyProgressBar.setProgress(championDifficulty);
            mAttackProgressBar.setProgress(championAttack);
            mDefenseProgressBar.setProgress(championDefense);
            mMagicProgressBar.setProgress(championMagic);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mChampionNameTv.setText("");
        mChampionTitleTv.setText("");
        mChampionLoreTv.setText("");
        mDifficultyProgressBar.setProgress(0);
        mAttackProgressBar.setProgress(0);
        mDefenseProgressBar.setProgress(0);
        mMagicProgressBar.setProgress(0);
        mAdapter.setData(null, null);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable(SPLASH_ART_LAYOUT_MANAGER_STATE_KEY, mSplashArtRecyclerView.getLayoutManager().onSaveInstanceState());
    }
}
