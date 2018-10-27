package com.example.android.leaguestats.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.res.Configuration;
import android.os.Bundle;
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
import com.example.android.leaguestats.adapters.ItemAdapter;
import com.example.android.leaguestats.data.database.entity.ItemEntry;
import com.example.android.leaguestats.interfaces.IdClickListener;
import com.example.android.leaguestats.utilities.InjectorUtils;
import com.example.android.leaguestats.utilities.LeaguePreferences;
import com.example.android.leaguestats.viewmodels.ItemModel;
import com.example.android.leaguestats.viewmodels.ItemModelFactory;

import java.util.ArrayList;
import java.util.List;

public class ItemListFragment extends Fragment implements IdClickListener {

    private static final String LOG_TAG = ItemListFragment.class.getSimpleName();
    private ItemAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private ItemModel mViewModel;
    private ProgressBar mIndicator;

    public ItemListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_list, container, false);

        mRecyclerView = rootView.findViewById(R.id.recycler_view);
        mIndicator = rootView.findViewById(R.id.indicator);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        int gridLayoutColumnCount;
        if (getContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            gridLayoutColumnCount = 3;
        } else {
            gridLayoutColumnCount = 2;
        }

        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), gridLayoutColumnCount));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        String patchVersion = LeaguePreferences.getPatchVersion(getContext());
        mAdapter = new ItemAdapter(getContext(), new ArrayList<ItemEntry>(), ItemListFragment.this, patchVersion);
        mRecyclerView.setAdapter(mAdapter);

        setupViewModel();
    }

    private void setupViewModel() {
        ItemModelFactory factory =
                InjectorUtils.provideItemModelFactory(getActivity().getApplicationContext());
        mViewModel = ViewModelProviders.of(getActivity(), factory).get(ItemModel.class);
        mViewModel.getItems().observe(getActivity(), new Observer<List<ItemEntry>>() {
            @Override
            public void onChanged(@Nullable List<ItemEntry> listItemEntries) {
                Log.d(LOG_TAG, "Receiving database update from LiveData");
                mAdapter.setData(listItemEntries);
                mIndicator.setVisibility(View.INVISIBLE);
            }
        });
    }

    @Override
    public void onClickListener(int id) {
        MasterFragment masterFragment = (MasterFragment) getParentFragment();
        masterFragment.addItemDetailFragment(id);
    }
}
