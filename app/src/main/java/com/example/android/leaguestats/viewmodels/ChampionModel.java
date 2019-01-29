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
import com.example.android.leaguestats.data.database.entity.ChampionEntry;
import com.example.android.leaguestats.models.Champion;

import java.util.List;

public class ChampionModel extends ViewModel {

    private static final String LOG_TAG = ChampionModel.class.getSimpleName();

    private LiveData<ChampionEntry> mChampion;
    private LiveData<List<Champion>> mChampions;

    private final MutableLiveData<String> mNameQuery = new MutableLiveData<>();
    private final MutableLiveData<Long> mIdQuery = new MutableLiveData<>();

    public ChampionModel(final LeagueDataRepository repository) {
        Log.d(LOG_TAG, "Getting ChampionModel");
        mChampions = repository.getChampions();

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
                    return repository.getChampion(championId);
                } else if (input instanceof String) {
                    String championName = (String) input;
                    return repository.getChampion(championName);
                }
                return null;
            }
        });
    }

    public LiveData<List<Champion>> getChampions() {
        return mChampions;
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
