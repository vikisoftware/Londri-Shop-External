package com.vikisoft.londrishops.helper;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ServicePriceResponce {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("serviceId")
    @Expose
    private Integer serviceId;
    @SerializedName("shopId")
    @Expose
    private Integer shopId;
    @SerializedName("wearId")
    @Expose
    private Integer wearId;
    @SerializedName("clothId")
    @Expose
    private Integer clothId;
    @SerializedName("clothName")
    @Expose
    private String clothName;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("image")
    @Expose
    private String image;
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public Integer getServiceId() {
        return serviceId;
    }
    public void setServiceId(Integer serviceId) {
        this.serviceId = serviceId;
    }

    public Integer getShopId() {
        return shopId;
    }

    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }

    public Integer getWearId() {
        return wearId;
    }

    public void setWearId(Integer wearId) {
        this.wearId = wearId;
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
