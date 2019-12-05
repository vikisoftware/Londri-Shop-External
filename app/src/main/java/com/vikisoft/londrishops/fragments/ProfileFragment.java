package com.vikisoft.londrishops.fragments;


import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.fabric.sdk.android.Fabric;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.text.TextUtils;
import android.util.Pair;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.crashlytics.android.Crashlytics;
import com.google.gson.Gson;
import com.makeramen.roundedimageview.RoundedImageView;
import com.vikisoft.londrishops.R;
import com.vikisoft.londrishops.activity.BankDeailsActivity;
import com.vikisoft.londrishops.activity.EditAddressActivity;
import com.vikisoft.londrishops.activity.GstEditActivity;
import com.vikisoft.londrishops.activity.ProfileEditActivity;
import com.vikisoft.londrishops.activity.ServicePrice;
import com.vikisoft.londrishops.activity.SettingsActivity;
import com.vikisoft.londrishops.adapter.WeeklyOffAdapter;
import com.vikisoft.londrishops.api.ApiInterface;
import com.vikisoft.londrishops.helper.LoginDataResponce;
import com.vikisoft.londrishops.helper.ServiceListResponce;
import com.vikisoft.londrishops.helper.WeeklyOff;
import com.vikisoft.londrishops.utils.SessionManager;

import java.util.ArrayList;
import java.util.List;

import static com.vikisoft.londrishops.api.ApiCall.getRetrofit;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {
    public static final String ADDRESSTAG = "tag";
    public static final String ADDRESSID = "aId";
    public static final String SERVICE = "service";

    private View view;
    private Context context;
    private LoginDataResponce data;
    private TextView customerId, customerName, addressLabel;
    private ImageView editName;
    private RoundedImageView profilePicture;
    private SessionManager sessionManager;
    private String home;
    private AddressListAdapter addressListAdapter;
    private RecyclerView addressList;
    private Button addAddress;
    private ApiInterface apiInterface;
    private LinearLayout serviceCheckList, serviceSelectedCards;
    private List<ServiceListResponce> list;
    private ProgressDialog progressDialog;
    private ImageView backIv;
    private TextView bankNameTv, branchNameTv, accountNoTv, accountHolderTv, accountTypeTv, ifscTv;
    private RecyclerView weeklyOffRv;
    private TextView addressTv;
    private ImageView editAddressIv;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Fabric.with(getContext(), new Crashlytics());
        try {
            view = inflater.inflate(R.layout.fragment_profile, container, false);
            progressDialog = new ProgressDialog(getContext(), ProgressDialog.THEME_DEVICE_DEFAULT_LIGHT);
            context = view.getContext();
            sessionManager = new SessionManager(view.getContext());
            customerId = view.findViewById(R.id.customerID);
            customerName = view.findViewById(R.id.customerName);
            serviceCheckList = view.findViewById(R.id.serviceCheckList);
            serviceSelectedCards = view.findViewById(R.id.serviceSelectedCards);
            addressList = view.findViewById(R.id.addressList);
            addAddress = view.findViewById(R.id.customerNewAddress);
            profilePicture = view.findViewById(R.id.profile_image);
            editName = view.findViewById(R.id.edit_profile_pic_iv);
//            addressLabel = view.findViewById(R.id.addressLabel);
            editName.setOnClickListener(clickListner);
            addAddress.setOnClickListener(clickListner);
            apiInterface = getRetrofit().create(ApiInterface.class);
            view.findViewById(R.id.editBank).setOnClickListener(clickListner);
            view.findViewById(R.id.editGST).setOnClickListener(clickListner);
//            view.findViewById(R.id.card1).setOnClickListener(clickListner);
//            view.findViewById(R.id.bankCard).setOnClickListener(clickListner);
            RelativeLayout logout = view.findViewById(R.id.logout);
            backIv = view.findViewById(R.id.back);
            bankNameTv  = view.findViewById(R.id.bank_name_tv);
            branchNameTv = view.findViewById(R.id.branch_name_tv);
            accountNoTv = view.findViewById(R.id.account_no_tv);
            accountHolderTv = view.findViewById(R.id.account_holder_tv);
            accountTypeTv = view.findViewById(R.id.account_type_tv);
            ifscTv = view.findViewById(R.id.ifsc_tv);
            weeklyOffRv = view.findViewById(R.id.recyclerView);
            addressTv = view.findViewById(R.id.address_tv);
            editAddressIv = view.findViewById(R.id.edit_address_iv);
            backIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getActivity().getSupportFragmentManager().popBackStack();
                }
            });

            logout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

//                    sessionManager.logout( getActivity());
                    Intent intent = new Intent(getContext(), SettingsActivity.class);
                    startActivity(intent);
                }
            });
            if (sessionManager.isLogin()) {
                data = sessionManager.getProfileJson();
                if (data == null) {
                    getData();
                } else {

                    setData();
                }
                getData();
            }
            if (sessionManager.getLondriId() != 0) {

            }

            editAddressIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent1 = new Intent(view.getContext(), EditAddressActivity.class);
                    intent1.putExtra(ADDRESSTAG, 1);
                    intent1.putExtra(ADDRESSID, data.getShopAddId());
                    startActivityForResult(intent1, 101);
                }
            });
        } catch (Exception e) {
//            Crashlytics.logException(e);
        }

        setWeeklyOffAdapter();

        return view;
    }

    private void getServiceList() {
        Call<List<ServiceListResponce>> call = apiInterface.getServiceList(String.valueOf(sessionManager.getLondriId()));
        call.enqueue(new Callback<List<ServiceListResponce>>() {
            @Override
            public void onResponse(Call<List<ServiceListResponce>> call, Response<List<ServiceListResponce>> response) {
                if (response.body() != null)
                    if (response.body().size() != 0) {
                        sessionManager.setService(new Gson().toJson(response.body()));
                        setService(response.body());
                    } else
                        Toast.makeText(context, "Service List Error", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(context, "Service List Error", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<List<ServiceListResponce>> call, Throwable t) {
                Toast.makeText(context, "Service List Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setService(List<ServiceListResponce> list) {
        Resources r = view.getResources();
        final float textSize = (float) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP,
                18,
                r.getDisplayMetrics()
        );
        final float textSize18 = (float) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP,
                18,
                r.getDisplayMetrics()
        );
        serviceCheckList.removeAllViews();
        serviceSelectedCards.removeAllViews();
        float dp = context.getResources().getDisplayMetrics().density;
        for (final ServiceListResponce dd : list) {
            final CheckBox check = new CheckBox(context);
            check.setText(dd.getServiceName());
            check.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);

            check.setButtonTintList(context.getResources().getColorStateList(R.color.colorAccent));
            check.setTextColor(getResources().getColor(R.color.grey_800));
            final CardView cardView = new CardView(context);
            final LinearLayout linlay = new LinearLayout(context);
            final TextView textView = new TextView(context);
            final TextView textView1 = new TextView(context);
            cardView.setRadius(5f);
            cardView.setTranslationZ(5f);
            LinearLayout.LayoutParams cardViewParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f);
            cardView.setLayoutParams(cardViewParams);
            cardView.setTransitionName("serviceTransition");
            cardView.setCardBackgroundColor(r.getColor(R.color.mdtp_white));
            ViewGroup.MarginLayoutParams cardViewMarginParams = (ViewGroup.MarginLayoutParams) cardView.getLayoutParams();
            cardViewMarginParams.setMargins(20, 20, 20, 20);
            cardView.requestLayout();
            linlay.setLayoutParams(cardViewParams);
            linlay.setPadding(((int) (10 * dp + 0.5f)), ((int) (7 * dp + 0.5f)), ((int) (10 * dp + 0.5f)), ((int) (7 * dp + 0.5f)));
            //linlay.getBackground().setColorFilter(getResources().getColor(R.color.mdtp_white), PorterDuff.Mode.SRC_ATOP);;
            //linlay.setBackgroundTintList(ContextCompat.getColorStateList(context,R.color.mdtp_white));
            textView.setGravity(Gravity.CENTER_VERTICAL);
            textView.setText(dd.getServiceName());
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
            textView.setTextColor(r.getColor(R.color.grey_800));
            LinearLayout.LayoutParams textViewParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f);
            textView.setLayoutParams(textViewParams);

            textView1.setText("Set Price");
            textView1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
            textView1.setTextColor(r.getColor(R.color.colorAccent));
            textView1.setGravity(Gravity.END);
            //LinearLayout.LayoutParams textViewParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,1.0f);
            textView1.setLayoutParams(textViewParams);
            textView1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sessionManager.setPriceCount(0);
                    Pair[] pair = new Pair[1];
                    pair[0] = new Pair<View, String>(cardView, "serviceTransition");
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(getActivity(), pair);
                    Intent intent = new Intent(context, ServicePrice.class);
                    intent.putExtra(SERVICE, new Gson().toJson(dd));
                    startActivity(intent, options.toBundle());
                }
            });
            //check.setEnabled(false);
            if (dd.getLondriId() != 0 && dd.getAccess().equals("Y")) {
                check.setChecked(true);

                //serviceSelectedCards.removeAllViews();
                linlay.addView(textView);
                linlay.addView(textView1);
                cardView.addView(linlay);
                serviceSelectedCards.addView(cardView);

            }
            /*check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    Toast.makeText(context, "Modify services through Londri Console", Toast.LENGTH_LONG).show();
                    if (isChecked){
                        buttonView.setChecked(false);
                    }else{
                        buttonView.setChecked(true);
                    }
                }
            });*/
            check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        linlay.removeAllViews();
                        linlay.addView(textView);
                        linlay.addView(textView1);
                        cardView.removeAllViews();
                        cardView.addView(linlay);
                        serviceSelectedCards.addView(cardView);
                        addService(dd, "Y", sessionManager.getLondriId());
                    } else {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(context, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
                        dialog.setMessage("Are you sure to deselect service?");
                        dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                serviceSelectedCards.removeView(cardView);
                                addService(dd, "N", sessionManager.getLondriId());
                                dialog.dismiss();
                            }
                        });
                        dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                serviceSelectedCards.removeView(cardView);
                                check.setChecked(true);
                            }
                        });
                        dialog.show();

                    }
                }
            });
            serviceCheckList.addView(check);

        }

    }

    private void addService(ServiceListResponce dd, String y, int londriId) {
        dd.setAccess(y);
        dd.setLondriId(londriId);
        Call<String> call = apiInterface.addService(new Gson().toJson(dd));
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.body().equals("done"))
                    getServiceList();
                else
                    Toast.makeText(context, "Service Add problem try after some time.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(context, "Service Add problem try after some time.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setData() {

        if (!TextUtils.isEmpty(data.getShopName())) {
            home = data.getShopFlatNo() + "," + data.getShopRoadName() + "," + data.getShopCity() + "," + data.getShopState() + ","
                    + data.getShopCountry() + "," + data.getShopPostalCode();

            addressTv.setText(home);
        }

        //LoginDataResponce data1=data.get(0);
        if (progressDialog != null && progressDialog.isShowing())
            progressDialog.dismiss();
        if (data != null)
                if (TextUtils.isEmpty(data.getShopName())){
                    Intent intent = new Intent(view.getContext(), ProfileEditActivity.class);
                    Pair[] pairs = new Pair[3];
                    pairs[0] = new Pair<View, String>(profilePicture, "profileTransition");
                    pairs[1] = new Pair<View, String>(customerName, "nameTransition");
//                    pairs[2] = new Pair<View, String>(addressLabel, "labelTransormation");
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(getActivity(), pairs);
                    startActivityForResult(intent, 101, options.toBundle());
                }
                if (data.getShopFlatNo() != null)
                    if (!TextUtils.isEmpty(data.getShopFlatNo())) {
                        List<LoginDataResponce> data1 = new ArrayList<>();

                        data1.add(data);
                        addressListAdapter = new AddressListAdapter(data1, view.getContext());
                        addressList.setLayoutManager(new LinearLayoutManager(view.getContext()));
                        addressList.setAdapter(addressListAdapter);
                        addAddress.setEnabled(false);
                    }
                if(data.getBankId()!=null)
                if (data.getBankId() != 0) {
                   /* String bank = "Bank Name:\t" + getString(R.string.tab) + data.getBankName() + "\n"
                            + "Branch Name:\t" + getString(R.string.tab) + data.getBracch() + "\n"
                            + "Account No.:\t" + data.getAccountNo() + "\n"
                            + "Account Holder:\t" + getString(R.string.tab) + data.getBenifName() + "\n"
                            + "Account Type:\t" + getString(R.string.tab) + data.getAccountType() + "\n"
                            + "IFSC Code:\t" + getString(R.string.tab) + data.getIfsc();
                    ((TextView) view.findViewById(R.id.bankDetails)).setText(bank);*/

                    bankNameTv.setText(data.getBankName());
                    branchNameTv.setText(data.getBracch());
                    accountNoTv.setText(data.getAccountNo());
                    accountHolderTv.setText(data.getBenifName());
                    accountTypeTv.setText(data.getAccountType());
                    ifscTv.setText(data.getIfsc());
                }

            if (data.getFirstName() != null && !data.getFirstName().equals("")) {
                if (!TextUtils.isEmpty(data.getProfilePhoto()))
                    Glide.with(view.getContext()).load(data.getProfilePhoto()).thumbnail(0.5f).into(profilePicture);
                String uname = "#" + String.format("%06d", data.getShopId());
                customerId.setText(uname);
                //String name = data1.getFirstName() + " " + data1.getLastName();
                customerName.setText(data.getShopName());
                if (data.getShopName().length()>20 && data.getShopName().length()<30){
                    customerName.setTextSize(TypedValue.COMPLEX_UNIT_SP,18);
                }
                else if(data.getShopName().length()>30){
                    customerName.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
                }else {
                    customerName.setTextSize(TypedValue.COMPLEX_UNIT_SP,25);
                }
                //customerBalance.setText("0");



            } /*else {
                Intent intent = new Intent(view.getContext(), ProfileEditActivity.class);
                Pair[] pairs = new Pair[3];
                pairs[0] = new Pair<View, String>(profilePicture, "profileTransition");
                pairs[1] = new Pair<View, String>(customerName, "nameTransition");
                pairs[2] = new Pair<View, String>(addressLabel, "labelTransormation");
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(getActivity(), pairs);
                startActivityForResult(intent, 101, options.toBundle());

            }*/
        List<ServiceListResponce> xx = sessionManager.getService();
        if (xx != null) {
            if (xx.size() != 0) {
                setService(xx);
            } else getServiceList();
        } else getServiceList();

    }

    private void getData() {
        progressDialog.setMessage("Loading data..");
        progressDialog.setCancelable(false);
        progressDialog.show();
        Call<LoginDataResponce> call = apiInterface.getLogin(data.getMobileNo());
        call.enqueue(new Callback<LoginDataResponce>() {
            @Override
            public void onResponse(Call<LoginDataResponce> call, Response<LoginDataResponce> response) {
                if (response.body() != null) {

                        sessionManager.saveProfileJson(new Gson().toJson(response.body()));
                        data = response.body();
                        setData();

                    //Log.d("JSONDATA", "onResponse: "+new Gson().toJson(data));
                }else {
                    if (progressDialog != null && progressDialog.isShowing())
                        progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<LoginDataResponce> call, Throwable t) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
            }
        });
    }

    private View.OnClickListener clickListner = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
//                case R.id.card1:
                case R.id.edit_profile_pic_iv:
                    Intent intent2 = new Intent(view.getContext(), ProfileEditActivity.class);
//                    Pair[] pairs2 = new Pair[3];
//                    pairs2[0] = new Pair<View, String>(profilePicture, "profileTransition");
//                    pairs2[1] = new Pair<View, String>(customerName, "nameTransition");
//                    pairs2[2] = new Pair<View, String>(addressLabel, "labelTransormation");
//                    ActivityOptions options2 = ActivityOptions.makeSceneTransitionAnimation(getActivity(), pairs2);
                    startActivityForResult(intent2, 101);//, options2.toBundle());
                    break;

                case R.id.customerNewAddress:
                    Intent intent4 = new Intent(view.getContext(), EditAddressActivity.class);
                    intent4.putExtra(ADDRESSTAG, 0);
                    Pair[] pairs4 = new Pair[1];
                    //pairs4[0] = new Pair<View, String>(customerAddress3, "addressTeansition");
                    //pairs4[1] = new Pair<View, String>(view.findViewById(R.id.tag2), "addressTagTransition");
//                    pairs4[0] = new Pair<View, String>(addressLabel, "labelTransormation");
                    ActivityOptions options4 = ActivityOptions.makeSceneTransitionAnimation(getActivity(), pairs4);
                    startActivityForResult(intent4, 101, options4.toBundle());
                    break;
                case R.id.editBank:
               /* case R.id.bankCard:*/
                    Intent intent5 = new Intent(view.getContext(), BankDeailsActivity.class);
                    intent5.putExtra(ADDRESSTAG, 0);
                    /*Pair[] pairs5 = new Pair[1];
                    //pairs4[0] = new Pair<View, String>(customerAddress3, "addressTeansition");
                    //pairs4[1] = new Pair<View, String>(view.findViewById(R.id.tag2), "addressTagTransition");
                    pairs5[0] = new Pair<View, String>(view.findViewById(R.id.bankCard), "labelTransormation");
                    ActivityOptions options5 = ActivityOptions.makeSceneTransitionAnimation(getActivity(), pairs5);*/
                    startActivityForResult(intent5, 101);//, options5.toBundle());
                    break;
                //pairs4[0] = new Pair<View, String>(customerAddress3, "addressTeansition");
                    //pairs4[1] = new Pair<View, String>(view.findViewById(R.id.tag2), "addressTagTransition");
                case R.id.editGST:
                    Intent intent7 = new Intent(view.getContext(), GstEditActivity.class);
                    intent7.putExtra(ADDRESSTAG, 0);
                    Pair[] pairs7 = new Pair[1];
                    //pairs4[0] = new Pair<View, String>(customerAddress3, "addressTeansition");
                    //pairs4[1] = new Pair<View, String>(view.findViewById(R.id.tag2), "addressTagTransition");
//                    pairs7[0] = new Pair<View, String>(view.findViewById(R.id.bankCard), "labelTransormation");
//                    ActivityOptions options7 = ActivityOptions.makeSceneTransitionAnimation(getActivity(), pairs7);
                    startActivityForResult(intent7, 101);//, options7.toBundle());
                    break;

            }
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        getData();
    }

    private class AddressListAdapter extends RecyclerView.Adapter<AddressListAdapter.ViewHolder> {
        private List<LoginDataResponce> dataResponces;
        private Context context;
        private TextView lastdefalilt;
        private ImageView lastCheck;

        AddressListAdapter(List<LoginDataResponce> dataResponces, Context context) {
            this.dataResponces = dataResponces;
            this.context = context;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.view_address_card, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final ViewHolder h, int position) {
            final LoginDataResponce data = dataResponces.get(position);
            home = data.getShopFlatNo() + "," + data.getShopRoadName() + "," + data.getShopCity() + "," + data.getShopState() + ","
                    + data.getShopCountry() + "," + data.getShopPostalCode();
            h.customerAddress.setText(home);
            h.tag1.setText("Shop");

            h.editCustomerAddress.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            h.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent1 = new Intent(view.getContext(), EditAddressActivity.class);
                    intent1.putExtra(ADDRESSTAG, 1);
                    intent1.putExtra(ADDRESSID, data.getShopAddId());
                    Pair[] pairs1 = new Pair[3];
                    pairs1[0] = new Pair<View, String>(h.customerAddress, "addressTeansition");
                    pairs1[1] = new Pair<View, String>(h.tag1, "addressTagTransition");
//                    pairs1[2] = new Pair<View, String>(addressLabel, "labelTransormation");
//                    ActivityOptions options1 = ActivityOptions.makeSceneTransitionAnimation(getActivity(), pairs1);
                    startActivityForResult(intent1, 101);//, options1.toBundle());
                }
            });

            h.check.setVisibility(View.GONE);
        }

        @Override
        public int getItemCount() {
            return dataResponces.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            private TextView default1, customerAddress, tag1;
            private ImageView editCustomerAddress;
            private ImageView check;
            private RelativeLayout cardView;

            ViewHolder(@NonNull View itemView) {
                super(itemView);
                cardView = itemView.findViewById(R.id.selected_row);
                editCustomerAddress = itemView.findViewById(R.id.editAddress1);
                check = itemView.findViewById(R.id.check1);
                default1 = itemView.findViewById(R.id.defaulte1);
                customerAddress = itemView.findViewById(R.id.address1);
                tag1 = itemView.findViewById(R.id.tag1);
            }
        }
    }

    private void setWeeklyOffAdapter() {
        List<WeeklyOff> weeklyOffList = new ArrayList<>();
        weeklyOffList.add(new WeeklyOff('S', false));
        weeklyOffList.add(new WeeklyOff('M', false));
        weeklyOffList.add(new WeeklyOff('T', false));
        weeklyOffList.add(new WeeklyOff('W', false));
        weeklyOffList.add(new WeeklyOff('T', false));
        weeklyOffList.add(new WeeklyOff('F', false));
        weeklyOffList.add(new WeeklyOff('S', false));

        WeeklyOffAdapter adapter = new WeeklyOffAdapter(weeklyOffList ,getContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
        weeklyOffRv.setLayoutManager(layoutManager);
        weeklyOffRv.setAdapter(adapter);
    }

}
