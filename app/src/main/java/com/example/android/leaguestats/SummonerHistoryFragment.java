package com.example.android.leaguestats;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Parcelable;
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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.leaguestats.ViewModels.ChampionViewModelShared;
import com.example.android.leaguestats.ViewModels.ChampionViewModelSharedFactory;
import com.example.android.leaguestats.ViewModels.SummonerSpellSharedViewModel;
import com.example.android.leaguestats.ViewModels.SummonerSpellSharedViewModelFactory;
import com.example.android.leaguestats.adapters.MatchHistoryAdapter;
import com.example.android.leaguestats.interfaces.MatchListTaskCompleted;
import com.example.android.leaguestats.interfaces.MatchTaskCompleted;
import com.example.android.leaguestats.interfaces.SummonerTaskCompleted;
import com.example.android.leaguestats.models.Match;
import com.example.android.leaguestats.models.MatchList;
import com.example.android.leaguestats.models.Summoner;
import com.example.android.leaguestats.room.AppDatabase;
import com.example.android.leaguestats.room.ChampionEntry;
import com.example.android.leaguestats.room.SummonerSpellEntry;
import com.example.android.leaguestats.utilities.AsyncTasks.MatchAsyncTask;
import com.example.android.leaguestats.utilities.AsyncTasks.MatchListAsyncTask;
import com.example.android.leaguestats.utilities.AsyncTasks.SummonerAsyncTask;
import com.example.android.leaguestats.utilities.PreferencesUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class SummonerHistoryFragment extends Fragment {

    private static final String LOG_TAG = SummonerHistoryFragment.class.getSimpleName();
    private RecyclerView mRecyclerView;
    private MatchHistoryAdapter mAdapter;
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
    private ArrayList<Match> mMatches;
    private SummonerSpellSharedViewModel mSummonerSpellViewModel;
    private ChampionViewModelShared mChampionViewModel;
    private AppDatabase mDb;
    private String LAYOUT_MANAGER_STATE_KEY = "layoutManagerStateKey";
    private Parcelable mLayoutManagerViewState;
    public static final String ENTRY_REGION_STRING = "ENTRY_REGION_STRING";
    private static final String SUMMONER_LEVEL_KEY = "SUMMONER_LEVEL_KEY";
    private static final String PROFILE_ICON_KEY = "PROFILE_ICON_KEY";
    public static final String SUMMONER_NAME_KEY = "SUMMONER_NAME_KEY";

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
        Log.d(LOG_TAG, "onCreate");

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
        Log.d(LOG_TAG, "onCreateView");

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
        Log.d(LOG_TAG, "onActivityCreated");

        mPatchVersion = PreferencesUtils.getPatchVersion(getContext());

        mAdapter = new MatchHistoryAdapter(getContext(), new ArrayList<Match>(),
                new ArrayList<ChampionEntry>(), new ArrayList<SummonerSpellEntry>(),
                new ArrayList<SummonerSpellEntry>(), mPatchVersion);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mDb = AppDatabase.getInstance(getActivity().getApplicationContext());

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(LAYOUT_MANAGER_STATE_KEY)) {
                mLayoutManagerViewState = savedInstanceState.getParcelable(LAYOUT_MANAGER_STATE_KEY);
                mRecyclerView.getLayoutManager().onRestoreInstanceState(mLayoutManagerViewState);

                mSummonerName = savedInstanceState.getString(SUMMONER_NAME_KEY);
                mSummonerLevel = savedInstanceState.getLong(SUMMONER_LEVEL_KEY);
                mProfileIconId = savedInstanceState.getInt(PROFILE_ICON_KEY);
                bindDataToViews();
            }
        } else {
            Log.d(LOG_TAG, "savedInstance null");
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
                        bindDataToViews();

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
                updateUi(matches);
            }
        };
        MatchAsyncTask matchAsyncTask = new MatchAsyncTask(matchTaskCompleted);
        matchAsyncTask.execute(entryRegionString, String.valueOf(matchLists.get(0).getGameId()));
    }

    private void updateUi(ArrayList<Match> matches) {
        mAdapter.setMatches(mMatches);
        mRecyclerIndicator.setVisibility(View.INVISIBLE);

        setupViewModels(matches);
    }

    private void setupViewModels(List<Match> matches) {
        int[] summonerSpell1Array = listToIntArray(matches.get(0).getSpell1Id());
        int[] summonerSpell2Array = listToIntArray(matches.get(0).getSpell2Id());
        int[] championIdArray = listToIntArray(matches.get(0).getChampionId());

        Log.d(LOG_TAG, "Getting summonerSpell");
        SummonerSpellSharedViewModelFactory summonerSpellFactory = new SummonerSpellSharedViewModelFactory(mDb);
        mSummonerSpellViewModel = ViewModelProviders.of(getActivity(), summonerSpellFactory).get(SummonerSpellSharedViewModel.class);
        mSummonerSpellViewModel.setSummonerSpell1(summonerSpell1Array);
        mSummonerSpellViewModel.setSummonerSpell2(summonerSpell2Array);
        mSummonerSpellViewModel.getSummonerSpell1().observe(this, new Observer<List<SummonerSpellEntry>>() {
            @Override
            public void onChanged(@Nullable List<SummonerSpellEntry> summonerSpellEntries) {
                Log.d(LOG_TAG, "Receiving database update from LiveData for summonerSpell1");
                mSummonerSpellViewModel.getSummonerSpell1().removeObserver(this);
                mAdapter.setSummonerSpell1(summonerSpellEntries);
            }
        });
        mSummonerSpellViewModel.getSummonerSpell2().observe(this, new Observer<List<SummonerSpellEntry>>() {
            @Override
            public void onChanged(@Nullable List<SummonerSpellEntry> summonerSpellEntries) {
                Log.d(LOG_TAG, "Receiving database update from LiveData for summonerSpell2");
                mSummonerSpellViewModel.getSummonerSpell2().removeObserver(this);
                mAdapter.setSummonerSpell2(summonerSpellEntries);
            }
        });

        Log.d(LOG_TAG, "Getting champion");
        ChampionViewModelSharedFactory championFactory = new ChampionViewModelSharedFactory(mDb);
        mChampionViewModel = ViewModelProviders.of(getActivity(), championFactory).get(ChampionViewModelShared.class);
        mChampionViewModel.setHistoryChampions(championIdArray);
        mChampionViewModel.getHistoryChampions().observe(this, new Observer<List<ChampionEntry>>() {
            @Override
            public void onChanged(@Nullable List<ChampionEntry> championEntries) {
                Log.d(LOG_TAG, "Receiving database update from LiveData for champions");
                mChampionViewModel.getHistoryChampions().removeObserver(this);
                mAdapter.setChampions(championEntries);
            }
        });
    }

    private int[] listToIntArray(List<Integer> list) {
        int[] intArray = new int[list.size()];
        for (int i = 0; i < intArray.length; i++) {
            intArray[i] = list.get(i);
        }
        return intArray;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(LOG_TAG, "onSaveInstanceState");

        mLayoutManagerViewState = mRecyclerView.getLayoutManager().onSaveInstanceState();
        outState.putParcelable(LAYOUT_MANAGER_STATE_KEY, mLayoutManagerViewState);
        outState.putString(SUMMONER_NAME_KEY, mSummonerName);
        outState.putLong(SUMMONER_LEVEL_KEY, mSummonerLevel);
        outState.putInt(PROFILE_ICON_KEY, mProfileIconId);
    }

    private void bindDataToViews() {
        mSummonerNameTv.setText(mSummonerName);
        mSummonerLevelTv.setText(getString(R.string.level) + " " + String.valueOf(mSummonerLevel));
        Picasso.get()
                .load("http://ddragon.leagueoflegends.com/cdn/" + mPatchVersion + "/img/profileicon/" + String.valueOf(mProfileIconId) + ".png")
                .resize(200, 200)
                .error(R.drawable.ic_launcher_background)
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(mProfileIcon);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = ((ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE));
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
