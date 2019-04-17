package com.example.gav.mapweatherapplication.features.weather.repository;

import com.example.gav.mapweatherapplication.features.weather.model.ForecastWeatherResponse;
import com.example.gav.mapweatherapplication.features.weather.model.current.CurrentWeatherResponse;

import io.reactivex.Observable;

public interface WeatherRepository {
    Observable<ForecastWeatherResponse> getForecastWeather(double latitude, double longitude);
    Observable<CurrentWeatherResponse> getCurrentWeather(double latitude, double longitude);
}
