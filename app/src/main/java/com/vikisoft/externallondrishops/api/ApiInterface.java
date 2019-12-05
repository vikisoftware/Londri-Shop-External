package com.vikisoft.externallondrishops.api;


import com.vikisoft.externallondrishops.helper.LoginDataResponce;
import com.vikisoft.externallondrishops.helper.OrdersListResponce;
import com.vikisoft.externallondrishops.helper.ServiceListResponce;
import com.vikisoft.externallondrishops.helper.ServicePriceResponce;
import com.vikisoft.externallondrishops.helper.SmsKeyResponse;
import com.vikisoft.externallondrishops.helper.TransactionResponce;
import com.vikisoft.externallondrishops.helper.UpdatePriceResponce;
import com.vikisoft.externallondrishops.helper.firebasenotification.Notification;
import com.vikisoft.externallondrishops.helper.firebasenotification.NotificationResponce;
import com.vikisoft.externallondrishops.helper.pincode.PostalCodeResponce;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface ApiInterface {
    @Headers({"Authorization:key=AIzaSyAH3_qon2C6hzoZID__FJP7wQXsHoKTGY8"})
    @POST("/fcm/send")
    Call<NotificationResponce> sendNotification(@Body Notification notification);
    @FormUrlEncoded
    @POST
    Call<String> sendOTPSMS(@Url String url, @Field("country") String country, @Field("sender") String sender
            , @Field("route") String route, @Field("mobiles") String mobiles, @Field("authkey") String authkey
            , @Field("message") String message);

    @FormUrlEncoded
    @POST("androidRunnerOrderDetails")
    Call<List<OrdersListResponce>> refreshOrder(@Field("pid") String paymentId);

    @FormUrlEncoded
    @POST("androidLondriShopLogin")
    Call<LoginDataResponce> getLogin(@Field("mobno") String mobileNo);

    @FormUrlEncoded
    @POST("androidSaveShopProfile")
    Call<LoginDataResponce> saveProfile(@Field("data") String profileData);

    @FormUrlEncoded
    @POST("androidProfilePhotoUpload")
    Call<String> uploadImage(@Field("data") String base64Image);

    @GET
    Call<PostalCodeResponce> getRegion(@Url String url);
    @FormUrlEncoded
    @POST("androidShopOrders")
    Call<List<OrdersListResponce>> getOrders(@Field("sid") String shopId);

    @FormUrlEncoded
    @POST("androidShopTransaction")
    Call<List<TransactionResponce>> getTransaction(@Field("sid") String shopId);

    @GET
    Call<String> getifecDetails(@Url String url);
    @FormUrlEncoded
    @POST("androidShopService")
    Call<List<ServiceListResponce>> getServiceList(@Field("sid") String shopId);

    @FormUrlEncoded
    @POST("androidAddService")
    Call<String> addService(@Field("service") String toJson);

    @FormUrlEncoded
    @POST("androidServicePriceDate")
    Call<List<ServicePriceResponce>> getServicePrice(@Field("service") String data,@Field("wid") String s);

    @FormUrlEncoded
    @POST("androidSavePrising")
    Call<String> addPricing(@Field("prisinglist") String toJson);

    @FormUrlEncoded
    @POST("accessKey")
    Call<SmsKeyResponse> getSmsKey(@Field("key") String keyType);

    @FormUrlEncoded
    @POST("androidAppVersionCheck")
    Call<String> getVersion(@Field("appName") String appName);

    @FormUrlEncoded
    @POST("androidRunnerChangeStatus")
    Call<String> changeStatus(@Field("data") String toJson);

    @FormUrlEncoded
    @POST("androidOrderDetailsData")
    Call<List<UpdatePriceResponce>> getClothDetails(@Field("pid") Integer id);

    @FormUrlEncoded
    @POST("updateOrderPrice")
    Call<String> updateNewPricing(@Field("data") String toJson);

}
