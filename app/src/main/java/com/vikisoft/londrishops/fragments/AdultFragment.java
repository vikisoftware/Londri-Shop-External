package com.vikisoft.londrishops.fragments;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.crashlytics.android.Crashlytics;
import com.google.gson.Gson;
import com.vikisoft.londrishops.R;
import com.vikisoft.londrishops.api.ApiInterface;
import com.vikisoft.londrishops.helper.ServiceListResponce;
import com.vikisoft.londrishops.helper.ServicePriceResponce;
import com.vikisoft.londrishops.utils.SessionManager;

import java.util.ArrayList;
import java.util.List;

import static com.vikisoft.londrishops.api.ApiCall.getRetrofit;
import static com.vikisoft.londrishops.fragments.ProfileFragment.SERVICE;

/**
 * Crated by Ashish 21-02-2019
 */
public class AdultFragment extends Fragment {

    private ServicePriceAdapter adapter = null;
    private ServiceListResponce serData;
    private ListView recyclerView;
    private View view;
    private String data;
    private Context _context;

    public AdultFragment() {

    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_adult, container, false);
        _context = view.getContext();
        assert getArguments() != null;
        data = getArguments().getString(SERVICE);
        if (data != null) {
            serData = new Gson().fromJson(data, ServiceListResponce.class);
        }
        recyclerView = view.findViewById(R.id.adultRecyclerView);
        //recyclerView.setLayoutManager(new LinearLayoutManager(_context));
        initWork();
        return view;
    }

    private void initWork() {
        final ProgressDialog dialog = new ProgressDialog(getContext(), ProgressDialog.THEME_DEVICE_DEFAULT_LIGHT);
        dialog.setMessage("Loading Data..");
        dialog.show();
        final ApiInterface apiInterface = getRetrofit().create(ApiInterface.class);
        Call<List<ServicePriceResponce>> call = apiInterface.getServicePrice(data, "1");
        call.enqueue(new Callback<List<ServicePriceResponce>>() {
            @Override
            public void onResponse(@NonNull Call<List<ServicePriceResponce>> call, @NonNull Response<List<ServicePriceResponce>> response) {
                if (dialog.isShowing())
                    dialog.dismiss();
                if (response.body() != null)
                    if (response.body().size() != 0) {
                        adapter = new ServicePriceAdapter(response.body(), _context);
                        recyclerView.setAdapter(adapter);
                    } else {
                        recyclerView.setVisibility(View.GONE);
                        (view.findViewById(R.id.emptyView)).setVisibility(View.VISIBLE);
                    }
                else {
                    recyclerView.setVisibility(View.GONE);
                    (view.findViewById(R.id.emptyView)).setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<ServicePriceResponce>> call, @NonNull Throwable t) {
                if (dialog.isShowing())
                    dialog.dismiss();
                Toast.makeText(_context, "Network Problem", Toast.LENGTH_SHORT).show();
                Crashlytics.logException(t);
            }
        });
        Button saveAdult = view.findViewById(R.id.saveAdult);
        saveAdult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (adapter != null) {
                    List<ServicePriceResponce> data = adapter.getList();
                    if (data != null) if (data.size() == 0) {
                       /* for (int childCount = recyclerView.getChildCount(), i = 0; i < childCount; ++i) {
                            final RecyclerView.ViewHolder holder = recyclerView.getChildViewHolder(recyclerView.getChildAt(i));
                            View view=holder.itemView;
                            Toast.makeText(_context, "price: "+((EditText)view.findViewById(R.id.price)).getText(), Toast.LENGTH_SHORT).show();
                        }*/
                        Toast.makeText(_context, "Please insert at least one price", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    final ProgressDialog dialog = new ProgressDialog(getContext(), ProgressDialog.THEME_DEVICE_DEFAULT_LIGHT);
                    dialog.setMessage("Saving Data..");
                    dialog.show();
                    //hideSoftKeyboard(Objects.requireNonNull(getActivity()), v);

                    Call<String> cc = apiInterface.addPricing(new Gson().toJson(data));
                    cc.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                            if (dialog.isShowing())
                                dialog.dismiss();
                            if (response.body() != null) {
                                if (response.body().equals("done")) {
                                    Toast.makeText(_context, "Price saved successfully", Toast.LENGTH_SHORT).show();
                                    SessionManager sessionManager = new SessionManager(_context);
                                    sessionManager.setPriceCount(sessionManager.getPriceCount() + 1);
                                    initWork();
                                } else
                                    Toast.makeText(_context, "Error saving price", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<String> call,@NonNull  Throwable t) {
                            if (dialog.isShowing())
                                dialog.dismiss();
                            Toast.makeText(_context, "Error saving price", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }


    class ServicePriceAdapter extends BaseAdapter {
        List<ServicePriceResponce> list;
        Context context;
        List<ServicePriceResponce> data;



        ServicePriceAdapter(List<ServicePriceResponce> list, Context context) {
            this.list = list;
            this.context = context;
            data = new ArrayList<>();
        }



        public List<ServicePriceResponce> getList() {
            return data;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            @SuppressLint("ViewHolder") View itemView = LayoutInflater.from(context).inflate(R.layout.view_price_card, parent, false);
            TextView textView = itemView.findViewById(R.id.textView);
            final EditText price = itemView.findViewById(R.id.price);
            final ServicePriceResponce dd = list.get(position);
            final ImageView image=itemView.findViewById(R.id.clothImage);
            textView.setText(dd.getClothName());
            if (!TextUtils.isEmpty(dd.getPrice()))
                price.setText(String.valueOf(dd.getPrice()));
            if (!TextUtils.isEmpty(dd.getImage()))
                Glide.with(_context).load(dd.getImage()).into(image);
            price.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    String regExp = "\\d+-\\d+";
                    if (price.getText().toString().matches(regExp) || price.getText().toString().matches("\\d+")) {
                        if (TextUtils.isEmpty(price.getText().toString())) {
                            dd.setPrice("0");
                            return;
                        } else
                            dd.setPrice(price.getText().toString());
                        int flag = 0;
                        if (data.size() == 0)
                            flag = 1;
                        for (int i = 0; i < data.size(); i++) {

                            ServicePriceResponce pp = data.get(i);
                            if (pp.getClothId().equals(dd.getClothId())) {
                                data.remove(i);
                                dd.setWearId(1);
                                dd.setServiceId(serData.getServiceId());
                                dd.setShopId(serData.getLondriId());
                                data.add(i, dd);
                                flag = 0;
                            } else {
                                flag = 1;
                            }
                        }
                        if (flag == 1) {
                            dd.setWearId(1);
                            dd.setServiceId(serData.getServiceId());
                            dd.setShopId(serData.getLondriId());
                            data.add(dd);
                        }
                    } else {
                        price.setError("e.g 123 or 123-123");
                        price.requestFocus();
                        return;
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            return itemView;
        }


    }

}
