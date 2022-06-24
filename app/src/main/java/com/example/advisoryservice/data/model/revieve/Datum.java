package com.example.advisoryservice.data.model.revieve;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Datum {


    @SerializedName("detail")
    @Expose
    private List<Detail> detail = null;
    @SerializedName("eyes")
    @Expose
    private List<Eye> eyes = null;

    public List<Detail> getDetail() {
        return detail;
    }

    public void setDetail(List<Detail> detail) {
        this.detail = detail;
    }

    public List<Eye> getEyes() {
        return eyes;
    }

    public void setEyes(List<Eye> eyes) {
        this.eyes = eyes;
    }

}
