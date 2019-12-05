package com.vikisoft.londrishops.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.maps.android.SphericalUtil;
import com.vikisoft.londrishops.R;


import java.util.Arrays;

import static com.vikisoft.londrishops.activity.ShopMarckerActivity.MY_LATITUDE;
import static com.vikisoft.londrishops.activity.ShopMarckerActivity.MY_LONGITUDE;

public class PlaceSearchActivity extends FragmentActivity {

    private Location mcurrentLocation;
    private LatLng center;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_search);
        try {
            double Lat=getIntent().getExtras().getDouble(MY_LATITUDE);
            double Log=getIntent().getExtras().getDouble(MY_LONGITUDE);

            PlaceAutocomplete.IntentBuilder placeAutocompleteBuilder = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN);
            if (Lat != 0) {
                center = new LatLng(Lat, Log);
                LatLngBounds latLngBounds = toBounds(center, 10000);
                placeAutocompleteBuilder.setBoundsBias(latLngBounds);
            }

            Intent intent =
                    placeAutocompleteBuilder
                            .build(PlaceSearchActivity.this);
            startActivityForResult(intent, 1001);
        }catch (Exception e){}
    }
    public static LatLngBounds toBounds(LatLng center, double radiusInMeters) {
        double distanceFromCenterToCorner = radiusInMeters * Math.sqrt(2.0);
        LatLng southwestCorner =
                SphericalUtil.computeOffset(center, distanceFromCenterToCorner, 225.0);
        LatLng northeastCorner =
                SphericalUtil.computeOffset(center, distanceFromCenterToCorner, 45.0);
        return new LatLngBounds(southwestCorner, northeastCorner);
    }
}
