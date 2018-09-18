package com.example.android.leaguestats.viewModels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.example.android.leaguestats.models.Summoner;
import com.example.android.leaguestats.sync.LeagueRepository;


public class SummonerModel extends ViewModel {

    private final String LOG_TAG = SummonerModel.class.getSimpleName();
    private LiveData<Summoner> mSummoner;
    private final LeagueRepository mRepository;

    public SummonerModel(LeagueRepository repository) {
        mRepository = repository;
    }

    public void setSummoner(String entryUrlString, String summonerName) {
        if (entryUrlString.isEmpty() || summonerName.isEmpty()) {
            return;
        }
        Log.d(LOG_TAG, "Getting Summoner");
        mSummoner = mRepository.getSummoner(entryUrlString, summonerName);
    }

    public LiveData<Summoner> getSummoner() {
        return mSummoner;
    }
}
