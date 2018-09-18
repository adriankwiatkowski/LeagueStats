package com.example.android.leaguestats.utilities;

import android.content.Context;

import com.example.android.leaguestats.AppExecutors;
import com.example.android.leaguestats.database.AppDatabase;
import com.example.android.leaguestats.sync.LeagueNetworkDataSource;
import com.example.android.leaguestats.sync.LeagueRepository;
import com.example.android.leaguestats.viewModels.ChampionDetailModelFactory;
import com.example.android.leaguestats.viewModels.ChampionListModelFactory;
import com.example.android.leaguestats.viewModels.HistoryModelFactory;
import com.example.android.leaguestats.viewModels.ItemDetailModelFactory;
import com.example.android.leaguestats.viewModels.ItemListModelFactory;
import com.example.android.leaguestats.viewModels.MasteryModelFactory;
import com.example.android.leaguestats.viewModels.SummonerModelFactory;
import com.example.android.leaguestats.viewModels.SummonerSpellDetailModelFactory;
import com.example.android.leaguestats.viewModels.SummonerSpellListModelFactory;

public class InjectorUtils {

    public static LeagueRepository provideRepository(Context context) {
        AppDatabase database = AppDatabase.getInstance(context.getApplicationContext());
        AppExecutors executors = AppExecutors.getInstance();
        LeagueNetworkDataSource networkDataSource  =
                LeagueNetworkDataSource.getInstance(context.getApplicationContext(), executors);

        return LeagueRepository.getInstance(
                database.championDao(), database.itemDao(), database.iconDao(),
                database.summonerSpellDao(), networkDataSource, executors);
    }

    public static LeagueNetworkDataSource provideNetworkDataSource(Context context) {
        provideRepository(context.getApplicationContext());
        AppExecutors executors = AppExecutors.getInstance();
        return LeagueNetworkDataSource.getInstance(context.getApplicationContext(), executors);
    }

    public static ChampionListModelFactory provideChampionListModelFactory(Context context) {
        LeagueRepository repository = provideRepository(context.getApplicationContext());
        return new ChampionListModelFactory(repository);
    }

    public static ChampionDetailModelFactory provideChampionDetailModelFactory(Context context) {
        LeagueRepository repository = provideRepository(context.getApplicationContext());
        return new ChampionDetailModelFactory(repository);
    }

    public static ItemDetailModelFactory provideItemDetailModelFactory(Context context, long id) {
        LeagueRepository repository = provideRepository(context.getApplicationContext());
        return new ItemDetailModelFactory(repository, id);
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

    public static SummonerSpellDetailModelFactory provideSummonerSpellDetailModelFactory(Context context, String id) {
        LeagueRepository repository = provideRepository(context.getApplicationContext());
        return new SummonerSpellDetailModelFactory(repository, id);
    }

    public static SummonerSpellListModelFactory provideSummonerSpellListModelFactory(Context context) {
        LeagueRepository repository = provideRepository(context.getApplicationContext());
        return new SummonerSpellListModelFactory(repository);
    }

    public static HistoryModelFactory provideHistoryModelFactory(Context context) {
        LeagueRepository repository = provideRepository(context.getApplicationContext());
        return new HistoryModelFactory(repository);
    }
}
