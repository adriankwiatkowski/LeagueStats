package com.example.android.leaguestats.utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;

import com.example.android.leaguestats.R;
import com.example.android.leaguestats.data.network.LeagueNetworkDataSource;

public class LeaguePreferences {

    private static final String PREF_PATCH = "patch";

    public static String getPatchVersion(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);

        return sp.getString(PREF_PATCH, "");
    }

    public static void savePatchVersion(Context context, String newPatchVersion) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();

        editor.putString(PREF_PATCH, newPatchVersion);
        editor.apply();
    }

    public static String getLanguage(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);

        String keyForLanguage = context.getString(R.string.pref_language_key);
        String defaultLanguage = context.getString(R.string.pref_language_default);

        return sp.getString(keyForLanguage, defaultLanguage);
    }

    public static void fetchNewData(Context context) {
        Log.d("LeaguePreferences", "fetchNewData");
        LeagueNetworkDataSource networkDataSource =
                InjectorUtils.provideNetworkDataSource(context.getApplicationContext());
        networkDataSource.startFetchDataService(true);
    }
}
