package com.vikisoft.externallondrishops.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.makeramen.roundedimageview.RoundedImageView;
import com.theartofdev.edmodo.cropper.CropImage;
import com.vikisoft.externallondrishops.R;
import com.vikisoft.externallondrishops.api.ApiCall;
import com.vikisoft.externallondrishops.api.ApiInterface;
import com.vikisoft.externallondrishops.helper.LoginDataResponce;
import com.vikisoft.externallondrishops.utils.SessionManager;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ProfileEditActivity extends AppCompatActivity {
    private static final int SELECT_PICTURE = 1001;
    private static final int CROP_REQUEST = 1002;
    private SessionManager sessionManager;
    private LoginDataResponce mainData;

    private EditText firstName, middleName, lastName, emailId, shopName;
    private ApiInterface apiInterface;
    private RoundedImageView profilePhoto;
    private ProgressBar progressBar;
    private ImageView loadImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_profile_edit);
            sessionManager = new SessionManager(ProfileEditActivity.this);
            mainData = sessionManager.getProfileJson();
            firstName = findViewById(R.id.firstName);
            middleName = findViewById(R.id.middleName);
            lastName = findViewById(R.id.lastName);
            emailId = findViewById(R.id.emailId);
            shopName = findViewById(R.id.shopName);
            progressBar = findViewById(R.id.photoLoading);
            loadImage = findViewById(R.id.photoPicker);
            loadImage.setOnClickListener(clickListner);
            ImageView back = findViewById(R.id.back);
            back.setOnClickListener(clickListner);
            profilePhoto = findViewById(R.id.profilePhoto);
            Button nameSave = findViewById(R.id.nameSave);
            nameSave.setOnClickListener(clickListner);
            profilePhoto.setOnClickListener(clickListner);
            apiInterface = ApiCall.getRetrofit().create(ApiInterface.class);


            if (mainData.getProfilePhoto() != null || !TextUtils.isEmpty(mainData.getProfilePhoto()))
                Glide.with(ProfileEditActivity.this)
                        .load(mainData.getProfilePhoto())
                        .thumbnail(0.5f)
                        .into(profilePhoto);

            firstName.setText(mainData.getFirstName());

            middleName.setText(mainData.getMiddleName());
            lastName.setText(mainData.getLastName());
            emailId.setText(mainData.getEmailid());
            shopName.setText(mainData.getShopName());


        } catch (Exception e) {
            e.printStackTrace();
//            Crashlytics.logException(e);
        }
    }

    View.OnClickListener clickListner = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.nameSave:
                    String sFirstName = firstName.getText().toString();
                    String sMiddleName = middleName.getText().toString();
                    String sLastName = lastName.getText().toString();
                    String sEmail = emailId.getText().toString();
                    String sShopName = shopName.getText().toString();
                    if (TextUtils.isEmpty(sFirstName)) {
                        firstName.setError(getString(R.string.required_field));
                        return;
                    }
                    if (TextUtils.isEmpty(sMiddleName)) {
                        middleName.setError(getString(R.string.required_field));
                        return;
                    }
                    if (TextUtils.isEmpty(sLastName)) {
                        lastName.setError(getString(R.string.required_field));
                        return;
                    }
                    if (TextUtils.isEmpty(sEmail)) {
                        emailId.setError(getString(R.string.required_field));
                        return;
                    }
                    if (TextUtils.isEmpty(sShopName)) {
                        shopName.setError(getString(R.string.required_field));
                        return;
                    }
                    if (mainData.getUserDetailId() == null) {
                        mainData.setUserDetailId(0);
                        mainData.setShopId(0);
                    }
                    mainData.setFirstName(sFirstName);
                    mainData.setMiddleName(sMiddleName);
                    mainData.setLastName(sLastName);
                    mainData.setEmailid(sEmail);
                    mainData.setShopName(sShopName);
                    sessionManager.saveProfileJson(new Gson().toJson(mainData));
                    Call<LoginDataResponce> apiCall = apiInterface.saveProfile(new Gson().toJson(mainData));
                    apiCall.enqueue(new Callback<LoginDataResponce>() {
                        @Override
                        public void onResponse(Call<LoginDataResponce> call, Response<LoginDataResponce> response) {
                            if (response.body() != null) {
                                sessionManager.saveProfileJson(new Gson().toJson(response.body()));
                                mainData = response.body();
                                Toast.makeText(ProfileEditActivity.this, getString(R.string.profile_saved), Toast.LENGTH_SHORT).show();
                                setResult(Activity.RESULT_OK);
                                finish();

                            } else
                                Toast.makeText(ProfileEditActivity.this, getString(R.string.network_problem), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(Call<LoginDataResponce> call, Throwable t) {
                            Toast.makeText(ProfileEditActivity.this, getString(R.string.network_problem), Toast.LENGTH_SHORT).show();
                        }
                    });
                    break;
                case R.id.profilePhoto:
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
                    break;
                case R.id.back:
                    onBackPressed();
                    break;
                case R.id.photoPicker:
                    Intent intent1 = new Intent();
                    intent1.setType("image/*");
                    intent1.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent1, "Select Picture"), SELECT_PICTURE);
                    break;
            }
        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable final Intent data) {
        /*super.onActivityResult(requestCode, resultCode, data);*/
        if (requestCode == SELECT_PICTURE && resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            if (null != selectedImageUri) {


                CropImage.activity(selectedImageUri).setAspectRatio(1, 1)
                        .start(this);


            }
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            progressBar.setVisibility(View.VISIBLE);

            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            Uri resultUri = result.getUri();
            Bitmap bm = null;
            try {
                bm = MediaStore.Images.Media.getBitmap(getContentResolver(), resultUri);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] b = baos.toByteArray();
            String encodedImage = Base64.encodeToString(b, Base64.NO_WRAP);
            Call<String> apiCall = apiInterface.uploadImage(encodedImage);
            apiCall.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    mainData.setProfilePhoto(response.body());
                    sessionManager.saveProfileJson(new Gson().toJson(mainData));
                    Glide.with(ProfileEditActivity.this).load(response.body()).into(profilePhoto);
                    progressBar.setVisibility(View.GONE);//Toast.makeText(ProfileEditActivity.this, getString(R.string.image_uploaded), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Toast.makeText(ProfileEditActivity.this, getString(R.string.network_problem), Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            });
        }

        super.onActivityResult(requestCode, resultCode, data);

    }
}
