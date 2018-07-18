package com.example.android.leaguestats.interfaces;

import com.example.android.leaguestats.models.SummonerSpell;

import java.util.List;

public interface SummonerSpellTaskCompleted {
    void summonerSpellTaskCompleted(List<SummonerSpell> summonerSpells);
}
