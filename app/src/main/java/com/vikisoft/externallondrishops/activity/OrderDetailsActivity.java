package com.vikisoft.externallondrishops.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.makeramen.roundedimageview.RoundedImageView;
import com.vikisoft.externallondrishops.R;
import com.vikisoft.externallondrishops.api.ApiInterface;
import com.vikisoft.externallondrishops.api.FMCApi;
import com.vikisoft.externallondrishops.helper.OrderAcceptHelper;
import com.vikisoft.externallondrishops.helper.OrdersListResponce;
import com.vikisoft.externallondrishops.helper.ServiceListResponce;
import com.vikisoft.externallondrishops.helper.firebasenotification.Data;
import com.vikisoft.externallondrishops.helper.firebasenotification.Notification;
import com.vikisoft.externallondrishops.helper.firebasenotification.NotificationResponce;
import com.vikisoft.externallondrishops.utils.SessionManager;

import org.jetbrains.annotations.NotNull;

import java.text.Format;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.vikisoft.externallondrishops.activity.ViewClothsActivity.PAYMENTID;
import static com.vikisoft.externallondrishops.activity.ViewClothsActivity.SERVICE_NAME;
import static com.vikisoft.externallondrishops.api.ApiCall.getRetrofit;
import static com.vikisoft.externallondrishops.fragments.OrderFragment.SELECTED_ORDER;


public class OrderDetailsActivity extends AppCompatActivity {

    public static final String ORDER_ID = "order_id";
    OrdersListResponce data;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        Gson gson = new Gson();
        data = gson.fromJson(Objects.requireNonNull(getIntent().getExtras()).getString(SELECTED_ORDER), OrdersListResponce.class);
        if (data == null)
            onBackPressed();
        initWork();
    }

    private void initWork() {

        //Button payNow;

        RoundedImageView boyImgd = findViewById(R.id.dilevery_boy_iv);
        ImageView backButton = findViewById(R.id.back);
        TextView serviceName = findViewById(R.id.serviceName);
        TextView status = findViewById(R.id.status);
        TextView adultCount = findViewById(R.id.adultCount);
        TextView adultPrice = findViewById(R.id.adultPrice);
        TextView kidCount = findViewById(R.id.kidCount);
        TextView kidPrice = findViewById(R.id.kidPrice);
        TextView houseCount = findViewById(R.id.houseCount);
        TextView housePrice = findViewById(R.id.housePrice);
        TextView totalPrice = findViewById(R.id.totalPrice);



        TextView deliveryCharge = findViewById(R.id.deliveryCharge);
        TextView orderPaid = findViewById(R.id.orderPaid);
        TextView boyNamed = findViewById(R.id.laundry_boy_name);
        TextView pickUpDate = findViewById(R.id.pickup_date_tv);
        TextView deliveryDate = findViewById(R.id.delivery_date_tv);
        TextView pickUpAddress = findViewById(R.id.pick_up_address_tv);
        Button btnUpdatePrice = findViewById(R.id.payNow);
        RelativeLayout btnUpdatePriceLay = findViewById(R.id.rlChangePrice);
        btnUpdatePriceLay.setVisibility(View.GONE);
        Button shopStatus = findViewById(R.id.shopStatus);
        Button viewClothesBtn = findViewById(R.id.view_cloths_btn);
        //    private List<ServiceListResponce> serviceList;
        RelativeLayout goBackRl = findViewById(R.id.go_back_rl);
        RelativeLayout btnUserNavigation = findViewById(R.id.btnUserNavigation);
        goBackRl.setOnClickListener(view -> finish());
        btnUserNavigation.setOnClickListener(v -> {
            if (data.getLatitude() != 0 && data.getLatitude() != 0.0) {
                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://maps.google.com/maps?daddr=" + data.getLatitude() + "," + data.getLongitude() + ""));
                startActivity(intent);
            }
        });

        getServiceList();


        Format n = NumberFormat.getCurrencyInstance(new Locale("en", "in"));
        try {


            //Date dDate=dateFormat.parse(data.getDeliveryDate());
            //String ddd=dateFormat2.format(dDate);
            serviceName.setText(data.getServiceName());
            status.setText(data.getStatus1());
            adultCount.setText(String.valueOf(data.getAdultCount()));
            adultPrice.setText(n.format(data.getAdultPrice()));
            kidCount.setText(String.valueOf(data.getKidCount()));
            kidPrice.setText(n.format(data.getKidPrice()));
            houseCount.setText(String.valueOf(data.getHouseCount()));
            housePrice.setText(n.format(data.getHousePrice()));
            totalPrice.setText(n.format(data.getFinalAmount()));

            btnUpdatePrice.setOnClickListener(v -> {
                Intent intent = new Intent(OrderDetailsActivity.this, UpdatePrice.class);
                intent.putExtra(ORDER_ID, data.getPaymentId());
                intent.putExtra(SERVICE_NAME, data.getServiceName());
                startActivityForResult(intent, 1000);
//                   setPrices();

            });
            if (data.getPaymentStatus().equals("captured")) {
                orderPaid.setText(getString(R.string.paid));
                //payNow.setVisibility(View.GONE);
                orderPaid.setWidth(40);
                orderPaid.setBackground(getResources().getDrawable(R.drawable.button_shape));
            } else {
                orderPaid.setText(getString(R.string.unpaid));
                orderPaid.setWidth(70);
                orderPaid.setBackground(getResources().getDrawable(R.drawable.button_shape_red));
            }

            orderPaid.setVisibility(View.GONE);

            deliveryCharge.setText(n.format(data.getFinalAmount() - (data.getAdultPrice() + data.getKidPrice() + data.getHousePrice())));

            pickUpAddress.setText(data.getDeliveryAddress());
            //pickUpDate.setText(ddd);
            //pickupDate.setVisibility(View.GONE);
            pickUpDate.setText(data.getPickupDate());
            deliveryDate.setText(data.getDeliveryDate());
            boyNamed.setText(data.getBoyName());
            if (!data.getPhotoUrl().equals("-")) {

                Glide.with(this).load(data.getUserPhoto()).into(boyImgd);
            }

            backButton.setOnClickListener(v -> onBackPressed());
            if (data.getStatus1().equals("Processing")) {
                btnUpdatePriceLay.setVisibility(View.VISIBLE);
//                shopStatus.setVisibility(View.VISIBLE);
                shopStatus.setOnClickListener(v -> {
                    final OrderAcceptHelper helper = new OrderAcceptHelper();
                    helper.setImages(null);
                    helper.setId(data.getPaymentId());
                    helper.setStatus("Out for Delivery");
                    //saveData(helper);
                    ApiInterface apiInterface = getRetrofit().create(ApiInterface.class);
                    Call<String> call = apiInterface.changeStatus(new Gson().toJson(helper));
                    call.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(@NonNull Call<String> call,@NonNull Response<String> response) {
                            if (response.body() != null)
                                if (response.body().equals("done")) {
                                    Toast.makeText(OrderDetailsActivity.this, "Done", Toast.LENGTH_SHORT).show();
                                    final ApiInterface apiService =
                                            FMCApi.getClient().create(ApiInterface.class);
                                    @SuppressLint("DefaultLocale") Data data1 = new Data("Order Status", "Order #" + String.format("%06d", helper.getId()) + " is " + helper.getStatus());
                                    Notification notification = new Notification(data1, "/topics/order_status_" + data.getPaymentId());

                                    final Call<NotificationResponce> notificationResponceCall = apiService.sendNotification(notification);

                                    notificationResponceCall.enqueue(new Callback<NotificationResponce>() {
                                        @Override
                                        public void onResponse(@NonNull Call<NotificationResponce> call,@NonNull Response<NotificationResponce> response) {


                                            //NotificationResponce notificationResponce = response.body();

//                        Toast.makeText(getContext(),"Message ID is "+notificationResponce.getMessageId().toString(),Toast.LENGTH_LONG).show();
                                        }

                                        @Override
                                        public void onFailure(@NonNull Call<NotificationResponce> call,@NonNull Throwable t) {
//                        Toast.makeText(getContext(),"admin did not notification",Toast.LENGTH_LONG).show();
                                        }
                                    });
                                    finish();
                                } else
                                    Toast.makeText(OrderDetailsActivity.this, "Error", Toast.LENGTH_SHORT).show();
                            else
                                Toast.makeText(OrderDetailsActivity.this, "Error", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(@NonNull Call<String> call,@NonNull Throwable t) {
                            Toast.makeText(OrderDetailsActivity.this, "Error", Toast.LENGTH_SHORT).show();
                        }
                    });
                });
            } else {
                btnUpdatePriceLay.setVisibility(View.GONE);
            }




            viewClothesBtn.setOnClickListener(view -> {
                Intent intent = new Intent(OrderDetailsActivity.this, ViewClothsActivity.class);
                intent.putExtra(PAYMENTID, data.getPaymentId() + "");
                intent.putExtra(SERVICE_NAME, data.getServiceName());
                startActivity(intent);
            });


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*private void setPrices() {
        Intent intent = new Intent(OrderDetailsActivity.this, ServicePrice.class);
        if (getService() > -1) {
            intent.putExtra(SERVICE, new Gson().toJson(serviceList.get(getService())));
            startActivity(intent);//, options.toBundle());
        }

    }*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data1) {
        if (resultCode == RESULT_OK) {
            Call<List<OrdersListResponce>> call = (getRetrofit().create(ApiInterface.class)).refreshOrder(String.valueOf(data.getPaymentId()));
            //Toast.makeText(OrderDetailsActivity.this, "Swipe", Toast.LENGTH_SHORT).show();
            call.enqueue(new Callback<List<OrdersListResponce>>() {
                @Override
                public void onResponse(@NonNull Call<List<OrdersListResponce>> call,@NonNull Response<List<OrdersListResponce>> response) {

                    if (response.body() != null)
                        if (response.body().size() != 0) {
                            data = response.body().get(0);
                            data.setPickupDate(data.getDeliveryDate());
                            initWork();
                        }

                }

                @Override
                public void onFailure(@NonNull Call<List<OrdersListResponce>> call,@NonNull Throwable t) {

                    Toast.makeText(OrderDetailsActivity.this, "Network Error", Toast.LENGTH_SHORT).show();
                }
            });
        }

        super.onActivityResult(requestCode, resultCode, data1);
    }

    private void getServiceList() {
        final SessionManager sessionManager = new SessionManager(OrderDetailsActivity.this);
        ApiInterface apiInterface = getRetrofit().create(ApiInterface.class);
        Call<List<ServiceListResponce>> call = apiInterface.getServiceList(String.valueOf(sessionManager.getLondriId()));
        call.enqueue(new Callback<List<ServiceListResponce>>() {
            @Override
            public void onResponse(@NotNull Call<List<ServiceListResponce>> call, @NotNull Response<List<ServiceListResponce>> response) {
                if (response.body() != null)
                    if (response.body().size() != 0) {
                        sessionManager.setService(new Gson().toJson(response.body()));
                        //serviceList = response.body();
                    } else
                        Toast.makeText(OrderDetailsActivity.this, "Service List Error", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(OrderDetailsActivity.this, "Service List Error", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(@NotNull Call<List<ServiceListResponce>> call, @NotNull Throwable t) {
                Toast.makeText(OrderDetailsActivity.this, "Service List Error", Toast.LENGTH_SHORT).show();
            }
        });
    }


    /*private int getService() {
        if (serviceList != null && serviceList.size() > 0)
        for (int i = 0; i < serviceList.size(); i++) {
            if (data.getServiceName().equals(serviceList.get(i).getServiceName())) {
                return i;
            }
        }

        return -1;
    }*/
}
