package com.vikisoft.londrishops.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.api.Api;
import com.google.gson.Gson;
import com.vikisoft.londrishops.R;
import com.vikisoft.londrishops.adapter.PickUpListAdpater;
import com.vikisoft.londrishops.api.ApiInterface;
import com.vikisoft.londrishops.helper.ServiceListResponce;
import com.vikisoft.londrishops.utils.SessionManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.vikisoft.londrishops.api.ApiCall.getRetrofit;

public class ServiceFragment extends Fragment {
    private RecyclerView pickUpListRv;
    private FragmentDisplaylistener displaylistener;
    private ImageView backIv;

    public void setFragmentListener(FragmentDisplaylistener displaylistener) {
        this.displaylistener = displaylistener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_service, container, false);

        init(view);
        getServiceList();
        return view;
    }

    private void init(View view) {
        pickUpListRv = view.findViewById(R.id.recyclerView);
        backIv = view.findViewById(R.id.back);

        backIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStack();
                displaylistener.currentFragment(2);
            }
        });

    }

    private void setAdapter(List<ServiceListResponce> serviceListResponces) {

        PickUpListAdpater adpater = new PickUpListAdpater(serviceListResponces, getContext(), getActivity());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        pickUpListRv.setLayoutManager(layoutManager);
        pickUpListRv.setAdapter(adpater);
    }


    private void getServiceList() {
        final SessionManager sessionManager = new SessionManager(getContext());
        ApiInterface apiInterface = getRetrofit().create(ApiInterface.class);
        Call<List<ServiceListResponce>> call = apiInterface.getServiceList(String.valueOf(sessionManager.getLondriId()));
        call.enqueue(new Callback<List<ServiceListResponce>>() {
            @Override
            public void onResponse(Call<List<ServiceListResponce>> call, Response<List<ServiceListResponce>> response) {
                if (response.body() != null)
                    if (response.body().size() != 0) {
                        sessionManager.setService(new Gson().toJson(response.body()));
                        setAdapter(response.body());
                    } else
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


    public interface FragmentDisplaylistener{
        void currentFragment(int position);
    }
}
