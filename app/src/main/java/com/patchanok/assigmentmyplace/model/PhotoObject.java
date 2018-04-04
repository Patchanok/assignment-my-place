package com.patchanok.assigmentmyplace.model;

import android.util.Log;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by patchanok on 3/29/2018 AD.
 */

public class PhotoObject {

    /**
     * height : 680
     * html_attributions : ["<a href=\"https://maps.google.com/maps/contrib/114059216641058628408/photos\">บริษัท บางบอนพลาสติค กรุ๊ป จำกัด (Bangbon Plastic Group Co.,Ltd)<\/a>"]
     * photo_reference : CmRaAAAAXlNY99BNgBGiTDhxdS4xkF1I-GC2xBAM8B-zMLgRSBHshyQGRihplzuY61YBqTk--Z9yYPzEq7BdUuaveDApaHoVS7U33gvVKq5xU66MZnR-eF-XQRbLtyAHlP59qoxnEhBp5iOcvlEYHNgdOU3AbnlQGhQdhZInsrfvOyUN9Vx1yDENeFd-Nw
     * width : 1098
     */

    private int height;
    @SerializedName("photo_reference")
    private String photoReference;
    private int width;
    @SerializedName("html_attributions")
    private List<String> htmlAttributions;

    public PhotoObject(int height, String photoReference, int width, List<String> htmlAttributions) {
        this.height = height;
        this.photoReference = photoReference;
        this.width = width;
        this.htmlAttributions = htmlAttributions;
    }

    public int getHeight() {
        return height;
    }

    public String getPhotoReference() {
        return photoReference;
    }

    public int getWidth() {
        return width;
    }

    public List<String> getHtmlAttributions() {
        return urlImage();
    }

    private List<String> urlImage() {
        List<String> url = new ArrayList<>();
//        if (htmlAttributions.size() > 0) {
//            for (String urlDefault : htmlAttributions) {
//                Log.w("","urlDefault : "+urlDefault);
//
//                String[] parts = urlDefault.split(" \" ");
//                Log.w("","parts : "+parts[0]);
//                Log.w("","parts : "+parts[1]);
//                url.add(parts[1]);
//            }
//        }
        return url;
    }
}
