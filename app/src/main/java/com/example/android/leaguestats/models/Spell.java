package com.example.android.leaguestats.models;

import java.util.ArrayList;
import java.util.List;

public class Spell {

    private String mName;
    private String mDescription;
    private String mImage;
    private String mResource;
    private String mCooldown;
    private String mCost;
    private List<Double> mCooldownArray;
    private List<Integer> mCostArray;

    public Spell(String name, String description, String image, String resource,
                 ArrayList<Double> cooldown, ArrayList<Integer> cost) {

        this.mName = name;
        this.mDescription = description;
        this.mImage = image;
        this.mResource = resource;
        this.mCooldownArray = cooldown;
        this.mCostArray = cost;
    }

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

    public List<Double> getCooldownList() {
        return mCooldownArray;
    }

    public void setCooldownList(List<Double> cooldown) {
        this.mCooldownArray = cooldown;
    }

    public List<Integer> getCostList() {
        return mCostArray;
    }

    public void setCostList(List<Integer> cost) {
        this.mCostArray = cost;
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

    public void setCooldown(String mCooldown) {
        this.mCooldown = mCooldown;
    }

    public String getCost() {
        return mCost;
    }

    public void setCost(String mCost) {
        this.mCost = mCost;
    }
}
