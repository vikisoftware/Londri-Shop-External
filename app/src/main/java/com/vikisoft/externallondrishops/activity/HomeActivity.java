package com.vikisoft.externallondrishops.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import io.fabric.sdk.android.Fabric;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.balysv.materialripple.MaterialRippleLayout;
import com.crashlytics.android.Crashlytics;
import com.google.gson.Gson;
import com.vikisoft.externallondrishops.R;
import com.vikisoft.externallondrishops.api.ApiInterface;
import com.vikisoft.externallondrishops.fragments.HomeFragment;
import com.vikisoft.externallondrishops.fragments.OrderFragment;
import com.vikisoft.externallondrishops.fragments.ProfileFragment;
import com.vikisoft.externallondrishops.fragments.ServiceFragment;
import com.vikisoft.externallondrishops.helper.LoginDataResponce;
import com.vikisoft.externallondrishops.utils.SessionManager;

import static com.vikisoft.externallondrishops.api.ApiCall.getRetrofit;
import static com.vikisoft.externallondrishops.utils.SessionManager.MOBILE_NO;

public class HomeActivity extends AppCompatActivity {
    public static final String SERVICE_FILTER="ser_fil";
    private String mobileNo = null;
    private LinearLayout linearLayout;
    private ProgressBar progressBar;
    private FragmentTransaction ft;
    private TextView home, order, services, profile;
    private String currentVersion;
    private SessionManager sessionManager;
    private ImageView homeIv, orderIv, mapIv, profileIv;
    private HomeFragment homeFragment;
    private ServiceFragment serviceFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this,new Crashlytics());
        setContentView(R.layout.activity_home);

        try {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
//            WindowUiChanges.setStatusBarColor(getWindow(), ContextCompat.getColor(this, R.color.white));

            home = findViewById(R.id.home);
            order = findViewById(R.id.order);
            services = findViewById(R.id.services);
            profile = findViewById(R.id.profile);
            homeIv = findViewById(R.id.home_seleted_iv);
            orderIv = findViewById(R.id.order_seleted_iv);
            mapIv = findViewById(R.id.map_seleted_iv);
            profileIv = findViewById(R.id.profile_seleted_iv);

            sessionManager = new SessionManager(this);
            MaterialRippleLayout nav_home = findViewById(R.id.nav_home);
            MaterialRippleLayout nav_order = findViewById(R.id.nav_order);
            MaterialRippleLayout nav_profile = findViewById(R.id.nav_profile);
            MaterialRippleLayout nav_payment = findViewById(R.id.nav_payment);
            progressBar = findViewById(R.id.proressbarHome);
            linearLayout = findViewById(R.id.linearLayoutHome);
            linearLayout.setVisibility(View.GONE);
            linearLayout.setVisibility(View.VISIBLE);
            final FragmentManager fm = getSupportFragmentManager();
            ft = fm.beginTransaction();



            setTabIconColor();
            homeIv.setColorFilter(ContextCompat.getColor(HomeActivity.this, R.color.figmaGreen), PorterDuff.Mode.SRC_IN);

            tabTextColor();

            homeFragment = new HomeFragment();
            serviceFragment = new ServiceFragment();
            serviceFragment.setFragmentListener(new ServiceFragment.FragmentDisplaylistener() {
                @Override
                public void currentFragment(int position) {
                    setIconColor(2);
                }
            });
            homeFragment.setFragmentListener(new HomeFragment.FragmentDisplaylistener() {
                @Override
                public void currentFragment(int position) {
                    setIconColor(0);
                }
            });

            nav_home.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (sessionManager.getProfileJson() != null)
                         {
                            ft = fm.beginTransaction();
                            ft.replace(R.id.homeFrame, homeFragment);
                            ft.commit();
                        }

                    setIconColor(0);

                }
            });

            nav_order.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    ft = fm.beginTransaction();
                    Bundle bundle=new Bundle();
                    bundle.putInt(SERVICE_FILTER,0);
                    OrderFragment of=new OrderFragment();
                    of.setArguments(bundle);
                    ft.replace(R.id.homeFrame, of);
                    ft.commit();

                    setIconColor(1);

                }
            });

            nav_payment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (sessionManager.getProfileJson() != null)
                         {
                            ft = fm.beginTransaction();
//                            ft.replace(R.id.homeFrame, new PaymentFragment());
                             ft.replace(R.id.homeFrame, serviceFragment);
                             ft.addToBackStack(serviceFragment.getClass().toString());
                            ft.commit();
                        }


                    setIconColor(2);

                }
            });


            nav_profile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    ft = fm.beginTransaction();
                    ft.replace(R.id.homeFrame, new ProfileFragment());
                    ft.addToBackStack(new ProfileFragment().getClass().toString());
                    ft.commit();

                    setIconColor(3);

                }
            });


            Bundle bundle = getIntent().getExtras();
            if (bundle != null)
                mobileNo = bundle.getString(MOBILE_NO);
            final ApiInterface apiInterface = getRetrofit().create(ApiInterface.class);


            if (mobileNo != null) {
                getLogin(ft, apiInterface);
            }

            else {
                sessionManager.logout(HomeActivity.this);
            }
        } catch (Exception e) {
            e.printStackTrace();
              Crashlytics.logException(e);
        }

    }



    private void getLogin(final FragmentTransaction ft, ApiInterface apiInterface) throws Exception {

        Call<LoginDataResponce> call=apiInterface.getLogin(mobileNo);
        call.enqueue(new Callback<LoginDataResponce>() {
            @Override
            public void onResponse(Call<LoginDataResponce> call, Response<LoginDataResponce> response) {
                linearLayout.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                if (response.body()!=null){

                    sessionManager.saveProfileJson(new Gson().toJson(response.body()));

                    ft.replace(R.id.homeFrame,homeFragment);
                    ft.commit();
                    home.setTextColor(getResources().getColor(R.color.darkGreen));
                    order.setTextColor(getResources().getColor(R.color.black));
                    profile.setTextColor(getResources().getColor(R.color.black));
                }else {

                    // homeView3.setVisibility(View.INVISIBLE);

                    ProfileFragment frag=new ProfileFragment();

                    LoginDataResponce data=new LoginDataResponce();
                    data.setMobileNo(mobileNo);
                    data.setShopMobile(mobileNo);
                    sessionManager.saveProfileJson(new Gson().toJson(data));
                    ft.replace(R.id.homeFrame,frag);
                    ft.commit();
                }
            }

            @Override
            public void onFailure(Call<LoginDataResponce> call, Throwable t) {
                Toast.makeText(HomeActivity.this, getString(R.string.network_problem), Toast.LENGTH_SHORT).show();
            }
        });


    }



    private void tabTextColor() {
       /* if (theme.equals(DARK_THEME)) {
            home.setTextColor(getResources().getColor(R.color.mdtp_white));
            order.setTextColor(getResources().getColor(R.color.mdtp_white));
            payment.setTextColor(getResources().getColor(R.color.mdtp_white));
            profile.setTextColor(getResources().getColor(R.color.mdtp_white));
        } else {*/
            home.setTextColor(getResources().getColor(R.color.black));
            order.setTextColor(getResources().getColor(R.color.black));
            services.setTextColor(getResources().getColor(R.color.black));
            profile.setTextColor(getResources().getColor(R.color.black));
//        }
    }


    private void setTabIconColor() {
//        if (theme.equals(LIGHT_THEME)) {
            homeIv.setColorFilter(ContextCompat.getColor(HomeActivity.this, R.color.figmaGrey), PorterDuff.Mode.SRC_IN);
            orderIv.setColorFilter(ContextCompat.getColor(HomeActivity.this, R.color.figmaGrey), PorterDuff.Mode.SRC_IN);
            mapIv.setColorFilter(ContextCompat.getColor(HomeActivity.this, R.color.figmaGrey), PorterDuff.Mode.SRC_IN);
            profileIv.setColorFilter(ContextCompat.getColor(HomeActivity.this, R.color.figmaGrey), PorterDuff.Mode.SRC_IN);
//            bottomNavLl.setBackgroundColor(getResources().getColor(R.color.mdtp_white));

        /*} else {
            homeIv.setColorFilter(ContextCompat.getColor(HomeActivity.this, R.color.white), PorterDuff.Mode.SRC_IN);
            orderIv.setColorFilter(ContextCompat.getColor(HomeActivity.this, R.color.white), PorterDuff.Mode.SRC_IN);
            mapIv.setColorFilter(ContextCompat.getColor(HomeActivity.this, R.color.white), PorterDuff.Mode.SRC_IN);
            profileIv.setColorFilter(ContextCompat.getColor(HomeActivity.this, R.color.white), PorterDuff.Mode.SRC_IN);
        }*/
    }

    public void setIconColor(int position) {

        tabTextColor();

        setTabIconColor();

        switch (position) {
            case 0:
                homeIv.setColorFilter(ContextCompat.getColor(HomeActivity.this, R.color.figma_green), PorterDuff.Mode.SRC_IN);
                home.setTextColor(getResources().getColor(R.color.figma_green));
                break;

            case 1:
                orderIv.setColorFilter(ContextCompat.getColor(HomeActivity.this, R.color.figma_green), PorterDuff.Mode.SRC_IN);
                order.setTextColor(getResources().getColor(R.color.figma_green));
                break;

            case 2:
                mapIv.setColorFilter(ContextCompat.getColor(HomeActivity.this, R.color.figma_green), PorterDuff.Mode.SRC_IN);
                services.setTextColor(getResources().getColor(R.color.figma_green));
                break;

            case 3:
                profileIv.setColorFilter(ContextCompat.getColor(HomeActivity.this, R.color.figma_green), PorterDuff.Mode.SRC_IN);
                profile.setTextColor(getResources().getColor(R.color.figma_green));
                break;
        }
    }
}
