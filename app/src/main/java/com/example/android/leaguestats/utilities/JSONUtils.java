package com.example.android.leaguestats.utilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JSONUtils {

    private static final String LOG_TAG = JSONUtils.class.getSimpleName();

    public static String getStringFromJSONArray(JSONArray jsonArray) throws JSONException {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < jsonArray.length(); i++) {
            builder.append(jsonArray.getString(i));
            if (i != jsonArray.length() - 1) {
                builder.append(DataUtils.STRING_DIVIDER);
            }
        }
        return builder.toString();
    }

    public static String getStringSpellImage(
            JSONArray jsonArray, String JSONObjectKey, String key) throws JSONException {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject object = jsonArray.getJSONObject(i);
            JSONObject spellImage = object.getJSONObject(JSONObjectKey);
            builder.append(spellImage.optString(key));
            if (i != jsonArray.length() - 1) {
                builder.append(DataUtils.STRING_DIVIDER);
            }
        }
        return builder.toString();
    }

    public static String getStringFromJSONObjectFromJSONArray(JSONArray jsonArray, String key) throws JSONException {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject object = jsonArray.getJSONObject(i);
            builder.append(object.optString(key));
            if (i != jsonArray.length() - 1) {
                builder.append(DataUtils.STRING_DIVIDER);
            }
        }
        return builder.toString();
    }

    public static String getSplashArtPathFromJSONObjectFromJSONArray(
            JSONArray jsonArray, String key, String championName) throws JSONException {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject object = jsonArray.getJSONObject(i);
            int splashArtId = object.optInt(key);
            String splashArtPath = championName + "_" + String.valueOf(splashArtId) + ".jpg";
            builder.append(splashArtPath);
            if (i != jsonArray.length() - 1) {
                builder.append(DataUtils.STRING_DIVIDER);
            }
        }
        return builder.toString();
    }

    public static String getStringFromJSONArrayFromJSONObjectFromJSONArray(
            JSONArray jsonArray, String JSONArrayKey) throws JSONException {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject object = jsonArray.getJSONObject(i);
            JSONArray array = object.getJSONArray(JSONArrayKey);
            for (int j = 0; j < array.length(); j++) {
                builder.append(array.getDouble(j)).append(DataUtils.STRING_DIVIDER);
            }
        }
        for (int i = 0; i < DataUtils.STRING_DIVIDER.length(); i++) {
            builder.deleteCharAt(builder.length() - 1);
        };
        return builder.toString();
    }
}
