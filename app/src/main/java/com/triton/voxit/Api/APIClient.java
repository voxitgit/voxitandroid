package com.triton.voxit.Api;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class APIClient {

    private static Retrofit retrofit = null;
    private static OkHttpClient client;

    public static Retrofit getClient() {
        client = new OkHttpClient();
        client = new OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60 / 2, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .cache(null)
                .build();
        retrofit = new Retrofit.Builder()
                //.baseUrl("https://voxitprod.herokuapp.com/auth/")
                .baseUrl("https://voxitdev.herokuapp.com/auth/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        return retrofit;
    }
}
