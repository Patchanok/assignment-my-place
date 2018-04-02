package com.patchanok.assigmentmyplace;

import com.patchanok.assigmentmyplace.model.PlaceObject;

/**
 * Created by patchanok on 3/30/2018 AD.
 */

public class NearbyEvent {

    public PlaceObject placeObject;

    public NearbyEvent(PlaceObject placeObject) {
        this.placeObject = placeObject;
    }

    public PlaceObject getPlaceObject() {
        return placeObject;
    }

}
