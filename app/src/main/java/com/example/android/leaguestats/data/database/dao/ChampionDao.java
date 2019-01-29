package com.example.android.leaguestats.data.database.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import com.example.android.leaguestats.data.database.entity.ChampionEntry;
import com.example.android.leaguestats.models.Champion;

import java.util.List;

@Dao
public abstract class ChampionDao implements BaseDao<ChampionEntry> {

    @Query("SELECT * FROM champion ORDER BY champion_name")
    public abstract LiveData<List<ChampionEntry>> getChampionEntries();

    @Query("SELECT champion_id, champion_key, champion_name, champion_title, champion_image_id, " +
            "champion_splash_art_id, champion_splash_art_name FROM champion ORDER BY champion_name")
    public abstract LiveData<List<Champion>> getChampions();

    @Query("SELECT champion_id, champion_key, champion_name, champion_title, champion_image_id, " +
            "champion_splash_art_id, champion_splash_art_name  FROM champion WHERE champion_id IN (:id)")
    public abstract LiveData<List<Champion>> getChampions(List<Integer> id);

    @Query("SELECT * FROM champion WHERE champion_id = :id")
    public abstract LiveData<ChampionEntry> getChampion(long id);

    @Query("SELECT * FROM champion WHERE champion_name LIKE :name")
    public abstract LiveData<ChampionEntry> getChampion(String name);

    @Query("SELECT COUNT(champion_id) FROM champion")
    public abstract LiveData<Integer> countAllLiveChampions();

    @Query("SELECT COUNT(champion_id) FROM champion")
    public abstract int countAllChampions();

    @Query("DELETE FROM champion")
    public abstract void deleteChampions();
}
