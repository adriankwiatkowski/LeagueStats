package com.example.android.leaguestats.models;

import java.util.List;

public class Match {

    private List<Integer> mParticipantId;
    private List<Long> mAccountId;
    private List<Long> mSummonerId;
    private List<String> mSummonerName;
    private List<Integer> mTeamId;
    private List<Integer> mChampionId;
    private List<Integer> mSpell1Id;
    private List<Integer> mSpell2Id;
    private long mGameDuration;
    private long mGameCreation;

    public Match(List<Integer> participantId, List<Long> accountId, List<Long> summonerId,
                 List<String> summonerName, List<Integer> teamId, List<Integer> championId,
                 List<Integer> spell1, List<Integer> spell2, long gameDuration, long gameCreation) {
        this.mParticipantId = participantId;
        this.mAccountId = accountId;
        this.mSummonerId = summonerId;
        this.mSummonerName = summonerName;
        this.mTeamId = teamId;
        this.mChampionId = championId;
        this.mSpell1Id = spell1;
        this.mSpell2Id = spell2;
        this.mGameDuration = gameDuration;
        this.mGameCreation = gameCreation;
    }

    public List<Integer> getParticipantId() {
        return mParticipantId;
    }

    public void setParticipantId(List<Integer> participantId) {
        this.mParticipantId = participantId;
    }

    public List<String> getSummonerName() {
        return mSummonerName;
    }

    public void setSummonerName(List<String> summonerName) {
        this.mSummonerName = summonerName;
    }

    public List<Integer> getTeamId() {
        return mTeamId;
    }

    public void setTeamId(List<Integer> teamId) {
        this.mTeamId = teamId;
    }

    public List<Integer> getChampionId() {
        return mChampionId;
    }

    public void setChampionId(List<Integer> championId) {
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

    public List<Integer> getSpell2Id() {
        return mSpell2Id;
    }

    public void setSpell2Id(List<Integer> spell2Id) {
        this.mSpell2Id = spell2Id;
    }

    public List<Integer> getSpell1Id() {
        return mSpell1Id;
    }

    public void setSpell1Id(List<Integer> spell1Id) {
        this.mSpell1Id = spell1Id;
    }

    public List<Long> getAccountId() {
        return mAccountId;
    }

    public void setAccountId(List<Long> accountId) {
        this.mAccountId = accountId;
    }

    public List<Long> getSummonerId() {
        return mSummonerId;
    }

    public void setSummonerId(List<Long> summonerId) {
        this.mSummonerId = summonerId;
    }
}
