package com.iexamcenter.calendarweather.response;

import androidx.annotation.Keep;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by sasikanta on 5/1/2016.
 */
@Keep
public class WeatherResponse implements Serializable {
    private static final long serialVersionUID = 7L;

    public WeatherResponse() {

    }

    @SerializedName("dt")
    private int dt;
    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("cod")
    private int cod;

    @SerializedName("coord")
    private Coord coord;
    @SerializedName("main")
    private Main main;

    @SerializedName("wind")
    private Wind wind;
    @SerializedName("clouds")
    private Clouds clouds;
    @SerializedName("sys")
    private Sys sys;
    @SerializedName("weather")
    private ArrayList<Weather> weather;

    public int getDt() {
        return dt;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getCod() {
        return cod;
    }

    public Coord getCoord() {
        return coord;
    }

    public Main getMain() {
        return main;
    }

    public Wind getWind() {
        return wind;
    }

    public Clouds getClouds() {
        return clouds;
    }

    public Sys getSys() {
        return sys;
    }

    public ArrayList<Weather> getWeather() {
        return weather;
    }
    @Keep
    public static class Sys implements Serializable {
        private static final long serialVersionUID = 6L;
        @SerializedName("type")
        protected String type;
        @SerializedName("id")
        protected int id;
        @SerializedName("message")
        protected String message;
        @SerializedName("country")
        protected String country;
        @SerializedName("sunrise")
        protected int sunrise;
        @SerializedName("sunset")
        protected int sunset;

        public String getMessage() {
            return message;
        }

        public String getCountry() {
            return country;
        }

        public int getSunrise() {
            return sunrise;
        }

        public int getSunset() {
            return sunset;
        }
    }
    @Keep
    public static class Clouds implements Serializable {
        private static final long serialVersionUID = 5L;
        @SerializedName("all")
        protected String all;
    }
    @Keep
    public static class Wind implements Serializable {
        private static final long serialVersionUID = 4L;
        @SerializedName("speed")
        protected String speed;
        @SerializedName("deg")
        protected String deg;

        public String getSpeed() {
            return speed;
        }

        public String getDeg() {
            return deg;
        }
    }
    @Keep
    public static class Main implements Serializable {
        private static final long serialVersionUID = 3L;
        @SerializedName("temp")
        protected String temp;
        @SerializedName("pressure")
        protected String pressure;
        @SerializedName("humidity")
        protected String humidity;
        @SerializedName("temp_min")
        protected String temp_min;
        @SerializedName("temp_max")
        protected String temp_max;

        public String getTemp() {
            return temp;
        }

        public String getPressure() {
            return pressure;
        }

        public String getHumidity() {
            return humidity;
        }

        public String getTemp_min() {
            return temp_min;
        }

        public String getTemp_max() {
            return temp_max;
        }
    }
    @Keep
    public static class Coord implements Serializable {
        private static final long serialVersionUID = 2L;
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
    @Keep
    public static class Weather implements Serializable {
        private static final long serialVersionUID = 1L;

        @SerializedName("id")
        protected int id;

        @SerializedName("description")
        protected String description;
        @SerializedName("icon")
        protected String icon;

        public int getId() {
            return id;
        }



        public String getDescription() {
            return description;
        }

        public String getIcon() {
            return icon;
        }
    }
}