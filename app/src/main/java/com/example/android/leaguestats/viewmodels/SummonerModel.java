package com.example.android.leaguestats.viewmodels;

import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.text.TextUtils;
import android.util.Log;

import com.example.android.leaguestats.models.Summoner;
import com.example.android.leaguestats.data.LeagueRepository;
import com.example.android.leaguestats.viewmodels.querymodels.SummonerQuery;

public class SummonerModel extends ViewModel {

    private final String LOG_TAG = SummonerModel.class.getSimpleName();
    private final MutableLiveData<SummonerQuery> mSummonerQuery;
    private LiveData<Summoner> mSummoner;

    public SummonerModel(final LeagueRepository repository) {
        Log.d(LOG_TAG, "Getting SummonerModel");
        mSummonerQuery = new MutableLiveData<>();
        mSummoner = Transformations.switchMap(mSummonerQuery, new Function<SummonerQuery, LiveData<Summoner>>() {
            @Override
            public LiveData<Summoner> apply(SummonerQuery summonerQuery) {
                Log.d(LOG_TAG, "Getting new summoner");
                return repository.getSummoner(summonerQuery.getEntryUrlString(), summonerQuery.getSummonerName());
            }
        });
    }

    public void searchSummoner(SummonerQuery newSummonerQuery) {
        if (mSummonerQuery.getValue() != null) {
            SummonerQuery summonerQuery = mSummonerQuery.getValue();
            if (isSameSummoner(summonerQuery, newSummonerQuery)) {
                return;
            }
        }
        mSummonerQuery.setValue(newSummonerQuery);
    }

    public LiveData<Summoner> getSummoner() {
        return mSummoner;
    }

    private boolean isSameSummoner(SummonerQuery cachedSummonerQuery, SummonerQuery newSummonerQuery) {

        String cachedEntryUrl = cachedSummonerQuery.getEntryUrlString();
        String newEntryUrl = newSummonerQuery.getEntryUrlString();

        String cachedSummonerName = cachedSummonerQuery.getSummonerName();
        String newSummonerName = newSummonerQuery.getSummonerName();

        return isSameSummoner(cachedEntryUrl, newEntryUrl) && isSameSummoner(cachedSummonerName, newSummonerName);
    }

    private boolean isSameSummoner(String string, String string1) {
        string = removeWhitespaces(string);
        string1 = removeWhitespaces(string1);
        return TextUtils.equals(string, string1);
    }

    private String removeWhitespaces(String string) {
        StringBuilder builder = new StringBuilder();
        char[] characters = string.toCharArray();
        for (char c : characters) {
            if (!Character.isWhitespace(c)) {
                builder.append(c);
            }
        }
        return builder.toString().toLowerCase();
    }
}
