package com.example.android.leaguestats.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Mastery implements Parcelable {

    public static final Creator<Mastery> CREATOR = new Creator<Mastery>() {
        @Override
        public Mastery createFromParcel(Parcel in) {
            return new Mastery(in);
        }

        @Override
        public Mastery[] newArray(int size) {
            return new Mastery[size];
        }
    };

    private long mPlayerId;
    private long mChampionId;
    private int mChampionLevel;
    private int mChampionPoints;
    private long mLastPlayTime;
    private boolean mIsChestGranted;
    private String mChampionName;
    private String mChampionImage;

    protected Mastery(Parcel in) {
        mPlayerId = in.readLong();
        mChampionId = in.readLong();
        mChampionLevel = in.readInt();
        mChampionPoints = in.readInt();
        mLastPlayTime = in.readLong();
        mIsChestGranted = in.readByte() != 0;
        mChampionName = in.readString();
        mChampionImage = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mChampionName);
        dest.writeString(mChampionImage);
        dest.writeLong(mChampionId);
        dest.writeInt(mChampionLevel);
        dest.writeInt(mChampionPoints);
        dest.writeLong(mLastPlayTime);
        dest.writeByte((byte) (mIsChestGranted ? 1 : 0));
    }

    public Mastery(long playerId, long championId, int championLevel, int championPoints, long lastPlayTime, boolean chestGranted) {
        mPlayerId = playerId;
        mChampionId = championId;
        mChampionLevel = championLevel;
        mChampionPoints = championPoints;
        mLastPlayTime = lastPlayTime;
        mIsChestGranted = chestGranted;
    }

    public Mastery(String name, String image, long championId, int championLevel, int championPoints, long lastPlayTime, boolean chestGranted) {
        mChampionName = name;
        mChampionImage = image;
        mChampionId = championId;
        mChampionLevel = championLevel;
        mChampionPoints = championPoints;
        mLastPlayTime = lastPlayTime;
        mIsChestGranted = chestGranted;
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
