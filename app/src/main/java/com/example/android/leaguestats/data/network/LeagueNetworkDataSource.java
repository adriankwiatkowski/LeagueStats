package com.example.android.leaguestats.data.network;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.Driver;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;

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

    private LeagueNetworkDataSource(Context context) {
        mContext = context;
    }

    public static LeagueNetworkDataSource getInstance(Context context) {
        Log.d(LOG_TAG, "Getting the network data source");
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new LeagueNetworkDataSource(context.getApplicationContext());
                Log.d(LOG_TAG, "Made new network data source");
            }
        }
        return sInstance;
    }

    public void startFetchDataService(boolean fetchDataImmediately) {
        Intent intentToFetch = new Intent(mContext, LeagueSyncIntentService.class);
        if (fetchDataImmediately) {
            intentToFetch.setAction(LeagueSyncIntentService.ACTION_FETCH_DATA_IMMEDIATELY);
        } else {
            intentToFetch.setAction(LeagueSyncIntentService.ACTION_FETCH_DATA);
        }
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
}
