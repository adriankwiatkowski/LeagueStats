package com.example.android.leaguestats.models;

public class SummonerSpell {

    private int mId;
    private String mKey;
    private String mName;
    private String mDescription;
    private String mImage;
    private int mCost;
    private int mCooldown;
    private int mRange;
    private String mModes;

    public SummonerSpell(int id, String key, String name, String description,
                         String image, int cost, int cooldown, int range, String modes) {

        this.mId = id;
        this.mKey = key;
        this.mName = name;
        this.mDescription = description;
        this.mImage = image;
        this.mCost = cost;
        this.mCooldown = cooldown;
        this.mRange = range;
        this.mModes = modes;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        this.mId = id;
    }

    public String getKey() {
        return mKey;
    }

    public void setKey(String key) {
        this.mKey = key;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        this.mDescription = description;
    }

    public String getImage() {
        return mImage;
    }

    public void setImage(String image) {
        this.mImage = image;
    }

    public int getCost() {
        return mCost;
    }

    public void setCost(int cost) {
        this.mCost = cost;
    }

    public int getCooldown() {
        return mCooldown;
    }

    public void setCooldown(int cooldown) {
        this.mCooldown = cooldown;
    }

    public int getRange() {
        return mRange;
    }

    public void setRange(int range) {
        this.mRange = range;
    }

    public String getModes() {
        return mModes;
    }

    public void setModes(String modes) {
        this.mModes = modes;
    }
}
