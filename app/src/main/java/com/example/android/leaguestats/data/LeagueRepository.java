package com.example.android.leaguestats.data;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.android.leaguestats.data.network.api.models.MasteryResponse;
import com.example.android.leaguestats.data.network.api.models.MatchListResponse;
import com.example.android.leaguestats.data.network.api.RetrofitDataService;
import com.example.android.leaguestats.data.network.api.RetrofitInstance;
import com.example.android.leaguestats.data.network.api.models.match.MatchResponse;
import com.example.android.leaguestats.data.network.api.models.match.Participant;
import com.example.android.leaguestats.data.database.LeagueDatabase;
import com.example.android.leaguestats.data.database.entity.ChampionEntry;
import com.example.android.leaguestats.data.database.entity.IconEntry;
import com.example.android.leaguestats.data.database.entity.ItemEntry;
import com.example.android.leaguestats.data.database.entity.SummonerSpellEntry;
import com.example.android.leaguestats.data.network.LeagueNetworkDataSource;
import com.example.android.leaguestats.models.Mastery;
import com.example.android.leaguestats.models.Match;
import com.example.android.leaguestats.data.network.api.models.MatchList;
import com.example.android.leaguestats.models.Summoner;
import com.example.android.leaguestats.data.network.OpenDataJsonParser;
import com.example.android.leaguestats.utilities.LeaguePreferences;
import com.google.gson.JsonElement;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LeagueRepository {

    private static final String LOG_TAG = LeagueRepository.class.getSimpleName();

    private static final int MATCH_COUNT = 10;

    // For Singleton instantiation
    private static final Object LOCK = new Object();
    private static LeagueRepository sInstance;
    private LeagueDatabase mDb;
    private final LeagueNetworkDataSource mLeagueNetworkDataSource;
    private boolean mInitialized = false;

    private RetrofitDataService mService;

    private LeagueRepository(Context context, LeagueDatabase leagueDatabase,
                             LeagueNetworkDataSource leagueNetworkDataSource,
                             Retrofit retrofit) {
        mService = retrofit.create(RetrofitDataService.class);
        mDb = leagueDatabase;
        mLeagueNetworkDataSource = leagueNetworkDataSource;
        initializeData(context.getApplicationContext());
    }

    public synchronized static LeagueRepository getInstance(Context context, LeagueDatabase leagueDatabase,
                                                            LeagueNetworkDataSource leagueNetworkDataSource,
                                                            Retrofit retrofit) {
        Log.d(LOG_TAG, "Getting the repository");
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new LeagueRepository(context.getApplicationContext(), leagueDatabase, leagueNetworkDataSource, retrofit);
                Log.d(LOG_TAG, "Made new repository");
            }
        }
        return sInstance;
    }

    private synchronized void initializeData(final Context context) {
        if (mInitialized) return;
        Log.d(LOG_TAG, "Initializing data");
        mInitialized = true;
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                int championCount = mDb.championDao().countAllChampions();
                int itemCount = mDb.itemDao().countAllItems();
                int summonerSpellCount = mDb.summonerSpellDao().countAllSummonerSpells();
                int iconCount = mDb.iconDao().countAllIcons();
                if (championCount <= 0 || itemCount <= 0 || summonerSpellCount <= 0 || iconCount <= 0) {
                  mLeagueNetworkDataSource.initializeData(context, true);
                }
                return null;
            }
        }.execute();
    }

    public void fetchData(Context context) {
        Log.d(LOG_TAG, "Fetch data started");

        String patchVersion = LeaguePreferences.getPatchVersion(context);
        String language = LeaguePreferences.getLanguage(context);
        OpenDataJsonParser openDataJsonParser = new OpenDataJsonParser();

        fetchChampionData(patchVersion, language, openDataJsonParser);
        fetchItemData(patchVersion, language, openDataJsonParser);
        fetchSummonerSpellData(patchVersion, language, openDataJsonParser);
        fetchIconData(patchVersion, language, openDataJsonParser);
    }

    private void fetchChampionData(final String patchVersion, final String language, final OpenDataJsonParser openDataJsonParser) {

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
    }

    private void fetchItemData(String patchVersion, String language, final OpenDataJsonParser openDataJsonParser) {

        Call<JsonElement> call = mService.getItems(patchVersion, language);

        try {

            String itemJsonResponse = call.execute().body().toString();

            ItemEntry[] itemEntries = openDataJsonParser.parseItemResponse(itemJsonResponse);

            mDb.itemDao().deleteItems();
            mDb.itemDao().bulkInsert(itemEntries);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void fetchSummonerSpellData(String patchVersion, String language, final OpenDataJsonParser openDataJsonParser) {

        Call<JsonElement> call = mService.getSummonerSpells(patchVersion, language);

        try {

            String summonerSpellJsonResponse = call.execute().body().toString();

            SummonerSpellEntry[] summonerSpellEntries = openDataJsonParser.parseSummonerSpellResponse(summonerSpellJsonResponse);

            mDb.summonerSpellDao().deleteSpells();
            mDb.summonerSpellDao().bulkInsert(summonerSpellEntries);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void fetchIconData(String patchVersion, String language, final OpenDataJsonParser openDataJsonParser) {

        Call<JsonElement> call = mService.getIcons(patchVersion, language);

        try {

            String iconJsonResponse = call.execute().body().toString();

            IconEntry[] iconEntries = openDataJsonParser.parseIconResponse(iconJsonResponse);

            mDb.iconDao().deleteIcons();
            mDb.iconDao().bulkInsert(iconEntries);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public LiveData<Summoner> getSummoner(final String entryUrlString, String summonerName) {
        Log.d(LOG_TAG, "Getting summoner");

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

    // TODO switchMap to the rescue?
    public LiveData<List<Mastery>> getMasteries(String entryUrlString, long summonerId) {
        Log.d(LOG_TAG, "Getting masteries");

        final MutableLiveData<List<Mastery>> masteryMutableLiveData = new MutableLiveData<>();

        Call<List<MasteryResponse>> call = mService.getMasteries(RetrofitInstance.getMasteryUrl(entryUrlString, summonerId));

        call.enqueue(new Callback<List<MasteryResponse>>() {

            @Override
            public void onResponse(Call<List<MasteryResponse>> call, Response<List<MasteryResponse>> response) {
                if (response.isSuccessful()) {
                    final List<MasteryResponse> masteries = response.body();
                    postMasteryValue(masteryMutableLiveData, masteries);
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

    private void postMasteryValue(final MutableLiveData<List<Mastery>> masteryMutableLiveData, final List<MasteryResponse> masteryResponses) {

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                final int[] championId = new int[masteryResponses.size()];
                for (int i = 0; i < masteryResponses.size(); i++) {
                    championId[i] = masteryResponses.get(i).getChampionId();
                }

                List<ChampionEntry> championEntries = getChampionEntries(championId);

                List<Mastery> masteryList = new ArrayList<>();

                for (int i = 0; i < masteryResponses.size(); i++) {
                    MasteryResponse masteryResponse = masteryResponses.get(i);
                    Mastery mastery = new Mastery(masteryResponse, championEntries);
                    masteryList.add(mastery);
                }

                masteryMutableLiveData.postValue(masteryList);
                Log.d(LOG_TAG, "Masteries changed");
                return null;
            }
        }.execute();
    }

    public LiveData<List<Match>> getMatches(final String entryUrlString, long accountId, final long summonerId) {
        Log.d(LOG_TAG, "Getting matches");

        final MutableLiveData<List<Match>> matchMutableLiveData = new MutableLiveData<>();

        Call<MatchListResponse> matchListCall = mService.getMatchList(RetrofitInstance.getMatchListUrl(entryUrlString, accountId));

        matchListCall.enqueue(new Callback<MatchListResponse>() {

            @Override
            public void onResponse(Call<MatchListResponse> call, Response<MatchListResponse> response) {

                if (response.isSuccessful()) {

                    MatchListResponse matchListResponse = response.body();

                    List<MatchList> matchList = matchListResponse.getMatchList();

                    getMatches(matchMutableLiveData, matchList, entryUrlString, summonerId);
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

    private void getMatches(final MutableLiveData<List<Match>> matchMutableLiveData,
                            final List<MatchList> matchList, final String entryUrlString,
                            final long summonerId) {

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {

                List<MatchResponse> matchResponseList = new ArrayList<>();

                for (int i = 0; i < MATCH_COUNT; i++) {

                    Call<MatchResponse> call = mService.getMatch(RetrofitInstance.getMatchUrl(entryUrlString, matchList.get(i).getGameId()));

                    try {
                        Response<MatchResponse> response = call.execute();
                        if (response.isSuccessful()) {
                            MatchResponse matchResponse = response.body();
                            matchResponseList.add(matchResponse);
                            if (matchResponseList.size() == MATCH_COUNT) {
                                // Get info from database and update liveData.
                                setMatches(matchMutableLiveData, matchResponseList, summonerId);
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
    }

    private void setMatches(final MutableLiveData<List<Match>> matchMutableLiveData, final List<MatchResponse> matchResponseList, final long summonerId) {

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {

                List<Match> matchList = new ArrayList<>();

                List<Integer> championIdList = getChampionIdList(matchResponseList);
                List<ChampionEntry> championEntries = getMatchListChampionEntry(championIdList);

                List<Integer> summonerSpell1IdList = getSummonerSpellIdList(matchResponseList, true);
                List<SummonerSpellEntry> summonerSpell1Entries = getMatchSummonerSpells(summonerSpell1IdList);

                List<Integer> summonerSpell2IdList = getSummonerSpellIdList(matchResponseList, false);
                List<SummonerSpellEntry> summonerSpell2Entries = getMatchSummonerSpells(summonerSpell2IdList);

                List<Integer> itemIdList = getItemIdList(matchResponseList);
                List<ItemEntry> itemEntries = getMatchListItemEntry(itemIdList);

                int count = 0;
                int itemCount = 0;
                for (int i = 0; i < matchResponseList.size(); i++) {

                    MatchResponse matchResponse = matchResponseList.get(i);

                    int participantsSize = matchResponse.getParticipants().size();

                    List<ChampionEntry> championEntryList = new ArrayList<>();
                    List<SummonerSpellEntry> summonerSpellEntries = new ArrayList<>();
                    List<SummonerSpellEntry> summonerSpell2List = new ArrayList<>();
                    List<ItemEntry> itemEntryList = new ArrayList<>();

                    for (int j = 0; j < participantsSize; j++) {

                        championEntryList.add(championEntries.get(count));
                        summonerSpellEntries.add(summonerSpell1Entries.get(count));
                        summonerSpell2List.add(summonerSpell2Entries.get(count));

                        count++;
                    }

                    // There is always 7 items.
                    for (int j = 0; j < 7 * participantsSize; j++) {

                        itemEntryList.add(itemEntries.get(itemCount));

                        itemCount++;
                    }

                    matchList.add(new Match(matchResponse, championEntryList, summonerSpellEntries,
                            summonerSpell2List, itemEntryList, summonerId));
                }

                matchMutableLiveData.postValue(matchList);
                Log.d(LOG_TAG, "Matches changed");
                return null;
            }
        }.execute();
    }

    private List<Integer> getChampionIdList(List<MatchResponse> matchResponseArray) {

        List<Integer> championIdList = new ArrayList<>();

        for (MatchResponse matchResponse : matchResponseArray) {
            List<Participant> participants = matchResponse.getParticipants();
            for (Participant participant : participants) {
                championIdList.add(participant.getChampionId());
            }
        }

        return championIdList;
    }

    private List<Integer> getSummonerSpellIdList(List<MatchResponse> matchResponseList, boolean isFirst) {

        List<Integer> summonerSpellIdList = new ArrayList<>();

        for (MatchResponse matchResponse : matchResponseList) {
            List<Participant> participants = matchResponse.getParticipants();
            for (Participant participant : participants) {
                if (isFirst) {
                    summonerSpellIdList.add(participant.getSpell1Id());
                } else {
                    summonerSpellIdList.add(participant.getSpell2Id());
                }
            }
        }

        return summonerSpellIdList;
    }

    private List<Integer> getItemIdList(List<MatchResponse> matchResponseList) {

        List<Integer> itemIdList = new ArrayList<>();

        for (MatchResponse matchResponse : matchResponseList) {
            List<Participant> participants = matchResponse.getParticipants();
            for (Participant participant : participants) {
                itemIdList.add(participant.getStats().getItem0());
                itemIdList.add(participant.getStats().getItem1());
                itemIdList.add(participant.getStats().getItem2());
                itemIdList.add(participant.getStats().getItem3());
                itemIdList.add(participant.getStats().getItem4());
                itemIdList.add(participant.getStats().getItem5());
                itemIdList.add(participant.getStats().getItem6());
            }
        }

        return itemIdList;
    }

    private List<ChampionEntry> getMatchListChampionEntry(List<Integer> championIdList) {

        int[] id = getIdArrayWithNoDuplicate(championIdList);

        List<ChampionEntry> championEntries = getChampionEntries(id);

        List<ChampionEntry> championList = new ArrayList<>();

        for (int i = 0; i < championIdList.size(); i++) {
            // Find champion for given id.
            for (int j = 0; j < championEntries.size(); j++) {
                if (championIdList.get(i) == championEntries.get(j).getId()) {
                    championList.add(championEntries.get(j));
                }
            }
        }
        return championList;
    }

    private List<SummonerSpellEntry> getMatchSummonerSpells(List<Integer> summonerSpellIdList) {

        int[] id = getIdArrayWithNoDuplicate(summonerSpellIdList);

        List<SummonerSpellEntry> summonerSpellEntries = getSummonerSpells(id);

        List<SummonerSpellEntry> summonerSpellList = new ArrayList<>();

        for (int i = 0; i < summonerSpellIdList.size(); i++) {
            for (int j = 0; j < summonerSpellEntries.size(); j++) {
                if (summonerSpellIdList.get(i) == summonerSpellEntries.get(j).getId()) {
                    summonerSpellList.add(summonerSpellEntries.get(j));
                }
            }
        }

        return summonerSpellList;
    }

    private List<ItemEntry> getMatchListItemEntry(List<Integer> matchItemIdList) {

        int[] id = getIdArrayWithNoDuplicate(matchItemIdList);

        List<ItemEntry> itemEntries = getItemEntries(id);

        List<ItemEntry> itemList = new ArrayList<>();

        for (int i = 0; i < matchItemIdList.size(); i++) {
            // Need to check if item doesnt exist in database e.g. null item.
            boolean isAdded = false;
            for (int j = 0; j < itemEntries.size(); j++) {
                if (matchItemIdList.get(i) == itemEntries.get(j).getId()) {
                    itemList.add(itemEntries.get(j));
                    isAdded = true;
                }
            }
            if (!isAdded) {
                itemList.add(new ItemEntry(0, "", "", "", 0, "", null, null, 0, 0, 0, "", 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0));
            }
        }

        return itemList;
    }

    /*
     * @return Return idArray with no duplicates.
     */
    private int[] getIdArrayWithNoDuplicate(List<Integer> idList) {

        List<Integer> sortedList = new ArrayList<>(idList);

        HashSet hashSet = new HashSet(sortedList);
        sortedList.clear();
        sortedList.addAll(hashSet);

        final int[] idArray = new int[sortedList.size()];
        for (int i = 0; i < sortedList.size(); i++) {
            idArray[i] = sortedList.get(i);
        }

        return idArray;
    }

    public LiveData<List<ChampionEntry>> getChampionEntries() {
        return mDb.championDao().getChampionList();
    }

    public LiveData<ChampionEntry> getChampionEntry(long id) {
        return mDb.championDao().getChampion(id);
    }

    public LiveData<ChampionEntry> getChampionEntry(String name) {
        return mDb.championDao().getChampion(name);
    }

    public List<ChampionEntry> getChampionEntries(int[] id) {
        return mDb.championDao().getChampionList(id);
    }

    public LiveData<List<ItemEntry>> getItemEntries() {
        return mDb.itemDao().loadListItem();
    }

    public LiveData<ItemEntry> getItemEntry(long id) {
        return mDb.itemDao().loadItem(id);
    }

    public LiveData<ItemEntry> getItemEntry(String name) {
        return mDb.itemDao().loadItem(name);
    }

    public LiveData<List<ItemEntry>> getItemEntries(String[] id) {
        return mDb.itemDao().loadItemsWithId(id);
    }

    public List<ItemEntry> getItemEntries(int[] id) {
        return mDb.itemDao().loadItemsWithId(id);
    }

    public LiveData<List<SummonerSpellEntry>> getSummonerSpells() {
        return mDb.summonerSpellDao().loadSpellList();
    }

    public LiveData<SummonerSpellEntry> getSummonerSpell(long id) {
        return mDb.summonerSpellDao().loadSpellById(id);
    }

    public List<SummonerSpellEntry> getSummonerSpells(int[] id) {
        return mDb.summonerSpellDao().loadSpellsWithId(id);
    }
}
