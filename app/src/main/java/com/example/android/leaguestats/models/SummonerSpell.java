package com.example.android.leaguestats.models;

import android.arch.persistence.room.ColumnInfo;

public class SummonerSpell {

    @ColumnInfo(name = "summoner_spell_id")
    private int id;
    @ColumnInfo(name = "summoner_spell_name")
    private String name;
    @ColumnInfo(name = "summoner_spell_description")
    private String description;
    @ColumnInfo(name = "summoner_spell_key")
    private String key;
    @ColumnInfo(name = "summoner_spell_image_id")
    private String summonerSpellImageId;
    @ColumnInfo(name = "summoner_spell_cooldown")
    private int cooldown;

    public SummonerSpell(int id, String key, String name, String description, String summonerSpellImageId, int cooldown) {
        this.id = id;
        this.key = key;
        this.name = name;
        this.description = description;
        this.summonerSpellImageId = summonerSpellImageId;
        this.cooldown = cooldown;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSummonerSpellImageId() {
        return summonerSpellImageId;
    }

    public void setSummonerSpellImageId(String summonerSpellImageId) {
        this.summonerSpellImageId = summonerSpellImageId;
    }

    public int getCooldown() {
        return cooldown;
    }

    public void setCooldown(int cooldown) {
        this.cooldown = cooldown;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
