package com.example.android.leaguestats.data;

import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.Transformations;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.android.leaguestats.data.network.api.models.MasteryResponse;
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
import com.example.android.leaguestats.utilities.LeaguePreferences;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class LeagueRepository {

    private static final String LOG_TAG = LeagueRepository.class.getSimpleName();

    private static final int MATCH_COUNT = 10;

    // For Singleton instantiation
    private static final Object LOCK = new Object();
    private static LeagueRepository sInstance;
    private LeagueDatabase mDb;
    private final LeagueNetworkDataSource mLeagueNetworkDataSource;
    private boolean mInitialized = false;

    private LeagueRepository(Context context, LeagueDatabase leagueDatabase,
                             LeagueNetworkDataSource leagueNetworkDataSource) {
        mDb = leagueDatabase;
        mLeagueNetworkDataSource = leagueNetworkDataSource;
        initializeData(context.getApplicationContext());
    }

    public synchronized static LeagueRepository getInstance(Context context, LeagueDatabase leagueDatabase,
                                                            LeagueNetworkDataSource leagueNetworkDataSource) {
        Log.d(LOG_TAG, "Getting the repository");
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new LeagueRepository(context.getApplicationContext(), leagueDatabase, leagueNetworkDataSource);
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

    public void fetchData(final Context context) {
        Log.d(LOG_TAG, "Fetch data started");

        String patchVersion = LeaguePreferences.getPatchVersion(context);
        String language = LeaguePreferences.getLanguage(context);

        final LiveData<ChampionEntry[]> championEntryData =
                mLeagueNetworkDataSource.fetchChampionData(patchVersion, language);

        championEntryData.observeForever(new Observer<ChampionEntry[]>() {
            @Override
            public void onChanged(@Nullable ChampionEntry[] championEntries) {
                // TODO cant remove observer on background thread e.g. IntentService.
                //championEntriesData.removeObserver(this);
                mDb.championDao().deleteChampions();
                mDb.championDao().bulkInsert(championEntries);
            }
        });

        LiveData<ItemEntry[]> itemEntryData =
                mLeagueNetworkDataSource.fetchItemData(patchVersion, language);

        itemEntryData.observeForever(new Observer<ItemEntry[]>() {
            @Override
            public void onChanged(@Nullable ItemEntry[] itemEntries) {
                mDb.itemDao().deleteItems();
                mDb.itemDao().bulkInsert(itemEntries);
            }
        });

        LiveData<SummonerSpellEntry[]> summonerSpellEntryData =
                mLeagueNetworkDataSource.fetchSummonerSpellData(patchVersion, language);

        summonerSpellEntryData.observeForever(new Observer<SummonerSpellEntry[]>() {
            @Override
            public void onChanged(@Nullable SummonerSpellEntry[] summonerSpellEntries) {
                mDb.summonerSpellDao().deleteSpells();
                mDb.summonerSpellDao().bulkInsert(summonerSpellEntries);
            }
        });

        LiveData<IconEntry[]> iconEntryData =
                mLeagueNetworkDataSource.fetchIconData(patchVersion, language);

        iconEntryData.observeForever(new Observer<IconEntry[]>() {
            @Override
            public void onChanged(@Nullable IconEntry[] iconEntries) {
                mDb.iconDao().deleteIcons();
                mDb.iconDao().bulkInsert(iconEntries);
            }
        });
    }

    public LiveData<Summoner> getSummoner(final String entryUrlString, String summonerName) {
        Log.d(LOG_TAG, "Getting summoner");
        return mLeagueNetworkDataSource.fetchSummoner(entryUrlString, summonerName);
    }

    public LiveData<List<Mastery>> getMasteries(String entryUrlString, long summonerId) {
        Log.d(LOG_TAG, "Getting masteries");

        LiveData<List<MasteryResponse>> masteryNetworkResponse =
                mLeagueNetworkDataSource.fetchMasteries(entryUrlString, summonerId);

        return Transformations.switchMap(masteryNetworkResponse, new Function<List<MasteryResponse>, LiveData<List<Mastery>>>() {
            @Override
            public LiveData<List<Mastery>> apply(final List<MasteryResponse> masteryResponse) {

                final MutableLiveData<List<Mastery>> masteries = new MutableLiveData<>();

                if (masteryResponse != null) {
                    new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected Void doInBackground(Void... voids) {
                            final int[] championId = new int[masteryResponse.size()];
                            for (int i = 0; i < masteryResponse.size(); i++) {
                                championId[i] = masteryResponse.get(i).getChampionId();
                            }

                            List<ChampionEntry> championEntries = getChampionEntries(championId);

                            List<Mastery> masteryList = new ArrayList<>();

                            for (int i = 0; i < masteryResponse.size(); i++) {
                                MasteryResponse response = masteryResponse.get(i);
                                Mastery mastery = new Mastery(response, championEntries);
                                masteryList.add(mastery);
                            }

                            masteries.postValue(masteryList);
                            Log.d(LOG_TAG, "Masteries changed");
                            return null;
                        }
                    }.execute();
                } else {
                    masteries.postValue(null);
                }

                return masteries;
            }
        });
    }

    public LiveData<List<Match>> getMatches(final String entryUrlString, long accountId, final long summonerId) {
        Log.d(LOG_TAG, "Getting matches");

        LiveData<List<MatchList>> matchList = mLeagueNetworkDataSource.fetchMatchList(entryUrlString, accountId);

        LiveData<List<MatchResponse>> matchResponse = Transformations.switchMap(matchList, new Function<List<MatchList>, LiveData<List<MatchResponse>>>() {
            @Override
            public LiveData<List<MatchResponse>> apply(List<MatchList> matchList) {
                return mLeagueNetworkDataSource.fetchMatches(entryUrlString, matchList, MATCH_COUNT);
            }
        });

        return Transformations.switchMap(matchResponse, new Function<List<MatchResponse>, LiveData<List<Match>>>() {
            @Override
            public LiveData<List<Match>> apply(final List<MatchResponse> matchResponseList) {

                final MutableLiveData<List<Match>> matchMutableLiveData = new MutableLiveData<>();

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

                        int index = 0;
                        int itemIndex = 0;
                        for (int i = 0; i < matchResponseList.size(); i++) {

                            MatchResponse matchResponse = matchResponseList.get(i);

                            int participantsSize = matchResponse.getParticipants().size();

                            List<ChampionEntry> championEntryList = new ArrayList<>();
                            List<SummonerSpellEntry> summonerSpellEntries = new ArrayList<>();
                            List<SummonerSpellEntry> summonerSpell2List = new ArrayList<>();
                            List<ItemEntry> itemEntryList = new ArrayList<>();

                            for (int j = 0; j < participantsSize; j++) {

                                championEntryList.add(championEntries.get(index));
                                summonerSpellEntries.add(summonerSpell1Entries.get(index));
                                summonerSpell2List.add(summonerSpell2Entries.get(index));

                                index++;
                            }

                            // There is always 7 items.
                            for (int j = 0; j < 7 * participantsSize; j++) {

                                itemEntryList.add(itemEntries.get(itemIndex));

                                itemIndex++;
                            }

                            matchList.add(new Match(matchResponse, championEntryList, summonerSpellEntries,
                                    summonerSpell2List, itemEntryList, summonerId));
                        }

                        matchMutableLiveData.postValue(matchList);
                        Log.d(LOG_TAG, "Matches changed");
                        return null;
                    }
                }.execute();

                return matchMutableLiveData;
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
