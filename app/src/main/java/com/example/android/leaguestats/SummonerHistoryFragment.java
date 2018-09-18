package com.example.android.leaguestats;

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
import android.widget.ExpandableListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.leaguestats.adapters.ExpandableMatchAdapter;
import com.example.android.leaguestats.models.Match;
import com.example.android.leaguestats.utilities.InjectorUtils;
import com.example.android.leaguestats.utilities.PreferencesUtils;
import com.example.android.leaguestats.viewModels.HistoryModel;
import com.example.android.leaguestats.viewModels.HistoryModelFactory;

import java.util.ArrayList;
import java.util.List;

public class SummonerHistoryFragment extends Fragment {

    private HistoryListener mCallback;

    public interface HistoryListener {
        void onHistoryChampionListener(String championId);
    }

    private static final String LOG_TAG = SummonerHistoryFragment.class.getSimpleName();
    private ExpandableListView mExpandableListView;
    private ExpandableMatchAdapter mAdapter;
    private TextView mEmptyViewTv;
    private ProgressBar mRecyclerIndicator;
    private String mPatchVersion;
    private long mSummonerId;
    private HistoryModel mHistoryModel;
    private final String SUMMONER_ID_KEY = "SUMMONER_ID_KEY";

    public SummonerHistoryFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(LOG_TAG, "onCreate");
    }

    // TODO This is example. Do some documentation :).

    /**
     * Called to have the fragment instantiate its user interface view.
     * This is optional, and non-graphical fragments can return null (which
     * is the default implementation).  This will be called between
     * {@link #onCreate(Bundle)} and {@link #onActivityCreated(Bundle)}.
     * <p>
     * <p>If you return a View from here, you will later be called in
     * {@link #onDestroyView} when the view is being released.
     *
     * @param inflater           The LayoutInflater object that can be used to inflate
     *                           any views in the fragment,
     * @param container          If non-null, this is the parent view that the fragment's
     *                           UI should be attached to.  The fragment should not add the view itself,
     *                           but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     *                           from a previous saved state as given here.
     * @return Return the View for the fragment's UI, or null.
     */

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

        mPatchVersion = PreferencesUtils.getPatchVersion(getContext());

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(SUMMONER_ID_KEY)) {
                mSummonerId = savedInstanceState.getLong(SUMMONER_ID_KEY);
            }
        }

        mAdapter = new ExpandableMatchAdapter(getContext(), new ArrayList<Match>(), mPatchVersion);
        mExpandableListView.setAdapter(mAdapter);

        mExpandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                if (parent.isGroupExpanded(groupPosition)) {
                    Toast.makeText(getContext(), "is going to collapse", Toast.LENGTH_SHORT).show();
                    v.findViewById(R.id.match_champion_header_image).setVisibility(View.VISIBLE);
                    v.findViewById(R.id.match_summoner_spell_1_header_image).setVisibility(View.VISIBLE);
                    v.findViewById(R.id.match_summoner_spell_2_header_image).setVisibility(View.VISIBLE);
                    v.findViewById(R.id.match_summoner_stats_header_tv).setVisibility(View.GONE);
                } else {
                    Toast.makeText(getContext(), "is going to expand", Toast.LENGTH_SHORT).show();
                    v.findViewById(R.id.match_champion_header_image).setVisibility(View.GONE);
                    v.findViewById(R.id.match_summoner_spell_1_header_image).setVisibility(View.GONE);
                    v.findViewById(R.id.match_summoner_spell_2_header_image).setVisibility(View.GONE);
                    v.findViewById(R.id.match_summoner_stats_header_tv).setVisibility(View.GONE);
                }
                return false;
            }
        });

        mExpandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                switch (v.getId()) {
                    case R.id.match_champion_child_image:
                        break;
                }
                return false;
            }
        });

        setupViewModel();
    }

    private void setupViewModel() {

        if (mHistoryModel != null) {
            observeHistoryModel();
            return;
        }

        HistoryModelFactory factory =
                InjectorUtils.provideHistoryModelFactory(getActivity().getApplicationContext());
        mHistoryModel = ViewModelProviders.of(getActivity(), factory).get(HistoryModel.class);

        observeHistoryModel();
    }

    // Called from SummonerActivity.
    public void initHistoryData(String entryUrlString, long accountId, long summonerId) {
        Log.d(LOG_TAG, "initHistoryData");

        mSummonerId = summonerId;

        mRecyclerIndicator.setVisibility(View.VISIBLE);

        mHistoryModel.initHistoryMatches(entryUrlString, accountId);

        setupViewModel();
    }

    private void observeHistoryModel() {
        if (mHistoryModel.getMatches() != null) {
            if (!mHistoryModel.getMatches().hasObservers()) {
                mHistoryModel.getMatches().observe(getActivity(), new Observer<List<Match>>() {
                    @Override
                    public void onChanged(@Nullable List<Match> matches) {
                        Log.d(LOG_TAG, "Receiving update");
                        updateUi(matches);
                    }
                });
            }
        }
    }

    private void updateUi(List<Match> matches) {
        mRecyclerIndicator.setVisibility(View.INVISIBLE);
        if (matches != null && !matches.isEmpty()) {
            mAdapter.setData(matches, mSummonerId);
        } else {
            mEmptyViewTv.setText(getString(R.string.no_matches_found));
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

        //mExpandableListView.setAdapter(null);
        mCallback = null;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putLong(SUMMONER_ID_KEY, mSummonerId);
    }
}
