package com.patchanok.assigmentmyplace.model;

import java.util.List;

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

    public NearbyItemObject(String id, String name, String url, double lat, double lng, boolean isFavorite) {
        this.id = id;
        this.name = name;
        this.url = url;
        this.lat = lat;
        this.lng = lng;
        this.isFavorite = isFavorite;
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

}
