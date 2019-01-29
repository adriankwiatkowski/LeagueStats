package com.example.android.leaguestats.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.android.leaguestats.data.LeagueDataRepository;

public class MainModel extends ViewModel {

    private static final String LOG_TAG = MainModel.class.getSimpleName();

    private final MediatorLiveData<Boolean> mIsNotEmpty = new MediatorLiveData<>();

    public MainModel(LeagueDataRepository repository) {
        Log.d(LOG_TAG, "Getting MainModel");
        setObservers(repository);
    }

    private void setObservers(LeagueDataRepository repository) {
        final LiveData<Integer> championCount = repository.countAllLiveChampions();
        final LiveData<Integer> itemCount = repository.countAllLiveItems();
        final LiveData<Integer> summonerSpellCount = repository.countAllLiveSummonerSpells();
        final LiveData<Integer> iconCount = repository.countAllLiveIcons();

        final Counters counters = new Counters();

        mIsNotEmpty.addSource(championCount, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer integer) {
                if (integer != null && integer > 0) {
                    mIsNotEmpty.removeSource(championCount);
                    counters.championCount = integer;
                    setIsNotEmpty(counters);
                }
            }
        });
        mIsNotEmpty.addSource(itemCount, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer integer) {
                if (integer != null && integer > 0) {
                    mIsNotEmpty.removeSource(itemCount);
                    counters.itemCount = integer;
                    setIsNotEmpty(counters);
                }
            }
        });
        mIsNotEmpty.addSource(summonerSpellCount, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer integer) {
                if (integer != null && integer > 0) {
                    mIsNotEmpty.removeSource(summonerSpellCount);
                    counters.summonerSpellCount = integer;
                    setIsNotEmpty(counters);
                }
            }
        });
        mIsNotEmpty.addSource(iconCount, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer integer) {
                if (integer != null && integer > 0) {
                    mIsNotEmpty.removeSource(iconCount);
                    counters.iconCount = integer;
                    setIsNotEmpty(counters);
                }
            }
        });
    }

    private void setIsNotEmpty(Counters counters) {
        mIsNotEmpty.setValue(counters.isNotEmpty());
    }

    public LiveData<Boolean> isNotEmpty() {
        return mIsNotEmpty;
    }

    private static class Counters {
        int championCount = 0;
        int itemCount = 0;
        int summonerSpellCount = 0;
        int iconCount = 0;

        private boolean isNotEmpty() {
            return championCount > 0 && itemCount > 0 && summonerSpellCount > 0 && iconCount > 0;
        }
    }
}
