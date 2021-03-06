package com.novadwisapta.ngetrip;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.novadwisapta.ngetrip.interfaces.NgetripRestApi;
import com.novadwisapta.ngetrip.model.PaketWisata;
import com.novadwisapta.ngetrip.model.TiketWisata;

import org.jetbrains.annotations.NotNull;

import java.text.NumberFormat;
import java.util.Currency;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CheckOutActivity extends AppCompatActivity {

    DatePicker tglPaket;
    Button btnBeliPaket, btnMines, btnPlus;
    LinearLayout btnBack;
    TextView txtJumlahPaket, txtTotalHarga, txtSaldo, txtNamaWisata, txtNamaAgentTravel, txtMeetingPoint;
    Integer valueJumlahPaket = 1;
    Integer saldoSaya = 0;
    Integer valueTotalHarga = 0;
    Integer valueHargaPaket = 0;
    ImageView noticeUang;
    Integer sisaSaldo = 0;

    DatabaseReference reference, reference2, reference3, reference4;

    String USERNAME_KEY = "username_key";
    String username_key = "";
    String username_key_new = "";

    String dateWisata = "";
    String timeWisata = "";

    // Generate nomor integer secara random
    // Untuk membuat transaksi secara unik
    Integer nomorTransaksi = new Random().nextInt();

    NgetripRestApi ngetripRestApi;
    MyConfiguration myConfiguration;
    String myIPAddress = "";
    ProgressDialog progressDialog;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);

        // Get My IP Address
        myConfiguration = new MyConfiguration();
        myIPAddress = myConfiguration.getIPAddress();

        // Progress Dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Harap tunggu\nProses pembelian sedang berlangsung");

        getUsernameLocal();

        // Get ID Paket Wisata
        int idPaketWisata = 0;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            idPaketWisata = (int) extras.get("id_paket_wisata");
        }

        // Inisialisasi Component
        btnBack = findViewById(R.id.btn_back);
        btnMines = findViewById(R.id.btnmines);
        btnPlus = findViewById(R.id.btnplus);
        txtJumlahPaket = findViewById(R.id.txt_jumlah_paket);
        btnBeliPaket = findViewById(R.id.btn_beli_paket);
        noticeUang = findViewById(R.id.notice_uang);
        txtNamaWisata = findViewById(R.id.txt_nama_wisata);
        txtNamaAgentTravel = findViewById(R.id.txt_nama_agent_travel);
        txtMeetingPoint = findViewById(R.id.txt_meeting_point);
        txtTotalHarga = findViewById(R.id.txt_total_harga);
        txtSaldo = findViewById(R.id.txt_saldo);
        tglPaket = findViewById(R.id.tglPaket);

        // Setting value untuk beberapa komponent
        txtJumlahPaket.setText(valueJumlahPaket.toString());

        // Secara default, btnmines di-hide
        btnMines.animate().alpha(0).setDuration(300).start();
        btnMines.setEnabled(false);
        noticeUang.setVisibility(View.GONE);

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

        // Get Paket Wisata By ID
        getPaketWisataById(idPaketWisata);

        // Mengambil data user dari firebase
        reference2 = FirebaseDatabase.getInstance().getReference().child("Users").child(username_key_new);
        reference2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                saldoSaya = Integer.valueOf(snapshot.child("user_balance").getValue().toString());
                txtSaldo.setText("Rp. " + saldoSaya);

                if (saldoSaya < valueHargaPaket) {
                    btnBeliPaket.animate().translationY(250)
                            .alpha(0).setDuration(350).start();
                    btnBeliPaket.setEnabled(false);
                    txtSaldo.setTextColor(Color.parseColor("#D1206B"));
                    noticeUang.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        // Menangani tombol plus untuk menambah quantity paket
        btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                valueJumlahPaket += 1;
                txtJumlahPaket.setText(valueJumlahPaket.toString());
                if (valueJumlahPaket > 0) {
                    btnMines.animate().alpha(1).setDuration(300).start();
                    btnMines.setEnabled(true);
                }
                valueTotalHarga = valueHargaPaket * valueJumlahPaket;
                txtTotalHarga.setText("Rp. " + valueTotalHarga);
                if (valueTotalHarga > saldoSaya) {
                    btnBeliPaket.animate().translationY(250)
                            .alpha(0).setDuration(350).start();
                    btnBeliPaket.setEnabled(false);
                    txtSaldo.setTextColor(Color.parseColor("#D1206B"));
                    noticeUang.setVisibility(View.VISIBLE);
                }
            }
        });

        // Menangani tombol minus untuk mengurangi quantity paket
        btnMines.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                valueJumlahPaket -= 1;
                txtJumlahPaket.setText(valueJumlahPaket.toString());
                if (valueJumlahPaket < 2) {
                    btnMines.animate().alpha(0).setDuration(300).start();
                    btnMines.setEnabled(false);
                }
                valueTotalHarga = valueHargaPaket * valueJumlahPaket;
                txtTotalHarga.setText("Rp. " + valueTotalHarga);
                if (valueTotalHarga < saldoSaya) {
                    btnBeliPaket.animate().translationY(0)
                            .alpha(1).setDuration(350).start();
                    btnBeliPaket.setEnabled(true);
                    txtSaldo.setTextColor(Color.parseColor("#203DD1"));
                    noticeUang.setVisibility(View.GONE);
                }
            }
        });

        // Menangani tombol back
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        // Menangani tombol beli paket wisata
        int finalIdPaketWisata = idPaketWisata;
        btnBeliPaket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();

                // Update saldo user saat ini hasil pembelian paket wisata
                updateSaldo();

                // Get tanggal paket
                String tglPaket2 = tglPaket.getYear() + "-" + (tglPaket.getMonth() + 1) + "-" + tglPaket.getDayOfMonth();
                // Get Kode Transaksi
                long tsLong = System.currentTimeMillis() / 1000;
                String ts = Long.toString(tsLong);
                String kodeTransaksi = "TPW" + ts;

                Map<String, String> fields = new HashMap<>();
                fields.put("id_paket_wisata", String.valueOf(finalIdPaketWisata));
                fields.put("username", username_key_new);
                fields.put("tgl_tiket", tglPaket2);
                fields.put("total_tiket", String.valueOf(valueJumlahPaket));
                if (valueTotalHarga > 0) {
                    fields.put("total_harga", String.valueOf(valueTotalHarga));
                } else {
                    fields.put("total_harga", String.valueOf(valueHargaPaket));
                }
                fields.put("kode_transaksi", kodeTransaksi);

                Call<TiketWisata> call = ngetripRestApi.beliPaketWisata(fields);

                call.enqueue(new Callback<TiketWisata>() {
                    @Override
                    public void onResponse(Call<TiketWisata> call, Response<TiketWisata> response) {
                        progressDialog.dismiss();

                        if (!response.isSuccessful()) {
                            // Jika Gagal
                            Toast.makeText(CheckOutActivity.this, "Beli Paket Wisata Gagal. Code: " + response.code(), Toast.LENGTH_LONG).show();
                            return;
                        }
                        // Jika berhasil
                        Intent goToSuksesBeli = new Intent(CheckOutActivity.this, SuksesBeliPaketActivity.class);
                        startActivity(goToSuksesBeli);
                    }

                    @Override
                    public void onFailure(Call<TiketWisata> call, Throwable t) {
                        Toast.makeText(CheckOutActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        });


    }

    private void updateSaldo() {
        reference3 = FirebaseDatabase.getInstance().getReference().child("Users").child(username_key_new);
        reference3.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                int valueHarga = 0;
                if (valueTotalHarga > 0) {
                    valueHarga = valueTotalHarga;
                } else {
                    valueHarga = valueHargaPaket;
                }
                sisaSaldo = saldoSaya - valueHarga;
                reference3.getRef().child("user_balance").setValue(sisaSaldo);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    private void getPaketWisataById(int idPaketWisata) {
        Call<PaketWisata> call = ngetripRestApi.getPaketWisataById(idPaketWisata);

        call.enqueue(new Callback<PaketWisata>() {
            @Override
            public void onResponse(Call<PaketWisata> call, Response<PaketWisata> response) {
                if (!response.isSuccessful()) {
                    // Jika gagal
                    Toast.makeText(CheckOutActivity.this, "Calling API of Paket Wisata is failed, code: " + response.code(), Toast.LENGTH_LONG).show();
                    return;
                }
                // Jika berhasil
                PaketWisata paketWisata = response.body();
                valueHargaPaket = paketWisata.getHarga();
                txtNamaWisata.setText(paketWisata.getNama_paket_wisata());
                txtNamaAgentTravel.setText(paketWisata.getNama_agent_travel());
                txtMeetingPoint.setText(paketWisata.getMeeting_point());
                txtTotalHarga.setText("Rp. " + valueHargaPaket.toString());
            }

            @Override
            public void onFailure(Call<PaketWisata> call, Throwable t) {
                Toast.makeText(CheckOutActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getUsernameLocal() {
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
        username_key_new = sharedPreferences.getString(username_key, "");
    }
}