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

import com.example.android.leaguestats.data.LeagueDataRepository;
import com.example.android.leaguestats.data.database.entity.ItemEntry;
import com.example.android.leaguestats.models.Item;

import java.util.List;

public class ItemModel extends ViewModel {

    private static final String LOG_TAG = ItemModel.class.getSimpleName();

    private LiveData<List<Item>> mItems;
    private LiveData<ItemEntry> mItem;
    private LiveData<List<Item>> mItemFrom;
    private LiveData<List<Item>> mItemInto;

    private final MutableLiveData<Long> mIdQuery = new MutableLiveData<>();
    private final MutableLiveData<String> mNameQuery = new MutableLiveData<>();
    private final MutableLiveData<String[]> mFromQuery = new MutableLiveData<>();
    private final MutableLiveData<String[]> mIntoQuery = new MutableLiveData<>();

    public ItemModel(final LeagueDataRepository repository) {
        Log.d(LOG_TAG, "Retrieving Items from database");
        mItems = repository.getItems();

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
                Log.d(LOG_TAG, "Getting new Item");
                LiveData<ItemEntry> itemEntry = null;
                if (input instanceof Long || input instanceof Integer) {
                    long itemId = (long) input;
                    itemEntry = repository.getItem(itemId);
                } else if (input instanceof String) {
                    String itemName = (String) input;
                    itemEntry = repository.getItem(itemName);
                }
                updateItem(itemEntry);
                return itemEntry;
            }
        });

        mItemFrom = Transformations.switchMap(mFromQuery, new Function<String[], LiveData<List<Item>>>() {
            @Override
            public LiveData<List<Item>> apply(String[] input) {
                return input == null ? null : repository.getItems(input);
            }
        });
        mItemInto = Transformations.switchMap(mIntoQuery, new Function<String[], LiveData<List<Item>>>() {
            @Override
            public LiveData<List<Item>> apply(String[] input) {
                return input == null ? null : repository.getItems(input);
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

    public LiveData<List<Item>> getItems() {
        return mItems;
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
        if (itemEntry == null) {
            mFromQuery.setValue(null);
        } else {
            List<String> fromId = itemEntry.getFrom();
            mFromQuery.setValue(fromId == null ? null : fromId.toArray(new String[fromId.size()]));
        }
    }

    public LiveData<List<Item>> getItemFrom() {
        return mItemFrom;
    }

    private void setItemInto(ItemEntry itemEntry) {
        if (itemEntry == null) {
            mIntoQuery.setValue(null);
        } else {
            List<String> intoId = itemEntry.getInto();
            mIntoQuery.setValue(intoId == null ? null : intoId.toArray(new String[intoId.size()]));
        }
    }

    public LiveData<List<Item>> getItemInto() {
        return mItemInto;
    }
}
