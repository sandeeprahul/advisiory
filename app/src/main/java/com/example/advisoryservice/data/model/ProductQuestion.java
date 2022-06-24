package com.example.advisoryservice.data.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class ProductQuestion {

    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("Default_image")
    @Expose
    private String defaultImage;
    @SerializedName("data")
    @Expose
    private List<com.example.advisoryservice.data.model.questions.Datum> data = null;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDefaultImage() {
        return defaultImage;
    }

    public void setDefaultImage(String defaultImage) {
        this.defaultImage = defaultImage;
    }

    public List<com.example.advisoryservice.data.model.questions.Datum> getData() {
        return data;
    }

    public void setData(List<com.example.advisoryservice.data.model.questions.Datum> data) {
        this.data = data;
    }

}