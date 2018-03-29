package com.patchanok.assigmentmyplace.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by patchanok on 3/29/2018 AD.
 */

public class PlaceDetailObject {

    /**
     * geometry : {"location":{"lat":13.6726813,"lng":100.4414628},"viewport":{"northeast":{"lat":13.6740302802915,"lng":100.4428117802915},"southwest":{"lat":13.6713323197085,"lng":100.4401138197085}}}
     * icon : https://maps.gstatic.com/mapfiles/place_api/icons/generic_business-71.png
     * id : 09a77c63d032fa2026c0316f8330be744b7461d4
     * name : บริษัท บางบอนพลาสติค กรุ๊ป จำกัด (Bangbon Plastic Group Co.,Ltd)
     * opening_hours : {"open_now":false,"weekday_text":[]}
     * photos : [{"height":680,"html_attributions":["<a href=\"https://maps.google.com/maps/contrib/114059216641058628408/photos\">บริษัท บางบอนพลาสติค กรุ๊ป จำกัด (Bangbon Plastic Group Co.,Ltd)<\/a>"],"photo_reference":"CmRaAAAAXlNY99BNgBGiTDhxdS4xkF1I-GC2xBAM8B-zMLgRSBHshyQGRihplzuY61YBqTk--Z9yYPzEq7BdUuaveDApaHoVS7U33gvVKq5xU66MZnR-eF-XQRbLtyAHlP59qoxnEhBp5iOcvlEYHNgdOU3AbnlQGhQdhZInsrfvOyUN9Vx1yDENeFd-Nw","width":1098}]
     * place_id : ChIJz9ODYyS94jARHlNEw-5hz4M
     * rating : 4.8
     * reference : CmRSAAAAx_-ERPc2Q8V0TJ-J1n2HQspcc2BABJE10LDXc0JaYYosAnhjKeF4XqopMjVNArJ3yzEYpUo7R2wg4S1VNQXHCA8asp2lrwbpGoue3cHSVHKCwA0fcdM_nPWyZuLO92sDEhBfUnFVIrMKRrxW_u6loQGtGhQn8XACE0-QL2iGfl4A6swD0VUnig
     * scope : GOOGLE
     * types : ["point_of_interest","establishment"]
     * vicinity : ซอย 54, 186 พระราม 2 ซอย 69, แขวง แสมดำ, เขต บางขุนเทียน
     */

    private GeometryObject geometryObject;
    private String icon;
    private String id;
    private String name;
    @SerializedName("opening_hours")
    private OpeningHoursObject openingHoursObject;
    @SerializedName("place_id")
    private String placeId;
    private double rating;
    private String reference;
    private String scope;
    private String vicinity;
    @SerializedName("photos")
    private List<PhotoObject> photoObjectList;
    private List<String> types;

    public GeometryObject getGeometry() {
        return geometryObject;
    }

    public void setGeometry(GeometryObject geometryObject) {
        this.geometryObject = geometryObject;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public OpeningHoursObject getOpeningHoursObject() {
        return openingHoursObject;
    }

    public void setOpeningHoursObject(OpeningHoursObject openingHoursObject) {
        this.openingHoursObject = openingHoursObject;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlace_id(String placeId) {
        this.placeId = placeId;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getVicinity() {
        return vicinity;
    }

    public void setVicinity(String vicinity) {
        this.vicinity = vicinity;
    }

    public List<PhotoObject> getPhotoObjectList() {
        return photoObjectList;
    }

    public void setPhotoObjectList(List<PhotoObject> photoObjectList) {
        this.photoObjectList = photoObjectList;
    }

    public List<String> getTypes() {
        return types;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }

}
