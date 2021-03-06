package com.novadwisapta.ngetrip;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.novadwisapta.ngetrip.interfaces.NgetripRestApi;
import com.novadwisapta.ngetrip.model.DeskripsiPaketWisata;
import com.novadwisapta.ngetrip.model.ExcludePaketWisata;
import com.novadwisapta.ngetrip.model.IncludePaketWisata;
import com.novadwisapta.ngetrip.model.PaketWisata;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.Currency;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DetailPaketWisataActivity extends Activity {

    TextView txtNamaPaketWisata,
                     txtNamaAgentTravel,
                     txtHarga,
                     txtMeetingPoint,
                     txtDeskripsi,
                     txtInclude,
                     txtExclude;
    ImageView imgPaketWisata,
              imgAgentTravel;
    Button btnBeli;
    NgetripRestApi ngetripRestApi;
    MyConfiguration myConfiguration;
    String myIPAddress = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_paket_wisata);

        // Set Configuration for API Server
        myConfiguration = new MyConfiguration();
        myIPAddress = myConfiguration.getIPAddress();

        txtNamaPaketWisata = findViewById(R.id.txt_nama_paket_wisata);
        txtNamaAgentTravel = findViewById(R.id.txt_nama_agent_travel);
        txtHarga = findViewById(R.id.txt_harga);
        txtMeetingPoint = findViewById(R.id.txt_meeting_point);
        txtDeskripsi = findViewById(R.id.txt_deskripsi);
        txtInclude = findViewById(R.id.txt_include);
        txtExclude = findViewById(R.id.txt_exclude);
        imgPaketWisata = findViewById(R.id.img_paket_wisata);
        imgAgentTravel = findViewById(R.id.img_agent_travel);
        btnBeli = findViewById(R.id.btn_beli);



        // Get ID Paket Wisata
        int idPaketWisata = 0;
        Bundle extras = getIntent().getExtras();

        if(extras != null)
        {
            idPaketWisata = (int) extras.get("id_paket_wisata");
        }

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
        // Get Deskripsi Wisata
        getDeskripsiWisata(idPaketWisata);
        // Get Include Wisata
        getIncludeWisata(idPaketWisata);
        // Get Exclude Wisata
        getExcludeWisata(idPaketWisata);

        // Go To Checkout Paket Wisata
        int finalIdPaketWisata = idPaketWisata;
        btnBeli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToCheckOut = new Intent(DetailPaketWisataActivity.this, CheckOutActivity.class);
                goToCheckOut.putExtra("id_paket_wisata", finalIdPaketWisata);
                startActivity(goToCheckOut);
            }
        });
    }

    private void getExcludeWisata(int idPaketWisata) {
        Call<List<ExcludePaketWisata>> call = ngetripRestApi.getExcludeWisata(idPaketWisata);

        call.enqueue(new Callback<List<ExcludePaketWisata>>() {
            @Override
            public void onResponse(Call<List<ExcludePaketWisata>> call, Response<List<ExcludePaketWisata>> response) {
                if (!response.isSuccessful())
                {
                    // Jika gagal
                    Toast.makeText(DetailPaketWisataActivity.this, "Calling API of Exclude Wisata is failed, Code: " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                // Jika berhasil
                List<ExcludePaketWisata> excludePaketWisata = response.body();
                for (ExcludePaketWisata exc : excludePaketWisata)
                {
                    String exclude = "";
                    exclude += "- " + exc.getExclude() + "\n";

                    txtExclude.append(exclude);
                }
            }

            @Override
            public void onFailure(Call<List<ExcludePaketWisata>> call, Throwable t) {
                Toast.makeText(DetailPaketWisataActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getIncludeWisata(int idPaketWisata) {
        Call<List<IncludePaketWisata>> call = ngetripRestApi.getIncludeWisata(idPaketWisata);

        call.enqueue(new Callback<List<IncludePaketWisata>>() {
            @Override
            public void onResponse(Call<List<IncludePaketWisata>> call, Response<List<IncludePaketWisata>> response) {
                if (!response.isSuccessful())
                {
                    // Jika gagal
                    Toast.makeText(DetailPaketWisataActivity.this, "Calling API of Include Wisata is failed, Code: " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                // Jika berhasil
                List<IncludePaketWisata> includePaketWisata = response.body();
                for (IncludePaketWisata inc : includePaketWisata)
                {
                    String include = "";
                    include += "- " + inc.getInclude() + "\n";

                    txtInclude.append(include);
                }
            }

            @Override
            public void onFailure(Call<List<IncludePaketWisata>> call, Throwable t) {
                Toast.makeText(DetailPaketWisataActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getDeskripsiWisata(int idPaketWisata) {
        Call<List<DeskripsiPaketWisata>> call = ngetripRestApi.getDeskripsiWisata(idPaketWisata);

        call.enqueue(new Callback<List<DeskripsiPaketWisata>>() {
            @Override
            public void onResponse(Call<List<DeskripsiPaketWisata>> call, Response<List<DeskripsiPaketWisata>> response) {
                if (!response.isSuccessful())
                {
                    // Jika gagal
                    Toast.makeText(DetailPaketWisataActivity.this, "Calling API of Deskripsi is failed, Code: " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }

                // Jika berhasil
                List<DeskripsiPaketWisata> deskripsiPaketWisata = response.body();
                for (DeskripsiPaketWisata desc : deskripsiPaketWisata)
                {
                    String deskripsi = "";
                    deskripsi += "Hari ke-" + desc.getHari_ke() + " " + ":" + " " + desc.getKeterangan() + "\n";

                    txtDeskripsi.append(deskripsi);
                }
            }

            @Override
            public void onFailure(Call<List<DeskripsiPaketWisata>> call, Throwable t) {
                Toast.makeText(DetailPaketWisataActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getPaketWisataById(int idPaketWisata) {
        
        Call<PaketWisata> call = ngetripRestApi.getPaketWisataById(idPaketWisata);

        call.enqueue(new Callback<PaketWisata>() {
            @Override
            public void onResponse(Call<PaketWisata> call, Response<PaketWisata> response) {
                if(!response.isSuccessful())
                {
                    Toast.makeText(DetailPaketWisataActivity.this, "Calling API of Paket Wisata is failed, Code: " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }

                PaketWisata paketWisata = response.body();

                txtNamaPaketWisata.setText(paketWisata.getNama_paket_wisata());
                txtNamaAgentTravel.setText(paketWisata.getNama_agent_travel());
                txtMeetingPoint.setText(paketWisata.getMeeting_point());
                // Format Harga dalam bentuk Rupiah
                NumberFormat numberFormat = NumberFormat.getCurrencyInstance();
                numberFormat.setMaximumFractionDigits(0);
                numberFormat.setCurrency(Currency.getInstance("IDR"));
                String harga = numberFormat.format(paketWisata.getHarga());
                txtHarga.setText(harga + " " + "/" + " " + paketWisata.getSatuan());

                String url_image = myConfiguration.getIPAddressForFile();

                Picasso.with(DetailPaketWisataActivity.this)
                        .load(url_image + paketWisata.getGambar())
                        .into(imgPaketWisata);

                Picasso.with(DetailPaketWisataActivity.this)
                        .load(url_image + paketWisata.getGambarAgentTravel())
                        .into(imgAgentTravel);
            }

            @Override
            public void onFailure(Call<PaketWisata> call, Throwable t) {
                Toast.makeText(DetailPaketWisataActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}


















