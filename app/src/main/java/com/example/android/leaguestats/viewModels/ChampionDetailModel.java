package com.example.android.leaguestats.viewModels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.example.android.leaguestats.database.AppDatabase;
import com.example.android.leaguestats.database.entity.ChampionEntry;
import com.example.android.leaguestats.sync.LeagueRepository;

public class ChampionDetailModel extends ViewModel {

    private static final String LOG_TAG = ChampionDetailModel.class.getSimpleName();
    private LiveData<ChampionEntry> mChampion;
    private LeagueRepository mRepository;

    public ChampionDetailModel(LeagueRepository repository) {
        Log.d(LOG_TAG, "Retrieving champions from database");
        mRepository = repository;
    }

    public void initChampion(String id) {
        if (!id.isEmpty()) {
            mChampion = mRepository.getChampionEntry(id);
        }
    }

    public LiveData<ChampionEntry> getChampion() {
        return mChampion;
    }
}
