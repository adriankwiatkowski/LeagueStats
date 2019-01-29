package com.example.android.leaguestats.models;

import android.arch.persistence.room.ColumnInfo;

public class Item {

    @ColumnInfo(name = "item_id")
    private int id;
    @ColumnInfo(name = "item_key")
    private String key;
    @ColumnInfo(name = "item_name")
    private String name;
    @ColumnInfo(name = "item_plain_text")
    private String plainText;
    @ColumnInfo(name = "item_description")
    private String description;
    @ColumnInfo(name = "item_image_id")
    private String itemImageId;
    @ColumnInfo(name = "item_total_gold")
    private int totalGold;

    public Item(int id, String key, String name, String plainText, String description, String itemImageId, int totalGold) {
        this.id = id;
        this.key = key;
        this.name = name;
        this.plainText = plainText;
        this.description = description;
        this.itemImageId = itemImageId;
        this.totalGold = totalGold;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getItemImageId() {
        return itemImageId;
    }

    public void setItemImageId(String itemImageId) {
        this.itemImageId = itemImageId;
    }

    public int getTotalGold() {
        return totalGold;
    }

    public void setTotalGold(int totalGold) {
        this.totalGold = totalGold;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPlainText() {
        return plainText;
    }

    public void setPlainText(String plainText) {
        this.plainText = plainText;
    }
}
