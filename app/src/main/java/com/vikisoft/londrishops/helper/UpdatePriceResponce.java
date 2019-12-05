package com.vikisoft.londrishops.helper;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UpdatePriceResponce {

@SerializedName("id")
@Expose
private Integer id;
@SerializedName("wearId")
@Expose
private Integer wearId;
@SerializedName("paymentId")
@Expose
private Integer paymentId;
@SerializedName("clothCount")
@Expose
private Integer clothCount;
@SerializedName("mainPrice")
@Expose
private String mainPrice;
@SerializedName("clothName")
@Expose
private String clothName;
@SerializedName("clothImage")
@Expose
private String clothImage;
@SerializedName("wearType")
@Expose
private String wearType;
@SerializedName("appliedPrice")
@Expose
private Double appliedPrice;

public Integer getId() {
return id;
}

public void setId(Integer id) {
this.id = id;
}

public Integer getWearId() {
return wearId;
}

public void setWearId(Integer wearId) {
this.wearId = wearId;
}

public Integer getPaymentId() {
return paymentId;
}

public void setPaymentId(Integer paymentId) {
this.paymentId = paymentId;
}

public Integer getClothCount() {
return clothCount;
}

public void setClothCount(Integer clothCount) {
this.clothCount = clothCount;
}

public String getMainPrice() {
return mainPrice;
}

public void setMainPrice(String mainPrice) {
this.mainPrice = mainPrice;
}

public String getClothName() {
return clothName;
}

public void setClothName(String clothName) {
this.clothName = clothName;
}

public String getClothImage() {
return clothImage;
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

public Double getAppliedPrice() {
return appliedPrice;
}

public void setAppliedPrice(Double appliedPrice) {
this.appliedPrice = appliedPrice;
}

}