package com.example.gav.mapweatherapplication.features.map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.gav.mapweatherapplication.R;
import com.example.gav.mapweatherapplication.features.weather.WeatherActivity;
import com.example.gav.mapweatherapplication.utils.KeyboardUtils;
import com.example.gav.mapweatherapplication.utils.PermissionUtils;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MapActivity extends AppCompatActivity implements GoogleMap.OnMapClickListener,
        GoogleMap.OnCameraIdleListener, OnMapReadyCallback, GoogleMap.OnMyLocationClickListener, GoogleMap.OnMyLocationButtonClickListener {

    private GoogleMap mMap;
    private View mapView;
    private Marker marker;
    private ImageButton ibWeather;
    private ImageButton ibSearch;
    private EditText etSearch;
    public static final double BASE_LATITUDE = 50.431782;
    public static final double BASE_LONGITUDE = 30.516382;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        initViews();
        initListener();
    }

    private void initViews() {
        ibWeather = findViewById(R.id.ibWeather);
        ibSearch = findViewById(R.id.ibSearch);
        etSearch = findViewById(R.id.etSearch);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
            mapView = mapFragment.getView();
        }
    }

    private void initListener() {
        ibSearch.setOnClickListener(view -> {
            String searchString = etSearch.getText().toString().trim();
            if (searchString.length() > 3) {
                searchRegion(searchString);
            }
        });

        etSearch.setOnKeyListener((v, keyCode, event) -> {
            if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                    (keyCode == KeyEvent.KEYCODE_ENTER)) {
                String searchString = etSearch.getText().toString().trim();
                if (searchString.length() > 3) {
                    searchRegion(searchString);
                }
                return true;
            }
            return false;
        });

        ibWeather.setOnClickListener(view -> {
            Intent intent = new Intent(MapActivity.this, WeatherActivity.class);
            intent.putExtra("lat", marker.getPosition().latitude);
            intent.putExtra("long", marker.getPosition().longitude);
            startActivity(intent);

        });
    }

    private void searchRegion(String searchString) {
        KeyboardUtils.hideKeyboard(MapActivity.this);
        Geocoder geocoder = new Geocoder(MapActivity.this);
        List<Address> list = new ArrayList<>();
        try {
            list = geocoder.getFromLocationName(searchString, 1);
        } catch (IOException e) {
            Toast.makeText(this, getString(R.string.error_search), Toast.LENGTH_SHORT).show();
        }
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
        mMap.setOnCameraIdleListener(this);
        mMap.getUiSettings().setIndoorLevelPickerEnabled(true);
        marker = mMap.addMarker(new MarkerOptions().position(new LatLng(BASE_LATITUDE, BASE_LONGITUDE)));
        defaultFocus();
        enableMyLocation();
    }

    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        } else if (mMap != null) {
            mMap.setMyLocationEnabled(true);
            mMap.setOnMyLocationButtonClickListener(this);
            mMap.setOnMyLocationClickListener(this);

            //change position location button
            View locationButton = ((View) mapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
            RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
            rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
            rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
            rlp.setMargins(0, 180, 50, 0);
        }
    }

    public void animateMarker(final Marker marker, final LatLng toPosition,
                              final boolean hideMarker) {
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        Projection proj = mMap.getProjection();
        Point startPoint = proj.toScreenLocation(marker.getPosition());
        final LatLng startLatLng = proj.fromScreenLocation(startPoint);
        final long duration = 500;

        final LinearInterpolator interpolator = new LinearInterpolator();

        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = interpolator.getInterpolation((float) elapsed
                        / duration);
                double lng = t * toPosition.longitude + (1 - t)
                        * startLatLng.longitude;
                double lat = t * toPosition.latitude + (1 - t)
                        * startLatLng.latitude;
                marker.setPosition(new LatLng(lat, lng));

                if (t < 1.0) {
                    // Post again 16ms later.
                    handler.postDelayed(this, 16);
                } else {
                    if (hideMarker) {
                        marker.setVisible(false);
                    } else {
                        marker.setVisible(true);
                    }
                }

            }
        });
    }

    @Override
    public void onCameraIdle() {
        //Toast.makeText(this, mMap.getCamera Position().toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMapClick(LatLng latLng) {
        focusedOnPoint(latLng);

    }

    private void focusedOnPoint(LatLng latLng) {
        moveCamera(latLng);
        marker.setPosition(latLng);
        //animateMarker(marker, latLng, false);
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
}
