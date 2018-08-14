package com.example.android.leaguestats.utilities.AsyncTasks;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.example.android.leaguestats.BuildConfig;
import com.example.android.leaguestats.interfaces.ChampionTaskCompleted;
import com.example.android.leaguestats.interfaces.ResultTask;
import com.example.android.leaguestats.room.ChampionEntry;
import com.example.android.leaguestats.utilities.DataUtils;
import com.example.android.leaguestats.utilities.JSONUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ChampionsAsyncTask extends AsyncTask<String, Integer, List<ChampionEntry>> {

    private static final String PERSONAL_API_KEY = BuildConfig.HIDDEN_API_KEY;
    private static final String LOG_TAG = ChampionsAsyncTask.class.getSimpleName();
    private static final int TIMEOUT = 10000;
    private static final int CONNECT_TIMEOUT = 15000;
    private static final int RESPONSE_CODE = 200;
    private static final String REQUEST_METHOD = "GET";
    private static final String ERROR_RETRIEVING_DATA = "Error retrieving data";
    private static final String ERROR_CLOSING_STREAM = "Error closing stream";
    private static final String ERROR_RESPONSE_CODE = "Error response code: ";
    private static final String HTTP_ENTRY_URL = "https://eun1.api.riotgames.com/lol/static-data/v3/champions";
    private static final String API_KEY = "api_key";
    private static final String LOCALE = "locale";
    private static final String QUERY_PARAMETER_TAGS_LORE = "lore";
    private static final String QUERY_PARAMETER_TAGS_IMAGE = "image";
    private static final String QUERY_PARAMETER_TAGS_SPLASH_ART = "skins";
    private static final String QUERY_PARAMETER_TAGS_INFO = "info";
    private static final String QUERY_PARAMETER_TAGS_STATS = "stats";
    private static final String QUERY_PARAMETER_TAGS_ENEMY_TIPS = "enemytips";
    private static final String QUERY_PARAMETER_TAGS_ALLY_TIPS = "allytips";
    private static final String QUERY_PARAMETER_TAGS_SPELLS = "spells";
    private static final String TAGS = "tags";
    private static final String QUERY_PARAMETER_DATA_BY_ID = "false";
    private static final String DATA_BY_ID = "dataById";

    private ChampionTaskCompleted mChampionListener;
    private ResultTask mResultListener;

    public ChampionsAsyncTask(ChampionTaskCompleted championListener, ResultTask resultListener) {
        mChampionListener = championListener;
        mResultListener = resultListener;
    }

    @Override
    protected List<ChampionEntry> doInBackground(String... strings) {
        BufferedReader reader = null;
        String jsonResponse = "";
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try {
            URL url = createUrl(strings);

            urlConnection = (HttpURLConnection) url.openConnection();
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

        try {
            return getJsonData(jsonResponse);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }

        return null;
    }

    private List<ChampionEntry> getJsonData(String json) throws JSONException {
        JSONObject root = new JSONObject(json);
        JSONObject data = root.getJSONObject("data");

        List<ChampionEntry> championList = new ArrayList<>();

        mResultListener.maxProgress(data.length());

        int progressUpdate = 0;
        publishProgress(progressUpdate);

        for (int i = 0; i < DataUtils.CHAMPION_NAME_ARRAY.length; i++) {

            JSONObject championObject = data.getJSONObject(DataUtils.CHAMPION_NAME_ARRAY[i]);

            // Get Champion Name, title...
            long championId = championObject.getLong("id");
            String championName = championObject.getString("name");
            String championKey = championObject.getString("key");
            String championTitle = championObject.getString("title");
            String championLore = championObject.getString("lore");

            // Get Champion Spells.
            JSONArray spellsArray = championObject.getJSONArray("spells");
            List<String> spellName = JSONUtils.getListStringFromJSONObjectFromJSONArray(spellsArray, "name");
            List<String> spellDescription = JSONUtils.getListStringFromJSONObjectFromJSONArray(spellsArray, "sanitizedDescription");
            List<String> spellImage = JSONUtils.getSpellImage(spellsArray, "image", "full");
            List<String> spellResource = JSONUtils.getListStringFromJSONObjectFromJSONArray(spellsArray, "resource");
            List<Double> spellCooldown = JSONUtils.getListFromJSONArrayFromJSONObjectFromJSONArray(spellsArray, "cooldown");
            List<Double> spellCost = JSONUtils.getListFromJSONArrayFromJSONObjectFromJSONArray(spellsArray, "cost");
            List<Integer> spellMaxRank = JSONUtils.getListIntegerFromJSONObjectFromJSONArray(spellsArray, "maxrank");

            // Get Champion Thumbnail.
            JSONObject image = championObject.getJSONObject("image");
            String championThumbnail = image.getString("full");

            // Get Champion allyTips.
            JSONArray allyTipsArray = championObject.getJSONArray("allytips");
            List<String> asTips = JSONUtils.getStringListFromJSONArray(allyTipsArray);

            // Get Champion enemyTips.
            JSONArray enemyTipsArray = championObject.getJSONArray("enemytips");
            List<String> vsTips = JSONUtils.getStringListFromJSONArray(enemyTipsArray);

            // Get Champion SplashArt.
            // Example http://ddragon.leagueoflegends.com/cdn/img/champion/splash/Aatrox_0.jpg
            JSONArray skins = championObject.getJSONArray("skins");

            List<String> splashArtList = JSONUtils.getSplashArtPathFromJSONObjectFromJSONArray(skins, "num", championName);
            List<String> splashArtName = JSONUtils.getListStringFromJSONObjectFromJSONArray(skins, "name");

            // Get Champion info.
            JSONObject info = championObject.getJSONObject("info");
            int championDifficulty = info.optInt("difficulty");
            int championAttack = info.optInt("attack");
            int championDefense = info.optInt("defense");
            int championMagic = info.optInt("magic");

            // Get Champion stats. data type double.
            JSONObject statsObject = championObject.getJSONObject("stats");
            double armorPerLevel = statsObject.getDouble("armorperlevel");
            double attackDamage = statsObject.getDouble("attackdamage");
            double manaPerLevel = statsObject.getDouble("mpperlevel");
            double attackSpeedOffset = statsObject.getDouble("attackspeedoffset");
            double mana = statsObject.getDouble("mp");
            double armor = statsObject.getDouble("armor");
            double health = statsObject.getDouble("hp");
            double healthRegenPerLevel = statsObject.getDouble("hpregenperlevel");
            double attackSpeedPerLevel = statsObject.getDouble("attackspeedperlevel");
            double attackRange = statsObject.getDouble("attackrange");
            double moveSpeed = statsObject.getDouble("movespeed");
            double attackDamagePerLevel = statsObject.getDouble("attackdamageperlevel");
            double manaRegenPerLevel = statsObject.getDouble("mpregenperlevel");
            double critPerLevel = statsObject.getDouble("critperlevel");
            double magicResistPerLevel = statsObject.getDouble("spellblockperlevel");
            double crit = statsObject.getDouble("crit");
            double manaRegen = statsObject.getDouble("mpregen");
            double magicResist = statsObject.getDouble("spellblock");
            double healthRegen = statsObject.getDouble("hpregen");
            double healthPerLevel = statsObject.getDouble("hpperlevel");

            // Add champion to List.
            ChampionEntry champion = new ChampionEntry(championId, championName, championKey, championTitle,
                    championLore, championThumbnail, splashArtList, splashArtName,
                    championDifficulty, championAttack, championDefense, championMagic, vsTips,
                    asTips, attackDamage, attackDamagePerLevel, attackRange, armor, armorPerLevel,
                    health, healthPerLevel, healthRegen, healthRegenPerLevel, mana, manaPerLevel,
                    manaRegen, manaRegenPerLevel, attackSpeedOffset, attackSpeedPerLevel, moveSpeed,
                    crit, critPerLevel, magicResist, magicResistPerLevel, spellName,
                    spellDescription, spellImage, spellResource, spellCooldown, spellCost, spellMaxRank);
            championList.add(champion);

            publishProgress(i);
        }

        return championList;
    }

    private URL createUrl(String[] language) {
        Uri builtUri = Uri.parse(HTTP_ENTRY_URL).buildUpon()
                .appendQueryParameter(LOCALE, language[0])
                .appendQueryParameter(TAGS, QUERY_PARAMETER_TAGS_LORE)
                .appendQueryParameter(TAGS, QUERY_PARAMETER_TAGS_IMAGE)
                .appendQueryParameter(TAGS, QUERY_PARAMETER_TAGS_SPLASH_ART)
                .appendQueryParameter(TAGS, QUERY_PARAMETER_TAGS_INFO)
                .appendQueryParameter(TAGS, QUERY_PARAMETER_TAGS_ENEMY_TIPS)
                .appendQueryParameter(TAGS, QUERY_PARAMETER_TAGS_STATS)
                .appendQueryParameter(TAGS, QUERY_PARAMETER_TAGS_ALLY_TIPS)
                .appendQueryParameter(TAGS, QUERY_PARAMETER_TAGS_SPELLS)
                .appendQueryParameter(DATA_BY_ID, QUERY_PARAMETER_DATA_BY_ID)
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

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);

        mResultListener.resultTask(values[0]);
    }

    @Override
    protected void onPostExecute(List<ChampionEntry> champions) {
        super.onPostExecute(champions);

        mChampionListener.championTaskCompleted(champions);
    }
}
