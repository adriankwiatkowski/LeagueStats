package com.example.android.leaguestats.interfaces;

import com.example.android.leaguestats.models.Match;

import java.util.ArrayList;

public interface MatchTaskCompleted {
    void matchTaskCompleted(ArrayList<Match> matches);
}
