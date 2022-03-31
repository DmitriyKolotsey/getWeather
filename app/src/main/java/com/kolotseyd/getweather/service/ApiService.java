package com.kolotseyd.getweather.service;

import com.kolotseyd.getweather.model.CurrentWeather.CurrentWeatherResponse;
import com.kolotseyd.getweather.model.DailyWeather.DailyWeatherResponse;
import com.kolotseyd.getweather.model.HourlyWeather.HourlyWeatherResponse;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {

    @GET("weather?units=metric")
    Single<CurrentWeatherResponse> getCurrentWeatherByCityName(
            @Query("lang") String lang,
            @Query("q") String q,
            @Query("appid") String appid
    );

    @GET("onecall?units=metric&exclude=current,hourly,minutely")
    Single<DailyWeatherResponse> getDailyWeatherByCoordinate(
            @Query("lang") String lang,
            @Query("lat") String latitude,
            @Query("lon") String longitude,
            @Query("appid") String appid
    );

    @GET("onecall?units=metric&exclude=current,daily,minutely")
    Single<HourlyWeatherResponse> getHourlyWeatherByCoordinate(
            @Query("lang") String lang,
            @Query("lat") String latitude,
            @Query("lon") String longitude,
            @Query("appid") String appid
    );
}
