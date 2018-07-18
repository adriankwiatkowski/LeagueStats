package com.example.android.leaguestats.models;

public class MatchList {

    private long mGameId;
    private long mTimestamp;

    public MatchList(long gameId, long timestamp) {
        mGameId = gameId;
        mTimestamp = timestamp;
    }

    public long getTimestamp() {
        return mTimestamp;
    }

    public void setTimestamp(long timestamp) {
        this.mTimestamp = timestamp;
    }

    public long getGameId() {
        return mGameId;
    }

    public void setGameId(long gameId) {
        this.mGameId = gameId;
    }
}
