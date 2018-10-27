package com.example.android.leaguestats.data.database.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "icon")
public class IconEntry {

    @PrimaryKey
    @ColumnInfo(name = "id")
    private int id;
    private String icon;

    public IconEntry(int id, String icon) {
        this.icon = icon;
        this.id = id;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String iconPath) {
        this.icon = iconPath;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
