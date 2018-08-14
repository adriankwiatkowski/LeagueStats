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
import com.example.android.leaguestats.adapters.MasteryAdapter;
import com.example.android.leaguestats.interfaces.MasteryTaskCompleted;
import com.example.android.leaguestats.interfaces.SummonerTaskCompleted;
import com.example.android.leaguestats.models.Mastery;
import com.example.android.leaguestats.models.Summoner;
import com.example.android.leaguestats.room.AppDatabase;
import com.example.android.leaguestats.room.ChampionEntry;
import com.example.android.leaguestats.utilities.AsyncTasks.MasteryAsyncTask;
import com.example.android.leaguestats.utilities.PreferencesUtils;
import com.example.android.leaguestats.utilities.AsyncTasks.SummonerAsyncTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class SummonerMasteryFragment extends Fragment
        implements MasteryAdapter.ListItemClickListener {

    OnChampionClickListener mCallback;

    public interface OnChampionClickListener {
        void onChampionClick(long championId);
    }

    private static final String LOG_TAG = SummonerMasteryFragment.class.getSimpleName();
    private RecyclerView mRecyclerView;
    private MasteryAdapter mAdapter;
    private String mPatchVersion;
    private TextView mEmptyViewTv;
    private ProgressBar mRecyclerIndicator;
    private TextView mSummonerNameTv;
    private TextView mSummonerLevelTv;
    private ImageView mProfileIcon;
    private int mProfileIconId;
    private long mSummonerLevel;
    private long mSummonerId;
    private String mSummonerName;
    private String mEntryRegionString;
    private ArrayList<Mastery> mMasteryList;
    private String LAYOUT_MANAGER_STATE_KEY = "layoutManagerStateKey";
    private Parcelable mLayoutManagerViewState;
    private final String PARCEL_MASTERY = "PARCEL_MASTERY";
    public static final String SUMMONER_NAME_KEY = "SUMMONER_NAME_KEY";
    public static final String ENTRY_REGION_STRING = "ENTRY_REGION_STRING";
    private static final String SUMMONER_LEVEL_KEY = "SUMMONER_LEVEL_KEY";
    private static final String PROFILE_ICON_KEY = "PROFILE_ICON_KEY";
    private AppDatabase mDb;
    private ChampionViewModelShared mChampionViewModel;

    public SummonerMasteryFragment() {
    }

    public static SummonerMasteryFragment newInstance(String summonerName, String entryRegionString) {
        SummonerMasteryFragment summonerMasteryFragment = new SummonerMasteryFragment();
        Bundle args = new Bundle();
        args.putString(SUMMONER_NAME_KEY, summonerName);
        args.putString(ENTRY_REGION_STRING, entryRegionString);
        summonerMasteryFragment.setArguments(args);
        return summonerMasteryFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(LOG_TAG, "onCreate");

        Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey(SUMMONER_NAME_KEY) && bundle.containsKey(ENTRY_REGION_STRING)) {
            mSummonerName = bundle.getString(SUMMONER_NAME_KEY);
            mEntryRegionString = bundle.getString(ENTRY_REGION_STRING);
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

        mAdapter = new MasteryAdapter(getContext(), new ArrayList<Mastery>(), SummonerMasteryFragment.this, mPatchVersion);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mDb = AppDatabase.getInstance(getActivity().getApplicationContext());

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(LAYOUT_MANAGER_STATE_KEY)) {
                mLayoutManagerViewState = savedInstanceState.getParcelable(LAYOUT_MANAGER_STATE_KEY);
                mRecyclerView.getLayoutManager().onRestoreInstanceState(mLayoutManagerViewState);
            }

            if (savedInstanceState.containsKey(PARCEL_MASTERY)) {
                mMasteryList = savedInstanceState.getParcelableArrayList(PARCEL_MASTERY);
                mAdapter.swapData(mMasteryList);

                mSummonerName = savedInstanceState.getString(SUMMONER_NAME_KEY);
                mSummonerLevel = savedInstanceState.getLong(SUMMONER_LEVEL_KEY);
                mProfileIconId = savedInstanceState.getInt(PROFILE_ICON_KEY);
                bindDataToViews();
            } else {
                getSummonerData(mSummonerName, mEntryRegionString);
            }
        } else {
            Log.i(LOG_TAG, "savedInstance null");
            getSummonerData(mSummonerName, mEntryRegionString);
        }

    }

    private void getSummonerData(String summonerName, String entryUrlString) {
        if (isNetworkAvailable()) {
            SummonerTaskCompleted summonerTaskCompleted = new SummonerTaskCompleted() {
                @Override
                public void summonerTaskCompleted(Summoner summoner) {
                    if (summoner != null) {
                        mProfileIconId = summoner.getProfileIconId();
                        mSummonerLevel = summoner.getSummonerLevel();
                        mSummonerId = summoner.getSummonerId();
                        mSummonerName = summoner.getSummonerName();

                        bindDataToViews();

                        getMasteryData(mEntryRegionString, String.valueOf(mSummonerId));
                    } else {
                        Toast.makeText(getContext(), getString(R.string.summoner_not_found)
                                + " " + getString(R.string.check_spelling_and_region), Toast.LENGTH_LONG).show();
                        getActivity().onBackPressed();
                    }
                }
            };
            SummonerAsyncTask summonerAsyncTask = new SummonerAsyncTask(summonerTaskCompleted);
            summonerAsyncTask.execute(entryUrlString, summonerName);
        } else {
            Toast.makeText(getContext(), "No internet connection found.", Toast.LENGTH_LONG).show();
        }
    }

    private void getMasteryData(String summonerRegion, String summonerId) {
        mRecyclerIndicator.setVisibility(View.VISIBLE);
        MasteryTaskCompleted masteryTaskCompleted = new MasteryTaskCompleted() {
            @Override
            public void masteryTaskCompleted(final List<Mastery> masteries) {
                if (!(masteries == null || masteries.isEmpty())) {
                    ChampionViewModelSharedFactory factory = new ChampionViewModelSharedFactory(mDb);
                    mChampionViewModel = ViewModelProviders.of(getActivity(), factory).get(ChampionViewModelShared.class);
                    long[] ids = new long[masteries.size()];
                    for (int i = 0; i < masteries.size(); i++) {
                        ids[i] = masteries.get(i).getChampionId();
                    }
                    mChampionViewModel.setMasteryChampions(ids);
                    mChampionViewModel.getMasteryChampions().observe(SummonerMasteryFragment.this, new Observer<List<ChampionEntry>>() {
                        @Override
                        public void onChanged(@Nullable List<ChampionEntry> championEntries) {
                            mChampionViewModel.getMasteryChampions().removeObserver(this);
                            Log.d(LOG_TAG, "Receiving database update from LiveData");
                            updateUi(championEntries, masteries);
                        }
                    });
                } else {
                    mEmptyViewTv.setText(getString(R.string.no_masteries_found));
                    mEmptyViewTv.setVisibility(View.VISIBLE);
                    mRecyclerIndicator.setVisibility(View.INVISIBLE);
                }
            }
        };
        MasteryAsyncTask masteriesAsyncTask = new MasteryAsyncTask(masteryTaskCompleted);
        masteriesAsyncTask.execute(summonerRegion, summonerId);
    }

    private void updateUi(List<ChampionEntry> championEntries, List<Mastery> masteries) {
        List<Mastery> masteryList = new ArrayList<>();

        for (int i = 0; i < masteries.size(); i++) {

            String championName = "";
            String championThumbnail = "";
            long championId = 0;
            int championLevel = 0;
            int championPoints = 0;
            long lastPlayTime = 0;
            boolean isChestGranted = false;

            // Find champion for given id.
            for (int j = 0; j < championEntries.size(); j++) {
                if (masteries.get(i).getChampionId() == championEntries.get(j).getId()) {
                    championName = championEntries.get(j).getName();
                    championThumbnail = championEntries.get(j).getThumbnail();
                    championId = masteries.get(i).getChampionId();
                    championLevel = masteries.get(i).getChampionLevel();
                    championPoints = masteries.get(i).getChampionPoints();
                    lastPlayTime = masteries.get(i).getLastPlayTime();
                    isChestGranted = masteries.get(i).isChestGranted();
                }
            }
            masteryList.add(new Mastery(championName, championThumbnail, championId,
                    championLevel, championPoints, lastPlayTime, isChestGranted));
        }
        mAdapter.swapData(masteryList);
        mRecyclerIndicator.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onListItemClick(long championId) {
        Log.d(LOG_TAG, "onListItemClick");
        mCallback.onChampionClick(championId);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        mLayoutManagerViewState = mRecyclerView.getLayoutManager().onSaveInstanceState();
        outState.putParcelable(LAYOUT_MANAGER_STATE_KEY, mLayoutManagerViewState);
        outState.putParcelableArrayList(PARCEL_MASTERY, mMasteryList);
        outState.putString(SUMMONER_NAME_KEY, mSummonerName);
        outState.putLong(SUMMONER_LEVEL_KEY, mSummonerLevel);
        outState.putInt(PROFILE_ICON_KEY, mProfileIconId);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mCallback = (OnChampionClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnChampionClickListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(LOG_TAG, "onDetach");
        mRecyclerView.setAdapter(null);
        mCallback = null;
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
