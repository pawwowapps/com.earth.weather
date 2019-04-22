package com.example.gav.mapweatherapplication;

import android.app.Application;
import android.content.Context;

import com.crashlytics.android.Crashlytics;
import com.example.gav.mapweatherapplication.api.OpenWeatherApi;

import io.fabric.sdk.android.Fabric;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class App extends Application {
    private OpenWeatherApi openWeatherApi;
    private static Context context;
    private static User currentUser;

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        if (context == null) {
            context = getApplicationContext();
        }
    }

    public OpenWeatherApi getOpenWeatherApi() {
        if (openWeatherApi == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://api.openweathermap.org/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
            openWeatherApi = retrofit.create(OpenWeatherApi.class);
        }
        return openWeatherApi;
    }

    public static App getApp(Context context) {
        return ((App) context.getApplicationContext());
    }

    public static Context getContext() {
        return context;
    }

    public static User getCurrentUser() {
        if (currentUser == null){
            currentUser = new User();
            currentUser.load();
        }

        return currentUser;
    }
}
