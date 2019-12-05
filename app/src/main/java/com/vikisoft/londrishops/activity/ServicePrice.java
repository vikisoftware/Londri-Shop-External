package com.vikisoft.londrishops.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.vikisoft.londrishops.R;
import com.vikisoft.londrishops.adapter.PickUpListAdpater;
import com.vikisoft.londrishops.fragments.AdultFragment;
import com.vikisoft.londrishops.fragments.HouseFragment;
import com.vikisoft.londrishops.fragments.KidFragment;
import com.vikisoft.londrishops.helper.ServiceListResponce;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.vikisoft.londrishops.fragments.ProfileFragment.SERVICE;

public class ServicePrice extends AppCompatActivity {
    private ImageView backIv, clothIv;
    private TextView selectedOptionTitleTv, orderNoTv;
    private ServiceListResponce myService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_price);

        init();


        ViewPager viewPager = findViewById(R.id.viewpager);
        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        String data= Objects.requireNonNull(getIntent().getExtras()).getString(SERVICE);

        myService= new Gson().fromJson(data, ServiceListResponce.class);
        selectedOptionTitleTv.setText(myService.getServiceName());
        setImage();

        setupViewPager(viewPager,data);
    }


    private void init() {
        backIv = findViewById(R.id.back);
        selectedOptionTitleTv = findViewById(R.id.selected_option_tv);
        orderNoTv = findViewById(R.id.order_no_tv);
        clothIv = findViewById(R.id.cloth_img);
        backIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


    private void setupViewPager(ViewPager viewPager, String data) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        AdultFragment af=new AdultFragment();
        KidFragment kf=new KidFragment();
        HouseFragment hf=new HouseFragment();
        Bundle bundle=new Bundle();
        bundle.putString(SERVICE,data);
        af.setArguments(bundle);
        kf.setArguments(bundle);
        hf.setArguments(bundle);
        adapter.addFragment(af, "Adult");
        adapter.addFragment(kf, "Kids");
        adapter.addFragment(hf, "House");
        viewPager.setAdapter(adapter);
    }
    class ViewPagerAdapter extends FragmentPagerAdapter{
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }
        @NonNull
        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }
        void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    public void onBackPressed() {

        //SessionManager sessionManager=new SessionManager(this);
        /* if (sessionManager.getPriceCount()==0){*/
        super.onBackPressed();

        /*}else if (sessionManager.getPriceCount()<3){
            AlertDialog.Builder builder=new AlertDialog.Builder(this, android.app.AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
            builder.setTitle("Alert");
            builder.setMessage("You must set Price of all (Adult, Kid, Household) clothes");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.show();

        }else {
            super.onBackPressed();*/
        //}
    }

    private void setImage() {
        switch (myService.getServiceName()) {
            case "Wash and Fold":
                clothIv.setImageResource(R.drawable.ic_wash_fold);
                orderNoTv.setText(("(2 Days)"));
                break;
            case "Wash and Iron":
                clothIv.setImageResource(R.drawable.ic_wash_iron);
                orderNoTv.setText(("(1 Day)"));
                break;
            case "Dry Clean and Hang":
                clothIv.setImageResource(R.drawable.ic_jacket);
                orderNoTv.setText(("(3 Days)"));
                break;
            case "Dry Clean":
                clothIv.setImageResource(R.drawable.ic_hang);
                orderNoTv.setText(("(3 Days)"));
                break;
            case "Iron Only":
                clothIv.setImageResource(R.drawable.ic_iron);
                orderNoTv.setText(("(1 Day)"));
                break;
        }
    }
}
