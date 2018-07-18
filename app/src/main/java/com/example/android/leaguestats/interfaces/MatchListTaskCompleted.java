package com.example.android.leaguestats.interfaces;

import com.example.android.leaguestats.models.MatchList;

import java.util.ArrayList;

public interface MatchListTaskCompleted {
    void matchListTaskCompleted(ArrayList<MatchList> matchLists);
}
