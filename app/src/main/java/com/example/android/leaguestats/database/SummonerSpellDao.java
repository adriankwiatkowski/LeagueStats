package com.example.android.leaguestats.room;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface SummonerSpellDao {

    @Query("SELECT * FROM summoner_spell")
    LiveData<List<SummonerSpellEntry>> loadAllSpells();

    @Query("SELECT * FROM summoner_spell WHERE id = :id")
    LiveData<SummonerSpellEntry> loadSpellById(int id);

    @Query("SELECT * FROM summoner_spell WHERE id IN (:ids)")
    LiveData<List<SummonerSpellEntry>> loadSpellsWithIds(int[] ids);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertSpell(SummonerSpellEntry spellEntry);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void bulkInsert(SummonerSpellEntry... spellEntries);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateSpell(SummonerSpellEntry spellEntry);

    @Delete
    void deleteSpell(SummonerSpellEntry spellEntry);
}
