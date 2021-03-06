package com.novadwisapta.ngetrip.MyViewHolders;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.novadwisapta.ngetrip.R;

import org.jetbrains.annotations.NotNull;

public class TiketWisataViewHolder extends RecyclerView.ViewHolder {
    public TextView txtNamaPaketWisata, txtTglTiket, txtJumlahTiket;

    public TiketWisataViewHolder(@NonNull @NotNull View itemView) {
        super(itemView);

        txtNamaPaketWisata = itemView.findViewById(R.id.txt_nama_paket_wisata);
        txtTglTiket = itemView.findViewById(R.id.txt_tgl_tiket);
        txtJumlahTiket = itemView.findViewById(R.id.txt_jumlah_tiket);
    }
}
