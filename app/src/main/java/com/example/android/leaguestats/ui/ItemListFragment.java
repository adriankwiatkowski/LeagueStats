package com.example.android.leaguestats.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.android.leaguestats.R;
import com.example.android.leaguestats.adapters.ItemAdapter;
import com.example.android.leaguestats.interfaces.IdClickListener;
import com.example.android.leaguestats.models.Item;
import com.example.android.leaguestats.utilities.InjectorUtils;
import com.example.android.leaguestats.utilities.LeaguePreferences;
import com.example.android.leaguestats.viewmodels.ItemModel;
import com.example.android.leaguestats.viewmodels.ItemModelFactory;

import java.util.List;

public class ItemListFragment extends Fragment implements IdClickListener {

    public interface OnItemSelectedListener {
        void onItemSelected(int id);
    }

    private static final String LOG_TAG = ItemListFragment.class.getSimpleName();
    private OnItemSelectedListener mCallback;
    private RecyclerView mRecyclerView;
    private ItemAdapter mAdapter;
    private ItemModel mViewModel;
    private ProgressBar mIndicator;
    private String mPatchVersion;

    public ItemListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_item_list, container, false);
        mRecyclerView = rootView.findViewById(R.id.recycler_view);
        mIndicator = rootView.findViewById(R.id.indicator);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mPatchVersion = LeaguePreferences.getPatchVersion(getContext());

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new ItemAdapter(getContext(), this, mPatchVersion);
        mRecyclerView.setAdapter(mAdapter);

        setupViewModel();
    }

    private void setupViewModel() {
        ItemModelFactory factory = InjectorUtils.provideItemModelFactory(getActivity().getApplicationContext());
        mViewModel = ViewModelProviders.of(getActivity(), factory).get(ItemModel.class);
        mViewModel.getItems().observe(this, new Observer<List<Item>>() {
            @Override
            public void onChanged(@Nullable List<Item> itemList) {
                Log.d(LOG_TAG, "Receiving database update from LiveData");
                if (itemList != null && !itemList.isEmpty()) {
                    mAdapter.setData(itemList);
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
        mCallback.onItemSelected(id);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mCallback = (OnItemSelectedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnItemSelectedListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallback = null;
    }
}
