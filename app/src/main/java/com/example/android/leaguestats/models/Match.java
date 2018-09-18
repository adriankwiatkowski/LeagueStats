package com.example.android.leaguestats.models;

import com.example.android.leaguestats.database.models.ListChampionEntry;
import com.example.android.leaguestats.database.models.ListSummonerSpellEntry;

import java.util.List;

public class Match {

    private List<Integer> mParticipantId;
    private List<Long> mAccountId;
    private List<Long> mSummonerId;
    private List<String> mSummonerName;
    private List<Integer> mTeamId;
    private List<String> mChampionId;
    private List<Integer> mSpell1Id;
    private List<Integer> mSpell2Id;
    private long mGameDuration;
    private long mGameCreation;
    private List<Boolean> mWin;
    private List<Integer> mItems;
    private List<Integer> mKills;
    private List<Integer> mDeaths;
    private List<Integer> mAssists;
    private List<Long> mTotalDamageToChampions;

    private List<ListChampionEntry> mChampionEntries;
    private List<ListSummonerSpellEntry> mSpellEntries1;
    private List<ListSummonerSpellEntry> mSpellEntries2;

    public Match(List<Integer> participantId, List<Long> accountId, List<Long> summonerId,
                 List<String> summonerName, List<Integer> teamId, List<String> championId,
                 List<Integer> spell1, List<Integer> spell2, long gameDuration,
                 long gameCreation, List<Boolean> win, List<Integer> itemList,
                 List<Integer> killList, List<Integer> deathList, List<Integer> assistList,
                 List<Long> totalDamageToChampions) {
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
        this.mWin = win;
        this.mItems = itemList;
        this.mKills = killList;
        this.mDeaths = deathList;
        this.mAssists = assistList;
        this.mTotalDamageToChampions = totalDamageToChampions;
    }

    public Match(List<Integer> mParticipantId, List<Long> mAccountId, List<Long> mSummonerId,
                 List<String> mSummonerName, List<Integer> mTeamId, long mGameDuration,
                 long mGameCreation, List<ListChampionEntry> mChampionEntries,
                 List<ListSummonerSpellEntry> mSpellEntries1, List<ListSummonerSpellEntry> mSpellEntries2,
                 List<Boolean> win, List<Integer> itemList, List<Integer> killList,
                 List<Integer> deathList, List<Integer> assistList, List<Long> totalDamageToChampions) {
        this.mParticipantId = mParticipantId;
        this.mAccountId = mAccountId;
        this.mSummonerId = mSummonerId;
        this.mSummonerName = mSummonerName;
        this.mTeamId = mTeamId;
        this.mGameDuration = mGameDuration;
        this.mGameCreation = mGameCreation;
        this.mChampionEntries = mChampionEntries;
        this.mSpellEntries1 = mSpellEntries1;
        this.mSpellEntries2 = mSpellEntries2;
        this.mWin = win;
        this.mItems = itemList;
        this.mKills = killList;
        this.mDeaths = deathList;
        this.mAssists = assistList;
        this.mTotalDamageToChampions = totalDamageToChampions;
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

    public List<String> getChampionId() {
        return mChampionId;
    }

    public void setChampionId(List<String> championId) {
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

    public List<ListSummonerSpellEntry> getSpellEntries2() {
        return mSpellEntries2;
    }

    public void setSpellEntries2(List<ListSummonerSpellEntry> spellEntries2) {
        this.mSpellEntries2 = spellEntries2;
    }

    public List<ListSummonerSpellEntry> getSpellEntries1() {
        return mSpellEntries1;
    }

    public void setSpellEntries1(List<ListSummonerSpellEntry> spellEntries1) {
        this.mSpellEntries1 = spellEntries1;
    }

    public List<ListChampionEntry> getChampionEntries() {
        return mChampionEntries;
    }

    public void setChampionEntries(List<ListChampionEntry> championEntries) {
        this.mChampionEntries = championEntries;
    }

    public List<Boolean> isWin() {
        return mWin;
    }

    public void setWin(List<Boolean> win) {
        this.mWin = win;
    }

    public List<Long> getTotalDamageToChampions() {
        return mTotalDamageToChampions;
    }

    public void setTotalDamageToChampions(List<Long> totalDamageToChampions) {
        this.mTotalDamageToChampions = totalDamageToChampions;
    }

    public List<Integer> getAssists() {
        return mAssists;
    }

    public void setAssists(List<Integer> assists) {
        this.mAssists = assists;
    }

    public List<Integer> getDeaths() {
        return mDeaths;
    }

    public void setDeaths(List<Integer> deaths) {
        this.mDeaths = deaths;
    }

    public List<Integer> getKills() {
        return mKills;
    }

    public void setKills(List<Integer> kills) {
        this.mKills = kills;
    }

    public List<Integer> getItems() {
        return mItems;
    }

    public void setItems(List<Integer> items) {
        this.mItems = items;
    }
}
