package com.novadwisapta.ngetrip.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.novadwisapta.ngetrip.MyViewHolders.TiketWisataViewHolder;
import com.novadwisapta.ngetrip.R;
import com.novadwisapta.ngetrip.model.TiketWisata;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class TicketWisataAdapter extends RecyclerView.Adapter<TiketWisataViewHolder> {

    Context context;
    ArrayList<TiketWisata> tiketWisata;

    public TicketWisataAdapter(Context context, ArrayList<TiketWisata> tiketWisata) {
        this.context = context;
        this.tiketWisata = tiketWisata;
    }

    @NonNull
    @NotNull
    @Override
    public TiketWisataViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_myticket, parent, false);

        return new TiketWisataViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull TiketWisataViewHolder holder, int position) {
        holder.txtNamaPaketWisata.setText(tiketWisata.get(position).getNama_paket_wisata());
        holder.txtJumlahTiket.setText(String.valueOf(tiketWisata.get(position).getTotal_tiket()));
        holder.txtTglTiket.setText(tiketWisata.get(position).getTgl_tiket());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "ID: " + tiketWisata.get(position).getId_tiket_wisata(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return tiketWisata.size();
    }
}
