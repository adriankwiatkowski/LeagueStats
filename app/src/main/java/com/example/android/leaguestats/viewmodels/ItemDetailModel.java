package com.example.android.leaguestats.viewmodels;

import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.android.leaguestats.data.database.entity.ItemEntry;
import com.example.android.leaguestats.data.database.models.ListItemEntry;
import com.example.android.leaguestats.data.LeagueRepository;

import java.util.List;

public class ItemDetailModel extends ViewModel {

    private static final String LOG_TAG = ItemDetailModel.class.getSimpleName();
    private final MutableLiveData<Long> mIdQuery;
    private final MutableLiveData<String> mNameQuery;
    private final MutableLiveData<String[]> mFromQuery;
    private final MutableLiveData<String[]> mIntoQuery;
    private LiveData<ItemEntry> mItem;
    private LiveData<List<ListItemEntry>> mItemFrom;
    private LiveData<List<ListItemEntry>> mItemInto;

    public ItemDetailModel(final LeagueRepository repository) {
        mIdQuery = new MutableLiveData<>();
        mNameQuery = new MutableLiveData<>();
        mFromQuery = new MutableLiveData<>();
        mIntoQuery = new MutableLiveData<>();

        final MediatorLiveData mediatorLiveData = new MediatorLiveData();
        mediatorLiveData.addSource(mNameQuery, new Observer() {
            @Override
            public void onChanged(@Nullable Object o) {
                mediatorLiveData.setValue(o);
            }
        });
        mediatorLiveData.addSource(mIdQuery, new Observer() {
            @Override
            public void onChanged(@Nullable Object o) {
                mediatorLiveData.setValue(o);
            }
        });

        mItem = Transformations.switchMap(mediatorLiveData, new Function() {
            @Override
            public Object apply(Object input) {
                Log.d(LOG_TAG, "Getting new item");
                if (input instanceof Long || input instanceof Integer) {
                    long itemId = (long) input;

                    LiveData<ItemEntry> itemEntry = repository.getItemEntry(itemId);

                    updateItem(itemEntry);

                    return itemEntry;

                } else if (input instanceof String) {
                    String itemName = (String) input;

                    LiveData<ItemEntry> itemEntry = repository.getItemEntry(itemName);

                    updateItem(itemEntry);

                    return repository.getItemEntry(itemName);
                } else {
                    return null;
                }
            }
        });

        mItemFrom = Transformations.switchMap(mFromQuery, new Function<String[], LiveData<List<ListItemEntry>>>() {
            @Override
            public LiveData<List<ListItemEntry>> apply(String[] fromId) {
                return repository.getListItemEntry(fromId);
            }
        });

        mItemInto = Transformations.switchMap(mIntoQuery, new Function<String[], LiveData<List<ListItemEntry>>>() {
            @Override
            public LiveData<List<ListItemEntry>> apply(String[] intoId) {
                return repository.getListItemEntry(intoId);
            }
        });
    }

    private void updateItem(final LiveData<ItemEntry> itemEntryLiveData) {
        itemEntryLiveData.observeForever(new Observer<ItemEntry>() {
            @Override
            public void onChanged(@Nullable ItemEntry itemEntry) {
                itemEntryLiveData.removeObserver(this);
                setItemFrom(itemEntry);
                setItemInto(itemEntry);
            }
        });
    }

    public void initItem(String name) {
        mNameQuery.setValue(name);
    }

    public void initItem(long id) {
        mIdQuery.setValue(id);
    }

    public LiveData<ItemEntry> getItem() {
        return mItem;
    }

    private void setItemFrom(ItemEntry itemEntry) {
        List<String> fromId = itemEntry.getFrom();
        mFromQuery.setValue(fromId.toArray(new String[fromId.size()]));
    }

    public LiveData<List<ListItemEntry>> getItemFrom() {
        return mItemFrom;
    }

    private void setItemInto(ItemEntry itemEntry) {
        List<String> intoId = itemEntry.getInto();
        mIntoQuery.setValue(intoId.toArray(new String[intoId.size()]));
    }

    public LiveData<List<ListItemEntry>> getItemInto() {
        return mItemInto;
    }
}
