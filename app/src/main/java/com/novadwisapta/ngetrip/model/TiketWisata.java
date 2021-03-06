package com.novadwisapta.ngetrip.model;

public class TiketWisata {
    int id_tiket_wisata;
    int id_paket_wisata;
    int total_tiket;
    int total_harga;
    String kode_transaksi;
    String username;
    String tgl_tiket;
    String nama_paket_wisata;
    String meeting_point;

    public TiketWisata(int id_paket_wisata, int total_tiket, int total_harga, String kode_transaksi, String username, String tgl_tiket) {
        this.id_paket_wisata = id_paket_wisata;
        this.total_tiket = total_tiket;
        this.total_harga = total_harga;
        this.kode_transaksi = kode_transaksi;
        this.username = username;
        this.tgl_tiket = tgl_tiket;
    }

    public String getNama_paket_wisata() {
        return nama_paket_wisata;
    }

    public String getMeeting_point() {
        return meeting_point;
    }

    public int getId_tiket_wisata() {
        return id_tiket_wisata;
    }

    public void setId_tiket_wisata(int id_tiket_wisata) {
        this.id_tiket_wisata = id_tiket_wisata;
    }

    public int getId_paket_wisata() {
        return id_paket_wisata;
    }

    public void setId_paket_wisata(int id_paket_wisata) {
        this.id_paket_wisata = id_paket_wisata;
    }

    public int getTotal_tiket() {
        return total_tiket;
    }

    public void setTotal_tiket(int total_tiket) {
        this.total_tiket = total_tiket;
    }

    public int getTotal_harga() {
        return total_harga;
    }

    public void setTotal_harga(int total_harga) {
        this.total_harga = total_harga;
    }

    public String getKode_transaksi() {
        return kode_transaksi;
    }

    public void setKode_transaksi(String kode_transaksi) {
        this.kode_transaksi = kode_transaksi;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTgl_tiket() {
        return tgl_tiket;
    }

    public void setTgl_tiket(String tgl_tiket) {
        this.tgl_tiket = tgl_tiket;
    }
}
