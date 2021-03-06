package com.novadwisapta.ngetrip.interfaces;

import com.novadwisapta.ngetrip.model.AgentTravel;
import com.novadwisapta.ngetrip.model.DeskripsiPaketWisata;
import com.novadwisapta.ngetrip.model.ExcludePaketWisata;
import com.novadwisapta.ngetrip.model.IncludePaketWisata;
import com.novadwisapta.ngetrip.model.PaketWisata;
import com.novadwisapta.ngetrip.model.TiketWisata;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface NgetripRestApi {
    // Paket Wisata
    // Get Paket Wisata
    @GET("paket-wisata")
    Call<List<PaketWisata>> getAllPaketWisata();
    // Get Paket Wisata By ID
    @GET("paket-wisata/{id_paket_wisata}")
    Call<PaketWisata> getPaketWisataById(@Path("id_paket_wisata") int id_paket_wisata);
    // Get Deskripsi Wisata
    @GET("paket-wisata/{id_paket_wisata}/deskripsi")
    Call<List<DeskripsiPaketWisata>> getDeskripsiWisata(@Path("id_paket_wisata") int id_paket_wisata);
    // Get Include Wisata
    @GET("paket-wisata/{id_paket_wisata}/include")
    Call<List<IncludePaketWisata>> getIncludeWisata(@Path("id_paket_wisata") int id_paket_wisata);
    // Get Exclude Wisata
    @GET("paket-wisata/{id_paket_wisata}/exclude")
    Call<List<ExcludePaketWisata>> getExcludeWisata(@Path("id_paket_wisata") int id_paket_wisata);

    // Agent Travel
    // Get Agent Travel
    @GET("agent-travel")
    Call<List<AgentTravel>> getAllAgentTravel();
    // Get Agent Travel By ID
    @GET("agent-travel/{id_agent_travel}")
    Call<AgentTravel> getAgentTravelById(@Path("id_agent_travel") int id_agent_travel);
    // Get Paket Wisata By Agent Travel
    @GET("agent-travel/{id_agent_travel}/paket-wisata")
    Call<List<PaketWisata>> getPaketWisataByAgentTravel(@Path("id_agent_travel") int id_agent_travel);

    // Beli Tiket Paket Wisata
    @FormUrlEncoded
    @POST("tiket-wisata")
    Call<TiketWisata> beliPaketWisata(@FieldMap Map<String, String> fields);
    // Get Tiket Paket Wisata By Username
    @GET("tiket-wisata/username/{username}")
    Call<List<TiketWisata>> getTiketWisataByUsername(@Path("username") String username);
    // Get Tiket Paket Wisata By ID
    @GET("tiket-wisata/{id_tiket_wisata}")
    Call<TiketWisata> getTiketWisataById(@Path("id_tiket_wisata") int id_tiket_wisata);
}
