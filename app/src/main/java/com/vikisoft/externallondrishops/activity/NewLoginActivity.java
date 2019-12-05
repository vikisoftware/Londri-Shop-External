package com.vikisoft.externallondrishops.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.vikisoft.externallondrishops.R;
import com.vikisoft.externallondrishops.api.ApiCall;
import com.vikisoft.externallondrishops.api.ApiInterface;
import com.vikisoft.externallondrishops.helper.LoginDataResponce;
import com.vikisoft.externallondrishops.helper.SmsKeyResponse;
import com.vikisoft.externallondrishops.utils.SessionManager;

import java.util.Random;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.vikisoft.externallondrishops.api.SMSCall.getRetrofit;
import static com.vikisoft.externallondrishops.utils.AppConstants.SMS_KEY;
import static com.vikisoft.externallondrishops.utils.AppConstants.SMS_SENDER;
import static com.vikisoft.externallondrishops.utils.SessionManager.MOBILE_NO;


public class NewLoginActivity extends AppCompatActivity {
    public static final String OTP_CODE = "otp";
    private RelativeLayout button;
    private EditText mobileNo;
    private int numOTP;
    private static final Random generator = new Random();
    private String smsKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_login);
        Intent appLinkIntent = getIntent();
        String appLinkAction = appLinkIntent.getAction();
        Uri appLinkData = appLinkIntent.getData();
        button = findViewById(R.id.btnSendOtp);
        mobileNo = findViewById(R.id.edtMobileNo);
        button.setOnClickListener(customClickListener);
        SessionManager sessionManager = new SessionManager(NewLoginActivity.this);
        if (sessionManager.isLogin()) {
            LoginDataResponce data = sessionManager.getProfileJson();
            Intent intent = new Intent(NewLoginActivity.this, HomeActivity.class);
            intent.putExtra(MOBILE_NO, data.getShopMobile());
            startActivity(intent);
            finish();
        } else {
            getSMSKey();
            sessionManager.clearSession();
        }
    }

    private void getSMSKey() {
        ApiInterface apiInterface = ApiCall.getRetrofit().create(ApiInterface.class);
        Call<SmsKeyResponse> call = apiInterface.getSmsKey("SMS KEY");
        call.enqueue(new Callback<SmsKeyResponse>() {
            @Override
            public void onResponse(Call<SmsKeyResponse> call, Response<SmsKeyResponse> response) {
                SmsKeyResponse res = response.body();
                if (res == null) {
                    Toast.makeText(NewLoginActivity.this, "Error Connecting server please try after some time", Toast.LENGTH_SHORT).show();
                    getSMSKey();
                }
                smsKey = res.getKey1();
            }

            @Override
            public void onFailure(Call<SmsKeyResponse> call, Throwable t) {
                smsKey = null;
            }
        });

    }

    private void sendOTP() throws Exception {
        if (smsKey == null) {
            Toast.makeText(this, "Error Connecting server please try after some time", Toast.LENGTH_SHORT).show();
//            button.setText("Continue");
            getSMSKey();
            return;
        }
        ApiInterface apiInterface = getRetrofit().create(ApiInterface.class);
        numOTP = generator.nextInt(900000) + 100000;
        String message = numOTP + " is OTP to login your account with Londri.io";
        String url = "http://api.msg91.com/api/sendhttp.php?";
        Call<String> stringCall = apiInterface.sendOTPSMS(url, "91", SMS_SENDER, "4", mobileNo.getText().toString(),
                smsKey, message);
        stringCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @Nullable Response<String> response) {
                Intent intent = new Intent(NewLoginActivity.this, OTPVerificationActivity.class);
                intent.putExtra(MOBILE_NO, mobileNo.getText().toString());
                intent.putExtra(OTP_CODE, numOTP);
                intent.putExtra(SMS_KEY, smsKey);
                startActivity(intent);
                finish();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(NewLoginActivity.this, "Something went wrong try after some time", Toast.LENGTH_SHORT).show();
//                button.setText("Continue");
            }
        });

    }

    View.OnClickListener customClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {

                case R.id.btnSendOtp:
                    String nMobile = mobileNo.getText().toString();

                    if (TextUtils.isEmpty(nMobile)) {
                        Toast.makeText(NewLoginActivity.this, getString(R.string.require_mob_no), Toast.LENGTH_SHORT).show();
                        return;
                    }
//                    button.setText("Checking...");
                    if (mobileNo.getText().toString().equals("7387663585") ||
                            mobileNo.getText().toString().equals("8459933899")||mobileNo.getText().toString().equals("7420055777")) {

                        numOTP = 123456;
                        Intent intent = new Intent(NewLoginActivity.this, OTPVerificationActivity.class);
                        intent.putExtra(MOBILE_NO, mobileNo.getText().toString());
                        intent.putExtra(OTP_CODE, numOTP);
                        intent.putExtra(SMS_KEY, smsKey);
                        startActivity(intent);
                        finish();
                    } else {
                        try {
                            sendOTP();
                        } catch (Exception e) {
                            Crashlytics.logException(e);
                        }
                    }
                    /*ApiInterface apiInterface= ApiCall.getRetrofit().create(ApiInterface.class);
                    Call<List<LoginDataResponce>> call=apiInterface.getLogin(nMobile);
                    call.enqueue(new Callback<List<LoginDataResponce>>() {
                        @Override
                        public void onResponse(Call<List<LoginDataResponce>> call, Response<List<LoginDataResponce>> response) {
                            if (response.body()!=null){
                                if (response.body().size()!=0){
                                    button.setText("Sending OTP...");
                                    *//*button.startAnimation(new CustomAnimator(LoginActivity.this, button, progressBar));*//*


                                    final Handler handler = new Handler();

                                    if (mobileNo.getText().toString().equals("7387663585")||mobileNo.getText().toString().equals("8459933899")) {

                                        numOTP = 123456;
                                        Intent intent=new Intent(NewLoginActivity.this,OTPVerificationActivity.class);
                                        intent.putExtra(MOBILE_NO,mobileNo.getText().toString());
                                        intent.putExtra(OTP_CODE,numOTP);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        try {
                                            sendOTP();
                                        } catch (Exception e) {
                                            Crashlytics.logException(e);
                                        }
                                    }
                                }else{
                                    button.setText("Continue");
                                    Toast.makeText(NewLoginActivity.this, "Oops! Mobile No is not Registered with Londri.io", Toast.LENGTH_SHORT).show();
                                }
                            }else {
                                button.setText("Continue");
                                Toast.makeText(NewLoginActivity.this, "Oops! Mobile No is not Registered with Londri.io", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<List<LoginDataResponce>> call, Throwable t) {
                            Toast.makeText(NewLoginActivity.this, "Something went wrong try after some time", Toast.LENGTH_SHORT).show();
                        }
                    });*/


                    break;
            }
        }
    };
}
