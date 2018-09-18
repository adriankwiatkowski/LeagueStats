package com.example.android.leaguestats.database.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.android.leaguestats.database.entity.ChampionEntry;
import com.example.android.leaguestats.database.models.ListChampionEntry;

import java.util.List;

@Dao
public interface ChampionDao {

    @Query("SELECT id, champion_key, name, title, image FROM champion ORDER BY name")
    LiveData<List<ListChampionEntry>> loadChampionList();

    @Query("SELECT * FROM champion WHERE champion_key = :id")
    LiveData<ChampionEntry> loadChampionById(String id);

    @Query("SELECT id, champion_key, name, title, image FROM champion WHERE champion_key IN (:id)")
    List<ListChampionEntry> loadChampionsWithId(String[] id);

    @Query("SELECT COUNT(id) FROM champion")
    int countAllChampions();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertChampion(ChampionEntry championEntry);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void bulkInsert(ChampionEntry... championEntries);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateChampion(ChampionEntry championEntry);

    @Query("DELETE FROM champion")
    void deleteChampions();
}
