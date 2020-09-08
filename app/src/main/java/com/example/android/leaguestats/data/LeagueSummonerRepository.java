package com.example.android.leaguestats.data;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.android.leaguestats.data.database.LeagueDatabase;
import com.example.android.leaguestats.data.database.entity.IconEntry;
import com.example.android.leaguestats.data.network.MatchBoundResource;
import com.example.android.leaguestats.data.network.SummonerBoundResource;
import com.example.android.leaguestats.data.network.api.ApiResponse;
import com.example.android.leaguestats.data.network.api.RetrofitDataService;
import com.example.android.leaguestats.data.network.api.RetrofitInstance;
import com.example.android.leaguestats.data.network.api.models.MatchListResponse;
import com.example.android.leaguestats.data.network.api.models.match.Participant;
import com.example.android.leaguestats.data.network.api.models.match.Stats;
import com.example.android.leaguestats.models.Champion;
import com.example.android.leaguestats.models.Item;
import com.example.android.leaguestats.models.Mastery;
import com.example.android.leaguestats.models.Match;
import com.example.android.leaguestats.models.Resource;
import com.example.android.leaguestats.models.Status;
import com.example.android.leaguestats.models.Summoner;
import com.example.android.leaguestats.models.SummonerSpell;
import com.example.android.leaguestats.utilities.RateLimiter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.TimeUnit;

import retrofit2.Retrofit;

// TODO: Remove it later.
public class LeagueSummonerRepository {

    private static final String LOG_TAG = LeagueSummonerRepository.class.getSimpleName();

    private static final int MATCH_COUNT = 10;

    // For Singleton instantiation
    private static final Object LOCK = new Object();
    private static LeagueSummonerRepository sInstance;

    private LeagueDatabase mDb;

    private RetrofitDataService mService;

    private RateLimiter<String> mRateLimit = new RateLimiter<>(30, TimeUnit.SECONDS);

    private LeagueSummonerRepository(LeagueDatabase leagueDatabase, Retrofit retrofit) {
        mDb = leagueDatabase;
        mService = retrofit.create(RetrofitDataService.class);
    }

    public synchronized static LeagueSummonerRepository getInstance(LeagueDatabase leagueDatabase, Retrofit retrofit) {
        Log.d(LOG_TAG, "Getting the repository");
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new LeagueSummonerRepository(leagueDatabase, retrofit);
                Log.d(LOG_TAG, "Made new repository");
            }
        }
        return sInstance;
    }

    public LiveData<Resource<Summoner>> getSummoner(final String entryUrlString, final String summonerName) {
        return new SummonerBoundResource<Summoner, IconEntry>() {

            @Override
            protected boolean shouldFetch() {
                return mRateLimit.shouldFetch(entryUrlString + summonerName);
            }

            @Override
            protected boolean shouldLoadFromDb(@NonNull Summoner summoner) {
                return false;
            }

            @Nullable
            @Override
            protected LiveData<IconEntry> loadFromDb(@NonNull Summoner summoner) {
                return null;
            }

            @NonNull
            @Override
            protected Summoner saveLoadFromDbResult(@NonNull Summoner summoner, @Nullable IconEntry iconEntry) {
                return summoner;
            }

            @NonNull
            @Override
            protected Summoner saveResult(@NonNull Summoner summoner) {
                summoner.setEntryUrl(entryUrlString);
                return summoner;
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<Summoner>> createCall() {
                return mService.getSummoner(RetrofitInstance.getSummonerUrl(entryUrlString, summonerName));
            }

            @Override
            protected void onFetchFailed() {
                super.onFetchFailed();
                mRateLimit.reset(entryUrlString + summonerName);
            }
        }.asLiveData();
    }

    public LiveData<Resource<List<Mastery>>> getMasteries(final String entryUrlString, final String summonerId) {
        Log.d(LOG_TAG, "Getting masteries: " + mService.getMasteries(RetrofitInstance.getMasteryUrl(entryUrlString, summonerId)));
        return new SummonerBoundResource<List<Mastery>, List<Champion>>() {

            @Override
            protected boolean shouldFetch() {
                return mRateLimit.shouldFetch(entryUrlString + summonerId);
            }

            @Override
            protected boolean shouldLoadFromDb(@NonNull List<Mastery> masteryList) {
                return masteryList.size() > 0 && masteryList.get(0).getChampionName() == null;
            }

            @Nullable
            @Override
            protected LiveData<List<Champion>> loadFromDb(@NonNull List<Mastery> masteryList) {
                List<Integer> idList = new ArrayList<>();
                for (int i = 0; i < masteryList.size(); i++) {
                    idList.add(masteryList.get(i).getChampionId());
                }
                return getChampions(idList);
            }

            @NonNull
            @Override
            protected List<Mastery> saveLoadFromDbResult(@NonNull List<Mastery> masteryList, @Nullable List<Champion> champions) {
                if (champions != null) {
                    for (int i = 0; i < masteryList.size(); i++) {
                        Mastery mastery = masteryList.get(i);
                        for (int j = 0; j < champions.size(); j++) {
                            if (mastery.getChampionId() == (champions.get(j).getId())) {
                                Champion champion = champions.get(j);
                                mastery.setChampionName(champion.getName());
                                mastery.setChampionImageId(champion.getChampionImageId());
                                break;
                            }
                        }
                    }
                }
                return masteryList;
            }

            @NonNull
            @Override
            protected List<Mastery> saveResult(@NonNull List<Mastery> masteryList) {
                return masteryList;
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<List<Mastery>>> createCall() {
                return mService.getMasteries(RetrofitInstance.getMasteryUrl(entryUrlString, summonerId));
            }

            @Override
            protected void onFetchFailed() {
                super.onFetchFailed();
                mRateLimit.reset(entryUrlString + summonerId);
            }
        }.asLiveData();
    }

    public LiveData<Resource<List<Resource<Match>>>> getMatches(final String entryUrlString, final String accountId, final String summonerId) {
        Log.d(LOG_TAG, "Getting matches");

        final LiveData<Resource<List<Resource<Match>>>> matchResourceLiveData = new MatchBoundResource<Match, MatchListResponse>() {
            @Override
            protected boolean shouldFetch() {
                return mRateLimit.shouldFetch(entryUrlString + accountId + summonerId);
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<MatchListResponse>> createCall() {
                return mService.getMatchList(RetrofitInstance.getMatchListUrl(entryUrlString, accountId));
            }

            @NonNull
            @Override
            protected List<LiveData<ApiResponse<Match>>> createCalls(@NonNull MatchListResponse matchListResponse) {
                List<LiveData<ApiResponse<Match>>> liveDataList = new ArrayList<>();
                int matchCount = matchListResponse.getMatchList().size() >= MATCH_COUNT ? MATCH_COUNT : matchListResponse.getMatchList().size();
                for (int i = 0; i < matchCount; i++) {
                    liveDataList.add(mService.getMatch(RetrofitInstance.getMatchUrl(
                            entryUrlString, matchListResponse.getMatchList().get(i).getGameId())));
                }
                return liveDataList;
            }

            @Override
            protected void onFetchFailed() {
                mRateLimit.reset(entryUrlString + accountId + summonerId);
            }
        }.asLiveData();

        final MediatorLiveData<Resource<List<Resource<Match>>>> matchMediatorLiveData = new MediatorLiveData<>();

        matchMediatorLiveData.addSource(matchResourceLiveData, new Observer<Resource<List<Resource<Match>>>>() {
            @Override
            public void onChanged(@Nullable Resource<List<Resource<Match>>> response) {
                if (response == null) {
                    return;
                }
                if (response.status == Status.LOADING) {
                    return;
                }
                matchMediatorLiveData.removeSource(matchResourceLiveData);
                if (response.status == Status.ERROR) {
                    Log.d(LOG_TAG, "Error with fetching MatchListResponse: " + response.message);
                    matchMediatorLiveData.setValue(Resource.error(response.message, (List<Resource<Match>>) null));
                    return;
                }
                if (response.data == null) {
                    Log.d(LOG_TAG, "MatchListResponse null data");
                    matchMediatorLiveData.setValue(Resource.error("MatchListResponse null data", (List<Resource<Match>>) null));
                    return;
                }
                final List<Resource<Match>> matchResourceList = response.data;
                final LiveData<MatchLiveData> matchLiveData = getLiveDataForMatches(matchResourceList);
                matchMediatorLiveData.addSource(matchLiveData, new Observer<MatchLiveData>() {
                    @Override
                    public void onChanged(@Nullable MatchLiveData matchData) {
                        matchMediatorLiveData.removeSource(matchLiveData);
                        if (matchData == null) {
                            matchMediatorLiveData.setValue(Resource.error("Couldnt retrieve data from database", (List<Resource<Match>>) null));
                            return;
                        }

                        List<Resource<Match>> matchList = new ArrayList<>();

                        List<Champion> champions = matchData.getChampionList();
                        List<Item> items = matchData.getItemList();
                        List<SummonerSpell> summonerSpells = matchData.getSummonerSpellList();

                        for (int i = 0; i < matchResourceList.size(); i++) {
                            Match matchResponse = matchResourceList.get(i).data;
                            if (matchResponse != null) {
                                for (int j = 0; j < matchResponse.getParticipants().size(); j++) {
                                    for (int k = 0; k < champions.size(); k++) {
                                        if (matchResponse.getParticipants().get(j).getChampionId() == champions.get(k).getId()) {
                                            matchResponse.getParticipants().get(j).setChampion(champions.get(k));
                                        }
                                    }
                                    for (int k = 0; k < summonerSpells.size(); k++) {
                                        if (matchResponse.getParticipants().get(j).getSpell1Id() == summonerSpells.get(k).getId()) {
                                            matchResponse.getParticipants().get(j).setSummonerSpell1(summonerSpells.get(k));
                                        }
                                        if (matchResponse.getParticipants().get(j).getSpell2Id() == summonerSpells.get(k).getId()) {
                                            matchResponse.getParticipants().get(j).setSummonerSpell2(summonerSpells.get(k));
                                        }
                                    }
                                }
                                for (int j = 0; j < matchResponse.getParticipants().size(); j++) {
                                    Stats stats = matchResponse.getParticipants().get(j).getStats();
                                    for (int k = 0; k < items.size(); k++) {
                                        if (stats.getItem0Id() == items.get(k).getId()) {
                                            stats.setItem0(items.get(k));
                                        }
                                        if (stats.getItem1Id() == items.get(k).getId()) {
                                            stats.setItem1(items.get(k));
                                        }
                                        if (stats.getItem2Id() == items.get(k).getId()) {
                                            stats.setItem2(items.get(k));
                                        }
                                        if (stats.getItem3Id() == items.get(k).getId()) {
                                            stats.setItem3(items.get(k));
                                        }
                                        if (stats.getItem4Id() == items.get(k).getId()) {
                                            stats.setItem4(items.get(k));
                                        }
                                        if (stats.getItem5Id() == items.get(k).getId()) {
                                            stats.setItem5(items.get(k));
                                        }
                                        if (stats.getItem6Id() == items.get(k).getId()) {
                                            stats.setItem6(items.get(k));
                                        }
                                    }
                                    matchResponse.getParticipants().get(j).setStats(stats);
                                }
                                matchResponse.setCurrentSummonerId(summonerId);
                            }
                            matchList.add(Resource.success(matchResponse));
                        }
                        matchMediatorLiveData.setValue(Resource.success(matchList));
                    }
                });
            }
        });
        return matchMediatorLiveData;
    }

    private LiveData<MatchLiveData> getLiveDataForMatches(@NonNull List<Resource<Match>> matchResourceList) {
        List<Match> matchResponseList = new ArrayList<>();
        for (Resource<Match> matchResource : matchResourceList) {
            if (matchResource.status == Status.SUCCESS) {
                matchResponseList.add(matchResource.data);
            }
        }
        List<Integer> championIdList = new ArrayList<>();
        List<Integer> itemIdList = new ArrayList<>();
        List<Integer> summonerSpellIdList = new ArrayList<>();
        for (Match matchResponse : matchResponseList) {
            if (matchResponse != null) {
                List<Participant> participants = matchResponse.getParticipants();
                for (Participant participant : participants) {
                    championIdList.add(participant.getChampionId());
                    itemIdList.add(participant.getStats().getItem0Id());
                    itemIdList.add(participant.getStats().getItem1Id());
                    itemIdList.add(participant.getStats().getItem2Id());
                    itemIdList.add(participant.getStats().getItem3Id());
                    itemIdList.add(participant.getStats().getItem4Id());
                    itemIdList.add(participant.getStats().getItem5Id());
                    itemIdList.add(participant.getStats().getItem6Id());
                    summonerSpellIdList.add(participant.getSpell1Id());
                    summonerSpellIdList.add(participant.getSpell2Id());
                }
            }
        }
        List<Integer> championIdNoDuplicate = getIdArrayWithNoDuplicate(championIdList);
        List<Integer> summonerSpellIdNoDuplicate = getIdArrayWithNoDuplicate(summonerSpellIdList);
        List<Integer> itemIdNoDuplicate = getIdArrayWithNoDuplicate(itemIdList);

        return new MatchLiveData(championIdNoDuplicate, itemIdNoDuplicate, summonerSpellIdNoDuplicate).getData();
    }

    /*
     * @return Return idArray with no duplicates.
     */
    private List<Integer> getIdArrayWithNoDuplicate(List<Integer> idList) {
        List<Integer> sortedList = new ArrayList<>(idList);
        HashSet<Integer> hashSet = new HashSet<>(sortedList);
        sortedList.clear();
        sortedList.addAll(hashSet);
        return sortedList;
    }

    public LiveData<List<Champion>> getChampions(List<Integer> id) {
        return mDb.summonerDao().getChampions(id);
    }

    public LiveData<List<Item>> getItems(List<Integer> itemId) {
        return mDb.summonerDao().getItems(itemId);
    }

    public LiveData<List<SummonerSpell>> getSummonerSpells(List<Integer> summonerSpellIdList) {
        return mDb.summonerDao().getSummonerSpells(summonerSpellIdList);
    }

    private class MatchLiveData {

        private List<Champion> championList;
        private List<Item> itemList;
        private List<SummonerSpell> summonerSpellList;
        private int count = 0;
        private MediatorLiveData<MatchLiveData> mediatorLiveData = new MediatorLiveData<>();

        MatchLiveData(List<Integer> championIdList, List<Integer> itemIdList, List<Integer> summonerSpellIdList) {
            final LiveData<List<Champion>> liveChampions = getChampions(championIdList);
            final LiveData<List<Item>> liveItems = getItems(itemIdList);
            final LiveData<List<SummonerSpell>> liveSummonerSpells = getSummonerSpells(summonerSpellIdList);
            mediatorLiveData.addSource(liveChampions, new Observer<List<Champion>>() {
                @Override
                public void onChanged(@Nullable List<Champion> champions) {
                    mediatorLiveData.removeSource(liveChampions);
                    championList = champions;
                    setCount();
                }
            });
            mediatorLiveData.addSource(liveItems, new Observer<List<Item>>() {
                @Override
                public void onChanged(@Nullable List<Item> items) {
                    mediatorLiveData.removeSource(liveItems);
                    itemList = items;
                    setCount();
                }
            });
            mediatorLiveData.addSource(liveSummonerSpells, new Observer<List<SummonerSpell>>() {
                @Override
                public void onChanged(@Nullable List<SummonerSpell> summonerSpells) {
                    mediatorLiveData.removeSource(liveSummonerSpells);
                    summonerSpellList = summonerSpells;
                    setCount();
                }
            });
        }

        private void setCount() {
            count++;
            if (count >= 3) {
                mediatorLiveData.setValue(this);
            }
        }

        public List<Champion> getChampionList() {
            return championList;
        }

        public List<Item> getItemList() {
            return itemList;
        }

        public List<SummonerSpell> getSummonerSpellList() {
            return summonerSpellList;
        }

        public LiveData<MatchLiveData> getData() {
            return mediatorLiveData;
        }
    }
}
