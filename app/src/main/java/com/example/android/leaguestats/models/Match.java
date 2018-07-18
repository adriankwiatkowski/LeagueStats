package com.example.android.leaguestats.models;

public class Match {

    private int mParticipantId;
    private String mSummonerName;
    private int mTeamId;
    private int mChampionId;
    private int mSpell1Id;
    private int mSpell2Id;
    private long mGameDuration;
    private long mGameCreation;

    public Match(int participantId, String summonerName, int teamId, int championId,
                 int spell1, int spell2, long gameDuration, long gameCreation) {
        this.mParticipantId = participantId;
        this.mSummonerName = summonerName;
        this.mTeamId = teamId;
        this.mChampionId = championId;
        this.mSpell1Id = spell1;
        this.mSpell2Id = spell2;
        this.mGameDuration = gameDuration;
        this.mGameCreation = gameCreation;
    }

    public int getParticipantId() {
        return mParticipantId;
    }

    public void setParticipantId(int participantId) {
        this.mParticipantId = participantId;
    }

    public String getSummonerName() {
        return mSummonerName;
    }

    public void setSummonerName(String summonerName) {
        this.mSummonerName = summonerName;
    }

    public int getTeamId() {
        return mTeamId;
    }

    public void setTeamId(int teamId) {
        this.mTeamId = teamId;
    }

    public int getChampionId() {
        return mChampionId;
    }

    public void setChampionId(int championId) {
        this.mChampionId = championId;
    }

    public long getGameDuration() {
        return mGameDuration;
    }

    public void setGameDuration(long gameDuration) {
        this.mGameDuration = gameDuration;
    }

    public long getGameCreation() {
        return mGameCreation;
    }

    public void setGameCreation(long gameCreation) {
        this.mGameCreation = gameCreation;
    }

    public int getSpell2Id() {
        return mSpell2Id;
    }

    public void setSpell2Id(int spell2Id) {
        this.mSpell2Id = spell2Id;
    }

    public int getSpell1Id() {
        return mSpell1Id;
    }

    public void setSpell1Id(int spell1Id) {
        this.mSpell1Id = spell1Id;
    }
}
