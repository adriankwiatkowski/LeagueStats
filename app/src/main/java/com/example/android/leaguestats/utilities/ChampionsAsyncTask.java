package com.example.android.leaguestats.utilities;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.example.android.leaguestats.BuildConfig;
import com.example.android.leaguestats.Data;
import com.example.android.leaguestats.interfaces.ChampionTaskCompleted;
import com.example.android.leaguestats.models.Champion;
import com.example.android.leaguestats.models.Spell;
import com.example.android.leaguestats.models.Stats;

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

public class ChampionsAsyncTask extends AsyncTask<String, Void, ArrayList<Champion>> {

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
    private static final String QUERY_PARAMETER_LOCALE = "en_US";
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

    private ChampionTaskCompleted mListener;

    public ChampionsAsyncTask(ChampionTaskCompleted listener) {
        mListener = listener;
    }

    @Override
    protected ArrayList<Champion> doInBackground(String... strings) {
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

    private ArrayList<Champion> getJsonData(String json) throws JSONException {
        JSONObject root = new JSONObject(json);
        JSONObject data = root.getJSONObject("data");

        ArrayList<Champion> championList = new ArrayList<>();

        for (int i = 0; i < Data.CHAMPION_NAME_ARRAY.length; i++) {

            ArrayList<Spell> spellList = new ArrayList<>();

            ArrayList<String> allyTipsList = new ArrayList<>();
            ArrayList<String> enemyTipsList = new ArrayList<>();

            ArrayList<String> splashArtList = new ArrayList<>();
            ArrayList<String> splashArtNameList = new ArrayList<>();

            ArrayList<Stats> statsList = new ArrayList<>();

            JSONObject championObject = data.getJSONObject(Data.CHAMPION_NAME_ARRAY[i]);

            // Get Champion Name, title...
            long championId = championObject.getLong("id");
            String championName = championObject.getString("name");
            String championKey = championObject.getString("key");
            String championTitle = championObject.getString("title");
            String championLore = championObject.getString("lore");

            // Get Champion Spells.
            JSONArray spellsArray = championObject.getJSONArray("spells");

            for (int j = 0; j < spellsArray.length(); j++) {

                JSONObject spellsObject = spellsArray.getJSONObject(j);

                String name = checkIfContainString(spellsObject, "name", i, j);

                String description = checkIfContainString(spellsObject, "description", i, j);

                JSONObject imageObject = spellsObject.getJSONObject("image");
                String image = checkIfContainString(imageObject, "full", i, j);

                String resource = checkIfContainString(spellsObject, "resource", i, j);

                ArrayList<Double> spellCooldownList = new ArrayList<>();
                ArrayList<Integer> spellCostList = new ArrayList<>();

                JSONArray cooldownArray = spellsObject.getJSONArray("cooldown");
                for (int k = 0; k < cooldownArray.length(); k++) {
                    double cooldown = cooldownArray.getDouble(k);
                    spellCooldownList.add(cooldown);
                }

                JSONArray costArray = spellsObject.getJSONArray("cost");
                for (int k = 0; k < costArray.length(); k++) {
                    int cost = costArray.getInt(k);
                    spellCostList.add(cost);
                }

                spellList.add(new Spell(name, description, image, resource, spellCooldownList, spellCostList));
            }

            // Get Champion Thumbnail.
            JSONObject image = championObject.getJSONObject("image");
            String championThumbnail = image.getString("full");

            // Get Champion info.
            JSONObject info = championObject.getJSONObject("info");
            int championDifficulty = info.getInt("difficulty");
            int championAttack = info.getInt("attack");
            int championDefense = info.getInt("defense");
            int championMagic = info.getInt("magic");

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

            // Set Stats.
            Stats stats = new Stats(attackDamage, attackDamagePerLevel, attackRange, armor, armorPerLevel,
                    health, healthPerLevel, healthRegen, healthRegenPerLevel, mana, manaPerLevel,
                    manaRegen, manaRegenPerLevel, attackSpeedOffset, attackSpeedPerLevel, moveSpeed,
                    crit, critPerLevel, magicResist, magicResistPerLevel);
            statsList.add(stats);

            // Get Champion allyTips.
            JSONArray allyTipsArray = championObject.getJSONArray("allytips");

            for (int j = 0; j < allyTipsArray.length(); j++) {

                String championAllyTip = allyTipsArray.getString(j);

                allyTipsList.add(championAllyTip);
            }

            // Get Champion enemyTips.
            JSONArray enemyTipsArray = championObject.getJSONArray("enemytips");

            for (int j = 0; j < enemyTipsArray.length(); j++) {

                String championEnemyTip = enemyTipsArray.getString(j);

                enemyTipsList.add(championEnemyTip);
            }

            // Get Champion SplashArt.
            // Example http://ddragon.leagueoflegends.com/cdn/img/champion/splash/Aatrox_0.jpg
            JSONArray skins = championObject.getJSONArray("skins");

            for (int j = 0; j < skins.length(); j++) {
                JSONObject skinsObject = skins.getJSONObject(j);

                int splashArtNumber = skinsObject.getInt("num");
                String splashArt = Data.CHAMPION_NAME_ARRAY[i] + "_" + String.valueOf(splashArtNumber) + ".jpg";

                String splashArtName = skinsObject.getString("name");

                splashArtList.add(splashArt);
                splashArtNameList.add(splashArtName);
            }

            // Add champion to List.
            Champion champion = new Champion(championId, championName, championKey, championTitle, championLore,
                    championThumbnail, splashArtList, splashArtNameList, championDifficulty, championAttack,
                    championDefense, championMagic, enemyTipsList, allyTipsList, statsList, spellList);
            championList.add(champion);
        }

        return championList;
    }

    private String checkIfContainString(JSONObject jsonObject, String name, int i, int j) throws JSONException {
        boolean has = jsonObject.has(name);
        if (has) {
            return jsonObject.getString(name);
        } else {
            Log.d(LOG_TAG, "null " + name + " " + j + Data.CHAMPION_NAME_ARRAY[i]);
            return "";
        }
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
    protected void onPostExecute(ArrayList<Champion> champions) {
        super.onPostExecute(champions);

        mListener.championTaskCompleted(champions);
    }
}
