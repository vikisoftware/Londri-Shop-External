package com.vikisoft.externallondrishops.helper;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.View;
import android.widget.Toast;


public class CommonFunctions {


    private ProgressDialog progressDialog;

    public void showProgressBar(Context context, String message) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(message);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    public void hideProgressBar() {
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    public static View getElement(Activity activity, int objectid){
        return activity.findViewById(objectid);
    }


    Context context;

    public CommonFunctions(Context context) {
        this.context = context;
    }
    /*public String GetCountryZipCode(){
        String CountryID="";
        String CountryZipCode="";

        TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        //getNetworkCountryIso
        CountryID= manager.getSimCountryIso().toUpperCase();
        String[] rl=context.getResources().getStringArray(R.array.CountryCodes);
        for(int i=0;i<rl.length;i++){
            String[] g=rl[i].split(",");
            if(g[1].trim().equals(CountryID.trim())){
                CountryZipCode=g[0];
                break;
            }
        }
        return CountryZipCode;
    }*/
    public void ShowToast(String message){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
