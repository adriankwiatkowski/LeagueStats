package com.example.android.leaguestats.models;

import com.example.android.leaguestats.data.network.api.models.match.MatchResponse;
import com.example.android.leaguestats.data.network.api.models.match.Participant;
import com.example.android.leaguestats.data.network.api.models.match.ParticipantIdentity;
import com.example.android.leaguestats.data.database.models.ListChampionEntry;
import com.example.android.leaguestats.data.database.models.ListItemEntry;
import com.example.android.leaguestats.data.database.models.ListSummonerSpellEntry;

import java.util.ArrayList;
import java.util.List;

public class Match {

    private List<Integer> mParticipantId;
    private List<Long> mAccountId;
    private List<Long> mSummonerId;
    private List<String> mSummonerName;
    private List<Integer> mTeamId;
    private List<Boolean> mWin;
    private List<Integer> mKills;
    private List<Integer> mDeaths;
    private List<Integer> mAssists;
    private List<Long> mTotalDamageToChampions;
    private List<Integer> mTotalMinionsKilled;
    private List<Integer> mGoldEarned;

    private List<ListChampionEntry> mChampionEntries;
    private List<ListSummonerSpellEntry> mSpellEntries1;
    private List<ListSummonerSpellEntry> mSpellEntries2;
    private List<ListItemEntry> mItemEntries;

    private long mGameDuration;
    private long mGameCreation;
    private long mCurrentSummonerId;
    private String mPlatformId;
    private String mHighestAchievedSeasonTier;

    public Match(List<Integer> mParticipantId, List<Long> mAccountId, List<Long> mSummonerId,
                 List<String> mSummonerName, List<Integer> mTeamId, long mGameDuration,
                 long mGameCreation, List<ListChampionEntry> mChampionEntries,
                 List<ListSummonerSpellEntry> mSpellEntries1, List<ListSummonerSpellEntry> mSpellEntries2,
                 List<Boolean> win, List<ListItemEntry> itemList, List<Integer> killList,
                 List<Integer> deathList, List<Integer> assistList,
                 List<Long> totalDamageToChampions, long currentSummonerId) {
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
        this.mItemEntries = itemList;
        this.mKills = killList;
        this.mDeaths = deathList;
        this.mAssists = assistList;
        this.mTotalDamageToChampions = totalDamageToChampions;
        this.mCurrentSummonerId = currentSummonerId;
    }

    public Match(MatchResponse matchResponse,
                 List<ListChampionEntry> championEntries,
                 List<ListSummonerSpellEntry> summonerSpell1Entries,
                 List<ListSummonerSpellEntry> summonerSpell2Entries,
                 List<ListItemEntry> itemEntries,
                 long currentSummonerId) {

        List<Integer> participantId = new ArrayList<>();
        List<Integer> teamId = new ArrayList<>();
        List<Boolean> isWin = new ArrayList<>();
        List<Integer> kills = new ArrayList<>();
        List<Integer> deaths = new ArrayList<>();
        List<Integer> assists = new ArrayList<>();
        List<Long> totalDamageToChampions = new ArrayList<>();
        List<Integer> totalMinionsKilled = new ArrayList<>();
        List<Integer> totalGoldEarned = new ArrayList<>();

        List<Participant> participantList = matchResponse.getParticipants();
        for (Participant participant : participantList) {
            participantId.add(participant.getParticipantId());
            teamId.add(participant.getTeamId());
            isWin.add(participant.getStats().isWin());
            kills.add(participant.getStats().getKills());
            deaths.add(participant.getStats().getDeaths());
            assists.add(participant.getStats().getAssists());
            totalDamageToChampions.add(participant.getStats().getTotalDamageDealtToChampions());
            totalMinionsKilled.add(participant.getStats().getTotalMinionsKilled());
            totalGoldEarned.add(participant.getStats().getGoldEarned());
        }

        List<Long> accountId = new ArrayList<>();
        List<Long> summonerId = new ArrayList<>();
        List<String> summonerName = new ArrayList<>();

        List<ParticipantIdentity> participantIdentities = matchResponse.getParticipantIdentities();
        for (ParticipantIdentity participantIdentity : participantIdentities) {
            accountId.add(participantIdentity.getPlayer().getAccountId());
            summonerId.add(participantIdentity.getPlayer().getSummonerId());
            summonerName.add(participantIdentity.getPlayer().getSummonerName());
        }

        mParticipantId = participantId;
        mTeamId = teamId;
        mAccountId = accountId;
        mSummonerId = summonerId;
        mSummonerName = summonerName;
        mWin = isWin;
        mKills = kills;
        mDeaths = deaths;
        mAssists = assists;
        mTotalDamageToChampions = totalDamageToChampions;
        mTotalMinionsKilled = totalMinionsKilled;
        mGoldEarned = totalGoldEarned;
        mChampionEntries = championEntries;
        mSpellEntries1 = summonerSpell1Entries;
        mSpellEntries2 = summonerSpell2Entries;
        mItemEntries = itemEntries;
        mGameDuration = matchResponse.getGameDuration();
        mGameCreation = matchResponse.getGameCreation();
        mCurrentSummonerId = currentSummonerId;
        mPlatformId = matchResponse.getPlatformId();

        for (int i = 0; i < matchResponse.getParticipantIdentities().size(); i++) {
            if (currentSummonerId == matchResponse.getParticipantIdentities().get(i).getPlayer().getSummonerId()) {
                mHighestAchievedSeasonTier = matchResponse.getParticipants().get(i).getHighestAchievedSeasonTier();
            }
        }
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

    public List<ListItemEntry> getItemEntries() {
        return mItemEntries;
    }

    public void setItemEntries(List<ListItemEntry> mItemEntries) {
        this.mItemEntries = mItemEntries;
    }

    public long getCurrentSummonerId() {
        return mCurrentSummonerId;
    }

    public void setCurrentSummonerId(long mCurrentSummonerId) {
        this.mCurrentSummonerId = mCurrentSummonerId;
    }

    public String getPlatformId() {
        return mPlatformId;
    }

    public void setPlatformId(String platformId) {
        this.mPlatformId = platformId;
    }

    public String getHighestAchievedSeasonTier() {
        return mHighestAchievedSeasonTier;
    }

    public void setHighestAchievedSeasonTier(String highestAchievedSeasonTier) {
        this.mHighestAchievedSeasonTier = highestAchievedSeasonTier;
    }

    public List<Integer> getTotalMinionsKilled() {
        return mTotalMinionsKilled;
    }

    public void setTotalMinionsKilled(List<Integer> totalMinionsKilled) {
        this.mTotalMinionsKilled = totalMinionsKilled;
    }

    public List<Integer> getGoldEarned() {
        return mGoldEarned;
    }

    public void setGoldEarned(List<Integer> totalGoldEarned) {
        this.mGoldEarned = totalGoldEarned;
    }
}
