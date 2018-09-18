package com.example.android.leaguestats.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Summoner implements Parcelable {

    private int mProfileIconId;
    private String mSummonerName;
    private long mSummonerLevel;
    private long mSummonerId;
    private long mAccountId;
    private long mSummonerRevisionDate;

    public Summoner(int profileIconId, String summonerName, long summonerLevel, long accountId, long summonerId, long summonerRevisionDate) {
        this.mProfileIconId = profileIconId;
        this.mSummonerName = summonerName;
        this.mSummonerLevel = summonerLevel;
        this.mAccountId = accountId;
        this.mSummonerId = summonerId;
        this.mSummonerRevisionDate = summonerRevisionDate;
    }

    protected Summoner(Parcel in) {
        mProfileIconId = in.readInt();
        mSummonerName = in.readString();
        mSummonerLevel = in.readLong();
        mSummonerId = in.readLong();
        mAccountId = in.readLong();
        mSummonerRevisionDate = in.readLong();
    }

    public static final Creator<Summoner> CREATOR = new Creator<Summoner>() {
        @Override
        public Summoner createFromParcel(Parcel in) {
            return new Summoner(in);
        }

        @Override
        public Summoner[] newArray(int size) {
            return new Summoner[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mProfileIconId);
        dest.writeString(mSummonerName);
        dest.writeLong(mSummonerLevel);
        dest.writeLong(mSummonerId);
        dest.writeLong(mAccountId);
        dest.writeLong(mSummonerRevisionDate);
    }

    public int getProfileIconId() {
        return mProfileIconId;
    }

    public void setProfileIconId(int profileIconId) {
        this.mProfileIconId = profileIconId;
    }

    public String getSummonerName() {
        return mSummonerName;
    }

    public void setSummonerName(String summonerName) {
        this.mSummonerName = summonerName;
    }

    public long getSummonerLevel() {
        return mSummonerLevel;
    }

    public void setSummonerLevel(long summonerLevel) {
        this.mSummonerLevel = summonerLevel;
    }

    public long getSummonerRevisionDate() {
        return mSummonerRevisionDate;
    }

    public void setSummonerRevisionDate(long summonerRevisionDate) {
        this.mSummonerRevisionDate = summonerRevisionDate;
    }

    public long getSummonerId() {
        return mSummonerId;
    }

    public void setSummonerId(long summonerId) {
        this.mSummonerId = summonerId;
    }

    public long getAccountId() {
        return mAccountId;
    }

    public void setAccountId(long accountId) {
        this.mAccountId = accountId;
    }
}
