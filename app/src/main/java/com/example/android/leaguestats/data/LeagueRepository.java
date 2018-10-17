package com.example.android.leaguestats.data;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import com.example.android.leaguestats.AppExecutors;
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
import com.example.android.leaguestats.data.database.models.ListChampionEntry;
import com.example.android.leaguestats.data.database.models.ListItemEntry;
import com.example.android.leaguestats.data.database.models.ListSummonerSpellEntry;
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

    private static final int MATCH_COUNT = 20;

    // For Singleton instantiation
    private static final Object LOCK = new Object();
    private static LeagueRepository sInstance;
    private LeagueDatabase mDb;
    private final LeagueNetworkDataSource mLeagueNetworkDataSource;
    private final AppExecutors mExecutors;
    private boolean mInitialized = false;

    private RetrofitDataService mService;

    private LeagueRepository(LeagueDatabase leagueDatabase,
                             LeagueNetworkDataSource leagueNetworkDataSource,
                             AppExecutors executors, Retrofit retrofit) {
        mService = retrofit.create(RetrofitDataService.class);
        mDb = leagueDatabase;
        mLeagueNetworkDataSource = leagueNetworkDataSource;
        mExecutors = executors;
        initializeData();
    }

    public synchronized static LeagueRepository getInstance(LeagueDatabase leagueDatabase,
                                                            LeagueNetworkDataSource leagueNetworkDataSource,
                                                            AppExecutors executors,
                                                            Retrofit retrofit) {
        Log.d(LOG_TAG, "Getting the repository");
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new LeagueRepository(leagueDatabase, leagueNetworkDataSource, executors, retrofit);
                Log.d(LOG_TAG, "Made new repository");
            }
        }
        return sInstance;
    }

    private synchronized void initializeData() {
        if (mInitialized) return;
        Log.d(LOG_TAG, "Initializing data");
        mInitialized = true;

        mLeagueNetworkDataSource.scheduleRecurringFetchDataSync();

        startFetchDataService(false);
    }

    public void fetchData(Context context, boolean fetchDataImmediately) {
        Log.d(LOG_TAG, "Fetch data started");

        if (!fetchDataImmediately) {
            if (!isFetchNeeded(context)) {
                Log.d(LOG_TAG, "Fetch no needed");
                return;
            }
        }

        String patchVersion = LeaguePreferences.getPatchVersion(context);
        String language = LeaguePreferences.getLanguage(context);
        OpenDataJsonParser openDataJsonParser = new OpenDataJsonParser();

        fetchChampionData(patchVersion, language, openDataJsonParser);
        fetchItemData(patchVersion, language, openDataJsonParser);
        fetchSummonerSpellData(patchVersion, language, openDataJsonParser);
        fetchIconData(patchVersion, language, openDataJsonParser);
    }

    private boolean isFetchNeeded(Context context) {
        OpenDataJsonParser openDataJsonParser = new OpenDataJsonParser();

        boolean isFetchNeeded = false;

        Call<JsonElement> call = mService.getPatchVersion();

        try {

            Response<JsonElement> response = call.execute();

            if (response.isSuccessful()) {

                String jsonString = response.body().toString();
                String responsePatch = openDataJsonParser.parsePatchResponse(jsonString);
                String savedPatchVersion = LeaguePreferences.getPatchVersion(context);

                if (!TextUtils.isEmpty(responsePatch)) {
                    if (!responsePatch.equals(savedPatchVersion)) {
                        LeaguePreferences.savePatchVersion(context, responsePatch);
                        isFetchNeeded = true;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return isFetchNeeded;
    }

    public void startFetchDataService(boolean fetchDataImmediately) {
        mLeagueNetworkDataSource.startFetchDataService(fetchDataImmediately);
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

        mExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {

                final int[] championId = new int[masteryResponses.size()];
                for (int i = 0; i < masteryResponses.size(); i++) {
                    championId[i] = masteryResponses.get(i).getChampionId();
                }

                List<ListChampionEntry> championEntries = getListChampionEntry(championId);

                List<Mastery> masteryList = new ArrayList<>();

                for (int i = 0; i < masteryResponses.size(); i++) {
                    MasteryResponse masteryResponse = masteryResponses.get(i);
                    Mastery mastery = new Mastery(masteryResponse, championEntries);
                    masteryList.add(mastery);
                }

                masteryMutableLiveData.postValue(masteryList);
                Log.d(LOG_TAG, "Masteries changed");
            }
        });
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

        mExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {

                List<Match> matchList = new ArrayList<>();

                List<Integer> championIdList = getChampionIdList(matchResponseList);
                List<ListChampionEntry> championEntries = getMatchListChampionEntry(championIdList);

                List<Integer> summonerSpell1IdList = getSummonerSpellIdList(matchResponseList, true);
                List<ListSummonerSpellEntry> summonerSpell1Entries = getMatchListSummonerSpell(summonerSpell1IdList);

                List<Integer> summonerSpell2IdList = getSummonerSpellIdList(matchResponseList, false);
                List<ListSummonerSpellEntry> summonerSpell2Entries = getMatchListSummonerSpell(summonerSpell2IdList);

                List<Integer> itemIdList = getItemIdList(matchResponseList);
                List<ListItemEntry> itemEntries = getMatchListItemEntry(itemIdList);

                int count = 0;
                int itemCount = 0;
                for (int i = 0; i < matchResponseList.size(); i++) {


                    MatchResponse matchResponse = matchResponseList.get(i);

                    int participantsSize = matchResponse.getParticipants().size();

                    List<ListChampionEntry> championEntryList = new ArrayList<>();
                    List<ListSummonerSpellEntry> summonerSpell1List = new ArrayList<>();
                    List<ListSummonerSpellEntry> summonerSpell2List = new ArrayList<>();
                    List<ListItemEntry> itemEntryList = new ArrayList<>();

                    for (int j = 0; j < participantsSize; j++) {

                        championEntryList.add(championEntries.get(count));
                        summonerSpell1List.add(summonerSpell1Entries.get(count));
                        summonerSpell2List.add(summonerSpell2Entries.get(count));

                        count++;
                    }

                    // There is always 7 items.
                    for (int j = 0; j < 7 * participantsSize; j++) {

                        itemEntryList.add(itemEntries.get(itemCount));

                        itemCount++;
                    }

                    matchList.add(new Match(matchResponse, championEntryList, summonerSpell1List,
                            summonerSpell2List, itemEntryList, summonerId));
                }

                matchMutableLiveData.postValue(matchList);
                Log.d(LOG_TAG, "Matches changed");
            }
        });
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

    private List<ListChampionEntry> getMatchListChampionEntry(List<Integer> championIdList) {

        int[] id = getIdArrayWithNoDuplicate(championIdList);

        List<ListChampionEntry> championEntries = getListChampionEntry(id);

        List<ListChampionEntry> championList = new ArrayList<>();

        for (int i = 0; i < championIdList.size(); i++) {
            int championId = 0;
            String championKey = "";
            String championName = "";
            String championTitle = "";
            String championThumbnail = "";

            // Find champion for given id.
            for (int j = 0; j < championEntries.size(); j++) {
                if (championIdList.get(i) == championEntries.get(j).getId()) {
                    championId = championEntries.get(j).getId();
                    championKey = championEntries.get(j).getKey();
                    championName = championEntries.get(j).getName();
                    championTitle = championEntries.get(j).getTitle();
                    championThumbnail = championEntries.get(j).getImage();
                }
            }
            championList.add(new ListChampionEntry(championId, championKey,
                    championName, championTitle, championThumbnail));
        }
        return championList;
    }

    private List<ListSummonerSpellEntry> getMatchListSummonerSpell(List<Integer> summonerSpellIdList) {

        int[] id = getIdArrayWithNoDuplicate(summonerSpellIdList);

        List<ListSummonerSpellEntry> summonerSpellEntries = getSummonerSpells(id);

        List<ListSummonerSpellEntry> summonerSpellList = new ArrayList<>();

        for (int i = 0; i < summonerSpellIdList.size(); i++) {
            int spellId = 0;
            String spellKey = "";
            String spellName = "";
            int spellCooldown = 0;
            String spellImage = "";

            for (int j = 0; j < summonerSpellEntries.size(); j++) {
                if (summonerSpellIdList.get(i) == summonerSpellEntries.get(j).getId()) {
                    spellId = summonerSpellEntries.get(j).getId();
                    spellKey = summonerSpellEntries.get(j).getKey();
                    spellName = summonerSpellEntries.get(j).getName();
                    spellCooldown = summonerSpellEntries.get(j).getCooldown();
                    spellImage = summonerSpellEntries.get(j).getImage();
                }
            }
            summonerSpellList.add(new ListSummonerSpellEntry(spellId, spellKey, spellName, spellCooldown, spellImage));
        }
        return summonerSpellList;
    }

    private List<ListItemEntry> getMatchListItemEntry(List<Integer> matchItemIdList) {

        int[] id = getIdArrayWithNoDuplicate(matchItemIdList);

        List<ListItemEntry> itemEntries = getListItemEntry(id);

        List<ListItemEntry> itemList = new ArrayList<>();

        for (int i = 0; i < matchItemIdList.size(); i++) {
            int itemId = 0;
            String itemName = "";
            String itemImage = "";
            int totalGold = 0;

            for (int j = 0; j < itemEntries.size(); j++) {
                if (matchItemIdList.get(i) == itemEntries.get(j).getId()) {
                    itemId = itemEntries.get(j).getId();
                    itemName = itemEntries.get(j).getName();
                    itemImage = itemEntries.get(j).getImage();
                    totalGold = itemEntries.get(j).getTotalGold();
                }
            }
            itemList.add(new ListItemEntry(itemId, itemName, itemImage, totalGold));
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

    public LiveData<List<ListChampionEntry>> getListChampionEntry() {
        return mDb.championDao().getChampionList();
    }

    public LiveData<ChampionEntry> getChampionEntry(long id) {
        return mDb.championDao().getChampion(id);
    }

    public LiveData<ChampionEntry> getChampionEntry(String name) {
        return mDb.championDao().getChampion(name);
    }

    public List<ListChampionEntry> getListChampionEntry(int[] id) {
        return mDb.championDao().getChampionList(id);
    }

    public LiveData<List<ListItemEntry>> getListItemEntry() {
        return mDb.itemDao().loadListItem();
    }

    public LiveData<ItemEntry> getItemEntry(long id) {
        return mDb.itemDao().loadItem(id);
    }

    public LiveData<ItemEntry> getItemEntry(String name) {
        return mDb.itemDao().loadItem(name);
    }

    public LiveData<List<ListItemEntry>> getListItemEntry(String[] id) {
        return mDb.itemDao().loadItemsWithId(id);
    }

    public List<ListItemEntry> getListItemEntry(int[] id) {
        return mDb.itemDao().loadItemsWithId(id);
    }

    public LiveData<List<ListSummonerSpellEntry>> getSummonerSpells() {
        return mDb.summonerSpellDao().loadSpellList();
    }

    public LiveData<SummonerSpellEntry> getSummonerSpell(int id) {
        return mDb.summonerSpellDao().loadSpellById(id);
    }

    public List<ListSummonerSpellEntry> getSummonerSpells(int[] id) {
        return mDb.summonerSpellDao().loadSpellsWithId(id);
    }
}
