package com.iexamcenter.calendarweather.retro;

public class ApiUtil {

    private static final String BASE_URL1 = "http://iexamcenter.com/iexamcenter/web/frontend_dev.php/";
    private static final String BASE_URL2 = "http://api.openweathermap.org/";

    public static Webservice getIExamCenterBaseURLClass(){
        return RetrofitAPI.getRetrofit(BASE_URL1).create(Webservice.class);
    }
    public static Webservice getWeatherBaseURLClass(){
        return RetrofitAPI.getRetrofit(BASE_URL2).create(Webservice.class);
    }
}