package com.novadwisapta.ngetrip;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class AddFundActivity extends AppCompatActivity {
    ImageView photo_profile;
    TextView nama_lengkap, bio, user_balance;
    EditText input_dana;
    Button btn_add_fund, btn_minus_fund, btn_goToMenuUtama;
    int dana;

    String USERNAME_KEY = "username_key";
    String username_key = "";
    String username_key_new = "";

    DatabaseReference reference;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_fund);

        getUsernameLocal();

        btn_goToMenuUtama = findViewById(R.id.btn_goToMenuUtama);
        photo_profile = findViewById(R.id.photo_profile);
        nama_lengkap = findViewById(R.id.nama_lengkap);
        bio = findViewById(R.id.bio);
        user_balance = findViewById(R.id.user_balance);
        input_dana = findViewById(R.id.input_dana);

        btn_add_fund = findViewById(R.id.btn_add_fund);
        btn_minus_fund = findViewById(R.id.btn_minus_fund);

        // Progress Dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);

        // Menuju ke menu utama
        btn_goToMenuUtama.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToMenuUtama = new Intent(AddFundActivity.this, HomeActivity.class);
                startActivity(goToMenuUtama);
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
                dana = Integer.parseInt(snapshot.child("user_balance").getValue().toString());
                Picasso.with(AddFundActivity.this)
                        .load(snapshot.child("url_photo_profile").getValue().toString())
                        .centerCrop().fit()
                        .into(photo_profile);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AddFundActivity.this, "Database Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        // Tambah Dana
        btn_add_fund.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tambahDana();
            }
        });
        
        // Tarik Dana
        btn_minus_fund.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tarikDana();
            }
        });
    }

    public void tambahDana() {
        progressDialog.show();

        String userBalance = input_dana.getText().toString();

        if(userBalance.isEmpty())
        {
            progressDialog.dismiss();
            Toast.makeText(this, "Dana harus diisi!", Toast.LENGTH_SHORT).show();
        }
        else
        {
            int userBalance2 = Integer.parseInt(userBalance);


            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    //int totalDana = userBalance2 + dana;
                    int dana = Integer.parseInt(snapshot.child("user_balance").getValue().toString());
                    snapshot.getRef().child("user_balance").setValue(userBalance2 + dana).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            progressDialog.dismiss();
                            if(task.isSuccessful())
                            {
                                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        Toast.makeText(AddFundActivity.this, "Tambah Dana Berhasil", Toast.LENGTH_SHORT).show();
                                        user_balance.setText("Rp. " + snapshot.child("user_balance").getValue().toString());
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });


                            }
                        }
                    });

                    //dana = Integer.parseInt(snapshot.child("user_balance").getValue().toString());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }
    }

    public void tarikDana() {
        progressDialog.show();
        String userBalance = input_dana.getText().toString();

        if(userBalance.isEmpty())
        {
            Toast.makeText(this, "Dana harus diisi!", Toast.LENGTH_SHORT).show();
        }
        else
        {
            int userBalance2 = Integer.parseInt(userBalance);
                
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    int dana = Integer.parseInt(snapshot.child("user_balance").getValue().toString());
                    if(dana <= 0)
                    {
                        progressDialog.dismiss();
                        user_balance.setText("Rp. 0");
                        snapshot.getRef().child("user_balance").setValue(0);
                        Toast.makeText(AddFundActivity.this, "Dana telah habis, silahkan tambah dana", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        snapshot.getRef().child("user_balance").setValue(dana - userBalance2).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                progressDialog.dismiss();
                                if(task.isSuccessful())
                                {
                                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            Toast.makeText(AddFundActivity.this, "Tarik Dana Berhasil", Toast.LENGTH_SHORT).show();
                                            user_balance.setText("Rp. " + snapshot.child("user_balance").getValue().toString());
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                }
                            }
                        });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }
    }

    public void getUsernameLocal() {
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
        username_key_new = sharedPreferences.getString(username_key, "");
    }
}