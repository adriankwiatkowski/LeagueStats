package com.example.android.leaguestats.data.database.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.android.leaguestats.data.database.entity.ChampionEntry;

import java.util.List;

@Dao
public interface ChampionDao {

    @Query("SELECT * FROM champion ORDER BY name")
    LiveData<List<ChampionEntry>> getChampions();

    @Query("SELECT * FROM champion WHERE id = :id")
    LiveData<ChampionEntry> getChampion(long id);

    @Query("SELECT * FROM champion WHERE name LIKE + :name")
    LiveData<ChampionEntry> getChampion(String name);

    @Query("SELECT * FROM champion WHERE id IN (:id)")
    LiveData<List<ChampionEntry>> getChampions(int[] id);

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
