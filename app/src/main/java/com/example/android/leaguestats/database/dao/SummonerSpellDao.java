package com.example.android.leaguestats.database.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.android.leaguestats.database.entity.SummonerSpellEntry;
import com.example.android.leaguestats.database.models.ListSummonerSpellEntry;

import java.util.List;

@Dao
public interface SummonerSpellDao {

    @Query("SELECT id, summoner_spell_key name, cooldown, image FROM summoner_spell")
    LiveData<List<ListSummonerSpellEntry>> loadSpellList();

    @Query("SELECT * FROM summoner_spell WHERE summoner_spell_key = :id")
    LiveData<SummonerSpellEntry> loadSpellById(String id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void bulkInsert(SummonerSpellEntry... spellEntries);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateSpell(SummonerSpellEntry spellEntry);

    @Query("DELETE FROM summoner_spell")
    void deleteSpells();

    @Query("SELECT id, summoner_spell_key, name, cooldown, image FROM summoner_spell WHERE summoner_spell_key IN (:id)")
    List<ListSummonerSpellEntry> loadSpellsWithId(String[] id);
}