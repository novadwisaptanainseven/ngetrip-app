package com.novadwisapta.ngetrip.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PaketWisata {
    int id_paket_wisata;
    int id_agent_travel;
    String nama_agent_travel;
    String nama_paket_wisata;
    String satuan;
    int harga;
    String meeting_point;
    String gambar;
    @SerializedName("gambar_agent_travel")
    @Expose
    String gambarAgentTravel;

    public String getGambarAgentTravel() {
        return gambarAgentTravel;
    }

    public String getNama_agent_travel() {
        return nama_agent_travel;
    }

    public void setNama_agent_travel(String nama_agent_travel) {
        this.nama_agent_travel = nama_agent_travel;
    }

    public String getSatuan() {
        return satuan;
    }

    public int getId_paket_wisata() {
        return id_paket_wisata;
    }

    public void setId_paket_wisata(int id_paket_wisata) {
        this.id_paket_wisata = id_paket_wisata;
    }

    public int getId_agent_travel() {
        return id_agent_travel;
    }

    public void setId_agent_travel(int id_agent_travel) {
        this.id_agent_travel = id_agent_travel;
    }

    public String getNama_paket_wisata() {
        return nama_paket_wisata;
    }

    public void setNama_paket_wisata(String nama_paket_wisata) {
        this.nama_paket_wisata = nama_paket_wisata;
    }

    public int getHarga() {
        return harga;
    }

    public void setHarga(int harga) {
        this.harga = harga;
    }

    public String getMeeting_point() {
        return meeting_point;
    }

    public void setMeeting_point(String meeting_point) {
        this.meeting_point = meeting_point;
    }

    public String getGambar() {
        return gambar;
    }

    public void setGambar(String gambar) {
        this.gambar = gambar;
    }
}

