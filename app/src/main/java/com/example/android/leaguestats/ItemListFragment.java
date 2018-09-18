package com.example.android.leaguestats;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
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

import com.example.android.leaguestats.adapters.ItemAdapter;
import com.example.android.leaguestats.database.AppDatabase;
import com.example.android.leaguestats.database.models.ListItemEntry;
import com.example.android.leaguestats.interfaces.IdClickListener;
import com.example.android.leaguestats.utilities.InjectorUtils;
import com.example.android.leaguestats.utilities.PreferencesUtils;
import com.example.android.leaguestats.viewModels.ItemListModel;
import com.example.android.leaguestats.viewModels.ItemListModelFactory;

import java.util.ArrayList;
import java.util.List;

public class ItemListFragment extends Fragment implements IdClickListener {

    public interface OnItemSelected {
        void onItemSelected(long id);
    }

    private static final String LOG_TAG = ItemListFragment.class.getSimpleName();
    private OnItemSelected mCallback;
    private ItemAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private ItemListModel mViewModel;

    public ItemListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(LOG_TAG, "onCreateView");
        View rootView = inflater.inflate(R.layout.fragment_item_list, container, false);

        mRecyclerView = rootView.findViewById(R.id.item_recycler_view);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(LOG_TAG, "onActivityCreated");

        int gridLayoutColumnCount;
        if (getContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            gridLayoutColumnCount = 3;
        } else {
            gridLayoutColumnCount = 2;
        }

        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), gridLayoutColumnCount));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        String patchVersion = PreferencesUtils.getPatchVersion(getContext());
        mAdapter = new ItemAdapter(getContext(), new ArrayList<ListItemEntry>(), ItemListFragment.this, patchVersion);
        mRecyclerView.setAdapter(mAdapter);


        setupViewModel();
    }

    private void setupViewModel() {
        ItemListModelFactory factory =
                InjectorUtils.provideItemListModelFactory(getActivity().getApplicationContext());
        mViewModel = ViewModelProviders.of(this, factory).get(ItemListModel.class);
        mViewModel.getItems().observe(this, new Observer<List<ListItemEntry>>() {
            @Override
            public void onChanged(@Nullable List<ListItemEntry> listItemEntries) {
                Log.d(LOG_TAG, "Receiving database update from LiveData");
                mViewModel.getItems().removeObserver(this);
                mAdapter.setData(listItemEntries);
            }
        });
    }

    @Override
    public void onClickListener(long id) {
        mCallback.onItemSelected(id);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mCallback = (OnItemSelected) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnItemSelected");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallback = null;
    }
}
