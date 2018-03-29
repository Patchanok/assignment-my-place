package com.patchanok.assigmentmyplace.service;

import com.patchanok.assigmentmyplace.model.PlaceObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by patchanok on 3/29/2018 AD.
 */

public interface ApiService {
    @GET("nearbysearch/json?")
    Call<PlaceObject> getNearbyPlace(@Query("location") String latlng,
                                     @Query("radius") double radius,
                                     @Query("types") String types,
                                     @Query("key") String key);
}
