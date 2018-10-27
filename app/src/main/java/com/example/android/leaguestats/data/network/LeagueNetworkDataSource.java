package com.example.android.leaguestats.data.network;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import com.example.android.leaguestats.data.database.entity.ChampionEntry;
import com.example.android.leaguestats.data.database.entity.IconEntry;
import com.example.android.leaguestats.data.database.entity.ItemEntry;
import com.example.android.leaguestats.data.database.entity.SummonerSpellEntry;
import com.example.android.leaguestats.data.network.api.RetrofitDataService;
import com.example.android.leaguestats.data.network.api.RetrofitInstance;
import com.example.android.leaguestats.data.network.api.models.MasteryResponse;
import com.example.android.leaguestats.data.network.api.models.MatchList;
import com.example.android.leaguestats.data.network.api.models.MatchListResponse;
import com.example.android.leaguestats.data.network.api.models.match.MatchResponse;
import com.example.android.leaguestats.models.Summoner;
import com.example.android.leaguestats.utilities.LeaguePreferences;
import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.Driver;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;
import com.google.gson.JsonElement;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LeagueNetworkDataSource {

    private static final String LOG_TAG = LeagueNetworkDataSource.class.getSimpleName();

    private static final int SYNC_INTERVAL_HOURS = 24;
    private static final int SYNC_INTERVAL_SECONDS = (int) (TimeUnit.HOURS.toSeconds(SYNC_INTERVAL_HOURS));
    private static final int SYNC_FLEXTIME_SECONDS = SYNC_INTERVAL_SECONDS / 3;

    private static final String SYNC_TAG = "job-tag";

    // For Singleton instantiation
    private static final Object LOCK = new Object();
    private static LeagueNetworkDataSource sInstance;

    private boolean mInitialized = false;

    private RetrofitDataService mService;

    private LeagueNetworkDataSource(Context context, Retrofit retrofit) {
        mService = retrofit.create(RetrofitDataService.class);
        scheduleRecurringFetchDataSync(context);
        initializeData(context, false);
    }

    public static LeagueNetworkDataSource getInstance(Context context, Retrofit retrofit) {
        Log.d(LOG_TAG, "Getting the network data source");
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new LeagueNetworkDataSource(context.getApplicationContext(), retrofit);
                Log.d(LOG_TAG, "Made new network data source");
            }
        }
        return sInstance;
    }

    /**
     *
     * @param context
     * @param isLanguageChanged - value to determine whether to fetch data even if patch version is same or not.
     */
    public void initializeData(Context context, boolean isLanguageChanged) {

        if (!isLanguageChanged && mInitialized) {
            return;
        }

        mInitialized = true;

        fetchPatch(context.getApplicationContext(), isLanguageChanged);
    }

    /**
     * Fetches patch and determine whether to fetch data or not.
     * @param context
     * @param isLanguageChanged - value to determine whether to fetch data even if patch version is same or not.
     */
    private void fetchPatch(final Context context, final boolean isLanguageChanged) {

        Call<JsonElement> call = mService.getPatchVersion();

        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                if (response.isSuccessful()) {
                    OpenDataJsonParser openDataJsonParser = new OpenDataJsonParser();
                    String jsonString = response.body().toString();
                    String responsePatch = openDataJsonParser.parsePatchResponse(jsonString);

                    if (isDifferentPatch(context, responsePatch) || isLanguageChanged) {
                        LeaguePreferences.savePatchVersion(context, responsePatch);
                        startFetchDataIntentService(context);
                    }

                    mInitialized = true;
                } else {
                    mInitialized = false;
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                mInitialized = false;
            }
        });
    }

    public LiveData<ChampionEntry[]> fetchChampionData(String patchVersion, String language) {

        OpenDataJsonParser openDataJsonParser = new OpenDataJsonParser();

        final MutableLiveData<ChampionEntry[]> mutableLiveData = new MutableLiveData<>();

        Call<JsonElement> call = mService.getChampions(patchVersion, language);

        try {

            String jsonChampionsResponse = call.execute().body().toString();

            String[] championId = openDataJsonParser.parseChampions(jsonChampionsResponse);

            ChampionEntry[] championEntries = new ChampionEntry[championId.length];

            for (int i = 0; i < championEntries.length; i++) {

                Call<JsonElement> championCall = mService.getChampion(patchVersion, language, championId[i]);

                String jsonChampionResponse = championCall.execute().body().toString();

                ChampionEntry championEntry = openDataJsonParser.parseChampionResponse(jsonChampionResponse);

                championEntries[i] = championEntry;
                Log.d(LOG_TAG, "champion fetched " + String.valueOf(i));
            }

            mutableLiveData.postValue(championEntries);
        } catch (IOException e) {
            e.printStackTrace();
        }

        /*
        Call<JsonElement> championListCall = mService.getChampions(patchVersion, language);

        try {

            String jsonChampionsResponse = championListCall.execute().body().toString();

            String[] championId = openDataJsonParser.parseChampions(jsonChampionsResponse);

            ChampionEntry[] championEntries = new ChampionEntry[championId.length];

            for (int i = 0; i < championEntries.length; i++) {

                Call<JsonElement> championCall = mService.getChampion(patchVersion, language, championId[i]);

                String jsonChampionResponse = championCall.execute().body().toString();

                ChampionEntry championEntry = openDataJsonParser.parseChampionResponse(jsonChampionResponse);

                championEntries[i] = championEntry;
                Log.d(LOG_TAG, "champion fetched " + String.valueOf(i));
            }

            mDb.championDao().deleteChampions();
            mDb.championDao().bulkInsert(championEntries);
        } catch (IOException e) {
            e.printStackTrace();
        }
         */

        return mutableLiveData;
    }

    public LiveData<ItemEntry[]> fetchItemData(String patchVersion, String language) {

        final OpenDataJsonParser openDataJsonParser = new OpenDataJsonParser();

        final MutableLiveData<ItemEntry[]> mutableLiveData = new MutableLiveData<>();

        Call<JsonElement> call = mService.getItems(patchVersion, language);

        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                if (response.isSuccessful()) {
                    Log.d(LOG_TAG, "Item response successful");

                    String itemJsonResponse = response.body().toString();

                    ItemEntry[] itemEntries = openDataJsonParser.parseItemResponse(itemJsonResponse);

                    mutableLiveData.postValue(itemEntries);
                } else {
                    Log.d(LOG_TAG, "call failed against the url: " + call.request().url());
                    Log.d(LOG_TAG, "Item response not successful");
                    mutableLiveData.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Log.d(LOG_TAG, "call failed against the url: " + call.request().url());
                Log.d(LOG_TAG, "Failed to fetch item data");
                Log.d(LOG_TAG, t.getMessage());
                mutableLiveData.postValue(null);
            }
        });

        return mutableLiveData;
    }

    public LiveData<SummonerSpellEntry[]> fetchSummonerSpellData(String patchVersion, String language) {

        final OpenDataJsonParser openDataJsonParser = new OpenDataJsonParser();

        final MutableLiveData<SummonerSpellEntry[]> mutableLiveData = new MutableLiveData<>();

        Call<JsonElement> call = mService.getSummonerSpells(patchVersion, language);

        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                if (response.isSuccessful()) {
                    Log.d(LOG_TAG, "Summoner spell response successful");

                    String itemJsonResponse = response.body().toString();

                    SummonerSpellEntry[] summonerSpellEntries = openDataJsonParser.parseSummonerSpellResponse(itemJsonResponse);

                    mutableLiveData.postValue(summonerSpellEntries);
                } else {
                    Log.d(LOG_TAG, "call failed against the url: " + call.request().url());
                    Log.d(LOG_TAG, "Summoner spell response not successful");
                    mutableLiveData.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Log.d(LOG_TAG, "call failed against the url: " + call.request().url());
                Log.d(LOG_TAG, "Failed to fetch summoner spell data");
                Log.d(LOG_TAG, t.getMessage());
                mutableLiveData.postValue(null);
            }
        });

        return mutableLiveData;
    }

    public LiveData<IconEntry[]> fetchIconData(String patchVersion, String language) {

        final OpenDataJsonParser openDataJsonParser = new OpenDataJsonParser();

        final MutableLiveData<IconEntry[]> mutableLiveData = new MutableLiveData<>();

        Call<JsonElement> call = mService.getIcons(patchVersion, language);

        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                if (response.isSuccessful()) {
                    Log.d(LOG_TAG, "Summoner spell response successful");

                    String itemJsonResponse = response.body().toString();

                    IconEntry[] iconEntries = openDataJsonParser.parseIconResponse(itemJsonResponse);

                    mutableLiveData.postValue(iconEntries);
                } else {
                    Log.d(LOG_TAG, "call failed against the url: " + call.request().url());
                    Log.d(LOG_TAG, "Summoner spell response not successful");
                    mutableLiveData.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Log.d(LOG_TAG, "call failed against the url: " + call.request().url());
                Log.d(LOG_TAG, "Failed to fetch icon data");
                Log.d(LOG_TAG, t.getMessage());
                mutableLiveData.postValue(null);
            }
        });

        return mutableLiveData;
    }

    public LiveData<Summoner> fetchSummoner(final String entryUrlString, String summonerName) {

        final MutableLiveData<Summoner> summonerMutableLiveData = new MutableLiveData<>();

        Call<Summoner> call = mService.getSummoner(RetrofitInstance.getSummonerUrl(entryUrlString, summonerName));

        call.enqueue(new Callback<Summoner>() {

            @Override
            public void onResponse(Call<Summoner> call, Response<Summoner> response) {
                if (response.isSuccessful()) {
                    Log.d(LOG_TAG, "Summoner response successful");
                    response.body().setEntryUrl(entryUrlString);
                    summonerMutableLiveData.postValue(response.body());
                } else {
                    Log.d(LOG_TAG, "call failed against the url: " + call.request().url());
                    Log.d(LOG_TAG, "Summoner response not successful");
                    summonerMutableLiveData.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<Summoner> call, Throwable t) {
                Log.d(LOG_TAG, "call failed against the url: " + call.request().url());
                Log.d(LOG_TAG, "Failed to fetch summoner");
                Log.d(LOG_TAG, t.getMessage());
                summonerMutableLiveData.postValue(null);
            }
        });

        return summonerMutableLiveData;
    }

    public LiveData<List<MasteryResponse>> fetchMasteries(String entryUrlString, long summonerId) {

        final MutableLiveData<List<MasteryResponse>> masteryMutableLiveData = new MutableLiveData<>();

        Call<List<MasteryResponse>> call = mService.getMasteries(RetrofitInstance.getMasteryUrl(entryUrlString, summonerId));

        call.enqueue(new Callback<List<MasteryResponse>>() {

            @Override
            public void onResponse(Call<List<MasteryResponse>> call, Response<List<MasteryResponse>> response) {
                if (response.isSuccessful()) {
                    final List<MasteryResponse> masteries = response.body();
                    masteryMutableLiveData.postValue(masteries);
                } else {
                    Log.d(LOG_TAG, "call failed against the url: " + call.request().url());
                    Log.d(LOG_TAG, "Mastery response not successful");
                    masteryMutableLiveData.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<List<MasteryResponse>> call, Throwable t) {
                Log.d(LOG_TAG, "call failed against the url: " + call.request().url());
                Log.d(LOG_TAG, "Failed to fetch masteries");
                Log.d(LOG_TAG, t.getMessage());
                masteryMutableLiveData.postValue(null);

                // todo log to some central bug tracking service
                if (t instanceof IOException) {
                    Log.d(LOG_TAG, "this is an actual network failure :( inform the user and possibly retry");
                    // logging probably not necessary
                } else {
                    Log.d(LOG_TAG, "conversion issue! big problems :(");
                }
            }
        });

        return masteryMutableLiveData;
    }

    public LiveData<List<MatchList>> fetchMatchList(String entryUrlString, long accountId) {

        final MutableLiveData<List<MatchList>> matchMutableLiveData = new MutableLiveData<>();

        Call<MatchListResponse> matchListCall = mService.getMatchList(RetrofitInstance.getMatchListUrl(entryUrlString, accountId));

        matchListCall.enqueue(new Callback<MatchListResponse>() {

            @Override
            public void onResponse(Call<MatchListResponse> call, Response<MatchListResponse> response) {

                if (response.isSuccessful()) {

                    MatchListResponse matchListResponse = response.body();

                    List<MatchList> matchList = matchListResponse.getMatchList();

                    matchMutableLiveData.postValue(matchList);
                } else {
                    Log.d(LOG_TAG, "call failed against the url: " + call.request().url());
                    Log.d(LOG_TAG, "matchList response not successful");
                    matchMutableLiveData.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<MatchListResponse> call, Throwable t) {
                Log.d(LOG_TAG, "call failed against the url: " + call.request().url());
                Log.d(LOG_TAG, "Failed to fetch matchList");
                Log.d(LOG_TAG, t.getMessage());
                matchMutableLiveData.postValue(null);
            }
        });

        return matchMutableLiveData;
    }

    public LiveData<List<MatchResponse>> fetchMatches(final String entryUrlString, final List<MatchList> matchList, final int matchCount) {

        final MutableLiveData<List<MatchResponse>> matchMutableLiveData = new MutableLiveData<>();

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {

                List<MatchResponse> matchResponseList = new ArrayList<>();

                for (int i = 0; i < matchCount; i++) {

                    Call<MatchResponse> call = mService.getMatch(RetrofitInstance.getMatchUrl(entryUrlString, matchList.get(i).getGameId()));

                    try {
                        Response<MatchResponse> response = call.execute();
                        if (response.isSuccessful()) {
                            MatchResponse matchResponse = response.body();
                            matchResponseList.add(matchResponse);
                            if (matchResponseList.size() == matchCount) {
                                matchMutableLiveData.postValue(matchResponseList);
                            }
                        } else {
                            Log.d(LOG_TAG, "Match response not successful");
                            matchMutableLiveData.postValue(null);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.d(LOG_TAG, "Failed to fetch match");
                        Log.d(LOG_TAG, e.getMessage());
                        matchMutableLiveData.postValue(null);
                    }
                }

                return null;
            }
        }.execute();

        /*
        final List<MatchResponse> matchResponseList = new ArrayList<>();

        for (int i = 0; i < MATCH_COUNT; i++) {

            Call<MatchResponse> call = mService.getMatch(RetrofitInstance.getMatchUrl(entryUrlString, matchList.get(i).getGameId()));

            call.enqueue(new Callback<MatchResponse>() {
                @Override
                public void onResponse(Call<MatchResponse> call, Response<MatchResponse> response) {

                    if (response.isSuccessful()) {
                        matchResponseList.add(response.body());
                        if (matchResponseList.size() == MATCH_COUNT) {
                            setMatches(matchMutableLiveData, matchResponseList, summonerId);
                        }
                    } else {
                        Log.d(LOG_TAG, "call failed against the url: " + call.request().url());
                        Log.d(LOG_TAG, "match response not successful");
                        matchMutableLiveData.postValue(null);
                    }
                }

                @Override
                public void onFailure(Call<MatchResponse> call, Throwable t) {
                    Log.d(LOG_TAG, "call failed against the url: " + call.request().url());
                    Log.d(LOG_TAG, "Failed to fetch match");
                    Log.d(LOG_TAG, t.getMessage());
                    matchMutableLiveData.postValue(null);
                }
            });
        }
        */

        return matchMutableLiveData;
    }

    private boolean isDifferentPatch(Context context, String responsePatch) {
        String savedPatchVersion = LeaguePreferences.getPatchVersion(context);
        if (!TextUtils.isEmpty(responsePatch)) {
            return !responsePatch.equals(savedPatchVersion);
        }
        return false;
    }

    private void startFetchDataIntentService(Context context) {
        Intent intentToFetch = new Intent(context, LeagueSyncIntentService.class);
        context.startService(intentToFetch);
        Log.d(LOG_TAG, "Service created");
    }

    private void scheduleRecurringFetchDataSync(Context context) {
        Driver driver = new GooglePlayDriver(context);
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(driver);
        Job syncLeagueJob = dispatcher.newJobBuilder()
                .setService(LeagueFirebaseJobService.class)
                .setTag(SYNC_TAG)
                .setConstraints(
                        Constraint.ON_UNMETERED_NETWORK,
                        Constraint.DEVICE_CHARGING)
                .setLifetime(Lifetime.FOREVER)
                .setRecurring(true)
                .setTrigger(Trigger.executionWindow(
                        SYNC_INTERVAL_SECONDS,
                        SYNC_INTERVAL_SECONDS + SYNC_FLEXTIME_SECONDS))
                .setReplaceCurrent(true)
                .build();
        dispatcher.schedule(syncLeagueJob);
        Log.d(LOG_TAG, "Job scheduled");
    }
}
