package com.fit2081.invoicegeneratorapp;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class ItemsMapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items_map);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng australia = new LatLng(-25.2744, 133.7751);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(australia, 5));

        mMap.setOnMapClickListener(this::onMapClick);
    }

    private void onMapClick(LatLng latLng) {
        String countryName = getCountryName(latLng);
        if (countryName != null) {
            Toast.makeText(this, "Selected country: " + countryName, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "No country at this location.", Toast.LENGTH_LONG).show();
        }
    }

    private String getCountryName(LatLng latLng) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            if (addresses != null && !addresses.isEmpty()) {
                return addresses.get(1).getCountryName();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
