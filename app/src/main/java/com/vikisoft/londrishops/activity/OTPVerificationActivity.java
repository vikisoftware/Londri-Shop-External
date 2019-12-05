package com.vikisoft.londrishops.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.vikisoft.londrishops.R;
import com.vikisoft.londrishops.api.ApiInterface;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.vikisoft.londrishops.activity.NewLoginActivity.OTP_CODE;
import static com.vikisoft.londrishops.api.ApiCall.getRetrofit;
import static com.vikisoft.londrishops.utils.AppConstants.SMS_KEY;
import static com.vikisoft.londrishops.utils.AppConstants.SMS_SENDER;
import static com.vikisoft.londrishops.utils.SessionManager.MOBILE_NO;


public class OTPVerificationActivity extends AppCompatActivity {

    private String mobileNo;
    private int numOTP;
    private EditText edtOtp;
    private RelativeLayout verify;
    private TextView timerTv;
    private Button resendOtpBtn;
    private String smsKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpverification);
        Bundle bundle=getIntent().getExtras();
        mobileNo=bundle.getString(MOBILE_NO);
        smsKey = bundle.getString(SMS_KEY);
        numOTP=bundle.getInt(OTP_CODE);
        edtOtp=findViewById(R.id.edtOTP);
        verify=findViewById(R.id.btnVerifyOtp);
        timerTv = findViewById(R.id.timer_tv);
        resendOtpBtn = findViewById(R.id.get_new_btn);
        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                verify.setText("Verifying..");
                String sOTP=edtOtp.getText().toString();
                if (TextUtils.isEmpty(sOTP)){
                    edtOtp.setError("Required Field");
//                    verify.setText("Verify");
                    return;
                }
                if (numOTP == Integer.parseInt(sOTP)) {
                    Intent intent = new Intent(OTPVerificationActivity.this, HomeActivity.class);
                    intent.putExtra(MOBILE_NO, mobileNo);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(OTPVerificationActivity.this, getString(R.string.incorrect_otp), Toast.LENGTH_SHORT).show();
                }
            }
        });

        startTimer();

    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(this,NewLoginActivity.class);
        startActivity(intent);
        finish();
    }


    private void startTimer() {
        new CountDownTimer(3000, 1000) {
            @Override
            public void onTick(long l) {
                timerTv.setText("00:"+l/1000 + "");
            }

            @Override
            public void onFinish() {
                resendOtpBtn.setEnabled(true);
                resendOtpBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            resendOtpBtn.setEnabled(false);
                            sendOTP();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

            }
        }.start();
    }


    private void sendOTP() throws Exception {

        ApiInterface apiInterface = getRetrofit().create(ApiInterface.class);
        String message = numOTP + " is OTP to login your account with Londri.io";
        String url = "http://api.msg91.com/api/sendhttp.php?";
        Call<String> stringCall = apiInterface.sendOTPSMS(url, "91", SMS_SENDER, "4", mobileNo,
                smsKey, message);
        stringCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @Nullable Response<String> response) {
                Toast.makeText(OTPVerificationActivity.this, getString(R.string.otp_resent), Toast.LENGTH_SHORT).show();
                startTimer();
//                finish();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(OTPVerificationActivity.this, "Something went wrong try after some time", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
