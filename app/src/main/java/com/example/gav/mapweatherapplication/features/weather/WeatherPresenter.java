package com.example.gav.mapweatherapplication.features.weather;

import com.example.gav.mapweatherapplication.App;
import com.example.gav.mapweatherapplication.R;
import com.example.gav.mapweatherapplication.features.weather.model.ForecastWeatherResponse;
import com.example.gav.mapweatherapplication.features.weather.model.current.CurrentWeatherResponse;
import com.example.gav.mapweatherapplication.features.weather.repository.WeatherRepository;
import com.example.gav.mapweatherapplication.utils.Constants;

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
    public void loadWeather(double latitude, double longitude, int mode) {
        weatherView.showProgressbar();
        if (mode == Constants.CURRENT) {
            compositeDisposable.add(
                    weatherRepository.getCurrentWeather(latitude, longitude)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(this::onLoadCurrentWeather, weatherView::errorShowWeather)
            );
        } else if (mode == Constants.FIVE_DAYS){
            compositeDisposable.add(
                    weatherRepository.getForecastWeather(latitude, longitude)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(this::onLoadForecastWeather, weatherView::errorShowWeather)
            );
        }

    }

    private void onLoadForecastWeather(ForecastWeatherResponse forecastWeatherResponse) {
        weatherView.showWeather(forecastWeatherResponse.getList());
        weatherView.updateToolbar(forecastWeatherResponse.getCity().getName());
        weatherView.hideProgressbar();
    }

    private void onLoadCurrentWeather(CurrentWeatherResponse currentWeatherResponse) {
        weatherView.showWeather(currentWeatherResponse);
        weatherView.updateToolbar(App.getContext().getString(R.string.app_name));
        weatherView.hideProgressbar();
    }

    @Override
    public void onDestroy() {
        compositeDisposable.clear();
    }
}
