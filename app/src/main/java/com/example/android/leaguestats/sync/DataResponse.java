package com.example.android.leaguestats.sync;

import android.support.annotation.NonNull;

import com.example.android.leaguestats.database.entity.ChampionEntry;
import com.example.android.leaguestats.database.entity.IconEntry;
import com.example.android.leaguestats.database.entity.ItemEntry;
import com.example.android.leaguestats.database.entity.SummonerSpellEntry;

public class DataResponse {

    @NonNull
    private final ChampionEntry[] mChampions;
    @NonNull
    private final ItemEntry[] mItems;
    @NonNull
    private final IconEntry[] mIcons;
    @NonNull
    private final SummonerSpellEntry[] mSummonerSpells;

    public DataResponse(@NonNull final ChampionEntry[] championEntries,
                        @NonNull final ItemEntry[] itemEntries,
                        @NonNull final IconEntry[] iconEntries,
                        @NonNull final SummonerSpellEntry[] summonerSpellEntries) {

        mChampions = championEntries;
        mItems = itemEntries;
        mIcons = iconEntries;
        mSummonerSpells = summonerSpellEntries;
    }

    @NonNull
    public ChampionEntry[] getChampions() {
        return mChampions;
    }

    @NonNull
    public ItemEntry[] getItems() {
        return mItems;
    }

    @NonNull
    public IconEntry[] getIcons() {
        return mIcons;
    }

    @NonNull
    public SummonerSpellEntry[] getSummonerSpells() {
        return mSummonerSpells;
    }
}
