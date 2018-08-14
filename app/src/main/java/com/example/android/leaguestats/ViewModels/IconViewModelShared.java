package com.example.android.leaguestats.ViewModels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.example.android.leaguestats.room.AppDatabase;
import com.example.android.leaguestats.room.IconEntry;

public class IconViewModelShared extends ViewModel {

    private final static String LOG_TAG = IconViewModelShared.class.getSimpleName();
    private LiveData<IconEntry> mIcon;

    public IconViewModelShared(AppDatabase database, int id) {
        Log.d(LOG_TAG, "Retrieving icon from database");
        mIcon = database.iconDao().loadIconById(id);
    }

    public LiveData<IconEntry> getIcon() {
        return mIcon;
    }
}
