package com.vikisoft.londrishops.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vikisoft.londrishops.R;
import com.vikisoft.londrishops.utils.SessionManager;

import java.util.List;

import static com.vikisoft.londrishops.utils.SessionManager.LIGHT_THEME;

public class TransacationListAdapter extends RecyclerView.Adapter<TransacationListAdapter.MyViewHolder> {

    private List<String> transactionList;
    private Context context;
    private com.vikisoft.londrishops.utils.SessionManager sessionManager;
    private String theme = LIGHT_THEME;


    public TransacationListAdapter(List<String> transactionList, Context context) {
        this.transactionList = transactionList;
        this.context = context;
        sessionManager = new SessionManager(context);
        theme = sessionManager.getCurrentTheme();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_transcation, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        setTheme(holder);
    }

    @Override
    public int getItemCount() {
        return transactionList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView dayTv, dateTimeTv, clothsCountTv, transactionAmtTv, ordersTv, totalPaymentTv;
        ImageView shoppingBagIv, giftIv;
//        View dashedMarginIv;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            dayTv = itemView.findViewById(R.id.day_tv);
            dateTimeTv = itemView.findViewById(R.id.date_and_time_tv);
            clothsCountTv = itemView.findViewById(R.id.cloths_count_tv);
            transactionAmtTv = itemView.findViewById(R.id.transaction_amount_tv);
            ordersTv = itemView.findViewById(R.id.orders_tv);
            totalPaymentTv = itemView.findViewById(R.id.total_payment_tv);
            shoppingBagIv = itemView.findViewById(R.id.shopping_bag_iv);
            giftIv = itemView.findViewById(R.id.gift_iv);
//            dashedMarginIv = itemView.findViewById(R.id.dashed_gap_v);

        }
    }

    private void setTheme(MyViewHolder holder) {
       /* if (theme.equals(LIGHT_THEME)) {
            holder.giftIv.setColorFilter(ContextCompat.getColor(context, R.color.figmaBlue), PorterDuff.Mode.SRC_IN);
            holder.shoppingBagIv.setColorFilter(ContextCompat.getColor(context, R.color.figmaBlue), PorterDuff.Mode.SRC_IN);
            holder.dashedMarginIv.setColorFilter(ContextCompat.getColor(context, R.color.black), PorterDuff.Mode.SRC_IN);
            holder.clothsCountTv.setTextColor(context.getResources().getColor(R.color.figmaBlue));
            holder.transactionAmtTv.setTextColor(context.getResources().getColor(R.color.figmaBlue));

        }*/

    }
}