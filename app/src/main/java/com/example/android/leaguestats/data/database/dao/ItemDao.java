package com.example.android.leaguestats.data.database.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.android.leaguestats.data.database.entity.ItemEntry;
import com.example.android.leaguestats.data.database.models.ListItemEntry;

import java.util.List;

@Dao
public interface ItemDao {

    @Query("SELECT id, name, image, total_gold FROM item WHERE purchasable = 'true' AND total_gold > 0 ORDER BY total_gold")
    LiveData<List<ListItemEntry>> loadListItem();

    @Query("SELECT * FROM item WHERE id IN (:id)")
    LiveData<ItemEntry> loadItem(long id);

    @Query("SELECT * FROM item WHERE name LIKE + :name")
    LiveData<ItemEntry> loadItem(String name);

    @Query("SELECT id, name, image, total_gold FROM item WHERE id IN (:id)")
    LiveData<List<ListItemEntry>> loadItemsWithId(String[] id);

    @Query("SELECT id, name, image, total_gold FROM item WHERE id IN (:id)")
    List<ListItemEntry> loadItemsWithId(int[] id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void bulkInsert(ItemEntry... itemEntries);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateItem(ItemEntry itemEntry);

    @Query("DELETE FROM item")
    void deleteItems();

}
