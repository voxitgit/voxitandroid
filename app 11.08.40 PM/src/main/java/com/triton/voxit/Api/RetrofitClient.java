package com.triton.voxit.Api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    public static APIInterface restApiInterface;
    public static Retrofit retrofit;
    public static String BASE_URL = "http://tritontutebox.com/voxit/";



    private static OkHttpClient buildClient() {
        return new OkHttpClient
                .Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .build();
    }

    static Gson gson = new GsonBuilder().setLenient().create();

    private static void createRetrofitService() {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(buildClient())
                .build();

    }

    public static APIInterface getApiService() {
        if (restApiInterface == null) {
            createRetrofitService();
            restApiInterface = retrofit.create(APIInterface.class);
        }
        return restApiInterface;
    }


}
