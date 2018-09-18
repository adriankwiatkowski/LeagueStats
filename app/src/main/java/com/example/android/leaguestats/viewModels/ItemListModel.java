package com.example.android.leaguestats.viewModels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.example.android.leaguestats.database.AppDatabase;
import com.example.android.leaguestats.database.models.ListItemEntry;
import com.example.android.leaguestats.sync.LeagueRepository;

import java.util.List;

public class ItemListModel extends ViewModel {

    private static final String LOG_TAG = ItemListModel.class.getSimpleName();
    private LiveData<List<ListItemEntry>> mItems;

    public ItemListModel(LeagueRepository repository) {
        Log.d(LOG_TAG, "Retrieving items from database");
        mItems = repository.getListItemEntry();
    }

    public LiveData<List<ListItemEntry>> getItems() {
        return mItems;
    }
}
