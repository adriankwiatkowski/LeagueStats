package com.example.android.leaguestats.viewmodels.querymodels;

public class SummonerQuery {

    private String entryUrlString;
    private String summonerName;

    public SummonerQuery(String entryUrlString, String summonerName) {
        this.entryUrlString = entryUrlString;
        this.summonerName = summonerName;
    }

    public String getEntryUrlString() {
        return entryUrlString;
    }

    public String getSummonerName() {
        return summonerName;
    }
}
