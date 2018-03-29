package com.patchanok.assigmentmyplace.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by patchanok on 3/29/2018 AD.
 */

public class PlaceObject {

    @SerializedName("next_page_token")
    private String nextPageToken;
    private String status;
    @SerializedName("html_attributions")
    private List<?> htmlAttributions;
    @SerializedName("results")
    private List<PlaceDetailObject> placeDetailObjectList;

    public String getNextPageToken() {
        return nextPageToken;
    }

    public void setNextPageToken(String nextPageToken) {
        this.nextPageToken = nextPageToken;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<?> getHtmlAttributions() {
        return htmlAttributions;
    }

    public void setHtmlAttributions(List<?> htmlAttributions) {
        this.htmlAttributions = htmlAttributions;
    }

    public List<PlaceDetailObject> getPlaceDetailObject() {
        return placeDetailObjectList;
    }

    public void setPlaceDetailObject(List<PlaceDetailObject> placeDetailObjectList) {
        this.placeDetailObjectList = placeDetailObjectList;
    }
}
