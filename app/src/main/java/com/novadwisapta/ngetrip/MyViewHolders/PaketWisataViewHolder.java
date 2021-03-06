package com.novadwisapta.ngetrip.MyViewHolders;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.novadwisapta.ngetrip.R;

import org.jetbrains.annotations.NotNull;

public class PaketWisataViewHolder extends RecyclerView.ViewHolder {
    public TextView nama_paket_wisata;
    public TextView nama_agent_travel;
    public TextView harga;
    public ImageView img_wisata;
    public Button btn_detail;

    public PaketWisataViewHolder(@NonNull @NotNull View itemView) {
        super(itemView);

        nama_paket_wisata = itemView.findViewById(R.id.nama_paket_wisata);
        nama_agent_travel = itemView.findViewById(R.id.nama_agent_travel);
        harga = itemView.findViewById(R.id.harga);
        img_wisata = itemView.findViewById(R.id.img_wisata);
        btn_detail = itemView.findViewById(R.id.btn_detail);
    }
}
