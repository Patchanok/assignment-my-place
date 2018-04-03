package com.patchanok.assigmentmyplace.service;

import com.patchanok.assigmentmyplace.main.PlaceDetailByIdObject;
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
                                     @Query("type") String types,
                                     @Query("key") String key);

    @GET("details/json?")
    Call<PlaceDetailByIdObject> getPlaceDetailById(@Query("placeid") String placeId,
                                               @Query("key") String key);
}