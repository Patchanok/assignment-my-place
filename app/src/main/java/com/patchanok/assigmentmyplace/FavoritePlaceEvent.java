package com.patchanok.assigmentmyplace;

import com.patchanok.assigmentmyplace.model.FavoriteItemObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by patchanok on 4/2/2018 AD.
 */

public class FavoritePlaceEvent {

    public List<FavoriteItemObject> favoriteItemObjectList = new ArrayList<>();

    public FavoritePlaceEvent(List<FavoriteItemObject> favoriteItemObjectList) {
        this.favoriteItemObjectList = favoriteItemObjectList;
    }

    public List<FavoriteItemObject> getFavoriteItemObjectList() {
        return favoriteItemObjectList;
    }
}
