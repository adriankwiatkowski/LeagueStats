package com.example.android.leaguestats.data.network.api.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MatchList {

    @SerializedName("platformId")
    @Expose
    private String platformId;
    @SerializedName("gameId")
    @Expose
    private long gameId;
    @SerializedName("champion")
    @Expose
    private int champion;
    @SerializedName("queue")
    @Expose
    private int queue;
    @SerializedName("season")
    @Expose
    private int season;
    @SerializedName("timestamp")
    @Expose
    private long timestamp;
    @SerializedName("role")
    @Expose
    private String role;
    @SerializedName("lane")
    @Expose
    private String lane;

    /**
     * No args constructor for use in serialization
     *
     */
    public MatchList() {
    }

    /**
     *
     * @param timestamp
     * @param champion
     * @param queue
     * @param season
     * @param gameId
     * @param role
     * @param platformId
     * @param lane
     */
    public MatchList(String platformId, long gameId, int champion, int queue, int season, long timestamp, String role, String lane) {
        super();
        this.platformId = platformId;
        this.gameId = gameId;
        this.champion = champion;
        this.queue = queue;
        this.season = season;
        this.timestamp = timestamp;
        this.role = role;
        this.lane = lane;
    }

    public String getPlatformId() {
        return platformId;
    }

    public void setPlatformId(String platformId) {
        this.platformId = platformId;
    }

    public long getGameId() {
        return gameId;
    }

    public void setGameId(long gameId) {
        this.gameId = gameId;
    }

    public int getChampion() {
        return champion;
    }

    public void setChampion(int champion) {
        this.champion = champion;
    }

    public int getQueue() {
        return queue;
    }

    public void setQueue(int queue) {
        this.queue = queue;
    }

    public int getSeason() {
        return season;
    }

    public void setSeason(int season) {
        this.season = season;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getLane() {
        return lane;
    }

    public void setLane(String lane) {
        this.lane = lane;
    }
}
