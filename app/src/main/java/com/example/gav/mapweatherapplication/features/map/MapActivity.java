package com.example.gav.mapweatherapplication.features.map;

import android.Manifest;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.gav.mapweatherapplication.App;
import com.example.gav.mapweatherapplication.R;
import com.example.gav.mapweatherapplication.User;
import com.example.gav.mapweatherapplication.api.OpenWeatherApi;
import com.example.gav.mapweatherapplication.features.weather.ForecastWeatherFragment;
import com.example.gav.mapweatherapplication.features.weather.WeatherPresenter;
import com.example.gav.mapweatherapplication.features.weather.repository.CurrentWeatherFragment;
import com.example.gav.mapweatherapplication.features.weather.repository.WeatherRetrofitRepository;
import com.example.gav.mapweatherapplication.utils.Constants;
import com.example.gav.mapweatherapplication.utils.KeyboardUtils;
import com.example.gav.mapweatherapplication.utils.PermissionUtil;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MapActivity extends AppCompatActivity implements GoogleMap.OnMapClickListener,
        OnMapReadyCallback, GoogleMap.OnMyLocationClickListener, GoogleMap.OnMyLocationButtonClickListener {

    @BindView(R.id.toolbar)
    protected Toolbar toolbar;

    @BindView(R.id.bottom_navigation)
    protected BottomNavigationView bottomNavigationView;

    @BindView(R.id.flContent)
    protected FrameLayout flContent;

    private GoogleMap mMap;
    private View mapView;
    private Marker marker;

    private Fragment currentFragment;
    private int currentMode = -1;

    private SearchView searchView;
    private MenuItem searchItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        initViews();
        setUpBottomNavigation();
    }

    private void setUpBottomNavigation() {
        bottomNavigationView.inflateMenu(R.menu.bottom_menu);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id){
                    case R.id.action_current_weather:
                        onWeatherClick(Constants.CURRENT);
                        break;
                    case R.id.action_five_days_weather:
                        onWeatherClick(Constants.FIVE_DAYS);
                        break;
                }
                return true;
            }
        });
    }

    private void initViews() {
        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
            mapView = mapFragment.getView();
        }
    }

    private void onWeatherClick(int mode) {
        if (currentMode == mode)
            return;
        mapView.setVisibility(View.GONE);
        searchItem.setVisible(false);
        searchView.setVisibility(View.GONE);
        addWeatherFragment(marker.getPosition().latitude, marker.getPosition().longitude, mode);
    }

    private void addWeatherFragment(double latitude, double longitude, int mode) {
        OpenWeatherApi openWeatherApi = App.getApp(this).getOpenWeatherApi();
        currentMode = mode;
        if (currentFragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .remove(currentFragment)
                    .commitAllowingStateLoss();
        }
        if (mode == Constants.FIVE_DAYS) {
            ForecastWeatherFragment forecastWeatherFragment = ForecastWeatherFragment.newInstance(latitude, longitude, mode);
            currentFragment = forecastWeatherFragment;
            forecastWeatherFragment.setPresenter(new WeatherPresenter(new WeatherRetrofitRepository(openWeatherApi), forecastWeatherFragment));
            getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit)
                    .add(R.id.flContent, forecastWeatherFragment, ForecastWeatherFragment.TAG)
                    .commitAllowingStateLoss();
        } else if (mode == Constants.CURRENT) {
            CurrentWeatherFragment currentWeatherFragment = CurrentWeatherFragment.newInstance(latitude, longitude, mode);
            currentFragment = currentWeatherFragment;
            currentWeatherFragment.setPresenter(new WeatherPresenter(new WeatherRetrofitRepository(openWeatherApi), currentWeatherFragment));
            getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(R.anim.pop_enter, R.anim.pop_exit, R.anim.enter, R.anim.exit)
                    .add(R.id.flContent, currentWeatherFragment, CurrentWeatherFragment.TAG)
                    .commitAllowingStateLoss();
        }

    }

    private void searchRegion(String searchString) {
        KeyboardUtils.hideKeyboard(MapActivity.this);
        Geocoder geocoder = new Geocoder(MapActivity.this);
        List<Address> list = new ArrayList<>();
        try {
            list = geocoder.getFromLocationName(searchString, 20);
        } catch (IOException e) {
            Toast.makeText(this, getString(R.string.error_search), Toast.LENGTH_SHORT).show();
        }
        invalidateOptionsMenu();
        if (list.size() > 0) {
            Address address = list.get(0);
            focusedOnPoint(new LatLng(address.getLatitude(), address.getLongitude()));
        } else {
            Toast.makeText(this, getString(R.string.error_search), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMapClickListener(this);
        mMap.getUiSettings().setIndoorLevelPickerEnabled(true);
        marker = mMap.addMarker(new MarkerOptions().position(new LatLng(App.getCurrentUser().getLastLatitude(), App.getCurrentUser().getLastLongitude())));
        defaultFocus();
        enableMyLocation();
    }

    private void enableMyLocation() {
        if (!PermissionUtil.checkLocationPermissionsGranted(this)) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, PermissionUtil.FINE_LOCATION_CODE);
        } else
            setupMap();
    }

    @Override
    public void onRequestPermissionsResult(final int requestCode, @NonNull final String[] permissions, @NonNull final int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PermissionUtil.FINE_LOCATION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setupMap();
            } else {
                Toast.makeText(this, getString(R.string.permission_required_toast), Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchItem = menu.findItem(R.id.action_search);
        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(MapActivity.this.getComponentName()));
        }
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String searchString = query.trim();
                if (searchString.length() > 2) {
                    searchRegion(searchString);
                }
                searchView.clearFocus();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private void setupMap() {
        if (mMap == null)
            return;
        mMap.setMyLocationEnabled(true);
        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnMyLocationClickListener(this);

        //change position location button
        View locationButton = ((View) mapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
        RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();

        rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
        rlp.setMargins(0, 10, rlp.width + 10, 0);

        mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                if(mMap.getMyLocation() == null)
                    return false;
                focusedOnPoint(new LatLng(mMap.getMyLocation().getLatitude(), mMap.getMyLocation().getLongitude()));
                return false;
            }
        });
    }

    @Override
    public void onMapClick(LatLng latLng) {
        App.getCurrentUser().setLastLatitude(latLng.latitude);
        App.getCurrentUser().setLastLongitude(latLng.longitude);
        App.getCurrentUser().save();
        focusedOnPoint(latLng);

    }

    private void focusedOnPoint(LatLng latLng) {
        moveCamera(latLng);
        marker.setPosition(latLng);
    }

    private void moveCamera(LatLng position) {
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(position, mMap.getCameraPosition().zoom);
        mMap.animateCamera(cameraUpdate);
    }

    private void defaultFocus() {
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 8.0f);
        mMap.animateCamera(cameraUpdate);
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
    }

    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().findFragmentByTag(CurrentWeatherFragment.TAG) == null
                && getSupportFragmentManager().findFragmentByTag(ForecastWeatherFragment.TAG) == null)
            super.onBackPressed();
        else {
            getSupportFragmentManager()
                    .beginTransaction()
                    .remove(currentFragment)
                    .commitAllowingStateLoss();
            mapView.setVisibility(View.VISIBLE);
            searchView.setVisibility(View.VISIBLE);
            currentFragment = null;
            setTitle(R.string.app_name);
            searchItem.setVisible(true);
            currentMode = -1;
        }


    }

    /*@Override
    protected void onStop() {
        super.onStop();
        if (currentFragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .remove(currentFragment)
                    .commitAllowingStateLoss();
        }
        mapView.setVisibility(View.VISIBLE);
        searchView.setVisibility(View.VISIBLE);
        currentFragment = null;
        setTitle(R.string.app_name);
        searchItem.setVisible(true);
        currentMode = -1;

    }*/
}
