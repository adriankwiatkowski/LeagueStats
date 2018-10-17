package com.example.android.leaguestats.data.database.models;

import android.arch.persistence.room.ColumnInfo;

public class ListSummonerSpellEntry {

    private int id;
    @ColumnInfo(name = "summoner_spell_key")
    private String key;
    private String name;
    private int cooldown;
    private String image;

    public ListSummonerSpellEntry(int id, String key, String name, int cooldown, String image) {
        this.id = id;
        this.key = key;
        this.name = name;
        this.cooldown = cooldown;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getCooldown() {
        return cooldown;
    }

    public String getImage() {
        return image;
    }

    public String getKey() {
        return key;
    }
}
