package com.example.gav.mapweatherapplication.features.weather;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.gav.mapweatherapplication.R;
import com.example.gav.mapweatherapplication.features.weather.model.ResultItem;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherViewHolder> {

    private List<ResultItem> resultItems;

    public WeatherAdapter(List<ResultItem> weatherItems) {
        this.resultItems = weatherItems;
    }

    public WeatherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.weather_item, parent, false);
        final WeatherViewHolder weatherViewHolder= new WeatherViewHolder(view);

        return weatherViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherViewHolder holder, int position) {
        holder.bind(resultItems.get(position));
    }

    @Override
    public int getItemCount() {
        return resultItems.size();
    }

    public void setItems(List<ResultItem> resultItems) {
        this.resultItems = resultItems;
        notifyDataSetChanged();
    }
}
