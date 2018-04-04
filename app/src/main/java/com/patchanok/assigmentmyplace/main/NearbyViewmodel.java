package com.patchanok.assigmentmyplace.main;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.patchanok.assigmentmyplace.favorite.FavoriteItemObject;
import com.patchanok.assigmentmyplace.firebase.RxFirebaseAuth;
import com.patchanok.assigmentmyplace.model.AllPlaceDataObject;
import com.patchanok.assigmentmyplace.model.PhotoObject;
import com.patchanok.assigmentmyplace.model.PlaceDetailObject;
import com.patchanok.assigmentmyplace.model.PlaceObject;
import com.patchanok.assigmentmyplace.nearby.NearbyItemObject;
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

import static com.patchanok.assigmentmyplace.Constants.API_KEY;
import static com.patchanok.assigmentmyplace.Constants.MAP_FAVID;
import static com.patchanok.assigmentmyplace.Constants.MAP_FAVLAT;
import static com.patchanok.assigmentmyplace.Constants.MAP_FAVLNG;
import static com.patchanok.assigmentmyplace.Constants.MAP_FAVNAME;
import static com.patchanok.assigmentmyplace.Constants.MAP_FAVURL;
import static com.patchanok.assigmentmyplace.Constants.MAP_FAVVICI;
import static com.patchanok.assigmentmyplace.Constants.MAP_FAV_ID;
import static com.patchanok.assigmentmyplace.Constants.MAP_FAV_ISFAV;
import static com.patchanok.assigmentmyplace.Constants.MAP_IS_FAV;
import static com.patchanok.assigmentmyplace.Constants.MAP_PLACEID;
import static com.patchanok.assigmentmyplace.Constants.MAP_USER_UID;
import static com.patchanok.assigmentmyplace.Constants.STATUS_RESULT_OK;
import static com.patchanok.assigmentmyplace.service.HttpClient.BASE_URL;

/**
 * Created by patchanok on 3/29/2018 AD.
 */

public class NearbyViewmodel extends ViewModel {

    private final int radius = 500;
    private String location;

    private HttpClient service;
    private FirebaseService firebaseService = new FirebaseService();
    private MutableLiveData<PlaceObject> placeObjectMutableLiveData;

    public MutableLiveData<PlaceObject> checkAuthFirebase(double lat, double lng, String type, String key) {
        if (RxFirebaseAuth.getCurrentUser() == null) {
            return registerFirebase(lat, lng, type, key);
        }
        return getNearbyPlace(lat, lng, type, key);
    }

    public MutableLiveData<PlaceObject> registerFirebase(double lat, double lng, String type, String key) {
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

    public MutableLiveData<PlaceObject> getNearbyPlace(double lat, double lng, String type, String key) {
        placeObjectMutableLiveData = new MutableLiveData<>();
        service = HttpClient.getInstance();
        firebaseService = new FirebaseService();

        location = String.valueOf(lat) + "," + String.valueOf(lng);
        service.getService().getNearbyPlace(location, radius, type, key).enqueue(new Callback<PlaceObject>() {
            @Override
            public void onResponse(Call<PlaceObject> call, Response<PlaceObject> response) {

                if (response.body().getStatus().equals(STATUS_RESULT_OK)) {
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
                                        isFav, result.get(MAP_FAVVICI).toString(),
                                        result.get(MAP_PLACEID).toString()
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
                                if (placeDetailObject.getId().equals(favoriteItemObject.getFavId()) &&
                                        favoriteItemObject.isFavorite()) {
                                    Map<String, String> fav = new HashMap<>();
                                    fav.put(MAP_FAV_ID, favoriteItemObject.getFavId());
                                    fav.put(MAP_FAV_ISFAV, String.valueOf(favoriteItemObject.isFavorite()));
                                    favorites.add(fav);
                                }
                            }
                        }

                        allPlaceDataObject.getPlaceObject().setNearbyItemObjectList(mapNearbyPlaceItem(
                                allPlaceDataObject.getPlaceObject(), favorites));
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
                                    placeObjectMutableLiveData.setValue(allPlaceDataObject.getPlaceObject());
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

    private List<NearbyItemObject> mapNearbyPlaceItem(PlaceObject placeObject, List<Map<String, String>> favoritesPlaceCurrent) {
        String name, vicinity, placeId;
        String url = "";
        double lat, lng;
        List<PlaceDetailObject> placeDetailObjectList = placeObject.getPlaceDetailObject();
        List<PhotoObject> photoObjectList;
        List<NearbyItemObject> nearbyItemObjectList = new ArrayList<>();

        for (PlaceDetailObject placeDetailObject : placeDetailObjectList) {
            name = checkIsEmpty(placeDetailObject.getName());
            lat = checkDouble(placeDetailObject.getGeometry().getLocationObject().getLat());
            lng = checkDouble(placeDetailObject.getGeometry().getLocationObject().getLng());
            vicinity = checkIsEmpty(placeDetailObject.getVicinity());
            placeId = checkIsEmpty(placeDetailObject.getPlaceId());
            if (placeDetailObject.getPhotoObjectList() != null) {
                photoObjectList = placeDetailObject.getPhotoObjectList();
                for (PhotoObject photoObject : photoObjectList) {
                    url = BASE_URL + "photo?maxwidth=100&photoreference=" +
                            photoObject.getPhotoReference() + "&key=" + API_KEY;
                }
            }

            NearbyItemObject nearbyItemObject = new NearbyItemObject();
            if (favoritesPlaceCurrent.size() != 0) {
                List<String> object = new ArrayList<>();
                for (Map<String, String> fav : favoritesPlaceCurrent) {
                    object.add(fav.get(MAP_FAV_ID));
                }
                if (object.contains(placeDetailObject.getId())) {
                    nearbyItemObject = new NearbyItemObject(placeDetailObject.getId(),
                            name, url, lat, lng, true, vicinity, placeId);
                } else {
                    nearbyItemObject = new NearbyItemObject(placeDetailObject.getId(),
                            name, url, lat, lng, false, vicinity, placeId);
                }
                nearbyItemObjectList.add(nearbyItemObject);
            } else {
                nearbyItemObject = new NearbyItemObject(placeDetailObject.getId(),
                        name, url, lat, lng, false, vicinity, placeId);
                nearbyItemObjectList.add(nearbyItemObject);
            }
        }
        return nearbyItemObjectList;
    }

    private Double checkDouble(Double position) {
        String positonString = String.valueOf(position);
        return Double.valueOf(checkIsEmpty(positonString));
    }

    private String checkIsEmpty(String value) {
        return (value.isEmpty() || value == null) ? "" : value;
    }

}
