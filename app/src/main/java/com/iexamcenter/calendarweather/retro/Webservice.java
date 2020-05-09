package com.iexamcenter.calendarweather.retro;

import com.iexamcenter.calendarweather.response.FamousPeopleResponse;
import com.iexamcenter.calendarweather.response.FeedbackResponse;
import com.iexamcenter.calendarweather.response.ForecastResponse;
import com.iexamcenter.calendarweather.response.OnThisDayResponse;
import com.iexamcenter.calendarweather.response.ThisDayResponse;
import com.iexamcenter.calendarweather.response.WeatherResponse;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface Webservice {


    @POST("calendar_api/ThisDayMore")
    Call<OnThisDayResponse> getThisDayMore(@Body RequestBody params);

    @POST("calendar_api/Weather")
    Call<WeatherResponse> getWeatherReport(@Body RequestBody params);

    @POST("calendar_api/Forecast")
    Call<ForecastResponse> getForecastReport(@Body RequestBody params);


    @POST("calendar_api/Today")
    Call<ThisDayResponse> getToday(@Body RequestBody params);

    @POST("calendar_api/ThisDayMore")
    Call<FeedbackResponse> sendFeedBack(@Body RequestBody params);

    @POST("calendar_api/FamousPeopleQuotes")
    Call<FamousPeopleResponse> getFamousPeople(@Body RequestBody params);

    @GET("data/2.5/weather")
    Call<WeatherResponse> getWeatherReport(@Query("lat") String lat,
                                           @Query("lon") String lng,
                                           @Query("appid") String appid,
                                           @Query("units") String units);
    @GET("data/2.5/forecast/daily")
    Call<ForecastResponse> getForecastReport(@Query("lat") String lat,
                                            @Query("lon") String lng,
                                             @Query("cnt") Integer cnt,
                                            @Query("appid") String appid,
                                            @Query("units") String units);
}