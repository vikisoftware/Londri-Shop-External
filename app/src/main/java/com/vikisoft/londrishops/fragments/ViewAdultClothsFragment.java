package com.vikisoft.londrishops.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.vikisoft.londrishops.R;
import com.vikisoft.londrishops.adapter.ClothsCountAdapter;
import com.vikisoft.londrishops.api.ApiInterface;
import com.vikisoft.londrishops.helper.ClothCountResponse;
import com.vikisoft.londrishops.helper.CommonFunctions;
import com.vikisoft.londrishops.helper.UpdatePriceResponce;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.vikisoft.londrishops.api.ApiCall.getRetrofit;


public class ViewAdultClothsFragment extends Fragment {

    private RecyclerView clothListRv;
    private String paymentId = "";
    public static final int ADULT_CLOTHS = 1;

    public ViewAdultClothsFragment(String paymentId) {
        this.paymentId = paymentId;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_adult_cloth, container, false);

        init(view);

        getClothsDetails(paymentId);
        return view;
    }

    private void init(View view) {
        clothListRv = view.findViewById(R.id.cloths_list_rv);
    }


    private void getClothsDetails(String paymentId) {
        final CommonFunctions commonFunctions = new CommonFunctions(getContext());
        commonFunctions.showProgressBar(getContext(), getString(R.string.loading));
        ApiInterface apiInterface = getRetrofit().create(ApiInterface.class);
        Call<List<UpdatePriceResponce>> call = apiInterface.getClothDetails(Integer.parseInt(paymentId));

        call.enqueue(new Callback<List<UpdatePriceResponce>>() {
            @Override
            public void onResponse(Call<List<UpdatePriceResponce>> call, Response<List<UpdatePriceResponce>> response) {
                commonFunctions.hideProgressBar();
                if (response.body() != null) {
                    setAdapter(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<UpdatePriceResponce>> call, Throwable t) {
                commonFunctions.hideProgressBar();
                Toast.makeText(getContext(), getString(R.string.network_problem), Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });

    }

    private void setAdapter(List<UpdatePriceResponce> clothList) {
        filterAdultCloths(clothList);
        ClothsCountAdapter adapter = new ClothsCountAdapter(clothList, getContext(), ADULT_CLOTHS);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        clothListRv.setLayoutManager(linearLayoutManager);
        clothListRv.setAdapter(adapter);
    }

    private void filterAdultCloths(List<UpdatePriceResponce> clothList) {

        for (int i = 0; i < clothList.size(); ) {
            if (clothList.get(i).getWearId() != ADULT_CLOTHS) {
                clothList.remove(i);
            } else {
                i++;
            }
        }

    }
}
