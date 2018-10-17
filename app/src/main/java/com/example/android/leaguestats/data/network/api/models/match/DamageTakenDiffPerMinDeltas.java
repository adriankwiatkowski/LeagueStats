package com.example.android.leaguestats.data.network.api.models.match;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DamageTakenDiffPerMinDeltas {

    @SerializedName("10-20")
    @Expose
    private double _1020;
    @SerializedName("0-10")
    @Expose
    private double _010;

    /**
     * No args constructor for use in serialization
     *
     */
    public DamageTakenDiffPerMinDeltas() {
    }

    /**
     *
     * @param _010
     * @param _1020
     */
    public DamageTakenDiffPerMinDeltas(double _1020, double _010) {
        super();
        this._1020 = _1020;
        this._010 = _010;
    }

    public double get1020() {
        return _1020;
    }

    public void set1020(double _1020) {
        this._1020 = _1020;
    }

    public double get010() {
        return _010;
    }

    public void set010(double _010) {
        this._010 = _010;
    }

}
