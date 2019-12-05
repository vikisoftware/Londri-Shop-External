package com.vikisoft.externallondrishops.helper;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrdersListResponce {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("amount")
    @Expose
    private Double amount;
    @SerializedName("count1")
    @Expose
    private Integer count1;
    @SerializedName("pickupDate")
    @Expose
    private String pickupDate;
    @SerializedName("deliveryDate")
    @Expose
    private String deliveryDate;
    @SerializedName("status1")
    @Expose
    private String status1;
    @SerializedName("clothId")
    @Expose
    private Integer clothId;
    @SerializedName("clothName")
    @Expose
    private String clothName;
    @SerializedName("paymentId")
    @Expose
    private Integer paymentId;
    @SerializedName("finalAmount")
    @Expose
    private Double finalAmount;
    @SerializedName("transactionId")
    @Expose
    private String transactionId;
    @SerializedName("paymentStatus")
    @Expose
    private String paymentStatus;
    @SerializedName("shopId")
    @Expose
    private Integer shopId;
    @SerializedName("shopName")
    @Expose
    private String shopName;
    @SerializedName("deliveryNote")
    @Expose
    private String deliveryNote;
    @SerializedName("orderDeleveredDate")
    @Expose
    private String orderDeleveredDate;
    @SerializedName("pickupNote")
    @Expose
    private String pickupNote;
    @SerializedName("pickupAddressId")
    @Expose
    private Integer pickupAddressId;
    @SerializedName("pickupAddress")
    @Expose
    private String pickupAddress;
    @SerializedName("deliveryAddressId")
    @Expose
    private Integer deliveryAddressId;
    @SerializedName("deliveryAddress")
    @Expose
    private String deliveryAddress;
    @SerializedName("serviceId")
    @Expose
    private Integer serviceId;
    @SerializedName("serviceName")
    @Expose
    private String serviceName;
    @SerializedName("wearId")
    @Expose
    private Integer wearId;
    @SerializedName("adultCount")
    @Expose
    private Integer adultCount;
    @SerializedName("adultPrice")
    @Expose
    private Double adultPrice;
    @SerializedName("kidCount")
    @Expose
    private Integer kidCount;
    @SerializedName("kidPrice")
    @Expose
    private Double kidPrice;
    @SerializedName("houseCount")
    @Expose
    private Integer houseCount;
    @SerializedName("housePrice")
    @Expose
    private Double housePrice;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("pickUp")
    @Expose
    private String pickUp;
    @SerializedName("userId")
    @Expose
    private Integer userId;
    @SerializedName("photoUrl")
    @Expose
    private String photoUrl;
    @SerializedName("boyName")
    @Expose
    private String boyName;
    @SerializedName("boyId")
    @Expose
    private Integer boyId;
    @SerializedName("latitude")
    @Expose
    private double latitude;
    @SerializedName("longitude")
    @Expose
    private double longitude;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Integer getCount1() {
        return count1;
    }

    public void setCount1(Integer count1) {
        this.count1 = count1;
    }

    public String getPickupDate() {
        return pickupDate;
    }

    public void setPickupDate(String pickupDate) {
        this.pickupDate = pickupDate;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getStatus1() {
        return status1;
    }

    public void setStatus1(String status1) {
        this.status1 = status1;
    }

    public Integer getClothId() {
        return clothId;
    }

    public void setClothId(Integer clothId) {
        this.clothId = clothId;
    }

    public String getClothName() {
        return clothName;
    }

    public void setClothName(String clothName) {
        this.clothName = clothName;
    }

    public Integer getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Integer paymentId) {
        this.paymentId = paymentId;
    }

    public Double getFinalAmount() {
        return finalAmount;
    }

    public void setFinalAmount(Double finalAmount) {
        this.finalAmount = finalAmount;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public Integer getShopId() {
        return shopId;
    }

    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getDeliveryNote() {
        return deliveryNote;
    }

    public void setDeliveryNote(String deliveryNote) {
        this.deliveryNote = deliveryNote;
    }

    public String getOrderDeleveredDate() {
        return orderDeleveredDate;
    }

    public void setOrderDeleveredDate(String orderDeleveredDate) {
        this.orderDeleveredDate = orderDeleveredDate;
    }

    public String getPickupNote() {
        return pickupNote;
    }

    public void setPickupNote(String pickupNote) {
        this.pickupNote = pickupNote;
    }

    public Integer getPickupAddressId() {
        return pickupAddressId;
    }

    public void setPickupAddressId(Integer pickupAddressId) {
        this.pickupAddressId = pickupAddressId;
    }

    public String getPickupAddress() {
        return pickupAddress;
    }

    public void setPickupAddress(String pickupAddress) {
        this.pickupAddress = pickupAddress;
    }

    public Integer getDeliveryAddressId() {
        return deliveryAddressId;
    }

    public void setDeliveryAddressId(Integer deliveryAddressId) {
        this.deliveryAddressId = deliveryAddressId;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public Integer getServiceId() {
        return serviceId;
    }

    public void setServiceId(Integer serviceId) {
        this.serviceId = serviceId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public Integer getWearId() {
        return wearId;
    }

    public void setWearId(Integer wearId) {
        this.wearId = wearId;
    }

    public Integer getAdultCount() {
        return adultCount;
    }

    public void setAdultCount(Integer adultCount) {
        this.adultCount = adultCount;
    }

    public Double getAdultPrice() {
        return adultPrice;
    }

    public void setAdultPrice(Double adultPrice) {
        this.adultPrice = adultPrice;
    }

    public Integer getKidCount() {
        return kidCount;
    }

    public void setKidCount(Integer kidCount) {
        this.kidCount = kidCount;
    }

    public Double getKidPrice() {
        return kidPrice;
    }

    public void setKidPrice(Double kidPrice) {
        this.kidPrice = kidPrice;
    }

    public Integer getHouseCount() {
        return houseCount;
    }

    public void setHouseCount(Integer houseCount) {
        this.houseCount = houseCount;
    }

    public Double getHousePrice() {
        return housePrice;
    }

    public void setHousePrice(Double housePrice) {
        this.housePrice = housePrice;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPickUp() {
        return pickUp;
    }

    public void setPickUp(String pickUp) {
        this.pickUp = pickUp;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getBoyName() {
        return boyName;
    }

    public void setBoyName(String boyName) {
        this.boyName = boyName;
    }

    public Integer getBoyId() {
        return boyId;
    }

    public void setBoyId(Integer boyId) {
        this.boyId = boyId;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}