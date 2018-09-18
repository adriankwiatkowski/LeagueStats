package com.example.android.leaguestats.sync;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.android.leaguestats.utilities.InjectorUtils;

public class LeagueSyncIntentService extends IntentService {

    private static final String LOG_TAG = LeagueSyncIntentService.class.getSimpleName();

    public LeagueSyncIntentService() {
        super("LeagueSyncIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.d(LOG_TAG, "Intent service started");
        LeagueNetworkDataSource networkDataSource =
                InjectorUtils.provideNetworkDataSource(this.getApplicationContext());
        networkDataSource.fetchData();
    }
}
