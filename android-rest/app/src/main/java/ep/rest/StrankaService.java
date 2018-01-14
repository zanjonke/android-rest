package ep.rest;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public class StrankaService {
    interface RestApi {
        String URL = "http://10.0.2.2:8080/netbeans/trgovina/api/";

        @FormUrlEncoded
        @POST("posodobistranke")
        Call<Void> update(@Field("id") String id,
                          @Field("ime") String ime,
                          @Field("priimek") String priimek,
                          @Field("email") String email,
                          @Field("geslo") String geslo,
                          @Field("naslov") String naslov,
                          @Field("telefon") String telefon,
                          @Field("aktiviran") String aktiviran);
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
