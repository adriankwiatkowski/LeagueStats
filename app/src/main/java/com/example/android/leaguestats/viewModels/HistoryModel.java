package com.example.android.leaguestats.viewModels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.android.leaguestats.models.Match;
import com.example.android.leaguestats.sync.LeagueRepository;

import java.util.List;

public class HistoryModel extends ViewModel {

    private static final String LOG_TAG = HistoryModel.class.getSimpleName();
    private final LeagueRepository mRepository;
    private LiveData<List<Match>> mMatches;

    public HistoryModel(LeagueRepository repository) {
        mRepository = repository;
    }

    public void initHistoryMatches(String entryUrlString, long accountId) {
        Log.d(LOG_TAG, "Retrieving history from server");
        mMatches = mRepository.getMatches(entryUrlString, accountId);
    }

    public LiveData<List<Match>> getMatches() {
        return mMatches;
    }
}
