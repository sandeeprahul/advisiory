package com.example.advisoryservice.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CustomerResultData {
    @SerializedName("locationCode")
    @Expose
    private String locationCode;
    @SerializedName("customerTransId")
    @Expose
    private String customerTransId;
    @SerializedName("transDate")
    @Expose
    private String transDate;
    @SerializedName("customerName")
    @Expose
    private String customerName;

    public String getLocationCode() {
        return locationCode;
    }

    public void setLocationCode(String locationCode) {
        this.locationCode = locationCode;
    }

    public String getCustomerTransId() {
        return customerTransId;
    }

    public void setCustomerTransId(String customerTransId) {
        this.customerTransId = customerTransId;
    }

    public String getTransDate() {
        return transDate;
    }

    public void setTransDate(String transDate) {
        this.transDate = transDate;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getResultStatus() {
        return resultStatus;
    }

    public void setResultStatus(String resultStatus) {
        this.resultStatus = resultStatus;
    }

    @SerializedName("resultStatus")
    @Expose
    private String resultStatus;

}
