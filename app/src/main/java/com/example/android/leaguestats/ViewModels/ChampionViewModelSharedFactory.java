package com.example.android.leaguestats.ViewModels;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.example.android.leaguestats.room.AppDatabase;

public class ChampionViewModelSharedFactory extends ViewModelProvider.NewInstanceFactory {

    private final AppDatabase mDb;

    public ChampionViewModelSharedFactory(AppDatabase database) {
        mDb = database;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new ChampionViewModelShared(mDb);
    }
}
