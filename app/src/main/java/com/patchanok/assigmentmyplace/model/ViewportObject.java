package com.patchanok.assigmentmyplace.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by patchanok on 3/29/2018 AD.
 */

public class ViewportObject {

    /**
     * northeast : {"lat":13.6740302802915,"lng":100.4428117802915}
     * southwest : {"lat":13.6713323197085,"lng":100.4401138197085}
     */

    @SerializedName("northeast")
    private NortheastObject northeastObject;
    @SerializedName("southwest")
    private SouthwestObject southwestObject;

    public NortheastObject getNortheastObject() {
        return northeastObject;
    }

    public void setNortheastObject(NortheastObject northeastObject) {
        this.northeastObject = northeastObject;
    }

    public SouthwestObject getSouthwestObject() {
        return southwestObject;
    }

    public void setSouthwestObject(SouthwestObject southwestObject) {
        this.southwestObject = southwestObject;
    }
}
