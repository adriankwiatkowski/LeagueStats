package com.example.android.leaguestats.interfaces;

import com.example.android.leaguestats.room.ChampionEntry;

import java.util.List;

public interface ChampionTaskCompleted {
    void championTaskCompleted(List<ChampionEntry> champion);
}
