package com.example.anonymous.catering.response;

import com.example.anonymous.catering.models.Pemesanan;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponsePemesanan {

    @SerializedName("master")
    @Expose
    private List<Pemesanan> master = null;

    public List<Pemesanan> getMaster() {
        return master;
    }

    public void setMaster(List<Pemesanan> master) {
        this.master = master;
    }
}
