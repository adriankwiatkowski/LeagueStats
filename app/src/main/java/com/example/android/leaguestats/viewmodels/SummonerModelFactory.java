package com.example.android.leaguestats.viewmodels;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.example.android.leaguestats.data.LeagueSummonerRepository;

public class SummonerModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final LeagueSummonerRepository mRepository;

    public SummonerModelFactory(LeagueSummonerRepository repository) {
        mRepository = repository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new SummonerModel(mRepository);
    }
}
