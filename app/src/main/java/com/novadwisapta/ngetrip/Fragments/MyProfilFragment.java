package com.novadwisapta.ngetrip.Fragments;

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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.novadwisapta.ngetrip.EditProfileActivity;
import com.novadwisapta.ngetrip.GetStartedActivity;
import com.novadwisapta.ngetrip.MyConfiguration;
import com.novadwisapta.ngetrip.MyProfileActivity;
import com.novadwisapta.ngetrip.R;
import com.novadwisapta.ngetrip.adapter.TicketAdapter;
import com.novadwisapta.ngetrip.adapter.TicketWisataAdapter;
import com.novadwisapta.ngetrip.interfaces.NgetripRestApi;
import com.novadwisapta.ngetrip.model.MyTicket;
import com.novadwisapta.ngetrip.model.TiketWisata;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyProfilFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyProfilFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MyProfilFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyProfilFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyProfilFragment newInstance(String param1, String param2) {
        MyProfilFragment fragment = new MyProfilFragment();
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

    LinearLayout item_my_ticket;
    Button btn_edit_profile, btn_back_home, btn_sign_out;

    TextView nama_lengkap, bio;
    ImageView photo_profile;

    DatabaseReference reference, reference2;

    String USERNAME_KEY = "username_key";
    String username_key = "";
    String username_key_new = "";

    RecyclerView listTiketWisata;
    ArrayList<TiketWisata> tiketWisata;
    TicketWisataAdapter ticketWisataAdapter;
    NgetripRestApi ngetripRestApi;
    MyConfiguration myConfiguration;
    String myIPAddress = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_my_profil, container, false);

        // Configuration IP Address
        myConfiguration = new MyConfiguration();
        myIPAddress = myConfiguration.getIPAddress();

        getUsernameLocal();

        item_my_ticket = v.findViewById(R.id.item_my_ticket);
        btn_edit_profile = v.findViewById(R.id.btn_edit_profile);
//        btn_back_home = v.findViewById(R.id.btn_back_home);
        btn_sign_out = v.findViewById(R.id.btn_sign_out);

        nama_lengkap = v.findViewById(R.id.nama_lengkap);
        bio = v.findViewById(R.id.bio);
        photo_profile = v.findViewById(R.id.photo_profile);

        listTiketWisata = v.findViewById(R.id.myticket_place);
        listTiketWisata.setLayoutManager(new LinearLayoutManager(getContext()));
        tiketWisata = new ArrayList<>();

        // Configuration Debugging
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(httpLoggingInterceptor)
                .build();

        // Pemanggilan API menggunakan Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(myIPAddress)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
        ngetripRestApi = retrofit.create(NgetripRestApi.class);

        // Get Tiket Paket Wisata by Username
        getTiketWisataByUsername();

        reference = FirebaseDatabase.getInstance().getReference()
                .child("Users").child(username_key_new);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                nama_lengkap.setText(snapshot.child("nama_lengkap").getValue().toString());
                bio.setText(snapshot.child("bio").getValue().toString());
                Picasso.with(getActivity())
                        .load(snapshot.child("url_photo_profile").getValue().toString())
                        .centerCrop().fit()
                        .into(photo_profile);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btn_edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToEditProfile = new Intent(getActivity(), EditProfileActivity.class);
                startActivity(goToEditProfile);
            }
        });

//        btn_back_home.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent goToHome = new Intent(getActivity(), MainActivity.class);
//                startActivity(goToHome);
//            }
//        });

        btn_sign_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Menghapus isi / nilai / value dari username local
                SharedPreferences sharedPreferences = getContext().getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(username_key, null);
                editor.apply();

                Toast.makeText(getContext(), "Sign Out Berhasil", Toast.LENGTH_SHORT).show();

                // Berpindah activity
                Intent goToGetStarted = new Intent(getActivity(), GetStartedActivity.class);
                startActivity(goToGetStarted);
                getActivity().finish();
            }
        });

        return v;
    }

    private void getTiketWisataByUsername() {
        Call<List<TiketWisata>> call = ngetripRestApi.getTiketWisataByUsername(username_key_new);

        call.enqueue(new Callback<List<TiketWisata>>() {
            @Override
            public void onResponse(Call<List<TiketWisata>> call, Response<List<TiketWisata>> response) {
                if (!response.isSuccessful()) {
                    // Jika gagal
                    Toast.makeText(getContext(), "Calling API of Tiket Wisata is failed. Code: " + response.code(), Toast.LENGTH_LONG).show();
                }
                // Jika berhasil
                List<TiketWisata> tiketWisataList = response.body();
                tiketWisata.addAll(tiketWisataList);
                ticketWisataAdapter = new TicketWisataAdapter(getContext(), tiketWisata);
                listTiketWisata.setAdapter(ticketWisataAdapter);
            }

            @Override
            public void onFailure(Call<List<TiketWisata>> call, Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void getUsernameLocal() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
        username_key_new = sharedPreferences.getString(username_key, "");
    }
}