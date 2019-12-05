package com.vikisoft.externallondrishops.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;


import androidx.fragment.app.FragmentActivity;

import com.google.gson.Gson;
import com.vikisoft.externallondrishops.activity.NewLoginActivity;
import com.vikisoft.externallondrishops.helper.LoginDataResponce;
import com.vikisoft.externallondrishops.helper.OrdersListResponce;
import com.vikisoft.externallondrishops.helper.ServiceListResponce;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class SessionManager {
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    Context context;
    int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "LondriShopePref";
    private static final String IS_LOGIN = "IsLoggedIn";
    public static final String MOBILE_NO = "mobile_no";
    public static final String USER_NAME = "user_name";
    public static final String USER_ID = "user_id";
    public static final String IMAGE_LINK = "imagr_link";
    public static final String PROFILE_DATA = "profile_json";
    public static final String DEFAULT_ADDRESS = "default_address";
    public static final String CURRENT_ORDER = "current_order";
    public static final String LONDRILIST = "londri_list";
    public static final String ACCESS_COUNT = "acc_count";
    public static final String LONDRI_ID = "lindri_Id";
    public static final String PRICE_DETAILS = "price";
    public static final String SERVICE_DETAILS = "serv";
    public static final String CURRENT_THEME = "current theme";
    public static final String LIGHT_THEME = "light theme";
    public static final String DARK_THEME = "dark theme";

    public SessionManager(Context context) {
        this.context = context.getApplicationContext();
        preferences = this.context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = preferences.edit();
    }

    public void createLogin(String mobileNo, String userName, String userId, String imageUrl) {
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(MOBILE_NO, mobileNo);
        editor.putString(USER_NAME, userName);
        editor.putString(USER_ID, userId);
        editor.putString(IMAGE_LINK, imageUrl);
        editor.commit();
    }

    public HashMap<String, String> getUserdetail() {
        HashMap<String, String> map = new HashMap<>();
        map.put(MOBILE_NO, preferences.getString(MOBILE_NO, null));
        map.put(USER_NAME, preferences.getString(USER_NAME, null));
        map.put(USER_ID, preferences.getString(USER_ID, null));
        map.put(IMAGE_LINK, preferences.getString(IMAGE_LINK, null));
        return map;
    }

    public boolean isLogin() {
        return preferences.getBoolean(IS_LOGIN, false);
    }

    public void logout(FragmentActivity activity) {
        editor.clear();
        editor.commit();
        Intent i = new Intent(context, NewLoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
        activity.finish();
    }

    public void clearSession() {
        editor.clear();
        editor.commit();

    }

    public void saveProfileJson(String profileJson) {
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(PROFILE_DATA, profileJson);
        editor.commit();
    }

    public LoginDataResponce getProfileJson() {
        Gson gson = new Gson();
        if (preferences.contains(PROFILE_DATA)) {
            String jsonFavorites = preferences.getString(PROFILE_DATA, null);
            return  gson.fromJson(jsonFavorites,LoginDataResponce.class);
        }


        //LoginDataResponce list =new LoginDataResponce();
        /*List<LoginDataResponce> list = new ArrayList<>();*/
        //list=new Gson().fromJson( pref.getString(USER_LIST, null),List<LoginResponce>);
        /*if (preferences.contains(PROFILE_DATA)) {

            if (jsonFavorites != null) {
                Gson gson = new Gson();
                LoginDataResponce[] favoriteItems = gson.fromJson(jsonFavorites,
                        LoginDataResponce[].class);


                *//*list = (list);*//*
                list = Arrays.asList(favoriteItems);
                return new ArrayList<>(list);
            } else
                return null;
        }*/ else
            return null;
    }

    public void setAddress(int address) {
        editor.putInt(DEFAULT_ADDRESS, address);
        editor.commit();
    }


    public void setCurrentThemesetIntialTheme() {
        setTheme(LIGHT_THEME);
    }

    public void setTheme(String theme) {
        editor.putString(CURRENT_THEME, theme);
        editor.commit();
    }

    public String getCurrentTheme() {
        return preferences.getString(CURRENT_THEME, LIGHT_THEME);
    }

    public int getAddress() {
        return preferences.getInt(DEFAULT_ADDRESS, 0);
    }

    public void saveOrder(String order) {
        editor.putString(CURRENT_ORDER, order);
        editor.commit();
    }

    public String getOrder() {
        return preferences.getString(CURRENT_ORDER, null);
    }

    public void saveLondriList(String toJson) {
        editor.putString(LONDRILIST, toJson);
        editor.commit();

    }

    public void removeOrder() {
        editor.remove(CURRENT_ORDER);
        editor.commit();
    }

    public void saveOrderList(String toJson) {
        editor.putString(CURRENT_ORDER, toJson);
        editor.commit();
    }

    public List<OrdersListResponce> getOrderList() {
        List<OrdersListResponce> list = new ArrayList<>();

        if (preferences.contains(CURRENT_ORDER)) {
            String jsonFavorites = preferences.getString(CURRENT_ORDER, null);
            if (jsonFavorites != null) {
                if (jsonFavorites.equals("null"))
                    return null;
                else {
                    Gson gson = new Gson();
                    OrdersListResponce[] favoriteItems = gson.fromJson(jsonFavorites,
                            OrdersListResponce[].class);


                    /*list = (list);*/
                    list = Arrays.asList(favoriteItems);
                    return new ArrayList<>(list);
                }
            } else
                return null;
        } else
            return null;
    }

    public int getInt() {
        return preferences.getInt(ACCESS_COUNT, 0);
    }

    public void saveInt(int i) {
        editor.putInt(ACCESS_COUNT, i);
        editor.commit();
    }

    public int getLondriId() {
        return preferences.getInt(LONDRI_ID, 0);
    }

    public void saveLindriId(Integer id) {
        editor.putInt(LONDRI_ID, id);
        editor.commit();
    }

    public void setPriceCount(int value) {
        editor.putInt(PRICE_DETAILS, value);
        editor.commit();
    }

    public int getPriceCount() {
        return preferences.getInt(PRICE_DETAILS, 0);
    }

    public void setService(String toJson) {
        editor.putString(SERVICE_DETAILS,toJson);
        editor.commit();
    }
    public List<ServiceListResponce> getService(){
        List<ServiceListResponce> list = new ArrayList<>();

        if (preferences.contains(SERVICE_DETAILS)) {
            String jsonFavorites = preferences.getString(SERVICE_DETAILS, null);
            if (jsonFavorites != null) {
                if (jsonFavorites.equals("null"))
                    return null;
                else {
                    Gson gson = new Gson();
                    ServiceListResponce[] favoriteItems = gson.fromJson(jsonFavorites,
                            ServiceListResponce[].class);


                    /*list = (list);*/
                    list = Arrays.asList(favoriteItems);
                    return new ArrayList<>(list);
                }
            } else
                return null;
        } else
            return null;
    }
}
