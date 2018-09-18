package com.example.android.leaguestats.viewModels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.example.android.leaguestats.database.AppDatabase;
import com.example.android.leaguestats.database.models.ListSummonerSpellEntry;
import com.example.android.leaguestats.sync.LeagueRepository;

import java.util.List;

public class SummonerSpellListModel extends ViewModel {

    private static final String LOG_TAG = SummonerSpellListModel.class.getSimpleName();
    private LiveData<List<ListSummonerSpellEntry>> mSummonerSpells;

    public SummonerSpellListModel(LeagueRepository repository) {
        Log.d(LOG_TAG, "Retrieving summonerSpells from database");
        mSummonerSpells = repository.getSummonerSpells();
    }

    public LiveData<List<ListSummonerSpellEntry>> getSpellList() {
        return mSummonerSpells;
    }
}
