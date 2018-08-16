package com.example.android.leaguestats.viewModels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.example.android.leaguestats.database.AppDatabase;
import com.example.android.leaguestats.database.ChampionEntry;

import java.util.List;

public class ChampionViewModelShared extends ViewModel {

    private static final String LOG_TAG = ChampionViewModelShared.class.getSimpleName();
    private LiveData<List<ChampionEntry>> mChampions;
    private final MutableLiveData<ChampionEntry> mSelected = new MutableLiveData<>();
    private LiveData<List<ChampionEntry>> mMasteryChampions;
    private final AppDatabase mDb;
    private LiveData<List<ChampionEntry>> mHistoryChampions;

    public ChampionViewModelShared(AppDatabase database) {
        mDb = database;
        Log.d(LOG_TAG, "Retrieving champions from database");
        mChampions = database.championDao().loadAllChampions();
    }

    public LiveData<List<ChampionEntry>> getChampions() {
        return mChampions;
    }

    public void select(ChampionEntry championEntry) {
        mSelected.setValue(championEntry);
    }

    public LiveData<ChampionEntry> getSelected() {
        return mSelected;
    }

    public LiveData<ChampionEntry> getChampionById(long id) {
        return mDb.championDao().loadChampionById(id);
    }

    public void setMasteryChampions(long[] ids) {
        mMasteryChampions = mDb.championDao().loadChampionsWithIds(ids);
    }

    public LiveData<List<ChampionEntry>> getMasteryChampions() {
        return mMasteryChampions;
    }

    public void setHistoryChampions(int[] ids) {
        mHistoryChampions = mDb.championDao().loadChampionsWithIds(ids);
    }

    public LiveData<List<ChampionEntry>> getHistoryChampions() {
        return mHistoryChampions;
    }
}
