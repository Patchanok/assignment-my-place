package com.patchanok.assigmentmyplace.favorite;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.patchanok.assigmentmyplace.model.FavoriteItemObject;
import com.patchanok.assigmentmyplace.model.NearbyItemObject;
import com.patchanok.assigmentmyplace.service.FirebaseService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.patchanok.assigmentmyplace.firebase.RxFirebaseDatabase.ONCOMPLETE;
import static com.patchanok.assigmentmyplace.service.FirebaseService.MAP_FAVID;
import static com.patchanok.assigmentmyplace.service.FirebaseService.MAP_FAVLAT;
import static com.patchanok.assigmentmyplace.service.FirebaseService.MAP_FAVLNG;
import static com.patchanok.assigmentmyplace.service.FirebaseService.MAP_FAVNAME;
import static com.patchanok.assigmentmyplace.service.FirebaseService.MAP_FAVURL;
import static com.patchanok.assigmentmyplace.service.FirebaseService.MAP_IS_FAV;

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
                nearbyItemObject.getLat(), nearbyItemObject.getLng(), isFavorite);

        firebaseService.createFavoriteItem(favoriteItemObject)
                .map(stringBooleanMap -> {
                    if (stringBooleanMap.get(ONCOMPLETE)) {
                        mapResult.put("isFavorite", isFavorite);
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
                                Double.valueOf(result.get(MAP_FAVLNG).toString()), isFav);
                    }
                }
                favoriteItemObjectList.add(favoriteItem);
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
