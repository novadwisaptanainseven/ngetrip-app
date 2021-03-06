package com.novadwisapta.ngetrip.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.novadwisapta.ngetrip.R;
import com.novadwisapta.ngetrip.model.MyTicket;

import java.util.ArrayList;

public class TicketAdapter extends RecyclerView.Adapter<TicketAdapter.MyViewHolder> {

    Context context;
    ArrayList<MyTicket> myTicket;

    public TicketAdapter(Context c, ArrayList<MyTicket> p)
    {
        context = c;
        myTicket = p;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_myticket, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i)
    {
        myViewHolder.xnama_wisata.setText(myTicket.get(i).getNama_wisata());
        myViewHolder.xlokasi.setText(myTicket.get(i).getLokasi());
        myViewHolder.xjumlah_tiket.setText(myTicket.get(i).getJumlah_tiket() + " Ticket");

        final String getNamaWisata = myTicket.get(i).getNama_wisata();

        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Click it : " + i, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return myTicket.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView xnama_wisata, xlokasi, xjumlah_tiket;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

//            xnama_wisata = itemView.findViewById(R.id.xnama_wisata);
//            xlokasi = itemView.findViewById(R.id.xlokasi);
//            xjumlah_tiket = itemView.findViewById(R.id.xjumlah_tiket);
        }
    }

}
