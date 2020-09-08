package com.example.android.leaguestats.data.network.api.models.match;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

// TODO: Remove it later.
public class Ban {

    @SerializedName("championId")
    @Expose
    private int championId;
    @SerializedName("pickTurn")
    @Expose
    private int pickTurn;

    /**
     * No args constructor for use in serialization
     *
     */
    public Ban() {
    }

    /**
     *
     * @param pickTurn
     * @param championId
     */
    public Ban(int championId, int pickTurn) {
        super();
        this.championId = championId;
        this.pickTurn = pickTurn;
    }

    public int getChampionId() {
        return championId;
    }

    public void setChampionId(int championId) {
        this.championId = championId;
    }

    public int getPickTurn() {
        return pickTurn;
    }

    public void setPickTurn(int pickTurn) {
        this.pickTurn = pickTurn;
    }

}
