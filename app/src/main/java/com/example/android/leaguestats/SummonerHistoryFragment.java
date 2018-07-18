package com.example.android.leaguestats;

import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.leaguestats.adapters.MatchAdapter;
import com.example.android.leaguestats.database.Contract;
import com.example.android.leaguestats.interfaces.MatchListTaskCompleted;
import com.example.android.leaguestats.interfaces.MatchTaskCompleted;
import com.example.android.leaguestats.interfaces.SummonerTaskCompleted;
import com.example.android.leaguestats.models.Match;
import com.example.android.leaguestats.models.MatchList;
import com.example.android.leaguestats.models.Summoner;
import com.example.android.leaguestats.utilities.AsyncTasks.MatchAsyncTask;
import com.example.android.leaguestats.utilities.AsyncTasks.MatchListAsyncTask;
import com.example.android.leaguestats.utilities.AsyncTasks.SummonerAsyncTask;
import com.example.android.leaguestats.utilities.DataUtils;
import com.example.android.leaguestats.utilities.PreferencesUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SummonerHistoryFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String LOG_TAG = SummonerHistoryFragment.class.getSimpleName();
    private RecyclerView mRecyclerView;
    private MatchAdapter mAdapter;
    private TextView mEmptyViewTv;
    private ProgressBar mRecyclerIndicator;
    private TextView mSummonerNameTv;
    private TextView mSummonerLevelTv;
    private ImageView mProfileIcon;
    private int mProfileIconId;
    private long mSummonerLevel;
    private String mSummonerName;
    private long mAccountId;
    private String mPatchVersion;
    private String mEntryRegionString;
    private String[] mSummonerSpellId1Array;
    private String[] mSummonerSpellId2Array;
    private String[] mChampionIdArray;
    private ArrayList<Match> mMatches;
    private String LAYOUT_MANAGER_STATE_KEY = "layoutManagerStateKey";
    private Parcelable mLayoutManagerViewState;
    public static final String ENTRY_REGION_STRING = "ENTRY_REGION_STRING";
    public static final String ACCOUNT_ID_KEY = "ACCOUNT_ID_KEY";
    private static final String SUMMONER_LEVEL_KEY = "SUMMONER_LEVEL_KEY";
    private static final String PROFILE_ICON_KEY = "PROFILE_ICON_KEY";
    public static final String SUMMONER_NAME_KEY = "SUMMONER_NAME_KEY";
    private static final int SUMMONER_SPELL_1_LOADER = 0;
    private static final int SUMMONER_SPELL_2_LOADER = 1;
    private static final int CHAMPION_LOADER = 2;

    public SummonerHistoryFragment() {
    }

    public static SummonerHistoryFragment newInstance(String entryUrlString, String summonerName) {
        SummonerHistoryFragment summonerHistoryFragment = new SummonerHistoryFragment();
        Bundle args = new Bundle();
        args.putString(ENTRY_REGION_STRING, entryUrlString);
        args.putString(SUMMONER_NAME_KEY, summonerName);
        summonerHistoryFragment.setArguments(args);
        return summonerHistoryFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(LOG_TAG, "onCreate");

        Bundle args = getArguments();
        if (args != null && args.containsKey(ENTRY_REGION_STRING) && args.containsKey(SUMMONER_NAME_KEY)) {
            mEntryRegionString = args.getString(ENTRY_REGION_STRING);
            mSummonerName = args.getString(SUMMONER_NAME_KEY);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_summoner, container, false);
        Log.i(LOG_TAG, "onCreateView");

        mRecyclerView = rootView.findViewById(R.id.summoner_recycler_view);

        mEmptyViewTv = rootView.findViewById(R.id.summoner_empty_view_tv);
        mSummonerNameTv = rootView.findViewById(R.id.summoner_name_tv);
        mSummonerLevelTv = rootView.findViewById(R.id.summoner_level_tv);
        mProfileIcon = rootView.findViewById(R.id.summoner_profile);
        mRecyclerIndicator = rootView.findViewById(R.id.summoner_recycler_indicator);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i(LOG_TAG, "onActivityCreated");

        mPatchVersion = PreferencesUtils.getPatchVersion(getContext());

        mAdapter = new MatchAdapter(getContext(), new ArrayList<Match>(), null, null, null, mPatchVersion);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(LAYOUT_MANAGER_STATE_KEY)) {
                mLayoutManagerViewState = savedInstanceState.getParcelable(LAYOUT_MANAGER_STATE_KEY);
                mRecyclerView.getLayoutManager().onRestoreInstanceState(mLayoutManagerViewState);
            }
        } else {
            Log.i(LOG_TAG, "savedInstance null");
        }
        getSummonerData(mEntryRegionString, mSummonerName);
    }

    private void getSummonerData(final String entryRegionString, final String summonerName) {
        if (isNetworkAvailable()) {
            SummonerTaskCompleted summonerTaskCompleted = new SummonerTaskCompleted() {
                @Override
                public void summonerTaskCompleted(Summoner summoner) {
                    if (summoner != null) {
                        mAccountId = summoner.getAccountId();
                        mProfileIconId = summoner.getProfileIconId();
                        mSummonerLevel = summoner.getSummonerLevel();
                        mSummonerName = summoner.getSummonerName();

                        mSummonerNameTv.setText(mSummonerName);
                        mSummonerLevelTv.setText(String.valueOf(mSummonerLevel) + " " + getString(R.string.level));
                        loadImage(mProfileIcon, mProfileIconId);

                        getMatchList(entryRegionString, String.valueOf(mAccountId));
                    } else {
                        Toast.makeText(getContext(), getString(R.string.summoner_not_found)
                                + " " + getString(R.string.check_spelling_and_region), Toast.LENGTH_LONG).show();
                        getActivity().onBackPressed();
                    }
                }
            };
            SummonerAsyncTask summonerAsyncTask = new SummonerAsyncTask(summonerTaskCompleted);
            summonerAsyncTask.execute(entryRegionString, summonerName);
        } else {
            Toast.makeText(getContext(), "No internet connection found.", Toast.LENGTH_LONG).show();
        }
    }

    private void getMatchList(final String entryRegionString, final String accountId) {
        MatchListTaskCompleted matchListTaskCompleted = new MatchListTaskCompleted() {
            @Override
            public void matchListTaskCompleted(ArrayList<MatchList> matchLists) {
                if (matchLists != null) {
                    if (!matchLists.isEmpty()) {
                        getMatches(entryRegionString, matchLists);
                    }
                } else {
                    mEmptyViewTv.setText(getString(R.string.no_matches_found));
                    mEmptyViewTv.setVisibility(View.VISIBLE);
                    mRecyclerIndicator.setVisibility(View.INVISIBLE);
                }
            }
        };
        MatchListAsyncTask matchListAsyncTask = new MatchListAsyncTask(matchListTaskCompleted);
        matchListAsyncTask.execute(entryRegionString, accountId);
    }

    private void getMatches(String entryRegionString, ArrayList<MatchList> matchLists) {
        MatchTaskCompleted matchTaskCompleted = new MatchTaskCompleted() {
            @Override
            public void matchTaskCompleted(ArrayList<Match> matches) {
                mMatches = matches;
                mAdapter.swapMatchesData(mMatches);
                mRecyclerIndicator.setVisibility(View.INVISIBLE);

                mSummonerSpellId1Array = new String[matches.size()];
                for (int i = 0; i < matches.size(); i++) {
                    mSummonerSpellId1Array[i] = String.valueOf(matches.get(i).getSpell1Id());
                }
                mSummonerSpellId2Array = new String[matches.size()];
                for (int i = 0; i < matches.size(); i++) {
                    mSummonerSpellId2Array[i] = String.valueOf(matches.get(i).getSpell2Id());
                }
                mChampionIdArray = new String[matches.size()];
                for (int i = 0; i < matches.size(); i++) {
                    mChampionIdArray[i] = String.valueOf(matches.get(i).getChampionId());
                }

                getActivity().getSupportLoaderManager().initLoader(SUMMONER_SPELL_1_LOADER, null, SummonerHistoryFragment.this);
                getActivity().getSupportLoaderManager().initLoader(SUMMONER_SPELL_2_LOADER, null, SummonerHistoryFragment.this);
                getActivity().getSupportLoaderManager().initLoader(CHAMPION_LOADER, null, SummonerHistoryFragment.this);
            }
        };
        MatchAsyncTask matchAsyncTask = new MatchAsyncTask(matchTaskCompleted);
        matchAsyncTask.execute(entryRegionString, String.valueOf(matchLists.get(0).getGameId()));
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        final String[] PROJECTION;
        String inClause;
        switch (id) {
            case SUMMONER_SPELL_1_LOADER:
                PROJECTION = new String[]{Contract.SummonerSpellEntry._ID,
                        Contract.SummonerSpellEntry.COLUMN_IMAGE};
                inClause = DataUtils.buildInClause(mSummonerSpellId1Array);
                return new CursorLoader(getContext(),
                        Contract.SummonerSpellEntry.CONTENT_URI,
                        PROJECTION,
                        Contract.SummonerSpellEntry._ID + inClause,
                        mSummonerSpellId1Array,
                        null);
            case SUMMONER_SPELL_2_LOADER:
                PROJECTION = new String[]{Contract.SummonerSpellEntry._ID,
                        Contract.SummonerSpellEntry.COLUMN_IMAGE};
                inClause = DataUtils.buildInClause(mSummonerSpellId2Array);
                return new CursorLoader(getContext(),
                        Contract.SummonerSpellEntry.CONTENT_URI,
                        PROJECTION,
                        Contract.SummonerSpellEntry._ID + inClause,
                        mSummonerSpellId2Array,
                        null);
            case CHAMPION_LOADER:
                 PROJECTION = new String[]{Contract.ChampionEntry._ID,
                         Contract.ChampionEntry.COLUMN_CHAMPION_THUMBNAIL};
                 inClause = DataUtils.buildInClause(mChampionIdArray);
                 return new CursorLoader(getContext(),
                         Contract.ChampionEntry.CONTENT_URI,
                         PROJECTION,
                         Contract.ChampionEntry._ID + inClause,
                         mChampionIdArray,
                         null);
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
        switch (loader.getId()) {
            case SUMMONER_SPELL_1_LOADER:
                mAdapter.swapSummonerSpell1Data(cursor);
                break;
            case SUMMONER_SPELL_2_LOADER:
                mAdapter.swapSummonerSpell2Data(cursor);
                break;
            case CHAMPION_LOADER:
                mAdapter.swapChampionCursor(cursor);
                break;
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i(LOG_TAG, "onSaveInstanceState");

        mLayoutManagerViewState = mRecyclerView.getLayoutManager().onSaveInstanceState();
        outState.putParcelable(LAYOUT_MANAGER_STATE_KEY, mLayoutManagerViewState);
        outState.putString(SUMMONER_NAME_KEY, mSummonerName);
        outState.putLong(SUMMONER_LEVEL_KEY, mSummonerLevel);
        outState.putInt(PROFILE_ICON_KEY, mProfileIconId);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(LOG_TAG, "onDetach");
        mRecyclerView.setAdapter(null);
    }

    private void loadImage(ImageView imageView, int profileIconId) {
        Picasso.get()
                .load("http://ddragon.leagueoflegends.com/cdn/" + mPatchVersion + "/img/profileicon/" + String.valueOf(profileIconId) + ".png")
                .resize(200, 200)
                .error(R.drawable.ic_launcher_background)
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(imageView);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = ((ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE));
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
