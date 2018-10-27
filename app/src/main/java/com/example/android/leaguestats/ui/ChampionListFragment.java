package com.example.android.leaguestats.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.android.leaguestats.R;
import com.example.android.leaguestats.adapters.ChampionAdapter;
import com.example.android.leaguestats.data.database.entity.ChampionEntry;
import com.example.android.leaguestats.utilities.InjectorUtils;
import com.example.android.leaguestats.utilities.LeaguePreferences;
import com.example.android.leaguestats.viewmodels.ChampionModel;
import com.example.android.leaguestats.viewmodels.ChampionModelFactory;

import java.util.ArrayList;
import java.util.List;

public class ChampionListFragment extends Fragment implements ChampionAdapter.ChampionListener {

    public interface OnChampionSelected {
        void onChampionSelected(int id);
    }

    private static final String LOG_TAG = ChampionListFragment.class.getSimpleName();
    private OnChampionSelected mCallback;
    private RecyclerView mRecyclerView;
    private ChampionAdapter mAdapter;
    private ChampionModel mViewModel;
    private ProgressBar mIndicator;

    public ChampionListFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_list, container, false);

        mRecyclerView = rootView.findViewById(R.id.recycler_view);
        mIndicator = rootView.findViewById(R.id.indicator);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        int gridLayoutColumnCount;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            gridLayoutColumnCount = 3;
        } else {
            gridLayoutColumnCount = 2;
        }

        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), gridLayoutColumnCount));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        String patchVersion = LeaguePreferences.getPatchVersion(getContext());
        mAdapter = new ChampionAdapter(getContext(), new ArrayList<ChampionEntry>(), this, patchVersion);
        mRecyclerView.setAdapter(mAdapter);

        setupViewModel();
    }

    private void setupViewModel() {
        ChampionModelFactory factory = InjectorUtils.provideChampionModelFactory(getActivity().getApplicationContext());
        mViewModel =
                ViewModelProviders.of(getActivity(), factory).get(ChampionModel.class);
        mViewModel.getChampions().observe(getActivity(), new Observer<List<ChampionEntry>>() {
            @Override
            public void onChanged(@Nullable List<ChampionEntry> listChampionEntries) {
                Log.d(LOG_TAG, "Receiving database update from LiveData");
                mAdapter.setData(listChampionEntries);
                mIndicator.setVisibility(View.INVISIBLE);
            }
        });
    }

    @Override
    public void onChampionClick(ChampionEntry championEntry) {
        Fragment parentFragment = getParentFragment();
        boolean twoPane = parentFragment.getView().findViewById(R.id.master_fragment_detail_container) != null;
        if (twoPane) {
            MasterFragment masterFragment = (MasterFragment) getParentFragment();
            masterFragment.addChampionFragment();
        } else {
            mViewModel.initChampion(championEntry);
            mCallback.onChampionSelected(championEntry.getId());
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mCallback = (OnChampionSelected) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnChampionSelected");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallback = null;
    }
}
