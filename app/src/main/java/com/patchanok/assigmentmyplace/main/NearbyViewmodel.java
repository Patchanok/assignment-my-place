package com.patchanok.assigmentmyplace.main;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.patchanok.assigmentmyplace.model.PlaceObject;
import com.patchanok.assigmentmyplace.service.HttpClient;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by patchanok on 3/29/2018 AD.
 */

public class NearbyViewmodel extends ViewModel {

    private final int radius = 200;
    private HttpClient service;
    private MutableLiveData<PlaceObject> placeObjectMutableLiveData;

    public MutableLiveData<PlaceObject> getNearbyPlace(double lat, double lng, String type, String key) {
        placeObjectMutableLiveData = new MutableLiveData<>();
        service = HttpClient.getInstance();

        String location = String.valueOf(lat)+","+String.valueOf(lng);
        service.getService().getNearbyPlace(location, radius, type, key).enqueue(new Callback<PlaceObject>() {
            @Override
            public void onResponse(Call<PlaceObject> call, Response<PlaceObject> response) {

                if (response.body().getStatus().equals("OK")) {
                    Observable.just(response.body())
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Observer<PlaceObject>() {
                                @Override
                                public void onSubscribe(Disposable d) {

                                }

                                @Override
                                public void onNext(PlaceObject placeObject) {
                                    placeObjectMutableLiveData.setValue(placeObject);

                                }

                                @Override
                                public void onError(Throwable e) {

                                }

                                @Override
                                public void onComplete() {

                                }
                            });
                }

            }

            @Override
            public void onFailure(Call<PlaceObject> call, Throwable t) {

            }
        });
        return placeObjectMutableLiveData;
    }
}
