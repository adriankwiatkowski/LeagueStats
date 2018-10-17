package com.example.android.leaguestats.data.network.api.models.match;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Player {

    @SerializedName("platformId")
    @Expose
    private String platformId;
    @SerializedName("accountId")
    @Expose
    private long accountId;
    @SerializedName("summonerName")
    @Expose
    private String summonerName;
    @SerializedName("summonerId")
    @Expose
    private long summonerId;
    @SerializedName("currentPlatformId")
    @Expose
    private String currentPlatformId;
    @SerializedName("currentAccountId")
    @Expose
    private long currentAccountId;
    @SerializedName("matchHistoryUri")
    @Expose
    private String matchHistoryUri;
    @SerializedName("profileIcon")
    @Expose
    private int profileIcon;

    /**
     * No args constructor for use in serialization
     *
     */
    public Player() {
    }

    /**
     *
     * @param accountId
     * @param profileIcon
     * @param matchHistoryUri
     * @param currentPlatformId
     * @param summonerName
     * @param currentAccountId
     * @param platformId
     * @param summonerId
     */
    public Player(String platformId, long accountId, String summonerName, int summonerId, String currentPlatformId, long currentAccountId, String matchHistoryUri, int profileIcon) {
        super();
        this.platformId = platformId;
        this.accountId = accountId;
        this.summonerName = summonerName;
        this.summonerId = summonerId;
        this.currentPlatformId = currentPlatformId;
        this.currentAccountId = currentAccountId;
        this.matchHistoryUri = matchHistoryUri;
        this.profileIcon = profileIcon;
    }

    public String getPlatformId() {
        return platformId;
    }

    public void setPlatformId(String platformId) {
        this.platformId = platformId;
    }

    public long getAccountId() {
        return accountId;
    }

    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }

    public String getSummonerName() {
        return summonerName;
    }

    public void setSummonerName(String summonerName) {
        this.summonerName = summonerName;
    }

    public long getSummonerId() {
        return summonerId;
    }

    public void setSummonerId(long summonerId) {
        this.summonerId = summonerId;
    }

    public String getCurrentPlatformId() {
        return currentPlatformId;
    }

    public void setCurrentPlatformId(String currentPlatformId) {
        this.currentPlatformId = currentPlatformId;
    }

    public long getCurrentAccountId() {
        return currentAccountId;
    }

    public void setCurrentAccountId(long currentAccountId) {
        this.currentAccountId = currentAccountId;
    }

    public String getMatchHistoryUri() {
        return matchHistoryUri;
    }

    public void setMatchHistoryUri(String matchHistoryUri) {
        this.matchHistoryUri = matchHistoryUri;
    }

    public int getProfileIcon() {
        return profileIcon;
    }

    public void setProfileIcon(int profileIcon) {
        this.profileIcon = profileIcon;
    }

}
