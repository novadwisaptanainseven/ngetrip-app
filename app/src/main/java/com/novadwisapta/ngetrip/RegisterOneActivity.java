package com.novadwisapta.ngetrip;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class RegisterOneActivity extends AppCompatActivity {

    LinearLayout btn_back;
    EditText username, password, email_address;
    DatabaseReference reference, reference_username, reference_email;
    Button btn_continue;

    String USERNAME_KEY = "username_key";
    String username_key = "";

    Boolean status_register = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_one);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        email_address = findViewById(R.id.email_address);

        btn_back = findViewById(R.id.btn_back);

        btn_continue = findViewById(R.id.btn_continue);
        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Ubah state menjadi loading
                btn_continue.setEnabled(false);
                btn_continue.setText("Loading...");

                String username2 = username.getText().toString();
                String password2 = password.getText().toString();
                String email_address2 = email_address.getText().toString();

                // Validasi form register
                if(username2.isEmpty())
                {
                    Toast.makeText(RegisterOneActivity.this, "Username harus diisi!", Toast.LENGTH_SHORT).show();

                    btn_continue.setEnabled(true);
                    btn_continue.setText("Lanjutkan");
                }
                else if(email_address2.isEmpty())
                {
                    Toast.makeText(RegisterOneActivity.this, "Email Address harus diisi!", Toast.LENGTH_SHORT).show();

                    btn_continue.setEnabled(true);
                    btn_continue.setText("Lanjutkan");
                }
                else if(password2.isEmpty())
                {
                    Toast.makeText(RegisterOneActivity.this, "Password harus diisi!", Toast.LENGTH_SHORT).show();

                    btn_continue.setEnabled(true);
                    btn_continue.setText("Lanjutkan");
                }
                else
                {
                    // Jika validasi berhasil
                    // Mengambil username pada firebase
                    reference_username = FirebaseDatabase.getInstance().getReference()
                            .child("Users").child(username.getText().toString());

                    reference_username.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {

                            // Jika email tersedia
                            if(snapshot.exists())
                            {
                                Toast.makeText(RegisterOneActivity.this, "Username sudah tersedia", Toast.LENGTH_SHORT).show();

                                // Ubah state menjadi loading active
                                btn_continue.setEnabled(true);
                                btn_continue.setText("LANJUTKAN");
                            }
                            else {
                                // Toast.makeText(RegisterOneActivity.this, "Snapshot: " + snapshot.child("username").getValue(), Toast.LENGTH_SHORT).show();
                                reference_email = FirebaseDatabase.getInstance().getReference()
                                        .child("Users");
                                reference_email.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        for(DataSnapshot mDataSnapshot : snapshot.getChildren())
                                        {
                                           String email = mDataSnapshot.child("email_address").getValue().toString();
                                           if(email_address2.equals(email))
                                           {
                                               status_register = false;
                                           }
                                        }

                                        if(!status_register)
                                        {
                                            Toast.makeText(RegisterOneActivity.this, "Email sudah ada!", Toast.LENGTH_SHORT).show();

                                            btn_continue.setEnabled(true);
                                            btn_continue.setText("Lanjutkan");
                                            status_register = true;
                                        }
                                        else
                                        {
                                            status_register = true;
                                            register();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                                // Validasi email
//                                if(!status_register)
//                                {
//                                    Toast.makeText(RegisterOneActivity.this, "Email sudah ada!", Toast.LENGTH_SHORT).show();
//
//                                    btn_continue.setEnabled(true);
//                                    btn_continue.setText("Lanjutkan");
//                                }
//                                else
//                                {
//                                    // Menyimpan data ke dalam local storage (handphone)
//                                    SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
//                                    SharedPreferences.Editor editor = sharedPreferences.edit();
//                                    editor.putString(username_key, username.getText().toString());
//                                    editor.apply();
//
//                                    // Simpan data akun ke dalam database users
//                                    reference = FirebaseDatabase.getInstance().getReference()
//                                            .child("Users").child(username.getText().toString());
//                                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
//                                        @Override
//                                        public void onDataChange(DataSnapshot snapshot) {
//                                            snapshot.getRef().child("username").setValue(username.getText().toString());
//                                            snapshot.getRef().child("password").setValue(password.getText().toString());
//                                            snapshot.getRef().child("email_address").setValue(email_address.getText().toString());
//                                            snapshot.getRef().child("user_balance").setValue(800);
//                                            snapshot.getRef().child("role").setValue("user");
//                                        }
//
//                                        @Override
//                                        public void onCancelled(DatabaseError error) {
//
//                                        }
//                                    });
//
//                                    // Berpindah activity
//                                    Intent gotonextregister = new Intent(RegisterOneActivity.this, RegisterTwoActivity.class);
//                                    startActivity(gotonextregister);
//                                }

                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError error) {
                            Toast.makeText(RegisterOneActivity.this, "Database error : " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void register() {
        // Menyimpan data ke dalam local storage (handphone)
                                    SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString(username_key, username.getText().toString());
                                    editor.apply();

                                    // Simpan data akun ke dalam database users
                                    reference = FirebaseDatabase.getInstance().getReference()
                                            .child("Users").child(username.getText().toString());
                                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot snapshot) {
                                            snapshot.getRef().child("username").setValue(username.getText().toString());
                                            snapshot.getRef().child("password").setValue(password.getText().toString());
                                            snapshot.getRef().child("email_address").setValue(email_address.getText().toString());
                                            snapshot.getRef().child("user_balance").setValue(800);
                                            snapshot.getRef().child("role").setValue("user");
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError error) {

                                        }
                                    });

                                    // Berpindah activity
                                    Intent gotonextregister = new Intent(RegisterOneActivity.this, RegisterTwoActivity.class);
                                    startActivity(gotonextregister);
    }
}