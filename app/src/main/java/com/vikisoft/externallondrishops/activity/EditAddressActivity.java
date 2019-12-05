package com.vikisoft.externallondrishops.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import io.fabric.sdk.android.Fabric;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.vikisoft.externallondrishops.R;
import com.vikisoft.externallondrishops.api.ApiInterface;
import com.vikisoft.externallondrishops.helper.LoginDataResponce;
import com.vikisoft.externallondrishops.helper.pincode.PostOffice;
import com.vikisoft.externallondrishops.helper.pincode.PostalCodeResponce;
import com.vikisoft.externallondrishops.utils.SessionManager;

import java.util.List;

import static com.vikisoft.externallondrishops.activity.ShopMarckerActivity.MY_LATITUDE;
import static com.vikisoft.externallondrishops.activity.ShopMarckerActivity.MY_LONGITUDE;
import static com.vikisoft.externallondrishops.api.ApiCall.getRetrofit;
import static com.vikisoft.externallondrishops.fragments.ProfileFragment.ADDRESSID;
import static com.vikisoft.externallondrishops.fragments.ProfileFragment.ADDRESSTAG;
import static com.vikisoft.externallondrishops.utils.AppConstants.POSTAL_CODE_URL;

public class EditAddressActivity extends AppCompatActivity {

    private Button btnOffice;
    private EditText edtAddress1, edtAddress2, edtLandmark, edtCity, edtState, edtCountry, edtPostalCode;
    private int selection = 0;
    private SessionManager sessionManager;
    private ApiInterface apiInterface;
    private LoginDataResponce data;
    private int selectedAddress, bundle;
    private double selectedLatitude=0,selectedLongitude=0;
    private boolean locationSelection=false;
    private LatLng latLng;
    private boolean isLocationSelected=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_edit_address);
        bundle = getIntent().getExtras().getInt(ADDRESSTAG);
        selectedAddress = getIntent().getExtras().getInt(ADDRESSID);
        /*------------------------access ui components---------------------------------*/
        //btnHome = findViewById(R.id.home);
        btnOffice = findViewById(R.id.office);
        //btnNeighbour = findViewById(R.id.neighbour);
        edtAddress1 = findViewById(R.id.address1);
        edtAddress2 = findViewById(R.id.address2);
        edtLandmark = findViewById(R.id.landmark);
        edtCity = findViewById(R.id.city);
        edtState = findViewById(R.id.state);
        edtCountry = findViewById(R.id.country);
        edtPostalCode = findViewById(R.id.postalCode);
        Button save = findViewById(R.id.saveAddress);
        ImageView back = findViewById(R.id.back);

        /*------------------------assign click listener---------------------------------*/

        btnOffice.setOnClickListener(clickListener);

        save.setOnClickListener(clickListener);
        back.setOnClickListener(clickListener);

        /*------------------------heighlight lables i.e home.office,other---------------------------------*/
        //btnOffice.setBackground(getDrawable(R.drawable.button_shape));
        /*------------------------access shred preference---------------------------------*/
        sessionManager = new SessionManager(EditAddressActivity.this);
        data = sessionManager.getProfileJson();
        if (data != null) {
            if (bundle != 0) {
                setData(selection);
            }
        }
        apiInterface = getRetrofit().create(ApiInterface.class);
        edtPostalCode.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    String code = edtPostalCode.getText().toString();
                    if (TextUtils.isEmpty(code)) {
                        edtPostalCode.setError(getString(R.string.required_field));
                        return;
                    }
                    String url = POSTAL_CODE_URL + code;
                    Call<PostalCodeResponce> call = apiInterface.getRegion(url);
                    call.enqueue(new Callback<PostalCodeResponce>() {
                        @Override
                        public void onResponse(Call<PostalCodeResponce> call, Response<PostalCodeResponce> response) {
                            if (response.body() != null) {
                                if (response.body().getStatus().equals("Success")) {
                                    showPop(response.body());
                                } else
                                    Toast.makeText(EditAddressActivity.this, "Postal Code not recognized", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<PostalCodeResponce> call, Throwable t) {

                        }
                    });
                }
            }
        });
    }

    String[] data1;

    private void showPop(PostalCodeResponce body) {
        if (body.getPostOffice().size() > 1) {

            final List<PostOffice> list = body.getPostOffice();
            data1 = new String[list.size()];
            int i = 0;
            for (PostOffice office : list) {
                data1[i] = office.getName() + "," + office.getRegion();
                i++;
            }
            AlertDialog.Builder mBuilder = new AlertDialog.Builder(EditAddressActivity.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
            mBuilder.setTitle("Choose an Address");

            mBuilder.setSingleChoiceItems(data1, -1, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int x) {
                    for (PostOffice office : list) {
                        if ((office.getName() + "," + office.getRegion()).equals(data1[x])) {
                            edtCity.setText(office.getRegion());
                            edtAddress2.setText(office.getName());
                            edtState.setText(office.getState());
                            edtCountry.setText(office.getCountry());
                        }
                    }
                    dialogInterface.dismiss();
                }
            });

            AlertDialog mDialog = mBuilder.create();
            mDialog.show();
        }
    }

    private void setData(int selection) {
        if (data.getShopAddId() == selectedAddress) {
            if (data.getShopFlatNo() != null) {
                edtAddress1.setText(data.getShopFlatNo());
                edtAddress2.setText(data.getShopRoadName());
                edtLandmark.setText(data.getShopLlandMark());
                edtCity.setText(data.getShopCity());
                edtState.setText(data.getShopState());
                edtCountry.setText(data.getShopCountry());
                edtPostalCode.setText(data.getShopPostalCode());
            } else {
                empty();
            }
        }
    }

    private void empty() {
        edtAddress1.setText("");
        edtAddress2.setText("");
        edtLandmark.setText("");
        edtCity.setText("");
        edtState.setText("");
        edtCountry.setText("");
        edtPostalCode.setText("");
    }

    /*------------------------click listener defination---------------------------------*/
    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {

                case R.id.saveAddress:
                    saveData();
                    break;
                case R.id.back:
                    setResult(Activity.RESULT_OK);
                    onBackPressed();
                    break;
                case R.id.office:
                    loadMap();
                    break;
            }
        }
    };

    private void loadMap() {
        Dexter.withActivity(EditAddressActivity.this)
                .withPermissions(Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()){
                            PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

                            try {
                                startActivityForResult(builder.build(EditAddressActivity.this), 999);
                            } catch (GooglePlayServicesRepairableException e) {
                                e.printStackTrace();
                            } catch (GooglePlayServicesNotAvailableException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).onSameThread().check();
    }

    private void saveData() {

        /*------------------------Access Data from Components---------------------------------*/
        String sAddress1 = edtAddress1.getText().toString();
        String sAddress2 = edtAddress2.getText().toString();
        String sLandmark = edtLandmark.getText().toString();
        String sCity = edtCity.getText().toString();
        String sState = edtState.getText().toString();
        String sCountry = edtCountry.getText().toString();
        String sPostalCode = edtPostalCode.getText().toString();

        /*------------------------Check for empty field---------------------------------*/
        if (TextUtils.isEmpty(sPostalCode)) {
            edtPostalCode.setError(getString(R.string.required_field));
            return;
        }
        if (TextUtils.isEmpty(sAddress1)) {
            edtAddress1.setError(getString(R.string.required_field));
            return;
        }
        if (TextUtils.isEmpty(sAddress2)) {
            edtAddress2.setError(getString(R.string.required_field));
            return;
        }
        if (TextUtils.isEmpty(sLandmark)) {
            edtLandmark.setError(getString(R.string.required_field));
            return;
        }
        if (TextUtils.isEmpty(sCity)) {
            edtCity.setError(getString(R.string.required_field));
            return;
        }
        if (TextUtils.isEmpty(sState)) {
            edtState.setError(getString(R.string.required_field));
            return;
        }
        if (TextUtils.isEmpty(sCountry)) {
            edtCountry.setError(getString(R.string.required_field));
            return;
        }
        if (!isLocationSelected){
            Toast.makeText(this, "Please select store location on map", Toast.LENGTH_SHORT).show();
            return;
        }

        if (bundle == 0) {

            data.setAddressId(0);
            data.setFlatNo(sAddress1);
            data.setRoadName(sAddress2);
            data.setLandMark(sLandmark);
            data.setCity(sCity);
            data.setState(sState);
            data.setCountry(sCountry);
            data.setPostalCode(sPostalCode);
            data.setLatitude(latLng.latitude);
            data.setLongitude(latLng.longitude);
                /*if (selection == 1) {
                    data1.setAddressTag("Home");
                } else if (selection == 2) {*/
            data.setAddressTag("Work");
                /*} else if (selection == 3) {
                    data1.setAddressTag("Other");
                } else {
                    Toast.makeText(this, getString(R.string.select_tag), Toast.LENGTH_SHORT).show();
                    return;
                }*/

        } else {

            if (data.getAddressId() == selectedAddress) {
                data.setFlatNo(sAddress1);
                data.setRoadName(sAddress2);
                data.setLandMark(sLandmark);
                data.setCity(sCity);
                data.setState(sState);
                data.setCountry(sCountry);
                data.setPostalCode(sPostalCode);
                data.setLatitude(latLng.latitude);
                data.setLongitude(latLng.longitude);
                    /*if (selection == 1) {
                        data1.setAddressTag("Home");
                    } else if (selection == 2) {*/
                data.setAddressTag("Work");
                    /*} else if (selection == 3) {
                        data1.setAddressTag("Other");
                    } else {
                        Toast.makeText(this, getString(R.string.select_tag), Toast.LENGTH_SHORT).show();
                        return;
                    }*/
            }
        }

        Call<LoginDataResponce> call = apiInterface.saveProfile(new Gson().toJson(data));
        call.enqueue(new Callback<LoginDataResponce>() {
            @Override
            public void onResponse(Call<LoginDataResponce> call, Response<LoginDataResponce> response) {
                if (response.body() != null) {
                    sessionManager.saveProfileJson(new Gson().toJson(response.body()));
                    data = response.body();
                    Toast.makeText(EditAddressActivity.this, getString(R.string.profile_saved), Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<LoginDataResponce> call, Throwable t) {
                Toast.makeText(EditAddressActivity.this, getString(R.string.network_problem), Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK){
            if (requestCode==101){
                selectedLatitude=data.getExtras().getDouble(MY_LATITUDE);
                selectedLongitude=data.getExtras().getDouble(MY_LONGITUDE);
                //Toast.makeText(this, "Lat:"+lat+" Log:"+log, Toast.LENGTH_SHORT).show();
                locationSelection=true;
                btnOffice.setBackground(getDrawable(R.drawable.button_shape));

            }
            if (requestCode==999 && resultCode==RESULT_OK){
                Place selectedPlace = PlacePicker.getPlace(data, this);
                //Toast.makeText(this, ""+selectedPlace.getLatLng().latitude, Toast.LENGTH_SHORT).show();
                latLng=selectedPlace.getLatLng();
                isLocationSelected=true;
            }
        }


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(Activity.RESULT_OK);
    }
}
