package com.example.android.leaguestats;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;

public class LeagueApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
        LeakCanary.install(this);
    }
}
