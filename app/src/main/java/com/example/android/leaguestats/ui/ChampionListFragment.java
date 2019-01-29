package com.example.android.leaguestats.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.android.leaguestats.R;
import com.example.android.leaguestats.adapters.ChampionAdapter;
import com.example.android.leaguestats.interfaces.IdClickListener;
import com.example.android.leaguestats.models.Champion;
import com.example.android.leaguestats.utilities.InjectorUtils;
import com.example.android.leaguestats.utilities.LeaguePreferences;
import com.example.android.leaguestats.viewmodels.ChampionModel;
import com.example.android.leaguestats.viewmodels.ChampionModelFactory;

import java.util.List;

public class ChampionListFragment extends Fragment implements IdClickListener {

    public interface OnChampionSelected {
        void onChampionSelected(int id);
    }

    private static final String LOG_TAG = ChampionListFragment.class.getSimpleName();
    private OnChampionSelected mCallback;
    private RecyclerView mRecyclerView;
    private ChampionAdapter mAdapter;
    private ChampionModel mViewModel;
    private ProgressBar mIndicator;
    private String mPatchVersion;

    public ChampionListFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_champion_list, container, false);
        mRecyclerView = rootView.findViewById(R.id.recycler_view);
        mIndicator = rootView.findViewById(R.id.indicator);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mPatchVersion = LeaguePreferences.getPatchVersion(getContext());

        if (getContext().getResources().getBoolean(R.bool.twoPane)) {
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        } else {
            mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), getNumberOfColumns()));
        }
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new ChampionAdapter(getContext(), this, mPatchVersion);
        mRecyclerView.setAdapter(mAdapter);

        setupViewModel();
    }

    public int getNumberOfColumns() {
        View view = View.inflate(getContext(), R.layout.champion_card_item, null);
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int width = view.getMeasuredWidth();
        int count = getResources().getDisplayMetrics().widthPixels / width;
        int remaining = getResources().getDisplayMetrics().widthPixels - width * count;
        if (remaining > width - 15)
            count++;
        return count - 1;
    }

    private void setupViewModel() {
        ChampionModelFactory factory = InjectorUtils.provideChampionModelFactory(getActivity().getApplicationContext());
        mViewModel = ViewModelProviders.of(getActivity(), factory).get(ChampionModel.class);
        mViewModel.getChampions().observe(this, new Observer<List<Champion>>() {
            @Override
            public void onChanged(@Nullable List<Champion> championList) {
                Log.d(LOG_TAG, "Receiving database update from LiveData");
                if (championList != null && !championList.isEmpty()) {
                    mAdapter.setData(championList);
                    mIndicator.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    public void filterResults(String query) {
        mAdapter.getFilter().filter(query);
    }

    @Override
    public void onClick(int id) {
        mCallback.onChampionSelected(id);
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
