package com.example.android.leaguestats.data.network.api.models.match;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ParticipantIdentity {

    @SerializedName("participantId")
    @Expose
    private int participantId;
    @SerializedName("player")
    @Expose
    private Player player;

    /**
     * No args constructor for use in serialization
     *
     */
    public ParticipantIdentity() {
    }

    /**
     *
     * @param player
     * @param participantId
     */
    public ParticipantIdentity(int participantId, Player player) {
        super();
        this.participantId = participantId;
        this.player = player;
    }

    public int getParticipantId() {
        return participantId;
    }

    public void setParticipantId(int participantId) {
        this.participantId = participantId;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

}
