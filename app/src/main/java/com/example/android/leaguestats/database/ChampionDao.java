package com.example.android.leaguestats.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface ChampionDao {

    @Query("SELECT * FROM champion ORDER BY name")
    LiveData<List<ChampionEntry>> loadAllChampions();

    @Query("SELECT * FROM champion WHERE id = :id")
    LiveData<ChampionEntry> loadChampionById(long id);

    @Query("SELECT * FROM champion WHERE id IN (:ids)")
    LiveData<List<ChampionEntry>> loadChampionsWithIds(long[] ids);

    @Query("SELECT * FROM champion WHERE id IN (:ids)")
    LiveData<List<ChampionEntry>> loadChampionsWithIds(int[] ids);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertChampion(ChampionEntry championEntry);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void bulkInsert(ChampionEntry... championEntries);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateChampion(ChampionEntry championEntry);

    @Delete
    void deleteChampion(ChampionEntry championEntry);
}
