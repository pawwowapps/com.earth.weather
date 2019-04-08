package com.example.gav.mapweatherapplication.features.weather;

import com.example.gav.mapweatherapplication.base.BasePresenter;
import com.example.gav.mapweatherapplication.base.BaseView;
import com.example.gav.mapweatherapplication.features.weather.model.ResultItem;

import java.util.List;

public interface WeatherContract {
    interface View extends BaseView<Presenter> {

        void showWeather(List<ResultItem> weatherItems);
        void errorShowWeather(Throwable throwable);
        void showProgressbar();
        void hideProgressbar();
        void updateToolbar(String name);
    }

    interface Presenter extends BasePresenter {

        void loadWeather(double latitude, double longitude, int mode);
    }
}
