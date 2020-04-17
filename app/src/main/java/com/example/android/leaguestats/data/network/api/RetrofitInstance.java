package com.example.android.leaguestats.data.network.api;

import android.content.Context;

import com.example.android.leaguestats.BuildConfig;
import com.example.android.leaguestats.data.network.api.adapters.LiveDataCallAdapterFactory;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitInstance {

    private static final String BASE_DDRAGON_URL = "https://ddragon.leagueoflegends.com/";

    private static String PERSONAL_API_KEY = BuildConfig.HIDDEN_API_KEY;

    private static Retrofit retrofit;

    public static Retrofit getRetrofitInstance(Context context) {
        if (retrofit == null) {

            int cacheSize = 10 * 1024 * 1024; // 10 MB
            Cache cache = new Cache(context.getApplicationContext().getCacheDir(), cacheSize);

            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);

            OkHttpClient client = new OkHttpClient.Builder()
                    .cache(cache)
                    .addInterceptor(loggingInterceptor)
                    .build();

            retrofit = new retrofit2.Retrofit.Builder()
                    .baseUrl(BASE_DDRAGON_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(new LiveDataCallAdapterFactory())
                    .client(client)
                    .build();
        }

        return retrofit;
    }

    public static String getSummonerUrl(String entryUrlString, String summonerName) {
        return entryUrlString + "/lol/" + "summoner/v4/summoners/by-name/" + summonerName + "?api_key=" + PERSONAL_API_KEY;
    }

    public static String getMasteryUrl(String entryUrlString, String summonerId) {
        return entryUrlString + "/lol/" + "champion-mastery/v4/champion-masteries/by-summoner/" + String.valueOf(summonerId) + "?api_key=" + PERSONAL_API_KEY;
    }

    public static String getMatchListUrl(String entryUrlString, String accountId) {
        return entryUrlString + "/lol/" + "match/v4/matchlists/by-account/" + String.valueOf(accountId) + "?api_key=" + PERSONAL_API_KEY;
    }

    public static String getMatchUrl(String entryUrlString, long gameId) {
        return entryUrlString + "/lol/" + "match/v4/matches/" + String.valueOf(gameId) + "?api_key=" + PERSONAL_API_KEY;
    }
}
