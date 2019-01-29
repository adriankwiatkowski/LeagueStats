package com.example.android.leaguestats.data.database.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import com.example.android.leaguestats.data.database.entity.ItemEntry;
import com.example.android.leaguestats.models.Item;

import java.util.List;

@Dao
public abstract class ItemDao implements BaseDao<ItemEntry> {

    @Query("SELECT item_id, item_key, item_name, item_description, item_plain_text, item_image_id, item_total_gold FROM item WHERE item_purchasable = 'true' AND item_total_gold > 0 ORDER BY item_total_gold")
    public abstract LiveData<List<Item>> getItems();

    @Query("SELECT item_id, item_key, item_name, item_description, item_plain_text, item_image_id, item_total_gold FROM item WHERE item_id IN (:id) ORDER BY item_total_gold")
    public abstract LiveData<List<Item>> getItems(List<Integer> id);

    @Query("SELECT * FROM item WHERE item_id IN (:id)")
    public abstract LiveData<ItemEntry> getItem(long id);

    @Query("SELECT * FROM item WHERE item_name LIKE :name")
    public abstract LiveData<ItemEntry> getItem(String name);

    @Query("SELECT item_id, item_key, item_name, item_description, item_plain_text, item_image_id, item_total_gold FROM item WHERE item_id IN (:id) ORDER BY item_total_gold")
    public abstract LiveData<List<Item>> getItems(String[] id);

    @Query("SELECT COUNT(item_id) FROM item")
    public abstract LiveData<Integer> countAllLiveItems();

    @Query("SELECT COUNT(item_id) FROM item")
    public abstract int countAllItems();

    @Query("DELETE FROM item")
    public abstract void deleteItems();
}
