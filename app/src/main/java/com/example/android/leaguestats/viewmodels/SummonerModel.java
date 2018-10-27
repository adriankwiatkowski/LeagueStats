package com.example.android.leaguestats.viewmodels;

import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.text.TextUtils;
import android.util.Log;

import com.example.android.leaguestats.models.Mastery;
import com.example.android.leaguestats.models.Match;
import com.example.android.leaguestats.models.Summoner;
import com.example.android.leaguestats.data.LeagueRepository;

import java.util.List;

public class SummonerModel extends ViewModel {

    private final String LOG_TAG = SummonerModel.class.getSimpleName();

    private final MutableLiveData<SummonerQuery> mSummonerQuery = new MutableLiveData<>();
    private LiveData<Summoner> mSummoner;

    private LiveData<List<Mastery>> mMasteries;

    private LiveData<List<Match>> mMatches;

    public SummonerModel(final LeagueRepository repository) {
        Log.d(LOG_TAG, "Getting SummonerModel");

        mSummoner = Transformations.switchMap(mSummonerQuery, new Function<SummonerQuery, LiveData<Summoner>>() {
            @Override
            public LiveData<Summoner> apply(SummonerQuery summonerQuery) {
                Log.d(LOG_TAG, "Getting new summoner");
                return repository.getSummoner(summonerQuery.getEntryUrlString(), summonerQuery.getSummonerName());
            }
        });

        mMasteries = Transformations.switchMap(mSummoner, new Function<Summoner, LiveData<List<Mastery>>>() {
            @Override
            public LiveData<List<Mastery>> apply(Summoner input) {
                Log.d(LOG_TAG, "Getting new masteries");
                return repository.getMasteries(input.getEntryUrl(), input.getSummonerId());
            }
        });

        mMatches = Transformations.switchMap(mSummoner, new Function<Summoner, LiveData<List<Match>>>() {
            @Override
            public LiveData<List<Match>> apply(Summoner input) {
                Log.d(LOG_TAG, "Getting matches");
                return repository.getMatches(input.getEntryUrl(), input.getAccountId(), input.getSummonerId());
            }
        });
    }

    public void searchSummoner(String entryUrlString, String summonerName) {
        SummonerQuery newSummonerQuery = new SummonerQuery(entryUrlString, summonerName);
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

    public LiveData<List<Mastery>> getMasteries() {
        return mMasteries;
    }

    public LiveData<List<Match>> getMatches() {
        return mMatches;
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

    @Override
    protected void onCleared() {
        super.onCleared();
        // TODO cancel AsyncTask?
    }

    class SummonerQuery {

        String entryUrlString;
        String summonerName;

        SummonerQuery(String entryUrlString, String summonerName) {
            this.entryUrlString = entryUrlString;
            this.summonerName = summonerName;
        }

        String getEntryUrlString() {
            return entryUrlString;
        }

        String getSummonerName() {
            return summonerName;
        }
    }
}
