package com.example.anonymous.catering.response;

import com.example.anonymous.catering.models.Member;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseMember {


    @SerializedName("master")
    @Expose
    private List<Member> master = null;

    public List<Member> getMaster() {
        return master;
    }

    public void setMaster(List<Member> master) {
        this.master = master;
    }
}

