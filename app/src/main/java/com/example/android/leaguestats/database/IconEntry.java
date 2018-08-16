package com.example.android.leaguestats.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "icon")
public class IconEntry {

    @PrimaryKey
    @ColumnInfo(name = "icon_id")
    private int iconId;
    private String icon;

    public IconEntry(int iconId, String icon) {
        this.icon = icon;
        this.iconId = iconId;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String iconPath) {
        this.icon = iconPath;
    }

    public int getIconId() {
        return iconId;
    }

    public void setIconId(int iconId) {
        this.iconId = iconId;
    }
}
