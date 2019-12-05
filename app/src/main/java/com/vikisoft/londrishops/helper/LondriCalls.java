package com.vikisoft.londrishops.helper;

import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.vikisoft.londrishops.api.ApiInterface;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.vikisoft.londrishops.utils.AppConstants.IFSC_URL;

/**
 * Crated by Ashish 11-02-2019
 */

public class LondriCalls {
    private static List<OrdersListResponce> data;
    private static List<TransactionResponce> data1;
    private static IfscResponce data2;
    private static List<ServiceListResponce> datal;

    public static List<OrdersListResponce> getOrders(ApiInterface apiInterface, String shopId) {

        Call<List<OrdersListResponce>> res = apiInterface.getOrders(shopId);
        res.enqueue(new Callback<List<OrdersListResponce>>() {
            @Override
            public void onResponse(Call<List<OrdersListResponce>> call, Response<List<OrdersListResponce>> response) {
                if (response.body() != null)
                    if (response.body().size() != 0)
                        data = response.body();
                    else data = null;
                else data = null;
            }

            @Override
            public void onFailure(Call<List<OrdersListResponce>> call, Throwable t) {
                data = null;
            }
        });
        return data;
    }

    public static List<TransactionResponce> getTransaction(ApiInterface apiInterface, String shopId) {

        Call<List<TransactionResponce>> res = apiInterface.getTransaction(shopId);
        res.enqueue(new Callback<List<TransactionResponce>>() {
            @Override
            public void onResponse(Call<List<TransactionResponce>> call, Response<List<TransactionResponce>> response) {
                if (response.body() != null)
                    if (response.body().size() != 0)
                        data1 = response.body();
                    else data1 = null;
                else data1 = null;
            }

            @Override
            public void onFailure(Call<List<TransactionResponce>> call, Throwable t) {
                data1 = null;
            }
        });
        return data1;
    }

    public static IfscResponce getIfscCode(ApiInterface apiInterface, String ifsc) {
        Call<String> res = apiInterface.getifecDetails(IFSC_URL + ifsc);
        res.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                //Log.d("x", "onResponse: ");
                if (response.body() == null)
                    data2 = null;
                else data2 = new Gson().fromJson(response.body(), IfscResponce.class);


            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                data2 = null;
            }
        });
        return data2;
    }

    public static List<ServiceListResponce> getServiceList(ApiInterface apiInterface, String sid) {

        Call<List<ServiceListResponce>> call = apiInterface.getServiceList(sid);
        call.enqueue(new Callback<List<ServiceListResponce>>() {
            @Override
            public void onResponse(Call<List<ServiceListResponce>> call, Response<List<ServiceListResponce>> response) {
                if (response.body() != null)
                    if (response.body().size() != 0)
                        datal = response.body();
                    else datal = null;
                else datal = null;
            }

            @Override
            public void onFailure(Call<List<ServiceListResponce>> call, Throwable t) {
                datal = null;
            }
        });
        return datal;
    }
}