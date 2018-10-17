package com.example.android.leaguestats.data.network.api.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MasteryResponse {

    @SerializedName("playerId")
    @Expose
    private int playerId;
    @SerializedName("championId")
    @Expose
    private int championId;
    @SerializedName("championLevel")
    @Expose
    private int championLevel;
    @SerializedName("championPoints")
    @Expose
    private int championPoints;
    @SerializedName("lastPlayTime")
    @Expose
    private long lastPlayTime;
    @SerializedName("championPointsSinceLastLevel")
    @Expose
    private int championPointsSinceLastLevel;
    @SerializedName("championPointsUntilNextLevel")
    @Expose
    private int championPointsUntilNextLevel;
    @SerializedName("chestGranted")
    @Expose
    private boolean chestGranted;
    @SerializedName("tokensEarned")
    @Expose
    private int tokensEarned;

    /**
     * No args constructor for use in serialization
     */
    public MasteryResponse() {
    }

    public MasteryResponse(int playerId, int championId, int championLevel, int championPoints,
                           long lastPlayTime, int championPointsSinceLastLevel,
                           int championPointsUntilNextLevel, boolean chestGranted,
                           int tokensEarned) {
        super();
        this.playerId = playerId;
        this.championId = championId;
        this.championLevel = championLevel;
        this.championPoints = championPoints;
        this.lastPlayTime = lastPlayTime;
        this.championPointsSinceLastLevel = championPointsSinceLastLevel;
        this.championPointsUntilNextLevel = championPointsUntilNextLevel;
        this.chestGranted = chestGranted;
        this.tokensEarned = tokensEarned;
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public int getChampionId() {
        return championId;
    }

    public void setChampionId(int championId) {
        this.championId = championId;
    }

    public int getChampionLevel() {
        return championLevel;
    }

    public void setChampionLevel(int championLevel) {
        this.championLevel = championLevel;
    }

    public int getChampionPoints() {
        return championPoints;
    }

    public void setChampionPoints(int championPoints) {
        this.championPoints = championPoints;
    }

    public long getLastPlayTime() {
        return lastPlayTime;
    }

    public void setLastPlayTime(long lastPlayTime) {
        this.lastPlayTime = lastPlayTime;
    }

    public int getChampionPointsSinceLastLevel() {
        return championPointsSinceLastLevel;
    }

    public void setChampionPointsSinceLastLevel(int championPointsSinceLastLevel) {
        this.championPointsSinceLastLevel = championPointsSinceLastLevel;
    }

    public int getChampionPointsUntilNextLevel() {
        return championPointsUntilNextLevel;
    }

    public void setChampionPointsUntilNextLevel(int championPointsUntilNextLevel) {
        this.championPointsUntilNextLevel = championPointsUntilNextLevel;
    }

    public boolean isChestGranted() {
        return chestGranted;
    }

    public void setChestGranted(boolean chestGranted) {
        this.chestGranted = chestGranted;
    }

    public int getTokensEarned() {
        return tokensEarned;
    }

    public void setTokensEarned(int tokensEarned) {
        this.tokensEarned = tokensEarned;
    }

}
