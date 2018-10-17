package com.example.android.leaguestats.data.network.api.models.match;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MatchResponse {

    @SerializedName("gameId")
    @Expose
    private long gameId;
    @SerializedName("platformId")
    @Expose
    private String platformId;
    @SerializedName("gameCreation")
    @Expose
    private long gameCreation;
    @SerializedName("gameDuration")
    @Expose
    private long gameDuration;
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
    private List<Participant> participants;
    @SerializedName("participantIdentities")
    @Expose
    private List<ParticipantIdentity> participantIdentities;

    /**
     * No args constructor for use in serialization
     *
     */
    public MatchResponse() {
    }

    /**
     *
     * @param gameMode
     * @param teams
     * @param mapId
     * @param gameVersion
     * @param gameType
     * @param queueId
     * @param gameId
     * @param seasonId
     * @param gameDuration
     * @param gameCreation
     * @param participants
     * @param platformId
     * @param participantIdentities
     */
    public MatchResponse(long gameId, String platformId, long gameCreation, long gameDuration, int queueId, int mapId, int seasonId, String gameVersion, String gameMode, String gameType, List<Team> teams, List<Participant> participants, List<ParticipantIdentity> participantIdentities) {
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

    public long getGameId() {
        return gameId;
    }

    public void setGameId(long gameId) {
        this.gameId = gameId;
    }

    public String getPlatformId() {
        return platformId;
    }

    public void setPlatformId(String platformId) {
        this.platformId = platformId;
    }

    public long getGameCreation() {
        return gameCreation;
    }

    public void setGameCreation(long gameCreation) {
        this.gameCreation = gameCreation;
    }

    public long getGameDuration() {
        return gameDuration;
    }

    public void setGameDuration(long gameDuration) {
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
