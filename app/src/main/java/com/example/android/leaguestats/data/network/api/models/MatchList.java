package com.example.android.leaguestats.data.network.api.models;

import java.util.List;

import com.example.android.leaguestats.data.network.api.models.match.Participant;
import com.example.android.leaguestats.data.network.api.models.match.ParticipantIdentity;
import com.example.android.leaguestats.data.network.api.models.match.Team;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

// TODO: Remove it later.
public class MatchList {

    @SerializedName("gameId")
    @Expose
    private int gameId;
    @SerializedName("platformId")
    @Expose
    private String platformId;
    @SerializedName("gameCreation")
    @Expose
    private int gameCreation;
    @SerializedName("gameDuration")
    @Expose
    private int gameDuration;
    @SerializedName("queueId")
    @Expose
    private int queueId;
    @SerializedName("mapId")
    @Expose
    private int mapId;
    @SerializedName("seasonId")
    @Expose
    private int seasonId;
    @SerializedName("gameVersion")
    @Expose
    private String gameVersion;
    @SerializedName("gameMode")
    @Expose
    private String gameMode;
    @SerializedName("gameType")
    @Expose
    private String gameType;
    @SerializedName("teams")
    @Expose
    private List<Team> teams = null;
    @SerializedName("participants")
    @Expose
    private List<Participant> participants = null;
    @SerializedName("participantIdentities")
    @Expose
    private List<ParticipantIdentity> participantIdentities = null;

    /**
     * No args constructor for use in serialization
     *
     */
    public MatchList() {
    }

    /**
     *
     * @param gameId
     * @param participantIdentities
     * @param queueId
     * @param gameType
     * @param gameDuration
     * @param teams
     * @param platformId
     * @param gameCreation
     * @param seasonId
     * @param gameVersion
     * @param mapId
     * @param gameMode
     * @param participants
     */
    public MatchList(int gameId, String platformId, int gameCreation, int gameDuration, int queueId, int mapId, int seasonId, String gameVersion, String gameMode, String gameType, List<Team> teams, List<Participant> participants, List<ParticipantIdentity> participantIdentities) {
        super();
        this.gameId = gameId;
        this.platformId = platformId;
        this.gameCreation = gameCreation;
        this.gameDuration = gameDuration;
        this.queueId = queueId;
        this.mapId = mapId;
        this.seasonId = seasonId;
        this.gameVersion = gameVersion;
        this.gameMode = gameMode;
        this.gameType = gameType;
        this.teams = teams;
        this.participants = participants;
        this.participantIdentities = participantIdentities;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public String getPlatformId() {
        return platformId;
    }

    public void setPlatformId(String platformId) {
        this.platformId = platformId;
    }

    public int getGameCreation() {
        return gameCreation;
    }

    public void setGameCreation(int gameCreation) {
        this.gameCreation = gameCreation;
    }

    public int getGameDuration() {
        return gameDuration;
    }

    public void setGameDuration(int gameDuration) {
        this.gameDuration = gameDuration;
    }

    public int getQueueId() {
        return queueId;
    }

    public void setQueueId(int queueId) {
        this.queueId = queueId;
    }

    public int getMapId() {
        return mapId;
    }

    public void setMapId(int mapId) {
        this.mapId = mapId;
    }

    public int getSeasonId() {
        return seasonId;
    }

    public void setSeasonId(int seasonId) {
        this.seasonId = seasonId;
    }

    public String getGameVersion() {
        return gameVersion;
    }

    public void setGameVersion(String gameVersion) {
        this.gameVersion = gameVersion;
    }

    public String getGameMode() {
        return gameMode;
    }

    public void setGameMode(String gameMode) {
        this.gameMode = gameMode;
    }

    public String getGameType() {
        return gameType;
    }

    public void setGameType(String gameType) {
        this.gameType = gameType;
    }

    public List<Team> getTeams() {
        return teams;
    }

    public void setTeams(List<Team> teams) {
        this.teams = teams;
    }

    public List<Participant> getParticipants() {
        return participants;
    }

    public void setParticipants(List<Participant> participants) {
        this.participants = participants;
    }

    public List<ParticipantIdentity> getParticipantIdentities() {
        return participantIdentities;
    }

    public void setParticipantIdentities(List<ParticipantIdentity> participantIdentities) {
        this.participantIdentities = participantIdentities;
    }

}