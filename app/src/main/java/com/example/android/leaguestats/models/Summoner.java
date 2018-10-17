package com.example.android.leaguestats.models;

import com.google.gson.annotations.SerializedName;

public class Summoner {

    private String mEntryUrl;

    @SerializedName("profileIconId")
    private int mProfileIconId;

    @SerializedName("name")
    private String mSummonerName;

    @SerializedName("summonerLevel")
    private long mSummonerLevel;

    @SerializedName("id")
    private long mSummonerId;

    @SerializedName("accountId")
    private long mAccountId;

    @SerializedName("revisionDate")
    private long mSummonerRevisionDate;

    public Summoner(String entryUrl, int profileIconId, String summonerName, long summonerLevel,
                    long accountId, long summonerId, long summonerRevisionDate) {
        this.mEntryUrl = entryUrl;
        this.mProfileIconId = profileIconId;
        this.mSummonerName = summonerName;
        this.mSummonerLevel = summonerLevel;
        this.mAccountId = accountId;
        this.mSummonerId = summonerId;
        this.mSummonerRevisionDate = summonerRevisionDate;
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

    public String getEntryUrl() {
        return mEntryUrl;
    }

    public void setEntryUrl(String entryUrl) {
        this.mEntryUrl = entryUrl;
    }
}
