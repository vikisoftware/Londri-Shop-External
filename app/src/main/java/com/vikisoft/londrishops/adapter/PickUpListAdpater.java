package com.vikisoft.londrishops.adapter;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.google.android.gms.common.api.Api;
import com.google.gson.Gson;
import com.vikisoft.londrishops.R;
import com.vikisoft.londrishops.activity.HomeActivity;
import com.vikisoft.londrishops.activity.ServicePrice;
import com.vikisoft.londrishops.api.ApiInterface;
import com.vikisoft.londrishops.helper.ServiceListResponce;
import com.vikisoft.londrishops.utils.SessionManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.vikisoft.londrishops.api.ApiCall.getRetrofit;
import static com.vikisoft.londrishops.fragments.ProfileFragment.SERVICE;

public class PickUpListAdpater extends RecyclerView.Adapter<PickUpListAdpater.MyViewViewHolder> {

    private List<ServiceListResponce> serviceList;
    private Context context;
    private SessionManager sessionManager;
    private Activity activity;


    public PickUpListAdpater(List<ServiceListResponce> serviceList, Context context, Activity activity) {
        this.serviceList = serviceList;
        this.context = context;
        this.activity = activity;
        sessionManager = new SessionManager(context);
    }

    @NonNull
    @Override
    public MyViewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_pickup, parent, false);
        return new MyViewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewViewHolder holder, final int position) {

        final int londriId = sessionManager.getLondriId();
        final ServiceListResponce service = serviceList.get(position);

        if (service.getAccess().equals("Y"))
        {
            holder.checkBox.setChecked(true);
        }
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    addService(service, "Y", londriId);
                } else {
                   openDialog(service, londriId, holder);
                }
            }
        });

        holder.serviceNameTv.setText(service.getServiceName());

        setImage(holder, position);

        holder.arrowIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSetRangePrices(service, holder);
            }
        });
    }

    private void openSetRangePrices(ServiceListResponce service, MyViewViewHolder holder) {
        /*Intent intent = new Intent(context, ServicePrice.class);
        intent.putExtra(SERVICE, new Gson().toJson(service));
        context.startActivity(intent);*/

        sessionManager.setPriceCount(0);
        Pair[] pair = new Pair[1];
        pair[0] = new Pair<View, String>(holder.serviceNameTv, "serviceTransition");
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(activity, pair);
        Intent intent = new Intent(context, ServicePrice.class);
        intent.putExtra(SERVICE, new Gson().toJson(service));
        context.startActivity(intent, options.toBundle());
    }

    private void setImage(MyViewViewHolder holder, int position) {
        switch (position) {
            case 0:
                holder.clothIv.setImageResource(R.drawable.ic_wash_fold);
                holder.serviceTimeTv.setText(("(2 Days)"));
                break;
            case 1:
                holder.clothIv.setImageResource(R.drawable.ic_wash_iron);
                holder.serviceTimeTv.setText(("(1 Day)"));
                break;
            case 2:
                holder.clothIv.setImageResource(R.drawable.ic_jacket);
                holder.serviceTimeTv.setText(("(3 Days)"));
                break;
            case 3:
                holder.clothIv.setImageResource(R.drawable.ic_hang);
                holder.serviceTimeTv.setText(("(3 Days)"));
                break;
            case 4:
                holder.clothIv.setImageResource(R.drawable.ic_iron);
                holder.serviceTimeTv.setText(("(1 Day)"));
                break;
        }
    }

    @Override
    public int getItemCount() {
        return serviceList.size();
    }

    public class MyViewViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkBox;
        TextView serviceNameTv, serviceTimeTv;
        ImageView clothIv, arrowIv;
        public MyViewViewHolder(@NonNull View itemView) {
            super(itemView);

            checkBox = itemView.findViewById(R.id.check_box);
            serviceNameTv = itemView.findViewById(R.id.order_name_tv);
            serviceTimeTv = itemView.findViewById(R.id.order_time_tv);
            clothIv = itemView.findViewById(R.id.cloth_iv);
            arrowIv = itemView.findViewById(R.id.right_arrow_iv);
        }
    }

    private void addService(ServiceListResponce dd, String y, int londriId) {
        dd.setAccess(y);
        dd.setLondriId(londriId);
        ApiInterface apiInterface = getRetrofit().create(ApiInterface.class);
        Call<String> call = apiInterface.addService(new Gson().toJson(dd));
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.body().equals("done")) {

                }
//                    getServiceList();
                else
                    Toast.makeText(context, "Service Add problem try after some time.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(context, "Service Add problem try after some time.", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void openDialog(final ServiceListResponce service, final int londriId, MyViewViewHolder holder) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        dialog.setMessage("Are you sure to deselect service?");
        dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                addService(service, "N", londriId);
                dialog.dismiss();
            }
        });
        dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                holder.checkBox.setChecked(true);
            }
        });
        dialog.show();
    }
}
