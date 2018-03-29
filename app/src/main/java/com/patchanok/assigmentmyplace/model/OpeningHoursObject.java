package com.patchanok.assigmentmyplace.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by patchanok on 3/29/2018 AD.
 */

public class OpeningHoursObject {

    /**
     * open_now : false
     * weekday_text : []
     */

    @SerializedName("open_now")
    private boolean openNow;
    @SerializedName("weekday_text")
    private List<?> weekdayText;

    public boolean isOpenNow() {
        return openNow;
    }

    public void setOpenNow(boolean openNow) {
        this.openNow = openNow;
    }

    public List<?> getWeekdayText() {
        return weekdayText;
    }

    public void setWeekdayText(List<?> weekdayText) {
        this.weekdayText = weekdayText;
    }
}
