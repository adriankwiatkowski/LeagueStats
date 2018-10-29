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
import com.example.android.leaguestats.data.LeagueRepository;

import java.util.List;

public class ItemModel extends ViewModel {

    private static final String LOG_TAG = ItemModel.class.getSimpleName();

    private LiveData<List<ItemEntry>> mItems;

    private LiveData<ItemEntry> mItem;

    private LiveData<List<ItemEntry>> mItemFrom;
    private LiveData<List<ItemEntry>> mItemInto;

    private final MutableLiveData<ItemEntry> mItemQuery = new MutableLiveData<>();
    private final MutableLiveData<Long> mIdQuery = new MutableLiveData<>();
    private final MutableLiveData<String> mNameQuery = new MutableLiveData<>();
    private final MutableLiveData<String[]> mFromQuery = new MutableLiveData<>();
    private final MutableLiveData<String[]> mIntoQuery = new MutableLiveData<>();

    public ItemModel(final LeagueRepository repository) {
        Log.d(LOG_TAG, "Retrieving items from database");
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
        mediatorLiveData.addSource(mItemQuery, new Observer() {
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

                    LiveData<ItemEntry> itemEntry = repository.getItem(itemId);

                    updateItem(itemEntry);

                    return itemEntry;

                } else if (input instanceof String) {
                    String itemName = (String) input;

                    LiveData<ItemEntry> itemEntry = repository.getItem(itemName);

                    updateItem(itemEntry);

                    return repository.getItem(itemName);
                } else if (input instanceof ItemEntry){
                    return input;
                } else {
                    return null;
                }
            }
        });

        mItemFrom = Transformations.switchMap(mFromQuery, new Function<String[], LiveData<List<ItemEntry>>>() {
            @Override
            public LiveData<List<ItemEntry>> apply(String[] input) {
                return repository.getItems(input);
            }
        });

        mItemInto = Transformations.switchMap(mIntoQuery, new Function<String[], LiveData<List<ItemEntry>>>() {
            @Override
            public LiveData<List<ItemEntry>> apply(String[] input) {
                return repository.getItems(input);
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

    public LiveData<List<ItemEntry>> getItems() {
        return mItems;
    }

    public void initItem(ItemEntry itemEntry) {
        mItemQuery.setValue(itemEntry);
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

    public LiveData<List<ItemEntry>> getItemFrom() {
        return mItemFrom;
    }

    private void setItemInto(ItemEntry itemEntry) {
        List<String> intoId = itemEntry.getInto();
        mIntoQuery.setValue(intoId.toArray(new String[intoId.size()]));
    }

    public LiveData<List<ItemEntry>> getItemInto() {
        return mItemInto;
    }
}
