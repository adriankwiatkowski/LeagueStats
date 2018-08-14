package com.example.android.leaguestats.ViewModels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.example.android.leaguestats.room.AppDatabase;
import com.example.android.leaguestats.room.ItemEntry;

import java.util.List;

public class ItemViewModelShared extends ViewModel {

    private static final String LOG_TAG = ItemViewModelShared.class.getSimpleName();
    private LiveData<List<ItemEntry>> mItems;
    private final MutableLiveData<ItemEntry> mSelected = new MutableLiveData<>();
    private LiveData<List<ItemEntry>> mItemFrom;
    private LiveData<List<ItemEntry>> mItemInto;
    private final AppDatabase mDb;

    public ItemViewModelShared(AppDatabase database) {
        mDb = database;
        Log.d(LOG_TAG, "Retrieving items from database");
        mItems = database.itemDao().loadAllPurchasableItems();
    }

    public LiveData<List<ItemEntry>> getItems() {
        return mItems;
    }

    public void select(ItemEntry itemEntry) {
        mSelected.setValue(itemEntry);
    }

    public LiveData<ItemEntry> getSelected() {
        return mSelected;
    }

    public void setItemFrom(String[] fromIds) {
        mItemFrom = mDb.itemDao().loadItemsWithIds(fromIds);
    }

    public LiveData<List<ItemEntry>> getItemFrom() {
        return mItemFrom;
    }

    public void setItemInto(String[] intoIds) {
        mItemInto = mDb.itemDao().loadItemsWithIds(intoIds);
    }

    public LiveData<List<ItemEntry>> getItemInto() {
        return mItemInto;
    }


}
