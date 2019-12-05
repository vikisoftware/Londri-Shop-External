package com.vikisoft.londrishops.fragments;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.vikisoft.londrishops.R;
import com.vikisoft.londrishops.api.ApiInterface;
import com.vikisoft.londrishops.helper.TransactionResponce;
import com.vikisoft.londrishops.utils.SessionManager;

import java.text.Format;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.vikisoft.londrishops.api.ApiCall.getRetrofit;
import static com.vikisoft.londrishops.helper.LondriCalls.getTransaction;

/**
 * Crated by Ashish 13-02-2019
 */
public class PaymentFragment extends Fragment {

    private SimpleDateFormat dateFormat, dateFormat2;

    public PaymentFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_payment, container, false);
        RecyclerView transactionRecyclerView = view.findViewById(R.id.transactionRecyclerView);
        dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
        dateFormat2 = new SimpleDateFormat("MMMM dd, yyyy");
        ApiInterface apiInterface = getRetrofit().create(ApiInterface.class);
        Context context1 = view.getContext();
        SessionManager sessionManager = new SessionManager(context1);
        List<TransactionResponce> list;
        if (sessionManager.getLondriId() != 0) {
            list = getTransaction(apiInterface, Integer.toString(sessionManager.getLondriId()));
        } else {
            list = null;
        }
        if (list != null)
            if (list.size() != 0) {
                TransactionAdapter adapter = new TransactionAdapter(list, context1);
                transactionRecyclerView.setLayoutManager(new LinearLayoutManager(context1));
                transactionRecyclerView.setAdapter(adapter);
            } else Toast.makeText(context1, "No Transaction Records", Toast.LENGTH_SHORT).show();
        else Toast.makeText(context1, "No Transaction Records", Toast.LENGTH_SHORT).show();
        return view;
    }

    private class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.ViewHolder> {
        List<TransactionResponce> data;
        Context context;
        Format n = NumberFormat.getCurrencyInstance(new Locale("en", "in"));

        TransactionAdapter(List<TransactionResponce> data, Context context) {
            this.data = data;
            this.context = context;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.view_transaction_card, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            TransactionResponce res = data.get(position);
            holder.onlinePaymentId.setText(res.getOnlinePaymentId());
            holder.orderId.setText("#" + String.format("%06d", res.getId()));
            holder.orderTotal.setText(n.format(res.getPaymentAmount()));
            holder.userName.setText(res.getUserName());
            if (res.getPaymentStatus().equals("captured")) {
                holder.orderPaid.setText(getString(R.string.paid));
                holder.orderPaid.setWidth(40);
                holder.orderPaid.setBackground(getResources().getDrawable(R.drawable.button_shape));
            } else {
                holder.orderPaid.setText(getString(R.string.unpaid));
                holder.orderPaid.setWidth(70);
                holder.orderPaid.setBackground(getResources().getDrawable(R.drawable.button_shape_red));
            }
            try {
                Date date = dateFormat.parse(res.getPaymentDate());
                String dd = dateFormat2.format(date);
                holder.paymentDate.setText(dd);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        private class ViewHolder extends RecyclerView.ViewHolder {
            TextView userName, orderPaid, paymentDate, orderId, orderTotal, onlinePaymentId;

            ViewHolder(@NonNull View itemView) {
                super(itemView);
                userName = itemView.findViewById(R.id.userName);
                orderPaid = itemView.findViewById(R.id.orderPaid);
                paymentDate = itemView.findViewById(R.id.paymentDate);
                orderId = itemView.findViewById(R.id.orderId);
                orderTotal = itemView.findViewById(R.id.orderTotal);
                onlinePaymentId = itemView.findViewById(R.id.onlinePaymentId);
            }
        }
    }

}
