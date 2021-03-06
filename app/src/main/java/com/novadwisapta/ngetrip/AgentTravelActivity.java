package com.novadwisapta.ngetrip;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.novadwisapta.ngetrip.adapter.PaketWisataAdapter;
import com.novadwisapta.ngetrip.interfaces.NgetripRestApi;
import com.novadwisapta.ngetrip.model.AgentTravel;
import com.novadwisapta.ngetrip.model.KontakAgentTravel;
import com.novadwisapta.ngetrip.model.PaketWisata;
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

public class AgentTravelActivity extends AppCompatActivity {

    TextView txtNamaAgentTravel;
    TextView txtAlamat;
    TextView txtKontak;
    TextView txtDeskripsi;
    ImageView imgAgentTravel;
    TextView btnPrev;
    NgetripRestApi ngetripRestApi;
    MyConfiguration myConfiguration;
    RecyclerView listPaketWisata;
    ArrayList<PaketWisata> paketWisatas;
    PaketWisataAdapter paketWisataAdapter;
    String myIPAddress = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agent_travel2);

        // Set Configuration for API Server
        myConfiguration = new MyConfiguration();
        myIPAddress = myConfiguration.getIPAddress();

        txtNamaAgentTravel = findViewById(R.id.txt_nama_agent_travel);
        txtAlamat = findViewById(R.id.txt_alamat);
        txtKontak = findViewById(R.id.txt_kontak);
        txtDeskripsi = findViewById(R.id.txt_deskripsi);
        btnPrev = findViewById(R.id.btnPrev);
        imgAgentTravel = findViewById(R.id.img_agent_travel);

        listPaketWisata = findViewById(R.id.listPaketWisata);
        listPaketWisata.setLayoutManager(new LinearLayoutManager(this));
        paketWisatas = new ArrayList<>();

        // Get ID Agent Travel
        int idAgentTravel = 0;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            idAgentTravel = (int) extras.get("id_agent_travel");
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

        // Get Agent Travel By ID
        getAgentTravelById(idAgentTravel);
        // Get Paket Wisata by Agent Travel
        getPaketWisataByAgentTravel(idAgentTravel);

        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void getPaketWisataByAgentTravel(int idAgentTravel) {
        Call<List<PaketWisata>> call = ngetripRestApi.getPaketWisataByAgentTravel(idAgentTravel);

        call.enqueue(new Callback<List<PaketWisata>>() {
            @Override
            public void onResponse(Call<List<PaketWisata>> call, Response<List<PaketWisata>> response) {
                if (!response.isSuccessful()) {
                    // Jika gagal
                    Toast.makeText(AgentTravelActivity.this, "Calling API of Paket Wisata is failed. Code: " + response.code(), Toast.LENGTH_LONG).show();
                    return;
                }

                // Jika berhasil
                List<PaketWisata> paketWisatas1 = response.body();
                paketWisatas.addAll(paketWisatas1);
                paketWisataAdapter = new PaketWisataAdapter(AgentTravelActivity.this, paketWisatas);
                listPaketWisata.setAdapter(paketWisataAdapter);
            }

            @Override
            public void onFailure(Call<List<PaketWisata>> call, Throwable t) {
                Toast.makeText(AgentTravelActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getAgentTravelById(int idAgentTravel) {
        Call<AgentTravel> call = ngetripRestApi.getAgentTravelById(idAgentTravel);

        call.enqueue(new Callback<AgentTravel>() {
            @Override
            public void onResponse(Call<AgentTravel> call, Response<AgentTravel> response) {
                if (!response.isSuccessful()) {
                    // Jika gagal
                    Toast.makeText(AgentTravelActivity.this, "Calling API of Agent Travel is failed, code:" + response.code(), Toast.LENGTH_LONG).show();
                    return;
                }
                // Jika berhasil
                AgentTravel agentTravel = response.body();

                txtNamaAgentTravel.setText(agentTravel.getNama_agent_travel());
                txtAlamat.setText(agentTravel.getAlamat());
                txtDeskripsi.setText(agentTravel.getDeskripsi());
                List<KontakAgentTravel> kontakAgentTravels = agentTravel.getKontak();

                for (KontakAgentTravel kontak : kontakAgentTravels) {
                    String content = "";

                    content = "- " + kontak.getNama() + " :" + " " + kontak.getKontak() + "\n";

                    txtKontak.append(content);
                }

                Picasso.with(AgentTravelActivity.this)
                        .load(myIPAddress + "file/" + agentTravel.getGambar())
                        .into(imgAgentTravel);
            }

            @Override
            public void onFailure(Call<AgentTravel> call, Throwable t) {
                Toast.makeText(AgentTravelActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}







