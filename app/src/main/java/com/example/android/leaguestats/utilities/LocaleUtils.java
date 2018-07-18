package com.example.android.leaguestats.utilities;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.text.TextUtils;

import com.example.android.leaguestats.MainActivity;
import com.example.android.leaguestats.R;

import java.util.Locale;

public class LocaleUtils {

    private static final String LOG_TAG = LocaleUtils.class.getSimpleName();

    public static boolean didUserSelectedLanguage(Context context) {
        String language = PreferencesUtils.getUserLanguage(context);
        if (!TextUtils.isEmpty(language)) {
            setLocale(context, language);
            return true;
        }
        return false;
    }

    @SuppressWarnings("deprecation")
    public static Locale getSystemLocaleLegacy(Configuration configuration) {
        return configuration.locale;
    }

    @TargetApi(Build.VERSION_CODES.N)
    public static Locale getSystemLocale(Configuration configuration) {
        return configuration.getLocales().get(0);
    }

    @SuppressWarnings("deprecation")
    public static void setSystemLocaleLegacy(Configuration configuration, Locale locale) {
        configuration.locale = locale;
    }

    @TargetApi(Build.VERSION_CODES.N)
    public static void setSystemLocale(Configuration configuration, Locale locale) {
        configuration.setLocale(locale);
    }

    public static void setLocale(Context context, String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);

        Configuration configuration = new Configuration();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            setSystemLocale(configuration, locale);
        } else {
            setSystemLocaleLegacy(configuration, locale);
        }

        context.getResources().updateConfiguration(configuration, context.getResources().getDisplayMetrics());
    }

    public static void changeLanguage(Context packageContext) {
        Intent intent = new Intent(packageContext, MainActivity.class);
        intent.putExtra(packageContext.getString(R.string.change_language_key), true);
        packageContext.startActivity(intent);
    }
}
