package com.novadwisapta.ngetrip.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.novadwisapta.ngetrip.DetailPaketWisataActivity;
import com.novadwisapta.ngetrip.MyConfiguration;
import com.novadwisapta.ngetrip.MyViewHolders.PaketWisataViewHolder;
import com.novadwisapta.ngetrip.R;
import com.novadwisapta.ngetrip.model.PaketWisata;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Currency;

public class PaketWisataAdapter extends RecyclerView.Adapter<PaketWisataViewHolder> {
    Context context;
    ArrayList<PaketWisata> paketWisata;
    MyConfiguration myConfiguration;
    String myIPAddress = "";

    public PaketWisataAdapter(Context context, ArrayList<PaketWisata> paketWisata) {
        this.context = context;
        this.paketWisata = paketWisata;

        // Set Configuration for API Server
        myConfiguration = new MyConfiguration();
        myIPAddress = myConfiguration.getIPAddressForFile();
    }

    @NonNull
    @NotNull
    @Override
    public PaketWisataViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.list_paket_wisata, parent, false);

        return new PaketWisataViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull PaketWisataViewHolder holder, int position) {
        holder.nama_paket_wisata.setText(paketWisata.get(position).getNama_paket_wisata());
        holder.nama_agent_travel.setText(paketWisata.get(position).getNama_agent_travel());
        int harga = paketWisata.get(position).getHarga();

        // Format Harga dalam bentuk Rupiah
        NumberFormat format_harga = NumberFormat.getCurrencyInstance();
        format_harga.setMaximumFractionDigits(0);
        format_harga.setCurrency(Currency.getInstance("IDR"));
        format_harga.format(harga);

        String harga2 = format_harga.format(harga);

        holder.harga.setText(harga2 + " " + "/" + " " + paketWisata.get(position).getSatuan());
        int id_paket_wisata = paketWisata.get(position).getId_paket_wisata();

        holder.btn_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(context, "id_wisata: " + id_paket_wisata, Toast.LENGTH_SHORT).show();
                Intent goToDetail = new Intent(context, DetailPaketWisataActivity.class);
                goToDetail.putExtra("id_paket_wisata", id_paket_wisata);
                context.startActivity(goToDetail);
            }
        });
        String url_image = myIPAddress;
        Picasso.with(context)
                .load(url_image + paketWisata.get(position).getGambar())
                .into(holder.img_wisata);
    }

    @Override
    public int getItemCount() {
        return paketWisata.size();
    }
}
