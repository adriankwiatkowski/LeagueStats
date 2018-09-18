package com.example.android.leaguestats.viewModels;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.example.android.leaguestats.database.AppDatabase;
import com.example.android.leaguestats.sync.LeagueRepository;

public class SummonerSpellListModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final LeagueRepository mRepository;

    public SummonerSpellListModelFactory(LeagueRepository repository) {
        mRepository = repository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new SummonerSpellListModel(mRepository);
    }
}
