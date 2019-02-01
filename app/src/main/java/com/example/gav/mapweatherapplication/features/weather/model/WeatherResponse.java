package com.example.gav.mapweatherapplication.features.weather.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class WeatherResponse{

	@SerializedName("city")
	private City city;

	@SerializedName("cnt")
	private int cnt;

	@SerializedName("cod")
	private String cod;

	@SerializedName("message")
	private double message;

	@SerializedName("list")
	private List<ResultItem> list;

	public void setCity(City city){
		this.city = city;
	}

	public City getCity(){
		return city;
	}

	public void setCnt(int cnt){
		this.cnt = cnt;
	}

	public int getCnt(){
		return cnt;
	}

	public void setCod(String cod){
		this.cod = cod;
	}

	public String getCod(){
		return cod;
	}

	public void setMessage(double message){
		this.message = message;
	}

	public double getMessage(){
		return message;
	}

	public void setList(List<ResultItem> list){
		this.list = list;
	}

	public List<ResultItem> getList(){
		return list;
	}
}