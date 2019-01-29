package com.example.android.leaguestats.viewmodels;

import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.android.leaguestats.data.LeagueDataRepository;
import com.example.android.leaguestats.data.database.entity.SummonerSpellEntry;
import com.example.android.leaguestats.models.SummonerSpell;

import java.util.List;

public class SummonerSpellModel extends ViewModel {

    private static final String LOG_TAG = SummonerSpellModel.class.getSimpleName();

    private LiveData<List<SummonerSpell>> mSummonerSpells;
    private LiveData<SummonerSpellEntry> mSummonerSpell;

    private final MutableLiveData<Long> mIdQuery = new MutableLiveData<>();
    private final MutableLiveData<String> mNameQuery = new MutableLiveData<>();

    public SummonerSpellModel(final LeagueDataRepository repository) {
        Log.d(LOG_TAG, "Retrieving SummonerSpells from database");
        mSummonerSpells = repository.getSummonerSpells();

        final MediatorLiveData mediatorLiveData = new MediatorLiveData();
        mediatorLiveData.addSource(mIdQuery, new Observer() {
            @Override
            public void onChanged(@Nullable Object o) {
                mediatorLiveData.setValue(o);
            }
        });
        mediatorLiveData.addSource(mNameQuery, new Observer() {
            @Override
            public void onChanged(@Nullable Object o) {
                mediatorLiveData.setValue(o);
            }
        });

        mSummonerSpell = Transformations.switchMap(mediatorLiveData, new Function() {
            @Override
            public Object apply(Object input) {
                Log.d(LOG_TAG, "Getting new SummonerSpell from database");
                LiveData<SummonerSpellEntry> summonerSpellEntry = null;
                if (input instanceof Long || input instanceof Integer) {
                    long summonerSpellId = (long) input;
                    summonerSpellEntry = repository.getSummonerSpell(summonerSpellId);
                } else if (input instanceof String) {
                    String summonerSpellName = (String) input;
                    summonerSpellEntry = repository.getSummonerSpell(summonerSpellName);
                }
                return summonerSpellEntry;
            }
        });
    }

    public LiveData<List<SummonerSpell>> getSummonerSpells() {
        return mSummonerSpells;
    }

    public void initSummonerSpell(long summonerSpellId) {
        mIdQuery.setValue(summonerSpellId);
    }

    public LiveData<SummonerSpellEntry> getSummonerSpell() {
        return mSummonerSpell;
    }

    public void initSummonerSpell(String query) {
        mNameQuery.setValue(query);
    }
}
