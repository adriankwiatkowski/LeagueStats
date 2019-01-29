package com.example.android.leaguestats.models;

import android.arch.persistence.room.ColumnInfo;

import com.example.android.leaguestats.data.database.entity.ChampionEntry;

import java.util.List;

public class Champion {

    @ColumnInfo(name = "champion_id")
    private int id;
    @ColumnInfo(name = "champion_name")
    private String name;
    @ColumnInfo(name = "champion_key")
    private String key;
    @ColumnInfo(name = "champion_title")
    private String title;
    @ColumnInfo(name = "champion_image_id")
    private String championImageId;
    @ColumnInfo(name = "champion_splash_art_id")
    private List<Integer> splashArtId;
    @ColumnInfo(name = "champion_splash_art_name")
    private List<String> splashArtName;

    public Champion(int id, String key, String name, String title, String championImageId, List<Integer> splashArtId, List<String> splashArtName) {
        this.id = id;
        this.key = key;
        this.name = name;
        this.title = title;
        this.championImageId = championImageId;
        this.splashArtId = splashArtId;
        this.splashArtName = splashArtName;
    }

    public Champion(ChampionEntry championEntry) {
        this.id = championEntry.getId();
        this.name = championEntry.getName();
        this.key = championEntry.getKey();
        this.title = championEntry.getTitle();
        this.championImageId = championEntry.getImageId();
        this.splashArtId = championEntry.getSplashArtId();
        this.splashArtName = championEntry.getSplashArtName();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getChampionImageId() {
        return championImageId;
    }

    public void setChampionImageId(String championImageId) {
        this.championImageId = championImageId;
    }

    public List<Integer> getSplashArtId() {
        return splashArtId;
    }

    public void setSplashArtId(List<Integer> splashArtId) {
        this.splashArtId = splashArtId;
    }

    public List<String> getSplashArtName() {
        return splashArtName;
    }

    public void setSplashArtName(List<String> splashArtName) {
        this.splashArtName = splashArtName;
    }
}
