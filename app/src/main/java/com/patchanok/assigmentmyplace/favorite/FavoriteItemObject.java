package com.patchanok.assigmentmyplace.favorite;

/**
 * Created by patchanok on 4/1/2018 AD.
 */

public class FavoriteItemObject {

    private String favId;
    private String favName;
    private String favUrl;
    private double favLat;
    private double favLng;
    private boolean isFavorite;
    private String vicinity;
    private String placeId;

    public FavoriteItemObject(){}

    public FavoriteItemObject(String favId, String favName, String favUrl, double favLat,
                              double favLng, boolean isFavorite, String vicinity, String placeId) {
        this.favId = favId;
        this.favName = favName;
        this.favUrl = favUrl;
        this.favLat = favLat;
        this.favLng = favLng;
        this.isFavorite = isFavorite;
        this.vicinity = vicinity;
        this.placeId = placeId;
    }

    public String getFavId() {
        return favId;
    }

    public String getFavName() {
        return favName;
    }

    public String getFavUrl() {
        return favUrl;
    }

    public double getFavLat() {
        return favLat;
    }

    public double getFavLng() {
        return favLng;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public String getVicinity() {
        return vicinity;
    }

    public String getPlaceId() {
        return placeId;
    }
}
