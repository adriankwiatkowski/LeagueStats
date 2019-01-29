package com.example.android.leaguestats.models;

import android.support.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Mastery {

    @SerializedName("playerId")
    @Expose
    private int mPlayerId;
    @SerializedName("championId")
    @Expose
    private int mChampionId;
    @SerializedName("championLevel")
    @Expose
    private int mChampionLevel;
    @SerializedName("championPoints")
    @Expose
    private int mChampionPoints;
    @SerializedName("lastPlayTime")
    @Expose
    private long mLastPlayTime;
    @SerializedName("championPointsSinceLastLevel")
    @Expose
    private int mChampionPointsSinceLastLevel;
    @SerializedName("championPointsUntilNextLevel")
    @Expose
    private int mChampionPointsUntilNextLevel;
    @SerializedName("chestGranted")
    @Expose
    private boolean mIsChestGranted;
    @SerializedName("tokensEarned")
    @Expose
    private int mTokensEarned;
    @Nullable
    private String mChampionName;
    @Nullable
    private String mChampionImageId;

    public Mastery(int playerId, int championId, int championLevel, int championPoints,
                   long lastPlayTime, int championPointsSinceLastLevel,
                   int championPointsUntilNextLevel, boolean chestGranted,
                   int tokensEarned) {

        mPlayerId = playerId;
        mChampionId = championId;
        mChampionLevel = championLevel;
        mChampionPoints = championPoints;
        mLastPlayTime = lastPlayTime;
        mChampionPointsSinceLastLevel = championPointsSinceLastLevel;
        mChampionPointsUntilNextLevel = championPointsUntilNextLevel;
        mIsChestGranted = chestGranted;
        mTokensEarned = tokensEarned;
    }

    public int getChampionId() {
        return mChampionId;
    }

    public void setChampionId(int championId) {
        this.mChampionId = championId;
    }

    public int getChampionLevel() {
        return mChampionLevel;
    }

    public void setChampionLevel(int championLevel) {
        this.mChampionLevel = championLevel;
    }

    public int getChampionPoints() {
        return mChampionPoints;
    }

    public void setChampionPoints(int championPoints) {
        this.mChampionPoints = championPoints;
    }

    public boolean isChestGranted() {
        return mIsChestGranted;
    }

    public void setChestGranted(boolean chestGranted) {
        this.mIsChestGranted = chestGranted;
    }

    public long getLastPlayTime() {
        return mLastPlayTime;
    }

    public void setLastPlayTime(long lastPlayTime) {
        this.mLastPlayTime = lastPlayTime;
    }

    public String getChampionName() {
        return mChampionName;
    }

    public void setChampionName(String championName) {
        this.mChampionName = championName;
    }

    public String getChampionImageId() {
        return mChampionImageId;
    }

    public void setChampionImageId(String championImageId) {
        this.mChampionImageId = championImageId;
    }

    public int getPlayerId() {
        return mPlayerId;
    }

    public void setPlayerId(int playerId) {
        this.mPlayerId = playerId;
    }

    public int getChampionPointsSinceLastLevel() {
        return mChampionPointsSinceLastLevel;
    }

    public void setChampionPointsSinceLastLevel(int championPointsSinceLastLevel) {
        this.mChampionPointsSinceLastLevel = championPointsSinceLastLevel;
    }

    public int getTokensEarned() {
        return mTokensEarned;
    }

    public void setTokensEarned(int tokensEarned) {
        this.mTokensEarned = tokensEarned;
    }

    public int getChampionPointsUntilNextLevel() {
        return mChampionPointsUntilNextLevel;
    }

    public void setChampionPointsUntilNextLevel(int championPointsUntilNextLevel) {
        this.mChampionPointsUntilNextLevel = championPointsUntilNextLevel;
    }
}
