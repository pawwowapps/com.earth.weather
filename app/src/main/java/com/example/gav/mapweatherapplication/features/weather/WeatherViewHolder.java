package com.example.gav.mapweatherapplication.features.weather;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.gav.mapweatherapplication.R;
import com.example.gav.mapweatherapplication.features.weather.model.ResultItem;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class WeatherViewHolder extends RecyclerView.ViewHolder {

    private ImageView ivIcon;
    private TextView tvDay;
    private TextView tvDescription;
    private TextView tvFromTemp;
    private TextView tvToTemp;

    public WeatherViewHolder(@NonNull View itemView) {
        super(itemView);
        initViews(itemView);
    }

    private void initViews(View itemView) {
        ivIcon = itemView.findViewById(R.id.ivIcon);
        tvDay = itemView.findViewById(R.id.tvDay);
        tvDescription = itemView.findViewById(R.id.tvDescription);
        tvFromTemp = itemView.findViewById(R.id.tvFromTemp);
        tvToTemp = itemView.findViewById(R.id.tvToTemp);
    }

    public void bind(ResultItem resultItem) {
        fillIcon(resultItem);
        fillDay(resultItem);
        fillTemperature(resultItem);
    }

    private void fillIcon(ResultItem resultsItem) {
        String url = "http://openweathermap.org/img/w/" + resultsItem.getWeather().get(0).getIcon() + ".png";

        Glide.with(ivIcon)
                .load(url)
                .into(ivIcon);
    }

    private void fillDay(ResultItem resultsItem) {

        String dtTxt = resultsItem.getDtTxt();
        String dtTxtWithoutYear = dtTxt.substring(5, dtTxt.length());
        String resultString = dtTxtWithoutYear.substring(0, dtTxtWithoutYear.length() - 3);
        tvDay.setText(resultString);
        tvDescription.setText(resultsItem.getWeather().get(0).getDescription());
    }

    private void fillTemperature(ResultItem resultsItem) {
        tvFromTemp.setText(Long.toString(Math.round(resultsItem.getMain().getTempMin()))+"\u00B0");
        tvToTemp.setText(Long.toString(Math.round(resultsItem.getMain().getTempMax()))+"\u00B0");
    }
}
