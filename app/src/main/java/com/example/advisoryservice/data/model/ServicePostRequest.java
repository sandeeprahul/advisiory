package com.example.advisoryservice.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ServicePostRequest {

    @SerializedName("advisoryService")
    @Expose
    private String advisoryService;
    @SerializedName("custTransId")
    @Expose
    private String custTransId;
    @SerializedName("locationCode")
    @Expose
    private String locationCode;
    @SerializedName("gender")
    @Expose
    private String gender;

    public String getAdvisoryService() {
        return advisoryService;
    }

    public void setAdvisoryService(String advisoryService) {
        this.advisoryService = advisoryService;
    }

    public String getCustTransId() {
        return custTransId;
    }

    public void setCustTransId(String custTransId) {
        this.custTransId = custTransId;
    }

    public String getLocationCode() {
        return locationCode;
    }

    public void setLocationCode(String locationCode) {
        this.locationCode = locationCode;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
