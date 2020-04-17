package com.example.android.leaguestats.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.leaguestats.R;
import com.example.android.leaguestats.adapters.MatchAdapter;
import com.example.android.leaguestats.models.Match;
import com.example.android.leaguestats.models.Resource;
import com.example.android.leaguestats.models.Status;
import com.example.android.leaguestats.utilities.DataUtils;
import com.example.android.leaguestats.utilities.InjectorUtils;
import com.example.android.leaguestats.utilities.LeaguePreferences;
import com.example.android.leaguestats.viewmodels.SummonerModel;
import com.example.android.leaguestats.viewmodels.SummonerModelFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
    private SummonerModel mSummonerModel;
    private final int DEFAULT_POSITION = -1;
    private int mLastExpandablePosition = DEFAULT_POSITION;
    private String mPatchVersion;

    public SummonerHistoryFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_summoner_history, container, false);
        mExpandableListView = rootView.findViewById(R.id.summoner_expandable_list);
        mEmptyViewTv = rootView.findViewById(R.id.summoner_empty_view_tv);
        mRecyclerIndicator = rootView.findViewById(R.id.summoner_recycler_indicator);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mPatchVersion = LeaguePreferences.getPatchVersion(getContext());
        mAdapter = new MatchAdapter(getContext(), new ArrayList<Match>(), mPatchVersion);
        mExpandableListView.setAdapter(mAdapter);

        mExpandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                Log.d(LOG_TAG, "onGroupExpand");
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
                Match match = mAdapter.getGroup(groupPosition);
                String summonerName = match.getParticipantIdentities().get(childPosition).getPlayer().getSummonerName();
                String entryUrlString = DataUtils.getEntryUrl(match.getPlatformId());
                mCallback.onHistorySummonerListener(entryUrlString, summonerName);
                return false;
            }
        });

        ViewCompat.setNestedScrollingEnabled(mExpandableListView, true);

        setupViewModel();
    }

    private void setupViewModel() {
        SummonerModelFactory factory =
                InjectorUtils.provideSummonerModelFactory(getActivity().getApplicationContext());
        mSummonerModel = ViewModelProviders.of(getActivity(), factory).get(SummonerModel.class);
        mSummonerModel.getMatches().observe(this, new Observer<Resource<List<Resource<Match>>>>() {
            @Override
            public void onChanged(@Nullable Resource<List<Resource<Match>>> matchListResource) {
                Log.d(LOG_TAG, "Getting matches");
                updateUi(matchListResource);
            }
        });
    }

    private void updateUi(@Nullable Resource<List<Resource<Match>>> matchListResource) {
        if (matchListResource == null) {
            Log.d(LOG_TAG, "MatchListResource null");
            return;
        }
        switch (matchListResource.status) {
            case LOADING:
                mRecyclerIndicator.setVisibility(View.VISIBLE);
                mExpandableListView.setVisibility(View.INVISIBLE);
                mEmptyViewTv.setVisibility(View.INVISIBLE);
                mEmptyViewTv.setText("");
                mAdapter.clear();
                break;
            case SUCCESS:
                mRecyclerIndicator.setVisibility(View.INVISIBLE);
                mExpandableListView.setVisibility(View.VISIBLE);
                mEmptyViewTv.setVisibility(View.INVISIBLE);

                String highestAchievedSeasonTier = "";
                List<Resource<Match>> matchResourceList = matchListResource.data;
                if (matchResourceList != null && !matchResourceList.isEmpty()) {
                    List<Match> matchList = new ArrayList<>();
                    for (int i = 0; i < matchResourceList.size(); i++) {
                        if (matchResourceList.get(i).status == Status.SUCCESS
                                && matchResourceList.get(i).data != null) {
                            matchList.add(matchResourceList.get(i).data);
                        }
                    }
                    Log.d(LOG_TAG, "Match list size: " + matchList.size() + ".");
                    if (!matchList.isEmpty()) {
                        Collections.sort(matchList, new Comparator<Match>() {
                            @Override
                            public int compare(Match o1, Match o2) {
                                return (int) (o2.getGameCreation() - o1.getGameCreation());
                            }
                        });
                        mAdapter.setData(matchList);
                        for (int i = 0; i < matchList.get(0).getParticipantIdentities().size(); i++) {
                            if (matchList.get(0).getCurrentSummonerId().equals(matchList.get(0).getParticipantIdentities().get(i).getPlayer().getSummonerId())) {
                                highestAchievedSeasonTier = matchList.get(0).getParticipants().get(i).getHighestAchievedSeasonTier();
                                break;
                            }
                        }
                    }
                }
                mCallback.onHighestRank(highestAchievedSeasonTier);
                break;
            case ERROR:
                mRecyclerIndicator.setVisibility(View.INVISIBLE);
                mExpandableListView.setVisibility(View.INVISIBLE);
                mAdapter.clear();
                mEmptyViewTv.setVisibility(View.VISIBLE);
                mEmptyViewTv.setText(getString(R.string.no_matches_found));
                break;
        }
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
        mCallback = null;
    }
}
