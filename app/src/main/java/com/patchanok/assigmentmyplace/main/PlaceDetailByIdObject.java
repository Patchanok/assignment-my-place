package com.patchanok.assigmentmyplace.main;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PlaceDetailByIdObject {

    @SerializedName("result")
    private ResultPlaceDetailByIdObject resultPlaceDetailByIdObject;
    private String status;
    @SerializedName("html_attributions")
    private List<?> htmlAttributions;

    public PlaceDetailByIdObject(ResultPlaceDetailByIdObject resultPlaceDetailByIdObject, String status, List<?> htmlAttributions) {
        this.resultPlaceDetailByIdObject = resultPlaceDetailByIdObject;
        this.status = status;
        this.htmlAttributions = htmlAttributions;
    }

    public ResultPlaceDetailByIdObject getResultPlaceDetailByIdObject() {
        return resultPlaceDetailByIdObject;
    }

    public String getStatus() {
        return status;
    }

    public List<?> getHtmlAttributions() {
        return htmlAttributions;
    }

    public class ResultPlaceDetailByIdObject {

        @SerializedName("formatted_address")
        private String formattedAddress;
        @SerializedName("formatted_phone_number")
        private String formattedPhone;
        private String icon;
        private String id;
        @SerializedName("international_phone_number")
        private String internationalPhone;
        private String name;
        @SerializedName("place_id")
        private String placeId;
        private double rating;
        private String url;
        @SerializedName("utc_offset")
        private int utcOffset;
        private String website;

        public ResultPlaceDetailByIdObject(String formattedAddress, String formattedPhone,
                                           String icon, String id, String internationalPhone,
                                           String name, String placeId, double rating, String url,
                                           int utcOffset, String website) {
            this.formattedAddress = formattedAddress;
            this.formattedPhone = formattedPhone;
            this.icon = icon;
            this.id = id;
            this.internationalPhone = internationalPhone;
            this.name = name;
            this.placeId = placeId;
            this.rating = rating;
            this.url = url;
            this.utcOffset = utcOffset;
            this.website = website;
        }

        public String getFormattedAddress() {
            return formattedAddress;
        }

        public String getFormattedPhone() {
            return formattedPhone;
        }

        public String getIcon() {
            return icon;
        }

        public String getId() {
            return id;
        }

        public String getInternationalPhone() {
            return internationalPhone;
        }

        public String getName() {
            return name;
        }

        public String getPlaceId() {
            return placeId;
        }

        public double getRating() {
            return rating;
        }

        public String getUrl() {
            return url;
        }

        public int getUtcOffset() {
            return utcOffset;
        }

        public String getWebsite() {
            return website;
        }
    }


}
