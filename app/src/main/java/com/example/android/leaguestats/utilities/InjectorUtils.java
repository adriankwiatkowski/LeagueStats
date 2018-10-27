package com.example.android.leaguestats.utilities;

import android.content.Context;

import com.example.android.leaguestats.data.database.LeagueDatabase;
import com.example.android.leaguestats.data.network.LeagueNetworkDataSource;
import com.example.android.leaguestats.data.LeagueRepository;
import com.example.android.leaguestats.data.network.api.RetrofitInstance;
import com.example.android.leaguestats.viewmodels.ChampionModelFactory;
import com.example.android.leaguestats.viewmodels.ItemModelFactory;
import com.example.android.leaguestats.viewmodels.SummonerModelFactory;
import com.example.android.leaguestats.viewmodels.SummonerSpellModelFactory;

import retrofit2.Retrofit;

public class InjectorUtils {

    public static LeagueRepository provideRepository(Context context) {
        LeagueDatabase database = LeagueDatabase.getInstance(context.getApplicationContext());
        Retrofit retrofit = RetrofitInstance.getRetrofitInstance(context.getApplicationContext());
        LeagueNetworkDataSource networkDataSource  =
                LeagueNetworkDataSource.getInstance(context.getApplicationContext(), retrofit);

        return LeagueRepository.getInstance(context.getApplicationContext(), database, networkDataSource, retrofit);
    }

    public static LeagueNetworkDataSource provideNetworkDataSource(Context context) {
        provideRepository(context.getApplicationContext());
        Retrofit retrofit = RetrofitInstance.getRetrofitInstance(context.getApplicationContext());
        return LeagueNetworkDataSource.getInstance(context.getApplicationContext(), retrofit);
    }

    public static ChampionModelFactory provideChampionModelFactory(Context context) {
        LeagueRepository repository = provideRepository(context.getApplicationContext());
        return new ChampionModelFactory(repository);
    }

    public static ItemModelFactory provideItemModelFactory(Context context) {
        LeagueRepository repository = provideRepository(context.getApplicationContext());
        return new ItemModelFactory(repository);
    }

    public static SummonerModelFactory provideSummonerModelFactory(Context context) {
        LeagueRepository repository = provideRepository(context.getApplicationContext());
        return new SummonerModelFactory(repository);
    }

    public static SummonerSpellModelFactory provideSummonerSpellModelFactory(Context context) {
        LeagueRepository repository = provideRepository(context.getApplicationContext());
        return new SummonerSpellModelFactory(repository);
    }
}
