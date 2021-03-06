package com.novadwisapta.ngetrip.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AgentTravel {
    int id_agent_travel;
    @SerializedName("nama")
    @Expose
    String nama_agent_travel;
    String alamat;
    String gambar;
    String deskripsi;
    List<KontakAgentTravel> kontak;

    public AgentTravel() {
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public List<KontakAgentTravel> getKontak() {
        return kontak;
    }

    public String getGambar() {
        return gambar;
    }

    public int getId_agent_travel() {
        return id_agent_travel;
    }

    public String getNama_agent_travel() {
        return nama_agent_travel;
    }

    public String getAlamat() {
        return alamat;
    }
}

