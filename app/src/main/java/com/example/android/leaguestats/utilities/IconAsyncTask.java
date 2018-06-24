package com.example.android.leaguestats.utilities;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.example.android.leaguestats.BuildConfig;
import com.example.android.leaguestats.interfaces.IconTaskCompleted;
import com.example.android.leaguestats.interfaces.ResultTask;
import com.example.android.leaguestats.models.Icon;

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

public class IconAsyncTask extends AsyncTask<Void, Integer, List<Icon>> {

    private static final String PERSONAL_API_KEY = BuildConfig.HIDDEN_API_KEY;
    private static final String LOG_TAG = IconAsyncTask.class.getSimpleName();
    private static final int TIMEOUT = 10000;
    private static final int CONNECT_TIMEOUT = 15000;
    private static final int RESPONSE_CODE = 200;
    private static final String REQUEST_METHOD = "GET";
    private static final String ERROR_RETRIEVING_DATA = "Error retrieving data";
    private static final String ERROR_CLOSING_STREAM = "Error closing stream";
    private static final String ERROR_RESPONSE_CODE = "Error response code: ";
    private static final String HTTP_ENTRY_URL = "https://eun1.api.riotgames.com/lol/static-data/v3/profile-icons";
    private static final String API_KEY = "api_key";
    private static final String LOCALE = "locale";

    private IconTaskCompleted mIconListener;
    private ResultTask mResultListener;

    public IconAsyncTask(IconTaskCompleted iconListener, ResultTask resultListener) {
        mIconListener = iconListener;
        mResultListener = resultListener;
    }

    @Override
    protected List<Icon> doInBackground(Void... voids) {BufferedReader reader = null;
        String jsonResponse = "";
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try {
            URL url = createUrl();

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

    private List<Icon> getJsonData(String json) throws JSONException {
        JSONObject root = new JSONObject(json);

        List<Icon> iconList = new ArrayList<>();

        JSONObject data = root.getJSONObject("data");
        Iterator<String> iterator = data.keys();

        mResultListener.maxProgress(data.length());

        int progressUpdate = 0;
        publishProgress(progressUpdate);

        while (iterator.hasNext()) {
            JSONObject iconObject = data.getJSONObject(iterator.next());

            JSONObject iconImage = iconObject.getJSONObject("image");
            String groupString = iconImage.getString("group");
            String fullString = iconImage.getString("full");

            int iconId = iconObject.getInt("id");

            iconList.add(new Icon(groupString + "/" + fullString, iconId));
            Log.d(LOG_TAG, groupString + " " + fullString + " " + iconId);

            publishProgress(progressUpdate++);
        }

        return iconList;
    }

    private URL createUrl() {
        Uri builtUri = Uri.parse(HTTP_ENTRY_URL).buildUpon()
                .appendQueryParameter(LOCALE, "en_US")
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
    protected void onPostExecute(List<Icon> icons) {
        super.onPostExecute(icons);

        mIconListener.iconTaskCompleted(icons);
    }
}
