package com.vikisoft.externallondrishops.api;

import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import static com.vikisoft.externallondrishops.utils.AppConstants.BASE_URL;


public class SMSCall {
    public static Retrofit retrofit=null;
    public static Retrofit getRetrofit(){
        if (retrofit==null){
            retrofit=new Retrofit.Builder()

                    .baseUrl(BASE_URL)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
