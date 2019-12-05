package com.vikisoft.londrishops.helper;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TransactionResponce {

@SerializedName("id")
@Expose
private Integer id;
@SerializedName("paymentAmount")
@Expose
private Double paymentAmount;
@SerializedName("paymentDate")
@Expose
private String paymentDate;
@SerializedName("paymentMode")
@Expose
private String paymentMode;
@SerializedName("paymentStatus")
@Expose
private String paymentStatus;
@SerializedName("userId")
@Expose
private Integer userId;
@SerializedName("userName")
@Expose
private String userName;
@SerializedName("onlinePaymentId")
@Expose
private String onlinePaymentId;

public Integer getId() {
return id;
}

public void setId(Integer id) {
this.id = id;
}

public Double getPaymentAmount() {
return paymentAmount;
}

public void setPaymentAmount(Double paymentAmount) {
this.paymentAmount = paymentAmount;
}

public String getPaymentDate() {
return paymentDate;
}

public void setPaymentDate(String paymentDate) {
this.paymentDate = paymentDate;
}

public String getPaymentMode() {
return paymentMode;
}

public void setPaymentMode(String paymentMode) {
this.paymentMode = paymentMode;
}

public String getPaymentStatus() {
return paymentStatus;
}

public void setPaymentStatus(String paymentStatus) {
this.paymentStatus = paymentStatus;
}

public Integer getUserId() {
return userId;
}

public void setUserId(Integer userId) {
this.userId = userId;
}

public String getUserName() {
return userName;
}

public void setUserName(String userName) {
this.userName = userName;
}

public String getOnlinePaymentId() {
return onlinePaymentId;
}

public void setOnlinePaymentId(String onlinePaymentId) {
this.onlinePaymentId = onlinePaymentId;
}

}