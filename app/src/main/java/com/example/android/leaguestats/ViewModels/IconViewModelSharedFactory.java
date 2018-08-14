package com.example.android.leaguestats.ViewModels;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.example.android.leaguestats.room.AppDatabase;

public class IconViewModelSharedFactory extends ViewModelProvider.NewInstanceFactory {

    private final AppDatabase mDb;
    private final int mIconId;

    public IconViewModelSharedFactory(AppDatabase database, int iconId) {
        mDb = database;
        mIconId = iconId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new IconViewModelShared(mDb, mIconId);
    }
}
