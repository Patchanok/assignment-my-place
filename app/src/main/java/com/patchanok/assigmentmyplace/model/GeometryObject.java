package com.patchanok.assigmentmyplace.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by patchanok on 3/29/2018 AD.
 */

public class GeometryObject {

    /**
     * location : {"lat":13.6726813,"lng":100.4414628}
     * viewport : {"northeast":{"lat":13.6740302802915,"lng":100.4428117802915},"southwest":{"lat":13.6713323197085,"lng":100.4401138197085}}
     */

    @SerializedName("location")
    private LocationObject locationObject;
    @SerializedName("viewport")
    private ViewportObject viewportObject;

    public LocationObject getLocationObject() {
        return locationObject;
    }

    public void setLocationObject(LocationObject locationObject) {
        this.locationObject = locationObject;
    }

    public ViewportObject getViewportObject() {
        return viewportObject;
    }

    public void setViewportObject(ViewportObject viewportObject) {
        this.viewportObject = viewportObject;
    }
}
