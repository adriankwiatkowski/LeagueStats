package com.example.android.leaguestats.data;

import com.example.android.leaguestats.data.database.entity.ChampionEntry;
import com.example.android.leaguestats.data.database.entity.ItemEntry;
import com.example.android.leaguestats.data.database.entity.SummonerSpellEntry;

import java.util.List;

class MatchMergedData {

    private List<ChampionEntry> mChampionEntries = null;
    private List<SummonerSpellEntry> mSummonerSpell1Entries = null;
    private List<SummonerSpellEntry> mSummonerSpell2Entries = null;
    private List<ItemEntry> mItemEntries = null;

    List<ChampionEntry> getChampionEntries() {
        return mChampionEntries;
    }

    List<SummonerSpellEntry> getSummonerSpell1Entries() {
        return mSummonerSpell1Entries;
    }

    List<SummonerSpellEntry> getSummonerSpell2Entries() {
        return mSummonerSpell2Entries;
    }

    List<ItemEntry> getItemEntries() {
        return mItemEntries;
    }

    void setChampionEntries(List<ChampionEntry> championEntries) {
        this.mChampionEntries = championEntries;
    }

    void setSummonerSpell1Entries(List<SummonerSpellEntry> summonerSpell1Entries) {
        this.mSummonerSpell1Entries = summonerSpell1Entries;
    }

    void setSummonerSpell2Entries(List<SummonerSpellEntry> summonerSpell2Entries) {
        this.mSummonerSpell2Entries = summonerSpell2Entries;
    }

    void setItemEntries(List<ItemEntry> itemEntries) {
        this.mItemEntries = itemEntries;
    }
}
