package com.example.anonymous.catering.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Pemesanan {
    @SerializedName("id_pemesanan")
    @Expose
    private String idPemesanan;
    @SerializedName("member_id")
    @Expose
    private String memberId;
    @SerializedName("nama_paket")
    @Expose
    private String namaPaket;
    @SerializedName("jumlah_pesanan")
    @Expose
    private String jumlahPesanan;
    @SerializedName("total")
    @Expose
    private String total;
    @SerializedName("alamat_lengkap")
    @Expose
    private String alamatLengkap;
    @SerializedName("latitude")
    @Expose
    private String latitude;
    @SerializedName("longitude")
    @Expose
    private String longitude;
    @SerializedName("tgl_pesanan")
    @Expose
    private String tglPesanan;
    @SerializedName("pesan_tambahan")
    @Expose
    private String pesanTambahan;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("nama_lengkap")
    @Expose
    private String namaLengkap;

    public String getIdPemesanan() {
        return idPemesanan;
    }

    public void setIdPemesanan(String idPemesanan) {
        this.idPemesanan = idPemesanan;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getNamaPaket() {
        return namaPaket;
    }

    public void setNamaPaket(String namaPaket) {
        this.namaPaket = namaPaket;
    }

    public String getJumlahPesanan() {
        return jumlahPesanan;
    }

    public void setJumlahPesanan(String jumlahPesanan) {
        this.jumlahPesanan = jumlahPesanan;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getAlamatLengkap() {
        return alamatLengkap;
    }

    public void setAlamatLengkap(String alamatLengkap) {
        this.alamatLengkap = alamatLengkap;
    }

        public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getTglPesanan() {
        return tglPesanan;
    }

    public void setTglPesanan(String tglPesanan) {
        this.tglPesanan = tglPesanan;
    }

    public String getPesanTambahan() {
        return pesanTambahan;
    }

    public void setPesanTambahan(String pesanTambahan) {
        this.pesanTambahan = pesanTambahan;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNamaLengkap() {
        return namaLengkap;
    }

    public void setNamaLengkap(String namaLengkap) {
        this.namaLengkap = namaLengkap;
    }

}