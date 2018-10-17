package com.example.android.leaguestats.utilities;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.text.TextUtils;

import com.example.android.leaguestats.ui.MainActivity;
import com.example.android.leaguestats.R;

import java.util.Locale;

public class LocaleUtils {

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

        Resources res = context.getResources();
        Configuration config = new Configuration(res.getConfiguration());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            config.setLocale(locale);
            setSystemLocale(config, locale);
            context = context.createConfigurationContext(config);
        } else {
            setSystemLocaleLegacy(config, locale);
            res.updateConfiguration(config, res.getDisplayMetrics());
        }
    }
}
