package com.example.android.leaguestats.sync;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.android.leaguestats.AppExecutors;
import com.example.android.leaguestats.database.dao.ChampionDao;
import com.example.android.leaguestats.database.dao.IconDao;
import com.example.android.leaguestats.database.dao.ItemDao;
import com.example.android.leaguestats.database.dao.SummonerSpellDao;
import com.example.android.leaguestats.database.entity.ChampionEntry;
import com.example.android.leaguestats.database.entity.IconEntry;
import com.example.android.leaguestats.database.entity.ItemEntry;
import com.example.android.leaguestats.database.entity.SummonerSpellEntry;
import com.example.android.leaguestats.database.models.ListChampionEntry;
import com.example.android.leaguestats.database.models.ListItemEntry;
import com.example.android.leaguestats.database.models.ListSummonerSpellEntry;
import com.example.android.leaguestats.models.Mastery;
import com.example.android.leaguestats.models.Match;
import com.example.android.leaguestats.models.Summoner;

import java.util.ArrayList;
import java.util.List;

public class LeagueRepository {

    private static final String LOG_TAG = LeagueRepository.class.getSimpleName();

    // For Singleton instantiation
    private static final Object LOCK = new Object();
    private static LeagueRepository sInstance;
    private final ChampionDao mChampionDao;
    private final ItemDao mItemDao;
    private final IconDao mIconDao;
    private final SummonerSpellDao mSummonerSpellDao;
    private final LeagueNetworkDataSource mLeagueNetworkDataSource;
    private final AppExecutors mExecutors;
    private boolean mInitialized = false;

    private final MutableLiveData<Summoner> mSummoner = new MutableLiveData<>();
    private final MutableLiveData<List<Mastery>> mMasteries = new MutableLiveData<>();
    private final MutableLiveData<List<Match>> mMatches = new MutableLiveData<>();

    private LeagueRepository(ChampionDao championDao, ItemDao itemDao,
                             IconDao iconDao, SummonerSpellDao summonerSpellDao,
                             LeagueNetworkDataSource leagueNetworkDataSource, AppExecutors executors) {
        mChampionDao = championDao;
        mItemDao = itemDao;
        mIconDao = iconDao;
        mSummonerSpellDao = summonerSpellDao;
        mLeagueNetworkDataSource = leagueNetworkDataSource;
        mExecutors = executors;
        LiveData<ChampionEntry[]> networkChampions = mLeagueNetworkDataSource.getChampions();
        networkChampions.observeForever(new Observer<ChampionEntry[]>() {
            @Override
            public void onChanged(@Nullable final ChampionEntry[] championEntry) {
                mExecutors.diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        mChampionDao.deleteChampions();
                        mChampionDao.bulkInsert(championEntry);
                        Log.d(LOG_TAG, "champions inserted");
                    }
                });
            }
        });
        LiveData<ItemEntry[]> networkItems = mLeagueNetworkDataSource.getItems();
        networkItems.observeForever(new Observer<ItemEntry[]>() {
            @Override
            public void onChanged(@Nullable final ItemEntry[] itemEntries) {
                mExecutors.diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        mItemDao.deleteItems();
                        mItemDao.bulkInsert(itemEntries);
                        Log.d(LOG_TAG, "items inserted");
                    }
                });
            }
        });
        LiveData<IconEntry[]> networkIcons = mLeagueNetworkDataSource.getIcons();
        networkIcons.observeForever(new Observer<IconEntry[]>() {
            @Override
            public void onChanged(@Nullable final IconEntry[] iconEntries) {
                mExecutors.diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        mIconDao.deleteIcons();
                        mIconDao.bulkInsert(iconEntries);
                        Log.d(LOG_TAG, "icons inserted");
                    }
                });
            }
        });
        LiveData<SummonerSpellEntry[]> networkSummonerSpells = mLeagueNetworkDataSource.getSummonerSpells();
        networkSummonerSpells.observeForever(new Observer<SummonerSpellEntry[]>() {
            @Override
            public void onChanged(@Nullable final SummonerSpellEntry[] summonerSpellEntries) {
                mExecutors.diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        mSummonerSpellDao.deleteSpells();
                        mSummonerSpellDao.bulkInsert(summonerSpellEntries);
                        Log.d(LOG_TAG, "summonerSpells inserted");
                    }
                });
            }
        });
        LiveData<Summoner> networkSummoner = mLeagueNetworkDataSource.getSummoner();
        networkSummoner.observeForever(new Observer<Summoner>() {
            @Override
            public void onChanged(@Nullable Summoner summoner) {
                mSummoner.postValue(summoner);
                Log.d(LOG_TAG, "Summoner changed");
            }
        });
        // TODO observe summoner name and cache data. memory and/or db.
        LiveData<List<Mastery>> networkMasteries = mLeagueNetworkDataSource.getMasteries();
        networkMasteries.observeForever(new Observer<List<Mastery>>() {
            @Override
            public void onChanged(@Nullable final List<Mastery> masteries) {

                final String[] id = new String[masteries.size()];
                for (int i = 0; i < masteries.size(); i++) {
                    id[i] = masteries.get(i).getChampionId();
                }

                mExecutors.diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        List<ListChampionEntry> championEntries = getListChampionEntry(id);
                        List<Mastery> masteryList = getMasteryList(masteries, championEntries);
                        mMasteries.postValue(masteryList);
                        Log.d(LOG_TAG, "Masteries changed");
                    }
                });
            }
        });
        LiveData<List<Match>> networkMatches = mLeagueNetworkDataSource.getMatches();
        networkMatches.observeForever(new Observer<List<Match>>() {
            @Override
            public void onChanged(@Nullable final List<Match> matches) {

                mExecutors.diskIO().execute(new Runnable() {
                    @Override
                    public void run() {

                        List<Match> matchList = new ArrayList<>();

                        for (int i = 0; i < matches.size(); i++) {
                            Match match = matches.get(i);
                            List<ListChampionEntry> championEntries = getListChampionEntry(match);
                            List<ListSummonerSpellEntry> summonerSpell1Entries = getListSummonerSpell(match.getSpell1Id());
                            List<ListSummonerSpellEntry> summonerSpell2Entries = getListSummonerSpell(match.getSpell2Id());

                            Match matchData = new Match(match.getParticipantId(), match.getAccountId(),
                                    match.getSummonerId(), match.getSummonerName(), match.getTeamId(),
                                    match.getGameDuration(), match.getGameCreation(), championEntries,
                                    summonerSpell1Entries, summonerSpell2Entries, match.isWin(),
                                    match.getItems(), match.getKills(), match.getDeaths(),
                                    match.getAssists(), match.getTotalDamageToChampions());
                            matchList.add(matchData);
                        }

                        mMatches.postValue(matchList);
                        Log.d(LOG_TAG, "Matches changed");
                    }
                });
            }
        });
    }

    public synchronized static LeagueRepository getInstance(
            ChampionDao championDao, ItemDao itemDao, IconDao iconDao,
            SummonerSpellDao summonerSpellDao, LeagueNetworkDataSource leagueNetworkDataSource,
            AppExecutors executors) {
        Log.d(LOG_TAG, "Getting the repository");
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new LeagueRepository(championDao, itemDao, iconDao, summonerSpellDao,
                        leagueNetworkDataSource, executors);
                Log.d(LOG_TAG, "Made new repository");
            }
        }
        return sInstance;
    }

    private synchronized void initializeData() {
        if (mInitialized) return;
        mInitialized = true;

        mLeagueNetworkDataSource.scheduleRecurringFetchDataSync();

        mExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                if (isFetchNeeded()) {
                    Log.d(LOG_TAG, "fetch is needed");
                    startFetchDataService();
                } else {
                    Log.d(LOG_TAG, "fetch isnt needed");
                }
            }
        });
    }

    private void deleteOldData() {
        mChampionDao.deleteChampions();
        mItemDao.deleteItems();
        mIconDao.deleteIcons();
        mSummonerSpellDao.deleteSpells();
    }

    private boolean isFetchNeeded() {
        int count = mChampionDao.countAllChampions();
        if (count <= 0) {
            deleteOldData();
            return true;
        } else {
            return false;
        }
    }

    private void startFetchDataService() {
        mLeagueNetworkDataSource.startFetchDataService();
    }

    public LiveData<List<ListChampionEntry>> getListChampionEntry() {
        initializeData();
        return mChampionDao.loadChampionList();
    }

    public LiveData<ChampionEntry> getChampionEntry(String id) {
        initializeData();
        return mChampionDao.loadChampionById(id);
    }

    public List<ListChampionEntry> getListChampionEntry(String[] id) {
        initializeData();
        return mChampionDao.loadChampionsWithId(id);
    }

    public LiveData<List<ListItemEntry>> getListItemEntry() {
        initializeData();
        return mItemDao.loadListItem();
    }

    public LiveData<ItemEntry> getItemEntry(long id) {
        initializeData();
        return mItemDao.loadItemById(id);
    }

    public LiveData<List<ListItemEntry>> getListItemEntry(String[] id) {
        initializeData();
        return mItemDao.loadItemsWithId(id);
    }

    public LiveData<List<ListSummonerSpellEntry>> getSummonerSpells() {
        initializeData();
        return mSummonerSpellDao.loadSpellList();
    }

    public LiveData<SummonerSpellEntry> getSummonerSpell(String id) {
        initializeData();
        return mSummonerSpellDao.loadSpellById(id);
    }

    public List<ListSummonerSpellEntry> getSummonerSpells(String[] id) {
        initializeData();
        return mSummonerSpellDao.loadSpellsWithId(id);
    }

    public LiveData<Summoner> getSummoner(String entryUrlString, String summonerName) {
        initializeData();
        Log.d(LOG_TAG, "Getting summoner");
        mLeagueNetworkDataSource.fetchSummoner(entryUrlString, summonerName);
        return mSummoner;
    }

    public LiveData<List<Mastery>> getMasteries(String entryUrlString, long summonerId) {
        initializeData();
        Log.d(LOG_TAG, "Getting masteries");
        mLeagueNetworkDataSource.fetchMasteries(entryUrlString, summonerId);
        return mMasteries;
    }

    public LiveData<List<Match>> getMatches(final String entryUrlString, long accountId) {
        initializeData();
        Log.d(LOG_TAG, "Getting matches");
        mLeagueNetworkDataSource.fetchMatches(entryUrlString, accountId);
        return mMatches;
    }

    private List<ListChampionEntry> getListChampionEntry(Match match) {
        final int championSize = match.getChampionId().size();

        final String[] id = new String[championSize];
        for (int i = 0; i < championSize; i++) {
            id[i] = match.getChampionId().get(i);
        }

        List<ListChampionEntry> championEntries = getListChampionEntry(id);

        List<ListChampionEntry> championList = new ArrayList<>();

        for (int i = 0; i < championSize; i++) {
            String championId = "";
            String championKey = "";
            String championName = "";
            String championTitle = "";
            String championThumbnail = "";

            // Find champion for given id.
            for (int j = 0; j < championEntries.size(); j++) {
                if (String.valueOf(match.getChampionId().get(i)).equals(championEntries.get(j).getKey())) {
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

    private List<ListSummonerSpellEntry> getListSummonerSpell(List<Integer> summonerSpellIdList) {
        final int summonerSpellsSize = summonerSpellIdList.size();
        final String[] id = new String[summonerSpellsSize];
        for (int i = 0; i < summonerSpellsSize; i++) {
            id[i] = String.valueOf(summonerSpellIdList.get(i));
        }

        List<ListSummonerSpellEntry> summonerSpellEntries = getSummonerSpells(id);

        List<ListSummonerSpellEntry> summonerSpellList = new ArrayList<>();

        for (int i = 0; i < summonerSpellsSize; i++) {
            String spellId = "";
            String spellKey = "";
            String spellName = "";
            int spellCooldown = 0;
            String spellImage = "";

            for (int j = 0; j < summonerSpellEntries.size(); j++) {
                if (String.valueOf(summonerSpellIdList.get(i)).equals(summonerSpellEntries.get(j).getKey())) {
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

    private List<Mastery> getMasteryList(List<Mastery> masteries, List<ListChampionEntry> championEntries) {

        List<Mastery> mastery = new ArrayList<>();

        for (int i = 0; i < masteries.size(); i++) {
            String championName = "";
            String championThumbnail = "";
            String championId = "";
            int championLevel = 0;
            int championPoints = 0;
            long lastPlayTime = 0;
            boolean isChestGranted = false;

            // Find champion for given id.
            for (int j = 0; j < championEntries.size(); j++) {
                if (masteries.get(i).getChampionId().equals(championEntries.get(j).getKey())) {
                    championName = championEntries.get(j).getName();
                    championThumbnail = championEntries.get(j).getImage();
                    championId = masteries.get(i).getChampionId();
                    championLevel = masteries.get(i).getChampionLevel();
                    championPoints = masteries.get(i).getChampionPoints();
                    lastPlayTime = masteries.get(i).getLastPlayTime();
                    isChestGranted = masteries.get(i).isChestGranted();
                }
            }
            mastery.add(new Mastery(championName, championThumbnail, championId,
                    championLevel, championPoints, lastPlayTime, isChestGranted));
        }
        return mastery;
    }
}
