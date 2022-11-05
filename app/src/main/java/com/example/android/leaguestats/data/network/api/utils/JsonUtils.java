package com.example.android.leaguestats.data.network.api.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

class JsonUtils {

    static List<String> getStringsFromArray(JSONArray jsonArray) throws JSONException {
        List<String> stringList = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            stringList.add(i, jsonArray.getString(i));
        }
        return stringList;
    }

    static List<String> getStringsFromObjectsInArray(JSONArray jsonArray, String key) throws JSONException {
        List<String> stringList = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject object = jsonArray.getJSONObject(i);
            stringList.add(object.optString(key));
        }
        return stringList;
    }

    static List<Integer> getIntegersFromObjectsInArray(JSONArray jsonArray, String key) throws JSONException  {
        List<Integer> integerList = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject object = jsonArray.getJSONObject(i);
            integerList.add(object.optInt(key));
        }
        return integerList;
    }
}
