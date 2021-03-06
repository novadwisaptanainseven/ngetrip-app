package com.novadwisapta.ngetrip.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.novadwisapta.ngetrip.AgentTravelActivity;
import com.novadwisapta.ngetrip.MyViewHolders.AgentTravelViewHolder;
import com.novadwisapta.ngetrip.MyConfiguration;
import com.novadwisapta.ngetrip.R;
import com.novadwisapta.ngetrip.model.AgentTravel;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class AgentTravelAdapter extends RecyclerView.Adapter<AgentTravelViewHolder> {
    Context context;
    ArrayList<AgentTravel> agentTravels;
    MyConfiguration myConfiguration;
    String myIPAddress = "";

    public AgentTravelAdapter(Context context, ArrayList<AgentTravel> agentTravels) {
        this.context = context;
        this.agentTravels = agentTravels;

        // Set Configuration for API Server
        myConfiguration = new MyConfiguration();
        myIPAddress = myConfiguration.getIPAddressForFile();
    }

    @NonNull
    @NotNull
    @Override
    public AgentTravelViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_agent_travel, parent, false);

        return new AgentTravelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull AgentTravelViewHolder holder, int position) {
        holder.nama_agent_travel.setText(agentTravels.get(position).getNama_agent_travel());
        holder.alamat.setText(agentTravels.get(position).getAlamat());

        String url_image = myIPAddress;
        Picasso.with(context)
                .load(url_image + agentTravels.get(position).getGambar())
                .into(holder.img_agent_travel);

        // Tombol menuju detail agent travel
        holder.btn_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Toast.makeText(context, "id_agent_travel: " + agentTravels.get(position).getId_agent_travel(), Toast.LENGTH_SHORT).show();
                Intent goToDetail = new Intent(context, AgentTravelActivity.class);
                goToDetail.putExtra("id_agent_travel", agentTravels.get(position).getId_agent_travel());
                context.startActivity(goToDetail);
            }
        });
    }

    @Override
    public int getItemCount() {
        return agentTravels.size();
    }
}
