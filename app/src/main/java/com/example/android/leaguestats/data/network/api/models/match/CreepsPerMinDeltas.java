package com.example.android.leaguestats.data.network.api.models.match;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

// TODO: Remove it later.
public class CreepsPerMinDeltas {

    @SerializedName("10-20")
    @Expose
    private int _1020;
    @SerializedName("0-10")
    @Expose
    private float _010;
    @SerializedName("20-30")
    @Expose
    private float _2030;

    /**
     * No args constructor for use in serialization
     *
     */
    public CreepsPerMinDeltas() {
    }

    /**
     *
     * @param _2030
     * @param _010
     * @param _1020
     */
    public CreepsPerMinDeltas(int _1020, float _010, float _2030) {
        super();
        this._1020 = _1020;
        this._010 = _010;
        this._2030 = _2030;
    }

    public int get1020() {
        return _1020;
    }

    public void set1020(int _1020) {
        this._1020 = _1020;
    }

    public float get010() {
        return _010;
    }

    public void set010(float _010) {
        this._010 = _010;
    }

    public float get2030() {
        return _2030;
    }

    public void set2030(float _2030) {
        this._2030 = _2030;
    }

}