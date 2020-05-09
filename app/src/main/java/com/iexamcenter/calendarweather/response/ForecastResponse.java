package com.iexamcenter.calendarweather.response;

import androidx.annotation.Keep;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by sasikanta on 5/1/2016.
 */
@Keep
public class ForecastResponse implements Serializable {
    private static final long serialVersionUID = 1L;


    public ForecastResponse() {

    }

    @SerializedName("city")
    protected City city;


    @SerializedName("list")
    private ArrayList<WeatherList> list;

    public City getCity() {
        return city;
    }

    public ArrayList<WeatherList> getList() {
        return list;
    }
    @Keep
    public static class City implements Serializable {
        private static final long serialVersionUID = 8L;
        @SerializedName("id")
        protected int id;
        @SerializedName("name")
        protected String name;
        @SerializedName("coord")
        private Coord coord;

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public Coord getCoord() {
            return coord;
        }
    }
    @Keep
    public static class Coord implements Serializable {
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
    @Keep
    public static class WeatherList implements Serializable {
        private static final long serialVersionUID = 7L;
        @SerializedName("dt")
        protected int dt;
        @SerializedName("pressure")
        protected String pressure;
        @SerializedName("humidity")
        protected String humidity;
        @SerializedName("speed")
        protected String speed;
        @SerializedName("deg")
        protected String deg;
        @SerializedName("clouds")
        protected String clouds;
        @SerializedName("temp")
        private Temp temp;
        @SerializedName("weather")
        private ArrayList<Weather> weather;

        public int getDt() {
            return dt;
        }

        public String getPressure() {
            return pressure;
        }

        public String getHumidity() {
            return humidity;
        }

        public String getSpeed() {
            return speed;
        }

        public String getDeg() {
            return deg;
        }

        public String getClouds() {
            return clouds;
        }

        public Temp getTemp() {
            return temp;
        }

        public ArrayList<Weather> getWeather() {
            return weather;
        }
    }
    @Keep
    public static class Temp implements Serializable {
        private static final long serialVersionUID = 2L;

        @SerializedName("day")
        protected String day;
        @SerializedName("min")
        protected String min;
        @SerializedName("max")
        protected String max;
        @SerializedName("night")
        protected String night;
        @SerializedName("morn")
        protected String morn;
        @SerializedName("eve")
        protected String eve;

        public String getDay() {
            return day;
        }

        public String getMin() {
            return min;
        }

        public String getMax() {
            return max;
        }

        public String getNight() {
            return night;
        }

        public String getMorn() {
            return morn;
        }

        public String getEve() {
            return eve;
        }
    }
    @Keep
    public static class Weather implements Serializable {
        private static final long serialVersionUID = 1L;

        @SerializedName("id")
        protected int id;
        @SerializedName("main")
        protected String main;
        @SerializedName("description")
        protected String description;
        @SerializedName("icon")
        protected String icon;

        public String getMain() {
            return main;
        }

        public String getDescription() {
            return description;
        }

        public String getIcon() {
            return icon;
        }
    }

}