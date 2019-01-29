package com.example.android.leaguestats.data.database.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import com.example.android.leaguestats.data.database.entity.IconEntry;

import java.util.List;

@Dao
public abstract class IconDao implements BaseDao<IconEntry> {

    @Query("SELECT * FROM icon")
    public abstract LiveData<List<IconEntry>> getIcons();

    @Query("SELECT * FROM icon WHERE icon_id = :id")
    public abstract LiveData<IconEntry> getIcon(int id);

    @Query("SELECT COUNT(icon_id) FROM icon")
    public abstract LiveData<Integer> countAllLiveIcons();

    @Query("SELECT COUNT(icon_id) FROM icon")
    public abstract int countAllIcons();

    @Query("DELETE FROM icon")
    public abstract void deleteIcons();
}
