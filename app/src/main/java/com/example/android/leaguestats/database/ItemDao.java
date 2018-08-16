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
public interface ItemDao {

    @Query("SELECT * FROM item")
    LiveData<List<ItemEntry>> loadAllItems();

    @Query("SELECT * FROM item WHERE purchasable = 'true' AND total_gold > 0 ORDER BY total_gold")
    LiveData<List<ItemEntry>> loadAllPurchasableItems();

    @Query("SELECT * FROM item WHERE id = :id")
    LiveData<ItemEntry> loadItemById(int id);

    @Query("SELECT * FROM item WHERE id IN (:ids) AND total_gold > 0 ORDER BY total_gold")
    LiveData<List<ItemEntry>> loadItemsWithIds(String[] ids);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertItem(ItemEntry itemEntry);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void bulkInsert(ItemEntry... itemEntries);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateItem(ItemEntry itemEntry);

    @Delete
    void deleteItem(ItemEntry itemEntry);
}
