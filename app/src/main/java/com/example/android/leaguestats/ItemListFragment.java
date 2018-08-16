package com.example.android.leaguestats;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.android.leaguestats.viewModels.ItemViewModelShared;
import com.example.android.leaguestats.viewModels.ItemViewModelSharedFactory;
import com.example.android.leaguestats.adapters.ItemAdapter;
import com.example.android.leaguestats.interfaces.ChampionTaskCompleted;
import com.example.android.leaguestats.interfaces.IconTaskCompleted;
import com.example.android.leaguestats.interfaces.ItemTaskCompleted;
import com.example.android.leaguestats.interfaces.PatchTaskCompleted;
import com.example.android.leaguestats.interfaces.ResultTask;
import com.example.android.leaguestats.interfaces.SummonerSpellTaskCompleted;
import com.example.android.leaguestats.database.AppDatabase;
import com.example.android.leaguestats.database.ChampionEntry;
import com.example.android.leaguestats.database.IconEntry;
import com.example.android.leaguestats.database.ItemEntry;
import com.example.android.leaguestats.database.SummonerSpellEntry;
import com.example.android.leaguestats.utilities.AsyncTasks.ChampionsAsyncTask;
import com.example.android.leaguestats.utilities.AsyncTasks.IconAsyncTask;
import com.example.android.leaguestats.utilities.AsyncTasks.ItemAsyncTask;
import com.example.android.leaguestats.utilities.AsyncTasks.PatchAsyncTask;
import com.example.android.leaguestats.utilities.AsyncTasks.SummonerSpellAsyncTask;
import com.example.android.leaguestats.utilities.PreferencesUtils;

import java.util.ArrayList;
import java.util.List;

public class ItemListFragment extends Fragment implements ItemAdapter.ItemClickListener {

    public interface OnItemSelected {
        void onItemSelected();
    }

    private static final String LOG_TAG = ItemListFragment.class.getSimpleName();
    private OnItemSelected mCallback;
    private ItemAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private ItemViewModelShared mViewModel;
    private AppDatabase mDb;

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
        mAdapter = new ItemAdapter(getContext(), new ArrayList<ItemEntry>(), ItemListFragment.this, patchVersion);
        mRecyclerView.setAdapter(mAdapter);

        mDb = AppDatabase.getInstance(getActivity().getApplicationContext());

        setupViewModel();
    }

    private void setupViewModel() {
        ItemViewModelSharedFactory factory = new ItemViewModelSharedFactory(mDb);
        mViewModel = ViewModelProviders.of(getActivity(), factory).get(ItemViewModelShared.class);
        mViewModel.getItems().observe(this, new Observer<List<ItemEntry>>() {
            @Override
            public void onChanged(@Nullable List<ItemEntry> itemEntries) {
                mViewModel.getItems().removeObserver(this);
                mAdapter.setData(itemEntries);
            }
        });
    }

    @Override
    public void onItemClick(ItemEntry itemEntry) {
        mViewModel.select(itemEntry);
        mCallback.onItemSelected();
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
