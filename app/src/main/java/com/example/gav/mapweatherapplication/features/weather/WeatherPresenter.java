package com.example.gav.mapweatherapplication.features.weather;

import com.example.gav.mapweatherapplication.features.weather.model.WeatherResponse;
import com.example.gav.mapweatherapplication.features.weather.repository.WeatherRepository;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class WeatherPresenter implements WeatherContract.Presenter{

    private final WeatherRepository weatherRepository;
    private final WeatherContract.View weatherView;
    private CompositeDisposable compositeDisposable;

    public WeatherPresenter(WeatherRepository weatherRepository, WeatherContract.View weatherView) {
        this.weatherRepository = weatherRepository;
        this.weatherView = weatherView;
    }

    @Override
    public void onCreate() {
        compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void loadWeather(double latitude, double longitude) {
        weatherView.showProgressbar();
        compositeDisposable.add(
                weatherRepository.getWeather(latitude, longitude)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(this::onLoadWeather, weatherView::errorShowWeather)
        );
    }

    private void onLoadWeather(WeatherResponse weatherResponse) {
        weatherView.showWeather(weatherResponse.getList());
        weatherView.updateToolbar(weatherResponse.getCity().getName());
        weatherView.hideProgressbar();
    }

    @Override
    public void onDestroy() {
        compositeDisposable.clear();
    }
}
