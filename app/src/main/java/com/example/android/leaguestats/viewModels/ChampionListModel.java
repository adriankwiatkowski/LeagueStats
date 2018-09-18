package com.example.android.leaguestats.viewModels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.example.android.leaguestats.database.AppDatabase;
import com.example.android.leaguestats.database.models.ListChampionEntry;
import com.example.android.leaguestats.sync.LeagueRepository;

import java.util.List;

public class ChampionListModel extends ViewModel {

    private static final String LOG_TAG = ChampionListModel.class.getSimpleName();
    private LiveData<List<ListChampionEntry>> mChampions;

    public ChampionListModel(LeagueRepository repository) {
        Log.d(LOG_TAG, "Retrieving champions from database");
        mChampions = repository.getListChampionEntry();
    }

    public LiveData<List<ListChampionEntry>> getChampions() {
        return mChampions;
    }
}
