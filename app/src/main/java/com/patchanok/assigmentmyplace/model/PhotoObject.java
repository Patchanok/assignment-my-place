package com.patchanok.assigmentmyplace.model;

import com.google.gson.annotations.SerializedName;

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

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getPhotoReference() {
        return photoReference;
    }

    public void setPhotoReference(String photoReference) {
        this.photoReference = photoReference;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public List<String> getHtmlAttributions() {
        return htmlAttributions;
    }

    public void setHtmlAttributions(List<String> htmlAttributions) {
        this.htmlAttributions = htmlAttributions;
    }
}
