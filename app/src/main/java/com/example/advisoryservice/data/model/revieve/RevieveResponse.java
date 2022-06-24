package com.example.advisoryservice.data.model.revieve;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RevieveResponse {

    @SerializedName("message")
    @Expose
    private String message;

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }

    @SerializedName("data")
    @Expose
    private List<Datum> data = null;
   /* @SerializedName("detail")
    @Expose
    private List<Detail> detail = null;
    @SerializedName("eyes")
    @Expose
    private List<Eye> eyes = null;*/

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

   /* public List<Detail> getDetail() {
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
    }*/

}
