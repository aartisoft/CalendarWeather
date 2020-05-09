package com.iexamcenter.calendarweather.response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by sasikanta on 5/1/2016.
 */

public class TimeZoneResponse implements Serializable {
    private static final long serialVersionUID = 7L;


    public TimeZoneResponse() {

    }

    @SerializedName("timeZoneId")
    private String timeZoneId;
    @SerializedName("status")
    private String status;
    @SerializedName("timeZoneName")
    private String timeZoneName;
    @SerializedName("rawOffset")
    private String rawOffset;

    @SerializedName("dstOffset")
    private String dstOffset;

    public String getTimeZoneId() {
        return timeZoneId;
    }

    public String getStatus() {
        return status;
    }

    public String getTimeZoneName() {
        return timeZoneName;
    }

    public String getRawOffset() {
        return rawOffset;
    }

    public String getDstOffset() {
        return dstOffset;
    }
}