package com.example.android.leaguestats.utilities.AsyncTasks;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.example.android.leaguestats.BuildConfig;
import com.example.android.leaguestats.interfaces.ItemTaskCompleted;
import com.example.android.leaguestats.interfaces.ResultTask;
import com.example.android.leaguestats.room.ItemEntry;
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
import java.util.Iterator;
import java.util.List;

public class ItemAsyncTask extends AsyncTask<String, Integer, List<ItemEntry>> {
    private static final String PERSONAL_API_KEY = BuildConfig.HIDDEN_API_KEY;
    private static final String LOG_TAG = ItemAsyncTask.class.getSimpleName();
    private static final int TIMEOUT = 10000;
    private static final int CONNECT_TIMEOUT = 15000;
    private static final int RESPONSE_CODE = 200;
    private static final String REQUEST_METHOD = "GET";
    private static final String ERROR_RETRIEVING_DATA = "Error retrieving data";
    private static final String ERROR_CLOSING_STREAM = "Error closing stream";
    private static final String ERROR_RESPONSE_CODE = "Error response code: ";
    private static final String UNKNOWN_MOD = "Unknown mod for: ";
    private static final String HTTP_ENTRY_URL = "https://eun1.api.riotgames.com/lol/static-data/v3/items";
    private static final String API_KEY = "api_key";
    private static final String LOCALE = "locale";
    private static final String QUERY_PARAMETER_TAGS_ALL = "all";
    private static final String TAGS = "tags";

    private ItemTaskCompleted mItemListener;
    private ResultTask mResultListener;

    public ItemAsyncTask(ItemTaskCompleted itemListener, ResultTask resultListener) {
        mItemListener = itemListener;
        mResultListener = resultListener;
    }

    @Override
    protected List<ItemEntry> doInBackground(String... strings) {
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

    private List<ItemEntry> getJsonData(String json) throws JSONException {
        JSONObject root = new JSONObject(json);
        JSONObject data = root.getJSONObject("data");

        List<ItemEntry> itemList = new ArrayList<>();

        mResultListener.maxProgress(data.length());

        int progressUpdate = 0;
        publishProgress(progressUpdate);

        Iterator<String> itemIterator = data.keys();
        while (itemIterator.hasNext()) {

            String jsonItemString = itemIterator.next();
            JSONObject itemJson = data.getJSONObject(jsonItemString);

            int id = itemJson.optInt("id");
            String name = itemJson.optString("name");
            String description = itemJson.optString("sanitizedDescription");
            String plainText = itemJson.optString("plaintext");
            int depth = itemJson.optInt("depth");

            JSONArray fromArray = itemJson.optJSONArray("from");
            List<String> fromList = new ArrayList<>();
            if (fromArray != null) {
                    fromList = JSONUtils.getStringListFromJSONArray(fromArray);
            }

            JSONArray intoArray = itemJson.optJSONArray("into");
            List<String> intoList = new ArrayList<>();
            if (intoArray != null) {
                intoList = JSONUtils.getStringListFromJSONArray(intoArray);
            }

            double flatArmor = 0;
            double flatSpellBlock = 0;
            double flatHPPool = 0;
            double flatMPPool = 0;
            double flatHPRegen = 0;
            double flatCritChance = 0;
            double flatMagicDamage = 0;
            double flatPhysicalDamage = 0;
            double flatMovementSpeed = 0;
            double percentMovementSpeed = 0;
            double percentAttackSpeed = 0;
            double percentLifeSteal = 0;

            if (itemJson.has("stats")) {
                JSONObject statsJson = itemJson.getJSONObject("stats");
                Iterator<String> statsIterator = statsJson.keys();
                while (statsIterator.hasNext()) {
                    String jsonStatKeys = statsIterator.next();
                    double stat = statsJson.getDouble(jsonStatKeys);
                    switch (jsonStatKeys) {
                        case "FlatArmorMod":
                            flatArmor = stat;
                            break;
                        case "FlatSpellBlockMod":
                            flatSpellBlock = stat;
                            break;
                        case "FlatHPPoolMod":
                            flatHPPool = stat;
                            break;
                        case "FlatMPPoolMod":
                            flatMPPool = stat;
                            break;
                        case "FlatHPRegenMod":
                            flatHPRegen = stat;
                            break;
                        case "FlatCritChanceMod":
                            flatCritChance = stat;
                            break;
                        case "FlatMagicDamageMod":
                            flatMagicDamage = stat;
                            break;
                        case "FlatPhysicalDamageMod":
                            flatPhysicalDamage = stat;
                            break;
                        case "FlatMovementSpeedMod":
                            flatMovementSpeed = stat;
                            break;
                        case "PercentMovementSpeedMod":
                            percentMovementSpeed = stat;
                            break;
                        case "PercentAttackSpeedMod":
                            percentAttackSpeed = stat;
                            break;
                        case "PercentLifeStealMod":
                            percentLifeSteal = stat;
                            break;
                        default:
                            Log.w(LOG_TAG, UNKNOWN_MOD + jsonStatKeys);
                    }
                }
            }

            JSONObject imageJson = itemJson.getJSONObject("image");
            String image = imageJson.optString("full");

            int baseGold = 0;
            int totalGold = 0;
            int sellGold = 0;
            String purchasable = "";
            if (itemJson.has("gold")) {
                JSONObject goldJson = itemJson.getJSONObject("gold");
                baseGold = goldJson.optInt("base");
                totalGold = goldJson.optInt("total");
                sellGold = goldJson.optInt("sell");
                // "true", "false"
                purchasable = goldJson.optString("purchasable");
            }

            ItemEntry item = new ItemEntry(id, name, plainText, description, depth, image, intoList,
                    fromList, baseGold, totalGold, sellGold, purchasable, flatArmor, flatSpellBlock,
                    flatHPPool, flatMPPool, flatHPRegen, flatCritChance, flatMagicDamage, flatPhysicalDamage,
                    flatMovementSpeed, percentMovementSpeed, percentAttackSpeed, percentLifeSteal);
            itemList.add(item);

            publishProgress(progressUpdate++);
        }

        return itemList;
    }

    // language[0] - language
    private URL createUrl(String[] language) {
        Uri builtUri = Uri.parse(HTTP_ENTRY_URL).buildUpon()
                .appendQueryParameter(LOCALE, language[0])
                .appendQueryParameter(TAGS, QUERY_PARAMETER_TAGS_ALL)
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
    protected void onPostExecute(List<ItemEntry> items) {
        super.onPostExecute(items);

        mItemListener.itemTaskCompleted(items);
    }
}
