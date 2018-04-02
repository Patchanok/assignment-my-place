package com.patchanok.assigmentmyplace.service;

import android.content.Context;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by patchanok on 3/29/2018 AD.
 */

public class HttpClient {

    public static final String BASE_URL = "https://maps.googleapis.com/maps/api/place/";
//    https://maps.googleapis.com/maps/api/place/photo?maxwidth=100&photoreference=CmRaAAAAufg_St7D6EM--PxpDm1mEK7tRmESnwQpBW6NjJxi4jq3HxZaFm1HS1-26maqkS_JQ2AjyN_ac6iTff8TtE5xM1F4N3RDT52-Q3B7d1ksKndNj3z9d6niedapzRfUcFajEhBiJIPNT11vrHX8_OAXyHaRGhTHJIRfafZAKrFz7la4pZHSpwWuvw&key=AIzaSyC4r5i4Qlifrb8FMR4qhW5xrfU_XsXCLKs

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
