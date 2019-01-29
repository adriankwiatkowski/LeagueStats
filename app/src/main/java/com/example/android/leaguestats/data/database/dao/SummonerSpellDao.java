package com.example.android.leaguestats.data.database.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import com.example.android.leaguestats.data.database.entity.SummonerSpellEntry;
import com.example.android.leaguestats.models.SummonerSpell;

import java.util.List;

@Dao
public abstract class SummonerSpellDao implements BaseDao<SummonerSpellEntry> {

    @Query("SELECT summoner_spell_id, summoner_spell_key, summoner_spell_name, summoner_spell_description, summoner_spell_image_id, summoner_spell_cooldown FROM summoner_spell")
    public abstract LiveData<List<SummonerSpell>> getSummonerSpells();

    @Query("SELECT summoner_spell_id, summoner_spell_key, summoner_spell_name, summoner_spell_description, summoner_spell_image_id, summoner_spell_cooldown FROM summoner_spell WHERE summoner_spell_id IN (:summonerSpellIdList)")
    public abstract LiveData<List<SummonerSpell>> getSummonerSpells(List<Integer> summonerSpellIdList);

    @Query("SELECT * FROM summoner_spell WHERE summoner_spell_id = :id")
    public abstract LiveData<SummonerSpellEntry> getSummonerSpells(long id);

    @Query("SELECT * FROM summoner_spell WHERE summoner_spell_name LIKE :name")
    public abstract LiveData<SummonerSpellEntry> getSummonerSpells(String name);

    @Query("SELECT COUNT(summoner_spell_id) FROM summoner_spell")
    public abstract LiveData<Integer> countAllLiveSummonerSpells();

    @Query("SELECT COUNT(summoner_spell_id) FROM summoner_spell")
    public abstract int countAllSummonerSpells();

    @Query("DELETE FROM summoner_spell")
    public abstract void deleteSpells();
}
