package com.example.advisoryservice.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;



public class CompleteResultDetailsResponde {


    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private List<CustomerResultData> data = null;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<CustomerResultData> getData() {
        return data;
    }

    public void setData(List<CustomerResultData> data) {
        this.data = data;
    }
}
