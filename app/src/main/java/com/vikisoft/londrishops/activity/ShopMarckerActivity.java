package com.vikisoft.londrishops.activity;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;

import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.SphericalUtil;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.vikisoft.londrishops.R;

import java.util.List;


public class ShopMarckerActivity extends FragmentActivity implements OnMapReadyCallback {

    public static final String MY_LATITUDE = "lat";
    public static final String MY_LONGITUDE = "log";
    private GoogleMap mMap;
    ;
    private Location mcurrentLocation;
    private LatLng center;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_marcker);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        /*PlaceAutocompleteFragment  placeAutoComplete = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.place_autocomplete);
        placeAutoComplete.setAllowEnterTransitionOverlap(true);
        //placeAutoComplete.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME));
        placeAutoComplete.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                Toast.makeText(ShopMarckerActivity.this, place.getLatLng().latitude + "    "+place.getLatLng().latitude, Toast.LENGTH_SHORT).show();
                //Log.d("Maps", "Place selected: " + place.getName());
            }

            @Override
            public void onError(Status status) {
                //Log.d("Maps", "An error occurred: " + status);
            }
        });*/

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        // Button button = findViewById(R.id.search);
        //button.setVisibility(View.GONE);
        /*button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    PlaceAutocomplete.IntentBuilder placeAutocompleteBuilder = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN);
                    if (mcurrentLocation != null) {
                        center = new LatLng(mcurrentLocation.getLatitude(), mcurrentLocation.getLongitude());
                        LatLngBounds latLngBounds = toBounds(center, 4000);
                        placeAutocompleteBuilder.setBoundsBias(latLngBounds);
                    }
                    mMap.clear();
                    Intent intent =
                            placeAutocompleteBuilder
                                    .build(ShopMarckerActivity.this);
                    startActivityForResult(intent, 1001);
                    *//*Intent intent = new Intent(ShopMarckerActivity.this, PlaceSearchActivity.class);
                    if (mcurrentLocation != null) {
                        intent.putExtra(MY_LATITUDE, mcurrentLocation.getLatitude());
                        intent.putExtra(MY_LONGITUDE, mcurrentLocation.getLongitude());
                    } else {
                        intent.putExtra(MY_LATITUDE, 0);
                        intent.putExtra(MY_LONGITUDE, 0);
                    }
                    startActivityForResult(intent, 2001);*//*

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });*/


    }

    private Marker marker;

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Dexter.withActivity(ShopMarckerActivity.this)
                .withPermissions(Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION)
                .withListener(new MultiplePermissionsListener() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                // TODO: Consider calling
                                //    Activity#requestPermissions
                                // here to request the missing permissions, and then overriding
                                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                //                                          int[] grantResults)
                                // to handle the case where the user grants the permission. See the documentation
                                // for Activity#requestPermissions for more details.
                                return;
                            }
                            mMap.setMyLocationEnabled(true);
                            mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
                                @Override
                                public void onMyLocationChange(Location location) {
                                    mcurrentLocation = location;
                                    if (marker != null) {
                                        marker.remove();
                                    }
                                    LatLng myLocation = new LatLng(location.getLatitude(), location.getLatitude());
                                    //marker = mMap.addMarker(new MarkerOptions().position(myLocation).title("My Location"));
                                    mMap.moveCamera(CameraUpdateFactory.newLatLng(myLocation));

                                    /*mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
                                    mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));*/
                                }
                            });
                            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                                @Override
                                public void onMapClick(final LatLng latLng) {
                                    if (marker != null)
                                        marker.remove();
                                    //LatLng myLocation = new LatLng(location.getLatitude(), location.getLatitude());
                                    marker = mMap.addMarker(new MarkerOptions().position(latLng).title("Selected Location"));
                                    mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                                    Button button=findViewById(R.id.setLocation);
                                    button.setVisibility(View.VISIBLE);
                                    button.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent=new Intent();
                                            intent.putExtra(MY_LATITUDE,latLng.latitude);
                                            intent.putExtra(MY_LONGITUDE,latLng.longitude);
                                            setResult(Activity.RESULT_OK,intent);
                                            finish();
                                        }
                                    });
                                }
                            });
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).onSameThread().check();

        // Add a marker in Sydney and move the camera

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
