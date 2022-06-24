package com.example.advisoryservice.data.model.revieveDetail;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResultResponse {

    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("value")
    @Expose
    private String value;
    @SerializedName("eyes")
    @Expose
    private String eyes;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getEyes() {
        return eyes;
    }

    public void setEyes(String eyes) {
        this.eyes = eyes;
    }

}
