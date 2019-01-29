package com.example.android.leaguestats.data.network.api;

import android.arch.lifecycle.LiveData;

import com.example.android.leaguestats.data.network.api.models.MatchListResponse;
import com.example.android.leaguestats.models.Mastery;
import com.example.android.leaguestats.models.Match;
import com.example.android.leaguestats.models.Summoner;
import com.google.gson.JsonElement;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Url;

public interface RetrofitDataService {

    @GET()
    LiveData<ApiResponse<Summoner>> getSummoner(@Url String summonerUrl);

    @GET()
    LiveData<ApiResponse<List<Mastery>>> getMasteries(@Url String masteryUrl);

    @GET()
    LiveData<ApiResponse<MatchListResponse>> getMatchList(@Url String matchListUrl);

    @GET()
    LiveData<ApiResponse<Match>> getMatch(@Url String matchUrl);

    @GET("cdn/{patchVersion}/data/{language}/champion.json")
    LiveData<ApiResponse<JsonElement>> getChampions(@Path("patchVersion") String patchVersion, @Path("language") String language);

    @GET("cdn/{patchVersion}/data/{language}/champion/{id}.json")
    LiveData<ApiResponse<JsonElement>> getChampion(@Path("patchVersion") String patchVersion, @Path("language") String language, @Path("id") String championId);

    @GET("cdn/{patchVersion}/data/{language}/item.json")
    LiveData<ApiResponse<JsonElement>> getItems(@Path("patchVersion") String patchVersion, @Path("language") String language);

    @GET("cdn/{patchVersion}/data/{language}/summoner.json")
    LiveData<ApiResponse<JsonElement>> getSummonerSpells(@Path("patchVersion") String patchVersion, @Path("language") String language);

    @GET("cdn/{patchVersion}/data/{language}/profileicon.json")
    LiveData<ApiResponse<JsonElement>> getIcons(@Path("patchVersion") String patchVersion, @Path("language") String language);

    @GET("api/versions.json")
    LiveData<ApiResponse<JsonElement>> getPatchVersion();

}
