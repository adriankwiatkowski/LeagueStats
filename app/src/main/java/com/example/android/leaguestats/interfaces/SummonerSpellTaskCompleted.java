package com.example.android.leaguestats.interfaces;

import com.example.android.leaguestats.room.SummonerSpellEntry;

import java.util.List;

public interface SummonerSpellTaskCompleted {
    void summonerSpellTaskCompleted(List<SummonerSpellEntry> list);
}
