package com.vikisoft.londrishops.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ags.easyalertdialoglibrary.EasyAlertDialog;
import com.vikisoft.londrishops.R;
import com.vikisoft.londrishops.helper.WeeklyOff;

import java.util.List;

public class WeeklyOffAdapter extends RecyclerView.Adapter<WeeklyOffAdapter.MyViewHolder>{


    private List<WeeklyOff> weeklyOffList;
    private Context context;


    public WeeklyOffAdapter(List<WeeklyOff> weeklyOffList, Context context) {
        this.weeklyOffList = weeklyOffList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_weekly_off, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        WeeklyOff day = weeklyOffList.get(position);

        holder.whiteCircleRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AreYouSureOff(holder, day);
               /* if (day.isSelected()) {
                    day.setSelected(false);
                    holder.whiteCircleRl.setBackground(context.getDrawable(R.drawable.button_shape_round_blue));
                    holder.dayTv.setTextColor(context.getResources().getColor(R.color.white));
                } else {
                    day.setSelected(true);
                    holder.whiteCircleRl.setBackground(context.getDrawable(R.drawable.button_shape_round_white));
                    holder.dayTv.setTextColor(context.getResources().getColor(R.color.black));
                }*/
            }
        });

        holder.dayTv.setText(day.getDay() + "");
    }

    @Override
    public int getItemCount() {
        return weeklyOffList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView dayTv;
        RelativeLayout whiteCircleRl;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            dayTv = itemView.findViewById(R.id.day_tv);
            whiteCircleRl = itemView.findViewById(R.id.white_circle_rl);
        }
    }

    private void AreYouSureOff(MyViewHolder holder, WeeklyOff day) {
        EasyAlertDialog.showAlertDialog(context, "Warning", "Are you sure? \nSet week off on selected day?" , "Yes", "No"
                , new EasyAlertDialog.DialogClickListener() {
            @Override
            public void onPositiveButtonClick(DialogInterface dialog, int position) {

                    day.setSelected(false);
                    holder.whiteCircleRl.setBackground(context.getDrawable(R.drawable.button_shape_round_blue));
                    holder.dayTv.setTextColor(context.getResources().getColor(R.color.white));

            }

            @Override
            public void onNegativeButtonClick(DialogInterface dialog) {
                day.setSelected(true);
                holder.whiteCircleRl.setBackground(context.getDrawable(R.drawable.button_shape_round_white));
                holder.dayTv.setTextColor(context.getResources().getColor(R.color.black));
            }
        });
    }
}
