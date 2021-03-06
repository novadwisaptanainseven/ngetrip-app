package com.novadwisapta.ngetrip;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
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
import com.novadwisapta.ngetrip.adapter.TicketWisataAdapter;
import com.novadwisapta.ngetrip.interfaces.NgetripRestApi;
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

public class MyProfileActivity extends AppCompatActivity {

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        // Configuration IP Address
        myConfiguration = new MyConfiguration();
        myIPAddress = myConfiguration.getIPAddress();

        getUsernameLocal();

        item_my_ticket = findViewById(R.id.item_my_ticket);
        btn_edit_profile = findViewById(R.id.btn_edit_profile);
        btn_back_home = findViewById(R.id.btn_back_home);
        btn_sign_out = findViewById(R.id.btn_sign_out);

        nama_lengkap = findViewById(R.id.nama_lengkap);
        bio = findViewById(R.id.bio);
        photo_profile = findViewById(R.id.photo_profile);

        listTiketWisata = findViewById(R.id.myticket_place);
        listTiketWisata.setLayoutManager(new LinearLayoutManager(this));
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
                Picasso.with(MyProfileActivity.this)
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
                Intent goToEditProfile = new Intent(MyProfileActivity.this, EditProfileActivity.class);
                startActivity(goToEditProfile);
            }
        });

        btn_back_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToHome = new Intent(MyProfileActivity.this, HomeActivity.class);
                startActivity(goToHome);
//                onBackPressed();
            }
        });

        btn_sign_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Menghapus isi / nilai / value dari username local
                SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(username_key, null);
                editor.apply();

                Toast.makeText(MyProfileActivity.this, "Sign Out Berhasil", Toast.LENGTH_SHORT).show();

                // Berpindah activity
                Intent goToGetStarted = new Intent(MyProfileActivity.this, GetStartedActivity.class);
                startActivity(goToGetStarted);
                finish();
            }
        });
    }

    private void getTiketWisataByUsername() {
        Call<List<TiketWisata>> call = ngetripRestApi.getTiketWisataByUsername(username_key_new);

        call.enqueue(new Callback<List<TiketWisata>>() {
            @Override
            public void onResponse(Call<List<TiketWisata>> call, Response<List<TiketWisata>> response) {
                if (!response.isSuccessful()) {
                    // Jika gagal
                    Toast.makeText(MyProfileActivity.this, "Calling API of Tiket Wisata is failed. Code: " + response.code(), Toast.LENGTH_LONG).show();
                }
                // Jika berhasil
                List<TiketWisata> tiketWisataList = response.body();
                tiketWisata.addAll(tiketWisataList);
                ticketWisataAdapter = new TicketWisataAdapter(MyProfileActivity.this, tiketWisata);
                listTiketWisata.setAdapter(ticketWisataAdapter);
            }

            @Override
            public void onFailure(Call<List<TiketWisata>> call, Throwable t) {
                Toast.makeText(MyProfileActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void getUsernameLocal() {
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
        username_key_new = sharedPreferences.getString(username_key, "");
    }
}
















