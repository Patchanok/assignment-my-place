package com.patchanok.assigmentmyplace.model;

import com.patchanok.assigmentmyplace.favorite.FavoriteItemObject;

import java.util.List;

/**
 * Created by patchanok on 4/1/2018 AD.
 */

public class AllPlaceDataObject {
    private PlaceObject placeObject;
    private List<FavoriteItemObject> favoriteItemObjectList;

    public AllPlaceDataObject(PlaceObject placeObject, List<FavoriteItemObject> favoriteItemObjectList) {
        this.placeObject = placeObject;
        this.favoriteItemObjectList = favoriteItemObjectList;
    }

    public PlaceObject getPlaceObject() {
        return placeObject;
    }

    public List<FavoriteItemObject> getFavoriteItemObjectList() {
        return favoriteItemObjectList;
    }
}
