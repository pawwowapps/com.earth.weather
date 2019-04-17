package com.example.gav.mapweatherapplication.features.weather.repository;

import com.example.gav.mapweatherapplication.api.OpenWeatherApi;
import com.example.gav.mapweatherapplication.features.weather.model.ForecastWeatherResponse;
import com.example.gav.mapweatherapplication.features.weather.model.current.CurrentWeatherResponse;
import com.example.woofutils.utils.TimeConverter;

import io.reactivex.Observable;

public class WeatherRetrofitRepository implements WeatherRepository {

    private final OpenWeatherApi openWeatherApi;

    public WeatherRetrofitRepository(OpenWeatherApi openWeatherApi) {
        this.openWeatherApi = openWeatherApi;
    }

    @Override
    public Observable<ForecastWeatherResponse> getForecastWeather(double latitude, double longitude) {
        return openWeatherApi.getWeatherForecast(latitude,longitude, OpenWeatherApi.API_KEY, "metric");
    }

    @Override
    public Observable<CurrentWeatherResponse> getCurrentWeather(double latitude, double longitude) {
        return openWeatherApi.getWeatherCurrent(latitude,longitude, OpenWeatherApi.API_KEY, "metric");
    }
}
