package com.example.android.leaguestats.utilities;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.example.android.leaguestats.BuildConfig;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public final class NetworkUtils {

    private static final String LOG_TAG = NetworkUtils.class.getSimpleName();

    private static final int TIMEOUT = 10000;
    private static final int CONNECT_TIMEOUT = 15000;
    private static final int RESPONSE_CODE = 200;
    private static final String REQUEST_METHOD = "GET";
    private static final String ERROR_RETRIEVING_DATA = "Error retrieving data";
    private static final String ERROR_CLOSING_STREAM = "Error closing stream";
    private static final String ERROR_RESPONSE_CODE = "Error response code: ";

    private static final String PERSONAL_API_KEY = BuildConfig.HIDDEN_API_KEY;
    private static final String API_KEY = "api_key";

    private static final String DDRAGON_ENTRY_URL = "https://ddragon.leagueoflegends.com/cdn";
    private static final String PATCH_HTTP_ENTRY_URL = "https://ddragon.leagueoflegends.com/api/versions.json";

    private static final String DATA_PATH = "data";
    private static final String CHAMPION_PATH = "champion.json";
    private static final String ITEM_PATH = "item.json";
    private static final String SUMMONER_SPELL_PATH = "summoner.json";
    private static final String ICON_PATH = "profileicon.json";
    private static final String CHAMPION = "champion";
    private static final String JSON = ".json";

    public static URL getChampionUrl(Context context) {
        String language = PreferencesUtils.getUserLanguage(context);
        String patchVersion = PreferencesUtils.getPatchVersion(context);
        return buildChampionUrl(language, patchVersion);
    }

    private static URL buildChampionUrl(String language, String patchVersion) {
        Uri builtUri = Uri.parse(DDRAGON_ENTRY_URL).buildUpon()
                .appendPath(patchVersion)
                .appendPath(DATA_PATH)
                .appendPath(language)
                .appendPath(CHAMPION_PATH)
                .build();
        try {
            URL url = new URL(builtUri.toString());
            return url;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static URL getChampionUrl(Context context, String id) {
        String language = PreferencesUtils.getUserLanguage(context);
        String patchVersion = PreferencesUtils.getPatchVersion(context);
        return buildChampionUrl(language, patchVersion, id);
    }

    private static URL buildChampionUrl(String language, String patchVersion, String id) {
        Uri builtUri = Uri.parse(DDRAGON_ENTRY_URL).buildUpon()
                .appendPath(patchVersion)
                .appendPath(DATA_PATH)
                .appendPath(language)
                .appendPath(CHAMPION)
                .appendPath(id + JSON)
                .build();
        try {
            URL url = new URL(builtUri.toString());
            return url;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static URL getItemUrl(Context context) {
        String language = PreferencesUtils.getUserLanguage(context);
        String patchVersion = PreferencesUtils.getPatchVersion(context);
        return buildItemUrl(language, patchVersion);
    }

    private static URL buildItemUrl(String language, String patchVersion) {
        Uri builtUri = Uri.parse(DDRAGON_ENTRY_URL).buildUpon()
                .appendPath(patchVersion)
                .appendPath(DATA_PATH)
                .appendPath(language)
                .appendPath(ITEM_PATH)
                .build();
        try {
            URL url = new URL(builtUri.toString());
            return url;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static URL getIconUrl(Context context) {
        String language = PreferencesUtils.getUserLanguage(context);
        String patchVersion = PreferencesUtils.getPatchVersion(context);
        return buildIconUrl(language, patchVersion);
    }

    private static URL buildIconUrl(String language, String patchVersion) {
        Uri builtUri = Uri.parse(DDRAGON_ENTRY_URL).buildUpon()
                .appendPath(patchVersion)
                .appendPath(DATA_PATH)
                .appendPath(language)
                .appendPath(ICON_PATH)
                .build();
        try {
            URL url = new URL(builtUri.toString());
            return url;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static URL getSummonerSpellUrl(Context context) {
        String language = PreferencesUtils.getUserLanguage(context);
        String patchVersion = PreferencesUtils.getPatchVersion(context);
        return buildSummonerSpellUrl(language, patchVersion);
    }

    private static URL buildSummonerSpellUrl(String language, String patchVersion) {
        Uri builtUri = Uri.parse(DDRAGON_ENTRY_URL).buildUpon()
                .appendPath(patchVersion)
                .appendPath(DATA_PATH)
                .appendPath(language)
                .appendPath(SUMMONER_SPELL_PATH)
                .build();
        try {
            URL url = new URL(builtUri.toString());
            return url;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static URL getPatchUrl() {
        return buildPatchUrl();
    }

    private static URL buildPatchUrl() {
        Uri builtUri = Uri.parse(PATCH_HTTP_ENTRY_URL).buildUpon()
                .build();
        try {
            URL url = new URL(builtUri.toString());
            return url;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static URL getSummonerUrl(String entryUrlString, String summonerName) {
        return buildSummonerUrl(entryUrlString, summonerName);
    }

    private static URL buildSummonerUrl(String entryUrlString, String summonerName) {
        Uri builtUri = Uri.parse(entryUrlString).buildUpon()
                .appendPath("lol")
                .appendPath("summoner")
                .appendPath("v3")
                .appendPath("summoners")
                .appendPath("by-name")
                .appendPath(summonerName)
                .appendQueryParameter(API_KEY, PERSONAL_API_KEY)
                .build();
        try {
            URL url = new URL(builtUri.toString());
            return url;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static URL getMasteryUrl(String entryUrlString, long summonerId) {
        return buildMasteryUrl(entryUrlString, String.valueOf(summonerId));
    }

    private static URL buildMasteryUrl(String entryUrlString, String summonerId) {

        Uri builtUri = Uri.parse(entryUrlString).buildUpon()
                .appendPath("lol")
                .appendPath("champion-mastery")
                .appendPath("v3")
                .appendPath("champion-masteries")
                .appendPath("by-summoner")
                .appendPath(summonerId)
                .appendQueryParameter(API_KEY, PERSONAL_API_KEY)
                .build();
        try {
            URL url = new URL(builtUri.toString());
            return url;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static URL getMatchListUrl(String entryUrlString, long accountId) {
        return buildMatchListUrl(entryUrlString, accountId);
    }

    private static URL buildMatchListUrl(String entryUrlString, long accountId) {

        Uri builtUri = Uri.parse(entryUrlString).buildUpon()
                .appendPath("lol")
                .appendPath("match")
                .appendPath("v3")
                .appendPath("matchlists")
                .appendPath("by-account")
                .appendPath(String.valueOf(accountId))
                .appendQueryParameter(API_KEY, PERSONAL_API_KEY)
                .build();
        try {
            URL url = new URL(builtUri.toString());
            return url;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static URL getMatchUrl(String entryUrlString, long gameId) {
        return buildMatchUrl(entryUrlString, gameId);
    }

    private static URL buildMatchUrl(String entryUrlString, long gameId) {

        Uri builtUri = Uri.parse(entryUrlString).buildUpon()
                .appendPath("lol")
                .appendPath("match")
                .appendPath("v3")
                .appendPath("matches")
                .appendPath(String.valueOf(gameId))
                .appendQueryParameter(API_KEY, PERSONAL_API_KEY)
                .build();
        try {
            URL url = new URL(builtUri.toString());
            return url;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getResponse(URL url) throws IOException {

        BufferedReader reader = null;
        String jsonResponse = "";
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        urlConnection = (HttpURLConnection) url.openConnection();

        try {
            urlConnection.setReadTimeout(TIMEOUT);
            urlConnection.setConnectTimeout(CONNECT_TIMEOUT);
            urlConnection.setRequestMethod(REQUEST_METHOD);
            urlConnection.connect();

            if (urlConnection.getResponseCode() == RESPONSE_CODE) {
                inputStream = urlConnection.getInputStream();
                StringBuilder output = new StringBuilder();

                if (inputStream == null) {
                    return null;
                }

                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line = reader.readLine();
                while (line != null) {
                    output.append(line);
                    line = reader.readLine();
                }

                if (output.length() == 0) {
                    return null;
                }

                jsonResponse = output.toString();
            } else {
                Log.e(LOG_TAG, ERROR_RESPONSE_CODE + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, ERROR_RETRIEVING_DATA, e);
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.e(LOG_TAG, ERROR_CLOSING_STREAM, e);
                }
            }
        }

        return jsonResponse;
    }
}
