package com.novadwisapta.ngetrip.MyViewHolders;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.novadwisapta.ngetrip.R;

import org.jetbrains.annotations.NotNull;

public class AgentTravelViewHolder extends RecyclerView.ViewHolder {
    public TextView nama_agent_travel,
                    alamat;
    public ImageView img_agent_travel;
    public Button btn_detail;

    public AgentTravelViewHolder(@NonNull @NotNull View itemView) {
        super(itemView);

        nama_agent_travel = itemView.findViewById(R.id.nama_agent_travel);
        alamat = itemView.findViewById(R.id.alamat);
        img_agent_travel = itemView.findViewById(R.id.img_agent_travel);
        btn_detail = itemView.findViewById(R.id.btn_detail);
    }
}
