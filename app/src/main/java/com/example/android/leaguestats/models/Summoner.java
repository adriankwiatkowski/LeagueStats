package com.example.android.leaguestats.models;

import com.google.gson.annotations.SerializedName;

public class Summoner {

    private String mEntryUrl;

    @SerializedName("profileIconId")
    private int mIconId;

    @SerializedName("name")
    private String mSummonerName;

    @SerializedName("summonerLevel")
    private long mSummonerLevel;

    @SerializedName("id")
    private String mSummonerId;

    @SerializedName("accountId")
    private String mAccountId;

    @SerializedName("revisionDate")
    private long mSummonerRevisionDate;

    public Summoner(String entryUrl, int profileIconId, String summonerName, long summonerLevel,
                    String accountId, String summonerId, long summonerRevisionDate) {
        this.mEntryUrl = entryUrl;
        this.mIconId = profileIconId;
        this.mSummonerName = summonerName;
        this.mSummonerLevel = summonerLevel;
        this.mAccountId = accountId;
        this.mSummonerId = summonerId;
        this.mSummonerRevisionDate = summonerRevisionDate;
    }

    public int getIconId() {
        return mIconId;
    }

    public void setIconId(int profileIconId) {
        this.mIconId = profileIconId;
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

    public String getSummonerId() {
        return mSummonerId;
    }

    public void setSummonerId(String summonerId) {
        this.mSummonerId = summonerId;
    }

    public String getAccountId() {
        return mAccountId;
    }

    public void setAccountId(String accountId) {
        this.mAccountId = accountId;
    }

    public String getEntryUrl() {
        return mEntryUrl;
    }

    public void setEntryUrl(String entryUrl) {
        this.mEntryUrl = entryUrl;
    }
}
