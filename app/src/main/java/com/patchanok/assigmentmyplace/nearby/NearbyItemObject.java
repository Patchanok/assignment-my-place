package com.patchanok.assigmentmyplace.nearby;

/**
 * Created by patchanok on 3/30/2018 AD.
 */

public class NearbyItemObject {

    private String id;
    private String name;
    private String url;
    private double lat;
    private double lng;
    private boolean isFavorite;
    private String vicinity;
    private String placeId;

    public NearbyItemObject(){}

    public NearbyItemObject(String id, String name, String url, double lat,
                            double lng, boolean isFavorite, String vicinity, String placeId) {
        this.id = id;
        this.name = name;
        this.url = url;
        this.lat = lat;
        this.lng = lng;
        this.isFavorite = isFavorite;
        this.vicinity = vicinity;
        this.placeId = placeId;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
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
