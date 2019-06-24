package com.packtpub.bankingapp.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BankClient {

    private static Retrofit RETROFIT;

    public static final String SERVICE_BASE_URL = "http://192.168.100.4:8080/";

    public static Retrofit getRetrofit() {
        if (RETROFIT == null) {
            getClient();
        }
        return RETROFIT;
    }

    private static void getClient() {
        Gson gson = new GsonBuilder().setLenient().create();
        RETROFIT = new Retrofit.Builder()
                .baseUrl(SERVICE_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }
}
