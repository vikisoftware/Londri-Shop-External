package com.vikisoft.londrishops.activity;

import androidx.appcompat.app.AppCompatActivity;

import io.fabric.sdk.android.Fabric;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.google.gson.Gson;
import com.vikisoft.londrishops.R;
import com.vikisoft.londrishops.api.ApiCall;
import com.vikisoft.londrishops.api.ApiInterface;
import com.vikisoft.londrishops.api.SMSCall;
import com.vikisoft.londrishops.helper.IfscResponce;
import com.vikisoft.londrishops.helper.LoginDataResponce;
import com.vikisoft.londrishops.utils.SessionManager;

import java.util.List;

import static com.vikisoft.londrishops.api.SMSCall.getRetrofit;
import static com.vikisoft.londrishops.utils.AppConstants.IFSC_URL;

public class BankDeailsActivity extends AppCompatActivity {
    private EditText ifscCode, bankName, branchName, holderName, accountNo, accountType;
    private LoginDataResponce data = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_bank_deails);
        ifscCode = findViewById(R.id.ifscCode);
        bankName = findViewById(R.id.bankName);
        ifscCode.requestFocus();
        branchName = findViewById(R.id.branchName);
        holderName = findViewById(R.id.holderName);
        accountNo = findViewById(R.id.accountNo);
        accountType = findViewById(R.id.accountType);
        final ApiInterface apiInterface = getRetrofit().create(ApiInterface.class);
        final SessionManager sessionManager = new SessionManager(BankDeailsActivity.this);

        data = sessionManager.getProfileJson();
        if (data != null) {

            if (data.getBankId() != null)
                if (data.getBankId() != 0) {
                    LoginDataResponce rr = data;
                    ifscCode.setText(rr.getIfsc());
                    bankName.setText(rr.getBankName());
                    branchName.setText(rr.getBracch());
                    holderName.setText(rr.getBenifName());
                    accountNo.setText(rr.getAccountNo());
                    accountType.setText(rr.getAccountType());
                }
        }
        ifscCode.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (TextUtils.isEmpty(ifscCode.getText().toString())) {
                        return;
                    } else {
                        Call<String> rest = (SMSCall.getRetrofit().create(ApiInterface.class)).getifecDetails(IFSC_URL + ifscCode.getText().toString());
                        rest.enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {
                                //Log.d("x", "onResponse: ");
                                if (response.body() != null) {
                                    IfscResponce res = new Gson().fromJson(response.body(), IfscResponce.class);
                                    bankName.setText(res.getBANK());
                                    branchName.setText(res.getBRANCH());
                                    ifscCode.setText(res.getIFSC());
                                }


                            }

                            @Override
                            public void onFailure(Call<String> call, Throwable t) {

                            }
                        });
                    }
                }
            }
        });
        Button save = findViewById(R.id.saveBank);
        ImageView back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sIfsc = ifscCode.getText().toString();
                String sBank = bankName.getText().toString();
                String sBranch = branchName.getText().toString();
                String sHolder = holderName.getText().toString();
                String sAccNo = accountNo.getText().toString();
                String sType = accountType.getText().toString();
                if (TextUtils.isEmpty(sIfsc)) {
                    ifscCode.setError(getString(R.string.required_field));
                    return;
                }
                if (TextUtils.isEmpty(sBank)) {
                    bankName.setError(getString(R.string.required_field));
                    return;
                }
                if (TextUtils.isEmpty(sBranch)) {
                    branchName.setError(getString(R.string.required_field));
                    return;
                }
                if (TextUtils.isEmpty(sHolder)) {
                    holderName.setError(getString(R.string.required_field));
                    return;
                }
                if (TextUtils.isEmpty(sAccNo)) {
                    accountNo.setError(getString(R.string.required_field));
                    return;
                }
                if (TextUtils.isEmpty(sType)) {
                    accountType.setError(getString(R.string.required_field));
                    return;
                }
                if (data != null) {
                    if (data.getBankId() != null) {
                        data.setAccountType(sType);
                        data.setIfsc(sIfsc);
                        data.setBankName(sBank);
                        data.setBracch(sBranch);
                        data.setBenifName(sHolder);
                        data.setAccountNo(sAccNo);
                    } else {
                        data.setBankId(0);
                        data.setAccountType(sType);
                        data.setIfsc(sIfsc);
                        data.setBankName(sBank);
                        data.setBracch(sBranch);
                        data.setBenifName(sHolder);
                        data.setAccountNo(sAccNo);
                    }
                }

                final Gson gson = new Gson();
                ApiInterface apiInterface1 = ApiCall.getRetrofit().create(ApiInterface.class);
                Call<LoginDataResponce> call = apiInterface1.saveProfile(gson.toJson(data));
                call.enqueue(new Callback<LoginDataResponce>() {
                    @Override
                    public void onResponse(Call<LoginDataResponce> call, Response<LoginDataResponce> response) {
                        Log.d("x", "onResponse: ");
                        if (response.body() != null) {
                            sessionManager.saveProfileJson(gson.toJson(response.body()));
                            finish();
                        } else
                            Toast.makeText(BankDeailsActivity.this, getString(R.string.network_problem), Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onFailure(Call<LoginDataResponce> call, Throwable t) {
                        Toast.makeText(BankDeailsActivity.this, getString(R.string.network_problem), Toast.LENGTH_SHORT).show();
                    }
                });


            }
        });
    }
}
