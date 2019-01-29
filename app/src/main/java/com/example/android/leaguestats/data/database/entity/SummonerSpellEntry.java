package com.example.android.leaguestats.data.database.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.List;

@Entity(tableName = "summoner_spell")
public class SummonerSpellEntry {

    @PrimaryKey
    @ColumnInfo(name = "summoner_spell_id")
    private int id;
    @ColumnInfo(name = "summoner_spell_name")
    private String name;
    @ColumnInfo(name = "summoner_spell_key")
    private String key;
    @ColumnInfo(name = "summoner_spell_description")
    private String description;
    @ColumnInfo(name = "summoner_spell_image_id")
    private String imageId;
    @ColumnInfo(name = "summoner_spell_cost")
    private int cost;
    @ColumnInfo(name = "summoner_spell_cooldown")
    private int cooldown;
    @ColumnInfo(name = "summoner_spell_range")
    private int range;
    @ColumnInfo(name = "summoner_spell_modes")
    private List<String> modes;

    public SummonerSpellEntry(int id, String key, String name, String description, String imageId, int cost, int cooldown, int range, List<String> modes) {
        this.id = id;
        this.key = key;
        this.name = name;
        this.description = description;
        this.imageId = imageId;
        this.cost = cost;
        this.cooldown = cooldown;
        this.range = range;
        this.modes = modes;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public int getCooldown() {
        return cooldown;
    }

    public void setCooldown(int cooldown) {
        this.cooldown = cooldown;
    }

    public int getRange() {
        return range;
    }

    public void setRange(int range) {
        this.range = range;
    }

    public List<String> getModes() {
        return modes;
    }

    public void setModes(List<String> modes) {
        this.modes = modes;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }
}
