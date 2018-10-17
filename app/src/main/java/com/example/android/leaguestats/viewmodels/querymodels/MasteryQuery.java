package com.example.android.leaguestats.viewmodels.querymodels;

public class MasteryQuery {

    private String entryUrlString;
    private long summonerId;

    public MasteryQuery(String entryUrlString, long summonerId) {
        this.entryUrlString = entryUrlString;
        this.summonerId = summonerId;
    }

    public String getEntryUrlString() {
        return entryUrlString;
    }

    public void setEntryUrlString(String entryUrlString) {
        this.entryUrlString = entryUrlString;
    }

    public long getSummonerId() {
        return summonerId;
    }

    public void setSummonerId(long summonerId) {
        this.summonerId = summonerId;
    }
}
