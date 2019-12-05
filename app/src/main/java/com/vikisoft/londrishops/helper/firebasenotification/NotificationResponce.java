package com.vikisoft.londrishops.helper.firebasenotification;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NotificationResponce {

@SerializedName("message_id")
@Expose
private String messageId;

public String getMessageId() {
return messageId;
}

public void setMessageId(String messageId) {
this.messageId = messageId;
}

}