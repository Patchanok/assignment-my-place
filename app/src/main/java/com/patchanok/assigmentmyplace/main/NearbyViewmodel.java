package com.patchanok.assigmentmyplace.main;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.patchanok.assigmentmyplace.firebase.RxFirebaseAuth;
import com.patchanok.assigmentmyplace.model.AllPlaceDataObject;
import com.patchanok.assigmentmyplace.model.FavoriteItemObject;
import com.patchanok.assigmentmyplace.model.NearbyItemObject;
import com.patchanok.assigmentmyplace.model.PhotoObject;
import com.patchanok.assigmentmyplace.model.PlaceDetailObject;
import com.patchanok.assigmentmyplace.model.PlaceObject;
import com.patchanok.assigmentmyplace.service.FirebaseService;
import com.patchanok.assigmentmyplace.service.HttpClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.patchanok.assigmentmyplace.service.FirebaseService.MAP_FAVID;
import static com.patchanok.assigmentmyplace.service.FirebaseService.MAP_FAVLAT;
import static com.patchanok.assigmentmyplace.service.FirebaseService.MAP_FAVLNG;
import static com.patchanok.assigmentmyplace.service.FirebaseService.MAP_FAVNAME;
import static com.patchanok.assigmentmyplace.service.FirebaseService.MAP_FAVURL;
import static com.patchanok.assigmentmyplace.service.FirebaseService.MAP_FAVVICI;
import static com.patchanok.assigmentmyplace.service.FirebaseService.MAP_IS_FAV;
import static com.patchanok.assigmentmyplace.service.FirebaseService.MAP_USER_UID;
import static com.patchanok.assigmentmyplace.service.HttpClient.BASE_URL;

/**
 * Created by patchanok on 3/29/2018 AD.
 */

public class NearbyViewmodel extends ViewModel {

    private final String API_KEY = "AIzaSyC4r5i4Qlifrb8FMR4qhW5xrfU_XsXCLKs";
    private String location;
    private final int radius = 500;

    private HttpClient service;
    private FirebaseService firebaseService = new FirebaseService();
    private MutableLiveData<AllPlaceDataObject> placeObjectMutableLiveData;

    public MutableLiveData<AllPlaceDataObject> checkAuthFirebase(double lat, double lng, String type, String key) {
        if (RxFirebaseAuth.getCurrentUser() == null) {
            return registerFirebase(lat, lng, type, key);
        }
        return getNearbyPlace(lat, lng, type, key);
    }

    public MutableLiveData<AllPlaceDataObject> registerFirebase(double lat, double lng, String type, String key) {
        location = String.valueOf(lat) + "," + String.valueOf(lng);
        service = HttpClient.getInstance();
        firebaseService = new FirebaseService();
        placeObjectMutableLiveData = new MutableLiveData<>();
        firebaseService.loginAnonymous()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Map<String, String>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Map<String, String> stringMap) {
                        if (!stringMap.get(MAP_USER_UID).isEmpty()) {
                            placeObjectMutableLiveData = getNearbyPlace(lat, lng, type, key);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

        return placeObjectMutableLiveData;
    }

    public MutableLiveData<AllPlaceDataObject> getNearbyPlace(double lat, double lng, String type, String key) {
        placeObjectMutableLiveData = new MutableLiveData<>();
        service = HttpClient.getInstance();
        firebaseService = new FirebaseService();

        location = String.valueOf(lat) + "," + String.valueOf(lng);
        service.getService().getNearbyPlace(location, radius, type, key).enqueue(new Callback<PlaceObject>() {
            @Override
            public void onResponse(Call<PlaceObject> call, Response<PlaceObject> response) {

                if (response.body().getStatus().equals("OK")) {
                    Observable<PlaceObject> placeObjectObservable = Observable.just(response.body());
                    Observable.zip(placeObjectObservable, firebaseService.getFavoritePlace(), (placeObject, favoriteMaps) -> {
                        List<FavoriteItemObject> favoriteItemObjectList = new ArrayList<>();

                        for (Map<String, Object> result : favoriteMaps) {
                            FavoriteItemObject favoriteItem = null;
                            if (!result.isEmpty()) {
                                boolean isFav = (result.get(MAP_IS_FAV).toString().equals("true")) ? true : false;
                                favoriteItem = new FavoriteItemObject(
                                        result.get(MAP_FAVID).toString(), result.get(MAP_FAVNAME).toString(),
                                        result.get(MAP_FAVURL).toString(),
                                        Double.valueOf(result.get(MAP_FAVLAT).toString()),
                                        Double.valueOf(result.get(MAP_FAVLNG).toString()),
                                        isFav, result.get(MAP_FAVVICI).toString()
                                );
                            }
                            favoriteItemObjectList.add(favoriteItem);
                        }
                        AllPlaceDataObject placeDataObject = new AllPlaceDataObject(placeObject, favoriteItemObjectList);
                        return placeDataObject;
                    }).map(allPlaceDataObject -> {
                        List<Map<String, String>> favorites = new ArrayList<>();
                        for (PlaceDetailObject placeDetailObject : allPlaceDataObject.getPlaceObject().getPlaceDetailObject()) {
                            for (FavoriteItemObject favoriteItemObject : allPlaceDataObject.getFavoriteItemObjectList()) {
                                if (placeDetailObject.getId().equals(favoriteItemObject.getFavId())) {
                                    Map<String, String> fav = new HashMap<>();
                                    fav.put("id", favoriteItemObject.getFavId());
                                    fav.put("isFav", String.valueOf(favoriteItemObject.isFavorite()));
                                    favorites.add(fav);
                                }
                            }
                        }

                        allPlaceDataObject.getPlaceObject().setNearbyItemObjectList(getNearbyItem(allPlaceDataObject.getPlaceObject(), favorites));
                        return new AllPlaceDataObject(allPlaceDataObject.getPlaceObject(),
                                allPlaceDataObject.getFavoriteItemObjectList());
                    })
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Observer<AllPlaceDataObject>() {
                                @Override
                                public void onSubscribe(Disposable d) {

                                }

                                @Override
                                public void onNext(AllPlaceDataObject allPlaceDataObject) {
                                    placeObjectMutableLiveData.setValue(allPlaceDataObject);
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

    private List<NearbyItemObject> getNearbyItem(PlaceObject placeObject, List<Map<String, String>> favorites) {
        String name;
        String url = "";
        String vicinity;
        boolean isFavorite = false;
        double lat;
        double lng;
        List<PlaceDetailObject> placeDetailObjectList = placeObject.getPlaceDetailObject();
        List<PhotoObject> photoObjectList;
        List<NearbyItemObject> nearbyItemObjectList = new ArrayList<>();

        for (PlaceDetailObject placeDetailObject : placeDetailObjectList) {
            name = checkIsEmpty(placeDetailObject.getName());
            lat = Double.valueOf(checkIsEmpty(String.valueOf(placeDetailObject.getGeometry().getLocationObject().getLat())));
            lng = Double.valueOf(checkIsEmpty(String.valueOf(placeDetailObject.getGeometry().getLocationObject().getLng())));
            vicinity = placeDetailObject.getVicinity();
            if (placeDetailObject.getPhotoObjectList() != null) {
                photoObjectList = placeDetailObject.getPhotoObjectList();
                for (PhotoObject photoObject : photoObjectList) {
                    url = BASE_URL + "photo?maxwidth=100&photoreference=" + photoObject.getPhotoReference() + "&key=" + API_KEY;
                }
            }

            for (Map<String, String> favDetail : favorites) {
                if (placeDetailObject.getId().equals(favDetail.get("id"))) {
                    isFavorite = Boolean.parseBoolean(favDetail.get("isFav"));
                } else if (!placeDetailObject.getId().equals(favDetail.get("id"))) {
                    isFavorite = false;
                }
            }

            NearbyItemObject nearbyItemObject = new NearbyItemObject(placeDetailObject.getId(), name, url, lat, lng, isFavorite, vicinity);
            nearbyItemObjectList.add(nearbyItemObject);
        }
        return nearbyItemObjectList;
    }

    private String checkIsEmpty(String value) {
        return (value.isEmpty() || value == null) ? "" : value;
    }

}
