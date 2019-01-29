package com.example.android.leaguestats.viewmodels;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.example.android.leaguestats.data.LeagueDataRepository;

public class MainModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final LeagueDataRepository mRepository;

    public MainModelFactory(LeagueDataRepository repository) {
        mRepository = repository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new MainModel(mRepository);
    }
}
