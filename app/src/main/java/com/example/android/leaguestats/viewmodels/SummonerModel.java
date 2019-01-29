package com.example.android.leaguestats.viewmodels;

import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.text.TextUtils;
import android.util.Log;

import com.example.android.leaguestats.data.LeagueSummonerRepository;
import com.example.android.leaguestats.models.Mastery;
import com.example.android.leaguestats.models.Match;
import com.example.android.leaguestats.models.Resource;
import com.example.android.leaguestats.models.Summoner;
import com.example.android.leaguestats.utilities.AbsentLiveData;

import java.util.List;

public class SummonerModel extends ViewModel {

    private final String LOG_TAG = SummonerModel.class.getSimpleName();

    private LiveData<Resource<Summoner>> mSummoner;
    private LiveData<Resource<List<Mastery>>> mMasteries;
    private LiveData<Resource<List<Resource<Match>>>> mMatches;

    private final MutableLiveData<SummonerQuery> mSummonerQuery = new MutableLiveData<>();

    public SummonerModel(final LeagueSummonerRepository repository) {
        Log.d(LOG_TAG, "Getting SummonerModel");
        mSummoner = Transformations.switchMap(mSummonerQuery, new Function<SummonerQuery, LiveData<Resource<Summoner>>>() {
            @Override
            public LiveData<Resource<Summoner>> apply(SummonerQuery summonerQuery) {
                Log.d(LOG_TAG, "Getting new summoner");
                LiveData<Resource<Summoner>> summonerLiveData = repository.getSummoner(summonerQuery.getEntryUrlString(), summonerQuery.getSummonerName());
                if (summonerLiveData != null) {
                    return summonerLiveData;
                } else if (mSummoner != null) {
                    // Return current Summoner. Dont override with null.
                    return mSummoner;
                }
                return null;
            }
        });
        mMasteries = Transformations.switchMap(mSummoner, new Function<Resource<Summoner>, LiveData<Resource<List<Mastery>>>>() {
            @Override
            public LiveData<Resource<List<Mastery>>> apply(Resource<Summoner> input) {
                if (input != null) {
                    switch (input.status) {
                        case LOADING:
                            Log.d(LOG_TAG, "Waiting for Summoner to get masteries");
                            return AbsentLiveData.create(Resource.loading((List<Mastery>) null));
                        case SUCCESS:
                            Log.d(LOG_TAG, "Getting new masteries");
                            return repository.getMasteries(input.data.getEntryUrl(), input.data.getSummonerId());
                        case ERROR:
                            Log.d(LOG_TAG, "Error getting masteries. Summoner Error");
                            return AbsentLiveData.create(Resource.error(input.message, (List<Mastery>) null));
                    }
                }
                return null;
            }
        });
        mMatches = Transformations.switchMap(mSummoner, new Function<Resource<Summoner>, LiveData<Resource<List<Resource<Match>>>>>() {
            @Override
            public LiveData<Resource<List<Resource<Match>>>> apply(Resource<Summoner> input) {
                if (input != null) {
                    switch (input.status) {
                        case LOADING:
                            Log.d(LOG_TAG, "Waiting for Summoner to get matches");
                            return AbsentLiveData.create(Resource.loading((List<Resource<Match>>) null));
                        case SUCCESS:
                            Log.d(LOG_TAG, "Getting new matches");
                            return repository.getMatches(input.data.getEntryUrl(), input.data.getAccountId(), input.data.getSummonerId());
                        case ERROR:
                            Log.d(LOG_TAG, "Error getting matches. Summoner Error");
                            return AbsentLiveData.create(Resource.error(input.message, (List<Resource<Match>>) null));
                    }
                }
                return null;
            }
        });
    }

    public void searchSummoner(String entryUrlString, String summonerName) {
        summonerName = removeWhitespaces(summonerName);
        SummonerQuery newSummonerQuery = new SummonerQuery(entryUrlString, summonerName);
        if (mSummonerQuery.getValue() != null) {
            SummonerQuery summonerQuery = mSummonerQuery.getValue();
            if (isSameSummoner(summonerQuery, newSummonerQuery)) {
                return;
            }
        }
        mSummonerQuery.setValue(newSummonerQuery);
    }

    public LiveData<Resource<Summoner>> getSummoner() {
        return mSummoner;
    }

    public LiveData<Resource<List<Mastery>>> getMasteries() {
        return mMasteries;
    }

    public LiveData<Resource<List<Resource<Match>>>> getMatches() {
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

    static class SummonerQuery {

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
