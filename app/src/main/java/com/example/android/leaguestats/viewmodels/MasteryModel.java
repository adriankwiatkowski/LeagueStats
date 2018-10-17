package com.example.android.leaguestats.viewmodels;

import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.text.TextUtils;
import android.util.Log;

import com.example.android.leaguestats.models.Mastery;
import com.example.android.leaguestats.data.LeagueRepository;
import com.example.android.leaguestats.viewmodels.querymodels.MasteryQuery;

import java.util.List;

public class MasteryModel extends ViewModel {

    private static final String LOG_TAG = MasteryModel.class.getSimpleName();
    private final MutableLiveData<MasteryQuery> mSummonerQuery;
    private LiveData<List<Mastery>> mMasteries;

    public MasteryModel(final LeagueRepository repository) {
        mSummonerQuery = new MutableLiveData<>();
        mMasteries = Transformations.switchMap(mSummonerQuery, new Function<MasteryQuery, LiveData<List<Mastery>>>() {
            @Override
            public LiveData<List<Mastery>> apply(MasteryQuery input) {
                Log.d(LOG_TAG, "Getting matches");
                return repository.getMasteries(input.getEntryUrlString(), input.getSummonerId());
            }
        });
    }

    public void searchMasteries(MasteryQuery newMasteryQuery) {
        if (mSummonerQuery.getValue() != null) {
            MasteryQuery summonerQuery = mSummonerQuery.getValue();
            if (TextUtils.equals(summonerQuery.getEntryUrlString(), newMasteryQuery.getEntryUrlString())
                    && summonerQuery.getSummonerId() == newMasteryQuery.getSummonerId()) {
                return;
            }
        }
        mSummonerQuery.setValue(newMasteryQuery);
    }

    public LiveData<List<Mastery>> getMasteries() {
        return mMasteries;
    }
}
