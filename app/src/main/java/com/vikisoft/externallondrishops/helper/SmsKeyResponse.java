package com.vikisoft.externallondrishops.helper;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SmsKeyResponse {



        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("key1")
        @Expose
        private String key1;

        public Integer getId() {
            return id;
        }
        public void setId(Integer id) {
            this.id = id;
        }
        public String getKey1() {
            return key1;
        }
        public void setKey1(String key1) {
            this.key1 = key1;
        }


}