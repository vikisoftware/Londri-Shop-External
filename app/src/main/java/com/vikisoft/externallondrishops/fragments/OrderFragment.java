package com.vikisoft.externallondrishops.fragments;

import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Pair;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.vikisoft.externallondrishops.R;
import com.vikisoft.externallondrishops.activity.OrderDetailsActivity;
import com.vikisoft.externallondrishops.api.ApiCall;
import com.vikisoft.externallondrishops.api.ApiInterface;
import com.vikisoft.externallondrishops.helper.OrdersListResponce;
import com.vikisoft.externallondrishops.utils.SessionManager;

import java.text.Format;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.vikisoft.externallondrishops.activity.HomeActivity.SERVICE_FILTER;
import static com.vikisoft.externallondrishops.helper.LondriCalls.getOrders;

/**
 * Crated by Ashish 12-02-2019
 */
public class OrderFragment extends Fragment {
    public static final String SELECTED_ORDER = "selected_order";
    public static final String SELECTED_DATE = "selected_date";
    private View view;
    private Context context;
    private RecyclerView recyclerView;
    private TextView addFilter;
    private LinearLayout filters;
    private OrderListAdapter adapter;
    private SessionManager sessionManager;
    private SimpleDateFormat dateFormat, dateFormat2, dateFormat3;
    private int serveiceFilter;
    private List<OrdersListResponce> list;
    private TextView textView, textView1;
    private TextView introText, introMsgTv;
    private ImageView introIv;

    public OrderFragment() {

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_order, container, false);
        context = view.getContext();
        recyclerView = view.findViewById(R.id.recyclerView);
        addFilter = view.findViewById(R.id.addFilter);
        filters = view.findViewById(R.id.filters);
        dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
        dateFormat2 = new SimpleDateFormat("MMMM dd, yyyy");
        dateFormat3 = new SimpleDateFormat("dd/MM/yyyy");
        /*------------------------get data from server---------------------------------*/
        sessionManager = new SessionManager(view.getContext());
        addFilter.setOnClickListener(clickListener);
        introText = view.findViewById(R.id.hey_amigo_tv);
        introIv = view.findViewById(R.id.intro_iv);
        introMsgTv = view.findViewById(R.id.intro_message_tv);

        introIv.setVisibility(View.GONE);
        introText.setVisibility(View.GONE);
        introMsgTv.setVisibility(View.GONE);

        Resources r = getResources();
        final int px = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                5f,
                r.getDisplayMetrics()
        );
        final int height = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                35f,
                r.getDisplayMetrics()
        );
        textView = new TextView(context);
        Typeface font = ResourcesCompat.getFont(getContext(), R.font.poppins_semi_bold);
        textView.setTypeface(font);
        textView.setTextSize(14f);
        textView.setBackground(getResources().getDrawable(R.drawable.button_shape));
        textView.setPadding(px, 0, px, 0);
        Drawable img = getContext().getResources().getDrawable(R.drawable.ic_cancel_black_24dp);
        img.setBounds(0, 0, 50, 50);
        textView.setCompoundDrawables(null, null, img, null);
        textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, height));
        textView.setTextColor(getResources().getColor(R.color.mdtp_white));
        textView.setId(R.id.filterService);
        textView.setGravity(Gravity.CENTER);
        textView1 = new TextView(context);
        textView1.setBackground(getResources().getDrawable(R.drawable.button_shape));
        textView1.setPadding(px, 0, px, 0);
        Drawable img1 = getContext().getResources().getDrawable(R.drawable.ic_cancel);
        img1.setBounds(0, 0, 50, 50);
        textView1.setCompoundDrawables(null, null, img1, null);
        textView1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, height));
        textView1.setTextColor(getResources().getColor(R.color.mdtp_white));
        textView1.setGravity(Gravity.CENTER);
        textView1.setId(R.id.filterDate);
        initWork();
        return view;
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v == addFilter) {
                AlertDialog.Builder alert = new AlertDialog.Builder(context, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                alert.setTitle("Select Filter");
                String[] item = {"Service", "Date"};
                alert.setSingleChoiceItems(item, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            selectSelection();
                        }
                        if (which == 1) {
                            getDate();
                        }
                        dialog.dismiss();
                    }
                });
                alert.show();
            }
        }
    };

    private void selectSelection() {

        AlertDialog.Builder alert = new AlertDialog.Builder(context, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alert.setTitle("Select Service");
        String[] item = {"Wash and Fold", "Wash and Iron", "Wash and Hang", "Dry Clean", "Iron Only"};
        alert.setSingleChoiceItems(item, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                List<OrdersListResponce> data = new ArrayList<>();

                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (view.findViewById(R.id.filterDate) != null) {
                            setDateFilter(textView1.getText().toString());
                        } else {
                            setNoFilter();
                        }
                    }
                });
                if (which == 0) {
                    serveiceFilter = 1;
                    if (view.findViewById(R.id.filterDate) != null) {

                        setDualFilter(1, getString(R.string.washnfold1day), textView1.getText().toString());
                    } else {
                        setServiceFilter(1, getString(R.string.washnfold1day));
                    }
                } else if (which == 1) {
                    serveiceFilter = 2;
                    if (view.findViewById(R.id.filterDate) != null) {
                        setDualFilter(2, getString(R.string.washniron1day), textView1.getText().toString());
                    } else {
                        setServiceFilter(2, getString(R.string.washniron1day));
                    }
                } else if (which == 2) {
                    serveiceFilter = 3;
                    if (view.findViewById(R.id.filterDate) != null) {
                        setDualFilter(3, getString(R.string.washnhang1day), textView1.getText().toString());
                    } else {
                        setServiceFilter(3, getString(R.string.washnhang1day));
                    }
                } else if (which == 3) {
                    serveiceFilter = 4;
                    if (view.findViewById(R.id.filterDate) != null) {
                        setDualFilter(4, getString(R.string.dryclean), textView1.getText().toString());
                    } else {
                        setServiceFilter(4, getString(R.string.dryclean));
                    }
                } else if (which == 4) {
                    serveiceFilter = 5;
                    if (view.findViewById(R.id.filterDate) != null) {
                        setDualFilter(5, getString(R.string.iron), textView1.getText().toString());
                    } else {
                        setServiceFilter(6, getString(R.string.iron));
                    }
                }
                dialog.dismiss();
            }
        });
        alert.show();
    }

    private void setNoFilter() {
        filters.removeAllViews();
        if (list != null)
            if (list.size() != 0) {
                adapter = new OrderListAdapter(list, context);
                recyclerView.setAdapter(adapter);
            } else Toast.makeText(context, "No Data", Toast.LENGTH_SHORT).show();
        else Toast.makeText(context, "No Data", Toast.LENGTH_SHORT).show();
    }

    private void setDateFilter(String date) {
        filters.removeAllViews();
        textView1.setText(date);
        textView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setNoFilter();
            }
        });
        filters.addView(textView1);
        List<OrdersListResponce> dd = new ArrayList<>();
        if (list != null)
            if (list.size() != 0) {
                for (OrdersListResponce data : list) {
                    if (data.getPickupDate().contains(date)) {
                        dd.add(data);
                    }
                }
                adapter = new OrderListAdapter(dd, context);
                recyclerView.setAdapter(adapter);
            } else Toast.makeText(context, "No Data", Toast.LENGTH_SHORT).show();
        else Toast.makeText(context, "No Data", Toast.LENGTH_SHORT).show();
    }

    private void setServiceFilter(int serviceId, String serviceName) {
        filters.removeAllViews();
        textView.setText(serviceName);
        serveiceFilter = serviceId;
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setNoFilter();
            }
        });
        filters.addView(textView);
        List<OrdersListResponce> dd = new ArrayList<>();
        if (list != null) {
            if (list.size() != 0) {
                for (OrdersListResponce datax : list) {
                    if (datax.getServiceId() == serviceId) {
                        dd.add(datax);
                    }
                }
                adapter = new OrderListAdapter(dd, context);
                recyclerView.setAdapter(adapter);
            } else Toast.makeText(context, "No Data", Toast.LENGTH_SHORT).show();
        } else Toast.makeText(context, "No Data", Toast.LENGTH_SHORT).show();

    }


    private void setDualFilter(final int serviceId, final String serviceName, final String date) {
        //String date=((TextView) view.findViewById(R.id.filterDate)).getText().toString();
        filters.removeAllViews();
        textView1.setText(date);
        textView.setText(serviceName);
        serveiceFilter = serviceId;
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDateFilter(date);
            }
        });
        textView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setServiceFilter(serviceId, serviceName);
            }
        });
        filters.addView(textView1);
        filters.addView(textView);
        if (list != null)
            if (list.size() != 0) {
                List<OrdersListResponce> dd = new ArrayList<>();
                for (OrdersListResponce datax : list) {
                    if (datax.getPickupDate().contains(date) && datax.getServiceId() == serviceId) {
                        dd.add(datax);
                    }
                }
                adapter = new OrderListAdapter(dd, context);
                recyclerView.setAdapter(adapter);
            } else Toast.makeText(context, "No Data", Toast.LENGTH_SHORT).show();
        else Toast.makeText(context, "No Data", Toast.LENGTH_SHORT).show();
    }

    private void getDate() {

        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(context, R.style.DialogTheme,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view1, int year,
                                          int monthOfYear, int dayOfMonth) {
                        int dd = (monthOfYear + 1);
                        if (view.findViewById(R.id.filterService) != null) {
                            if (serveiceFilter == 1)
                                setDualFilter(1, getString(R.string.washnfold1day), dayOfMonth + "/" + String.format("%02d", dd) + "/" + year);
                            else if (serveiceFilter == 2)
                                setDualFilter(2, getString(R.string.washniron1day), dayOfMonth + "/" + String.format("%02d", dd) + "/" + year);
                            else if (serveiceFilter == 3)
                                setDualFilter(3, getString(R.string.washnhang1day), dayOfMonth + "/" + String.format("%02d", dd) + "/" + year);
                            else if (serveiceFilter == 4)
                                setDualFilter(4, getString(R.string.dryclean), dayOfMonth + "/" + String.format("%02d", dd) + "/" + year);
                            else if (serveiceFilter == 5)
                                setDualFilter(5, getString(R.string.iron), dayOfMonth + "/" + String.format("%02d", dd) + "/" + year);
                        } else {
                            setDateFilter(dayOfMonth + "/" + String.format("%02d", dd) + "/" + year);
                        }

                    }
                }, mYear, mMonth, mDay);

        datePickerDialog.show();
    }

    private void initWork() {
        assert getArguments() != null;
        serveiceFilter = getArguments().getInt(SERVICE_FILTER);
        List<OrdersListResponce> response;
        if (sessionManager.getOrderList() != null)
            if (sessionManager.getOrderList().size() != 0) {
                response = sessionManager.getOrderList();
                setData(response);
                response = getdata();
                setData(response);
            } else {
                response = getdata();
                setData(response);
            }
        else {
            response = getdata();
            setData(response);
        }

    }

    private void setData(List<OrdersListResponce> response) {
        list = response;
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        if (list == null || list.size() == 0) {
            introText.setVisibility(View.VISIBLE);
            introIv.setVisibility(View.VISIBLE);
            introMsgTv.setVisibility(View.VISIBLE);
        }
        /*filters.removeAllViews();
        List<OrdersListResponce> data = new ArrayList<>();*/
        String date = dateFormat3.format(new Date());


        if (response != null)
            if (response.size() != 0) {

                /*textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FragmentManager fm = getFragmentManager();
                        FragmentTransaction ft = fm.beginTransaction();
                        Bundle bundle = new Bundle();
                        if (view.findViewById(R.id.filterDate) != null)
                            bundle.putInt(SERVICE_FILTER, 6);
                        else
                            bundle.putInt(SERVICE_FILTER, 0);
                        OrderFragment of = new OrderFragment();
                        of.setArguments(bundle);
                        ft.replace(R.id.homeFrame, of);
                        ft.commit();
                    }
                });

                textView1.setText(date);
                textView1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FragmentManager fm = getFragmentManager();
                        FragmentTransaction ft = fm.beginTransaction();
                        Bundle bundle = new Bundle();
                        String x = 7 + "" + serveiceFilter;
                        if (view.findViewById(R.id.filterService) != null)
                            bundle.putInt(SERVICE_FILTER, Integer.parseInt(x));
                        else
                            bundle.putInt(SERVICE_FILTER, 0);
                        OrderFragment of = new OrderFragment();
                        of.setArguments(bundle);
                        ft.replace(R.id.homeFrame, of);
                        ft.commit();
                    }
                });*/
                if (serveiceFilter == 0) setNoFilter();
                /*adapter = new OrderListAdapter(response, view.getContext());*/
                if (serveiceFilter == 1) {
                    /*textView.setText(getString(R.string.washnfold1day));
                    filters.addView(textView);
                    filters.addView(textView1);
                    for (OrdersListResponce res : response) {
                        if (res.getServiceId() == 1 && res.getPickupDate().contains(date)) {
                            data.add(res);
                        }
                    }
                    adapter = new OrderListAdapter(data, view.getContext());*/
                    setDualFilter(1, getString(R.string.washnfold1day), date);
                }
                if (serveiceFilter == 2) {
                    /*textView.setText(getString(R.string.washniron1day));
                    filters.addView(textView);
                    filters.addView(textView1);
                    for (OrdersListResponce res : response) {
                        if (res.getServiceId() == 2 && res.getPickupDate().contains(date)) {
                            data.add(res);
                        }
                    }
                    adapter = new OrderListAdapter(data, view.getContext());*/
                    setDualFilter(2, getString(R.string.washniron1day), date);
                }
                if (serveiceFilter == 3) {
                    /*textView.setText(getString(R.string.washnhang1day));
                    filters.addView(textView);
                    filters.addView(textView1);
                    for (OrdersListResponce res : response) {
                        if (res.getServiceId() == 3 && res.getPickupDate().contains(date)) {
                            data.add(res);
                        }
                    }
                    adapter = new OrderListAdapter(data, view.getContext());*/
                    setDualFilter(3, getString(R.string.washnhang1day), date);
                }
                if (serveiceFilter == 4) {
                   /* textView.setText(getString(R.string.dryclean));
                    filters.addView(textView);
                    filters.addView(textView1);
                    for (OrdersListResponce res : response) {
                        if (res.getServiceId() == 4 && res.getPickupDate().contains(date)) {
                            data.add(res);
                        }
                    }
                    adapter = new OrderListAdapter(data, view.getContext());*/
                    setDualFilter(4, getString(R.string.dryclean), date);
                }
                if (serveiceFilter == 5) {
                    /*textView.setText(getString(R.string.iron));
                    filters.addView(textView);
                    filters.addView(textView1);
                    for (OrdersListResponce res : response) {
                        if (res.getServiceId() == 5 && res.getPickupDate().contains(date)) {
                            data.add(res);
                        }
                    }
                    adapter = new OrderListAdapter(data, view.getContext());*/
                    setDualFilter(5, getString(R.string.iron), date);
                }
                if (serveiceFilter == 6) {
                    /*filters.addView(textView1);
                    for (OrdersListResponce res : response) {
                        if (res.getPickupDate().contains(date)) {
                            data.add(res);
                        }
                    }
                    adapter = new OrderListAdapter(data, view.getContext());*/
                    setDateFilter(date);
                }
                /*if (serveiceFilter == 71) {
                    textView.setText(getString(R.string.washnfold1day));
                    filters.addView(textView);
                    //filters.addView(textView1);
                    for (OrdersListResponce res : response) {
                        if (res.getServiceId() == 1) {
                            data.add(res);
                        }
                    }
                    adapter = new OrderListAdapter(data, view.getContext());
                }
                if (serveiceFilter == 72) {
                    textView.setText(getString(R.string.washniron1day));
                    filters.addView(textView);
                    for (OrdersListResponce res : response) {
                        if (res.getServiceId() == 2) {
                            data.add(res);
                        }
                    }
                    adapter = new OrderListAdapter(data, view.getContext());
                }
                if (serveiceFilter == 73) {
                    textView.setText(getString(R.string.washnhang1day));
                    filters.addView(textView);
                    for (OrdersListResponce res : response) {
                        if (res.getServiceId() == 3) {
                            data.add(res);
                        }
                    }
                    adapter = new OrderListAdapter(data, view.getContext());
                }
                if (serveiceFilter == 74) {
                    textView.setText(getString(R.string.dryclean));
                    filters.addView(textView);
                    for (OrdersListResponce res : response) {
                        if (res.getServiceId() == 4) {
                            data.add(res);
                        }
                    }
                    adapter = new OrderListAdapter(data, view.getContext());
                }
                if (serveiceFilter == 75) {
                    textView.setText(getString(R.string.iron));
                    filters.addView(textView);
                    for (OrdersListResponce res : response) {
                        if (res.getServiceId() == 5) {
                            data.add(res);
                        }
                    }
                    adapter = new OrderListAdapter(data, view.getContext());
                }*/


            } else Toast.makeText(view.getContext(), "No Data", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(view.getContext(), "No Data", Toast.LENGTH_SHORT).show();


        /*recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(view.getContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view1, int position) {
                OrdersListResponce res = adapter.getSelectedItem(position);
                LinearLayout cardView = view1.findViewById(R.id.cardAddress);
                Pair[] pairs = new Pair[1];
                pairs[0] = new Pair<View, String>(cardView, "cardTransition");
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(getActivity(), pairs);
                Intent intent = new Intent(getContext(), OrderDetailsActivity.class);
                intent.putExtra(SELECTED_ORDER, new Gson().toJson(res));
                startActivity(intent, options.toBundle());
            }
        }));*/
    }

    private List<OrdersListResponce> getdata() {
        ApiInterface apiInterface = ApiCall.getRetrofit().create(ApiInterface.class);
        return getOrders(apiInterface, sessionManager.getProfileJson().getShopId().toString());
    }

    private class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.ViewHolder> {
        List<OrdersListResponce> list;
        Context context;

        OrderListAdapter(List<OrdersListResponce> list, Context context) {
            this.list = list;
            this.context = context;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.view_order_card, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
            final OrdersListResponce data = list.get(position);
            /*holder.bagNumber()*/
            String oid = String.format("%06d", data.getPaymentId());
            Format n = NumberFormat.getCurrencyInstance(new Locale("en", "in"));
            holder.orderId.setText("#" + oid);
            holder.orderPiece.setText(data.getAdultCount() + data.getKidCount() + data.getHouseCount() + " Piece");
            holder.orderTotal.setText(n.format(data.getFinalAmount()));

            if (data.getPaymentStatus().equals("captured")) {
                holder.orderPaid.setText(getString(R.string.paid));

                holder.orderPaid.setWidth(40);
                holder.orderPaid.setBackground(getResources().getDrawable(R.drawable.button_shape));
            } else {
                holder.orderPaid.setText(getString(R.string.unpaid));
                holder.orderPaid.setWidth(70);
                holder.orderPaid.setBackground(getResources().getDrawable(R.drawable.button_shape_red));
            }
            try {
                Date date = dateFormat.parse(data.getPickupDate());
                String dd = dateFormat2.format(date);
                holder.orderDate.setText(dd);
            } catch (Exception e) {
                e.printStackTrace();
            }

            holder.userName.setText(data.getName());

            if (data.getStatus1().equals("Ready to pickup")) {
                holder.orderStatusImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_pickup_pending));
                holder.orderStatusImage.setColorFilter(getContext().getResources().getColor(R.color.grey_600));
                /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    holder.orderStatusImage.setTooltipText("Order in Transport");
                }*/
                holder.orderStatusImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(context, "Order ready to pickup", Toast.LENGTH_SHORT).show();
                    }
                });
            } else if (data.getStatus1().equals("Washing")) {
                holder.orderStatusImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_watch));
                holder.orderStatusImage.setColorFilter(getContext().getResources().getColor(R.color.grey_600));
                /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    holder.orderStatusImage.setTooltipText("Order in Process");
                }*/
                holder.orderStatusImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(context, "Order in Process", Toast.LENGTH_SHORT).show();
                    }
                });
            } else if (data.getStatus1().equals("Out for delivery")) {
                holder.orderStatusImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_delivery_pending));
                holder.orderStatusImage.setColorFilter(getContext().getResources().getColor(R.color.grey_600));
                /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    holder.orderStatusImage.setTooltipText("Order out for delivery");
                }*/
                holder.orderStatusImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(context, "Order out for delivery", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            if (data.getServiceId() == 1) {
                holder.oImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_wash_fold));
            } else if (data.getServiceId() == 2) {
                holder.oImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_wash_iron));
            } else if (data.getServiceId() == 3) {
                holder.oImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_hang));
            } else if (data.getServiceId() == 4) {
                holder.oImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_jacket));
            } else if (data.getServiceId() == 5) {
                holder.oImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_iron));
            }
            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Pair[] pairs = new Pair[1];
                    pairs[0] = new Pair<View, String>(holder.cardView, "cardTransition");
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(getActivity(), pairs);
                    Intent intent = new Intent(getContext(), OrderDetailsActivity.class);
                    intent.putExtra(SELECTED_ORDER, new Gson().toJson(data));
                    startActivity(intent, options.toBundle());
                }
            });

        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public OrdersListResponce getSelectedItem(int position) {
            return list.get(position);
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            ImageView orderStatusImage;
            ImageView oImage;
            TextView userName, orderPiece, orderTotal, orderId, orderPaid, orderDate;
            CardView cardView;

            ViewHolder(@NonNull View i) {
                super(i);
                orderStatusImage = i.findViewById(R.id.orderStatusImage);
                oImage = i.findViewById(R.id.oImage);
                userName = i.findViewById(R.id.userName);
                //bagNumber=i.findViewById(R.id.bagNumber);
                orderPiece = i.findViewById(R.id.orderPiece);
                orderTotal = i.findViewById(R.id.orderTotal);
                orderId = i.findViewById(R.id.orderId);
                orderPaid = i.findViewById(R.id.orderPaid);
                orderDate = i.findViewById(R.id.orderDate);
                cardView = i.findViewById(R.id.cardAddress);
            }
        }
    }

}
