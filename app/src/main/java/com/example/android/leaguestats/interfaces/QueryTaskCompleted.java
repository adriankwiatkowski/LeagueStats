package com.example.android.leaguestats.interfaces;

import com.example.android.leaguestats.models.Mastery;

import java.util.List;

public interface QueryTaskCompleted {
    void queryTaskCompleted(List<Mastery> masteries);
}
