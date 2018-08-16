package com.example.android.leaguestats.viewModels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.example.android.leaguestats.database.AppDatabase;
import com.example.android.leaguestats.database.SummonerSpellEntry;

import java.util.List;

public class SummonerSpellSharedViewModel extends ViewModel {

    private static final String LOG_TAG = SummonerSpellSharedViewModel.class.getSimpleName();
    private LiveData<List<SummonerSpellEntry>> mSummonerSpells;
    private final MutableLiveData<SummonerSpellEntry> mSelected = new MutableLiveData<>();
    private LiveData<List<SummonerSpellEntry>> mSpell1;
    private LiveData<List<SummonerSpellEntry>> mSpell2;
    private final AppDatabase mDb;

    public SummonerSpellSharedViewModel(AppDatabase database) {
        mDb = database;
        Log.d(LOG_TAG, "Retrieving summonerSpells from database");
        mSummonerSpells = database.summonerSpellDao().loadAllSpells();
    }

    public LiveData<List<SummonerSpellEntry>> getSummonerSpells() {
        return mSummonerSpells;
    }

    public void select(SummonerSpellEntry summonerSpellEntry) {
        mSelected.setValue(summonerSpellEntry);
    }

    public LiveData<SummonerSpellEntry> getSelected() {
        return mSelected;
    }

    public void setSummonerSpell1(int[] ids) {
        mSpell1 = mDb.summonerSpellDao().loadSpellsWithIds(ids);
    }

    public LiveData<List<SummonerSpellEntry>> getSummonerSpell1() {
        return mSpell1;
    }

    public void setSummonerSpell2(int[] ids) {
        mSpell2 = mDb.summonerSpellDao().loadSpellsWithIds(ids);
    }

    public LiveData<List<SummonerSpellEntry>> getSummonerSpell2() {
        return mSpell2;
    }
}
