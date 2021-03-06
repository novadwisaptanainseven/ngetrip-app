package com.novadwisapta.ngetrip;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.florent37.shapeofview.shapes.CircleView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {

    LinearLayout btn_ticket_pisa,
                 btn_ticket_torri,
                 btn_ticket_candi,
                 btn_ticket_sphinx,
                 btn_ticket_pagoda,
                 btn_ticket_monas;

    Dialog dialog;

    CircleView btn_to_profile;
    ImageView photo_home_user;
    TextView user_balance, nama_lengkap, bio;

    DatabaseReference reference;

    String USERNAME_KEY = "username_key";
    String username_key = "";
    String username_key_new = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        getUsernameLocal();

        // Dialog tambah dana
        dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.dialog_dana_tidak_cukup);
        dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.bg_layout_white));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
        Button btnGoToTambahDana = dialog.findViewById(R.id.btn_goToTambahDana);
        Button btnCloseDialog = dialog.findViewById(R.id.btn_close);

        btnGoToTambahDana.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                Intent goToTambahDana = new Intent(MainActivity.this, AddFundActivity.class);
                startActivity(goToTambahDana);
            }
        });

        btnCloseDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        btn_ticket_pisa = findViewById(R.id.btn_ticket_pisa);
        btn_to_profile = findViewById(R.id.btn_to_profile);

        btn_ticket_torri = findViewById(R.id.btn_ticket_torri);
        btn_ticket_pagoda = findViewById(R.id.btn_ticket_pagoda);
        btn_ticket_candi = findViewById(R.id.btn_ticket_candi);
        btn_ticket_sphinx = findViewById(R.id.btn_ticket_sphinx);
        btn_ticket_monas = findViewById(R.id.btn_ticket_monas);

        photo_home_user = findViewById(R.id.photo_home_user);
        user_balance = findViewById(R.id.user_balance);
        nama_lengkap = findViewById(R.id.nama_lengkap);
        bio = findViewById(R.id.bio);

        // Menuju tambah dana
        user_balance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToTambahDana = new Intent(MainActivity.this, AddFundActivity.class);
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
                Picasso.with(MainActivity.this)
                        .load(snapshot.child("url_photo_profile").getValue().toString())
                        .centerCrop().fit()
                        .into(photo_home_user);

                if (userBalance < 1000)
                {
                    dialog.show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btn_to_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToMyProfile = new Intent(MainActivity.this, MyProfileActivity.class);
                startActivity(goToMyProfile);
            }
        });
    }

    public void getUsernameLocal() {
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
        username_key_new = sharedPreferences.getString(username_key, "");
    }
}