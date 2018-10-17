package com.example.android.leaguestats.utilities;

import android.content.Context;

import com.example.android.leaguestats.AppExecutors;
import com.example.android.leaguestats.data.database.LeagueDatabase;
import com.example.android.leaguestats.data.network.LeagueNetworkDataSource;
import com.example.android.leaguestats.data.LeagueRepository;
import com.example.android.leaguestats.data.network.api.RetrofitInstance;
import com.example.android.leaguestats.viewmodels.ChampionDetailModelFactory;
import com.example.android.leaguestats.viewmodels.ChampionListModelFactory;
import com.example.android.leaguestats.viewmodels.HistoryModelFactory;
import com.example.android.leaguestats.viewmodels.ItemDetailModelFactory;
import com.example.android.leaguestats.viewmodels.ItemListModelFactory;
import com.example.android.leaguestats.viewmodels.MasteryModelFactory;
import com.example.android.leaguestats.viewmodels.SummonerModelFactory;
import com.example.android.leaguestats.viewmodels.SummonerSpellDetailModelFactory;
import com.example.android.leaguestats.viewmodels.SummonerSpellListModelFactory;

import retrofit2.Retrofit;

public class InjectorUtils {

    public static LeagueRepository provideRepository(Context context) {
        LeagueDatabase database = LeagueDatabase.getInstance(context.getApplicationContext());
        AppExecutors executors = AppExecutors.getInstance();
        LeagueNetworkDataSource networkDataSource  =
                LeagueNetworkDataSource.getInstance(context.getApplicationContext());
        Retrofit retrofit = RetrofitInstance.getRetrofitInstance(context.getApplicationContext());

        return LeagueRepository.getInstance(database, networkDataSource, executors, retrofit);
    }

    public static LeagueNetworkDataSource provideNetworkDataSource(Context context) {
        provideRepository(context.getApplicationContext());
        return LeagueNetworkDataSource.getInstance(context.getApplicationContext());
    }

    public static ChampionListModelFactory provideChampionListModelFactory(Context context) {
        LeagueRepository repository = provideRepository(context.getApplicationContext());
        return new ChampionListModelFactory(repository);
    }

    public static ChampionDetailModelFactory provideChampionDetailModelFactory(Context context) {
        LeagueRepository repository = provideRepository(context.getApplicationContext());
        return new ChampionDetailModelFactory(repository);
    }

    public static ItemDetailModelFactory provideItemDetailModelFactory(Context context) {
        LeagueRepository repository = provideRepository(context.getApplicationContext());
        return new ItemDetailModelFactory(repository);
    }

    public static ItemListModelFactory provideItemListModelFactory(Context context) {
        LeagueRepository repository = provideRepository(context.getApplicationContext());
        return new ItemListModelFactory(repository);
    }

    public static MasteryModelFactory provideMasteryModelFactory(Context context) {
        LeagueRepository repository = provideRepository(context.getApplicationContext());
        return new MasteryModelFactory(repository);
    }

    public static SummonerModelFactory provideSummonerModelFactory(Context context) {
        LeagueRepository repository = provideRepository(context.getApplicationContext());
        return new SummonerModelFactory(repository);
    }

    public static HistoryModelFactory provideHistoryModelFactory(Context context) {
        LeagueRepository repository = provideRepository(context.getApplicationContext());
        return new HistoryModelFactory(repository);
    }

    public static SummonerSpellDetailModelFactory provideSummonerSpellDetailModelFactory(Context context, int id) {
        LeagueRepository repository = provideRepository(context.getApplicationContext());
        return new SummonerSpellDetailModelFactory(repository, id);
    }

    public static SummonerSpellListModelFactory provideSummonerSpellListModelFactory(Context context) {
        LeagueRepository repository = provideRepository(context.getApplicationContext());
        return new SummonerSpellListModelFactory(repository);
    }
}
