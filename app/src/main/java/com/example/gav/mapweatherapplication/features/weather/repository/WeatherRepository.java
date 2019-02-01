package com.example.gav.mapweatherapplication.features.weather.repository;

import com.example.gav.mapweatherapplication.features.weather.model.WeatherResponse;

import io.reactivex.Observable;

public interface WeatherRepository {
    Observable<WeatherResponse> getWeather(double latitude, double longitude);
}
