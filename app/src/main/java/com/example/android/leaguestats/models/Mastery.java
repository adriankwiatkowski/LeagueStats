package com.example.android.leaguestats.models;

public class Mastery {

    private long mPlayerId;
    private long mChampionId;
    private int mChampionLevel;
    private int mChampionPoints;
    private long mLastPlayTime;
    private boolean mChestGranted;
    private String mChampionName;
    private String mChampionImage;

    public Mastery(long playerId, long championId, int championLevel, int championPoints, long lastPlayTime, boolean chestGranted) {
        mPlayerId = playerId;
        mChampionId = championId;
        mChampionLevel = championLevel;
        mChampionPoints = championPoints;
        mLastPlayTime = lastPlayTime;
        mChestGranted = chestGranted;
    }

    public Mastery(String name, String image, long championId, int championLevel, int championPoints, long lastPlayTime, boolean chestGranted) {
        mChampionName = name;
        mChampionImage = image;
        mChampionId = championId;
        mChampionLevel = championLevel;
        mChampionPoints = championPoints;
        mLastPlayTime = lastPlayTime;
        mChestGranted = chestGranted;
    }

    public long getPlayerId() {
        return mPlayerId;
    }

    public void setPlayerId(long playerId) {
        this.mPlayerId = playerId;
    }

    public long getChampionId() {
        return mChampionId;
    }

    public void setChampionId(long championId) {
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
        return mChestGranted;
    }

    public void setChestGranted(boolean chestGranted) {
        this.mChestGranted = chestGranted;
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

    public String getChampionImage() {
        return mChampionImage;
    }

    public void setChampionImage(String championImage) {
        this.mChampionImage = championImage;
    }
}
