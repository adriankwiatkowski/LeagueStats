package com.example.android.leaguestats;

import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.leaguestats.adapters.SplashArtAdapter;
import com.example.android.leaguestats.database.Contract;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChampionDetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String LOG_TAG = ChampionDetailActivity.class.getSimpleName();
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
    private static final int CHAMPION_LOADER = 0;
    private Parcelable mLayoutManagerState;
    private final String LAYOUT_MANAGER_STATE_KEY = "layoutManagerKey";
    private final String STRING_DIVIDER = "_,_";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_champion_detail);

        mSplashArtRecyclerView = findViewById(R.id.splash_art_recycler_view);

        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        if (savedInstanceState != null && savedInstanceState.containsKey(LAYOUT_MANAGER_STATE_KEY)) {
            mLayoutManagerState = savedInstanceState.getParcelable(LAYOUT_MANAGER_STATE_KEY);
            mLayoutManager.onRestoreInstanceState(mLayoutManagerState);
        }

        mAdapter = new SplashArtAdapter(this, new ArrayList<String>(), new ArrayList<String>());
        mSplashArtRecyclerView.setLayoutManager(mLayoutManager);
        mSplashArtRecyclerView.setHasFixedSize(true);
        mSplashArtRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mSplashArtRecyclerView.setAdapter(mAdapter);

        mChampionNameTv = findViewById(R.id.champion_name_tv);
        mChampionTitleTv = findViewById(R.id.champion_title_tv);
        mChampionLoreTv = findViewById(R.id.champion_lore_tv);
        mDifficultyProgressBar = findViewById(R.id.difficulty_progress_bar);
        mAttackProgressBar = findViewById(R.id.attack_progress_bar);
        mDefenseProgressBar = findViewById(R.id.defense_progress_bar);
        mMagicProgressBar = findViewById(R.id.magic_progress_bar);

        Intent intent = getIntent();
        mCurrentChampionUri = intent.getData();

        if (mCurrentChampionUri == null) {
            Log.d(LOG_TAG, "ChampionUri is null");
        } else {
            getSupportLoaderManager().initLoader(CHAMPION_LOADER, null, this);
        }

        setupItemTouchHelper();
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

        return new CursorLoader(this,
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
            mSplashArtArray = Arrays.asList(championSplashArtString.split(STRING_DIVIDER));

            String splashArtNameString = cursor.getString(cursor.getColumnIndex(Contract.ChampionEntry.COLUMN_SPLASH_ART_NAME));
            mSplashArtNameArray = Arrays.asList(splashArtNameString.split(STRING_DIVIDER));

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
    }

    private void setupItemTouchHelper() {
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.UP | ItemTouchHelper.DOWN) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();

                if (direction == ItemTouchHelper.UP && position != (mSplashArtArray.size() - 1)) {
                    mLayoutManager.smoothScrollToPosition(mSplashArtRecyclerView, new RecyclerView.State(), position + 1);
                } else if (direction == ItemTouchHelper.DOWN && position != 0) {
                    mLayoutManager.smoothScrollToPosition(mSplashArtRecyclerView, new RecyclerView.State(), position - 1);
                }
            }


        }).attachToRecyclerView(mSplashArtRecyclerView);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        mLayoutManagerState = mLayoutManager.onSaveInstanceState();
        outState.putParcelable(LAYOUT_MANAGER_STATE_KEY, mLayoutManagerState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (savedInstanceState != null) {
            mLayoutManagerState = savedInstanceState.getParcelable(LAYOUT_MANAGER_STATE_KEY);
        }
    }

    public void onDetailsClick(View view) {
        Intent intent = new Intent(ChampionDetailActivity.this, ChampionStatsActivity.class);
        intent.setData(mCurrentChampionUri);
        startActivity(intent);
    }

    public void onStrategyClick(View view) {
        Intent intent = new Intent(ChampionDetailActivity.this, StrategyActivity.class);
        intent.setData(mCurrentChampionUri);
        startActivity(intent);
    }
}