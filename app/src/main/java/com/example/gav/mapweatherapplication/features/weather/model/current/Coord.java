package com.example.gav.mapweatherapplication.features.weather.model.current;

import com.google.gson.annotations.SerializedName;

public class Coord{

	@SerializedName("lon")
	private int lon;

	@SerializedName("lat")
	private int lat;

	public void setLon(int lon){
		this.lon = lon;
	}

	public int getLon(){
		return lon;
	}

	public void setLat(int lat){
		this.lat = lat;
	}

	public int getLat(){
		return lat;
	}

	@Override
 	public String toString(){
		return 
			"Coord{" + 
			"lon = '" + lon + '\'' + 
			",lat = '" + lat + '\'' + 
			"}";
		}
}