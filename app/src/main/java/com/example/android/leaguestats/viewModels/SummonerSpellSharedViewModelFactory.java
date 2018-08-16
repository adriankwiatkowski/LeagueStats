package com.example.android.leaguestats.viewModels;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.example.android.leaguestats.database.AppDatabase;

public class SummonerSpellSharedViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final AppDatabase mDb;

    public SummonerSpellSharedViewModelFactory(AppDatabase database) {
        mDb = database;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new SummonerSpellSharedViewModel(mDb);
    }
}
