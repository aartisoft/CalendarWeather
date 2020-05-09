package com.iexamcenter.calendarweather.response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by sasikanta on 5/1/2016.
 */

public class CityListResponse implements Serializable {
    private static final long serialVersionUID = 1L;
    @SerializedName("id")
    protected int id;
    @SerializedName("name")
    protected String name;
    @SerializedName("country")
    protected String country;
    @SerializedName("coord")
    private Coord coord;

    public int getId() {
        return id;
    }
    public String getCountry() {
        return country;
    }
    public String getName() {
        return name;
    }

    public Coord getCoord() {
        return coord;
    }

    public CityListResponse() {

    }



    public class Coord implements Serializable {
        private static final long serialVersionUID = 9L;
        @SerializedName("lat")
        protected String lat;
        @SerializedName("lon")
        protected String lon;

        public String getLat() {
            return lat;
        }

        public String getLon() {
            return lon;
        }
    }

}