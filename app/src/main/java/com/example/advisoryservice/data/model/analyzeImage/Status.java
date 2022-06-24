package com.example.advisoryservice.data.model.analyzeImage;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Status {

    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("idx")
    @Expose
    private int idx;
    @SerializedName("isError")
    @Expose
    private int isError;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getIdx() {
        return idx;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }

    public int getIsError() {
        return isError;
    }

    public void setIsError(int isError) {
        this.isError = isError;
    }

}
