package com.example.android.leaguestats.viewModels;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.example.android.leaguestats.database.AppDatabase;
import com.example.android.leaguestats.sync.LeagueRepository;

public class ItemDetailModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final LeagueRepository mRepository;
    private final long mId;

    public ItemDetailModelFactory(LeagueRepository repository, long id) {
        mRepository = repository;
        mId = id;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new ItemDetailModel(mRepository, mId);
    }
}
