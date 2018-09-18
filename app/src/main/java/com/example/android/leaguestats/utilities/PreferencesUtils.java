package com.example.android.leaguestats.utilities;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.android.leaguestats.R;

public class PreferencesUtils {

    public static String getPatchVersion(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(
                context.getString(R.string.preferences_file_name), Context.MODE_PRIVATE);
        return preferences.getString(context.getString(R.string.patch_version_key), "");
    }

    public static void savePatchVersion(Context context, String newPatchVersion) {
        SharedPreferences preferences = context.getSharedPreferences(
                context.getString(R.string.preferences_file_name), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(context.getString(R.string.patch_version_key), newPatchVersion);
        editor.apply();
    }

    public static String getUserLanguage(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("language", Context.MODE_PRIVATE);
        return sharedPreferences.getString(context.getString(R.string.user_language_key), "");
    }

    public static void saveUserLanguage(Context context, String language) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("language", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(context.getString(R.string.user_language_key), language);
        editor.apply();
    }
}
