package com.example.android.leaguestats.data.database.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.android.leaguestats.data.database.entity.SummonerSpellEntry;

import java.util.List;

@Dao
public interface SummonerSpellDao {

    @Query("SELECT * FROM summoner_spell")
    LiveData<List<SummonerSpellEntry>> getSummonerSpells();

    @Query("SELECT * FROM summoner_spell WHERE id = :id")
    LiveData<SummonerSpellEntry> getSummonerSpells(long id);

    @Query("SELECT * FROM summoner_spell WHERE id IN (:id)")
    LiveData<List<SummonerSpellEntry>> getSummonerSpells(int[] id);

    @Query("SELECT COUNT(id) FROM summoner_spell")
    int countAllSummonerSpells();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void bulkInsert(SummonerSpellEntry... spellEntries);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateSummonerSpell(SummonerSpellEntry spellEntry);

    @Query("DELETE FROM summoner_spell")
    void deleteSpells();
}
