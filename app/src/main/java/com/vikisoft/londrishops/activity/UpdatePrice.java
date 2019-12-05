package com.vikisoft.londrishops.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.vikisoft.londrishops.R;
import com.vikisoft.londrishops.api.ApiCall;
import com.vikisoft.londrishops.api.ApiInterface;
import com.vikisoft.londrishops.helper.UpdatePriceResponce;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.vikisoft.londrishops.activity.OrderDetailsActivity.ORDER_ID;
import static com.vikisoft.londrishops.activity.ViewClothsActivity.PAYMENTID;
import static com.vikisoft.londrishops.activity.ViewClothsActivity.SERVICE_NAME;

public class UpdatePrice extends AppCompatActivity {

    private boolean valid = true;
    List<UpdatePriceResponce> newPriceList;
    List<UpdatePriceResponce> priceList;

    private String paymentId, serviceName;
    private TextView selectedOption, orderId;
    private ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_price);


        selectedOption = findViewById(R.id.selected_option_tv);
        orderId = findViewById(R.id.order_no_tv);
        back = findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        getReceivedIntent(getIntent());

        Integer id = Objects.requireNonNull(getIntent().getExtras()).getInt(ORDER_ID);
        final LinearLayout linearLayout = findViewById(R.id.root);
        Button applyChanges = findViewById(R.id.applyChanges);
        final ApiInterface apiInterface = ApiCall.getRetrofit().create(ApiInterface.class);
        Call<List<UpdatePriceResponce>> call = apiInterface.getClothDetails(id);
        call.enqueue(new Callback<List<UpdatePriceResponce>>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call<List<UpdatePriceResponce>> call, @NonNull Response<List<UpdatePriceResponce>> response) {
                assert response.body() != null;
                if (response.body().size() > 0) {
                    priceList = response.body();
                    newPriceList = new ArrayList<>();
                    for (final UpdatePriceResponce res : response.body()) {
                        View view = LayoutInflater.from(UpdatePrice.this).inflate(R.layout.view_price_update, null);
                        ImageView imageView = view.findViewById(R.id.clothImage);
                        TextView clothName = view.findViewById(R.id.clothName);
                        TextView mainPrice = view.findViewById(R.id.mainPrice);
                        final TextView appliedPrice = view.findViewById(R.id.appliedPrice);
                        clothName.setText(res.getClothName());
                        Glide.with(UpdatePrice.this).load(res.getClothImage()).into(imageView);
                        mainPrice.setText("Rs." + res.getMainPrice());
                        newPriceList.add(res);
                        appliedPrice.setText(res.getAppliedPrice() + "");
                        if (res.getMainPrice().contains("-")) {
                            mainPrice.setEnabled(false);
                            appliedPrice.setEnabled(true);

                        } else {
                            mainPrice.setEnabled(false);
                            appliedPrice.setEnabled(false);
                        }
                        final String[] price = res.getMainPrice().split("-");
                        appliedPrice.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                                String newPrice = appliedPrice.getText().toString();
                                if (TextUtils.isEmpty(newPrice)) {
                                    valid = false;
                                    return;
                                }
                                if (Double.parseDouble(newPrice) >= Integer.parseInt(price[0]) && Double.parseDouble(newPrice) <= Integer.parseInt(price[1])) {
                                    valid = true;
                                    for (UpdatePriceResponce dd : newPriceList) {
                                        if (dd.getId().equals(res.getId())) {
                                            dd.setAppliedPrice(Double.valueOf(newPrice));
                                        }
                                    }
                                } else valid = false;
                            }

                            @Override
                            public void afterTextChanged(Editable s) {

                            }
                        });
                        linearLayout.addView(view);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<UpdatePriceResponce>> call, @NonNull Throwable t) {

            }
        });
        applyChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (valid) {
                    double total = 0;
                    for (UpdatePriceResponce old : priceList) {
                        int flag = 1;
                        for (UpdatePriceResponce newP : newPriceList) {
                            if (newP.getId().equals(old.getId())) {
                                total += (newP.getClothCount() * newP.getAppliedPrice());
                                flag = 1;
                                break;
                            } else flag = 0;
                        }
                        if (flag == 0) {
                            total += (old.getClothCount() * old.getAppliedPrice());
                        }

                    }
                    AlertDialog.Builder builder = new AlertDialog.Builder(UpdatePrice.this);
                    builder.setTitle("Final Confirmation");
                    builder.setMessage("Final price of this order excluding other charges will be Rs." + total + "\nAre you sure to apply changes?");
                    builder.setPositiveButton("Yes, Apply", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Call<String> stringCall = apiInterface.updateNewPricing(new Gson().toJson(newPriceList));
                            stringCall.enqueue(new Callback<String>() {
                                @Override
                                public void onResponse(Call<String> call, Response<String> response) {
                                    assert response.body() != null;
                                    if (response.body().equals("done")) {
                                        Intent returnIntent = new Intent();
                                        setResult(Activity.RESULT_OK, returnIntent);
                                        finish();
                                    } else {
                                        Toast.makeText(UpdatePrice.this, getString(R.string.network_problem), Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<String> call, Throwable t) {
                                    Toast.makeText(UpdatePrice.this, getString(R.string.network_problem), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.show();
                }else
                    Toast.makeText(UpdatePrice.this, "Price you entered must be in valid range", Toast.LENGTH_SHORT).show();
            }
        });

    }


    private void getReceivedIntent(Intent intent) {
        paymentId = intent.getExtras().getInt(ORDER_ID) + "";
        serviceName = intent.getStringExtra(SERVICE_NAME);

        selectedOption.setText(serviceName);
        orderId.setText("#" + paymentId);
    }

    @Override
    public void onBackPressed() {
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, returnIntent);
        finish();

    }
}
