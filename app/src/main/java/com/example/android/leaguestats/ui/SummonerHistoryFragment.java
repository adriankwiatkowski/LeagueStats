package com.example.android.leaguestats.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.leaguestats.R;
import com.example.android.leaguestats.adapters.MatchAdapter;
import com.example.android.leaguestats.models.Match;
import com.example.android.leaguestats.utilities.DataUtils;
import com.example.android.leaguestats.utilities.InjectorUtils;
import com.example.android.leaguestats.utilities.LeaguePreferences;
import com.example.android.leaguestats.viewmodels.HistoryModel;
import com.example.android.leaguestats.viewmodels.HistoryModelFactory;

import java.util.ArrayList;
import java.util.List;

public class SummonerHistoryFragment extends Fragment {

    private HistoryListener mCallback;

    public interface HistoryListener {
        void onHistorySummonerListener(String entryUrlString, String summonerName);
        void onHighestRank(String highestAchievedSeasonTier);
    }

    private static final String LOG_TAG = SummonerHistoryFragment.class.getSimpleName();
    private ExpandableListView mExpandableListView;
    private MatchAdapter mAdapter;
    private TextView mEmptyViewTv;
    private ProgressBar mRecyclerIndicator;
    private String mPatchVersion;
    private HistoryModel mHistoryModel;
    private final int DEFAULT_POSITION = -1;
    private int mLastExpandablePosition = DEFAULT_POSITION;
    private String LAYOUT_MANAGER_STATE_KEY = "layoutManagerStateKey";

    public SummonerHistoryFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_summoner_history, container, false);
        Log.d(LOG_TAG, "onCreateView");

        mExpandableListView = rootView.findViewById(R.id.summoner_expandable_list);
        mEmptyViewTv = rootView.findViewById(R.id.summoner_empty_view_tv);
        mRecyclerIndicator = rootView.findViewById(R.id.summoner_recycler_indicator);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(LOG_TAG, "onActivityCreated");

        mPatchVersion = LeaguePreferences.getPatchVersion(getContext());

        mAdapter = new MatchAdapter(getContext(), new ArrayList<Match>(), mPatchVersion);
        mExpandableListView.setAdapter(mAdapter);
        mExpandableListView.setDividerHeight(8);

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(LAYOUT_MANAGER_STATE_KEY)) {
                mExpandableListView.onRestoreInstanceState(
                        savedInstanceState.getParcelable(LAYOUT_MANAGER_STATE_KEY));
            }
        }

        mExpandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                if (mLastExpandablePosition != DEFAULT_POSITION &&
                        groupPosition != mLastExpandablePosition) {
                    mExpandableListView.collapseGroup(groupPosition);
                }
                mLastExpandablePosition = groupPosition;
            }
        });

        mExpandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                if (v.getId() == R.id.match_summoner_name_child_tv) {
                    Match match = mAdapter.getGroup(groupPosition);
                    String summonerName = match.getSummonerName().get(childPosition);
                    String entryUrlString = DataUtils.getEntryUrl(match.getPlatformId());
                    mCallback.onHistorySummonerListener(entryUrlString, summonerName);
                }
                return false;
            }
        });

        setupViewModel();
    }

    private void setupViewModel() {
        HistoryModelFactory factory =
                InjectorUtils.provideHistoryModelFactory(getActivity().getApplicationContext());
        mHistoryModel = ViewModelProviders.of(getActivity(), factory).get(HistoryModel.class);
        mHistoryModel.getMatches().observe(getActivity(), new Observer<List<Match>>() {
            @Override
            public void onChanged(@Nullable List<Match> matches) {
                updateUi(matches);
            }
        });
    }

    private void updateUi(@Nullable List<Match> matches) {
        mRecyclerIndicator.setVisibility(View.INVISIBLE);
        if (matches != null && !matches.isEmpty()) {
            mExpandableListView.setVisibility(View.VISIBLE);
            mAdapter.setData(matches);
            String highestAchievedSeasonTier = matches.get(0).getHighestAchievedSeasonTier();
            mCallback.onHighestRank(highestAchievedSeasonTier);
        } else {
            mEmptyViewTv.setText(getString(R.string.no_matches_found));
            mEmptyViewTv.setVisibility(View.VISIBLE);
        }
    }

    // Called from SummonerActivity.
    public void showHistoryIndicator() {
        Log.d(LOG_TAG, "showHistoryIndicator");

        mRecyclerIndicator.setVisibility(View.VISIBLE);
        mEmptyViewTv.setVisibility(View.INVISIBLE);
        mExpandableListView.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mCallback = (HistoryListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement HistoryListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

        mExpandableListView.setAdapter((ExpandableListAdapter) null);
        //mExpandableListView.setAdapter(null);
        mCallback = null;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable(LAYOUT_MANAGER_STATE_KEY, mExpandableListView.onSaveInstanceState());
    }
}
