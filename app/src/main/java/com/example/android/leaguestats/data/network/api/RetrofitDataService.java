package com.example.android.leaguestats.data.network.api;

import com.example.android.leaguestats.data.network.api.models.MasteryResponse;
import com.example.android.leaguestats.data.network.api.models.MatchListResponse;
import com.example.android.leaguestats.data.network.api.models.match.MatchResponse;
import com.example.android.leaguestats.models.Summoner;
import com.google.gson.JsonElement;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Url;

public interface RetrofitDataService {

    @GET()
    Call<Summoner> getSummoner(@Url String summonerUrl);

    @GET()
    Call<List<MasteryResponse>> getMasteries(@Url String masteryUrl);

    @GET()
    Call<MatchListResponse> getMatchList(@Url String matchListUrl);

    @GET()
    Call<MatchResponse> getMatch(@Url String matchUrl);

    @GET("cdn/{patchVersion}/data/{language}/champion.json")
    Call<JsonElement> getChampions(@Path("patchVersion") String patchVersion, @Path("language") String language);

    @GET("cdn/{patchVersion}/data/{language}/champion/{id}.json")
    Call<JsonElement> getChampion(@Path("patchVersion") String patchVersion, @Path("language") String language, @Path("id") String championId);

    @GET("cdn/{patchVersion}/data/{language}/item.json")
    Call<JsonElement> getItems(@Path("patchVersion") String patchVersion, @Path("language") String language);

    @GET("cdn/{patchVersion}/data/{language}/summoner.json")
    Call<JsonElement> getSummonerSpells(@Path("patchVersion") String patchVersion, @Path("language") String language);

    @GET("cdn/{patchVersion}/data/{language}/profileicon.json")
    Call<JsonElement> getIcons(@Path("patchVersion") String patchVersion, @Path("language") String language);

    @GET("api/versions.json")
    Call<JsonElement> getPatchVersion();

}
