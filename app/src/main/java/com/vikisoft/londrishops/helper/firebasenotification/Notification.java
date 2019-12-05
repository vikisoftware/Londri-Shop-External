package com.vikisoft.londrishops.helper.firebasenotification;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Notification {

@SerializedName("data")
@Expose
private Data data;
@SerializedName("to")
@Expose
private String to;

    public Notification(Data data, String to) {
        this.data = data;
        this.to = to;
    }

    public Data getData() {
return data;
}

public void setData(Data data) {
this.data = data;
}

public String getTo() {
return to;
}

public void setTo(String to) {
this.to = to;
}

}