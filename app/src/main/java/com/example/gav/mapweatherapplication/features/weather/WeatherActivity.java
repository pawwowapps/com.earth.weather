package com.example.gav.mapweatherapplication.features.weather;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;

import com.example.gav.mapweatherapplication.App;
import com.example.gav.mapweatherapplication.R;
import com.example.gav.mapweatherapplication.api.OpenWeatherApi;
import com.example.gav.mapweatherapplication.features.map.MapActivity;
import com.example.gav.mapweatherapplication.features.weather.repository.WeatherRepository;
import com.example.gav.mapweatherapplication.features.weather.repository.WeatherRetrofitRepository;
import com.example.gav.mapweatherapplication.utils.Constants;

public class WeatherActivity extends AppCompatActivity {

    private Toolbar appToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        initViews();

        setSupportActionBar(appToolbar);

        if (savedInstanceState == null) {
            addWeatherFragment(
                getIntent().getDoubleExtra(Constants.LAT, Constants.BASE_LATITUDE),
                getIntent().getDoubleExtra(Constants.LONG, Constants.BASE_LONGITUDE),
                getIntent().getIntExtra(Constants.MODE, Constants.CURRENT)
            );
        }
    }

    private void initViews() {
        appToolbar = findViewById(R.id.appToolbar);
    }

    private void addWeatherFragment(double latitude, double longitude, int mode) {
        OpenWeatherApi openWeatherApi = App.getApp(this).getOpenWeatherApi();
        WeatherFragment weatherFragment = WeatherFragment.newInstance(latitude, longitude, mode);
        weatherFragment.setPresenter(new WeatherPresenter(new WeatherRetrofitRepository(openWeatherApi), weatherFragment));
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.llRoot, weatherFragment, WeatherFragment.TAG)
                .commit();
    }

    public void updateToolbarTitle(String name) {
        if (name.length() < 2)
            name = getApplicationInfo().loadLabel(getPackageManager()).toString();
        appToolbar.setTitle(name);
    }
}
