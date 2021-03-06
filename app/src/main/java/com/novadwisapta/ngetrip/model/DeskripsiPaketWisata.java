package com.novadwisapta.ngetrip.model;

public class DeskripsiPaketWisata {
    int id_deskripsi_wisata;
    int id_paket_wisata;
    int hari_ke;
    String keterangan;

    public DeskripsiPaketWisata() {

    }

    public int getId_deskripsi_wisata() {
        return id_deskripsi_wisata;
    }

    public int getId_paket_wisata() {
        return id_paket_wisata;
    }

    public int getHari_ke() {
        return hari_ke;
    }

    public String getKeterangan() {
        return keterangan;
    }
}
