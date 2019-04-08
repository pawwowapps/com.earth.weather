package com.example.gav.mapweatherapplication.api;

import com.example.gav.mapweatherapplication.features.weather.model.WeatherResponse;
import com.example.gav.mapweatherapplication.features.weather.model.current.CurrentWeatherResponse;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface OpenWeatherApi {
    String API_KEY = "e276a2c6dde7a940cac239eddbb2738b";

    @GET("/data/2.5//forecast")
    Observable<WeatherResponse> getWeatherForecast(
            @Query("lat") double lat,
            @Query("lon") double lon,
            @Query("APPID") String apiKey,
            @Query("units") String metric
    );

    @GET("/data/2.5//weather")
    Observable<CurrentWeatherResponse> getWeatherCurrent(
            @Query("lat") double lat,
            @Query("lon") double lon,
            @Query("APPID") String apiKey,
            @Query("units") String metric
    );
}
