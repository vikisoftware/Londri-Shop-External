package com.vikisoft.londrishops.helper.firebasenotification;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

@SerializedName("title")
@Expose
private String title;
@SerializedName("body")
@Expose
private String body;
@SerializedName("img")
@Expose
private String img;

    public Data(String title, String body) {
        this.title = title;
        this.body = body;
    }

    public String getTitle() {
return title;
}

public void setTitle(String title) {
this.title = title;
}

public String getBody() {
return body;
}

public void setBody(String body) {
this.body = body;
}

public String getImg() {
return img;
}

public void setImg(String img) {
this.img = img;
}

}