package com.example.android.leaguestats.data.network.api.models.match;

import android.support.annotation.Nullable;

import com.example.android.leaguestats.models.Champion;
import com.example.android.leaguestats.models.SummonerSpell;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

// TODO: Remove it later.
public class Participant {

    @SerializedName("participantId")
    @Expose
    private int participantId;
    @SerializedName("teamId")
    @Expose
    private int teamId;
    @SerializedName("championId")
    @Expose
    private int championId;
    @Nullable
    private Champion champion;
    @SerializedName("spell1Id")
    @Expose
    private int spell1Id;
    @Nullable
    private SummonerSpell summonerSpell1;
    @SerializedName("spell2Id")
    @Expose
    private int spell2Id;
    @Nullable
    private SummonerSpell summonerSpell2;
    @SerializedName("highestAchievedSeasonTier")
    @Expose
    private String highestAchievedSeasonTier;
    @SerializedName("stats")
    @Expose
    private Stats stats;
    @SerializedName("timeline")
    @Expose
    private Timeline timeline;

    /**
     * No args constructor for use in serialization
     *
     */
    public Participant() {
    }

    /**
     *
     * @param stats
     * @param timeline
     * @param spell2Id
     * @param participantId
     * @param championId
     * @param teamId
     * @param highestAchievedSeasonTier
     * @param spell1Id
     */
    public Participant(int participantId, int teamId, int championId, int spell1Id, int spell2Id, String highestAchievedSeasonTier, Stats stats, Timeline timeline) {
        super();
        this.participantId = participantId;
        this.teamId = teamId;
        this.championId = championId;
        this.spell1Id = spell1Id;
        this.spell2Id = spell2Id;
        this.highestAchievedSeasonTier = highestAchievedSeasonTier;
        this.stats = stats;
        this.timeline = timeline;
    }

    public int getParticipantId() {
        return participantId;
    }

    public void setParticipantId(int participantId) {
        this.participantId = participantId;
    }

    public int getTeamId() {
        return teamId;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }

    public int getChampionId() {
        return championId;
    }

    public void setChampionId(int championId) {
        this.championId = championId;
    }

    public int getSpell1Id() {
        return spell1Id;
    }

    public void setSpell1Id(int spell1Id) {
        this.spell1Id = spell1Id;
    }

    public int getSpell2Id() {
        return spell2Id;
    }

    public void setSpell2Id(int spell2Id) {
        this.spell2Id = spell2Id;
    }

    public String getHighestAchievedSeasonTier() {
        return highestAchievedSeasonTier;
    }

    public void setHighestAchievedSeasonTier(String highestAchievedSeasonTier) {
        this.highestAchievedSeasonTier = highestAchievedSeasonTier;
    }

    public Stats getStats() {
        return stats;
    }

    public void setStats(Stats stats) {
        this.stats = stats;
    }

    public Timeline getTimeline() {
        return timeline;
    }

    public void setTimeline(Timeline timeline) {
        this.timeline = timeline;
    }

    @Nullable
    public SummonerSpell getSummonerSpell1() {
        return summonerSpell1;
    }

    public void setSummonerSpell1(@Nullable SummonerSpell summonerSpell1) {
        this.summonerSpell1 = summonerSpell1;
    }

    @Nullable
    public SummonerSpell getSummonerSpell2() {
        return summonerSpell2;
    }

    public void setSummonerSpell2(@Nullable SummonerSpell summonerSpell2) {
        this.summonerSpell2 = summonerSpell2;
    }

    @Nullable
    public Champion getChampion() {
        return champion;
    }

    public void setChampion(@Nullable Champion champion) {
        this.champion = champion;
    }
}
