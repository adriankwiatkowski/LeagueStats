package com.example.android.leaguestats.utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.android.leaguestats.Data;
import com.example.android.leaguestats.R;

import static android.content.Context.MODE_PRIVATE;

public final class DataUtils {

    public static void saveUserRegion(Context context, String region) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(
                R.string.shared_preferences_name), MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();

        switch (region) {
            case Data.ENTRY_URL_SUMMONER_EUNE:
                editor.putInt(context.getString(R.string.summoner_region_key),
                        context.getResources().getInteger(R.integer.region_eune));
                break;
            case Data.ENTRY_URL_SUMMONER_EUW:
                editor.putInt(context.getString(R.string.summoner_region_key),
                        context.getResources().getInteger(R.integer.region_euw));
                break;
            case Data.ENTRY_URL_SUMMONER_NA:
                editor.putInt(context.getString(R.string.summoner_region_key),
                        context.getResources().getInteger(R.integer.region_na));
                break;
        }
        editor.apply();
    }
}
