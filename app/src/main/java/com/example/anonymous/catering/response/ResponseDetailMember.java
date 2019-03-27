package com.example.anonymous.catering.response;

import com.example.anonymous.catering.models.Member;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseDetailMember {
    @SerializedName("master")
    @Expose
    private Member master;

    public Member getMaster() {
        return master;
    }

    public void setMaster(Member master) {
        this.master = master;
    }
}
