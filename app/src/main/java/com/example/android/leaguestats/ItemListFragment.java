package com.example.android.leaguestats;

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
import com.example.android.leaguestats.utilities.ItemLoader;
import com.example.android.leaguestats.utilities.PreferencesUtils;

public class ItemListFragment extends Fragment implements ItemAdapter.ItemClickListener {

    public interface OnItemSelected {
        void onItemSelected(int id);
    }

    private static final String LOG_TAG = ItemListFragment.class.getSimpleName();
    private OnItemSelected mCallback;
    private ItemAdapter mItemAdapter;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private static final int ITEM_LOADER = 3;

    public ItemListFragment() {}

    public static ItemListFragment newInstance() {
        ItemListFragment itemListFragment = new ItemListFragment();
        return itemListFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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

        mLayoutManager = new GridLayoutManager(getContext(), gridLayoutColumnCount);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        String patchVersion = PreferencesUtils.getPatchVersion(getContext());

        mItemAdapter = new ItemAdapter(getContext(), null, ItemListFragment.this, patchVersion);
        mRecyclerView.setAdapter(mItemAdapter);

        getActivity().getSupportLoaderManager().initLoader(ITEM_LOADER, null, new ItemLoader(getContext(), mItemAdapter));
    }

    @Override
    public void onItemClick(int id) {
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
}
