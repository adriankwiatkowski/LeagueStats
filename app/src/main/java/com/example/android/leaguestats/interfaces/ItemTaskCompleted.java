package com.example.android.leaguestats.interfaces;

import com.example.android.leaguestats.room.ItemEntry;

import java.util.List;

public interface ItemTaskCompleted {
    void itemTaskCompleted(List<ItemEntry> items);
}
