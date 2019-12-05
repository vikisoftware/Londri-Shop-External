package com.vikisoft.londrishops.helper;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;

import com.google.gson.Gson;
import com.vikisoft.londrishops.R;
import com.vikisoft.londrishops.activity.LoginActivity;
import com.vikisoft.londrishops.activity.NewLoginActivity;
import com.vikisoft.londrishops.activity.WelcomeActivity;
import com.vikisoft.londrishops.api.ApiInterface;
import com.vikisoft.londrishops.utils.SessionManager;

import java.util.List;

import androidx.core.app.NotificationCompat;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.vikisoft.londrishops.api.ApiCall.getRetrofit;

public class OrderService extends Service {
    public Context context = this;
    public Handler handler = null;
    public static Runnable runnable = null;

    public OrderService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }

    @Override
    public void onCreate() {
        //Toast.makeText(this, "Service created!", Toast.LENGTH_LONG).show();
        final SessionManager sessionManager = new SessionManager(context);
        final ApiInterface apiInterface = getRetrofit().create(ApiInterface.class);
        final Gson gson = new Gson();
        final String CHANNEL_ID = "10130";
        handler = new Handler();
        runnable = new Runnable() {
            public void run() {
                //Toast.makeText(context, "Service is still running", Toast.LENGTH_LONG).show();
                if (sessionManager.getProfileJson() != null)
                     {
                        Call<List<OrdersListResponce>> call = apiInterface.getOrders(String.valueOf(sessionManager.getProfileJson().getShopId()));
                        call.enqueue(new Callback<List<OrdersListResponce>>() {
                            @Override
                            public void onResponse(Call<List<OrdersListResponce>> call, Response<List<OrdersListResponce>> response) {
                                if (response.body() != null)
                                    if (response.body().size() != 0) {
                                        if (sessionManager.getOrderList() != null)
                                            if (sessionManager.getOrderList().size() < response.body().size()) {
                                                Intent intent = new Intent(context, WelcomeActivity.class);
                                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

                                                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                                                        .setSmallIcon(R.drawable.playstore_icon)
                                                        .setContentTitle("Order")
                                                        .setContentText("New Orders for your Londri")
                                                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                                        // Set the intent that will fire when the user taps the notification
                                                        .setContentIntent(pendingIntent)
                                                        .setAutoCancel(true);
                                                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                                    NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, getString(R.string.channel_name), NotificationManager.IMPORTANCE_HIGH);

                                                    notificationManager.createNotificationChannel(mChannel);
                                                    mBuilder.setChannelId(CHANNEL_ID);
                                                }

                                                notificationManager.notify(1002, mBuilder.build());
                                            }
                                        sessionManager.saveOrderList(gson.toJson(response.body()));
                                    }
                            }

                            @Override
                            public void onFailure(Call<List<OrdersListResponce>> call, Throwable t) {

                            }
                        });
                    }
                handler.postDelayed(runnable, 30000);
            }
        };

        handler.postDelayed(runnable, 30000);
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Intent restartServiceIntent = new Intent(getApplicationContext(), this.getClass());
        restartServiceIntent.setPackage(getPackageName());

        PendingIntent restartServicePendingIntent = PendingIntent.getService(getApplicationContext(), 1, restartServiceIntent, PendingIntent.FLAG_ONE_SHOT);
        AlarmManager alarmService = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        alarmService.set(
                AlarmManager.ELAPSED_REALTIME,
                SystemClock.elapsedRealtime() + 1000,
                restartServicePendingIntent);

        super.onTaskRemoved(rootIntent);
    }
}
