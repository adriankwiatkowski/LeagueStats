package com.example.android.leaguestats.viewmodels;

import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.text.TextUtils;
import android.util.Log;

import com.example.android.leaguestats.models.Match;
import com.example.android.leaguestats.data.LeagueRepository;
import com.example.android.leaguestats.viewmodels.querymodels.HistoryQuery;

import java.util.List;

public class HistoryModel extends ViewModel {

    private static final String LOG_TAG = HistoryModel.class.getSimpleName();
    private final MutableLiveData<HistoryQuery> mSummonerQuery;
    private LiveData<List<Match>> mMatches;

    public HistoryModel(final LeagueRepository repository) {
        mSummonerQuery = new MutableLiveData<>();
        mMatches = Transformations.switchMap(mSummonerQuery, new Function<HistoryQuery, LiveData<List<Match>>>() {
            @Override
            public LiveData<List<Match>> apply(HistoryQuery input) {
                Log.d(LOG_TAG, "Getting matches");
                return repository.getMatches(input.getEntryUrlString(), input.getAccountId(), input.getSummonerId());
            }
        });
    }

    public void searchHistoryMatches(HistoryQuery newHistoryQuery) {
        if (mSummonerQuery.getValue() != null) {
            HistoryQuery summonerQuery = mSummonerQuery.getValue();
            if (TextUtils.equals(summonerQuery.getEntryUrlString(), newHistoryQuery.getEntryUrlString())
                    && summonerQuery.getSummonerId() == newHistoryQuery.getSummonerId()) {
                return;
            }
        }
        mSummonerQuery.setValue(newHistoryQuery);
    }

    public LiveData<List<Match>> getMatches() {
        return mMatches;
    }
}
