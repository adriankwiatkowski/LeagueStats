package com.example.android.leaguestats.data.sync;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.android.leaguestats.data.LeagueDataRepository;
import com.example.android.leaguestats.utilities.InjectorUtils;

public class LeagueSyncIntentService extends IntentService {

    private static final String LOG_TAG = LeagueSyncIntentService.class.getSimpleName();

    public static final String ACTION_FETCH_NEEDED = "fetch-needed";

    public LeagueSyncIntentService() {
        super("LeagueSyncIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.d(LOG_TAG, "Intent service started");

        String action = intent.getAction();
        boolean isFetchNeeded = ACTION_FETCH_NEEDED.equals(action);

        LeagueDataRepository repository = InjectorUtils.provideDataRepository(this);
        repository.fetchData(this, isFetchNeeded);
    }
}
