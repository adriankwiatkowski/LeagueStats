package com.example.android.leaguestats.data;

import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
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

        // TODO Check if response is null.
        championEntryData.observeForever(new Observer<ChampionEntry[]>() {
            @Override
            public void onChanged(@Nullable final ChampionEntry[] championEntries) {
                // TODO cant remove observer on background thread e.g. IntentService.
                //championEntriesData.removeObserver(this);
                new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(Void... voids) {
                        mDb.championDao().deleteChampions();
                        mDb.championDao().bulkInsert(championEntries);
                        return null;
                    }
                }.execute();
            }
        });

        LiveData<ItemEntry[]> itemEntryData =
                mLeagueNetworkDataSource.fetchItemData(patchVersion, language);

        itemEntryData.observeForever(new Observer<ItemEntry[]>() {
            @Override
            public void onChanged(@Nullable final ItemEntry[] itemEntries) {
                new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(Void... voids) {
                        mDb.itemDao().deleteItems();
                        mDb.itemDao().bulkInsert(itemEntries);
                        return null;
                    }
                }.execute();
            }
        });

        LiveData<SummonerSpellEntry[]> summonerSpellEntryData =
                mLeagueNetworkDataSource.fetchSummonerSpellData(patchVersion, language);

        summonerSpellEntryData.observeForever(new Observer<SummonerSpellEntry[]>() {
            @Override
            public void onChanged(@Nullable final SummonerSpellEntry[] summonerSpellEntries) {
                new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(Void... voids) {
                        mDb.summonerSpellDao().deleteSpells();
                        mDb.summonerSpellDao().bulkInsert(summonerSpellEntries);
                        return null;
                    }
                }.execute();
            }
        });

        LiveData<IconEntry[]> iconEntryData =
                mLeagueNetworkDataSource.fetchIconData(patchVersion, language);

        iconEntryData.observeForever(new Observer<IconEntry[]>() {
            @Override
            public void onChanged(@Nullable final IconEntry[] iconEntries) {
                new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(Void... voids) {
                        mDb.iconDao().deleteIcons();
                        mDb.iconDao().bulkInsert(iconEntries);
                        return null;
                    }
                }.execute();
            }
        });
    }

    public LiveData<Summoner> getSummoner(final String entryUrlString, String summonerName) {
        Log.d(LOG_TAG, "Getting summoner");
        return mLeagueNetworkDataSource.fetchSummoner(entryUrlString, summonerName);
    }

    public LiveData<List<Mastery>> getMasteries(String entryUrlString, long summonerId) {
        Log.d(LOG_TAG, "Getting masteries");

        final LiveData<List<MasteryResponse>> masteryNetworkResponse = mLeagueNetworkDataSource.fetchMasteries(entryUrlString, summonerId);

        final MediatorLiveData<List<Mastery>> masteries = new MediatorLiveData<>();

        masteries.addSource(masteryNetworkResponse, new Observer<List<MasteryResponse>>() {
            @Override
            public void onChanged(@Nullable final List<MasteryResponse> masteryResponses) {

                masteries.removeSource(masteryNetworkResponse);

                final int[] championId = new int[masteryResponses.size()];
                for (int i = 0; i < masteryResponses.size(); i++) {
                    championId[i] = masteryResponses.get(i).getChampionId();
                }

                final LiveData<List<ChampionEntry>> liveChampionEntries = getChampions(championId);

                masteries.addSource(liveChampionEntries, new Observer<List<ChampionEntry>>() {
                    @Override
                    public void onChanged(@Nullable List<ChampionEntry> championEntries) {

                        masteries.removeSource(liveChampionEntries);

                        List<Mastery> masteryList = new ArrayList<>();

                        for (int i = 0; i < masteryResponses.size(); i++) {
                            MasteryResponse response = masteryResponses.get(i);
                            Mastery mastery = new Mastery(response, championEntries);
                            masteryList.add(mastery);
                        }

                        masteries.setValue(masteryList);
                    }
                });
            }
        });

        return masteries;
    }

    private MediatorLiveData<MatchMergedData> getLiveDataForMatches(List<MatchResponse> matchResponses) {

        final MatchMergedData mergedData = new MatchMergedData();
        final MediatorLiveData<MatchMergedData> liveDataMerger = new  MediatorLiveData<>();

        final List<Integer> championIdList = getChampionIdList(matchResponses);
        int[] championId = getIdArrayWithNoDuplicate(championIdList);
        final LiveData<List<ChampionEntry>> liveChampionEntries = getChampions(championId);

        final List<Integer> summonerSpell1IdList = getSummonerSpellIdList(matchResponses, true);
        int[] summonerSpell1Id = getIdArrayWithNoDuplicate(summonerSpell1IdList);
        final LiveData<List<SummonerSpellEntry>> liveSummonerSpell1Entries = getSummonerSpells(summonerSpell1Id);

        final List<Integer> summonerSpell2IdList = getSummonerSpellIdList(matchResponses, false);
        int[] summonerSpell2Id = getIdArrayWithNoDuplicate(summonerSpell2IdList);
        final LiveData<List<SummonerSpellEntry>> liveSummonerSpell2Entries = getSummonerSpells(summonerSpell2Id);

        final List<Integer> itemIdList = getItemIdList(matchResponses);
        int[] itemId = getIdArrayWithNoDuplicate(itemIdList);
        final LiveData<List<ItemEntry>> liveItemEntries = getItems(itemId);

        liveDataMerger.addSource(liveChampionEntries, new Observer<List<ChampionEntry>>() {
            @Override
            public void onChanged(@Nullable List<ChampionEntry> championEntries) {
                liveDataMerger.removeSource(liveChampionEntries);
                mergedData.setChampionEntries(getMatchListChampionEntry(championIdList, championEntries));
                liveDataMerger.setValue(mergedData);
            }
        });

        liveDataMerger.addSource(liveSummonerSpell1Entries, new Observer<List<SummonerSpellEntry>>() {
            @Override
            public void onChanged(@Nullable List<SummonerSpellEntry> summonerSpell1Entries) {
                liveDataMerger.removeSource(liveSummonerSpell1Entries);
                mergedData.setSummonerSpell1Entries(getMatchSummonerSpells(summonerSpell1IdList, summonerSpell1Entries));
                liveDataMerger.setValue(mergedData);
            }
        });

        liveDataMerger.addSource(liveSummonerSpell2Entries, new Observer<List<SummonerSpellEntry>>() {
            @Override
            public void onChanged(@Nullable List<SummonerSpellEntry> summonerSpell2Entries) {
                liveDataMerger.removeSource(liveSummonerSpell2Entries);
                mergedData.setSummonerSpell2Entries(getMatchSummonerSpells(summonerSpell2IdList, summonerSpell2Entries));
                liveDataMerger.setValue(mergedData);
            }
        });

        liveDataMerger.addSource(liveItemEntries, new Observer<List<ItemEntry>>() {
            @Override
            public void onChanged(@Nullable List<ItemEntry> itemEntries) {
                liveDataMerger.removeSource(liveItemEntries);
                mergedData.setItemEntries(getMatchListItemEntry(itemIdList, itemEntries));
                liveDataMerger.setValue(mergedData);
            }
        });

        return liveDataMerger;
    }

    public LiveData<List<Match>> getMatches(final String entryUrlString, long accountId, final long summonerId) {
        Log.d(LOG_TAG, "Getting matches");

        final LiveData<List<MatchList>> matchList = mLeagueNetworkDataSource.fetchMatchList(entryUrlString, accountId);

        final LiveData<List<MatchResponse>> matchResponse = Transformations.switchMap(matchList, new Function<List<MatchList>, LiveData<List<MatchResponse>>>() {
            @Override
            public LiveData<List<MatchResponse>> apply(List<MatchList> matchList) {
                return mLeagueNetworkDataSource.fetchMatches(entryUrlString, matchList, MATCH_COUNT);
            }
        });

        final MediatorLiveData<List<Match>> mediatorLiveData = new MediatorLiveData<>();
        mediatorLiveData.addSource(matchResponse, new Observer<List<MatchResponse>>() {
            @Override
            public void onChanged(@Nullable final List<MatchResponse> matchResponses) {
                mediatorLiveData.removeSource(matchResponse);

                mediatorLiveData.addSource(getLiveDataForMatches(matchResponses), new Observer<MatchMergedData>() {
                    @Override
                    public void onChanged(@Nullable MatchMergedData matchMergedData) {
                        if (matchMergedData.getChampionEntries() != null &&
                                matchMergedData.getSummonerSpell1Entries() != null &&
                                matchMergedData.getSummonerSpell2Entries() != null &&
                                matchMergedData.getItemEntries() != null) {

                            List<Match> matches = new ArrayList<>();

                            List<ChampionEntry> championEntries = matchMergedData.getChampionEntries();
                            List<SummonerSpellEntry> summonerSpell1Entries = matchMergedData.getSummonerSpell1Entries();
                            List<SummonerSpellEntry> summonerSpell2Entries = matchMergedData.getSummonerSpell2Entries();
                            List<ItemEntry> itemEntries = matchMergedData.getItemEntries();

                            int index = 0;
                            int itemIndex = 0;
                            for (int i = 0; i < matchResponses.size(); i++) {

                                MatchResponse matchResponse = matchResponses.get(i);

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

                                matches.add(new Match(matchResponse, championEntryList, summonerSpellEntries,
                                        summonerSpell2List, itemEntryList, summonerId));
                            }

                            mediatorLiveData.setValue(matches);
                            Log.d(LOG_TAG, "Matches changed");
                        }
                    }
                });
            }
        });

        return mediatorLiveData;
    }

    private List<Integer> getChampionIdList(List<MatchResponse> matchResponses) {

        List<Integer> championIdList = new ArrayList<>();

        for (MatchResponse matchResponse : matchResponses) {
            List<Participant> participants = matchResponse.getParticipants();
            for (Participant participant : participants) {
                championIdList.add(participant.getChampionId());
            }
        }

        return championIdList;
    }

    private List<Integer> getSummonerSpellIdList(List<MatchResponse> matchResponses, boolean isFirst) {

        List<Integer> summonerSpellIdList = new ArrayList<>();

        for (MatchResponse matchResponse : matchResponses) {
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

    private List<Integer> getItemIdList(List<MatchResponse> matchResponses) {

        List<Integer> itemIdList = new ArrayList<>();

        for (MatchResponse matchResponse : matchResponses) {
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

    private List<ChampionEntry> getMatchListChampionEntry(List<Integer> championIdList, List<ChampionEntry> championEntries) {

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

    private List<SummonerSpellEntry> getMatchSummonerSpells(List<Integer> summonerSpellIdList, List<SummonerSpellEntry> summonerSpellEntries) {

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

    private List<ItemEntry> getMatchListItemEntry(List<Integer> matchItemIdList, List<ItemEntry> itemEntries) {

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

    public LiveData<List<ChampionEntry>> getChampions() {
        return mDb.championDao().getChampions();
    }

    public LiveData<ChampionEntry> getChampion(long id) {
        return mDb.championDao().getChampion(id);
    }

    public LiveData<ChampionEntry> getChampion(String name) {
        return mDb.championDao().getChampion(name);
    }

    public LiveData<List<ChampionEntry>> getChampions(int[] id) {
        return mDb.championDao().getChampions(id);
    }

    public LiveData<List<ItemEntry>> getItems() {
        return mDb.itemDao().getItems();
    }

    public LiveData<ItemEntry> getItem(long id) {
        return mDb.itemDao().getItem(id);
    }

    public LiveData<ItemEntry> getItem(String name) {
        return mDb.itemDao().getItem(name);
    }

    private LiveData<List<ItemEntry>> getItems(int[] itemId) {
        return mDb.itemDao().getItems(itemId);
    }

    public LiveData<List<ItemEntry>> getItems(String[] id) {
        return mDb.itemDao().getItems(id);
    }

    public LiveData<List<SummonerSpellEntry>> getSummonerSpells() {
        return mDb.summonerSpellDao().getSummonerSpells();
    }

    public LiveData<SummonerSpellEntry> getSummonerSpell(long id) {
        return mDb.summonerSpellDao().getSummonerSpells(id);
    }

    public LiveData<List<SummonerSpellEntry>> getSummonerSpells(int[] id) {
        return mDb.summonerSpellDao().getSummonerSpells(id);
    }
}
