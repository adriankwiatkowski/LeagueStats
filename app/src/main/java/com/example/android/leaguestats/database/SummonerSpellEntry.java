package com.example.android.leaguestats.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.List;

@Entity(tableName = "summoner_spell")
public class SummonerSpellEntry {

    @PrimaryKey
    private int id;
    @ColumnInfo(name = "summoner_spell_key")
    private String key;
    private String name;
    private String description;
    private String image;
    private int cost;
    private int cooldown;
    private int range;
    private List<String> modes;

    public SummonerSpellEntry(int id, String key, String name, String description,
                              String image, int cost, int cooldown, int range, List<String> modes) {
        this.id = id;
        this.key = key;
        this.name = name;
        this.description = description;
        this.image = image;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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
}
