package com.example.android.leaguestats.utilities.AsyncTasks;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.example.android.leaguestats.interfaces.MatchTaskCompleted;
import com.example.android.leaguestats.models.Match;
import com.example.android.leaguestats.utilities.DataUtils;

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

public class MatchAsyncTask extends AsyncTask<String, Void, ArrayList<Match>> {

    private static final String LOG_TAG = MatchAsyncTask.class.getSimpleName();
    private static final int TIMEOUT = 10000;
    private static final int CONNECT_TIMEOUT = 15000;
    private static final int RESPONSE_CODE = 200;
    private static final String ERROR_RETRIEVING_DATA = "Error retrieving data";
    private static final String ERROR_CLOSING_STREAM = "Error closing stream";
    private static final String ERROR_RESPONSE_CODE = "Error response code: ";

    private MatchTaskCompleted mListener;

    public MatchAsyncTask(MatchTaskCompleted listener) {
        mListener = listener;
    }

    @Override
    protected ArrayList<Match> doInBackground(String... strings) {
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

    private ArrayList<Match> getJsonData(String json) throws JSONException {
        JSONObject root = new JSONObject(json);
        ArrayList<Match> matches = new ArrayList<>();

        JSONArray participantIdentities = root.getJSONArray("participantIdentities");

        int participantId = 0;
        String summonerName = "";
        for (int i = 0; i < participantIdentities.length(); i++) {
            JSONObject object = participantIdentities.getJSONObject(i);
            participantId = object.getInt("participantId");
            JSONObject player = object.getJSONObject("player");
            summonerName = player.getString("summonerName");
        }

        int teamId = 0;
        int championId = 0;
        int spell1 = 0;
        int spell2 = 0;
        JSONArray participants = root.getJSONArray("participants");
        for (int i = 0; i < participants.length(); i++) {
            JSONObject object = participants.getJSONObject(i);
            teamId = object.getInt("teamId");
            championId = object.getInt("championId");
            spell1 = object.getInt("spell1Id");
            spell2 = object.getInt("spell2Id");
        }

        long gameDuration = root.getLong("gameDuration");
        long gameCreation = root.getLong("gameCreation");

        matches.add(new Match(participantId, summonerName, teamId,
                championId, spell1, spell2, gameDuration, gameCreation));

        return matches;
    }

    // sort_param[0] - summoner region, entry region url
    // sort_param[1] - game id
    private URL createUrl(String[] sort_param) {

        String HTTP_ENTRY_URL = sort_param[0];

        Uri builtUri = Uri.parse(HTTP_ENTRY_URL).buildUpon()
                .appendPath("lol")
                .appendPath("match")
                .appendPath("v3")
                .appendPath("matches")
                .appendPath(sort_param[1])
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
    protected void onPostExecute(ArrayList<Match> matches) {
        super.onPostExecute(matches);

        mListener.matchTaskCompleted(matches);
    }

    private String getStringIfNotNull(JSONObject object, String key) throws JSONException {
        if (object.has(key)) {
            return object.getString(key);
        } else return "";
    }

    private int getIntIfNotNull(JSONObject object, String key) throws JSONException {
        if (object.has(key)) {
            return object.getInt(key);
        } else return 0;
    }
}
