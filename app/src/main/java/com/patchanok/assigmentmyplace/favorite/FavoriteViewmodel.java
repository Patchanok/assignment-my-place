package com.patchanok.assigmentmyplace.favorite;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.patchanok.assigmentmyplace.firebase.RxFirebaseAuth;
import com.patchanok.assigmentmyplace.nearby.NearbyItemObject;
import com.patchanok.assigmentmyplace.service.FirebaseService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.patchanok.assigmentmyplace.Constants.*;
import static com.patchanok.assigmentmyplace.firebase.RxFirebaseDatabase.ONCOMPLETE;

/**
 * Created by patchanok on 4/1/2018 AD.
 */

public class FavoriteViewmodel extends ViewModel {

    private FirebaseService firebaseService;
    private FavoriteItemObject favoriteItemObject;

    private MutableLiveData<Map<String, Boolean>> result;
    private MutableLiveData<List<FavoriteItemObject>> favoriteMutableLiveData;

    public MutableLiveData<Map<String, Boolean>> createFavoritePlace(boolean isFavorite, NearbyItemObject nearbyItemObject) {
        final Map<String, Boolean> mapResult = new HashMap<>();
        result = new MutableLiveData<>();
        firebaseService = new FirebaseService();
        favoriteItemObject = new FavoriteItemObject(nearbyItemObject.getId(),
                nearbyItemObject.getName(), nearbyItemObject.getUrl(),
                nearbyItemObject.getLat(), nearbyItemObject.getLng(), isFavorite,
                nearbyItemObject.getVicinity(), nearbyItemObject.getPlaceId());

        firebaseService.createFavoritePlace(favoriteItemObject)
                .map(stringBooleanMap -> {
                    if (stringBooleanMap.get(ONCOMPLETE)) {
                        mapResult.put(MAP_IS_FAV, isFavorite);
                    }
                    return mapResult;
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Map<String, Boolean>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Map<String, Boolean> stringBooleanMap) {
                        result.setValue(stringBooleanMap);
                        nearbyItemObject.setFavorite(isFavorite);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

        return result;
    }

    public MutableLiveData<List<FavoriteItemObject>> getFavoritePlace() {
        List<FavoriteItemObject> favoriteItemObjectList = new ArrayList<>();
        favoriteMutableLiveData = new MutableLiveData<>();
        firebaseService = new FirebaseService();
            firebaseService.getFavoritePlace().map(maps -> {
                favoriteItemObjectList.clear();
                for (Map<String, Object> result : maps) {
                    FavoriteItemObject favoriteItem = null;
                    if (!result.isEmpty()) {
                        boolean isFav = (result.get(MAP_IS_FAV).toString().equals("true")) ? true : false;
                        if (isFav) {
                            favoriteItem = new FavoriteItemObject(
                                    result.get(MAP_FAVID).toString(), result.get(MAP_FAVNAME).toString(),
                                    result.get(MAP_FAVURL).toString(),
                                    Double.valueOf(result.get(MAP_FAVLAT).toString()),
                                    Double.valueOf(result.get(MAP_FAVLNG).toString()),
                                    isFav, result.get(MAP_FAVVICI).toString(),
                                    result.get(MAP_PLACEID).toString());
                            favoriteItemObjectList.add(favoriteItem);
                        }
                    }
                }
                return favoriteItemObjectList;
            })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<List<FavoriteItemObject>>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(List<FavoriteItemObject> favoriteItemObjects) {
                            favoriteMutableLiveData.setValue(favoriteItemObjects);
                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        return favoriteMutableLiveData;
    }
}
