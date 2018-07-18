package com.example.android.leaguestats.utilities.AsyncTasks;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.example.android.leaguestats.SummonerActivity;
import com.example.android.leaguestats.interfaces.SummonerTaskCompleted;
import com.example.android.leaguestats.models.Summoner;
import com.example.android.leaguestats.utilities.DataUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class SummonerAsyncTask extends AsyncTask<String, Void, Summoner> {

    private static final String LOG_TAG = SummonerActivity.class.getSimpleName();
    private static final int TIMEOUT = 10000;
    private static final int CONNECT_TIMEOUT = 15000;
    private static final int RESPONSE_CODE = 200;
    private static final String ERROR_RETRIEVING_DATA = "Error retrieving data";
    private static final String ERROR_CLOSING_STREAM = "Error closing stream";
    private static final String ERROR_RESPONSE_CODE = "Error response code: ";

    private SummonerTaskCompleted mListener;

    public SummonerAsyncTask(SummonerTaskCompleted listener) {
        mListener = listener;
    }

    @Override
    protected Summoner doInBackground(String... strings) {
        BufferedReader reader = null;
        String jsonResponse = "";
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try {
            URL url = createUrl(strings);

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(TIMEOUT);
            urlConnection.setConnectTimeout(CONNECT_TIMEOUT);
            urlConnection.setRequestMethod(DataUtils.REQUEST_METHOD_GET);
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

    private Summoner getJsonData(String json) throws JSONException {
        JSONObject root = new JSONObject(json);

        int profileIconId = root.getInt("profileIconId");
        String summonerName = root.getString("name");
        long summonerLevel = root.getLong("summonerLevel");
        long accountId = root.getLong("accountId");
        long summonerId = root.getLong("id");
        long revisionDate = root.getLong("revisionDate");

        return new Summoner(profileIconId, summonerName, summonerLevel, accountId, summonerId, revisionDate);
    }

    // [0] entry url
    // [1] summoner name
    private URL createUrl(String[] strings) {

        String HTTP_ENTRY_URL = strings[0];

        Uri builtUri = Uri.parse(HTTP_ENTRY_URL).buildUpon()
                .appendPath("lol")
                .appendPath("summoner")
                .appendPath("v3")
                .appendPath("summoners")
                .appendPath("by-name")
                .appendPath(strings[1])
                .appendQueryParameter(DataUtils.API_KEY, DataUtils.PERSONAL_API_KEY)
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
    protected void onPostExecute(Summoner summoner) {
        super.onPostExecute(summoner);
        mListener.summonerTaskCompleted(summoner);
    }
}
