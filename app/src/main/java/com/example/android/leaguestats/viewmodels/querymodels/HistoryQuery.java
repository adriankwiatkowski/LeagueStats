package com.example.android.leaguestats.viewmodels.querymodels;

public class HistoryQuery {

    private String entryUrlString;
    private long accountId;
    private long summonerId;

    public HistoryQuery(String entryUrlString, long accountId, long summonerId) {
        this.entryUrlString = entryUrlString;
        this.accountId = accountId;
        this.summonerId = summonerId;
    }

    public String getEntryUrlString() {
        return entryUrlString;
    }

    public void setEntryUrlString(String entryUrlString) {
        this.entryUrlString = entryUrlString;
    }

    public long getAccountId() {
        return accountId;
    }

    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }

    public long getSummonerId() {
        return summonerId;
    }

    public void setSummonerId(long summonerId) {
        this.summonerId = summonerId;
    }
}
