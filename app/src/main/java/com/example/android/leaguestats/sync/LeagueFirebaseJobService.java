package com.example.android.leaguestats.sync;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.android.leaguestats.utilities.InjectorUtils;
import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

public class LeagueFirebaseJobService extends JobService {

    private static final String LOG_TAG = LeagueFirebaseJobService.class.getSimpleName();

    @Override
    public boolean onStartJob(final JobParameters job) {
        Log.d(LOG_TAG, "Job service started");

        LeagueNetworkDataSource networkDataSource =
                InjectorUtils.provideNetworkDataSource(this.getApplicationContext());
        networkDataSource.fetchData();

        jobFinished(job, false);

        return true;
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        return true;
    }
}
