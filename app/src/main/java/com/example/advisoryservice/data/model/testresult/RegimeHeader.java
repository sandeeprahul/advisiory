package com.example.advisoryservice.data.model.testresult;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RegimeHeader {

    @SerializedName("regimeName")
    @Expose
    private String regimeName;
    @SerializedName("regimeImageUrl")
    @Expose
    private String regimeImageUrl;

    public String getRegimeName() {
        return regimeName;
    }

    public void setRegimeName(String regimeName) {
        this.regimeName = regimeName;
    }

    public String getRegimeImageUrl() {
        return regimeImageUrl;
    }

    public void setRegimeImageUrl(String regimeImageUrl) {
        this.regimeImageUrl = regimeImageUrl;
    }

}
