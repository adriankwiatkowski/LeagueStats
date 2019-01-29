package com.example.android.leaguestats.data.sync;

import android.content.Intent;
import android.util.Log;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

public class LeagueFirebaseJobService extends JobService {

    private static final String LOG_TAG = LeagueFirebaseJobService.class.getSimpleName();

    @Override
    public boolean onStartJob(final JobParameters job) {
        Log.d(LOG_TAG, "Job service started");

        Intent intentToFetch = new Intent(this, LeagueSyncIntentService.class);
        startService(intentToFetch);
        Log.d(LOG_TAG, "Service created");

        jobFinished(job, false);

        return true;
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        return true;
    }
}
