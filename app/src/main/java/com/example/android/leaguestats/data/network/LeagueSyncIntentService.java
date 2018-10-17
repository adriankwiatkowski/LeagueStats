package com.example.android.leaguestats.data.network;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.android.leaguestats.data.LeagueRepository;
import com.example.android.leaguestats.utilities.InjectorUtils;

public class LeagueSyncIntentService extends IntentService {

    private static final String LOG_TAG = LeagueSyncIntentService.class.getSimpleName();

    public static final String ACTION_FETCH_DATA = "action-fetch-data";
    public static final String ACTION_FETCH_DATA_IMMEDIATELY = "action-fetch-data-immediately";

    public LeagueSyncIntentService() {
        super("LeagueSyncIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.d(LOG_TAG, "Intent service started");

        String action;
        if (intent != null) {
            action = intent.getAction();
        } else throw new NullPointerException("Intent action can't be null");

        LeagueRepository repository =
                InjectorUtils.provideRepository(this.getApplicationContext());

        switch (action) {
            case ACTION_FETCH_DATA:
                repository.fetchData(this, false);
                break;
            case ACTION_FETCH_DATA_IMMEDIATELY:
                repository.fetchData(this, true);
                break;
            default:
                try {
                    throw new Exception("Unknown action " + action);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }
}
