package ep.rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public class LoginService {
    interface RestApi {
        String URL = "http://10.0.2.2:8080/netbeans/trgovina/api/";

        @FormUrlEncoded
        @POST("login")
        Call<Stranka> login(@Field("email") String email,
                            @Field("geslo") String geslo);

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
