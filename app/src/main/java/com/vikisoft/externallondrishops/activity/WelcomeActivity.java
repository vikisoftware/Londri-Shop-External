package com.vikisoft.externallondrishops.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.TransitionDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.vikisoft.externallondrishops.R;
import com.vikisoft.externallondrishops.api.ApiCall;
import com.vikisoft.externallondrishops.api.ApiInterface;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        LinearLayout lable = findViewById(R.id.welcmlogoname);
        ImageView image = (ImageView) findViewById(R.id.mySchoolrLogo);
        Animation myFadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.logo_fadein);
        //image.setEnterTransition(fade);
        //lable.setAnimation(myFadeInAnimation);
        image.startAnimation(myFadeInAnimation);
        image.setImageResource(R.drawable.logo_animation);
        ((TransitionDrawable) image.getDrawable()).startTransition(1000);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 5s = 5000ms
                ApiInterface apiInterface= ApiCall.getRetrofit().create(ApiInterface.class);
                Call<String> call =apiInterface.getVersion("Londri Shop");
                call.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if (response.body()==null){
                            startActivity(new Intent(WelcomeActivity.this, NewLoginActivity.class));
                            finish();
                        }else{
                            String currentVersion=null;
                            try {
                                currentVersion = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            if (currentVersion!=null)
                                if (Float.valueOf(currentVersion)<Float.valueOf(response.body())){
                                    AlertDialog.Builder builder=new AlertDialog.Builder(WelcomeActivity.this,AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
                                    builder.setCancelable(false);
                                    builder.setTitle("Update Available").setMessage("To enjoy new features please update your app")
                                            .setNegativeButton("Later", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                    startActivity(new Intent(WelcomeActivity.this, NewLoginActivity.class));
                                                    finish();
                                                }
                                            })
                                            .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                                                    try {
                                                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                                                    } catch (android.content.ActivityNotFoundException anfe) {
                                                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                                                    }
                                                }
                                            }).show();
                                }else{
                                    startActivity(new Intent(WelcomeActivity.this, NewLoginActivity.class));
                                    finish();
                                }
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        startActivity(new Intent(WelcomeActivity.this, NewLoginActivity.class));
                        finish();
                    }
                });
            }
        }, 1500);

        // ATTENTION: This was auto-generated to handle app links.
        Intent appLinkIntent = getIntent();
        String appLinkAction = appLinkIntent.getAction();
        Uri appLinkData = appLinkIntent.getData();

    }
}
