package com.vikisoft.londrishops.fragments;


import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.makeramen.roundedimageview.RoundedImageView;
import com.vikisoft.londrishops.R;
import com.vikisoft.londrishops.activity.PreviousTransactionActivity;
import com.vikisoft.londrishops.adapter.TransacationListAdapter;
import com.vikisoft.londrishops.api.ApiInterface;
import com.vikisoft.londrishops.helper.LoginDataResponce;
import com.vikisoft.londrishops.helper.OrderService;
import com.vikisoft.londrishops.helper.OrdersListResponce;
import com.vikisoft.londrishops.helper.ServiceListResponce;
import com.vikisoft.londrishops.utils.SessionManager;

import java.text.Format;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import static com.vikisoft.londrishops.activity.HomeActivity.SERVICE_FILTER;
import static com.vikisoft.londrishops.api.ApiCall.getRetrofit;
import static com.vikisoft.londrishops.helper.LondriCalls.getOrders;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    private View view;
    private TextView londriName, tvLondriId, todaysOrderCount, remainingOrderCount, deliveredOrderCount, count1, count2, count3, count4, count5, todayEarning;
    private ImageView washnfoldcnticon, washnironcnticon, washnhangcnticon, dryclndcnticon, ironcnticon;
    private SessionManager sessionManager;
    private ApiInterface apiInterface;
    private List<OrdersListResponce> data;
    private LoginDataResponce logData;
    private SimpleDateFormat dateFormat;
    private Format n;
    private int xx = 0;
    private CardView cardWashFold, cardWashIron, cardWashHang, cardDryClean, cardIron;
    private RecyclerView transactionListRv;
    private TextView viewAllTransactionTv;
    private FragmentDisplaylistener displaylistener;

    public HomeFragment() {
        // Required empty public constructor
    }

    public void setFragmentListener(FragmentDisplaylistener displaylistener) {
        this.displaylistener = displaylistener;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);
        cardWashFold = view.findViewById(R.id.cardWashFold);
        cardWashIron = view.findViewById(R.id.cardWashIron);
        cardWashHang = view.findViewById(R.id.cardWashHang);
        cardDryClean = view.findViewById(R.id.cardDryClean);
        cardIron = view.findViewById(R.id.cardIron);
        cardWashFold.setOnClickListener(clickListener);
        cardWashIron.setOnClickListener(clickListener);
        cardWashHang.setOnClickListener(clickListener);
        cardDryClean.setOnClickListener(clickListener);
        cardIron.setOnClickListener(clickListener);
        londriName = view.findViewById(R.id.londriName);
        tvLondriId = view.findViewById(R.id.tvLondriId);
        todaysOrderCount = view.findViewById(R.id.todaysOrderCount);
        remainingOrderCount = view.findViewById(R.id.remainingOrderCount);
        deliveredOrderCount = view.findViewById(R.id.deliveredOrderCount);
        count1 = view.findViewById(R.id.count1);
        count2 = view.findViewById(R.id.count2);
        count3 = view.findViewById(R.id.count3);
        count4 = view.findViewById(R.id.count4);
        count5 = view.findViewById(R.id.count5);
        washnfoldcnticon = view.findViewById(R.id.washnfoldcnticon);
        washnironcnticon = view.findViewById(R.id.washnironcnticon);
        washnhangcnticon = view.findViewById(R.id.washnhangcnticon);
        dryclndcnticon = view.findViewById(R.id.dryclndcnticon);
        ironcnticon = view.findViewById(R.id.ironcnticon);
        todayEarning = view.findViewById(R.id.todayEarning);
        sessionManager = new SessionManager(view.getContext());
        apiInterface = getRetrofit().create(ApiInterface.class);
        dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        transactionListRv = view.findViewById(R.id.recyclerView);
        viewAllTransactionTv = view.findViewById(R.id.view_all_tv);
        viewAllTransactionTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), PreviousTransactionActivity.class);
                startActivity(intent);
            }
        });

        setTransactionDummyAdapter();

        setData();
        return view;
    }

    private void setData() {
        //String oid = String.format("%06d", data.getPaymentId());
        n = NumberFormat.getCurrencyInstance(new Locale("en", "in"));
        logData = sessionManager.getProfileJson();
        if (logData != null)
            {
                LoginDataResponce d = logData;

                if (!TextUtils.isEmpty(d.getProfilePhoto()) && !d.getProfilePhoto().equals("-"))
                    Glide.with(getContext()).load(d.getProfilePhoto()).into((RoundedImageView) view.findViewById(R.id.homeProfilePhoto));
                londriName.setText(d.getShopName());
                String sId = "#" + String.format("%06d", d.getShopId());
                String refreshedToken = FirebaseInstanceId.getInstance().getToken();
                //Log.d("Token","Device Token : "+refreshedToken);
                //FirebaseMessaging.getInstance().subscribeToTopic("order_status_"+d.getShopId());

                FirebaseMessaging.getInstance().subscribeToTopic("shop_notification");
                FirebaseMessaging.getInstance().subscribeToTopic("shop_order_"+d.getShopId());
                sessionManager.saveLindriId(d.getShopId());
                tvLondriId.setText(sId);
                if (sessionManager.getOrderList() == null) {
                    getData(d);
                    displayData();
                } else {
                    data = sessionManager.getOrderList();
                    displayData();
                    getData(d);
                    sessionManager.saveOrderList(new Gson().toJson(data));
                    //displayData();
                }
                getServiceList();
            }

    }

    private void getData(LoginDataResponce d) {
        data = getOrders(apiInterface, String.valueOf(d.getShopId()));
    }

    private void displayData() {
        if (data != null)
            if (data.size() != 0) {
                sessionManager.saveOrderList(new Gson().toJson(data));
                int c1 = 0, c2 = 0, c3 = 0, c4 = 0, c5 = 0, price = 0, remain = 0, delivered = 0;
                String date = dateFormat.format(new Date());
                for (OrdersListResponce x : data) {
                    if (x.getServiceId() == 1 && x.getPickupDate().contains(date))
                        c1++;
                    if (x.getServiceId() == 2 && x.getPickupDate().contains(date))
                        c2++;
                    if (x.getServiceId() == 3 && x.getPickupDate().contains(date))
                        c3++;
                    if (x.getServiceId() == 4 && x.getPickupDate().contains(date))
                        c4++;
                    if (x.getServiceId() == 5 && x.getPickupDate().contains(date))
                        c5++;
                    if (x.getPickupDate().contains(date))
                        if (x.getStatus1().equals("Delivered")) {
                            price += x.getFinalAmount();
                            delivered++;
                        } else remain++;
                    count1.setText(String.valueOf(c1));
                    count2.setText(String.valueOf(c2));
                    count3.setText(String.valueOf(c3));
                    count4.setText(String.valueOf(c4));
                    count5.setText(String.valueOf(c5));
                    todayEarning.setText(n.format(price));
                    remainingOrderCount.setText(String.valueOf(remain));
                    deliveredOrderCount.setText(String.valueOf(delivered));
                    todaysOrderCount.setText(String.valueOf(c1 + c2 + c3 + c4 + c5));
                }

            } else {
                hidex();
            }
        else {
            hidex();
        }
        try {
            OrderService ser = new OrderService();
            Objects.requireNonNull(getContext()).startService(new Intent(view.getContext(), OrderService.class));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void hidex() {
        count1.setText(String.valueOf(0));
        count2.setText(String.valueOf(0));
        count3.setText(String.valueOf(0));
        count4.setText(String.valueOf(0));
        count5.setText(String.valueOf(0));
        todayEarning.setText(n.format(0));
        remainingOrderCount.setText(String.valueOf(0));
        deliveredOrderCount.setText(String.valueOf(0));
        todaysOrderCount.setText(String.valueOf(0));

    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            FragmentManager fm = getFragmentManager();
            assert fm != null;
            FragmentTransaction ft = fm.beginTransaction();
            OrderFragment of = new OrderFragment();
            Bundle bundle = new Bundle();
            if (v == cardWashFold) {
                bundle.putInt(SERVICE_FILTER, 1);
                of.setArguments(bundle);
                ft.replace(R.id.homeFrame, of);
                ft.commit();

            } else if (v == cardWashIron) {
                bundle.putInt(SERVICE_FILTER, 2);
                of.setArguments(bundle);
                ft.replace(R.id.homeFrame, of);
                ft.commit();

            } else if (v == cardWashHang) {
                bundle.putInt(SERVICE_FILTER, 3);
                of.setArguments(bundle);
                ft.replace(R.id.homeFrame, of);
                ft.commit();

            } else if (v == cardDryClean) {
                bundle.putInt(SERVICE_FILTER, 4);
                of.setArguments(bundle);
                ft.replace(R.id.homeFrame, of);
                ft.commit();

            } else if (v == cardIron) {
                bundle.putInt(SERVICE_FILTER, 5);
                of.setArguments(bundle);
                ft.replace(R.id.homeFrame, of);
                ft.commit();

            }
        }
    };
    private void getServiceList() {
        Call<List<ServiceListResponce>> call = apiInterface.getServiceList(String.valueOf(sessionManager.getLondriId()));
        call.enqueue(new Callback<List<ServiceListResponce>>() {
            @Override
            public void onResponse(Call<List<ServiceListResponce>> call, Response<List<ServiceListResponce>> response) {
                if (response.body() != null)
                    if (response.body().size() != 0){
                        sessionManager.setService(new Gson().toJson(response.body()));
                       // setService(response.body());
                    }
                    else
                        Toast.makeText(getContext(), "Service List Error", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getContext(), "Service List Error", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<List<ServiceListResponce>> call, Throwable t) {
                Toast.makeText(getContext(), "Service List Error", Toast.LENGTH_SHORT).show();
            }
        });
    }



    private void setTransactionDummyAdapter() {
        List<String> transactionList = new ArrayList<>();
        transactionList.add("");
        transactionList.add("");
        transactionList.add("");
        transactionList.add("");
        transactionList.add("");
        transactionList.add("");

        TransacationListAdapter adapter = new TransacationListAdapter(transactionList, getContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        transactionListRv.setLayoutManager(layoutManager);
        transactionListRv.setAdapter(adapter);

    }

    @Override
    public void onResume() {
        super.onResume();
        displaylistener.currentFragment(0);

    }

    public interface FragmentDisplaylistener{
        void currentFragment(int position);
    }
}
