package com.vikisoft.externallondrishops.helper;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ClothCountResponse {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("clothId")
    @Expose
    private Integer clothId;
    @SerializedName("priceSinglePeice")
    @Expose
    private Double priceSinglePeice;
    @SerializedName("count")
    @Expose
    private Integer count;
    @SerializedName("serviceId")
    @Expose
    private Integer serviceId;
    @SerializedName("clothName")
    @Expose
    private String clothName;
    @SerializedName("clothImage")
    @Expose
    private String clothImage;
    @SerializedName("wearType")
    @Expose
    private String wearType;
    @SerializedName("wearId")
    @Expose
    private int wearId;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getClothId() {
        return clothId;
    }

    public void setClothId(Integer clothId) {
        this.clothId = clothId;
    }

    public Double getPriceSinglePeice() {
        return priceSinglePeice;
    }

    public void setPriceSinglePeice(Double priceSinglePeice) {
        this.priceSinglePeice = priceSinglePeice;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getServiceId() {
        return serviceId;
    }

    public void setServiceId(Integer serviceId) {
        this.serviceId = serviceId;
    }

    public String getClothName() {
        return clothName;
    }

    public String getClothImage() {
        return clothImage;
    }

    public void setClothName(String clothName) {
        this.clothName = clothName;
    }

    public void setClothImage(String clothImage) {
        this.clothImage = clothImage;
    }

    public String getWearType() {
        return wearType;
    }

    public void setWearType(String wearType) {
        this.wearType = wearType;
    }

    public int getWearId() {
        return wearId;
    }

    public void setWearId(int wearId) {
        this.wearId = wearId;
    }
}