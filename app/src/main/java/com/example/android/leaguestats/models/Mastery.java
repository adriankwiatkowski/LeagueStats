package com.example.android.leaguestats.models;

import com.example.android.leaguestats.data.database.entity.ChampionEntry;
import com.example.android.leaguestats.data.network.api.models.MasteryResponse;

import java.util.List;

public class Mastery {

    private int mChampionId;
    private int mChampionLevel;
    private int mChampionPoints;
    private long mLastPlayTime;
    private boolean mIsChestGranted;
    private String mChampionName;
    private String mChampionImage;

    public Mastery(MasteryResponse masteries, List<ChampionEntry> championEntries) {

        String championName = "";
        String championThumbnail = "";
        int championId = 0;
        int championLevel = 0;
        int championPoints = 0;
        long lastPlayTime = 0;
        boolean isChestGranted = false;

        // Find champion for given id.
        for (int j = 0; j < championEntries.size(); j++) {
            if (masteries.getChampionId() == (championEntries.get(j).getId())) {
                championName = championEntries.get(j).getName();
                championThumbnail = championEntries.get(j).getImage();
                championId = masteries.getChampionId();
                championLevel = masteries.getChampionLevel();
                championPoints = masteries.getChampionPoints();
                lastPlayTime = masteries.getLastPlayTime();
                isChestGranted = masteries.isChestGranted();
            }
        }
        mChampionName = championName;
        mChampionImage = championThumbnail;
        mChampionId = championId;
        mChampionLevel = championLevel;
        mChampionPoints = championPoints;
        mLastPlayTime = lastPlayTime;
        mIsChestGranted = isChestGranted;
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

    public String getChampionImage() {
        return mChampionImage;
    }

    public void setChampionImage(String championImage) {
        this.mChampionImage = championImage;
    }
}
