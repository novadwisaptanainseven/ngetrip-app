package com.novadwisapta.ngetrip;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    TextView btn_new_account;
    Button btn_sign_in;
    EditText xusername, xpassword;

    DatabaseReference reference;

    String USERNAME_KEY = "username_key";
    String username_key = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btn_new_account = findViewById(R.id.btn_new_account);
        btn_sign_in = findViewById(R.id.btn_sign_in);
        xusername = findViewById(R.id.xusername);
        xpassword = findViewById(R.id.xpassword);

        btn_new_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToRegisterOne = new Intent(LoginActivity.this, RegisterOneActivity.class);
                startActivity(goToRegisterOne);
            }
        });

        btn_sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Ubah state menjadi loading
                btn_sign_in.setEnabled(false);
                btn_sign_in.setText("Loading...");

                final String username = xusername.getText().toString();
                final String password = xpassword.getText().toString();

                if(username.isEmpty())
                {
                    Toast.makeText(LoginActivity.this, "Username harus diisi!", Toast.LENGTH_SHORT).show();

                    btn_sign_in.setEnabled(true);
                    btn_sign_in.setText("Masuk");
                }
                else if(password.isEmpty())
                {
                    Toast.makeText(LoginActivity.this, "Password harus diisi!", Toast.LENGTH_SHORT).show();

                    btn_sign_in.setEnabled(true);
                    btn_sign_in.setText("Masuk");
                }
                else
                {
                    reference = FirebaseDatabase.getInstance().getReference()
                        .child("Users").child(username);
                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists())
                            {
                                // Ambil data password dari firebase
                                String passwordFromFirebase = snapshot.child("password").getValue().toString();

                                // Validasi password dengan password firebase
                                if(password.equals(passwordFromFirebase))
                                {

                                    // Simpan username (key) dalam local
                                    SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString(username_key,xusername.getText().toString());
                                    editor.apply();
                                    
                                    // Pesan Success Login
                                    Toast.makeText(LoginActivity.this, "Login Berhasil", Toast.LENGTH_SHORT).show();

                                    btn_sign_in.setEnabled(true);
                                    btn_sign_in.setText("Masuk");

                                    // Berpindah activity
                                    Intent goToHome = new Intent(LoginActivity.this, HomeActivity.class);
                                    startActivity(goToHome);
                                }
                                else
                                {
                                    Toast.makeText(LoginActivity.this, "Password salah, coba lagi!", Toast.LENGTH_SHORT).show();

                                    btn_sign_in.setEnabled(true);
                                    btn_sign_in.setText("Masuk");
                                }
                            }
                            else
                            {
                                Toast.makeText(LoginActivity.this, "Username salah, coba lagi!", Toast.LENGTH_SHORT).show();

                                btn_sign_in.setEnabled(true);
                                btn_sign_in.setText("Masuk");
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(LoginActivity.this, "Database Error : " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }
}



















