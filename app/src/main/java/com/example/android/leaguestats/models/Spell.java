package com.example.android.leaguestats.models;

public class Spell {

    private String mName;
    private String mDescription;
    private String mImage;
    private String mResource;
    private String mCooldown;
    private String mCost;

    public Spell(String name, String description, String image, String resource, String cooldown, String cost) {
        this.mName = name;
        this.mDescription = description;
        this.mImage = image;
        this.mResource = resource;
        this.mCooldown = cooldown;
        this.mCost = cost;
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

    public String getResource() {
        return mResource;
    }

    public void setResource(String resource) {
        this.mResource = resource;
    }

    public String getCooldown() {
        return mCooldown;
    }

    public void setCooldown(String cooldown) {
        this.mCooldown = cooldown;
    }

    public String getCost() {
        return mCost;
    }

    public void setCost(String cost) {
        this.mCost = cost;
    }
}
