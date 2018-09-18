package com.example.android.leaguestats.viewModels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.example.android.leaguestats.models.Mastery;
import com.example.android.leaguestats.sync.LeagueRepository;

import java.util.List;

public class MasteryModel extends ViewModel {

    private static final String LOG_TAG = MasteryModel.class.getSimpleName();
    private final LeagueRepository mRepository;
    private LiveData<List<Mastery>> mMasteries;

    public MasteryModel(LeagueRepository repository) {
        mRepository = repository;
    }

    public void initMasteries(String entryUrlString, long summonerId) {
        Log.d(LOG_TAG, "Retrieving masteries from server");
        mMasteries = mRepository.getMasteries(entryUrlString, summonerId);
    }

    public LiveData<List<Mastery>> getMasteries() {
        return mMasteries;
    }
}
