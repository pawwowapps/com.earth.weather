package com.example.gav.mapweatherapplication.features.weather;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.gav.mapweatherapplication.R;
import com.example.gav.mapweatherapplication.features.weather.model.ResultItem;
import com.example.gav.mapweatherapplication.features.weather.model.current.CurrentWeatherResponse;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class ForecastWeatherFragment extends Fragment implements WeatherContract.View{

    public static final String TAG = "ForecastWeatherFragment";
    private WeatherContract.Presenter presenter;
    private RecyclerView rvWeather;
    private ProgressBar pbLoadWeather;
    private WeatherAdapter weatherAdapter;
    private int mode;
    private double latitude;
    private double longitude;

    public static ForecastWeatherFragment newInstance(double latitude, double longitude, int mode) {
        ForecastWeatherFragment fragment = new ForecastWeatherFragment();
        fragment.latitude = latitude;
        fragment.longitude = longitude;
        fragment.mode = mode;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forecast_weather, container, false);
        initViews(view);
        presenter.onCreate();
        presenter.loadWeather(latitude, longitude, mode);
        return view;
    }

    private void initViews(View view) {
        rvWeather = view.findViewById(R.id.rvWeather);
        pbLoadWeather = view.findViewById(R.id.pbLoadWeather);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        rvWeather.setLayoutManager(layoutManager);
        weatherAdapter = new WeatherAdapter(Collections.emptyList());
        rvWeather.setAdapter(weatherAdapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rvWeather.getContext(),
                ((LinearLayoutManager) layoutManager).getOrientation());
        rvWeather.addItemDecoration(dividerItemDecoration);
    }

    @Override
    public void showWeather(List<ResultItem> resultItems) {
        weatherAdapter.setItems(resultItems);
    }

    @Override
    public void showWeather(CurrentWeatherResponse currentWeatherResponse) {}

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
        FragmentActivity activity = getActivity();
        if (activity != null) {
            Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
            try {
                List<Address> addresses = geocoder.getFromLocation(
                        latitude,
                        longitude,
                        1
                );
                if (addresses.size() > 0) {
                    String locality = addresses
                            .get(0)
                            .getLocality();
                    if (locality != null) {
                        name = locality;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            activity.setTitle(name.isEmpty()?getContext().getString(R.string.app_name):name);
        }
    }

    @Override
    public void setPresenter(WeatherContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.onDestroy();
    }
}
