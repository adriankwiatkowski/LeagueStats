package com.example.android.leaguestats.data.database.models;

import android.arch.persistence.room.ColumnInfo;

public class ListItemEntry {

    private int id;
    private String name;
    private String image;
    @ColumnInfo(name = "total_gold")
    private int totalGold;

    public ListItemEntry(int id, String name, String image, int totalGold) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.totalGold = totalGold;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public int getTotalGold() {
        return totalGold;
    }
}
