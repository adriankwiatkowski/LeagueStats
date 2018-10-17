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

import com.example.android.leaguestats.data.database.entity.ChampionEntry;
import com.example.android.leaguestats.data.LeagueRepository;

public class ChampionDetailModel extends ViewModel {

    private static final String LOG_TAG = ChampionDetailModel.class.getSimpleName();
    private final MutableLiveData<String> mNameQuery;
    private final MutableLiveData<Long> mIdQuery;
    private LiveData<ChampionEntry> mChampion;

    public ChampionDetailModel(final LeagueRepository repository) {
        Log.d(LOG_TAG, "Getting ChampionDetailModel");
        mNameQuery = new MutableLiveData<>();
        mIdQuery = new MutableLiveData<>();

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

        mChampion = Transformations.switchMap(mediatorLiveData, new Function() {
            @Override
            public Object apply(Object input) {
                Log.d(LOG_TAG, "Getting new champion");
                if (input instanceof Long || input instanceof Integer) {
                    long championId = (long) input;
                    return repository.getChampionEntry(championId);
                } else if (input instanceof String) {
                    String championName = (String) input;
                    return repository.getChampionEntry(championName);
                } else {
                    return null;
                }
            }
        });
    }

    public void initChampion(long id) {
        mIdQuery.setValue(id);
    }

    public void initChampion(String championName) {
        mNameQuery.setValue(championName);
    }

    public LiveData<ChampionEntry> getChampion() {
        return mChampion;
    }

}
