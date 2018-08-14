package com.example.android.leaguestats.utilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JSONUtils {

    private static final String LOG_TAG = JSONUtils.class.getSimpleName();

    public static List<String> getStringListFromJSONArray(JSONArray jsonArray) throws JSONException {
        List<String> stringList = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            stringList.add(i, jsonArray.getString(i));
        }
        return stringList;
    }

    public static List<String> getSpellImage(JSONArray jsonArray, String JSONObjectKey, String key) throws JSONException {
        List<String> stringList = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject object = jsonArray.getJSONObject(i);
            JSONObject spellImage = object.getJSONObject(JSONObjectKey);
            stringList.add(spellImage.optString(key));
        }
        return stringList;
    }

    public static List<String> getListStringFromJSONObjectFromJSONArray(JSONArray jsonArray, String key) throws JSONException {
        List<String> stringList = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject object = jsonArray.getJSONObject(i);
            stringList.add(object.optString(key));
        }
        return stringList;
    }

    public static List<Integer> getListIntegerFromJSONObjectFromJSONArray(JSONArray jsonArray, String key) throws JSONException  {
        List<Integer> integerList = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject object = jsonArray.getJSONObject(i);
            integerList.add(object.optInt(key));
        }
        return integerList;
    }

    public static List<String> getSplashArtPathFromJSONObjectFromJSONArray(JSONArray jsonArray, String key, String championName) throws JSONException {
        List<String> stringList = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject object = jsonArray.getJSONObject(i);
            int splashArtId = object.optInt(key);
            String splashArtPath = championName + "_" + String.valueOf(splashArtId) + ".jpg";
            stringList.add(i, splashArtPath);
        }
        return stringList;
    }

    public static List<Double> getListFromJSONArrayFromJSONObjectFromJSONArray(JSONArray jsonArray, String JSONArrayKey) throws JSONException {
        List<Double> doubleList = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject object = jsonArray.getJSONObject(i);
            JSONArray array = object.getJSONArray(JSONArrayKey);
            for (int j = 0; j < array.length(); j++) {
                doubleList.add(array.getDouble(j));
            }
        }
        return doubleList;
    }
}
