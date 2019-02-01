package com.example.gav.mapweatherapplication.features.weather.repository;

import com.example.gav.mapweatherapplication.api.OpenWeatherApi;
import com.example.gav.mapweatherapplication.features.weather.model.WeatherResponse;

import io.reactivex.Observable;

public class WeatherRetrofitRepository implements WeatherRepository {

    private final OpenWeatherApi openWeatherApi;

    public WeatherRetrofitRepository(OpenWeatherApi openWeatherApi) {
        this.openWeatherApi = openWeatherApi;
    }

    @Override
    public Observable<WeatherResponse> getWeather(double latitude, double longitude) {
        return openWeatherApi.getWeatherForecast(latitude,longitude, OpenWeatherApi.API_KEY, "metric");
    }
}
