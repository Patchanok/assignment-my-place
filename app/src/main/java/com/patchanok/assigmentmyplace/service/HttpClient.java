package com.patchanok.assigmentmyplace.service;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by patchanok on 3/29/2018 AD.
 */

public class HttpClient {

    public static final String BASE_URL = "https://maps.googleapis.com/maps/api/place/";
    private static HttpClient instance;

    public static HttpClient getInstance() {
        if (instance == null)
            instance = new HttpClient();
        return instance;
    }

    private ApiService service;

    private HttpClient() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        service = retrofit.create(ApiService.class);
    }

    public ApiService getService() {
        return service;
    }
}
