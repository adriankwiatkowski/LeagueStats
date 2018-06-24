package com.example.android.leaguestats.models;

public class Icon {

    private String mIconPath;
    private int mIconId;

    public Icon(String iconPath, int iconId) {
        this.mIconPath = iconPath;
        this.mIconId = iconId;
    }

    public String getIconPath() {

        return mIconPath;
    }

    public void setIconPath(String iconPath) {
        this.mIconPath = iconPath;
    }

    public int getIconId() {
        return mIconId;
    }

    public void setIconId(int iconId) {
        this.mIconId = iconId;
    }
}
