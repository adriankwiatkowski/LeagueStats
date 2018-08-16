package com.example.android.leaguestats.database;

import android.arch.persistence.room.TypeConverter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class DataConverter {

    private static final String STRING_DIVIDER = "_,_";

    @TypeConverter
    public String StringListToString(List<String> list) {
        if (list == null) return null;
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            builder.append(list.get(i));
            if (i != list.size() - 1){
                builder.append(STRING_DIVIDER);
            }
        }
        return builder.toString();
    }

    @TypeConverter
    public List<String> toStringList(String string) {
        if (string == null) return Collections.emptyList();
        return Arrays.asList(string.split(STRING_DIVIDER));
    }

    @TypeConverter
    public String DoubleListToString(List<Double> list) {
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
    public List<Double> toDoubleList(String string) {
        if (string == null) return Collections.emptyList();
        List<Double> doubleList = new ArrayList<>();
        for (String s: string.split(STRING_DIVIDER)) {
            doubleList.add(Double.parseDouble(s));
        }
        return doubleList;
    }

    @TypeConverter
    public String IntegerListToString(List<Integer> list) {
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
    public List<Integer> toIntegerList(String string) {
        if (string == null) return Collections.emptyList();
        List<Integer> doubleList = new ArrayList<>();
        for (String s: string.split(STRING_DIVIDER)) {
            doubleList.add(Integer.parseInt(s));
        }
        return doubleList;
    }
}
