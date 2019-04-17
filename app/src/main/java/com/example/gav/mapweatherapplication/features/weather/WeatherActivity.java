package com.example.gav.mapweatherapplication.features.weather;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import butterknife.BindView;
import butterknife.ButterKnife;

import android.os.Bundle;

import com.example.gav.mapweatherapplication.App;
import com.example.gav.mapweatherapplication.R;
import com.example.gav.mapweatherapplication.api.OpenWeatherApi;
import com.example.gav.mapweatherapplication.features.weather.repository.CurrentWeatherFragment;
import com.example.gav.mapweatherapplication.features.weather.repository.WeatherRetrofitRepository;
import com.example.gav.mapweatherapplication.utils.Constants;

public class WeatherActivity extends AppCompatActivity {

    @BindView(R.id.appToolbar)
    protected Toolbar appToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        ButterKnife.bind(this);

        setSupportActionBar(appToolbar);

        if (savedInstanceState == null) {
            addWeatherFragment(
                getIntent().getDoubleExtra(Constants.LAT, Constants.BASE_LATITUDE),
                getIntent().getDoubleExtra(Constants.LONG, Constants.BASE_LONGITUDE),
                getIntent().getIntExtra(Constants.MODE, Constants.CURRENT)
            );
        }
    }


    private void addWeatherFragment(double latitude, double longitude, int mode) {
        OpenWeatherApi openWeatherApi = App.getApp(this).getOpenWeatherApi();
        if (mode == Constants.FIVE_DAYS) {
            ForecastWeatherFragment forecastWeatherFragment = ForecastWeatherFragment.newInstance(latitude, longitude, mode);
            forecastWeatherFragment.setPresenter(new WeatherPresenter(new WeatherRetrofitRepository(openWeatherApi), forecastWeatherFragment));
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.flRoot, forecastWeatherFragment, ForecastWeatherFragment.TAG)
                    .commit();
        } else if (mode == Constants.CURRENT) {
            CurrentWeatherFragment currentWeatherFragment = CurrentWeatherFragment.newInstance(latitude, longitude, mode);
            currentWeatherFragment.setPresenter(new WeatherPresenter(new WeatherRetrofitRepository(openWeatherApi), currentWeatherFragment));
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.flRoot, currentWeatherFragment, CurrentWeatherFragment.TAG)
                    .commit();
        }

    }

    public void updateToolbarTitle(String name) {
        if (name.length() < 2)
            name = getApplicationInfo().loadLabel(getPackageManager()).toString();
        appToolbar.setTitle(name);
    }
}
