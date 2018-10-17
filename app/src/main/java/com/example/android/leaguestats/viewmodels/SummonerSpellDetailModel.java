package com.example.android.leaguestats.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.example.android.leaguestats.data.database.entity.SummonerSpellEntry;
import com.example.android.leaguestats.data.LeagueRepository;

public class SummonerSpellDetailModel extends ViewModel {

    private static final String LOG_TAG = SummonerSpellListModel.class.getSimpleName();
    private LiveData<SummonerSpellEntry> mSummonerSpell;

    public SummonerSpellDetailModel(LeagueRepository repository, int id) {
        Log.d(LOG_TAG, "Retrieving summonerSpell by id from database");
        mSummonerSpell = repository.getSummonerSpell(id);
    }

    public LiveData<SummonerSpellEntry> getSummonerSpell() {
        return mSummonerSpell;
    }
}
