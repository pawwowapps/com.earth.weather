package com.example.gav.mapweatherapplication.features.weather;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.gav.mapweatherapplication.R;
import com.example.gav.mapweatherapplication.features.weather.model.ResultItem;
import com.google.android.material.snackbar.Snackbar;

import java.util.Collections;
import java.util.List;

public class WeatherFragment extends Fragment implements WeatherContract.View{

    public static final String TAG = "WeatherFragment";
    private WeatherContract.Presenter presenter;
    private RecyclerView rvWeather;
    private ProgressBar pbLoadWeather;
    private WeatherAdapter weatherAdapter;
    private int mode;
    private double latitude;
    private double longitude;

    public static WeatherFragment newInstance(double latitude, double longitude, int mode) {
        WeatherFragment fragment = new WeatherFragment();
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
        View view = inflater.inflate(R.layout.fragment_weather, container, false);
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
        WeatherActivity activity = (WeatherActivity) getActivity();
        if (activity != null) {
            activity.updateToolbarTitle(name);
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
