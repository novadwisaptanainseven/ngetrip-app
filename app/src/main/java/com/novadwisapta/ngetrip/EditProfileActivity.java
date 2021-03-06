package com.novadwisapta.ngetrip;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class EditProfileActivity extends AppCompatActivity {

    ImageView photo_edit_profile;
    EditText xnama_lengkap, xbio, xusername, xpassword, xemail_address;


    Uri photo_location;
    Integer photo_max = 1;

    DatabaseReference reference;
    StorageReference storage;

    String USERNAME_KEY = "username_key";
    String username_key = "";
    String username_key_new = "";

    Button btn_save, btn_add_new_photo;
    LinearLayout btn_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        photo_edit_profile = findViewById(R.id.photo_edit_profile);
        btn_add_new_photo = findViewById(R.id.btn_add_new_photo);

        btn_save = findViewById(R.id.btn_save);
        btn_back = findViewById(R.id.btn_back);

        xnama_lengkap = findViewById(R.id.xnama_lengkap);
        xbio = findViewById(R.id.xbio);
        xusername = findViewById(R.id.xusername);
        xpassword = findViewById(R.id.xpassword);
        xemail_address = findViewById(R.id.xemail_address);

        getUsernameLocal();

        reference = FirebaseDatabase.getInstance().getReference()
                .child("Users").child(username_key_new);
        storage = FirebaseStorage.getInstance().getReference().child("PhotoUsers").child(username_key_new);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                xnama_lengkap.setText(snapshot.child("nama_lengkap").getValue().toString());
                xbio.setText(snapshot.child("bio").getValue().toString());
                xusername.setText(snapshot.child("username").getValue().toString());
                xpassword.setText(snapshot.child("password").getValue().toString());
                xemail_address.setText(snapshot.child("email_address").getValue().toString());
                Picasso.with(EditProfileActivity.this)
                        .load(snapshot.child("url_photo_profile").getValue().toString())
                        .centerCrop().fit()
                        .into(photo_edit_profile);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Ubah state menjadi loading
                btn_save.setEnabled(false);
                btn_save.setText("Loading ...");

                String username = xusername.getText().toString();
                String nama_lengkap = xnama_lengkap.getText().toString();
                String password = xpassword.getText().toString();
                String email_address = xemail_address.getText().toString();
                String bio = xbio.getText().toString();

                if(username.isEmpty())
                {
                    btn_save.setEnabled(true);
                    btn_save.setText("Simpan");
                    Toast.makeText(EditProfileActivity.this, "Username harus diisi!", Toast.LENGTH_SHORT).show();
                }
                else if(nama_lengkap.isEmpty())
                {
                    btn_save.setEnabled(true);
                    btn_save.setText("Simpan");
                    Toast.makeText(EditProfileActivity.this, "Nama Lengkap harus diisi!", Toast.LENGTH_SHORT).show();
                }
                else if(password.isEmpty())
                {
                    btn_save.setEnabled(true);
                    btn_save.setText("Simpan");
                    Toast.makeText(EditProfileActivity.this, "Password harus diisi!", Toast.LENGTH_SHORT).show();
                }
                else if(email_address.isEmpty())
                {
                    btn_save.setEnabled(true);
                    btn_save.setText("Simpan");
                    Toast.makeText(EditProfileActivity.this, "Email Address harus diisi!", Toast.LENGTH_SHORT).show();
                }
                else if(bio.isEmpty())
                {
                    btn_save.setEnabled(true);
                    btn_save.setText("Simpan");
                    Toast.makeText(EditProfileActivity.this, "Bio harus diisi!", Toast.LENGTH_SHORT).show();
                }
                else {
                    simpanProfile(username, nama_lengkap, password, email_address, bio);
                }
            }
        });

        btn_add_new_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findPhoto();
            }
        });
    }

    public void simpanProfile(String username, String nama_lengkap, String password, String email_address, String bio)
    {
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                snapshot.getRef().child("username").setValue(username);
                snapshot.getRef().child("nama_lengkap").setValue(nama_lengkap);
                snapshot.getRef().child("password").setValue(password);
                snapshot.getRef().child("email_address").setValue(email_address);
                snapshot.getRef().child("bio").setValue(bio);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(EditProfileActivity.this, "Database Error: " + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        // Validasi untuk file apakah ada
        if(photo_location != null) {
            StorageReference storageReference1 = storage.child(System.currentTimeMillis() + "." + getFileExtension(photo_location));

            storageReference1.putFile(photo_location)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            storageReference1.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String uri_photo = uri.toString();
                                    reference.getRef().child("url_photo_profile").setValue(uri_photo);
                                }
                            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    Intent goBackToProfile = new Intent(EditProfileActivity.this, MyProfileActivity.class);
                                    startActivity(goBackToProfile);
                                }
                            });
                        }
                    });
        }
        else
        {
            btn_save.setEnabled(true);
            btn_save.setText("SIMPAN");
            Toast.makeText(this, "Photo belum ada!", Toast.LENGTH_SHORT).show();
        }
    }

    String getFileExtension(Uri uri)
    {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    public void findPhoto() {
        Intent pic = new Intent();
        pic.setType("image/*");
        pic.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(pic, photo_max);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == photo_max && resultCode == RESULT_OK && data != null && data.getData() != null)
        {
            photo_location = data.getData();
            Picasso.with(this).load(photo_location).centerCrop().fit().into(photo_edit_profile);
        }
    }

    public void getUsernameLocal() {
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
        username_key_new = sharedPreferences.getString(username_key, "");
    }
}



















