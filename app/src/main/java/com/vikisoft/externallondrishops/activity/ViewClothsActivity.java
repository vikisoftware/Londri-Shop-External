package com.vikisoft.externallondrishops.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.vikisoft.externallondrishops.R;
import com.vikisoft.externallondrishops.adapter.ClothsPagerAdapter;
import com.vikisoft.externallondrishops.fragments.ViewAdultClothsFragment;
import com.vikisoft.externallondrishops.fragments.ViewHouseholdClothsFragment;
import com.vikisoft.externallondrishops.fragments.ViewKidsClothsFragment;
import com.vikisoft.externallondrishops.helper.ClothCountResponse;


import java.util.List;


public class ViewClothsActivity extends AppCompatActivity {


    private ViewPager viewPager;
    private String paymentId = "0";
    private String serviceName;
    private List<ClothCountResponse> clothsList;
    public static final String PAYMENTID = "paymentId";
    private ImageView back;
    private TextView selectedOption, orderId;
    public static final String SERVICE_ID = "service_id";
    public static final String SERVICE_NAME = "service_name";


    public static final String LAUNDRY_LIST = "laundri_list";
    public static final int LAUNDRY_SELECTION_REQUEST_CODE = 402;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_cloths);

        init();
        getReceivedIntent(getIntent());
        setAdapter();
    }

    private void getReceivedIntent(Intent intent) {
        paymentId = intent.getStringExtra(PAYMENTID);
        serviceName = intent.getStringExtra(SERVICE_NAME);

        selectedOption.setText(serviceName);
        orderId.setText("#" + paymentId);
    }

    private void setAdapter() {


        ViewAdultClothsFragment fragobj = new ViewAdultClothsFragment(paymentId);
        ViewKidsClothsFragment fragobj2 = new ViewKidsClothsFragment(paymentId);
        ViewHouseholdClothsFragment fragobj3 = new ViewHouseholdClothsFragment(paymentId);


        ClothsPagerAdapter adapter = new ClothsPagerAdapter(getSupportFragmentManager(), paymentId);
        adapter.addFragment(fragobj, "Adult");
        adapter.addFragment(fragobj2, "Kid");
        adapter.addFragment(fragobj3, "HouseHold");
        viewPager.setAdapter(adapter);
    }

    private void init() {
        viewPager = findViewById(R.id.viewpager);
        TabLayout tab = findViewById(R.id.tabs);
        tab.setupWithViewPager(viewPager);
        selectedOption = findViewById(R.id.selected_option_tv);
        orderId = findViewById(R.id.order_no_tv);
        back = findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


}
