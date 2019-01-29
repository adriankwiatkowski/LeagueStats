package com.example.android.leaguestats.data.database;

import android.arch.persistence.room.TypeConverter;

import com.example.android.leaguestats.data.database.entity.Spell;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class DataConverter {

    private static Gson gson = new Gson();

    private static final String STRING_DIVIDER = "_,_";

    @TypeConverter
    public static List<Spell> toSpellList(String string) {
        if (string == null) {
            return Collections.emptyList();
        }
        Type type = new TypeToken<List<Spell>>() {}.getType();
        return gson.fromJson(string, type);
    }

    @TypeConverter
    public static String spellListToString(List<Spell> spellList) {
        return gson.toJson(spellList);
    }

    @TypeConverter
    public static String stringListToString(List<String> list) {
        if (list == null) return null;
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            builder.append(list.get(i));
            if (i != list.size() - 1) {
                builder.append(STRING_DIVIDER);
            }
        }
        return builder.toString();
    }

    @TypeConverter
    public static List<String> toStringList(String string) {
        if (string == null) return Collections.emptyList();
        return Arrays.asList(string.split(STRING_DIVIDER));
    }

    @TypeConverter
    public static String doubleListToString(List<Double> list) {
        if (list == null) return null;
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            builder.append(list.get(i));
            if (i != list.size() - 1) {
                builder.append(STRING_DIVIDER);
            }
        }
        return builder.toString();
    }

    @TypeConverter
    public static List<Double> toDoubleList(String string) {
        if (string == null) return Collections.emptyList();
        List<Double> list = new ArrayList<>();
        for (String s : string.split(STRING_DIVIDER)) {
            list.add(Double.parseDouble(s));
        }
        return list;
    }

    @TypeConverter
    public static String intListToString(List<Integer> list) {
        if (list == null) return null;
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            builder.append(list.get(i));
            if (i != list.size() - 1) {
                builder.append(STRING_DIVIDER);
            }
        }
        return builder.toString();
    }

    @TypeConverter
    public static List<Integer> stringToIntList(String string) {
        if (string == null) {
            return Collections.emptyList();
        }
        List<Integer> list = new ArrayList<>();
        for (String s : string.split(STRING_DIVIDER)) {
            list.add(Integer.parseInt(s));
        }
        return list;
    }
}
