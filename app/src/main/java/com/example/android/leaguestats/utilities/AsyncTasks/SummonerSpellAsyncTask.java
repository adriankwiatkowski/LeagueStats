package com.example.android.leaguestats.utilities.AsyncTasks;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.example.android.leaguestats.BuildConfig;
import com.example.android.leaguestats.interfaces.ResultTask;
import com.example.android.leaguestats.interfaces.SummonerSpellTaskCompleted;
import com.example.android.leaguestats.room.SummonerSpellEntry;
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

public class SummonerSpellAsyncTask extends AsyncTask<String, Integer, List<SummonerSpellEntry>> {

    private static final String PERSONAL_API_KEY = BuildConfig.HIDDEN_API_KEY;
    private static final String LOG_TAG = SummonerSpellAsyncTask.class.getSimpleName();
    private static final int TIMEOUT = 10000;
    private static final int CONNECT_TIMEOUT = 15000;
    private static final int RESPONSE_CODE = 200;
    private static final String REQUEST_METHOD = "GET";
    private static final String ERROR_RETRIEVING_DATA = "Error retrieving data";
    private static final String ERROR_CLOSING_STREAM = "Error closing stream";
    private static final String ERROR_RESPONSE_CODE = "Error response code: ";
    private static final String HTTP_ENTRY_URL = "https://eun1.api.riotgames.com/lol/static-data/v3/summoner-spells";
    private static final String API_KEY = "api_key";
    private static final String LOCALE = "locale";
    private static final String QUERY_PARAMETER_TAGS_ALL = "all";
    private static final String TAGS = "tags";
    private static final String QUERY_PARAMETER_DATA_BY_ID = "false";
    private static final String DATA_BY_ID = "dataById";

    private SummonerSpellTaskCompleted mListener;
    private ResultTask mResultListener;

    public SummonerSpellAsyncTask(SummonerSpellTaskCompleted summonerSpellListener, ResultTask resultTask) {
        mListener = summonerSpellListener;
        mResultListener = resultTask;
    }

    @Override
    protected List<SummonerSpellEntry> doInBackground(String... strings) {
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

    private List<SummonerSpellEntry> getJsonData(String json) throws JSONException {
        JSONObject root = new JSONObject(json);
        JSONObject data = root.getJSONObject("data");

        mResultListener.maxProgress(data.length());
        publishProgress(0);

        List<SummonerSpellEntry> summonerSpells = new ArrayList<>();

        Iterator<String> summonerSpellIterator = data.keys();
        int i = 0;
        while (summonerSpellIterator.hasNext()) {

            String jsonSummonerSpellString = summonerSpellIterator.next();
            JSONObject summonerSpellJson = data.getJSONObject(jsonSummonerSpellString);

            int id = summonerSpellJson.optInt("id");
            String name = summonerSpellJson.optString("name");
            String description = summonerSpellJson.optString("description");
            String key = summonerSpellJson.optString("key");

            JSONObject imageJson = summonerSpellJson.getJSONObject("image");
            String image = imageJson.optString("full");

            JSONArray costArray = summonerSpellJson.getJSONArray("cost");
            int cost = costArray.getInt(0);

            JSONArray cooldownArray = summonerSpellJson.getJSONArray("cooldown");
            int cooldown = cooldownArray.getInt(0);

            JSONArray rangeArray = summonerSpellJson.getJSONArray("range");
            int range = rangeArray.getInt(0);

            JSONArray modesArray = summonerSpellJson.optJSONArray("modes");
            List<String> modesList = JSONUtils.getStringListFromJSONArray(modesArray);

            summonerSpells.add(new SummonerSpellEntry(id, key, name, description,
                    image, cost, cooldown, range, modesList));

            i++;
            publishProgress(i);
        }
        return summonerSpells;
    }

    // language[0] - language
    private URL createUrl(String[] language) {
        Uri builtUri = Uri.parse(HTTP_ENTRY_URL).buildUpon()
                .appendQueryParameter(LOCALE, language[0])
                .appendQueryParameter(DATA_BY_ID, QUERY_PARAMETER_DATA_BY_ID)
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
    protected void onPostExecute(List<SummonerSpellEntry> summonerSpells) {
        super.onPostExecute(summonerSpells);
        mListener.summonerSpellTaskCompleted(summonerSpells);
    }
}
