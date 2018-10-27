package com.example.android.leaguestats.data.network;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.example.android.leaguestats.data.network.api.RetrofitDataService;
import com.example.android.leaguestats.utilities.LeaguePreferences;
import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.Driver;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;
import com.google.gson.JsonElement;

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

    public void initializeData(Context context, boolean fetchDataImmediately) {

        if (!fetchDataImmediately && mInitialized) {
            return;
        }

        mInitialized = true;

        fetchData(context, fetchDataImmediately);
    }

    // If user changed language fetch data
    private void fetchData(final Context context, final boolean fetchDataImmediately) {

        Call<JsonElement> call = mService.getPatchVersion();

        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                if (response.isSuccessful()) {
                    OpenDataJsonParser openDataJsonParser = new OpenDataJsonParser();
                    String jsonString = response.body().toString();
                    String responsePatch = openDataJsonParser.parsePatchResponse(jsonString);

                    if (isDifferentPatch(context, responsePatch) || fetchDataImmediately) {
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
