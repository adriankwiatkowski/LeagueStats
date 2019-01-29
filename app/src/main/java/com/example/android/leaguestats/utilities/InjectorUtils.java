package com.example.android.leaguestats.utilities;

import android.content.Context;

import com.example.android.leaguestats.data.LeagueDataRepository;
import com.example.android.leaguestats.data.LeagueSummonerRepository;
import com.example.android.leaguestats.data.database.LeagueDatabase;
import com.example.android.leaguestats.data.network.api.RetrofitInstance;
import com.example.android.leaguestats.viewmodels.ChampionModelFactory;
import com.example.android.leaguestats.viewmodels.ItemModelFactory;
import com.example.android.leaguestats.viewmodels.MainModelFactory;
import com.example.android.leaguestats.viewmodels.SummonerModelFactory;
import com.example.android.leaguestats.viewmodels.SummonerSpellModelFactory;

import retrofit2.Retrofit;

public class InjectorUtils {

    public static LeagueDataRepository provideDataRepository(Context context) {
        LeagueDatabase database = LeagueDatabase.getInstance(context.getApplicationContext());
        Retrofit retrofit = RetrofitInstance.getRetrofitInstance(context.getApplicationContext());
        return LeagueDataRepository.getInstance(context.getApplicationContext(), database, retrofit);
    }

    public static LeagueSummonerRepository provideSummonerRepository(Context context) {
        LeagueDatabase database = LeagueDatabase.getInstance(context.getApplicationContext());
        Retrofit retrofit = RetrofitInstance.getRetrofitInstance(context.getApplicationContext());
        return LeagueSummonerRepository.getInstance(database, retrofit);
    }

    public static ChampionModelFactory provideChampionModelFactory(Context context) {
        LeagueDataRepository repository = provideDataRepository(context.getApplicationContext());
        return new ChampionModelFactory(repository);
    }

    public static ItemModelFactory provideItemModelFactory(Context context) {
        LeagueDataRepository repository = provideDataRepository(context.getApplicationContext());
        return new ItemModelFactory(repository);
    }

    public static SummonerModelFactory provideSummonerModelFactory(Context context) {
        LeagueSummonerRepository repository = provideSummonerRepository(context.getApplicationContext());
        return new SummonerModelFactory(repository);
    }

    public static SummonerSpellModelFactory provideSummonerSpellModelFactory(Context context) {
        LeagueDataRepository repository = provideDataRepository(context.getApplicationContext());
        return new SummonerSpellModelFactory(repository);
    }

    public static MainModelFactory provideMainModelFactory(Context context) {
        LeagueDataRepository repository = provideDataRepository(context.getApplicationContext());
        return new MainModelFactory(repository);
    }
}
