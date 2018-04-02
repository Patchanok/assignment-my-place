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
    @SerializedName("geometry")
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

    public PlaceDetailObject(GeometryObject geometryObject, String icon, String id, String name,
                             OpeningHoursObject openingHoursObject, String placeId, double rating,
                             String reference, String scope, String vicinity,
                             List<PhotoObject> photoObjectList, List<String> types) {
        this.geometryObject = geometryObject;
        this.icon = icon;
        this.id = id;
        this.name = name;
        this.openingHoursObject = openingHoursObject;
        this.placeId = placeId;
        this.rating = rating;
        this.reference = reference;
        this.scope = scope;
        this.vicinity = vicinity;
        this.photoObjectList = photoObjectList;
        this.types = types;
    }

    public GeometryObject getGeometry() {
        return geometryObject;
    }

    public String getIcon() {
        return icon;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public OpeningHoursObject getOpeningHoursObject() {
        return openingHoursObject;
    }

    public String getPlaceId() {
        return placeId;
    }

    public double getRating() {
        return rating;
    }

    public String getReference() {
        return reference;
    }

    public String getScope() {
        return scope;
    }

    public String getVicinity() {
        return vicinity;
    }

    public List<PhotoObject> getPhotoObjectList() {
        return photoObjectList;
    }

    public List<String> getTypes() {
        return types;
    }

}
