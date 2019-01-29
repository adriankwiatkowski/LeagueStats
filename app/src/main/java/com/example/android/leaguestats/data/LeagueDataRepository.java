package com.example.android.leaguestats.data;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.example.android.leaguestats.data.database.LeagueDatabase;
import com.example.android.leaguestats.data.database.entity.ChampionEntry;
import com.example.android.leaguestats.data.database.entity.IconEntry;
import com.example.android.leaguestats.data.database.entity.ItemEntry;
import com.example.android.leaguestats.data.database.entity.SummonerSpellEntry;
import com.example.android.leaguestats.data.network.DataBoundResource;
import com.example.android.leaguestats.data.network.MatchBoundResource;
import com.example.android.leaguestats.data.network.api.ApiResponse;
import com.example.android.leaguestats.data.network.api.RetrofitDataService;
import com.example.android.leaguestats.data.network.api.utils.OpenDataJsonParser;
import com.example.android.leaguestats.data.sync.LeagueSyncIntentService;
import com.example.android.leaguestats.data.sync.LeagueSyncUtils;
import com.example.android.leaguestats.models.Champion;
import com.example.android.leaguestats.models.Item;
import com.example.android.leaguestats.models.Resource;
import com.example.android.leaguestats.models.Status;
import com.example.android.leaguestats.models.SummonerSpell;
import com.example.android.leaguestats.utilities.LeaguePreferences;
import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Retrofit;

public class LeagueDataRepository {

    private static final String LOG_TAG = LeagueDataRepository.class.getSimpleName();

    private static final Object LOCK = new Object();
    private static LeagueDataRepository sInstance;

    private LeagueDatabase mDb;
    private RetrofitDataService mService;

    private boolean mInitialized = false;

    private LeagueDataRepository(Context context, LeagueDatabase leagueDatabase, Retrofit retrofit) {
        mDb = leagueDatabase;
        mService = retrofit.create(RetrofitDataService.class);
        LeagueSyncUtils.initialize(context);
        initializeData(context);
    }

    public synchronized static LeagueDataRepository getInstance(Context context, LeagueDatabase leagueDatabase, Retrofit retrofit) {
        Log.d(LOG_TAG, "Getting the LeagueDataRepository");
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new LeagueDataRepository(context.getApplicationContext(), leagueDatabase, retrofit);
                Log.d(LOG_TAG, "Made new LeagueDataRepository");
            }
        }
        return sInstance;
    }

    private void initializeData(final Context context) {
        if (mInitialized) return;
        Log.d(LOG_TAG, "Initializing data");
        mInitialized = true;
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                boolean isFetchNeeded;
                int championCount = countAllChampions();
                int itemCount = countAllItems();
                int summonerSpellCount = countAllSummonerSpells();
                int iconCount = countAllIcons();
                if (championCount <= 0 || itemCount <= 0 || summonerSpellCount <= 0 || iconCount <= 0) {
                    isFetchNeeded = true;
                } else {
                    isFetchNeeded = false;
                }
                startFetchDataIntentService(context, isFetchNeeded);
                return null;
            }
        }.execute();
    }

    public void fetchData(final Context context, final boolean isFetchNeeded) {
        final LiveData<Resource<List<String>>> patchLiveData = fetchPatch();
        patchLiveData.observeForever(new Observer<Resource<List<String>>>() {
            @Override
            public void onChanged(@Nullable Resource<List<String>> resource) {
                if (resource != null && resource.status != Status.LOADING) {
                    patchLiveData.removeObserver(this);
                    if (resource.data != null) {
                        String latestPatch = resource.data.get(0);
                        String currentPatch = LeaguePreferences.getPatchVersion(context);
                        boolean isNewPatch = false;
                        if (!TextUtils.isEmpty(latestPatch) && !latestPatch.equals(currentPatch)) {
                            Log.d(LOG_TAG, "Saving patch");
                            LeaguePreferences.savePatchVersion(context, latestPatch);
                            isNewPatch = true;
                        } else {
                            Log.d(LOG_TAG, "Same patch");
                        }
                        if (isNewPatch || isFetchNeeded) {
                            // Fetch other data.
                            fetchData(context);
                        }
                    }
                }
            }
        });
    }

    private LiveData<Resource<List<String>>> fetchPatch() {
        return new DataBoundResource<JsonElement, List<String>>() {
            @Override
            protected LiveData<Resource<List<String>>> parseResponse(JsonElement response) {
                Log.d(LOG_TAG, "Patch response successful");
                String patchJsonResponse = response.toString();
                final MutableLiveData<Resource<List<String>>> mutableLiveData = new MutableLiveData<>();
                final List<String> patchResponseList = new OpenDataJsonParser().parsePatchResponse(patchJsonResponse);
                mutableLiveData.postValue(Resource.success(patchResponseList));
                return mutableLiveData;
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<JsonElement>> createCall() {
                return mService.getPatchVersion();
            }

            @Override
            protected void onFetchFailed() {

            }
        }.asLiveData();
    }

    private void fetchData(Context context) {
        Log.d(LOG_TAG, "Fetch data started");

        final String patchVersion = LeaguePreferences.getPatchVersion(context);
        final String language = LeaguePreferences.getLanguage(context);

        final LiveData<Resource<List<ChampionEntry>>> championEntryData = fetchChampionData(patchVersion, language);
        championEntryData.observeForever(new Observer<Resource<List<ChampionEntry>>>() {
            @Override
            public void onChanged(@Nullable final Resource<List<ChampionEntry>> championResource) {
                if (championResource != null && championResource.status != Status.LOADING) {
                    championEntryData.removeObserver(this);
                    new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected Void doInBackground(Void... voids) {
                            Log.d(LOG_TAG, "Inserting champions to database");
                            mDb.championDao().deleteChampions();
                            mDb.championDao().insert(championResource.data);
                            return null;
                        }
                    }.execute();
                }
            }
        });

        final LiveData<Resource<List<ItemEntry>>> itemEntryData = fetchItemData(patchVersion, language);
        itemEntryData.observeForever(new Observer<Resource<List<ItemEntry>>>() {
            @Override
            public void onChanged(@Nullable final Resource<List<ItemEntry>> itemEntryResource) {
                if (itemEntryResource != null && itemEntryResource.status != Status.LOADING) {
                    itemEntryData.removeObserver(this);
                }
            }
        });

        final LiveData<Resource<List<SummonerSpellEntry>>> summonerSpellEntryData = fetchSummonerSpellData(patchVersion, language);
        summonerSpellEntryData.observeForever(new Observer<Resource<List<SummonerSpellEntry>>>() {
            @Override
            public void onChanged(@Nullable final Resource<List<SummonerSpellEntry>> summonerSpellEntryResource) {
                if (summonerSpellEntryResource != null && summonerSpellEntryResource.status != Status.LOADING) {
                    summonerSpellEntryData.removeObserver(this);
                }
            }
        });

        final LiveData<Resource<List<IconEntry>>> iconEntryData = fetchIconData(patchVersion, language);
        iconEntryData.observeForever(new Observer<Resource<List<IconEntry>>>() {
            @Override
            public void onChanged(@Nullable final Resource<List<IconEntry>> iconResource) {
                if (iconResource != null && iconResource.status != Status.LOADING) {
                    iconEntryData.removeObserver(this);
                }
            }
        });
    }

    private LiveData<Resource<List<ChampionEntry>>> fetchChampionData(final String patchVersion, final String language) {
        final OpenDataJsonParser openDataJsonParser = new OpenDataJsonParser();
        final LiveData<Resource<List<Resource<JsonElement>>>> championLiveData = new MatchBoundResource<JsonElement, JsonElement>() {
            @Override
            protected boolean shouldFetch() {
                return true;
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<JsonElement>> createCall() {
                return mService.getChampions(patchVersion, language);
            }

            @NonNull
            @Override
            protected List<LiveData<ApiResponse<JsonElement>>> createCalls(@NonNull JsonElement championResponse) {
                List<LiveData<ApiResponse<JsonElement>>> liveDataList = new ArrayList<>();
                String championsJsonResponse = championResponse.toString();
                final String[] championId = openDataJsonParser.parseChampions(championsJsonResponse);
                for (int i = 0; i < championId.length; i++) {
                    liveDataList.add(mService.getChampion(patchVersion, language, championId[i]));
                }
                return liveDataList;
            }

            @Override
            protected void onFetchFailed() {

            }
        }.asLiveData();
        final MediatorLiveData<Resource<List<ChampionEntry>>> championMediatorLiveData = new MediatorLiveData<>();
        championMediatorLiveData.addSource(championLiveData, new Observer<Resource<List<Resource<JsonElement>>>>() {
            @Override
            public void onChanged(@Nullable Resource<List<Resource<JsonElement>>> listResource) {
                if (listResource != null && listResource.status != Status.LOADING) {
                    if (listResource.status == Status.ERROR) {
                        championMediatorLiveData.removeSource(championLiveData);
                    } else {
                        List<ChampionEntry> championEntryList = new ArrayList<>();
                        for (int i = 0; i < listResource.data.size(); i++) {
                            String championJsonResponse = listResource.data.get(i).data.toString();
                            final ChampionEntry championEntry = openDataJsonParser.parseChampionResponse(championJsonResponse);
                            championEntryList.add(championEntry);
                        }
                        championMediatorLiveData.removeSource(championLiveData);
                        championMediatorLiveData.setValue(Resource.success(championEntryList));
                    }
                }
            }
        });
        return championMediatorLiveData;
    }

    private LiveData<Resource<List<ItemEntry>>> fetchItemData(final String patchVersion, final String language) {
        return new DataBoundResource<JsonElement, List<ItemEntry>>() {
            @Override
            protected void saveCallResult(@Nullable final Resource<List<ItemEntry>> resource) {
                if (resource != null && resource.data != null) {
                    new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected Void doInBackground(Void... voids) {
                            Log.d(LOG_TAG, "Inserting Items into database");
                            mDb.itemDao().deleteItems();
                            mDb.itemDao().insert(resource.data);
                            return null;
                        }
                    }.execute();
                }
            }

            @Override
            protected LiveData<Resource<List<ItemEntry>>> parseResponse(JsonElement response) {
                Log.d(LOG_TAG, "Item response successful");
                String itemJsonResponse = response.toString();
                final MutableLiveData<Resource<List<ItemEntry>>> mutableLiveData = new MutableLiveData<>();
                final List<ItemEntry> itemResponseList = new OpenDataJsonParser().parseItemResponse(itemJsonResponse);
                mutableLiveData.postValue(Resource.success(itemResponseList));
                return mutableLiveData;
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<JsonElement>> createCall() {
                return mService.getItems(patchVersion, language);
            }

            @Override
            protected void onFetchFailed() {

            }
        }.asLiveData();
    }

    private LiveData<Resource<List<SummonerSpellEntry>>> fetchSummonerSpellData(final String patchVersion, final String language) {
        return new DataBoundResource<JsonElement, List<SummonerSpellEntry>>() {
            @Override
            protected void saveCallResult(@Nullable final Resource<List<SummonerSpellEntry>> resource) {
                if (resource != null && resource.data != null) {
                    new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected Void doInBackground(Void... voids) {
                            Log.d(LOG_TAG, "Inserting SummonerSpells into database");
                            mDb.summonerSpellDao().deleteSpells();
                            mDb.summonerSpellDao().insert(resource.data);
                            return null;
                        }
                    }.execute();
                }
            }

            @Override
            protected LiveData<Resource<List<SummonerSpellEntry>>> parseResponse(JsonElement response) {
                Log.d(LOG_TAG, "SummonerSpell response successful");
                String summonerSpellJsonResponse = response.toString();
                final MutableLiveData<Resource<List<SummonerSpellEntry>>> mutableLiveData = new MutableLiveData<>();
                final List<SummonerSpellEntry> summonerSpellResponseLIst =
                        new OpenDataJsonParser().parseSummonerSpellResponse(summonerSpellJsonResponse);
                mutableLiveData.postValue(Resource.success(summonerSpellResponseLIst));
                return mutableLiveData;
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<JsonElement>> createCall() {
                return mService.getSummonerSpells(patchVersion, language);
            }

            @Override
            protected void onFetchFailed() {

            }
        }.asLiveData();
    }

    private LiveData<Resource<List<IconEntry>>> fetchIconData(final String patchVersion, final String language) {
        return new DataBoundResource<JsonElement, List<IconEntry>>() {
            @Override
            protected void saveCallResult(@Nullable final Resource<List<IconEntry>> resource) {
                if (resource != null && resource.data != null) {
                    new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected Void doInBackground(Void... voids) {
                            Log.d(LOG_TAG, "Inserting Icons into database");
                            mDb.iconDao().deleteIcons();
                            mDb.iconDao().insert(resource.data);
                            return null;
                        }
                    }.execute();
                }
            }

            @Override
            protected LiveData<Resource<List<IconEntry>>> parseResponse(JsonElement response) {
                Log.d(LOG_TAG, "Icon response successful");
                String iconJsonResponse = response.toString();
                final MutableLiveData<Resource<List<IconEntry>>> mutableLiveData = new MutableLiveData<>();
                final List<IconEntry> summonerSpellResponseLIst =
                        new OpenDataJsonParser().parseIconResponse(iconJsonResponse);
                mutableLiveData.postValue(Resource.success(summonerSpellResponseLIst));
                return mutableLiveData;
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<JsonElement>> createCall() {
                return mService.getIcons(patchVersion, language);
            }

            @Override
            protected void onFetchFailed() {

            }
        }.asLiveData();
    }

    private void startFetchDataIntentService(Context context, boolean isFetchNeeded) {
        Intent intentToFetch = new Intent(context, LeagueSyncIntentService.class);
        if (isFetchNeeded) {
            intentToFetch.setAction(LeagueSyncIntentService.ACTION_FETCH_NEEDED);
        }
        context.startService(intentToFetch);
        Log.d(LOG_TAG, "Service created");
    }

    public LiveData<List<Champion>> getChampions() {
        return mDb.championDao().getChampions();
    }

    public LiveData<ChampionEntry> getChampion(long id) {
        return mDb.championDao().getChampion(id);
    }

    public LiveData<ChampionEntry> getChampion(String name) {
        return mDb.championDao().getChampion(name + "%");
    }

    public LiveData<List<Item>> getItems() {
        return mDb.itemDao().getItems();
    }

    public LiveData<ItemEntry> getItem(long id) {
        return mDb.itemDao().getItem(id);
    }

    public LiveData<ItemEntry> getItem(String name) {
        return mDb.itemDao().getItem(name + "%");
    }

    public LiveData<List<Item>> getItems(String[] id) {
        return mDb.itemDao().getItems(id);
    }

    public LiveData<List<SummonerSpell>> getSummonerSpells() {
        return mDb.summonerSpellDao().getSummonerSpells();
    }

    public LiveData<SummonerSpellEntry> getSummonerSpell(long id) {
        return mDb.summonerSpellDao().getSummonerSpells(id);
    }

    public LiveData<SummonerSpellEntry> getSummonerSpell(String name) {
        return mDb.summonerSpellDao().getSummonerSpells(name + "%");
    }

    public LiveData<List<IconEntry>> getIcons() {
        return mDb.iconDao().getIcons();
    }

    public LiveData<Integer> countAllLiveChampions() {
        return mDb.championDao().countAllLiveChampions();
    }

    public LiveData<Integer> countAllLiveItems() {
        return mDb.itemDao().countAllLiveItems();
    }

    public LiveData<Integer> countAllLiveSummonerSpells() {
        return mDb.summonerSpellDao().countAllLiveSummonerSpells();
    }

    public LiveData<Integer> countAllLiveIcons() {
        return mDb.iconDao().countAllLiveIcons();
    }

    private int countAllIcons() {
        return mDb.iconDao().countAllIcons();
    }

    private int countAllChampions() {
        return mDb.championDao().countAllChampions();
    }

    private int countAllItems() {
        return mDb.itemDao().countAllItems();
    }

    private int countAllSummonerSpells() {
        return mDb.summonerSpellDao().countAllSummonerSpells();
    }
}
