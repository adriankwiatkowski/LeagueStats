package com.example.android.leaguestats.data.network.api.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MatchListResponse {

    @SerializedName("matches")
    @Expose
    private List<MatchList> matches = null;
    @SerializedName("startIndex")
    @Expose
    private int startIndex;
    @SerializedName("endIndex")
    @Expose
    private int endIndex;
    @SerializedName("totalGames")
    @Expose
    private int totalGames;

    public MatchListResponse() {
    }

    public MatchListResponse(List<MatchList> matches, Integer startIndex, Integer endIndex, Integer totalGames) {
        super();
        this.matches = matches;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        this.totalGames = totalGames;
    }

    public List<MatchList> getMatchList() {
        return matches;
    }

    public void setMatches(List<MatchList> matches) {
        this.matches = matches;
    }

    public Integer getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(Integer startIndex) {
        this.startIndex = startIndex;
    }

    public Integer getEndIndex() {
        return endIndex;
    }

    public void setEndIndex(Integer endIndex) {
        this.endIndex = endIndex;
    }

    public Integer getTotalGames() {
        return totalGames;
    }

    public void setTotalGames(Integer totalGames) {
        this.totalGames = totalGames;
    }
}
