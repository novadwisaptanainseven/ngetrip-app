package com.novadwisapta.ngetrip.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.florent37.shapeofview.shapes.CircleView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.novadwisapta.ngetrip.AddFundActivity;
import com.novadwisapta.ngetrip.MyProfileActivity;
import com.novadwisapta.ngetrip.MyConfiguration;
import com.novadwisapta.ngetrip.R;
import com.novadwisapta.ngetrip.adapter.AgentTravelAdapter;
import com.novadwisapta.ngetrip.interfaces.NgetripRestApi;
import com.novadwisapta.ngetrip.model.AgentTravel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AgentTravelFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AgentTravelFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AgentTravelFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AgentTravelFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AgentTravelFragment newInstance(String param1, String param2) {
        AgentTravelFragment fragment = new AgentTravelFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    CircleView btn_to_profile;
    ImageView photo_home_user;
    TextView user_balance, nama_lengkap, bio;

    DatabaseReference reference;

    String USERNAME_KEY = "username_key";
    String username_key = "";
    String username_key_new = "";

    RecyclerView listAgentTravel;
    NgetripRestApi ngetripRestApi;
    ArrayList<AgentTravel> agentTravels;
    AgentTravelAdapter agentTravelAdapter;
    MyConfiguration myConfiguration;
    String myIPAddress = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_agent_travel, container, false);

        // Set Configuration for API Server
        myConfiguration = new MyConfiguration();
        myIPAddress = myConfiguration.getIPAddress();

        getUsernameLocal();

        listAgentTravel = v.findViewById(R.id.listAgentTravel);
        listAgentTravel.setLayoutManager(new LinearLayoutManager(getContext()));
        agentTravels = new ArrayList<>();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(myIPAddress)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ngetripRestApi = retrofit.create(NgetripRestApi.class);

        // Get All Agent Travel
        getAllAgentTravel();

        photo_home_user = v.findViewById(R.id.photo_home_user);
        user_balance = v.findViewById(R.id.user_balance);
        nama_lengkap = v.findViewById(R.id.nama_lengkap);
        btn_to_profile = v.findViewById(R.id.btn_to_profile);
        bio = v.findViewById(R.id.bio);

        // Menuju tambah dana
        user_balance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToTambahDana = new Intent(getActivity(), AddFundActivity.class);
                startActivity(goToTambahDana);
            }
        });

        reference = FirebaseDatabase.getInstance().getReference()
                .child("Users").child(username_key_new);

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                nama_lengkap.setText(snapshot.child("nama_lengkap").getValue().toString());
                bio.setText(snapshot.child("bio").getValue().toString());
                user_balance.setText("Rp. " + snapshot.child("user_balance").getValue().toString());
                int userBalance = Integer.parseInt(snapshot.child("user_balance").getValue().toString());
                Picasso.with(getContext())
                        .load(snapshot.child("url_photo_profile").getValue().toString())
                        .centerCrop().fit()
                        .into(photo_home_user);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // Go To Profile
        btn_to_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToMyProfile = new Intent(getActivity(), MyProfileActivity.class);
                startActivity(goToMyProfile);
            }
        });

        return v;
    }

    private void getAllAgentTravel() {
        Call<List<AgentTravel>> call = ngetripRestApi.getAllAgentTravel();

        call.enqueue(new Callback<List<AgentTravel>>() {
            @Override
            public void onResponse(Call<List<AgentTravel>> call, Response<List<AgentTravel>> response) {
                if(!response.isSuccessful())
                {
                    // Jika proses request tidak berhasil
                    // Maka tampilkan pesan berupa code response
                    Toast.makeText(getContext(), "Code : " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }

                List<AgentTravel> agentTravels2 = response.body();
                agentTravels.addAll(agentTravels2);
                agentTravelAdapter = new AgentTravelAdapter(getContext(), agentTravels);
                listAgentTravel.setAdapter(agentTravelAdapter);
            }

            @Override
            public void onFailure(Call<List<AgentTravel>> call, Throwable t) {
                Toast.makeText(getContext(), "Failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getUsernameLocal() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(USERNAME_KEY, Context.MODE_PRIVATE);
        username_key_new = sharedPreferences.getString(username_key, "");
    }
}