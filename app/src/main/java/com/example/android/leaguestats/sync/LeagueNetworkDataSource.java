package com.example.android.leaguestats.sync;

import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.android.leaguestats.AppExecutors;
import com.example.android.leaguestats.database.entity.ChampionEntry;
import com.example.android.leaguestats.database.entity.IconEntry;
import com.example.android.leaguestats.database.entity.ItemEntry;
import com.example.android.leaguestats.database.entity.SummonerSpellEntry;
import com.example.android.leaguestats.models.Mastery;
import com.example.android.leaguestats.models.Match;
import com.example.android.leaguestats.models.MatchList;
import com.example.android.leaguestats.models.Summoner;
import com.example.android.leaguestats.utilities.NetworkUtils;
import com.example.android.leaguestats.utilities.OpenDataJsonParser;
import com.example.android.leaguestats.utilities.PreferencesUtils;
import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.Driver;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class LeagueNetworkDataSource {

    private static final String LOG_TAG = LeagueNetworkDataSource.class.getSimpleName();

    private static final int SYNC_INTERVAL_HOURS = 24;
    private static final int SYNC_INTERVAL_SECONDS = (int) (TimeUnit.HOURS.toSeconds(SYNC_INTERVAL_HOURS));
    private static final int SYNC_FLEXTIME_SECONDS = SYNC_INTERVAL_SECONDS / 3;

    private static final String SYNC_TAG = "job-tag";

    // For Singleton instantiation
    private static final Object LOCK = new Object();
    private static LeagueNetworkDataSource sInstance;
    private final Context mContext;

    private final AppExecutors mExecutors;

    private final MutableLiveData<ChampionEntry[]> mDownloadedChampions;
    private final MutableLiveData<ItemEntry[]> mDownloadedItems;
    private final MutableLiveData<IconEntry[]> mDownloadedIcons;
    private final MutableLiveData<SummonerSpellEntry[]> mDownloadedSummonerSpells;
    private final MutableLiveData<Summoner> mDownloadedSummoner;
    private final MutableLiveData<String> mDownloadedEntryUrlString;
    private final MutableLiveData<List<Mastery>> mDownloadedMasteries;
    private final MutableLiveData<List<Match>> mDownloadedMatches;

    private LeagueNetworkDataSource(Context context, AppExecutors executors) {
        mContext = context;
        mExecutors = executors;
        mDownloadedChampions = new MutableLiveData<>();
        mDownloadedItems = new MutableLiveData<>();
        mDownloadedIcons = new MutableLiveData<>();
        mDownloadedSummonerSpells = new MutableLiveData<>();
        mDownloadedSummoner = new MutableLiveData<>();
        mDownloadedEntryUrlString = new MutableLiveData<>();
        mDownloadedMasteries = new MutableLiveData<>();
        mDownloadedMatches = new MutableLiveData<>();
    }

    public static LeagueNetworkDataSource getInstance(Context context, AppExecutors executors) {
        Log.d(LOG_TAG, "Getting the network data source");
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new LeagueNetworkDataSource(context.getApplicationContext(), executors);
                Log.d(LOG_TAG, "Made new network data source");
            }
        }
        return sInstance;
    }

    public void startFetchDataService() {
        Intent intentToFetch = new Intent(mContext, LeagueSyncIntentService.class);
        mContext.startService(intentToFetch);
        Log.d(LOG_TAG, "Service created");
    }

    public void scheduleRecurringFetchDataSync() {
        Driver driver = new GooglePlayDriver(mContext);
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

    void fetchData() {
        Log.d(LOG_TAG, "Fetch data started");
        mExecutors.networkIO().execute(new Runnable() {
            @Override
            public void run() {
                try {

                    OpenDataJsonParser openDataJsonParser = new OpenDataJsonParser();

                    boolean isFetchNeeded = false;

                    URL patchRequestUrl = NetworkUtils.getPatchUrl();
                    String jsonPatchResponse = NetworkUtils.getResponse(patchRequestUrl);

                    String responsePatch = openDataJsonParser.parsePatchResponse(jsonPatchResponse);
                    String currentPatch = PreferencesUtils.getPatchVersion(mContext);

                    if (responsePatch != null) {
                        if (!responsePatch.isEmpty()) {
                            if (!responsePatch.equals(currentPatch)) {
                                PreferencesUtils.savePatchVersion(mContext, responsePatch);
                                isFetchNeeded = true;
                            }
                        }
                    }

                    if (!isFetchNeeded) {
                        Log.d(LOG_TAG, "fetching isnt needed");
                        // TODO
                        return;
                    }

                    URL championRequestUrl = NetworkUtils.getChampionUrl(mContext);
                    String jsonChampionResponse = NetworkUtils.getResponse(championRequestUrl);
                    String[] championId = openDataJsonParser.parseChampions(jsonChampionResponse);

                    ChampionEntry[] championEntries = new ChampionEntry[championId.length];
                    for (int i = 0; i < championId.length; i++) {
                        URL specificChampionRequestUrl = NetworkUtils.getChampionUrl(mContext, championId[i]);
                        String jsonSpecificChampionResponse = NetworkUtils.getResponse(specificChampionRequestUrl);
                        ChampionEntry championEntry = openDataJsonParser.parseChampionResponse(jsonSpecificChampionResponse);
                        championEntries[i] = championEntry;
                        Log.d(LOG_TAG, "champion fetched " + String.valueOf(i));
                    }

                    if (championEntries.length != 0) {
                        Log.d(LOG_TAG, "champion JSON not null and has " + championEntries.length
                                + " values");
                        mDownloadedChampions.postValue(championEntries);
                    }

                    URL itemRequestUrl = NetworkUtils.getItemUrl(mContext);
                    String jsonItemResponse = NetworkUtils.getResponse(itemRequestUrl);
                    ItemEntry[] itemEntries = openDataJsonParser.parseItemResponse(jsonItemResponse);

                    URL iconRequestUrl = NetworkUtils.getIconUrl(mContext);
                    String jsonIconResponse = NetworkUtils.getResponse(iconRequestUrl);
                    IconEntry[] iconEntries = openDataJsonParser.parseIconResponse(jsonIconResponse);

                    URL summonerSpellRequestUrl = NetworkUtils.getSummonerSpellUrl(mContext);
                    String jsonSummonerSpellResponse = NetworkUtils.getResponse(summonerSpellRequestUrl);
                    SummonerSpellEntry[] summonerSpellEntries = openDataJsonParser.parseSummonerSpellResponse(jsonSummonerSpellResponse);

                    mDownloadedItems.postValue(itemEntries);
                    mDownloadedIcons.postValue(iconEntries);
                    mDownloadedSummonerSpells.postValue(summonerSpellEntries);

                    //DataResponse response = openDataJsonParser.parseResponse(jsonChampionResponse,
                    //        jsonItemResponse, jsonIconResponse, jsonSummonerSpellResponse);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    void fetchSummoner(final String entryUrlString, final String summonerName) {
        Log.d(LOG_TAG, "Fetch summoner started");
        mExecutors.networkIO().execute(new Runnable() {
            @Override
            public void run() {
                try {

                    OpenDataJsonParser openDataJsonParser = new OpenDataJsonParser();

                    URL summonerRequestUrl = NetworkUtils.getSummonerUrl(entryUrlString, summonerName);
                    String jsonSummonerResponse = NetworkUtils.getResponse(summonerRequestUrl);
                    Summoner response = openDataJsonParser.parseSummonerResponse(jsonSummonerResponse);

                    if (response != null) {
                        Log.d(LOG_TAG, "summoner JSON not null");
                        mDownloadedSummoner.postValue(response);
                        mDownloadedEntryUrlString.postValue(entryUrlString);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    void fetchMasteries(final String entryUrlString, final long summonerId) {
        Log.d(LOG_TAG, "Fetch masteries started");
        mExecutors.networkIO().execute(new Runnable() {
            @Override
            public void run() {
                try {

                    final OpenDataJsonParser openDataJsonParser = new OpenDataJsonParser();

                    URL masteryRequestUrl = NetworkUtils.getMasteryUrl(entryUrlString, summonerId);
                    String jsonMasteryResponse = NetworkUtils.getResponse(masteryRequestUrl);
                    List<Mastery> response = openDataJsonParser.parseMasteryResponse(jsonMasteryResponse);

                    if (response != null) {
                        Log.d(LOG_TAG, "masteries JSON not null");
                        mDownloadedMasteries.postValue(response);
                        mDownloadedEntryUrlString.postValue(entryUrlString);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    void fetchMatches(final String entryUrlString, final long accountId) {
        Log.d(LOG_TAG, "Fetch matches started");
        mExecutors.networkIO().execute(new Runnable() {
            @Override
            public void run() {
                try {

                    final OpenDataJsonParser openDataJsonParser = new OpenDataJsonParser();

                    URL matchListRequestUrl = NetworkUtils.getMatchListUrl(entryUrlString, accountId);
                    String matchListResponse = NetworkUtils.getResponse(matchListRequestUrl);

                    List<MatchList> matchList = openDataJsonParser.parseMatchListResponse(matchListResponse);

                    List<Match> matches = new ArrayList<>();
                    // TODO Remove constant size.
                    for (int i = 0; i < 20; i++) {
                        URL matchRequestUrl = NetworkUtils.getMatchUrl(entryUrlString, matchList.get(i).getGameId());
                        String matchResponse = NetworkUtils.getResponse(matchRequestUrl);
                        matches.add(openDataJsonParser.parseMatchResponse(matchResponse));
                    }

                    // TODO
                    if (!matches.isEmpty()) {
                        Log.d(LOG_TAG, "matches JSON not null");
                        mDownloadedMatches.postValue(matches);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public MutableLiveData<ChampionEntry[]> getChampions() {
        return mDownloadedChampions;
    }

    public MutableLiveData<ItemEntry[]> getItems() {
        return mDownloadedItems;
    }

    public MutableLiveData<IconEntry[]> getIcons() {
        return mDownloadedIcons;
    }

    public MutableLiveData<SummonerSpellEntry[]> getSummonerSpells() {
        return mDownloadedSummonerSpells;
    }

    public MutableLiveData<Summoner> getSummoner() {
        return mDownloadedSummoner;
    }

    public MutableLiveData<String> getEntryUrlString() {
        return mDownloadedEntryUrlString;
    }

    public MutableLiveData<List<Mastery>> getMasteries() {
        return mDownloadedMasteries;
    }

    public MutableLiveData<List<Match>> getMatches() {
        return mDownloadedMatches;
    }
}
