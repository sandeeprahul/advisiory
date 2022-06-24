package com.example.advisoryservice.data.model.questions;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SubQuestionOption {

    @SerializedName("subOptionId")
    @Expose
    private String subOptionId;
    @SerializedName("subOptionsName")
    @Expose
    private String subOptionsName;
    @SerializedName("subOptionsValue")
    @Expose
    private String subOptionsValue;

    @SerializedName("remarks")
    @Expose
    private String remarks;

    public String getSubOptionId() {
        return subOptionId;
    }

    public void setSubOptionId(String subOptionId) {
        this.subOptionId = subOptionId;
    }

    public String getSubOptionsName() {
        return subOptionsName;
    }

    public void setSubOptionsName(String subOptionsName) {
        this.subOptionsName = subOptionsName;
    }

    public String getSubOptionsValue() {
        return subOptionsValue;
    }

    public void setSubOptionsValue(String subOptionsValue) {
        this.subOptionsValue = subOptionsValue;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
