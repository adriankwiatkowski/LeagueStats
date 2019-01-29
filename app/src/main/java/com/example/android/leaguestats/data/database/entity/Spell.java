package com.example.android.leaguestats.data.database.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Ignore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Spell {

    @ColumnInfo(name = "champion_spell_name")
    private String mName;
    @ColumnInfo(name = "champion_spell_description")
    private String mDescription;
    @ColumnInfo(name = "champion_spell_tooltip")
    private String mTooltip;
    @ColumnInfo(name = "champion_spell_image_id")
    private String mImageId;
    @ColumnInfo(name = "champion_spell_resource")
    private String mResource;
    @ColumnInfo(name = "champion_spell_range_burn")
    private String mRangeBurn;
    @ColumnInfo(name = "champion_spell_cooldown")
    private String mCooldown;
    @ColumnInfo(name = "champion_spell_cost_type")
    private String mCostType;
    @ColumnInfo(name = "champion_spell_cost_burn")
    private String mCostBurn;
    @ColumnInfo(name = "champion_spell_max_rank")
    private int mMaxRank;
    @ColumnInfo(name = "champion_spell_effect_burn")
    private List<String> mEffectBurn;
    @ColumnInfo(name = "champion_spell_vars_cooef")
    private List<Double> mVarsCoeff;
    @ColumnInfo(name = "champion_spell_vars_key")
    private List<String> mVarsKey;

    public Spell(String name, String description, String tooltip, String id, String resource,
                 String cooldown , String costType, String range, String costBurn,
                 int maxRank, List<String> effectBurn, List<Double> varsCoeff, List<String> varsKey) {
        this.mName = name;
        this.mDescription = description;
        this.mTooltip = tooltip;
        this.mImageId = id;
        this.mResource = resource;
        this.mRangeBurn = range;
        this.mCooldown = cooldown;
        this.mCostType = costType;
        this.mCostBurn = costBurn;
        this.mMaxRank = maxRank;
        this.mEffectBurn = effectBurn;
        this.mVarsCoeff = varsCoeff;
        this.mVarsKey = varsKey;
    }

    @Ignore
    public Spell(JSONObject spellObject) throws JSONException {
        mName = spellObject.optString("name");
        mDescription = spellObject.optString("description");
        mTooltip = spellObject.optString("tooltip");
        mResource = spellObject.optString("resource");
        mRangeBurn = spellObject.getString("rangeBurn");
        mCooldown = spellObject.optString("cooldownBurn");
        mCostType = spellObject.getString("costType");
        mCostBurn = spellObject.optString("costBurn");
        mMaxRank = spellObject.optInt("maxrank");
        JSONObject imageObject = spellObject.getJSONObject("image");
        mImageId = imageObject.optString("full");

        List<String> effectBurnList = new ArrayList<>();
        JSONArray effectBurnArray = spellObject.getJSONArray("effectBurn");
        for (int i = 0; i < effectBurnArray.length(); i++) {
            effectBurnList.add(effectBurnArray.getString(i));
        }
        mEffectBurn = effectBurnList;

        List<Double> varsCoeffList = new ArrayList<>();
        List<String> varsKeyList = new ArrayList<>();
        JSONArray varsArray = spellObject.getJSONArray("vars");
        for (int i = 0; i < varsArray.length(); i++) {
            JSONObject varsObject = varsArray.getJSONObject(i);
            varsCoeffList.add(varsObject.optDouble("coeff", 0));
            varsKeyList.add(varsObject.getString("key"));
        }
        mVarsCoeff = varsCoeffList;
        mVarsKey = varsKeyList;
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

    public String getImageId() {
        return mImageId;
    }

    public void setImageId(String image) {
        this.mImageId = image;
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

    public String getTooltip() {
        return mTooltip;
    }

    public void setTooltip(String tooltip) {
        this.mTooltip = tooltip;
    }

    public List<String> getEffectBurn() {
        return mEffectBurn;
    }

    public void setEffectBurn(List<String> effectBurn) {
        this.mEffectBurn = effectBurn;
    }

    public List<String> getVarsKey() {
        return mVarsKey;
    }

    public void setVarsKey(List<String> varsKey) {
        this.mVarsKey = varsKey;
    }

    public List<Double> getVarsCoeff() {
        return mVarsCoeff;
    }

    public void setVarsCoeff(List<Double> varsCoeff) {
        this.mVarsCoeff = varsCoeff;
    }

    public String getCostType() {
        return mCostType;
    }

    public void setCostType(String costType) {
        this.mCostType = costType;
    }

    public String getRange() {
        return mRangeBurn;
    }

    public void setRange(String range) {
        this.mRangeBurn = range;
    }

    public String getCostBurn() {
        return mCostBurn;
    }

    public void setCostBurn(String costBurn) {
        this.mCostBurn = costBurn;
    }

    public Integer getMaxRank() {
        return mMaxRank;
    }

    public void setMaxRank(Integer maxRank) {
        this.mMaxRank = maxRank;
    }
}
