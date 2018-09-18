package com.example.android.leaguestats.viewModels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.example.android.leaguestats.database.entity.ItemEntry;
import com.example.android.leaguestats.database.models.ListItemEntry;
import com.example.android.leaguestats.sync.LeagueRepository;

import java.util.List;

public class ItemDetailModel extends ViewModel {

    private static final String LOG_TAG = ItemDetailModel.class.getSimpleName();
    private LiveData<ItemEntry> mSelected;
    private LiveData<List<ListItemEntry>> mItemFrom;
    private LiveData<List<ListItemEntry>> mItemInto;
    private final LeagueRepository mRepository;

    public ItemDetailModel(LeagueRepository repository, long id) {
        mRepository = repository;
        Log.d(LOG_TAG, "Retrieving items from database");
        mSelected = repository.getItemEntry(id);
    }

    public void select(long id) {
        mSelected = mRepository.getItemEntry(id);
    }

    public LiveData<ItemEntry> getSelected() {
        return mSelected;
    }

    public void setItemFrom(String[] fromIds) {
        mItemFrom = mRepository.getListItemEntry(fromIds);
    }

    public LiveData<List<ListItemEntry>> getItemFrom() {
        return mItemFrom;
    }

    public void setItemInto(String[] intoIds) {
        mItemInto = mRepository.getListItemEntry(intoIds);
    }

    public LiveData<List<ListItemEntry>> getItemInto() {
        return mItemInto;
    }
}
