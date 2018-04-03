package com.patchanok.assigmentmyplace.main;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.patchanok.assigmentmyplace.service.HttpClient;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.patchanok.assigmentmyplace.Constants.STATUS_RESULT_OK;

public class PlaceDetailViewmodel extends ViewModel {

    private HttpClient service;
    private MutableLiveData<PlaceDetailByIdObject.ResultPlaceDetailByIdObject> resultPlaceDetailMutableLiveData;

    public MutableLiveData<PlaceDetailByIdObject.
            ResultPlaceDetailByIdObject> getPlaceDetailMutableLiveData(String placeId, String key) {
        service = HttpClient.getInstance();
        resultPlaceDetailMutableLiveData = new MutableLiveData<>();

        service.getService().getPlaceDetailById(placeId, key).enqueue(new Callback<PlaceDetailByIdObject>() {
            @Override
            public void onResponse(Call<PlaceDetailByIdObject> call, Response<PlaceDetailByIdObject> response) {
                if (response.body().getStatus().equals(STATUS_RESULT_OK)) {
                    Observable.just(response.body().getResultPlaceDetailByIdObject())
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Observer<PlaceDetailByIdObject.ResultPlaceDetailByIdObject>() {
                                @Override
                                public void onSubscribe(Disposable d) {

                                }

                                @Override
                                public void onNext(PlaceDetailByIdObject.ResultPlaceDetailByIdObject
                                                           resultPlaceDetailByIdObject) {
                                    resultPlaceDetailMutableLiveData.setValue(resultPlaceDetailByIdObject);
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
            public void onFailure(Call<PlaceDetailByIdObject> call, Throwable t) {

            }
        });
        return resultPlaceDetailMutableLiveData;
    }
}
