package com.example.android.leaguestats.viewmodels;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.example.android.leaguestats.data.LeagueRepository;

public class SummonerSpellDetailModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final LeagueRepository mRepository;
    private final int mId;

    public SummonerSpellDetailModelFactory(LeagueRepository repository, int id) {
        mRepository = repository;
        mId = id;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new SummonerSpellDetailModel(mRepository, mId);
    }
}
