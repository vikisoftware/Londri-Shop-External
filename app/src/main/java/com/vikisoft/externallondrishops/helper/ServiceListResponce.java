package com.vikisoft.externallondrishops.helper;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ServiceListResponce {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("serviceId")
    @Expose
    private Integer serviceId;
    @SerializedName("londriId")
    @Expose
    private Integer londriId;
    @SerializedName("serviceName")
    @Expose
    private String serviceName;
    @SerializedName("access")
    @Expose
    private String access;

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

    public Integer getLondriId() {
        return londriId;
    }

    public void setLondriId(Integer londriId) {
        this.londriId = londriId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getAccess() {
        return access;
    }

    public void setAccess(String access) {
        this.access = access;
    }
}