package com.example.android.leaguestats.data.network.api.models.match;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Team {

    @SerializedName("teamId")
    @Expose
    private int teamId;
    @SerializedName("win")
    @Expose
    private String win;
    @SerializedName("firstBlood")
    @Expose
    private boolean firstBlood;
    @SerializedName("firstTower")
    @Expose
    private boolean firstTower;
    @SerializedName("firstInhibitor")
    @Expose
    private boolean firstInhibitor;
    @SerializedName("firstBaron")
    @Expose
    private boolean firstBaron;
    @SerializedName("firstDragon")
    @Expose
    private boolean firstDragon;
    @SerializedName("firstRiftHerald")
    @Expose
    private boolean firstRiftHerald;
    @SerializedName("towerKills")
    @Expose
    private int towerKills;
    @SerializedName("inhibitorKills")
    @Expose
    private int inhibitorKills;
    @SerializedName("baronKills")
    @Expose
    private int baronKills;
    @SerializedName("dragonKills")
    @Expose
    private int dragonKills;
    @SerializedName("vilemawKills")
    @Expose
    private int vilemawKills;
    @SerializedName("riftHeraldKills")
    @Expose
    private int riftHeraldKills;
    @SerializedName("dominionVictoryScore")
    @Expose
    private int dominionVictoryScore;
    @SerializedName("bans")
    @Expose
    private List<Ban> bans = null;

    /**
     * No args constructor for use in serialization
     *
     */
    public Team() {
    }

    /**
     *
     * @param bans
     * @param firstBlood
     * @param firstTower
     * @param firstInhibitor
     * @param firstDragon
     * @param vilemawKills
     * @param win
     * @param baronKills
     * @param teamId
     * @param inhibitorKills
     * @param dominionVictoryScore
     * @param riftHeraldKills
     * @param firstRiftHerald
     * @param towerKills
     * @param firstBaron
     * @param dragonKills
     */
    public Team(int teamId, String win, boolean firstBlood, boolean firstTower, boolean firstInhibitor, boolean firstBaron, boolean firstDragon, boolean firstRiftHerald, int towerKills, int inhibitorKills, int baronKills, int dragonKills, int vilemawKills, int riftHeraldKills, int dominionVictoryScore, List<Ban> bans) {
        super();
        this.teamId = teamId;
        this.win = win;
        this.firstBlood = firstBlood;
        this.firstTower = firstTower;
        this.firstInhibitor = firstInhibitor;
        this.firstBaron = firstBaron;
        this.firstDragon = firstDragon;
        this.firstRiftHerald = firstRiftHerald;
        this.towerKills = towerKills;
        this.inhibitorKills = inhibitorKills;
        this.baronKills = baronKills;
        this.dragonKills = dragonKills;
        this.vilemawKills = vilemawKills;
        this.riftHeraldKills = riftHeraldKills;
        this.dominionVictoryScore = dominionVictoryScore;
        this.bans = bans;
    }

    public int getTeamId() {
        return teamId;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }

    public String getWin() {
        return win;
    }

    public void setWin(String win) {
        this.win = win;
    }

    public boolean isFirstBlood() {
        return firstBlood;
    }

    public void setFirstBlood(boolean firstBlood) {
        this.firstBlood = firstBlood;
    }

    public boolean isFirstTower() {
        return firstTower;
    }

    public void setFirstTower(boolean firstTower) {
        this.firstTower = firstTower;
    }

    public boolean isFirstInhibitor() {
        return firstInhibitor;
    }

    public void setFirstInhibitor(boolean firstInhibitor) {
        this.firstInhibitor = firstInhibitor;
    }

    public boolean isFirstBaron() {
        return firstBaron;
    }

    public void setFirstBaron(boolean firstBaron) {
        this.firstBaron = firstBaron;
    }

    public boolean isFirstDragon() {
        return firstDragon;
    }

    public void setFirstDragon(boolean firstDragon) {
        this.firstDragon = firstDragon;
    }

    public boolean isFirstRiftHerald() {
        return firstRiftHerald;
    }

    public void setFirstRiftHerald(boolean firstRiftHerald) {
        this.firstRiftHerald = firstRiftHerald;
    }

    public int getTowerKills() {
        return towerKills;
    }

    public void setTowerKills(int towerKills) {
        this.towerKills = towerKills;
    }

    public int getInhibitorKills() {
        return inhibitorKills;
    }

    public void setInhibitorKills(int inhibitorKills) {
        this.inhibitorKills = inhibitorKills;
    }

    public int getBaronKills() {
        return baronKills;
    }

    public void setBaronKills(int baronKills) {
        this.baronKills = baronKills;
    }

    public int getDragonKills() {
        return dragonKills;
    }

    public void setDragonKills(int dragonKills) {
        this.dragonKills = dragonKills;
    }

    public int getVilemawKills() {
        return vilemawKills;
    }

    public void setVilemawKills(int vilemawKills) {
        this.vilemawKills = vilemawKills;
    }

    public int getRiftHeraldKills() {
        return riftHeraldKills;
    }

    public void setRiftHeraldKills(int riftHeraldKills) {
        this.riftHeraldKills = riftHeraldKills;
    }

    public int getDominionVictoryScore() {
        return dominionVictoryScore;
    }

    public void setDominionVictoryScore(int dominionVictoryScore) {
        this.dominionVictoryScore = dominionVictoryScore;
    }

    public List<Ban> getBans() {
        return bans;
    }

    public void setBans(List<Ban> bans) {
        this.bans = bans;
    }

}
