package ep.rest;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public class ArtikliService {
    interface RestApi {
        //192.168.1.13
        String URL = "http://10.0.2.2:8080/netbeans/trgovina/api/";

        @GET("artikli")
        Call<List<Artikel>> getAll();

        @GET("artikli/{id}")
        Call<Artikel> get(@Path("id") int id);

        @FormUrlEncoded
        @POST("artikli")
        Call<Void> insert(@Field("naziv") String naziv,
                          @Field("opis") String opis,
                          @Field("cena") double price,
                          @Field("aktiviran") int aktiviran);

        @FormUrlEncoded
        @PUT("artikli/{id}")
        Call<Void> update(@Path("id") int id,
                          @Field("naziv") String naziv,
                          @Field("opis") String opis,
                          @Field("cena") double price,
                          @Field("aktiviran") int aktiviran);

        @DELETE("artikli/{id}")
        Call<Void> delete(@Path("id") int id);
    }

    private static RestApi instance;

    public static synchronized RestApi getInstance() {
        if (instance == null) {
            final Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(RestApi.URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            instance = retrofit.create(RestApi.class);
        }

        return instance;
    }
}
