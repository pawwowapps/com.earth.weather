package com.example.gav.mapweatherapplication.features.weather.repository;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.gav.mapweatherapplication.R;
import com.example.gav.mapweatherapplication.features.weather.WeatherContract;
import com.example.gav.mapweatherapplication.features.weather.model.ResultItem;
import com.example.gav.mapweatherapplication.features.weather.model.current.CurrentWeatherResponse;
import com.example.gav.mapweatherapplication.utils.WeatherDescriptionProvider;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class CurrentWeatherFragment extends Fragment implements WeatherContract.View{

    public static final String TAG = "CurrentWeatherFragment";
    private WeatherContract.Presenter presenter;
    private int mode;
    private double latitude;
    private double longitude;

    private Unbinder unbinder;

    @BindView(R.id.tvTemperature)
    protected TextView tvTemperature;

    @BindView(R.id.tvLocation)
    protected TextView tvLocation;

    @BindView(R.id.tvHumidity)
    protected TextView tvHumidity;

    @BindView(R.id.tvPressure)
    protected TextView tvPressure;

    @BindView(R.id.tvDescription)
    protected TextView tvDescription;

    @BindView(R.id.tvWind)
    protected TextView tvWind;

    @BindView(R.id.pbLoadWeather)
    protected ProgressBar pbLoadWeather;

    @BindView(R.id.adView)
    protected AdView adView;

    public static CurrentWeatherFragment newInstance(double latitude, double longitude, int mode) {
        CurrentWeatherFragment fragment = new CurrentWeatherFragment();
        fragment.latitude = latitude;
        fragment.longitude = longitude;
        fragment.mode = mode;
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_current_weather, container, false);
        unbinder = ButterKnife.bind(this, view);
        initViews(view);
        presenter.onCreate();
        presenter.loadWeather(latitude, longitude, mode);
        loadBanner();
        return view;
    }

    private void loadBanner() {
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }

    private void initViews(View view) {
    }

    @Override
    public void showWeather(List<ResultItem> weatherItems) {}

    @Override
    public void showWeather(CurrentWeatherResponse currentWeatherResponse) {
        String cityName = "";
        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(
                    currentWeatherResponse.getCoord().getLat(),
                    currentWeatherResponse.getCoord().getLon(),
                    1
            );
            if (addresses.size() > 0) {
                String locality = addresses
                        .get(0)
                        .getLocality();
                if (locality != null) {
                    cityName = locality;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        tvLocation.setText(cityName.isEmpty() ?currentWeatherResponse.getName():cityName);
        tvTemperature.setText(
                (int)Math.round(currentWeatherResponse.getMain().getTemp()) + getString(R.string.celsium)
        );
        tvWind.setText(
                (int)Math.floor(currentWeatherResponse.getWind().getSpeed()) + getString(R.string.ms)
        );
        tvHumidity.setText(
                currentWeatherResponse.getMain().getHumidity() + getString(R.string.percent)
        );
        tvPressure.setText(
                (int)currentWeatherResponse.getMain().getPressure() + getString(R.string.mm)
        );
        tvDescription.setText(WeatherDescriptionProvider.getTranslatedDescription(getContext(), currentWeatherResponse.getWeather().get(0).getDescription()));
    }

    @Override
    public void errorShowWeather(Throwable throwable) {
        hideProgressbar();
        View viewRoot = getView();
        if (viewRoot != null) {
            Snackbar
                    .make(viewRoot, getActivity().getResources().getString(R.string.repeat_text), Snackbar.LENGTH_INDEFINITE)
                    .setAction("Ok", view -> {
                        presenter.loadWeather(latitude, longitude, mode);
                    }).show();
        }
    }

    @Override
    public void showProgressbar() {
        pbLoadWeather.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressbar() {
        pbLoadWeather.setVisibility(View.INVISIBLE);
    }

    @Override
    public void updateToolbar(String name) {
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (activity != null) {
            activity.setTitle(name);
        }
    }

    @Override
    public void setPresenter(WeatherContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        presenter.onDestroy();
    }
}
