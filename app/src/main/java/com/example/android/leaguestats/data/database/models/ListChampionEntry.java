package com.example.android.leaguestats.data.database.models;

import android.arch.persistence.room.ColumnInfo;

public class ListChampionEntry {

    private int id;
    @ColumnInfo(name = "champion_key")
    private String key;
    private String name;
    private String title;
    private String image;

    public ListChampionEntry(int id, String key, String name, String title, String image) {
        this.id = id;
        this.key = key;
        this.name = name;
        this.title = title;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getTitle() {
        return title;
    }

    public String getImage() {
        return image;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
