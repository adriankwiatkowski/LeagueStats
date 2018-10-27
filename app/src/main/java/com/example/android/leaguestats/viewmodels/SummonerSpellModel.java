package com.example.android.leaguestats.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.example.android.leaguestats.data.database.entity.SummonerSpellEntry;
import com.example.android.leaguestats.data.LeagueRepository;

import java.util.List;

public class SummonerSpellModel extends ViewModel {

    private static final String LOG_TAG = SummonerSpellModel.class.getSimpleName();
    private LiveData<List<SummonerSpellEntry>> mSummonerSpells;
    private final MutableLiveData<SummonerSpellEntry> mSummonerSpell = new MutableLiveData<>();

    public SummonerSpellModel(LeagueRepository repository) {
        Log.d(LOG_TAG, "Retrieving summonerSpells from database");
        mSummonerSpells = repository.getSummonerSpells();
    }

    public LiveData<List<SummonerSpellEntry>> getSummonerSpells() {
        return mSummonerSpells;
    }

    public void initSummonerSpell(SummonerSpellEntry summonerSpellEntry) {
        mSummonerSpell.setValue(summonerSpellEntry);
    }

    public LiveData<SummonerSpellEntry> getSummonerSpell() {
        return mSummonerSpell;
    }
}
